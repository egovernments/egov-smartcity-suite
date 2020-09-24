/**
 * 
 */
package org.egov.tl.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.CFinancialYearService;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.tl.entity.LicenseDemandVoucher;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.repository.LicenseDemandVoucherRepository;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pabitra
 *
 */

@Transactional(readOnly = true)
@Service
public class LicenseDemandVoucherService {
	
	private static final Logger LOGGER = Logger.getLogger(LicenseDemandVoucherService.class);
	private static final String DEMAND_VOUCHER_HEADER_PARAMS = "DEMAND_VOUCHER_HEADER_PARAMS";
    private static final String VOUCHERDESCRIPTION = "TL Demand voucher";
    private static final String DEMANDVOUCHER_DEPARTMENTCODE = "DEPARTMENT";
    private static final String DEMANDVOUCHER_FUNCTIONCODE = "FUNCTION";
    private static final String DEMANDVOUCHER_FUNDCODE = "FUND";
    private static final String DEMANDVOUCHER_FUNDSOURCECODE = "FUNDSOURCE";
    private static final String DEMANDVOUCHER_VOUCHERNAME = "VOUCHERNAME";
    private static final String DEMANDVOUCHER_VOUCHERTYPE = "VOUCHERTYPE";
    private static final String DEMANDVOUCHER_VOUCHERSTATUS = "VOUCHERSTATUS";
    private static final String MODULESID_QUERY = "select id from eg_modules where code =:code";
	final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    public static final String FINANCIALYEAR_START_DATE = "STARTDATE";
    public static final String FINANCIALYEAR_END_DATE = "ENDDATE";
    private static final String APPCONFIG_TL_DEMAND_VOUCHER_GLCODES = "TL_DEMAND_VOUCHER_GLCODES";
    private static final String APPCONFIG_VALUE_VOUCHER_CURRENTGLCODE = "CURRENTRECEIVABLE";
    private static final String PIPE_SEPERATOR = "|";
	
	@PersistenceContext
    private EntityManager entityManager;
		
	@Autowired
    private AppConfigValueService appConfigValuesService;
	
	@Autowired
    private CFinancialYearService cFinancialYearService;
	
	@Autowired
    private CreateVoucher createVoucher;
	
	@Autowired
	protected LicenseRepository licenseRepository;
	
	@Autowired
	private LicenseDemandVoucherRepository licenseDemandVoucherRepository;
	
	@Transactional
	public void createDemandVoucher(TradeLicense license, EgDemand demand,BigDecimal diffAmount, String demandVhPosting) {
		
		if(demandVhPosting.equalsIgnoreCase("YES")) {
			LOGGER.info("Trade License demand voucher posting start");
			HashMap<String, Object> headerdetails = prepareHeaderDetails(license.getApplicationNumber(), 
					license.getLicenseAppType().toString());
			List<HashMap<String, Object>> accountDetails = prepareDemandVoucherAccountDetails(license,
                    headerdetails.get(VoucherConstant.FUNCTIONCODE).toString(),demand, diffAmount);
			if (!accountDetails.isEmpty() && !headerdetails.isEmpty()) {
                LOGGER.info("TL Account Details :" + accountDetails);
                LOGGER.info("TL Header Details :" + headerdetails);
                CVoucherHeader voucherHeader = createVoucher(accountDetails,
                        headerdetails);
                persistLicenseDemandVoucher(license, voucherHeader);
            }
			
		}
	}
	
	@Transactional
    public void persistLicenseDemandVoucher(TradeLicense license, CVoucherHeader voucherHeader) {

        LicenseDemandVoucher licenseDemandVoucher = new LicenseDemandVoucher();
        licenseDemandVoucher.setLicensedetail(license);
        licenseDemandVoucher.setVoucherHeader(voucherHeader);
        licenseDemandVoucher.setCreatedBy(license.getCreatedBy().getId());
        licenseDemandVoucher.setLastModifiedBy(license.getLastModifiedBy().getId());
        licenseDemandVoucher.setCreatedDate(new Date());
        licenseDemandVoucher.setLastModifiedDate(new Date());
        licenseDemandVoucherRepository.save(licenseDemandVoucher);
    }
	
	/**
     * createVoucher create the TL demand voucher
     * @param accountDetList Account details required to created voucher
     * @param headerdetails Voucher header details
     * @return demand voucher created in finance.
     */

    public CVoucherHeader createVoucher(List<HashMap<String, Object>> accountDetList, HashMap<String, Object> headerdetails) {
        validateAccountDetails(accountDetList);
        try {
            CVoucherHeader voucher = createVoucher.createVoucher(headerdetails, accountDetList,
                    new ArrayList<HashMap<String, Object>>());
            if (voucher == null) {
                LOGGER.error("Voucher Creation failed. CVoucherHeader is null.");
                throw new ApplicationRuntimeException("Voucher Creation failed.");
            }
            LOGGER.info(" TL create demand voucher is created. Voucher number : " + voucher.getVoucherNumber());
            return voucher;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("createVoucher: headerdetails==>" + headerdetails + " \n accountDetList ==>" + accountDetList);
            throw new ApplicationRuntimeException("FAILED TO CREATE TL DEMAND VOUCHER", e);
        }
    }
    
