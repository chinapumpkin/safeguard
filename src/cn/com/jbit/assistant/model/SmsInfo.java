package cn.com.jbit.assistant.model;

/**
 * ��˵����������Ϣ
 */
public class SmsInfo {
	private String address;//��Դ
	private String date;//ʱ��
	private String type;//����
	private String body;//��Ϣ
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
