package com.bb.video.common.constant;

/**
 * Created by LiangyinKwai on 2018/12/26.
 */
public enum Router {

	/**
	 * 短信发送
	 */
	POST_SMS("/sms"),

	/**
	 * 注册
	 */
	POST_REGISTER("/register/registerBySms"),

	/**
	 * 登录
	 */
	POST_LOGIN("/login/loginBySms"),

	/**
	 * OSS上传回调
	 */
	OSS_UPLOAD_CALLBACK("/oss/upload/callback"),

	/**
	 * 高德地图逆向地理编码
	 */
	GAODE_GEOCODE("/v3/geocode/regeo"),

	/**
	 * 百度地图逆向地理编码
	 */
	BAIDU_GEOCODE("/geocoder/v2/"),

	/**
	 * 获取用户密钥
	 */
	FBI_SECRET_KEY("/fbi/secret/open/find/key/"),

	/**
	 * 校验token
	 */
	CHECK_TOKEN("/v3.2/dataSupply/viewMobileInfo"),

	;
	private String url;

	Router(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
