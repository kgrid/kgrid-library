package edu.umich.lhs.library.knowledgeObject;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;
import com.complexible.pinto.impl.IdentifableImpl;
import org.openrdf.model.Resource;

@RdfsClass(value="ot:citation")
public class Citation implements Identifiable {

	private Identifiable mIdendifiable = new IdentifableImpl();

	private String citation_id;
	
	private String citation_title;
	
	private String citation_at;

	@RdfProperty(value="ot:citationTitle")
	public String getCitation_title() {
		return citation_title;
	}

	public void setCitation_title(String citation_title) {
		this.citation_title = citation_title;
	}

	@RdfProperty(value="ot:citationAt")
	public String getCitation_at() {
		return citation_at;
	}

	public void setCitation_at(String citation_at) {
		this.citation_at = citation_at;
	}

	@RdfProperty(value = "ot:citationID")
	public String getCitation_id() {
		return citation_id;
	}

	public void setCitation_id(String citation_id) {
		this.citation_id = citation_id;
	}

	@Override
	public Resource id() {
		return mIdendifiable.id();
	}

	@Override
	public void id(final Resource resource) {
		mIdendifiable.id(resource);
	}


	@Override
	public String toString() {
		return "Citation [citation_id=" + citation_id + ", citation_title=" + citation_title + ", citation_at="
				+ citation_at + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Citation citation = (Citation) o;

		return citation_id != null ? citation_id.equals(citation.citation_id)
				: citation.citation_id == null;
	}

	@Override
	public int hashCode() {
		return citation_id != null ? citation_id.hashCode() : 0;
	}
}
