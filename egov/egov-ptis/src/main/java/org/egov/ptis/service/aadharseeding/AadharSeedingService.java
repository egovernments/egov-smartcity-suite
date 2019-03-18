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
import static org.egov.ptis.constants.PropertyTaxConstants.CIVILCOURTDECREE;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_RULE_FULL_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_PARTISION;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_SALE;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_TITLEDEED;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_REGISTERED;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_SUCCESSION;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_RENT;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_REASON_CODE_UNREGISTEREDWILL;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCTYPEBYMUTATIONREASON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        baseQry = baseQry
                .append("select mv.propertyId, mv.ownerName, mv.houseNo, mv.propertyAddress from PropertyMaterlizeView mv ")
                .append("where mv.latitude is not null and mv.longitude is not null and mv.sitalArea > 10 and mv.basicPropertyID not in")
                .append("(select basicPropertyID from PropertyMaterlizeView where usage <>'VACANTLAND' and totalBuiltUpArea <= 0) ")
                .append("and mv.basicPropertyID in(select p.basicProperty from PropertyImpl p where ")
                .append("p.propertyDetail.structure=false and p.status in('A','I') and p.basicProperty not in(select m.basicProperty from PropertyMutation m ")
                .append("where m.state.status <> 2) and p.basicProperty not in(select psv.referenceBasicProperty from PropertyStatusValues psv ")
                .append("where psv.referenceBasicProperty is not null and psv.referenceBasicProperty.underWorkflow = true)) and ")
                .append("mv.basicPropertyID not in(select basicProperty from AadharSeeding where status <> 'CANCELED') and mv.locality not in(select id from")
                .append(" Boundary b where b.boundaryNum in(select boundaryNum from BhudharExemptedLocalities))");
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
                    .getNonCanceledAadharSeeding((BasicPropertyImpl) basicProperty);
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
        if (aadharSeedingRepository.getNonCanceledAadharSeeding(basicProperty) == null) {
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
    public void approveAadharSeeding(final String assessmentList) {
        JSONObject obj = new JSONObject(assessmentList.trim());
        JSONArray jsonArr = new JSONArray(obj.get("info").toString());
        Map<String, String> assessments = new HashMap<>();
        for (int i = 0; i < jsonArr.length(); i++) {
            final org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
            assessments.put(jsonObj.get("propertyId").toString(), jsonObj.get("mode").toString());
        }
        for (Map.Entry<String, String> entry : assessments.entrySet()) {
            BasicPropertyImpl basicProperty = (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(entry.getKey());
            AadharSeeding aadharSeeding = aadharSeedingRepository.getNonCanceledAadharSeeding(basicProperty);
            if (entry.getValue().equals("reject"))
                aadharSeeding.setStatus("CANCELED");
            else
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
                formData.setDocNo(StringUtils.isBlank(mutation.getDecreeNumber()) ? "N/A" : mutation.getDecreeNumber());
                formData.setDocDate(mutation.getDecreeDate());
                formData.setDocumentType(DOCTYPEBYMUTATIONREASON.get(mutation.getMutationReason().getCode()));
            } else if (mutation.getType().equals(ADDITIONAL_RULE_FULL_TRANSFER)) {
                formData.setDocNo(mutation.getMutationRegistrationDetails().getDocumentNo());
                formData.setDocDate(mutation.getMutationRegistrationDetails().getDocumentDate());
                formData.setDocumentType(DOCTYPEBYMUTATIONREASON.get(mutation.getMutationReason().getCode()));
            } else if (Arrays.asList(MUTATION_REASON_CODE_PARTISION, MUTATION_REASON_CODE_SALE, MUTATION_REASON_CODE_TITLEDEED,
                    MUTATION_REASON_CODE_REGISTERED, MUTATION_REASON_CODE_SUCCESSION, MUTATION_REASON_CODE_RENT,
                    MUTATION_REASON_CODE_UNREGISTEREDWILL).contains(mutation.getMutationReason().getCode())) {
                formData.setDocNo(mutation.getDeedNo());
                formData.setDocDate(mutation.getDeedDate());
                formData.setDocumentType(DOCTYPEBYMUTATIONREASON.get(mutation.getMutationReason().getCode()));
            }
        } else if (!documentTypeDetails.isEmpty()) {
            formData.setDocNo(StringUtils.isBlank(documentTypeDetails.get(0).getDocumentNo()) ? "N/A"
                    : documentTypeDetails.get(0).getDocumentNo());
            formData.setDocDate(
                    documentTypeDetails.get(0).getDocumentDate() == null ? null : documentTypeDetails.get(0).getDocumentDate());
            formData.setDocumentType(StringUtils.isBlank(documentTypeDetails.get(0).getDocumentName()) ? "N/A"
                    : documentTypeDetails.get(0).getDocumentName());
        } else {
            formData.setDocNo(StringUtils.isBlank(basicProperty.getRegdDocNo()) ? "N/A"
                    : basicProperty.getRegdDocNo());
            formData.setDocDate(
                    basicProperty.getRegdDocDate());
            formData.setDocumentType("N/A");
        }
    }
}