package org.aksw.ldac.controller;

import java.util.List;

import org.aksw.ldac.autocompletion.AC_LinkedDataGrammar;
import org.aksw.ldac.autocompletion.AutoCompletion;
import org.aksw.ldac.eval.Evaluation;
import org.aksw.ldac.input.QueryReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoCompletionController {
	Logger log = LoggerFactory.getLogger(AutoCompletionController.class);

	public static void main(String args[]) {
		AutoCompletionController controller = new AutoCompletionController();
		controller.run();
	}

	private void run() {
		// read textual queries
		String data = "verbalizedDBpedia38logs.txt";
		log.info("Start working on data: " + data);
		List<String> queries = (new QueryReader(data)).getModel();

		// hand those to an autocompletion approach for learning
		AutoCompletion ac = new AC_LinkedDataGrammar();
		log.info("Start training of " + ac.toString());
		ac.setTrainingQueries(queries);
		log.info("Finished training of " + ac.toString());

		// evaluate
		log.info("Start evaluation of " + ac.toString());
		Evaluation ev = new Evaluation();
		ev.setTestQueries(queries);
		ev.setAutoCompletionAlgortihm(ac);
		double areaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries = ev.getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries();
		log.info("Finished evaluation of " + ac.toString());
		log.info("AUC = " + areaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries);

	}

}
