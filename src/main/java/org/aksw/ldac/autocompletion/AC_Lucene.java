package org.aksw.ldac.autocompletion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.util.Version;

public class AC_Lucene implements AutoCompletion {

	private AnalyzingSuggester suggester;

	public String getFullQuery(String substring) {
		List<LookupResult> results = suggester.lookup(substring, false, 1);
		if (!results.isEmpty()) {
			if (results.get(0) != null) {
				return results.get(0).key.toString();
			}
		}
		return substring;
	}

	public void setTrainingQueries(InputStream training) {
		try {
			Dictionary dic = new PlainTextDictionary(training);
			this.suggester = new FuzzySuggester(new StandardAnalyzer(Version.LUCENE_46));
			suggester.build(dic);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
