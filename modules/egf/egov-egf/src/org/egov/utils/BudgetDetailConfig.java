package org.egov.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;

public class BudgetDetailConfig {
	private static final String DELIMITER = ",";
	private PersistenceService persistenceService;
	List<String> headerFields = new ArrayList<String>();
	List<String> gridFields = new ArrayList<String>();
	List<String> mandatoryFields = new ArrayList<String>();
	
	public BudgetDetailConfig(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
		headerFields = fetchAppConfigValues("budgetDetail.header.component");
		gridFields = fetchAppConfigValues("budgetDetail.grid.component");
		mandatoryFields = fetchAppConfigValues("budgetDetail_mandatory_fields");
	}
	
	public final List<String> getGridFields() {
		return gridFields;
	}

	public final List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public final List<String> getHeaderFields() {
		return headerFields;
	}

	final List<String> fetchAppConfigValues(String keyName) {
		AppConfig appConfig = (AppConfig) persistenceService.find("from AppConfig where key_name='"+keyName+"'");
		if(appConfig!=null && appConfig.getAppDataValues()!= null){
			if(appConfig.getAppDataValues().iterator().hasNext()){
				AppConfigValues appDataValues = appConfig.getAppDataValues().iterator().next();
				return Arrays.asList(appDataValues.getValue().split(DELIMITER));
			}
		}
		return new ArrayList<String>();
	}
	
	public final boolean shouldShowField(List<String> fieldList,String field){
		return fieldList.isEmpty() || fieldList.contains(field);
	}
	
	public void checkHeaderMandatoryField(Map<String,Object> valuesToBeChecked) {
		for (Entry<String, Object> entry : valuesToBeChecked.entrySet()) {
			if(headerFields.contains(entry.getKey()) && mandatoryFields.contains(entry.getKey()) && entry.getValue() == null)
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail."+entry.getKey()+".mandatory","budgetDetail."+entry.getKey()+".mandatory")));
		}
	}

	public void checkGridMandatoryField(Map<String,Object> valuesToBeChecked) {
		for (Entry<String, Object> entry : valuesToBeChecked.entrySet()) {
			if(gridFields.contains(entry.getKey()) && mandatoryFields.contains(entry.getKey()) && entry.getValue() == null)
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail."+entry.getKey()+".mandatory","budgetDetail."+entry.getKey()+".mandatory")));
		}
	}
}
