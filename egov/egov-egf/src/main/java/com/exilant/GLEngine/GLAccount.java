//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\GLAccount.java

package com.exilant.GLEngine;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * a hashmap with key as id
 */
public class GLAccount implements Serializable
{
   
   /**
    * Id of the GLAccount
    */
	private int ID;
   
   /**
    * GLCode
    */
	private String glCode;
   
   /**
    * Name of the account code or GL code
    */
	private String name;
   
	private int isActiveForPosting;
   
    private ArrayList glParameters = new ArrayList();
  
   
   /**
    * @roseuid 41E23F6F0202
    */
   public GLAccount() 
   {
    
   }
   
   /**
    * Access method for the id property.
    * 
    * @return   the current value of the id property
    */
   public int getId() 
   {
      return ID;
   }
   
   /**
    * Sets the value of the id property.
    * 
    * @param aId the new value of the id property
    */
   public void setId(int aId) 
   {
      ID = aId;
   }
   
   /**
    * Access method for the code property.
    * 
    * @return   the current value of the code property
    */
   public String getCode() 
   {
      return glCode;
   }
   
   /**
    * Sets the value of the code property.
    * 
    * @param aCode the new value of the code property
    */
   public void setCode(String aCode) 
   {
      glCode = aCode;
   }
   
   /**
    * Access method for the name property.
    * 
    * @return   the current value of the name property
    */
   public String getName() 
   {
      return name;
   }
   
   /**
    * Sets the value of the name property.
    * 
    * @param aName the new value of the name property
    */
   public void setName(String aName) 
   {
      name = aName;
   }
   
   public int isActiveForPosting() 
   {
      return isActiveForPosting;
   }
   
   /**
    * Sets the value of the id property.
    * 
    * @param aId the new value of the id property
    */
   public void setIsActiveForPosting(int active) 
   {
   		isActiveForPosting = active;
   }
   public void setGLParameters(ArrayList aglParameters) 
   {
   		glParameters = aglParameters;
   }
   
   public ArrayList getGLParameters() 
   {
      return glParameters;
   }
}
