package tests;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import parser.Parser;
import parser.ParsingException;
import schema.Schema;
import schema.SchemaProperty;
import schema.SchemaType;

/**
 * @author Samuel Croset
 *
 */
public class ParserTests {

    Schema schema;

    @Before
    public void initParser() throws IOException, ParsingException, OWLOntologyCreationException {
	Parser parser = new Parser("data/microdata.txt", "version");
	parser.parse();
	this.schema = parser.getSchema();
	parser.getComments("data/schemaorg.owl");
    }

    @Test
    public void testSchema() {
	assertEquals(395, schema.getTypes().size());
	assertEquals(468, schema.getProperties().size());
    }

    @Test
    public void testTypes() {
	SchemaType creativeWork = schema.getType("CreativeWork");
	assertEquals(1, creativeWork.getSuperClasses().size());
	SchemaType thing = schema.getType("Thing");
	assertEquals(true, creativeWork.getSuperClasses().contains(thing));

	SchemaType TherapeuticProcedure = schema.getType("TherapeuticProcedure");
	assertEquals(2, TherapeuticProcedure.getSuperClasses().size());
	
	SchemaType medicalProcedure = schema.getType("MedicalProcedure");
	assertEquals(true, TherapeuticProcedure.getSuperClasses().contains(medicalProcedure));

    }

    @Test
    public void testProperties() {
	SchemaProperty foundingDate = schema.getProperty("foundingDate");
	assertEquals(1, foundingDate.getDomains().size());
	SchemaType organization = schema.getType("Organization");
	assertEquals(true, foundingDate.getDomains().contains(organization));
	
	SchemaProperty geo = schema.getProperty("geo");
	assertEquals(2, geo.getRanges().size());
	SchemaType geoShape = schema.getType("GeoShape");
	assertEquals(true, geo.getRanges().contains(geoShape));
	
	SchemaType medicalScholarlyArticle = schema.getType("MedicalScholarlyArticle");
	assertEquals(2, medicalScholarlyArticle.getProperties().size());
	
	SchemaProperty citation = schema.getProperty("citation");
	assertEquals(true, medicalScholarlyArticle.getProperties().contains(citation));
    }
    
    @Test
    public void testComments() {
	SchemaType restaurant = schema.getType("Restaurant");
	assertEquals("A restaurant.", restaurant.getComment());
	
	SchemaProperty knows = schema.getProperty("knows");
	assertEquals("The most generic bi-directional social/work relation.", knows.getComment());
	
	SchemaProperty email = schema.getProperty("email");
	assertEquals("Email address.", email.getComment());

	SchemaProperty additionalType = schema.getProperty("additionalType");
	assertEquals("No documentation available :-(", additionalType.getComment());
	
	SchemaProperty map = schema.getProperty("map");
	assertEquals("A URL to a map of the place.", map.getComment());
	
	SchemaProperty maps = schema.getProperty("maps");
	assertEquals("A URL to a map of the place. (legacy spelling; see singular form, map).", maps.getComment());

    }

}
