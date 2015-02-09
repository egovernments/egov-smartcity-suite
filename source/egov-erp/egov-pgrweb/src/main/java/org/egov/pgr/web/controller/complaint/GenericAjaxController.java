package org.egov.pgr.web.controller.complaint;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.pgr.entity.ComplaintType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/complaint/")
public class GenericAjaxController extends GenericComplaintController {

        @Autowired
        private BoundaryDAO boundaryDAO;

        
        @RequestMapping(value="citizen/isLocationRequired",method=GET)
        public @ResponseBody boolean isLocationRequired(@RequestParam final String complaintTypeName) {
                return complaintTypeService.findByName(complaintTypeName).isLocationRequired();
        } 
   
        @RequestMapping(value="citizen/complaintTypes",method=GET,produces=MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody List<ComplaintType> getAllComplaintTypesByNameLike(@RequestParam  final String complaintTypeName) {
                return complaintTypeService.findAllByNameLike(complaintTypeName);
        }
        
       
        @RequestMapping(value = "citizen/locations", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody String getAllLocationJSON(@RequestParam final String locationName) {
                final StringBuilder boundaryIdAndName = new StringBuilder("[");
                /*for (final Boundary boundary : boundaryDAO.getAllBoundaryById(1).as) {
                        boundaryIdAndName.append("{\"label\":\"");
                        if (boundary.isRoot()) {
                                boundaryIdAndName.append(boundary.getName());
                        } else {
                                Boundary tempBoundary = boundary;
                                while (!tempBoundary.isRoot()) {
                                        boundaryIdAndName.append(tempBoundary.getName()).append(", ");
                                        tempBoundary = tempBoundary.getParentBoundary();
                                        if (tempBoundary.isRoot()) {
                                                boundaryIdAndName.append(tempBoundary.getName());
                                                break;
                                        }
                                }
                        }

                        boundaryIdAndName.append("\",\"value\":").append(boundary.getId()).append("},");
                }
                boundaryIdAndName.deleteCharAt(boundaryIdAndName.lastIndexOf(","));
                boundaryIdAndName.append("]");*/
                return boundaryIdAndName.toString();
        }

}