package org.uofm.ot.services;


import com.complexible.pinto.Identifiable;
import com.complexible.pinto.RDFMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraGateway.ChildType;
import org.uofm.ot.fedoraGateway.FCRepoService;
import org.uofm.ot.fusekiGateway.FusekiService;
import org.uofm.ot.fusekiGateway.NamespaceConstants;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.knowledgeObject.ProvenanceLogData;
import org.uofm.ot.model.OTUser;


@Service
public class KnowledgeObjectService {

	@Autowired
	private IdService idService;

	@Autowired
	private FusekiService fusekiService;

	@Autowired
	private FCRepoService fcRepoService;

	private static final Logger logger = Logger.getLogger(KnowledgeObjectService.class);


	// We can use namespace objects (like below) or a list of namespaces instead of passing namespace
	// strings to the serializer when this pinto bug is fixed: https://github.com/stardog-union/pinto/issues/21
	//private static final Namespace OT_NAMESPACE = new SimpleNamespace("ot", "http://uofm.org/objectteller/");


	public KnowledgeObject getKnowledgeObject(ArkId arkId) throws ObjectTellerException, URISyntaxException{
		URI metadataURI = new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath());
		Metadata metadata = fetchAndDeserializeRDFData(Metadata.class, metadataURI, metadataURI.toString());

		URI citationContainerURI = new URI(metadataURI + "/" + ChildType.CITATIONS.getChildType());
		List<URI> citationURIs = fcRepoService.getChildrenURIs(citationContainerURI);
		ArrayList<Citation> citations = new ArrayList<>();
		for (URI citationURI : citationURIs) {
			citations.add(fetchAndDeserializeRDFData(Citation.class, citationURI, citationURI.toString()));
		}

