package org.aksw.ldac.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.aksw.ldac.autocompletion.AC_LinkedDataGrammar;
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
		String[] datasets = new String[] { "data/hotelModeSearches_100000.txt", "data/normalModeSearches_100000.txt" };

		HashMap<String, HashMap<String, Double>> approachDatasetValue = new HashMap<String, HashMap<String, Double>>();

		for (String data : datasets) {
			log.info("Start working on data: " + data);
			for (AutoCompletion ac : new AutoCompletion[] { new AC_Lucene(), new AC_PrefixTrie(), new AC_LinkedDataGrammar("data/autocompletion") }) {
				log.info("Approach:   " + ac.toString());
				ArrayList<Double> aucs = new ArrayList<Double>();
				for (int iteration = 0; iteration < 10; iteration++) {
					Pair<InputStream, InputStream> crossValidData = getPartsOfData(data, 10, iteration);

					log.debug("Start training of " + ac.toString());
					ac.setTrainingQueries(crossValidData.getRight());
					log.debug("Start evaluation of " + ac.toString());
					Evaluation ev = new Evaluation();
					ev.setTestQueries(crossValidData.getLeft());
					ev.setAutoCompletionAlgortihm(ac);
					aucs.add(ev.getAreaUnderCurveBetweenLengthOfQueryAndPercentCorrectQueries());
					log.debug("Finished evaluation of " + ac.toString());
				}
				log.info("\tAverage AUC = " + average(aucs));
				HashMap<String, Double> tmp = new HashMap<String, Double>();
				tmp.put(data, average(aucs));
				approachDatasetValue.put(ac.toString(), tmp);
			}
		}
		writeLatex(approachDatasetValue, datasets);
	}

	private void writeLatex(HashMap<String, HashMap<String, Double>> approachDatasetValue, String[] datasets) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("Autocompletion.tex"));
			bw.write("\\begin{table*}[htb!]");
			bw.newLine();
			bw.write("\\centering");
			bw.newLine();
			bw.write("\\caption{Evaluation of Linked Data Approaches.}");
			bw.newLine();
			bw.write("\\begin{tabular}{cccc}");
			bw.newLine();
			bw.write("\\toprule");
			bw.newLine();

			int i = 0;
			bw.write("&");
			for (String approach : approachDatasetValue.keySet()) {
				bw.write("\\textbf{" + approach + "} ");
				i++;
				if (i < approachDatasetValue.keySet().size()) {
					bw.write("&");
				} else {
					bw.write("\\\\");
				}
			}
			bw.newLine();
			bw.write("\\midrule");
			bw.newLine();

			for (String dataset : datasets) {
				i = 0;
				bw.write(dataset+"&");
				for (String approach : approachDatasetValue.keySet()) {
					bw.write("" + approachDatasetValue.get(approach).get(dataset) + "");
					i++;
					if (i < approachDatasetValue.keySet().size()) {
						bw.write("&");
					} else {
						bw.write("\\\\");
					}
				}bw.newLine();
			}
			bw.write("\\bottomrule");
			bw.newLine();
			bw.write("\\end{tabular}");
			bw.newLine();
			bw.write("\\end{table*}");

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double average(ArrayList<Double> aucs) {
		double sum = 0;
		for (Double d : aucs) {
			sum += d;
		}
		return sum / aucs.size();
	}

	private Pair<InputStream, InputStream> getPartsOfData(String dataFile, int numberOfParts, int testNumber) {
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
