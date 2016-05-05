package org.uofm.ot.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.FedoraObjectService;
import org.uofm.ot.pythonAdapter.PythonAdapter;
import org.uofm.ot.transferobjects.InputObject;
import org.uofm.ot.transferobjects.Result;

import org.uofm.ot.transferobjects.ParamDescription;
import org.uofm.ot.transferobjects.CodeMetadata;
import org.uofm.ot.transferobjects.DataType;
import org.uofm.ot.transferobjects.EngineType;

import com.google.gson.Gson;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;



@RestController
public class RestWSController {
	
	private FedoraObjectService fedoraObjectService;

	public void setFedoraObjectService(FedoraObjectService fedoraObjectService) {
		this.fedoraObjectService = fedoraObjectService;
	}

	@RequestMapping(value = "/rest/getResult", method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> calculateRisk(@RequestBody String content) throws ObjectTellerException {
		
		Gson gson = new Gson();

		Result result = null;
		String errormessage = null;
		boolean objectExists = false;
		
		InputObject io= gson.fromJson(content, InputObject.class);
		
		
		if(io.getObjectName() != null && io.getParams() != null && !io.getObjectName().isEmpty() && io.getParams().size() > 0){
			objectExists= fedoraObjectService.checkIfObjectExists(io.getObjectName());
			if ( objectExists ) {
				
				CodeMetadata metadata = fedoraObjectService.getCodemetadataFromXML(io.getObjectName(), "Metadata");

				if(metadata != null){
					errormessage = verifyInput(metadata, io);
					if(errormessage == null){
						String chunk = fedoraObjectService.getObjectContent(io.getObjectName(), "Payload");
					
						if( chunk != null) {
							if(metadata.getEngineType() == EngineType.PYTHON){
								PythonAdapter adapter = new PythonAdapter();
								result = adapter.executeString(chunk, metadata.getFunctionName(),io.getParams(),metadata.getReturntype());
							}
						} else 
							errormessage = "Unable to retrieve content of object "+io.getObjectName()+"/Payload";
					}
				} else 
					errormessage = "Unable to convert RDF metadata for object "+io.getObjectName()+" and Datastreem RDFMetadata";
			} else 
				errormessage = "Object with name "+io.getObjectName()+" does not exist";
		} else
			errormessage = "Either object name or parameter map is missing";

		if(errormessage != null){
			result = new Result();
			result.setErrorMessage(errormessage);
			result.setSuccess(0);
		}

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
				}
			}
		}
		return error;
	}
	
	
}
