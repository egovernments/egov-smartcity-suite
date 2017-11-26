/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.controller.common;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/downloadfile")
public class FileDownloadController {

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private CityService cityService;

    @GetMapping
    @ResponseBody
    public ResponseEntity download(@RequestParam String fileStoreId, @RequestParam String moduleName,
                                   @RequestParam(defaultValue = "false") boolean toSave) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId, moduleName, toSave);
    }

    @GetMapping("/logo")
    @ResponseBody
    public void getLogo(HttpServletResponse response) throws IOException {
        IOUtils.write(cityService.getCityLogoAsBytes(), response.getOutputStream());
    }

    @GetMapping("/gis")
    public void getKML(HttpServletResponse response) throws IOException {
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
