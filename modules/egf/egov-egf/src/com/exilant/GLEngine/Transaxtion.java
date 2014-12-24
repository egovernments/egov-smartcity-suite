//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\Transaxtion.java

package com.exilant.GLEngine;

import java.util.*;


public class Transaxtion
{

   /**
    * gl Code whose transaction is to be recorded
    */
	protected String glCode;

   /**
    * Name of the gl Code
    */
	protected String glName;

   /**
    * Amount to be debited from the glCode
    */
	protected String drAmount;

   /**
    * Amount to be credited for this GL Code
    */
	protected String crAmount;

   /**
    * Collection of Transaxtion class
    */

	protected String voucherLineId;
	protected String voucherDetailId;

	protected String voucherHeaderId;

	protected String narration="";

	protected ArrayList transaxtionParameters ;

	protected String functionId;

	protected String functionaryId;
	protected String schemeId;
	protected String subSchemeId;
	protected String field;
	protected String asOnDate;
	protected Long billId;


	

	/**
	 * @return the billId
	 */
	public Long getBillId() {
		return billId;
	}


	/**
	 * @param billId the billId to set
	 */
	public void setBillId(Long billId) {
		this.billId = billId;
	}


	/**
	 * @return Returns the asOnDate.
	 */
	public String getAsOnDate() {
		return asOnDate;
	}


	/**
	 * @param asOnDate The asOnDate to set.
	 */
	public void setAsOnDate(String asOnDate) {
		this.asOnDate = asOnDate;
	}


	/**
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
	}


	/**
	 * @param field The field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}


	/**
	 * @return Returns the functionaryId.
	 */
	public String getFunctionaryId() {
		return functionaryId;
	}


	/**
	 * @param functionaryId The functionaryId to set.
	 */
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}


	/**
	 * @return Returns the schemeId.
	 */
	public String getSchemeId() {
		return schemeId;
	}


	/**
	 * @param schemeId The schemeId to set.
	 */
	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}


	/**
	 * @return Returns the subSchemeId.
	 */
	public String getSubSchemeId() {
		return subSchemeId;
	}


	/**
	 * @param subSchemeId The subSchemeId to set.
	 */
	public void setSubSchemeId(String subSchemeId) {
		this.subSchemeId = subSchemeId;
	}


	//protected String functionId1;
	/**
    * @roseuid 41E26E230326
    */
   public Transaxtion()
   {

   }


   /**
    * Access method for the glCode property.
    *
    * @return   the current value of the glCode property
    */
   public String getGlCode()
   {
      return glCode;
   }

   /**
    * Sets the value of the glCode property.
    *
    * @param aGlCode the new value of the glCode property
    */
   public void setGlCode(String aGlCode)
   {
      glCode = aGlCode;
   }

   /**
    * Access method for the glName property.
    *
    * @return   the current value of the glName property
    */
   public String getGlName()
   {
      return glName;
   }

   /**
    * Sets the value of the glName property.
    *
    * @param aGlName the new value of the glName property
    */
   public void setGlName(String aGlName)
   {
      glName = aGlName;
   }

   /**
    * Access method for the drAmount property.
    *
    * @return   the current value of the drAmount property
    */
   public String getDrAmount()
   {
      return drAmount;
   }

   /**
    * Sets the value of the drAmount property.
    *
    * @param aDrAmount the new value of the drAmount property
    */
   public void setDrAmount(String aDrAmount)
   {
      drAmount = aDrAmount;
   }

   /**
    * Access method for the crAmount property.
    *
    * @return   the current value of the crAmount property
    */
   public String getCrAmount()
   {
      return crAmount;
   }

   /**
    * Sets the value of the crAmount property.
    *
    * @param aCrAmount the new value of the crAmount property
    */
   public void setCrAmount(String aCrAmount)
   {
      crAmount = aCrAmount;
   }

   public ArrayList getTransaxtionParam()
   {
      return transaxtionParameters;
   }
   public String getVoucherDetailId()
   {
      return voucherDetailId;
   }
   public void setVoucherDetailId(String avoucherDetailId)
   {
   		voucherDetailId = avoucherDetailId;
   }

   public String getVoucherLineId()
   {
      return voucherLineId;
   }

   public String getVoucherHeaderId()
   {
      return voucherHeaderId;
   }
   public String getNarration()
   {
      return narration;
   }
   public void setVoucherLineId(String avoucherLineId)
   {
   		voucherLineId = avoucherLineId;
   }
   public void setTransaxtionParam(ArrayList aTrnPrm)
   {
   		transaxtionParameters=aTrnPrm;
   }
   public void setVoucherHeaderId(String avoucherHeaderId)
   {
   		voucherHeaderId = avoucherHeaderId;
   }
   public void setNarration(String aNarration)
   {
   	    narration = aNarration;
   }
   public String getFunctionId()
   {
      return functionId;
   }
   public void setFunctionId(String afunctionId)
   {
   	functionId = afunctionId;
   }
/*   public String getfunctionId1()
   {
      return functionId1;
   }
   public void setFunctionId1(String afunctionId)
   {
   	functionId1 = afunctionId;
   }*/
}
