package edu.umich.lhs.library.services;


import com.complexible.pinto.Identifiable;
import com.complexible.pinto.RDFMapper;
import edu.umich.lhs.library.fusekiGateway.FusekiService;
import edu.umich.lhs.library.knowledgeObject.ArkId;
import edu.umich.lhs.library.knowledgeObject.Citation;
import edu.umich.lhs.library.knowledgeObject.KnowledgeObject;
import edu.umich.lhs.library.model.LibraryUser;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import edu.umich.lhs.library.exception.ObjectNotFoundException;
import edu.umich.lhs.library.exception.LibraryException;
import edu.umich.lhs.library.fedoraGateway.ChildType;
import edu.umich.lhs.library.fedoraGateway.FCRepoService;
import edu.umich.lhs.library.fusekiGateway.NamespaceConstants;
import edu.umich.lhs.library.knowledgeObject.License;
import edu.umich.lhs.library.knowledgeObject.Metadata;
import edu.umich.lhs.library.knowledgeObject.Payload;
import edu.umich.lhs.library.knowledgeObject.ProvenanceLogData;
import edu.umich.lhs.library.knowledgeObject.Version;


@Service
public class KnowledgeObjectService {

	private final IdService idService;

	private final FusekiService fusekiService;

	private final FCRepoService fcRepoService;

	private final VersioningService versioningService;

	private static final Logger logger = Logger.getLogger(KnowledgeObjectService.class);

	@Autowired
	public KnowledgeObjectService(IdService idService, FusekiService fusekiService,
			FCRepoService fcRepoService, VersioningService versioningService) {
		this.idService = idService;
		this.fusekiService = fusekiService;
		this.fcRepoService = fcRepoService;
		this.versioningService = versioningService;
	}

	// We can use namespace objects (like below) or a list of namespaces instead of passing namespace
	// strings to the serializer when this pinto bug is fixed: https://github.com/stardog-union/pinto/issues/21
	//private static final Namespace OT_NAMESPACE = new SimpleNamespace("ot", "http://uofm.org/objectteller/");

	public KnowledgeObject getKnowledgeObject(ArkId arkId) throws LibraryException, URISyntaxException {
		URI metadataURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());

