package org.egov.tl.web.adaptor;

import java.lang.reflect.Type;
import org.egov.tl.entity.Validity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
  public class ValidityJsonAdaptor implements JsonSerializer<Validity>
 {
@Override
 public JsonElement serialize(final Validity validity, final Type type,final JsonSerializationContext jsc) 
{
  final JsonObject jsonObject = new JsonObject();
 if (validity != null)
 {
 jsonObject.addProperty("natureOfBusiness", validity.getNatureOfBusiness().getName());
 jsonObject.addProperty("licenseCategory", validity.getLicenseCategory().getName());
 jsonObject.addProperty("day", validity.getDay());
 jsonObject.addProperty("week", validity.getWeek());
 jsonObject.addProperty("month", validity.getMonth());
 jsonObject.addProperty("year", validity.getYear());
     } 
 return jsonObject;  }
 }