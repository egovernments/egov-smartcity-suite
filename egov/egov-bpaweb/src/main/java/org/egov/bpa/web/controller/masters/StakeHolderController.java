/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.bpa.web.controller.masters;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.bpa.application.autonumber.StakeHolderCodeGenerator;
import org.egov.bpa.application.entity.CheckListDetail;
import org.egov.bpa.application.entity.StakeHolder;
import org.egov.bpa.application.entity.enums.StakeHolderType;
import org.egov.bpa.application.service.BPADocumentService;
import org.egov.bpa.masters.service.StakeHolderService;
import org.egov.bpa.web.controller.adaptors.StakeHolderJsonAdaptor;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.persistence.entity.PermanentAddress;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/stakeholder")
public class StakeHolderController {
    private static final String STAKEHOLDER_RESULT = "stakeholder-result";
    private static final String SEARCH_STAKEHOLDER_EDIT = "search-stakeholder-edit";
    private static final String STAKE_HOLDER = "stakeHolder";
    private static final String STAKEHOLDER_UPDATE = "stakeholder-update";
    private static final String STAKEHOLDER_VIEW = "stakeholder-view";
    private static final String SEARCH_STAKEHOLDER_VIEW = "search-stakeholder-view";
    private static final String DATA = "{ \"data\":";

    @Autowired
    private StakeHolderService stakeHolderService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private StakeHolderCodeGenerator stakeHolderCodeGenerator;
    @Autowired
    private BPADocumentService bpaDocumentService;

    private static final String STAKEHOLDER_NEW = "stakeholder-new";

