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

package org.egov.works.masters.service;

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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.uploadsor.UploadSOR;
import org.egov.works.uploadsor.UploadScheduleOfRate;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class UploadSORService {

    @PersistenceContext
    private EntityManager entityManager;
    private static final int DATA_STARTING_ROW_INDEX = 1;
    private static final int SORCODE_CELL_INDEX = 0;
    private static final int SORCATEGORY_CELL_INDEX = 1;
    private static final int SOR_DESCRIPTION_CELL_INDEX = 2;
    private static final int UOM_CELL_INDEX = 3;
    private static final int RATE_CELL_INDEX = 4;
    private static final int FROMDATE_CELL_INDEX = 5;
    private static final int TODATE_CELL_INDEX = 6;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    public List<UploadScheduleOfRate> loadToList(final HSSFSheet sheet) {
        final List<UploadScheduleOfRate> uploadSORRatesList = new ArrayList<UploadScheduleOfRate>();
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

    public UploadScheduleOfRate getRowData(final HSSFRow row) {
        final UploadScheduleOfRate sorRate = new UploadScheduleOfRate();
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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

    public String getStrValue(final HSSFCell cell) {
        if (cell == null)
            return null;
        double numericCellValue = 0d;
        String strValue = "";
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:
            numericCellValue = cell.getNumericCellValue();
            final DecimalFormat decimalFormat = new DecimalFormat("#");
            strValue = decimalFormat.format(numericCellValue);
            break;
        case Cell.CELL_TYPE_STRING:
            strValue = cell.getStringCellValue();
            break;
        }
        return strValue;

    }

    public BigDecimal getNumericValue(final HSSFCell cell) {
        if (cell == null)
            return null;
        double numericCellValue = 0d;
        BigDecimal bigDecimalValue = BigDecimal.ZERO;
        String strValue = "";

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:
            numericCellValue = cell.getNumericCellValue();
            bigDecimalValue = BigDecimal.valueOf(numericCellValue);
            break;
        case Cell.CELL_TYPE_STRING:
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
            }
            break;
        }
        return bigDecimalValue;

    }

    @Transactional
    public String prepareOutPutFileWithErrors(final String originalNameKey, final String outputNameKey, final File inputFile,
            final List<UploadScheduleOfRate> uploadSORRatesList,
            final UploadSOR uploadSOR,
            final BindingResult errors) {
        String outPutFileStoreId;
        try {
            final FileInputStream fIS = new FileInputStream(inputFile);

            final String timeStamp = new Timestamp(new Date().getTime()).toString().replace(".", "_");

            final Map<String, String> errorsMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fIS);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            final HSSFRow row = sheet.getRow(0);
            final HSSFCell cell = row.createCell(7);
            cell.setCellValue("Error Reason");

            for (final UploadScheduleOfRate obj : uploadSORRatesList)
                errorsMap.put(obj.getSorCode() + "-" + obj.getSorCategoryCode() + "-" + obj.getSorDescription() + "-"
                        + obj.getUomCode() + "-" + obj.getRate(), obj.getErrorReason());

            for (int i = DATA_STARTING_ROW_INDEX; i <= uploadSORRatesList.size(); i++) {
                final HSSFRow errorRow = sheet.getRow(i);
                final HSSFCell errorCell = errorRow.createCell(7);
                errorCell.setCellValue(errorsMap.get(getStrValue(sheet.getRow(i).getCell(SORCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SORCATEGORY_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SOR_DESCRIPTION_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(UOM_CELL_INDEX)) + "-"
                        + (getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX)).compareTo(BigDecimal.ZERO) == 0 ? null
                                : getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX)))));
            }

            final FileOutputStream output_file = new FileOutputStream(inputFile);
            wb.write(output_file);
            output_file.close();

            final String loadSorRateOutPutFileName = prepareOutPutFileName(originalNameKey, outputNameKey, timeStamp,
                    uploadSOR.getFile().getOriginalFilename(),
                    errors);

            final FileStoreMapper outPutFileStore = fileStoreService.store(inputFile,
                    loadSorRateOutPutFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

            persistenceService.persist(outPutFileStore);

            outPutFileStoreId = outPutFileStore.getFileStoreId();
        } catch (final FileNotFoundException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        } catch (final IOException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }

        return outPutFileStoreId;
    }

    @Transactional
    public String prepareOutPutFileWithFinalStatus(final String originalNameKey, final String outputNameKey, final File inputFile,
            final List<UploadScheduleOfRate> uploadSORRatesList,
            final UploadSOR uploadSOR,
            final BindingResult errors) {
        String outPutFileStoreId;
        final String timeStamp = new Timestamp(new Date().getTime()).toString().replace(".", "_");
        try {
            final FileInputStream fIS = new FileInputStream(inputFile);

            final Map<String, String> finalStatusMap = new HashMap<String, String>();
            final POIFSFileSystem fs = new POIFSFileSystem(fIS);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            final HSSFSheet sheet = wb.getSheetAt(0);
            final HSSFRow row = sheet.getRow(0);
            final HSSFCell cell = row.createCell(7);
            cell.setCellValue("Status");

            for (final UploadScheduleOfRate obj : uploadSORRatesList)
                finalStatusMap.put(obj.getSorCode() + "-" + obj.getSorCategoryCode() + "-" + obj.getSorDescription() + "-"
                        + obj.getUomCode() + "-" + obj.getRate(), obj.getFinalStatus());

            for (int i = DATA_STARTING_ROW_INDEX; i <= uploadSORRatesList.size(); i++) {
                final HSSFRow errorRow = sheet.getRow(i);
                final HSSFCell errorCell = errorRow.createCell(7);
                errorCell.setCellValue(finalStatusMap.get(getStrValue(sheet.getRow(i).getCell(SORCODE_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SORCATEGORY_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(SOR_DESCRIPTION_CELL_INDEX)) + "-"
                        + getStrValue(sheet.getRow(i).getCell(UOM_CELL_INDEX)) + "-"
                        + getNumericValue(sheet.getRow(i).getCell(RATE_CELL_INDEX))));
            }

            final FileOutputStream output_file = new FileOutputStream(inputFile);
            wb.write(output_file);
            output_file.close();

            final String loadSorRateOutPutFileName = prepareOutPutFileName(originalNameKey, outputNameKey, timeStamp,
                    uploadSOR.getFile().getOriginalFilename(),
                    errors);

            final FileStoreMapper outPutFileStore = fileStoreService.store(inputFile,
                    loadSorRateOutPutFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

            persistenceService.persist(outPutFileStore);

            outPutFileStoreId = outPutFileStore.getFileStoreId();
        } catch (final FileNotFoundException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        } catch (final IOException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }

        return outPutFileStoreId;
    }

    public List<UploadScheduleOfRate> removeEmptyRows(final List<UploadScheduleOfRate> uploadSORRatesList) {
        final List<UploadScheduleOfRate> tempList = new ArrayList<>();
        for (final UploadScheduleOfRate obj : uploadSORRatesList)
            if (obj.getErrorReason() != null && obj.getErrorReason().equalsIgnoreCase("Empty Record"))
                continue;
            else
                tempList.add(obj);
        return tempList;
    }

    public String prepareOriginalFileName(final String originalNameKey, final String outputNameKey, final String timeStamp,
            final String originalFilename, final BindingResult errors) {
        String loadSorRateOriginalFileName = "";
        if (originalFilename.contains(originalNameKey))
            loadSorRateOriginalFileName = originalFilename.split("_" + originalNameKey + "_")[0]
                    + "_" + originalNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        else if (originalFilename.contains("_" + outputNameKey + "_"))
            loadSorRateOriginalFileName = originalFilename.split("_" + outputNameKey + "_")[0]
                    + "_" + originalNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        else if (originalFilename.length() > 60)
            errors.reject("error.file.name.should.be.less.then.60.characters",
                    "error.file.name.should.be.less.then.60.characters");
        else
            loadSorRateOriginalFileName = originalFilename.split("\\.")[0]
                    + "_" + originalNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];

        return loadSorRateOriginalFileName;
    }

    public String prepareOutPutFileName(final String originalNameKey, final String outputNameKey, final String timeStamp,
            final String originalFilename, final BindingResult errors) {
        String loadSorRateOutPutFileName = "";
        if (originalFilename.contains(originalNameKey))
            loadSorRateOutPutFileName = originalFilename.split("_" + originalNameKey + "_")[0]
                    + "_" + outputNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        else if (originalFilename.contains("_" + outputNameKey + "_"))
            loadSorRateOutPutFileName = originalFilename.split("_" + outputNameKey + "_")[0]
                    + "_" + outputNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];
        else if (originalFilename.length() > 60)
            errors.reject("error.file.name.should.be.less.then.60.characters",
                    "error.file.name.should.be.less.then.60.characters");
        else
            loadSorRateOutPutFileName = originalFilename.split("\\.")[0] + "_" + outputNameKey + "_"
                    + timeStamp + "."
                    + originalFilename.split("\\.")[1];

        return loadSorRateOutPutFileName;
    }

    public Boolean isContainsWhitespace(final String name) {
        final Pattern pattern = Pattern.compile("\\s");
        final Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    public boolean isSpecialCharacterExist(final String name) {
        if (name.indexOf('\\') != -1 || name.indexOf('\'') != -1)
            return true;
        else
            return false;

    }

    public Boolean isNewLineOrTabExist(final String name) {
        final Pattern pattern = Pattern.compile("\\n");
        final Matcher matcher = pattern.matcher(name);

        final Pattern pattern1 = Pattern.compile("\\t");
        final Matcher matcher1 = pattern1.matcher(name);

        return matcher.find() || matcher1.find();
    }

}
