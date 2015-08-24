/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.web.controller.admin.masters.city;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.CityPreferences;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.EgovThreadLocals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/city/setup")
public class CitySetupController {

    @Autowired
    private CityService cityService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @ModelAttribute
    public City city() {
        final City city = cityService.getCityByCode(EgovThreadLocals.getCityCode());
        if (city.getPreferences() == null)
            city.setPreferences(new CityPreferences());
        return city;
    }

    @RequestMapping("/view")
    public String showCitySetup() {
        return "city-setup";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateCitySetup(@Valid @ModelAttribute final City city, final BindingResult bindResult,
            @RequestParam(required = false) final MultipartFile gisKML,
            @RequestParam(required = false) final MultipartFile gisShape,
            @RequestParam(required = false) final MultipartFile logo,
            final RedirectAttributes redirectAttrs) {
        if (bindResult.hasErrors())
            return "city-setup";
        addToCityFileData(city, Arrays.asList(gisKML, gisShape, logo));
        cityService.updateCity(city);
        redirectAttrs.addFlashAttribute("message", "City configuration successfully updated!");
        return "redirect:/city/setup/view";
    }

    private void addToCityFileData(final City city, final List<MultipartFile> files) {
        if (!files.isEmpty())
            files.stream().filter(file -> !file.isEmpty()).forEach(file -> {
                try {
                    final FileStoreMapper fileStore = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                            file.getContentType(), EgovThreadLocals.getCityCode());
                    switch (file.getName()) {
                    case "gisKML":
                        city.getPreferences().setGisKML(fileStore);
                        break;
                    case "logo":
                        city.getPreferences().setMunicipalityLogo(fileStore);
                        break;
                    default:
                        break;
                    }
                } catch (final Exception e) {
                    throw new EGOVRuntimeException("Error occurred while adding city related files");
                }
            });

    }
}
