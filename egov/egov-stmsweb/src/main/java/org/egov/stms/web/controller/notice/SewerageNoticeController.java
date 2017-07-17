/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 * <p>
 * Copyright (C) <2016>  eGovernments Foundation
 * <p>
 * The updated version of eGov suite of products as by eGovernments Foundation
 * is available at http://www.egovernments.org
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/ or
 * http://www.gnu.org/licenses/gpl.html .
 * <p>
 * In addition to the terms of the GPL license to be adhered to in using this
 * program, the following additional terms are to be complied with:
 * <p>
 * 1) All versions of this program, verbatim or modified must carry this
 * Legal Notice.
 * <p>
 * 2) Any misrepresentation of the origin of the material is prohibited. It
 * is required that all modified versions of this material be marked in
 * reasonable ways as different from the original version.
 * <p>
 * 3) This license does not grant any rights to any user of the program
 * with regards to rights under trademark law for use of the trade names
 * or trademarks of eGovernments Foundation.
 * <p>
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.notice;

import static org.egov.infra.utils.PdfUtils.appendFiles;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.stms.elasticSearch.entity.SewerageNoticeSearchRequest;
import org.egov.stms.elasticSearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reports")
public class SewerageNoticeController {
    private static final Logger LOGGER = Logger.getLogger(SewerageNoticeController.class);
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private CityService cityService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageNoticeService sewerageNoticeService;
    @Autowired
    private SewerageIndexService sewerageIndexService;

