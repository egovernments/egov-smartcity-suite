package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.commons.Accountdetailtype;

import java.lang.reflect.Type;
  public class AccountdetailtypeJsonAdaptor implements JsonSerializer<Accountdetailtype>
 {
@Override
 public JsonElement serialize(final Accountdetailtype accountdetailtype, final Type type,final JsonSerializationContext jsc) 
{
  final JsonObject jsonObject = new JsonObject();
 if (accountdetailtype != null)
 {
 jsonObject.addProperty("id", accountdetailtype.getId());
 jsonObject.addProperty("name", accountdetailtype.getName());
 jsonObject.addProperty("description", accountdetailtype.getDescription());
 jsonObject.addProperty("isactive", accountdetailtype.getIsactive());
 jsonObject.addProperty("fullQualifiedName", accountdetailtype.getFullQualifiedName());
     } 
 return jsonObject;  }
 }