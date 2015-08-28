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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/application")
public class CreateChairPersonMasterController extends GenericConnectionController {

    @Autowired
    private ChairPersonService chairPersonService;
    public static final String CONTENTTYPE_JSON = "application/json";

    @RequestMapping(value = "/chairPersonDetails", method = GET)
    public String viewForm() {
        return "chairperson-details";
    }

    @RequestMapping(value = "/ajax-chairPersonName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void getChairPersonName(@RequestParam final String name) {

        final ChairPerson chairPersonDetails = chairPersonService.getActiveChairPerson();
        chairPersonDetails.setActive(false);
        final Calendar cal = Calendar.getInstance();
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

    @RequestMapping(value = "/ajax-chairpersontable", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        if (request.getParameter("start") != null) {

        }
        final int pageStart = Integer.valueOf(request.getParameter("start"));
        int pageSize = Integer.valueOf(request.getParameter("length"));
        final int pageNumber = pageStart / pageSize + 1;
        final List<ChairPerson> totalRecords = chairPersonService.findAll();
        System.out.println("Total records" + totalRecords);
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

    public String toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ChairPerson.class, new ChairPersonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
