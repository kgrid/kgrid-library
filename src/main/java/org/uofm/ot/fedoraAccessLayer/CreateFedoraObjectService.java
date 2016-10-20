package org.uofm.ot.fedoraAccessLayer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.User;
import org.uofm.ot.services.IdService;
import org.uofm.ot.knowledgeObject.ArkId;

import java.util.Date;
import java.util.List;

public class CreateFedoraObjectService extends FedoraObjectService {
	
	
	@Autowired
	private IdService idService;

	private static final Logger logger = Logger.getLogger(CreateFedoraObjectService.class);


	public KnowledgeObject createObject(KnowledgeObject knowledgeObject, User loggedInUser, String libraryURL , ArkId existingArkId) throws ObjectTellerException {

		String transactionId = null; 
		String errorMessage = null; 
		if(baseURI!= null){

			ArkId arkId;
			if (existingArkId == null) {
				arkId = idService.mint();
			}
			else {
				arkId = existingArkId;
			}
			knowledgeObject.setArkId(arkId);
			

			String uri = knowledgeObject.getArkId().getFedoraPath();

			if(uri != null ) {
				
				transactionId =  super.createTransaction();
				
				createContainer(uri , transactionId);
				 
				String targetUrl = libraryURL + "/" + arkId.getArkId();
			
				List<String> metadata = idService.createBasicMetadata(loggedInUser.getFullName(), knowledgeObject.getMetadata().getTitle());
        
				idService.bind(knowledgeObject, metadata, targetUrl);
				
				createContainer(uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() , transactionId);
				addProvMetadataStart(uri,loggedInUser,transactionId);
				

				addHEMetadataProperties(knowledgeObject, uri, transactionId);
				
				createContainer(uri+"/"+ChildType.CITATIONS.getChildType(), transactionId);
				
				if(knowledgeObject.getMetadata() != null && knowledgeObject.getMetadata().getCitations() !=null) {
					for (Citation citation : knowledgeObject.getMetadata().getCitations()) {
						String location = createContainerWithAutoGeneratedName(uri+"/"+ChildType.CITATIONS.getChildType(), transactionId);

						citation.setCitation_id(location);

						addCitationProperties(citation, uri+"/"+ChildType.CITATIONS.getChildType()+"/"+location, transactionId);
					}
				}
				

				putBinary(knowledgeObject.getPayload().getContent(), uri, ChildType.PAYLOAD.getChildType(), transactionId);

				putBinary(knowledgeObject.getInputMessage(), uri, ChildType.INPUT.getChildType(), transactionId);

				putBinary(knowledgeObject.getOutputMessage(), uri , ChildType.OUTPUT.getChildType(), transactionId);

				addPayloadMetadataProperties(knowledgeObject.getPayload(), uri, transactionId);

				addProvMetadataEnd(uri, transactionId);
				
				logger.info("Successfully created object "+knowledgeObject);
				
				
				
				super.commitTransaction(transactionId);

			} else
				errorMessage = "Unable to generate unique id for the object "+knowledgeObject;

		} else 
			errorMessage = "Exception: Objects can not be created until fedora server is configured.";


		if(errorMessage != null) {
			logger.error(errorMessage);
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage(errorMessage);
			if(transactionId != null)
				super.rollbackTransaction(transactionId);
			throw exception;
		}

		return knowledgeObject;
	}

	private void addProvMetadataStart ( String uri, User loggedInUser, String transactionId ) throws ObjectTellerException {
		
		String objectURI ;
		if(transactionId != null)
			objectURI = baseURI + transactionId + "/" + uri;
		else
			objectURI = baseURI + uri ;
		
		
		String properties = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"> prov:isA \"http://www.w3.org/ns/prov#Entity\" ; \n"+
				" prov:wasAttributedTo  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" ; \n"+
				" prov:wasGeneratedBy  \""+ baseURI+uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() +"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(properties, uri,transactionId);

		Date today = new Date();
		String provStart = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:isA \"http://www.w3.org/ns/prov#Activity\" ; \n"+
				" prov:Used \""+baseURI+uri+"\" ; \n"+
				" prov:startedAtTime \""+ today.toString()+"\" ; \n"+
				" prov:wasAssociatedWith  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" . \n"+"} ";

