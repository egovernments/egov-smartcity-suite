/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.ptis.actions.reports;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.bean.CitizenMutationInfo;
import org.egov.ptis.bean.CitizenNoticeBean;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.notice.PtNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SURVEY_COMPARISON;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ParentPackage("egov")
@Namespace("/reports")
@ResultPath("/WEB-INF/jsp/")
@Results({
        @Result(name = SearchNoticesAction.SUCCESS, type = "stream", params = { "contentType", "${contentType}",
                "inputName", "fileStream", "contentDisposition", "attachment; filename=${fileName}" }),
        @Result(name = "RENDER_NOTICE", location = "/commons/htmlFileRenderer.jsp"),
        @Result(name = SearchNoticesAction.NEW, location = "reports/citizen-search-notice.jsp"),
        @Result(name = SearchNoticesAction.CITIZEN_SEARCH, location = "reports/citizen-searchnotice.jsp"),
        @Result(name = SearchNoticesAction.INDEX, location = "reports/searchNotices.jsp") })
public class SearchNoticesAction extends SearchFormAction {
    private static final String EXCEPTION_IN_ADD_FILES_TO_ZIP = "Exception in addFilesToZip : ";
    private static final String ERROR1 = "error";
    private static final String WARD_LIST = "wardList";
    private static final String NUMBER_OF_NOTICES = "Number of notices : ";
    private static final String PROPERTY_ID = ", propertyId: ";
    private static final Logger LOGGER = Logger.getLogger(SearchNoticesAction.class);
    private static final long serialVersionUID = 1L;
    protected static final String SUCCESS = "success";
    protected static final String CITIZEN_SEARCH = "citizen_search";
    private String ownerName;
    private Long zoneId;
    private Long wardId;
    private String propertyType;
    private String noticeType;
    private String noticeNumber;
    private Date noticeFromDate = null;
    private Date noticeToDate = null;
    private String indexNumber;
    private String houseNumber;
    private Map<String, String> noticeTypeMap;
    private String target = "new";
    private List<PtNotice> noticeList;
    private List<CitizenMutationInfo> mutationList;
    private String contentType;
    private String fileName;
    private InputStream fileStream;
    private Long contentLength;
    private String partNo;
    private transient List<CitizenNoticeBean> citizenNotices;
    private String applicationNo;

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private NoticeService noticeService;
    private String municipal;
    private String district;
    private String reportHeader;

    public SearchNoticesAction() {
        super();
    }

    @SkipValidation
    @Action(value = "/searchNotices-index")
    public String index() {
        return INDEX;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.SearchFormAction#search()
     */
    @Override
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = INDEX)
    @Action(value = "/searchNotices-search")
    public String search() {

        if (noticeType != "-1") {
            reportHeader = reportHeader + ", NoticeType: " + noticeType;
        }
        if (isNotBlank(ownerName)) {
            reportHeader = reportHeader + ", OwnerName: " + ownerName;
        }
        if (zoneId != -1) {
            reportHeader = reportHeader + ", Zone: " + getBoundary(zoneId);
        }
        if (wardId != -1) {
            reportHeader = reportHeader + ", Ward: " + getBoundary(wardId);
        }
        if (!propertyType.equalsIgnoreCase("-1")) {
            reportHeader = reportHeader + ", PropertyType: " + getPropType(propertyType);
        }
        if (isNotBlank(noticeNumber)) {
            reportHeader = reportHeader + ", noticeNum: " + noticeNumber;
        }
        if (noticeFromDate != null) {
            reportHeader = reportHeader + ", noticeDateFrom: " + noticeFromDate;
        }
        if (noticeToDate != null) {
            reportHeader = reportHeader + ", noticeDateTo: " + noticeToDate;
        }
        if (isNotBlank(indexNumber)) {
            reportHeader = reportHeader + PROPERTY_ID + indexNumber;
        }
        if (isNotBlank(houseNumber)) {
            reportHeader = reportHeader + ", HouseNo: " + houseNumber;
        }
        target = "searchresult";
        super.search();
        noticeList = searchResult.getList();
        return INDEX;
    }

    @SkipValidation
    @Action(value = "/searchNotices-citizen")
    public String citizen() {
        return NEW;
    }

