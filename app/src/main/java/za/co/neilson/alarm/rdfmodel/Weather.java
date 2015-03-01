package za.co.neilson.alarm.rdfmodel;

public class Weather {
	Weather(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}
	private String name; 
	private String uri;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getURI() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
