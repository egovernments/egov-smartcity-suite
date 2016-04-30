package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.model.recoveries.Recovery;

import java.lang.reflect.Type;

public class RecoveryJsonAdaptor implements JsonSerializer<Recovery>
{
    @Override
    public JsonElement serialize(final Recovery recovery, final Type type, final JsonSerializationContext jsc)
    {
        final JsonObject jsonObject = new JsonObject();
        if (recovery != null)
        {
            jsonObject.addProperty("recoverycode", recovery.getType()!=null?recovery.getType():"");
            jsonObject.addProperty("recoveryname", recovery.getRecoveryName()!=null?recovery.getRecoveryName():"");
            jsonObject.addProperty("subledgertype",recovery.getEgPartytype()!=null?(recovery.getEgPartytype().getCode()!=null?recovery.getEgPartytype().getCode():""):"");
            jsonObject.addProperty("chartofaccounts", recovery.getChartofaccounts()!=null?(recovery.getChartofaccounts().getName()!=null?recovery.getChartofaccounts().getName():""):"");
            jsonObject.addProperty("remittedto", recovery.getRemitted()!=null?recovery.getRemitted():"");
            jsonObject.addProperty("ifscCode", recovery.getIfscCode()!=null?recovery.getIfscCode():"");
            jsonObject.addProperty("accountNumber", recovery.getAccountNumber()!=null?recovery.getAccountNumber():"");
            jsonObject.addProperty("isactive", recovery.getIsactive()!=null?recovery.getIsactive().toString():"");
            jsonObject.addProperty("id", recovery.getId());
        }
        return jsonObject;
    }
}