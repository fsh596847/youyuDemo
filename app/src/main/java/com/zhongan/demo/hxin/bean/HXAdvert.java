package com.zhongan.demo.hxin.bean;


/**
 *  轮播图-实体类
 */

public class HXAdvert {


	//广告标题
	private String advertTitle = "";
	//广告图片
	private String advertPic = "";
	//广告链接
	private String advertUrl = "";
	//产品ID
	private String productId = "";

	public String getAdvertTitle() {
		return advertTitle;
	}

	public void setAdvertTitle(String advertTitle) {
		this.advertTitle = advertTitle;
	}

	public String getAdvertPic() {
		return advertPic;
	}

	public void setAdvertPic(String advertPic) {
		this.advertPic = advertPic;
	}

	public String getAdvertUrl() {
		return advertUrl;
	}

	public void setAdvertUrl(String advertUrl) {
		this.advertUrl = advertUrl;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
