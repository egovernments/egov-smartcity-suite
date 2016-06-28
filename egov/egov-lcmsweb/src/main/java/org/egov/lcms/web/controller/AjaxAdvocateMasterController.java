package org.egov.lcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.Bankbranch;
import org.egov.commons.service.BankBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxAdvocateMasterController {

    @Autowired
    BankBranchService bankBranchService;

    @RequestMapping(value = "/ajax-getAllBankBranchsByBank", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Bankbranch> getAllBankBranchsByBank(@RequestParam final Integer bankId) {
/*        List<Bankbranch> bankbranches = new ArrayList<Bankbranch>(0);
        bankbranches = bankBranchService.getAllBankBranchsByBank(bankId);*/
       /* bankbranches.forEach(bankbranch -> bankbranch.toString());*/
        return bankBranchService.getAllBankBranchsByBank(bankId);
    }

}
