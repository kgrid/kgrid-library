package org.uofm.ot.knowledgeObject;

import java.util.Date;
import java.util.List;

public class Metadata {

	private String title;
	
	private String owner;
	
	private String description;
	
	private String contributors;
	
	private String keywords; 
	
	private boolean published;
	
	private Date lastModified;
	
	private Date createdOn;
	
	private String objectType;
	
	private List<Citation> citations;
	
	private License license ;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContributors() {
		return contributors;
	}

	public void setContributors(String contributors) {
		this.contributors = contributors;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public List<Citation> getCitations() {
		return citations;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	@Override
	public String toString() {
		return "Metadata [title=" + title + ", owner=" + owner + ", description=" + description + ", contributors="
				+ contributors + ", keywords=" + keywords + ", published=" + published + ", lastModified="
				+ lastModified + ", createdOn=" + createdOn + ", objectType=" + objectType + ", citations=" + citations
				+ ", license=" + license + "]";
	}

}
