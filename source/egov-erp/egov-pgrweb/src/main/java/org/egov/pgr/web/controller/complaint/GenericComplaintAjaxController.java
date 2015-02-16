package org.egov.pgr.web.controller.complaint;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.pgr.entity.ComplaintType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/complaint/")
public class GenericComplaintAjaxController extends GenericComplaintController {

    @Autowired
    private BoundaryService boundaryService;

    @RequestMapping(value = { "citizen/isLocationRequired", "citizen/anonymous/isLocationRequired", "officials/isLocationRequired" }, method = GET)
    public @ResponseBody boolean isLocationRequired(@RequestParam final String complaintTypeName) {
        final ComplaintType complaintType = complaintTypeService.findByName(complaintTypeName);
        return complaintType == null ? Boolean.TRUE : complaintType.isLocationRequired();
    }

    @RequestMapping(value = { "citizen/complaintTypes", "citizen/anonymous/complaintTypes", "officials/complaintTypes" }, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComplaintType> getAllComplaintTypesByNameLike(@RequestParam final String complaintTypeName) {
        return complaintTypeService.findAllByNameLike(complaintTypeName);
    }

    @RequestMapping(value = { "citizen/locations", "citizen/anonymous/locations", "officials/locations" }, method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getAllLocationJSON(@RequestParam final String locationName) {
        final StringBuilder locationJSONData = new StringBuilder("[");
        //TODO Improve this code
        boundaryService.getBoundaryByNameLike(locationName).stream().forEach(location -> {
            locationJSONData.append("{\"name\":\"");
            if (location.isRoot())
                locationJSONData.append(location.getBndryNameLocal());
            else {
                Boundary currentLocation = location;
                while (!currentLocation.isRoot()) {
                    locationJSONData.append(currentLocation.getBndryNameLocal()).append(", ");
                    currentLocation = currentLocation.getParent();
                    if (currentLocation.isRoot()) {
                        locationJSONData.append(currentLocation.getBndryNameLocal());
                        break;
                    }
                }
            }
            locationJSONData.append("\",\"id\":").append(location.getId()).append("},");
        });
        if (locationJSONData.lastIndexOf(",") != -1)
            locationJSONData.deleteCharAt(locationJSONData.lastIndexOf(","));
        locationJSONData.append("]");
        return locationJSONData.toString();
    }

}