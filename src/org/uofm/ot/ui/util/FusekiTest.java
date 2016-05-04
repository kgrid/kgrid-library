package org.uofm.ot.ui.util;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;


public class FusekiTest {

	public static void main(String[] args) {
		/*String queryString = "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"+
			"PREFIX fedora: <http://fedora.info/definitions/v4/repository#> \n"+
			"PREFIX ot: <http://uofm.org/objectteller/> \n"+

				"SELECT  ?x ?title ?published ?lastModified ?owner \n"+

				"WHERE \n"+
				"{ \n"+ 
				  "?x  dc:title  ?title. \n"+
				  "?x  ot:published ?published. \n"+
				  "?x  fedora:lastModified  ?lastModified. \n"+
				  "?x  ot:owner  ?owner. \n"+
				  
				"} \n";
		
		System.out.println(queryString);
		
		Query query = QueryFactory.create(queryString) ;
		QueryExecution execution = QueryExecutionFactory.sparqlService("http://localhost:8080/fuseki/test/query", query);
		ResultSet resultSet = execution.execSelect();
		
		 
		while (resultSet.hasNext())
		{
			QuerySolution binding = resultSet.nextSolution();
			System.out.println(binding.get("x")+" | "+binding.get("title") +" | "+ binding.get("published") +" | "+  binding.get("owner") +"|"+ binding.get("lastModified"));
			String test = binding.get("lastModified").toString();
			test = test.substring(0, 16 );
			System.out.println(test);
		}*/
		

	/*	String queryString = 
				"PREFIX  ot:   <http://uofm.org/objectteller/> \n"+  
					"SELECT  ?libraryName \n"+
					"WHERE { \n"+ 
					 " <http://localhost:8080/fcrepo/rest/>  ot:libraryName ?libraryName. \n"+
					"} ";

		 System.out.println("Query string is "+queryString);
		 
		 Query query = QueryFactory.create(queryString) ;
		 QueryExecution execution = QueryExecutionFactory.sparqlService("http://localhost:8080/fuseki/test/query", query);
		 ResultSet resultSet = execution.execSelect();

		 String countString = null;
		 if (resultSet.hasNext()) {
			 QuerySolution binding = resultSet.nextSolution();
			 countString = binding.get("libraryName").toString();
			 System.out.println("CountString is "+countString);
			 
			 
		 }*/
		 
	
		 
		
				String queryString = "PREFIX  ot:   <http://uofm.org/objectteller/> \n"+

				 "SELECT  ?p ?o \n"+

				 "WHERE {  \n"+
				 "<http://localhost:8080/fcrepo/rest/testCancerObject>  ?p ?o.\n"+ 
				 "} \n" ;

				Query query = QueryFactory.create(queryString) ;
				QueryExecution execution = QueryExecutionFactory.sparqlService("http://localhost:8080/fuseki/test/query", query);
				ResultSet resultSet = execution.execSelect();

				while (resultSet.hasNext()) {
					QuerySolution binding = resultSet.nextSolution();
					System.out.println("$$$" + binding.toString());
					String predicate = binding.get("p").toString();
					if(predicate.contains("http://fedora.info/definitions/v4/repository#") == true){
						String actualPredicate = predicate.substring("http://fedora.info/definitions/v4/repository#".length());
						System.out.println(actualPredicate);
						if("lastModified".equals(actualPredicate) || "created".equals(actualPredicate) ){
							// #TODO 
						}
						
					}
					System.out.println(binding.get("o"));
					System.out.println("---------------");
				}
				
		
		
		 
 /*    String[] array =  countString.split("\\^\\^");
     for (String string : array) {
		System.out.println(":"+string);
	}*/
      
	}

}
