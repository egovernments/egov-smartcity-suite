package org.egov.pims.web.actions.designationHierarchy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.ObjectType;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationHierarchy;
import org.egov.pims.commons.DesignationMaster;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;
 

/**
 * @author Jagadeesan *
 */
@ParentPackage("egov")
@Validation()
public class DesignationHierarchyAction extends BaseFormAction{
	
	private static final long serialVersionUID = 1L;
	private DesignationHierarchy designationHierarchy = new DesignationHierarchy();
	protected transient PersistenceService<DesignationHierarchy, Long> designationHierarchyService;
	private String mode="";
	private  static final String VIEW="view";
	private Integer departmentIds[]=new Integer[0];
	private List<DesignationHierarchy> designationHierarchyList = new ArrayList<DesignationHierarchy>();
	private Integer fromDesigIds[]=new Integer[0];
	private Integer toDesigIds[]=new Integer[0]; 
	private String desigHierarchyDelIds="";
	private Integer desigHierarchyId[]=new Integer[0];
	private static final String SEARCH ="search";
	List designationHierarchyViewList=new ArrayList<HashMap<String, Object>>();
	private String viewDefaultDesigHierarchy="NO";

	public String getViewDefaultDesigHierarchy() {
		return viewDefaultDesigHierarchy;
	}

	public void setViewDefaultDesigHierarchy(String viewDefaultDesigHierarchy) {
		this.viewDefaultDesigHierarchy = viewDefaultDesigHierarchy;
	}

	@Override
	public Object getModel() {
		return designationHierarchy;
	}
	
	public DesignationHierarchyAction() {
		addRelatedEntity("objectType", ObjectType.class);
	}
	
	public String execute()
	{
		return NEW;
	}
	
	public void prepare()
	{
		super.prepare(); 
		addDropdownData("objectTypeList", getPersistenceService().findAllBy("from ObjectType"));
		addDropdownData("departmentListForLeftSelect",getPersistenceService().findAllBy("from DepartmentImpl"));
	}
	
	@SkipValidation
	public String loadToSearch()  //This called when loading the page before to create  designation hierarchy master.
	{
		return SEARCH;
	}
	
	@SkipValidation
	public String loadToCreate()  //This called when load the page to Designation hierarchy
	{
		if(designationHierarchy.getObjectType()==null)
		{
			addFieldError("objectType",getMessage("DesignationHierarchy.ObjectType.errMsg"));
			return loadToSearch(); 
		}
		
		if(departmentIds.length>0)
		{
			String deptWhereStr="";
			for(int i=0;i<departmentIds.length;i++)
			{
				if(i==0)
				{
					deptWhereStr = "id="+departmentIds[i];
				}
				else
				{
					deptWhereStr = deptWhereStr+" or id="+departmentIds[i];
				}
			}
			
			addDropdownData("departmentList",getPersistenceService().findAllBy("from DepartmentImpl where ("+deptWhereStr+")"));
		}
		else
		{
			designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department is null ", designationHierarchy.getObjectType().getId());
			if(!designationHierarchyList.isEmpty())
			{
				addActionError(getMessage("DesignationHierarchy.DefaultHierarchy.AlreadyExist.Msg")+" - "+designationHierarchy.getObjectType().getType());
				setMode("Create");
				return loadToSearch(); 
			}
			
		}
		
		return NEW;
	}
	
	@SkipValidation
	public String loadToModify()  //This called when load the page to Designation hierarchy
	{
		//To load the department details.
		if(designationHierarchy.getObjectType()==null)
		{
			addFieldError("objectType",getMessage("DesignationHierarchy.ObjectType.errMsg"));
			setMode("Modify");
			return loadToSearch(); 
		}
		
		if(departmentIds.length==1)
		{
			if(departmentIds[0]==-1)
			{
				designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department is null", designationHierarchy.getObjectType().getId());
			}
			else
			{
				designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department.id=?", designationHierarchy.getObjectType().getId(),departmentIds[0]);
				addDropdownData("departmentList",getPersistenceService().findAllBy("from DepartmentImpl where id=?",departmentIds[0]));
			}
		}
		
		if(designationHierarchyList.isEmpty())
		{
			if(departmentIds[0]==-1)
			{
				addActionError(getMessage("DesignationHierarchy.RecordNotExist.ObjectType.Msg"));
			}
			else
			{
				addActionError(getMessage("DesignationHierarchy.RecordNotExist.ObjectTypeAndDept.Msg"));
			}
			setMode("Modify");
			return SEARCH;
		}
		
		return EDIT;
	}
	
