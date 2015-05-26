/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 *      1) All versions of this program, verbatim or modified must carry this 
 *         Legal Notice.
 * 
 *      2) Any misrepresentation of the origin of the material is prohibited. It 
 *         is required that all modified versions of this material be marked in 
 *         reasonable ways as different from the original version.
 * 
 *      3) This license does not grant any rights to any user of the program 
 *         with regards to rights under trademark law for use of the trade names 
 *         or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.utils;

import static org.egov.eb.utils.EBConstants.STATUS_TNEB_BILL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.CommonsService;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.infstr.ValidationError;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class EBUtils {
        
        private static final Logger LOGGER = Logger.getLogger(EBUtils.class);
        private static final Integer MONTHS_TOBE_SUBTRACTED = -2;
        public static final BigDecimal EBBILL_VARIANCE_PERCENTAGE1 = new BigDecimal("-20");
        public static final BigDecimal EBBILL_VARIANCE_PERCENTAGE2 = new BigDecimal("20");
        @Autowired
        private static FinancialYearDAO financialYearDAO;
        private static EgwStatusHibernateDAO egwStatusHibernateDAO;
        public static final List<String> TNEB_BILLING_TYPES = new ArrayList<String>() {
                {
                        add(EBConstants.Billing_Odd_Month);
                        add(EBConstants.Billing_Even_Month);
                }
                
        };
        
        public static String getTargetArea(EBDetails ebDetails) {
                return (String)HibernateUtil.getCurrentSession()
                                .createQuery("select area.name from TargetAreaMappings where boundary = :mappedWard")
                                .setEntity("mappedWard", ebDetails.getEbConsumer().getWard())
                                .list().get(0);
        }
        
        public static Date getBillDate(Date dueDate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dueDate);
                calendar.add(Calendar.MONTH, MONTHS_TOBE_SUBTRACTED);
                return calendar.getTime();
        }
        
        public static int getYear(Date billDueDate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getBillDate(billDueDate));
                return calendar.get(Calendar.YEAR);
        }
        
        public static final List<ValidationError> genericErrorMessage = Arrays.asList(new ValidationError(
                        EBConstants.ERROR_MESSAGE, EBConstants.ERROR_MESSAGE));
        
        
        /**
         * Gives the group string by combining the <code> Month</code>, <code> Year</code>, 
         * <code> TargetArea</code> and <code> Region </code>
         * 
         * @param ebDetails
         * @return group string
         */
        public static String getEBBillGroup(EBDetails ebDetails) {
                return new StringBuilder().append(ebDetails.getMonth()).append("-")
                                        .append(EBUtils.getYear(ebDetails.getDueDate())).append("-")
                                        .append(ebDetails.getArea().getName()).append("-")
                                        .append(ebDetails.getEbConsumer().getRegion()).toString();
        }
        
        /**
         * Gives the short month name
         * 
         * @param month
         * @return short month name
         */
        public static String getShortMonthName(int month) {
                return DateUtils.getAllMonthsWithFullNames().get(month).substring(0, 3);
        }
        
        /**
         * Gives the TNEB bill status by code
         * 
         * @param statusCode
         * @return
         */
        public static EgwStatus getBillEgwStatusByCode(String statusCode) {
                return egwStatusHibernateDAO.getStatusByModuleAndCode(STATUS_TNEB_BILL, statusCode);
        }
        
        /**
         * Gives the current financial year
         * 
         * @return
         */
        public static CFinancialYear getCurrentFinancialYear() {
                CFinancialYear financialYear;
                
                
                 financialYear = financialYearDAO.getFinancialYearByDate(new Date());
                return financialYear;
        }
        
        /**
         * Gives the current financial year
         * 
         * @return
         */
        public static CFinancialYear getFinancialYearForGivenDate(Date date) {
                
                
                CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(date);
                return financialYear;
        }

        public EgwStatusHibernateDAO getEgwStatusHibernateDAO() {
                return egwStatusHibernateDAO;
        }

        public void setEgwStatusHibernateDAO(EgwStatusHibernateDAO egwStatusHibernateDAO) {
                this.egwStatusHibernateDAO = egwStatusHibernateDAO;
        }
        
}