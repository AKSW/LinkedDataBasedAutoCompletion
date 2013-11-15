package org.aksw.ldac.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;

public class SPARQL2NL {
	Logger log = LoggerFactory.getLogger(SPARQL2NL.class);

	public String Sparql2NL(String query) {
		// analyze query for bound variables
		Query q = QueryFactory.create(query);
		handleProjection(q, q.isSelectType() && q.isQueryResultStar());
		log.debug("getDatasetDescription" + q.getDatasetDescription());
		log.debug("getQueryPattern" + q.getQueryPattern());
		log.debug("getQueryPattern" + q.getQueryPattern());
		// verbalize them
		ElementVisitor visitor = new ElementVisitor();
		visitor.setNaturalLanguangeQuery(new String());
		ElementWalker.walk(q.getQueryPattern(), visitor);
		return visitor.getNaturalLanguangeQuery();
	}

	private void handleProjection(Query q, boolean isStarProjection) {
		// TODO Auto-generated method stub
		// of the form SELECT *?

	}
}
