package org.uofm.ot.fedoraAccessLayer;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;

import java.util.List;

public class EditFedoraObjectService extends FedoraObjectService {

	public void editObjectMetadata(Metadata metadata,String objectURI) throws ObjectTellerException{
			
			String baseQuery =  "%s { "+"\n "+
					"<"+super.baseURI+objectURI+">  dc:title  %s;"+ "\n"+
			   "ot:contributors %s; "+ " \n"+
			   "ot:description %s; "+ " \n"+
			   "ot:owner %s; "+ " \n"+
			   "ot:keywords %s; "+ " \n"+
			   "%s ot:licenseName %s; "+  " \n"+
			   "ot:licenseLink %s . %s"+ "\n" +
			   "}"+"\n" ;
			
			String deleteClause = String.format(baseQuery, new Object [] {"DELETE","?o0","?o1","?o2","?o3","?o4","","?o5","?o6",""});
			
			String licenseName = "";
			if(metadata.getLicense() != null)
				licenseName = metadata.getLicense().getLicenseName();
			
			String licenseLink = "";
			if(metadata.getLicense() != null)
				licenseLink = metadata.getLicense().getLicenseLink();
			
			
			String insertClause = String.format(baseQuery, new Object [] {"INSERT",quote(metadata.getTitle()),quote(metadata.getContributors()),quote(metadata.getDescription()),
					quote(metadata.getOwner()),quote(metadata.getKeywords()),"",quote(licenseName),quote(licenseLink),""});
			
			String whereClause = String.format(baseQuery, new Object [] {"WHERE","?o0","?o1","?o2","?o3","?o4","OPTIONAL { <"+super.baseURI+objectURI+">","?o5","?o6","}"});
			
			String data = FusekiConstants.PREFIX_DC +"\n "+
					FusekiConstants.PREFIX_OT +"\n "
					+ deleteClause + insertClause + whereClause;
					
			
			System.out.println(data);
		
		super.sendPatchRequestForUpdatingTriples(data, objectURI,null); 
	}

	public void toggleObject(String objectURI, String value) throws ObjectTellerException {

		String query = getGetSingleParamQuery(objectURI, "published", value);

		super.sendPatchRequestForUpdatingTriples(query, objectURI, null);
	}

	public void updateArkId(String objectURI, String arkId) throws ObjectTellerException {

		String query = getGetSingleParamQuery(objectURI, "arkId", arkId);

		super.sendPatchRequestForUpdatingTriples(query, objectURI, null);
	}

	private String getGetSingleParamQuery(String objectURI, String param, String value) {
		return FusekiConstants.PREFIX_OT +"\n "+
        "	DELETE \n" +
        "	{ \n" +
        "	  <"+super.baseURI+objectURI+ ">   ot:" + param + " ?o . \n" +
        "	} \n"+
        "	INSERT \n"+
        "	{ \n"+
        "	  <"+super.baseURI+objectURI+ ">   ot:" + param + "  \"" +value+"\" . \n"+
        "	} \n"+
        "	WHERE \n"+
        "	{  \n"+
        "	 <"+super.baseURI+objectURI+ "> 	 ot:" + param + " ?o . \n" +
        "	} ";
	}

	public void editPayloadMetadata(Payload payload, String objectURI) throws ObjectTellerException{

		String data = 
			FusekiConstants.PREFIX_OT +"\n "+		
			"	DELETE \n" +
			"	{ \n" +  
			"	  <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:functionName  ?o0 . \n"+
			"	  <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:executorType  ?o1 . \n"+
			"	} \n"+
			"	INSERT \n"+
			"	{ \n"+
			"	  <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName   \""+payload.getFunctionName()+"\"  .  \n"+
			"	  <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  \""+payload.getEngineType()+"\"  .  \n"+
			"	} \n"+
			"	WHERE \n"+
			"	{  \n"+
			"	 <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName  ?o0 . \n"+
			"	 <"+super.baseURI+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  ?o1 .\n"+
			"	} ";
		
		
		super.sendPatchRequestForUpdatingTriples(data, objectURI+"/"+ ChildType.PAYLOAD.getChildType()+"/fcr:metadata", null);
		 
	} 
	
	public void editCitations(String fedoraObjectURI , List<Citation> citations) throws ObjectTellerException{

		
							for (Citation citation : citations) {
				if(citation.getCitation_id() != null) {
					String citationObject = "<"+super.baseURI+fedoraObjectURI+"/"+ ChildType.CITATIONS.getChildType() +"/"+citation.getCitation_id()+">" ;
					String data = 
							FusekiConstants.PREFIX_OT +"\n "+		
									"	DELETE \n" +
									"	{ \n" +  
									"	  "+citationObject+"  ot:citationAt  ?o0 . \n"+
									"	  "+citationObject+"  ot:citationTitle  ?o1 . \n"+
									"	} \n"+
									"	INSERT \n"+
									"	{ \n"+
									"	  "+citationObject+"  ot:citationAt   \""+citation.getCitation_at()+"\"  .  \n"+
									"	 "+citationObject+"   ot:citationTitle  \""+citation.getCitation_title()+"\"  .  \n"+
									"	} \n"+
									"	WHERE \n"+
									"	{  \n"+
									"	 "+citationObject+"   ot:citationAt  ?o0 . \n"+
									"	 "+citationObject+"   ot:citationTitle  ?o1 .\n"+
									"	} ";

					super.sendPatchRequestForUpdatingTriples(data, fedoraObjectURI+"/"+ ChildType.CITATIONS.getChildType() +"/"+citation.getCitation_id(), null);
				}
			}
	}
	
	private String quote(String str){
		return "\""+str+"\"";
	}
}
