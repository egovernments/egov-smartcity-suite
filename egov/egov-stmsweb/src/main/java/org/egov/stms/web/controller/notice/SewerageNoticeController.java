/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

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
package org.egov.stms.web.controller.notice;

import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.stms.elasticSearch.entity.SewerageNoticeSearchRequest;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    String noticeType = null;
    String noticeTypeInput = null;
    
    private static final Logger LOGGER = Logger.getLogger(SewerageNoticeController.class);

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    
    @Autowired
    private SearchService searchService;
    @Autowired
    private CityService cityService;
     @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
    private SewerageNoticeService sewerageNoticeService;
    
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    
    @Autowired
    private FileStoreUtils fileStoreUtils;
    
    @RequestMapping(value = "/search-notice", method = RequestMethod.GET)
    public String newSearchNoticeForm(final Model model) {
        model.addAttribute("revenueWards",  boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                SewerageTaxConstants.REVENUE_WARD, REVENUE_HIERARCHY_TYPE));
        return "searchSewerageNotices";
    }
    
    @RequestMapping(value = "/searchResult",method = RequestMethod.POST)
    @ResponseBody
    public List<Document> searchApplication(@ModelAttribute final SewerageNoticeSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        //TODO: SORT BY NOTICE DATE
        final Sort sort = Sort.by().field(SewerageTaxConstants.SEARCHABLE_CONSUMER_NAME, SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);

        final List<Document> searchResultFomatted = new ArrayList<Document>(0);
        for (final Document document : searchResult.getDocuments()) {
            searchResultFomatted.add(document);
        }
        return searchResultFomatted;

    }
    
    @RequestMapping(value = "/search-NoticeResultSize",method = RequestMethod.GET)
    @ResponseBody
    public int getSerachResultCount(@ModelAttribute final SewerageNoticeSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field(SewerageTaxConstants.SEARCHABLE_CONSUMER_NAME, SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);
        return searchResult.getDocuments().size();
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/searchNotices-mergeAndDownload",method = RequestMethod.GET)
    public String mergeAndDownload(@ModelAttribute final SewerageNoticeSearchRequest searchRequest,final HttpServletResponse response) throws Exception {
        String noticeNo = null;
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field(SewerageTaxConstants.SEARCHABLE_CONSUMER_NAME, SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);
        
        List<SewerageNotice> noticeList = new ArrayList<SewerageNotice>(0);
        
        for (final Document document : searchResult.getDocuments()) {
            final Map<String, String> searchableObjects = (Map<String, String>) document.getResource()
                    .get("searchable");
            noticeTypeInput =  searchRequest.getNoticeType();
            if(noticeTypeInput != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_WORK_ORDER)){
                noticeNo = searchableObjects.get("workordernumber");
            } else if(noticeTypeInput != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_ESTIMATION)){
                noticeNo = searchableObjects.get("estimationnumber");
            }else if(noticeTypeInput !=null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION)){
                noticeNo = searchableObjects.get("closurenoticenumber");
            }
            if(noticeNo != null){
                getSewerageNoticeType(noticeNo,noticeTypeInput);
                SewerageNotice sewerageNotice =  sewerageNoticeService.findByNoticeNoAndNoticeType(noticeNo, noticeType);
                if(sewerageNotice!=null)
                noticeList.add(sewerageNotice);
            }
          
        }
       
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into mergeAndDownload method");
        final long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("mergeAndDownload : Start Time : " + startTime);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : ZERO));
        if (null == noticeList || noticeList.size() <= 0) {
          
        }

        final List<InputStream> pdfs = new ArrayList<InputStream>();
        if(noticeList != null && noticeList.size() > 0){
            for (final SewerageNotice sewerageNotice : noticeList)
                try {
                    if (sewerageNotice != null && sewerageNotice.getFileStore() != null) {
                        sewerageNotice.getApplicationDetails().getConnectionDetail().getPropertyIdentifier();
                        final FileStoreMapper fsm = sewerageNotice.getFileStore();
                        final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                        if(file.length() > 0){
                            final byte[] bFile = FileUtils.readFileToByteArray(file);
                            pdfs.add(new ByteArrayInputStream(bFile));
                        }
                    }
                } catch (final Exception e) {
                    LOGGER.error("mergeAndDownload : Getting notice failed for notice " + sewerageNotice, e);
                    continue;
                }
        }
        
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of pdfs : " + (pdfs != null ? pdfs.size() : ZERO));
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = sewerageNoticeService.mergePdfFiles(pdfs, output);
            response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + noticeType + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);

        } catch (final IOException e) {
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
    public String zipAndDownload(@ModelAttribute final SewerageNoticeSearchRequest searchRequest,final HttpServletResponse response) throws ValidationException {
        String noticeNo = null;
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field(SewerageTaxConstants.SEARCHABLE_CONSUMER_NAME, SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);

        List<SewerageNotice> noticeList = new ArrayList<SewerageNotice>(0);
        for (final Document document : searchResult.getDocuments()) {
            final Map<String, String> searchableObjects = (Map<String, String>) document.getResource()
                    .get("searchable");
            noticeTypeInput =  searchRequest.getNoticeType();
            if(noticeTypeInput != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_WORK_ORDER)){
                noticeNo = searchableObjects.get("workordernumber");
            } else if(noticeTypeInput != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_ESTIMATION)){
                noticeNo = searchableObjects.get("estimationnumber");
            }else if(noticeTypeInput !=null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION)){
                noticeNo = searchableObjects.get("closurenoticenumber");
            }
            if(noticeNo != null){
                getSewerageNoticeType(noticeNo,noticeTypeInput);
                SewerageNotice sewerageNotice =  sewerageNoticeService.findByNoticeNoAndNoticeType(noticeNo, noticeType);
                if(sewerageNotice!=null)
                noticeList.add(sewerageNotice);
            }
           
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into zipAndDownload method");
        final long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("zipAndDownload : Start Time : " + startTime);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : ZERO));
        try {
            ZipOutputStream zipOutputStream = null;
            if(noticeList != null && noticeList.size() > 0){
                zipOutputStream = new ZipOutputStream(response.getOutputStream());
                response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + ".zip");
                response.setContentType("application/zip");
            }

            for (final SewerageNotice sewerageNotice : noticeList)
                try {
                    if (sewerageNotice != null && sewerageNotice.getFileStore() != null) {
                        final FileStoreMapper fsm = sewerageNotice.getFileStore();
                        final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                        final byte[] bFile = FileUtils.readFileToByteArray(file);
                        zipOutputStream = sewerageNoticeService.addFilesToZip(new ByteArrayInputStream(bFile), file.getName(),
                                zipOutputStream);
                    }
                } catch (final Exception e) {
                    LOGGER.error("zipAndDownload : Getting notice failed for notice " + sewerageNotice, e);
                    continue;
                }
            
            zipOutputStream.closeEntry();
            zipOutputStream.close();

        } catch (final IOException e) {
            LOGGER.error("Exception in Zip and Download : ", e);
            e.printStackTrace();
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
    
    public void getSewerageNoticeType(final String noticeNo, String noticeTypeInput){
        if(noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_WORK_ORDER)){
            noticeType = SewerageTaxConstants.NOTICE_TYPE_WORK_ORDER_NOTICE;
        } else if(noticeNo != null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_ESTIMATION)){
            noticeType = SewerageTaxConstants.NOTICE_TYPE_ESTIMATION_NOTICE;
        }else if (noticeNo !=null && noticeTypeInput.equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION)){
            noticeType = SewerageTaxConstants.NOTICE_TYPE_CLOSER_NOTICE;
        }
    }
    
    @RequestMapping(value = "/searchNotices-showSewerageNotice/{noticeNo}/{noticeType}", method = RequestMethod.GET)
    public String showNotice(@PathVariable("noticeNo") final String noticeNo,@PathVariable("noticeType") final String noticeTypeInput,final Model model,
    HttpServletResponse response) throws IOException {
        if(noticeNo != null){
            getSewerageNoticeType(noticeNo,noticeTypeInput);
            SewerageNotice sewerageNotice = sewerageNoticeService.findByNoticeNoAndNoticeType(noticeNo, noticeType);
            if(sewerageNotice != null){
                final FileStoreMapper fsm = sewerageNotice.getFileStore();
                final File file = fileStoreService.fetch(fsm, SewerageTaxConstants.FILESTORE_MODULECODE);
                InputStream is = new FileInputStream(file);
                // MIME type of the file
                response.setContentType("application/pdf");
                // Response header
                response.setHeader("Content-Disposition", "attachment; filename=\""
                        + sewerageNotice.getNoticeNo() + ".pdf\"");
                // Read from the file and write into the response
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
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