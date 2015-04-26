package org.egov.ptis.actions.citizen.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class CollectionAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	NMCPTBillServiceImpl nmcPtBillServiceImpl;
	PropertyTaxUtil propertyTaxUtil;
	private String collectXML;
	private String indexNum;
	private Long userId;
	//FIX ME
	//private UserDAO userDao;

	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		//User usr = (User) userDao.getUserByName("citizenUser").get(0);
		User usr = null;
		setUserId(usr.getId().longValue());
		EGOVThreadLocals.setUserId(usr.getId().toString());
		LOGGER.debug("Exit from prepare method");
	}

	public String generatePropertyTaxBill() {
		LOGGER.debug("Entered method generatePropertyTaxBill, indexNum: " + indexNum);
		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO, indexNum);
		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
		Map<String, BigDecimal> demandCollMap = propertyTaxUtil.getDemandAndCollection(basicProperty.getProperty());
		BigDecimal currDue = demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR));
		BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
		
		if ((currDue.compareTo(BigDecimal.ZERO) == 0 || currDue.compareTo(BigDecimal.ZERO) == -1)
				&& (arrDue.compareTo(BigDecimal.ZERO) == 0 || arrDue.compareTo(BigDecimal.ZERO) == -1)) {
			return "taxPaid";
		}
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setLevyPenalty(true);
		nmcPTBill.setUserId(userId);
		nmcPTBill.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID()
				.getWard().getBoundaryNum().toString()));
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
		Map<Installment, PropertyInstTaxBean> penaltyMap = nmcPTBill.getCalculatedPenalty();
		nmcPTBill.setInstTaxBean(penaltyMap);
		collectXML = URLEncoder.encode(nmcPtBillServiceImpl.getBillXML(nmcPTBill));
		LOGGER.info("Exiting method generatePropertyTaxBill, collectXML: " + collectXML);
		return "collectTax";
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public NMCPTBillServiceImpl getNmcPtBillServiceImpl() {
		return nmcPtBillServiceImpl;
	}

	public void setNmcPtBillServiceImpl(NMCPTBillServiceImpl nmcPtBillServiceImpl) {
		this.nmcPtBillServiceImpl = nmcPtBillServiceImpl;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public String getCollectXML() {
		return collectXML;
	}

	public void setCollectXML(String collectXML) {
		this.collectXML = collectXML;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(String indexNum) {
		this.indexNum = indexNum;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
