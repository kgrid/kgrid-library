package org.kgrid.library.knowledgeObject;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;
import com.complexible.pinto.impl.IdentifableImpl;
import org.openrdf.model.Resource;

@RdfsClass(value="ko:payload")
public class Payload implements Identifiable {

	private Identifiable mIdendifiable = new IdentifableImpl();

	private String content;

	private String functionName ;
	
	private String engineType;

	@Override
	public Resource id() {
		return mIdendifiable.id();
	}

	@Override
	public void id(final Resource resource) {
		mIdendifiable.id(resource);
	}

	@RdfProperty(value="ot:content")
	public String getContent() {
		return content;
	}

	@RdfProperty(value="ot:content")
	public void setContent(String content) {
		this.content = content;
	}

	@RdfProperty(value="ot:functionName")
	public String getFunctionName() {
		return functionName;
	}

	@RdfProperty(value="ot:functionName")
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@RdfProperty(value="ot:executorType")
	public String getEngineType() {
		return engineType;
	}

	@RdfProperty(value="ot:executorType")
	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	@Override
	public String toString() {
		return "Payload [content=" + content + ", functionName=" + functionName + ", engineType=" + engineType
				+ "]";
	}
}
