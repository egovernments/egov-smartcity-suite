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
package org.egov.pims.client;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.service.EmployeeServiceOld;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeforeGenericMasterAction extends DispatchAction
{

	public final static Logger LOGGER = Logger.getLogger(BeforeGenericMasterAction.class.getClass());
	private EmployeeServiceOld employeeService;
	
	@Autowired
	private EgovMasterDataCaching masterDataCache;
	
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
            throw new ApplicationRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
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
            throw new ApplicationRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
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
            throw new ApplicationRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
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
            throw new ApplicationRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
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
            throw new ApplicationRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
        }
        return mapping.findForward(target);
    }

    private void populate(HttpServletRequest req)
    {
        try {
			HashMap genericMap = new HashMap();
			ArrayList statusMasterList = (ArrayList)masterDataCache.get("egEmp-EmployeeStatusMaster");
			genericMap.put("EmployeeStatusMaster", employeeService.getMapForList(statusMasterList));
			ArrayList gradeMasterList = (ArrayList)masterDataCache.get("egEmp-GradeMaster");
			genericMap.put("GradeMaster", employeeService.getMapForList(gradeMasterList));
			ArrayList bloodGroupList = (ArrayList)masterDataCache.get("egEmp-BloodGroupMaster");
			genericMap.put("BloodGroupMaster", employeeService.getMapForList(bloodGroupList));
			ArrayList catMasterList = (ArrayList)masterDataCache.get("egEmp-CategoryMaster");
			genericMap.put("CategoryMaster", employeeService.getMapForList(catMasterList));
			ArrayList commMasterList = (ArrayList)masterDataCache.get("egEmp-CommunityMaster");
			genericMap.put("CommunityMaster", employeeService.getMapForList(commMasterList));
			ArrayList langKnownMasterList = (ArrayList)masterDataCache.get("egEmp-LanguagesKnownMaster");
			genericMap.put("LanguagesKnownMaster", employeeService.getMapForList(langKnownMasterList));
			ArrayList langQualiMasterList = (ArrayList)masterDataCache.get("egEmp-LanguagesQulifiedMaster");
			genericMap.put("LanguagesQulifiedMaster", employeeService.getMapForList(langQualiMasterList));
			ArrayList recruimentMasterMasterList = (ArrayList)masterDataCache.get("egEmp-RecruimentMaster");
			genericMap.put("RecruimentMaster", employeeService.getMapForList(recruimentMasterMasterList));
			ArrayList religionMasterList = (ArrayList)masterDataCache.get("egEmp-ReligionMaster");
			genericMap.put("ReligionMaster", employeeService.getMapForList(religionMasterList));
			ArrayList typeOfRecMasterList = (ArrayList)masterDataCache.get("egEmp-TypeOfRecruimentMaster");
			genericMap.put("TypeOfRecruimentMaster", employeeService.getMapForList(typeOfRecMasterList));
			genericMap.put("genericTable", getMap());
			genericMap.put("genericName", getNameMap());
			req.getSession().setAttribute("genericMap", genericMap);
		} catch (ApplicationRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationRuntimeException(STR_EXCEPTION + e.getMessage(),e);
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
			throw new ApplicationRuntimeException(STR_EXCEPTION + e.getMessage(),e);
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
			throw new ApplicationRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
    }

	/**
	 * @return the eisManagr
	 */
	public EmployeeServiceOld getEmployeeService() {
		return employeeService;
	}

	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeServiceOld employeeService) {
		this.employeeService = employeeService;
	}
	
	private static final String STR_VIEWMODE="viewMode";
	private static final String STR_CLASSNAME="className";
	private static final String STR_EXCEPTION= "Exception:";
	private static final String STR_ERROR="error";

}
