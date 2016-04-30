package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.commons.Fund;

import java.lang.reflect.Type;
public class FundJsonAdaptor implements JsonSerializer<Fund>
{
	@Override
	public JsonElement serialize(final Fund fund, final Type type,final JsonSerializationContext jsc) 
	{
		final JsonObject jsonObject = new JsonObject();
		if (fund != null)
		{
			if(fund.getName()!=null)
				jsonObject.addProperty("name", fund.getName());
			else
				jsonObject.addProperty("name","");
			if(fund.getCode()!=null)
				jsonObject.addProperty("code", fund.getCode());
			else
				jsonObject.addProperty("code","");
			if(fund.getIdentifier()!=null)
				jsonObject.addProperty("identifier", fund.getIdentifier());
			else
				jsonObject.addProperty("identifier","");
			if(fund.getLlevel()!=null)
				jsonObject.addProperty("llevel", fund.getLlevel());
			else
				jsonObject.addProperty("llevel","");
			if(fund.getParentId()!=null)
				jsonObject.addProperty("parentId", fund.getParentId().getName());
			else
				jsonObject.addProperty("parentId","");
			if(fund.getIsnotleaf()!=null)
				jsonObject.addProperty("isnotleaf", fund.getIsnotleaf());
			else
				jsonObject.addProperty("isnotleaf","");
			if(fund.getIsactive()!=null)
				jsonObject.addProperty("isactive", fund.getIsactive());
			else
				jsonObject.addProperty("isactive","");
			jsonObject.addProperty("id", fund.getId());
		} 
		return jsonObject;  }
}