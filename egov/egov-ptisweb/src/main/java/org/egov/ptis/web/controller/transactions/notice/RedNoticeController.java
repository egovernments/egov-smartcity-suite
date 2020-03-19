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
package org.egov.ptis.web.controller.transactions.notice;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.RedNoticeInfo;
import org.egov.ptis.domain.service.notice.RedNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/rednotice")
public class RedNoticeController {

    private static final String REDNOTICESEARCH_FORM = "rednotice-search";
    private static final String REDNOTICE_VIEW = "rednotice-view";
    private static final String ERROR_MSG = "errorMsg";
    private static final String REDNOTICEINFO = "redNoticeInfo";
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private RedNoticeService redNoticeService;
    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(final Model model) {
        model.addAttribute(REDNOTICEINFO, new RedNoticeInfo());
        return REDNOTICESEARCH_FORM;
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("redNoticeInfo") final RedNoticeInfo redNoticeInfo, final Model model) {
        RedNoticeInfo redNotice;
        ModelAndView modelAndView = new ModelAndView();
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(redNoticeInfo.getAssessmentNo());
        if (basicProperty == null) {
            model.addAttribute(REDNOTICEINFO, redNoticeInfo);
            model.addAttribute(ERROR_MSG, "invalid.assessment");
            modelAndView.setViewName(REDNOTICESEARCH_FORM);
            return modelAndView;
        } else if (basicProperty.getProperty().getIsExemptedFromTax()) {
            model.addAttribute(REDNOTICEINFO, redNoticeInfo);
            model.addAttribute(ERROR_MSG, "exempted.assessment");
            modelAndView.setViewName(REDNOTICESEARCH_FORM);
            return modelAndView;
        } else {
            redNotice = redNoticeService.getRedNoticeInformation(redNoticeInfo.getAssessmentNo());
            model.addAttribute(REDNOTICEINFO, redNotice);
            if (redNotice.getInstallmentCount()) {
                model.addAttribute(ERROR_MSG, "rednotice.no.due");
                modelAndView.setViewName(REDNOTICESEARCH_FORM);
                return modelAndView;
            }
            modelAndView.setViewName(REDNOTICE_VIEW);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/generatenotice", method = RequestMethod.GET, params = { "assessmentNo" })
    public @ResponseBody ResponseEntity<byte[]> generateNotice(@RequestParam(value = "assessmentNo") String assessmentNo,
            final HttpServletRequest request, final Model model) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=RedNotice_"+assessmentNo+".pdf");
        final ReportOutput reportOutput = redNoticeService.generateNotice(assessmentNo,
                PropertyTaxConstants.NOTICE_TYPE_RED_NOTICE);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
