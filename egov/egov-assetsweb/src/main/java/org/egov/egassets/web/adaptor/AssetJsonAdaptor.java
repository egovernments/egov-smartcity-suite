package org.egov.egassets.web.adaptor;

import java.lang.reflect.Type;
import org.egov.assets.model.Asset;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AssetJsonAdaptor implements JsonSerializer<Asset> {
	@Override
	public JsonElement serialize(final Asset asset, final Type type,
			final JsonSerializationContext jsc) {
		final JsonObject jsonObject = new JsonObject();
		if (asset != null) {

			if (asset.getName() != null)
				jsonObject.addProperty("name", asset.getName());
			else
				jsonObject.addProperty("name", "");
			if (asset.getCode() != null)
				jsonObject.addProperty("code", asset.getCode());
			else
				jsonObject.addProperty("code", "");
			if (asset.getAssetCategory() != null)
				jsonObject.addProperty("assetCategory", asset
						.getAssetCategory().getName());
			else
				jsonObject.addProperty("assetCategory", "");
			if (asset.getDepartment() != null)
				jsonObject.addProperty("department", asset.getDepartment()
						.getName());
			else
				jsonObject.addProperty("department", "");
			if (asset.getStatus() != null)
				jsonObject.addProperty("status", asset.getStatus()
						.getDescription());
			else
				jsonObject.addProperty("status", "");
			if (asset.getAssetDetails() != null)
				jsonObject.addProperty("assetDetails", asset.getAssetDetails());
			else
				jsonObject.addProperty("assetDetails", "");
			jsonObject.addProperty("id", asset.getId());
		}
		return jsonObject;
	}
}