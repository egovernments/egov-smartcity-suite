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
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;

public class DocumentTypeAction extends DispatchAction {
	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeAction.class);
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
			throw new EGOVRuntimeException("Exception occurred in beforeCreate", ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward createDocumentType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = "";
		try {
			final DocumentTypeForm dtf = (DocumentTypeForm) form;
			EgwTypeOfWork typeOfWork;
			final Timestamp curDate = new Timestamp(System.currentTimeMillis());
			typeOfWork = new EgwTypeOfWork();

			if (dtf.getCode() != null && dtf.getCode().length() > 0) {
				typeOfWork.setCode(dtf.getCode());
			}
			if (dtf.getParentId() != null && !dtf.getParentId().equals("0")) {
				final EgwTypeOfWork typeOfWork1 = this.commonsService.getTypeOfWorkById(Long.parseLong(dtf.getParentId()));
				typeOfWork.setParentid(typeOfWork1);
			}
			if (dtf.getPartyTypeId() != null && !dtf.getPartyTypeId().equals("0")) {
				final EgPartytype egPartytype = this.commonsService.getPartytypeById(Integer.parseInt(dtf.getPartyTypeId()));
				typeOfWork.setEgPartytype(egPartytype);
			}
			if (dtf.getDescription() != null && dtf.getDescription().length() > 0) {
				typeOfWork.setDescription(dtf.getDescription());
			}

			typeOfWork.setCreatedby((Integer) req.getSession().getAttribute("com.egov.user.LoginUserId"));
			typeOfWork.setCreateddate(curDate);

			this.commonsService.createEgwTypeOfWork(typeOfWork);

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
			LOGGER.error("Exception occurred in createDocumentType", ex);
			throw new EGOVRuntimeException("Exception occurred in createDocumentType", ex);

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in createDocumentType", ex);
			throw new EGOVRuntimeException("Exception occurred in createDocumentType", ex);

		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	public ActionForward beforeDocumentTypeSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			final String mode = req.getParameter("mode");
			req.setAttribute("mode", mode);
			target = "searchDocumentTypeList";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in beforeDocumentTypeSearch", ex);
			throw new EGOVRuntimeException("Exception occurred in beforeDocumentTypeSearch", ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward getDocumentTypeSearchList(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		List<EgwTypeOfWork> documentTypeDetailList = null;
		DocumentTypeForm dtf = null;
		try {
			dtf = (DocumentTypeForm) form;
			final String code = dtf.getCode();
			final String parentCode = dtf.getParentCode();
			final String description = dtf.getDescription();
			final String partyTypeCode = dtf.getAppliedToCode();

			if (dtf.getMode().equalsIgnoreCase("view")) {
				documentTypeDetailList = this.commonsService.getTypeOfWorkDetailFilterByParty(code, parentCode, description, partyTypeCode);
			} else if (dtf.getMode().equalsIgnoreCase("modify")) {
				documentTypeDetailList = this.commonsService.getTypeOfWorkDetailFilterByParty(code, parentCode, description, partyTypeCode);
			}
			req.setAttribute("documentTypeDetailList", documentTypeDetailList);

			String mode = req.getParameter("mode");
			if (mode == null) {
				mode = "view";
			}
			req.setAttribute("mode", mode);

			target = "searchDocumentTypeList";

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in getDocumentTypeSearchList", ex);
			throw new EGOVRuntimeException("Exception occurred in getDocumentTypeSearchList", ex);

		}

		return mapping.findForward(target);
	}

	public ActionForward beforeViewAndModifyDocumentType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		try {
			final DocumentTypeForm dtf = (DocumentTypeForm) form;
			final String documentTypeId = req.getParameter("documentTypeId");
			final EgwTypeOfWork typeOfWork = this.commonsService.getTypeOfWorkById(Long.parseLong(documentTypeId));
			dtf.setId(typeOfWork.getId().toString());
			dtf.setCode(typeOfWork.getCode());
			if (typeOfWork.getParentid() != null) {
				dtf.setParentId(typeOfWork.getParentid().getId().toString());
			}

			if (typeOfWork.getEgPartytype() != null) {
				dtf.setPartyTypeId(typeOfWork.getEgPartytype().getId().toString());
			}

			dtf.setDescription(typeOfWork.getDescription());

			req.getSession().setAttribute("mode", req.getParameter("mode"));
			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in beforeViewAndModifyDocumentType", ex);
			throw new EGOVRuntimeException("Exception occurred in beforeViewAndModifyDocumentType", ex);

		}
		return mapping.findForward(target);
	}

	public ActionForward modifyDocumentType(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		String alertMessage = "";
		try {

			final DocumentTypeForm dtf = (DocumentTypeForm) form;
			final Timestamp curDate = new Timestamp(System.currentTimeMillis());
			final String typeOfWorkId = dtf.getId();
			final EgwTypeOfWork typeOfWork = this.commonsService.getTypeOfWorkById(Long.parseLong(typeOfWorkId));

			if (dtf.getCode() != null && dtf.getCode().length() > 0) {
				typeOfWork.setCode(dtf.getCode());
			}
			if (dtf.getParentId() != null && !dtf.getParentId().equals("0")) {
				final EgwTypeOfWork typeOfWork1 = this.commonsService.getTypeOfWorkById(Long.parseLong(dtf.getParentId()));
				typeOfWork.setParentid(typeOfWork1);
			} else {
				typeOfWork.setParentid(null);
			}

			if (dtf.getPartyTypeId() != null && !dtf.getPartyTypeId().equals("0")) {
				final EgPartytype egPartytype = this.commonsService.getPartytypeById(Integer.parseInt(dtf.getPartyTypeId()));
				typeOfWork.setEgPartytype(egPartytype);
			} else {
				typeOfWork.setEgPartytype(null);
			}

			if (dtf.getDescription() != null && dtf.getDescription().length() > 0) {
				typeOfWork.setDescription(dtf.getDescription());
			}

			typeOfWork.setLastmodifiedby((Integer) req.getSession().getAttribute("com.egov.user.LoginUserId"));
			typeOfWork.setLastmodifieddate(curDate);
			this.commonsService.updateEgwTypeOfWork(typeOfWork);

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
			LOGGER.error("Exception occurred in modifyDocumentType", ex);
			throw new EGOVRuntimeException("Exception occurred in modifyDocumentType", ex);

		} catch (final Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in createDocumentType", ex);
			throw new EGOVRuntimeException("Exception occurred in createDocumentType", ex);

		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
}
