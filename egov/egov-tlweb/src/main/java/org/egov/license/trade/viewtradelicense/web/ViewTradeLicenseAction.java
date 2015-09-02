package org.egov.license.trade.viewtradelicense.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.rjbac.user.ejb.api.UserManager;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatus;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.web.BaseLicenseAction;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.pims.service.EisManager;
import org.egov.web.annotation.ValidationErrorPageExt;
import org.egov.web.utils.ServletActionRedirectResult;

@ParentPackage("egov")
@Result(name="auditReport",type=ServletActionRedirectResult.class,value="auditReport",params={"moduleName","TL","namespace","/egi/auditing","method","searchForm","actionName","auditReport","prependServletContext","false"})
public class ViewTradeLicenseAction extends BaseLicenseAction implements ServletRequestAware {
	private static final Logger LOGGER = Logger.getLogger(ViewTradeLicenseAction.class);
	private static final long serialVersionUID = 1L;
	@SuppressWarnings({ "rawtypes", "unused" })
    private WorkflowService tradeLicenseWorkflowService;
	private TradeService ts;
	protected TradeLicense tradeLicense = new TradeLicense();
	private DocumentManagerService<Notice> documentManagerService;
	private String rejectreason;
	private HttpSession session = null;
	private HttpServletRequest requestObj;
	private Integer userId;
	private final String CITIZENUSER = "citizenUser";

	/**
	 * @return the rejectreason
	 */
	public String getRejectreason() {
		return this.rejectreason;
	}

