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
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.egov.works.masters.entity.ScheduleCategory;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.service.ScheduleCategoryService;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.masters.service.UploadSORService;
import org.egov.works.uploadsor.UploadSOR;
import org.egov.works.uploadsor.UploadScheduleOfRate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
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
@RequestMapping(value = "/uploadsor")
public class UploadSORController {
    private boolean errorInMasterData = false;
    private String originalFileStoreId, outPutFileStoreId;
    private String loadSorRateOriginalFileName;
    private File inputFile;
    private String timeStamp;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private UOMService uomService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    protected UploadSORService uploadSORService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/common-form", method = RequestMethod.GET)
    public String showCommonForm(@ModelAttribute("uploadSORRates") final UploadSOR uploadSOR,
            final Model model) throws ApplicationException {
        return "uploadSor";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String showForm(@ModelAttribute("uploadSORRates") final UploadSOR uploadSOR,
            final Model model) throws ApplicationException {
        model.addAttribute("originalFiles", worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY));
        model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_OUTPUT_FILE_NAME_KEY));
        return "uploadSor-form";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String create(@ModelAttribute("uploadSOR") final UploadSOR uploadSOR,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult errors)
            throws ApplicationException, IOException {

        try {
            errorInMasterData = false;
            final File convFile = new File(uploadSOR.getFile().getOriginalFilename());
            inputFile = convFile;
            uploadSOR.getFile().transferTo(convFile);
            final FileInputStream inputFile = new FileInputStream(convFile);
            final POIFSFileSystem fs = new POIFSFileSystem(inputFile);
            final HSSFWorkbook wb = new HSSFWorkbook(fs);
            wb.getNumberOfSheets();
            timeStamp = new Timestamp(new Date().getTime()).toString().replace(".", "_");
            final HSSFSheet sheet = wb.getSheetAt(0);

            List<UploadScheduleOfRate> uploadSORRatesList = uploadSORService.loadToList(sheet);

            validateDuplicateData(uploadSORRatesList);

            uploadSORRatesList = uploadSORService.removeEmptyRows(uploadSORRatesList);

            validateMandatoryFeilds(uploadSORRatesList);

            loadSorRateOriginalFileName = uploadSORService.prepareOriginalFileName(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY,
                    WorksConstants.SOR_OUTPUT_FILE_NAME_KEY, timeStamp, uploadSOR.getFile().getOriginalFilename(), errors);

            final FileStoreMapper originalFileStore = fileStoreService.store(uploadSOR.getFile().getInputStream(),
                    loadSorRateOriginalFileName, uploadSOR.getFile().getContentType(), WorksConstants.FILESTORE_MODULECODE);

            persistenceService.persist(originalFileStore);
            originalFileStoreId = originalFileStore.getFileStoreId();

            if (errorInMasterData) {
                inputFile.close();
                outPutFileStoreId = uploadSORService.prepareOutPutFileWithErrors(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY,
                        WorksConstants.SOR_OUTPUT_FILE_NAME_KEY, this.inputFile, uploadSORRatesList, uploadSOR, errors);
                errors.reject("error.while.validating.data", "error.while.validating.data");
                model.addAttribute("originalFileStoreId", originalFileStoreId);
                model.addAttribute("outPutFileStoreId", outPutFileStoreId);
                return "uploadSor-result";
            }

            uploadSORRatesList = scheduleOfRateService.createScheduleOfRate(uploadSORRatesList);

            inputFile.close();

            outPutFileStoreId = uploadSORService.prepareOutPutFileWithFinalStatus(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY,
                    WorksConstants.SOR_OUTPUT_FILE_NAME_KEY, this.inputFile, uploadSORRatesList, uploadSOR, errors);

            model.addAttribute("message", messageSource.getMessage("msg.load.sor.rates.sucessful", null, null));

        } catch (final ValidationException e) {
            model.addAttribute("originalFiles",
                    worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY));
            model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_OUTPUT_FILE_NAME_KEY));
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            model.addAttribute("originalFiles",
                    worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_ORIGINAL_FILE_NAME_KEY));
            model.addAttribute("outPutFiles", worksUtils.getLatestSorRateUploadFiles(WorksConstants.SOR_OUTPUT_FILE_NAME_KEY));
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));

        }
        model.addAttribute("originalFileStoreId", originalFileStoreId);
        model.addAttribute("outPutFileStoreId", outPutFileStoreId);
        return "uploadSor-result";
    }

    private void validateMandatoryFeilds(final List<UploadScheduleOfRate> uploadSORRatesList) {
        final List<UploadScheduleOfRate> tempList = new ArrayList<>();
        try {
            String error = "";
            final Map<String, ScheduleOfRate> sorMap = new HashMap<String, ScheduleOfRate>();
            final Map<String, ScheduleCategory> sorCategoryMap = new HashMap<String, ScheduleCategory>();
            final Map<String, UOM> uomMap = new HashMap<String, UOM>();
            final List<ScheduleOfRate> sorList = scheduleOfRateService.getAllScheduleOfRates();
            final List<ScheduleCategory> sorCategoryList = scheduleCategoryService.getAllScheduleCategories();
            final List<UOM> uomList = uomService.findAll();
            for (final ScheduleOfRate sor : sorList)
                sorMap.put(sor.getCode().toLowerCase(), sor);
            for (final ScheduleCategory scheduleCategory : sorCategoryList)
                sorCategoryMap.put(scheduleCategory.getCode().toLowerCase(), scheduleCategory);
            for (final UOM uom : uomList)
                uomMap.put(uom.getUom().toLowerCase(), uom);

            for (final UploadScheduleOfRate obj : uploadSORRatesList) {
                error = "";

                // Validating SOR code
                if (obj.getSorCode() != null && !obj.getSorCode().equalsIgnoreCase("")) {
                    if (sorMap.get(obj.getSorCode().toLowerCase()) == null) {
                        if (uploadSORService.isContainsWhitespace(obj.getSorCode()))
                            error = error + " "
                                    + messageSource.getMessage("error.whitespace.is.not.allowed.in.sorcode", null, null);
                    } else
                        obj.setScheduleOfRate(sorMap.get(obj.getSorCode().toLowerCase()));
                } else
                    error = error + " " + messageSource.getMessage("error.sorcode.is.required", null, null) + ",";

                // To-Do When ever there is a change in length of code in POJO we need to change this condition.
                if (obj.getSorCode() != null && obj.getSorCode().length() > 255)
                    error = error + " " + messageSource.getMessage("error.sor.code.length", null, null) + ",";

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

                if (uploadSORService.isSpecialCharacterExist(obj.getSorDescription())
                        || uploadSORService.isNewLineOrTabExist(obj.getSorDescription()))
                    error = error + " "
                            + messageSource.getMessage("error.special.characters.is.not.allowed.in.sor.description", null, null)
                            + ",";

                if (obj.getSorDescription() != null && obj.getSorDescription().length() > 4000)
                    error = error + " " + messageSource.getMessage("error.sor.description.length", null, null) + ",";

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
                else if (!obj.getRate().toString().matches("[0-9]+([,.][0-9]{1,2})?"))
                    error = error + " "
                            + messageSource.getMessage("error.more.then.two.decimal.places.not.allowed.rate", null, null)
                            + obj.getRate() + ",";

                // Validating from date
                if (obj.getFromDate() == null)
                    error = error + " " + messageSource.getMessage("error.fromdate.is.required", null, null) + ",";

                if (obj.getFromDate() != null && obj.getToDate() != null)
                    if (obj.getFromDate().compareTo(obj.getToDate()) > 0)
                        error = error + " " + messageSource.getMessage("error.fromdate.cannot.be.grater.then.todate", null, null)
                                + ",";

                /*
                 * // Validating market rate and from date if (obj.getMarketRate() != null &&
                 * (obj.getMarketRate().compareTo(BigDecimal.ZERO) == -1 || obj.getMarketRate().compareTo(BigDecimal.ZERO) == 0))
                 * error = error + " " + messageSource.getMessage("error.negative.values.not.allowed.in.market.rate", null, null)
                 * + obj.getMarketRate() + ","; if (obj.getMarketRate() != null && obj.getMarketFromDate() == null) error = error
                 * + " " + messageSource.getMessage("error.market.fromdate.is.required", null, null) + ","; if
                 * (obj.getMarketRate() != null && !obj.getMarketRate().toString().matches("[0-9]+([,.][0-9]{1,2})?")) error =
                 * error + " " + messageSource.getMessage("error.more.then.two.decimal.places.not.allowed.market.rate", null,
                 * null) + obj.getRate() + ","; if (obj.getMarketFromDate() != null && obj.getMarketRate() == null) error = error
                 * + " " + messageSource.getMessage("error.market.rate.is.required", null, null) + ","; if
                 * (obj.getMarketFromDate() != null && obj.getMarketToDate() != null) if
                 * (obj.getMarketFromDate().compareTo(obj.getMarketToDate()) > 0) error = error + " " + messageSource
                 * .getMessage("error.market.fromdate.cannot.be.grater.then.market.todate", null, null) + ",";
                 */

                // Validate duplicate (From Database)
                if (obj.getScheduleOfRate() != null
                        && obj.getScheduleCategory() != null)
                    if (obj.getScheduleOfRate().getScheduleCategory().getCode()
                            .equalsIgnoreCase(obj.getScheduleCategory().getCode()))
                        error = error + " " + messageSource
                                .getMessage("error.sorcode.already.exists", null, null) + ",";

                obj.setErrorReason(obj.getErrorReason() != null ? obj.getErrorReason() : "" + error);
                if (!error.equalsIgnoreCase(""))
                    errorInMasterData = true;
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

    private void validateDuplicateData(final List<UploadScheduleOfRate> uploadSORRatesList) {
        try {
            final Map<String, UploadScheduleOfRate> uploadSORRateMap = new HashMap<String, UploadScheduleOfRate>();
            for (final UploadScheduleOfRate obj : uploadSORRatesList)
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
                        && obj.getMarketFromDate() == null && obj.getMarketToDate() == null)
                    obj.setErrorReason(messageSource.getMessage("error.empty.record", null, null));
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
    }

}
