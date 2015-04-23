package org.egov.ptis.actions.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@ParentPackage("egov")
public class CollectPropertyTaxAction extends BaseFormAction {
	
	private static final String STRUTS_RESULT_SHOWPENALTY = "showPenalty";
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
	private Boolean isPenaltyConfirmed = false;
	private List<PropertyInstTaxBean> instTaxBeanList = new ArrayList<PropertyInstTaxBean>();		
	private Map<Integer, Installment> installmentAndId = new HashMap<Integer, Installment>();
	private Map<Installment, EgDemandDetails> installmentAndDemandDetails = new HashMap<Installment, EgDemandDetails>();
	
	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare, propertyId=" + propertyId);
		
		if (isPenaltyConfirmed) {
			List<EgDemandDetails> penaltyDemandDetails = persistenceService.findAllBy(
					"select dmdDtls from EgDemandDetails dmdDtls left join fetch dmdDtls.egDemandReason dmdRsn "
							+ "left join fetch dmdRsn.egInstallmentMaster installment "
							+ "left join dmdDtls.egDemand.egptProperty property "
							+ "left join property.basicProperty bp "
							+ "where bp.upicNo = ? "
							+ "and (property.status = 'A' or property.status = 'I') "
							+ "and dmdRsn.egDemandReasonMaster.code = ?", propertyId,
					NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES);

			for (EgDemandDetails penaltyDmdDtl : penaltyDemandDetails) {
				installmentAndDemandDetails.put(penaltyDmdDtl.getEgDemandReason().getEgInstallmentMaster(),
						penaltyDmdDtl);
			}
			
			LOGGER.debug("prepare - installmentAndDemandDetails=" + installmentAndDemandDetails);
			
			if (!instTaxBeanList.isEmpty()) {
				installmentAndId = getInstallmentAndIdAsMap();
			}
		}
		
