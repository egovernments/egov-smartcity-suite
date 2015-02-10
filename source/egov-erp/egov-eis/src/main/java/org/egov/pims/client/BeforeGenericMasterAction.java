package org.egov.pims.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.service.EmployeeService;

public class BeforeGenericMasterAction extends DispatchAction
{

	public final static Logger LOGGER = Logger.getLogger(BeforeGenericMasterAction.class.getClass());
	private EmployeeService employeeService;
    public BeforeGenericMasterAction()
    {
    }

    public ActionForward beforeCreate(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String target = "";
        try
        {
            saveToken(req);
            populate(req);
           
            req.getSession().setAttribute(STR_VIEWMODE, "create");
            String className = "";
            className = req.getParameter(STR_CLASSNAME).trim();
            req.getSession().setAttribute(STR_CLASSNAME, className);
            LOGGER.debug((new StringBuilder(">>> inside beforCreate")).append(className).toString());
            target = "createScreen";
            LOGGER.debug((new StringBuilder(">>> inside beforCreate")).append(target).toString());
        }
        catch(Exception e)
        {
            target = STR_ERROR;
            LOGGER.error(e.getMessage());
            throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    public ActionForward beforeModify(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String target = "";
        try
        {
            LOGGER.debug(">>> inside beforeModify"+req.getParameter("submitType"));
            populate(req);
            String className = "";
            className = req.getParameter(STR_CLASSNAME).trim();
            req.getSession().setAttribute(STR_CLASSNAME, className);
            target = "modify";
            req.getSession().setAttribute("mode", "modify");
        }
        catch(Exception e)
        {
            target = STR_ERROR;
            LOGGER.error(e.getMessage());
            throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    public ActionForward beforeView(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String target = "";
        try
        {
            populate(req);
            String className = "";
            className = req.getParameter(STR_CLASSNAME).trim();
            req.getSession().setAttribute(STR_CLASSNAME, className);
            target = "view";
            req.getSession().setAttribute("mode", "view");
        }
        catch(Exception e)
        {
            target = STR_ERROR;
            LOGGER.error(e.getMessage());
            throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    public ActionForward beforeDelete(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String target = "";
        try
        {
            String className = "";
            className = req.getParameter(STR_CLASSNAME).trim();
            req.getSession().setAttribute(STR_CLASSNAME, className);
            target = "delete";
            populate(req);
            req.getSession().setAttribute("mode", "delete");
        }
        catch(Exception e)
        {
            target = STR_ERROR;
            LOGGER.error(e.getMessage());
            throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    public ActionForward setIdForDetails(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        String target = "";
        try
        {
            LOGGER.debug((new StringBuilder(">>> req.getParameter(Id) ")).append(req.getParameter("Id")).append(" mode  ").append(req.getParameter(STR_VIEWMODE)).toString());
            req.getSession().setAttribute("Id", req.getParameter("Id"));
            req.getSession().setAttribute(STR_VIEWMODE, req.getParameter(STR_VIEWMODE));
            String className = "";
            className = req.getParameter(STR_CLASSNAME).trim();
            req.getSession().setAttribute(STR_CLASSNAME, className);
            target = "createScreenSubmit";
        }
        catch(Exception e)
        {
            target = STR_ERROR;
            LOGGER.error(e.getMessage());
            throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    private void populate(HttpServletRequest req)
    {
        try {
			HashMap genericMap = new HashMap();
			ArrayList statusMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeStatusMaster");
			genericMap.put("EmployeeStatusMaster", employeeService.getMapForList(statusMasterList));
			ArrayList gradeMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-GradeMaster");
			genericMap.put("GradeMaster", employeeService.getMapForList(gradeMasterList));
			ArrayList bloodGroupList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-BloodGroupMaster");
			genericMap.put("BloodGroupMaster", employeeService.getMapForList(bloodGroupList));
			ArrayList catMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-CategoryMaster");
			genericMap.put("CategoryMaster", employeeService.getMapForList(catMasterList));
			ArrayList commMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-CommunityMaster");
			genericMap.put("CommunityMaster", employeeService.getMapForList(commMasterList));
			ArrayList langKnownMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesKnownMaster");
			genericMap.put("LanguagesKnownMaster", employeeService.getMapForList(langKnownMasterList));
			ArrayList langQualiMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesQulifiedMaster");
			genericMap.put("LanguagesQulifiedMaster", employeeService.getMapForList(langQualiMasterList));
			ArrayList recruimentMasterMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-RecruimentMaster");
			genericMap.put("RecruimentMaster", employeeService.getMapForList(recruimentMasterMasterList));
			ArrayList religionMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-ReligionMaster");
			genericMap.put("ReligionMaster", employeeService.getMapForList(religionMasterList));
			ArrayList typeOfRecMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TypeOfRecruimentMaster");
			genericMap.put("TypeOfRecruimentMaster", employeeService.getMapForList(typeOfRecMasterList));
			genericMap.put("genericTable", getMap());
			genericMap.put("genericName", getNameMap());
			req.getSession().setAttribute("genericMap", genericMap);
		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
    }

   

    private Map getMap()
    {
        try {
			HashMap m = new HashMap();
			
			m.put("ReligionMaster", "EGEIS_RELIGION_MSTR");
			m.put("LanguagesQulifiedMaster", "EGEIS_LOCAL_LANG_QUL_MSTR");
			m.put("LanguagesKnownMaster", "EGEIS_LANGUAGES_KNOWN_MSTR");
			m.put("CommunityMaster", "EGEIS_COMMUNITY_MSTR");
			m.put("CategoryMaster", "EGEIS_CATEGORY_MSTR");
			m.put("BloodGroupMaster", "EGEIS_BLOODGROUP");
			m.put("GradeMaster", "EGEIS_GRADE_MSTR");
			m.put("TypeOfRecruimentMaster", "EGEIS_RECRUITMENT_TYPE_MSTR");
			m.put("RecruimentMaster", "EGEIS_MODE_OF_RECRUIMENT_MSTR");
			m.put("EmployeeStatusMaster", "EGEIS_STATUS_MASTER");
			return m;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
    }

    private Map getNameMap()
    {
        try {
			HashMap m = new HashMap();
			
			m.put("ReligionMaster", "Religion");
			m.put("LanguagesQulifiedMaster", "Local Languages Qualified");
			m.put("LanguagesKnownMaster", "Languages Known");
			m.put("CommunityMaster", "Community");
			m.put("CategoryMaster", "Category");
			m.put("BloodGroupMaster", "Blood Group");
			m.put("GradeMaster", "Grade");
			m.put("TypeOfRecruimentMaster", "AttendenceType Of Recruiment");
			m.put("RecruimentMaster", "Mode Of Recruiment");
			m.put("EmployeeStatusMaster", "Employee Status");
			return m;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
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
	
	private static final String STR_VIEWMODE="viewMode";
	private static final String STR_CLASSNAME="className";
	private static final String STR_EXCEPTION= "Exception:";
	private static final String STR_ERROR="error";

}
