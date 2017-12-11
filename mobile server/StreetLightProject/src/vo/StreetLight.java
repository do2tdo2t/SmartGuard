package vo;

public class StreetLight {
	String id;
	int code;
	double lat; //37.xxxxxxxx
	double lon; // 127.xxxxx
	String info;
	int alarm;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAlarm() {
		return alarm;
	}
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "StreetLight [id=" + id + ", code=" + code + ", lat=" + lat + ", lon=" + lon + ", info=" + info + "]";
	}
	
	
}
