package org.uofm.ot.knowledgeObject;

public class Citation {
	private String citation_id; 
	
	private String citation_title;
	
	private String citation_at;

	public String getCitation_title() {
		return citation_title;
	}

	public void setCitation_title(String citation_title) {
		this.citation_title = citation_title;
	}

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
