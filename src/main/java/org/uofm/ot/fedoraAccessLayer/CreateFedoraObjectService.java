package org.uofm.ot.fedoraAccessLayer;

import org.apache.log4j.Logger;
import org.uofm.ot.dao.ObjectIDDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.model.User;
import org.uofm.ot.knowledgeObject.ArkId;

import java.util.Date;

public class CreateFedoraObjectService extends FedoraObjectService {

	private ObjectIDDAO objectIDDAO;

	public void setObjectIDDAO(ObjectIDDAO objectIDDAO) {
		this.objectIDDAO = objectIDDAO;
	}
	
	// TODO: Autowire ID Service

	private static final Logger logger = Logger.getLogger(CreateFedoraObjectService.class);
	



	public FedoraObject createObject(FedoraObject fedoraObject, User loggedInUser) throws ObjectTellerException {

		String transactionId = null; 
		String errorMessage = null; 
		if(baseURI!= null){

			ArkId arkId = objectIDDAO.retrieveNewId();

			fedoraObject.setArkId(arkId);

			String uri = fedoraObject.getURI();

			if(uri != null ) {

				// TODO: Mint ID here
				
				transactionId =  super.createTransaction();
				
				createContainer(uri , transactionId);
				createContainer(uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() , transactionId);
				addProvMetadataStart(uri,loggedInUser,transactionId);
				

				addHEMetadataProperties(fedoraObject, uri, transactionId);
				
				createContainer(uri+"/"+ChildType.CITATIONS.getChildType(), transactionId);
				
				if(fedoraObject.getMetadata() != null && fedoraObject.getMetadata().getCitations() !=null) {
					for (Citation citation : fedoraObject.getMetadata().getCitations()) {
						String location = createContainerWithAutoGeneratedName(uri+"/"+ChildType.CITATIONS.getChildType(), transactionId);

						citation.setCitation_id(location);

						addCitationProperties(citation, uri+"/"+ChildType.CITATIONS.getChildType()+"/"+location, transactionId);
					}
				}
				

				putBinary(fedoraObject.getPayload().getContent(), uri, ChildType.PAYLOAD.getChildType(), transactionId);

				putBinary(fedoraObject.getInputMessage(), uri, ChildType.INPUT.getChildType(), transactionId);

				putBinary(fedoraObject.getOutputMessage(), uri , ChildType.OUTPUT.getChildType(), transactionId);

				addPayloadMetadataProperties(fedoraObject.getPayload(), uri, transactionId);

				addProvMetadataEnd(uri, transactionId);
				
				logger.info("Successfully created object "+fedoraObject);
				
				super.commitTransaction(transactionId);
				
				objectIDDAO.updateNewId();

			} else
				errorMessage = "Unable to generate unique id for the object "+fedoraObject;

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

		return fedoraObject;
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


	private void addHEMetadataProperties(FedoraObject fedoraObject, String uri,String transactionId) throws ObjectTellerException {
		
		if(fedoraObject.getMetadata() != null) {
			String objectURI ;
			if(transactionId != null)
				objectURI = baseURI + transactionId + "/" + uri;
			else
				objectURI = baseURI + uri ;

			String description = fedoraObject.getMetadata().getDescription();
			if(description == null)
				description = "";
			
			String owner = fedoraObject.getMetadata().getOwner();
			if(owner == null)
				owner = "";
			
			String contributors = fedoraObject.getMetadata().getContributors();
			if(contributors == null)
				contributors = "";
			
			String keywords = fedoraObject.getMetadata().getKeywords();
			if(keywords == null)
				keywords = "";
		
			
			String properties = FusekiConstants.PREFIX_OT +"\n "+	
					FusekiConstants.PREFIX_DC +"\n "+
					FusekiConstants.PREFIX_RDF +"\n "+
					"INSERT DATA \n"+
					"{ 	<"+objectURI+"> ot:owner  \""+owner+"\" ; \n"+
					" ot:description  \""+description+"\" ; \n"+
					" ot:contributors  \""+contributors+"\" ; \n"+
					" dc:title \""+fedoraObject.getMetadata().getTitle()+"\" ; \n"+
					" ot:keywords  \""+keywords+"\" ; \n"+ 
					" rdf:type "+FusekiConstants.OT_TYPE_KNOWLEDGE_OBJECT+" ; \n"+
					" ot:arkId \""+fedoraObject.getArkId().getArkId()+"\" ; \n"+
					" ot:published  \"no\" ; \n";

			if(fedoraObject.getMetadata().getLicense() != null) {
				properties = properties + 
						" ot:licenseName  \""+fedoraObject.getMetadata().getLicense().getLicenseName()+"\" ; \n"+
						" ot:licenseLink  \""+fedoraObject.getMetadata().getLicense().getLicenseLink()+"\" ; \n" ; 
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
