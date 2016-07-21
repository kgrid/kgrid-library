package org.uofm.ot.fedoraAccessLayer;

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

	
	

}
