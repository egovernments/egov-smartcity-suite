package org.egov.works.web.controller.abstractestimate;

import java.util.Date;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.master.service.OverheadService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/abstractestimate")
public class CreateAbstractEstimateController extends GenericWorkFlowController {

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private LineEstimateDetailService lineEstimateDetailService;
	
	@Autowired
	private OverheadService overheadService;

	@RequestMapping(value = "/newform", method = RequestMethod.GET)
	public String showAbstractEstimateForm(@RequestParam final String estimateNumber, final Model model) {
		Date currentDate = new Date();
		LineEstimateDetails lineEstimateDetails = lineEstimateDetailService
				.findLineEstimateByEstimateNumber(estimateNumber, WorksConstants.STATUS_TECHNICAL_SANCTIONED);
		model.addAttribute("lineEstimateDetails", lineEstimateDetails);
		model.addAttribute("abstractEstimate", new AbstractEstimate());
		model.addAttribute("currentDate", currentDate);

		setDropDownValues(model);
		return "newAbstractEstimate-form";
	}
	
	 private void setDropDownValues(final Model model) {
	        model.addAttribute("overheads", overheadService.getOverheadsByDate(new Date()));
	    }

}
