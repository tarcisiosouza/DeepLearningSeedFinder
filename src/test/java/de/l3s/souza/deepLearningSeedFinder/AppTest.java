package de.l3s.souza.deepLearningSeedFinder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp() throws Exception
    {
   
//initialQuery, limit, field, terms,  maxSimTerms,  maxDoc,  maxIter,  alpha, beta)
    	Query query = new Query ("bundestagswahl 2002",5000,"text",4,10,800,10,0.4,0.2);
    	//query.processQuery("angela merkel");
    }
}
