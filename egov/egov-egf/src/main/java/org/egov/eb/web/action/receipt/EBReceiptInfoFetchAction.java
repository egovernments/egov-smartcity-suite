package org.egov.eb.web.action.receipt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.eb.service.master.EBDetailsService;
import org.egov.eb.service.receipt.EBReceiptInfoService;
import org.egov.eb.utils.EBConstants;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.DateUtils;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 
 * @author nayeem
 *
 */
@ParentPackage(value = "egov")
public class EBReceiptInfoFetchAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(EBReceiptInfoFetchAction.class);
	private static final String STRUTS_RESULT_ACK = "ack";
	
	private FinancialYearHibernateDAO financialYearDAO;
	private Map<Integer, String> monthMap = new TreeMap<Integer, String>();
	private EBReceiptInfoService  receiptInfoService;
	
	private Integer month;
	private Long financialYearId;
	private String rtgsNumber;
	private List<String> rtgsNumbers;
	private EBDetailsService ebDetailsService;
	boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	private String ackMessage;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		monthMap = DateUtils.getAllMonthsWithFullNames();
		List<CFinancialYear> financialYears = financialYearDAO.getAllActiveFinancialYearList();
		addDropdownData("financialYearsList", financialYears);
		addDropdownData("rtgsNumbers", Collections.<String>emptyList());
	}

	@Override
	public Object getModel() {
		return null;
	}
	
	@SkipValidation
@Action(value="/receipt/eBReceiptInfoFetch-newForm")
	public String newForm() {
		
		return NEW;
	}
	
	@SuppressWarnings("unchecked")
	@Validations (requiredFields = {
			@RequiredFieldValidator(fieldName = "month", key = FinancialConstants.REQUIRED, message =""),
			@RequiredFieldValidator(fieldName = "financialYearId", key = FinancialConstants.REQUIRED, message ="") 
	})	
