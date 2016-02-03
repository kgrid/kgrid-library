package org.uofm.ot.fedoraAccessLayer;

public class FedoraObject {

	private String title;
	
	private boolean published;
	
	private String URI;
	
	private int bins;
	
	private ObjectType objectType;

	public FedoraObject(String title, boolean published, String URI, int bins, ObjectType objectType) {
		super();
		this.title = title;
		this.published = published;
		this.URI = URI;
		this.bins = bins;
		this.objectType = objectType;
	}
	
	public FedoraObject(){}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public int getBins() {
		return bins;
	}

	public void setBins(int bins) {
		this.bins = bins;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	@Override
	public String toString() {
		return "FedoraObject [title=" + title + ", published=" + published + ", URI=" + URI + ", bins=" + bins
				+ ", objectType=" + objectType + "]";
	}

}
