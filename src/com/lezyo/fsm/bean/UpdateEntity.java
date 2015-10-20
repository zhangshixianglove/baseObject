package com.lezyo.fsm.bean;

public class UpdateEntity {
	private int is_update;// ": 1, //0:无需更新；1:有更新；2:需要强制更新\
	
	private String update_desc;// ":"xxxx更新要点: <br/>1.添加xx功能<br/>2.添加yy功能。"
								// //当有新版本时，新版本的更新描述，无新版本更新时此字段可以不传。
	private String update_uri;// ":"http://www.appstore.cn/???" //更新的下载或者跳转地址
								// (Android及iOS越狱渠道下载地址；iOS非越狱渠道Appstore地址)。无新版本更新时此字段可以不传。
	private String version;// ": "1.0.1" //无版本更新时此字段为空字符串，字段结构不能不传(兼容已经上线的代码)。

	public int getIs_update() {
		return is_update;
	}

	public void setIs_update(int is_update) {
		this.is_update = is_update;
	}

	public String getUpdate_desc() {
		return update_desc;
	}

	public void setUpdate_desc(String update_desc) {
		this.update_desc = update_desc;
	}

	public String getUpdate_uri() {
		return update_uri;
	}

	public void setUpdate_uri(String update_uri) {
		this.update_uri = update_uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
