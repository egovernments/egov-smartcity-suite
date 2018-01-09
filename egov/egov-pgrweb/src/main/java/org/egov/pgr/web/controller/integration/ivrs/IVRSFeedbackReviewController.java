/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.pgr.web.controller.integration.ivrs;

import org.egov.pgr.integration.ivrs.entiry.IVRSFeedback;
import org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReason;
import org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReview;
import org.egov.pgr.integration.ivrs.service.IVRSFeedbackReasonService;
import org.egov.pgr.integration.ivrs.service.IVRSFeedbackReviewService;
import org.egov.pgr.integration.ivrs.service.IVRSFeedbackService;
import org.egov.pgr.service.ComplaintHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REOPENED;

@Controller
@RequestMapping("/complaint/ivrs/feedbackreview/{crn}")
public class IVRSFeedbackReviewController {

    private static final String FEEDBACKREVIEW = "feedbackreview";

    @Autowired
    private ComplaintHistoryService complaintHistoryService;

    @Autowired
    private IVRSFeedbackReviewService ivrsFeedbackReviewService;

    @Autowired
    private IVRSFeedbackReasonService ivrsFeedbackReasonService;

    @Autowired
    private IVRSFeedbackService ivrsFeedbackService;

    @ModelAttribute("ivrsFeedbackReview")
    public IVRSFeedbackReview ivrsFeedbackReview(@PathVariable String crn) {
        return ivrsFeedbackReviewService.getExistingFeedbackReviewByCRN(crn).orElse(new IVRSFeedbackReview());
    }

    @ModelAttribute("feedbackReasons")
    public List<IVRSFeedbackReason> feedbackReasons() {
        return ivrsFeedbackReasonService.getAllFeedbackReason();
    }

    @GetMapping
    public String showFeedbackReviewForm(@PathVariable String crn,
                                         @ModelAttribute IVRSFeedbackReview ivrsFeedbackReview,
                                         Model model) {
        IVRSFeedback complaintFeedback = ivrsFeedbackService.getComplaintByCRN(crn);
        model.addAttribute("complaintHistory", complaintHistoryService.getHistory(complaintFeedback.getComplaint()));
        model.addAttribute("complaint", complaintFeedback.getComplaint());
        model.addAttribute("rating", complaintFeedback.getIvrsRating());
        return FEEDBACKREVIEW;
    }

    @PostMapping
    public String createFeedbackReview(@PathVariable String crn,
                                       @Valid @ModelAttribute("ivrsFeedbackReview") IVRSFeedbackReview ivrsFeedbackReview,
                                       BindingResult bindingResult, RedirectAttributes responseAttrbs) {
        if (bindingResult.hasErrors())
            return "redirect:/complaint/ivrs/feedbackreview/" + crn;
        if (COMPLAINT_REOPENED.equals(ivrsFeedbackReview.getComplaint().getStatus().getName()))
            responseAttrbs.addFlashAttribute("successMessage", "msg.feedback.reopen");
        else {
            if (ivrsFeedbackReview.isExisting())
                ivrsFeedbackReviewService.updateFeedbackReview(ivrsFeedbackReview);
            else
                ivrsFeedbackReviewService.createFeedbackReview(ivrsFeedbackReview);
            responseAttrbs.addFlashAttribute("successMessage", "msg.feedback.success");
        }
        return "redirect:/complaint/ivrs/feedbackreview/" + crn;
    }
}
