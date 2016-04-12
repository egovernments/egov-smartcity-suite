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

import org.apache.commons.io.IOUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/downloadfile")
public class FileDownloadController {

    public static final String LOGO_IMAGE_PATH = "/resources/global/images/";
    public static final String CITY_LOGO_KEY = "citylogo";

    @Qualifier("localDiskFileStoreService")
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @RequestMapping
    public void download(@RequestParam String fileStoreId, @RequestParam String moduleName,
                         @RequestParam(defaultValue = "false") boolean toSave,
                         HttpServletResponse response) throws IOException {
        this.fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, moduleName, toSave, response);
    }

    @RequestMapping("/logo")
    public String download(@RequestParam String fileStoreId, @RequestParam String moduleName, HttpSession session) throws IOException, ServletException {
        Path image = Paths.get(session.getServletContext().getRealPath(LOGO_IMAGE_PATH) + File.separator + fileStoreId + ImageUtils.JPG_EXTN);
        if (!Files.exists(image)) {
            if (session.getAttribute(CITY_LOGO_KEY) != null && session.getAttribute(CITY_LOGO_KEY).toString().contains(fileStoreId)) {
                FileStoreMapper fileStoreMapper = this.fileStoreMapperRepository.findByFileStoreId(fileStoreId);
                if (fileStoreMapper != null) {
                    File file = this.fileStoreService.fetch(fileStoreMapper, moduleName);
                    Files.copy(file.toPath(), image);
                }
            }
        }
        String logoPath = LOGO_IMAGE_PATH + fileStoreId + ImageUtils.JPG_EXTN;
        session.setAttribute(CITY_LOGO_KEY, logoPath);
        return "forward:"+logoPath;
    }

    @RequestMapping("/gis")
    public void download(HttpServletResponse response) throws IOException {
        try (final InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("gis/" + EgovThreadLocals.getTenantID() + "/wards.kml");
             final OutputStream out = response.getOutputStream()) {
            if (in != null) {
                response.setHeader("Content-Disposition", "inline;filename=wards.kml");
                response.setContentType("application/vnd.google-earth.kml+xml");
                IOUtils.write(IOUtils.toByteArray(in), out);
            }
        }
    }

}
