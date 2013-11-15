package org.aksw.ldac.input;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SPARQLQueryLogReaderTest {
	@Test
	public void sparqlToNLTest() {
		SPARQL2NL sqlr = new SPARQL2NL();
		String query = "SELECT * WHERE {<http://dbpedia.org/resource/Paris> <http://dbpedia.org/ontology/area> ?a.}";
		String nl = "Paris Fläche";
		String generatedNL = sqlr.Sparql2NL(query);
		assertTrue(nl.equals(generatedNL));
	}
}
