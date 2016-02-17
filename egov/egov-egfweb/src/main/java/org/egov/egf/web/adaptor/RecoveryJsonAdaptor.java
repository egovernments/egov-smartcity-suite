package org.egov.egf.web.adaptor;

import java.lang.reflect.Type;
import org.egov.model.recoveries.Recovery;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RecoveryJsonAdaptor implements JsonSerializer<Recovery>
{
    @Override
    public JsonElement serialize(final Recovery recovery, final Type type, final JsonSerializationContext jsc)
    {
        final JsonObject jsonObject = new JsonObject();
        if (recovery != null)
        {
            jsonObject.addProperty("recoverycode", recovery.getType());
            jsonObject.addProperty("recoveryname", recovery.getRecoveryName());
            jsonObject.addProperty("subledgertype",recovery.getEgPartytype()!=null?recovery.getEgPartytype().getCode():"");
            jsonObject.addProperty("chartofaccounts", recovery.getChartofaccounts().getName());
            jsonObject.addProperty("remittedto", recovery.getRemitted());
            jsonObject.addProperty("ifscCode", recovery.getIfscCode());
            jsonObject.addProperty("accountNumber", recovery.getAccountNumber());
            jsonObject.addProperty("isactive", recovery.getIsactive());
            jsonObject.addProperty("id", recovery.getId());
        }
        return jsonObject;
    }
}