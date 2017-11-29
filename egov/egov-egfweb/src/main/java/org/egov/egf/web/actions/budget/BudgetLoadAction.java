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
package org.egov.egf.web.actions.budget;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetUpload;
import org.egov.services.budget.BudgetDetailService;
import org.egov.utils.FinancialConstants;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = "upload", location = "budgetLoad-upload.jsp"),
        @Result(name = "result", location = "budgetLoad-result.jsp")
})
public class BudgetLoadAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BudgetLoadAction.class);
    private File budgetInXls;
    private String budgetInXlsFileName;
    private String budgetInXlsContentType;
    private static final int RE_YEAR_ROW_INDEX = 1;
    private static final int BE_YEAR_ROW_INDEX = 2;
    private static final int DATA_STARTING_ROW_INDEX = 4;
    private static final int FUNDCODE_CELL_INDEX = 0;
    private static final int DEPARTMENTCODE_CELL_INDEX = 1;
    private static final int FUNCTIONCODE_CELL_INDEX = 2;
    private static final int GLCODE_CELL_INDEX = 3;
    private static final int REAMOUNT_CELL_INDEX = 4;
    private static final int BEAMOUNT_CELL_INDEX = 5;
    private static final int PLANNINGPERCENTAGE_CELL_INDEX = 6;
    private boolean errorInMasterData = false;
    private MultipartFile[] originalFile = new MultipartFile[1];
    private MultipartFile[] outPutFile = new MultipartFile[1];
    private String originalFileStoreId, outPutFileStoreId;
    private List<FileStoreMapper> originalFiles = new ArrayList<FileStoreMapper>();
    private List<FileStoreMapper> outPutFiles = new ArrayList<FileStoreMapper>();
    private String budgetOriginalFileName;
    private String budgetOutPutFileName;
    private String timeStamp;
    private String budgetUploadError = "Upload the Budget Data as shown in the Download Template format";
    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private FundHibernateDAO fundDAO;

    @Autowired
    private FunctionDAO functionDAO;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;

    @Autowired
    protected FileStoreService fileStoreService;

    @Override
    @SuppressWarnings("unchecked")
    public void prepare()
    {

    }

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/budget/budgetLoad-beforeUpload")
    public String beforeUpload()
    {
        originalFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                "from FileStoreMapper where fileName like '%budget_original%' order by id desc ").setMaxResults(5).list();
        outPutFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                "from FileStoreMapper where fileName like '%budget_output%' order by id desc ").setMaxResults(5).list();
        return "upload";
    }

    @ValidationErrorPage("upload")
    @Action(value = "/budget/budgetLoad-upload")
    public String upload()
    {
        try {
            FileInputStream fsIP = new FileInputStream(budgetInXls);

            final POIFSFileSystem fs = new POIFSFileSystem(fsIP);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            final HSSFRow reRow = sheet.getRow(RE_YEAR_ROW_INDEX);
            final HSSFRow beRow = sheet.getRow(BE_YEAR_ROW_INDEX);
            String reFinYearRange = getStrValue(reRow.getCell(1));
            String beFinYearRange = getStrValue(beRow.getCell(1));
            CFinancialYear reFYear = financialYearDAO.getFinancialYearByFinYearRange(reFinYearRange);
            CFinancialYear beFYear = financialYearDAO.getNextFinancialYearByDate(reFYear.getStartingDate());

            if (!validateFinancialYears(reFYear, beFYear, beFinYearRange))
                throw new ValidationException(Arrays.asList(new ValidationError(
                        getText("be.year.is.not.immediate.next.fy.year.of.re.year"),
                        getText("be.year.is.not.immediate.next.fy.year.of.re.year"))));
            timeStamp = new Timestamp((new Date()).getTime()).toString().replace(".", "_");
            if (budgetInXlsFileName.contains("_budget_original_")) {
                budgetOriginalFileName = budgetInXlsFileName.split("_budget_original_")[0] + "_budget_original_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else if (budgetInXlsFileName.contains("_budget_output_")) {
                budgetOriginalFileName = budgetInXlsFileName.split("_budget_output_")[0] + "_budget_original_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else {
                if (budgetInXlsFileName.length() > 60) {
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            getText("file.name.should.be.less.then.60.characters"),
                            getText("file.name.should.be.less.then.60.characters"))));
                } else
                    budgetOriginalFileName = budgetInXlsFileName.split("\\.")[0] + "_budget_original_"
                            + timeStamp + "."
                            + budgetInXlsFileName.split("\\.")[1];
            }

            final FileStoreMapper originalFileStore = fileStoreService.store(budgetInXls,
                    budgetOriginalFileName,
                    budgetInXlsContentType, FinancialConstants.MODULE_NAME_APPCONFIG,false);

            persistenceService.persist(originalFileStore);
            originalFileStoreId = originalFileStore.getFileStoreId();

            List<BudgetUpload> budgetUploadList = loadToBudgetUpload(sheet);
            budgetUploadList = validateMasterData(budgetUploadList);
            budgetUploadList = validateDuplicateData(budgetUploadList);

            if (errorInMasterData) {
                fsIP.close();
                prepareOutPutFileWithErrors(budgetUploadList);
                addActionMessage(getText("error.while.validating.masterdata"));
                return "result";
            }

            budgetUploadList = removeEmptyRows(budgetUploadList);

            budgetUploadList = budgetDetailService.loadBudget(budgetUploadList, reFYear, beFYear);

            fsIP.close();
            prepareOutPutFileWithFinalStatus(budgetUploadList);

            addActionMessage(getText("budget.load.sucessful"));

        } catch (final ValidationException e)
        {
            originalFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                    "from FileStoreMapper where fileName like '%budget_original%' order by id desc ").setMaxResults(5).list();
            outPutFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                    "from FileStoreMapper where fileName like '%budget_output%' order by id desc ").setMaxResults(5).list();
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            originalFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                    "from FileStoreMapper where fileName like '%budget_original%' order by id desc ").setMaxResults(5).list();
            outPutFiles = (List<FileStoreMapper>) persistenceService.getSession().createQuery(
                    "from FileStoreMapper where fileName like '%budget_output%' order by id desc ").setMaxResults(5).list();
            throw new ValidationException(Arrays.asList(new ValidationError(budgetUploadError,
                    budgetUploadError)));

        }

        return "result";
    }

    private void prepareOutPutFileWithErrors(List<BudgetUpload> budgetUploadList) {
        FileInputStream fsIP;
        try {
            fsIP = new FileInputStream(budgetInXls);

            Map<String, String> errorsMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fsIP);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(3);
            HSSFCell cell = row.createCell(7);
            cell.setCellValue("Error Reason");

            for (BudgetUpload budget : budgetUploadList)
                errorsMap.put(budget.getFundCode() + "-" + budget.getFunctionCode() + "-" + budget.getDeptCode()
                        + "-"
                        + budget.getBudgetHead(), budget.getErrorReason());

            for (int i = DATA_STARTING_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
                HSSFRow errorRow = sheet.getRow(i);
                HSSFCell errorCell = errorRow.createCell(7);
                errorCell.setCellValue(errorsMap.get((getStrValue(sheet.getRow(i).getCell(FUNDCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(FUNCTIONCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(DEPARTMENTCODE_CELL_INDEX)) + "-" + getStrValue(sheet.getRow(i)
                        .getCell(GLCODE_CELL_INDEX)))));
            }

            FileOutputStream output_file = new FileOutputStream(budgetInXls);
            wb.write(output_file);
            output_file.close();
            if (budgetInXlsFileName.contains("_budget_original_")) {
                budgetOutPutFileName = budgetInXlsFileName.split("_budget_original_")[0] + "_budget_output_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else if (budgetInXlsFileName.contains("_budget_output_")) {
                budgetOutPutFileName = budgetInXlsFileName.split("_budget_output_")[0] + "_budget_output_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else {
                if (budgetInXlsFileName.length() > 60) {
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            getText("file.name.should.be.less.then.60.characters"),
                            getText("file.name.should.be.less.then.60.characters"))));
                } else
                    budgetOutPutFileName = budgetInXlsFileName.split("\\.")[0] + "_budget_output_"
                            + timeStamp + "."
                            + budgetInXlsFileName.split("\\.")[1];
            }
            final FileStoreMapper outPutFileStore = fileStoreService.store(budgetInXls,
                    budgetOutPutFileName,
                    budgetInXlsContentType, FinancialConstants.MODULE_NAME_APPCONFIG);

            persistenceService.persist(outPutFileStore);

            outPutFileStoreId = outPutFileStore.getFileStoreId();
        } catch (FileNotFoundException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        } catch (IOException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
    }

    private void prepareOutPutFileWithFinalStatus(List<BudgetUpload> budgetUploadList) {
        FileInputStream fsIP;
        try {
            fsIP = new FileInputStream(budgetInXls);

            Map<String, String> errorsMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fsIP);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            Map<String, String> finalStatusMap = new HashMap<String, String>();

            HSSFRow row = sheet.getRow(3);
            HSSFCell cell = row.createCell(7);
            cell.setCellValue("Status");

            for (BudgetUpload budget : budgetUploadList)
                finalStatusMap.put(budget.getFundCode() + "-" + budget.getFunctionCode() + "-" + budget.getDeptCode()
                        + "-"
                        + budget.getBudgetHead(), budget.getFinalStatus());

            for (int i = DATA_STARTING_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
                HSSFRow finalStatusRow = sheet.getRow(i);
                HSSFCell finalStatusCell = finalStatusRow.createCell(7);
                finalStatusCell.setCellValue(finalStatusMap.get((getStrValue(sheet.getRow(i).getCell(FUNDCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(FUNCTIONCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(DEPARTMENTCODE_CELL_INDEX)) + "-" + getStrValue(sheet.getRow(i)
                        .getCell(GLCODE_CELL_INDEX)))));
            }

            FileOutputStream output_file = new FileOutputStream(budgetInXls);
            wb.write(output_file);
            output_file.close();
            if (budgetInXlsFileName.contains("_budget_original_")) {
                budgetOutPutFileName = budgetInXlsFileName.split("_budget_original_")[0] + "_budget_output_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else if (budgetInXlsFileName.contains("_budget_output_")) {
                budgetOutPutFileName = budgetInXlsFileName.split("_budget_output_")[0] + "_budget_output_"
                        + timeStamp + "."
                        + budgetInXlsFileName.split("\\.")[1];
            } else {
                if (budgetInXlsFileName.length() > 60) {
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            getText("file.name.should.be.less.then.60.characters"),
                            getText("file.name.should.be.less.then.60.characters"))));
                } else
                    budgetOutPutFileName = budgetInXlsFileName.split("\\.")[0] + "_budget_output_"
                            + timeStamp + "."
                            + budgetInXlsFileName.split("\\.")[1];
            }
            final FileStoreMapper outPutFileStore = fileStoreService.store(budgetInXls,
                    budgetOutPutFileName,
                    budgetInXlsContentType, FinancialConstants.MODULE_NAME_APPCONFIG);
            persistenceService.persist(outPutFileStore);

            outPutFileStoreId = outPutFileStore.getFileStoreId();
        } catch (FileNotFoundException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        } catch (IOException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
    }

    private List<BudgetUpload> removeEmptyRows(List<BudgetUpload> budgetUploadList) {
        List<BudgetUpload> tempList = new ArrayList<>();
        for (BudgetUpload budget : budgetUploadList) {
            if (!budget.getErrorReason().equalsIgnoreCase("Empty Record"))
                tempList.add(budget);

        }
        return tempList;
    }

    private List<BudgetUpload> validateMasterData(List<BudgetUpload> budgetUploadList) {
        List<BudgetUpload> tempList = new ArrayList<>();
        try {
            String error = "";
            Map<String, Fund> fundMap = new HashMap<String, Fund>();
            Map<String, CFunction> functionMap = new HashMap<String, CFunction>();
            Map<String, Department> departmentMap = new HashMap<String, Department>();
            Map<String, CChartOfAccounts> coaMap = new HashMap<String, CChartOfAccounts>();
            List<Fund> fundList = fundDAO.findAllActiveIsLeafFunds();
            List<CFunction> functionList = functionDAO.getAllActiveFunctions();
            List<Department> departmentList = departmentService.getAllDepartments();
            List<CChartOfAccounts> coaList = chartOfAccountsService.findAll();
            for (Fund fund : fundList)
                fundMap.put(fund.getCode(), fund);
            for (CFunction function : functionList)
                functionMap.put(function.getCode(), function);
            for (Department department : departmentList)
                departmentMap.put(department.getCode(), department);
            for (CChartOfAccounts coa : coaList)
                coaMap.put(coa.getGlcode(), coa);

            for (BudgetUpload budget : budgetUploadList) {
                error = "";
                if (budget.getFundCode() != null && !budget.getFundCode().equalsIgnoreCase("")
                        && fundMap.get(budget.getFundCode()) == null)
                    error = error + getText("fund.is.not.exist") + budget.getFundCode();
                else
                    budget.setFund(fundMap.get(budget.getFundCode()));
                if (budget.getFunctionCode() != null && !budget.getFunctionCode().equalsIgnoreCase("")
                        && functionMap.get(budget.getFunctionCode()) == null)
                    error = error + " " + getText("function.is.not.exist") + budget.getFunctionCode();
                else
                    budget.setFunction(functionMap.get(budget.getFunctionCode()));

                if (budget.getDeptCode() != null && !budget.getFundCode().equalsIgnoreCase("")
                        && departmentMap.get(budget.getDeptCode()) == null)
                    error = error + " " + getText("department.is.not.exist") + budget.getDeptCode();
                else
                    budget.setDept(departmentMap.get(budget.getDeptCode()));

                if (budget.getBudgetHead() != null && !budget.getBudgetHead().equalsIgnoreCase("")
                        && coaMap.get(budget.getBudgetHead()) == null)
                    error = error + " " + getText("coa.is.not.exist") + budget.getBudgetHead();
                else
                    budget.setCoa(coaMap.get(budget.getBudgetHead()));
                budget.setErrorReason(error);
                if (!error.equalsIgnoreCase("")) {
                    errorInMasterData = true;
                }
                tempList.add(budget);
            }
        } catch (final ValidationException e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return tempList;
    }

    private List<BudgetUpload> validateDuplicateData(List<BudgetUpload> budgetUploadList) {
        List<BudgetUpload> tempList = new ArrayList<>();
        try {
            String error = "";
            Map<String, BudgetUpload> budgetUploadMap = new HashMap<String, BudgetUpload>();
            for (BudgetUpload budget : budgetUploadList) {
                if (budget.getFundCode() != null && budget.getFunctionCode() != null && budget.getDeptCode() != null
                        && budget.getBudgetHead() != null && !budget.getFundCode().equalsIgnoreCase("")
                        && !budget.getFunctionCode().equalsIgnoreCase("") && !budget.getDeptCode().equalsIgnoreCase("")
                        && !budget.getBudgetHead().equalsIgnoreCase(""))
                    if (budgetUploadMap.get(budget.getFundCode() + "-" + budget.getFunctionCode() + "-" + budget.getDeptCode()
                            + "-"
                            + budget.getBudgetHead()) == null)
                        budgetUploadMap.put(budget.getFundCode() + "-" + budget.getFunctionCode() + "-" + budget.getDeptCode()
                                + "-"
                                + budget.getBudgetHead(), budget);
                    else {
                        budget.setErrorReason(budget.getErrorReason() + getText("duplicate.record"));
                        errorInMasterData = true;
                    }
                else if (budget.getFundCode() == null && budget.getFunctionCode() == null && budget.getDeptCode() == null
                        && budget.getBudgetHead() == null) {
                    budget.setErrorReason(getText("empty.record"));
                }
                else {
                    budget.setErrorReason(getText("empty.record"));
                }

                tempList.add(budget);
            }
        } catch (final ValidationException e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return tempList;
    }

    private List<BudgetUpload> loadToBudgetUpload(HSSFSheet sheet) {
        List<BudgetUpload> budgetUploadList = new ArrayList<BudgetUpload>();
        try {

            for (int i = DATA_STARTING_ROW_INDEX; i <= sheet.getLastRowNum(); i++)
                budgetUploadList.add(getBudgetUpload(sheet.getRow(i)));
        } catch (final ValidationException e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return budgetUploadList;

    }

    private BudgetUpload getBudgetUpload(HSSFRow row) {
        BudgetUpload budget = new BudgetUpload();
        try {
            if (row != null) {
                budget.setFundCode(getStrValue(row.getCell(FUNDCODE_CELL_INDEX)) == null ? "" : getStrValue(row
                        .getCell(FUNDCODE_CELL_INDEX)));
                budget.setDeptCode(getStrValue(row.getCell(DEPARTMENTCODE_CELL_INDEX)) == null ? "" : getStrValue(row
                        .getCell(DEPARTMENTCODE_CELL_INDEX)));
                budget.setFunctionCode(getStrValue(row.getCell(FUNCTIONCODE_CELL_INDEX)) == null ? "" : getStrValue(row
                        .getCell(FUNCTIONCODE_CELL_INDEX)));
                budget.setBudgetHead(getStrValue(row.getCell(GLCODE_CELL_INDEX)) == null ? "" : getStrValue(row
                        .getCell(GLCODE_CELL_INDEX)));
                budget.setReAmount(BigDecimal.valueOf(Long.valueOf(getStrValue(row.getCell(REAMOUNT_CELL_INDEX)) == null ? "0"
                        : getStrValue(row.getCell(REAMOUNT_CELL_INDEX)))));
                budget.setBeAmount(BigDecimal.valueOf(Long.valueOf(getStrValue(row.getCell(BEAMOUNT_CELL_INDEX)) == null ? "0"
                        : getStrValue(row.getCell(BEAMOUNT_CELL_INDEX)))));
                budget.setPlanningPercentage(getNumericValue(row.getCell(PLANNINGPERCENTAGE_CELL_INDEX)) == null ? 0
                        : getNumericValue(row.getCell(PLANNINGPERCENTAGE_CELL_INDEX)).longValue());
            }
        } catch (final ValidationException e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return budget;
    }

    private boolean validateFinancialYears(CFinancialYear reFYear, CFinancialYear beFYear, String beYear) {
        try {

            if (reFYear == null)
                throw new ValidationException(Arrays.asList(new ValidationError(getText("re.year.is.not.exist"),
                        getText("re.year.is.not.exist"))));

            if (beFYear == null)
                throw new ValidationException(Arrays.asList(new ValidationError(getText("be.year.is.not.exist"),
                        getText("be.year.is.not.exist"))));
            if (beFYear.getFinYearRange().equalsIgnoreCase(beYear))
                return true;
            else
                return false;
        } catch (final ValidationException e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            throw new ValidationException(Arrays.asList(new ValidationError(getText("year.is.not.exist"),
                    getText("year.is.not.exist"))));
        }
    }

    @Override
    public void validate()
    {

    }

    private String getStrValue(final HSSFCell cell) {
        if (cell == null)
            return null;
        double numericCellValue = 0d;
        String strValue = "";
        switch (cell.getCellType())
        {
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

        switch (cell.getCellType())
        {
        case HSSFCell.CELL_TYPE_NUMERIC:
            numericCellValue = cell.getNumericCellValue();
            bigDecimalValue = BigDecimal.valueOf(numericCellValue);
            break;
        case HSSFCell.CELL_TYPE_STRING:
            strValue = cell.getStringCellValue();
            strValue = strValue.replaceAll("[^\\p{L}\\p{Nd}]", "");
            if (strValue != null && strValue.contains("E+"))
            {
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
            } catch (final Exception e)
            {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Found : Non numeric value in Numeric Field :" + strValue + ":");
            }
            break;
        }
        return bigDecimalValue;

    }

    public File getBudgetInXls() {
        return budgetInXls;
    }

    public void setBudgetInXls(File budgetInXls) {
        this.budgetInXls = budgetInXls;
    }

    public String getOriginalFileStoreId() {
        return originalFileStoreId;
    }

    public void setOriginalFileStoreId(String originalFileStoreId) {
        this.originalFileStoreId = originalFileStoreId;
    }

    public void setBudgetInXlsFileName(String budgetInXlsFileName) {
        this.budgetInXlsFileName = budgetInXlsFileName;
    }

    public void setBudgetInXlsContentType(String budgetInXlsContentType) {
        this.budgetInXlsContentType = budgetInXlsContentType;
    }

    public String getOutPutFileStoreId() {
        return outPutFileStoreId;
    }

    public void setOutPutFileStoreId(String outPutFileStoreId) {
        this.outPutFileStoreId = outPutFileStoreId;
    }

    public List<FileStoreMapper> getOriginalFiles() {
        return originalFiles;
    }

    public void setOriginalFiles(List<FileStoreMapper> originalFiles) {
        this.originalFiles = originalFiles;
    }

    public List<FileStoreMapper> getOutPutFiles() {
        return outPutFiles;
    }

    public void setOutPutFiles(List<FileStoreMapper> outPutFiles) {
        this.outPutFiles = outPutFiles;
    }

    public String getBudgetOriginalFileName() {
        return budgetOriginalFileName;
    }

    public void setBudgetOriginalFileName(String budgetOriginalFileName) {
        this.budgetOriginalFileName = budgetOriginalFileName;
    }

    public String getBudgetOutPutFileName() {
        return budgetOutPutFileName;
    }

    public void setBudgetOutPutFileName(String budgetOutPutFileName) {
        this.budgetOutPutFileName = budgetOutPutFileName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
