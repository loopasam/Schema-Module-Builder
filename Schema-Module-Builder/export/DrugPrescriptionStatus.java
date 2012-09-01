package play.modules.schema.things;

import javax.persistence.Entity;

import import java.util.Date;

/**
* No documentation available :-(
* Auto-generated class - More info about this type: http://schema.org/DrugPrescriptionStatus
*/
@Entity
public class DrugPrescriptionStatus extends Model {

	/**
	* No documentation available :-(
	*/
	public MedicalCode code

	/**
	* No documentation available :-(
	*/
	public MedicalSpecialty relevantSpecialty

	/**
	* No documentation available :-(
	*/
	public Organization recognizingAuthority

	/**
	* No documentation available :-(
	*/
	public MedicalStudy study

	/**
	* No documentation available :-(
	*/
	public MedicineSystem medicineSystem

	/**
	* No documentation available :-(
	*/
	public MedicalGuideline guideline

	/**
	* No documentation available :-(
	*/
	public String alternateName

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
