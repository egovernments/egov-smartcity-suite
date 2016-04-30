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
package org.egov.bpa.services.extd.common;

//import static org.egov.portal.utils.ServiceConstants.USER_TYPE_SURVEYOR;

//TODO: Removed from POrtal ProjectSurveyor and below things from
/*import org.egov.portal.model.ActiveServiceRegistry;
import org.egov.portal.model.ServiceRequestRegistry;
import org.egov.portal.services.PortalIntegrationService;
import org.egov.portal.surveyor.model.Surveyor;
import org.egov.portal.surveyor.model.SurveyorServiceRequestRegistry;
*/
public class BpaSurvayorPortalExtnService { 
	//private PortalIntegrationService portalIntegrationService;
	private String requestUnderProgress = "Request under progress";
	private String lpReplyReceived = "LP reply recieved";
	private String challanNoticeSent = "Challan Notice Sent to Citizen";
	private String lpSentToApplicant = "Letter sent to applicant for clarification";
	private String revisedFeeInitiated = "Revised Fee Initiated";
	private String siteInspected = "Site inspected";
	private String siteInspectedByLS = "Site Inspected By LS";
	private String siteInspectionScheduledByLS = "Site Inspection Scheduled By LS";

	/*private void initServicePackForSurvayor() {
		portalIntegrationService.initServicePackByUserType(USER_TYPE_SURVEYOR);
	}

	public SurveyorServiceRequestRegistry getServiceRequestRegistryByEntityRefNo(String psnNumber) {
		initServicePackForSurvayor();
		return (SurveyorServiceRequestRegistry) portalIntegrationService
				.getServiceRequestRegistryByEntityRefNo(psnNumber);
	}

	public ServiceRequestRegistry createServiceRequestRegistry(RegistrationExtn registration, String statuscode,
			String message, Surveyor surveyor) {
		initServicePackForSurvayor();

		SurveyorServiceRequestRegistry ssrs = (SurveyorServiceRequestRegistry) portalIntegrationService
				.getServiceRequestRegistryInstance();
		ssrs.setServiceRegistry(portalIntegrationService.getServiceRegistryById(registration.getServiceRegistryId()));
		ssrs.setRequestID(registration.getRequest_number());
		ssrs.setEntityRefNo(registration.getPlanSubmissionNum());
		ssrs.setApplicationRefNo(registration.getId().toString());
		ssrs.setRequestStatus(statuscode);
		ssrs.setMessage(message);
		ssrs.setPortalUser(surveyor);
		return portalIntegrationService.createServiceRequestRegistry(ssrs);

	}

	public ActiveServiceRegistry createActiveServiceRegistry(RegistrationExtn registration) {
		if (registration != null) {
			initServicePackForSurvayor();
			return portalIntegrationService.createActiveServiceRegistry(registration.getPlanSubmissionNum(),
					registration.getRequest_number(), BpaConstants.APPLICATIONCLOSED,
					registration.getServiceRegistryId());

		} else
			return null;
	}

	public void deleteServiceRequestRegistry(RegistrationExtn registration) {
		initServicePackForSurvayor();
		portalIntegrationService.deleteServiceRequestRegistry(registration.getRequest_number());
	}

	public void updateServiceRequestRegistry(RegistrationExtn registration) {

		if (registration != null && registration.getRequest_number() != null
				&& !"".equals(registration.getRequest_number())) {
			initServicePackForSurvayor();
			ServiceRequestRegistry serviceReqRegistry = portalIntegrationService
					.getServiceRequestRegistryByEntityRefNo(registration.getPlanSubmissionNum());
			// serviceReqRegistry.setRequestStatus(requestStatus);
			if (serviceReqRegistry != null
					&& registration.getEgwStatus() != null 
					&& (registration.getEgwStatus().getCode()
							.equalsIgnoreCase(BpaConstants.CITIZENAPPLICATIONREGISTERED))) {
				serviceReqRegistry.setRequestStatus(BpaConstants.APPLICATION_FWDED_TO_LS);
			} else {
				if (serviceReqRegistry != null
						&& registration.getEgwStatus() != null
						&& (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.REJECTORDERISSUED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.REJECTORDERPREPARED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.LETTERTOPARTYREPLYRECEIVED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.LETTERTOPARTYRAISED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.INSPECTIONSCHEDULED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.RevisedFeeInitiated)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.CHALLANNOTICESENT)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.APPLICANTSIGNUPDATED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.CHALLANAMOUNTCOLLECTED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.ORDERISSUEDTOAPPLICANT)
								|| registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.ORDERPREPARED)
								|| registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.STATUSAPPROVED)
								|| registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CANCELLED) || registration
								.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.INSPECTED)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.SITEINSPECTIONSCHEDULEDBYLS)
								|| registration.getEgwStatus().getCode()
										.equalsIgnoreCase(BpaConstants.INSPECTEDBYLS)

						)) {

					if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.LETTERTOPARTYREPLYRECEIVED)) {

						serviceReqRegistry.setRequestStatus(lpReplyReceived);
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.LETTERTOPARTYRAISED)) {

						serviceReqRegistry.setRequestStatus(lpSentToApplicant);
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.RevisedFeeInitiated)) {

						serviceReqRegistry.setRequestStatus(revisedFeeInitiated);
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.STATUSAPPROVED)) {

						serviceReqRegistry.setRequestStatus(requestUnderProgress);
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CHALLANNOTICESENT)) {
						
						serviceReqRegistry.setRequestStatus(challanNoticeSent);
						
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CHALLANAMOUNTCOLLECTED)) {
						
						serviceReqRegistry.setRequestStatus(requestUnderProgress);
						
					} 
					else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.APPLICANTSIGNUPDATED))
					{
						serviceReqRegistry.setRequestStatus(BpaConstants.APPLICANTSIGNUPDATED);
					}
					else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.ORDERPREPARED))
					{
					//	createActiveServiceRegistry(registration);
						deleteServiceRequestRegistry(registration);
						serviceReqRegistry.setRequestStatus(registration.getEgwStatus().getCode());
					}
					else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.REJECTORDERISSUED)) {
						// TODO: CHECK WHETHER WE ARE CLOSING ON ISSUE OF
						// RECTION
						// ORDER
					//	createActiveServiceRegistry(registration);
						deleteServiceRequestRegistry(registration);
						serviceReqRegistry.setRequestStatus(registration.getEgwStatus().getCode());

					} else if (registration.getEgwStatus().getCode()
							.equalsIgnoreCase(BpaConstants.ORDERISSUEDTOAPPLICANT)) {
						// TODO: CHECK WHETHER WE ARE CLOSING ON ISSUE OF order
					//	createActiveServiceRegistry(registration);
						deleteServiceRequestRegistry(registration);
						serviceReqRegistry.setRequestStatus(registration.getEgwStatus().getCode());
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CANCELLED)) {
						serviceReqRegistry.setRequestStatus(registration.getEgwStatus().getCode());
						// deleteServiceRequestRegistry(registration);

					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.INSPECTED)) {
						serviceReqRegistry.setRequestStatus(siteInspected);
					}
					else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.SITEINSPECTIONSCHEDULEDBYLS)) {
						serviceReqRegistry.setRequestStatus(siteInspectionScheduledByLS);
					} else if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.INSPECTEDBYLS)) {
						serviceReqRegistry.setRequestStatus(siteInspectedByLS);
					}
				} else {
					serviceReqRegistry.setRequestStatus(registration.getEgwStatus().getCode());
				}
				//portalIntegrationService.updateServiceRequestRegistry(serviceReqRegistry);
			}
		}
	}

	public void setPortalIntegrationService(PortalIntegrationService portalIntegrationService) {
		this.portalIntegrationService = portalIntegrationService;
	}*/

}
