package com.ups.weatheralarm.rdfmodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFModel {
    private Model model = ModelFactory.createDefaultModel();


    RDFModel(Context context) throws IOException {
        InputStream f;
        //load model
        f = context.getAssets().open("rdfmodel.xml");
        model.read(f, "http://imi.org/");
    }

    /*
     * query for the best modality to use based on the situation and activity
     * TODO: build a class for situation just like UserProfile
     */
    public LinkedList<String> getModality(UserProfile user, Difficulty diff, Weather weather, Modality m) {

        String oldUri = weather.getURI();
        weather.setUri("http://imi.org/" + oldUri);

        LinkedList<String> allInputModalities = new LinkedList<String>();
        Query query = QueryFactory.create(
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                        + "PREFIX ex: <http://imi.org/>"
                        + "SELECT ?Modality ?UserProfile ?Difficulty ?Weather"
                        + "WHERE {"
                        + "?Modality ex:hasUserProfile ?UserProfile ."
                        + "?Modality ex:hasDifficulty ?Difficulty ."
                        + "?Modality ex:hasWeather ?Weather ."
                        + "FILTER ( ?UserProfile = <" + user.getURI() + ">) ."
                        + "FILTER ( ?Difficulty = <" + diff.getURI() + ">) ."
                        + "FILTER ( ?Weather = <" + weather.getURI() + ">) ."
                        + "}");

        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        ResultSet rs = qe.execSelect();
        //ResultSetFormatter.out(System.out, rs, query);

        ArrayList<String> inList = new ArrayList();
        ArrayList<String> outList = new ArrayList();

        while (rs.hasNext()) {
            QuerySolution row = rs.nextSolution();
            //System.out.println(row);

            allInputModalities.add(row.get("?Modality").toString());

            int index = row.get("?Modality").toString().lastIndexOf("/") + 1;
            String new_string =  row.get("?Modality").toString().substring(index);
//            System.out.println("LIST:" + list);

            if (new_string.equals("Speak") || new_string.equals("Blow") || new_string.equals("Click") || new_string.equals("Wipe") || new_string.equals("Shake")) {
                inList.add(new_string);
                m.setInputModality(inList);
            }
            if (new_string.equals("Visual") || new_string.equals("Sound") || new_string.equals("Vibrate") || new_string.equals("Flash")) {
                outList.add(new_string);
                m.setOutputModality(outList);
            }
        }
        Log.i("rescue", "input" + m.getInputModality());
        Log.i("rescue", "output" + m.getOutputModality());

        qe.close();
        return allInputModalities;
    }

    /*
     * Creates a list of activities based on the RDFModel
     * TODO: At the moment the printed string is "http://imi.org/SendingSignal"
     * since we are going to display this result in the drop down menu
     * see if you could only print out the activity name without the initial uri
     * TODO: Write a same query for the list of situations and weather
     */
    public LinkedList<String> createUserProfileFromRDF() {
        LinkedList<String> allUserProfile = new LinkedList<String>();
        Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ex: <http://imi.org/> " +
                "SELECT ?UserProfile WHERE {?UserProfile rdf:type ex:UserProfile }");
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        ResultSet rs = qe.execSelect();

        while (rs.hasNext()) {
            QuerySolution row = rs.nextSolution();
            allUserProfile.add(row.get("?UserProfile").toString());
        }
        qe.close();
        return allUserProfile;
    }

    public LinkedList<String> createDifficultyFromRDF() {
        LinkedList<String> allDifficulty = new LinkedList<String>();
        Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ex: <http://imi.org/> " +
                "SELECT ?Difficulty WHERE {?Difficulty rdf:type ex:Difficulty }");
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution row = rs.nextSolution();
            allDifficulty.add(row.get("?Difficulty").toString());
        }
        qe.close();
        return allDifficulty;
    }

    public LinkedList<String> createWeatherFromRDF() {
        LinkedList<String> allWeather = new LinkedList<String>();
        Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ex: <http://imi.org/> " +
                "SELECT ?Weather WHERE {?Weather rdf:type ex:Weather }");
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution row = rs.nextSolution();
            allWeather.add(row.get("?Weather").toString());
        }
        qe.close();
        return allWeather;
    }
}
