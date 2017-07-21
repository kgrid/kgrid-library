package org.uofm.ot.knowledgeObject;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.impl.IdentifableImpl;
import org.openrdf.model.Resource;

public class License implements Identifiable {

	private Identifiable mIdendifiable = new IdentifableImpl();
	
	private String licenseName = "";
	
	private String licenseLink = "";

	@RdfProperty(value="ot:licenseName")
	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	@RdfProperty(value="ot:licenseLink")
	public String getLicenseLink() {
		return licenseLink;
	}

	public void setLicenseLink(String licenseLink) {
		this.licenseLink = licenseLink;
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
		return "License [licenseName=" + licenseName + ", licenseLink=" + licenseLink + "]";
	}

}
