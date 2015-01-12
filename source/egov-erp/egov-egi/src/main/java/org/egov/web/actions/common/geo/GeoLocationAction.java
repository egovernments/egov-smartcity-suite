/*
 * @(#)GeoLocationAction.java 3.0, 14 Jun, 2013 1:43:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.common.geo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.GeoKmlInfo;
import org.egov.infstr.services.GeoLocationConstants;
import org.egov.infstr.services.GeoLocationService;
import org.egov.web.actions.BaseFormAction;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@ParentPackage("egov")
public class GeoLocationAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationAction.class);

	private String kmlDataModelKey;
	private String kmlUrlPath;

	@Override
	public Object getModel() {

		return null;
	}

	@SkipValidation
	public String getKMLStream() {
		try {
			LOGGER.debug("GeoLocationAction | getKMLStream | Start");
			final GeoKmlInfo geoKmlInfo = GeoLocationService.getKmlDataFromCache(this.kmlDataModelKey);
			final Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.putAll(geoKmlInfo.getWardWiseColor());
			dataMap.putAll(geoKmlInfo.getColorCodes());
			final Configuration cfg = new Configuration();

			final HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/xml");

			// If modules passing the kml file from a url location.

			if (null != this.kmlUrlPath) {
				try {
					final URL url = new URL(this.kmlUrlPath);
					cfg.setTemplateLoader(new URLTemplateLoader() {
						@Override
						protected URL getURL(final String urlName) {
							return url;
						}
					});
					final Template tpl = cfg.getTemplate("");
					tpl.process(dataMap, response.getWriter());
				} catch (final MalformedURLException e) {
					LOGGER.error("Error occurred while loading GIS",e);
					throw new ValidationException(Arrays.asList(new ValidationError("kmlUrlPath", e.getMessage())));
				}
			} else {
				final ClassTemplateLoader ctl = new ClassTemplateLoader(GeoLocationAction.class, "/reports/templates");
				cfg.setTemplateLoader(ctl);
				final Template tpl = cfg.getTemplate(GeoLocationConstants.BASE_KML_CLASS_PATH_FILE_NAME);
				tpl.process(dataMap, response.getWriter());
			}

		} catch (final Exception e) {
			LOGGER.error("Error occurred while loading GIS",e);
			throw new ValidationException(Arrays.asList(new ValidationError("kmlLoading", e.getMessage())));

		}
		LOGGER.debug("GeoLocationAction | getKMLStream | End");
		return null;
	}

	public String getKmlDataModelKey() {
		return this.kmlDataModelKey;
	}

	public void setKmlDataModelKey(final String kmlDataModelKey) {
		this.kmlDataModelKey = kmlDataModelKey;
	}

	public String getKmlUrlPath() {
		return this.kmlUrlPath;
	}

	public void setKmlUrlPath(final String kmlUrlPath) {
		this.kmlUrlPath = kmlUrlPath;
	}

}
