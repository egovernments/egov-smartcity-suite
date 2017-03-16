package org.egov.ptis.web.controller.demolition;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMOLITION;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/demolition/ack", "/citizen/demolition/ack" })
public class DemolitionAckController {
    
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    
    @RequestMapping(value = "/printAck/{assessmentNo}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> printAck(final HttpServletRequest request, final Model model,
            @PathVariable("assessmentNo") final String assessmentNo) {
        ReportOutput reportOutput = propertyTaxUtil.generateCitizenCharterAcknowledgement(assessmentNo, DEMOLITION,
                PropertyTaxConstants.WFLOW_ACTION_NAME_DEMOLITION);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=CitizenCharterAcknowledgement.pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
