package org.egov.asset.model;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class AssetCategoryNumberGenrator {
	
        @Autowired
        private SequenceGenerator sequenceGenerator;
        //private PersistenceService<Script, Long> scriptService;
        @Autowired
        private ScriptService scriptService;
	
	public String getAssetCategoryNumber(AssetCategory assetCategory,CFinancialYear financialYear){
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "assets.assetcategorynumber.generator");
		//return scripts.get(0).eval(Script.createContext("assetCategory",assetCategory,"finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
	       ScriptContext scriptContext = ScriptService.createContext("assetCategory",assetCategory,"finYear",financialYear,"sequenceGenerator",sequenceGenerator);
               return scriptService.executeScript("assets.assetcategorynumber.generator", scriptContext).toString();
	}
	
}
