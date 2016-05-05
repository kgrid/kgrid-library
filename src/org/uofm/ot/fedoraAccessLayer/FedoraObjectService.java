package org.uofm.ot.fedoraAccessLayer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.uofm.ot.dao.SystemConfigurationDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.model.Server_details;
import org.uofm.ot.transferobjects.CodeMetadata;
import org.uofm.ot.transferobjects.DataType;
import org.uofm.ot.transferobjects.EngineType;
import org.uofm.ot.transferobjects.ParamDescription;


public class FedoraObjectService {
	
	private SystemConfigurationDAO sysConfDao;
	
	private String baseURI;
	
	private String userName;
	
	private String password;
	

	public void setSysConfDao(SystemConfigurationDAO sysConfDao) {
		this.sysConfDao = sysConfDao;
	}


	public ArrayList<FedoraObject> getAllObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		ArrayList<FedoraObject> queryObjects = getQueryObjects();
		ArrayList<FedoraObject> knowledgeObjects = getKnowledgeObjects();
		ArrayList<FedoraObject> resultObjects = getResultObjects();
		ArrayList<FedoraObject> tailoringObjects = getTailoringObjects();
		arrayList.addAll(queryObjects);
		arrayList.addAll(knowledgeObjects);
		arrayList.addAll(resultObjects);
		arrayList.addAll(tailoringObjects);
		
