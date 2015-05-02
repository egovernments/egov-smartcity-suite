/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.reports;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.egov.ptis.constants.PropertyTaxConstants.PATTERN_BEGINS_WITH_1TO9;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE_BNDRY_TYPE;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.bean.DefaultersInfo;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Transactional(readOnly = true)
@Namespace("/reports")
public class DefaultersReportAction extends BaseFormAction {

	private static final String RESULT_NEW = "new";
	private static final String RESULT_RESULT = "result";
	private static final String REPORT_TITLE = "Chennai Muncipal Corporation";
	private static final String REPORT_NO_DATA = "No Data";
	private static final Logger LOGGER = Logger.getLogger(DefaultersReportAction.class);
	private static final int MB = 1024 * 1024;
	private Integer zoneId;
	private Integer wardId;
	private String partNo;
	private Map<Long, String> ZoneBndryMap;
	private String amountRange;
	private Boundary ward;
	private ByteArrayOutputStream outputBytes;
	private List<DefaultersInfo> defaulters = new ArrayList<DefaultersInfo>();
	private Integer reportId = -1;
	private Style STYLE_BLANK = Style.createBlankStyle("oddRowNoStyle");
	private Boolean resultPage = FALSE;

	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public void prepare() {
		@SuppressWarnings("unchecked")
		List<Boundary> zoneList = persistenceService.findAllBy("FROM BoundaryImpl BI "
				+ "WHERE BI.boundaryType.name=? " + "AND BI.boundaryType.heirarchyType.name=? "
				+ "AND BI.isHistory='N' " + "ORDER BY BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		prepareWardDropDownData(zoneId != null && !zoneId.equals(-1), wardId != null && !wardId.equals(-1));
		if (wardId == null || wardId.equals(-1)) {
			addDropdownData("partNumbers", Collections.EMPTY_LIST);
		} else {
			addDropdownData(
					"partNumbers",
					getPersistenceService()
							.findAllBy(
									"SELECT distinct pmv.partNo FROM PropertyMaterlizeView pmv WHERE pmv.ward.id = ? order by pmv.partNo asc",
									getWardId()));
		}
	}

	@SuppressWarnings("unchecked")
	private void prepareReportInfo() {
		LOGGER.debug("Entered into prepareReportInfo method");
		String boundaryQuery = "FROM BoundaryImpl BI " + "WHERE BI.id = ? " + "AND BI.boundaryType.name = ? "
				+ "AND BI.boundaryType.heirarchyType.name = ? " + "AND BI.isHistory = 'N' " + "ORDER BY BI.id";
		ward = (Boundary) persistenceService.find(boundaryQuery, getWardId(), WARD_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		String[] amounts = amountRange.split(" ");
		StringBuilder query = new StringBuilder(100);
		query.append(
				"select str(pmv.ward.boundaryNum) as wardNo, pmv.partNo as partNo, pmv.propertyId as indexNo, pmv.houseNo as houseNo, pmv.ownerName as ownerName, ")
				.append("year(dmv.fromDate) as fromYear, year(dmv.toDate) as toYear, (pmv.aggrArrDmd - pmv.aggrArrColl) as arrearsDue, (pmv.aggrCurrDmd - pmv.aggrCurrColl) as currentDue,")
				.append("  (pmv.aggrArrDmd - pmv.aggrArrColl + pmv.aggrCurrDmd - pmv.aggrCurrColl) as total ")
				.append("from DefaultersMaterializedView dmv, PropertyMaterlizeView pmv where dmv.basicPropertyId = pmv.basicPropertyID and pmv.propTypeMstrID.code not in ('")
				.append(PROPTYPE_STATE_GOVT)
				.append("', '")
				.append(PROPTYPE_CENTRAL_GOVT)
				.append("') ")
				.append("and pmv.zone.id = :zoneId and pmv.ward.id = :wardId ");
		if (amounts[0].equals("100001")) {
			query.append("and (pmv.aggrArrDmd - pmv.aggrArrColl) > :fromAmt ");
		} else {
			query.append("and (pmv.aggrArrDmd - pmv.aggrArrColl) between :fromAmt and :toAmt ");
		}
		if (partNo != null && !partNo.equals("-1")) {
			query.append("and pmv.partNo = :partNo ");
		}
		query.append("order by to_number(regexp_substr(pmv.houseNo, '" + PATTERN_BEGINS_WITH_1TO9
				+ "')), pmv.houseNo asc");
		Query hqlQry = getPersistenceService().getSession().createQuery(query.toString()).setInteger("zoneId", zoneId)
				.setInteger("wardId", wardId).setBigDecimal("fromAmt", new BigDecimal(amounts[0]));
		if (!amounts[0].equals("100001")) {
			hqlQry.setBigDecimal("toAmt", new BigDecimal(amounts[1]));
		}
		if (partNo != null && !partNo.equals("-1")) {
			hqlQry.setString("partNo", partNo);
		}
		List<DefaultersInfo> defaultersInfo = hqlQry.setResultTransformer(
				Transformers.aliasToBean(DefaultersInfo.class)).list();
		defaulters.addAll(defaultersInfo);
		LOGGER.debug("Exit from prepareReportInfo method");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService().findAllBy(
					"FROM BoundaryImpl BI " + "WHERE BI.boundaryType.name=? " + "AND BI.parent.id = ? "
							+ "AND BI.isHistory='N' " + "ORDER BY BI.name ", WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("Wards", wardNewList);
		} else {
			addDropdownData("Wards", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}

	@SkipValidation
	@Action(value = "/defaultersReport.action", results = { @Result(name = RESULT_NEW) })
	public String newForm() {
		return RESULT_NEW;
	}

	@ValidationErrorPage(value = "new")
	@Action(value = "/defaultersReport-generateReport", results = { @Result(name = RESULT_RESULT) })
	public String generateReport() {
		LOGGER.debug("Entered into generateReport");
		Long startTime = System.currentTimeMillis();
		prepareReportInfo();
		if (defaulters.isEmpty()) {
			setResultPage(TRUE);
			return RESULT_NEW;
		}
		try {
			JasperPrint jasperPrint = generateDefaultersReport(defaulters);
			outputBytes = new ByteArrayOutputStream(1 * MB);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputBytes);
		} catch (Exception e) {
			LOGGER.error("Error while getting pdf stream from Jasper", e);
			throw new EGOVRuntimeException("Error while getting pdf stream from Jasper", e);
		}
		ReportOutput reportOutput = new ReportOutput();
		reportOutput.setReportOutputData(outputBytes.toByteArray());
		reportOutput.setReportFormat(FileFormat.PDF);
		reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
		LOGGER.info("Defaulters report took = " + ((System.currentTimeMillis() - startTime) / 1000) + "sec(s)....");
		return RESULT_RESULT;
	}

	private JasperPrint generateDefaultersReport(List<DefaultersInfo> defaulters) throws JRException,
			ColumnBuilderException, ClassNotFoundException {
		FastReportBuilder drb = new FastReportBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		today.set(Calendar.DAY_OF_MONTH,today.get(Calendar.DAY_OF_MONTH)-1);
		String reportDateStr = sdf.format(today.getTime());
		drb.setPrintBackgroundOnOddRows(false).setWhenNoData(REPORT_NO_DATA, null)
				.setDefaultStyles(getTitleStyle(), getSubTitleStyle(), STYLE_BLANK, STYLE_BLANK).setDetailHeight(20)
				.setUseFullPageWidth(true).setTitleHeight(50).setPageSizeAndOrientation(Page.Page_A4_Landscape())
				.setOddRowBackgroundStyle(STYLE_BLANK).setWhenResourceMissingShowKey();

		drb.addColumn("Ward No", "wardNo", String.class.getName(), 2, getTextStyleLeftBorder(),
				getHeaderStyleLeftAlign())
				.addColumn("Part No", "partNo", String.class.getName(), 2, getTextStyle(), getHeaderStyleLeftAlign())
				.addColumn("Index No", "indexNo", String.class.getName(), 5, getTextStyle(), getHeaderStyleLeftAlign())
				.addColumn("House No", "houseNo", String.class.getName(), 5, getTextStyle(), getHeaderStyleLeftAlign())
				.addColumn("Property Owner Name", "ownerName", String.class.getName(), 15, getTextStyle(),
						getHeaderStyleLeftAlign())
				.addColumn("From Year", "fromYear", Integer.class.getName(), 2, getTextStyle(),
						getHeaderStyleLeftAlign())
				.addColumn("To Year", "toYear", Integer.class.getName(), 2, getTextStyle(), getHeaderStyleLeftAlign())
				.addColumn("Arrears Balance", "arrearsDue", BigDecimal.class.getName(), 3, getAmountStyle(),
						getHeaderStyleRightAlign())
				.addColumn("Current Balance", "currentDue", BigDecimal.class.getName(), 3, getAmountStyle(),
						getHeaderStyleRightAlign())
				.addColumn("Total", "total", BigDecimal.class.getName(), 3, getAmountStyle(),
						getHeaderStyleRightAlign());

		drb.addAutoText(AutoText.AUTOTEXT_PAGE_X, AutoText.POSITION_HEADER, AutoText.ALIGMENT_RIGHT,
				AutoText.DEFAULT_WIDTH, AutoText.DEFAULT_WIDTH);
		String reportSubTitle = " List of Defaulters ";

		if (ward != null) {
			reportSubTitle += " for Ward No: " + ward.getBoundaryNum();
		}

		if (StringUtils.isNotBlank(partNo) && !partNo.equals("-1")) {
			reportSubTitle += " and Part No : " + partNo + "\\n";
		}
		if (reportDateStr != null) {
			reportSubTitle += "          (As on date " + reportDateStr + ")";
		}

		drb.setTitle(REPORT_TITLE).setTitleStyle(getTitleStyle()).setSubtitle(reportSubTitle + "\\n")
				.setSubtitleStyle(getSubTitleStyle());
		DynamicReport dr = drb.build();
		dr.setWhenNoDataShowColumnHeader(false);
		dr.setWhenNoDataShowTitle(false);
		JRDataSource ds = new JRBeanCollectionDataSource(defaulters);
		return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
	}

	@ValidationErrorPage(value = "new")
	public void validateGenerateReport() {
		LOGGER.debug("Entered into validateReport method");

		if (getZoneId() == null || getZoneId() == -1) {
			addActionError(getText("mandatory.zone"));
		}

		if (getWardId() == null || getWardId() == -1) {
			addActionError(getText("mandatory.ward"));
		}

		if ("-1".equals(amountRange)) {
			addActionError(getText("mandatory.amountRange"));
		}

		LOGGER.debug("Exiting from validateReport method");
	}

	private Style getAmountStyle() {
		Style amountStyle = new Style("amount");
		amountStyle.setTextColor(Color.BLACK);
		amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		amountStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		amountStyle.setFont(new Font(10, Font._FONT_VERDANA, false));
		amountStyle.setPaddingRight(5);
		amountStyle.setTransparency(Transparency.OPAQUE);
		amountStyle.setBorderRight(Border.THIN);
		amountStyle.setBorderBottom(Border.THIN);
		return amountStyle;
	}

	private Style getTextStyle() {
		Style textStyle = new Style("textStyle");
		textStyle.setTextColor(Color.BLACK);
		textStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		textStyle.setFont(new Font(10, Font._FONT_VERDANA, false));
		textStyle.setPaddingLeft(5);
		textStyle.setTransparency(Transparency.OPAQUE);
		textStyle.setBorderRight(Border.THIN);
		textStyle.setBorderBottom(Border.THIN);
		textStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		// textStyle.setStretchWithOverflow(true);
		return textStyle;
	}

	private Style getTextStyleLeftBorder() {
		Style textStyle = getTextStyle();
		textStyle.setName("textStyleLeftBorder");
		textStyle.setBorderLeft(Border.THIN);
		return textStyle;
	}

	private Style getSubTitleStyle() {
		Style subTitleStyle = new Style("subTitleStyle");
		subTitleStyle.setFont(new Font(12, Font._FONT_ARIAL, true, false, false));
		subTitleStyle.setTextColor(Color.BLACK);
		subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

		return subTitleStyle;
	}

	private Style getTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(Font.VERDANA_BIG_BOLD);
		titleStyle.setTextColor(Color.BLACK);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		return titleStyle;
	}

	private Style getHeaderStyle() {
		Style headerStyle = new Style("header");
		headerStyle.setFont(new Font(12, Font._FONT_ARIAL, true, false, false));
		headerStyle.setBorder(Border.THIN);
		headerStyle.setTextColor(Color.BLACK);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setStretchWithOverflow(true);
		headerStyle.setPaddingLeft(5);
		return headerStyle;
	}

	private Style getHeaderStyleLeftAlign() {
		Style headerStyle = getHeaderStyle();
		headerStyle.setName("headerStyleLeftAlign");
		headerStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		return headerStyle;
	}

	private Style getHeaderStyleRightAlign() {
		Style headerStyle = getHeaderStyle();
		headerStyle.setName("headerStyleRightAlign");
		headerStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		headerStyle.setPaddingRight(10);

		return headerStyle;
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

	public Map<Long, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Long, String> zoneBndryMap) {
		ZoneBndryMap = zoneBndryMap;
	}

	public String getAmountRange() {
		return amountRange;
	}

	public void setAmountRange(String amountRange) {
		this.amountRange = amountRange;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public List<DefaultersInfo> getDefaulters() {
		return defaulters;
	}

	public void setDefaulters(List<DefaultersInfo> defaulters) {
		this.defaulters = defaulters;
	}

	public Boolean getResultPage() {
		return resultPage;
	}

	public void setResultPage(Boolean resultPage) {
		this.resultPage = resultPage;
	}
	
}
