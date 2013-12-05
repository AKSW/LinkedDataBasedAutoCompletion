package org.aksw.ldac.autocompletion;

import java.io.InputStream;

public class AC_LinkedDataGrammar implements AutoCompletion {

	public String getFullQuery(String substring) {
		return substring;
	}

	public void setTrainingQueries(InputStream training) {
		// TODO Auto-generated method stub

	}

}
