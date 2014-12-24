package org.egov.pims.model;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.hibernate.validator.constraints.Length
;

// Generated Aug 8, 2008 9:23:49 AM by Hibernate Tools 3.2.0.b9




/**
 * @author Divya
 * EligCertType 
 */
@Unique(fields={"type"},id="id",tableName="EGEIS_ELIG_CERT_TYPE",columnName={"TYPE"},message="Type is unique")
public class EligCertType extends BaseModel implements java.io.Serializable 
{

	
	@Required(message="Type should not be empty")
	@Length(max=50,message="Max 50 characters are allowed for Type")
	private String type;
	@Length(max=50,message="Max 50 characters are allowed for Description")
	private String description;

	public EligCertType()
	{
		
	}
	/*public EligCertType(String type, String description)
	{
		this.type=type;
		this.description=description;
	}*/
   public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
}
