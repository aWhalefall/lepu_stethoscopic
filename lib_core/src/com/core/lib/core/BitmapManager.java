package com.core.lib.core;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.core.lib.utils.main.ImageUtilBase;
import com.core.lib.utils.main.StringUtilBase;

public class BitmapManager {

	private static ExecutorService mPool;
	private static Map<ImageView, String> mImageViews;
	private static BitmapManager mInstance = null;

	static {
		mPool = Executors.newFixedThreadPool(5); // 固定线程池
		mImageViews = Collections
				.synchronizedMap(new WeakHashMap<ImageView, String>());
	}

	private BitmapManager() {

	}

	public static BitmapManager getInstance() {
		if (mInstance == null) {
			mInstance = new BitmapManager();
		}

		return mInstance;
	}

	/*
	 * imageview 有三种显示类型。
	 * 外面需要设置好，默认显示图片。（外边layout上，设置好默认图片）
	 * 网络加载成功后，显示网络图片。（成功后，会自动设置，网络图片）
	 * 网络加载失败后，显示异常图片。（需要传入参数	loadFailBmp）（需要设置，不需要，传null）
	 */
	/**
	 * 加载图片
	 * 
	 * 不需要网络加载后的失败图片
	 * 
	 * saveFilePath：
	 * 需要缓存到本地，传入正确本地地址
	 * 不需要缓存到本地，传null或者""
	 * 
	 * memoryCache
	 * true:放到内存缓存里，
	 * false：不放到内存缓存里
	 */
	public void loadBitmap(String url, String saveFilePath,
			ImageView imageView, boolean memoryCache) {
		loadBitmap(url, saveFilePath, imageView, null, 0, 0, memoryCache);
	}

	/**
	 * 加载图片-
	 * 
	 * 可设置加载失败后显示的默认图片
	 * 
	 */
	public void loadBitmap(String url, String saveFilePath,
			ImageView imageView, Bitmap loadFailBmp, boolean memoryCache) {
		loadBitmap(url, saveFilePath, imageView, loadFailBmp, 0, 0, memoryCache);
	}

	/**
	 * 加载图片-可指定显示图片的高宽
	 * 
	 * saveFilePath 有值：保存图片到该路径下 无值：不缓存图片
	 */
	public void loadBitmap(String url, String saveFilePath,
			ImageView imageView, Bitmap loadFailBmp, int width, int height,
			boolean memoryCache) {
		mImageViews.put(imageView, url);

		if (memoryCache) {
			Bitmap bitmap = getBitmapFromCache(url);

			if (bitmap != null) {
				// 显示缓存图片
				imageView.setImageBitmap(bitmap);
			} else {
				// 加载SD卡中的图片缓存
				File file = new File(saveFilePath);
				if (file.exists()) {
					// 显示SD卡中的图片缓存
					Bitmap bmp = ImageUtilBase.getBitmap(saveFilePath);
					ImageLoader.getInstance().addBitmapToMemoryCache(url, bmp);
					imageView.setImageBitmap(bmp);
				} else {
					// 线程加载网络图片
					queueJob(url, saveFilePath, imageView, loadFailBmp, width,
							height, memoryCache);
				}
			}
		} else {
			queueJob(url, saveFilePath, imageView, loadFailBmp, width, height,
					memoryCache);
		}
	}

	/**
	 * 从缓存中获取图片
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = ImageLoader.getInstance().getBitmapFromMemoryCache(url);
		return bitmap;
	}

	public static void clearBitmapMemoryCache() {
		ImageLoader.getInstance().clearMemoryCache();
	}

	/**
	 * 从网络中加载图片
	 */
	@SuppressLint("HandlerLeak")
	public void queueJob(final String url, final String saveFilePath,
			final ImageView imageView, final Bitmap loadFailBmp,
			final int width, final int height, final boolean memoryCache) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					if (bitmap != null) {
						if(width > 0 && height > 0)
						{
							// 指定显示图片的高宽
							bitmap = ImageUtilBase.zoomBitmap(bitmap, width, height);
						}
						// 放入缓存
						if (memoryCache) {
							ImageLoader.getInstance().addBitmapToMemoryCache(
									url, bitmap);
						}
						imageView.setImageBitmap(bitmap);
					}else{
						//网络下载图片失败，显示的图片
						imageView.setImageBitmap(loadFailBmp);
					}
				} else {
					//网络下载图片失败，显示的图片
					imageView.setImageBitmap(loadFailBmp);
				}
			}
		};

		mPool.execute(new Runnable() {
			public void run() {
				Message message = Message.obtain();
				message.obj = downloadBitmap(url, saveFilePath);
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 下载图片-可指定显示图片的高宽
	 */
	private Bitmap downloadBitmap(String url, String saveFilePath) {
		Bitmap bitmap = null;
		try {
			// http加载图片
			bitmap = ApiClient.getNetBitmap(url);

			// 往本地保存图片
			if (!StringUtilBase.stringIsEmpty(saveFilePath) && bitmap != null) {
				try {
					// 向SD卡中写入图片缓存
					ImageUtilBase.saveImageToSd(bitmap, saveFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}