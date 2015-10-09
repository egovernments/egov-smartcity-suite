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
package org.egov.ptis.domain.service.report;

import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BaseRegisterResult;
import org.egov.ptis.domain.entity.property.FloorDetailsView;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ReportService {

    private PersistenceService propPerServ;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    /**
     * Method gives List of properties with current and arrear individual demand
     * details
     * 
     * @param ward
     * @param block
     * @return
     */
    public List<BaseRegisterResult> getPropertyByWardAndBlock(final String ward, final String block) {

        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("from PropertyMaterlizeView pmv ");

        if (StringUtils.isNotBlank(ward))
            queryStr.append(" where pmv.ward.id=:ward ");
        if (StringUtils.isNotBlank(block))
            queryStr.append(" and pmv.block.id=:block ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            query.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            query.setLong("block", Long.valueOf(block));

        List<PropertyMaterlizeView> properties = query.list();
        List<BaseRegisterResult> baseRegisterResultList = new ArrayList<BaseRegisterResult>();

        for (PropertyMaterlizeView propMatView : properties) {
            BaseRegisterResult baseRegisterResultObj = new BaseRegisterResult();
            baseRegisterResultObj.setAssessmentNo(propMatView.getPropertyId());
            baseRegisterResultObj.setDoorNO(propMatView.getHouseNo());
            baseRegisterResultObj.setOwnerName(propMatView.getOwnerName());
            baseRegisterResultObj.setIsExempted(propMatView.getIsExempted());
            baseRegisterResultObj.setCourtCase(Boolean.FALSE);

            PropertyTypeMaster propertyType = null;
            if (null != propMatView.getPropTypeMstrID()) {
                propertyType = (PropertyTypeMaster) getPropPerServ().find("from PropertyTypeMaster where id = ?",
                        propMatView.getPropTypeMstrID().getId());
            }

            BigDecimal totalArrearPropertyTax = BigDecimal.ZERO;
            BigDecimal totalArrearEduCess = BigDecimal.ZERO;
            BigDecimal totalArreaLibCess = BigDecimal.ZERO;
            BigDecimal arrearPenaltyFine = BigDecimal.ZERO;
            List<InstDmdCollMaterializeView> instDemandCollList = new ArrayList<InstDmdCollMaterializeView>(
                    propMatView.getInstDmdColl());
            for (InstDmdCollMaterializeView instDmdCollObj : instDemandCollList) {
                if (instDmdCollObj.getInstallment().equals(propertyTaxUtil.getCurrentInstallment())) {
                    if (propertyType.getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
                        baseRegisterResultObj.setPropertyTax(instDmdCollObj.getVacantLandTax());
                    } else {
                        baseRegisterResultObj.setPropertyTax(instDmdCollObj.getGeneralTax());
                    }
                    baseRegisterResultObj.setEduCessTax(instDmdCollObj.getEduCessTax());
                    baseRegisterResultObj.setLibraryCessTax(instDmdCollObj.getLibCessTax());
                    baseRegisterResultObj.setPenaltyFines(instDmdCollObj.getPenaltyFinesTax());
                    baseRegisterResultObj.setCurrTotal(instDmdCollObj.getGeneralTax()
                            .add(instDmdCollObj.getEduCessTax()).add(instDmdCollObj.getLibCessTax()));
                } else {
                    if (propertyType.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                        totalArrearPropertyTax = totalArrearPropertyTax.add(instDmdCollObj.getVacantLandTax());
                    else
                        totalArrearPropertyTax = totalArrearPropertyTax.add(instDmdCollObj.getGeneralTax());
                    totalArrearEduCess = totalArrearEduCess.add(instDmdCollObj.getEduCessTax());
                    totalArreaLibCess = totalArreaLibCess.add(instDmdCollObj.getLibCessTax());
                    arrearPenaltyFine = arrearPenaltyFine.add(instDmdCollObj.getPenaltyFinesTax());
                }
            }
            List<String> classificationOfBuilding = new ArrayList<String>();
            List<String> natureOfUsage = new ArrayList<String>();
            List<BigDecimal> area = new ArrayList<BigDecimal>();
            List<FloorDetailsView> floorDetails = new ArrayList<FloorDetailsView>(propMatView.getFloorDetails());
            if (!propertyType.getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
                for (FloorDetailsView floor : floorDetails) {
                    classificationOfBuilding.add(floor.getClassification());
                    natureOfUsage.add(floor.getPropertyUsage());
                    area.add(floor.getBuiltUpArea());
                }
            }
            baseRegisterResultObj.setPropertyUsage(natureOfUsage);
            baseRegisterResultObj.setClassificationOfBuilding(classificationOfBuilding);
            baseRegisterResultObj.setArea(area);
            String arrearPerFrom = "";
            String arrearPerTo = "";
            if (instDemandCollList.size() > 1) {
                arrearPerTo = dateFormatter.format(instDemandCollList.get(instDemandCollList.size() - 2)
                        .getInstallment().getToDate());
                arrearPerFrom = dateFormatter.format(instDemandCollList.get(0).getInstallment().getFromDate());
                baseRegisterResultObj.setArrearPeriod(arrearPerFrom + "-" + arrearPerTo);
            } else {
                baseRegisterResultObj.setArrearPeriod("N/A");
            }
            
            baseRegisterResultObj.setArrearTotal(totalArrearPropertyTax.add(totalArrearEduCess).add(totalArreaLibCess));
            baseRegisterResultObj.setArrearPropertyTax(totalArrearPropertyTax);
            baseRegisterResultObj.setArrearLibraryTax(totalArreaLibCess);
            baseRegisterResultObj.setArrearEduCess(totalArrearEduCess);
            baseRegisterResultObj.setArrearPenaltyFines(arrearPenaltyFine);
            baseRegisterResultObj.setPropertyType(propertyType.getCode());
            baseRegisterResultList.add(baseRegisterResultObj);
        }
        return baseRegisterResultList;
    }

    public PersistenceService getPropPerServ() {
        return propPerServ;
    }

    public void setPropPerServ(PersistenceService propPerServ) {
        this.propPerServ = propPerServ;
    }

}
