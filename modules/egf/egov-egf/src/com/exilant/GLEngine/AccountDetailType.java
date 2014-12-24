//Source file: D:\\SUSHMA\\PROJECTS\\E-GOV\\ENGINEDESIGN\\com\\exilant\\GLEngine\\AccountDetailType.java

package com.exilant.GLEngine;

import java.io.Serializable;


public class AccountDetailType implements Serializable 
{
   
   /**
    * Id for the AccountDetailType class
    */
   private int id;
   
   /**
    * Name of the detail type
    */
   private String name;
   
   /**
    * Description of the detail type
    */
   private String description;
   
   /**
    * Table name in the database to verify if the detail key is valid or not
    */
   private String tableName;
   
   /**
    * Similar to the table name, the column name in the table to validate the detail 
    * key
    */
   private String columnName;
   
   /**
    * String with which the GL engine compares for the valid detail entered by any 
    * client
    */
   private String attributeName;
   
   /**
    * Number of levels, if any for this detail type
    */
   private int nbrOfLevels;
   
   /**
    * @roseuid 42076049021D
    */
   public AccountDetailType() 
   {
    
   }
   
   /**
    * Access method for the Id property.
    * 
    * @return   the current value of the Id property
    */
   public int getId() 
   {
      return id;
   }
   
   /**
    * Sets the value of the Id property.
    * 
    * @param aId the new value of the Id property
    */
   public void setId(final int aId) 
   {
      id = aId;
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
   public void setName(final String aName) 
   {
      name = aName;
   }
   
   /**
    * Access method for the description property.
    * 
    * @return   the current value of the description property
    */
   public String getDescription() 
   {
      return description;
   }
   
   /**
    * Sets the value of the description property.
    * 
    * @param aDescription the new value of the description property
    */
   public void setDescription(final String aDescription) 
   {
      description = aDescription;
   }
   
   /**
    * Access method for the tableName property.
    * 
    * @return   the current value of the tableName property
    */
   public String getTableName() 
   {
      return tableName;
   }
   
   /**
    * Sets the value of the tableName property.
    * 
    * @param aTableName the new value of the tableName property
    */
   public void setTableName(final String aTableName) 
   {
      tableName = aTableName;
   }
   
   /**
    * Access method for the columnName property.
    * 
    * @return   the current value of the columnName property
    */
   public String getColumnName() 
   {
      return columnName;
   }
   
   /**
    * Sets the value of the columnName property.
    * 
    * @param aColumnName the new value of the columnName property
    */
   public void setColumnName(final String aColumnName) 
   {
      columnName = aColumnName;
   }
   
   /**
    * Access method for the attributeName property.
    * 
    * @return   the current value of the attributeName property
    */
   public String getAttributeName() 
   {
      return attributeName;
   }
   
   /**
    * Sets the value of the attributeName property.
    * 
    * @param aAttributeName the new value of the attributeName property
    */
   public void setAttributeName(final String aAttributeName) 
   {
      attributeName = aAttributeName;
   }
   
   /**
    * Access method for the nbrOfLevels property.
    * 
    * @return   the current value of the nbrOfLevels property
    */
   public int getNbrOfLevels() 
   {
      return nbrOfLevels;
   }
   
   /**
    * Sets the value of the nbrOfLevels property.
    * 
    * @param aNbrOfLevels the new value of the nbrOfLevels property
    */
   public void setNbrOfLevels(final int aNbrOfLevels) 
   {
      nbrOfLevels = aNbrOfLevels;
   }
}
