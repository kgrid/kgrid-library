package org.uofm.ot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uofm.ot.adapter.OWLAdapter;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.pythonAdapter.PythonAdapter;
import org.uofm.ot.services.KnowledgeObjectService;
import org.uofm.ot.transferobjects.*;

import java.util.ArrayList;
import java.util.Map;



@RestController
public class RestWSController {

	private GetFedoraObjectService getFedoraObjectService;

	@Autowired
	KnowledgeObjectService knowledgeObjectService;

	public void setGetFedoraObjectService(GetFedoraObjectService getFedoraObjectService) {
		this.getFedoraObjectService = getFedoraObjectService;
	}

	@RequestMapping(value = "/knowledgeObject/ark:/{naan}/{name}/result", method = RequestMethod.POST,
			consumes = "application/owl+xml",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Result> getOWLResult(@RequestBody String content, ArkId arkId) throws ObjectTellerException {
		
		ResponseEntity<Result> resultEntity = null; 
		
		boolean objectExists = false;
		
		if(arkId != null ) {
			objectExists= getFedoraObjectService.checkIfObjectExists(arkId.getFedoraPath());
			if ( objectExists ) {
				String payload = getFedoraObjectService.getObjectContent(arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType());
				OWLAdapter adapter = new OWLAdapter();
				Result result = adapter.execute(content, payload);				
				resultEntity = new ResponseEntity<Result> (result, HttpStatus.OK);
			}
		}
		return resultEntity;
	}

	@RequestMapping(value = "/rest/getResult", method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@CrossOrigin
	public ResponseEntity<Result> getResult(@RequestBody InputObject io) throws ObjectTellerException {
		ArkId arkId = new ArkId(io.getObjectName());

		return getResultByArkId(io.getParams(), arkId) ;
	}

	@RequestMapping(value = "/knowledgeObject/ark:/{naan}/{name}/result", method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Result> getResultByArkId(@RequestBody Map<String,Object> params,ArkId arkId) throws ObjectTellerException {

		Result result = calculate(params, arkId);

		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}



	private String verifyInput(CodeMetadata codeMetadata, Map<String,Object> ipParams){
		String errorMessage= null;
		if(codeMetadata.getNoOfParams() != ipParams.size()){
			errorMessage = "Number of input parameters should be "+codeMetadata.getNoOfParams()+".";
		}

		for (ParamDescription param : codeMetadata.getParams()) {
			if(!ipParams.containsKey(param.getName())){
				if(errorMessage == null)
					errorMessage= " Input parameter "+param.getName()+" is missing.";
				else
					errorMessage = errorMessage + " Input parameter "+param.getName()+" is missing.";
				break;
			}
		}

		if(errorMessage == null)
			errorMessage = verifyParameters(codeMetadata.getParams(),ipParams);

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


	private Result calculate(Map<String,Object> map , ArkId arkId) throws ObjectTellerException {
		
		Result result = null;
		String errormessage;
		String title = null;
		boolean objectExists = false;
		
		String uri = arkId.getFedoraPath();

		if(uri != null && map != null && !uri.isEmpty() && map.size() > 0){
			objectExists= getFedoraObjectService.checkIfObjectExists(uri);
			if ( objectExists ) {

				FedoraObject object = knowledgeObjectService.getKnowledgeObject(new ArkId(uri));
				title = object.getMetadata().getTitle();

				CodeMetadata metadata = getFedoraObjectService.getCodemetadataFromXML(uri);

				if(metadata != null){
					errormessage = verifyInput(metadata, map);
					if(errormessage == null){
						String chunk = getFedoraObjectService.getObjectContent(uri, ChildType.PAYLOAD.getChildType());

						Payload payload = knowledgeObjectService.getPayload(new ArkId(uri));


			     			if( chunk != null) {
							if( EngineType.PYTHON.toString().equalsIgnoreCase(payload.getEngineType())){
								PythonAdapter adapter = new PythonAdapter();
								result = adapter.executeString(chunk, payload.getFunctionName(),map,metadata.getReturntype());
}
						} else 
							errormessage = "Unable to retrieve content of object with id: "+ arkId.getArkId();
					}
				} else 
					errormessage = "Unable to convert RDF metadata for object with id:"+ arkId.getArkId();
			} else 
				errormessage = "Object with id: "+ arkId.getArkId() +" does not exist";
		} else
			errormessage = "Either object id or parameter map is missing";

		if(errormessage != null || result == null){ // errormessaage has a value
			result = new Result();
			result.setErrorMessage(errormessage);
			result.setSuccess(0);
		}

		result.setSource(title);
		
		return result ; 
	}
	
}
