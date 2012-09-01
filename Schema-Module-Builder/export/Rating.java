package play.modules.schema.things;

import javax.persistence.Entity;

import import java.util.Date;

/**
* The rating of the video.
* Auto-generated class - More info about this type: http://schema.org/Rating
*/
@Entity
public class Rating extends Model {

	/**
	* The lowest value allowed in this rating system. If worstRating is omitted, 1 is assumed.
	*/
	public Integer worstRating

	/**
	* The lowest value allowed in this rating system. If worstRating is omitted, 1 is assumed.
	*/
	public String worstRating

	/**
	* The rating for the content.
	*/
	public String ratingValue

	/**
	* The highest value allowed in this rating system. If bestRating is omitted, 5 is assumed.
	*/
	public Integer bestRating

	/**
	* The highest value allowed in this rating system. If bestRating is omitted, 5 is assumed.
	*/
	public String bestRating

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
