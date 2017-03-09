package org.egov.tl.web.controller.payment;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.egov.tl.entity.License;
import org.egov.tl.entity.dto.DCBReportResult;
import org.egov.tl.service.DCBReportService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.response.adaptor.DCBReportResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewDCBController {

	private static final String LICENSE = "license";

	@Autowired
	private TradeLicenseService tradeLicenseService;

	@Autowired
	private DCBReportService dCBReportService;

	@RequestMapping(value = "/public/view-license-dcb/{id}", method = RequestMethod.GET)
	public String search(@PathVariable final Long id, Model model) throws IOException {
		final License license = tradeLicenseService.getLicenseById(id);
		model.addAttribute(LICENSE, license);
		model.addAttribute("dcbreport",
				toJSON(dCBReportService.generateReportResult(license.getLicenseNumber(), defaultString(LICENSE),
						defaultString(LICENSE)), DCBReportResult.class, DCBReportResponseAdaptor.class));

		return "view-license-dcb";
	}
}
