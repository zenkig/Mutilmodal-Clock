package com.ups.weatheralarm.rdfmodel;



public class UserProfile {
	
	private String name; 
	private String uri;
	
	UserProfile(String uri, String name) {
		this.uri = uri; 
		this.name = name; 
	}
	UserProfile(String uri) {
		this.uri = uri; 
	}
	// get the name for this userActivity
	public String getName() {
		return this.name;
	}
	// get the RDF uri for this userActivity
	public String getURI() {
		return this.uri;
	}

}
