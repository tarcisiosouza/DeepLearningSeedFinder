package de.l3s.souza.search;
import java.io.IOException;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class DbpediaQuery {
	
	public String queryDbpedia(String str) throws IOException {
		
		String output = null;
		String sparqlQueryString2 = "";
	    String service = "http://de.dbpedia.org/sparql";
	    String regex = "Kategorie:Deutsche.*";
		
		int queries = 0;
		int entities = 0;
		Query query = null;
		
			sparqlQueryString2 = " PREFIX  dc:  <http://purl.org/dc/elements/1.1/>"+
			           "SELECT    ?title "+
			          
			                 "{<http://de.dbpedia.org/resource/"+str+">"+ "<http://purl.org/dc/terms/subject> ?title "+
			                   
			           //        "FILTER regex(?title," + regex + ")"+
			                  "}LIMIT 10";
			try {
			 query = QueryFactory.create(sparqlQueryString2);
			 
			    ARQ.getContext().setTrue(ARQ.useSAX);
		
			    // Executing SPARQL Query and pointing to the DBpedia SPARQL Endpoint
			    QueryExecution qexec = QueryExecutionFactory.sparqlService(
			            service, query);
			    // Retrieving the SPARQL Query results
			    ResultSet results = qexec.execSelect();
			    // Iterating over the SPARQL Query results
			    while (results.hasNext()) {
			        QuerySolution soln = results.nextSolution();
			     
			        	//System.out.println (entity+": German entity");
			        	System.out.println(soln.getResource("?title").toString()+"\n");
			        	entities++;
			        	System.out.println (entities + " " + soln.getResource("?title").toString());
			        	output = soln.getResource("?title").toString();
			        
			    }
			    qexec.close();
			} 
			catch (Exception e)
			{
				
			}
			
			return output;
		}
		
	    
	 }
