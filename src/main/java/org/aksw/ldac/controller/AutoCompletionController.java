package org.aksw.ldac.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.aksw.ldac.autocompletion.AC_Lucene;
import org.aksw.ldac.autocompletion.AC_PrefixTrie;
import org.aksw.ldac.autocompletion.AutoCompletion;
import org.aksw.ldac.eval.Evaluation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
		String data = "/data/r.usbeck/Dropbox/Dissertation/LinkedDataAutoCompletion/searchHistoryData.txt";//"/data/r.usbeck/bluekiwi_logs/all_cleaned_sorted_uniq.txt";// "verbalizedDBpedia38logs.txt";//"test.txt";//
String materializedOntology="/data/r.usbeck/Dropbox/Dissertation/LinkedDataAutoCompletion/autocompletion";
		Integer numberOfCrossValidations = 10;
		ArrayList<Double> aucs = new ArrayList<Double>();

		log.info("Start working on data: " + data);
		// hand those to an autocompletion approach for learning
		for (AutoCompletion ac : new AutoCompletion[] { new AC_Lucene(), new AC_PrefixTrie() }) {
			log.info("Approach:   " +ac.toString());
			for (int iteration = 0; iteration < numberOfCrossValidations; iteration++) {
				File dataFile = new File(data);
				Pair<InputStream, InputStream> datasets = getPartsOfData(dataFile, numberOfCrossValidations, iteration);

				log.debug("Start training of " + ac.toString());

				ac.setTrainingQueries(datasets.getRight());

				// evaluate
				log.debug("Start evaluation of " + ac.toString());

				Evaluation ev = new Evaluation();
				ev.setTestQueries(datasets.getLeft());
				ev.setAutoCompletionAlgortihm(ac);
				aucs.add(ev.getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries());

				log.debug("Finished evaluation of " + ac.toString());
			}
			log.info("\tAUC = " + average(aucs));
		}
	}

	private double average(ArrayList<Double> aucs) {
		double sum = 0;
		for (Double d : aucs) {
			sum += d;
		}
		return sum / aucs.size();
	}

	private Pair<InputStream, InputStream> getPartsOfData(File dataFile, int numberOfParts, int testNumber) {
		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		ByteArrayInputStream leftStream = null;
		ByteArrayInputStream rightStream = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			while (br.ready()) {
				for (int i = 0; i < numberOfParts; ++i) {
					if (br.ready()) {
						String readLine = br.readLine();
						if (i == testNumber) {
							left.append(readLine);
							left.append("\n");
						} else {
							right.append(readLine);
							right.append("\n");
						}

					}
				}
			}
			br.close();
			leftStream = new ByteArrayInputStream(left.toString().getBytes("UTF-8"));
			rightStream = new ByteArrayInputStream(right.toString().getBytes("UTF-8"));

		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

		return new ImmutablePair<InputStream, InputStream>(leftStream, rightStream);
	}

}
