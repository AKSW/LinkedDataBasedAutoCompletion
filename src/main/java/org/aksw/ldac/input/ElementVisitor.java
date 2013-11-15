package org.aksw.ldac.input;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;

public class ElementVisitor extends ElementVisitorBase {
	Logger log = LoggerFactory.getLogger(ElementVisitor.class);
	private String naturalLanguangeQuery;
	private DBPedia dbpedia;

	public ElementVisitor() {
		dbpedia = new DBPedia();
	}

	@Override
	public void visit(ElementPathBlock el) {
		ListIterator<TriplePath> it = el.getPattern().iterator();
		while (it.hasNext()) {
			final TriplePath tp = it.next();
			naturalLanguangeQuery += verbalizeSubject(tp) + " ";
			naturalLanguangeQuery += verbalizePredicate(tp) + " ";
			naturalLanguangeQuery += verbalizeObject(tp) + " ";

			log.debug("triplePath" + tp);
		}
	}

	private String verbalizeSubject(TriplePath tp) {
		ResultSet res = dbpedia.askDbpedia("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + "SELECT ?label " + "WHERE {<" + tp.getSubject().getURI() + "> rdfs:label ?label . FILTER(langMatches(lang(?label), \"EN\"))" + "}");
		return res.next().getLiteral("label").getString();

	}

	private String verbalizePredicate(TriplePath tp) {
		ResultSet res = dbpedia.askDbpedia("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + "SELECT ?label " + "WHERE {<" + tp.getPredicate().getURI() + "> rdfs:label ?label . FILTER(langMatches(lang(?label), \"EN\"))" + "}");
		return res.next().getLiteral("label").getString();

	}

	private String verbalizeObject(TriplePath tp) {
		// TODO look up in index if not a Literal
		if (tp.getObject().isURI())
			return tp.getObject().getURI();
		else
			return new String();

	}

	public String getNaturalLanguangeQuery() {
		return naturalLanguangeQuery.trim();
	}

	public void setNaturalLanguangeQuery(String naturalLanguangeQuery) {
		this.naturalLanguangeQuery = naturalLanguangeQuery;
	}

}
