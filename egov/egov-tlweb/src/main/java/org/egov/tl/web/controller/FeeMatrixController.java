package org.egov.tl.web.controller;


import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.infstr.services.PersistenceService;
import org.egov.tl.domain.entity.FeeMatrix;
import org.egov.tl.domain.entity.FeeMatrixDetail;
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


@Controller 
@RequestMapping("/feematrix/")
public class FeeMatrixController {
	private final static String FEEMATRIX_NEW="feematrix-new";
	private final static String FEEMATRIX_RESULT="feematrix-result";
	private final static String FEEMATRIX_EDIT="feematrix-edit";
	private final static String FEEMATRIX_VIEW="feematrix-view";
	@Autowired
	private  FeeMatrixService feeMatrixService;
	@Autowired
	public PersistenceService persistenceService;
	
	@Autowired
	private FeeTypeService feeTypeService;

	private void prepareForNewForm(Model model) {
		model.addAttribute("feeMatrix",new FeeMatrix());
		model.addAttribute("licenseCategorys",(List<LicenseCategory>)persistenceService.findAllBy("select  c from LicenseCategory c order by name asc"));
		model.addAttribute("natureOfBusinesss",(List<NatureOfBusiness>)persistenceService.findAllBy("select n from org.egov.tl.domain.entity.NatureOfBusiness n order by name asc"));
		
		model.addAttribute("subCategorys",Collections.EMPTY_LIST);
		model.addAttribute("licenseAppTypes",(List<LicenseAppType>)persistenceService.findAllBy("select a from LicenseAppType a order by name asc"));
		model.addAttribute("feeTypes",feeTypeService.findAll());
		model.addAttribute("unitOfMeasurements",(List<UnitOfMeasurement>)persistenceService.findAllBy("select u from UnitOfMeasurement  u order by name asc"));
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String newForm(final Model model){
		prepareForNewForm(model);
		return FEEMATRIX_NEW;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final FeeMatrix feeMatrix,final BindingResult errors,final Model model,HttpServletRequest request){

		System.out.println(request.getParameterMap());
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		feeMatrixService.create(feeMatrix);
		model.addAttribute("feeMatrix", feeMatrix);
		return FEEMATRIX_RESULT;
	}
	
	@RequestMapping(value = "search", method = RequestMethod.GET)  
	public String search(@ModelAttribute final FeeMatrix feeMatrix,final BindingResult errors,final Model model){
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		FeeMatrix searchfeeMatrix = feeMatrixService.search(feeMatrix);
		if(searchfeeMatrix==null)
		{
			searchfeeMatrix=new FeeMatrix();
			
		}		
		for(FeeMatrixDetail fd:searchfeeMatrix.getFeeMatrixDetail())
		{
			System.out.println(fd.getUomFrom());
		}
		model.addAttribute("feeMatrix", searchfeeMatrix);
		return FEEMATRIX_RESULT;
	}

	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public String edit(@PathVariable("id") final String id){
		return FEEMATRIX_EDIT;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final FeeMatrix feeMatrix,final BindingResult errors,final Model model){
		if (errors.hasErrors())
			return FEEMATRIX_RESULT;
		feeMatrixService.update(feeMatrix);
		return FEEMATRIX_RESULT;
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.POST)
	public String view(@PathVariable("id") final String id){
		return FEEMATRIX_VIEW;
	}


}