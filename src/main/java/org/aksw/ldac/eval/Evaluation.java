package org.aksw.ldac.eval;

import java.util.List;

import org.aksw.ldac.autocompletion.AutoCompletion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluation {
	Logger log = LoggerFactory.getLogger(Evaluation.class);
	private List<String> testQueries;
	private AutoCompletion ac;

	public double getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries() {
		double[] bins = new double[500];
		//TODO
		for (String query : testQueries) {
			for (int i = 1; i <= query.length(); ++i) {
				String guessedQuery = ac.getFullQuery(query.substring(0, i));
				if (query.equals(guessedQuery)) {
//					 if(true){
					double d = (double) i / (double) query.length();
					bins[(int) Math.round(d * (bins.length-1))]++;
				}
			}
		}
		for (int i = 0; i < bins.length; ++i) {
//			bins[i] = bins[i]/(double)testQueries.size();
			log.info(((double) i / 100.0) + "->" + bins[i]);
		}

		double areaUnderCurve = 0;
		for (int i = 0; i < bins.length; ++i) {
			areaUnderCurve += bins[i];
		}
		return areaUnderCurve / ((bins.length - 1) * (bins.length - 1));
	}

	public void setAutoCompletionAlgortihm(AutoCompletion ac) {
		this.ac = ac;

	}

	public void setTestQueries(List<String> queries) {
		this.testQueries = queries;

	}

}
