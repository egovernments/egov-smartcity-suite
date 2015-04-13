package org.egov.infra.web.controller.admin.masters;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenericMasterAjaxController {

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @RequestMapping(value = "/boundaryTypes-by-hierarchyType", method = RequestMethod.GET)
    public @ResponseBody void getBoundaryTypeByHierarchyType(@RequestParam Long hierarchyTypeId, HttpServletResponse response) throws IOException {
        List<BoundaryType> boundaryTypes = boundaryTypeService.getAllBoundarTypesByHierarchyTypeId(hierarchyTypeId);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildJSONString(boundaryTypes), response.getWriter());
    }

    @RequestMapping(value = "/boundaries-by-boundaryType", method = RequestMethod.GET)
    public @ResponseBody void getBoundariesByBoundaryType(@RequestParam Long boundaryTypeId,
            HttpServletResponse response) throws IOException {
        List<Boundary> boundaries = boundaryService.getAllBoundariesByBoundaryTypeId(boundaryTypeId);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        for (Boundary boundary : boundaries) {
            jsonObject = new JSONObject();
            jsonObject.put("Text", boundary.getBoundaryNameLocal());
            jsonObject.put("Value", boundary.getBoundaryNameLocal());
            jsonArray.add(jsonObject);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(jsonArray.toString(), response.getWriter());
    }

    @RequestMapping(value = "/check-is-root", method = RequestMethod.GET)
    public @ResponseBody boolean isRootBoundary(@RequestParam Long boundaryTypeId, @RequestParam Long hierarchyTypeId) {
        BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByIdAndHierarchyType(boundaryTypeId,
                hierarchyTypeId);
        return boundaryType.getParent() == null ? false : boundaryType.getParent().getId() == 0 ? true : false;
    }

    // FIXME Can be made generic by the help of annotation which takes fields as
    // inputs [Nayeem]
    private String buildJSONString(List<BoundaryType> boundaryTypes) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        for (BoundaryType boundaryType : boundaryTypes) {
            jsonObject = new JSONObject();
            jsonObject.put("Text", boundaryType.getName());
            jsonObject.put("Value", boundaryType.getId());
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    /*
     * Used in ajax validation to check if child exists for a boundary type -
     * Add child screen
     */
    @RequestMapping(value = "/addChildBoundaryType/isChildPresent", method = RequestMethod.GET)
    public @ResponseBody boolean isChildBoundaryTypePresent(@RequestParam final Long parentId){
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByParent(parentId);
        return boundaryType != null ? Boolean.TRUE : Boolean.FALSE;
    }
    
}
