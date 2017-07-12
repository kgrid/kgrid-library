package org.uofm.ot.knowledgeObject;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.impl.IdentifableImpl;
import java.util.Date;
import java.util.List;
import org.openrdf.model.Resource;

public class Metadata implements Identifiable {

	private Identifiable mIdentifiable = new IdentifableImpl();

	private String title;

	private String arkId;

	private Version version;
	
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
	
	@RdfProperty(value="dc:title")
	public String getTitle() {
		return title;
	}

	@RdfProperty(value="dc:title")
	public void setTitle(String title) {
		this.title = title;
	}

	@RdfProperty(value="ot:arkId")
	public String getArkId() {
		return arkId;
	}

	@RdfProperty(value="ot:arkId")
	public void setArkId(String arkId) {
		this.arkId = arkId;
	}

	@RdfProperty(value="ot:version")
	public String getVersion() {
		return version == null ? "" : version.toString();
	}

	@RdfProperty(value = "ot:version")
	public void setVersion(String version) {
		this.version = new Version(version);
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	@RdfProperty(value="ot:owner")
	public String getOwner() {
		return owner;
	}

	@RdfProperty(value="ot:owner")
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@RdfProperty(value="ot:description")
	public String getDescription() {
		return description;
	}

	@RdfProperty(value="ot:description")
	public void setDescription(String description) {
		this.description = description;
	}

	@RdfProperty(value="ot:contributors")
	public String getContributors() {
		return contributors;
	}

	@RdfProperty(value="ot:contributors")
	public void setContributors(String contributors) {
		this.contributors = contributors;
	}

	@RdfProperty(value="ot:keywords")
	public String getKeywords() {
		return keywords;
	}

	@RdfProperty(value="ot:keywords")
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@RdfProperty(value="ot:published")
	public boolean isPublished() {
		return published;
	}

	@RdfProperty(value="ot:published")
	public void setPublished(boolean published) {
		this.published = published;
	}

	public Date getLastModified() {
		return lastModified;
	}

	@RdfProperty(value="fedora:lastModified")
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	@RdfProperty(value="fedora:created")
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@RdfProperty(value = "ot:citations", isList = true)
	public List<Citation> getCitations() {
		return citations;
	}

	@RdfProperty(value = "ot:citations", isList = true)
	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	@RdfProperty(value = "ot:license")
	public License getLicense() {
		return license;
	}

	@RdfProperty(value = "ot:license")
	public void setLicense(License license) {
		this.license = license;
	}

	@Override
	public Resource id() {
		return mIdentifiable.id();
	}

	@Override
	public void id(final Resource resource) {
		mIdentifiable.id(resource);
	}


	@Override
	public String toString() {
		return "Metadata{" +
				"mIdentifiable=" + mIdentifiable +
				", title='" + title + '\'' +
				", arkId='" + arkId + '\'' +
				", version='" + version + '\'' +
				", owner='" + owner + '\'' +
				", description='" + description + '\'' +
				", contributors='" + contributors + '\'' +
				", keywords='" + keywords + '\'' +
				", published=" + published +
				", lastModified=" + lastModified +
				", createdOn=" + createdOn +
				", objectType='" + objectType + '\'' +
				", citations=" + citations +
				", license=" + license +
				'}';
	}
}
