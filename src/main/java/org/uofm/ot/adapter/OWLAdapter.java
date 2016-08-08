package org.uofm.ot.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
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

		//final String BASE_IND_NS = "http://www.semanticweb.org/nbahulek/ontologies/2016/6/untitled-ontology-20";

		final String BASE_IND_NS = "http://med.umich.edu/cci/ontologies/domain-jnc7-hypertension-model-plus-individuals";

		OWLOntologyManager managerWC = OWLManager.createOWLOntologyManager();

		InputStream streamPayload = new ByteArrayInputStream(OWLpayload.getBytes(StandardCharsets.UTF_8));

		OWLOntology ontologyWC;
		try {
			ontologyWC = managerWC.loadOntologyFromOntologyDocument(streamPayload);


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

			String person = "Fred";
			OWLNamedIndividual personRDF = factoryWC.getOWLNamedIndividual(":"+person, pmWC);
			
			OWLReasonerFactory reasonerFactoryWC = PelletReasonerFactory.getInstance();
			OWLReasoner reasonerWC = reasonerFactoryWC.createReasoner(ontologyWC, new SimpleConfiguration());
			
			OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
			
			Set<OWLClassExpression> assertedClasses = personRDF.getTypes(ontologyWC);

			int index = 0;
			String resultString = "";
			for (OWLClass c : reasonerWC.getTypes(personRDF, true).getFlattened()) {
				System.out.println("OWLClass "+c);

				boolean asserted = assertedClasses.contains(c);
				index++;
			//	System.out.println((asserted ? String.valueOf(index) + ". asserted" : String.valueOf(index) +  ". inferred") + " class for "+person+" : " + renderer.render(c));

				resultString = resultString + (asserted ? String.valueOf(index) + ". asserted" : String.valueOf(index) +  ". inferred") + " class for "+person+" : " + renderer.render(c) + "\n";

				for (OWLClass cls : reasonerWC.getSuperClasses(c, false).getFlattened()) {
					boolean asserted1 = assertedClasses.contains(cls);
			//		System.out.println((asserted1 ? "***asserted" : "***inferred") + " super class : " + renderer.render(cls));
					resultString = resultString + (asserted1 ? "***asserted" : "***inferred") + " super class : " + renderer.render(cls) + "\n";
				}
			}
			
			result.setResult(resultString);
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
