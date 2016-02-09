package org.egov.egf.web.adaptor;

import java.lang.reflect.Type;
import org.egov.masters.model.AccountEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
  public class AccountEntityJsonAdaptor implements JsonSerializer<AccountEntity>
 {
@Override
 public JsonElement serialize(final AccountEntity accountEntity, final Type type,final JsonSerializationContext jsc) 
{
  final JsonObject jsonObject = new JsonObject();
 if (accountEntity != null)
 {
 jsonObject.addProperty("accountdetailtype", accountEntity.getAccountdetailtype().getName());
 jsonObject.addProperty("name", accountEntity.getName());
 jsonObject.addProperty("code", accountEntity.getCode());
 jsonObject.addProperty("isactive", accountEntity.getIsactive());
 jsonObject.addProperty("id", accountEntity.getId());
     } 
 return jsonObject;  }
 }