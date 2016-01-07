package org.egov.tl.web.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
import org.egov.tl.domain.entity.FeeType;
import org.egov.tl.domain.entity.LicenseAppType;
import org.egov.tl.domain.entity.LicenseCategory;
import org.egov.tl.domain.entity.NatureOfBusiness;
import org.egov.tl.domain.entity.UnitOfMeasurement;
import org.egov.tl.domain.service.FeeMatrixService;
import org.egov.tl.domain.service.FeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/feematrix/")
public class FeeMatrixController {
	private final static String FEEMATRIX_NEW = "feematrix-new";
	private final static String FEEMATRIX_RESULT = "feematrix-result";
	private final static String FEEMATRIX_EDIT = "feematrix-edit";
	private final static String FEEMATRIX_VIEW = "feematrix-view";
	@Autowired
	private FeeMatrixService feeMatrixService;
	@Autowired
	@Qualifier("persistenceService")
	public PersistenceService persistenceService;
	@Autowired
	private FinancialYearDAO financialYearDAO;

	@Autowired
	private FeeTypeService feeTypeService;

	@Autowired
	private AppConfigValueService appConfigValueService;
	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	private void prepareForNewForm(Model model, String fee) {

		List<UnitOfMeasurement> uomList = new ArrayList<UnitOfMeasurement>();
		List<FeeType> feeTypeList = new ArrayList<FeeType>();
		model.addAttribute(
				"licenseCategorys",
				persistenceService
						.findAllBy("select  c from LicenseCategory c order by name asc"));
		model.addAttribute(
				"natureOfBusinesss",
				persistenceService
						.findAllBy("select n from org.egov.tl.domain.entity.NatureOfBusiness n order by name asc"));
		model.addAttribute("financialYears",
				financialYearDAO
						.getAllActiveFinancialYearList());

		List<AppConfigValues> permTempAppconfigList = appConfigValueService
				.getConfigValuesByModuleAndKey("Trade License",
						"Is Fee For Permanent and Temporary Same");
		if (permTempAppconfigList.get(0).getValue().equals("Y")) {
			model.addAttribute("hideTemporary", true);

		}

		List<AppConfigValues> newRenewAppconfigList = appConfigValueService
				.getConfigValuesByModuleAndKey("Trade License",
						"Is Fee For New and Renew Same");
		if (newRenewAppconfigList.get(0).getValue().equals("Y")) {
			model.addAttribute("hideRenew", true);

		}
		model.addAttribute("feeMatrix", new FeeMatrix());
		model.addAttribute("subCategorys", Collections.EMPTY_LIST);
		model.addAttribute(
				"licenseAppTypes",
				persistenceService
						.findAllBy("select a from LicenseAppType a order by name asc"));
		if (fee != null) {
			feeTypeList.add(feeTypeService.findByName(fee));
			model.addAttribute("feeTypes", feeTypeList);
			switch (fee) {
			case "License Fee":
				uomList.addAll(persistenceService
						.findAllBy("select u from UnitOfMeasurement  u order by name asc"));
				model.addAttribute("unitOfMeasurements", uomList);
				break;
			case "Motor Fee":
				uomList.add((UnitOfMeasurement) persistenceService
						.find("select u from UnitOfMeasurement u   where u.name='HP'"));
				model.addAttribute("unitOfMeasurements", uomList);
				break;
			case "Workforce Fee":
				uomList.add((UnitOfMeasurement) persistenceService
						.find("select u from UnitOfMeasurement u   where u.name='Person'"));
				model.addAttribute("unitOfMeasurements", uomList);
				break;
			}
		} else {

			model.addAttribute("feeTypes", feeTypeService.findAll());
			model.addAttribute(
					"unitOfMeasurements",
					persistenceService
							.findAllBy("select u from UnitOfMeasurement  u order by name asc"));
		}

	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String newForm(final Model model,
			@RequestParam(required = false) String fee) {
		prepareForNewForm(model, fee);
		return FEEMATRIX_NEW;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute FeeMatrix feeMatrix,
			final BindingResult errors, final Model model,
			HttpServletRequest request) {
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		feeMatrixService.create(feeMatrix);
		model.addAttribute("feeMatrix", feeMatrix);
		return FEEMATRIX_RESULT;
	}

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String search(@ModelAttribute final FeeMatrix feeMatrix,
			final BindingResult errors, final Model model) {
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		FeeMatrix searchfeeMatrix = feeMatrixService.search(feeMatrix);
		if (searchfeeMatrix == null) {
			searchfeeMatrix = new FeeMatrix();

		}
		for (FeeMatrixDetail fd : searchfeeMatrix.getFeeMatrixDetail()) {
			LOGGER.debug(fd.getUomFrom());
		}
		model.addAttribute("feeMatrix", searchfeeMatrix);
		return FEEMATRIX_RESULT;
	}

	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public String edit(@PathVariable("id") final String id) {
		return FEEMATRIX_EDIT;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final FeeMatrix feeMatrix,
			final BindingResult errors, final Model model) {
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		feeMatrixService.update(feeMatrix);
		return FEEMATRIX_RESULT;
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.POST)
	public String view(@PathVariable("id") final String id) {
		return FEEMATRIX_VIEW;
	}

}