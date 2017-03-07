package de.l3s.souza.deepLearningSeedFinder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AppTest 
    extends TestCase
{


    public AppTest( String testName )
    {
        super( testName );
    }


    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp() throws Exception
    {
   
//initialQuery, limit, field, ExpandedTerms,  maxSimTerms, eventDate, maxDoc,  maxIter,  alpha, beta,gama, scoreFunctionParam)
    	Query query = new Query ("bundestagswahl 2002",2000,"url",7,10,"2002-22-09",50,2,0.4,0.2,0.1,0.6);
    	//query.processQuery("angela merkel");
    }
}
