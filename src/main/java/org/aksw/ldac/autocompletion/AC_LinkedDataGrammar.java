package org.aksw.ldac.autocompletion;

import java.util.List;

public class AC_LinkedDataGrammar implements AutoCompletion {

	public void setTrainingQueries(List<String> queries) {

	}

	public String getFullQuery(String substring) {
		return substring;
	}

}
