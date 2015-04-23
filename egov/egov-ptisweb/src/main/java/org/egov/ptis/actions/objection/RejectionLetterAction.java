/**
 * 
 */
package org.egov.ptis.actions.objection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.PersistenceService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class RejectionLetterAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(RejectionLetterAction.class);
	private Objection objection = new Objection();
	private static final String REJECTIONLETTERTEMPLATE = "objectionRejectionLetter";
	private PersistenceService<Objection, Long> objectionService;
	protected ReportService reportService;
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	private Integer reportId = -1;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	@Override
	public Object getModel() {
		
		return objection;
	}
	
	@Override
	public void prepare() {
		
		if(null != objection.getId() ){
			objection = objectionService.findById(objection.getId(),false);
		}
	}
	@SkipValidation
	public String print(){
		
		ReportRequest reportRequest = new ReportRequest(REJECTIONLETTERTEMPLATE, objection, getParamMap());
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId =  addingReportToSession(reportOutput);
		return "print";
	}
	
	
	private Map<String, Object> getParamMap(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("date", DDMMYYYYFORMATS.format(new Date()));
		paramMap.put("objectionNo", objection.getObjectionNumber());
		paramMap.put("description", objection.getDetails());
		paramMap.put("objectionDate", DDMMYYYYFORMATS.format(objection.getRecievedOn()));
		Boundary zone = objection.getBasicProperty().getBoundary().getParent();
		paramMap.put("zoneNo",zone!=null?zone.getBoundaryNum().toString():"");
		paramMap.put("slNo", propertyTaxNumberGenerator.getRejectionLetterSerialNum());
		paramMap.put("owner", ptisCacheMgr.buildOwnerFullName(objection.getBasicProperty().getProperty().getPropertyOwnerSet()));
		paramMap.put("address", ptisCacheMgr.buildAddressByImplemetation(objection.getBasicProperty().getAddress()));
		return paramMap;
	}
	
	protected Integer addingReportToSession(ReportOutput reportOutput){
		 return	ReportViewerUtil.addReportToSession(reportOutput, getSession());
	}
	public Objection getObjection() {
		return objection;
	}

	public void setObjection(Objection objection) {
		this.objection = objection;
	}

	public void setObjectionService(
			PersistenceService<Objection, Long> objectionService) {
		this.objectionService = objectionService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

}
