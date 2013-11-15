package org.aksw.ldac.input;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SPARQLQueryLogReaderTest {
	Logger log = LoggerFactory.getLogger(SPARQLQueryLogReaderTest.class);

	@Test
	public void sparqlToNLTest() {
		SPARQL2NL sqlr = new SPARQL2NL();
		String query = "SELECT * WHERE {<http://dbpedia.org/resource/Paris> <http://dbpedia.org/ontology/populationTotal> ?a.}";
		String nl = "Paris population total";
		String generatedNL = sqlr.Sparql2NL(query);
		log.info(generatedNL);
		assertTrue(nl.equals(generatedNL));
	}
}
