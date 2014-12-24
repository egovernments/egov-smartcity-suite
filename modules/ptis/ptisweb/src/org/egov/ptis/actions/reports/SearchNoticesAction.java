package org.egov.ptis.actions.reports;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.citizen.model.Owner;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.domain.dao.property.PropertyHibernateDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.opensymphony.xwork2.Action;

@ParentPackage("egov")
public class SearchNoticesAction extends SearchFormAction {
	private static final Logger LOGGER = Logger.getLogger(SearchNoticesAction.class);
	private static final long serialVersionUID = 1L;
	private String ownerName;
	private Integer zoneId;
	private Integer wardId;
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
	private transient DocumentManagerService<DocumentObject> documentManagerService;

	public SearchNoticesAction() {
		super();
	}

	@SkipValidation
	public String index() {
		return INDEX;
	}

	@SuppressWarnings("unchecked")
	public String search() {
		LOGGER.debug("Entered into search method");
		LOGGER.debug("Owner name : " + ownerName + ", " + "Notice Type : " + noticeType + ", " + "Zone Id : " + zoneId
				+ ", " + "Ward Id : " + wardId + ", " + "Property type :" + propertyType + ", " + "Notice Number : "
				+ noticeNumber + ", " + "Notice FromDate : " + noticeFromDate + ", " + "noticeToDate : " + noticeToDate
				+ ", " + "Property Id : " + indexNumber + ", " + "House Number : " + houseNumber);
		target = "searchresult";
		super.search();
		noticeList = searchResult.getList();
		if (noticeList != null && !noticeList.isEmpty()) {
			LOGGER.debug("Number of notices before owner name (if input given) filter : " + noticeList.size());
			searchOwnerNamePropType();
		}
		LOGGER.debug("Number of notices after owner name (if input given) filter : " + noticeList.size());
		LOGGER.debug("Exit from search method");
		return EDIT;
	}