	@SkipValidation
	public String loadToView()  //This called when load the page to Designation hierarchy
	{
		if(designationHierarchy.getObjectType()==null)
		{
			addFieldError("objectType",getMessage("DesignationHierarchy.ObjectType.errMsg"));
			setMode("View");
			return loadToSearch(); 
		}
		
		if(departmentIds.length>0) 
		{
			String deptWhereStr="";
			String deptWhereInStr="";
			for(int i=0;i<departmentIds.length;i++)
			{
				if(i==0)
				{
					deptWhereStr = "id="+departmentIds[i];
					deptWhereInStr = departmentIds[i].toString();
				}
				else
				{
					deptWhereStr = deptWhereStr+" or id="+departmentIds[i];
					deptWhereInStr = deptWhereInStr+" , "+departmentIds[i];
				}
			}
			
			designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department.id in ( "+deptWhereInStr+" )  order by id", designationHierarchy.getObjectType().getId());
		}

		if(designationHierarchyList.isEmpty())	
		{
			designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department is null  order by id", designationHierarchy.getObjectType().getId());
			viewDefaultDesigHierarchy ="YES";
		}
		
		int i=0;
		for(DesignationHierarchy tmpDesigHir : designationHierarchyList )
		{
			HashMap<String, Object> desigHierarchyHT=new HashMap<String, Object>(); 
			desigHierarchyHT.put("designationHierarchy", tmpDesigHir);
			designationHierarchyViewList.add(i,(HashMap<String,Object>)desigHierarchyHT);
			i++;
			
		}
		
		return VIEW;
	}
	
