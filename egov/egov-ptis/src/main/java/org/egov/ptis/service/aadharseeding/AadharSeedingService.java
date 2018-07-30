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
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.ptis.bean.aadharseeding.AadharSearchResult;
import org.egov.ptis.bean.aadharseeding.AadharSeeding;
import org.egov.ptis.bean.aadharseeding.AadharSeedingDetails;
import org.egov.ptis.bean.aadharseeding.AadharSeedingRequest;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.repository.aadharseeding.AadharSeedingRepository;
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
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;

    public void addModelAttributes(final Model model) {
        final List<Boundary> revWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                ELECTIONWARD_BNDRY_TYPE,
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

    public List<String[]> prepareOutput(AadharSeedingRequest aadharSeedingRequest) {
        List<String[]> aadharSeeding = getQueryResult(aadharSeedingRequest);
        List<AadharSearchResult> asrList = new ArrayList<>();
        for (Object[] str : aadharSeeding) {
            AadharSearchResult arsObj = new AadharSearchResult();
            arsObj.setAddress(str[3].toString());
            arsObj.setAssessmentNo(str[0].toString());
            arsObj.setDoorNo(str[2].toString());
            arsObj.setOwnerName(str[1].toString());
            asrList.add(arsObj);
        }
        return aadharSeeding;
    }

    @SuppressWarnings("unchecked")
    public List<String[]> getQueryResult(AadharSeedingRequest aadharSeedingRequest) {
        String searchQry = "";
        String baseQry = "select mv.propertyId, mv.ownerName, mv.houseNo, mv.propertyAddress from PropertyMaterlizeView mv "
                + "where mv.basicPropertyID in(select p.basicProperty from PropertyImpl p where p.source='SURVEY' and "
                + "p.propertyDetail.structure=false and p.status in('A','I') and p.id not in(select m.property from PropertyMutation m "
                + "where m.state.status <> 2)) and mv.basicPropertyID not in(select basicProperty from AadharSeeding)";
        String wherClause = "";
        String orderBy = " order by mv.propertyId";
        if (isNotBlank(aadharSeedingRequest.getAssessmentNo()))
            wherClause = " and mv.propertyId= '" + aadharSeedingRequest.getAssessmentNo() + "'";
        if (isNotBlank(aadharSeedingRequest.getDoorNo()))
            wherClause = " and mv.houseNo= '" + aadharSeedingRequest.getDoorNo() + "'";
        if (aadharSeedingRequest.getElectionWardId() != null)
            wherClause = " and mv.electionWard.id=" + aadharSeedingRequest.getElectionWardId();
        if (aadharSeedingRequest.getWardId() != null)
            wherClause = " and mv.ward.id=" + aadharSeedingRequest.getWardId();
        searchQry = baseQry + wherClause + orderBy;
        return entityManager.unwrap(Session.class).createQuery(searchQry).list();
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
        formData.setExtentOfSite(BigDecimal.valueOf(basicProperty.getProperty().getPropertyDetail().getSitalArea().getArea()));
        formData.setPlinthArea(BigDecimal.valueOf(basicProperty.getProperty().getPropertyDetail().getPlinthArea().getArea()));
        formData.setPropertyType(basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getType());
        formData.setDocNo(basicProperty.getRegdDocNo() == null ? "NA" : basicProperty.getRegdDocNo());
        formData.setDocDate(basicProperty.getRegdDocDate());
        formData.setSurveyNumber(basicProperty.getProperty().getPropertyDetail().getSurveyNumber());
        formData.setAddress(basicProperty.getAddress().toString());
        formData.setPropertyOwnerInfo(basicProperty.getPropertyOwnerInfo());
        if (status.equals(UPDATED)) {
            AadharSeeding aadharSeeding = aadharSeedingRepository
                    .getAadharSeedingByBasicProperty((BasicPropertyImpl) basicProperty);
            List<PropertyOwnerInfo> ownerList = new ArrayList();
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
        AadharSeeding aadharSeeding = new AadharSeeding();
        List<AadharSeedingDetails> detailsList = new ArrayList<>();
        aadharSeeding.setBasicProperty(
                (BasicPropertyImpl) basicPropertyDAO.getBasicPropertyByPropertyID(aadharSeedingRequest.getAssessmentNo()));
        aadharSeeding.setStatus(UPDATED);
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

    public void preparejasonData(List<String[]> aadharSeeding, List<AadharSearchResult> searchResultList) {
        for (Object[] obj : aadharSeeding) {
            AadharSearchResult resultObj = new AadharSearchResult();
            resultObj.setAddress(obj[3].toString());
            resultObj.setAssessmentNo(obj[0].toString());
            resultObj.setDoorNo(obj[2].toString());
            resultObj.setOwnerName(obj[1].toString());
            searchResultList.add(resultObj);
        }
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
}