/**
 * 
 */
package parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import schema.Schema;
import schema.SchemaProperty;
import schema.SchemaType;

/**
 * @author Samuel Croset
 *
 */
public class Parser {

    private String pathToFile;
    private Schema schema;
    private String version;

    public Parser(String pathToFile, String version) throws OWLOntologyCreationException {
	this.version = version;
	this.pathToFile = pathToFile;
	this.schema = new Schema();
    }

    public void parse() throws IOException, ParsingException {
	FileInputStream fstream = null;
	fstream = new FileInputStream(this.pathToFile);
	DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String line =null;

	while ((line = br.readLine()) != null)   {
	    if(line.startsWith("<div itemscope itemtype=\"http://schema.org/Type\"")){
		dealWithNewType(br, line);
	    }
	    if(line.startsWith("<div itemscope itemtype=\"http://schema.org/Property\"")){
		dealWithNewProperty(br, line);
	    }
	}
	
    }


    private void dealWithNewProperty(BufferedReader br, String line) throws ParsingException, IOException {

	Pattern patternPropertyName = Pattern.compile("<div itemscope itemtype=\"http://schema.org/Property\" itemid=\"http://schema.org/(\\w+?)\">");
	Matcher matcherPropertyName = patternPropertyName.matcher(line);
	if (matcherPropertyName.find()){
	    String propertyName = matcherPropertyName.group(1);
	    SchemaProperty newProperty = new SchemaProperty(propertyName);
	    if(schema.getProperty(propertyName.substring(0, propertyName.length() - 1 )) != null){
		newProperty.setPlural(true);
	    }else{
		newProperty.setPlural(false);
	    }

	    while((line = br.readLine()) != null && !line.startsWith("</div>")){
		if(line.contains("   <link itemprop=\"")){
		    Pattern patternProperty = Pattern.compile("<link itemprop=\"(\\w+?)\" href=\"http://schema.org/(\\w+?)\"/>");
		    Matcher matcherProperty = patternProperty.matcher(line);
		    if (matcherProperty.find()){
			String propertyLimit = matcherProperty.group(1);
			String propertyTarget = matcherProperty.group(2);

			if(propertyLimit.equals("domain")){
			    SchemaType target = schema.getType(propertyTarget);
			    newProperty.getDomains().add(target);
			    target.getProperties().add(newProperty);
			}else if(propertyLimit.equals("range")){
			    SchemaType target = schema.getType(propertyTarget);
			    newProperty.getRanges().add(target);
			}else{
			    throw new ParsingException("the property has something that is not a domain or a range: " + line);
			}
		    }else{
			throw new ParsingException("A problem arose while trying to get the properties: " + line);
		    }
		}
	    }
	    schema.getProperties().add(newProperty);
	}else{
	    throw new ParsingException("There are no property linked to the URI: " + line);
	}
    }

    private void dealWithNewType(BufferedReader br, String line) throws IOException, ParsingException {
	Pattern patternType = Pattern.compile("<div itemscope itemtype=\"http://schema.org/Type\" itemid=\"http://schema.org/(\\w+?)\">");
	Matcher matcherType = patternType.matcher(line);
	if (matcherType.find()){
	    String typeName = matcherType.group(1);
	    SchemaType newType = new SchemaType(typeName);
	    while((line = br.readLine()) != null && !line.startsWith("</div>")){
		if(line.contains("   <link itemprop=\"subClassOf\"")){
		    Pattern patternSubClassOf = Pattern.compile("<link itemprop=\"subClassOf\" href=\"http://schema.org/(\\w+?)\"/>");
		    Matcher matcherSubClassOf = patternSubClassOf.matcher(line);
		    if (matcherSubClassOf.find()){
			String nameOfSuperClass = matcherSubClassOf.group(1);
			SchemaType superType = schema.getType(nameOfSuperClass);
			newType.getSuperClasses().add(superType);
		    }else{
			throw new ParsingException("A problem arose with a subclass: " + line);
		    }
		}
	    }
	    schema.getTypes().add(newType);

	}else{
	    throw new ParsingException("There are no Type linked to the URI: " + line);
	}
    }

    public Schema getSchema() {
	return schema;
    }


    public void saveSerializedClasses(String path) throws IOException {
	for (SchemaType schemaType : schema.getTypes()) {
	    File exportFile = new File(path + "/" + schemaType.getName() + ".java");
	    PrintWriter pw = new PrintWriter(exportFile);	
	    schemaType.printJavaClass(pw, this.version);
	    pw.close();
	}
    }

