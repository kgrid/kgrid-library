package org.uofm.ot.fedoraAccessLayer;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;

public class EditFedoraObjectService extends FedoraObjectService {

	public void editObjectMetadata(FedoraObject fedoraObject) throws ObjectTellerException{
		String data = 
			FusekiConstants.PREFIX_DC +"\n "+
			FusekiConstants.PREFIX_OT +"\n "+		
			"	DELETE \n" +
			"	{ \n" +  
			"	  <"+super.baseURI+fedoraObject.getURI()+">   dc:title  ?o0. \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:contributors ?o1. \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:description ?o2. \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">	  ot:owner ?o3 . \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:keywords ?o4 . \n"+
			"	} \n"+
			"	INSERT \n"+
			"	{ \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   dc:title   \""+fedoraObject.getTitle()+"\"  .  \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:contributors  \""+fedoraObject.getContributors()+"\"  .  \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:description   \""+fedoraObject.getDescription()+"\"  . \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:owner    \""+fedoraObject.getOwner()+"\"  .  \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+">   ot:keywords  \""+fedoraObject.getKeywords()+"\" . \n"+
			"	} \n"+
			"	WHERE \n"+
			"	{  \n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+">   dc:title  ?o0 . \n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+">   ot:contributors ?o1 .\n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+">   ot:description ?o2 .\n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+">	 ot:owner ?o3 .\n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+"> 	 ot:keywords ?o4 . \n"+
			"	} ";
		
		super.sendPatchRequestForUpdatingTriples(data, fedoraObject.getURI()); 
	} 
	
	public void toggleObject(String objectURI, String param) throws ObjectTellerException {

		String data = 
			FusekiConstants.PREFIX_OT +"\n "+		
			"	DELETE \n" +
			"	{ \n" +  
			"	  <"+super.baseURI+objectURI+">   ot:published ?o . \n"+
			"	} \n"+
			"	INSERT \n"+
			"	{ \n"+
			"	  <"+super.baseURI+objectURI+">   ot:published  \""+param+"\" . \n"+
			"	} \n"+
			"	WHERE \n"+
			"	{  \n"+
			"	 <"+super.baseURI+objectURI+"> 	 ot:published ?o . \n"+
			"	} ";
	
		super.sendPatchRequestForUpdatingTriples(data, objectURI); 
	} 
	
	public void editPayloadMetadata(FedoraObject fedoraObject) throws ObjectTellerException{

		String data = 
			FusekiConstants.PREFIX_OT +"\n "+		
			"	DELETE \n" +
			"	{ \n" +  
			"	  <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:functionName  ?o0 . \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:executorType  ?o1 . \n"+
			"	} \n"+
			"	INSERT \n"+
			"	{ \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName   \""+fedoraObject.getPayloadDescriptor().getFunctionName()+"\"  .  \n"+
			"	  <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  \""+fedoraObject.getPayloadDescriptor().getEngineType()+"\"  .  \n"+
			"	} \n"+
			"	WHERE \n"+
			"	{  \n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName  ?o0 . \n"+
			"	 <"+super.baseURI+fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  ?o1 .\n"+
			"	} ";
		
		
		super.sendPatchRequestForUpdatingTriples(data, fedoraObject.getURI()+"/"+ ChildType.PAYLOAD.getChildType()+"/fcr:metadata");
		 
	} 
	
	
}
