package play.modules.schema.things;

import javax.persistence.Entity;

import import java.util.Date;

/**
* Structured values are strings&mdash;for example, addresses&mdash;that have certain constraints on their structure.
* Auto-generated class - More info about this type: http://schema.org/StructuredValue
*/
@Entity
public class StructuredValue extends Model {

	/**
	* No documentation available :-(
	*/
	public String additionalType

	/**
	* A short description of the item.
	*/
	public String description

	/**
	* URL of the item.
	*/
	public String url

	/**
	* URL of an image of the item.
	*/
	public String image

	/**
	* The name of the item.
	*/
	public String name
}
