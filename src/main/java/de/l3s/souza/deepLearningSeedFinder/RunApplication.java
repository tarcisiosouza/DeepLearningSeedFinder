package de.l3s.souza.deepLearningSeedFinder;

public class RunApplication {

	public static void main (String args[]) throws Exception
	{
		//Query(String initialQuery,int limit,String field,int terms, int maxSimTerms, int maxDoc, String currentQuery)
    		Query query = new Query ("bundestagswahl 2002",10000,"text",6,20,50,20,0.8,0.001,0.2);
	}
}

	
