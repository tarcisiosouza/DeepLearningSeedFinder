package de.l3s.souza.deepLearningSeedFinder;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.l3s.souza.search.DbpediaQuery;

public class DbpediaTest {

	@Test
	public void test() throws IOException {
		DbpediaQuery dbpedia = new DbpediaQuery ();
		dbpedia.queryDbpedia("Angela Merkel");
	}

}
