/**
 * 
 */
package schema;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Samuel Croset
 *
 */
public class SchemaProperty {
    
    private Set<SchemaType> domains;
    private Set<SchemaType> ranges;
    private String propertyName;
    private String comment;
    private boolean isPlural;
    
    public void setPlural(boolean isPlural) {
	this.isPlural = isPlural;
    }
    public boolean isPlural() {
	return isPlural;
    }
    public void setComment(String comment) {
	this.comment = comment;
    }
    public String getComment() {
	return comment;
    }
    
    public SchemaProperty(String propertyName) {
	this.propertyName = propertyName;
	this.domains = new HashSet<SchemaType>();
	this.ranges = new HashSet<SchemaType>();
    }
    
    public Set<SchemaType> getDomains() {
        return domains;
    }
    public void setDomains(HashSet<SchemaType> domains) {
        this.domains = domains;
    }
    public Set<SchemaType> getRanges() {
        return ranges;
    }
    public void setRanges(HashSet<SchemaType> ranges) {
        this.ranges = ranges;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
