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
package org.egov.lib.deduction.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.EgPartytype;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;

public class PartyTypeAction extends DispatchAction {
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyTypeAction.class);
	private CommonsService commonsService;

	public void setCommonsService(final CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public ActionForward beforeCreate(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			req.getSession().setAttribute("mode", "create");
			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in beforeCreate", ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward createPartyType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = "";
		try {
			final PartyTypeForm ptf = (PartyTypeForm) form;
			EgPartytype ptype = null;
			final Integer userId = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			final Timestamp curDate = new Timestamp(System.currentTimeMillis());

			ptype = new EgPartytype();

			if (ptf.getCode() != null && ptf.getCode().length() > 0) {
				ptype.setCode(ptf.getCode());
			}
			if (ptf.getParentId() != null && !ptf.getParentId().equals("0")) {
				final EgPartytype ptobj = this.commonsService.getPartytypeById(Integer.parseInt(ptf.getParentId()));
				ptype.setEgPartytype(ptobj);
			}
			if (ptf.getDescription() != null && ptf.getDescription().length() > 0) {
				ptype.setDescription(ptf.getDescription());
			}

			ptype.setCreatedby(new BigDecimal(userId));
			ptype.setCreateddate(curDate);

			this.commonsService.createEgPartytype(ptype);

			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");

			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

			req.setAttribute("buttonType", req.getParameter("button"));
			HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			target = "success";
		} catch (final RuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in createPartyType", ex);
			throw new EGOVRuntimeException("Exception occurred in createPartyType", ex);

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in createPartyType", ex);
			throw new EGOVRuntimeException("Exception occurred in createPartyType", ex);

		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	public ActionForward beforePartyTypeSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			final String mode = req.getParameter("mode");
			req.setAttribute("mode", mode);
			target = "searchPartyTypeList";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in beforePartyTypeSearch", ex);
			throw new EGOVRuntimeException("Exception occurred in beforePartyTypeSearch", ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward getPartyTypeSearchList(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		List<EgPartytype> partyTypeDetailList = null;
		PartyTypeForm ptf = null;
		try {
			ptf = (PartyTypeForm) form;

			final String code = ptf.getCode();
			final String parentCode = ptf.getParentCode();
			final String description = ptf.getDescription();

			if (ptf.getMode().equalsIgnoreCase("view")) {
				partyTypeDetailList = this.commonsService.getPartyTypeDetailFilterBy(code, parentCode, description);
			} else if (ptf.getMode().equalsIgnoreCase("modify")) {
				partyTypeDetailList = this.commonsService.getPartyTypeDetailFilterBy(code, parentCode, description);
			}

			req.setAttribute("partyTypeDetailList", partyTypeDetailList);

			String mode = req.getParameter("mode");
			if (mode == null) {
				mode = "view";
			}
			req.setAttribute("mode", mode);
			target = "searchPartyTypeList";

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in getPartyTypeSearchList", ex);
			throw new EGOVRuntimeException("Exception occurred in getPartyTypeSearchList", ex);

		}

		return mapping.findForward(target);
	}

	public ActionForward beforeViewAndModifyPartyType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			final PartyTypeForm ptf = (PartyTypeForm) form;
			final String partyTypeId = req.getParameter("partyTypeId");
			final EgPartytype ptype = this.commonsService.getPartytypeById(Integer.parseInt(partyTypeId));
			ptf.setId(ptype.getId().toString());
			ptf.setCode(ptype.getCode());

			if (ptype.getEgPartytype() != null) {
				ptf.setParentId(ptype.getEgPartytype().getId().toString());
			}

			ptf.setDescription(ptype.getDescription());
			req.getSession().setAttribute("mode", req.getParameter("mode"));
			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in beforeViewAndModifyPartyType", ex);
			throw new EGOVRuntimeException("Exception occurred in beforeViewAndModifyPartyType", ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward modifyPartyType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = "";
		try {
			final PartyTypeForm ptf = (PartyTypeForm) form;
			final Timestamp curDate = new Timestamp(System.currentTimeMillis());
			final String partyTypeId = ptf.getId();
			final EgPartytype ptype = this.commonsService.getPartytypeById(Integer.parseInt(partyTypeId));

			if (ptf.getCode() != null && ptf.getCode().length() > 0) {
				ptype.setCode(ptf.getCode());
			}
			if (ptf.getParentId() != null && !ptf.getParentId().equals("0")) {
				final EgPartytype ptype1 = this.commonsService.getPartytypeById(Integer.parseInt(ptf.getParentId()));
				ptype.setEgPartytype(ptype1);
			} else {
				ptype.setEgPartytype(null);
			}

			if (ptf.getDescription() != null && ptf.getDescription().length() > 0) {
				ptype.setDescription(ptf.getDescription());
			}

			ptype.setLastmodifiedby(new BigDecimal((Integer) req.getSession().getAttribute("com.egov.user.LoginUserId")));
			ptype.setLastmodifieddate(curDate);

			this.commonsService.updateEgPartytype(ptype);

			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");

			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

			req.setAttribute("buttonType", req.getParameter("button"));
			HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			target = "success";

		} catch (final RuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in modifyPartyType", ex);
			throw new EGOVRuntimeException("Exception occurred in modifyPartyType", ex);

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in createPartyType", ex);
			throw new EGOVRuntimeException("Exception occurred in createPartyType", ex);

		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

}
