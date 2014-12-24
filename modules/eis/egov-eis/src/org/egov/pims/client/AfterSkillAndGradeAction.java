/*
 * RegisterAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.client;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.dao.SkillMasterDAO;
import org.egov.pims.dao.TechnicalGradesMasterDAO;
import org.egov.pims.model.SkillMaster;
import org.egov.pims.model.TechnicalGradesMaster;






public class AfterSkillAndGradeAction extends DispatchAction
{

	
	private static final Logger LOGGER = Logger.getLogger(AfterSkillAndGradeAction.class);
	protected boolean returnToken(HttpServletRequest req)
	{
		
		if(!isTokenValid(req, true))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public ActionForward saveDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		
				String target ="success";
				String alertMessage=null;
				try
				{
					SkillVsGradeForm skillVsGradeForm = (SkillVsGradeForm)form;
					if (returnToken(req))
					{
						target = STR_ERROR;
					}
					else
					{
					
					SkillMaster skillMaster = new SkillMaster();
					SkillMasterDAO skillMasterDAO = new SkillMasterDAO();
					if(skillVsGradeForm.getSkillValue()!=null&&!skillVsGradeForm.getSkillValue().equals(""))
					{
						skillMaster.setName(skillVsGradeForm.getSkillValue());
					}
					if(skillVsGradeForm.getFromDate()!=null&&!skillVsGradeForm.getFromDate().equals(""))
					{
						skillMaster.setFromDate(getDateString(skillVsGradeForm.getFromDate()));
						
					}
					if(skillVsGradeForm.getToDate()!=null&&!skillVsGradeForm.getToDate().equals(""))
					{
						skillMaster.setToDate(getDateString(skillVsGradeForm.getToDate()));
						
					}
					String[] gradeValue=null;
					String[] frDte=skillVsGradeForm.getFromDateGrd();
					String[] toDte=skillVsGradeForm.getToDateGrd();
					
					gradeValue =skillVsGradeForm.getGradeValue();
					int ArrLenEq = gradeValue.length;
					LOGGER.debug("ArrLenEq"+ArrLenEq);
				
					if(ArrLenEq!=0)
					{

						for (int len = 0; len < ArrLenEq; len++)
						{
							
						TechnicalGradesMaster technicalGradesMaster = new TechnicalGradesMaster();
						LOGGER.debug("technicalGradesMaster"+technicalGradesMaster);
						if(gradeValue[len]!=null&&!gradeValue[len].equals(""))
						{
							technicalGradesMaster.setGradeName(gradeValue[len]);
						}
						if(frDte[len]!=null&&!frDte[len].equals(""))
						{
							technicalGradesMaster.setFromDate(getDateString(frDte[len]));
						}
						if(toDte[len]!=null&&!toDte[len].equals(""))
						{
							technicalGradesMaster.setToDate(getDateString(toDte[len]));
						}
							skillMaster.addTechnicalGradesMaster(technicalGradesMaster);
						}
					}
					LOGGER.info("skillMaster"+skillMaster.getSettechnicalGradesMaster());
					skillMasterDAO.createSkillMaster(skillMaster);

					HibernateUtil.getCurrentSession().flush();
					target = "success";
					alertMessage="Executed successfully";
				}
				}

				catch(Exception ex)
				{
				   target = STR_ERROR;
				   LOGGER.error(ex.getMessage());
				   throw new EGOVRuntimeException(ex.getMessage(),ex);
				}
				req.setAttribute("alertMessage", alertMessage);
				return mapping.findForward(target);

		
}

public ActionForward modifyDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{
	
		String target =null;
		String alertMessage=null;
		try
		{
		
		SkillMasterDAO skillMasterDAO = new SkillMasterDAO();
		SkillVsGradeForm skillVsGradeForm = (SkillVsGradeForm)form;
		String id = (String)req.getParameter("Id");
		LOGGER.info("id"+id);
		SkillMaster skillMaster = skillMasterDAO.getSkillMaster(Integer.valueOf(id).intValue());
		if(skillVsGradeForm.getSkillValue()!=null&&!skillVsGradeForm.getSkillValue().equals(""))
		{
			skillMaster.setName(skillVsGradeForm.getSkillValue());
		}
		if(skillVsGradeForm.getFromDate()!=null&&!skillVsGradeForm.getFromDate().equals(""))
		{
			skillMaster.setFromDate(getDateString(skillVsGradeForm.getFromDate()));
		}
		if(skillVsGradeForm.getToDate()!=null&&!skillVsGradeForm.getToDate().equals(""))
		{
			skillMaster.setToDate(getDateString(skillVsGradeForm.getToDate()));
		}
		TechnicalGradesMasterDAO technicalGradesMasterDAO = new TechnicalGradesMasterDAO();
		
		String gradeId[]=skillVsGradeForm.getGradeId();
		
		String[] frDte=skillVsGradeForm.getFromDateGrd();
		String[] toDte=skillVsGradeForm.getToDateGrd();
		
		String gradeValue[]=skillVsGradeForm.getGradeValue();
		
		if(gradeId!=null)
		{
			int finyearIdArr = gradeId.length;
			if(finyearIdArr!=0)
			{

				for(int i=0;i<finyearIdArr;i++)
				{
					LOGGER.info("gradeId[i]"+gradeId[i]);
					if(gradeId[i].equals("0"))
					{
						TechnicalGradesMaster technicalGradesMaster = null;
						technicalGradesMaster = new TechnicalGradesMaster();
						if(gradeValue[i]!=null && !gradeValue[i].equals(""))
						{
							technicalGradesMaster.setGradeName(gradeValue[i]);
						}
						if(frDte[i]!=null&&!frDte[i].equals(""))
						{
							technicalGradesMaster.setFromDate(getDateString(frDte[i]));
						}
						if(toDte[i]!=null&&!toDte[i].equals(""))
						{
							technicalGradesMaster.setToDate(getDateString(toDte[i]));
						}
						skillMaster.addTechnicalGradesMaster(technicalGradesMaster);

					}
					else
					{
						TechnicalGradesMaster technicalGradesMaster = null;
						technicalGradesMaster = technicalGradesMasterDAO.getTechnicalGradesMaster(Integer.valueOf(gradeId[i]).intValue());
						technicalGradesMaster.setGradeName(gradeValue[i]);
						LOGGER.info("technicalGradesMaster"+technicalGradesMaster);
						technicalGradesMasterDAO.updateTechnicalGradesMaster(technicalGradesMaster);
						
					}
					
					
				}
				
			}
		}
		//skillMasterDAO.updateSkillMaster(skillMaster);
			HibernateUtil.getCurrentSession().flush();
			target = "modifyfinal";
			alertMessage="Executed successfully";
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);

}

public ActionForward deleteDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{


		String target =null;
		String alertMessage=null;
		try
		{
			String id = (String)req.getParameter("Id");
			LOGGER.debug("id"+id);
			SkillMasterDAO skillMasterDAO = new SkillMasterDAO();
			SkillMaster skillMaster = skillMasterDAO.getSkillMaster(Integer.valueOf(id).intValue());
			/*
			Set gradeSet = null;
			if(skillMaster!=null)
			{
				if(skillMaster.getSettechnicalGradesMaster()!=null&&!skillMaster.getSettechnicalGradesMaster().isEmpty())
				{
					gradeSet = skillMaster.getSettechnicalGradesMaster();
					Iterator itr =gradeSet.iterator();
					while(itr.hasNext())
					{
						TechnicalGradesMaster technicalGradesMaster =(TechnicalGradesMaster)itr.next();
						skillMaster.removeTechnicalGradesMaster(technicalGradesMaster);

					}

				}

			}*/
		skillMasterDAO.removeSkillMaster(skillMaster);
		HibernateUtil.getCurrentSession().flush();
		target = "deletefinal";
		alertMessage="Executed successfully";
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);


}
private java.util.Date getDateString(String dateString)
{

	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d = new java.util.Date();
		try
		{
			d = dateFormat.parse(dateString);

		} catch (ParseException e)
		{
			 LOGGER.error(e.getMessage());
		}
		return d;
	
}

private final static String STR_ERROR = "error";
private final static String STR_EXCEPTION = "Exception:";

}