		sendPatchRequestForUpdatingTriples(provStart, uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType(),transactionId);



	}

	private void addProvMetadataEnd ( String uri, String transactionId) throws ObjectTellerException {
		
		String objectURI ;
		if(transactionId != null)
			objectURI = baseURI + transactionId + "/" + uri;
		else
			objectURI = baseURI + uri ;
		
		Date today = new Date();
		String provEnd = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:endedAtTime \""+today.toString()+"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(provEnd, uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType(),transactionId);
	}


	private void addHEMetadataProperties(KnowledgeObject knowledgeObject, String uri,String transactionId) throws ObjectTellerException {
		
		if(knowledgeObject.getMetadata() != null) {
			String objectURI ;
			if(transactionId != null)
				objectURI = baseURI + transactionId + "/" + uri;
			else
				objectURI = baseURI + uri ;

			String description = knowledgeObject.getMetadata().getDescription();
			if(description == null)
				description = "";
			
			String owner = knowledgeObject.getMetadata().getOwner();
			if(owner == null)
				owner = "";
			
			String contributors = knowledgeObject.getMetadata().getContributors();
			if(contributors == null)
				contributors = "";
			
			String keywords = knowledgeObject.getMetadata().getKeywords();
			if(keywords == null)
				keywords = "";
		
			
			String properties = FusekiConstants.PREFIX_OT +"\n "+	
					FusekiConstants.PREFIX_DC +"\n "+
					FusekiConstants.PREFIX_RDF +"\n "+
					"INSERT DATA \n"+
					"{ 	<"+objectURI+"> ot:owner  \""+owner+"\" ; \n"+
					" ot:description  \""+description+"\" ; \n"+
					" ot:contributors  \""+contributors+"\" ; \n"+
					" dc:title \""+knowledgeObject.getMetadata().getTitle()+"\" ; \n"+
					" ot:keywords  \""+keywords+"\" ; \n"+ 
					" rdf:type "+FusekiConstants.OT_TYPE_KNOWLEDGE_OBJECT+" ; \n"+
					" ot:arkId \""+knowledgeObject.getArkId().getArkId()+"\" ; \n"+
					" ot:published  \"no\" ; \n";

			if(knowledgeObject.getMetadata().getLicense() != null) {
				properties = properties + 
						" ot:licenseName  \""+knowledgeObject.getMetadata().getLicense().getLicenseName()+"\" ; \n"+
						" ot:licenseLink  \""+knowledgeObject.getMetadata().getLicense().getLicenseLink()+"\" ; \n" ; 
			} 

			properties = properties + "} ";

			sendPatchRequestForUpdatingTriples(properties, uri,transactionId);
		}

	}

	public void addPayloadMetadataProperties(Payload payload, String uri, String transactionId) throws ObjectTellerException {
		
		String objectURI ;
		if(transactionId != null)
			objectURI = baseURI + transactionId + "/" + uri;
		else
			objectURI = baseURI + uri ;
		
		String properties = FusekiConstants.PREFIX_OT +"\n "+	
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:functionName   \""+payload.getFunctionName()+"\" ; \n"+
				" ot:executorType  \""+payload.getEngineType()+"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(properties, uri+"/"+ ChildType.PAYLOAD.getChildType()+"/fcr:metadata", transactionId);

	}
	
	public void addCitationProperties(Citation citation, String uri, String transactionId) throws ObjectTellerException {
		
		String objectURI ;
		if(transactionId != null)
			objectURI = baseURI + transactionId + "/" + uri;
		else
			objectURI = baseURI + uri ;
		
		String properties = FusekiConstants.PREFIX_OT +"\n "+	
				"INSERT DATA \n"+
				"{ 	<"+objectURI+">   ot:citationTitle   \""+citation.getCitation_title()+"\" ; \n"+
				" ot:citationAt \""+citation.getCitation_at()+"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(properties, uri, transactionId);

	}
	
}
