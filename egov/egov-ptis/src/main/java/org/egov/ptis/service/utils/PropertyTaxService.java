/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2020  eGovernments Foundation
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

package org.egov.ptis.service.utils;

import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.dao.InstallmentHibDao;
import org.egov.infra.admin.master.repository.ModuleRepository;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.view.InstDmdCollInfo;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyTaxService {

    private static final String ASSESSMENT = "Assessment";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private InstallmentHibDao<?, ?> installmentDao;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<PropertyTaxDetails> getPropertyTaxDetails(final String assessmentNo, final String ownerName,
            final String mobileNumber, final String category, final String doorNo) {
        final List<BasicProperty> basicProperties = basicPropertyDAO.getBasicPropertiesForTaxDetails(assessmentNo,
                ownerName, mobileNumber, category, doorNo);
        List<PropertyTaxDetails> propTxDetailsList = new ArrayList<>();
        if (null != basicProperties && !basicProperties.isEmpty())
            for (final BasicProperty basicProperty : basicProperties) {
                final PropertyTaxDetails propertyTaxDetails = propertyExternalService.getPropertyTaxDetails(basicProperty,
                        category);
                propTxDetailsList.add(propertyTaxDetails);
            }
        else {
            PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PROPERTY_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(ASSESSMENT + PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
            propertyTaxDetails.setErrorDetails(errorDetails);
            propTxDetailsList.add(propertyTaxDetails);
        }
        return propTxDetailsList;
    }

    public List<String> getAssessmentsByOwnerOrMobile(final String assessmentNo, final String ownerName,
            final String mobileNumber) {
        final List<BasicProperty> basicProperties = basicPropertyDAO.getBasicPropertiesForTaxDetails(assessmentNo,
                ownerName, mobileNumber);
        List<String> assessmentList = new ArrayList<String>();
        if (!basicProperties.isEmpty()) {
            basicProperties.forEach(basicProperty -> assessmentList.add(basicProperty.getUpicNo()));
        }
        return assessmentList;
    }

    public BigDecimal getCourtVerdictAndWriteOffAmount(final PropertyMVInfo propMatView) {
        BigDecimal amount = BigDecimal.ZERO;
        amount = amount.add(nullCheckBigDecimal(propMatView.getArrearCourtVerdictAmount()))
                .add(nullCheckBigDecimal(propMatView.getArrearPenaltyCourtVerdictAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentFirstHalfCourtVerdictAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentSecondHalfCourtVerdictAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentPenaltyCourtVerdictAmount()))
                .add(nullCheckBigDecimal(propMatView.getArrearWriteOffAmount()))
                .add(nullCheckBigDecimal(propMatView.getArrearPenaltyWriteOffAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentFirstHalfWriteOffAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentSecondHalfWriteOffAmount()))
                .add(nullCheckBigDecimal(propMatView.getCurrentPenaltyWriteOffAmount()));
        return amount;
    }

    public BigDecimal nullCheckBigDecimal(BigDecimal value) {

        return value != null ? value : BigDecimal.ZERO;
    }

    public String getArrearsPeriod(final PropertyMVInfo propMatView) {
        List<InstDmdCollInfo> instDemandCollList = new LinkedList<>(propMatView.getInstDmdColl());
        Collections.sort(instDemandCollList, (o1, o2) -> o1.getInstallment() - o2.getInstallment());
        String arrearPerFrom = "";
        String arrearPerTo = "";
        if (!propMatView.getInstDmdColl().isEmpty()) {
            arrearPerTo = installmentDao
                    .getInsatllmentByModuleForGivenDate(moduleRepository.findByName(PropertyTaxConstants.PTMODULENAME), DateUtils
                            .add(propertyTaxCommonUtils.getCurrentInstallment().getFromDate(), Calendar.DAY_OF_MONTH, -1))
                    .getDescription();
            arrearPerFrom = installmentDao.findById(instDemandCollList.get(0).getInstallment(), false).getDescription();
        }
        return arrearPerFrom.isEmpty() ? "NA" : arrearPerFrom.concat(" to ").concat(arrearPerTo);
    }
}