    @SkipValidation
    @ValidationErrorPage(value = NEW)
    @Action(value = "/searchNotices-citizenSearch")
    public String searchNotice() {

        if (isNotBlank(indexNumber)) {
            reportHeader = reportHeader + PROPERTY_ID + indexNumber;
            setIndexNumber(indexNumber);

            mutationList = getMutationsList(indexNumber);
        } else {
            addActionError(getText("mandatory.indexNumber"));
        }

        return NEW;
    }

    private List<CitizenMutationInfo> getMutationsList(String indexNumber) {
        List<PropertyMutation> mutations = noticeService.getListofMutations(indexNumber);
        List<CitizenMutationInfo> citizenMutationInfo = new LinkedList<>();
        for (PropertyMutation mt : mutations) {
            CitizenMutationInfo citizenMutationBean = new CitizenMutationInfo();
            citizenMutationBean.setAssessmentNo(indexNumber);
            citizenMutationBean.setNewOwnerName(mt.getFullTranfereeName());
            citizenMutationBean.setOldOwnerName(mt.getFullTranferorName());
            citizenMutationBean.setApplicationNo(mt.getApplicationNo());
            citizenMutationBean.setMutationFee(mt.getMutationFee() != null ? mt.getMutationFee() : BigDecimal.ZERO);
            citizenMutationBean.setReceiptNo(mt.getReceiptNum() != null ? mt.getReceiptNum() : "");
            citizenMutationBean.setStatus(mt.getState().getValue());
            citizenMutationBean.setNotice(noticeService.getNoticeByApplicationNo(mt.getApplicationNo()));
            citizenMutationBean.setAddress(mt.getBasicProperty().getAddress().toString());
            citizenMutationInfo.add(citizenMutationBean);
        }
        return citizenMutationInfo;
    }

    /**
     * @return
     * @throws ValidationException
     */
    @ValidationErrorPage(value = INDEX)
    @Action(value = "/searchNotices-mergeAndDownload")
    public String mergeAndDownload() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into mergeAndDownload method");
        final List<PtNotice> noticeList = getNoticeBySearchParameter();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(NUMBER_OF_NOTICES + (noticeList.size()));
        if (noticeList.isEmpty()) {
            addActionError(getText("notice.file.merge.unavailable"));
            return INDEX;
        }

        final List<InputStream> pdfs = new ArrayList<>();

