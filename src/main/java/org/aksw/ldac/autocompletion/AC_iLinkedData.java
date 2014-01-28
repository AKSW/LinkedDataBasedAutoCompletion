package org.aksw.ldac.autocompletion;

import java.io.InputStream;

import org.aksw.ldac.util.SPARQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AC_iLinkedData implements AutoCompletion {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private SPARQL sparql;

	public AC_iLinkedData(String materializedGrammar) {
		 this.sparql = new SPARQL();
	}

	public String getFullQuery(String substring) {
	 return null;
	}

	public void setTrainingQueries(InputStream training) {
	}

	public String toString() {
		return "iLinkedData";
	}

}
