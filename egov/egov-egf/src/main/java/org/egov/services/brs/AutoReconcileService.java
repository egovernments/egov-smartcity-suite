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

package org.egov.services.brs;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.convention.annotation.Action;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.expensebill.repository.DocumentUploadRepository;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.DocumentUpload;
import org.egov.model.brs.AutoReconcileBean;
import org.egov.model.brs.BankStatementUploadFile;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.HibernateException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class AutoReconcileService {

    private static final String DID_NOT_FIND_MATCH_IN_BANKBOOK = "did not find match in Bank Book  (InstrumentHeader)";
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReconcileService.class);
    private static final int ACCOUNTNUMBER_ROW_INDEX = 2;
    private static final int STARTOF_DETAIL_ROW_INDEX = 8;
    private static final int TXNDT_INDEX = 1;
    private static final int NARRATION_INDEX = 2;
    private static final int CHEQUENO_INDEX = 4;
    private static final int TYPE_INDEX = 3;
    private static final int DEBIT_INDEX = 5;
    private static final int CREDIT_INDEX = 6;
    private static final int BALANCE_INDEX = 7;
    private static final int CSLNO_INDEX = 8;
    private static final String BRS_TRANSACTION_TYPE_BANK = "TRF";
    private final String BRS_TRANSACTION_TYPE_CHEQUE = "CLG";
    private final List<Bankaccount> accountList = Collections.EMPTY_LIST;
    private final String successMessage = "BankStatement upload completed Successfully # rows processed";
    private final String TABLENAME = "egf_brs_bankstatements";
    private final String BRS_ACTION_TO_BE_PROCESSED = "to be processed";
    private final String BRS_ACTION_TO_BE_PROCESSED_MANUALLY = "to be processed manually";
    private final String BRS_ACTION_PROCESSED = "processed";
    private final String jasperpath = "/reports/templates/AutoReconcileReport.jasper";
    private final String BRS_MESSAGE_MORE_THAN_ONE_MATCH = "found more than one match in instruments";
    private final String BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT = "duplicate instrument number within the bankstament";
    private final String dateInDotFormat = "dd.mm.yyyy";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy");
    private final String insertsql = "insert into egf_brs_bankstatements (ID,ACCOUNTNUMBER,ACCOUNTID,TXDATE,TYPE,INSTRUMENTNO,DEBIT,CREDIT,BALANCE"
            +
            ",NARRATION,CSLNO,CREATEDDATE) values (nextval('seq_egf_brs_bankstatements'),:accNo,:accountId,to_date(:txDate,"
            + "'"
            + dateInDotFormat + "'),:type,:instrumentNo,:debit" +
            ",:credit,:balance,:narration,:cslNo,CURRENT_DATE)";
    private String file_already_uploaded = "This file (#name) already uploaded ";
    private String bank_account_not_match_msg = "Selected Bank account and spreadsheet ( #name ) account does not match";
    private String bankStatementFormat = "Upload the Bank Statement as shown in the Download Template format.";
    private List<Bankbranch> branchList = Collections.EMPTY_LIST;
    private Integer accountId;
    private Date reconciliationDate;
    private Date fromDate;
    private Date toDate;
    private String accNo;
    private File bankStatmentInXls;
    private String bankStatmentInXlsFileName;
    private String failureMessage = "Invalid data in  the  following row(s), please correct and upload again\n";
    private boolean isFailed;
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private String message = "";
    private NativeQuery insertQuery;
    private int count;
    private int rowIndex;
    private int rowCount;
    private List<AutoReconcileBean> statementsNotInBankBookList;
    private List<AutoReconcileBean> statementsFoundButNotProcessed;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    private Date finYearStartDate;
    private List<AutoReconcileBean> entriesNotInBankStament;
    private Bankaccount bankAccount;
    private BigDecimal notInBooktotalDebit;
    private BigDecimal notInBooktotalCredit;
    private BigDecimal notprocessedCredit;
    private
    @Autowired
    EGovernCommon eGovernCommon;
    private BigDecimal notprocessedDebit;
    private BigDecimal notprocessedNet;
    private BigDecimal notInBookNet;
    private String notInBookNetBal;
    private BigDecimal notInStatementTotalDebit;
    private BigDecimal notInStatementTotalCredit;
    private BigDecimal notInStatementNet;
    private BigDecimal bankBookBalance;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;
    private BigDecimal brsBalance;
    private BigDecimal totalNotReconciledAmount;
    private Integer statusId;

    public BigDecimal getBankBookBalance() {
        return bankBookBalance;
    }

    public void setBankBookBalance(final BigDecimal bankBookBalance) {
        this.bankBookBalance = bankBookBalance;
    }

    public BigDecimal getBrsBalance() {
        return brsBalance;
    }

    public void setBrsBalance(final BigDecimal brsBalance) {
        this.brsBalance = brsBalance;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Transactional
    public String upload() {
        try {
            insertQuery = persistenceService.getSession().createNativeQuery(insertsql);
            final Bankaccount ba = (Bankaccount) persistenceService.find("from Bankaccount ba where id=?1",
                    Long.valueOf(accountId));
            accNo = ba.getAccountnumber();
            final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(bankStatmentInXls));
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            sheet.getFirstRowNum();
            // Validating selected bankaccount and BankStatements bankaccount
            final HSSFRow row = sheet.getRow(ACCOUNTNUMBER_ROW_INDEX);
            if (row == null) {
                bank_account_not_match_msg = bank_account_not_match_msg.replace("#name", bankStatmentInXlsFileName);
                throw new ValidationException(Arrays.asList(new ValidationError(bank_account_not_match_msg,
                        bank_account_not_match_msg)));
            }
            String strValue2 = getStrValue(row.getCell(0));
            strValue2 = strValue2.substring(strValue2.indexOf(':') + 1, strValue2.indexOf('-')).trim();
            if (!strValue2.equals(accNo.trim())) {
                bank_account_not_match_msg = bank_account_not_match_msg.replace("#name", bankStatmentInXlsFileName);
                throw new ValidationException(Arrays.asList(new ValidationError(bank_account_not_match_msg,
                        bank_account_not_match_msg)));
            }

            AutoReconcileBean ab = null;
            HSSFRow detailRow = null;
            String dateStr = null;
            rowIndex = STARTOF_DETAIL_ROW_INDEX;
            count = 0;
            do {
                try {

                    ab = new AutoReconcileBean();
                    if (rowIndex == STARTOF_DETAIL_ROW_INDEX) {
                        detailRow = sheet.getRow(rowIndex);
                        if (rowIndex >= 9290)
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug(String.valueOf(detailRow.getRowNum()));
                        dateStr = getStrValue(detailRow.getCell(TXNDT_INDEX));
                        if (alreadyUploaded(dateStr)) {
                            file_already_uploaded = file_already_uploaded.replace("#name", bankStatmentInXlsFileName);
                            throw new ValidationException(Arrays.asList(new ValidationError(file_already_uploaded,
                                    file_already_uploaded)));
                        }
                        ab.setTxDateStr(dateStr);
                    }
                    ab.setTxDateStr(dateStr);
                    ab.setInstrumentNo(getStrValue(detailRow.getCell(CHEQUENO_INDEX)));
                    // if(strValue!=null)
                    // ab.setInstrumentNo(strValue.replaceFirst(".0", ""));
                    ab.setDebit(getNumericValue(detailRow.getCell(DEBIT_INDEX)));
                    ab.setCredit(getNumericValue(detailRow.getCell(CREDIT_INDEX)));
                    ab.setBalance(getNumericValue(detailRow.getCell(BALANCE_INDEX)));
                    String strValue = getStrValue(detailRow.getCell(NARRATION_INDEX));
                    if (strValue != null) {
                        if (strValue.length() > 125)
                            strValue = strValue.substring(0, 125);
                        // strValue=strValue.replaceFirst(".0", "");
                        ab.setNarration(strValue);
                    }
                    ab.setType(getStrValue(detailRow.getCell(TYPE_INDEX)));
                    ab.setCSLno(getStrValue(detailRow.getCell(CSLNO_INDEX)));
                    // if(ab.getType()==null)
                    // ab.setType("CLG");
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(detailRow.getRowNum() + "   " + ab.toString());
                    insert(ab);
                    if (count % 20 == 0)
                        persistenceService.getSession().flush();

                } catch (ValidationException ve) {
                    throw ve;
                } catch (final NumberFormatException e) {
                    if (!isFailed)
                        failureMessage += detailRow.getRowNum() + 1;
                    else
                        failureMessage += " , " + detailRow.getRowNum() + 1;
                    isFailed = true;

                    throw new ValidationException(Arrays.asList(new ValidationError(failureMessage, failureMessage)));
                }
                rowIndex++;
                count++;
                detailRow = sheet.getRow(rowIndex);
                if (detailRow != null)
                    dateStr = getStrValue(detailRow.getCell(TXNDT_INDEX));
                else
                    dateStr = null;
                // ab.setTxDateStr(detailRow.getRowNum()+"-->" + dateStr);

            } while (dateStr != null && !dateStr.isEmpty());

            if (isFailed)
                throw new ValidationException(Arrays.asList(new ValidationError(failureMessage, failureMessage)));
            else {
                final FileStoreMapper fileStore = fileStoreService.store(getBankStatmentInXls(),
                        bankStatmentInXlsFileName,
                        "application/vnd.ms-excel", FinancialConstants.MODULE_NAME_APPCONFIG, false);

                persistenceService.persist(fileStore);
                String fileStoreId = fileStore.getFileStoreId();
                DocumentUpload upload = new DocumentUpload();
                upload.setFileStore(fileStore);
                upload.setObjectId(accountId.longValue());
                upload.setObjectType(FinancialConstants.BANK_STATEMET_OBJECT);
                upload.setUploadedDate(new Date());
                documentUploadRepository.save(upload);
                message = successMessage.replace("#", "" + count);
            }

        } catch (final FileNotFoundException e) {
            throw new ValidationException(
                    Arrays.asList(new ValidationError("File cannot be uploaded", "File cannot be uploaded")));

        } catch (final IOException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("Unable to read uploaded file",
                    "Unable to read uploaded file")));
        } catch (final ValidationException ve) {
            throw ve;
        } catch (final NullPointerException npe) {
            throw new ValidationException(Arrays.asList(new ValidationError(bankStatementFormat,
                    bankStatementFormat)));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(bankStatementFormat,
                    bankStatementFormat)));
        }

        return "upload";
    }

    private void insert(final AutoReconcileBean ab) {

        insertQuery.setString("accNo", accNo)
                .setInteger("accountId", accountId)
                .setString("txDate", ab.getTxDateStr())
                .setString("type", ab.getType())
                .setString("instrumentNo", ab.getInstrumentNo())
                .setBigDecimal("debit", ab.getDebit())
                .setBigDecimal("credit", ab.getCredit())
                .setBigDecimal("balance", ab.getBalance())
                .setString("narration", ab.getNarration())
                .setString("cslNo", ab.getCSLno());
        insertQuery.executeUpdate();

    }

    private boolean alreadyUploaded(final String dateStr) {
        final List list = persistenceService.getSession()
                .createNativeQuery("select id from egf_brs_bankstatements where accountid=:accountId and txdate=to_date(:date, :dateInDotFormat)")
                .setParameter("accountId", accountId, IntegerType.INSTANCE)
                .setParameter("date", dateStr, StringType.INSTANCE)
                .setParameter("dateInDotFormat", dateInDotFormat, StringType.INSTANCE)
                .list();
        if (list.size() >= 1)
            return true;
        else
            return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(final String failureMessage) {
        this.failureMessage = failureMessage;
    }

    private String getStrValue(final HSSFCell cell) {
        if (cell == null)
            return null;
        double numericCellValue = 0d;
        String strValue = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                numericCellValue = cell.getNumericCellValue();
                final DecimalFormat decimalFormat = new DecimalFormat("#");
                strValue = decimalFormat.format(numericCellValue);
                break;
            case HSSFCell.CELL_TYPE_STRING:
                strValue = cell.getStringCellValue();
                break;
        }
        return strValue;

    }

    private BigDecimal getNumericValue(final HSSFCell cell) {
        if (cell == null)
            return null;
        double numericCellValue = 0d;
        BigDecimal bigDecimalValue = BigDecimal.ZERO;
        String strValue = "";

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                numericCellValue = cell.getNumericCellValue();
                bigDecimalValue = BigDecimal.valueOf(numericCellValue);
                break;
            case HSSFCell.CELL_TYPE_STRING:
                strValue = cell.getStringCellValue();
                strValue = strValue.replaceAll("[^\\p{L}\\p{Nd}]", "");
                if (strValue != null && strValue.contains("E+")) {
                    final String[] split = strValue.split("E+");
                    String mantissa = split[0].replaceAll(".", "");
                    final int exp = Integer.parseInt(split[1]);
                    while (mantissa.length() <= exp + 1)
                        mantissa += "0";
                    numericCellValue = Double.parseDouble(mantissa);
                    bigDecimalValue = BigDecimal.valueOf(numericCellValue);
                } else if (strValue != null && strValue.contains(","))
                    strValue = strValue.replaceAll(",", "");
                // Ignore the error and continue Since in numric field we find empty or non numeric value
                try {
                    numericCellValue = Double.parseDouble(strValue);
                    bigDecimalValue = BigDecimal.valueOf(numericCellValue);
                } catch (final Exception e) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Found : Non numeric value in Numeric Field :" + strValue + ":");
                }
                break;
        }
        return bigDecimalValue;

    }

    /**
     * Step1: mark which are all we are going to process step2 :find duplicate and mark to be processed manually step3: process
     * non duplicates
     * @return
     */
    /**
     * @return
     */
    @Transactional
    public String schedule() {
        // Step1: mark which are all we are going to process
        count = 0;
        // persistenceService.getSession().getTransaction().setTimeout(900);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Started at " + new Date());
        markForProcessing(BRS_TRANSACTION_TYPE_CHEQUE);
        persistenceService.getSession().flush();
        // step2 :find duplicate and mark to be processed manually
        findandUpdateDuplicates();

        final List<AutoReconcileBean> detailList = getStatmentsForProcessing(BRS_TRANSACTION_TYPE_CHEQUE);


        StringBuilder statusQury = new StringBuilder();
        statusQury.append("select id from EgwStatus where upper(moduletype)=upper(:instrument) and")
                .append(" upper(description)=upper(:description)");
        final Query query = persistenceService.getSession().createQuery(statusQury.toString())
                .setParameter("instrument", "instrument", StringType.INSTANCE)
                .setParameter("description", FinancialConstants.INSTRUMENT_RECONCILED_STATUS, StringType.INSTANCE);

        statusId = (Integer) query.uniqueResult();
        final Long instrumentTypeId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_CHEQUE);
        final Long instrumentTypeDDId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_DD);
        // where instrumentheaderid= (select id.....) is used to fetch only one record may be double submit or two instrument
        // entries
        // let the user decide

        StringBuilder recociliationQuery = new StringBuilder();
        recociliationQuery.append("update EGF_InstrumentHeader set id_status=:statusId, lastmodifiedby=:userId,lastmodifieddate=CURRENT_DATE")
                .append(" where id= (select id from egf_instrumentheader where instrumentNumber=:instrumentNo and ")
                .append(" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in (:instrumentType)")
                .append(" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=upper(:instrumentStatus)))");

        StringBuilder recociliationAmountQuery = new StringBuilder();
        recociliationAmountQuery.append("update egf_instrumentOtherdetails set reconciledamount=:amount,instrumentstatusdate=:txDate ")
                .append(",lastmodifiedby=:userId,lastmodifieddate=CURRENT_DATE,reconciledOn=:reconciliationDate ")
                .append(" where instrumentheaderid= (select id from egf_instrumentheader where instrumentNumber=:instrumentNo and ")
                .append(" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in (:instrumentType)")
                .append(" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=upper(:instrumentStatus)))");

        final NativeQuery updateQuery = persistenceService.getSession().createNativeQuery(recociliationQuery.toString())
                .setParameterList("instrumentType", Arrays.asList(instrumentTypeId, instrumentTypeDDId), LongType.INSTANCE);
        final NativeQuery updateQuery2 = persistenceService.getSession().createNativeQuery(recociliationAmountQuery.toString())
                .setParameterList("instrumentType", Arrays.asList(instrumentTypeId, instrumentTypeDDId), LongType.INSTANCE);

        final String backUpdateBankStmtquery = String.format("update %s set action=:action", TABLENAME).concat(",reconciliationDate=:reconciliationDate where id=:id");
        final String backUpdateFailureBRSquery = String.format("update %s set action=:action", TABLENAME).concat(",errormessage=:e where id=:id");

        final NativeQuery backupdateQuery = persistenceService.getSession().createNativeQuery(backUpdateBankStmtquery)
                .setParameter("action", BRS_ACTION_PROCESSED, StringType.INSTANCE);
        final NativeQuery backupdateFailureQuery = persistenceService.getSession().createNativeQuery(backUpdateFailureBRSquery)
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE);
        rowCount = 0;
        for (final AutoReconcileBean bean : detailList) {
            int updated = -1;
            try {
                updateQuery.setParameter("statusId", statusId, IntegerType.INSTANCE);
                updateQuery.setParameter("accountId", accountId, IntegerType.INSTANCE);

                updateQuery.setParameter("instrumentNo", bean.getInstrumentNo(), StringType.INSTANCE);
                updateQuery.setParameter("userId", ApplicationThreadLocals.getUserId().intValue(), IntegerType.INSTANCE);

                updateQuery2.setParameter("txDate", bean.getTxDate(), DateType.INSTANCE);
                updateQuery2.setParameter("reconciliationDate", reconciliationDate, DateType.INSTANCE);
                updateQuery2.setParameter("accountId", accountId, IntegerType.INSTANCE);

                updateQuery2.setParameter("instrumentNo", bean.getInstrumentNo(), StringType.INSTANCE);
                updateQuery2.setParameter("userId", ApplicationThreadLocals.getUserId().intValue(), IntegerType.INSTANCE);
                if (bean.getDebit() != null && bean.getDebit().compareTo(BigDecimal.ZERO) != 0) {
                    updateQuery.setParameter("amount", bean.getDebit(), BigDecimalType.INSTANCE);
                    updateQuery.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                    updateQuery.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS, StringType.INSTANCE);
                    updated = updateQuery.executeUpdate();
                    if (updated != 0) {
                        updateQuery2.setParameter("amount", bean.getDebit(), BigDecimalType.INSTANCE);
                        updateQuery2.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                        updateQuery2.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS, StringType.INSTANCE);
                        updated = updateQuery2.executeUpdate();
                    }

                } else {
                    updateQuery.setParameter("amount", bean.getCredit(), BigDecimalType.INSTANCE);
                    updateQuery.setParameter("ispaycheque", '0', CharacterType.INSTANCE);
                    updateQuery.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_DEPOSITED_STATUS, StringType.INSTANCE);
                    updated = updateQuery.executeUpdate();
                    if (updated != 0) {
                        updateQuery2.setParameter("amount", bean.getCredit(), BigDecimalType.INSTANCE);
                        updateQuery2.setParameter("ispaycheque", '0', CharacterType.INSTANCE);
                        updateQuery2.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS, StringType.INSTANCE);
                        updated = updateQuery2.executeUpdate();
                    }
                }
                // if updated is 0 means nothing got updated means could not find matching row in instrumentheader
                if (updated == 0) {
                    backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                    backupdateFailureQuery.setParameter("e", DID_NOT_FIND_MATCH_IN_BANKBOOK, StringType.INSTANCE);
                    backupdateFailureQuery.executeUpdate();

                } else {
                    backupdateQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                    backupdateQuery.setParameter("reconciliationDate", reconciliationDate, DateType.INSTANCE);
                    backupdateQuery.executeUpdate();
                    count++;
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug(count);
                }
                rowCount++;
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("out of " + rowCount + "==>succesfull " + count);

                if (rowCount % 20 == 0)
                    persistenceService.getSession().flush();

                // These exception might be because the other entires in instrument which is not in egf_brs_bankstatements
                // so any issues leave it for manual update
            } catch (final HibernateException e) {
                if (e.getCause().getMessage().contains("single-row subquery returns more"))
                    backupdateFailureQuery.setParameter("e", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE);
                else
                    backupdateFailureQuery.setParameter("e", e.getMessage(), StringType.INSTANCE);
                backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                backupdateFailureQuery.executeUpdate();

            } catch (final Exception e) {
                backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                backupdateFailureQuery.setParameter("e", e.getMessage(), StringType.INSTANCE);
                backupdateFailureQuery.executeUpdate();
            }
        }
        processCSL();
        return "result";
    }

    private Long getInstrumentType(final String typeName) {

        return (Long) persistenceService.find("select id from InstrumentType where upper(type)=upper(?1)", typeName);
    }

    private void markForProcessing(final String type) {

        final StringBuffer sql = new StringBuffer(256);
        sql.append(String.format("update %s set action=:action", TABLENAME))
                .append(" where type=:type and accountid=:accountId and txdate>=:fromDate and txDate<=:toDate and  (action is null or action!='processed')");
        if (BRS_TRANSACTION_TYPE_BANK.equalsIgnoreCase(type))
            sql.append(" and CSLno is not null ");
        final NativeQuery markQuery = persistenceService.getSession().createNativeQuery(sql.toString())
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE)
                .setParameter("type", type, StringType.INSTANCE)
                .setParameter("fromDate", fromDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE);
        markQuery.executeUpdate();
    }

    private void processCSL() {
        markForProcessing(BRS_TRANSACTION_TYPE_BANK);
        final List<AutoReconcileBean> CSLList = getStatmentsForProcessing(BRS_TRANSACTION_TYPE_BANK);
        final Long instrumentTypeId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK);

        StringBuilder recociliationQuery = new StringBuilder();
        recociliationQuery.append("update EGF_InstrumentHeader set id_status=:statusId,  lastmodifiedby=:userId,lastmodifieddate=CURRENT_DATE")
                .append(" where id = (select ih.id from egf_instrumentheader ih,egf_instrumentvoucher iv,voucherheader vh where ")
                .append(" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in (:instrumentType)")
                .append(" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=")
                .append(" upper(:instrumentStatus)) and iv.instrumentheaderid=ih.id and iv.voucherheaderid=ih.id and vh.vouchernumber=:cslNo )  ");

        StringBuilder recociliationAmountQuery = new StringBuilder();
        recociliationAmountQuery.append("update egf_instrumentOtherdetails set reconciledamount=:amount,instrumentstatusdate=:txDate ")
                .append(" ,lastmodifiedby=:userId,lastmodifieddate=CURRENT_DATE,reconciledOn=:reconciliationDate ")
                .append(" where instrumentheaderid = (select ih.id from egf_instrumentheader ih,egf_instrumentvoucher iv,voucherheader vh where ")
                .append(" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in (:instrumentType)")
                .append(" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=")
                .append(" upper(:instrumentStatus)) and iv.instrumentheaderid=ih.id and iv.voucherheaderid=ih.id and vh.vouchernumber=:cslNo ) ");

        final NativeQuery updateQuery = persistenceService.getSession().createNativeQuery(recociliationQuery.toString())
                .setParameterList("instrumentType", Arrays.asList(instrumentTypeId), LongType.INSTANCE);
        final NativeQuery updateQuery2 = persistenceService.getSession().createNativeQuery(recociliationAmountQuery.toString())
                .setParameterList("instrumentType", Arrays.asList(instrumentTypeId), LongType.INSTANCE);

        StringBuilder backUpdateBankStmtquery = new StringBuilder();
        backUpdateBankStmtquery.append(String.format("update %s set action=:action", TABLENAME))
                .append(" ,reconciliationDate=:reconciliationDate where id=:id");

        StringBuilder backUpdateFailureBRSquery = new StringBuilder();
        backUpdateFailureBRSquery.append(String.format("update %s set action=:action", TABLENAME))
                .append(",errormessage=:e where id=:id");

        final NativeQuery backupdateQuery = persistenceService.getSession().createNativeQuery(backUpdateBankStmtquery.toString())
                .setParameter("action", BRS_ACTION_PROCESSED, StringType.INSTANCE);
        final NativeQuery backupdateFailureQuery = persistenceService.getSession().createNativeQuery(backUpdateFailureBRSquery.toString())
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE);
        for (final AutoReconcileBean bean : CSLList) {
            int updated = -1;
            try {
                updateQuery.setParameter("statusId", statusId, IntegerType.INSTANCE);
                updateQuery.setParameter("accountId", accountId, IntegerType.INSTANCE);

                updateQuery.setParameter("cslNo", bean.getCSLno(), StringType.INSTANCE);
                updateQuery.setParameter("userId", ApplicationThreadLocals.getUserId().intValue(), IntegerType.INSTANCE);

                updateQuery2.setParameter("txDate", bean.getTxDate(), DateType.INSTANCE);
                updateQuery2.setParameter("reconciliationDate", reconciliationDate, DateType.INSTANCE);
                updateQuery2.setParameter("accountId", accountId, IntegerType.INSTANCE);

                updateQuery2.setParameter("cslNo", bean.getCSLno(), StringType.INSTANCE);
                updateQuery2.setParameter("userId", ApplicationThreadLocals.getUserId().intValue(), IntegerType.INSTANCE);
                if (bean.getDebit() != null && bean.getDebit().compareTo(BigDecimal.ZERO) != 0) {
                    updateQuery.setParameter("amount", bean.getDebit(), BigDecimalType.INSTANCE);
                    updateQuery.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                    updateQuery.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS, StringType.INSTANCE);
                    updated = updateQuery.executeUpdate();
                    if (updated != 0) {
                        updateQuery2.setParameter("amount", bean.getDebit(), BigDecimalType.INSTANCE);
                        updateQuery2.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                        updateQuery2.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS, StringType.INSTANCE);
                        updated = updateQuery2.executeUpdate();
                    }

                } else {
                    updateQuery.setParameter("amount", bean.getCredit(), BigDecimalType.INSTANCE);
                    updateQuery.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                    updateQuery.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS, StringType.INSTANCE);
                    updated = updateQuery.executeUpdate();
                    if (updated != 0) {
                        updateQuery2.setParameter("amount", bean.getCredit(), BigDecimalType.INSTANCE);
                        updateQuery2.setParameter("ispaycheque", '1', CharacterType.INSTANCE);
                        updateQuery2.setParameter("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS, StringType.INSTANCE);
                        updated = updateQuery2.executeUpdate();
                    }
                    if (updated == 0) {

                    }
                }
                // if updated is 0 means nothing got updated means could not find matching row in instrumentheader

                if (updated == 0) {
                    backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                    backupdateFailureQuery.setParameter("e", DID_NOT_FIND_MATCH_IN_BANKBOOK, StringType.INSTANCE);
                    backupdateFailureQuery.executeUpdate();

                } else if (updated == -1) {
                    backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                    backupdateFailureQuery.setParameter("e", DID_NOT_FIND_MATCH_IN_BANKBOOK, StringType.INSTANCE);
                    backupdateFailureQuery.executeUpdate();
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug(count);
                } else {
                    backupdateQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                    backupdateQuery.setParameter("reconciliationDate", reconciliationDate, DateType.INSTANCE);
                    backupdateQuery.executeUpdate();
                    count++;
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug(count);
                }
                rowCount++;
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("out of " + rowCount + "==>succesfull " + count);

                if (rowCount % 20 == 0)
                    persistenceService.getSession().flush();

                // These exception might be because the other entires in instrument which is not in egf_brs_bankstatements
                // so any issues leave it for manual update
            } catch (final HibernateException e) {
                if (e.getCause().getMessage().contains("single-row subquery returns more"))
                    backupdateFailureQuery.setParameter("e", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE);
                else
                    backupdateFailureQuery.setParameter("e", e.getMessage(), StringType.INSTANCE);
                backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                backupdateFailureQuery.executeUpdate();

            } catch (final Exception e) {
                backupdateFailureQuery.setParameter("id", bean.getId(), LongType.INSTANCE);
                backupdateFailureQuery.setParameter("e", e.getMessage(), StringType.INSTANCE);
                backupdateFailureQuery.executeUpdate();
            }
        }
    }

    private List<AutoReconcileBean> getStatmentsForProcessing(final String type) {
        StringBuilder queryString = new StringBuilder();
        queryString = queryString.append(String.format("select id,txDate,instrumentNo,debit,credit,CSLno from %s", TABLENAME))
                .append(" where accountId=:accountId and type=:type and action=:action");
        final NativeQuery detailQuery = persistenceService.getSession().createNativeQuery(queryString.toString())
                .setParameter("type", type, StringType.INSTANCE)
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE);
        detailQuery.addScalar("id", LongType.INSTANCE).addScalar("txDate").addScalar("instrumentNo").addScalar("debit")
                .addScalar("credit").addScalar("CSLno")
                .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
        final List<AutoReconcileBean> detailList = detailQuery.list();
        return detailList;
    }

    @Action(value = "/brs/autoReconciliation-generateReport")
    @SuppressWarnings({"unchecked", "deprecation"})
    @Transactional(readOnly = true)
    public String generateReport() {
        // bankStatments not in BankBook

        try {
            bankBookBalance = eGovernCommon.getAccountBalance(dateFormatter.format(toDate), accountId.toString()).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
        } catch (final HibernateException e) {
            throw new ApplicationRuntimeException(e.getMessage());
        } catch (final TaskFailedException e) {
            throw new ApplicationRuntimeException(e.getMessage());
        }

        bankAccount = (Bankaccount) persistenceService.find("from Bankaccount ba where id=?1", Long.valueOf(accountId));
        StringBuilder statmentsNotInBankBookStr = new StringBuilder();
        statmentsNotInBankBookStr.append("select id,txDate,instrumentNo,debit,credit,narration,type,action as \"errorCode\",errorMessage")
                .append(String.format(" from %s", TABLENAME))
                .append(" where accountId=:accountId and txdate>=:fromDate ")
                .append(" and txdate<=:toDate and reconciliationdate is null and (errorMesSage is null or errorMessage !=:multipleEntryErrorMessage)")
                .append(" order by  txDate ");

        final Query statmentsNotInBankBookQry = persistenceService.getSession().createNativeQuery(statmentsNotInBankBookStr.toString())
                .addScalar("instrumentNo")
                .addScalar("credit")
                .addScalar("debit")
                .addScalar("txDate")
                .addScalar("id", LongType.INSTANCE)
                .addScalar("narration")
                .addScalar("type")
                .addScalar("errorCode")
                .addScalar("errorMessage")
                .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

        statmentsNotInBankBookQry.setParameter("fromDate", fromDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("multipleEntryErrorMessage", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE);
        statementsNotInBankBookList = statmentsNotInBankBookQry.list();
        notInBooktotalDebit = BigDecimal.ZERO;
        notInBooktotalCredit = BigDecimal.ZERO;
        notInBookNet = BigDecimal.ZERO;

        for (final AutoReconcileBean ab : statementsNotInBankBookList) {
            notInBooktotalDebit = notInBooktotalDebit.add(ab.getDebit() == null ? BigDecimal.ZERO : ab.getDebit());
            notInBooktotalCredit = notInBooktotalCredit.add(ab.getCredit() == null ? BigDecimal.ZERO : ab.getCredit());
        }
        notInBookNet = notInBooktotalCredit.subtract(notInBooktotalDebit);
        if (notInBookNet.compareTo(BigDecimal.ZERO) == -1)
            notInBookNetBal = notInBookNet + "(Dr)";
        else
            notInBookNetBal = notInBookNet + "(Cr)";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("notInBookNet" + notInBookNet);

        final CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(toDate);
        finYearStartDate = finYearByDate.getStartingDate();

        StringBuilder entriesNotInBankStamentStr = new StringBuilder();
        entriesNotInBankStamentStr.append("select  instrumentnumber as \"instrumentNo\",")
                .append(" instrumentdate as \"txDate\", instrumentamount as \"credit\",null as \"debit\", payto as \"narration\"  from egf_instrumentheader")
                .append("  where bankaccountid=:accountId and instrumentdate BETWEEN")
                .append(" :fromDate and :toDate and ispaycheque='0' and id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited')")
                .append(" and instrumentnumber is not null and instrumentamount is not null and instrumentnumber||'-'||instrumentamount")
                .append(" not in (select  instrumentno||'-'|| credit from egf_brs_bankstatements ")
                .append(" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage")
                .append("  and instrumentno is not null and  credit is not null and credit>0) ")
                .append(" union ")
                .append(" select   instrumentnumber as \"instrumentNo\",")
                .append(" instrumentdate as \"txDate\", instrumentamount \"debit\",null as \"credit\", payto as \"narration\" ")
                .append(" from egf_instrumentheader where bankaccountid=:accountId and instrumentdate BETWEEN :fromDate and :toDate ")
                .append(" and ispaycheque='1' and id_status=(select id from egw_status where moduletype='Instrument'  and description='New')")
                .append(" and  instrumentnumber is not null   and instrumentamount is not null and instrumentnumber||'-'||instrumentamount")
                .append(" not in  (select  instrumentno||'-'|| debit from egf_brs_bankstatements")
                .append(" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage")
                .append("  and instrumentno is not null and debit is not null and debit>0) order by \"txDate\"");

        Query entriesNotInBankStamentQry = persistenceService.getSession().createNativeQuery(entriesNotInBankStamentStr.toString())
                .addScalar("instrumentNo")
                .addScalar("credit")
                .addScalar("debit")
                .addScalar("txDate")
                .addScalar("narration")
                .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

        entriesNotInBankStamentQry.setParameter("fromDate", finYearStartDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE)
                .setParameter("multipleEntryErrorMessage", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE);
        entriesNotInBankStament = entriesNotInBankStamentQry.list();

        /**
         * ---------------------------------------
         */

        notInStatementTotalDebit = BigDecimal.ZERO;
        notInStatementTotalCredit = BigDecimal.ZERO;
        notInStatementNet = BigDecimal.ZERO;
        for (final AutoReconcileBean ab : entriesNotInBankStament) {
            // LOGGER.error("notInStatementTotalDebit=="+notInStatementTotalDebit+"           "+ab.getDebit());
            notInStatementTotalDebit = notInStatementTotalDebit.add(ab.getDebit() == null ? BigDecimal.ZERO : ab.getDebit());
            LOGGER.error("no=" + ab.getInstrumentNo() + " t =" + notInStatementTotalCredit + " a=" + ab.getCredit());
            notInStatementTotalCredit = notInStatementTotalCredit.add(ab.getCredit() == null ? BigDecimal.ZERO : ab.getCredit());
            // LOGGER.error("notInStatementTotalCredit=="+notInStatementTotalCredit+"           "+"notInStatementTotalDebit=="+notInStatementTotalDebit+"           count"+i);
        }
        LOGGER.error("notInStatementTotalCredit==" + notInStatementTotalCredit + "           " + "notInStatementTotalDebit=="
                + notInStatementTotalDebit);
        notInStatementNet = notInStatementTotalCredit.subtract(notInStatementTotalDebit);// this one will be reverse
        // LOGGER.error("notInStatementTotalCredit=="+notInStatementTotalCredit+"           "+"notInStatementTotalDebit=="+notInStatementTotalDebit
        // +"notInStatementNet                       "+notInStatementNet);
        // for match
        entriesNotInBankStamentStr = new StringBuilder();
        entriesNotInBankStamentStr.append("select  sum(instrumentamount) as \"credit\"  from egf_instrumentheader  where bankaccountid=:accountId and instrumentdate BETWEEN")
                .append(" :fromDate and :toDate and ispaycheque='0' and id_status=(select id from egw_status where moduletype='Instrument' and description='Deposited')")
                .append(" and instrumentnumber is not null and instrumentamount is not null and instrumentnumber||'-'||instrumentamount")
                .append(" not in (select  instrumentno||'-'|| credit from egf_brs_bankstatements ")
                .append(" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage ")
                .append(" and instrumentno is not null and  credit is not null and credit>0) ")
                .append(" union ")
                .append(" select   sum(instrumentamount) as \"credit\" ")
                .append(" from egf_instrumentheader where bankaccountid=:accountId and instrumentdate BETWEEN :fromDate and :toDate ")
                .append(" and ispaycheque='1' and id_status=(select id from egw_status where moduletype='Instrument'  and description='New')")
                .append(" and  instrumentnumber is not null   and instrumentamount is not null and instrumentnumber||'-'||instrumentamount")
                .append(" not in  (select  instrumentno||'-'|| debit from egf_brs_bankstatements")
                .append(" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage ")
                .append(" and instrumentno is not null and debit is not null and debit>0) ");

        entriesNotInBankStamentQry = persistenceService.getSession().createNativeQuery(entriesNotInBankStamentStr.toString())
                // .addScalar("instrumentNo")
                .addScalar("credit")
                // .addScalar("debit")
                // .addScalar("txDate")
                // .addScalar("narration")
                .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

        entriesNotInBankStamentQry.setParameter("fromDate", finYearStartDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("action", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE)
                .setParameter("multipleEntryErrorMessage", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE);
        final List<AutoReconcileBean> entriesNotInBankStament1 = entriesNotInBankStamentQry.list();
        if (entriesNotInBankStament1.size() > 0) {
            notInStatementTotalCredit = entriesNotInBankStament1.get(0).getCredit();
            if (notInStatementTotalCredit == null)
                notInStatementTotalCredit = BigDecimal.ZERO;
        }
        if (entriesNotInBankStament1.size() > 1) {
            notInStatementTotalDebit = entriesNotInBankStament1.get(1).getCredit();
            if (notInStatementTotalDebit == null)
                notInStatementTotalDebit = BigDecimal.ZERO;
        }

        notInStatementNet = notInStatementTotalCredit.subtract(notInStatementTotalDebit);
        StringBuilder statmentsfoundButNotProcessed = new StringBuilder();
        statmentsfoundButNotProcessed.append("select id,txDate,instrumentNo,debit,credit,narration,type,action as \"errorCode\",errorMessage ")
                .append(String.format("from %s", TABLENAME))
                .append(" where accountId=:accountId and txdate>=:fromDate  and txdate<=:toDate and reconciliationdate is null ")
                .append(" and  errorMessage =:multipleEntryErrorMessage order by  txDate ");

        final Query statmentsfoundButNotProcessedQry = persistenceService.getSession()
                .createNativeQuery(statmentsfoundButNotProcessed.toString())
                .addScalar("instrumentNo")
                .addScalar("credit")
                .addScalar("debit")
                .addScalar("txDate")
                .addScalar("id", LongType.INSTANCE)
                .addScalar("narration")
                .addScalar("type")
                .addScalar("errorCode")
                .addScalar("errorMessage")
                .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

        statmentsfoundButNotProcessedQry.setParameter("fromDate", fromDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("multipleEntryErrorMessage", BRS_MESSAGE_MORE_THAN_ONE_MATCH, StringType.INSTANCE)
                .setParameter("accountId", accountId, IntegerType.INSTANCE);
        statementsFoundButNotProcessed = statmentsfoundButNotProcessedQry.list();
        notprocessedDebit = BigDecimal.ZERO;
        notprocessedCredit = BigDecimal.ZERO;
        notprocessedNet = BigDecimal.ZERO;

        for (final AutoReconcileBean ab : statementsFoundButNotProcessed) {
            LOGGER.error("notprocessedDebit==" + notprocessedDebit + "           " + ab.getDebit());
            notprocessedDebit = notprocessedDebit.add(ab.getDebit() == null ? BigDecimal.ZERO : ab.getDebit());
            LOGGER.error("notprocessedCredit==" + notprocessedCredit + "           " + ab.getCredit());
            notprocessedCredit = notprocessedCredit.add(ab.getCredit() == null ? BigDecimal.ZERO : ab.getCredit());
            LOGGER.error("notprocessedDebit==" + notprocessedDebit + "           " + "notprocessedCredit==" + notprocessedCredit);
        }
        LOGGER.error("notprocessedDebit==" + notprocessedDebit + "           " + "notprocessedCredit==" + notprocessedCredit);
        notprocessedNet = notprocessedCredit.subtract(notprocessedDebit);// this one will be reverse
        LOGGER.error("notprocessedDebit==" + notprocessedDebit + "           " + "notprocessedCredit==" + notprocessedCredit);
        totalNotReconciledAmount = notInStatementNet.add(notprocessedNet.negate());
        brsBalance = bankBookBalance.add(notInStatementNet).add(notInBookNet).add(notprocessedNet);
        return "report";

    }

    public BigDecimal getTotalNotReconciledAmount() {
        return totalNotReconciledAmount;
    }

    public void setTotalNotReconciledAmount(final BigDecimal totalNotReconciledAmount) {
        this.totalNotReconciledAmount = totalNotReconciledAmount;
    }

    public BigDecimal getNotInBooktotalDebit() {
        return notInBooktotalDebit;
    }

    public void setNotInBooktotalDebit(final BigDecimal notInBooktotalDebit) {
        this.notInBooktotalDebit = notInBooktotalDebit;
    }

    public BigDecimal getNotInBooktotalCredit() {
        return notInBooktotalCredit;
    }

    public void setNotInBooktotalCredit(final BigDecimal notInBooktotalCredit) {
        this.notInBooktotalCredit = notInBooktotalCredit;
    }

    public BigDecimal getNotInBookNet() {
        return notInBookNet;
    }

    public void setNotInBookNet(final BigDecimal notInBookNet) {
        this.notInBookNet = notInBookNet;
    }

    public BigDecimal getNotInStatementTotalDebit() {
        return notInStatementTotalDebit;
    }

    public void setNotInStatementTotalDebit(final BigDecimal notInStatementTotalDebit) {
        this.notInStatementTotalDebit = notInStatementTotalDebit;
    }

    public BigDecimal getNotInStatementTotalCredit() {
        return notInStatementTotalCredit;
    }

    public void setNotInStatementTotalCredit(final BigDecimal notInStatementTotalCredit) {
        this.notInStatementTotalCredit = notInStatementTotalCredit;
    }

    public BigDecimal getNotInStatementNet() {
        return notInStatementNet;
    }

    public void setNotInStatementNet(final BigDecimal notInStatementNet) {
        this.notInStatementNet = notInStatementNet;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(final int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(final int rowCount) {
        this.rowCount = rowCount;
    }

    private void findandUpdateDuplicates() {
        // for payment cheques instrumentNo,debit,accountId combination should be unique else mark it duplicate
        try {
            StringBuilder duplicates = new StringBuilder();
            duplicates.append("select instrumentNo,debit,accountId")
                    .append(String.format(" from %s", TABLENAME))
                    .append(" where accountId=:accountId and debit>0 and action=:action")
                    .append(" group by  instrumentNo,debit,accountId having count(*)>1");

            final NativeQuery paymentDuplicateChequesQuery = persistenceService.getSession().createNativeQuery(duplicates.toString());
            paymentDuplicateChequesQuery.addScalar("instrumentNo")
                    .addScalar("debit")
                    .addScalar("accountId", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class))
                    .setParameter("action", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE)
                    .setParameter("accountId", accountId, IntegerType.INSTANCE);
            final List<AutoReconcileBean> duplicatePaymentCheques = paymentDuplicateChequesQuery.list();

            StringBuilder backUpdateDuplicatePaymentquery = new StringBuilder();
            backUpdateDuplicatePaymentquery.append(String.format("update %s", TABLENAME))
                    .append(" set action=:updateAction, errorMessage=:errorMsg")
                    .append(" where debit=:debit and accountid=:accountId and instrumentNo=:instrumentNo ")
                    .append(" and action=:whereAction");

            final NativeQuery paymentDuplicateUpdate = persistenceService.getSession().createNativeQuery(backUpdateDuplicatePaymentquery.toString())
                    .setParameter("updateAction", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE)
                    .setParameter("errorMsg", BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT, StringType.INSTANCE)
                    .setParameter("whereAction", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE);
            for (final AutoReconcileBean bean : duplicatePaymentCheques) {
                paymentDuplicateUpdate.setParameter("accountId", bean.getAccountId(), LongType.INSTANCE);
                paymentDuplicateUpdate.setParameter("debit", bean.getDebit(), BigDecimalType.INSTANCE);
                paymentDuplicateUpdate.setParameter("instrumentNo", bean.getInstrumentNo(), StringType.INSTANCE);
                paymentDuplicateUpdate.executeUpdate();

            }
            // this portion is for receipts instrumentNo,credit,accountId combination should be unique else mark it duplicate
            duplicates = new StringBuilder();
            duplicates.append("select instrumentNo,credit,accountId")
                    .append(String.format(" from %s", TABLENAME))
                    .append(" where accountid=:accountId and  credit>0 and action=:action")
                    .append(" group by  instrumentNo,credit,accountId having count(*)>1");

            final NativeQuery receiptsDuplicateChequesQuery = persistenceService.getSession().createNativeQuery(duplicates.toString())
                    .setParameter("action", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE);
            receiptsDuplicateChequesQuery.addScalar("instrumentNo")
                    .addScalar("credit")
                    .addScalar("accountId", LongType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
            receiptsDuplicateChequesQuery.setParameter("accountId", accountId, IntegerType.INSTANCE);
            final List<AutoReconcileBean> duplicateReceiptsCheques = receiptsDuplicateChequesQuery.list();

            StringBuilder backUpdateDuplicateReceiptsQuery = new StringBuilder();
            backUpdateDuplicateReceiptsQuery.append(String.format("update %s", TABLENAME))
                    .append(" set action=:updateAction, errorMessage=:errorMsg")
                    .append(" where credit=:credit and accountid=:accountId and instrumentNo=:instrumentNo ")
                    .append(" and action=:whereAction");
            final NativeQuery receiptDuplicateUpdate = persistenceService.getSession().createNativeQuery(backUpdateDuplicateReceiptsQuery.toString())
                    .setParameter("updateAction", BRS_ACTION_TO_BE_PROCESSED_MANUALLY, StringType.INSTANCE)
                    .setParameter("errorMsg", BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT, StringType.INSTANCE)
                    .setParameter("whereAction", BRS_ACTION_TO_BE_PROCESSED, StringType.INSTANCE);

            for (final AutoReconcileBean bean : duplicateReceiptsCheques) {
                receiptDuplicateUpdate.setParameter("accountId", bean.getAccountId(), LongType.INSTANCE);
                receiptDuplicateUpdate.setParameter("credit", bean.getCredit(), BigDecimalType.INSTANCE);
                receiptDuplicateUpdate.setParameter("instrumentNo", bean.getInstrumentNo(), StringType.INSTANCE);
                receiptDuplicateUpdate.executeUpdate();
            }
        } catch (final HibernateException e) {
            throw new ApplicationRuntimeException("Failed while processing autoreconciliation ");
        }

    }

    public List<DocumentUpload> getUploadedFiles(BankStatementUploadFile bankStatementUploadFile) {
        List<DocumentUpload> uploadedFileList = new ArrayList<>();
        if (bankStatementUploadFile.getBankAccount() == null && bankStatementUploadFile.getAsOnDate() == null) {
            uploadedFileList = documentUploadRepository.findByObjectType(FinancialConstants.BANK_STATEMET_OBJECT);
        } else if (bankStatementUploadFile.getBankAccount() != null && bankStatementUploadFile.getAsOnDate() == null) {
            uploadedFileList = documentUploadRepository.findByObjectId(bankStatementUploadFile.getBankAccount().getId());
        } else if (bankStatementUploadFile.getBankAccount() == null && bankStatementUploadFile.getAsOnDate() != null) {
            uploadedFileList = documentUploadRepository.findByUploadedDateAndObjectType(bankStatementUploadFile.getAsOnDate(), FinancialConstants.BANK_STATEMET_OBJECT);
        } else {
            uploadedFileList = documentUploadRepository.findByUploadedDateAndObjectId(bankStatementUploadFile.getAsOnDate(), bankStatementUploadFile.getBankAccount().getId());
        }
        return uploadedFileList;
    }

    public DocumentUpload getDocumentsByFileStoreId(String fileStore) {
        return documentUploadRepository.findByFileStore(fileStore);
    }

    public Date getReconciliationDate() {
        return reconciliationDate;
    }

    public void setReconciliationDate(final Date reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(final int accountId) {
        this.accountId = accountId;
    }

    public File getBankStatmentInXls() {
        return bankStatmentInXls;
    }

    public void setBankStatmentInXls(final File bankStatmentInXls) {
        this.bankStatmentInXls = bankStatmentInXls;
    }

    public void setBankStatmentInXlsContentType(final String bankStatmentInXlsContentType) {
    }

    public void setBankStatmentInXlsFileName(final String bankStatmentInXlsFileName) {
        this.bankStatmentInXlsFileName = bankStatmentInXlsFileName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(final String accNo) {
        this.accNo = accNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public List<AutoReconcileBean> getStatementsNotInBankBookList() {
        return statementsNotInBankBookList;
    }

    public void setStatementsNotInBankBookList(final List<AutoReconcileBean> statementsNotInBankBookList) {
        this.statementsNotInBankBookList = statementsNotInBankBookList;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public List<AutoReconcileBean> getEntriesNotInBankStament() {
        return entriesNotInBankStament;
    }

    public void setEntriesNotInBankStament(final List<AutoReconcileBean> entriesNotInBankStament) {
        this.entriesNotInBankStament = entriesNotInBankStament;
    }

    public List<AutoReconcileBean> getStatementsFoundButNotProcessed() {
        return statementsFoundButNotProcessed;
    }

    public void setStatementsFoundButNotProcessed(final List<AutoReconcileBean> statementsFoundButNotProcessed) {
        this.statementsFoundButNotProcessed = statementsFoundButNotProcessed;
    }

    public BigDecimal getNotprocessedNet() {
        return notprocessedNet;
    }

    public void setNotprocessedNet(final BigDecimal notprocessedNet) {
        this.notprocessedNet = notprocessedNet;
    }

    public String getNotInBookNetBal() {
        return notInBookNetBal;
    }

    public void setNotInBookNetBal(String notInBookNetBal) {
        this.notInBookNetBal = notInBookNetBal;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean isFailed) {
        this.isFailed = isFailed;
    }

    public BigDecimal getNotprocessedCredit() {
        return notprocessedCredit;
    }

    public void setNotprocessedCredit(BigDecimal notprocessedCredit) {
        this.notprocessedCredit = notprocessedCredit;
    }

    public BigDecimal getNotprocessedDebit() {
        return notprocessedDebit;
    }

    public void setNotprocessedDebit(BigDecimal notprocessedDebit) {
        this.notprocessedDebit = notprocessedDebit;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

}
