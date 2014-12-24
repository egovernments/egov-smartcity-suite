package org.egov.ptis.actions.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.egov.web.actions.BaseFormAction;

public class CollectPropertyTaxAction extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(CollectPropertyTaxAction.class);
	PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	PropertyTaxCollection propertyTaxCollection;
	NMCPTBillServiceImpl nmcPtBillServiceImpl;
	PropertyTaxUtil propertyTaxUtil;
	private String propertyId;
	BasicProperty basicProperty;
	private String collectXML = "";
	private Boolean levyPenalty;
	private List<PropertyInstTaxBean> instTaxBeanList = new ArrayList<PropertyInstTaxBean>();

	public String generatePropertyTaxBill() {
		LOGGER.info("Entered method generatePropertyTaxBill, Generating bill for index no : " + propertyId);
		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		Map<Installment, PropertyInstTaxBean> instTaxBean = new HashMap<Installment, PropertyInstTaxBean>();
		BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId);
		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		for (PropertyInstTaxBean propertyInstTaxBean : instTaxBeanList) {
			instTaxBean.put((Installment) instalDao.findById(propertyInstTaxBean.getInstallmentId(), true),
					propertyInstTaxBean);
		}
		nmcPTBill.setInstTaxBean(instTaxBean);
		nmcPTBill.setLevyPenalty(levyPenalty);
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(Long.valueOf(propertyTaxUtil.getLoggedInUser(getSession()).getId()));
		nmcPTBill.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID()
				.getWard().getBoundaryNum().toString()));
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
		String billXml = nmcPtBillServiceImpl.getBillXML(nmcPTBill);
		collectXML = URLEncoder.encode(billXml);
		LOGGER.info("Exiting method generatePropertyTaxBill, collectXML (before decode): " + billXml);
		return "view";
	}

	public String showPenalty() {
		LOGGER.info("Entered method showPenalty, propertyId: " + propertyId);
		BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId);
		Map<String, BigDecimal> demandCollMap = propertyTaxUtil.getDemandAndCollection(basicProperty.getProperty());

		BigDecimal currDue = demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR));
		BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
		if ((currDue.compareTo(BigDecimal.ZERO) == 0 || currDue.compareTo(BigDecimal.ZERO) == -1)
				&& (arrDue.compareTo(BigDecimal.ZERO) == 0 || arrDue.compareTo(BigDecimal.ZERO) == -1)) {
			return "taxPaid";
		}
		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
		nmcPTBill.setLevyPenalty(Boolean.TRUE);
		nmcPTBill.setBasicProperty(basicProperty);
		Map<Installment, PropertyInstTaxBean> penaltyMap = nmcPTBill.getCalculatedPenalty();
		for (Map.Entry<Installment, PropertyInstTaxBean> mapEntry : penaltyMap.entrySet()) {
			getInstTaxBeanList().add(mapEntry.getValue());
		}
		LOGGER.debug("before comparator, instTaxBeanListTemp: " + getInstTaxBeanList());
		Collections.sort(getInstTaxBeanList(), new Comparator<PropertyInstTaxBean>() {
			public int compare(PropertyInstTaxBean bean1, PropertyInstTaxBean bean2) {
				return bean1.getInstallment().getFromDate().compareTo(bean2.getInstallment().getFromDate());
			}
		});
		LOGGER.debug("after comparator, instTaxBeanListTemp: " + getInstTaxBeanList());
		LOGGER.debug("Exiting method showPenalty, penaltyMap: " + penaltyMap);
		return "showPenalty";
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getCollectXML() {
		return collectXML;
	}

	public void setCollectXML(String collectXML) {
		this.collectXML = collectXML;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setNmcPtBillServiceImpl(NMCPTBillServiceImpl nmcPtBillServiceImpl) {
		this.nmcPtBillServiceImpl = nmcPtBillServiceImpl;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public Boolean getLevyPenalty() {
		return levyPenalty;
	}

	public void setLevyPenalty(Boolean levyPenalty) {
		this.levyPenalty = levyPenalty;
	}

	public PropertyTaxCollection getPropertyTaxCollection() {
		return propertyTaxCollection;
	}

	public void setPropertyTaxCollection(PropertyTaxCollection propertyTaxCollection) {
		this.propertyTaxCollection = propertyTaxCollection;
	}

	public List<PropertyInstTaxBean> getInstTaxBeanList() {
		return instTaxBeanList;
	}

	public void setInstTaxBeanList(List<PropertyInstTaxBean> instTaxBeanList) {
		this.instTaxBeanList = instTaxBeanList;
	}

}
