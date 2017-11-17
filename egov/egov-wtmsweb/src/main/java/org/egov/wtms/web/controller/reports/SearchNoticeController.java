/*
 * eGov SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.wtms.application.entity.SearchNoticeDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.SearchNoticeService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.reports.entity.SearchNoticeAdaptor;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

@Controller
@RequestMapping("/report/notice/search")
public class SearchNoticeController {

    private static final String REVENUEWARD = "revenueWard";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String PROPERTY_TYPE = "propertyType";
    private static final String CONNECTION_TYPE = "connectionType";
    private static final String ASSESSMENT_NUMBER = "assessmentNumber";
    private static final String HOUSE_NUMBER = "houseNumber";
    private static final String EXCEPTION_IN_ADDFILESTOZIP = "Exception in addFilesToZip : ";
    private static final String NOTICE_TYPE = "noticeType";
    private static final String FROMDATE = "fromDate";
    private static final String TODATE = "toDate";
    private static final String SANCTION_ORDER = "Sanction Order";
    private static final String DEMAND_BILL = "Demand Bill";

    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private SearchNoticeService searchNoticeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    private static final Logger LOGGER = Logger.getLogger(SearchNoticeController.class);

    @RequestMapping(method = GET)
    public String search(final Model model) {

        return "searchnotice-report";
    }

    @ModelAttribute
    public SearchNoticeDetails reportModel() {
        return new SearchNoticeDetails();
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

    @ModelAttribute("noticetypeList")
    public List<String> getNoticeTypes() {
        final List<String> noticeList = new ArrayList<>();
        noticeList.add(DEMAND_BILL);
        noticeList.add(SANCTION_ORDER);
        return noticeList;
    }

    @RequestMapping(value = "/result", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void searchResult(final SearchNoticeDetails searchNoticeDetails,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String result = null;
        List<SearchNoticeDetails> generateConnectionBillList = new ArrayList<>();
        if (request.getParameter(NOTICE_TYPE) != null)
            if (DEMAND_BILL.equals(request.getParameter(NOTICE_TYPE)))
                generateConnectionBillList = searchNoticeService.getBillReportDetails(searchNoticeDetails,
                        request.getParameter("zone"),
                        request.getParameter(REVENUEWARD), request.getParameter(PROPERTY_TYPE),
                        request.getParameter(APPLICATION_TYPE), request.getParameter(CONNECTION_TYPE),
                        request.getParameter(WATERCHARGES_CONSUMERCODE), request.getParameter(HOUSE_NUMBER),
                        request.getParameter(ASSESSMENT_NUMBER),
                        request.getParameter(FROMDATE), request.getParameter(TODATE));
            else if (SANCTION_ORDER.equals(request.getParameter(NOTICE_TYPE)))
                generateConnectionBillList = searchNoticeService.getSanctionOrderDetails(searchNoticeDetails,
                        request.getParameter("zone"),
                        request.getParameter(REVENUEWARD), request.getParameter(PROPERTY_TYPE),
                        request.getParameter(APPLICATION_TYPE), request.getParameter(CONNECTION_TYPE),
                        request.getParameter(WATERCHARGES_CONSUMERCODE), request.getParameter(HOUSE_NUMBER),
                        request.getParameter(ASSESSMENT_NUMBER),
                        request.getParameter(FROMDATE), request.getParameter(TODATE));

        final long foundRows = searchNoticeService.getTotalCountofBills(request.getParameter("zone"),
                request.getParameter(REVENUEWARD), request.getParameter(PROPERTY_TYPE),
                request.getParameter(APPLICATION_TYPE), request.getParameter(CONNECTION_TYPE),
                request.getParameter(WATERCHARGES_CONSUMERCODE), request.getParameter(HOUSE_NUMBER),
                request.getParameter(ASSESSMENT_NUMBER));

        final int count = generateConnectionBillList.size();
        LOGGER.info("Total count of records-->" + Long.valueOf(count));
        result = new StringBuilder("{ \"draw\":").append(request.getParameter("draw")).append(", \"recordsTotal\":")
                .append(foundRows).append(", \"recordsFiltered\":").append(foundRows).append(", \"data\":")
                .append(toJSON(generateConnectionBillList, SearchNoticeDetails.class,
                        SearchNoticeAdaptor.class))
                .append(", \"recordsCount\":").append(Long.valueOf(count)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());

    }

    @RequestMapping(value = "/result", method = GET)
    public void getBillBySearchParameter(final HttpServletRequest request, final HttpServletResponse response,
            @RequestParam("consumerCode") final String consumerCode, @RequestParam(NOTICE_TYPE) final String noticeType,
            @RequestParam("billNo") final String billNo, @RequestParam("workOrderNumber") final String workOrderNumber) {
        List<Long> waterChargesDocumentslist;
        final List<String> waterChargesFileStoreId = new ArrayList<>();
        if (SANCTION_ORDER.equals(noticeType)) {
            WaterConnectionDetails waterConnectionDetails = null;
            waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode,
                    ConnectionStatus.INPROGRESS);
            if (waterConnectionDetails == null)
                waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode,
                        ConnectionStatus.ACTIVE);
            if (waterConnectionDetails != null
                    && (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPROVED) ||
                            waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)))
                waterChargesFileStoreId.add(waterConnectionDetails.getFileStore() != null
                        ? waterConnectionDetails.getFileStore().getFileStoreId() : null);
        } else {
            waterChargesDocumentslist = searchNoticeService.getDocuments(consumerCode,
                    waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode).getApplicationType()
                            .getName());
            waterChargesFileStoreId.add(waterChargesDocumentslist.get(0) + "");
        }
        getDemandBillByFileStoreId(waterChargesFileStoreId, consumerCode, response);
    }

    @RequestMapping(value = "/mergeAndDownload", method = GET)
    public String mergeAndDownload(final SearchNoticeDetails searchNoticeDetails, final HttpServletRequest request,
            final HttpServletResponse response) {
        final long startTime = System.currentTimeMillis();
        final String noticeType = request.getParameter(NOTICE_TYPE);
        List<SearchNoticeDetails> searchResultList;
        if (noticeType != null && DEMAND_BILL.equalsIgnoreCase(noticeType))
            searchResultList = searchNoticeService
                    .getBillReportDetails(searchNoticeDetails, request.getParameter("zone"), request.getParameter(REVENUEWARD),
                            request.getParameter(PROPERTY_TYPE), request.getParameter(APPLICATION_TYPE),
                            request.getParameter(CONNECTION_TYPE), request.getParameter(WATERCHARGES_CONSUMERCODE),
                            request.getParameter(HOUSE_NUMBER), request.getParameter(ASSESSMENT_NUMBER),
                            request.getParameter(FROMDATE), request.getParameter(TODATE));
        else
            searchResultList = searchNoticeService
                    .getSanctionOrderDetails(searchNoticeDetails, request.getParameter("zone"), request.getParameter(REVENUEWARD),
                            request.getParameter(PROPERTY_TYPE), request.getParameter(APPLICATION_TYPE),
                            request.getParameter(CONNECTION_TYPE), request.getParameter(WATERCHARGES_CONSUMERCODE),
                            request.getParameter(HOUSE_NUMBER), request.getParameter(ASSESSMENT_NUMBER),
                            request.getParameter(FROMDATE), request.getParameter(TODATE));

        mergeAndDownloadNotice(searchResultList, response);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of Bills : "
                    + (searchResultList != null ? searchResultList.size() : ZERO));

        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GenerateBill | mergeAndDownload | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from mergeAndDownload method");
        }
        return null;
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

        return outputStream != null ? outputStream.toByteArray() : null;
    }

    @RequestMapping(value = "/zipAndDownload", method = GET)
    public String zipAndDownload(final SearchNoticeDetails searchNoticeDetails, final HttpServletRequest request,
            final HttpServletResponse response)
            throws IOException {
        final String noticeType = request.getParameter(NOTICE_TYPE);
        final long startTime = System.currentTimeMillis();
        List<SearchNoticeDetails> noticeList;
        if (DEMAND_BILL.equalsIgnoreCase(noticeType))
            noticeList = searchNoticeService
                    .getBillReportDetails(searchNoticeDetails, request.getParameter("zone"), request.getParameter(REVENUEWARD),
                            request.getParameter(PROPERTY_TYPE), request.getParameter(APPLICATION_TYPE),
                            request.getParameter(CONNECTION_TYPE), request.getParameter(WATERCHARGES_CONSUMERCODE),
                            request.getParameter(HOUSE_NUMBER), request.getParameter(ASSESSMENT_NUMBER),
                            request.getParameter(FROMDATE), request.getParameter(TODATE));
        else
            noticeList = searchNoticeService.getSanctionOrderDetails(searchNoticeDetails, request.getParameter("zone"),
                    request.getParameter(REVENUEWARD),
                    request.getParameter(PROPERTY_TYPE), request.getParameter(APPLICATION_TYPE),
                    request.getParameter(CONNECTION_TYPE), request.getParameter(WATERCHARGES_CONSUMERCODE),
                    request.getParameter(HOUSE_NUMBER), request.getParameter(ASSESSMENT_NUMBER),
                    request.getParameter(FROMDATE), request.getParameter(TODATE));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of Bills : "
                    + (noticeList != null ? noticeList.size() : ZERO));
        try {
            zipAndDownloadNotice(noticeList, response);
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

    public void zipAndDownloadNotice(final List<SearchNoticeDetails> noticeList, final HttpServletResponse response)
            throws IOException {

        ZipOutputStream zipOutputStream = null;
        if (null != noticeList && noticeList.size() >= 0) {

            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            response.setHeader(WaterTaxConstants.CONTENT_DISPOSITION, "attachment;filename=" + "searchbill" + ".zip");
            response.setContentType("application/zip");
        }

        for (final SearchNoticeDetails connectionbill : noticeList)
            try {
                WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(connectionbill.getHscNo(),
                        ConnectionStatus.INPROGRESS);
                if (waterConnectionDetails == null)
                    waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(connectionbill.getHscNo(),
                            ConnectionStatus.ACTIVE);
                if (waterConnectionDetails != null && waterConnectionDetails.getFileStore() != null  && (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPROVED) ||
                        waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED))) {
                    final FileStoreMapper fsm = fileStoreMapperRepository
                            .findByFileStoreId(waterConnectionDetails.getFileStore().getFileStoreId());
                    final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                    final byte[] bFile = FileUtils.readFileToByteArray(file);
                    zipOutputStream = addFilesToZip(new ByteArrayInputStream(bFile), file.getName(),
                            zipOutputStream);
                }
            } catch (final Exception e) {
                LOGGER.error("zipAndDownload : Getting demand notice failed ", e);
                continue;
            }
        if (zipOutputStream != null) {
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        }

    }

    public void mergeAndDownloadNotice(final List<SearchNoticeDetails> searchResultList, final HttpServletResponse response) {
        final List<InputStream> pdfs = new ArrayList<>();
        for (final SearchNoticeDetails connectionbill : searchResultList)
            if (connectionbill != null)
                try {
                    WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                            .findByConsumerCodeAndConnectionStatus(connectionbill.getHscNo(), ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails == null)
                        waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(connectionbill.getHscNo(),
                                ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails != null && waterConnectionDetails.getFileStore() != null  && (waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPROVED) ||
                            waterConnectionDetails.getStatus().getCode().equals(WaterTaxConstants.APPLICATION_STATUS_SANCTIONED)))  {
                        final FileStoreMapper fsm = fileStoreMapperRepository
                                .findByFileStoreId(waterConnectionDetails.getFileStore().getFileStoreId());
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

        if (!pdfs.isEmpty())
            getServletResponse(response, pdfs, "search_bill");
        else
            throw new ValidationException("err.demand.notice");
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
            LOGGER.error(EXCEPTION_IN_ADDFILESTOZIP, iae);
        } catch (final FileNotFoundException fnfe) {
            LOGGER.error(EXCEPTION_IN_ADDFILESTOZIP, fnfe);
        } catch (final IOException ioe) {
            LOGGER.error(EXCEPTION_IN_ADDFILESTOZIP, ioe);
        }
        return out;
    }

    public void getDemandBillByFileStoreId(final List<String> waterChargesFileStoreId, final String consumerCode,
            final HttpServletResponse response) {
        if (!waterChargesFileStoreId.isEmpty() && waterChargesFileStoreId.get(0) != null)
            try {
                final FileStoreMapper fsm = fileStoreMapperRepository
                        .findByFileStoreId(waterChargesFileStoreId.get(0) + "");
                final List<InputStream> pdfs = new ArrayList<>();
                final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                final byte[] bFile = FileUtils.readFileToByteArray(file);
                pdfs.add(new ByteArrayInputStream(bFile));
                getServletResponse(response, pdfs, consumerCode);
            } catch (final Exception e) {
                throw new ValidationException(e.getMessage());
            }
        else
            throw new ValidationException("err.demand.notice");
    }
}