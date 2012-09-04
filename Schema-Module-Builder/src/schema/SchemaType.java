/**
 * 
 */
package schema;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Samuel Croset
 *
 */
public class SchemaType {

    private String name;
    private Set<SchemaProperty> properties;
    private Set<SchemaType> superClasses;
    private String comment;


    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getComment() {
	return comment;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<SchemaProperty> getProperties() {
	return properties;
    }

    public void setProperties(Set<SchemaProperty> properties) {
	this.properties = properties;
    }

    public Set<SchemaType> getSuperClasses() {
	return superClasses;
    }

    public void setSuperClasses(Set<SchemaType> superClasses) {
	this.superClasses = superClasses;
    }


    public SchemaType(String name) {
	this.properties = new HashSet<SchemaProperty>();
	this.superClasses = new HashSet<SchemaType>();
	this.name = name;
    }

    public void printJavaClass(PrintWriter pw, String version) {
	pw.append("package models.schema;\n\n");
	pw.append("import java.util.*;\n\n");
	pw.append("/**\n");
	pw.append("* " + this.getComment() + "\n");
	pw.append("* Auto-generated class ("+ version +") - More info about this type: http://schema.org/" + this.getName() + "\n");
	pw.append( "*/\n");

	pw.append("public class " + this.getName() + " {\n");
	Set<SchemaProperty> visited = new HashSet<SchemaProperty>();
	for (SchemaProperty currentProperty : this.getProperties()) {
	    visited.add(currentProperty);
	    printProperty(currentProperty, pw);
	}
	printSuperClassesProperties(this, pw, visited);
	pw.append("}\n");
    }

    private void printSuperClassesProperties(SchemaType schemaType, PrintWriter pw, Set<SchemaProperty> visited) {
	for (SchemaType superClass : schemaType.getSuperClasses()) {

	    for (SchemaProperty currentProperty : superClass.getProperties()) {
		if(!visited.contains(currentProperty)){
		    visited.add(currentProperty);

		    printProperty(currentProperty, pw);
		}
	    }
	    if(superClass.getSuperClasses().size() > 0){
		printSuperClassesProperties(superClass, pw, visited);
	    }
	}
    }

    //TODO hyper dirty
    private void printProperty(SchemaProperty currentProperty, PrintWriter pw) {
	boolean hasBeenConvertedInString = false;

	String listStartSymbol = "";
	String listEndSymbol = "";
	if(currentProperty.isPlural()){
	    listStartSymbol = "List<";
	    listEndSymbol = ">";
	}

	for (SchemaType currentRange : currentProperty.getRanges()) {

	    String commentedSign = "";

	    if(currentProperty.getRanges().size() == 2){
		boolean hasText = false;
		boolean hasUrl = false;
		for (SchemaType range : currentProperty.getRanges()) {
		    if(range.getName().equals("Text")){
			hasText = true;
		    }else if(range.getName().equals("URL")){
			hasUrl = true;
		    }
		}
		if(hasText && hasUrl){
		    commentedSign = "";
		}else{
		    commentedSign = "//";
		}
	    }else if(currentProperty.getRanges().size() > 2){
		commentedSign = "//";
	    }


	    String name = currentProperty.getPropertyName();

	    if(currentRange.getName().equals("Text") || currentRange.getName().equals("URL")){
		if(!hasBeenConvertedInString){
		    pw.append("\n\t/**\n");
		    pw.append("\t* " + currentProperty.getComment() + "\n");
		    pw.append( "\t*/\n");
		    pw.append("\t"+commentedSign+"public " + listStartSymbol + "String" + listEndSymbol + " " + name + ";\n");
		    hasBeenConvertedInString = true;
		}
	    }else if(currentRange.getName().equals("Number")){
		pw.append("\n\t/**\n");
		pw.append("\t* " + currentProperty.getComment() + "\n");
		pw.append( "\t*/\n");
		pw.append("\t"+commentedSign+"public " + listStartSymbol + "Integer" + listEndSymbol + " " + name + ";\n");
	    }else if(currentRange.getName().equals("Duration")){
		pw.append("\n\t/**\n");
		pw.append("\t* " + currentProperty.getComment() + "\n");
		pw.append( "\t*/\n");
		pw.append("\t"+commentedSign+"public " + listStartSymbol + "String" + listEndSymbol + " " + name + ";\n");
	    }
	    else{
		pw.append("\n\t/**\n");
		pw.append("\t* " + currentProperty.getComment() + "\n");
		pw.append( "\t*/\n");
		pw.append("\t"+commentedSign+"public " + listStartSymbol + currentRange.getName() + listEndSymbol + " " + name + ";\n");
	    }

	}
    }

    public void printPlayTag(PrintWriter pw) {
	

	for (SchemaProperty currentProperty : this.getProperties()) {
	    printTagProperty(currentProperty, pw);
	}

	Set<SchemaProperty> visited = new HashSet<SchemaProperty>();

	printSuperClassesTagProperties(this, pw, visited);

    }

    private void printSuperClassesTagProperties(SchemaType schemaType, PrintWriter pw, Set<SchemaProperty> visited) {
	for (SchemaType superClass : schemaType.getSuperClasses()) {
	    for (SchemaProperty currentProperty : superClass.getProperties()) {
		if(!visited.contains(currentProperty)){
		    visited.add(currentProperty);
		    printTagProperty(currentProperty, pw);
		}
	    }
	    if(superClass.getSuperClasses().size() > 0){
		printSuperClassesTagProperties(superClass, pw, visited);
	    }
	}
    }


    private void printTagProperty(SchemaProperty currentProperty, PrintWriter pw) {
	boolean hasBeenConvertedInString = false;
	for (SchemaType currentRange : currentProperty.getRanges()) {
	    if(!hasBeenConvertedInString){

		pw.append("\t*{" + currentProperty.getComment() +"}*\n");

		String propertyName = currentProperty.getPropertyName();
		String propertyNameSingular = currentProperty.getPropertyName();
		String singularSpacer = "_arg.";


		if(currentProperty.isPlural()){
		    propertyNameSingular = propertyName.substring(0, propertyName.length() - 1 );
		    pw.append("\t#{list items:_arg."+ propertyName +", as:'"+ propertyNameSingular +"'}\n");
		    singularSpacer = "";
		}


		if(currentRange.getName().equals("Text") || currentRange.getName().equals("URL")){
		    hasBeenConvertedInString = true;
		    pw.append("\t<span itemprop=\""+ propertyNameSingular +"\">${"+singularSpacer+""+ propertyNameSingular +"}</span>\n");
		}else if(currentRange.getName().equals("Number")){
		    pw.append("\t<span itemprop=\""+ propertyNameSingular +"\">${"+singularSpacer+""+ propertyNameSingular +"}</span>\n");
		}else if(currentRange.getName().equals("Boolean")){
		    pw.append("\t<span itemprop=\""+ propertyNameSingular +"\">${"+singularSpacer+""+ propertyNameSingular +"}</span>\n");
		}else if(currentRange.getName().equals("Duration")){
		    pw.append("\t<time itemprop=\"" + propertyNameSingular+ "\" datetime=\"${"+singularSpacer + propertyNameSingular + "}\">${"+ singularSpacer + propertyNameSingular + "}</time>\n");
		}else if(currentRange.getName().equals("Integer")){
		    pw.append("\t<span itemprop=\""+ propertyNameSingular +"\">${"+singularSpacer+""+ propertyNameSingular +"}</span>\n");
		}else if(currentRange.getName().equals("Date")){
		    pw.append("\t<time itemprop=\"" + propertyNameSingular+ "\" datetime=\"${"+singularSpacer + propertyNameSingular + ".format('yyyy-MM-dd')}\">${"+ singularSpacer + propertyNameSingular + ".format('yyyy-MM-dd')}</time>\n");
		}
		else{
		    pw.append("\t<div itemprop=\"" + propertyNameSingular + "\" itemscope itemtype=\"http://schema.org/"+currentRange.getName()+"\">\n");
		    pw.append("\t\t#{"+currentRange.getName()+" "+ singularSpacer + propertyNameSingular+" /}\n");
		    pw.append("\t</div>\n");
		}

		if(currentProperty.isPlural()){
		    pw.append("\t#{/list}\n");
		}
		pw.append("\n");

	    }
	}
    }
}
