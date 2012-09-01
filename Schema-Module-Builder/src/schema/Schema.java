/**
 * 
 */
package schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel Croset
 *
 */
public class Schema {

    public List<SchemaType> getTypes() {
	return types;
    }
    public void setTypes(List<SchemaType> types) {
	this.types = types;
    }
    public List<SchemaProperty> getProperties() {
	return properties;
    }
    public void setProperties(List<SchemaProperty> properties) {
	this.properties = properties;
    }

    private List<SchemaType> types;
    private List<SchemaProperty> properties;


    public Schema() {
	this.types = new  ArrayList<SchemaType>();
	this.properties = new ArrayList<SchemaProperty>();
    }

    public SchemaType getType(String nameOfType) {
	for (SchemaType type : this.types) {
	    if(type.getName().equals(nameOfType)){
		return type;
	    }
	}
	return null;
    }

    public SchemaProperty getProperty(String nameOfProperty) {
	for (SchemaProperty property : this.properties) {
	    if(property.getPropertyName().equals(nameOfProperty)){
		return property;
	    }
	}
	return null;
    }

}
