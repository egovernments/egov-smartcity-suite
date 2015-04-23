//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\GLParameters.java

package com.exilant.GLEngine;

import java.io.Serializable;


public class GLParameter implements Serializable
{
   
   /**
    * if this detail is required for posting?
    */
   
   private int glcodeId;
   /**
    * Name of the detail the GL code requires
    */
   private int detailId;
   
   
   private String detailName;
   
   /**
    * The value of the detail for that detail name
    */
   private String detailKey;
   
   private String detailAmt;
   
   private String tdsId;
   
   
   
   public int getDetailId() 
   {
      return detailId;
   }
   
   /**
    * Sets the value of the detailName property.
    * 
    * @param aDetailName the new value of the detailName property
    */
   public void setDetailId(int aDetailId) 
   {
      detailId = aDetailId;
   }
   
   
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
   
   /**
    * Sets the value of the detailKey property.
    * 
    * @param aDetailKey the new value of the detailKey property
    */
   public void setDetailKey(String aDetailKey) 
   {
      detailKey = aDetailKey;
   }
   
   /**
    * Access method for the detailAmt property.
    * 
    * @return   the current value of the detailKey property
    */
   public String getDetailAmt() 
   {
      return detailAmt;
   }
   
   /**
    * Sets the value of the detailAmt property.
    * 
    * @param aDetailAmt the new value of the detailAmt property
    */
   public void setDetailAmt(String aDetailAmt) 
   {
   	detailAmt = aDetailAmt;
   }
/**
 * @return Returns the glcodeId.
 */
public int getGlcodeId()
{
	return glcodeId;
}
/**
 * @param glcodeId The glcodeId to set.
 */
public void setGlcodeId(int glcodeId)
{
	this.glcodeId = glcodeId;
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