    @ModelAttribute("stakeHolderDocumentList")
    public List<CheckListDetail> getStakeHolderDocuments() {
        return bpaDocumentService.getStakeHolderDocuments();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showStakeHolder(final Model model) {
        model.addAttribute(STAKE_HOLDER, new StakeHolder());
        model.addAttribute("genderList", Arrays.asList(Gender.values()));
        model.addAttribute("stakeHolderTypes", Arrays.asList(StakeHolderType.values()));
        model.addAttribute("isOnbehalfOfOrganization", false);
        return STAKEHOLDER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createStakeholder(@ModelAttribute(STAKE_HOLDER) final StakeHolder stakeHolder,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors, final RedirectAttributes redirectAttributes) {
        if (null == stakeHolder.getCode())
            stakeHolder.setCode(stakeHolderCodeGenerator.generateStakeHolderCode(stakeHolder));
        final List<Address> addressList = new ArrayList<>();
        addressList.add(setCorrespondenceAddress(stakeHolder));
        addressList.add(setPermanentAddress(stakeHolder));
        stakeHolder.setAddress(addressList);
        stakeHolder.setUsername(stakeHolder.getEmailId());
        stakeHolder.setPassword(stakeHolder.getMobileNumber());
        stakeHolder.setType(UserType.BUSINESS);
        stakeHolderService.save(stakeHolder);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("msg.create.stakeholder.success", null, null));
        return "redirect:/stakeholder/result/" + stakeHolder.getId();
    }

    private CorrespondenceAddress setCorrespondenceAddress(final StakeHolder stakeHolder) {
        final CorrespondenceAddress correspondenceAddress = new CorrespondenceAddress();
        correspondenceAddress.setHouseNoBldgApt(stakeHolder.getCorrespondenceAddress().getHouseNoBldgApt());
        correspondenceAddress.setStreetRoadLine(stakeHolder.getCorrespondenceAddress().getStreetRoadLine());
        correspondenceAddress.setAreaLocalitySector(stakeHolder.getCorrespondenceAddress().getAreaLocalitySector());
        correspondenceAddress.setCityTownVillage(stakeHolder.getCorrespondenceAddress().getCityTownVillage());
        correspondenceAddress.setDistrict(stakeHolder.getCorrespondenceAddress().getDistrict());
        correspondenceAddress.setState(stakeHolder.getCorrespondenceAddress().getState());
        correspondenceAddress.setPinCode(stakeHolder.getCorrespondenceAddress().getPinCode());
        correspondenceAddress.setUser(stakeHolder);
        return correspondenceAddress;
    }

    private PermanentAddress setPermanentAddress(final StakeHolder stakeHolder) {
        final PermanentAddress permanentAddress = new PermanentAddress();
        permanentAddress.setHouseNoBldgApt(stakeHolder.getPermanentAddress().getHouseNoBldgApt());
        permanentAddress.setStreetRoadLine(stakeHolder.getPermanentAddress().getStreetRoadLine());
        permanentAddress.setAreaLocalitySector(stakeHolder.getPermanentAddress().getAreaLocalitySector());
        permanentAddress.setCityTownVillage(stakeHolder.getPermanentAddress().getCityTownVillage());
        permanentAddress.setDistrict(stakeHolder.getPermanentAddress().getDistrict());
        permanentAddress.setState(stakeHolder.getPermanentAddress().getState());
        permanentAddress.setPinCode(stakeHolder.getPermanentAddress().getPinCode());
        permanentAddress.setUser(stakeHolder);
        return permanentAddress;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editStakeholder(@PathVariable final Long id, final Model model) {
        final StakeHolder stakeHolder = stakeHolderService.findById(id);

        for (final Address address : stakeHolder.getAddress())
            if (AddressType.CORRESPONDENCE.equals(address.getType()))
                stakeHolder.setCorrespondenceAddress((CorrespondenceAddress) address);
            else
                stakeHolder.setPermanentAddress((PermanentAddress) address);
        model.addAttribute(STAKE_HOLDER, stakeHolder);
        return STAKEHOLDER_UPDATE;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateStakeholder(@ModelAttribute(STAKE_HOLDER) final StakeHolder stakeHolder,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors, final RedirectAttributes redirectAttributes) {
        stakeHolderService.removeAddress(stakeHolder.getAddress());
        final List<Address> addressList = new ArrayList<>();
        addressList.add(stakeHolder.getCorrespondenceAddress());
        addressList.add(stakeHolder.getPermanentAddress());
        stakeHolder.setAddress(addressList);
        stakeHolderService.update(stakeHolder);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("msg.update.stakeholder.success", null, null));
        return "redirect:/stakeholder/result/" + stakeHolder.getId();
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String resultStakeHolder(@PathVariable final Long id, final Model model) {
        model.addAttribute(STAKE_HOLDER, stakeHolderService.findById(id));
        return STAKEHOLDER_RESULT;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewStakeHolder(@PathVariable final Long id, final Model model) {
        model.addAttribute(STAKE_HOLDER, stakeHolderService.findById(id));
        return STAKEHOLDER_VIEW;
    }

    @RequestMapping(value = "/search/update", method = RequestMethod.GET)
    public String searchEditStakeHolder(final Model model) {
        model.addAttribute(STAKE_HOLDER, new StakeHolder());
        return SEARCH_STAKEHOLDER_EDIT;
    }

    @RequestMapping(value = "/search/update", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getStakeHolderResultForEdit(@ModelAttribute final StakeHolder stakeHolder, final Model model) {
        final List<StakeHolder> searchResultList = stakeHolderService.search(stakeHolder);
        return new StringBuilder(DATA).append(toJSON(searchResultList, StakeHolder.class, StakeHolderJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/search/view", method = RequestMethod.GET)
    public String searchViewStakeHolder(final Model model) {
        model.addAttribute(STAKE_HOLDER, new StakeHolder());
        return SEARCH_STAKEHOLDER_VIEW;
    }

    @RequestMapping(value = "/search/view", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getStakeHolderForView(@ModelAttribute final StakeHolder stakeHolder, final Model model) {
        final List<StakeHolder> searchResultList = stakeHolderService.search(stakeHolder);
        return new StringBuilder(DATA).append(toJSON(searchResultList, StakeHolder.class, StakeHolderJsonAdaptor.class))
                .append("}")
                .toString();
    }
}