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
package org.egov.ptis.service.aadharseeding;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.ptis.bean.aadharseeding.AadharSearchResult;
import org.egov.ptis.bean.aadharseeding.AadharSeeding;
import org.egov.ptis.bean.aadharseeding.AadharSeedingDetails;
import org.egov.ptis.bean.aadharseeding.AadharSeedingRequest;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.repository.aadharseeding.AadharSeedingRepository;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class AadharSeedingService extends GenericWorkFlowController {
    private static final String REGISTERED_WILL_DOCUMENT = "Registered Will Document";

    private static final String REGISTERED = "REGISTERED";

    private static final String REGISTERED_DOCUMENT = "Registered Document";

    private static final String TITLEDEED = "TITLEDEED";

    private static final String SALEDEED = "SALEDEED";

    private static final String PARTISION = "PARTISION";

    private static final String DECREE_BY_CIVIL_COURT = "Decree by Civil Court";

    private static final String CIVILCOURTDECREE = "CIVILCOURTDECREE";

    private static final String UPDATED = "UPDATED";

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private AadharSeedingRepository aadharSeedingRepository;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    public void addModelAttributes(final Model model) {
        final List<Boundary> revWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                WARD,
                ADMIN_HIERARCHY_TYPE);
        Map<Long, String> wards = new ConcurrentHashMap<>();
        Map<Long, String> electionWards = new ConcurrentHashMap<>();
        for (Boundary revWard : revWardList) {
            wards.put(revWard.getId(), revWard.getName());
        }
        for (Boundary electionWard : electionWardList) {
            electionWards.put(electionWard.getId(), electionWard.getName());
        }
        model.addAttribute("aadharSeeding", new AadharSeedingRequest());
        model.addAttribute("wardId", wards);
        model.addAttribute("electionWardId", electionWards);
    }

    public List<AadharSearchResult> prepareOutput(AadharSeedingRequest aadharSeedingRequest) {
        List<String[]> aadharSeeding = getQueryResult(aadharSeedingRequest);
        List<AadharSearchResult> asrList = new ArrayList<>();
        for (Object[] str : aadharSeeding) {
            AadharSearchResult arsObj = new AadharSearchResult();
            arsObj.setAddress(str[3].toString());
            arsObj.setAssessmentNo(str[0].toString());
            arsObj.setDoorNo(str[2] == null ? "NA" : str[2].toString());
            arsObj.setOwnerName(str[1] == null ? "NA" : str[1].toString());
            asrList.add(arsObj);
        }
        return asrList;
    }

    @SuppressWarnings("unchecked")
    public List<String[]> getQueryResult(AadharSeedingRequest aadharSeedingRequest) {
        StringBuilder baseQry = new StringBuilder();
        StringBuilder orderBy = new StringBuilder();
        baseQry = baseQry.append("select mv.propertyId, mv.ownerName, mv.houseNo, mv.propertyAddress from PropertyMaterlizeView mv ")
                .append("where mv.basicPropertyID in(select p.basicProperty from PropertyImpl p where ")
                .append("p.propertyDetail.structure=false and p.status in('A','I') and p.id not in(select m.property from PropertyMutation m ")
                .append("where m.state.status <> 2)) and mv.basicPropertyID not in(select basicProperty from AadharSeeding)");
        final StringBuilder wherClause = new StringBuilder();
        orderBy = orderBy.append(" order by mv.propertyId");
        if (isNotBlank(aadharSeedingRequest.getAssessmentNo()))
            wherClause.append(" and mv.propertyId= '" + aadharSeedingRequest.getAssessmentNo() + "'");
        if (isNotBlank(aadharSeedingRequest.getDoorNo()))
            wherClause.append(" and mv.houseNo like '" + aadharSeedingRequest.getDoorNo() + "" + "%'");
        if (aadharSeedingRequest.getElectionWardId() != null)
            wherClause.append(" and mv.electionWard.id=" + aadharSeedingRequest.getElectionWardId());
        if (aadharSeedingRequest.getWardId() != null)
            wherClause.append(" and mv.ward.id=" + aadharSeedingRequest.getWardId());
        StringBuilder searchQry = baseQry.append(wherClause).append(orderBy);
        return entityManager.unwrap(Session.class).createQuery(searchQry.toString()).setMaxResults(500).list();
    }

    public void addPropertyDetailstoModel(final Model model, final String assessmentNo, final String status) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        AadharSeedingRequest formData = new AadharSeedingRequest();
        formData.setAssessmentNo(assessmentNo);
        formData.setOwnershipCategory(basicProperty.getProperty().getPropertyDetail().getCategoryType());
        basicProperty.getProperty();
        formData.setDoorNo(basicProperty.getAddress().getHouseNoBldgApt() != null
                ? basicProperty.getAddress().getHouseNoBldgApt() : "N/A");
        formData.setZoneName(basicProperty.getPropertyID().getZone().getName());
        formData.setRevenueWardName(basicProperty.getPropertyID().getWard().getName());
        formData.setBlockName(basicProperty.getPropertyID().getArea().getName());
        formData.setElectionWardName(basicProperty.getPropertyID().getElectionBoundary().getName());
        formData.setLocalty(basicProperty.getPropertyID().getLocality().getName());
        formData.setLatitude(basicProperty.getLatitude());
        formData.setLongitude(basicProperty.getLongitude());
        formData.setExtentOfSite(BigDecimal.valueOf(basicProperty.getProperty().getPropertyDetail().getSitalArea() == null ? 0
                : basicProperty.getProperty().getPropertyDetail().getSitalArea().getArea()));
        formData.setPlinthArea(
                BigDecimal.valueOf(basicProperty.getProperty().getPropertyDetail().getTotalBuiltupArea() == null ? 0
                        : basicProperty.getProperty().getPropertyDetail().getTotalBuiltupArea().getArea()));
        formData.setPropertyType(basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getType());
        setDocumentDetails(basicProperty, formData);
        formData.setSurveyNumber(basicProperty.getProperty().getPropertyDetail().getSurveyNumber());
        formData.setAddress(basicProperty.getAddress().toString());
        formData.setPropertyOwnerInfo(basicProperty.getPropertyOwnerInfo());
        if (status.equals(UPDATED)) {
            AadharSeeding aadharSeeding = aadharSeedingRepository
                    .getAadharSeedingByBasicProperty((BasicPropertyImpl) basicProperty);
            List<PropertyOwnerInfo> ownerList = new ArrayList<>();
            PropertyOwnerInfo propertyOwnerInfo;
            for (AadharSeedingDetails aadharSeedingDetails : aadharSeeding.getAadharSeedingDetails()) {
                propertyOwnerInfo = new PropertyOwnerInfo();
                propertyOwnerInfo.setOwner(userService.getUserById(aadharSeedingDetails.getOwner()));
                ownerList.add(propertyOwnerInfo);
            }
            formData.setPropertyOwnerInfoProxy(ownerList);
            model.addAttribute("aadharSeedingDetails", aadharSeeding.getAadharSeedingDetails());

        } else {
            formData.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
        }
        model.addAttribute("aadharSeedingUpdate", formData);
    }

    @Transactional
    public void saveSeedingDetails(AadharSeedingRequest aadharSeedingRequest) {
        WorkFlowMatrix wfmatrix;
        BasicPropertyImpl basicProperty = (BasicPropertyImpl) basicPropertyDAO
                .getBasicPropertyByPropertyID(aadharSeedingRequest.getAssessmentNo());
        if (aadharSeedingRepository.getAadharSeedingByBasicProperty(basicProperty) == null) {
            AadharSeeding aadharSeeding = new AadharSeeding();
            List<AadharSeedingDetails> detailsList = new ArrayList<>();
            aadharSeeding.setBasicProperty(
                    (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(aadharSeedingRequest.getAssessmentNo()));
            aadharSeeding.setStatus(UPDATED);
            aadharSeeding.setFlag(false);
            wfmatrix = propertyWorkflowService.getWfMatrix("AadharSeeding", null,
                    null, "AADHAR SEEDING", "AadharSeeding:New", null);
            startWorkflow(wfmatrix, aadharSeeding);
            for (PropertyOwnerInfo owner : aadharSeedingRequest.getPropertyOwnerInfoProxy()) {
                AadharSeedingDetails aadharSeedingDetails = new AadharSeedingDetails();
                aadharSeedingDetails.setAadharSeeding(aadharSeeding);
                aadharSeedingDetails.setOwner(owner.getUserId());
                aadharSeedingDetails.setAadharNo(owner.getOwner().getAadhaarNumber());
                detailsList.add(aadharSeedingDetails);
            }
            aadharSeeding.setAadharSeedingDetails(detailsList);
            aadharSeedingRepository.save(aadharSeeding);
        }
    }

    private void startWorkflow(WorkFlowMatrix wfmatrix,
            AadharSeeding aadharSeeding) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        aadharSeeding.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                .withStateValue(wfmatrix.getNextState())
                .withDateInfo(currentDate.toDate()).withNextAction(wfmatrix.getNextAction());
    }

    @Transactional
    public void approveAadharSeeding(String assessmentList) {
        JSONObject obj = new JSONObject(assessmentList.trim());
        JSONArray jsonArr = new JSONArray(obj.get("info").toString());
        List<String> assessments = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {
            final org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
            assessments.add(jsonObj.get("propertyId").toString());
        }
        for (String assessmentNo : assessments) {
            BasicPropertyImpl basicProperty = (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
            AadharSeeding aadharSeeding = aadharSeedingRepository.getAadharSeedingByBasicProperty(basicProperty);
            aadharSeeding.setStatus("APPROVED");
            WorkFlowMatrix wfmatrix;
            wfmatrix = propertyWorkflowService.getWfMatrix("AadharSeeding", null,
                    null, "AADHAR SEEDING", "AadharSeeding:Updated", null);
            transitionWorkflow(wfmatrix, aadharSeeding);
            aadharSeedingRepository.save(aadharSeeding);
        }
    }

    private void transitionWorkflow(WorkFlowMatrix wfmatrix,
            AadharSeeding aadharSeeding) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        aadharSeeding.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                .withStateValue(wfmatrix.getNextState())
                .withDateInfo(currentDate.toDate()).withNextAction(wfmatrix.getNextAction());
    }

    public List<AadharSearchResult> getProperties() {
        List<AadharSeeding> seedingResult = aadharSeedingRepository.findAllByStatus(UPDATED);
        List<AadharSearchResult> resultList = new ArrayList<>();
        AadharSearchResult asrObj = null;
        for (AadharSeeding seeding : seedingResult) {
            asrObj = new AadharSearchResult();
            asrObj.setAssessmentNo(seeding.getBasicProperty().getUpicNo());
            asrObj.setDoorNo(seeding.getBasicProperty().getAddress().getHouseNoBldgApt());
            asrObj.setAddress(seeding.getBasicProperty().getAddress().toString());
            asrObj.setOwnerName(seeding.getBasicProperty().getFullOwnerName());
            resultList.add(asrObj);
        }
        return resultList;
    }

    private void setDocumentDetails(BasicProperty basicProperty, AadharSeedingRequest formData) {
        final Query getdocumentsQuery = entityManager.createNamedQuery("DOCUMENT_TYPE_DETAILS_BY_ID");
        getdocumentsQuery.setParameter("basicProperty", basicProperty.getId());
        List<DocumentTypeDetails> documentTypeDetails = (List<DocumentTypeDetails>) getdocumentsQuery.getResultList();
        PropertyMutation mutation = propertyTaxCommonUtils.getLatestApprovedMutationForAssessmentNo(basicProperty.getUpicNo());
        if (mutation != null) {
            if (mutation.getMutationReason().getCode().equals(CIVILCOURTDECREE)) {
                setDocNoDateAndType(formData, mutation.getDecreeNumber(), mutation.getDecreeDate(), DECREE_BY_CIVIL_COURT);
            } else if (Arrays.asList(PARTISION, SALEDEED, TITLEDEED).contains(mutation.getMutationReason().getCode())) {
                setDocNoDateAndType(formData, mutation.getDeedNo(), mutation.getDeedDate(), REGISTERED_DOCUMENT);
            } else if (mutation.getMutationReason().getCode().equals(REGISTERED)) {
                setDocNoDateAndType(formData, mutation.getDeedNo(), mutation.getDeedDate(), REGISTERED_WILL_DOCUMENT);
            }
        } else if (!documentTypeDetails.isEmpty()) {
            setDocNoDateAndType(formData, documentTypeDetails.get(0).getDocumentNo(),
                    documentTypeDetails.get(0).getDocumentDate(), documentTypeDetails.get(0).getDocumentName());
        } else {
            setDocNoDateAndType(formData, basicProperty.getRegdDocNo(), basicProperty.getRegdDocDate(), "N/A");
        }
    }

    private void setDocNoDateAndType(AadharSeedingRequest formData, String docNo, Date docDate, String docType) {
        formData.setDocNo(StringUtils.isBlank(docNo) ? "N/A" : docNo);
        formData.setDocDate(docDate == null ? null : docDate);
        formData.setDocumentType(StringUtils.isBlank(docType) ? "N/A" : docType);
    }
}