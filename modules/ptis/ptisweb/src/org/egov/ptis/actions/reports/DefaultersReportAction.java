package org.egov.ptis.actions.reports;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.bean.DefaultersInfo;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

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
public class DefaultersReportAction extends BaseFormAction {
	
	private static final String RESULT_NEW = "new";
	private static final String RESULT_RESULT = "result";
	private static final String REPORT_TITLE = "Nagpur Muncipal Corporation";
	private static final String REPORT_NO_DATA = "No Data";
	private static final Logger LOGGER = Logger.getLogger(DefaultersReportAction.class);
	
	private static final int MB = 1024 * 1024;
	
	
	private Integer zoneId;
	private Integer wardId;
	private Map<Integer, String> ZoneBndryMap;
	private String amountRange;

	private Boundary zone;
	private Boundary ward;
	private ByteArrayOutputStream outputBytes;
	private List<DefaultersInfo> defaulters = new ArrayList<DefaultersInfo>();
	private Integer reportId = -1;
	
	private Style STYLE_BLANK = Style.createBlankStyle("oddRowNoStyle");
	
	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	public void prepare() {
		
		@SuppressWarnings("unchecked")
		List<Boundary> zoneList = persistenceService.findAllBy(
				"FROM BoundaryImpl BI " +
				"WHERE BI.boundaryType.name=? " +
				"AND BI.boundaryType.heirarchyType.name=? " +
				"AND BI.isHistory='N' " +
				"ORDER BY BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		prepareWardDropDownData(zoneId != null && !zoneId.equals(-1), wardId != null && !wardId.equals(-1));
	}
	
	private void prepareReportInfo() {		
		LOGGER.debug("Entered into prepareReportInfo method");
		
		//ReportInfo reportInfo = new ReportInfo();
		
		String boundaryQuery = "FROM BoundaryImpl BI " +
								"WHERE BI.id = ? " +
							   "AND BI.boundaryType.name = ? " +
							   "AND BI.boundaryType.heirarchyType.name = ? " +
							   "AND BI.isHistory = 'N' " +
							   "ORDER BY BI.id";
		
		zone = (Boundary) persistenceService.find(boundaryQuery, getZoneId(), ZONE_BNDRY_TYPE,
				REVENUE_HIERARCHY_TYPE);
		ward = (Boundary) persistenceService.find(boundaryQuery, getWardId(), WARD_BNDRY_TYPE,
				REVENUE_HIERARCHY_TYPE);
		
		String[] amounts = amountRange.split(" ");
		
		
		StringBuilder query = new StringBuilder(100);
		
		query.append("FROM PropertyMaterlizeView WHERE ");
		
		if (wardId != null && !wardId.equals(-1)) {
			query.append("wardID = ? AND ");
		} else {
			query.append("zoneID = ? AND ");
		}
		
		query.append("aggrArrDmd BETWEEN ? AND ? "); 
		
		if (wardId == null || wardId.equals(-1)) {
		   query.append(" ORDER BY wardID, partNo, ");
		} else {
		   query.append(" ORDER BY partNo, ");
		}
		
		query.append("aggrArrDmd DESC");
		
		@SuppressWarnings("unchecked")
		List<PropertyMaterlizeView> properties = getPersistenceService().findAllBy(query.toString(),
				((wardId != null && !wardId.equals(-1)) ? wardId : zoneId), new BigDecimal(amounts[0]),
				new BigDecimal(amounts[1]));

		for (PropertyMaterlizeView property : properties) {
			DefaultersInfo defaulter = new DefaultersInfo();
			defaulter.setZoneNo(zone.getBoundaryNum().toString());
			
			if (ward != null) {
				defaulter.setWardNo(ward.getBoundaryNum().toString());
			} else {
				Boundary wardBoundary = (Boundary) persistenceService.find(boundaryQuery, property.getWardID(),
						WARD_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
				defaulter.setWardNo(wardBoundary.getBoundaryNum().toString());
			}
			
			defaulter.setPartNo(property.getPartNo());
			defaulter.setIndexNo(property.getPropertyId());
			defaulter.setHouseNo(property.getHouseNo());
			defaulter.setOwnerName(property.getOwnerName());
			defaulter.setArrearsTax(property.getAggrArrDmd());
			defaulter.setCurrentTax(property.getAggrCurrDmd());		
			defaulter.setTotal(property.getAggrArrDmd().add(property.getAggrCurrDmd()));
			defaulters.add(defaulter);
		}
		
		LOGGER.debug("Exit from prepareReportInfo method");
	}
	
	
	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService().findAllBy(
							"FROM BoundaryImpl BI " +
							"WHERE BI.boundaryType.name=? " +
							"AND BI.parent.id = ? " +
							"AND BI.isHistory='N' " +
							"ORDER BY BI.name ",
							WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("Wards", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}
	
	@SkipValidation
	public String newForm() {		
		return RESULT_NEW;
	}
	
	@ValidationErrorPage(value="new")
	public String generateReport() {
		
		prepareReportInfo();
		
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
		
		return RESULT_RESULT;
	}
	
	private JasperPrint generateDefaultersReport(List<DefaultersInfo> defaulters) throws JRException, ColumnBuilderException, ClassNotFoundException {
		FastReportBuilder drb = new FastReportBuilder();
		drb.setPrintBackgroundOnOddRows(false)
		.setWhenNoData(REPORT_NO_DATA, null)
		.setDefaultStyles(getTitleStyle(), getSubTitleStyle(), STYLE_BLANK, STYLE_BLANK)
		.setDetailHeight(20)
		.setUseFullPageWidth(true)
		.setTitleHeight(50)		
		.setPageSizeAndOrientation(Page.Page_A4_Landscape())
		.setOddRowBackgroundStyle(STYLE_BLANK);
			
		drb.addColumn("Ward No", "wardNo", String.class.getName(), 2, getTextStyleLeftBorder(), getHeaderStyleLeftAlign())
		   .addColumn("Part No", "partNo", String.class.getName(), 2, getTextStyle(), getHeaderStyleLeftAlign())
		   .addColumn("Index No", "indexNo", String.class.getName(), 5, getTextStyle(), getHeaderStyleLeftAlign())
		   .addColumn("House No", "houseNo", String.class.getName(), 5, getTextStyle(), getHeaderStyleLeftAlign())
		   .addColumn("Property Owner Name", "ownerName", String.class.getName(), 15, getTextStyle(), getHeaderStyleLeftAlign())
		   .addColumn("Arrears Tax", "arrearsTax", BigDecimal.class.getName(), 3, getAmountStyle(), getHeaderStyleRightAlign())
		   .addColumn("Current Tax", "currentTax", BigDecimal.class.getName(), 3, getAmountStyle(), getHeaderStyleRightAlign())
		   .addColumn("Total", "total", BigDecimal.class.getName(), 3, getAmountStyle(), getHeaderStyleRightAlign());
		
		drb.addAutoText(AutoText.AUTOTEXT_PAGE_X, AutoText.POSITION_HEADER, AutoText.ALIGMENT_RIGHT,
				AutoText.DEFAULT_WIDTH, AutoText.DEFAULT_WIDTH);
		String reportSubTitle = " List of Defaulters " ;
		
		if (ward != null) {
			reportSubTitle += " for Ward No: " + ward.getBoundaryNum();
		}
		
		drb.setTitle(REPORT_TITLE).setTitleStyle(getTitleStyle()).setSubtitle(reportSubTitle + "\\n").setSubtitleStyle(getSubTitleStyle());
		DynamicReport dr = drb.build();
		dr.setWhenNoDataShowColumnHeader(false);
		dr.setWhenNoDataShowTitle(false);
		JRDataSource ds = new JRBeanCollectionDataSource(defaulters);  
		return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
	}
	
	
	@ValidationErrorPage(value="new")
	public void validateGenerateReport() {
		LOGGER.debug("Entered into validateReport method");
		
		if (getZoneId() == null || getZoneId() == -1) {
			addActionError(getText("mandatory.zone"));
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
		amountStyle.setFont(new Font(10, Font._FONT_VERDANA, false));
		amountStyle.setPaddingRight(5);
		amountStyle.setTransparency(Transparency.OPAQUE);
		amountStyle.setBorderRight(Border.THIN());
		amountStyle.setBorderBottom(Border.THIN());
		return amountStyle;
	}
	
	private Style getTextStyle() {
		Style textStyle = new Style("textStyle");
		textStyle.setTextColor(Color.BLACK);
		textStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		textStyle.setFont(new Font(10, Font._FONT_VERDANA, false));
		textStyle.setPaddingLeft(5);
		textStyle.setTransparency(Transparency.OPAQUE);		
		textStyle.setBorderRight(Border.THIN());
		textStyle.setBorderBottom(Border.THIN());				
		textStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		//textStyle.setStretchWithOverflow(true);		
		return textStyle;
	}
	
	private Style getTextStyleLeftBorder() {
		Style textStyle = getTextStyle();
		textStyle.setName("textStyleLeftBorder");
		textStyle.setBorderLeft(Border.THIN());		
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
		headerStyle.setBorder(Border.THIN());
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

	public Map<Integer, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Integer, String> zoneBndryMap) {
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
}
