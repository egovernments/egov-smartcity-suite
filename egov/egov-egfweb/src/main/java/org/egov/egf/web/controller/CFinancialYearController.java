package org.egov.egf.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.web.adaptor.CFinancialYearJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/cfinancialyear")
public class CFinancialYearController {
    private final static String CFINANCIALYEAR_NEW = "cfinancialyear-new";
    private final static String CFINANCIALYEAR_RESULT = "cfinancialyear-result";
    private final static String CFINANCIALYEAR_EDIT = "cfinancialyear-edit";
    private final static String CFINANCIALYEAR_VIEW = "cfinancialyear-view";
    private final static String CFINANCIALYEAR_SEARCH = "cfinancialyear-search";

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private MessageSource messageSource;

    private final SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");

    private void prepareNewForm(final Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        final CFinancialYear financialYear = new CFinancialYear();
        if (financialYear.getcFiscalPeriod().isEmpty())
            financialYear.addCFiscalPeriod(new CFiscalPeriod());
        final Date nextStartingDate = cFinancialYearService.getNextFinancialYearStartingDate();
        model.addAttribute("startingDate", dtFormat.format(nextStartingDate));
        model.addAttribute("CFinancialYear", financialYear);
        model.addAttribute("mode", "create");
        return CFINANCIALYEAR_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute CFinancialYear cFinancialYear, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        final Boolean flag = false;
        final Boolean isActive = true;
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute("mode", "create");
            return CFINANCIALYEAR_NEW;
        }
        CFiscalPeriod fiscalPeriod = new CFiscalPeriod();
        final List<CFiscalPeriod> fiscalList = cFinancialYear.getcFiscalPeriod();
        for (final CFiscalPeriod fiscal : fiscalList) {
            fiscalPeriod = cFinancialYearService.findByFiscalName(fiscal.getName());
            if (fiscalPeriod != null) {
                prepareNewForm(model);
                redirectAttrs.addFlashAttribute("financialYear", cFinancialYear);
                model.addAttribute("message", "Entered Fiscal Period Name " + fiscalPeriod.getName()
                        + " already Exists");
                model.addAttribute("mode", "create");
                return CFINANCIALYEAR_NEW;
            }
        }
        cFinancialYear.setIsActive(isActive);
        cFinancialYear.setIsClosed(flag);
        cFinancialYear.setTransferClosingBalance(flag);
        cFinancialYear = buildFiscalPeriodDetails(cFinancialYear, cFinancialYear.getcFiscalPeriod());
        cFinancialYearService.create(cFinancialYear);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.cFinancialYear.success", null, Locale.ENGLISH));
        return "redirect:/cfinancialyear/result/" + cFinancialYear.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("cFinancialYear", cFinancialYear);
        model.addAttribute("mode", "edit");
        return CFINANCIALYEAR_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final CFinancialYear cFinancialYear, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return CFINANCIALYEAR_EDIT;
        }
        cFinancialYearService.update(cFinancialYear);
        redirectAttrs.addFlashAttribute("message",
                messageSource.getMessage("msg.cFinancialYear.success", null, Locale.ENGLISH));
        return "redirect:/cfinancialyear/result/" + cFinancialYear.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("cFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final CFinancialYear cFinancialYear = cFinancialYearService.findOne(id);
        model.addAttribute("cFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final CFinancialYear cFinancialYear = new CFinancialYear();
        prepareNewForm(model);
        model.addAttribute("cFinancialYear", cFinancialYear);
        return CFINANCIALYEAR_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final CFinancialYear cFinancialYear) {
        final List<CFinancialYear> searchResultList = cFinancialYearService.search(cFinancialYear);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(CFinancialYear.class, new CFinancialYearJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    private CFinancialYear buildFiscalPeriodDetails(final CFinancialYear cFinancialYear,
            final List<CFiscalPeriod> fiscalPeriodDetail) {
        final Boolean flag = false;
        final Set<CFiscalPeriod> fiscalPeriodSet = new HashSet<CFiscalPeriod>();

        for (final CFiscalPeriod fpDetail : fiscalPeriodDetail) {
            fpDetail.setIsActive(flag);
            fpDetail.setIsActiveForPosting(flag);
            fpDetail.setIsClosed(flag);
            fpDetail.setcFinancialYear(cFinancialYear);
            fiscalPeriodSet.add(fpDetail);
        }

        cFinancialYear.getcFiscalPeriod().clear();

        cFinancialYear.getcFiscalPeriod().addAll(fiscalPeriodSet);

        return cFinancialYear;

    }
}