		LOGGER.debug("Exiting from prepare, propertyId=" + propertyId);
	}
	
	@Override
	public void validate() {
		LOGGER.debug("Entered into validate, propertyId=" + propertyId + ", isPenaltyConfirmed=" + isPenaltyConfirmed);
		
		EgDemandDetails penaltyDmdDtls = null;
		Boolean isPenaltyLessThanCollection = true;
		List<String> installmentDescriptions = new ArrayList<String>();
		Installment installment = null;
		boolean thereIsPenalty = false;
		
		if (isPenaltyConfirmed) {
			for (PropertyInstTaxBean propertyInstTaxBean : instTaxBeanList) {
				installment = installmentAndId.get(propertyInstTaxBean.getInstallmentId());
				penaltyDmdDtls = installmentAndDemandDetails.get(installment);
				thereIsPenalty = propertyInstTaxBean.getInstPenaltyAmt().compareTo(BigDecimal.ZERO) > 0;
				
				if (penaltyDmdDtls != null && thereIsPenalty) {
					
					isPenaltyLessThanCollection = propertyInstTaxBean.getInstPenaltyAmt()
							.add(penaltyDmdDtls.getAmtCollected()).compareTo(penaltyDmdDtls.getAmtCollected()) < 0;
					
					if (isPenaltyLessThanCollection) {
						installmentDescriptions.add(installment.getDescription());
					}
				}
			}
			
			if (installmentDescriptions.size() > 0) {
				String inst = installmentDescriptions.toString().replace('[', ' ').replace(']', ' ');
				List<String> msgParams = new ArrayList<String>();
				msgParams.add(inst);
				addActionError(getText("confirmPenalty.penaltyMsg", msgParams));
			}
		}
		
		LOGGER.debug("Exiting  from validate, propertyId=" + propertyId);
	}
	
	@ValidationErrorPage(value = STRUTS_RESULT_SHOWPENALTY)
	public String save() {
		LOGGER.info("Entered method generatePropertyTaxBill, Generating bill for index no : " + propertyId);
		
		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		Map<Installment, PropertyInstTaxBean> instTaxBean = new HashMap<Installment, PropertyInstTaxBean>();
		
		BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId);
		
		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);

		for (PropertyInstTaxBean propertyInstTaxBean : instTaxBeanList) {
			instTaxBean.put(installmentAndId.get(propertyInstTaxBean.getInstallmentId()), propertyInstTaxBean);
		}
		
		nmcPTBill.setInstTaxBean(instTaxBean);
		nmcPTBill.setLevyPenalty(levyPenalty);
		nmcPTBill.setBasicProperty(basicProperty);
		nmcPTBill.setUserId(Long.valueOf(propertyTaxUtil.getLoggedInUser(getSession()).getId()));
		nmcPTBill.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID()
				.getWard().getBoundaryNum().toString()));
		nmcPTBill.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_AUTO));
		
		setPenaltyToExistingPenalty(instTaxBean);
		
		String billXml = nmcPtBillServiceImpl.getBillXML(nmcPTBill);
		collectXML = URLEncoder.encode(billXml);
		LOGGER.info("Exiting method generatePropertyTaxBill, collectXML (before decode): " + billXml);
		return "view";
	}

	/**
	 * @return
	 */
	private Map<Integer, Installment> getInstallmentAndIdAsMap() {
		List<Integer> installmentIds = new ArrayList<Integer>();
		
		for (PropertyInstTaxBean propertyInstTaxBean : instTaxBeanList) {
			installmentIds.add(propertyInstTaxBean.getInstallmentId());
		}
		
		@SuppressWarnings("unchecked")
		List<Installment> installments = persistenceService.findAllBy("from Installment where id in " + installmentIds
				.toString().replace('[', '(').replace(']', ')'));
		
		Map<Integer, Installment> installmentAndId = new HashMap<Integer, Installment>();
		
		for (Installment installment : installments) {
			installmentAndId.put(installment.getId(), installment);
		}
		return installmentAndId;
	}
		
	@SuppressWarnings("unchecked")
	private void setPenaltyToExistingPenalty(Map<Installment, PropertyInstTaxBean> installmentTaxBeanMap) {
		EgDemandDetails penaltyDmdDtls = null;
		EgDemandDetailsDao demandDetailsDao = PropertyDAOFactory.getDAOFactory().getEgDemandDetailsDao();
		boolean isUpdated = false;
		
		for (Map.Entry<Installment, PropertyInstTaxBean> entry : installmentTaxBeanMap.entrySet()) {
			penaltyDmdDtls = installmentAndDemandDetails.get(entry.getKey());
			
			if (penaltyDmdDtls != null) {
				penaltyDmdDtls.setAmount(penaltyDmdDtls.getAmtCollected().add(entry.getValue().getInstPenaltyAmt()));
				demandDetailsDao.update(penaltyDmdDtls);
				isUpdated = true;
			}
		}
		
		if (isUpdated) {
			HibernateUtil.getCurrentSession().flush();
		}
	}

	@ValidationErrorPage(value = STRUTS_RESULT_SHOWPENALTY)
	public String showPenalty() {
		LOGGER.info("Entered method showPenalty, propertyId: " + propertyId);
		
		String noBillMessage = "Bill is not available penalty calculation for " + propertyId;
		BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId);
		
		/*
		  
		Map<String, BigDecimal> demandCollMap = propertyTaxUtil.getDemandAndCollection(basicProperty.getProperty());

		BigDecimal currDue = demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR));
		BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
		
		if ((currDue.compareTo(BigDecimal.ZERO) == 0 || currDue.compareTo(BigDecimal.ZERO) == -1)
				&& (arrDue.compareTo(BigDecimal.ZERO) == 0 || arrDue.compareTo(BigDecimal.ZERO) == -1)) {
			return "taxPaid";
		}*/
		
		NMCPropertyTaxBillable nmcPTBill = new NMCPropertyTaxBillable();
		LOGGER.debug("generatePropertyTaxBill : BasicProperty :" + basicProperty);
		nmcPTBill.setLevyPenalty(Boolean.TRUE);
		nmcPTBill.setBasicProperty(basicProperty);
		
		Map<Installment, PropertyInstTaxBean> penaltyMap = new TreeMap<Installment, PropertyInstTaxBean>();   
				
		try {
			penaltyMap = nmcPTBill.getCalculatedPenalty();
		} catch (ValidationException ve) {
			throw new ValidationException(Arrays.asList(new ValidationError(noBillMessage , noBillMessage)));
		}
		
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
		return STRUTS_RESULT_SHOWPENALTY;
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

	public Boolean getIsPenaltyConfirmed() {
		return isPenaltyConfirmed;
	}

	public void setIsPenaltyConfirmed(Boolean isPenaltyConfirmed) {
		this.isPenaltyConfirmed = isPenaltyConfirmed;
	}
}
