package cn.com.jbit.assistant.model;

/**
 * 类说明：短信信息
 */
public class SmsInfo {
	private String address;//来源
	private String date;//时间
	private String type;//类型
	private String body;//信息
	public SmsInfo() {
	}
	public SmsInfo(String address, String date, String type, String body) {
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