	/**
	 * To create the designation hierarchy
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@SkipValidation
	public String create()  throws IOException,ServletException
	{
		if(departmentIds.length>0){
			for(int i=0;i<departmentIds.length;i++)
			{
				DepartmentImpl tmpDepartment = (DepartmentImpl)getPersistenceService().find("from DepartmentImpl where id=?", departmentIds[i]);
				createDesignationHierarchy(tmpDepartment);
			}
		}
		else
		{
			createDesignationHierarchy(null);//create the designation hierarchy with empty department.
		}
		
		
		if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
		{
			addActionMessage(getMessage("DesignationHierarchy.Create.Success.Msg"));
		}
		
		return loadToView();
	}
	
	public void createDesignationHierarchy(DepartmentImpl tmpDepartment)
	{
	
		for(int j=0;j<fromDesigIds.length;j++){
			DesignationHierarchy tmpDesignationHierarchy = new DesignationHierarchy();

			tmpDesignationHierarchy.setObjectType(designationHierarchy.getObjectType());
			if(tmpDepartment!=null){
				tmpDesignationHierarchy.setDepartment(tmpDepartment);
			}
			
			DesignationMaster tmpFromDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", fromDesigIds[j]);
			DesignationMaster tmpToDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", toDesigIds[j]);
			tmpDesignationHierarchy.setFromDesig(tmpFromDesignation);
			tmpDesignationHierarchy.setToDesig(tmpToDesignation);

			designationHierarchyService.create(tmpDesignationHierarchy);
		}
	}
	
	@SkipValidation
	public String edit()  throws IOException,ServletException
	{
		if(departmentIds.length!=0){
			DepartmentImpl tmpDepartment = (DepartmentImpl)getPersistenceService().find("from DepartmentImpl where id=?", departmentIds[0]);
			editDesignationHierarchy(tmpDepartment);
		}
		else
		{
			editDesignationHierarchy(null);//edit the designation hierarchy with empty department.
		}
		
		setMode("Modify");
		
		return loadToView();
	}
	
	public void editDesignationHierarchy(DepartmentImpl tmpDepartment)
	{
		String tmpdesigHierarchyDelIds[]=new String[0];
		boolean isDesignationHierarchyDeleteAll=false;
		if(!desigHierarchyDelIds.toString().equals(""))//Only some ids there to delete.
		{
			tmpdesigHierarchyDelIds= desigHierarchyDelIds.split("@");
		}
		//To get the list to modify
		if(departmentIds.length==0)
		{
			designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department is null ", designationHierarchy.getObjectType().getId());
		}
		else
		{
			designationHierarchyList = designationHierarchyService.findAllBy("from DesignationHierarchy where objectType.id=? and department.id=?", designationHierarchy.getObjectType().getId(),departmentIds[0]);
		}
		
		for(DesignationHierarchy designationHierarchy :designationHierarchyList) 
		{
			if(fromDesigIds.length==1 && fromDesigIds[0]==null)//to delete all designation hierarchy.
			{
				designationHierarchyService.delete(designationHierarchy);
				isDesignationHierarchyDeleteAll=true;
			}
			else  
			{
				boolean isDesignationHierarchyDelete=false;
				//to delete the particular designation hierarchy.
				for(int i=0;i<tmpdesigHierarchyDelIds.length;i++)
				{
					if(designationHierarchy.getId().toString().equals(tmpdesigHierarchyDelIds[i].toString()))
					{
						designationHierarchyService.delete(designationHierarchy);
						//designationHierarchyList.remove(designationHierarchy);
						isDesignationHierarchyDelete=true;
					}
				}
				
				if(!isDesignationHierarchyDelete){
					//to update the particular designation hierarchy.
					for(int j=0;j<fromDesigIds.length;j++){
						
						DesignationHierarchy tmpDesignationHierarchy = new DesignationHierarchy();
						if(desigHierarchyId[j]!=null && !desigHierarchyId[j].toString().equals("")) //to update the record
						{
							tmpDesignationHierarchy = (DesignationHierarchy)designationHierarchyService.findById(Long.valueOf(desigHierarchyId[j].toString()), false);
							DesignationMaster tmpFromDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", fromDesigIds[j]);
							DesignationMaster tmpToDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", toDesigIds[j]);
							
							tmpDesignationHierarchy.setFromDesig(tmpFromDesignation);
							tmpDesignationHierarchy.setToDesig(tmpToDesignation);

							designationHierarchyService.update(tmpDesignationHierarchy);
						}
					}
				}
				
			}
		}
		
		if(!isDesignationHierarchyDeleteAll)
		{
			//to create the particular designation hierarchy.
			for(int j=0;j<fromDesigIds.length;j++)
			{
				DesignationHierarchy tmpDesignationHierarchy = new DesignationHierarchy();
				if(desigHierarchyId[j]==null)//to create a new designation hierarchy record
				{
					tmpDesignationHierarchy.setObjectType(designationHierarchy.getObjectType());
					if(tmpDepartment!=null){
						tmpDesignationHierarchy.setDepartment(tmpDepartment);
					}
					
					DesignationMaster tmpFromDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", fromDesigIds[j]);
					DesignationMaster tmpToDesignation = (DesignationMaster)getPersistenceService().find("from DesignationMaster where designationId=?", toDesigIds[j]);
					
					tmpDesignationHierarchy.setFromDesig(tmpFromDesignation);
					tmpDesignationHierarchy.setToDesig(tmpToDesignation);

					designationHierarchyService.create(tmpDesignationHierarchy);
				}
			}
			
			if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
			{
				addActionMessage(getMessage("DesignationHierarchy.Edit.Success.Msg"));
			}
		}
		else //when all records deleted.
		{
			if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
			{
				addActionMessage(getMessage("DesignationHierarchy.Edit.DeleteAll.Msg")+" "+designationHierarchy.getObjectType().getType()+(tmpDepartment!=null ? "/"+tmpDepartment.getDeptName() : "" ));
			}
		}
	}
		
	public void validate()
	{

	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	protected String getMessage(final String key) {
		return getText(key);
	}
	
	public DesignationHierarchy getDesignationHierarchy() {
		return designationHierarchy;
	}

	public void setDesignationHierarchy(DesignationHierarchy designationHierarchy) {
		this.designationHierarchy = designationHierarchy;
	}

	public void setDesigHierarchyDelIds(String desigHierarchyDelIds) {
		this.desigHierarchyDelIds = desigHierarchyDelIds;
	}

	public void setDesignationHierarchyService(
			PersistenceService<DesignationHierarchy, Long> designationHierarchyService) {
		this.designationHierarchyService = designationHierarchyService;
	}

	public Integer[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(Integer[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	public List<DesignationHierarchy> getDesignationHierarchyList() {
		return designationHierarchyList;
	}

	public void setDesignationHierarchyList(
			List<DesignationHierarchy> designationHierarchyList) {
		this.designationHierarchyList = designationHierarchyList;
	}

	public void setFromDesigIds(Integer[] fromDesigIds) {
		this.fromDesigIds = fromDesigIds;
	}

	public void setToDesigIds(Integer[] toDesigIds) {
		this.toDesigIds = toDesigIds;
	}
	
	public void setDesigHierarchyId(Integer[] desigHierarchyId) {
		this.desigHierarchyId = desigHierarchyId;
	}

	public List getDesignationHierarchyViewList() {
		return designationHierarchyViewList;
	}

	public void setDesignationHierarchyViewList(List designationHierarchyViewList) {
		this.designationHierarchyViewList = designationHierarchyViewList;
	}

}