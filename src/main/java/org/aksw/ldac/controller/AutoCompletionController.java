package org.aksw.ldac.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import org.aksw.ldac.autocompletion.AC_PrefixTrie;
import org.aksw.ldac.autocompletion.AutoCompletion;
import org.aksw.ldac.eval.Evaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoCompletionController {
	Logger log = LoggerFactory.getLogger(AutoCompletionController.class);

	public static void main(String args[]) throws FileNotFoundException {
		AutoCompletionController controller = new AutoCompletionController();
		controller.run();
	}

	private void run() throws FileNotFoundException {
		// read textual queries
		String data = "test.txt";// /data/r.usbeck/bluekiwi_logs/all_cleaned_sorted_uniq.txt";// "test.txt";//""verbalizedDBpedia38logs.txt";
		log.info("Start working on data: " + data);
		File dataFile = new File(data);
		double numberOfLinesInFile = getNumberOfLinesInAFile(new FileInputStream(dataFile));

		ArrayList<InputStream> testSets = getPartsOfData(dataFile, 10);
		for (int iteration = 0; iteration < 10; iteration++) {
			// hand those to an autocompletion approach for learning
			AutoCompletion ac = new AC_PrefixTrie();
			log.debug("Start training of " + ac.toString());
			for (int training = 0; training < 10 && training != iteration; ++training) {
				ac.setTrainingQueries(testSets.get(training));
			}
			log.debug("Finished training of " + ac.toString());

			// evaluate
			log.debug("Start evaluation of " + ac.toString());
			Evaluation ev = new Evaluation();
			InputStream test = testSets.get(iteration);
			ev.setTestQueries(test);
			ev.setAutoCompletionAlgortihm(ac);
			double areaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries = ev.getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries();
			log.debug("Finished evaluation of " + ac.toString());
			log.info("AUC = " + areaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries);
		}
	}

	private ArrayList<InputStream> getPartsOfData(File dataFile, int numberOfParts) {
		ArrayList<InputStream> tmp = new ArrayList<InputStream>();
		ArrayList<StringBuilder> builder = new ArrayList<StringBuilder>();
		for (int i = 0; i < numberOfParts; ++i) {
			builder.add(new StringBuilder());
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			while (br.ready()) {
				for (int i = 0; i < numberOfParts; ++i) {
					if (br.ready()) {
						String readLine = br.readLine();
						builder.get(i).append(readLine);
						builder.get(i).append("\n");
					}
				}
			}
			br.close();
			for (int i = 0; i < numberOfParts; ++i) {
				tmp.add(new ByteArrayInputStream(builder.get(i).toString().getBytes("UTF-8")));
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		return tmp;
	}

	private double getNumberOfLinesInAFile(InputStream file) {
		double numberOfLines = 0;
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(file));
			while (lnr.ready()) {
				lnr.readLine();
			}
			numberOfLines = lnr.getLineNumber();
			lnr.close();
		} catch (FileNotFoundException e) {
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		return numberOfLines;
	}

}
