package models.ontology;

import java.util.ArrayList;

import models.core.serverModels.businessObject.BusinessObjectInstance;
import models.core.serverModels.document.Document;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import play.Logger;

public class OntologyHandler {

    public static void addFlowForOntology(BusinessObjectInstance boi, Document doc, String docType){
        String bo_sap_id = boi.getBusinessObject().getSAPId();
        String boi_db_id = "" + boi.getDatabaseId();
        
        
    }
    
    
    public static ArrayList<Document> getAllDocuments(){
        ArrayList<Document> res = new ArrayList<Document>();
        OWLOntology ontology = OntologyManager.ontology;
        OWLDataFactory factory = OntologyManager.factory;
        
        OWLClass document = factory.getOWLClass(IRI.create(Settings.NS + "Document"));
        for(OWLClassExpression classy:document.getSubClasses(ontology)){
            if(!classy.isAnonymous()){
                for(OWLClassExpression subClass:classy.asOWLClass().getSubClasses(ontology)){
                    Logger.info(((OWLClass)subClass).getIRI().toString());
                    
                    for(OWLIndividual ind1:subClass.asOWLClass().getIndividuals(ontology)){
                        OWLNamedIndividual ind = (OWLNamedIndividual)ind1;
                        Document doc = new Document();
                        ArrayList<String> images = new ArrayList<String>();
                        for(OWLDataProperty dp:ontology.getDataPropertiesInSignature()){
                            if( dp.getDomains(ontology).iterator().hasNext() && !dp.getDomains(ontology).iterator().next().asOWLClass().getIRI().getFragment().equals("Document")){
                                continue;
                            }
                            
                            String frag = dp.getIRI().getFragment();
                            Logger.info(frag);
                            if(frag.equals("name") && ind.getDataPropertyValues(dp, ontology).iterator().hasNext()){
                                doc.setName(ind.getDataPropertyValues(dp, ontology).iterator().next().getLiteral());
                            }
                            else if(frag.equals("description") && ind.getDataPropertyValues(dp, ontology).iterator().hasNext()){
                                doc.setDescription(ind.getDataPropertyValues(dp, ontology).iterator().next().getLiteral());
                            }
                            else if(frag.equals("id") && ind.getDataPropertyValues(dp, ontology).iterator().hasNext()){
                                doc.setId(ind.getDataPropertyValues(dp, ontology).iterator().next().getLiteral());
                            }
                            else if(frag.equals("image") && ind.getDataPropertyValues(dp, ontology).iterator().hasNext()){
                                images.add(ind.getDataPropertyValues(dp, ontology).iterator().next().getLiteral());
                            }
                            else if(frag.equals("filepath") && ind.getDataPropertyValues(dp, ontology).iterator().hasNext()){
                                doc.setFilepath(ind.getDataPropertyValues(dp, ontology).iterator().next().getLiteral());
                            }
                        }
                        doc.setImages(images);
                        res.add(doc);
                    }
                }
            }
        }
        
        return res;
    }
    
}