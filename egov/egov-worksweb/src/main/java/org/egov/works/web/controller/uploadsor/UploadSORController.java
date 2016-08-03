/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.works.web.controller.uploadsor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.egov.common.entity.UOM;
import org.egov.commons.service.UOMService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.master.service.ScheduleOfRateService;
import org.egov.works.models.masters.ScheduleCategory;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.uploadsor.UploadSOR;
import org.egov.works.uploadsor.UploadScheduleOfRate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/uploadsor/form")
public class UploadSORController {

    private static final Logger LOGGER = Logger.getLogger(UploadSORController.class);
    private static final int DATA_STARTING_ROW_INDEX = 1;
    private static final int SORCODE_CELL_INDEX = 0;
    private static final int SORCATEGORY_CELL_INDEX = 1;
    private static final int SOR_DESCRIPTION_CELL_INDEX = 2;
    private static final int UOM_CELL_INDEX = 3;
    private static final int RATE_CELL_INDEX = 4;
    private static final int FROMDATE_CELL_INDEX = 5;
    private static final int TODATE_CELL_INDEX = 6;
    private static final int MARKET_RATE_CELL_INDEX = 7;
    private static final int MARKET_RATE_FROMDATE_CELL_INDEX = 8;
    private static final int MARKET_RATE_TODATE_CELL_INDEX = 9;
    private boolean errorInMasterData = false;
    private String originalFileStoreId, outPutFileStoreId;
    private String loadSorRateOriginalFileName;
    private String loadSorRateOutPutFileName;
    private File inputFile;
    private String timeStamp;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private UOMService uomService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected FileStoreService fileStoreService;
    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public String showNewLineEstimateForm(@ModelAttribute("uploadSORRates") final UploadSOR uploadSOR,
            final Model model) throws ApplicationException {
        model.addAttribute("originalFiles", worksUtils.getLatestSorRateUploadOriginalFiles());
        model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadOutPutFiles());
        return "uploadSor";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute("uploadSOR") final UploadSOR uploadSOR,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult errors)
            throws ApplicationException, IOException {

        try {
            errorInMasterData = false;
            File convFile = new File(uploadSOR.getFile().getOriginalFilename());
            inputFile = convFile;
            uploadSOR.getFile().transferTo(convFile);
            FileInputStream inputFile = new FileInputStream(convFile);
            final POIFSFileSystem fs = new POIFSFileSystem(inputFile);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            timeStamp = new Timestamp((new Date()).getTime()).toString().replace(".", "_");
            final HSSFSheet sheet = wb.getSheetAt(0);

            List<UploadScheduleOfRate> uploadSORRatesList = loadToList(sheet);

            validateDuplicateData(uploadSORRatesList);

            uploadSORRatesList = removeEmptyRows(uploadSORRatesList);

            validateMandatoryFeilds(uploadSORRatesList);

            prepareOriginalFileName(uploadSOR.getFile().getOriginalFilename(), errors);

            final FileStoreMapper originalFileStore = fileStoreService.store(uploadSOR.getFile().getInputStream(),
                    loadSorRateOriginalFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

            persistenceService.persist(originalFileStore);
            originalFileStoreId = originalFileStore.getFileStoreId();

            if (errorInMasterData) {
                inputFile.close();
                prepareOutPutFileWithErrors(uploadSORRatesList, uploadSOR, errors);
                errors.reject("error.while.validating.data", "error.while.validating.data");
                model.addAttribute("originalFileStoreId", originalFileStoreId);
                model.addAttribute("outPutFileStoreId", outPutFileStoreId);
                return "uploadSor-result";
            }

            uploadSORRatesList = scheduleOfRateService.createScheduleOfRate(uploadSORRatesList);

            inputFile.close();

            prepareOutPutFileWithFinalStatus(uploadSORRatesList, uploadSOR, errors);

            model.addAttribute("message", messageSource.getMessage("msg.load.sor.rates.sucessful", null, null));

        } catch (final ValidationException e) {
            model.addAttribute("originalFiles", worksUtils.getLatestSorRateUploadOriginalFiles());
            model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadOutPutFiles());
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            model.addAttribute("originalFiles", worksUtils.getLatestSorRateUploadOriginalFiles());
            model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadOutPutFiles());
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));

        }
        model.addAttribute("originalFileStoreId", originalFileStoreId);
        model.addAttribute("outPutFileStoreId", outPutFileStoreId);
        return "uploadSor-result";
    }

    private void prepareOriginalFileName(String originalFilename, BindingResult errors) {
        if (originalFilename.contains("_sor_original_")) {
            loadSorRateOriginalFileName = originalFilename.split("_sor_original_")[0]
                    + "_sor_original_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        } else if (originalFilename.contains("_sor_output_")) {
            loadSorRateOriginalFileName = originalFilename.split("_sor_output_")[0]
                    + "_sor_original_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        } else {
            if (originalFilename.length() > 60) {
                errors.reject("error.file.name.should.be.less.then.60.characters",
                        "error.file.name.should.be.less.then.60.characters");
            } else
                loadSorRateOriginalFileName = originalFilename.split("\\.")[0]
                        + "_sor_original_"
                        + timeStamp + "."
                        + originalFilename.split("\\.")[1];
        }

    }

    private void prepareOutPutFileName(String originalFilename, BindingResult errors) {

        if (originalFilename.contains("_sor_original_")) {
            loadSorRateOutPutFileName = originalFilename.split("_sor_original_")[0]
                    + "_sor_output_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        } else if (originalFilename.contains("_sor_output_")) {
            loadSorRateOutPutFileName = originalFilename.split("_sor_output_")[0]
                    + "_sor_output_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        } else {
            if (originalFilename.length() > 60) {
                errors.reject("error.file.name.should.be.less.then.60.characters",
                        "error.file.name.should.be.less.then.60.characters");
            } else
                loadSorRateOutPutFileName = originalFilename.split("\\.")[0] + "_sor_output_"
                        + timeStamp + "."
                        + originalFilename.split("\\.")[1];
        }
    }

    private void validateMandatoryFeilds(List<UploadScheduleOfRate> uploadSORRatesList) {
        List<UploadScheduleOfRate> tempList = new ArrayList<>();
        try {
            String error = "";
            Map<String, ScheduleOfRate> sorMap = new HashMap<String, ScheduleOfRate>();
            Map<String, ScheduleCategory> sorCategoryMap = new HashMap<String, ScheduleCategory>();
            Map<String, UOM> uomMap = new HashMap<String, UOM>();
            List<ScheduleOfRate> sorList = scheduleOfRateService.getAllScheduleOfRates();
            List<ScheduleCategory> sorCategoryList = scheduleCategoryService.getAllScheduleCategories();
            List<UOM> uomList = uomService.findAll();
            for (ScheduleOfRate sor : sorList)
                sorMap.put(sor.getCode().toLowerCase(), sor);
            for (ScheduleCategory scheduleCategory : sorCategoryList)
                sorCategoryMap.put(scheduleCategory.getCode().toLowerCase(), scheduleCategory);
            for (UOM uom : uomList)
                uomMap.put(uom.getUom().toLowerCase(), uom);

            for (UploadScheduleOfRate obj : uploadSORRatesList) {
                error = "";

                // Validating SOR code
                if (obj.getSorCode() != null && !obj.getSorCode().equalsIgnoreCase("")) {
                    if (sorMap.get(obj.getSorCode().toLowerCase()) == null)
                        if (isContainsWhitespace(obj.getSorCode())) {
                            error = error + " "
                                    + messageSource.getMessage("error.whitespace.is.not.allowed.in.sorcode", null, null)
                                    + obj.getSorCode() + ",";
                        } else
                            obj.setCreateSor(true);
                    else {
                        obj.setScheduleOfRate(sorMap.get(obj.getSorCode().toLowerCase()));
                        obj.setCreateSor(false);
                    }
                } else {
                    error = error + " " + messageSource.getMessage("error.sorcode.is.required", null, null) + ",";

                }

                if (obj.getSorCode() != null && obj.getSorCode().length() > 255) {
                    error = error + " " + messageSource.getMessage("error.sor.code.length", null, null) + ",";
                }

                // Validating SOR Category Code
                if (obj.getSorCategoryCode() == null || obj.getSorCategoryCode().equalsIgnoreCase(""))
                    error = error + " " + messageSource.getMessage("error.schedulecategory.code.is.required", null, null) + ",";

                else if (obj.getSorCategoryCode() != null && !obj.getSorCategoryCode().equalsIgnoreCase("")
                        && sorCategoryMap.get(obj.getSorCategoryCode().toLowerCase()) == null)
                    error = error + " " + messageSource.getMessage("error.schedulecategory.is.not.exist", null, null)
                            + obj.getSorCategoryCode() + ",";
                else

                    obj.setScheduleCategory(sorCategoryMap.get(obj.getSorCategoryCode().toLowerCase()));

                // Validating SOR description
                if (obj.getSorDescription() == null || obj.getSorDescription().equalsIgnoreCase(""))
                    error = error + " " + messageSource.getMessage("error.sordescription.is.required", null, null) + ",";

                if (isSpecialCharacterExist(obj.getSorDescription()) || isNewLineOrTabExist(obj.getSorDescription()))
                    error = error + " "
                            + messageSource.getMessage("error.special.characters.is.not.allowed.in.sor.description", null, null)
                            + ",";

                if (obj.getSorDescription() != null && obj.getSorDescription().length() > 4000) {
                    error = error + " " + messageSource.getMessage("error.sor.description.length", null, null) + ",";
                }

                // Validating uom code
                if (obj.getUomCode() == null || obj.getUomCode().equalsIgnoreCase(""))
                    error = error + " " + messageSource.getMessage("error.uom.is.required", null, null) + ",";

                else if (obj.getUomCode() != null && !obj.getUomCode().equalsIgnoreCase("")
                        && uomMap.get(obj.getUomCode().toLowerCase()) == null)
                    error = error + " " + messageSource.getMessage("error.uom.is.not.exist", null, null)
                            + obj.getUomCode() + ",";
                else
                    obj.setUom(uomMap.get(obj.getUomCode().toLowerCase()));

                // Validating rate
                if (obj.getRate() == null)
                    error = error + " " + messageSource.getMessage("error.rate.is.required", null, null) + ",";
                else if (obj.getRate().compareTo(BigDecimal.ZERO) == -1 || obj.getRate().compareTo(BigDecimal.ZERO) == 0)
                    error = error + " " + messageSource.getMessage("error.negative.values.not.allowed.in.rate", null, null)
                            + obj.getRate() + ",";
                else if (!(obj.getRate().toString().matches("[0-9]+([,.][0-9]{1,2})?")))
                    error = error + " "
                            + messageSource.getMessage("error.more.then.two.decimal.places.not.allowed.rate", null, null)
                            + obj.getRate() + ",";

                // Validating from date
                if (obj.getFromDate() == null)
                    error = error + " " + messageSource.getMessage("error.fromdate.is.required", null, null) + ",";

                if (obj.getFromDate() != null && obj.getToDate() != null) {
                    if (obj.getFromDate().compareTo(obj.getToDate()) > 0)
                        error = error + " " + messageSource.getMessage("error.fromdate.cannot.be.grater.then.todate", null, null)
                                + ",";
                }

                // Validating market rate and from date
                if (obj.getMarketRate() != null && (obj.getMarketRate().compareTo(BigDecimal.ZERO) == -1
                        || obj.getMarketRate().compareTo(BigDecimal.ZERO) == 0))
                    error = error + " " + messageSource.getMessage("error.negative.values.not.allowed.in.market.rate", null, null)
                            + obj.getMarketRate() + ",";

                if (obj.getMarketRate() != null && obj.getMarketFromDate() == null) {
                    error = error + " " + messageSource.getMessage("error.market.fromdate.is.required", null, null) + ",";
                }
                if (obj.getMarketRate() != null && !(obj.getMarketRate().toString().matches("[0-9]+([,.][0-9]{1,2})?")))
                    error = error + " "
                            + messageSource.getMessage("error.more.then.two.decimal.places.not.allowed.market.rate", null, null)
                            + obj.getRate() + ",";

                if (obj.getMarketFromDate() != null && obj.getMarketRate() == null) {
                    error = error + " " + messageSource.getMessage("error.market.rate.is.required", null, null) + ",";
                }

                if (obj.getMarketFromDate() != null && obj.getMarketToDate() != null) {
                    if (obj.getMarketFromDate().compareTo(obj.getMarketToDate()) > 0)
                        error = error + " " + messageSource
                                .getMessage("error.market.fromdate.cannot.be.grater.then.market.todate", null, null) + ",";
                }

                // Validate duplicate (From Database)
                if (obj.getCreateSor() != null && !obj.getCreateSor() && obj.getScheduleOfRate() != null
                        && obj.getScheduleCategory() != null) {
                    if (obj.getScheduleOfRate().getScheduleCategory().getCode()
                            .equalsIgnoreCase(obj.getScheduleCategory().getCode()))
                        error = error + " " + messageSource
                                .getMessage("error.sorcode.already.exists", null, null) + ",";
                }

                obj.setErrorReason(obj.getErrorReason() != null ? obj.getErrorReason() : "" + error);
                if (!error.equalsIgnoreCase("")) {
                    errorInMasterData = true;
                }
                tempList.add(obj);
            }

        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
    }

    private void validateDuplicateData(List<UploadScheduleOfRate> uploadSORRatesList) {
        try {
            Map<String, UploadScheduleOfRate> uploadSORRateMap = new HashMap<String, UploadScheduleOfRate>();
            for (UploadScheduleOfRate obj : uploadSORRatesList) {
                if (obj.getSorCode() != null && obj.getSorCategoryCode() != null && !obj.getSorCode().equalsIgnoreCase("")
                        && !obj.getSorCategoryCode().equalsIgnoreCase(""))
                    if (uploadSORRateMap.get(obj.getSorCode() + "-" + obj.getSorCategoryCode()) == null)
                        uploadSORRateMap.put(obj.getSorCode() + "-" + obj.getSorCategoryCode(), obj);
                    else {
                        obj.setErrorReason(obj.getErrorReason() != null ? obj.getErrorReason()
                                : "" + messageSource.getMessage("error.duplicate.record", null, null));
                        errorInMasterData = true;
                    }
                else if ((obj.getSorCode() == null || obj.getSorCode().equalsIgnoreCase(""))
                        && (obj.getSorCategoryCode() == null || obj.getSorCategoryCode().equalsIgnoreCase(""))
                        && (obj.getSorDescription() == null || obj.getSorDescription().equalsIgnoreCase(""))
                        && (obj.getUomCode() == null || obj.getUomCode().equalsIgnoreCase("")) && obj.getRate() == null
                        && obj.getFromDate() == null && obj.getToDate() == null && obj.getMarketRate() == null
                        && obj.getMarketFromDate() == null && obj.getMarketToDate() == null) {
                    obj.setErrorReason(messageSource.getMessage("error.empty.record", null, null));
                }
            }
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
    }

    private List<UploadScheduleOfRate> loadToList(HSSFSheet sheet) {
        List<UploadScheduleOfRate> uploadSORRatesList = new ArrayList<UploadScheduleOfRate>();
        try {

            for (int i = DATA_STARTING_ROW_INDEX; i <= sheet.getLastRowNum(); i++)
                uploadSORRatesList.add(getRowData(sheet.getRow(i)));
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return uploadSORRatesList;

    }

    private UploadScheduleOfRate getRowData(HSSFRow row) {
        UploadScheduleOfRate sorRate = new UploadScheduleOfRate();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (row != null) {
                sorRate.setSorCode(getStrValue(row.getCell(SORCODE_CELL_INDEX)) == null ? null : getStrValue(row
                        .getCell(SORCODE_CELL_INDEX)));
                sorRate.setSorCategoryCode(getStrValue(row.getCell(SORCATEGORY_CELL_INDEX)) == null ? null : getStrValue(row
                        .getCell(SORCATEGORY_CELL_INDEX)));
                sorRate.setSorDescription(getStrValue(row.getCell(SOR_DESCRIPTION_CELL_INDEX)) == null ? null : getStrValue(row
                        .getCell(SOR_DESCRIPTION_CELL_INDEX)));
                sorRate.setUomCode(getStrValue(row.getCell(UOM_CELL_INDEX)) == null ? null : getStrValue(row
                        .getCell(UOM_CELL_INDEX)));
                sorRate.setRate(
                        getNumericValue(row.getCell(RATE_CELL_INDEX)).compareTo(BigDecimal.ZERO) == 0 ? null : getNumericValue(row
                                .getCell(RATE_CELL_INDEX)));
                try {
                    sorRate.setTempFromDate(
                            row.getCell(FROMDATE_CELL_INDEX) == null ? null
                                    : row.getCell(FROMDATE_CELL_INDEX).getStringCellValue());

                    if (sorRate.getTempFromDate() != null && !sorRate.getTempFromDate().equalsIgnoreCase(""))
                        sorRate.setFromDate(df.parse(sorRate.getTempFromDate()));
                    else
                        sorRate.setErrorReason(sorRate.getErrorReason() != null ? sorRate.getErrorReason() + ","
                                : "" + messageSource.getMessage("error.fromdate.invalid", null, null));
                } catch (final Exception e) {
                    sorRate.setErrorReason(
                            sorRate.getErrorReason() != null ? sorRate.getErrorReason() + ","
                                    : "" + messageSource.getMessage("error.fromdate.invalid", null, null));
                }

                try {
                    sorRate.setTempToDate(
                            row.getCell(TODATE_CELL_INDEX) == null ? null : getStrValue(row.getCell(TODATE_CELL_INDEX)));
                    if (sorRate.getTempToDate() != null && !sorRate.getTempToDate().equalsIgnoreCase(""))
                        sorRate.setToDate(df.parse(sorRate.getTempToDate()));
                } catch (final Exception e) {
                    sorRate.setErrorReason(
                            sorRate.getErrorReason() != null ? sorRate.getErrorReason() + ","
                                    : "" + messageSource.getMessage("error.todate.invalid", null, null));
                }
                /*
                 * sorRate.setMarketRate( getNumericValue(row.getCell(MARKET_RATE_CELL_INDEX)).compareTo(BigDecimal.ZERO) == 0 ?
                 * null : getNumericValue(row .getCell(MARKET_RATE_CELL_INDEX))); try {
                 * sorRate.setMarketFromDate(row.getCell(MARKET_RATE_FROMDATE_CELL_INDEX) == null ? null :
                 * row.getCell(MARKET_RATE_FROMDATE_CELL_INDEX).getDateCellValue()); } catch (final Exception e) {
                 * sorRate.setErrorReason(messageSource.getMessage("error.market.fromdate.invalid", null, null)); } try {
                 * sorRate.setMarketToDate(row.getCell(MARKET_RATE_TODATE_CELL_INDEX) == null ? null :
                 * row.getCell(MARKET_RATE_TODATE_CELL_INDEX).getDateCellValue()); } catch (final Exception e) {
                 * sorRate.setErrorReason(messageSource.getMessage("error.market.todate.invalid", null, null)); }
                 */
            }
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return sorRate;
    }

    private void prepareOutPutFileWithErrors(List<UploadScheduleOfRate> uploadSORRatesList, UploadSOR uploadSOR,
            BindingResult errors) {
        try {
            FileInputStream fIS = new FileInputStream(inputFile);

            Map<String, String> errorsMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fIS);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.createCell(7);
            cell.setCellValue("Error Reason");

            for (UploadScheduleOfRate obj : uploadSORRatesList)
                errorsMap.put(obj.getSorCode() + "-" + obj.getSorCategoryCode() + "-" + obj.getSorDescription() + "-"
                        + obj.getUomCode() + "-" + obj.getRate(), obj.getErrorReason());

            for (int i = DATA_STARTING_ROW_INDEX; i <= uploadSORRatesList.size(); i++) {
                HSSFRow errorRow = sheet.getRow(i);
                HSSFCell errorCell = errorRow.createCell(7);
                errorCell.setCellValue(errorsMap.get((getStrValue(sheet.getRow(i).getCell(SORCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SORCATEGORY_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SOR_DESCRIPTION_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(UOM_CELL_INDEX)) + "-"
                        + (getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX)).compareTo(BigDecimal.ZERO) == 0 ? null
                                : getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX))))));
            }

            FileOutputStream output_file = new FileOutputStream(inputFile);
            wb.write(output_file);
            output_file.close();

            prepareOutPutFileName(uploadSOR.getFile().getOriginalFilename(), errors);

            final FileStoreMapper outPutFileStore = fileStoreService.store(inputFile,
                    loadSorRateOutPutFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

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

    private void prepareOutPutFileWithFinalStatus(List<UploadScheduleOfRate> uploadSORRatesList, UploadSOR uploadSOR,
            BindingResult errors) {
        try {
            FileInputStream fIS = new FileInputStream(inputFile);

            Map<String, String> finalStatusMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fIS);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.createCell(7);
            cell.setCellValue("Status");

            for (UploadScheduleOfRate obj : uploadSORRatesList)
                finalStatusMap.put(obj.getSorCode() + "-" + obj.getSorCategoryCode() + "-" + obj.getSorDescription() + "-"
                        + obj.getUomCode() + "-" + obj.getRate(), obj.getFinalStatus());

            for (int i = DATA_STARTING_ROW_INDEX; i <= uploadSORRatesList.size(); i++) {
                HSSFRow errorRow = sheet.getRow(i);
                HSSFCell errorCell = errorRow.createCell(7);
                errorCell.setCellValue(finalStatusMap.get((getStrValue(sheet.getRow(i).getCell(SORCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SORCATEGORY_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SOR_DESCRIPTION_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(UOM_CELL_INDEX)) + "-"
                        + getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX)))));
            }

            FileOutputStream output_file = new FileOutputStream(inputFile);
            wb.write(output_file);
            output_file.close();

            prepareOutPutFileName(uploadSOR.getFile().getOriginalFilename(), errors);

            final FileStoreMapper outPutFileStore = fileStoreService.store(inputFile,
                    loadSorRateOutPutFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

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

    private List<UploadScheduleOfRate> removeEmptyRows(List<UploadScheduleOfRate> uploadSORRatesList) {
        List<UploadScheduleOfRate> tempList = new ArrayList<>();
        for (UploadScheduleOfRate obj : uploadSORRatesList) {
            if (obj.getErrorReason() != null && obj.getErrorReason().equalsIgnoreCase("Empty Record"))
                continue;
            else
                tempList.add(obj);

        }
        return tempList;
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
            // strValue = strValue.replaceAll("[^\\p{L}\\p{Nd}]", "");
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

    private Boolean isContainsWhitespace(String name) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    private boolean isSpecialCharacterExist(String name) {
        if (name.indexOf('\\') != -1 || name.indexOf('\'') != -1)
            return true;
        else
            return false;

    }

    private Boolean isNewLineOrTabExist(String name) {
        Pattern pattern = Pattern.compile("\\n");
        Matcher matcher = pattern.matcher(name);

        Pattern pattern1 = Pattern.compile("\\t");
        Matcher matcher1 = pattern1.matcher(name);

        return matcher.find() || matcher1.find();
    }

}
