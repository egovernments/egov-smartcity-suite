/*
 * @(#)PartyTypeAction.java 3.0, 16 Jun, 2013 10:48:22 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
