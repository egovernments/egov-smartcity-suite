package org.egov.egf.web.adaptor;

import java.lang.reflect.Type;
import org.egov.commons.Relation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
public class RelationJsonAdaptor implements JsonSerializer<Relation>
{
	@Override
	public JsonElement serialize(final Relation relation, final Type type,final JsonSerializationContext jsc) 
	{
		final JsonObject jsonObject = new JsonObject();
		if (relation != null)
		{
			jsonObject.addProperty("code", relation.getCode());
			jsonObject.addProperty("name", relation.getName());
			jsonObject.addProperty("mobile", relation.getMobile());
			jsonObject.addProperty("isactive", relation.getIsactive());
			jsonObject.addProperty("panno", relation.getPanno());
			jsonObject.addProperty("bankaccount", relation.getBankaccount());
			if(relation.getBank()!=null)
			jsonObject.addProperty("bank", relation.getBank().getName());
			else
			jsonObject.addProperty("bank", "");
				
			jsonObject.addProperty("id", relation.getId());    
		} 
		return jsonObject;  }
}