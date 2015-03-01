package za.co.neilson.alarm.rdfmodel;

import java.util.ArrayList;

public class Modality {
	public ArrayList<String> outputModality = new ArrayList();
	public ArrayList<String> inputModality = new ArrayList();
	
	public ArrayList<String> getOutputModality() {
		return outputModality;
	}
	public void setOutputModality(ArrayList<String> outputModality) {
		this.outputModality = outputModality;
	}
	public ArrayList<String> getInputModality() {
		return inputModality;
	}
	public void setInputModality(ArrayList<String> inputModality) {
		this.inputModality = inputModality;
	}
}
