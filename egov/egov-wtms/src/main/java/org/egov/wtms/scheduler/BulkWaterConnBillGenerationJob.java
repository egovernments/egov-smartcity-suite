/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.wtms.scheduler;

import com.google.common.base.Stopwatch;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.wtms.service.bill.WaterConnectionBillService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

public class BulkWaterConnBillGenerationJob extends AbstractQuartzJob {

    private static final long serialVersionUID = 6652765005378915324L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkWaterConnBillGenerationJob.class);

    private Integer billsCount;
    private Integer modulo;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Override
    public void executeJob() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting water tax bulk bill generation for {}", waterTaxUtils.getCityCode());
        Stopwatch stopwatch = new Stopwatch().start();
        WaterConnectionBillService waterConnectionBillService = null;
        try {
            waterConnectionBillService = (WaterConnectionBillService) beanProvider.getBean("waterConnectionBillService");
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.warn("waterConnectionBillService implementation not found");
        }
        if (waterConnectionBillService != null && waterTaxUtils.getAppconfigValueForSchedulearEnabled())
            waterConnectionBillService.bulkBillGeneration(modulo, billsCount);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Water tax bulk bill generation completed for {} in {} seconds", waterTaxUtils.getCityCode(),
                    stopwatch.stop().elapsedTime(TimeUnit.SECONDS));
    }

    public void setBillsCount(Integer billsCount) {
        this.billsCount = billsCount;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }
}