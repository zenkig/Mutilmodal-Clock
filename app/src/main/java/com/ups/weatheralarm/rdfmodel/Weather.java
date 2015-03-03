package com.ups.weatheralarm.rdfmodel;

public class Weather {
	public Weather(String name, String uri) {
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