    public void printDistributions(String pathToSave) throws FileNotFoundException {
	HashMap<String, Integer> distribution = new HashMap<String, Integer>();
	File exportFile = new File(pathToSave);
	PrintWriter pw = new PrintWriter(exportFile);

	for (SchemaType schemaType : schema.getTypes()) {
	    for (SchemaProperty currentProperty : schemaType.getProperties()) {
		for (SchemaType currentRange : currentProperty.getRanges()) {

		    if(distribution.containsKey(currentRange.getName())){
			Integer currentCount = distribution.get(currentRange.getName());
			currentCount++;
			distribution.put(currentRange.getName(), currentCount);
		    }else{
			distribution.put(currentRange.getName(), 1);
		    }
		}
	    }
	}

	for (String key : sortByValue(distribution).keySet()) {
	    pw.append(key + ": " + distribution.get(key) + "\n");
	}

	pw.close();
    }

    public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
	List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
	Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	    public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
		return (m2.getValue()).compareTo(m1.getValue());
	    }
	});
	Map<String, Integer> result = new LinkedHashMap<String, Integer>();
	for (Map.Entry<String, Integer> entry : list) {
	    result.put(entry.getKey(), entry.getValue());
	}
	return result;
    }

    public void saveSerializedTags(String path) throws FileNotFoundException {
	for (SchemaType schemaType : schema.getTypes()) {
	    File exportFile = new File(path + "/" + schemaType.getName() + ".html");
	    PrintWriter pw = new PrintWriter(exportFile);	    
	    schemaType.printPlayTag(pw);
	    pw.close();
	}
    }

    public void getComments(String path) throws OWLOntologyCreationException {
	OWLOntologyManager man = OWLManager.createOWLOntologyManager();
	final OWLOntology ontology = man.loadOntologyFromOntologyDocument(new File(path));
	final OWLDataFactory df = man.getOWLDataFactory();

	OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ontology));


	OWLOntologyWalkerVisitor<Object> entityVisitor =  new OWLOntologyWalkerVisitor<Object>(walker) {

	    public Object visit(OWLObjectProperty objectProperty) {
		OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		for (OWLAnnotation annotation : objectProperty.getAnnotations(ontology, label)) {
		    if (annotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) annotation.getValue();
			String name = objectProperty.getIRI().toString();
			String propertyName = name.substring(18, name.length());
			String propertyNameSingular = name.substring(18, name.length() -1);
			if(schema.getProperty(propertyNameSingular) != null){
			    schema.getProperty(propertyNameSingular).setComment(val.getLiteral());
			    schema.getProperty(propertyName).setComment(val.getLiteral() + " (legacy spelling; see singular form, "+ propertyNameSingular +").");
			}else{
			    schema.getProperty(propertyName).setComment(val.getLiteral());
			}
		    }
		}
		return null;
	    }

	    public Object visit(OWLClass owlClass) {
		OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		for (OWLAnnotation annotation : owlClass.getAnnotations(ontology, label)) {
		    if (annotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) annotation.getValue();
			String name = owlClass.getIRI().toString();
			schema.getType(name.substring(18, name.length())).setComment(val.getLiteral());
		    }
		}
		return null;
	    }

	    public Object visit(OWLDataProperty dataProperty) {
		OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		for (OWLAnnotation annotation : dataProperty.getAnnotations(ontology, label)) {
		    if (annotation.getValue() instanceof OWLLiteral) {
			OWLLiteral val = (OWLLiteral) annotation.getValue();
			String name = dataProperty.getIRI().toString();
			if(schema.getProperty(name.substring(18, name.length())) == null){
			    try {
				throw new ParsingException("The data property " + name + " is not present in the microdata file. Modify the OWL file by hand.");
			    } catch (ParsingException e) {
				e.printStackTrace();
			    }
			}else{
			    String propertyName = name.substring(18, name.length());
			    String propertyNameSingular = name.substring(18, name.length() -1);
			    if(schema.getProperty(propertyNameSingular) != null){
				schema.getProperty(propertyNameSingular).setComment(val.getLiteral());
				schema.getProperty(propertyName).setComment(val.getLiteral() + " (legacy spelling; see singular form, "+ propertyNameSingular +").");
			    }else{
				schema.getProperty(propertyName).setComment(val.getLiteral());
			    }
			}
		    }
		}
		return null;
	    }


	};

	walker.walkStructure(entityVisitor);

	for (SchemaType type : schema.getTypes()) {
	    if(type.getComment() == null){
		type.setComment("No documentation available :-(");
	    }
	}

	for (SchemaProperty property : schema.getProperties()) {
	    if(property.getComment() == null){
		property.setComment("No documentation available :-(");
	    }
	}

    }


}
