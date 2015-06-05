package org.egov.infra.admin.master.service;



import java.util.List;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Author Roopa
 */
@Service
@Transactional(readOnly = true)
public class AppConfigService {
	
	private final AppConfigRepository appConfigValueRepository;
	
   
	 @Autowired
	    public AppConfigService(final AppConfigRepository appConfigValueRepos) {
	        this.appConfigValueRepository = appConfigValueRepos;
	    }

	 public AppConfig findBykeyNameAndModuleName(final String keyName,String moduleName) {
	        return appConfigValueRepository.findBykeyNameAndModuleName(keyName,moduleName);
	    }
	    public AppConfig findBykeyName(final String keyName) {
	        return appConfigValueRepository.findBykeyName(keyName);
	    }
	 
	  public List<Module> findByNameContainingIgnoreCase(String likemoduleName) {
		  return appConfigValueRepository.findByNameContainingIgnoreCase(likemoduleName);
	    }
	  public Module findByModuleById(Long moduleId) {
		 return appConfigValueRepository.findByModuleById(moduleId );
	    }
	  public List<AppConfig> findAll() {
	        return appConfigValueRepository.findAll();
	    }
	  public List<Module> findAllModules() {
	        return appConfigValueRepository.findAllModules();
	    }
	    @Transactional
	    public void createAppConfigValues(final AppConfig appConfig) {
	    	appConfigValueRepository.save(appConfig);
	    }
	    @Transactional
	    public void updateAppConfigValues(final AppConfig appConfig) {
	    	appConfigValueRepository.save(appConfig);
	    }
	
}
