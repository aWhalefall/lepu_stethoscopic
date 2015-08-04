package com.lepu.stethoscopic.main.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BeanCheckVersion {

	private boolean ClientUpgrade;
	private String ClientLatestVersion;
	private String ClientDownloadUrl;
	private String ClientUpgradeMemo;

	public boolean getClientUpgrade() {
		return ClientUpgrade;
	}

	public void setClientUpgrade(boolean clientUpgrade) {
		ClientUpgrade = clientUpgrade;
	}

	public String getClientLatestVersion() {
		return ClientLatestVersion;
	}

	public void setClientLatestVersion(String clientLatestVersion) {
		ClientLatestVersion = clientLatestVersion;
	}

	public String getClientDownloadUrl() {
		return ClientDownloadUrl;
	}

	public void setClientDownloadUrl(String clientDownloadUrl) {
		ClientDownloadUrl = clientDownloadUrl;
	}

	public String getClientUpgradeMemo() {
		return ClientUpgradeMemo;
	}

	public void setClientUpgradeMemo(String clientUpgradeMemo) {
		ClientUpgradeMemo = clientUpgradeMemo;
	}
	
	/*
	 * 解析数据
	 */
	public static BeanCheckVersion parseCheckVersion(String dataString)
	{
		String result = "";
		try {
			JSONObject jsonObject = new JSONObject(dataString);
			JSONObject obj1 = (JSONObject) jsonObject.get("Result");
			JSONObject obj2 = (JSONObject) obj1.get("DetailInfo");
			
			result = obj2.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		BeanCheckVersion checkVersion = gson.fromJson(result,
				new TypeToken<BeanCheckVersion>() {
				}.getType());
		return checkVersion;
	}

}
