/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.struts.actions.common.geo;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.gis.model.GeoKmlInfo;
import org.egov.infra.gis.service.GeoLocationConstants;
import org.egov.infra.gis.service.GeoLocationService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
