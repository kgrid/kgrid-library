package org.uofm.ot.fedoraAccessLayer;

import java.util.Date;

import org.apache.jena.tdb.transaction.Transaction;
import org.apache.log4j.Logger;
import org.uofm.ot.dao.ObjectIDDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.model.ObjectId;
import org.uofm.ot.model.User;

public class CreateFedoraObjectService extends FedoraObjectService {

	private ObjectIDDAO objectIDDAO;

	public void setObjectIDDAO(ObjectIDDAO objectIDDAO) {
		this.objectIDDAO = objectIDDAO;
	}

	private static final Logger logger = Logger.getLogger(CreateFedoraObjectService.class);
	
	private static final String ID_PREFIX = "OT";


	public FedoraObject createObject(FedoraObject fedoraObject, User loggedInUser) throws ObjectTellerException {

		String transactionId = null; 
		String errorMessage = null; 
		if(baseURI!= null){

			ObjectId newObjId = objectIDDAO.retrieveNewId();

			if(newObjId != null ) {

				String uri = ID_PREFIX + newObjId.getNewObjectId();
				transactionId =  super.createTransaction();
				
				createContainer(uri , transactionId);
				createContainer(uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() , transactionId);
				addProvMetadataStart(uri,loggedInUser,transactionId);

				addHEMetadataProperties(fedoraObject, uri, transactionId);

				putBinary(fedoraObject.getPayload(), uri, ChildType.PAYLOAD.getChildType(), transactionId);

				putBinary(fedoraObject.getInputMessage(), uri, ChildType.INPUT.getChildType(), transactionId);

				putBinary(fedoraObject.getOutputMessage(), uri , ChildType.OUTPUT.getChildType(), transactionId);

				addPayloadMetadataProperties(fedoraObject.getPayloadDescriptor(), uri, transactionId);

				addProvMetadataEnd(uri, transactionId);
				
				fedoraObject.setURI(uri);
				
				logger.info("Successfully created object "+fedoraObject);
				
				super.commitTransaction(transactionId);
				
				objectIDDAO.updateNewId(newObjId);

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
		
		String objectURI ;
		if(transactionId != null)
			objectURI = baseURI + transactionId + "/" + uri;
		else
			objectURI = baseURI + uri ;
		
		String properties = FusekiConstants.PREFIX_OT +"\n "+	
				FusekiConstants.PREFIX_DC +"\n "+
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"> ot:owner  \""+fedoraObject.getOwner()+"\" ; \n"+
				" ot:description  \""+fedoraObject.getDescription()+"\" ; \n"+
				" ot:contributors  \""+fedoraObject.getContributors()+"\" ; \n"+
				" dc:title \""+fedoraObject.getTitle()+"\" ; \n"+
				" ot:keywords  \""+fedoraObject.getKeywords()+"\" ; \n"+ 
				" ot:published  \"no\" . \n"+"} ";

		sendPatchRequestForUpdatingTriples(properties, uri,transactionId);

	}

	public void addPayloadMetadataProperties(PayloadDescriptor payload, String uri, String transactionId) throws ObjectTellerException {
		
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
}
