/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.tl.web.controller.transactions.demand;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.tl.entity.contracts.DemandNoticeRequest;
import org.egov.tl.enums.NoticeTypeEnum;
import org.egov.tl.service.DemandNoticeService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.response.adaptor.DemandNoticeAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.tl.utils.Constants.ADMIN_HIERARCHY;
import static org.egov.tl.utils.Constants.ADMIN_WARD;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.REVENUE_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.REVENUE_WARD;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/demand-notice")
public class DemandNoticeController {

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private DemandNoticeService demandNoticeService;

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
	private FileStoreMapperRepository fileStoreMapperRepository;
    
    private static final Logger LOGGER = Logger.getLogger(DemandNoticeController.class);

    @Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;
    
    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @GetMapping("search")
    public String searchFormforNotice(Model model) {
        model.addAttribute("demandNoticeRequest", new DemandNoticeRequest());
        model.addAttribute("categoryList", licenseCategoryService.getCategoriesOrderByName());
        model.addAttribute("subCategoryList", Collections.emptyList());
        model.addAttribute("localityList", boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE));
        model.addAttribute("revenueWards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE));
        model.addAttribute("adminWards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ADMIN_WARD,
                ADMIN_HIERARCHY));
        model.addAttribute("noticeType", Arrays.asList(NoticeTypeEnum.GENERATEBULKDEMANDNOTICE.toString(), 
        		NoticeTypeEnum.REJECTIONNOTICE.toString()));
        return "search-demandnotice";
    }

    @PostMapping(value = "search", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchResult(@ModelAttribute DemandNoticeRequest demandnoticeForm) {
    	if(NoticeTypeEnum.REJECTIONNOTICE.toString().equalsIgnoreCase(demandnoticeForm.getNoticeTypeName())) {
			
    		return new StringBuilder("{ \"data\":")
                    .append(toJSON(tradeLicenseService.getLicenseRejectionNotices(demandnoticeForm),
                    		DemandNoticeRequest.class, DemandNoticeAdaptor.class)).append("}").toString();
			 
    	}else {
    		return new StringBuilder("{ \"data\":")
                    .append(toJSON(tradeLicenseService.getLicenseDemandNotices(demandnoticeForm),
                    		DemandNoticeRequest.class, DemandNoticeAdaptor.class)).append("}").toString();
    	}
        
    }
    
    @RequestMapping(value = "/result", method = GET)
	public void getBillBySearchParameter(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("applNumber") final String applNumber,
			@RequestParam("tlFileStoreId") final String tlFileStoreId) {
		
		getNoticeByFileStoreId(tlFileStoreId, applNumber, response);
	}
    
    public void getNoticeByFileStoreId(final String tlFileStoreId, final String applNumber,
			final HttpServletResponse response) {
		if (!tlFileStoreId.isEmpty() && tlFileStoreId != null)
			try {
				final FileStoreMapper fsm = fileStoreMapperRepository
						.findByFileStoreId(tlFileStoreId + "");
				final List<InputStream> pdfs = new ArrayList<>();
				final File file = fileStoreService.fetch(fsm, Constants.TL_FILE_STORE_DIR);
				final byte[] bFile = FileUtils.readFileToByteArray(file);
				pdfs.add(new ByteArrayInputStream(bFile));
				getServletResponse(response, pdfs, applNumber);
			} catch (final Exception e) {
				throw new ValidationException(e.getMessage());
			}
		else
			throw new ValidationException("err.demand.notice");
	}
    
    private HttpServletResponse getServletResponse(final HttpServletResponse response, final List<InputStream> pdfs,
			final String filename) {
		try {
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			final byte[] data = concatPDFs(pdfs, output);
			response.setHeader(Constants.TL_FILE_STORE_DIR, "attachment;filename=" + filename + ".pdf");
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

    @GetMapping(value = "generate/{licenseId}", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> generateDemandNotice(@PathVariable Long licenseId) {
        return ReportUtil.reportAsResponseEntity(demandNoticeService.generateReport(licenseId));
    }

    @GetMapping(value = "generate", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> mergeAndDownload(@ModelAttribute DemandNoticeRequest searchRequest) {
        return ReportUtil.reportAsResponseEntity(demandNoticeService.generateBulkDemandNotice(searchRequest));
    }
}
