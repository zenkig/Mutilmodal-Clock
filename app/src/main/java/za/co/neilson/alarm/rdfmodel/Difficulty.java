package za.co.neilson.alarm.rdfmodel;


public class Difficulty {
	private String name; 
	private String uri;

    Difficulty(String uri, String name) {
		this.uri = uri; 
		this.name = name; 
	}
    Difficulty(String uri) {
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
