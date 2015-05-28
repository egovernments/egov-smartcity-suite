/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.web.actions.positionMaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.services.PersistenceService;
import org.egov.masters.model.BillNumberMaster;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("serial")
@ParentPackage("egov")
public class PositionMasterAction extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(PositionMasterAction.class);
	
	private String query ;
	private List<Designation> designationList = null;
	private List<Department> departmentList = null;
	//private List<BillNumberMaster> billNumberList = new ArrayList<BillNumberMaster>();
	private List<BillNumberMaster> billNumberLineList = new ArrayList<BillNumberMaster>();
	private List<BillNumberMaster> billNumberHeaderList = new ArrayList<BillNumberMaster>();
	private List<DrawingOfficer> doList = null;
	
	private List<Position> positionList = null;
	private static final String DESIGNATION_RESULTS   = "designationResults";
	private static final String DEPARTMENT_RESULTS   = "departmentResults";
	private static final String BILLNUMBER_RESULTS   = "billnumberResults";
	private static final String DRAWINGOFFICER_RESULTS   = "drawingofficerResults";
	private static final String POSITION_RESULTS   = "positionResults";
	private PersistenceService<DrawingOfficer, Long> drawingOfficerService;
	
	private Integer designationId;
	private Integer departmentId;
	private String departmentName;
	private String designationName;
	private String designationDescription;
	private Integer billNumberId;
	private String billNo;
	private Integer positionId;
	private boolean showUnmappedPositions;
	
	private Integer sanctionedPosts;
	private Integer outsourcedPosts;
	
	private String[] positionName;
	private Integer[] isOutsourced;
	private String mode="";
	
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void prepare()
	{
		//designationList = persistenceService.getSession().createQuery("from DesignationMaster").list();
		//departmentList = persistenceService.getSession().createQuery("from Department").list();
	}
	public String search()
	{
		setMode("load"); 
		return NEW;
	}
	public String createOrModify() 
	{
		DeptDesig deptDesig=(DeptDesig)persistenceService.find(
				" from DeptDesig where department.id=? and designation.designationId=? ",getDepartmentId(),getDesignationId());
		billNumberHeaderList=(List<BillNumberMaster>)persistenceService.findAllBy("from BillNumberMaster where department.id=?", departmentId);
		if(null!=deptDesig )
		{
			
			try{
			positionList=getPosListByDeptDesig();
			 if(positionList.size()>200)
				{
					positionList=Collections.EMPTY_LIST;//making this empty wont render positions as search results
					throw new EGOVRuntimeException(getText("exceedingPosition.msg"));
				}
			}catch (EGOVRuntimeException e) {
				addActionError(e.getMessage());
			}
			
			
			setSanctionedPosts(deptDesig.getSanctionedPosts());
			setOutsourcedPosts(deptDesig.getOutsourcedPosts());
			
		}
		else
		{
			positionList=null;
			addActionMessage(getText("post.not.created"));
		}
		if(getBillNumberId()!=null &&  getBillNumberId()>0 && getDepartmentId()!=null && getDepartmentId()>0)
		{
			BillNumberMaster bill=(BillNumberMaster)persistenceService.find("from BillNumberMaster where id=? and department.id=?",
					getBillNumberId(),getDepartmentId());
			billNumberLineList.add(bill);
		}
		else if(getDepartmentId()!=null && getDepartmentId()>0)
		{
			billNumberLineList.addAll(billNumberHeaderList);
		}
		setMode("createModify"); 
		return NEW;
	}
	
	
	public String savePosition() 
	{
		List<Position> posListDB=null;
		List<Position> posListToShowOnSave=new ArrayList<Position>(positionList);
		Iterator<Position> oldPosIterator=null;
		
		
		DeptDesig deptDesig=(DeptDesig)persistenceService.find(
				" from DeptDesig where deptId.id=? and desigId.designationId=? ",getDepartmentId(),getDesignationId());
		if(null!=deptDesig)
		{
			posListDB=getPosListByDeptDesig();
			oldPosIterator = posListDB.listIterator();
			
			deptDesig.setSanctionedPosts(getSanctionedPosts());
			deptDesig.setOutsourcedPosts(getOutsourcedPosts());
			persistenceService.getSession().saveOrUpdate(deptDesig);
		}
		
		
		while(null!=oldPosIterator && oldPosIterator.hasNext()){
			Position oldPos  = oldPosIterator.next();
			
			Iterator<Position> newPosIterator = positionList.listIterator();
			while(newPosIterator.hasNext()){
				Position newPos  = newPosIterator.next();

				if(null != newPos && null!= newPos.getId() && 
						newPos.getId().equals(oldPos.getId()))
					
				{
					//: TODO copy the contents from new position to old position 
					setPositionAttributes(oldPos, newPos);
					newPosIterator.remove();
					//oldPos.setLastModifiedDate(new DateTime());
					//oldPos.setLastModifiedBy(EisManagersUtill.getUserService().getUserById(Long.valueOf(EGOVThreadLocals.getUserId())));
					persistenceService.getSession().saveOrUpdate(oldPos);
					break;
				}
			}
		}
		if(!positionList.isEmpty())
		{
			for(Position position:positionList){
				if(position!=null){
					if((null==posListDB || posListDB.isEmpty()) && null==deptDesig) //create new Positions
					{
						deptDesig=new DeptDesig();
						deptDesig.setSanctionedPosts(getSanctionedPosts());
						deptDesig.setOutsourcedPosts(getOutsourcedPosts());
						deptDesig.setDepartment( ( (Department)persistenceService.find("from Department where id=?", getDepartmentId()) ));
						deptDesig.setDesignation( ((Designation)persistenceService.find("from Designation where id=?", getDesignationId())));
						position.setDeptDesig(deptDesig);
					}
					else{			
						
						position.setDeptDesig(deptDesig);//update existing position
						
					}
					persistenceService.getSession().saveOrUpdate(deptDesig);
					/*if(null!=position.getBillNumber() && null!=position.getBillNumber().getId() )
					{
						BillNumberMaster bill=(BillNumberMaster)persistenceService.find("from BillNumberMaster where id=?",position.getBillNumber().getId());
						position.setBillNumber(bill);
						billNumberLineList.add(bill);
					}
					else
					{
						position.setBillNumber(null);
					}
					if(null!=position.getDrawingOfficer() && null!=position.getDrawingOfficer().getCode())
					{
						DrawingOfficer drawingofficer=getDrawingOfficerService().find("from DrawingOfficer where code =?",  position.getDrawingOfficer().getCode());
						position.setDrawingOfficer(drawingofficer);
					}*/
					//position.setLastModifiedDate(new Date());
					//position.setLastModifiedBy(EisManagersUtill.getUserService().getUserById(Long.valueOf(EGOVThreadLocals.getUserId())));
					persistenceService.getSession().saveOrUpdate(position);
				}
			}
		}
		
		addActionMessage(getText("post.modified.successfully"));
		setMode("view");
		positionList.clear();
		positionList.addAll(posListToShowOnSave);//to show all  the positions after save
		
		return NEW;
		
	}
	public String view()
	{
		setMode("view");
		billNumberLineList.clear();
		DeptDesig deptDesig=(DeptDesig)persistenceService.find(
				" from DeptDesig where deptId.id=? and desigId.designationId=? ",getDepartmentId(),getDesignationId());
		try{
				if(null!=deptDesig )
				{
						positionList=getPosListByDeptDesig();
						
						if(null!=positionList && positionList.size()>0){
							
							
								 if(positionList.size()>200)
									{
										positionList=Collections.EMPTY_LIST;//making this empty wont render positions as search results
										throw new EGOVRuntimeException(getText("exceedingPosition.msg"));
									}
								
							
								/*if(positionList.get(0).getBillNumber()!=null)
								billNumberLineList.add(positionList.get(0).getBillNumber());*/
						
						}
						else if(null!=positionList && positionList.size()==1){
							positionList=getPosListByDeptDesig();
								/*for(Position pos:positionList)
								{
									if(positionList.get(0).getBillNumber()!=null)
									billNumberLineList.add(pos.getBillNumber());	
								}*/
							}
								
						setSanctionedPosts(deptDesig.getSanctionedPosts());
						setOutsourcedPosts(deptDesig.getOutsourcedPosts());
						
					}
					else
					{
						positionList=null;
					}
		}catch (EGOVRuntimeException e) {
			addActionError(e.getMessage());
		}
		return NEW;
	}

	private List getPosListByDeptDesig() {
		
		String qryBillIdNull="";
		LOGGER.debug("isShowUnmappedPositions "+isShowUnmappedPositions()+" billnumber "+getBillNumberId()+" posid" +getPositionId());
		
		
		if(isShowUnmappedPositions())
			qryBillIdNull=" and billNumber is null";
		
		if(getDepartmentId()!=null && getDepartmentId()>0 && getDesignationId()!=null && getDesignationId()>0)
		{
			//get positions by billnumber,position, and dept,desg when isShowUnmappedPositions not checked
			 if( getBillNumberId()!=null &&  getBillNumberId()>0 && getPositionId()!=null && getPositionId()>0 && !isShowUnmappedPositions()){
				
				return persistenceService.findAllBy(
						"from Position where deptDesigId in(select id from DeptDesig where deptId.id=? and desigId.designationId=?) and billNumber.id=?" +
						" and id=? )",
						getDepartmentId(),getDesignationId(),getBillNumberId(),getPositionId());
				
			}
			//get positions by billnumber, and dept,desg when isShowUnmappedPositions not checked
			 else if( getBillNumberId()!=null &&  getBillNumberId()>0 && !isShowUnmappedPositions()){
				
			return persistenceService.findAllBy(
					"from Position where deptDesigId in(select id from DeptDesig where deptId.id=? and desigId.designationId=?) and billNumber.id=?)",
					getDepartmentId(),getDesignationId(),getBillNumberId());
			}
			
			else if (getPositionId()!=null && getPositionId()>0)
			{
				LOGGER.info("isShowUnmappedPositions "+isShowUnmappedPositions());
				return persistenceService.findAllBy(
						"from Position where deptDesigId in(select id from DeptDesig where deptId.id=? and desigId.designationId=?) and id=? "+qryBillIdNull+" )",
						getDepartmentId(),getDesignationId(),getPositionId());
			}
			
			//get unmapped positions by  dept,desg when isShowUnmappedPositions  checked, else get all positions by dept,desg 
			else
			{
			
			return persistenceService.findAllBy(
			"from Position where deptDesigId in(select id from DeptDesig where deptId.id=? and desigId.designationId=?) "+qryBillIdNull+"  )",
			getDepartmentId(),getDesignationId());
			
			}
						
		}
		else
			return Collections.EMPTY_LIST;
	}
	
	public String getDesignations() {

		if (StringUtils.isNotBlank(query)) {
			Criteria criteria =persistenceService.getSession().createCriteria(Designation.class, "designation");
			criteria.add(Restrictions.ilike("designationName", query, MatchMode.START));
			designationList= criteria.list();
		}
		return DESIGNATION_RESULTS;
	}
	
	public String getDepartments() {

		if (StringUtils.isNotBlank(query)) {
			Criteria criteria =persistenceService.getSession().createCriteria(Department.class, "Department");
			criteria.add(Restrictions.ilike("deptName", query, MatchMode.START));
			departmentList= criteria.list();
			
		}
		return DEPARTMENT_RESULTS;
	}
	
	
	public String getDrawingOfficers() {

		doList=getDrawingOfficerService().findAll();		
		return DRAWINGOFFICER_RESULTS;
	}
	public void setPositionAttributes(Position oldPosition,Position newPosition)
	{
		oldPosition.setName(newPosition.getName());
		oldPosition.setPostOutsourced(newPosition.isPostOutsourced());
		/*if(null!=newPosition.getBillNumber() && null!=newPosition.getBillNumber().getId())
		{
			BillNumberMaster bill=(BillNumberMaster)persistenceService.find("from BillNumberMaster where id=? ",newPosition.getBillNumber().getId());
		oldPosition.setBillNumber(bill);
		}
		if(null!=newPosition.getDrawingOfficer() && null!=newPosition.getDrawingOfficer().getCode())
		{	
		DrawingOfficer drawingofficer=getDrawingOfficerService().find("from DrawingOfficer where code =?", newPosition.getDrawingOfficer().getCode());
		oldPosition.setDrawingOfficer(drawingofficer);
		}
		billNumberLineList.add(oldPosition.getBillNumber());*/
	}
	public String billNumbersByDept()
	{
		if(departmentId!=null && departmentId>0)
		{
			billNumberHeaderList=(List<BillNumberMaster>)persistenceService.findAllBy("from BillNumberMaster where department.id=?", departmentId);
			
		}
		return BILLNUMBER_RESULTS;
	}
	
	public String positionsByDeptDesignation()
	{
		if(getDepartmentId()!=null && getDepartmentId()>0 && getDesignationId()!=null && getDesignationId()>0)
		{
			positionList= persistenceService.findAllBy(
				"from Position where deptDesigId in(select id from DeptDesig where deptId.id=? and desigId.designationId=?) and upper(name) like ? )",
				getDepartmentId(),getDesignationId(),(query.toUpperCase()+"%"));
		}
		return POSITION_RESULTS;
	}

	
	public List<BillNumberMaster> getBillNumberLineList() {
		return billNumberLineList;
	}

	public void setBillNumberLineList(List<BillNumberMaster> billNumberLineList) {
		this.billNumberLineList = billNumberLineList;
	}

	public void setBillNumberHeaderList(List<BillNumberMaster> billNumberHeaderList) {
		this.billNumberHeaderList = billNumberHeaderList;
	}

	public List<BillNumberMaster> getBillNumberHeaderList() {
		return billNumberHeaderList;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public List<Designation> getDesignationList() {
		return designationList;
	}


	public void setDesignationList(List<Designation> designationList) {
		this.designationList = designationList;
	}


	public List<Department> getDepartmentList() {
		return departmentList;
	}


	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	

	public List<DrawingOfficer> getDoList() {
		return doList;
	}

	public void setDoList(List<DrawingOfficer> doList) {
		this.doList = doList;
	}

	public PersistenceService<DrawingOfficer, Long> getDrawingOfficerService() {
		return drawingOfficerService;
	}

	public void setDrawingOfficerService(
			PersistenceService<DrawingOfficer, Long> drawingOfficerService) {
		this.drawingOfficerService = drawingOfficerService;
	}

	

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String[] getPositionName() {
		return positionName;
	}

	public void setPositionName(String[] positionName) {
		this.positionName = positionName;
	}

	public String getDesignationDescription() {
		return designationDescription;
	}

	public void setDesignationDescription(String designationDescription) {
		this.designationDescription = designationDescription;
	}

	public Integer getSanctionedPosts() {
		return sanctionedPosts;
	}

	public void setSanctionedPosts(Integer sanctionedPosts) {
		this.sanctionedPosts = sanctionedPosts;
	}

	public Integer getOutsourcedPosts() {
		return outsourcedPosts;
	}

	public void setOutsourcedPosts(Integer outsourcedPosts) {
		this.outsourcedPosts = outsourcedPosts;
	}

	public Integer[] getIsOutsourced() {
		return isOutsourced;
	}

	public void setIsOutsourced(Integer[] isOutsourced) {
		this.isOutsourced = isOutsourced;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getBillNumberId() {
		return billNumberId;
	}

	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public boolean isShowUnmappedPositions() {
		return showUnmappedPositions;
	}

	public void setShowUnmappedPositions(boolean showUnmappedPositions) {
		this.showUnmappedPositions = showUnmappedPositions;
	}


	
}
