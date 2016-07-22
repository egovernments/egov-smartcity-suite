
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
package org.egov.wtms.web.controller.reports;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.wtms.application.service.GenerateConnectionBillService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.service.bill.WaterConnectionBillService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/report")
public class GenerateBillForConsumerCodeController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private GenerateConnectionBillService generateConnectionBillService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @RequestMapping(value = "/generateBillForHSCNo/{consumerCode}", method = GET)
    public void newForm(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request,
            final HttpServletResponse response) {
        WaterConnectionBillService waterConnectionBillService = null;
        try {
            waterConnectionBillService = (WaterConnectionBillService) beanProvider
                    .getBean("waterConnectionBillService");
        } catch (final NoSuchBeanDefinitionException e) {

        }
        if (waterConnectionBillService != null)
            waterConnectionBillService.generateBillForConsumercode(consumerCode);
        generatePDF(consumerCode, request, response);

    }

    public void generatePDF(final String consumerCode, final HttpServletRequest request,
            final HttpServletResponse response) {
        final List<Long> waterChargesDocumentslist = generateConnectionBillService.getDocuments(consumerCode,
                waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode).getApplicationType()
                        .getName());
        response.setHeader("content-disposition", "attachment; filename=\"" + "generate_bill.pdf" + "\"");
        if (!waterChargesDocumentslist.isEmpty() && waterChargesDocumentslist.get(0) != null)
            try {
                final FileStoreMapper fsm = fileStoreMapperRepository
                        .findByFileStoreId(waterChargesDocumentslist.get(0) + "");
                final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                final FileInputStream inStream = new FileInputStream(file);
                final PrintWriter outStream = response.getWriter();
                int bytesRead = -1;
                while ((bytesRead = inStream.read()) != -1)
                    outStream.write(bytesRead);
                inStream.close();
                outStream.close();
            } catch (final FileNotFoundException fileNotFoundExcep) {
                throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
            } catch (final IOException ioExcep) {
                throw new ApplicationRuntimeException("Exception while generating bill : " + ioExcep);
            }
        else
            throw new ValidationException("err.demand.notice");
    }
}
