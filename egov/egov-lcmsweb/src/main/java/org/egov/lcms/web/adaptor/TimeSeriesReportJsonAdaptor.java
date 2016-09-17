package org.egov.lcms.web.adaptor;

import java.lang.reflect.Type;

import org.egov.lcms.reports.entity.TimeSeriesReportResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TimeSeriesReportJsonAdaptor implements JsonSerializer<TimeSeriesReportResult> {

    @Override
    public JsonElement serialize(final TimeSeriesReportResult timeSeriesresult, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", timeSeriesresult.getCount().toString());
        jsonObject.addProperty("aggregatedBy", timeSeriesresult.getAggregatedBy());
        jsonObject.addProperty("year", timeSeriesresult.getYear().toString()== null ? "": timeSeriesresult.getYear().toString());
        jsonObject.addProperty("month", timeSeriesresult.getMonth() == null ? "":timeSeriesresult.getMonth());
        return jsonObject;
    }

}
