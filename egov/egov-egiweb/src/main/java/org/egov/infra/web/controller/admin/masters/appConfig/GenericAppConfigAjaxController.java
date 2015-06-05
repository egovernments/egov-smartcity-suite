package org.egov.infra.web.controller.admin.masters.appConfig;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
@RequestMapping(value = "/appConfig")
public class GenericAppConfigAjaxController {

	 private AppConfigService appConfigValueService;

	 @Autowired
	    public GenericAppConfigAjaxController(AppConfigService appConfigValueService) {
	        this.appConfigValueService = appConfigValueService;
	    }
	
	 @RequestMapping(value = { "/modules" }, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody List<Module> getAllModulesByNameLike(@RequestParam final String moduleName,
	            final HttpServletResponse response) throws IOException {
		 final String likemoduleName = "%" + moduleName + "%";
	        return  appConfigValueService.findByNameContainingIgnoreCase(likemoduleName);
	    }
	   
}
