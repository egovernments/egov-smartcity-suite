package org.egov.assets.web.adaptor;

import java.lang.reflect.Type;

import org.egov.assets.model.AssetCategory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AssetCategoryJsonAdaptor implements JsonSerializer<AssetCategory> {
	@Override
	public JsonElement serialize(final AssetCategory assetCategory,
			final Type type, final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (assetCategory != null) {
			if (assetCategory.getName() != null)
				jsonObject.addProperty("name", assetCategory.getName());
			else
				jsonObject.addProperty("name", "");
			if (assetCategory.getCode() != null)
				jsonObject.addProperty("code", assetCategory.getCode());
			else
				jsonObject.addProperty("code", "");
			if (assetCategory.getAssetType() != null)
				jsonObject.addProperty("assetType", assetCategory
						.getAssetType().name());
			else
				jsonObject.addProperty("assetType", "");
			if (assetCategory.getParent() != null)
				jsonObject.addProperty("parent", assetCategory.getParent()
						.getName());
			else
				jsonObject.addProperty("parent", "");
			if (assetCategory.getUom() != null)
				jsonObject.addProperty("uom", assetCategory.getUom().getUom());
			else
				jsonObject.addProperty("uom", "");
			jsonObject.addProperty("id", assetCategory.getId());
		}
		return jsonObject;
	}
}