    private void validateAccountDetails(List<HashMap<String, Object>> accountDetList) {
        BigDecimal totaldebitAmount = BigDecimal.valueOf(0);
        BigDecimal totalcreditAmount = BigDecimal.valueOf(0);
        for (final HashMap<String, Object> accDetailMap : accountDetList) {
            final BigDecimal debitAmount = new BigDecimal(accDetailMap.get(
                    VoucherConstant.DEBITAMOUNT).toString());
            final BigDecimal creditAmount = new BigDecimal(accDetailMap.get(
                    VoucherConstant.CREDITAMOUNT).toString());
            totaldebitAmount = totaldebitAmount.add(debitAmount);
            totalcreditAmount = totalcreditAmount.add(creditAmount);
            if (debitAmount.compareTo(BigDecimal.ZERO) != 0
                    && creditAmount.compareTo(BigDecimal.ZERO) != 0)
                throw new ApplicationRuntimeException(
                        "Both debit amount and credit amount cannot be greater than zero");
            if (debitAmount.compareTo(BigDecimal.ZERO) == 0
                    && creditAmount.compareTo(BigDecimal.ZERO) == 0)
                throw new ApplicationRuntimeException(
                        "debit and credit both amount is Zero");

        }
        if (totaldebitAmount.compareTo(totalcreditAmount) != 0)
            throw new ApplicationRuntimeException(
                    "total debit and total credit amount is not matching");
    }
	
	public HashMap<String, Object> prepareHeaderDetails(String applicationNumber, String applicationType) {
        HashMap<String, Object> headerdetails = new HashMap<>();
        try {
            headerdetails.put(VoucherConstant.VOUCHERDATE, format.parse(format.format(new Date())));
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing voucher date", e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        Map<String, String> voucherHeaderDataMap = getAppconfigValueMap(DEMAND_VOUCHER_HEADER_PARAMS);
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERNAME));
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERTYPE));
        headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNDSOURCECODE));
        headerdetails.put(VoucherConstant.STATUS, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERSTATUS));
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_DEPARTMENTCODE));
        headerdetails.put(VoucherConstant.FUNDCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNDCODE));
        headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNCTIONCODE));
        headerdetails.put(VoucherConstant.MODULEID, getModulesId());
        headerdetails.put(VoucherConstant.DESCRIPTION, String.format(VOUCHERDESCRIPTION, applicationType, applicationNumber));
        return headerdetails;
    }
	
	private BigInteger getModulesId() {
        final Query query = entityManager.unwrap(Session.class).createSQLQuery(MODULESID_QUERY);
        query.setParameter("code", Constants.TL_SERVICE_CODE);
        List<BigInteger> result = query.list();
        return result.isEmpty() ? BigInteger.ZERO : result.get(0);
    }
	
	public Map<String, String> getAppconfigValueMap(String appconfigKeyName) {
        final List<AppConfigValues> appConfigValuesList = getAppConfigValueByModuleNameAndKeyName(
                        Constants.MODULE_NAME, appconfigKeyName);
        Map<String, String> voucherHeaderDataMap = new HashMap<>();
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            final String value = appConfigVal.getValue();
            voucherHeaderDataMap.put(appConfigVal.getValue().substring(0, value.indexOf(PIPE_SEPERATOR)),
                    appConfigVal.getValue().substring(value.indexOf(PIPE_SEPERATOR) + 1));
        }
        return voucherHeaderDataMap;
    }
	
	public List<AppConfigValues> getAppConfigValueByModuleNameAndKeyName(String moduleName, String keyName) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
    }
	
	public List<HashMap<String, Object>> prepareDemandVoucherAccountDetails(TradeLicense license,
            final String functionCode, EgDemand demand, BigDecimal diffAmount) {
         
        List<HashMap<String, Object>> accountDetList = new ArrayList<>();
        if (demand != null) {
            final List<EgDemandDetails> demandDetailsList = new ArrayList<>();
            demandDetailsList.addAll(demand.getEgDemandDetails());
            
            BigDecimal currentDemandAmount = demandDetailsList.stream().map(EgDemandDetails::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            Map<String, String> glCodeMap = getAppconfigValueMap(APPCONFIG_TL_DEMAND_VOUCHER_GLCODES);
			
            if(diffAmount == null) {
            	if (currentDemandAmount.compareTo(BigDecimal.ZERO) > 0) {
                    accountDetList.add(createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTGLCODE),
                            currentDemandAmount,
                            BigDecimal.ZERO, functionCode));
                    accountDetList.add(createAccDetailmap(
                    		demandDetailsList.get(0).getEgDemandReason().getGlcodeId().getGlcode(),
                            BigDecimal.ZERO, currentDemandAmount, functionCode));
                }
            }else {
            	accountDetList.add(createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTGLCODE),
            			diffAmount, BigDecimal.ZERO, functionCode));
                accountDetList.add(createAccDetailmap(
                		demandDetailsList.get(0).getEgDemandReason().getGlcodeId().getGlcode(),
                        BigDecimal.ZERO, diffAmount, functionCode));
            }
        }
        return accountDetList;
    }
	
	public HashMap<String, Date> getCurrentFinancialYearStartEndDate() {
        HashMap<String, Date> dateMap = new HashMap<>();
        final CFinancialYear financialYear = cFinancialYearService.getCurrentFinancialYear();
        dateMap.put(FINANCIALYEAR_START_DATE, financialYear.getStartingDate());
        dateMap.put(FINANCIALYEAR_END_DATE, financialYear.getEndingDate());
        return dateMap;
    }
	
	public HashMap<String, Object> createAccDetailmap(String glcode, BigDecimal debitAmount, BigDecimal creditAmount,
            String functionCode) {
        HashMap<String, Object> accountdetailmap = new HashMap<>();
        accountdetailmap.put(VoucherConstant.GLCODE, glcode);
        accountdetailmap.put(VoucherConstant.DEBITAMOUNT, debitAmount);
        accountdetailmap.put(VoucherConstant.CREDITAMOUNT, creditAmount);
        accountdetailmap.put(VoucherConstant.FUNCTIONCODE, functionCode);
        return accountdetailmap;
    }

}