	/**
	 * @param rejectreason the rejectreason to set
	 */
	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}

	@Override
	public Object getModel() {
		return this.tradeLicense;

	}
	
	@SuppressWarnings("rawtypes")
    public void setTradeLicenseWorkflowService(final WorkflowService tradeLicenseWorkflowService) {
		this.tradeLicenseWorkflowService = tradeLicenseWorkflowService;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	@Override
	public String showForApproval() {
		 LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.license().getId());
		LOGGER.debug("Exiting from the showForApproval method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return super.showForApproval();
	}

	public String view() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		LOGGER.debug("Exiting from the view method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.VIEW;
	}
	
	public String viewCitizen() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		session = requestObj.getSession();
		UserDAO userDao = new UserDAO();
		User user = userDao.getUserByUserName(CITIZENUSER);
		userId = user.getId();
		EGOVThreadLocals.setUserId(userId.toString());
		session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		LOGGER.debug("Exiting from the view Citizen method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.VIEW;
	}

	public String generateCertificate() {
		 LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		String certificate = Constants.CNCCERTIFICATE;
		setLicenseIdIfServletRedirect();

		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		if (this.documentManagerService.getDocumentObject(this.tradeLicense.getApplicationNumber(), "egtradelicense") == null) {
			ViewTradeLicenseAction.LOGGER.debug("Creating Certificate object for DMS");
			final Notice notice = new Notice();
			notice.setDocumentNumber(this.tradeLicense.getApplicationNumber());
			notice.setDomainName(EGOVThreadLocals.getDomainName());
			notice.setModuleName("egtradelicense");
			notice.setNoticeType(this.license().getClass().getSimpleName() + "-Certificate");
			notice.setNoticeDate(new Date());
			this.request.put("noticeObject", notice);
		}
		this.tradeLicense.setIsCertificateGenerated(true);
		if (this.tradeLicense.getFeeTypeStr().equalsIgnoreCase(Constants.PFA)) {
			certificate = Constants.PFACERTIFICATE;
		} else if (this.tradeLicense.getFeeTypeStr().equalsIgnoreCase(Constants.CNC)) {
			certificate = Constants.CNCCERTIFICATE;
		}
		 LOGGER.debug("Exiting from the generateCertificate method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return certificate;
	}
	
	public String generateNoc() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		setLicenseIdIfServletRedirect();
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		LOGGER.debug("Exiting from the generate NOC method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "noc";
	}
	
	@SuppressWarnings("unchecked")
	public String createNoc() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.persistenceService.setType(TradeLicense.class);
		setLicenseIdIfServletRedirect();
		final TradeLicense modifiedTL = this.tradeLicense;
		this.tradeLicense = (TradeLicense) this.persistenceService.findById(modifiedTL.getId(), false);
		this.tradeLicense.setSandBuckets(modifiedTL.getSandBuckets());
		this.tradeLicense.setWaterBuckets(modifiedTL.getWaterBuckets());
		this.tradeLicense.setDcpExtinguisher(modifiedTL.getDcpExtinguisher());
		final String runningNumber = this.service().getNextRunningLicenseNumber(Constants.TL_PROVISIONAL_NOC_NUMBER);
		this.tradeLicense.generateNocNumber(runningNumber);
		this.service().endWorkFlowForLicense(tradeLicense);
		final LicenseStatus activeStatus = (LicenseStatus) this.persistenceService.find("from org.egov.license.domain.entity.LicenseStatus where code='ACT'");
		tradeLicense.setStatus(activeStatus);
		this.persistenceService.update(this.tradeLicense);
		LOGGER.debug("Exiting from the generate NOC method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "createnoc";
	}
	
	public String duplicateNoc()
	{
		return "duplicatenoc";
	}
	
	public String generateDuplicateNoc()
	{
		setLicenseIdIfServletRedirect();
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		return "createnoc";
	}

	private void setLicenseIdIfServletRedirect() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		if (this.tradeLicense.getId() == null) {
			if (this.getSession().get("model.id") != null) {
				this.tradeLicense.setId(Long.valueOf((Long) this.getSession().get("model.id")));
				this.getSession().remove("model.id");
			}
		}
		 LOGGER.debug("Exiting from the setLicenseIdIfServletRedirect method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@SkipValidation
	public String generateRejCertificate() {
		LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		setLicenseIdIfServletRedirect();
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		LOGGER.debug("Exiting from the generateRejCertificate method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "rejCertificate";
	}

	@SkipValidation
	public String certificateForRej() {
		 LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.getSession().get("model.id");
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.license().getId());
		LOGGER.debug("Exiting from the certificateForRej method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return "certificateForRej";
	}

	public String duplicateCertificate() {
		return "duplicate";
	}

	@Override
	protected License license() {
		return this.tradeLicense;
	}

	@Override
	@SkipValidation
	@ValidationErrorPageExt(action = "approve", makeCall = true, toMethod = "setupWorkflowDetails")
	public String approve() {
		 LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		 Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
			if (userId != null) {
				setRoleName(licenseUtils.getRolesForUserId(userId));
			}
		String docNumber = this.tradeLicense.getDocNumber();
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		this.tradeLicense.setDocNumber(docNumber);
		 LOGGER.debug("Exiting from the approve method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return super.approve();
	}
	
	
	@SkipValidation
	@ValidationErrorPageExt(
			action = "approveRenew", makeCall = true, toMethod = "setupWorkflowDetails")
	public String approveRenew() {
		 LOGGER.debug("Trade License Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		 Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
			if (userId != null) {
				setRoleName(licenseUtils.getRolesForUserId(userId));
			}
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		 LOGGER.debug("Exiting from the approveRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return super.approveRenew();
	}

	@SuppressWarnings("unchecked")
    @Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

	public void setEisCommonsManager(final EisCommonsManager eisCommonsManager) {
		this.eisCommonsManager = eisCommonsManager;
	}

	public void setEisManager(final EisManager eisManager) {
		this.eisManager = eisManager;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	public void setUserManager(final UserManager usrManager) {
		this.userManager = usrManager;
	}

	public void setDocumentManagerService(final DocumentManagerService<Notice> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}
	
	@Override
	@SkipValidation
    public void setServletRequest(HttpServletRequest arg0) {
        this.requestObj = arg0;
    }

}