    @RequestMapping(value = "/search-notice", method = RequestMethod.GET)
    public String newSearchNoticeForm(final Model model) {
        model.addAttribute("revenueWards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                SewerageTaxConstants.REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
        return "searchSewerageNotices";
    }

    private List<SewerageIndex> getSearchResult(final SewerageNoticeSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());
        final BoolQueryBuilder boolQuery = sewerageIndexService.getQueryFilterForNotice(searchRequest);
        return sewerageIndexService.getNoticeSearchResultByBoolQuery(boolQuery);
    }
    private Page<SewerageIndex> getNoticeSearchResult(final SewerageNoticeSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());
        final BoolQueryBuilder boolQuery = sewerageIndexService.getQueryFilterForNotice(searchRequest);
        return sewerageIndexService.getPagedNoticeSearchResultByBoolQuery(boolQuery,searchRequest);
    }
    @RequestMapping(value = "/searchResult", method = RequestMethod.POST)
    @ResponseBody
    public DataTable<SewerageSearchResult> searchApplication(@ModelAttribute final SewerageNoticeSearchRequest searchRequest) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final List<SewerageSearchResult> searchResultFomatted = new ArrayList<>();
        final Page<SewerageIndex> searchResult = getNoticeSearchResult(searchRequest);
        SewerageSearchResult searchResultObject ;
        final Pageable pageable = new PageRequest(searchRequest.pageNumber(),
                searchRequest.pageSize(), searchRequest.orderDir(), searchRequest.orderBy());
        for (final SewerageIndex sewerageIndexObject : searchResult) {
            searchResultObject = new SewerageSearchResult();
            searchResultObject.setApplicationNumber(sewerageIndexObject.getApplicationNumber());
            if (searchRequest.getNoticeType() != null)
                if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_WORK_ORDER)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getWorkOrderNumber());
                    searchResultObject.setNoticeDate(formatter.format(sewerageIndexObject.getWorkOrderDate()));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_ESTIMATION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getEstimationNumber());
                    searchResultObject.setNoticeDate(formatter.format(sewerageIndexObject.getEstimationDate()));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getClosureNoticeNumber());
                    searchResultObject.setNoticeDate(formatter.format(sewerageIndexObject.getClosureNoticeDate()));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_REJECTION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getRejectionNoticeNumber());
                    searchResultObject.setNoticeDate(formatter.format(sewerageIndexObject.getRejectionNoticeDate()));
                }
            searchResultObject.setShscNumber(sewerageIndexObject.getShscNumber());
            searchResultObject.setDoorNumber(sewerageIndexObject.getDoorNo());
            searchResultObject.setAddress(sewerageIndexObject.getAddress());
            searchResultObject.setApplicantName(sewerageIndexObject.getConsumerName());
            searchResultFomatted.add(searchResultObject);
        }
        return new DataTable<>(new PageImpl<>(searchResultFomatted, pageable, searchResult.getTotalElements()),searchRequest.draw()) ;  }

    @RequestMapping(value = "/search-NoticeResultSize", method = RequestMethod.GET)
    @ResponseBody
    public int getSerachResultCount(@ModelAttribute final SewerageNoticeSearchRequest searchRequest) {
        final List<SewerageIndex> searchResult = getSearchResult(searchRequest);
        return searchResult.size();
    }

    private List<SewerageNotice> getSearchedNotices(final SewerageNoticeSearchRequest searchRequest) {
        String noticeType ;
        String noticeTypeInput = null;
        String noticeNo;
        final List<SewerageNotice> noticeList = new ArrayList<>(0);
        final List<SewerageIndex> searchResult = getSearchResult(searchRequest);
        for (final SewerageIndex sewerageIndexObject : searchResult) {
            noticeNo = "";
            if (searchRequest.getNoticeType() != null) {
                noticeTypeInput = searchRequest.getNoticeType();
                if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_WORK_ORDER))
                    noticeNo = sewerageIndexObject.getWorkOrderNumber();
                else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_ESTIMATION))
                    noticeNo = sewerageIndexObject.getEstimationNumber();
                else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION))
                    noticeNo = sewerageIndexObject.getClosureNoticeNumber();
                else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_REJECTION))
                    noticeNo = sewerageIndexObject.getRejectionNoticeNumber();
            }
            if (noticeNo != null && !noticeNo.isEmpty()) {
                noticeType = getSewerageNoticeType(noticeNo, noticeTypeInput);
                final SewerageNotice sewerageNotice = sewerageNoticeService.findByNoticeNoAndNoticeType(noticeNo,
                        noticeType);
                if (sewerageNotice != null)
                    noticeList.add(sewerageNotice);
            }
        }
        return noticeList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/searchNotices-mergeAndDownload", method = RequestMethod.GET)
    public String mergeAndDownload(@ModelAttribute final SewerageNoticeSearchRequest searchRequest,
            final HttpServletResponse response) throws Exception {
        final String noticeType = null;
        final List<SewerageNotice> noticeList = getSearchedNotices(searchRequest);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into mergeAndDownload method");
        final long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("mergeAndDownload : Start Time : " + startTime);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : 0));

        final List<InputStream> pdfs = new ArrayList<>();
        if (noticeList != null && !noticeList.isEmpty())
            for (final SewerageNotice sewerageNotice : noticeList)
                try {
                    if (sewerageNotice.getFileStore() != null) {
                        sewerageNotice.getApplicationDetails().getConnectionDetail().getPropertyIdentifier();
                        final FileStoreMapper fsm = sewerageNotice.getFileStore();
                        final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                        if (file.length() > 0) {
                            final byte[] bFile = FileUtils.readFileToByteArray(file);
                            pdfs.add(new ByteArrayInputStream(bFile));
                        }
                    }
                } catch (final Exception e) {
                    LOGGER.error("mergeAndDownload : Getting notice failed for notice " + sewerageNotice, e);
                    continue;
                }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of pdfs : " + (pdfs != null ? pdfs.size() : 0));
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = appendFiles(pdfs, output);
            response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + noticeType + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);

        } catch (final ApplicationRuntimeException e) {
            LOGGER.error("Exception in Merge and Download : ", e);
            throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
        }
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("mergeAndDownload : End Time : " + endTime);
            LOGGER.debug("SearchNoticesAction | mergeAndDownload | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from mergeAndDownload method");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/searchNotices-seweragezipAndDownload")
    public String zipAndDownload(@ModelAttribute final SewerageNoticeSearchRequest searchRequest,
            final HttpServletResponse response) throws ValidationException {
        final List<SewerageNotice> noticeList = getSearchedNotices(searchRequest);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into zipAndDownload method");
        final long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("zipAndDownload : Start Time : " + startTime);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : 0));
        try {
            ZipOutputStream zipOutputStream = null;
            if (noticeList != null && !noticeList.isEmpty()) {
                zipOutputStream = new ZipOutputStream(response.getOutputStream());
                response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + ".zip");
                response.setContentType("application/zip");
            }

            for (final SewerageNotice sewerageNotice : noticeList){
                try {
                    if (sewerageNotice != null && sewerageNotice.getFileStore() != null) {
                        final FileStoreMapper fsm = sewerageNotice.getFileStore();
                        final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                        final byte[] bFile = FileUtils.readFileToByteArray(file);
                        zipOutputStream = sewerageNoticeService.addFilesToZip(new ByteArrayInputStream(bFile),
                                file.getName(), zipOutputStream);
                    }
                } catch (final Exception e) {
                    LOGGER.error("zipAndDownload : Getting notice failed for notice " + sewerageNotice, e);
                    continue;
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();

        } catch (final IOException e) {
            LOGGER.error("Exception in Zip and Download : ", e);
            throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
        }
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("zipAndDownload : End Time : " + endTime);
            LOGGER.debug("SewerageNoticeController | zipAndDownload | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from zipAndDownload method");
        }
        return null;
    }

    public String getSewerageNoticeType(final String noticeNo, final String noticeTypeInput) {
        String noticeType = null;
        if (noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_WORK_ORDER))
            noticeType = SewerageTaxConstants.NOTICE_TYPE_WORK_ORDER_NOTICE;
        else if (noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_ESTIMATION))
            noticeType = SewerageTaxConstants.NOTICE_TYPE_ESTIMATION_NOTICE;
        else if (noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION))
            noticeType = SewerageTaxConstants.NOTICE_TYPE_CLOSER_NOTICE;
        else if (noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_REJECTION))
            noticeType = SewerageTaxConstants.NOTICE_TYPE_REJECTION_NOTICE;
        return noticeType;
    }

    @RequestMapping(value = "/searchNotices-showSewerageNotice/{noticeNo}/{noticeType}", method = RequestMethod.GET)
    public String showNotice(@PathVariable("noticeNo") final String noticeNo,
            @PathVariable("noticeType") final String noticeTypeInput, final Model model,
            final HttpServletResponse response) throws IOException {
        String noticeType = null;
        if (noticeNo != null) {
            noticeType = getSewerageNoticeType(noticeNo, noticeTypeInput);
            final SewerageNotice sewerageNotice = sewerageNoticeService.findByNoticeNoAndNoticeType(noticeNo,
                    noticeType);
            if (sewerageNotice != null) {
                final FileStoreMapper fsm = sewerageNotice.getFileStore();
                final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                final InputStream is = new FileInputStream(file);
                // MIME type of the file
                response.setContentType("application/pdf");
                // Response header
                response.setHeader("Content-Disposition", "attachment; filename=\"" + sewerageNotice.getNoticeNo()
                        + ".pdf\"");
                // Read from the file and write into the response
                final OutputStream os = response.getOutputStream();
                final byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1)
                    os.write(buffer, 0, len);
                os.flush();
                os.close();
                is.close();
            } else {
                model.addAttribute("message", "msg.notice.not.found");
                return "common-error";
            }

        }
        return null;
    }
}