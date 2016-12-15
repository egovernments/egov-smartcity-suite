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
package org.egov.works.web.controller.mb;

import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/mb")
public class ViewMBController {

    @Autowired
    private MBHeaderService mBHeaderService;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/view/{mbheaderId}", method = RequestMethod.GET)
    public String showSearchWorkOrder(@PathVariable final String mbheaderId, final Model model) {
        final MBHeader mBHeader = mBHeaderService.getMBHeaderById(Long.parseLong(mbheaderId));
        for (final MBDetails mBDetail : mBHeader.getMbDetails())
            if (mBDetail != null) {
                final Double prevCumulativeAmount = mBHeaderService.getPreviousCumulativeQuantity(mBHeader.getId(),
                        mBDetail.getWorkOrderActivity().getId());
                if (prevCumulativeAmount != null)
                    mBDetail.setPrevCumlvQuantity(prevCumulativeAmount);
            }
        mBHeader.setDocumentDetails(worksUtils.findByObjectIdAndObjectType(mBHeader.getId(), WorksConstants.MBHEADER));
        model.addAttribute("mbHeader", mBHeader);
        model.addAttribute("nameOfWork", mBHeader.getWorkOrderEstimate().getEstimate().getName());
        model.addAttribute("loaNumber", mBHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
        model.addAttribute("loaAmount", mBHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount());
        model.addAttribute("documentDetails", mBHeader.getDocumentDetails());
        model.addAttribute("mode", "view");
        model.addAttribute("workflowHistory",
                worksUtils.getHistory(mBHeader.getState(), mBHeader.getStateHistory()));
        return "mbheader-view";
    }

}