	public String mergeAndDownload() throws ValidationException {
		LOGGER.debug("Entered into mergeAndDownload method");
		long startTime = System.nanoTime();
		LOGGER.debug("mergeAndDownload : Start Time : " + startTime);
		List<PtNotice> noticeList = getNoticeBySearchParameter();
		LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : ZERO));
		if (null == noticeList || noticeList.size() <= 0) {
			addActionError(getText("notice.file.merge.unavailable"));
			return Action.ERROR;
		}

		List<InputStream> pdfs = new ArrayList<InputStream>();

		for (PtNotice ptNotice : noticeList) {
			AssociatedFile file = documentManagerService.getFileFromDocumentObject(ptNotice.getNoticeNo(), "PT",
					ptNotice.getNoticeNo() + ".pdf");
			pdfs.add(file.getFileInputStream());
		}
		LOGGER.debug("Number of pdfs : " + (pdfs != null ? pdfs.size() : ZERO));
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] data = concatPDFs(pdfs, output);
			response.setHeader("Content-disposition", "attachment;filename=" + "notice.pdf");
			response.setContentType("application/pdf");
			response.setContentLength(data.length);
			response.getOutputStream().write(data);

		} catch (IOException e) {
			LOGGER.error("Exception in Merge and Download : ", e);
			e.printStackTrace();
			throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
		}
		long endTime = System.nanoTime();
		LOGGER.debug("mergeAndDownload : End Time : " + endTime);
		LOGGER.debug("SearchNoticesAction | mergeAndDownload | Took " + (endTime - startTime) + " nano seconds");
		LOGGER.debug("Exit from mergeAndDownload method");
		return null;
	}

	public String zipAndDownload() throws ValidationException {
		LOGGER.debug("Entered into zipAndDownload method");
		long startTime = System.nanoTime();
		LOGGER.debug("zipAndDownload : Start Time : " + startTime);
		HttpServletResponse response = ServletActionContext.getResponse();
		List<PtNotice> noticeList = getNoticeBySearchParameter();
		LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : ZERO));
		try {
			ZipOutputStream zipOutputStream;
			if (null == noticeList || noticeList.size() <= 0) {
				addActionError(getText("notice.file.zip.unavailable"));
				return Action.ERROR;
			} else {
				zipOutputStream = new ZipOutputStream(response.getOutputStream());
				response.setHeader("Content-disposition", "attachment;filename=" + "notice.zip");
				response.setContentType("application/zip");
			}

			for (PtNotice ptNotice : noticeList) {
				AssociatedFile file = documentManagerService.getFileFromDocumentObject(ptNotice.getNoticeNo(), "PT",
						ptNotice.getNoticeNo() + ".pdf");
				zipOutputStream = addFilesToZip(file.getFileInputStream(), file.getFileName(), zipOutputStream);
			}
			zipOutputStream.closeEntry();
			zipOutputStream.close();

		} catch (IOException e) {
			LOGGER.error("Exception in Zip and Download : ", e);
			e.printStackTrace();
			throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
		}
		long endTime = System.nanoTime();
		LOGGER.debug("zipAndDownload : End Time : " + endTime);
		LOGGER.debug("SearchNoticesAction | zipAndDownload | Took " + (endTime - startTime) + " nano seconds");
		LOGGER.debug("Exit from zipAndDownload method");
		return null;
	}

	public String reset() {
		LOGGER.debug("reset : Before reset values : ownerName : " + ownerName + " zoneId : " + zoneId + " wardId : "
				+ wardId + " propertyType : " + propertyType + " noticeType : " + noticeType + " noticeNumber : "
				+ noticeNumber + " noticeFromDate : " + noticeFromDate + " noticeToDate : " + noticeToDate
				+ " indexNumber : " + indexNumber + " houseNumber : " + houseNumber);
		ownerName = "";
		zoneId = -1;
		wardId = -1;
		propertyType = "-1";
		noticeType = "-1";
		noticeNumber = "";
		noticeFromDate = null;
		noticeToDate = null;
		indexNumber = "";
		houseNumber = "";
		LOGGER.debug("Exit from reset method");
		return EDIT;
	}

	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		super.prepare();
		List<Boundary> zoneList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.name", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy("from PropertyTypeMaster");

		addDropdownData("Zone", zoneList);
		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		prepareWardDropDownData(zoneId != null, wardId != null);
		addDropdownData("PropTypeMaster", propTypeList);
		setNoticeTypeMap(CommonServices.getNoticeTypeMstr());
		LOGGER.debug("Zone List : " + (zoneList != null ? zoneList : ZERO));
		LOGGER.debug("Property type List : " + (propTypeList != null ? propTypeList : ZERO));
		LOGGER.debug("Notice type map size : " + (noticeTypeMap != null ? noticeTypeMap.size() : ZERO));
		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService()
					.findAllBy(
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}

	public String getBoundary(Integer boundaryId) {
		LOGGER.debug("Entered into getBoundary method");
		LOGGER.debug("Boundary Id : " + boundaryId);
		Boundary bndry = null;
		if (boundaryId != null && !boundaryId.equals(Integer.valueOf(-1))) {
			bndry = (Boundary) getPersistenceService().find("from BoundaryImpl BI where id = ?", boundaryId);
		}
		LOGGER.debug("Boundary : " + bndry);
		LOGGER.debug("Exit from getBoundary method");
		return bndry.getName();
	}

	public String getPropType(String propertyType) {
		LOGGER.debug("Entered into getPropType method");
		LOGGER.debug("Property type id : " + propertyType);
		PropertyTypeMasterDAO propTypeMstrDao = PropertyHibernateDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		PropertyTypeMaster propTypeMstr = propTypeMstrDao.getPropertyTypeMasterById(Integer.valueOf(propertyType));
		LOGGER.debug("Property type : " + propTypeMstr);
		LOGGER.debug("Exit from getPropType method");
		return propTypeMstr.getType();
	}

	/**
	 * @param noticeList
	 *            This method removes the notices from the list which do not
	 *            match the selected Owner Name and Property Type
	 */
	@SuppressWarnings("unchecked")
	private void searchOwnerNamePropType() {
		LOGGER.debug("Entered into searchOwnerNamePropType method");
		LOGGER.debug("searchOwnerNamePropType : Owner Name : " + ownerName + ", " + "Property Type : " + propertyType);
		LOGGER.debug("searchOwnerNamePropType : Number of notices before removal: "
				+ (noticeList != null ? noticeList.size() : ZERO));
		if ((ownerName != null && !ownerName.equals("")) || (propertyType != null && !propertyType.equals("-1"))) {
			List<PtNotice> noticeRmvList = new ArrayList();
			for (PtNotice notice : noticeList) {
				Property prop = notice.getBasicProperty().getProperty();
				LOGGER.debug("Property : " + prop);
				if (ownerName != null && !ownerName.equals("")) {
					boolean isOwnerExist = true;
					for (Owner propOwner : prop.getPropertyOwnerSet()) {
						if (!getOwnerName().equalsIgnoreCase(propOwner.getFirstName())) {
							noticeRmvList.add(notice);
							isOwnerExist = false;
							break;
						}
					}
					if (!isOwnerExist)
						continue;
				}
				if (propertyType != null && !propertyType.equals("-1")) {
					if (!getPropType(getPropertyType()).equals(
							prop.getPropertyDetail().getPropertyTypeMaster().getType())) {
						noticeRmvList.add(notice);
					}
				}
			}
			LOGGER.debug("searchOwnerNamePropType : Number of notices to be removed : "
					+ (noticeRmvList != null ? noticeRmvList.size() : ZERO));
			noticeList.removeAll(noticeRmvList);
			LOGGER.debug("searchOwnerNamePropType : Number of notices after removal: "
					+ (noticeList != null ? noticeList.size() : ZERO));
			((EgovPaginatedList) searchResult).setFullListSize(noticeList.size());
		}
		LOGGER.debug("Exit from searchOwnerNamePropType method");
	}

	public String getNonHistoryOwnerName(BasicProperty basicProperty) {
		LOGGER.debug("Entered into getNonHistoryOwnerName method");
		LOGGER.debug("getNonHistoryOwnerName : Basic Property : " + basicProperty);
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		String NonHistoryOwnerName = ptisCacheMgr.buildOwnerFullName(basicProperty.getProperty().getPropertyOwnerSet());
		LOGGER.debug("getNonHistoryOwnerName : Non-History Owner Name : " + NonHistoryOwnerName);
		LOGGER.debug("Exit from getNonHistoryOwnerName method");
		return NonHistoryOwnerName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SearchQuery prepareQuery(String sortField, String sortDir) {
		LOGGER.debug("Entered into prepareQuery method");
		LOGGER.debug("Sort Field : " + sortField + ", " + "Sort Dir : " + sortDir);
		StringBuilder searchQueryString = new StringBuilder("select distinct notice ");
		StringBuilder countQueryString = new StringBuilder("select count(distinct notice) ");
		StringBuilder fromString = new StringBuilder(
				" from org.egov.ptis.notice.PtNotice notice left join notice.basicProperty bp");
		
		StringBuilder fromClauseForBill = new StringBuilder(
				"FROM EgBill bill, PtNotice notice left join notice.basicProperty bp");
		
		final String orderByString = " order by notice.noticeDate desc";

		Map<String, Object> map = getCriteriaString();
		String searchQuery = null;
		String criteriaAndOrder = new StringBuilder().append(map.get("criteriaString"))
												.append(orderByString).toString();

		searchQuery = NOTICE_TYPE_BILL.equalsIgnoreCase(noticeType) 
						? searchQueryString.append(fromClauseForBill).toString() 
						: searchQueryString.append(fromString).toString();
		searchQuery = searchQueryString.append(criteriaAndOrder).toString();

		LOGGER.debug("Search Query : " + searchQuery);

		String countQuery = NOTICE_TYPE_BILL.equalsIgnoreCase(noticeType) 
						? countQueryString.append(fromClauseForBill).toString() 
						: countQueryString.append(fromString).toString();
		countQuery = countQuery + criteriaAndOrder;
				
		LOGGER.debug("Count Query : " + countQuery);
		LOGGER.debug("Exit from prepareQuery method");
		return new SearchQueryHQL(searchQuery, countQuery, (ArrayList<Object>) map.get("params"));
	}

	private Map<String, Object> getCriteriaString() {
		LOGGER.debug("Entered into getCriteriaString method");
		LOGGER.debug("Notice Type : " + noticeType + ", " + "Zone Id : " + zoneId + ", " + "Ward Id : " + wardId + ", "
				+ "Notice Number : " + noticeNumber + ", " + "Notice FromDate : " + noticeFromDate + ", "
				+ "noticeToDate : " + noticeToDate + ", " + "Property Id : " + indexNumber + ", " + "House Number : "
				+ houseNumber);
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder criteriaString = new StringBuilder(" where "); 		
		params.add(noticeType);
		
		// To show only the active Demand Bill
		if (NOTICE_TYPE_BILL.equalsIgnoreCase(noticeType)) {
			criteriaString = criteriaString.append(" bill.is_History = 'N' and bill.billNo = notice.noticeNo and ");
		}

		criteriaString = criteriaString.append(" notice.noticeType = ? ");
		
		if (zoneId != null && !zoneId.equals(Integer.valueOf(-1))) {
			criteriaString.append(" and bp.propertyID.zone.id = ?");
			params.add(zoneId);
		}
		if (wardId != null && !wardId.equals(Integer.valueOf(-1))) {
			criteriaString.append(" and bp.propertyID.ward.id = ?");
			params.add(wardId);
		}
		if (noticeNumber != null && !noticeNumber.equals("")) {
			criteriaString.append(" and notice.noticeNo = ?");
			params.add(noticeNumber);
		}
		if (noticeFromDate != null && !noticeFromDate.equals("DD/MM/YYYY")) {
			criteriaString.append(" and notice.noticeDate >= ?");
			params.add(noticeFromDate);
		}
		if (noticeToDate != null && !noticeToDate.equals("DD/MM/YYYY")) {
			Calendar nextDate = Calendar.getInstance();
			nextDate.setTime(noticeToDate);
			nextDate.add(Calendar.DATE, 1);
			criteriaString.append(" and notice.noticeDate <= ?");
			params.add(nextDate.getTime());
		}
		if (indexNumber != null && !indexNumber.equals("")) {
			criteriaString.append(" and bp.upicNo = ?");
			params.add(indexNumber);
		}
		if (houseNumber != null && !houseNumber.equals("")) {
			criteriaString.append(" and bp.address.houseNo like ?");
			params.add(houseNumber);
		}
		map.put("criteriaString", criteriaString);
		map.put("params", params);
		LOGGER.debug("Criteria String : " + criteriaString);
		LOGGER.debug("Exit from getCriteriaString method");
		return map;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void validate() {
		LOGGER.debug("Entered into validate method");
		if (noticeType == null || noticeType.equals("-1")) {
			addActionError(getText("mandatory.noticeType"));
		}
		if (noticeFromDate != null && !noticeFromDate.equals("DD/MM/YYYY")
				&& (noticeToDate == null || noticeToDate.equals("DD/MM/YYYY"))) {
			addActionError(getText("mandatory.noticeTodt"));
		}
		if (noticeToDate != null && !noticeToDate.equals("DD/MM/YYYY")
				&& (noticeFromDate == null || noticeFromDate.equals("DD/MM/YYYY"))) {
			addActionError(getText("mandatory.noticeFromdt"));
		}
		if (noticeFromDate != null && !noticeFromDate.equals("DD/MM/YYYY") && noticeFromDate.after(new Date())) {
			addActionError(getText("mandatory.noticeFromdtBeforeCurr"));
		}
		if (noticeToDate != null && !noticeToDate.equals("DD/MM/YYYY") && noticeToDate.after(new Date())) {
			addActionError(getText("mandatory.noticeTodtBeforeCurr"));
		}
		if (noticeFromDate != null && !noticeFromDate.equals("DD/MM/YYYY") && noticeToDate != null
				&& !noticeToDate.equals("DD/MM/YYYY") && noticeToDate.before(noticeFromDate)) {
			addActionError(getText("mandatory.noticeTodtgtoreqCurr"));
		}
		LOGGER.debug("Exit from validate method");
	}

	@SuppressWarnings("unchecked")
	private List<PtNotice> getNoticeBySearchParameter() {
		LOGGER.debug("Entered into getNoticeBySearchParameter method");

		StringBuilder searchQueryString = new StringBuilder("select distinct notice ");
		StringBuilder fromString = new StringBuilder(
				" from org.egov.ptis.notice.PtNotice notice left join notice.basicProperty bp");

		final String orderByString = " order by notice.noticeDate desc";

		Map<String, Object> map = getCriteriaString();
		String searchQuery = searchQueryString.append(fromString).append(map.get("criteriaString"))
				.append(orderByString).toString();
		LOGGER.debug("Search Query : " + searchQuery);
		List<PtNotice> noticeList = (List<PtNotice>) persistenceService.findAllBy(searchQuery,
				((ArrayList<Object>) map.get("params")).toArray());
		LOGGER.debug("Number of notices : " + (noticeList != null ? noticeList.size() : ZERO));
		LOGGER.debug("Exit from getNoticeBySearchParameter method");
		return noticeList;
	}

	private byte[] concatPDFs(List<InputStream> streamOfPDFFiles, ByteArrayOutputStream outputStream) {
		LOGGER.debug("Entered into concatPDFs method");
		Document document = null;
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				if (null == document)
					document = new Document(pdfReader.getPageSize(1));
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();

		} catch (Exception e) {
			LOGGER.error("Exception in concat PDFs : ", e);
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				LOGGER.error("Exception in concat PDFs : ", ioe);
				ioe.printStackTrace();
			}
		}
		LOGGER.debug("Exit from concatPDFs method");
		return outputStream.toByteArray();
	}

	protected String getContentDisposition(FileFormat fileFormat) {
		return "inline; filename=report." + fileFormat.toString();
	}

	private ZipOutputStream addFilesToZip(InputStream inputStream, String noticeNo, ZipOutputStream out) {
		LOGGER.debug("Entered into addFilesToZip method");
		byte[] buffer = new byte[1024];
		try {
			out.setLevel(Deflater.DEFAULT_COMPRESSION);
			out.putNextEntry(new ZipEntry(noticeNo.replaceAll("/", "_")));
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			inputStream.close();

		} catch (IllegalArgumentException iae) {
			LOGGER.error("Exception in addFilesToZip : ", iae);
			iae.printStackTrace();
			throw new ValidationException(Arrays.asList(new ValidationError("error", iae.getMessage())));
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("Exception in addFilesToZip : ", fnfe);
			fnfe.printStackTrace();
			throw new ValidationException(Arrays.asList(new ValidationError("error", fnfe.getMessage())));
		} catch (IOException ioe) {
			LOGGER.error("Exception in addFilesToZip : ", ioe);
			ioe.printStackTrace();
			throw new ValidationException(Arrays.asList(new ValidationError("error", ioe.getMessage())));
		}
		LOGGER.debug("Exit from addFilesToZip method");
		return out;
	}

	public String getFormattedBndryStr(BoundaryImpl boundary) {
		LOGGER.debug("Entered into getFormattedBndryStr method");
		LOGGER.debug("boundary : " + boundary);
		StringBuilder formattedStr = new StringBuilder();
		if (boundary != null) {
			formattedStr.append(boundary.getBoundaryNum().toString()).append("-").append(boundary.getName());
		}
		LOGGER.debug("formattedStr : " + formattedStr.toString());
		LOGGER.debug("Exit from getFormattedBndryStr method");
		return formattedStr.toString();
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public Date getNoticeFromDate() {
		return noticeFromDate;
	}

	public void setNoticeFromDate(Date noticeFromDate) {
		this.noticeFromDate = noticeFromDate;
	}

	public Date getNoticeToDate() {
		return noticeToDate;
	}

	public void setNoticeToDate(Date noticeToDate) {
		this.noticeToDate = noticeToDate;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public Map<String, String> getNoticeTypeMap() {
		return noticeTypeMap;
	}

	public void setNoticeTypeMap(Map<String, String> noticeTypeMap) {
		this.noticeTypeMap = noticeTypeMap;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<PtNotice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<PtNotice> noticeList) {
		this.noticeList = noticeList;
	}

	public void setDocumentManagerService(DocumentManagerService<DocumentObject> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}
}
