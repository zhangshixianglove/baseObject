package com.lezyo.fsm.activity.base;
/**
 * 没一个实体类都要继承该类
 */
public class BaseEntity {
	private String RspCode;
	private String RspInfo;
	public String getRspCode() {
		return RspCode;
	}
	public void setRspCode(String rspCode) {
		RspCode = rspCode;
	}
	public String getRspInfo() {
		return RspInfo;
	}
	public void setRspInfo(String rspInfo) {
		RspInfo = rspInfo;
	}
	@Override
	public String toString() {
		return "BaseEntity [RspCode=" + RspCode + ", RspInfo=" + RspInfo + "]";
	}
	
}
