package com.core.lib.utils.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.ListView;

public class ImageUtilBase {

	public static final int IMAGE_QUALITY_SMALL = 50;
	public static final int IMAGE_QUALITY_MEDIUM = 75;// 一般使用这个,适中
	public static final int IMAGE_QUALITY_BIG = 100;

	public static Bitmap convertToBitmap(String filePath, int w, int h) {

		Bitmap bitmap = getBitmap(filePath);
		if (bitmap != null) {
			bitmap = zoomBitmap(bitmap, w, h);
		}

		return bitmap;
	}

	public static Bitmap getBitmap(String filePath, int newWidth, int newHeight) {
		Bitmap bitmap = null;

		File file = new File(filePath);
		if (file.exists()) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, newWidth
					* newHeight);
			opts.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeFile(filePath, opts);
			} catch (OutOfMemoryError err) {
				err.printStackTrace();
			}
		}

		return bitmap;
	}

	public static Bitmap getBitmap(String filePath) {

		FileInputStream fis = null;
		Bitmap bitmap = null;

		File file = new File(filePath);
		if (file.exists()) {
			try {
				fis = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}

	public static Bitmap getBitmapSmall(String filePath) {
		FileInputStream fis = null;
		Bitmap bitmap = null;

		File file = new File(filePath);
		if (file.exists()) {
			try {
				fis = new FileInputStream(file);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;// 宽是原始图片1/2,高是原始图片1/2.整体图片是原始图片的1/4
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
				options.inPurgeable = true;
				options.inInputShareable = true;
				bitmap = BitmapFactory.decodeStream(fis, null, options);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public static Bitmap getBitmap(String filePath, Bitmap.Config bitmapConfig) {

		Bitmap bitmap = null;
		File file = new File(filePath);
		if (file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = bitmapConfig;
				options.inPurgeable = true;
				options.inInputShareable = true;
				bitmap = BitmapFactory.decodeStream(inputStream, null, options);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/*
	 * 从流中，取原始图片
	 */
	public static Bitmap getBitmap(InputStream inputStream) {

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/*
	 * 从流中，图片大小变为原来的1/4，图片色差为 ARGB_4444
	 */
	public static Bitmap getBitmapSmall(InputStream inputStream) {

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;// 宽是原始图片1/2,高是原始图片1/2.整体图片是原始图片的1/4
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			options.inPurgeable = true;
			options.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/*
	 * 从流中读取图片，可以设置 bitmapConfig
	 */
	public static Bitmap getBitmap(InputStream inputStream,
			Bitmap.Config bitmapConfig) {

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = bitmapConfig;
			options.inPurgeable = true;
			options.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {

		}
		return bitmap;
	}

	// ===========================================================================================
	/**
	 * write file /data/data/PACKAGE_NAME/files 目录下
	 * 
	 */
	@SuppressWarnings("unused")
	private static void saveImageToFile(Context context, String fileName,
			Bitmap bitmap) {
		saveImageToFile(context, fileName, bitmap, IMAGE_QUALITY_BIG);
	}

	private static void saveImageToFile(Context context, String fileName,
			Bitmap bitmap, int quality) {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveImageToSd(Bitmap bitmap, String filePath) {
		saveImageToSd(bitmap, filePath, IMAGE_QUALITY_BIG);
	}

	/*
	 * write file to SD
	 */
	public static void saveImageToSd(Bitmap bitmap, String filePath, int quality) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.flush();
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// =============================================================================================
	/*
	 * 给图片添加水印 水印图片和水印文字
	 */
	public static Bitmap createPrintBitmap(Bitmap src, Bitmap waterMark,
			String title) {

		if (src == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();

		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();

		// 加入图片
		if (waterMark != null) {
			int ww = waterMark.getWidth();
			int hh = waterMark.getHeight();
			paint.setAlpha(50);
			cv.drawBitmap(waterMark, (w - ww) / 2, (h - hh) / 2, paint);// 屏幕的中间画图
		}

		// 加入文字
		if (title != null) {
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.BOLD);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(Color.GRAY);
			textPaint.setTypeface(font);
			textPaint.setTextSize(50);
			// 这里是自动换行的
			StaticLayout layout = new StaticLayout(title, textPaint, w,
					Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			layout.draw(cv);
			// cv.drawText(title, w / 2, h / 2, textPaint);
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}

	/*
	 * 获取activity的截图
	 */
	public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		// int height = activity.getWindowManager().getDefaultDisplay()
		// .getHeight();
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width,
				b1.getHeight() - statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	/*
	 * 获取view的截图
	 */
	public static Bitmap getViewBitmap(View v) {

		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_4444);
		v.draw(new Canvas(bitmap));

		return bitmap;
	}

	/*
	 * 获取ListView的截图
	 */
	public static Bitmap getListViewBitmap(ListView listView) {

		int h = 0;
		for (int i = 0; i < listView.getChildCount(); i++) {
			h += listView.getChildAt(i).getHeight();
		}
		Bitmap bitmap = Bitmap.createBitmap(listView.getWidth(), h,
				Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		listView.draw(canvas);

		return bitmap;
	}

	/*
	 * 合并成一张图片
	 */
	public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
		Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(),
				firstBitmap.getHeight(), firstBitmap.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(firstBitmap, new Matrix(), null);
		canvas.drawBitmap(secondBitmap, 0, 0, null);
		return bitmap;
	}

	public static Bitmap getBitmapByResId(Context context, int resId) {
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}

	public static Bitmap getBitmapByByte(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	/**
	 * 获得圆角图片的方法
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            一般设成14
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得带倒影的图片方法
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_4444);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 获取图片类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getImageType(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String type = getImageType(in);
			return type;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 获取图片的类型信息
	 * 
	 * @param in
	 * @return
	 * @see #getImageType(byte[])
	 */
	public static String getImageType(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);
			return getImageType(bytes);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获取图片的类型信息
	 * 
	 * @param bytes
	 *            2~8 byte at beginning of the image file
	 * @return image mimetype or null if the file is not image
	 */
	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return "image/jpeg";
		}
		if (isGIF(bytes)) {
			return "image/gif";
		}
		if (isPNG(bytes)) {
			return "image/png";
		}
		if (isBMP(bytes)) {
			return "application/x-bmp";
		}
		return null;
	}

	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
				&& (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
				&& b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
				&& b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 0x42) && (b[1] == 0x4d);
	}

	/**
	 * 让Gallery上能马上看到该图片
	 */
	public static void scanPhoto(Context ctx, String imgFileName) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(imgFileName);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		ctx.sendBroadcast(mediaScanIntent);
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}
		return newbmp;
	}
}