		return arrayList;
	}
	
	public ArrayList<FedoraObject> getKnowledgeObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		/*FedoraObject object1 = new FedoraObject("Lung Cancer Risk Calculator", false, "spark.171", 8, ObjectType.KNOWLEDGE.toString());
		FedoraObject object2 = new FedoraObject("Lung Cancer Screening Guideline", false, "spark.193", 4, ObjectType.KNOWLEDGE.toString());
		FedoraObject object3 = new FedoraObject("Acetaminophen Drug-Drug Interactions", true, "spark.223", 12, ObjectType.KNOWLEDGE.toString());
		FedoraObject object4 = new FedoraObject("Breathing Performance Scale", false, "spark.891", 8, ObjectType.KNOWLEDGE.toString());
		FedoraObject object5 = new FedoraObject("Lung Cancer Risk Categories", true, "spark.099", 7, ObjectType.KNOWLEDGE.toString());
		FedoraObject object6 = new FedoraObject("Hazards of Smoking Cigarettes", false, "spark.241", 4, ObjectType.KNOWLEDGE.toString());
		FedoraObject object7 = new FedoraObject("Hazards of Pipe Smoking", false, "spark.242", 6, ObjectType.KNOWLEDGE.toString());
		FedoraObject object8 = new FedoraObject("Hazards of Asbestos", false, "spark.243", 9, ObjectType.KNOWLEDGE.toString());
		arrayList.add(object1);
		arrayList.add(object2);
		arrayList.add(object3);
		arrayList.add(object4);
		arrayList.add(object5);
		arrayList.add(object6);
		arrayList.add(object7);
		arrayList.add(object8);*/

		return arrayList;
	}
	
	public ArrayList<FedoraObject> getQueryObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		/*FedoraObject object1 = new FedoraObject("Hazards of Cigar Smoking", false, "spark.224", 8, ObjectType.QUERY.toString());
		FedoraObject object2 = new FedoraObject("Smoking Cessation Therapeutic Guideline", false, "spark.303", 11, ObjectType.QUERY.toString());
		arrayList.add(object1);
		arrayList.add(object2);*/

		return arrayList;
	}
	public ArrayList<FedoraObject> getResultObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		/*FedoraObject object1 = new FedoraObject("Smoking Cessation Patient Education Course", false, "spark.304", 9, ObjectType.RESULT.toString());
		FedoraObject object2 = new FedoraObject("Varenicline Drug-Drug Interarctions", true, "spark.305", 11, ObjectType.RESULT.toString());
		FedoraObject object3 = new FedoraObject("Asthma Assessment Tool", false, "spark.306", 4, ObjectType.RESULT.toString());
		FedoraObject object4 = new FedoraObject("Lung Cancer Screening Harm Model", false, "spark.111", 7, ObjectType.RESULT.toString());
		arrayList.add(object1);
		arrayList.add(object2);
		arrayList.add(object3);
		arrayList.add(object4);*/
		return arrayList;
	}
	
	public ArrayList<FedoraObject> getTailoringObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		return arrayList;
	}
	
	public FedoraObject getObject(String URI){
		FedoraObject object1 = new FedoraObject();
		object1.setTitle("Hazards of Cigar Smoking");
		object1.setPublished(false);
		object1.setURI("spark.224");
		object1.setObjectType(ObjectType.QUERY.toString());
		return object1;
		
		
		
	}
	
	public boolean createObject(FedoraObject fedoraObject) throws ObjectTellerException {
		configureBaseURI();
		boolean success = false;
		if(baseURI!= null){
			HttpClient httpClient = new DefaultHttpClient();

			HttpPut httpPutRequest = new HttpPut(baseURI+fedoraObject.getTitle());
			httpPutRequest.addHeader(BasicScheme.authenticate(
					 new UsernamePasswordCredentials(userName, password),
					 "UTF-8", false));
			try {
				HttpResponse httpResponse = httpClient.execute(httpPutRequest);

				if(httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 201) {
					if(addPropertiesToObject(fedoraObject)) {
						if(createBinary(fedoraObject.getPayload(), fedoraObject.getTitle(), "Payload")){
							success= createBinary(fedoraObject.getFunctionDescriptor(), fedoraObject.getTitle(), "Metadata");
							if(success != true){
								ObjectTellerException exception = new ObjectTellerException();
								exception.setErrormessage("Exception occured while creating metdada binary for object "+fedoraObject.getTitle());
								throw exception;
							}

						} else {
							ObjectTellerException exception = new ObjectTellerException();
							exception.setErrormessage("Exception occured while creating payload binary for object "+fedoraObject.getTitle());
							throw exception;

						}
					} else {
						ObjectTellerException exception = new ObjectTellerException();
						exception.setErrormessage("Exception occured while creating properties (triples) for thr object  "+fedoraObject.getTitle());
						throw exception;

					}

				} else {
					ObjectTellerException exception = new ObjectTellerException();
					exception.setErrormessage("Exception occured while creating the object "+fedoraObject.getTitle()+".");
					throw exception;
				}
			} catch (IOException e) {
				ObjectTellerException exception = new ObjectTellerException(e);
				exception.setErrormessage("Exception occured while creating the object "+fedoraObject.getTitle()+"."+e.getMessage());
				throw exception;
			}
		} else {
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage("Exception: Objects can not be created until fedora server is configured.");
			throw exception;
		}
		
		return success;
	}
	
	private boolean createBinary(String binary, String objIdentifier, String type) throws ObjectTellerException {
		
		HttpPut httpPutRequestPayload = new HttpPut(baseURI + objIdentifier + "/" + type);
		httpPutRequestPayload.addHeader(BasicScheme.authenticate(
				 new UsernamePasswordCredentials(userName, password),
				 "UTF-8", false));
		
		HttpClient httpClient1 = new DefaultHttpClient();
		
		HttpResponse httpResponse1;
		HttpResponse response;
		
		boolean success = false;
		try {
			StringEntity entity = new StringEntity(binary);
			httpPutRequestPayload.setEntity(entity );
			response = httpClient1.execute(httpPutRequestPayload) ;
			if (response != null) {
				if(response.getStatusLine().getStatusCode() == 201) {
					success = true;
				}
			}

		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while creating binary of type"+type+" for object "+objIdentifier+"."+e.getMessage());
			throw exception;
		}
		return success;
	}
	
	private void configureBaseURI(){
		Server_details configuration =sysConfDao.getFedoraServerConfiguration();
		if(configuration != null){
			baseURI = configuration.getComplete_url() ;
			userName = configuration.getSvr_name();
			password= configuration.getSvr_passwd();
			
			baseURI = "http://localhost:8080/fcrepo/rest/";
			userName  = "fedoraAdmin";
			password = "secret3" ;
		} else{
			baseURI = null;
		}
	}
	
	private boolean addPropertiesToObject(FedoraObject fedoraObject) throws ObjectTellerException {
		HttpPatch httpPatch = new HttpPatch(baseURI + fedoraObject.getTitle() );
		httpPatch.addHeader(BasicScheme.authenticate(
				 new UsernamePasswordCredentials(userName, password),
				 "UTF-8", false));
		
		boolean success = false;
		
		HttpClient client = new DefaultHttpClient() ;		
		HttpResponse response;
		String properties = "PREFIX ot: <http://uofm.org/objectteller/> \n"+
			"INSERT DATA \n"+
			"{ 	<"+baseURI+fedoraObject.getTitle()+"> ot:owner  \""+fedoraObject.getOwner()+"\" ; \n"+
												 " ot:description  \""+fedoraObject.getDescription()+"\" ; \n"+
												 " ot:contributors  \""+fedoraObject.getContributors()+"\" ; \n"+
												 " ot:type  \""+fedoraObject.getObjectType()+"\" ; \n"+
												 " ot:keywords  \""+fedoraObject.getKeywords()+"\" . \n"+ "} ";
			
		try {
			 InputStream requestEntity = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
				
			httpPatch.setEntity(new InputStreamEntity(requestEntity));
			response = client.execute(httpPatch);	
			if(response != null) {
				if(response.getStatusLine().getStatusCode() == 204)
					success = true;
			}
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while adding properties (triples) for object "+fedoraObject.getTitle());
			throw exception;
		}
	
		return success;
		
	} 

	public boolean checkIfObjectExists(String objectId) throws ObjectTellerException{

		if(baseURI == null)
			configureBaseURI();
		
		boolean result = false;

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/");
		httpGetRequest.addHeader(BasicScheme.authenticate(
				 new UsernamePasswordCredentials(userName, password),
				 "UTF-8", false));

		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			if ( 200 == httpResponse.getStatusLine().getStatusCode())
				result = true;
		} catch (ClientProtocolException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while verifying object name "+objectId +"."+ e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while verifying object name "+objectId+"."+e.getMessage());
			throw exception;
		}
		return result;
	}

	public String getObjectContent(String objectId, String datastremId) throws ObjectTellerException  {

		HttpClient httpClient = new DefaultHttpClient();

		

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/"+datastremId+"/");
		httpGetRequest.addHeader(BasicScheme.authenticate(
				 new UsernamePasswordCredentials(userName, password),
				 "UTF-8", false));

		String chunk = null;
		HttpResponse httpResponse;
		
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			HttpEntity entity = httpResponse.getEntity();
			if(httpResponse.getStatusLine().getStatusCode() == 200){

				byte[] buffer = new byte[4096];
				if (entity != null) {
					InputStream inputStream = entity.getContent();
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);

					while ((bytesRead = bis.read(buffer)) != -1) {
						if(chunk == null)
							chunk = new String(buffer, 0, bytesRead);
						else
							chunk = chunk + new String(buffer, 0, bytesRead);
						
					}
					
					
				}
			} else {
				ObjectTellerException exception = new ObjectTellerException();
				exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				throw exception;
			} 

		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			throw exception;
		}
		return chunk;
	}

	public CodeMetadata getCodemetadataFromXML(String objectId, String datastremId) throws ObjectTellerException  {
		
		CodeMetadata codeMetadata = new CodeMetadata();
			
		Model model = ModelFactory.createDefaultModel();
		

		HttpGet httpGetRequest = new HttpGet(baseURI+objectId+"/"+datastremId+"/");
		httpGetRequest.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(userName, password),
				"UTF-8", false));

		HttpClient httpClient = new DefaultHttpClient();
		String chunk = null;
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGetRequest);
			HttpEntity entity = httpResponse.getEntity();

			if(httpResponse.getStatusLine().getStatusCode() == 200){

				byte[] buffer = new byte[4096];
				if (entity != null) {
					InputStream in = entity.getContent();
					BufferedInputStream bis = new BufferedInputStream(in);
					int bytesRead = 0;
					while ((bytesRead = bis.read(buffer)) != -1) {
						if(chunk == null)
							chunk = new String(buffer, 0, bytesRead);
						else
							chunk = chunk + new String(buffer, 0, bytesRead);
						
					}
				
					
					InputStream stream = new ByteArrayInputStream(chunk.getBytes(StandardCharsets.UTF_8));
					
					model.read(stream,null);

					StmtIterator iter = model.getResource(baseURI+objectId).listProperties();
					ArrayList<String> params = new ArrayList<String>();


					while (iter.hasNext()) {
		
						Statement stmt = iter.nextStatement(); 
						
						if("functionname".equals(stmt.getPredicate().getLocalName()))
							codeMetadata.setFunctionName(stmt.getObject().toString());
						if("enginetype".equals(stmt.getPredicate().getLocalName()))
							codeMetadata.setEngineType(EngineType.valueOf(stmt.getObject().toString()));
						if("noofparams".equals(stmt.getPredicate().getLocalName()))
							codeMetadata.setNoOfParams(Integer.parseInt(stmt.getObject().toString()));

						if("returntype".equals(stmt.getPredicate().getLocalName()))
							codeMetadata.setReturntype(DataType.valueOf(stmt.getObject().toString()));


						if("executionDS".equals(stmt.getPredicate().getLocalName()))
							codeMetadata.setExecutionDS(stmt.getObject().toString());


						if("params".equals(stmt.getPredicate().getLocalName())){
							NodeIterator props = model.getSeq(stmt.getResource()).iterator();
							while(props.hasNext()){
								RDFNode st = props.nextNode();
								if(st.isLiteral())
									params.add(st.asLiteral().getString());
							}
						}
					}

					ArrayList<ParamDescription> paramList = new ArrayList<ParamDescription>();
					for (String param : params) {
						StmtIterator paramDesc = model.getResource("http://www.objectteller.codeparams/"+param+"/").listProperties();
						ParamDescription description = new ParamDescription();
						description.setName(param);
						while(paramDesc.hasNext()){
							Statement stmt = paramDesc.nextStatement(); 
							if("datatype".equals(stmt.getPredicate().getLocalName()))
								description.setDatatype(DataType.valueOf(stmt.getObject().toString()));
							if("min".equals(stmt.getPredicate().getLocalName()))
								description.setMin(Integer.parseInt(stmt.getObject().toString()));
							if("max".equals(stmt.getPredicate().getLocalName()))
								description.setMax(Integer.parseInt(stmt.getObject().toString()));
						}
						paramList.add(description);
					}

					codeMetadata.setParams(paramList);
				}
			}
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			throw exception;
		}
		return codeMetadata;
	}

}
