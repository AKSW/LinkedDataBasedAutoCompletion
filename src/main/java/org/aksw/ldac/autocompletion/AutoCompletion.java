package org.aksw.ldac.autocompletion;

import java.util.List;

public interface AutoCompletion {

	void setTrainingQueries(List<String> queries);

	String getFullQuery(String substring);

}
