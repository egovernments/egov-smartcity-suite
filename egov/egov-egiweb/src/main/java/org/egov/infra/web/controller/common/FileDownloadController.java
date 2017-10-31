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

package org.egov.infra.web.controller.common;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGO_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGO_PATH_KEY;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGO_STATIC_URL;

@Controller
@RequestMapping("/downloadfile")
public class FileDownloadController {

    private static final String LOGO_IMAGE_PATH = "/resources/global/images/";

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private CityService cityService;

    @RequestMapping
    public void download(@RequestParam String fileStoreId, @RequestParam String moduleName,
                         @RequestParam(defaultValue = "false") boolean toSave,
                         HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, moduleName, toSave, response);
    }

    @RequestMapping("/logo")
    public String download(@RequestParam String fileStoreId, @RequestParam String moduleName,
                           HttpSession session) throws IOException {
        String logoPath = new StringBuilder().append(LOGO_IMAGE_PATH).append(fileStoreId).append(ImageUtils.JPG_EXTN).toString();
        Path logoRealPath = Paths.get(new StringBuilder().append(session.getServletContext().getRealPath(LOGO_IMAGE_PATH))
                .append(File.separator).append(fileStoreId).append(ImageUtils.JPG_EXTN).toString());
        if (!logoRealPath.toFile().exists()) {
            String cityLogoKey = (String) session.getAttribute(CITY_LOGO_KEY);
            if (cityLogoKey != null && cityLogoKey.contains(fileStoreId)) {
                this.fileStoreUtils.copyFileToPath(logoRealPath, fileStoreId, moduleName);
            }
        }
        session.setAttribute(CITY_LOGO_PATH_KEY, logoPath);
        cityService.addToCityCache(CITY_LOGO_PATH_KEY, format(CITY_LOGO_STATIC_URL, getDomainURL(), logoPath));
        return "forward:" + logoPath;
    }

    @RequestMapping("/gis")
    public void download(HttpServletResponse response) throws IOException {
        try (final InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("gis/" + ApplicationThreadLocals.getTenantID() + "/wards.kml");
             final OutputStream out = response.getOutputStream()) {
            if (in != null) {
                response.setHeader("Content-Disposition", "inline;filename=wards.kml");
                response.setContentType("application/vnd.google-earth.kml+xml");
                IOUtils.write(IOUtils.toByteArray(in), out);
            }
        }
    }

}
