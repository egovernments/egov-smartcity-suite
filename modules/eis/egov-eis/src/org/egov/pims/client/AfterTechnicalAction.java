package org.egov.pims.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.dao.SkillMasterDAO;
import org.egov.pims.dao.TechnicalGradesMasterDAO;
import org.egov.pims.model.DeptTests;
import org.egov.pims.model.EduDetails;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.HowAcquiredMaster;
import org.egov.pims.model.ImmovablePropDetails;
import org.egov.pims.model.MovablePropDetails;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.SkillMaster;
import org.egov.pims.model.TechnicalGradesMaster;
import org.egov.pims.model.TecnicalQualification;
import org.egov.pims.model.TestNameMaster;
import org.egov.pims.service.EmployeeService;

public class AfterTechnicalAction extends DispatchAction  
{
	public final static Logger LOGGER = Logger.getLogger(AfterTechnicalAction.class.getClass());
	private EmployeeService employeeService;
	
	
	public ActionForward saveDetailsTechnical(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		try
		{
			 PIMSForm pIMSForm  = (PIMSForm)form;
			 PersonalInformation egpimsPersonalInformation=(PersonalInformation)req.getAttribute("employeeob");
			 setEducationDetails(pIMSForm,egpimsPersonalInformation,req);
			 setTecnicalQualification(pIMSForm,egpimsPersonalInformation,req);
			 setDeptTests(egpimsPersonalInformation,req);
			 //set property details
			 setPropertydiscriptionImm(pIMSForm,egpimsPersonalInformation,req);
			 setPropertydiscriptionMov(pIMSForm,egpimsPersonalInformation,req);
			 
			 target = "success";
			 alertMessage="Executed successfully";
		
		}
		catch(Exception ex)
		{
		   target = "error";
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	public ActionForward modifyDetailsTechnical(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{
		String target =null;
		String alertMessage=null;
		
			try {
				PIMSForm pIMSForm  = (PIMSForm)form;
				String setName="delEdu";
				String setTech = "delTeck";
				String setdelDep="delDep";
				String setDelImm="delImm";
				String setDelMov = "delMov";
				PersonalInformation egpimsPersonalInformation =null;
				if( req.getAttribute("employeeob") != null)
				{
					 egpimsPersonalInformation=(PersonalInformation)req.getAttribute("employeeob");
				}
				
				//education delete set
				Set<Integer> delSet=(Set)req.getSession().getAttribute(setName);
				
				//Technical delete set
				Set<Integer> delTechSet=(Set)req.getSession().getAttribute(setTech);
				
				//Department delete set
				Set<Integer> delDeptSet=(Set)req.getSession().getAttribute(setdelDep);
				
				//Immovable Property delete set
				Set<Integer> delImmSet=(Set)req.getSession().getAttribute(setDelImm);
				
				//Movable Property set
				Set<Integer> delMovableSet=(Set)req.getSession().getAttribute(setDelMov);
				
				 setEducationDetails(pIMSForm,egpimsPersonalInformation,req);
				 setTecnicalQualification(pIMSForm,egpimsPersonalInformation,req);
				 setDeptTests(egpimsPersonalInformation,req);
				 
				//set property details
				 setPropertydiscriptionImm(pIMSForm,egpimsPersonalInformation,req);
				 setPropertydiscriptionMov(pIMSForm,egpimsPersonalInformation,req);
				 
				
					if(delSet!=null)
					{
						deleteRecord(delSet,egpimsPersonalInformation,setName);
			        }
					
					if(delTechSet!=null)
					{
						deleteRecord(delTechSet,egpimsPersonalInformation,setTech);
			        }
					
					if(delDeptSet!=null)
					{
						deleteRecord(delDeptSet,egpimsPersonalInformation,setdelDep);
			        }
					if(delImmSet!=null)
					{
						deleteRecord(delImmSet,egpimsPersonalInformation,setDelImm);
			        }
					if(delMovableSet!=null)
					{
						deleteRecord(delMovableSet,egpimsPersonalInformation,setDelMov);
			        }
					
			    //remove the set attribute for education
				req.getSession().removeAttribute(setName);
				
				//remove the set attribute for technical Qualification
				req.getSession().removeAttribute(setTech);
				
				//remove the set attribute for department
				req.getSession().removeAttribute(setdelDep);
				
				//remove the set attribute for Immovable Property Details
				req.getSession().removeAttribute(setDelImm);
				
				//remove the set attribute for Movable Property Details
				req.getSession().removeAttribute(setDelMov);
				
				target = "successUpdateEmployee";
				alertMessage="Executed successfully";
			} catch(Exception ex)
			{
				   target = "error";
				   LOGGER.error(ex.getMessage());
				   throw new EGOVRuntimeException(ex.getMessage(),ex);
				}
		
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
}
	
	private void deleteRecord(Set<Integer> delSet,PersonalInformation egpimsPersonalInformation,String TypeOfsetDelete)
	{
		try {
			
			if("delEdu".equals(TypeOfsetDelete))
			{
				for(Integer eduId:delSet)
				{
					EduDetails eduDetObj = employeeService.getEduDetailsById(eduId);
					egpimsPersonalInformation.removeEduDetails(eduDetObj);
				}
			}
			else if("delTeck".equals(TypeOfsetDelete))
			{
				for(Integer techId:delSet)
				{
					TecnicalQualification techDelObj = employeeService.getTecnicalQualificationById(techId);
					egpimsPersonalInformation.removeTecnicalQualification(techDelObj);
				}
			}
			else if("delDep".equals(TypeOfsetDelete))
			{
				for(Integer delDeptId:delSet)
				{
					DeptTests deptTestObj = employeeService.getDeptTestsById(delDeptId);
				    egpimsPersonalInformation.removeDeptTests(deptTestObj);
				}
			}
			else if("delImm".equals(TypeOfsetDelete))
			{
				for(Integer delimmovableId:delSet)
				{
					ImmovablePropDetails delImmovableObj = employeeService.getImmovablePropDetailsById(delimmovableId);
				    egpimsPersonalInformation.removeImmovablePropDetailses(delImmovableObj);
				}
			}
			else if("delMov".equals(TypeOfsetDelete))
			{
				for(Integer delmovableId:delSet)
				{
					MovablePropDetails delmovableObj = employeeService.getMovablePropDetailsById(delmovableId);
				    egpimsPersonalInformation.removeMovablePropDetails(delmovableObj);
				}
			}
			
		} catch (Exception e) {
			
			LOGGER.error(e);
		}
	}
	
	
	
	private void setEducationDetails(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String educationDetailsId[]=req.getParameterValues("educationDetailsId");
			String[] qulification=null;
			qulification =(String[])pIMSForm.getQulification();
			int ArrLenEq = educationDetailsId.length;
			if(qulification[0]!=null && !qulification[0].equals(""))
			{
				for (int len = 0; len < ArrLenEq; len++)
				{
					if(qulification[len]!=null && !qulification[len].equals(""))
					{

						EduDetails egpimsEduDetails =null;

						if(educationDetailsId[len].equals("0"))
						{
							egpimsEduDetails =new EduDetails();
							egpimsEduDetails.setEmployeeId(egpimsPersonalInformation);
							setEduDetails( egpimsEduDetails, pIMSForm, len);
							egpimsPersonalInformation.getEgpimsEduDetails().add(egpimsEduDetails);
							//getEisManagr().addEduDetails(egpimsPersonalInformation,egpimsEduDetails);
						}
						else
						{
							egpimsEduDetails = employeeService.getEduDetailsById(Integer.valueOf(educationDetailsId[len]).intValue());
							setEduDetails(egpimsEduDetails, pIMSForm, len);
							//getEisManagr().updateEduDetails(egpimsEduDetails);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	private void setTecnicalQualification(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String tecnicalQualificationId[]=req.getParameterValues("tecnicalQualificationId");
			String[] skillId=null;
			skillId =(String[])pIMSForm.getSkillId();
			int ArrLenTQ = tecnicalQualificationId.length;
			if(!skillId[0].equals("0"))
			{

				for (int len = 0; len < ArrLenTQ; len++)
				{
					if(!skillId[len].equals("0"))
					{
						TecnicalQualification egpimsTecnicalQualification =null;

						if(tecnicalQualificationId[len].equals("0"))
						{
							egpimsTecnicalQualification=new TecnicalQualification();
							egpimsTecnicalQualification.setEmployeeId(egpimsPersonalInformation);
							setTecnicalQualificationLoop(egpimsTecnicalQualification, pIMSForm, len,req);
							egpimsPersonalInformation.getEgpimsTecnicalQualification().add(egpimsTecnicalQualification);
							//getEisManagr().addTecnicalQualification(egpimsPersonalInformation,egpimsTecnicalQualification);
						}
						else
						{

							egpimsTecnicalQualification = employeeService.getTecnicalQualificationById(Integer.valueOf(tecnicalQualificationId[len]).intValue());
							setTecnicalQualificationLoop(  egpimsTecnicalQualification, pIMSForm, len,req);
							//getEisManagr().updateTecnicalQualification(egpimsTecnicalQualification);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private void setDeptTests(PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String deptTestsId[]=req.getParameterValues("deptTestsId");
			String nameOfTheTestId[]= null;
			nameOfTheTestId =req.getParameterValues("nameOfTheTestId");
			int ArrLennmtst = deptTestsId.length;
			if(!nameOfTheTestId[0].equals("0"))
			{
				for (int len = 0; len < ArrLennmtst; len++)
				{
					if(!nameOfTheTestId[len].equals("0"))
					{

						DeptTests egpimsDeptTests =null;
						if(deptTestsId[len].equals("0"))
						{
							egpimsDeptTests =new DeptTests();
							egpimsDeptTests.setEmployeeId(egpimsPersonalInformation);
							setDeptTestsLoop(  egpimsDeptTests, len,req);
							egpimsPersonalInformation.getEgpimsDeptTests().add(egpimsDeptTests);
							
						}
						else
						{
							egpimsDeptTests = employeeService.getDeptTestsById(Integer.valueOf(deptTestsId[len]).intValue());
							setDeptTestsLoop(  egpimsDeptTests, len,req);
							//getEisManagr().updateDeptTests(egpimsDeptTests);
						}
					}
				}

			 }
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
	}
	
	private void setEduDetails(EduDetails  egpimsEduDetails,PIMSForm pIMSForm,int len)
	{
		try {
			String[] qulification=null;
			String[] majorSubject=null;
			String[] monthandYearOfPass=null;
			String[] universityBoard=null;
			String[] eduDocNo = null;
			qulification =(String[])pIMSForm.getQulification();
			majorSubject =(String[])pIMSForm.getMajorSubject();
			monthandYearOfPass =(String[])pIMSForm.getMonthandYearOfPass();
			universityBoard=(String[])pIMSForm.getUniversityBoard();
			eduDocNo=(String[])pIMSForm.getEduDocNo();
			if(qulification[len]!=null&&!qulification[len].equals(""))
			{
				egpimsEduDetails.setQulificationMaster(qulification[len]);
			}
			if(majorSubject[len]!=null&&!majorSubject[len].equals(""))
			{
				egpimsEduDetails.setMajorSubject(majorSubject[len]);
			}
			if(monthandYearOfPass[len]!=null&&!monthandYearOfPass[len].equals(""))
			{
				egpimsEduDetails.setMonthYearPass(getDateString(monthandYearOfPass[len]));
			}
			if(universityBoard[len]!=null&&!universityBoard[len].equals(""))
			{
				egpimsEduDetails.setUniBoard(universityBoard[len]);
			}
			if(eduDocNo!=null && !eduDocNo[len].equals("")){
				egpimsEduDetails.setDocNo(Long.valueOf(eduDocNo[len]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	
	private java.util.Date getDateString(String dateString)
	{

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			java.util.Date date = null;
			try
			{
				date = dateFormat.parse(dateString);
	
			} catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
			}
			return date;
	}
	private void setTecnicalQualificationLoop(TecnicalQualification  egpimsTecnicalQualification,PIMSForm pIMSForm,int len,HttpServletRequest req)
	{
		try {
			String[] skillId=null;
			String[] yearOfPassTQ=null;
			String[] gradeId1=null;
			String[] remarks=null;
			String[] techDocNo = null;
			remarks=(String[])pIMSForm.getRemarks();
			skillId =(String[])pIMSForm.getSkillId();
			yearOfPassTQ =(String[])pIMSForm.getYearOfPassTQ();
			techDocNo=(String[])pIMSForm.getTechDocNo();
			gradeId1=req.getParameterValues("gradeId1");
			if(!skillId[len].equals("0"))
			{
				SkillMasterDAO skillMasterAO = new SkillMasterDAO();
				SkillMaster skillMaster = skillMasterAO.getSkillMaster(Integer.valueOf(skillId[len]).intValue());
				egpimsTecnicalQualification.setSkillMaster(skillMaster);
			}
			if(gradeId1!=null && !gradeId1[len].equals("0"))
			{
				TechnicalGradesMasterDAO technicalGradesMasterAO = new TechnicalGradesMasterDAO();
				TechnicalGradesMaster TechnicalGradesMaster = technicalGradesMasterAO.getTechnicalGradesMaster(Integer.valueOf(gradeId1[len]).intValue());
				egpimsTecnicalQualification.setTechnicalGradesMaster(TechnicalGradesMaster);
			}
			if(yearOfPassTQ[len]!=null && !yearOfPassTQ[len].equals(""))
			{
				egpimsTecnicalQualification.setPassedDate(getDateString(yearOfPassTQ[len]));
			}
 
			if(remarks[len]!=null &&!remarks[len].equals(""))
			{
				egpimsTecnicalQualification.setRemarks(remarks[len]);
			}
			if(techDocNo!=null && !techDocNo[len].equals("")){
				egpimsTecnicalQualification.setDocNo(Long.valueOf(techDocNo[len]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	private void setDeptTestsLoop(DeptTests  egpimsDeptTests,int len,HttpServletRequest req)
	{
		try {
			String[] nameOfTheTestId=null;
			String[] monthandYearOfPassDT=null;

			nameOfTheTestId =req.getParameterValues("nameOfTheTestId");
			monthandYearOfPassDT =req.getParameterValues("monthandYearOfPassDT");
			if(monthandYearOfPassDT[len]!=null&&!monthandYearOfPassDT[len].equals(""))
			{
				egpimsDeptTests.setDateOfPass(getDateString(monthandYearOfPassDT[len]));
			}
			if(!nameOfTheTestId[len].equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf(nameOfTheTestId[len]).intValue(),"TestNameMaster");
				TestNameMaster testNameMaster =(TestNameMaster)genericMaster;
				egpimsDeptTests.setNameOfTestMstr(testNameMaster);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	
	private void setPropertydiscriptionImm(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String[] propertydiscriptionImm=null;
			String immPropertyDetailsId[]=req.getParameterValues("immPropertyDetailsId");
			propertydiscriptionImm =(String[])pIMSForm.getPropertydiscriptionImm();
			int ArrProImm = immPropertyDetailsId.length;

			if(propertydiscriptionImm[0]!=null&&!propertydiscriptionImm[0].equals(""))
			{
				for (int len = 0; len < ArrProImm; len++)
				{
					if(propertydiscriptionImm[len]!=null&&!propertydiscriptionImm[len].equals(""))
					{
						ImmovablePropDetails egpimsImmovablePropDetails =null;

						if(immPropertyDetailsId[len].equals("0"))
						{
							egpimsImmovablePropDetails =new ImmovablePropDetails();
							egpimsImmovablePropDetails.setEmployeeId(egpimsPersonalInformation);
							setImmovablePropDetails(  egpimsImmovablePropDetails, pIMSForm, len);
							egpimsPersonalInformation.getEgpimsImmovablePropDetailses().add(egpimsImmovablePropDetails);
							//egpimsPersonalInformation.addImmovablePropDetailses(egpimsImmovablePropDetails);
							//getEisManagr().addImmovablePropDetailses(egpimsPersonalInformation,egpimsImmovablePropDetails);
						}
						else
						{
							egpimsImmovablePropDetails = employeeService.getImmovablePropDetailsById(Integer.valueOf(immPropertyDetailsId[len]).intValue());
							setImmovablePropDetails(  egpimsImmovablePropDetails, pIMSForm, len);
							//getEisManagr().updateImmovablePropDetails(egpimsImmovablePropDetails);
						}
					}

				}

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	
	private void setImmovablePropDetails( ImmovablePropDetails egpimsImmovablePropDetails,PIMSForm pIMSForm,int len)
	{
		try {
			String[] propertydiscriptionImm=null;
			String[] placeImm=null;
			String[] howAcquiredImm=null;
			String[] presentValueImm=null;
			String[] permissionObtainedImm=null;
			String[] orderNoImm=null;
			String[] dateImm=null;
			propertydiscriptionImm =(String[])pIMSForm.getPropertydiscriptionImm();
			placeImm =(String[])pIMSForm.getPlaceImm();
			howAcquiredImm =(String[])pIMSForm.getHowAcquiredImm();
			presentValueImm =(String[])pIMSForm.getPresentValueImm();
			permissionObtainedImm =(String[])pIMSForm.getPermissionObtainedImm();
			orderNoImm =(String[])pIMSForm.getOrderNoImm();
			dateImm =(String[])pIMSForm.getDateImm();
			if(propertydiscriptionImm[len]!=null&&!propertydiscriptionImm[len].equals(""))
			{
				egpimsImmovablePropDetails.setPropertyDescription(propertydiscriptionImm[len]);
			}
			if(placeImm[len]!=null&&!placeImm[len].equals(""))
			{
				egpimsImmovablePropDetails.setPlace(placeImm[len]);
			}
			if(presentValueImm[len]!=null&&!presentValueImm[len].equals(""))
			{
				egpimsImmovablePropDetails.setPresentValue(new BigDecimal(presentValueImm[len]));
			}
			if(permissionObtainedImm[len].trim().equals("Y"))
			{
				egpimsImmovablePropDetails.setPermissionObtained(Character.valueOf('1'));
			}
			else
			{
				egpimsImmovablePropDetails.setPermissionObtained(Character.valueOf('0'));
			}
			if(orderNoImm[len]!=null&&!orderNoImm[len].equals(""))
			{
				egpimsImmovablePropDetails.setOrderNo(orderNoImm[len]);
			}
			if(dateImm[len]!=null&&!dateImm[len].equals(""))
			{
				egpimsImmovablePropDetails.setOrderDate(getDateString(dateImm[len]));
			}
			if(!howAcquiredImm[len].equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf(howAcquiredImm[len]).intValue(),"HowAcquiredMaster");
				HowAcquiredMaster howAcquiredMaster =(HowAcquiredMaster)genericMaster;
				egpimsImmovablePropDetails.setHowAcquiredMstr(howAcquiredMaster);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private void setPropertydiscriptionMov(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String movPropertyDetailsId[]=req.getParameterValues("movPropertyDetailsId");
			String[] propertydiscriptionMo=null;
			propertydiscriptionMo =(String[])pIMSForm.getPropertydiscriptionMo();
			int ArrProMo  = movPropertyDetailsId.length;
			if(propertydiscriptionMo[0]!=null&&!propertydiscriptionMo[0].equals(""))
			{
				for (int len = 0; len < ArrProMo; len++)
				{
					if(propertydiscriptionMo[len]!=null&&!propertydiscriptionMo[len].equals(""))
					{


						MovablePropDetails egpimsMovablePropDetails =null;

						if(movPropertyDetailsId[len].equals("0"))
						{
							egpimsMovablePropDetails =new MovablePropDetails();
							egpimsMovablePropDetails.setEmployeeId(egpimsPersonalInformation);
							setmovablePropDetails(  egpimsMovablePropDetails, pIMSForm, len);
							egpimsPersonalInformation.getEgpimsMovablePropDetailses().add(egpimsMovablePropDetails);
							//egpimsPersonalInformation.addMovablePropDetails(egpimsMovablePropDetails);
							//getEisManagr().addMovablePropDetails(egpimsPersonalInformation,egpimsMovablePropDetails);
						}
						else
						{
							egpimsMovablePropDetails = employeeService.getMovablePropDetailsById(Integer.valueOf(movPropertyDetailsId[len]).intValue());
							setmovablePropDetails(  egpimsMovablePropDetails, pIMSForm, len);
							//getEisManagr().updateMovablePropDetails(egpimsMovablePropDetails);
						}
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private void setmovablePropDetails(MovablePropDetails  egpimsMovablePropDetails,PIMSForm pIMSForm,int len)
	{
		try {
			String[] propertydiscriptionMo=null;
			String[] valueOfTimeOfPurchaseMo=null;
			String[] howAcquiredMov=null;
			String[] permissionObtainedMo=null;
			String[] orderNoMo=null;
			String[] dateMo=null;
			propertydiscriptionMo =(String[])pIMSForm.getPropertydiscriptionMo();
			valueOfTimeOfPurchaseMo =(String[])pIMSForm.getValueOfTimeOfPurchaseMo();
			howAcquiredMov =(String[])pIMSForm.getHowAcquiredMov();
			permissionObtainedMo =(String[])pIMSForm.getPermissionObtainedMo();
			orderNoMo =(String[])pIMSForm.getOrderNoMo();
			dateMo =(String[])pIMSForm.getDateMo();
			if(propertydiscriptionMo[len]!=null&&!propertydiscriptionMo[len].equals(""))
			{
				egpimsMovablePropDetails.setPropertyDiscription(propertydiscriptionMo[len]);
			}
			if(valueOfTimeOfPurchaseMo[len]!=null&&!valueOfTimeOfPurchaseMo[len].equals(""))
			{
				egpimsMovablePropDetails.setValAtPurchase(new BigDecimal(valueOfTimeOfPurchaseMo[len]));
			}
			if(orderNoMo[len]!=null&&!orderNoMo[len].equals(""))
			{
				egpimsMovablePropDetails.setOrderNo(orderNoMo[len]);
			}
			if(dateMo[len]!=null&&!dateMo[len].equals(""))
			{
				egpimsMovablePropDetails.setOrderDate(getDateString(dateMo[len]));
			}
			if(permissionObtainedMo[len].trim().equals("Y"))
			{
				egpimsMovablePropDetails.setPermissionObtained(Character.valueOf('1'));
			}
			else
			{
				egpimsMovablePropDetails.setPermissionObtained(Character.valueOf('0'));
			}
			if(!howAcquiredMov[len].equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf(howAcquiredMov[len]).intValue(),"HowAcquiredMaster");
				HowAcquiredMaster howAcquiredMaster =(HowAcquiredMaster)genericMaster;
				egpimsMovablePropDetails.setHowAcquiredMstr(howAcquiredMaster);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	
	/**
	 * @return the eisManagr
	 */
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	
	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	
}
