package org.egov.eis.web.controller.masters.position;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.egov.eis.service.DeptDesigService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/position")
public class SearchPositionController {

	private DepartmentService departmentService;
	private DesignationService designationService;
	private DeptDesigService deptDesigService;
	private PositionMasterService positionMasterService;
	private static final String WARNING = "warning";
	private static final String POSITION_NOT_PRESENT = "There is no position added for the selected department and designation.";

	
	@Autowired
	private SearchPositionController(PositionMasterService positionMasterService,DepartmentService departmentService,DesignationService designationMasterService,DeptDesigService deptDesigService)
	{
		this.departmentService=departmentService;
		this.designationService=designationMasterService;
		this.deptDesigService=deptDesigService;
		this.positionMasterService=positionMasterService;
	}
	
	  @ModelAttribute
	    public DeptDesig deptDesig() {
	        return new DeptDesig();
	    }
	
	 @ModelAttribute("departments")
	    public List<Department> departments() {
	        return departmentService.getAllDepartments();
	    }

	 @ModelAttribute("designations")	
	 public List<Designation> designations()
	 {
		 return designationService.getAllDesignationsSortByNameAsc();
	 }
	 
	
	 @RequestMapping(value = "search", method = GET)
	    public String search(Model model) {
			model.addAttribute("mode", "new");
	        return "position-search";
	    }
	 
	 
	 @RequestMapping(value = "position-getTotalPositionCount" , method = RequestMethod.GET)
	    public @ResponseBody String searchSanctionedAndOutSourcePositions(@RequestParam final String departmentId,@RequestParam final String designationId) {
		 Long deptid = Long.valueOf(0),desigid=  Long.valueOf(0);
			Integer outsourcedPost=0,sanctionedPost=0;
			
		 if(departmentId!=null && !"".equals(departmentId))
			 deptid=Long.valueOf(departmentId);
		 if(designationId!=null && !"".equals(designationId))
			 desigid=Long.valueOf(designationId);
		 
		 outsourcedPost= positionMasterService.getTotalOutSourcedPosts(deptid, desigid);
		 sanctionedPost= positionMasterService.getTotalSanctionedPosts(deptid, desigid);
			
	 return outsourcedPost +"/"+ sanctionedPost;
	 }
	  @RequestMapping(value = "position-update" , method = RequestMethod.GET)
	    public @ResponseBody String changePosition(@RequestParam final String desigName, @RequestParam final String positionName, 
	    		@RequestParam final String deptName,@RequestParam final String isoutsourced,@RequestParam final String positionId) {
	    	
	    	if(positionId!=null)
	    	{
	    		Position positionObj= positionMasterService.getPositionById(Long.valueOf(positionId));
	    		if(positionObj!=null && !positionObj.getName().equalsIgnoreCase(positionName))
	    		{
	    			List<Position> positionList = positionMasterService
							.findByNameContainingIgnoreCase(positionName);
					if (positionList!=null && positionList.size() > 0)
						return "POSITIONNAMEALREADYEXIST";
	    			//return "NOCHANGESINEXISTINGNAME";
	    		}
	    		
				positionObj.setName(positionName);
				
				if(isoutsourced!=null && isoutsourced.equalsIgnoreCase("TRUE"))
				{
					//Current position outsource is true.
					if( !positionObj.getIsPostOutsourced()){
					 positionObj.setIsPostOutsourced(true);
					 positionObj.getDeptDesig().setOutsourcedPosts(positionObj.getDeptDesig().getOutsourcedPosts()!=null?positionObj.getDeptDesig().getOutsourcedPosts()+1:1);
					}
				}else
				{
					//If outsourced is false.
					if( positionObj.getIsPostOutsourced())
					{
						 positionObj.setIsPostOutsourced(false);
							positionObj.getDeptDesig().setOutsourcedPosts(positionObj.getDeptDesig().getOutsourcedPosts()!=null?positionObj.getDeptDesig().getOutsourcedPosts()-1:0);
					}
				}
				
				positionMasterService.updatePosition(positionObj);
				return "SUCCESS";
	    	}
	    	if(positionName==null)
	    		return "POSITIONNAMEISNULL";
	    	return "SUCCESS";
	    }
	  
	 @RequestMapping(value = "resultList-update", method = RequestMethod.GET)
	    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
	            final HttpServletResponse response) throws IOException {
		 Long departmentId = Long.valueOf(0),designationId=  Long.valueOf(0);
	        final int pageStart = Integer.valueOf(request.getParameter("start"));
	        final int pageSize = Integer.valueOf(request.getParameter("length"));
	       
	        if(request.getParameter("departmentId")!=null && !"".equals(request.getParameter("departmentId"))) 
	        	departmentId = Long.valueOf(request.getParameter("departmentId"));
	        if(request.getParameter("designationId")!=null && !"".equals(request.getParameter("designationId"))) 
	             designationId = Long.valueOf(request.getParameter("designationId"));
	        final int pageNumber = pageStart / pageSize + 1;
	        
	        final String complaintRouterJSONData = commonSearchResult(pageNumber, pageSize, departmentId,
	        		designationId);
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        IOUtils.write(complaintRouterJSONData, response.getWriter());
	    }

	 public String commonSearchResult(final Integer pageNumber, final Integer pageSize, final Long departmentId,
	            final Long designationId) {
			 
	        final Page<Position> pageOfPositions = positionMasterService.getPageOfPositions(pageNumber, pageSize,
	        		departmentId, designationId);
	        final List<Position> positionList = pageOfPositions.getContent();
	        final StringBuilder complaintRouterJSONData = new StringBuilder();
	        complaintRouterJSONData.append("{\"draw\": ").append("0");
	        complaintRouterJSONData.append(",\"recordsTotal\":").append(pageOfPositions.getTotalElements());
	        complaintRouterJSONData.append(",\"totalDisplayRecords\":").append(pageSize);
	        complaintRouterJSONData.append(",\"recordsFiltered\":").append(pageOfPositions.getTotalElements());
	        complaintRouterJSONData.append(",\"data\":").append(toJSON(positionList)).append("}");
	        return complaintRouterJSONData.toString();
	    }

	    private String toJSON(final Object object) {
	        final GsonBuilder gsonBuilder = new GsonBuilder();
	        final Gson gson = gsonBuilder.registerTypeAdapter(Position.class, new PositionAdaptor()).create();
	        final String json = gson.toJson(object);
	        return json;
	    }
	 
	 
	 
	 @RequestMapping(value = "search",method = RequestMethod.POST)
	public String searchPosition(
			@Valid @ModelAttribute  DeptDesig deptDesig,final BindingResult errors, final RedirectAttributes redirectAttrs,
			Model model) {
		 if (errors.hasErrors())
				return "position-search";
			
		 DeptDesig departmentDesignation = deptDesigService.findByDepartmentAndDesignation(deptDesig.getDepartment().getId(),
				 deptDesig.getDesignation().getId());
		
		 if (departmentDesignation==null) {
				model.addAttribute(WARNING, POSITION_NOT_PRESENT);
				 model.addAttribute("deptDesig", new DeptDesig());
					model.addAttribute("mode", "error");
				 return "position-search";
			}
		 
		List <Position> positionList= positionMasterService.getAllPositionsByDeptDesigId(departmentDesignation.getId());
		 model.addAttribute("deptDesig", departmentDesignation);
		 model.addAttribute("positions", positionList); 
		 	
		 return "position-search";
	 }
}