		try {
			return getKnowledgeObject(metadataURI);
		} catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundException("Cannot find object with id " + arkId);
		}
	}

	public KnowledgeObject getKnowledgeObject(URI uri) throws LibraryException, URISyntaxException {
		Metadata metadata;
		try {
			metadata = fetchAndDeserializeRDFData(Metadata.class, uri, uri.toString());
		} catch (NullPointerException e) {
			throw new ObjectNotFoundException("Cannot find knowledge object at url " + uri);
		}

		URI licenseURI = constructURI(uri, License.class.getSimpleName());
		License license = fetchAndDeserializeRDFData(License.class, licenseURI, licenseURI.toString());
		if(license != null) {
			metadata.setLicense(license);
		}
		URI citationContainerURI = constructURI(uri, ChildType.CITATIONS.getChildType());
		List<URI> citationURIs = fcRepoService.getChildrenURIs(citationContainerURI);
		ArrayList<Citation> citations = new ArrayList<>();
		for (URI citationURI : citationURIs) {
			citations.add(fetchAndDeserializeRDFData(Citation.class, citationURI, citationURI.toString()));
		}
		if(!citations.isEmpty()) {
			metadata.setCitations(citations);
		}
		KnowledgeObject object = new KnowledgeObject();
		object.setMetadata(metadata);
		object.setURI(metadata.getArkId());
		object.setArkId(new ArkId(metadata.getArkId()));
		return object;
	}

	public String getKnowledgeObjectJSON(ArkId arkId) throws LibraryException {
		URI objectURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		org.apache.jena.rdf.model.Model model = fcRepoService.getRdfJson(objectURI);
		StringWriter writer = new StringWriter();
		model.write(writer, "RDF/JSON");
		return writer.toString();
	}

	public List<KnowledgeObject> getKnowledgeObjects(boolean onlyPublished) throws LibraryException {
		return fusekiService.getKnowledgeObjects(onlyPublished);
	}

	public KnowledgeObject getCompleteKnowledgeObject(ArkId arkId) throws LibraryException, URISyntaxException {

		KnowledgeObject knowledgeObject = getKnowledgeObject(arkId);

		if (knowledgeObject != null) {

			knowledgeObject.setPayload(getPayload(arkId));

			knowledgeObject.setLogData(getProvData(arkId).toString());

			knowledgeObject.setInputMessage(getInputMessageContent(arkId));

			knowledgeObject.setOutputMessage(getOutputMessageContent(arkId));
		}

		return knowledgeObject;
	}

	public KnowledgeObject getCompleteKnowledgeObject(URI objectURI) throws LibraryException, URISyntaxException {

		KnowledgeObject knowledgeObject = getKnowledgeObject(objectURI);

		if (knowledgeObject != null) {

			knowledgeObject.setPayload(getPayload(constructURI(objectURI, ChildType.PAYLOAD.getChildType())));

			knowledgeObject.setLogData(getProvData(constructURI(objectURI, ChildType.LOG.getChildType(), ChildType.CREATEACTIVITY.getChildType())).toString());

			knowledgeObject.setInputMessage(getInputMessageContent(constructURI(objectURI, ChildType.INPUT.getChildType())));

			knowledgeObject.setOutputMessage(getOutputMessageContent(constructURI(objectURI, ChildType.OUTPUT.getChildType())));
		}

		return knowledgeObject;
	}

	public void deleteObject(ArkId arkId) throws LibraryException {
		fcRepoService.deleteResource(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
	}

	public String getInputMessageContent(ArkId arkId) throws LibraryException {
		return fcRepoService.getStringObjectContent(arkId.getFedoraPath(), ChildType.INPUT.getChildType());
	}

	public String getInputMessageContent(URI objectURI) throws LibraryException {
		return fcRepoService.getStringObjectContent(objectURI);
	}

	public String getOutputMessageContent(ArkId arkId) throws LibraryException {
		return fcRepoService.getStringObjectContent(arkId.getFedoraPath(), ChildType.OUTPUT.getChildType());
	}

	public String getOutputMessageContent(URI objectURI) throws LibraryException {
		return fcRepoService.getStringObjectContent(objectURI);
	}

	public String getPayloadContent(String arkId) throws LibraryException {
		return fcRepoService.getStringObjectContent(arkId, ChildType.PAYLOAD.getChildType());
	}

	public ProvenanceLogData getProvData(ArkId arkId) throws LibraryException, URISyntaxException {
		URI provDataURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(),
				ChildType.LOG.getChildType(), ChildType.CREATEACTIVITY.getChildType());
		return getProvData(provDataURI);
	}

	public ProvenanceLogData getProvData(URI objectURI) throws LibraryException, URISyntaxException {

		return fetchAndDeserializeRDFData(ProvenanceLogData.class, objectURI,	objectURI.toString());
	}

	public void editInputMessageContent(ArkId arkId, String inputMessage)
			throws LibraryException, URISyntaxException {
		fcRepoService.putBinary(inputMessage, arkId, ChildType.INPUT.getChildType());
	}

	public void editOutputMessageContent(ArkId arkId, String outputMessage)
			throws LibraryException, URISyntaxException {
		fcRepoService.putBinary(outputMessage, arkId, ChildType.OUTPUT.getChildType());
	}

	public Payload getPayload(ArkId arkId) throws LibraryException {
		URI payloadURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType());

		return getPayload(payloadURI);
	}

	public Payload getPayload(URI payloadURI) throws LibraryException {
		URI payloadMetadataURI = constructURI(payloadURI, "fcr:metadata");

		Payload payload = fetchAndDeserializeRDFData(Payload.class, payloadMetadataURI, payloadURI.toString());
		payload.setContent(fcRepoService.getStringObjectContent(payloadURI));
		return payload;
	}

	public void editPayload(ArkId arkId, Payload payload) throws LibraryException, URISyntaxException {
		fcRepoService.putBinary(payload.getContent(), arkId, ChildType.PAYLOAD.getChildType());
		// Clear the content so it doesn't appear in fedora as a triple and not a binary
		payload.setContent(null);
		serializeAndInsertRDFData(payload, constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType()));
	}

	@Deprecated
	public void patchKnowledgeObject(KnowledgeObject knowledgeObject, ArkId arkId)
			throws LibraryException, URISyntaxException {
		if (knowledgeObject != null && knowledgeObject.getMetadata() != null) {
			boolean param = !(Boolean)knowledgeObject.getMetadata().getPublished();
			togglePublishedStatus(arkId, knowledgeObject.getMetadata(), param);
		}
	}

	public boolean exists(ArkId arkId) throws LibraryException {
		return fcRepoService.checkIfObjectExists(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
	}

	public boolean exists(ArkId arkId, Version version) throws LibraryException {
		return fcRepoService.checkIfObjectExists(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), version.getFedoraVersion()));
	}

	public void publishKnowledgeObject(ArkId arkId, boolean isToBePublished, LibraryUser loggedInUser)
			throws LibraryException, URISyntaxException {

		KnowledgeObject ko = getKnowledgeObject(arkId);

		if (ko == null) {
			throw new LibraryException("Unable to retrieve knowledge object: " + arkId.getArkId());
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
			throws LibraryException, URISyntaxException {

		metadata.setPublished(value);
		addOrEditMetadataToArkId(arkId, metadata);
	}

	public void addOrEditMetadataToArkId(ArkId arkID, Metadata metadata)
			throws LibraryException, URISyntaxException {
		URI objectURI = constructURI(fcRepoService.getBaseURI(), arkID.getFedoraPath());
		addOrEditMetadataToURI(objectURI, metadata);
	}

	private void addOrEditMetadataToURI(URI objectURI, Metadata metadata)
			throws LibraryException, URISyntaxException {

		if (metadata == null) {
			return;
		}

		if (metadata.getVersion() == null || metadata.getVersion().equals("")) {
			metadata.setVersion(new Version());
		}

		License license = metadata.getLicense();
		addLicense(license, objectURI);
		metadata.setLicense(null);

		addCitations(metadata.getCitations(), objectURI);

		// Clearing the citations after they've all been inserted so that we don't try to insert them
		// again when the metadata object is added
		metadata.setCitations(null);

		// Need to set these dates to null for now otherwise the RDF serializer will try to edit them
		// and we're not allowed to set these values directly through a put request
		// There's currently no way to set the fields in the object to not be serialized
		// see pinto issue https://github.com/stardog-union/pinto/issues/14 for updates
		Date lastModified = metadata.getLastModified();
		Date createdOn = metadata.getCreatedOn();
		metadata.setLastModified(null);
		metadata.setCreatedOn(null);

		serializeAndInsertRDFData(metadata, objectURI);

		// Reset these values so that when the object is returned it has the correct data
		metadata.setLastModified(lastModified);
		metadata.setCreatedOn(createdOn);
	}

	private void addCitations(List<Citation> citations, URI objectURI)
			throws URISyntaxException, LibraryException {
		if (citations != null) {
			URI citationParentURI = constructURI(objectURI, ChildType.CITATIONS.getChildType());

			if (!fcRepoService.checkIfObjectExists(citationParentURI)) {
				fcRepoService.createContainer(objectURI, ChildType.CITATIONS.getChildType());
			} else {
				// Remove deleted citations
				List<URI> storedCitationURIs = fcRepoService.getChildrenURIs(citationParentURI);
				for (URI storedURI : storedCitationURIs) {
					Citation storedCitation = getCitation(storedURI);
					if (citations.isEmpty()) {
						fcRepoService.deleteResource(storedURI);
					} else if (!citations.contains(storedCitation)) {
						fcRepoService.deleteResource(storedURI);
					}
				}
			}

			// Add new citations, update existing ones if the submitted ones have an ID
			for (Citation citation : citations) {
				if(citation.getCitation_title() != null && citation.getCitation_at() != null) {
					String citationID;

					if(citation.getCitation_id() != null) {
						citationID = citation.getCitation_id();
					} else {
						citationID = UUID.randomUUID().toString();
						citation.setCitation_id(citationID);
						fcRepoService.createContainer(constructURI(citationParentURI, citationID));
					}
					serializeAndInsertRDFData(citation, constructURI(citationParentURI, citationID));
				}
			}
		}
	}

	private Citation getCitation(URI citationURI) throws LibraryException {
		return fetchAndDeserializeRDFData(Citation.class, citationURI, citationURI.toString());
	}

	private void addLicense(License license, URI objectURI) throws URISyntaxException, LibraryException {
		if (license != null) {
			URI licenseURI = constructURI(objectURI, license.getClass().getSimpleName());
			if(!fcRepoService.checkIfObjectExists(licenseURI))
				fcRepoService.createContainer(licenseURI);

			serializeAndInsertRDFData(license, licenseURI);
		}
	}

	public KnowledgeObject createVersion(ArkId arkId, String versionString) throws LibraryException, URISyntaxException {
		KnowledgeObject ko = getCompleteKnowledgeObject(arkId);
		Version oldVersion = new Version(ko.getMetadata().getVersion());
		Version version;
		String[] reservedWords = {"versions", "metadata", "payload", "inputmessage", "outputmessage", "logdata", "published", "unpublished"};
		if(versionString == null || versionString.equals("")) {
			if(ko.getMetadata() != null && ko.getMetadata().getVersion() != null) {
				version = new Version(ko.getMetadata().getVersion()).increment();
			} else {
				version = new Version();
			}
		} else {
			version = new Version(versionString);
			if(version.usesSemVer() && oldVersion.usesSemVer() && version.compareTo(oldVersion) < 0) {
				throw new LibraryException("Cannot create a version with a number before the current version. " +
				versionString + " is not after " + ko.getMetadata().getVersion());
			} else if (!versionString.matches("[A-Za-z][A-Za-z0-9-_.]*")) {
				throw new LibraryException("Version id must start with a letter an cannot contain any special characters besides hyphen(-) underscore(_) and period(.)");
			} else if (Arrays.asList(reservedWords).contains(versionString.toLowerCase())) {
				throw new LibraryException("Version id cannot be the word " + versionString + " as that is reserved for retrieving a subset of the knowledge object");
			}
		}
		try {
			ko.getMetadata().setVersion(version);
			editObject(ko, arkId);
			versioningService.createNewVersionSnapshot(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()),
							version.toString());
		} catch (LibraryException e) {
			ko.getMetadata().setVersion(oldVersion);
			editObject(ko, arkId);
			throw new LibraryException(e);
		}
		return ko;
	}

	public List<String> getVersions(ArkId arkId) throws LibraryException, URISyntaxException {
		Model versionModel = versioningService.listVersions(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
		List<String> versionList = new ArrayList<>();
		for (Statement statement : versionModel) {
			if(statement.getPredicate().toString().endsWith("hasVersionLabel")) {
				String versionName = statement.getObject().stringValue();
				if (new Version(versionName).usesSemVer()) {
					versionName = versionName.replace(".", "-");
				}
				versionList.add(versionName);
			}
		}
		return versionList;
	}

	public KnowledgeObject getVersionSnapshot(ArkId arkId, Version version, boolean complete) throws LibraryException, URISyntaxException {
		URI versionURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), "fcr:versions", version.toString());
		if(complete)
			return getCompleteKnowledgeObject(versionURI);
		else
			return getKnowledgeObject(versionURI);
	}

	public void rollbackToPriorVersionSnapshot(ArkId arkId, Version version) throws LibraryException, URISyntaxException {
		URI koVersionURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		versioningService.revertToPreviousVersion(koVersionURI, version.toString());
	}

	public void deleteVersion(ArkId arkId, Version version) throws LibraryException, URISyntaxException {
		URI versionURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		versioningService.deleteVersion(versionURI, version.toString());
	}

	public KnowledgeObject editObject(KnowledgeObject newObject, ArkId arkId)
			throws LibraryException, URISyntaxException {

		addOrEditMetadataToArkId(arkId, newObject.getMetadata());
		editPayload(arkId, newObject.getPayload());
		editInputMessageContent(arkId, newObject.getInputMessage());
		editOutputMessageContent(arkId, newObject.getOutputMessage());
		return getCompleteKnowledgeObject(arkId);
	}

	public KnowledgeObject createNewKnowledgeObject(KnowledgeObject knowledgeObject, LibraryUser loggedInUser)
			throws LibraryException, URISyntaxException {

		ArkId arkId = idService.mint();

		knowledgeObject.setArkId(arkId);

		return createKnowledgeObject(arkId, loggedInUser, knowledgeObject);
	}

	public KnowledgeObject copyKnowledgeObject(KnowledgeObject knowledgeObject, LibraryUser loggedInUser)
			throws LibraryException, URISyntaxException {
		ArkId arkId = knowledgeObject.getArkId();
		try {
			idService.resolve(arkId);
		} catch (HttpClientErrorException e) {
			try {
				idService.create(arkId);
			} catch (HttpClientErrorException ex) {
				throw new LibraryException(
						"Invalid ark id specified for editing an object. " + arkId.getArkId()
								+ " is not valid. " + ex, ex);
			}
		}

		return createKnowledgeObject(arkId, loggedInUser, knowledgeObject);
	}

	private KnowledgeObject createKnowledgeObject(ArkId arkId, LibraryUser loggedInUser, KnowledgeObject knowledgeObject)
			throws LibraryException, URISyntaxException {
		URI transactionURI = fcRepoService.createTransaction();
		try {
			URI koURI = fcRepoService.createContainer(constructURI(transactionURI, arkId.getFedoraPath()));

			List<String> metadata = idService.createBasicMetadata(
					(loggedInUser != null ? loggedInUser.getFirst_name() : "AnonymousUser"), knowledgeObject.getMetadata().getTitle());
			URI targetURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
			idService.bind(arkId, metadata, targetURI);

			URI provLogURI = fcRepoService.createContainer(
					constructURI(koURI, ChildType.LOG.getChildType(), ChildType.CREATEACTIVITY.getChildType()));

			addProvMetadataStart(provLogURI, loggedInUser);
			knowledgeObject.setArkId(arkId);

			if("".equals(knowledgeObject.getMetadata().getVersion()))
				knowledgeObject.getMetadata().setVersion(new Version());

			if("".equals(knowledgeObject.getMetadata().getPublished()))
				knowledgeObject.getMetadata().setPublished(false);
			addOrEditMetadataToURI(koURI, knowledgeObject.getMetadata());

			fcRepoService.putBinary(knowledgeObject.getPayload().getContent(), koURI,
					ChildType.PAYLOAD.getChildType());
			fcRepoService.putBinary(knowledgeObject.getInputMessage(), koURI,
					ChildType.INPUT.getChildType());
			fcRepoService.putBinary(knowledgeObject.getOutputMessage(), koURI,
					ChildType.OUTPUT.getChildType());
			// Clear the content so it doesn't appear in fedora as a triple and not a binary
			knowledgeObject.getPayload().setContent(null);
			serializeAndInsertRDFData(knowledgeObject.getPayload(),
					constructURI(koURI, ChildType.PAYLOAD.getChildType()));
			addProvMetadataEnd(provLogURI);

		} catch (LibraryException e) {
			fcRepoService.rollbackTransaction(transactionURI);
		}
		fcRepoService.commitTransaction(transactionURI);
		logger.info("Successfully created object " + knowledgeObject);
		return knowledgeObject;
	}

	private void addProvMetadataStart(URI uri, LibraryUser loggedInUser) throws LibraryException {
		String username = loggedInUser == null ? null : loggedInUser.getFullName();
		ProvenanceLogData provLogData = new ProvenanceLogData(username,
				uri.toString(), username, uri.toString(), new Date(), null);
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void addProvMetadataEnd(URI uri) throws LibraryException {
		ProvenanceLogData provLogData = new ProvenanceLogData(null,
				null, null, null, null, new Date());
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void serializeAndInsertRDFData(Identifiable data, URI objectURI) throws LibraryException {
		IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
		data.id(iri);
		Model dataModel = RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().writeValue(data);
		fcRepoService.putRDFData(dataModel, objectURI);
	}

	private <T> T fetchAndDeserializeRDFData(Class<T> clazz, URI objectURI, String iri) throws LibraryException {
		Model model = fcRepoService.getRDFData(objectURI);
		if(model == null) {
			return null;
		}
		return RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().readValue(model, clazz, SimpleValueFactory.getInstance().createIRI(iri));
	}

	private static URI constructURI(String... parts) {
		StringBuilder uriString = new StringBuilder("");
		URI uri = null;
		for (String part : parts) {
			if(uriString.length() < 1 || uriString.toString().endsWith("/"))
				uriString.append(part);
			else
				uriString.append("/").append(part);
		}
		try {
			uri = new URI(uriString.toString());
		} catch (URISyntaxException e) {
			logger.error("Invalid uri " + uriString);
		}
		return uri;
	}

	private static URI constructURI(URI rootURI, String... parts) {
		String[] url = new String[parts.length + 1];
		url[0] = rootURI.toString();
		System.arraycopy(parts, 0, url, 1, parts.length);
		return constructURI(url);
	}

}


