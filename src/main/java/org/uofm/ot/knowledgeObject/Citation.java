package org.uofm.ot.knowledgeObject;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citation_id == null) ? 0 : citation_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citation other = (Citation) obj;
		if (citation_id == null) {
			if (other.citation_id != null)
				return false;
		} else if (!citation_id.equals(other.citation_id))
			return false;
		return true;
	}

	
	

}
