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
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.log4j.Logger;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.transferobjects.CodeMetadata;
import org.uofm.ot.transferobjects.DataType;
import org.uofm.ot.transferobjects.ParamDescription;

public class GetFedoraObjectService extends FedoraObjectService {
	
	private static final Logger logger = Logger.getLogger(GetFedoraObjectService.class);
	
	public boolean checkIfObjectExists(String objectId) throws ObjectTellerException{

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
			logger.error("Exception occured while verifying object id "+objectId +"."+ e.getMessage());
			exception.setErrormessage("Exception occured while verifying object id "+objectId +"."+ e.getMessage());
			throw exception;
		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while verifying object id "+objectId+"."+e.getMessage());
			exception.setErrormessage("Exception occured while verifying object id "+objectId+"."+e.getMessage());
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
				logger.error("Exception occured while retrieving object content for object "+objectId+"/"+datastremId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId+". Request status code is "+httpResponse.getStatusLine().getStatusCode());
				throw exception;
			} 

		} catch (IOException e) {
			ObjectTellerException exception = new ObjectTellerException(e);
			logger.error("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			exception.setErrormessage("Exception occured while retrieving object content for object "+objectId+"/"+datastremId + e.getMessage());
			throw exception;
		}
		return chunk;
	}
	
	public CodeMetadata getCodemetadataFromXML(String objectId) throws ObjectTellerException  {

		CodeMetadata codeMetadata = new CodeMetadata();

		Model modelInput = ModelFactory.createDefaultModel();

		String chunkInput = getObjectContent(objectId,ChildType.INPUT.getChildType());

		InputStream streamInput = new ByteArrayInputStream(chunkInput.getBytes(StandardCharsets.UTF_8));

		modelInput.read(streamInput,null);



		StmtIterator iterInput = modelInput.getResource("http://uofm.org/objectteller/inputMessage").listProperties();
		ArrayList<String> params = new ArrayList<String>();


		while (iterInput.hasNext()) {

			Statement stmt = iterInput.nextStatement(); 

			if("noofparams".equals(stmt.getPredicate().getLocalName()))
				codeMetadata.setNoOfParams(Integer.parseInt(stmt.getObject().toString()));

			if("params".equals(stmt.getPredicate().getLocalName())){
				NodeIterator props = modelInput.getSeq(stmt.getResource()).iterator();
				while(props.hasNext()){
					RDFNode st = props.nextNode();
					if(st.isLiteral())
						params.add(st.asLiteral().getString());
				}
			}
		}

		ArrayList<ParamDescription> paramList = new ArrayList<ParamDescription>();
		for (String param : params) {
			StmtIterator paramDesc = modelInput.getResource(FusekiConstants.OT_NAMESPACE+param+"/").listProperties();
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

		Model modelOutput = ModelFactory.createDefaultModel();

		String chunkOuput = getObjectContent(objectId,ChildType.OUTPUT.getChildType());

		InputStream streamOutput = new ByteArrayInputStream(chunkOuput.getBytes(StandardCharsets.UTF_8));

		modelOutput.read(streamOutput,null);

		StmtIterator iterOutput = modelOutput.getResource("http://uofm.org/objectteller/outputMessage").listProperties();

		while (iterOutput.hasNext()) {

			Statement stmt = iterOutput.nextStatement(); 

			if("returntype".equals(stmt.getPredicate().getLocalName()))
				codeMetadata.setReturntype(DataType.valueOf(stmt.getObject().toString()));

		}

		return codeMetadata;
	}

}
