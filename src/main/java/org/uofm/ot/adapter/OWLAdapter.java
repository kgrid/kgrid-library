package org.uofm.ot.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.transferobjects.Result;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

public class OWLAdapter {
	
	public Result execute(String input, String OWLpayload) throws ObjectTellerException {
		Result result = new Result();
		
		Map<String, List<String>> inferredMap =  (Map<String, List<String>>) new HashMap<String, List<String>>();
		
		Map<String, List<String>> opInferredMap =  (Map<String, List<String>>) new HashMap<String, List<String>>();

	//	final String BASE_IND_NS = "http://www.semanticweb.org/nbahulek/ontologies/2016/6/untitled-ontology-20";

		String BASE_IND_NS = "http://med.umich.edu/cci/ontologies/domain-jnc7-hypertension-model-plus-individuals";

		OWLOntologyManager managerWC = OWLManager.createOWLOntologyManager();

		InputStream streamPayload = new ByteArrayInputStream(OWLpayload.getBytes(StandardCharsets.UTF_8));

		OWLOntology ontologyWC;
		try {
			ontologyWC = managerWC.loadOntologyFromOntologyDocument(streamPayload);

			//System.out.println("***************** "+ontologyWC.getOntologyID().getDefaultDocumentIRI().toString());
			
		//	String BASE_IND_NS = ontologyWC.getOntologyID().getDefaultDocumentIRI().toString();
			
			OWLOntologyManager ptManager = OWLManager.createOWLOntologyManager();

			InputStream streamInput = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

			OWLOntology ptOntology = ptManager.loadOntologyFromOntologyDocument(streamInput);

		//	System.out.println("Axiom count before "+ontologyWC.getAxiomCount());


			Set<OWLAxiom> axioms = ptOntology.getAxioms();

			for (OWLAxiom owlAxiom : axioms) {
				managerWC.addAxiom(ontologyWC, owlAxiom);
			}

		//	System.out.println("Axiom count after "+ontologyWC.getAxiomCount());
			OWLDataFactory factoryWC = managerWC.getOWLDataFactory();
			PrefixOWLOntologyFormat pmWC = (PrefixOWLOntologyFormat) managerWC.getOntologyFormat(ontologyWC);
			pmWC.setDefaultPrefix(BASE_IND_NS + "#");
			//get a given individual

			String person = "Charles";
			OWLNamedIndividual personRDF = factoryWC.getOWLNamedIndividual(":"+person, pmWC);
			
			OWLReasonerFactory reasonerFactoryWC = PelletReasonerFactory.getInstance();
			OWLReasoner reasonerWC = reasonerFactoryWC.createReasoner(ontologyWC, new SimpleConfiguration());
			
			OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
			
			Set<OWLClassExpression> assertedClasses = personRDF.getTypes(ontologyWC);
			
			
			
			List<OWLClass> inferredOWLClasses = new ArrayList<OWLClass>();

			
			String resultString = "";
			for (OWLClass c : reasonerWC.getTypes(personRDF, true).getFlattened()) {
				

				boolean asserted = assertedClasses.contains(c);
				if(!asserted ) {
					if( ! inferredOWLClasses.contains(c))
						inferredOWLClasses.add(c);
				}
				for (OWLClass cls : reasonerWC.getSuperClasses(c, false).getFlattened() ) {
					boolean asserted1 = assertedClasses.contains(cls);
					if(!asserted1) {
						if(inferredMap.get(renderer.render(cls)) == null ){
							ArrayList<String> list = new ArrayList<String>();
							list.add(renderer.render(c));
							inferredMap.put(renderer.render(cls),list);
						} else {
							inferredMap.get(renderer.render(cls)).add(renderer.render(c));
						}

					}
					
				}
			}
			
			
			
			
		//	NeedTreatmentConcept, TreatmentGoalConcept, DrugClass, LifestyleModificationConcept
			
			opInferredMap.put("NeedTreatmentConcept", inferredMap.get("NeedTreatmentConcept"));
			opInferredMap.put("TreatmentGoalConcept", inferredMap.get("TreatmentGoalConcept"));
			opInferredMap.put("DrugClass", inferredMap.get("DrugClass"));
			opInferredMap.put("LifestyleModificationConcept", inferredMap.get("LifestyleModificationConcept"));
			
			result.setResult(opInferredMap);
			result.setSuccess(1);

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
			result.setSuccess(0);
			result.setErrorMessage(e.getMessage());
			throw new ObjectTellerException(e);
		}


		return result;

	}

}
