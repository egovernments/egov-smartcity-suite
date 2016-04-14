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

package org.egov.wtms.web.controller.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

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
        final List<GenerateConnectionBill> generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));
        final List<GenerateConnectionBill> generateconnectionBillList = generateConnectionBillService
                .getBillData(generateConnectionBillList);
        result = new StringBuilder("{ \"data\":").append(toJSON(generateconnectionBillList)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
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

        final List<GenerateConnectionBill> generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));

        final List<InputStream> pdfs = new ArrayList<InputStream>();

        for (final GenerateConnectionBill connectionbill : generateConnectionBillList)
            try {

                final List<Long> waterChargesDocumentslist = generateConnectionBillService.getDocuments(
                        connectionbill.getHscNo(), connectionbill.getApplicationType());
                if (waterChargesDocumentslist != null && waterChargesDocumentslist.get(0) != null) {
                    final FileStoreMapper fsm = fileStoreMapperRepository.findByFileStoreId(waterChargesDocumentslist
                            .get(0) + "");
                    final File file = fileStoreService.fetch(fsm, WaterTaxConstants.FILESTORE_MODULECODE);
                    final byte[] bFile = FileUtils.readFileToByteArray(file);
                    pdfs.add(new ByteArrayInputStream(bFile));
                }
            } catch (final Exception e) {
                System.out.println("exception" + e);

                continue;
            }

        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = concatPDFs(pdfs, output);
            response.setHeader("Content-disposition", "attachment;filename=" + "generate_bill" + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);

        } catch (final IOException e) {

            throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
        }
        System.currentTimeMillis();

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

            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (final IOException ioe) {

                ioe.printStackTrace();
            }
        }

        return outputStream.toByteArray();
    }

    @RequestMapping(value = "/zipAndDownload", method = RequestMethod.GET)
    public String zipAndDownload(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ParseException, ValidationException {

        System.currentTimeMillis();

        final List<GenerateConnectionBill> generateConnectionBillList = generateConnectionBillService
                .getBillReportDetails(request.getParameter("zone"), request.getParameter("revenueWard"),
                        request.getParameter("propertyType"), request.getParameter("applicationType"),
                        request.getParameter("connectionType"), request.getParameter("consumerCode"),
                        request.getParameter("houseNumber"), request.getParameter("assessmentNumber"));
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

                    continue;
                }

            zipOutputStream.closeEntry();
            zipOutputStream.close();

        } catch (final IOException e) {

            e.printStackTrace();
            throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
        }
        System.currentTimeMillis();

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
            iae.printStackTrace();
            throw new ValidationException(Arrays.asList(new ValidationError("error", iae.getMessage())));
        } catch (final FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            throw new ValidationException(Arrays.asList(new ValidationError("error", fnfe.getMessage())));
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            throw new ValidationException(Arrays.asList(new ValidationError("error", ioe.getMessage())));
        }
        return out;
    }
}