		metadata.setCitations(citations);
		KnowledgeObject object = new KnowledgeObject();
		object.setMetadata(metadata);
		object.setURI(arkId.toString());
		object.setArkId(arkId);
		return object;
	}

	public KnowledgeObject editObject(KnowledgeObject newObject, ArkId arkId)
			throws ObjectTellerException, URISyntaxException {

		addOrEditMetadataToArkId(arkId, newObject.getMetadata());
		editPayload(arkId, newObject.getPayload());
		editInputMessageContent(arkId, newObject.getInputMessage());
		editOutputMessageContent(arkId, newObject.getOutputMessage());
		return getCompleteKnowledgeObject(arkId);
	}

	public void deleteObject(ArkId arkId) throws ObjectTellerException {
		try {
			fcRepoService
					.deleteFedoraResource(new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException(
					"Incorrect url syntax when trying to delete object with arkID " + arkId.getArkId() + ". "
							+ e);
		}
	}

	public List<KnowledgeObject> getKnowledgeObjects(boolean onlyPublished) throws ObjectTellerException {
		return fusekiService.getKnowledgeObjects(onlyPublished);
	}

	public KnowledgeObject getCompleteKnowledgeObject(ArkId arkId) throws ObjectTellerException, URISyntaxException {

		KnowledgeObject knowledgeObject = getKnowledgeObject(arkId);

		if (knowledgeObject != null) {

			Payload payload = getPayload(arkId);

			knowledgeObject.setPayload(payload);

			knowledgeObject.setLogData(getProvData(arkId).toString());

			knowledgeObject.setInputMessage(getInputMessageContent(arkId));

			knowledgeObject.setOutputMessage(getOutputMessageContent(arkId));
		}

		return knowledgeObject;
	}

	public String getInputMessageContent(ArkId arkId) throws ObjectTellerException {
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.INPUT.getChildType());
	}

	public String getOutputMessageContent(ArkId arkId) throws ObjectTellerException {
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.OUTPUT.getChildType());
	}

	public String getPayloadContent(String objectURI) throws ObjectTellerException {
		return fcRepoService.getObjectContent(objectURI, ChildType.PAYLOAD.getChildType());
	}

	public ProvenanceLogData getProvData(ArkId arkId) throws ObjectTellerException, URISyntaxException {
		URI provDataURI = new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath() +
				"/" + ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType());

		return  fetchAndDeserializeRDFData(ProvenanceLogData.class, provDataURI, provDataURI.toString());
	}

	public void editInputMessageContent(ArkId arkId, String inputMessage)
			throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(inputMessage, arkId, ChildType.INPUT.getChildType());
	}

	public void editOutputMessageContent(ArkId arkId, String outputMessage)
			throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(outputMessage, arkId, ChildType.OUTPUT.getChildType());
	}

	public Payload getPayload(ArkId arkId) throws ObjectTellerException, URISyntaxException {
		String payloadIRI = fcRepoService.getBaseURI() + arkId.getFedoraPath() + "/" + ChildType.PAYLOAD.getChildType();
		URI payloadURI = new URI(payloadIRI + "/fcr:metadata");
		return fetchAndDeserializeRDFData(Payload.class, payloadURI, payloadIRI);
	}

	public void editPayload(ArkId arkId, Payload payload) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(payload.getContent(), arkId, ChildType.PAYLOAD.getChildType());
		serializeAndInsertRDFData(payload, new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath() + "/" + ChildType.PAYLOAD.getChildType()));
	}

	@Deprecated
	public void patchKnowledgeObject(KnowledgeObject knowledgeObject, ArkId arkId)
			throws ObjectTellerException, URISyntaxException {
		if (knowledgeObject != null && knowledgeObject.getMetadata() != null) {
			boolean param = knowledgeObject.getMetadata().isPublished();
			togglePublishedStatus(arkId, knowledgeObject.getMetadata(), param);
		}
	}

	public boolean exists(ArkId arkId) throws ObjectTellerException {
		try {
			return fcRepoService.checkIfObjectExists(
					new URI(fcRepoService.getBaseURI() + "/" + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException(
					"Error building URI for arkID " + arkId.getArkId() + ". " + e);
		}
	}

	public void publishKnowledgeObject(ArkId arkId, boolean isToBePublished, OTUser loggedInUser)
			throws ObjectTellerException, URISyntaxException {

		KnowledgeObject ko = getKnowledgeObject(arkId);

		if (ko == null) {
			throw new ObjectTellerException("Unable to retrieve knowledge object: " + arkId.getArkId());
		}

		togglePublishedStatus(arkId, ko.getMetadata(), isToBePublished);

		String name;
		if (loggedInUser != null) {
			name = loggedInUser.getFullName();
		} else {
			name = "Anonymous User";
		}

		List<String> metadata = idService.createBasicMetadata(name, ko.getMetadata().getTitle());
		if (isToBePublished) {
			idService.publish(arkId, metadata);
		} else {
			idService.retract(arkId, metadata);
		}
	}

	private void togglePublishedStatus(ArkId arkId, Metadata metadata, boolean value)
			throws ObjectTellerException, URISyntaxException {

		metadata.setPublished(value);
		addOrEditMetadataToArkId(arkId, metadata);
	}

	public void addOrEditMetadataToArkId(ArkId arkID, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {
		URI objectURI = new URI(fcRepoService.getBaseURI() + arkID.getFedoraPath());
		addOrEditMetadataToURI(objectURI, metadata);
	}

	private void addOrEditMetadataToURI(URI objectURI, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {

		if (metadata == null) {
			return;
		}

		if (metadata.getLicense() != null) {
			IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
			metadata.getLicense().id(iri);
		}

		addCitations(metadata.getCitations(), objectURI);

		// Clearing the citations after they've all been inserted so that we don't try to insert them
		// again when the metadata object is added
		metadata.setCitations(null);

		// Need to set these dates to null for now otherwise the RDF serializer will try to edit them
		// and we're not allowed to set these values directly through a put request
		// There's currently no way to set the fields in the object to not be serialized
		// see pinto issue https://github.com/stardog-union/pinto/issues/14 for updates
		metadata.setLastModified(null);
		metadata.setCreatedOn(null);

		serializeAndInsertRDFData(metadata, objectURI);
	}

	private void addCitations(List<Citation> citations, URI objectURI)
			throws URISyntaxException, ObjectTellerException {
		if (citations != null && !citations.isEmpty()) {
			URI citationParentURI = new URI(objectURI + "/" + ChildType.CITATIONS.getChildType());

			// Rather than comparing the existing list of citations vs the ones being added and edited
			// just delete all of the citations in fedora and re-add them all
			// if this is a performance issue we can go back to looping over everything comparing existing
			// citations vs the ones being added/edited/removed.
			if (fcRepoService.checkIfObjectExists(citationParentURI)) {
				fcRepoService.deleteFedoraResource(citationParentURI);
				fcRepoService.deleteFedoraResource(new URI(citationParentURI + "/fcr:tombstone"));
			}

			fcRepoService.createContainer(objectURI, ChildType.CITATIONS.getChildType());

			for (Citation citation : citations) {

				URI citationURI = fcRepoService.createContainerWithAutoGeneratedName(citationParentURI);
				serializeAndInsertRDFData(citation, citationURI);
			}
		}
	}

	public KnowledgeObject createObject(KnowledgeObject knowledgeObject, OTUser loggedInUser,
			String libraryURL, ArkId existingArkId) throws ObjectTellerException, URISyntaxException {

		URI transactionURI = null;
		String errorMessage = null;
		if (fcRepoService.getBaseURI() != null) {

			ArkId arkId = existingArkId;
			if (arkId == null) {
				arkId = idService.mint();
			} try {
				idService.resolve(arkId);
			} catch (HttpClientErrorException e) {
				try {
					idService.create(arkId);
				} catch (HttpClientErrorException ex) {
					throw new ObjectTellerException("Invalid ark id specified for creating/editing an object. " + arkId.getArkId() + " is not valid. " + ex, ex);
				}
			}

			knowledgeObject.setArkId(arkId);

			String arkIDPath = knowledgeObject.getArkId().getFedoraPath();

			if (arkIDPath != null) {

				transactionURI = fcRepoService.createTransaction();

				URI koParentURI = fcRepoService.createContainer(transactionURI, arkIDPath);

				List<String> metadata = idService.createBasicMetadata(
						(loggedInUser != null ? loggedInUser.getFirst_name() : "AnonymousUser"),
						knowledgeObject.getMetadata().getTitle());
				URI targetURI = new URI(libraryURL + "/" + arkId.getFedoraPath());
				idService.bind(knowledgeObject, metadata, targetURI);

				URI provLogURI = fcRepoService.createContainer(koParentURI,
						ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType());

				addProvMetadataStart(provLogURI, loggedInUser);

				addOrEditMetadataToURI(koParentURI, knowledgeObject.getMetadata());

				fcRepoService.putBinary(knowledgeObject.getPayload().getContent(), koParentURI,
						ChildType.PAYLOAD.getChildType());

				fcRepoService.putBinary(knowledgeObject.getInputMessage(), koParentURI,
						ChildType.INPUT.getChildType());

				fcRepoService.putBinary(knowledgeObject.getOutputMessage(), koParentURI,
						ChildType.OUTPUT.getChildType());

				serializeAndInsertRDFData(knowledgeObject.getPayload(), new URI(koParentURI + "/Payload"));

				addProvMetadataEnd(provLogURI);

				fcRepoService.commitTransaction(transactionURI);

				logger.info("Successfully created object " + knowledgeObject);

			} else
				errorMessage = "Unable to generate unique id for the object " + knowledgeObject;

		} else
			errorMessage = "Exception: Objects can not be created until fedora server is configured.";

		if (errorMessage != null) {
			logger.error(errorMessage);
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage(errorMessage);
			if (transactionURI != null)
				fcRepoService.rollbackTransaction(transactionURI);
			throw exception;
		}
		return knowledgeObject;
	}

	private void addProvMetadataStart(URI uri, OTUser loggedInUser) throws ObjectTellerException {
		String username = loggedInUser == null ? null : loggedInUser.getFullName();
		ProvenanceLogData provLogData = new ProvenanceLogData(username,
				uri.toString(), username, uri.toString(), new Date(), null);
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void addProvMetadataEnd(URI uri) throws ObjectTellerException {
		ProvenanceLogData provLogData = new ProvenanceLogData(null,
				null, null, null, null, new Date());
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void serializeAndInsertRDFData(Identifiable data, URI objectURI) throws ObjectTellerException {
		IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
		data.id(iri);
		Model dataModel = RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().writeValue(data);
		fcRepoService.putRDFData(dataModel, objectURI);
	}

	private <T> T fetchAndDeserializeRDFData(Class<T> clazz, URI objectURI, String iri) throws ObjectTellerException{
		Model model = fcRepoService.getRDFData(objectURI);
		return RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().readValue(model, clazz, SimpleValueFactory.getInstance().createIRI(iri));
	}

}


