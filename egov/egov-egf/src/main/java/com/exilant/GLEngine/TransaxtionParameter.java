//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\t

package com.exilant.GLEngine;


public class TransaxtionParameter 
{
   
   /**
    * name of the detail key
    */
	protected String detailName;
   
   /**
    * value of the detail key
    */
	protected String detailKey;
	
	protected String detailAmt;
	
	protected String glcodeId;
	
	protected String tdsId;
	
	protected String detailTypeId;
	
   
   /**
    * Access method for the detailName property.
    * 
    * @return   the current value of the detailName property
    */
   public String getDetailName() 
   {
      return detailName;
   }
   
   /**
    * Sets the value of the detailName property.
    * 
    * @param aDetailName the new value of the detailName property
    */
   public void setDetailName(String aDetailName) 
   {
      detailName = aDetailName;
   }
   
   /**
    * Access method for the detailKey property.
    * 
    * @return   the current value of the detailKey property
    */
   public String getDetailKey() 
   {
      return detailKey;
   }
   
  
   public void setDetailKey(String aDetailKey) 
   {
      detailKey = aDetailKey;
   }
  
   /**
    * Access method for the detailKey property.
    * 
    * @return   the current value of the detailKey property
    */
   public String getDetailTypeId() 
   {
      return detailTypeId;
   }
   
  
   public void setDetailTypeId(String aDetailType) 
   {
   	detailTypeId = aDetailType;
   }
  
   /**
    * Access methods for detail amount
    * @param aDetailKey
    */
   public String getDetailAmt() 
   {
      return detailAmt;
   }
   
   /**
    * Access methods for set Detail amount
    * @param adetailAmt
    */
   public void setDetailAmt(String adetailAmt) 
   {
   	detailAmt = adetailAmt;
   }
   
   /**
    * Access methods for GlCode Id
    * @param  the current value of the glcodeId property
    */
   public String getGlcodeId() 
   {
      return glcodeId;
   }
   
   /**
    * Access methods for set GlCode Id
    * @param aGlcodeId
    */
   public void setGlcodeId(String aGlcodeId) 
   {
	   glcodeId = aGlcodeId;
   }

/**
 * @return the tdsId
 */
public String getTdsId()
{
	return tdsId;
}

/**
 * @param tdsId the tdsId to set
 */
public void setTdsId(String tdsId)
{
	this.tdsId = tdsId;
}
}
