package com.exilant.GLEngine;

import java.io.Serializable;

public class AccountDetailKey implements Serializable
{
   
   private int id;
   private int groupID;
   private int glCodeID;
   private int detailName;
   private String detailKey;
   
   public AccountDetailKey() 
   {
    
   }
   
   public int getId() 
   {
      return id;
   }
   
   public void setId(final int aId) 
   {
      id = aId;
   }
   
   public int getGroupId() 
   {
      return groupID;
   }
   
   public void setGroupId(final int aGroupId) 
   {
      groupID = aGroupId;
   }
   
   public int getGlCodeId() 
   {
      return glCodeID;
   }
   
   public void setGlCodeId(final int aGlCodeId) 
   {
      glCodeID = aGlCodeId;
   }
   
   public int getDetailNameId() 
   {
      return detailName;
   }
   
   public void setDetailNameId(final int aDetailNameId) 
   {
      detailName = aDetailNameId;
   }
   
   public String getDetailKey() 
   {
      return detailKey;
   }
   
   public void setDetailKey(final String aDetailKey) 
   {
      detailKey = aDetailKey;
   }
   
  
}