        for (final PtNotice ptNotice : noticeList)
            try {
                if (ptNotice != null && ptNotice.getFileStore() != null) {
                    final FileStoreMapper fsm = ptNotice.getFileStore();
                    final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                    final byte[] bFile = FileUtils.readFileToByteArray(file);
                    pdfs.add(new ByteArrayInputStream(bFile));
                }
            } catch (final Exception e) {
                LOGGER.error("mergeAndDownload : Getting notice failed for notice " + ptNotice, e);
                continue;
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Number of pdfs : " + (pdfs != null ? pdfs.size() : ZERO));
        try {
            final HttpServletResponse response = ServletActionContext.getResponse();
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final byte[] data = concatPDFs(pdfs, output);
            response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + noticeType + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);

        } catch (final IOException e) {
            LOGGER.error("Exception in Merge and Download : ", e);
            throw new ValidationException(Arrays.asList(new ValidationError(ERROR1, e.getMessage())));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exit from mergeAndDownload method");
        }
        return null;
    }

    /**
     * @return
     * @throws ValidationException
     */
    @ValidationErrorPage(value = INDEX)
    @Action(value = "/searchNotices-zipAndDownload")
    public String zipAndDownload() throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into zipAndDownload method");
        final HttpServletResponse response = ServletActionContext.getResponse();
        final List<PtNotice> noticeList = getNoticeBySearchParameter();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(NUMBER_OF_NOTICES + (noticeList != null ? noticeList.size() : ZERO));
        try {
            ZipOutputStream zipOutputStream;
            if (noticeList.isEmpty()) {
                addActionError(getText("notice.file.zip.unavailable"));
                return INDEX;
            } else {
                zipOutputStream = new ZipOutputStream(response.getOutputStream());
                response.setHeader("Content-disposition", "attachment;filename=" + "notice_" + noticeType + ".zip");
                response.setContentType("application/zip");
            }
            for (final PtNotice ptNotice : noticeList)
                try {
                    if (ptNotice != null && ptNotice.getFileStore() != null) {
                        final FileStoreMapper fsm = ptNotice.getFileStore();
                        final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                        final byte[] bFile = FileUtils.readFileToByteArray(file);
                        zipOutputStream = addFilesToZip(new ByteArrayInputStream(bFile), file.getName(),
                                zipOutputStream);
                    }
                } catch (final Exception e) {
                    LOGGER.error("zipAndDownload : Getting notice failed for notice " + ptNotice, e);
                    continue;
                }

            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (final IOException e) {
            LOGGER.error("Exception in Zip and Download : ", e);

            throw new ValidationException(Arrays.asList(new ValidationError(ERROR1, e.getMessage())));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exit from zipAndDownload method");
        }
        return null;
    }

    /**
     * This method only to show Bills as Bills(file stream) saved into PT system
     * in eg_filestoremap table column.
     *
     * @throws IOException
     */
    @SkipValidation
    @Action(value = "/searchNotices-showNotice")
    public String showNotice() throws IOException {
        final PtNotice ptNotice = (PtNotice) getPersistenceService().find("from PtNotice notice where noticeNo=?",
                noticeNumber);
        if (ptNotice == null) {
            addActionError(getText("DocMngr.file.unavailable"));
            return INDEX;
        }

        if (ptNotice.getFileStore() != null) {
            final FileStoreMapper fsm = ptNotice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            final byte[] bFile = FileUtils.readFileToByteArray(file);
            final InputStream myInputStream = new ByteArrayInputStream(bFile);
            fileStream = myInputStream;
            if(NOTICE_TYPE_SURVEY_COMPARISON.equalsIgnoreCase(noticeType))
                fileName = new StringBuffer(ptNotice.getApplicationNumber()).append("-")
                        .append(ptNotice.getNoticeType()).toString();
            else
                fileName = new StringBuffer(ptNotice.getBasicProperty().getUpicNo()).append("-")
                        .append(ptNotice.getNoticeType()).toString();
            contentType = "application/pdf";
            contentLength = Long.valueOf(file.length());
        }
        return SUCCESS;
    }

    /**
     * reset all the values
     * 
     * @return
     */
    @SkipValidation
    @Action(value = "/searchNotices-reset")
    public String reset() {
        ownerName = "";
        zoneId = -1l;
        wardId = -1l;
        propertyType = "-1";
        noticeType = "-1";
        noticeNumber = "";
        noticeFromDate = null;
        noticeToDate = null;
        indexNumber = "";
        houseNumber = "";
        return INDEX;
    }

    @Override
    public void prepare() {
        super.prepare();
        municipal = getSession().get("citymunicipalityname").toString();
        district = getSession().get("districtName").toString();
        district = district.substring(0, 1) + district.substring(1, district.length()).toLowerCase() + " District";
        reportHeader = municipal + ", " + district;
        final List<Boundary> zoneList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE.toUpperCase(), REVENUE_HIERARCHY_TYPE);
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                REVENUE_HIERARCHY_TYPE);
        final List<PropertyTypeMaster> propTypeList = propertyTypeMasterDAO.findAll();
        addDropdownData("Zone", zoneList);
        addDropdownData(WARD_LIST, wardList);
        addDropdownData("PropTypeMaster", propTypeList);
        setNoticeTypeMap(CommonServices.getNoticeTypeMstr());
    }

    @SuppressWarnings({ "unused" })
    private void prepareWardDropDownData(final boolean zoneExists, final boolean wardExists) {
        if (zoneExists && wardExists) {
            List<Boundary> wardNewList;
            wardNewList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
            addDropdownData(WARD_LIST, wardNewList);
        } else
            addDropdownData(WARD_LIST, Collections.emptyList());
    }

    /**
     * @param boundaryId
     * @return boundary name for a given boundary id
     */
    public String getBoundary(final Long boundaryId) {
        Boundary boundary = null;
        if (boundaryId != null && boundaryId != -1)
            boundary = boundaryService.getBoundaryById(boundaryId);
        return boundary.getName();
    }

    /**
     * @param propertyType
     * @return
     */
    public String getPropType(final String propertyType) {
        final PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propertyType), false);
        return propTypeMstr.getType();
    }

    /**
     * @param basicProperty
     * @return
     */
    public String getNonHistoryOwnerName(final BasicProperty basicProperty) {
        return basicProperty.getFullOwnerName();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infra.web.struts.actions.SearchFormAction#prepareQuery(java.
     * lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortDir) {
        final Map<String, Object> map = getCriteriaString();
        return new SearchQueryHQL(prepareSearchQuery(map.get("criteriaString")),
                prepareCountQuery(map.get("criteriaString")), (ArrayList<Object>) map.get("params"));
    }

    private Map<String, Object> getCriteriaString() {
        final Map<String, Object> map = new HashMap<>();
        final ArrayList<Object> params = new ArrayList<>();

        StringBuilder criteriaString;
        criteriaString = new StringBuilder(" where notice.noticeType = ?");
        params.add(noticeType);

        // To show only the active Demand Bill
        if (NOTICE_TYPE_BILL.equalsIgnoreCase(noticeType))
            criteriaString = criteriaString.append(
                    " and bill.isactive = true and bill.isHistory='N' and bill.billnumber = notice.noticeNo and pmv.propertyId=bill.assessmentNo");
        else if(NOTICE_TYPE_SURVEY_COMPARISON.equalsIgnoreCase(noticeType))
            criteriaString = criteriaString.append(" and notice.basicProperty.id=bp.id ");
        else
            criteriaString = criteriaString.append(" and bp.upicNo=pmv.propertyId");
        if (isNotBlank(ownerName)) {
            criteriaString.append(" and pmv.ownerName like ?");
            params.add("%" + ownerName + "%");
        }
        if (zoneId != null && !zoneId.equals(-1l)) {
            criteriaString.append(" and bp.propertyID.zone.id = ?");
            params.add(zoneId);
        }
        if (wardId != null && !wardId.equals(-1l)) {
            criteriaString.append(" and bp.propertyID.ward.id = ?");
            params.add(wardId);
        }
        if (propertyType != null && !propertyType.equals("-1")) {
            criteriaString.append(" and pmv.propTypeMstrID.id = ?");
            params.add(Long.parseLong(propertyType));
        }
        if (isNotBlank(noticeNumber)) {
            criteriaString.append(" and notice.noticeNo = ?");
            params.add(noticeNumber);
        }
        if (noticeFromDate != null) {
            criteriaString.append(" and notice.noticeDate >= ?");
            params.add(noticeFromDate);
        }
        if (noticeToDate != null) {
            final Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(noticeToDate);
            nextDate.add(Calendar.DATE, 1);
            criteriaString.append(" and notice.noticeDate <= ?");
            params.add(nextDate.getTime());
        }
        if (isNotBlank(indexNumber)) {
        	if(NOTICE_TYPE_SURVEY_COMPARISON.equalsIgnoreCase(noticeType))
        		criteriaString.append(" and bp.upicNo = ?");
        	else
        		criteriaString.append(" and pmv.propertyId = ?");
            params.add(indexNumber);
        }
        if (isNotBlank(houseNumber)) {
            criteriaString.append(" and bp.address.houseNoBldgApt like ?");
            params.add(houseNumber);
        }
        map.put("criteriaString", criteriaString);
        map.put("params", params);
        return map;
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void validate() {
        if (wardId == null || wardId == -1)
            addActionError(getText("mandatory.noticeward"));
        if (noticeType == null || noticeType.equals("-1"))
            addActionError(getText("mandatory.noticeType"));
        if (noticeFromDate != null && noticeToDate == null)
            addActionError(getText("mandatory.noticeTodt"));
        if (noticeToDate != null && noticeFromDate == null)
            addActionError(getText("mandatory.noticeFromdt"));
        if (noticeFromDate != null && noticeFromDate.after(new Date()))
            addActionError(getText("mandatory.noticeFromdtBeforeCurr"));
        if (noticeToDate != null && noticeToDate.after(new Date()))
            addActionError(getText("mandatory.noticeTodtBeforeCurr"));
        if (noticeFromDate != null && noticeToDate != null && noticeToDate.before(noticeFromDate))
            addActionError(getText("mandatory.noticeTodtgtoreqCurr"));
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<PtNotice> getNoticeBySearchParameter() {
        final Map<String, Object> map = getCriteriaString();
        return this.persistenceService.findAllBy(
                prepareSearchQuery(map.get("criteriaString")), ((ArrayList<Object>) map.get("params")).toArray());
    }

    /**
     * @param criteria
     * @return query string
     */
    private String prepareSearchQuery(final Object criteria) {
        final StringBuilder searchQuery = new StringBuilder("select notice");
        searchQuery.append(noticeType.equals(NOTICE_TYPE_BILL) ? " from DemandBill bill, PtNotice notice left join notice.basicProperty bp, PropertyMaterlizeView pmv"
                : NOTICE_TYPE_SURVEY_COMPARISON.equalsIgnoreCase(noticeType) ? " from PtNotice notice left join notice.basicProperty bp " : " from PtNotice notice left join notice.basicProperty bp,PropertyMaterlizeView pmv");
        searchQuery.append(criteria);
        searchQuery.append(noticeType.equals(PropertyTaxConstants.INTEGRATED_BILL) ? " order by notice.basicProperty.address.houseNoBldgApt asc" : " order by notice.noticeDate desc");
        return searchQuery.toString();
    }

    /**
     * @param criteria
     * @return count query
     */
    private String prepareCountQuery(final Object criteria) {
        final StringBuilder countQuery = new StringBuilder("select count(notice)");
        countQuery.append(noticeType.equals(NOTICE_TYPE_BILL) ? " from DemandBill bill, PtNotice notice left join notice.basicProperty bp, PropertyMaterlizeView pmv"
                : (NOTICE_TYPE_SURVEY_COMPARISON.equalsIgnoreCase(noticeType) ? " from PtNotice notice left join notice.basicProperty bp " : " from PtNotice notice left join notice.basicProperty bp,PropertyMaterlizeView pmv"));
        countQuery.append(criteria);
        return countQuery.toString();
    }

    /**
     * @param streamOfPDFFiles
     * @param outputStream
     * @return
     */
    private byte[] concatPDFs(final List<InputStream> streamOfPDFFiles, final ByteArrayOutputStream outputStream) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into concatPDFs method");
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
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from concatPDFs method");
        return outputStream.toByteArray();
    }

    protected String getContentDisposition(final ReportFormat fileFormat) {
        return "inline; filename=report." + fileFormat.toString();
    }

    /**
     * @param inputStream
     * @param noticeNo
     * @param out
     * @return zip output stream file
     */
    private ZipOutputStream addFilesToZip(final InputStream inputStream, final String noticeNo,
            final ZipOutputStream out) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into addFilesToZip method");
        final byte[] buffer = new byte[1024];
        try {
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            out.putNextEntry(new ZipEntry(noticeNo.replaceAll("/", "_")));
            int len;
            while ((len = inputStream.read(buffer)) > 0)
                out.write(buffer, 0, len);
            inputStream.close();

        } catch (final IllegalArgumentException iae) {
            LOGGER.error(EXCEPTION_IN_ADD_FILES_TO_ZIP, iae);
            throw new ValidationException(Arrays.asList(new ValidationError(ERROR1, iae.getMessage())));
        } catch (final FileNotFoundException fnfe) {
            LOGGER.error(EXCEPTION_IN_ADD_FILES_TO_ZIP, fnfe);
            throw new ValidationException(Arrays.asList(new ValidationError(ERROR1, fnfe.getMessage())));
        } catch (final IOException ioe) {
            LOGGER.error(EXCEPTION_IN_ADD_FILES_TO_ZIP, ioe);
            throw new ValidationException(Arrays.asList(new ValidationError(ERROR1, ioe.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from addFilesToZip method");
        return out;
    }

    /**
     * @param boundary
     * @return
     */
    public String getFormattedBndryStr(final Boundary boundary) {
        final StringBuilder formattedStr = new StringBuilder();
        if (boundary != null)
            formattedStr.append(boundary.getBoundaryNum().toString()).append("-").append(boundary.getName());
        return formattedStr.toString();
    }
    
    @SkipValidation
    @Action(value = "/citizen-searchnotices")
    public String searchNotices() {
        return CITIZEN_SEARCH;
    }

    @SkipValidation
    @ValidationErrorPage(value = CITIZEN_SEARCH)
    @Action(value = "/searchnotice-result")
    public String getNotices() {

        if (isNotBlank(indexNumber)) {
            reportHeader = reportHeader + PROPERTY_ID + indexNumber;
            setIndexNumber(indexNumber);

            citizenNotices = getAllNoticesByAssessmentNo(indexNumber);
        } else {
            reportHeader = reportHeader + ", applicationNumber: " + applicationNo;
            setApplicationNo(applicationNo);
            List<CitizenNoticeBean> citizenNoticeList = new LinkedList<>();
            CitizenNoticeBean noticeBean = getNoticeByApplicationNo(applicationNo);
            if (isNotBlank(noticeBean.getNotice())) {
                citizenNoticeList.add(noticeBean);
                setCitizenNotices(citizenNoticeList);
            }
        }

        return CITIZEN_SEARCH;
    }

    private List<CitizenNoticeBean> getAllNoticesByAssessmentNo(final String assessementNumber) {
        List<PtNotice> ptNoticeList = noticeService.getNoticeByAssessmentNumner(assessementNumber);
        List<CitizenNoticeBean> citizenNoticeList = new LinkedList<>();
        if (!ptNoticeList.isEmpty()) {
            for (PtNotice notice : ptNoticeList) {
                CitizenNoticeBean citizenNoticeBean = new CitizenNoticeBean();
                setNoticeDetails(citizenNoticeBean, notice);
                citizenNoticeList.add(citizenNoticeBean);
            }
        }
        return citizenNoticeList;
    }

    private CitizenNoticeBean getNoticeByApplicationNo(final String applicationNumber) {
        PtNotice ptNotice = noticeService.getNoticeByApplicationNumber(applicationNumber);
        CitizenNoticeBean citizenNoticeBean = new CitizenNoticeBean();
        if (ptNotice != null)
            setNoticeDetails(citizenNoticeBean, ptNotice);
        return citizenNoticeBean;
    }

    private void setNoticeDetails(CitizenNoticeBean citizenNoticeBean, PtNotice notice) {
        citizenNoticeBean.setAssessmentNo(notice.getBasicProperty().getUpicNo());
        citizenNoticeBean.setNotice(notice.getNoticeNo());
        citizenNoticeBean.setNoticeDate(notice.getNoticeDate());
        citizenNoticeBean.setNoticeType(notice.getNoticeType());
        citizenNoticeBean.setAddress(notice.getBasicProperty().getAddress().toString());
        citizenNoticeBean.setOwnerName(notice.getBasicProperty().getPrimaryOwner().getName());
        citizenNoticeBean.setHouseNo(notice.getBasicProperty().getAddress().getHouseNoBldgApt());
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(final String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(final String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public Date getNoticeFromDate() {
        return noticeFromDate;
    }

    public void setNoticeFromDate(final Date noticeFromDate) {
        this.noticeFromDate = noticeFromDate;
    }

    public Date getNoticeToDate() {
        return noticeToDate;
    }

    public void setNoticeToDate(final Date noticeToDate) {
        this.noticeToDate = noticeToDate;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(final String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Map<String, String> getNoticeTypeMap() {
        return noticeTypeMap;
    }

    public void setNoticeTypeMap(final Map<String, String> noticeTypeMap) {
        this.noticeTypeMap = noticeTypeMap;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public List<PtNotice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(final List<PtNotice> noticeList) {
        this.noticeList = noticeList;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(final InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(final Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(final String partNo) {
        this.partNo = partNo;
    }

    public String getMunicipal() {
        return municipal;
    }

    public void setMunicipal(String municipal) {
        this.municipal = municipal;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(String reportHeader) {
        this.reportHeader = reportHeader;
    }

    public List<CitizenMutationInfo> getMutationList() {
        return mutationList;
    }

    public void setMutationList(List<CitizenMutationInfo> mutationList) {
        this.mutationList = mutationList;
    }

    public List<CitizenNoticeBean> getCitizenNotices() {
        return citizenNotices;
    }

    public void setCitizenNotices(List<CitizenNoticeBean> citizenNotices) {
        this.citizenNotices = citizenNotices;
    }
    
    public void addCitizenNotices(CitizenNoticeBean citizenNoticeBean){
        this.citizenNotices.add(citizenNoticeBean);
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

}
