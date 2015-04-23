package org.egov.asset.model;

import javax.script.ScriptContext;

import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class AssetNumberGenrator {
    
        @Autowired
	private SequenceGenerator sequenceGenerator;
	//private PersistenceService<Script, Long> scriptService;
	@Autowired
	private ScriptService scriptService;
	
	public String getAssetNumber(Asset asset,String year){
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "assets.assetnumber.generator");
		ScriptContext scriptContext = ScriptService.createContext("asset",asset,"year",year,"sequenceGenerator",sequenceGenerator);
		return scriptService.executeScript("assets.assetnumber.generator", scriptContext).toString();
		//return scripts.get(0).eval(Script.createContext("asset",asset,"year",year,"sequenceGenerator",sequenceGenerator)).toString();
	}

}
