package org.uofm.ot.controller;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.adapter.OWLAdapter;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.pythonAdapter.PythonAdapter;
import org.uofm.ot.transferobjects.*;

import java.util.ArrayList;
import java.util.Map;



@RestController
public class RestWSController {

	private GetFedoraObjectService getFedoraObjectService;

	private FusekiService fusekiService;


	public void setGetFedoraObjectService(GetFedoraObjectService getFedoraObjectService) {
		this.getFedoraObjectService = getFedoraObjectService;
	}



	public void setFusekiService(FusekiService fusekiService) {
		this.fusekiService = fusekiService;
	}


	@RequestMapping(value = "/rest/{objectID}/result", method = RequestMethod.POST,
			consumes = "application/owl+xml",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getOWLResult(@RequestBody String content, @PathVariable String objectID) throws ObjectTellerException {
		
		ResponseEntity<String> resultEntity = null; 
		
		Gson gson = new Gson();
		
		boolean objectExists = false;
		
		if(objectID != null ) {
			objectExists= getFedoraObjectService.checkIfObjectExists(objectID);
			if ( objectExists ) {
				String payload = getFedoraObjectService.getObjectContent(objectID, ChildType.PAYLOAD.getChildType());
				OWLAdapter adapter = new OWLAdapter();
				Result result = adapter.execute(content, payload);
				String json = gson.toJson(result);
				resultEntity = new ResponseEntity<String> (json, HttpStatus.OK);
			}
		}
		return resultEntity;
	}

	@RequestMapping(value = "/rest/getResult", method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getResult(@RequestBody String content) throws ObjectTellerException {

		Gson gson = new Gson();

		Result result = null;
		String errormessage;
		String title = null;
		boolean objectExists = false;

		InputObject io= gson.fromJson(content, InputObject.class);


		if(io.getObjectName() != null && io.getParams() != null && !io.getObjectName().isEmpty() && io.getParams().size() > 0){
			objectExists= getFedoraObjectService.checkIfObjectExists(io.getObjectName());
			if ( objectExists ) {

				FedoraObject object = fusekiService.getKnowledgeObject(new ArkId(io.getObjectName()));
				title = object.getMetadata().getTitle();

				CodeMetadata metadata = getFedoraObjectService.getCodemetadataFromXML(io.getObjectName());

				if(metadata != null){
					errormessage = verifyInput(metadata, io);
					if(errormessage == null){
						String chunk = getFedoraObjectService.getObjectContent(io.getObjectName(), ChildType.PAYLOAD.getChildType());

						Payload payload = fusekiService.getPayloadProperties(io.getObjectName());


			     			if( chunk != null) {
							if( EngineType.PYTHON.toString().equalsIgnoreCase(payload.getEngineType())){
								PythonAdapter adapter = new PythonAdapter();
								result = adapter.executeString(chunk, payload.getFunctionName(),io.getParams(),metadata.getReturntype());
							}
						} else 
							errormessage = "Unable to retrieve content of object "+io.getObjectName();
					}
				} else 
					errormessage = "Unable to convert RDF metadata for object "+io.getObjectName();
			} else 
				errormessage = "Object with name "+io.getObjectName()+" does not exist";
		} else
			errormessage = "Either object name or parameter map is missing";

		if(errormessage != null || result == null){ // errormessaage has a value
			result = new Result();
			result.setErrorMessage(errormessage);
			result.setSuccess(0);
		}

		result.setSource(title);
		String json = gson.toJson(result);

		return new ResponseEntity<String>(json, HttpStatus.OK);
	}

	private String verifyInput(CodeMetadata codeMetadata, InputObject inputObject){
		String errorMessage= null;
		if(codeMetadata.getNoOfParams() != inputObject.getParams().size()){
			errorMessage = "Number of input parameters should be "+codeMetadata.getNoOfParams()+".";
		}

		for (ParamDescription param : codeMetadata.getParams()) {
			if(!inputObject.getParams().containsKey(param.getName())){
				if(errorMessage == null)
					errorMessage= " Input parameter "+param.getName()+" is missing.";
				else
					errorMessage = errorMessage + " Input parameter "+param.getName()+" is missing.";
				break;
			}
		}

		if(errorMessage == null)
			errorMessage = verifyParameters(codeMetadata.getParams(),inputObject.getParams());

		return errorMessage;
	}

	private String verifyParameters(ArrayList<ParamDescription> list, Map<String,Object> params) {
		String error = null;
		for (ParamDescription paramDescription : list) {
			Object obj = params.get(paramDescription.getName());

			if (DataType.INT == paramDescription.getDatatype()){
				try {
					Integer value = Integer.parseInt(obj.toString());
					if(paramDescription.getMin() != null){
						if(value < paramDescription.getMin()) {
							error = " Parameter "+paramDescription.getName()+" should be of minimum value "+paramDescription.getMin();
							break;
						}
					}

					if(paramDescription.getMax() != null){
						if(value > paramDescription.getMax()) {
							error = " Parameter "+paramDescription.getName()+" should be less than maximum allowed value "+paramDescription.getMax();
							break;
						}
					}
					params.replace(paramDescription.getName(), value);
				} catch (NumberFormatException e){
					e.printStackTrace();
					error = " Parameter "+paramDescription.getName()+" should be of type INT";
					break;
				}
			} else {
				if(DataType.FLOAT == paramDescription.getDatatype()){
					try {
						Float value = Float.parseFloat(obj.toString());
						if(paramDescription.getMin() != null){
							if( value < paramDescription.getMin()){
								error = " Parameter "+paramDescription.getName()+" should be of minimum value "+paramDescription.getMin();
								break;
							}
						}

						if(paramDescription.getMax() != null){
							if(value > paramDescription.getMax()){
								error = " Parameter "+paramDescription.getName()+" should be less than maximum allowed value "+paramDescription.getMax();
								break;
							}
						}
					} catch (NumberFormatException e){
						e.printStackTrace();
						error = " Parameter "+paramDescription.getName()+" should be of type FLOAT";
						break;
					}
				} else {
					if(DataType.LONG == paramDescription.getDatatype()){
						try {
							Long value = Long.parseLong(obj.toString());
							if(paramDescription.getMin() != null){
								if( value < paramDescription.getMin()){
									error = " Parameter "+paramDescription.getName()+" should be of minimum value "+paramDescription.getMin();
									break;
								}
							}

							if(paramDescription.getMax() != null){
								if(value > paramDescription.getMax()){
									error = " Parameter "+paramDescription.getName()+" should be less than maximum allowed value "+paramDescription.getMax();
									break;
								}
							}
						} catch (NumberFormatException e){
							e.printStackTrace();
							error = " Parameter "+paramDescription.getName()+" should be of type LONG";
							break;
						}
					} 
				} 
			}
		}
		return error;
	}


}
