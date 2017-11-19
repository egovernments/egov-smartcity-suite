/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.ptis.domain.service.deactivation;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_BAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_DEACT_RSN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.ptis.domain.dao.property.PropertyMutationMasterHibDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyDeactivation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.repository.deactivation.DeactivationRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PropertyDeactivationService {

    private static final String ACTIVE = "ACTIVE";

    @Autowired
    private PropertyMutationMasterHibDAO propertyMutationMasterHibDAO;

    @Autowired
    private DeactivationRepository deactivationRepository;

    @Autowired
    private PropertyService propService;

    @Autowired
    private PropertyService propertyService;

    public List<String> getDeactivationReasons() {
        List<String> rsnList = new ArrayList<>();
        List<PropertyMutationMaster> reasons = propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(PROP_DEACT_RSN);
        for (PropertyMutationMaster rsn : reasons) {
            rsnList.add(rsn.getMutationDesc());
        }
        return rsnList;
    }

    public Map<String, String> getPropertyDetails(final BasicProperty basicProperty) {
        Map<String, String> propdetails = new LinkedHashMap<>();
        propdetails.put("AssessmentNo", basicProperty.getUpicNo());
        propdetails.put("OwnerName", basicProperty.getFullOwnerName());
        propdetails.put("PropertyTaxDue", getCurrentPTTaxDue(basicProperty).toString());
        propdetails.put("PropertyType",
                basicProperty.getActiveProperty().getPropertyDetail().getCategoryType().toLowerCase());
        propdetails.put("Address", basicProperty.getAddress().toString());
        return propdetails;
    }

    public List<DocumentType> getDocuments(final TransactionType transactionType) {
        return propService.getDocumentTypesForTransactionType(transactionType);
    }

    private BigDecimal getCurrentPTTaxDue(BasicProperty basicproperty) {
        BigDecimal currentPropertyTaxDue;
        BigDecimal arrearPropertyTaxDue;
        final Map<String, BigDecimal> propertyTaxDetails = propertyService
                .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());
        Map<String, BigDecimal> currentTaxAndDue = propertyService.getCurrentTaxAndBalance(propertyTaxDetails,
                new Date());
        currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
        arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));

        return currentPropertyTaxDue.add(arrearPropertyTaxDue);
    }

    public boolean checkActiveWC(List<Map<String, Object>> wcDetails) {
        boolean connStatus = false;
        for (Map<String, Object> status : wcDetails) {
            for (Object state : status.values()) {
                if (ACTIVE.equalsIgnoreCase(state.toString())) {
                    connStatus = true;
                }
            }
        }
        return connStatus;

    }

    @Transactional
    public void save(final PropertyDeactivation propertyDeactivation) {
        deactivationRepository.save(propertyDeactivation);
    }

    public DocumentType getDocType(String docname) {
        return deactivationRepository.findDocumentTypeByName(docname);

    }

}
