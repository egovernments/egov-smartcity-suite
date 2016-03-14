package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.entity.ChairPerson;
import org.egov.commons.service.ChairPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/application")
public class CreateChairPersonMasterController {

    @Autowired
    private ChairPersonService chairPersonService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @RequestMapping(value = "/chairPersonDetails", method = GET)
    public String viewForm() {
        return "chairperson-details";
    }

    @RequestMapping(value = "/ajax-activeChairPersonExistsAsOnCurrentDate", method = RequestMethod.GET)
    public @ResponseBody boolean getChairPersonName(@RequestParam final String name) {
        if (chairPersonService.getActiveChairPersonByCurrentDate() == null)
            return true;
        else
            return false;
    }

    @RequestMapping(value = "/ajax-chairpersontable", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        final int pageNumber = pageStart / pageSize + 1;
        final List<ChairPerson> totalRecords = chairPersonService.findAll();

        if (pageSize == -1)
            pageSize = totalRecords.size();
        final List<ChairPerson> chairPersonsList = chairPersonService.getListOfChairPersons(pageNumber, pageSize)
                .getContent();
        final StringBuilder chairPersonJSONData = new StringBuilder();
        chairPersonJSONData.append("{\"draw\": ").append("0");
        chairPersonJSONData.append(",\"recordsTotal\":").append(totalRecords.size());
        chairPersonJSONData.append(",\"totalDisplayRecords\":").append(chairPersonsList.size());
        chairPersonJSONData.append(",\"recordsFiltered\":").append(totalRecords.size());
        chairPersonJSONData.append(",\"data\":").append(toJSON(chairPersonsList)).append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(chairPersonJSONData, response.getWriter());
    }

    @RequestMapping(value = "/ajax-addChairPersonName", method = RequestMethod.GET)
    public @ResponseBody void addChairPersonName(@RequestParam final String name) {
    	final Calendar cal = Calendar.getInstance();
        if (chairPersonService.getActiveChairPersonByCurrentDate() != null) {
            ChairPerson chairPerson = new ChairPerson();
            chairPerson = chairPersonService.getActiveChairPersonByCurrentDate();
            chairPerson.setName(name);
            chairPersonService.updateChairPerson(chairPerson);

        } else {
            final ChairPerson chairPersonDetails = chairPersonService.getActiveChairPerson();
            if(chairPersonDetails ==null)
            {
            	final ChairPerson chairPerson = new ChairPerson();
            	chairPerson.setActive(true);
            	chairPerson.setName(name);
            	chairPerson.setFromDate(new Date());
            	chairPerson.setToDate(null);
            	chairPersonService.createChairPerson(chairPerson);
            }
            else
            {
            chairPersonDetails.setActive(false);
            cal.add(Calendar.DATE, -1);
            chairPersonDetails.setToDate(cal.getTime());
            final ChairPerson chairPerson = new ChairPerson();
            chairPerson.setName(name);
            chairPerson.setFromDate(new Date());
            chairPerson.setToDate(null);
            chairPerson.setActive(true);
            chairPersonService.updateChairPerson(chairPersonDetails);
            chairPersonService.createChairPerson(chairPerson);
            }
        }
    }

    public String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ChairPerson.class, new ChairPersonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
