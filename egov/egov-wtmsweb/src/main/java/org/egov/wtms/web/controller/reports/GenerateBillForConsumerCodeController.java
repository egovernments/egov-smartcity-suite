
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.demand.model.EgBill;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infstr.services.PersistenceService;
import org.egov.wtms.application.service.SearchNoticeService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.service.bill.WaterConnectionBillService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@Controller
@RequestMapping(value = "/report")
public class GenerateBillForConsumerCodeController {

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private SearchNoticeService searchNoticeService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @Autowired
    private FileStoreService fileStoreService;

    private static final Logger LOGGER = Logger.getLogger(GenerateBillForConsumerCodeController.class);

    @RequestMapping(value = "/generateBillForHSCNo/{consumerCode}", method = GET)
    public String newForm(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request,
            final HttpServletResponse response) {
        WaterConnectionBillService waterConnectionBillService = null;
        try {
            final EgBill egBill = getBillByConsumerCode(consumerCode);
            if (egBill != null)
                throw new ValidationException("err.demand.bill.generated");
            else
                waterConnectionBillService = (WaterConnectionBillService) beanProvider
                        .getBean("waterConnectionBillService");
        } catch (final NoSuchBeanDefinitionException e) {

        }
        if (waterConnectionBillService != null)
            waterConnectionBillService.generateBillForConsumercode(consumerCode);
        final List<Long> waterChargesDocumentslist = searchNoticeService.getDocuments(consumerCode,
                waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode).getApplicationType()
                        .getName());
        model.addAttribute("successMessage", "Demand Bill got generated, Please click on download.");
        model.addAttribute("fileStoreId", !waterChargesDocumentslist.isEmpty() ? waterChargesDocumentslist.get(0) : "");
        return "generatebill-consumercode";

    }

    @RequestMapping(value = "/generateBillForHSCNo/downloadDemandBill")
    public void generatePDF(final String consumerCode, final HttpServletRequest request,
            final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        if (signedFileStoreId != null)
            try {
                final FileStoreMapper fsm = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
                final List<InputStream> pdfs = new ArrayList<>();
                final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                final byte[] bFile = FileUtils.readFileToByteArray(file);
                pdfs.add(new ByteArrayInputStream(bFile));
                getServletResponse(response, pdfs, consumerCode);
            } catch (final FileNotFoundException fileNotFoundExcep) {
                throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
            } catch (final IOException ioExcep) {
                throw new ApplicationRuntimeException("Exception while generating bill : " + ioExcep);
            }
        else
            throw new ValidationException("err.demand.notice");
    }

    private HttpServletResponse getServletResponse(final HttpServletResponse response, final List<InputStream> pdfs,
            final String filename) {
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = concatPDFs(pdfs, output);
            response.setHeader(WaterTaxConstants.CONTENT_DISPOSITION, "attachment;filename=" + filename + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
            return response;
        } catch (final IOException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private byte[] concatPDFs(final List<InputStream> streamOfPDFFiles, final ByteArrayOutputStream outputStream) {

        Document document = null;
        try {
            final List<InputStream> pdfs = streamOfPDFFiles;
            final List<PdfReader> readers = new ArrayList<>();
            final Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                final InputStream pdf = iteratorPDFs.next();
                final PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                if (null == document)
                    document = new Document(pdfReader.getPageSize(1));
            }
            // Create a writer for the outputstream
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            final PdfContentByte cb = writer.getDirectContent(); // Holds the
            // PDF
            // data

            PdfImportedPage page;
            int pageOfCurrentReaderPDF = 0;
            final Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                final PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();

        } catch (final Exception e) {

            LOGGER.error("Exception in concat PDFs : ", e);
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (final IOException ioe) {
                LOGGER.error("Exception in concat PDFs : ", ioe);
            }
        }
        return outputStream.toByteArray();
    }

    public EgBill getBillByConsumerCode(final String consumerCode) {
        EgBill egBill = null;
        final String query = "select distinct bill From EgBill bill,EgBillType billtype,WaterConnection conn,WaterConnectionDetails connDet,EgwStatus status,WaterDemandConnection conndem  , EgDemand demd "
                + "where billtype.id=bill.egBillType and billtype.code='MANUAL' and bill.consumerId = conn.consumerCode and conn.id=connDet.connection and connDet.id=conndem.waterConnectionDetails "
                + " and demd.id=bill.egDemand and demd.id=conndem.demand and connDet.connectionType='"
                + WaterTaxConstants.NON_METERED + "' "
                + " and demd.isHistory = '" + WaterTaxConstants.DEMANDISHISTORY + "' and  bill.is_Cancelled='"
                + WaterTaxConstants.DEMANDISHISTORY + "' and bill.serviceCode='"
                + WaterTaxConstants.COLLECTION_STRING_SERVICE_CODE + "'"
                + " and connDet.connectionStatus='" + WaterTaxConstants.MASTERSTATUSACTIVE
                + "' and connDet.status=status.id and status.moduletype='" + WaterTaxConstants.MODULETYPE + "' "
                + " and status.code='" + WaterTaxConstants.APPLICATION_STATUS_SANCTIONED
                + "' and conn.consumerCode =:consumerCode  order by bill.id desc";
        final Query hibquery = persistenceService.getSession().createQuery(query.toString())
                .setString("consumerCode", consumerCode);
        final List<EgBill> egBilltemp = hibquery.list();
        if (!egBilltemp.isEmpty())
            egBill = egBilltemp.get(0);
        LOGGER.debug(
                "query to get Bill for single consumernumber" + query.toString() + " for consumer No= " + consumerCode);
        return egBill;
    }
}
