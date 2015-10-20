package com.lezyo.fsm.bean.teacher;

import java.io.Serializable;
import java.util.ArrayList;

import com.lezyo.fsm.activity.base.BaseEntity;

public class LoginResponse extends BaseEntity implements Serializable{
	
	private String PersonId;
	
	private String ImKey;
	
	private String Stype;


	public String getPersonId() {
		return PersonId;
	}

	public void setPersonId(String personId) {
		PersonId = personId;
	}

	public String getStype() {
		return Stype;
	}

	public void setStype(String stype) {
		Stype = stype;
	}

	public String getImKey() {
		return ImKey;
	}

	public void setImKey(String imKey) {
		ImKey = imKey;
	}

	@Override
	public String toString() {
		return "LoginResponse [RspCode=" + getRspCode() + ", RspInfo=" + getRspInfo()
				+ ", PersonId=" + PersonId + ", ImKey=" + ImKey + ", Stype="
				+ Stype +  "]";
	}

	
	
	
}
