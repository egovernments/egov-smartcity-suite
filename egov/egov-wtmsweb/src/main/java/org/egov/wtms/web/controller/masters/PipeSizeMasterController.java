/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyPipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/masters")
public class PipeSizeMasterController {

    private final PropertyTypeService propertyTypeService;

    private final PipeSizeService pipeSizeService;

    private final PropertyPipeSizeService propertyPipeSizeService;

    @Autowired
    public PipeSizeMasterController(final PropertyTypeService propertyTypeService,
            final PipeSizeService pipeSizeService, final PropertyPipeSizeService propertyPipeSizeService) {
        this.propertyTypeService = propertyTypeService;
        this.pipeSizeService = pipeSizeService;
        this.propertyPipeSizeService = propertyPipeSizeService;

    }

    @RequestMapping(value = "/pipesizeMaster", method = GET)
    public String viewForm(@ModelAttribute PropertyPipeSize propertyPipeSize, final Model model) {
        propertyPipeSize = new PropertyPipeSize();
        model.addAttribute("propertyPipeSize", propertyPipeSize);
        model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
        return "pipesize-master";
    }

    @RequestMapping(value = "/pipesizeMaster", method = RequestMethod.POST)
    public String addCategoryMasterData(@Valid @ModelAttribute final PropertyPipeSize propertyPipeSize,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "pipesize-master";

        PropertyPipeSize propertypipeSize = new PropertyPipeSize();
        if (propertyPipeSize.getPipeSize().getId() == null)
            propertypipeSize = null;
        else
            propertypipeSize = propertyPipeSizeService.findByPropertyTypeAndPipeSize(
                    propertyPipeSize.getPropertyType(), propertyPipeSize.getPipeSize());
        if (propertypipeSize != null) {
            redirectAttrs.addFlashAttribute("propertyPipeSize", propertyPipeSize);
            model.addAttribute("message", "Entered PipeSize for the Chosen Property Type is already Exists");
        } else {
            PipeSize pipesize = new PipeSize();
            pipesize = propertyPipeSize.getPipeSize();
            final PropertyType propertyType = propertyPipeSize.getPropertyType();
            PropertyPipeSize propertyPipeSizeobj = new PropertyPipeSize();
            propertyPipeSizeobj = propertyPipeSizeService.findByPropertyTypeAndPipeSizeInmm(propertyType,
                    pipesize.getSizeInMilimeter());
            if (propertyPipeSizeobj == null) {
                PropertyPipeSize propertyPipesizeobj = new PropertyPipeSize();
                final String pipeSizeCode = pipesize.getCode().trim();
                propertyPipesizeobj = propertyPipeSizeService.findByPropertyTypeAndPipeSizecode(propertyType,
                        pipesize.getCode());
                if (propertyPipesizeobj != null)

                {
                    model.addAttribute("mode", "errorMode");
                    model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
                    resultBinder.rejectValue("pipeSize.code", "invalid.code");
                    return "pipesize-master";
                }
                PipeSize pipeSizeObj = new PipeSize();
                final double pipeSizeinmm = pipesize.getSizeInMilimeter();
                pipeSizeObj = pipeSizeService.findBysizeInMilimeter(pipesize.getSizeInMilimeter());
                if (pipeSizeObj == null)
                    pipesize.setSizeInMilimeter(pipeSizeinmm);
                PipeSize pipesizeObj = new PipeSize();
                pipesizeObj = pipeSizeService.findByCode(pipesize.getCode());
                if (pipesizeObj == null)
                    pipesize.setCode(pipeSizeCode);
                if (pipeSizeObj != null || pipesizeObj != null) {
                    final PropertyPipeSize propertypipeSizeobj = new PropertyPipeSize();
                    propertypipeSizeobj.setPropertyType(propertyType);
                    propertypipeSizeobj.setPipeSize(pipeSizeObj);
                    propertyPipeSizeService.createPropertyPipeSize(propertypipeSizeobj);
                    redirectAttrs.addFlashAttribute("propertyPipeSize", propertypipeSizeobj);
                } else {
                    pipesize.setActive(true);
                    pipeSizeService.createPipeSize(pipesize);
                    propertyPipeSizeService.createPropertyPipeSize(propertyPipeSize);
                    redirectAttrs.addFlashAttribute("propertyPipeSize", propertyPipeSize);
                }
            } else {
                model.addAttribute("mode", "errorMode");
                model.addAttribute("propertyTypeList", propertyTypeService.getAllActivePropertyTypes());
                resultBinder.rejectValue("pipeSize.sizeInMilimeter", "invalid.size");
                return "pipesize-master";
            }
            model.addAttribute("message", "Property PipeSize Data created successfully");
        }

        return "pipesize-master-success";
    }

}
