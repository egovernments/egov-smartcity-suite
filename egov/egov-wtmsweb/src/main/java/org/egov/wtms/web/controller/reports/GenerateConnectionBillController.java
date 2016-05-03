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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.service.GenerateConnectionBill;
import org.egov.wtms.application.service.GenerateConnectionBillService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/report/generateBill/search")
public class GenerateConnectionBillController {

    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private GenerateConnectionBillService generateConnectionBillService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    ServletContext context;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    private static final Logger LOGGER = Logger.getLogger(GenerateConnectionBillController.class);

    @RequestMapping(method = GET)
    public String search(final Model model) {

        return "generateBill-Report";
    }

    @ModelAttribute
    public GenerateConnectionBill reportModel() {
        return new GenerateConnectionBill();
    }

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HIERARCHY_TYPE);
    }

    public @ModelAttribute("revenueWards") List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WaterTaxConstants.REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE);
    }

    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getNonMeteredConnectionTypesMap();
    }

    public @ModelAttribute("propertyTypes") List<PropertyType> propertyTypes() {
        return propertyTypeService.getAllActivePropertyTypes();
    }

    public @ModelAttribute("applicationTypes") List<ApplicationType> applicationTypes() {
        return applicationTypeService.findAll();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException {
        String result = null;
        List<GenerateConnectionBill> generateConnectionBillList = new ArrayList<>();
        generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));
        final int count = generateConnectionBillList.size();
        LOGGER.info("Total count of records-->"+Long.valueOf(count));
        List<GenerateConnectionBill> generateconnectionBillList = new ArrayList<>();
        if (Long.valueOf(count)<1000){
            generateconnectionBillList = generateConnectionBillService
                .getBillData(generateConnectionBillList);
        }
        else
        {
             generateconnectionBillList = new ArrayList<>();
        }
        result = new StringBuilder("{ \"data\":").append(toJSON(generateConnectionBillList)).append(", \"recordsCount\":").append(Long.valueOf(count)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
        
    }

    @RequestMapping(value = "/result/{consumerCode}", method = GET)
    public void getBillBySearchParameter(final HttpServletRequest request, final HttpServletResponse response,
            @PathVariable final String consumerCode) {
        final List<Long> waterChargesDocumentslist = generateConnectionBillService.getDocuments(consumerCode,
                waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode).getApplicationType()
                        .getCode());
        response.setHeader("content-disposition", "attachment; filename=\"" + "generate_bill.pdf" + "\"");
        if (!waterChargesDocumentslist.isEmpty() && waterChargesDocumentslist.get(0) != null)
            try {

                final FileStoreMapper fsm = fileStoreMapperRepository.findByFileStoreId(waterChargesDocumentslist
                        .get(0) + "");
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

    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(GenerateConnectionBill.class,
                new GenerateConnectionBillAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/mergeAndDownload", method = RequestMethod.GET)
    public String mergeAndDownload(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException, ValidationException {
        final long startTime = System.currentTimeMillis();
        final List<GenerateConnectionBill> generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of Bills : " + (generateConnectionBillList != null ? generateConnectionBillList.size() : ZERO));
        final List<InputStream> pdfs = new ArrayList<InputStream>();

        for (final GenerateConnectionBill connectionbill : generateConnectionBillList)
            try {

                final List<Long> waterChargesDocumentslist = generateConnectionBillService.getDocuments(
                        connectionbill.getHscNo(), connectionbill.getApplicationType());
                if (!waterChargesDocumentslist.isEmpty() && waterChargesDocumentslist.get(0) != null) {
                    final FileStoreMapper fsm = fileStoreMapperRepository.findByFileStoreId(waterChargesDocumentslist
                            .get(0) + "");
                    final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                    final byte[] bFile = FileUtils.readFileToByteArray(file);
                    pdfs.add(new ByteArrayInputStream(bFile));
                }
            } catch (final Exception e) {
                LOGGER.debug("Entered into executeJob" + e);
                continue;
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of pdfs : " + (pdfs != null ? pdfs.size() : ZERO));
        try {
            if (!pdfs.isEmpty()) {
                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                final byte[] data = concatPDFs(pdfs, output);
                response.setHeader("Content-disposition", "attachment;filename=" + "generate_bill" + ".pdf");
                response.setContentType("application/pdf");
                response.setContentLength(data.length);
                response.getOutputStream().write(data);
            } else
                throw new ValidationException("err.demand.notice");

        } catch (final IOException e) {

            throw new ValidationException(e.getMessage());

        }
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GenerateBill | mergeAndDownload | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from mergeAndDownload method");
        }

        return null;
    }

    private byte[] concatPDFs(final List<InputStream> streamOfPDFFiles, final ByteArrayOutputStream outputStream) {

        Document document = null;
        try {
            final List<InputStream> pdfs = streamOfPDFFiles;
            final List<PdfReader> readers = new ArrayList<PdfReader>();
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

    @RequestMapping(value = "/zipAndDownload", method = RequestMethod.GET)
    public String zipAndDownload(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException, ValidationException {
        final long startTime = System.currentTimeMillis();

        final List<GenerateConnectionBill> generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of BIlls : " + (generateConnectionBillList != null ? generateConnectionBillList.size() : ZERO));
        try {
            ZipOutputStream zipOutputStream = null;
            if (null != generateConnectionBillList || generateConnectionBillList.size() >= 0) {

                zipOutputStream = new ZipOutputStream(response.getOutputStream());
                response.setHeader("Content-disposition", "attachment;filename=" + "genaratebill" + ".zip");
                response.setContentType("application/zip");
            }

            for (final GenerateConnectionBill connectionbill : generateConnectionBillList)
                try {
                    final List<Long> filestoreList = generateConnectionBillService.getDocuments(
                            connectionbill.getHscNo(), connectionbill.getApplicationType());
                    if (filestoreList != null && filestoreList.get(0) != null) {
                        final FileStoreMapper fsm = fileStoreMapperRepository.findByFileStoreId(filestoreList.get(0)
                                + "");
                        final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                        final byte[] bFile = FileUtils.readFileToByteArray(file);
                        zipOutputStream = addFilesToZip(new ByteArrayInputStream(bFile), file.getName(),
                                zipOutputStream);
                    }
                } catch (final Exception e) {
                    LOGGER.error("zipAndDownload : Getting demand notice failed ", e);
                    continue;
                }

            zipOutputStream.closeEntry();
            zipOutputStream.close();

        } catch (final IOException e) {
            LOGGER.error("Exception in Zip and Download : ", e);
        }
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GenerateBill | zipAndDownload | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from zipAndDownload method");
        }

        return null;
    }

    private ZipOutputStream addFilesToZip(final InputStream inputStream, final String noticeNo,
            final ZipOutputStream out) {
        final byte[] buffer = new byte[1024];
        try {
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            out.putNextEntry(new ZipEntry(noticeNo.replaceAll("/", "_")));
            int len;
            while ((len = inputStream.read(buffer)) > 0)
                out.write(buffer, 0, len);
            inputStream.close();

        } catch (final IllegalArgumentException iae) {
            LOGGER.error("Exception in addFilesToZip : ", iae);
        } catch (final FileNotFoundException fnfe) {
            LOGGER.error("Exception in addFilesToZip : ", fnfe);
        } catch (final IOException ioe) {
            LOGGER.error("Exception in addFilesToZip : ", ioe);
        }
        return out;
    }
}