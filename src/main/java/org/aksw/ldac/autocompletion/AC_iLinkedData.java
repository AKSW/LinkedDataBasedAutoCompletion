package org.aksw.ldac.autocompletion;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.aksw.ldac.eval.Evaluation;
import org.aksw.ldac.util.SPARQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AC_iLinkedData implements AutoCompletion {

	private static Logger log = LoggerFactory.getLogger(AC_iLinkedData.class);
	private SPARQL sparql;

	public static void main(String[] args) throws UnsupportedEncodingException {

		ArrayList<String> testQueries = new ArrayList();
		testQueries.add("Hotel auf Mallorca");
		testQueries.add("Hotel Hamburg");
		testQueries.add("Hotel auf Mallorca");
		testQueries.add("Hotel auf Mallorca");
		testQueries.add("Hotel in Dominikanische Republik");
		testQueries.add("Malediven buchen Hotel");
		testQueries.add("T�rkei Bewertung Urlaub");
		testQueries.add("Louvre Bewertung buchen");
		testQueries.add("Last Minute Reisen lastminute Flug Flughafen");
		testQueries.add("T�rkei istanbul");

		AutoCompletion ac = new AC_iLinkedData();
		for (String query : testQueries) {
			// TODO hack for wrongly encoded dumbs
			if (!query.contains("�")) {
				log.debug(URLDecoder.decode(query, "UTF8"));
			}
		}
		Evaluation ev = new Evaluation();
		ev.setTestQueries(testQueries);
		ev.setAutoCompletionAlgortihm(ac);
		log.debug("" + ev.getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries());
	}

	public AC_iLinkedData() {
		this.sparql = new SPARQL();
	}

	public String getFullQuery(String substring) {
		// classify substring whether it is a concept or instance

		// rank after popularity
		
		// identify next possible step
		
		return null;
	}

	public void setTrainingQueries(InputStream training) {
	}

	public String toString() {
		return "iLinkedData";
	}

}
