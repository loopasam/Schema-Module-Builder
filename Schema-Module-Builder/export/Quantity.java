package play.modules.schema.things;

import javax.persistence.Entity;

import import java.util.Date;

/**
* Quantities such as distance, time, mass, weight, etc. Particular instances of say Mass are entities like '3 Kg' or '4 milligrams'.
* Auto-generated class - More info about this type: http://schema.org/Quantity
*/
@Entity
public class Quantity extends Model {

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
