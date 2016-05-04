package org.uofm.ot.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.FedoraObject;
import org.uofm.ot.fedoraAccessLayer.FedoraObjectService;
import org.uofm.ot.model.User;
import org.uofm.ot.ui.util.Menu;
import org.apache.http.entity.mime.content.ContentBody; 
import org.apache.http.entity.mime.content.StringBody; 

@Controller
public class ObjectController {

	private FedoraObjectService fedoraObjectService;

	public void setFedoraObjectService(FedoraObjectService fedoraObjectService) {
		this.fedoraObjectService = fedoraObjectService;
	}

	@RequestMapping(value = "/object.{objectURI}", method = RequestMethod.GET)
	public String getObject( @PathVariable String objectURI, ModelMap model) {
		String view = "objects/objectInfo";
		FedoraObject object = fedoraObjectService.getObject(objectURI);

		model.addAttribute("fedoraObject",object);
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("ActiveSubTab", Menu.LHSingleObjectMenuOptions.SUMMARY.getName());
		model.addAttribute("PageType", "Single");
		return view;
	}

	@RequestMapping(value = "/selectObjectType", method = RequestMethod.GET)
	public String newObjectStepOne(  FedoraObject fedoraObject, BindingResult bindingResult , HttpSession session,  ModelMap model) {
		String view = "objects/selectObjectType";

		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "SelectObjectType");
		model.addAttribute("fedoraObject", new FedoraObject());
		return view;
	}

	@RequestMapping(value = "/loadPayload", method = RequestMethod.POST)
	public String newObjectStepTwo(  FedoraObject fedoraObject, BindingResult bindingResult, HttpSession session, ModelMap model) {
		String view = "objects/loadPayload";
		
		session.setAttribute("SessionParameterFedoraObject", fedoraObject);
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "LoadPayload");
		return view;
	}

	@RequestMapping(value = "/loadAPI", method = RequestMethod.POST)
	public String newObjectStepThree(FedoraObject fedoraObject, BindingResult bindingResult,   HttpSession session, ModelMap model) {
		String view = "objects/loadAPI";
		
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "LoadAPI");
		return view;
	}

	@RequestMapping(value = "/linkNewObject", method = RequestMethod.GET)
	public String newObjectStepFour(  ModelMap model) {
		String view = "objects/linkNewObject";

		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "LinkNewObject");
		return view;
	}

	@RequestMapping(value="/uploadPayload", method=RequestMethod.POST)
	public  String handleUploadPayload(
			@RequestParam("file") MultipartFile file, ModelMap model, HttpSession session) {		
		FedoraObject fedoraObject = null;
		if (!file.isEmpty()) {
			byte[] bytes;
			try {
				bytes = file.getBytes();

				String str = new String(bytes);
				fedoraObject = (FedoraObject)session.getAttribute("SessionParameterFedoraObject");
				fedoraObject.setPayload(str);
				session.setAttribute("SessionParameterFedoraObject", fedoraObject);
			} catch (IOException e) {
			//	ObjectTellerException exception = new ObjectTellerException(e);
			//	exception.setErrormessage("Unable to upload Payload file for object "+fedoraObject.getTitle());
			//	throw exception;
				e.printStackTrace();
			}

		} else {
		//	ObjectTellerException exception = new ObjectTellerException();
		//	exception.setErrormessage("You failed to upload  because the file was empty."+fedoraObject.getTitle());
		//	throw exception;
			System.out.println("You failed to upload  because the file was empty."+fedoraObject.getTitle());
		}
		model.addAttribute("fedoraObject", fedoraObject);
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "LoadPayload");
		return "objects/loadPayload";
	}

	@RequestMapping(value="/uploadFunctionDescriptor", method=RequestMethod.POST)
	public  String handleUploadFunctionDescriptor(
			@RequestParam("file") MultipartFile file, ModelMap model, HttpSession session)  {		
		FedoraObject fedoraObject = null;
		if (!file.isEmpty()) {
			byte[] bytes;
			try {
				bytes = file.getBytes();

				String str = new String(bytes);
				fedoraObject = (FedoraObject)session.getAttribute("SessionParameterFedoraObject");
				fedoraObject.setFunctionDescriptor(str);
				session.setAttribute("SessionParameterFedoraObject", fedoraObject);
			} catch (IOException e) {
				/*ObjectTellerException exception = new ObjectTellerException(e);
				exception.setErrormessage("Unable to upload Function Descriptor file for object "+fedoraObject.getTitle());
				throw exception;*/
				
				e.printStackTrace();
			}

		} else {
			/*ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage("You failed to upload  because the file was empty."+fedoraObject.getTitle());
			throw exception;*/
			
			System.out.println("You failed to upload  because the file was empty."+fedoraObject.getTitle());
		}
		model.addAttribute("fedoraObject", fedoraObject);
		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "LoadAPI");
		return "objects/loadAPI";
	}

	@RequestMapping(value="/finalize", method=RequestMethod.POST)
	public  String createObject( ModelMap model, HttpSession session) throws ObjectTellerException{		
		FedoraObject fedoraObject = null;
		fedoraObject = (FedoraObject)session.getAttribute("SessionParameterFedoraObject");

		fedoraObjectService.createObject(fedoraObject);

		model.addAttribute("ActiveTab", Menu.TopMenuOptions.OBJECTS.getName());
		model.addAttribute("PageType", "List");

		ArrayList<FedoraObject> queryObjects = fedoraObjectService.getQueryObjects();
		model.addAttribute("QueryObjects", queryObjects.size());

		ArrayList<FedoraObject> resultObjects = fedoraObjectService.getResultObjects();	
		model.addAttribute("ResultObjects", resultObjects.size());

		ArrayList<FedoraObject> knowledgeObjects = fedoraObjectService.getKnowledgeObjects();	
		model.addAttribute("KnowledgeObjects", knowledgeObjects.size());

		ArrayList<FedoraObject> tailoringObjects = fedoraObjectService.getTailoringObjects();	
		model.addAttribute("TailoringObjects", tailoringObjects.size());

		ArrayList<FedoraObject> objects = new ArrayList<FedoraObject>();
		objects.addAll(queryObjects); 
		objects.addAll(resultObjects);
		objects.addAll(knowledgeObjects);
		objects.addAll(tailoringObjects);

		model.addAttribute("objects",objects);
		model.addAttribute("TotalObjects", objects.size());

		return "login/home";

	}
}
