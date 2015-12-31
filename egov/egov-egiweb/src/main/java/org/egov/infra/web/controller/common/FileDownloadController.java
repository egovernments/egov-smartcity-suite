/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.infra.web.controller.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/downloadfile")
public class FileDownloadController {

    @Qualifier("localDiskFileStoreService")
    private @Autowired FileStoreService fileStoreService;
    @Autowired
    private FileStoreUtils fileStoreUtils;

    @RequestMapping
    public void download(@RequestParam final String fileStoreId, @RequestParam final String moduleName,
            @RequestParam(defaultValue = "false") final boolean toSave,
            final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, moduleName, toSave, response);
    }

    @RequestMapping("/logo")
    public void download(@RequestParam final String fileStoreId, @RequestParam final String moduleName, final HttpSession session,
            final HttpServletResponse response) throws IOException {
        if (session.getAttribute("citylogo") != null && session.getAttribute("citylogo").toString().contains(fileStoreId))
            fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, moduleName, false, response);

    }

    @RequestMapping("/gis")
    public void download(final HttpServletResponse response) throws IOException {
        try (final InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("gis/" + EgovThreadLocals.getTenantID() + "/wards.kml");
                final OutputStream out = response.getOutputStream();) {
            if (in != null) {
                response.setHeader("Content-Disposition", "inline;filename=wards.kml");
                response.setContentType("application/vnd.google-earth.kml+xml");
                IOUtils.write(IOUtils.toByteArray(in), out);
            }
        }
    }

}
