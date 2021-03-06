package play.modules.schema.things;

import javax.persistence.Entity;

import import java.util.Date;

/**
* No documentation available :-(
* Auto-generated class - More info about this type: http://schema.org/DiagnosticProcedure
*/
@Entity
public class DiagnosticProcedure extends Model {

	/**
	* No documentation available :-(
	*/
	public MedicalProcedureType procedureType

	/**
	* No documentation available :-(
	*/
	public String followup

	/**
	* No documentation available :-(
	*/
	public String preparation

	/**
	* No documentation available :-(
	*/
	public String howPerformed

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

	/**
	* No documentation available :-(
	*/
	public String normalRange

	/**
	* No documentation available :-(
	*/
	public Drug affectedBy

	/**
	* No documentation available :-(
	*/
	public MedicalCondition usedToDiagnose

	/**
	* No documentation available :-(
	*/
	public MedicalDevice usesDevice

	/**
	* No documentation available :-(
	*/
	public MedicalSign signDetected
}