@Action(value="/receipt/eBReceiptInfoFetch-create")
	public String create() {
		
		if (isDebugEnabled) LOGGER.debug("Entered into create");

		String query = "select distinct ih.transactionNumber, d " +
				"from EBDetails d, EgBillregister br, Miscbilldetail mbd, " +
				"InstrumentVoucher iv inner join iv.instrumentHeaderId ih " +
				"where d.receiptNo is null " +
				"and d.egBillregister = br " +
				"and br.egBillregistermis.voucherHeader = mbd.billVoucherHeader " +
				"and mbd.payVoucherHeader = iv.voucherHeaderId " +
				"and ih.statusId.code in ('"
				+ FinancialConstants.INSTRUMENT_CREATED_STATUS + "', '"
				+ FinancialConstants.INSTRUMENT_RECONCILED_STATUS + "') " +
				"and month(ih.transactionDate) = ? " +
				"and year(ih.transactionDate) between  ? and ?";
		
		if (StringUtils.isNotBlank(rtgsNumber)) {
			query = query + " and ih.transactionNumber = ?";
		}
		
		CFinancialYear financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",
				financialYearId);
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(financialYear.getStartingDate());
		Integer startingYear = calendar.get(Calendar.YEAR);
		
		calendar.setTime(financialYear.getEndingDate());
		Integer endingYear = calendar.get(Calendar.YEAR);
		
		List<Object> results = new ArrayList<Object>();
		
		if (StringUtils.isNotBlank(rtgsNumber)) {
			results = (List<Object>) persistenceService.findAllBy(query, month, startingYear, endingYear, rtgsNumber);
		} else {
			results = (List<Object>) persistenceService.findAllBy(query, month, startingYear, endingYear);
		}
		
		if (results == null || results.isEmpty()) {
			LOGGER.debug("No RTGS numbers to fetch the receipt information...!!");
			throw new ValidationException(Arrays.asList(new ValidationError("", "No RTGS Numbers")));
		} else {

			Map<String, Map<String, EBDetails>> ebDetailsByRtgsNumber = new HashMap<String, Map<String, EBDetails>>();

			String rtgsNo = null;
			EBDetails ebDtls = null;
			Object[] obj = null;
			Map<String, EBDetails> ebDetailsAndConsumerNo = new HashMap<String, EBDetails>();

			for (Object object : results) {
				obj = (Object[]) object;
				rtgsNo = (String) obj[0];
				ebDtls = (EBDetails) obj[1];

				if (ebDetailsByRtgsNumber.get(rtgsNo) == null) {
					ebDetailsAndConsumerNo.put(ebDtls.getEbConsumer().getName(), ebDtls);
					ebDetailsByRtgsNumber.put(rtgsNo, ebDetailsAndConsumerNo);
				} else {

					if (ebDetailsByRtgsNumber.get(rtgsNo).get(ebDtls.getEbConsumer().getName()) == null) {
						ebDetailsAndConsumerNo.put(ebDtls.getEbConsumer().getName(), ebDtls);
						ebDetailsByRtgsNumber.put(rtgsNo, ebDetailsAndConsumerNo);
					} 
				}
			}

			Map<String, List<Map<String, String>>> rtgsNumberAndResponse = new HashMap<String, List<Map<String, String>>>();

			try {
				for (String rtgsNumber : ebDetailsByRtgsNumber.keySet()) {
					rtgsNumberAndResponse.put(rtgsNumber, receiptInfoService.fetchReceiptInfo(rtgsNumber));
				}
			} catch (MalformedURLException e) {
				LOGGER.error("Error in URL", e);
				throw new ValidationException(Arrays.asList(new ValidationError("", "Error in URL")));
			} catch (IOException e) {
				LOGGER.error("Error in opening the connection", e);
				throw new ValidationException(Arrays.asList(new ValidationError("", "Error in opening the connection")));
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error("TLS Alogorithm does not exists", e);
				throw new ValidationException(Arrays.asList(new ValidationError("", "No Alogorithm")));
			} catch (KeyManagementException e) {
				LOGGER.error("Error in initializing trust manager", e);
				throw new ValidationException(Arrays.asList(new ValidationError("", "Error in initializing trust manager")));
		    }	

			String consumerNo = null;

			for (Map.Entry<String, List<Map<String, String>>> entry : rtgsNumberAndResponse.entrySet()) {
				rtgsNo = entry.getKey();

				for (Map<String, String> responseMap : entry.getValue()) {

					consumerNo = responseMap.get("ConsumerNo");

					if (StringUtils.isBlank(consumerNo)) {
						LOGGER.debug("ReceiptInfoFetch - Consumer no is null/empty for rtgs : " + rtgsNo);
					} else {

						ebDtls = ebDetailsByRtgsNumber.get(rtgsNo).get(consumerNo);

						if (ebDtls == null) {
							LOGGER.debug("EBdetails is not found for consumer no " + consumerNo + " and rtgs no "
									+ rtgsNo);
						} else {
							ebDtls.setReceiptNo(responseMap.get("ReceiptNo"));
							ebDtls.setReceiptDate(DateUtils.getDate(responseMap.get("ReceiptDate"),
									EBConstants.DATE_FORMAT_DDMMYYYY));
							//ebDetailsService.update(ebDtls);
						}
					}
				}
			}
		}
		
		if (isDebugEnabled) LOGGER.debug("Exiting from create");
		
		ackMessage = "Receipt information is successfully fetched...";
		
		return STRUTS_RESULT_ACK;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Map<Integer, String> getMonthMap() {
		return monthMap;
	}

	public void setMonthMap(Map<Integer, String> monthMap) {
		this.monthMap = monthMap;
	}

	public void setReceiptInfoService(EBReceiptInfoService receiptInfoService) {
		this.receiptInfoService = receiptInfoService;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(Long financialYearId) {
		this.financialYearId = financialYearId;
	}
	
	public String getRtgsNumber() {
		return rtgsNumber;
	}

	public void setRtgsNumber(String rtgsNumber) {
		this.rtgsNumber = rtgsNumber;
	}

	public List<String> getRtgsNumbers() {
		return rtgsNumbers;
	}

	public void setRtgsNumbers(List<String> rtgsNumbers) {
		this.rtgsNumbers = rtgsNumbers;
	}

	public void setEbDetailsService(EBDetailsService ebDetailsService) {
		this.ebDetailsService = ebDetailsService;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}
}
