package org.aksw.ldac.autocompletion;

import java.io.InputStream;

public class AC_LinkedDataGrammar implements AutoCompletion {


	private String materializedGrammar;
	public AC_LinkedDataGrammar(String materializedGrammar) {
		this.materializedGrammar = materializedGrammar;
	}

	public String getFullQuery(String substring) {
		return substring;
	}

	public void setTrainingQueries(InputStream training) {
		// TODO Auto-generated method stub

	}
	public String toString(){
		return "Linked Data-based Grammar";
	}

}
