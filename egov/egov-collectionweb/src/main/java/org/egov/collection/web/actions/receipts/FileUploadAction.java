/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.web.actions.receipts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.service.ChallanService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Position;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@ParentPackage("egov")
@Transactional(readOnly = true)
public class FileUploadAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FileUploadAction.class);

    private File importFile;
    private String importContentType;
    private String importFileName;
    private String importfileCaption;

    /**
     * An array list of String arrays. Each String array represents an input set of values for challan receipt creation.
     */
    private List<String[]> inputList = new ArrayList<String[]>(0);

    private List<ReceiptDetailInfo> billCreditDetailslist = null;
    private List<ReceiptDetailInfo> subLedgerlist = null;

    SimpleDateFormat sdfInput = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    SimpleDateFormat sdfOutput = new SimpleDateFormat("ddMMyy", Locale.getDefault());

    private PersistenceService persistenceService;

    private CollectionsUtil collectionsUtil;

    private FinancialsUtil financialsUtil;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;

    private ReceiptHeaderService receiptHeaderService;

    private ChallanService challanService;

    private CollectionCommon collectionCommon;

    private BigDecimal cashOrCardInstrumenttotal;

    private BigDecimal chequeInstrumenttotal;

    @Autowired
    private ApplicationContext beanProvider;

    private final Map<Integer, String> errorRowMap = new TreeMap<Integer, String>();

    private List<Long> errorReceiptList = new ArrayList<Long>(0);

    private Integer successNo = 0;

    private String source = "";

    private boolean testMode = false;

    public void setInputList(final List<String[]> inputList) {
        this.inputList = inputList;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public void setReceiptWorkflowService(
            final WorkflowService<ReceiptHeader> receiptWorkflowService) {
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    public void setChallanService(final ChallanService challanService) {
        this.challanService = challanService;
    }


    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    @Override
    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String execute() throws Exception {
        return list();
    }

    public String newform() {
        return NEW;
    }

    public String list() {
        return INDEX;
    }

    public String edit() {

        return EDIT;
    }

    /**
     * This method removes any rows which have all null/empty entries in the column cells.
     */
    private void removeNullListEntries() {
        final List<String[]> tempInputList = new ArrayList<String[]>();

        for (final String[] currentArray : inputList) {
            boolean isNull = true;
            // last element need not be considered as it will always contain the correct row number
            for (int j = 0; j < currentArray.length - 1; j++)
                if (currentArray[j] != null && !currentArray[j].equals(CollectionConstants.BLANK)) {
                    isNull = false;
                    break;
                }
            if (!isNull)
                tempInputList.add(currentArray);
        }
        inputList = tempInputList;
    }

    /**
     * This method is used to persist the data from the uploaded file.
     * 
     * @return
     */
    @Transactional
    public String save() {
        readColumn();
        setSource("upload");
        removeNullListEntries();

        // populate receipt header and challan objects for each of the input
        // sets of values

        String[] input;
        for (int i = 0; i < inputList.size(); i++) {
            // for (int i = 0; i < 10; i++) {
            input = inputList.get(i);
            final String challanNumber = input[0];
            input = formatValues(input);

            final List<String[]> nextRecords = new ArrayList<String[]>();

            // if current mode of payt is cheque/dd, and current Amount column is not null or empty
            if (input[15].equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_CHEQUE) ||
                    CollectionConstants.INSTRUMENTTYPE_DD.equalsIgnoreCase(input[15])
                    && input[9] != null && !CollectionConstants.BLANK.equals(input[9]))
                // if a next row exists
                // add the next record to 'nextRecords' if the current and next record has the same challan number and challan
                // date
                // and the next record has mode of payt as cheque/dd and next record has a null/empty Amount column.
                // - i.e., current transaction has made payt though multiple cheque/DD
                while (i + 1 < inputList.size() && inputList.get(i + 1) != null &&
                        challanNumber.equals(inputList.get(i + 1)[0])
                        && input[1].equals(inputList.get(i + 1)[1])
                        && (inputList.get(i + 1)[9] == null || CollectionConstants.BLANK.equals(inputList.get(i + 1)[9])))
                    if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equalsIgnoreCase(inputList.get(i + 1)[15]) ||
                            CollectionConstants.INSTRUMENTTYPE_DD.equalsIgnoreCase(inputList.get(i + 1)[15])) {
                        final String[] rec = formatChequeDDValues(inputList.get(i + 1));
                        nextRecords.add(rec);
                        i++;
                    }

            if (validateExcel(input, nextRecords)) {
                billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
                subLedgerlist = new ArrayList<ReceiptDetailInfo>();
                cashOrCardInstrumenttotal = BigDecimal.ZERO;
                chequeInstrumenttotal = BigDecimal.ZERO;
                ReceiptHeader receiptHeader = initialiseValuesForSaveNew(input);

                // handling multiple account heads
                if (i + 1 < inputList.size() && inputList.get(i + 1) != null)
                    while (inputList.get(i + 1)[0] != null
                            && !inputList.get(i + 1)[0]
                                    .equals(CollectionConstants.BLANK)
                            && challanNumber
                                    .equals(inputList.get(i + 1)[0])
                            && input[1].equals(inputList.get(i + 1)[1])
                            && input[6].equals(inputList.get(i + 1)[6])
                            && inputList.get(i + 1)[8] != null
                            && !inputList.get(i + 1)[8]
                                    .equals(CollectionConstants.BLANK)) {
                        createCreditDetailslist(inputList.get(i + 1));

                        try {
                            if (!CollectionConstants.BLANK.equals(inputList
                                    .get(i + 1)[10])) {
                                if (inputList.get(i + 1)[11].endsWith(".0"))
                                    inputList.get(i + 1)[11] = inputList.get(i + 1)[11].substring(
                                            0, inputList.get(i + 1)[11].indexOf('.'));
                                createSubLedgerlist(inputList.get(i + 1));
                            }
                        } catch (final Exception e) {
                            LOGGER.debug(e.getMessage());
                        }
                        i++;
                        if (i >= inputList.size() - 1)
                            break;
                    }
                if (!testMode) {
                    // HibernateUtil.beginTransaction();
                }
                try {
                    // if any exception is present so far, do not create the challan.
                    if (errorRowMap.get(Integer.valueOf(input[21])) == null)
                        // create challan for each set of input values
                        receiptHeader = saveChallan(receiptHeader, input[7]);
                    else
                        // stop processing the record, and move to next
                        continue;
                } catch (final Exception e) {
                    if (e.getClass().equals(ConstraintViolationException.class) &&
                            e.getCause().toString().indexOf("UNIQUE_CHALLANNUMBER") != -1)
                        errorRowMap.put(Integer.valueOf(input[21]), "Duplicate Challan Number - [" + input[0] + "]");
                    else
                        errorRowMap.put(Integer.valueOf(input[21]), "Error in challan creation.");
                    if (!testMode) {
                        // HibernateUtil.rollbackTransaction();
                    }
                    continue;
                }

                try {
                    // create challan receipt for each set of input
                    createChallanReceipt(receiptHeader, input, nextRecords);
                } catch (final Exception e) {
                    errorRowMap.put(Integer.valueOf(input[21]), "Error in Challan Receipt creation.");
                    LOGGER.debug(e.getMessage());
                    if (!testMode) {
                        // HibernateUtil.rollbackTransaction();
                    }
                    continue;
                }
                if (!testMode) {
                    // HibernateUtil.commitTransaction();
                }
                setSuccessNo(++successNo);

                LOGGER.debug("Challan details in Row " + input[21] + "uploaded and persisted successfully!");
            }
        }

        try {
            generateErrorReport();
        } catch (final IOException e) {
            LOGGER.error("Error occrured while generating report : " + e.getMessage());
        }

        return SUCCESS;
    }

    private void generateErrorReport() throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("ManualChallanUploadErrorReport.txt"));

            if (!errorRowMap.isEmpty()) {
                String[] errors = null;
                fos.write("Errors in Uploading Following Rows \n".getBytes());
                fos.write("********************************** \n\n".getBytes());
                final Iterator it = errorRowMap.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pairs = (Map.Entry) it.next();
                    errors = new String[20];
                    errors = pairs.getValue().toString().split(",");
                    for (final String error2 : errors)
                        fos.write(("  Row " + pairs.getKey().toString() + " : " + error2 + "\n").getBytes());
                    errors = null;
                }
            }
            LOGGER.debug("Written to file successfully ");
            fos.close();

        } catch (final FileNotFoundException e) {
            LOGGER.error("Error occrured while generating report : " + e.getMessage());
        } finally {
            fos.close();
        }
    }

    public String fileValidate() {
        readColumn();
        setSource("validate");
        removeNullListEntries();

        // populate receipt header and challan objects for each of the input
        // sets of values

        String[] input;
        for (int i = 0; i < inputList.size(); i++) {
            // for (int i = 0; i < 10; i++) {
            input = inputList.get(i);
            final String challanNumber = input[0];
            input = formatValues(input);

            final List<String[]> nextRecords = new ArrayList<String[]>();

            // if current mode of payt is cheque/dd, and current Amount column is not null or empty
            if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equalsIgnoreCase(input[15]) ||
                    CollectionConstants.INSTRUMENTTYPE_DD.equalsIgnoreCase(input[15])
                    && input[9] != null && !CollectionConstants.BLANK.equals(input[9]))
                // if a next row exists
                // add the next record to 'nextRecords' if the current and next record has the same challan number and challan
                // date
                // and the next record has mode of payt as cheque/dd and next record has a null/empty Amount column.
                // - i.e., current transaction has made payt though multiple cheque/DD
                while (i + 1 < inputList.size() && inputList.get(i + 1) != null &&
                        challanNumber.equals(inputList.get(i + 1)[0])
                        && input[1].equals(inputList.get(i + 1)[1])
                        && (inputList.get(i + 1)[9] == null || CollectionConstants.BLANK.equals(inputList.get(i + 1)[9])))
                    if (inputList.get(i + 1)[15].equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || CollectionConstants.INSTRUMENTTYPE_DD.equalsIgnoreCase(inputList.get(i + 1)[15])) {
                        final String[] nextRec = formatChequeDDValues(inputList.get(i + 1));
                        nextRecords.add(nextRec);
                        // nextRecords.add(inputList.get(i+1));
                        i++;
                    }
            if (validateExcel(input, nextRecords))
                successNo++;
            else
                LOGGER.debug("Challan row no: " + i
                        + " not persisted due to some incomplete data!!!!");
        }
        try {
            generateErrorReport();
        } catch (final IOException e) {
            LOGGER.error("Error occrured while generating report : " + e.getMessage());
        }
        return SUCCESS;
    }

    private boolean validateExcel(final String[] inputArray, final List<String[]> nextRecords) {
        boolean valid = true;
        String errorMsgs = "";

        if (inputArray[0] == null || CollectionConstants.BLANK.equals(inputArray[0])) {
            errorMsgs += getErrorMsg(errorMsgs, "Challan Number is null/Empty", null);
            if (errorMsgs.equals(CollectionConstants.BLANK))
                errorMsgs += "Challan Date is null/Empty";
            else
                errorMsgs += ", Challan Date is null/Empty";
            LOGGER.debug("Challan Number is null/Empty");
            valid = false;
        }

        if (inputArray[1] == null || CollectionConstants.BLANK.equals(inputArray[1])) {
            errorMsgs += getErrorMsg(errorMsgs, "Challan Date is null/Empty", null);
            LOGGER.debug("Challan Date is null/Empty");
            valid = false;
        }
        try {
            final Date date = sdfInput.parse(inputArray[1]);
            collectionsUtil.getFinancialYearforDate(date);
        } catch (final ParseException e) {
            errorMsgs += getErrorMsg(errorMsgs, "Invalid Challan Date", inputArray[1]);
            valid = false;
        } catch (final Exception ex) {
            errorMsgs += getErrorMsg(errorMsgs, "No financial year found for challan date", inputArray[1]);
            valid = false;
        }

        if (inputArray[5] == null || CollectionConstants.BLANK.equals(inputArray[5])) {
            errorMsgs += getErrorMsg(errorMsgs, "Fund is null/Empty", null);

            LOGGER.debug("Fund is null/Empty");
            valid = false;
        }
        else {
            final Fund fund = (Fund) persistenceService.find("from Fund  where name=? ", inputArray[5]);
            if (fund == null) {
                errorMsgs += getErrorMsg(errorMsgs, "Incorrect value for fund", inputArray[5]);

                LOGGER.debug("Incorrect value for fund" + inputArray[5] + "]");
                valid = false;
            }

        }
        if (inputArray[6] == null || CollectionConstants.BLANK.equals(inputArray[6])) {
            errorMsgs += getErrorMsg(errorMsgs, "Department is null/Empty", null);
            LOGGER.debug("Department is null/Empty");
            valid = false;
        }
        else {
            final Department dept = (Department) persistenceService.find(
                    "from Department d where d.deptName=? ", inputArray[6]);
            if (dept == null) {
                errorMsgs += getErrorMsg(errorMsgs, "Incorrect value for Department", inputArray[6]);
                LOGGER.debug("Incorrect value for Department[ " + inputArray[6] + "]");
                valid = false;
            }
        }
        if (inputArray[7] != null && !CollectionConstants.BLANK.equals(inputArray[7])) {
            final CFunction function =
                    (CFunction) persistenceService.find("from CFunction  where code=? ", inputArray[7]);
            if (function == null) {
                errorMsgs += getErrorMsg(errorMsgs, "Incorrect value for Function", inputArray[7]);
                LOGGER.debug("Incorrect value for Function " + inputArray[7]);
                valid = false;
            }
        }

        if (inputArray[8] == null || CollectionConstants.BLANK.equals(inputArray[8])) {
            errorMsgs += getErrorMsg(errorMsgs, "Account Code is null/Empty", null);
            LOGGER.debug("Account Code is null/Empty");
            valid = false;
        }
        else {
            final CChartOfAccounts account = (CChartOfAccounts) persistenceService.find(
                    "from CChartOfAccounts  where glcode=? and isActiveForPosting=true", inputArray[8]);
            if (account == null) {
                errorMsgs += getErrorMsg(errorMsgs, "Incorrect value for Account Code ", inputArray[8]);
                LOGGER.debug("Incorrect value for Account Code ");
                valid = false;
            }
            else {
                final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) persistenceService.find(
                        " from CChartOfAccountDetail" +
                                " where glCodeId=(select id from CChartOfAccounts where glcode=? and isActiveForPosting=true)",
                        inputArray[8]);
                if (null != chartOfAccountDetail && (inputArray[10] == null || CollectionConstants.BLANK.equals(inputArray[10]))) {
                    errorMsgs += getErrorMsg(errorMsgs, "No Subledger Data provided for account Code", inputArray[8]);
                    LOGGER.debug("No Subledger Data provided for account Code ");
                    valid = false;
                }
                if (null != chartOfAccountDetail
                        && inputArray[9] != null
                        && !CollectionConstants.BLANK
                                .equals(inputArray[9])
                        && inputArray[12] != null && !CollectionConstants.BLANK
                                .equals(inputArray[12]) && !inputArray[9].equals(inputArray[12])) {
                    errorMsgs += getErrorMsg(errorMsgs, "Account head Amount [" + inputArray[9]
                            + "] is not same as Subledger Amount [" + inputArray[12] + "] for account Code [" + inputArray[8]
                            + "]", null);
                    LOGGER.debug("Account head Amount [" + inputArray[9] + "] is not same as Subledger Amount [" + inputArray[12]
                            + "] for account Code " + inputArray[8]);
                    valid = false;

                }
            }
        }

        if (inputArray[9] == null || CollectionConstants.BLANK.equals(inputArray[9])) {
            errorMsgs += getErrorMsg(errorMsgs, "Account head Amount is null/Empty", null);
            LOGGER.debug("Account head Amount is null/Empty");
            valid = false;
        }
        if (inputArray[10] != null && !CollectionConstants.BLANK.equals(inputArray[10]))
            try {
                final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                        "from Accountdetailtype  where name=? ", inputArray[10]);
                final String table = accountdetailtype.getFullQualifiedName();
                Class<?> service;
                service = Class.forName(table);
                service.getSimpleName();
            } catch (final Exception e) {
                errorMsgs += getErrorMsg(errorMsgs, "Subledger account type not found", inputArray[10]);
                LOGGER.debug("Subledger account type not found for [" + inputArray[10] + "]");
                valid = false;
            }
        if (inputArray[10] != null
                && !CollectionConstants.BLANK.equals(inputArray[10])
                && (inputArray[11] == null
                        || CollectionConstants.BLANK.equals(inputArray[11]) || inputArray[12] == null
                        || CollectionConstants.BLANK.equals(inputArray[12]))) {
            errorMsgs += getErrorMsg(errorMsgs, "Subledger Data is incomplete for account Code", inputArray[8]);
            LOGGER.debug(" Subledger Data is incomplete for account Code ");
            valid = false;

        }
        if (inputArray[13] == null || CollectionConstants.BLANK.equals(inputArray[13])) {
            errorMsgs += getErrorMsg(errorMsgs, "Receipt number is null/Empty", null);
            LOGGER.debug("Receipt number is null/Empty");
            valid = false;
        }

        if (inputArray[14] == null || CollectionConstants.BLANK.equals(inputArray[14])) {
            errorMsgs += getErrorMsg(errorMsgs, "Receipt date is null/Empty", null);
            LOGGER.debug("Receipt date is null/Empty");
            valid = false;
        }

        try {
            final Date date = sdfInput.parse(inputArray[14]);
            collectionsUtil.getFinancialYearforDate(date);
        } catch (final ParseException e) {
            errorMsgs += getErrorMsg(errorMsgs, "Invalid Receipt Date Format", inputArray[14]);
            valid = false;
        } catch (final Exception ex) {
            errorMsgs += getErrorMsg(errorMsgs, "No financial year found for receipt date", inputArray[14]);
            valid = false;
        }

        if (inputArray[15] == null || CollectionConstants.BLANK.equals(inputArray[15])) {
            errorMsgs += getErrorMsg(errorMsgs, "Payment Mode is null/Empty", null);
            LOGGER.debug("Payment Mode is null/Empty");
            valid = false;
        }

        if (inputArray[15] != null
                && (CollectionConstants.INSTRUMENTTYPE_CHEQUE
                        .equalsIgnoreCase(inputArray[15]) || CollectionConstants.INSTRUMENTTYPE_DD
                        .equalsIgnoreCase(inputArray[15].toLowerCase()))) {
            final String result = validateChequeDD(inputArray);

            if (!CollectionConstants.BLANK.equals(result)) {
                errorMsgs += getErrorMsg(errorMsgs, result, null);
                valid = false;
            }
        }

        BigDecimal sumOfPayts = BigDecimal.valueOf(0);

        if (inputArray[16] == null || CollectionConstants.BLANK.equals(inputArray[16])) {
            errorMsgs += getErrorMsg(errorMsgs, "Payment Amount is null/Empty", null);
            LOGGER.debug("Payment Amount is null/Empty");
            valid = false;
        } else
            sumOfPayts = new BigDecimal(inputArray[16]);
        boolean multipleChequesValid = true;
        // validations for multiple Cheque/DD mode of payment
        if (nextRecords != null && !nextRecords.isEmpty()) {

            for (final String[] nextRecord : nextRecords) {
                if (nextRecord[16] != null && !CollectionConstants.BLANK.equals(nextRecord[16]))
                    sumOfPayts = sumOfPayts.add(new BigDecimal(nextRecord[16]));

                final String result = validateChequeDD(nextRecord);

                if (!CollectionConstants.BLANK.equals(result)) {
                    // valid=false;
                    multipleChequesValid = false;
                    errorRowMap.put(Integer.valueOf(nextRecord[21]), result);
                }
            }

            if (sumOfPayts.compareTo(new BigDecimal(inputArray[9])) != 0) {
                errorMsgs += getErrorMsg(errorMsgs, "Sum of Cheque/DD amounts do not tally with the total amount", null);
                valid = false;
            }
        }

        if (!valid)
            errorRowMap.put(Integer.valueOf(inputArray[21]), errorMsgs);

        return valid && multipleChequesValid;
    }

    private String getErrorMsg(final String errorMsgs, final String msg, final String value) {
        final StringBuilder tempmsg = new StringBuilder();
        tempmsg.append(msg);
        if (value != null)
            tempmsg.append(CollectionConstants.OPENBRACKET + value + CollectionConstants.CLOSEBRACKET);
        if (errorMsgs.equals(CollectionConstants.BLANK))
            return tempmsg.toString();
        else
            return CollectionConstants.COMMA + tempmsg.toString();
    }

    private String validateChequeDD(final String[] inputArray) {
        String errorMsgs = "";
        if (inputArray[16] == null
                || inputArray[16].equals(CollectionConstants.BLANK)) {
            errorMsgs += getErrorMsg(errorMsgs, "Cheque/DD Payment Amount is null/empty", null);
            LOGGER.debug("Cheque/DD Payment Amount is null/empty");
        }
        if (inputArray[17] == null
                || inputArray[17].equals(CollectionConstants.BLANK)) {
            errorMsgs += getErrorMsg(errorMsgs, "Cheque/DD Number is null/empty", null);
            LOGGER.debug("Cheque/DD Number is null/empty");
        }
        else if (inputArray[17].length() != 6) {
            errorMsgs += getErrorMsg(errorMsgs, "Invalid Cheque/DD Number", inputArray[17]);
            LOGGER.debug("Invalid Cheque/DD Number [" + inputArray[17] + "]");
        }
        if (inputArray[18] == null
                || inputArray[18].equals(CollectionConstants.BLANK)) {
            errorMsgs += getErrorMsg(errorMsgs, "Cheque/DD Date is null/empty", null);
            LOGGER.debug("Cheque/DD Date is null/empty");
        }
        else {
            try {
                sdfInput.parse(inputArray[18]);
            } catch (final ParseException e) {
                errorMsgs += getErrorMsg(errorMsgs, "Invalid Cheque/DD Date", inputArray[18]);
            }
            LOGGER.debug("Invalid Cheque/DD Date [" + inputArray[18] + "]");
        }
        if (inputArray[19] == null // bank name
                || inputArray[19].equals(CollectionConstants.BLANK)) {
            errorMsgs += getErrorMsg(errorMsgs, "Cheque/DD Bank Name is null/empty", null);
            LOGGER.debug("Cheque/DD Bank Name is null/empty");
        }
        else {
            final Bank bank = (Bank) persistenceService.find("from Bank where name=?", inputArray[19]);
            if (bank == null) {
                errorMsgs += getErrorMsg(errorMsgs, "Incorrect value for Bank Name", inputArray[19]);
                LOGGER.debug("Incorrect value for Bank Name[" + inputArray[19] + "]");
            }
        }

        return errorMsgs;
    }

    /**
     * 
     * @param receiptHeader
     * @param functionName
     * @return
     * @throws ApplicationException
     */
    private ReceiptHeader saveChallan(final ReceiptHeader receipt,
            final String functionName) {

        receipt.getReceiptDetails().clear();
        final ReceiptHeader receiptHeader = populateAndPersistChallanReceipt(receipt,
                functionName);
        final Position position = collectionsUtil.getPositionOfUser(collectionsUtil
                .getLoggedInUser());
        challanService.workflowtransition(receiptHeader.getChallan(), position,
                CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN,
                "Challan Upload-Phase1");
        challanService.getSession().flush();

        return receiptHeader;

    }

    /**
     * This method creates a receipt along with the challan. The receipt is created in PENDING status where as the challan is
     * created with a CREATED status.
     * 
     * The receipt is actually created later when there is a request for it to be created against the challan.
     */
    @Transactional
    private ReceiptHeader populateAndPersistChallanReceipt(final ReceiptHeader receiptHeader, final String functionName) {

        receiptHeader.setService((ServiceDetails) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_CODE,
                CollectionConstants.SERVICE_CODE_COLLECTIONS));
        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                CollectionConstants.RECEIPT_STATUS_CODE_PENDING));

        // recon flag should be set as false when the receipt is actually
        // created against the challan
        receiptHeader.setIsReconciled(Boolean.TRUE);
        receiptHeader.setIsModifiable(Boolean.FALSE);
        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_CHALLAN);
        receiptHeader.setPaidBy(CollectionConstants.CHAIRPERSON);

        receiptHeader.getReceiptMisc().setReceiptHeader(receiptHeader);

        BigDecimal debitamount = BigDecimal.ZERO;
        int m = 0;

        // validateAccountDetails();

        BigDecimal totalAmt = BigDecimal.ZERO;

        for (final ReceiptDetailInfo rDetails : billCreditDetailslist) {
            final CChartOfAccounts account = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(
                    rDetails.getGlcodeDetail());
            final CFunction function = (CFunction) persistenceService.find("from CFunction  where code=? ", functionName);
            ReceiptDetail receiptDetail = new ReceiptDetail(
                    account, function, rDetails.getCreditAmountDetail(),
                    rDetails.getDebitAmountDetail(), null,
                    Long.valueOf(m), null, null, receiptHeader);
            receiptDetail.setCramount(rDetails.getCreditAmountDetail());

            totalAmt = totalAmt.add(
                    receiptDetail.getCramount()).subtract(
                    receiptDetail.getDramount());

            final CFinancialYear financialYear = financialYearDAO.getFinancialYearById(rDetails.getFinancialYearId());
            receiptDetail.setFinancialYear(financialYear);

            if (rDetails.getCreditAmountDetail() == null)
                receiptDetail.setCramount(BigDecimal.ZERO);
            else
                receiptDetail.setCramount(rDetails.getCreditAmountDetail());

            if (rDetails.getDebitAmountDetail() == null)
                receiptDetail.setDramount(BigDecimal.ZERO);
            else
                receiptDetail.setDramount(rDetails.getDebitAmountDetail());

            receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
            receiptHeader.addReceiptDetail(receiptDetail);
            debitamount = debitamount.add(rDetails.getCreditAmountDetail());
            debitamount = debitamount.subtract(rDetails.getDebitAmountDetail());
            m++;
        }

        receiptHeader.setTotalAmount(totalAmt);

        if (receiptHeader.getChallan().getCreatedBy() == null)
            receiptHeader.getChallan().setCreatedBy(
                    collectionsUtil.getLoggedInUser());

        receiptHeader.getChallan().setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_CHALLAN,
                CollectionConstants.CHALLAN_STATUS_CODE_CREATED));
        // set service in challan
        if (receiptHeader.getChallan().getService() != null && receiptHeader.getChallan().getService().getId() != null)
            receiptHeader.getChallan().setService((ServiceDetails) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_ID,
                    receiptHeader.getChallan().getService().getId()));

        /*
         * ReceiptPayeeDetails receiptPayee = receiptHeader.getReceiptPayeeDetails();
         * receiptPayee.addReceiptHeader(receiptHeader); receiptPayee=receiptPayeeDetailsService.persistChallan(receiptPayee);
         */
        //receiptHeaderService.persistChallan(receiptHeader);
        receiptHeaderService.getSession().flush();
        LOGGER.info("Persisted Challan and Created Receipt In Pending State For the Challan");

        return receiptHeader;
    }

    public ReceiptDetail setAccountPayeeDetails(final List<ReceiptDetailInfo> subLedgerlist, final ReceiptDetail receiptDetail) {
        for (final ReceiptDetailInfo subvoucherDetails : subLedgerlist)
            if (subvoucherDetails.getGlcode() != null && subvoucherDetails.getGlcode().getId() != 0 &&
                    subvoucherDetails.getGlcode().getId().equals(receiptDetail.getAccounthead().getId())) {

                final Accountdetailtype accdetailtype = (Accountdetailtype) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID,
                        subvoucherDetails.getDetailType().getId());
                final Accountdetailkey accdetailkey = (Accountdetailkey) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY,
                        subvoucherDetails.getDetailKeyId(), subvoucherDetails.getDetailType().getId());

                final AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(
                        accdetailtype, accdetailkey, subvoucherDetails.getAmount(), receiptDetail);

                receiptDetail.addAccountPayeeDetail(accPayeeDetail);
            }
        return receiptDetail;
    }

    @Transactional
    private void createChallanReceipt(final ReceiptHeader receiptHeader, final String[] input,
            final List<String[]> nextRecords) {

        // for post remittance cancellation
        if (receiptHeader.getReceiptHeader() != null)
            collectionCommon.cancelChallanReceiptOnCreation(receiptHeader);

        boolean setInstrument = true;
        List<InstrumentHeader> receiptInstrList = new ArrayList<InstrumentHeader>();
        chequeInstrumenttotal = BigDecimal.valueOf(0);

        receiptHeader.setIsReconciled(Boolean.FALSE);
        receiptHeader.setIsModifiable(Boolean.TRUE);
        receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);

        receiptHeader.setLocation(collectionsUtil.getLocationOfUser(getSession()));

        // manually created challan receipts are in approved state
        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));

        receiptHeader.setCreatedBy(collectionsUtil.getLoggedInUser());
        receiptHeader.setCreatedDate(receiptHeader.getManualreceiptdate());
        if (setInstrument) {
            receiptInstrList = populateInstrumentDetails(input, nextRecords);
            setInstrument = false;
        }

        receiptHeader.setReceiptInstrument(new HashSet(receiptInstrList));

        BigDecimal debitAmount = BigDecimal.ZERO;

        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
            debitAmount = debitAmount.add(receiptDetail.getCramount());

        receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(
                debitAmount, receiptHeader, chequeInstrumenttotal, cashOrCardInstrumenttotal,
                input[15]));

        if (chequeInstrumenttotal != null && chequeInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
            receiptHeader.setTotalAmount(chequeInstrumenttotal);

        if (cashOrCardInstrumenttotal != null && cashOrCardInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
            receiptHeader.setTotalAmount(cashOrCardInstrumenttotal);

        // receiptPayeeDetailsService.setReceiptNumber(receiptHeader);

        receiptHeaderService.persist(receiptHeader);

        // Start work flow for all newly created receipts This might internally
        // create vouchers also based on configuration

        receiptHeaderService.startWorkflow(receiptHeader);
        LOGGER.info("Workflow started for newly created receipts");

        // transition the receipt header workflow to Approved state
        // receiptWorkflowService.transition(CollectionConstants.WF_ACTION_APPROVE,
        // receiptHeader, "Approval of Manually Created Challan Receipt Complete");
        // End the Receipt header workflow

        // receiptWorkflowService.end(receiptHeader, collectionsUtil
        // .getPositionOfUser(receiptHeader.getCreatedBy()),
        // "Manually Created Challan Receipt Approved - Workflow ends");

        final List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>(0);

        // If vouchers are created during work flow step, add them to the list
        final Set<ReceiptVoucher> receiptVouchers = receiptHeader.getReceiptVoucher();
        for (final ReceiptVoucher receiptVoucher : receiptVouchers)
            try {
                voucherHeaderList.add(receiptVoucher.getVoucherheader());
            } catch (final Exception e) {
                LOGGER.error("Error in getting voucher header for id ["
                        + receiptVoucher.getVoucherheader() + "]", e);
            }

        if (voucherHeaderList != null && receiptInstrList != null)
            receiptHeaderService.updateInstrument(receiptHeader);

    }

    /**
     * Populate Instrument Details.
     *
     * @return the list
     */
    private List<InstrumentHeader> populateInstrumentDetails(final String[] input, final List<String[]> nextRecords) {
        final List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();

        if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(input[15])) {
            final InstrumentHeader instrHeaderCash = new InstrumentHeader();

            instrHeaderCash.setInstrumentType(
                    financialsUtil.getInstrumentTypeByType(
                            CollectionConstants.INSTRUMENTTYPE_CASH));

            instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
            instrHeaderCash.setInstrumentAmount(new BigDecimal(input[16]));

            // this total is needed for creating debit account head
            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(
                    instrHeaderCash.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderCash);
        }

        if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equalsIgnoreCase(input[15]) ||
                CollectionConstants.INSTRUMENTTYPE_DD.equalsIgnoreCase(input[15])) {

            instrumentHeaderList.add(populateInstrumentHeaderForChequeDD(
                    input));

            if (nextRecords != null && !nextRecords.isEmpty())
                for (final String[] nextRecord : nextRecords)
                    instrumentHeaderList.add(
                            populateInstrumentHeaderForChequeDD(nextRecord));
        }

        // instrumentHeaderList=receiptPayeeDetailsService.createInstrument(instrumentHeaderList);

        return instrumentHeaderList;
    }

    private InstrumentHeader populateInstrumentHeaderForChequeDD(final String[] input) {
        final InstrumentHeader instrumentHeader = new InstrumentHeader();

        instrumentHeader.setInstrumentAmount(new BigDecimal(input[16]));

        if (input[15].equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
            instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(
                    CollectionConstants.INSTRUMENTTYPE_CHEQUE));
        else if (input[15].equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_DD))
            instrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(
                    CollectionConstants.INSTRUMENTTYPE_DD));

        instrumentHeader.setInstrumentNumber(input[17]);

        if (input[19] != null) {
            final Bank bank = (Bank) persistenceService.find("from Bank where name=?", input[19]);
            instrumentHeader.setBankId(bank);
        }

        if (input[20] != null)
            instrumentHeader.setBankBranchName(input[20]);

        Date dt = new Date();
        if (input[18] != null) {
            try {
                dt = sdfInput.parse(input[18]);

            } catch (final ParseException e) {
                LOGGER.debug("Error occured while parsing instrument date " + e.getMessage());
            }
            instrumentHeader.setInstrumentDate(dt);
        }

        chequeInstrumenttotal = chequeInstrumenttotal.add(new BigDecimal(input[16]));

        instrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);

        return instrumentHeader;

    }

    public String create() {
        return SUCCESS;
    }

    protected void readColumn()
    {
        try {
            final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(getImportFile()));
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            final HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            int rows; // Number of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // Number of columns
            int tmp = 0;

            for (int i = 0; i < 15 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols)
                        cols = tmp;
                }
            }

            for (int r = 3; r < rows; r++)
            {
                row = sheet.getRow(r);
                if (row != null)
                {
                    // Each row represents a set of input values for challan receipt creation
                    final String[] inputArray = new String[22];
                    for (int c = 0; c < 21; c++)
                    {
                        cell = row.getCell(c);

                        if (cell == null)
                            inputArray[c] = "";
                        else {
                            final int ct = cell.getCellType();
                            if (ct == HSSFCell.CELL_TYPE_FORMULA)
                                inputArray[c] = Double.toString(cell
                                        .getNumericCellValue());
                            else
                                inputArray[c] = cell.toString();
                        }
                    }

                    // the last element holds the correct row number
                    inputArray[21] = String.valueOf(r + 1);
                    inputList.add(inputArray);
                }
            }

        } catch (final Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    private String[] formatValues(final String[] inputArray) {
        // To change challan number from 1.0 to 1
        if (inputArray[0] != null && !CollectionConstants.BLANK.equals(inputArray[0]) && inputArray[0].endsWith(".0"))
            inputArray[0] = inputArray[0].substring(0, inputArray[0].indexOf('.'));

        // To Change challan number from 1 to E/1
        if (inputArray[6] != null && !CollectionConstants.BLANK.equals(inputArray[6])) {
            final Department dept = (Department) persistenceService.find("from Department d where d.deptName=? ", inputArray[6]);
            if (dept != null) {
                final String departmentCode = dept.getCode();
                Date date = new Date();
                try {
                    if (inputArray[1] != null && !inputArray[1].equals(CollectionConstants.BLANK))
                        date = sdfInput.parse(inputArray[1]);

                } catch (final ParseException e) {
                    LOGGER.debug(e.getMessage());
                }
                inputArray[0] = departmentCode.concat("/" + sdfOutput.format(date) + "/" + inputArray[0]);
            }
        }

        // To change function code from 808201.0 to 808201
        if (inputArray[7] != null && !CollectionConstants.BLANK.equals(inputArray[7]) && inputArray[7].endsWith(".0"))
            inputArray[7] = inputArray[7].substring(0, inputArray[7].indexOf('.'));
        // To change account code from 140200201.0 to 140200201
        if (inputArray[8] != null && !CollectionConstants.BLANK.equals(inputArray[8])) {
            if (inputArray[8].endsWith(".0"))
                inputArray[8] = inputArray[8].substring(0, inputArray[8].indexOf('.'));
            // To change account code from 1.401306E8 to 140200201
            final BigDecimal bd = new BigDecimal(inputArray[8]);
            inputArray[8] = bd.toPlainString();
        }

        // To change payment mode from CASH to cash
        if (inputArray[15] != null && !CollectionConstants.BLANK.equals(inputArray[15]))
            inputArray[15] = inputArray[15].toLowerCase();
        // To change subledger account code from 101.0 to 101
        if (inputArray[11] != null && !CollectionConstants.BLANK.equals(inputArray[11]) && inputArray[11].endsWith(".0"))
            inputArray[11] = inputArray[11].substring(0, inputArray[11].indexOf('.'));
        // To change chequenumber account code from 123456.0 to 123456
        if (inputArray[17] != null && !inputArray[17].equals(CollectionConstants.BLANK) && inputArray[17].endsWith(".0"))
            inputArray[17] = inputArray[17].substring(0, inputArray[17].indexOf('.'));
        // To set cheque date as receipt date in case if its empty and if payment is cheque/dd
        if ((inputArray[15] != null && inputArray[15]
                .equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || inputArray[15]
                    .equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_DD))
                && inputArray[18] != null && inputArray[18].equals(CollectionConstants.BLANK))
            inputArray[18] = inputArray[14];
        return inputArray;
    }

    private String[] formatChequeDDValues(final String[] inputArray) {
        // To change chequenumber account code from 123456.0 to 123456
        if (inputArray[17] != null && !inputArray[17].equals(CollectionConstants.BLANK) && inputArray[17].endsWith(".0"))
            inputArray[17] = inputArray[17].substring(0, inputArray[17].indexOf('.'));
        // To set cheque date as receipt date in case if its empty and if payment is cheque/dd
        if ((inputArray[15] != null && inputArray[15]
                .equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || inputArray[15]
                    .equalsIgnoreCase(CollectionConstants.INSTRUMENTTYPE_DD))
                && inputArray[18] != null && inputArray[18].equals(CollectionConstants.BLANK))
            inputArray[18] = inputArray[14];

        return inputArray;
    }

    @Transactional
    private ReceiptHeader initialiseValuesForSaveNew(final String[] inputArray) {

        final ReceiptHeader header = createReceiptHeader(inputArray);
        final Challan challan = createChallan(header, inputArray);
        header.setChallan(challan);

        createCreditDetailslist(inputArray);

        try {
            if (CollectionConstants.BLANK.equals(inputArray[10]))
                createEmptySubLedgerlist();
            else
                createSubLedgerlist(inputArray);
        } catch (final Exception e) {
            LOGGER.debug("Exception in initialising values during sub ledger list creation : " + e.getMessage());
        }

        return header;
    }

    public ReceiptHeader createReceiptHeader(final String[] input) {
        final ReceiptHeader receiptHeader = new ReceiptHeader();
        receiptHeader.setReferenceDesc(input[4]);
        receiptHeader.setReceiptMisc(createReceiptMisc(receiptHeader, input));
        // receiptHeader.setReceiptPayeeDetails(createReceiptPayee(input));
        receiptHeader.setManualreceiptnumber(input[13]);
        Date date = null;
        try {
            date = sdfInput.parse(input[14]);
        } catch (final ParseException e) {
            LOGGER.debug("Exception in parsing date : " + input[14] + " - " + e.getMessage());
        }
        receiptHeader.setManualreceiptdate(date);
        receiptHeader.setVoucherDate(date);
        return receiptHeader;
    }

    public ReceiptMisc createReceiptMisc(final ReceiptHeader receiptHeader, final String[] inputArray) {
        final ReceiptMisc receiptMisc = new ReceiptMisc();
        final Fund fund = (Fund) persistenceService.find("from Fund  where name=? ", inputArray[5]);
        final Department dept = (Department) persistenceService.find(
                "from Department d where d.deptName=? ", inputArray[6]);
        receiptMisc.setDepartment(dept);
        receiptMisc.setFund(fund);
        receiptMisc.setReceiptHeader(receiptHeader);
        return receiptMisc;
    }

    /*
     * public ReceiptPayeeDetails createReceiptPayee(String[] inputArray) { ReceiptPayeeDetails receiptPayee = new
     * ReceiptPayeeDetails(); receiptPayee.setPayeeAddress(inputArray[3]); receiptPayee.setPayeename(inputArray[2]); return
     * receiptPayee; }
     */

    public Challan createChallan(final ReceiptHeader header, final String[] inputArray) {
        final Challan challan = new Challan();

        Date date = null;
        try {
            date = sdfInput.parse(inputArray[1]);

        } catch (final ParseException e) {
            LOGGER.debug("Exception in parsing challan date : " + inputArray[1] + " - " + e.getMessage());
        }
        challan.setChallanDate(date);
        challan.setChallanNumber(inputArray[0]);
        challan.setReceiptHeader(header);
        challan.setLastModifiedBy(collectionsUtil.getLoggedInUser());
        challan.setLastModifiedDate(date);

        return challan;
    }

    public void createCreditDetailslist(final String[] inputArray) {
        final ReceiptDetailInfo vd = new ReceiptDetailInfo();
        final CChartOfAccounts account = (CChartOfAccounts) persistenceService.find(
                "from CChartOfAccounts  where glcode=? and isActiveForPosting=true", inputArray[8]);
        if (account != null) {
            vd.setAccounthead(account.getName());
            vd.setCreditAmountDetail(new BigDecimal(inputArray[9]));
            vd.setDebitAmountDetail(BigDecimal.ZERO);
            vd.setAmount(BigDecimal.ZERO);
            vd.setGlcodeDetail(account.getGlcode());
            vd.setGlcodeIdDetail(account.getId());

            final CFinancialYear financialYear1011 = (CFinancialYear) persistenceService
                    .find("from CFinancialYear  where finYearRange=? ",
                            "2010-11");

            vd.setFinancialYearId(financialYear1011.getId());
            billCreditDetailslist.add(vd);
        }
    }

    public List<ReceiptDetailInfo> createEmptySubLedgerlist() {
        final List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
        final ReceiptDetailInfo vd = new ReceiptDetailInfo();
        vd.setCreditAmountDetail(BigDecimal.ZERO);
        vd.setDebitAmountDetail(BigDecimal.ZERO);
        vd.setAmount(BigDecimal.ZERO);
        vd.setDetailCode(CollectionConstants.BLANK);
        vd.setDetailKey(CollectionConstants.BLANK);
        subLedgerlist.add(vd);
        return subLedgerlist;
    }

    public List<ReceiptDetailInfo> createSubLedgerlist(final String[] inputArray) throws Exception {
        final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                "from Accountdetailtype  where upper(name)=? ", inputArray[10].toUpperCase());
        final CChartOfAccounts account = (CChartOfAccounts) persistenceService.find(
                "from CChartOfAccounts  where glcode=? and isActiveForPosting=true", inputArray[8]);
        if (accountdetailtype != null && account != null) {
            final String table = accountdetailtype.getFullQualifiedName();
            final Class<?> service = Class.forName(table);
            String simpleName = service.getSimpleName();
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

            /*
             * ApplicationContext applicationContext = new ClassPathXmlApplicationContext( new String[] {
             * "classpath*:org/egov/infstr/beanfactory/globalApplicationContext.xml" });//To get the globalappContext
             * EntityTypeService entityService = (EntityTypeService) applicationContext .getBean(simpleName);
             */
            final EntityTypeService entityService = (EntityTypeService) beanProvider.getBean(simpleName);
            final List entityList = entityService.filterActiveEntities(inputArray[11], -1, accountdetailtype.getId());
            EntityType entityType = null;
            if (entityList.size() == 1)
                entityType = (EntityType) entityList.get(0);

            final ReceiptDetailInfo vd = new ReceiptDetailInfo();
            vd.setCreditAmountDetail(BigDecimal.ZERO);
            vd.setDebitAmountDetail(BigDecimal.ZERO);
            vd.setAmount(new BigDecimal(inputArray[12]));
            vd.setDetailCode(inputArray[11]);
            vd.setDetailKey(entityType.getName());
            vd.setDetailKeyId(entityType.getEntityId());
            vd.setDetailType(accountdetailtype);
            vd.setDetailTypeName(inputArray[10]);
            vd.setGlcode(account);
            vd.setSubledgerCode(inputArray[8]);
            subLedgerlist.add(vd);
        }
        return subLedgerlist;
    }

    @Transactional
    public String createVouchers() {
        errorReceiptList.clear();

        final List<ReceiptHeader> approvedReceipts = persistenceService.findAllBy(
                "from org.egov.collection.entity.ReceiptHeader where status.code=?",
                CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);

        final List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>(0);
        Set<ReceiptVoucher> receiptVouchers = new HashSet<ReceiptVoucher>(0);

        for (final ReceiptHeader receiptHeader : approvedReceipts) {
            voucherHeaderList.clear();
            receiptVouchers.clear();
            if (receiptHeader.getReceiptVoucher().isEmpty()) {

                try {
                    receiptHeaderService.createVoucherForReceipt(receiptHeader);
                    // If vouchers are created during work flow step, add them to
                    // the list

                    receiptVouchers = receiptHeader.getReceiptVoucher();
                    for (final ReceiptVoucher receiptVoucher : receiptVouchers)
                        voucherHeaderList.add(receiptVoucher.getVoucherheader());
                    final List<InstrumentHeader> receiptInstrList = new ArrayList<InstrumentHeader>();
                    receiptInstrList.addAll(receiptHeader.getReceiptInstrument());
                    if (voucherHeaderList != null && !receiptInstrList.isEmpty())
                        receiptHeaderService.updateInstrument(receiptHeader);

                } catch (final Exception e) {
                    errorReceiptList.add(receiptHeader.getId());
                    continue;
                }
                successNo++;
            }

        }
        Collections.sort(errorReceiptList);
        return "vouchercreationresult";
    }

    public List<Long> getErrorReceiptList() {
        return errorReceiptList;
    }

    public void setErrorReceiptList(final List<Long> errorReceiptList) {
        this.errorReceiptList = errorReceiptList;
    }

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(final File importFile) {
        this.importFile = importFile;
    }

    public String getImportContentType() {
        return importContentType;
    }

    public void setImportContentType(final String importContentType) {
        this.importContentType = importContentType;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(final String importFileName) {
        this.importFileName = importFileName;
    }

    public String getImportfileCaption() {
        return importfileCaption;
    }

    public void setImportfileCaption(final String importfileCaption) {
        this.importfileCaption = importfileCaption;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public Map<Integer, String> getErrorRowMap() {
        return errorRowMap;
    }

    public Integer getSuccessNo() {
        return successNo;
    }

    public void setSuccessNo(final Integer successNo) {
        this.successNo = successNo;
    }

    public void setTestMode(final boolean testMode) {
        this.testMode = testMode;
    }
}