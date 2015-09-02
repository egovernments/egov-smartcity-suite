/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.service.objection;

import java.util.Date;
import java.util.List;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsManager;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseStatus;
import org.egov.tradelicense.domain.entity.WorkflowBean;
import org.egov.tradelicense.domain.entity.objection.Activity;
import org.egov.tradelicense.domain.entity.objection.LicenseObjection;
import org.egov.tradelicense.domain.entity.objection.Notice;
import org.egov.tradelicense.domain.service.BaseLicenseService;
import org.egov.tradelicense.utils.Constants;

public class ObjectionService extends PersistenceService<LicenseObjection, Long> {
	protected EisCommonsManager eisCommonsManager;
	protected SequenceGenerator sequenceGenerator;
	protected BaseLicenseService licenseService;
	private SimpleWorkflowService<LicenseObjection> objectionWorkflowService;
	private String contextName;
	
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	// set the licenseService in applicationcontext.xml
	// eg:TradeService
	@SuppressWarnings("unchecked")
	public LicenseObjection recordObjection(LicenseObjection objection, Long licenseId, WorkflowBean workflowBean) {
		// Objection objection=license.getObjections().get(0);
		final String runningNumber = this.getNextRunningNumber(Constants.OBJECTIONNUMBERPREFIX);
		objection.generateNumber(runningNumber);
		final License license = (License) this.licenseService.getPersistenceService().find("from License where id=?", licenseId);
		objection.setLicense(license);
		this.persist(objection);
		this.initiateWorkflow(objection, workflowBean);
		return objection;
	}

	private void initiateWorkflow(LicenseObjection objection, WorkflowBean workflowBean) {
		final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		this.objectionWorkflowService.start(objection, position, workflowBean.getComments());
		final LicenseStatus objectedStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='OBJ'");
		objection.getLicense().setStatus(objectedStatus);
		this.processWorkflow(objection, workflowBean);
	}

	public SimpleWorkflowService<LicenseObjection> getObjectionWorkflowService() {
		return this.objectionWorkflowService;
	}

	public void setObjectionWorkflowService(SimpleWorkflowService<LicenseObjection> objectionWorkflowService) {
		this.objectionWorkflowService = objectionWorkflowService;
	}

	private void processWorkflow(LicenseObjection objection, WorkflowBean workflowBean) {
		if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_APPROVED, position, workflowBean.getComments());
			objection.getCurrentState().setText2(contextName);
			position = this.eisCommonsManager.getPositionByUserId(objection.getCreatedBy().getId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_GENERATESUSPENSIONLETTER, position, workflowBean.getComments());
		} else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD) && ((objection.getActivities() != null && !objection.getActivities().isEmpty()) && ("Inspection").equalsIgnoreCase(objection.getActivities().get(0).getType()))) {
			final Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PIGENERATED, nextPosition, workflowBean.getComments());
		} else if (objection.getState().getValue().equalsIgnoreCase(Constants.NEW) && workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD) && ((objection.getActivities() != null && !objection.getActivities().isEmpty()) && ("Response").equalsIgnoreCase(objection.getActivities().get(0).getType()))) {
			final Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.NEW, nextPosition, workflowBean.getComments());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			final Position nextPosition = this.eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition, workflowBean.getComments());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (objection.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
				final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_GENERATECANCELLATIONLETTER, position, workflowBean.getComments());
			} else {
				final Position position = this.eisCommonsManager.getPositionByUserId(objection.getCreatedBy().getId());
				objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_REJECTED, position, workflowBean.getComments());
			}
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
			final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			this.objectionWorkflowService.end(objection, position);
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='SUS'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDREJECTIONLETTER)) {
			final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			this.objectionWorkflowService.end(objection, position);
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='REJ'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDPN)) {
			final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_PNGENERATED, position, workflowBean.getComments());
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
			objection.getLicense().setStatus(activeStatus);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDSCN)) {
			final Position position = this.eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + Constants.WORKFLOW_STATE_SCNGENERATED, position, workflowBean.getComments());
			// identify the state for all objections
			final LicenseStatus activeStatus = (LicenseStatus) this.licenseService.getPersistenceService().find("from LicenseStatus where code='ACT'");
			objection.getLicense().setStatus(activeStatus);
		}
		objection.getCurrentState().setText2(contextName);		
	}

	public BaseLicenseService getLicenseService() {
		return this.licenseService;
	}

	public void setLicenseService(BaseLicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public String getNextRunningNumber(String type) {
		final Sequence seq = this.sequenceGenerator.getNextNumberWithFormat(type, Constants.APPLICATIONNO_LENGTH, new Character('0'));
		return seq.getFormattedNumber();
	}

	public void setEisCommonsManager(EisCommonsManager eisCommonsManager) {
		this.eisCommonsManager = eisCommonsManager;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public LicenseObjection recordResponseOrInspection(LicenseObjection objection, WorkflowBean workflowBean) {
		if (objection.getActivities().get(objection.getActivities().size()-1).getType() != null || workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			final Activity activity = objection.getActivities().get(objection.getActivities().size()-1);
			objection = this.find("from org.egov.license.domain.entity.objection.LicenseObjection where id=?", objection.getId());
			activity.setObjection(objection);
			objection.getActivities().add(activity);
			if(objection.getActivities().get(objection.getActivities().size()-1).getType() != null && 
					(objection.getActivities().get(objection.getActivities().size()-1).getType().equals("PreNotice")|| 
					objection.getActivities().get(objection.getActivities().size()-1).getType().equals("SCNotice")||
					objection.getActivities().get(objection.getActivities().size()-1).getType().equals("suspend")||
					objection.getActivities().get(objection.getActivities().size()-1).getType().equals("cancelled"))){
			final Notice notice = getNotices(objection.getActivities().get(objection.getActivities().size()-1).getType(), objection);
			notice.setObjection(objection);
			objection.getNotices().add(notice);
			}
			this.persist(objection);
			this.processWorkflow(objection, workflowBean);			
		} else {
			objection = this.find("from org.egov.license.domain.entity.objection.LicenseObjection where id=?", objection.getId());
			this.processWorkflow(objection, workflowBean);	
			/*int userId = workflowBean.getApproverUserId();			
			if (userId == -1) {
				if(objection.getCurrentState().getValue().equals(State.NEW)) {
					this.objectionWorkflowService.end(objection, objection.getCurrentState().getOwner());
					return objection; 
				}				
			}
			final Position nextPosition = userId == -1 ? objection.getState().getPrevious().getOwner() : this.eisCommonsManager.getPositionByUserId(userId);
			objection.changeState(Constants.WORKFLOW_STATE_TYPE_OBJECTLICENSE + (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT) ? Constants.WORKFLOW_STATE_REJECTED : Constants.WORKFLOW_STATE_FORWARDED), nextPosition, workflowBean.getComments());
			objection.getCurrentState().setText2(contextName);*/
		}
		return objection;
	}

	private Notice getNotices(String noticeType, LicenseObjection objection) {
		final Notice notice=new Notice();
		if(objection.getActivities().get(objection.getActivities().size()-1).getType().equals(noticeType)){
			final String runningNumber = this.getNextRunningNumber(Constants.OBJECTIONNOICENUMBERPREFIX);
			notice.generateNumber(runningNumber);
			//notice.setNoticeNumber(objection.getNumber());
			notice.setDocNumber(objection.getNumber() + "_"+noticeType);
			notice.setNoticeType(objection.getClass().getSimpleName() + "_"+noticeType);
			notice.setNoticeDate(new Date());
			notice.setModuleName("egtradelicense");
		}
		return notice;
	}
}
