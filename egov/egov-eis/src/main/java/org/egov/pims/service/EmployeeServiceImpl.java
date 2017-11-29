/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pims.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.commons.exception.TooManyValuesException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.AssignmentDAO;
import org.egov.pims.dao.GenericMasterDAO;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.model.EmployeeNamePoJo;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.SearchEmpDTO;
import org.egov.pims.model.ServiceHistory;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeServiceOld {

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean#ejbCreate
     * ()
     */
    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    private EisUtilService eisService;
    private PersistenceService persistenceService;
    private PersonalInformationDAO personalInformationDAO;
    private AssignmentDAO assignmentDAO;
    private AppConfigValueService appConfigValuesService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @Autowired
    private GenericMasterDAO genericMasterDAO;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private final static Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class);

    @Deprecated
    public List searchEmployee(Integer departmentId, Integer designationId, String code, String name, String searchAll)
            throws Exception {

        List<EmployeeView> employeeList = null;
        try {

            String mainStr = "from EmployeeView ev where";
            if (code != null && !code.equals("")) {
                mainStr += " upper(trim(ev.employeeCode)) = :employeeCode and ";
            }
            if (departmentId.intValue() != 0) {
                mainStr += " ev.deptId.id= :deptId and ";
            }
            if (designationId.intValue() != 0) {
                mainStr += " ev.desigId.designationId = :designationId and ";
            }
            if (name != null && !name.equals("")) {
                mainStr += " trim(upper(ev.employeeName))  like '%" + name.trim().toUpperCase() + "%' and ";
            }
            /*
             * if(code!=null&&!code.equals("")) { mainStr
             * +=" where upper(trim(ev.employeeCode)) = :employeeCode "; } else
             * {
             */
            if ((searchAll.equals("false") && designationId.intValue() != 0)) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.isActive = '1' ";
            } else if ((searchAll.equals("true") && designationId.intValue() != 0)) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // Inspite of SearchAll is true or false, if employee code is
            // entered, search for all active and inactive employees
            else if (code != null && !code.equals("")) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else if (searchAll.equals("true") && designationId.intValue() == 0) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // }

            Query qry = null;
            qry = getCurrentSession().createQuery(mainStr);
            LOGGER.info("qryqryqryqry" + qry.toString());
            if (code != null && !code.equals("")) {
                qry.setString("employeeCode", code.trim().toUpperCase());
            }
            if (departmentId.intValue() != 0) {
                qry.setLong("deptId", departmentId.longValue());
            }
            if (designationId.intValue() != 0) {
                qry.setInteger("designationId", designationId);
            }
            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    public List searchEmployee(Integer departmentId, Integer designationId, String code, String name, Integer status)
            throws Exception {

        List<EmployeeView> employeeList = null;
        try {

            String mainStr = "from EmployeeView ev where ev.isPrimary='Y' and ";
            if (code != null && !code.equals("")) {
                mainStr += " upper(trim(ev.employeeCode)) = :employeeCode and ";
            }
            if (departmentId.intValue() != 0) {
                mainStr += " ev.deptId.id= :deptId and ";
            }
            if (designationId.intValue() != 0) {
                mainStr += " ev.desigId.designationId = :designationId and ";
            }
            if (name != null && !name.equals("")) {
                mainStr += " trim(upper(ev.employeeName))  like '%" + name.trim().toUpperCase() + "%' and ";
            }
            if (status.intValue() != 0) {
                mainStr += " ev.employeeStatus.id = :employeeStatus and ";
            }
            /*
             * if(code!=null&&!code.equals("")) { mainStr
             * +=" where upper(trim(ev.employeeCode)) = :employeeCode "; } else
             * {
             */

            if (status.intValue() != 0 && designationId.intValue() == 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
            } else if (status.intValue() == 0 && designationId.intValue() != 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // Inspite of SearchAll is true or false, if employee code is
            // entered, search for all active and inactive employees
            else if (code != null && !code.equals("")) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else if (status.intValue() != 0 || designationId.intValue() == 0) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // }

            Query qry = null;
            qry = getCurrentSession().createQuery(mainStr);
            LOGGER.info("qryqryqryqry" + qry.toString());
            if (code != null && !code.equals("")) {
                qry.setString("employeeCode", code.trim().toUpperCase());
            }
            if (departmentId.intValue() != 0) {
                qry.setInteger("deptId", departmentId);
            }
            if (designationId.intValue() != 0) {
                qry.setInteger("designationId", designationId);
            }
            if (status.intValue() != 0) {
                qry.setInteger("employeeStatus", status);
            }
            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    /*
     * search employee by department,designation,functionary,code and name
     */
    public List searchEmployee(Integer departmentId, Integer designationId, Integer functionaryId, String code,
            String name, Integer status) throws Exception {
        List<EmployeeView> employeeList = null;
        try {

            String mainStr = "from EmployeeView ev where  ";
            if (code != null && !code.equals("")) {
                mainStr += " upper(trim(ev.employeeCode)) = :employeeCode and ";
            }
            if (departmentId.intValue() != 0) {
                mainStr += " ev.deptId.id= :deptId and ";
            }
            if (designationId.intValue() != 0) {
                mainStr += " ev.desigId.designationId = :designationId and ";
            }
            if (functionaryId.intValue() != 0) {
                mainStr += " ev.functionary.id = :functionaryId and ";
            }

            if (name != null && !name.equals("")) {
                mainStr += " trim(upper(ev.employeeName))  like '%" + name.trim().toUpperCase() + "%' and ";
            }
            if (status.intValue() != 0) {
                mainStr += " ev.employeeStatus.id = :employeeStatus and ";
            }
            /*
             * if(code!=null&&!code.equals("")) { mainStr
             * +=" where upper(trim(ev.employeeCode)) = :employeeCode "; } else
             * {
             */

            if (status.intValue() != 0 && designationId.intValue() == 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
            } else if (status.intValue() == 0 && designationId.intValue() != 0) {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // Inspite of SearchAll is true or false, if employee code is
            // entered, search for all active and inactive employees
            else if (code != null && !code.equals("")) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else if (status.intValue() != 0 || designationId.intValue() == 0) {
                mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
            } else {
                mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
            }
            // }

            Query qry = null;
            qry = getCurrentSession().createQuery(mainStr);
            LOGGER.info("qryqryqryqry" + qry.toString());
            if (code != null && !code.equals("")) {
                qry.setString("employeeCode", code.trim().toUpperCase());
            }
            if (departmentId.intValue() != 0) {
                qry.setInteger("deptId", departmentId);
            }
            if (designationId.intValue() != 0) {
                qry.setInteger("designationId", designationId);
            }
            if (functionaryId.intValue() != 0) {
                qry.setInteger("functionaryId", functionaryId);
            }
            if (status.intValue() != 0) {
                qry.setInteger("employeeStatus", status);
            }
            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    /**
     * @param designationId
     * @param code
     * @param name
     * @param status
     * @param empType
     * @param finParams
     *            department,functionary,function optional
     * @return
     * @throws Exception
     */
    public List<EmployeeView> searchEmployee(Integer designationId, String code, String name, Integer status,
            Integer empType, Map<String, Integer> finParams) throws Exception {

        List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
        Integer departmentId = finParams.get("departmentId") == null ? 0 : finParams.get("departmentId");
        Integer functionaryId = finParams.get("functionaryId") == null ? 0 : finParams.get("functionaryId");
        Integer functionId = finParams.get("functionId") == null ? 0 : finParams.get("functionId");
        Integer userId = finParams.get("userId") == null ? 0 : finParams.get("userId");
        Integer isActive = finParams.get("isActive") == null ? 0 : finParams.get("isActive");
        try {
            Query qry = null;
            if (code != null && !code.equals("")) {
                logger.info(" Search by Code " + code);
               
                List<EmployeeView> list = persistenceService.findAllBy(
                        " from EmployeeView ev where upper(ev.employeeCode) like ? ", code);
                Iterator itr = list.iterator();
                EmployeeView ev = null;
                EmployeeView ev1 = null;
                Assignment ass = null;
                Date date = new Date();
                PersonalInformation emp = null;
                while (itr.hasNext()) {
                    ev = (EmployeeView) itr.next();
                    emp = getEmloyeeById(ev.getId().intValue());
                    break;
                }
                /*if (emp != null)
                    ass = emp.getAssignment(date,persistenceService);*/ // Returns current/latest
                                                   // primary/temp assignment as
                                                   // on current date
                Iterator itr1 = list.iterator();
                while (itr1.hasNext()) {
                    ev1 = (EmployeeView) itr1.next();

                    if (ass != null && ev1.getAssignment().equals(ass)) {
                        logger.info("ev.assId=" + ev1.getAssignment().getId());
                        logger.info("ass.assId=" + ass.getId());
                        logger.info("emp " + ev1.getName() + " : " + ev1.getPosition().getName() + " : "
                                + ev1.getPrimary());
                        break;
                    }
                }
                if (ev1 != null)
                    employeeList.add(ev1);
            } else {
                logger.info(" Search by other params ");
                String mainStr = "from EmployeeView ev where ";
                if (departmentId.intValue() != 0) {
                    mainStr += " ev.deptId.id= :deptId and ";
                }
                if (designationId.intValue() != 0) {
                    mainStr += " ev.desigId.designationId = :designationId and ";
                }
                if (functionaryId.intValue() != 0) {
                    mainStr += " ev.functionary.id = :functionaryId and ";
                }
                if (functionId != null && functionId.intValue() != 0) {
                    mainStr += " ev.functionId.id = :functionId and ";
                }

                if (name != null && !name.equals("")) {
                    mainStr += " trim(upper(ev.employeeName))  like '%" + name.trim().toUpperCase() + "%' and ";
                }
                if (status.intValue() != 0) {
                    mainStr += " ev.employeeStatus.id = :employeeStatus and ";
                }
                if (empType.intValue() != 0) {
                    mainStr += " ev.employeeType.id=:employeeType and ";
                }
                if (isActive.intValue() != 0) {
                    mainStr += " ev.userMaster.active=:isActive  and ";
                }
                if (userId.intValue() != 0)// to query the employee using
                                           // employee username
                {
                    mainStr += " ev.userMaster.id= :userId and ";
                }
                mainStr += " upper(ev.isPrimary)='Y' AND ((  ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR ( ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) "
                        + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                        + " WHERE evn.id = ev.id  AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                        + " ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))) ";

                qry = getCurrentSession().createQuery(mainStr);
                if (departmentId.intValue() != 0) {
                    qry.setInteger("deptId", departmentId);
                }
                if (designationId.intValue() != 0) {
                    qry.setInteger("designationId", designationId);
                }
                if (functionaryId.intValue() != 0) {
                    qry.setInteger("functionaryId", functionaryId);
                }
                if (status.intValue() != 0) {
                    qry.setInteger("employeeStatus", status);
                }
                if (empType.intValue() != 0) {
                    qry.setInteger("employeeType", empType);
                }
                if (isActive.intValue() != 0) {
                    qry.setInteger("isActive", isActive);
                }
                if (userId.intValue() != 0) {
                    qry.setInteger("userId", userId);
                }
                employeeList = (List) qry.list();
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    /*
     * public List searchEmployeeForNominees(Integer departmentId,Integer
     * designationId,Integer functionaryId,String code,String name,Integer
     * status,Integer empType)throws Exception { //session =
     * null; try { String mainStr =
     * "select distinct ev from EmployeeView ev,EmployeeNomineeMaster enm where ev.id=enm.employeeId.id and "
     * ; if(code!=null&&!code.equals("")) { mainStr
     * +=" upper(trim(ev.employeeCode)) = :employeeCode and "; name=""; }
     * if(departmentId.intValue() != 0) { mainStr
     * +=" ev.deptId.id= :deptId and "; } if(designationId.intValue() != 0) {
     * mainStr += " ev.desigId.designationId = :designationId and "; }
     * if(functionaryId.intValue() !=0) { mainStr +=
     * " ev.functionary.id = :functionaryId and "; } if(name!= null &&
     * !name.equals("")) { mainStr +=
     * " trim(upper(ev.employeeName))  like :empName and "; }
     * if(status.intValue() != 0 ) { mainStr +=
     * " ev.employeeStatus.id = :employeeStatus and "; }
     * if(empType.intValue()!=0) { mainStr +=
     * " ev.employeeType.id=:employeeType and "; } mainStr +=
     * "  ((ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (upper(ev.isPrimary)='Y' and ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) "
     * +
     * " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
     * +
     * " WHERE evn.id = ev.id AND upper(ev.isPrimary)='Y' AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
     * +
     * " ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))"
     * + " ) "; Query qry = null; qry =
     * getCurrentSession().createQuery(mainStr);
     * LOGGER.info("qryqryqryqry"+qry.toString());
     * if(code!=null&&!code.equals("")) { qry.setString("employeeCode",
     * code.trim().toUpperCase()); } if(name!= null && !name.equals("")) {
     * qry.setString("empName","%"+name.trim().toUpperCase()+"%"); }
     * if(departmentId.intValue() != 0) { qry.setInteger("deptId",
     * departmentId); } if(designationId.intValue() != 0) {
     * qry.setInteger("designationId", designationId); }
     * if(functionaryId.intValue() != 0) { qry.setInteger("functionaryId",
     * functionaryId); } if(status.intValue() != 0) {
     * qry.setInteger("employeeStatus", status); } if(empType.intValue() != 0) {
     * qry.setInteger("employeeType",empType); } employeeList =
     * (List)qry.list(); } catch (HibernateException he) { LOGGER.error(he);
     * ApplicationRuntimeException("Exception:" + he.getMessage(),he); } catch
     * throw new ApplicationRuntimeException("Exception:" + he.getMessage(),he); }
     * return employeeList; }
     */

    @Deprecated
    /**
     *
     * Api to get the employee with primary assignment for currentdate.
     * If there is no Assignment for the current date then it pick's up the employee with  lastest assignment ie: Maximum( From Date)
     * @param departmentId
     * @param designationId
     * @param functionaryId
     * @param code
     * @param name
     * @param status
     * @param empType
     * @return List of EmployeeView for current or Lastest Assignment.
     * @throws Exception
     */
    public List searchEmployee(Integer departmentId, Integer designationId, Integer functionaryId, String code,
            String name, Integer status, Integer empType) throws Exception {

        List<EmployeeView> employeeList = null;
        try {

            String mainStr = "from EmployeeView ev where ";
            if (code != null && !code.equals("")) {
                mainStr += " upper(trim(ev.employeeCode)) = :employeeCode and ";
                name = "";
            }
            if (departmentId.intValue() != 0) {
                mainStr += " ev.deptId.id= :deptId and ";
            }
            if (designationId.intValue() != 0) {
                mainStr += " ev.desigId.designationId = :designationId and ";
            }
            if (functionaryId.intValue() != 0) {
                mainStr += " ev.functionary.id = :functionaryId and ";
            }

            if (name != null && !name.equals("")) {
                mainStr += " trim(upper(ev.employeeName))  like :empName and ";
            }
            if (status.intValue() != 0) {
                mainStr += " ev.employeeStatus.id = :employeeStatus and ";
            }
            if (empType.intValue() != 0) {
                mainStr += " ev.employeeType.id=:employeeType and ";
            }

            mainStr += "  ((ev.toDate IS NULL AND ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (upper(ev.isPrimary)='Y' and ev.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')) "
                    + " OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   "
                    + " WHERE evn.id = ev.id AND upper(ev.isPrimary)='Y' AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND "
                    + " ((evn2.toDate  IS NULL AND evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy')) OR (evn2.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') AND evn2.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy'))) )))) ";

            Query qry = null;
            qry = getCurrentSession().createQuery(mainStr);
            LOGGER.info("qryqryqryqry" + qry.toString());
            if (code != null && !code.equals("")) {
                qry.setString("employeeCode", code.trim().toUpperCase());
            }
            if (departmentId.intValue() != 0) {
                qry.setInteger("deptId", departmentId);
            }
            if (name != null && !name.equals("")) {
                qry.setString("empName", "%" + name.trim().toUpperCase() + "%");
            }
            if (designationId.intValue() != 0) {
                qry.setInteger("designationId", designationId);
            }
            if (functionaryId.intValue() != 0) {
                qry.setInteger("functionaryId", functionaryId);
            }
            if (status.intValue() != 0) {
                qry.setInteger("employeeStatus", status);
            }
            if (empType.intValue() != 0) {
                qry.setInteger("employeeType", empType);
            }
            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    public List<EmployeeView> searchEmployeeByGrouping(LinkedList<String> groupingByOrder) throws Exception {
        List<EmployeeView> employeeList = null;
        try {
            String mainStr = "from EmployeeView ev where ";

            String orderByStr = "";
            for (int i = 0; i < groupingByOrder.size(); i++) {
                if (groupingByOrder.get(i).toString().equals("FundCode")) {
                    if (!orderByStr.equals("")) {
                        orderByStr = orderByStr + ", ";
                    }
                    orderByStr = orderByStr + " ev.assignment.fundId.code";
                } else if (groupingByOrder.get(i).toString().equals("FunctionCode")) {
                    if (!orderByStr.equals("")) {
                        orderByStr = orderByStr + ", ";
                    }
                    orderByStr = orderByStr + " ev.assignment.functionId.code";
                } else if (groupingByOrder.get(i).toString().equals("DeptCode")) {
                    if (!orderByStr.equals("")) {
                        orderByStr = orderByStr + ", ";
                    }
                    orderByStr = orderByStr + " ev.assignment.deptId.deptCode";
                }
            }

            if (!orderByStr.equals("")) {
                orderByStr = " order by " + orderByStr;
            }
            mainStr += " ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.assignment.isPrimary='Y' ";

            mainStr = mainStr + orderByStr;

            Query qry = null;
            qry = getCurrentSession().createQuery(mainStr);

            LOGGER.info("Query in search Employee by grouping==" + qry.toString());

            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            LOGGER.error("Exception ===" + he.getMessage());
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception e) {
            LOGGER.error("Exception ===" + e.getMessage());
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);

        }
        return employeeList;
    }

    public List searchEmployee(Integer empId) throws Exception {
        ArrayList<SearchEmpDTO> dataElCol = new ArrayList<SearchEmpDTO>();
        List employeeList = null;

        try {

            String mainStr = "select ev.employeeCode,ev.employeeName,ev.id,ev.desigId.designationId, ev.deptId.id ,ev.fromDate,ev.toDate from EmployeeView ev where ev.id = :empId";
            Query qry = getCurrentSession().createQuery(mainStr);

            if (empId.intValue() != 0) {
                qry.setInteger("empId", empId);

            }
            employeeList = qry.list();

            for (Iterator iter = employeeList.iterator(); iter.hasNext();) {
                Object[] objArray = (Object[]) iter.next();
                addEmployee(objArray, dataElCol);
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }

        return dataElCol;
    }

    private void addEmployee(Object[] objArray, ArrayList<SearchEmpDTO> dataElColt) {
        try {
            int len = objArray.length;

            Integer id = Integer.valueOf(0);
            String empCode = "";
            String fName = "";
            Integer desid = null;
            Integer deptId = null;
            java.sql.Date fromDate = null;
            java.sql.Date toDate = null;

            for (int i = 0; i < len; i++) {

                if (i == 0) {
                    empCode = (String) objArray[i];

                } else if (i == 1) {
                    fName = (String) objArray[i];

                } else if (i == 2) {
                    id = (Integer) objArray[i];

                } else if (i == 3) {
                    desid = (Integer) objArray[i];

                } else if (i == 4) {
                    deptId = (Integer) objArray[i];

                } else if (i == 5) {
                    fromDate = (java.sql.Date) objArray[i];

                } else if (i == 6) {
                    toDate = (java.sql.Date) objArray[i];

                }
                if (i == 6) {
                    dataElColt.add(new SearchEmpDTO(desid, deptId, empCode, fName, id, fromDate, toDate));
                }
            }
        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public Assignment getAssignmentByEmpAndDate(Date date, Integer empId) {
        Assignment assignment = null;
        try {

            if (empId != null) {
                String mainStr = " select 	ev.assignment  from EmployeeView ev  where ev.assignment.isPrimary = 'Y' and ev.id = :empId and ((ev.toDate is null and ev.fromDate <= :date1 ) OR (ev.fromDate <= :date2 AND ev.toDate >= :date3 ))";
                Query qry = getCurrentSession().createQuery(mainStr);
                qry.setInteger("empId", empId);
                qry.setDate("date1", new java.sql.Date(date.getTime()));
                qry.setDate("date2", new java.sql.Date(date.getTime()));
                qry.setDate("date3", new java.sql.Date(date.getTime()));

                if (qry.list() != null && !qry.list().isEmpty()) {
                    assignment = (Assignment) qry.list().get(0);
                }
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;
    }

    public Assignment getLatestAssignmentForEmployee(Integer empId) {
        // changing sysdate to current date of java
        Date currDate = new Date();
        Assignment assignment = null;
        try {
            String mainStr = "";
            mainStr = " select ev.assignment from EmployeeView ev where ev.assignment.isPrimary = 'Y' and ev.id = :empId and ((ev.toDate is null and ev.fromDate <= :sysDate ) OR (ev.fromDate <= :sysDate AND ev.toDate >= :sysDate))";
            Query qry = getCurrentSession().createQuery(mainStr);

            if (empId != null) {
                qry.setInteger("empId", empId);
                qry.setDate("sysDate", new java.sql.Date(currDate.getTime()));
            }
            if (qry.list() != null && !qry.list().isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    assignment = (Assignment) iter.next();
                }
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;
    }

    /*
     * public List getListByAppNoAndMeMoNo(String applicationNumber ,String
     * chargeMemoNo,Integer empId) { List<DisciplinaryPunishment> list = new
     * ArrayList<DisciplinaryPunishment>(); //session =
     * "select dp.disciplinaryPunishmentId  from DisciplinaryPunishment dp,PersonalInformation pi  where  dp.employeeId = pi.idPersonalInformation and dp.employeeId = :empId "
     * ; if(chargeMemoNo!=null&&!chargeMemoNo.equals("")) mainStr
     * +=" and upper(trim(dp.chargeMemoNo)) = :chargeMemoNo ";
     * if(applicationNumber!=null&&!applicationNumber.equals("")) mainStr
     * +=" and upper(trim(dp.applicationNumber)) = :applicationNumber "; Query
     * qry = getCurrentSession().createQuery(mainStr); if(empId != null) {
     * qry.setInteger("empId", empId); }
     * if(chargeMemoNo!=null&&!chargeMemoNo.equals("")) {
     * qry.setString("chargeMemoNo", chargeMemoNo); }
     * if(applicationNumber!=null&&!applicationNumber.equals("")) {
     * qry.setString("applicationNumber", applicationNumber); }
     * if(qry.list()!=null&&!qry.list().isEmpty()) { Integer desigId = null;
     * for(Iterator iter = qry.list().iterator();iter.hasNext();) { desigId =
     * (Integer)iter.next(); list.add(getDisciplinaryPunishmentById(desigId)); }
     * } } catch (HibernateException he) { LOGGER.error(he);
     * ApplicationRuntimeException("Exception:" + he.getMessage(),he); } catch
     * throw new ApplicationRuntimeException("Exception:" + he.getMessage(),he); }
     * return list; }
     */

    /*
     * public List<SearchEmpDTO> getHistoryOfEmpForCurrentFinY(Integer
     * empId,java.util.Date givenDate) { List<SearchEmpDTO> list = new
     * ArrayList<SearchEmpDTO>(); CFinancialYear financialYear=null;
     * CalendarYear calYear=null; SimpleDateFormat smt = new
     * SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault()); String finId = null;
     * java.util.Date stFyDate=null; Date myGivenDate = givenDate; //String
     * finId = EisManagersUtill.getCommonsManager().getCurrYearFiscalId();
     * if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased()) {
     * if(myGivenDate==null) { myGivenDate = new Date(); finId
     * =smt.format(myGivenDate); } else { finId =smt.format(myGivenDate); }
     * calYear
     * =EisManagersUtill.getEmpLeaveService().getCalendarYearById(Long.valueOf
     * (finId)); stFyDate = calYear.getStartingDate(); } else { //fix for leave
     * report encahshed leave type where from date will be null
     * if(myGivenDate==null) { myGivenDate = new Date(); finId =
     * smt.format(myGivenDate); } else { finId = smt.format(myGivenDate); }
     * financialYear =
     * EisManagersUtill.getCommonsService().findFinancialYearById
     * (Long.valueOf(finId)); stFyDate = financialYear.getStartingDate(); }
     * String mainStr =
     * "select ev.desigId.designationId, ev.fromDate,ev.toDate  from EmployeeView ev  where ev.id = :empId and  ( ev.toDate is null or ev.toDate >= :startingFy ) and  ev.fromDate <  :givenDate "
     * ; Query qry = getCurrentSession().createQuery(mainStr); if(empId != null)
     * { qry.setInteger("empId", empId); qry.setDate("startingFy", new
     * java.sql.Date(stFyDate.getTime())); qry.setDate("givenDate",
     * myGivenDate); } List listOfHistory = qry.list();
     * if(qry.list()!=null&&!qry.list().isEmpty()) { for(Iterator iter =
     * listOfHistory.iterator();iter.hasNext();) { Object [] objArray = (Object
     * [])iter.next();
     * addListEmployeeHistory(empId,objArray,list,myGivenDate,stFyDate); } } }
     * catch (HibernateException he) { LOGGER.error(he);
     * ApplicationRuntimeException("Exception:" + he.getMessage(),he); } catch
     * throw new ApplicationRuntimeException("Exception:" + he.getMessage(),he); }
     * return list; }
     */

    /*
     * public List getHistoryOfEmpForGivenFinY(Integer empId,java.util.Date
     * givenDate,CFinancialYear financialYear) { List<SearchEmpDTO> list = new
     * ArrayList<SearchEmpDTO>(); //String finId =
     * EisManagersUtill.getCommonsManager().getCurrYearFiscalId(); //session =
     * = EisManagersUtill.getCommonsManager().findFinancialYearById(new
     * Long(finId)); java.util.Date stFyDate = financialYear.getStartingDate();
     * String mainStr =
     * "select ev.desigId.designationId, ev.fromDate,ev.toDate  from EmployeeView ev  where ev.id = :empId and  ( ev.toDate is null or ev.toDate >= :startingFy ) and  ev.fromDate <  :givenDate "
     * ; Query qry = getCurrentSession().createQuery(mainStr); if(empId != null)
     * { qry.setInteger("empId", empId); qry.setDate("startingFy", new
     * java.sql.Date(stFyDate.getTime())); qry.setDate("givenDate", givenDate);
     * } List listOfHistory = qry.list();
     * if(qry.list()!=null&&!qry.list().isEmpty()) { for(Iterator iter =
     * listOfHistory.iterator();iter.hasNext();) { Object [] objArray = (Object
     * [])iter.next();
     * addListEmployeeHistory(empId,objArray,list,givenDate,stFyDate); } } }
     * catch (HibernateException he) { LOGGER.error(he);
     * ApplicationRuntimeException("Exception:" + he.getMessage(),he); } catch
     * throw new ApplicationRuntimeException("Exception:" + he.getMessage(),he); }
     * return list; }
     */

    /*
     * private void addListEmployeeHistory(Integer empId,Object []
     * objArray,List<SearchEmpDTO> l,java.util.Date givenDate,java.util.Date
     * stFyDate) { try { int len = objArray.length; java.sql.Date todesDate
     * =null; java.sql.Date fromdesDate =null; Integer desig =null; for(int
     * i=0;i<len;i++) { if(i==1) { fromdesDate =(java.sql.Date)objArray[i]; }
     * else if(i==0) { desig =(Integer)objArray[i]; } else if(i==2) { todesDate
     * =(java.sql.Date)objArray[i]; if(todesDate==null) { todesDate = new
     * java.sql.Date(givenDate.getTime()); } else {
     * if(todesDate.getTime()>givenDate.getTime()) { todesDate = new
     * java.sql.Date(givenDate.getTime()); } } if(fromdesDate.getTime() <
     * stFyDate.getTime()) { fromdesDate = new
     * java.sql.Date(stFyDate.getTime()); } l.add(new
     * SearchEmpDTO(desig,Integer.
     * valueOf(0),"","",Integer.valueOf(empId),fromdesDate,todesDate)); } } }
     * catch (Exception e) {  LOGGER.error(e);
     * ApplicationRuntimeException("Exception:" + e.getMessage(),e); } }
     */

    public PersonalInformation getEmployeeforPosition(Position pos) {
        User uerImpl = null;
        PersonalInformation personalInformation = new PersonalInformation();
        try {

            String mainStr = "";
            mainStr = " select 	id  from EG_EIS_EMPLOYEEINFO ev  where ev.POS_ID = :pos and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date > SYSDATE))";
            Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("id", IntegerType.INSTANCE);
            ;

            if (pos != null) {
                qry.setEntity("pos", pos);
            }
            if (qry.list() != null && !qry.list().isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    Integer id = (Integer) iter.next();
                    personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
                }
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return personalInformation;

    }

    public Position getPositionforEmp(Integer empId) {
        Position position = null;
        List list = null;
        try {

            String mainStr = "";
            mainStr = " select 	POS_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.ID = :empId and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date >= SYSDATE))";
            Query qry = getCurrentSession().createSQLQuery(mainStr).addScalar("POS_ID", IntegerType.INSTANCE);

            if (empId != null) {
                qry.setInteger("empId", empId);
            }
            list = qry.list();
            if (list != null && !list.isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    Integer id = (Integer) iter.next();
                    if (id != null)
                        position = EisManagersUtill.getEisCommonsService().getPositionById(id);
                }
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return position;

    }

    /*
     * public boolean checkSanctionNoForDisciplinary(String sanctionNo) {
     * qry = getCurrentSession().createQuery(
     * "select dp.id from  DisciplinaryPunishmentApproval dp where upper(dp.sanctionNo) = :sanctionNo "
     * ); if(sanctionNo != null ) { qry.setString("sanctionNo", sanctionNo); }
     * if(qry.list()!=null&&!qry.list().isEmpty()) { Object obj = null;
     * for(Iterator iter = qry.list().iterator();iter.hasNext();) { obj =
     * (Object)iter.next(); b = true; } } return b; }
     */

    public boolean checkDuplication(String name, String className) {

        boolean b = false;
        try {
            Query qry = getCurrentSession()
                    .createQuery("from " + className + " CA where trim(upper(CA.name)) = :name ");
            qry.setString("name", name);
            Iterator iter = qry.iterate();

            if (iter.hasNext()) {

                b = true;
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return b;
    }

    List appliedDisiplinaryApplications = new ArrayList();
    List rejectedDisiplinaryApplications = new ArrayList();
    List approvedDisiplinaryApplications = new ArrayList();

    public PersonalInformation getEmpForUserId(Long userId) {
        return personalInformationDAO.getPersonalInformationByUserId(userId);
    }

    public void createAssignment(Assignment egEmpAssignment) {
        try {
            assignmentDAO.create(egEmpAssignment);
        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation) {
       try {
            if (egpimsPersonalInformation != null) {
                personalInformationDAO.create(egpimsPersonalInformation);
                Accountdetailtype accountdetailtype = (accountdetailtypeHibernateDAO.getAccountdetailtypeByName("Employee"));
                Accountdetailkey adk = new Accountdetailkey();
                adk.setAccountdetailtype(accountdetailtype);
                adk.setGroupid(1);
                adk.setDetailkey(egpimsPersonalInformation.getIdPersonalInformation());
                adk.setDetailname(accountdetailtype.getAttributename());
                accountdetailkeyHibernateDAO.create(adk);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }

        return egpimsPersonalInformation;
    }

    public void updateEmloyee(PersonalInformation egpimsPersonalInformation) {
        try {
            if (egpimsPersonalInformation != null)
                personalInformationDAO.update(egpimsPersonalInformation);
        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public PersonalInformation getEmloyeeById(Integer employeeId) {
        PersonalInformation egpimsPersonalInformation = null;
        egpimsPersonalInformation = personalInformationDAO.getPersonalInformationByID(employeeId);
        return egpimsPersonalInformation;
    }

    public GenericMaster getGenericMaster(Integer masterId, String masterName) {
        GenericMaster genericMaster = null;
        genericMaster = (GenericMaster) genericMasterDAO.getGenericMaster(masterId.intValue(), masterName);
        return genericMaster;
    }

    public Assignment getAssignmentById(Integer assignmentId) {
        Assignment assignment = null;
        assignment = assignmentDAO.getAssignmentById(assignmentId);
        return assignment;
    }

    public void updateAssignment(Assignment assignment) {
        try {
            if (assignment != null)
                assignmentDAO.update(assignment);
        } catch (RuntimeException e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public void addLangKnown(PersonalInformation personalInformation, LangKnown langKnown) {
        try {
            personalInformation.addLangKnown(langKnown);
        } catch (RuntimeException e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public List getListOfEmpforDept(Integer deptId) {
        PersonalInformation personalInformation = null;
        List listOfEmpOfSameDept = new ArrayList();
        try {
            Collection employeeList = searchEmployee(Integer.valueOf(deptId), Integer.valueOf(0), "", "", "false");
            if (!employeeList.isEmpty()) {
                Iterator iter = employeeList.iterator();
                while (iter.hasNext()) {
                    EmployeeView cataEl = (EmployeeView) iter.next();
                    personalInformation = (PersonalInformation) getEmloyeeById(cataEl.getId().intValue());
                    listOfEmpOfSameDept.add(personalInformation);

                }
            }

        } catch (Exception e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return listOfEmpOfSameDept;
    }

    public List getListOfEmpforDesignation(Integer desigId) {

        PersonalInformation personalInformation = null;
        List listOfEmpOfSameDesig = new ArrayList();

        try {
            Collection employeeList = searchEmployee(Integer.valueOf(0), Integer.valueOf(desigId), "", "", "false");
            if (!employeeList.isEmpty()) {
                Iterator iter = employeeList.iterator();
                while (iter.hasNext()) {
                    EmployeeView cataEl = (EmployeeView) iter.next();
                    personalInformation = (PersonalInformation) getEmloyeeById(cataEl.getId().intValue());
                    listOfEmpOfSameDesig.add(personalInformation);

                }
            }

        } catch (Exception e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return listOfEmpOfSameDesig;
    }

    public EmployeeNamePoJo getNameOfEmployee(Integer empId) {
        try {
            EmployeeNamePoJo employeeNamePoJo = null;
            PersonalInformation personalInformation = getEmloyeeById(Integer.valueOf(empId));
            String fn = "";
            String mn = "";
            String ln = "";
            if (personalInformation.getEmployeeFirstName() != null) {
                fn = personalInformation.getEmployeeFirstName();
            }
            if (personalInformation.getEmployeeMiddleName() != null) {
                mn = personalInformation.getEmployeeMiddleName();
            }
            if (personalInformation.getEmployeeLastName() != null) {
                ln = personalInformation.getEmployeeLastName();
            }
            employeeNamePoJo = new EmployeeNamePoJo(fn, mn, ln);
            return employeeNamePoJo;
        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }

    }

    public Map getAllPIMap() {
        return personalInformationDAO.getAllPIMap();
    }

    public Map getMapForList(List list) {
        Map<Integer, String> retMap = new LinkedHashMap<Integer, String>();
        try {

            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Object object = (Object) iter.next();
                Class classObj = object.getClass();

                Field id = classObj.getField("id");
                Field name = classObj.getField("name");
                retMap.put((Integer) id.get(object), (String) name.get(object));

            }
        } catch (NoSuchFieldException nfe) {
            throw new ApplicationRuntimeException("Exception:" + nfe.getMessage(), nfe);
        } catch (IllegalAccessException iac) {
            throw new ApplicationRuntimeException("Exception:" + iac.getMessage(), iac);
        }
        return retMap;

    }

    /**
     * Returns Map for a given list
     * 
     * @param list
     * @return
     */
    public Map getMapForList(List list, String fieldName1, String fieldName2) {
        Map<Integer, String> retMap = new LinkedHashMap<Integer, String>();
        try {
            String id = null;
            String name = null;
            Long longObj = null;
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Object object = (Object) iter.next();

                id = (String) BeanUtils.getProperty(object, fieldName1);
                name = (String) BeanUtils.getProperty(object, fieldName2);
                if (id != null) {
                    retMap.put(Integer.valueOf(id), (String) name);
                }
            }
        } catch (IllegalAccessException iac) {

            throw new ApplicationRuntimeException("Exception:" + iac.getMessage(), iac);
        } catch (InvocationTargetException e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return retMap;

    }

    public void deleteLangKnownForEmp(PersonalInformation personalInformation) {
        try {
            personalInformationDAO.deleteLangKnownForEmp(personalInformation);
        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
    }

    public Integer getNextVal() {

        Integer id = Integer.valueOf(0);
        try {
            Query qry = getCurrentSession().createSQLQuery("SELECT SEQ_DIS_APP.nextval as id from dual").addScalar(
                    "id", IntegerType.INSTANCE);

            if (qry.list() != null && !qry.list().isEmpty()) {
                Integer obj = null;
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    obj = (Integer) iter.next();
                    id = obj;
                }

            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return id;
    }

    private String getNextValForCode() {

        Integer id = Integer.valueOf(0);
        try {
            Query qry = getCurrentSession()
                    .createSQLQuery(
                            "SELECT CODE AS id FROM EG_EMPLOYEE emp  WHERE emp.CODE =(SELECT MAX(code) FROM EG_EMPLOYEE )  FOR UPDATE ")
                    .addScalar("id", IntegerType.INSTANCE);
            if (qry.list() != null && !qry.list().isEmpty()) {
                Integer obj = null;
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    obj = (Integer) iter.next();
                    if (obj == null) {
                        id = 1;
                    } else {
                        id = obj + 1;
                    }
                }
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return String.valueOf(id);
    }

    public String getEmployeeCode() {
        LOGGER.info("getNextValForCode" + getNextValForCode());
        return getNextValForCode();
    }

    public boolean checkPos(Integer posId, Date fromDate, Date toDate, Integer empId, String isPrimary) {

        boolean b = false;

        try {
            Query qry = null;

            if (fromDate != null && toDate != null) {
                String main = "from Assignment ev  where ev.isPrimary =:isPrimary and ev.position.id = :posId and ";
                if (empId != null) {
                    main += "ev.employee.idPersonalInformation <>:empId and ";
                }
                main += "((ev.toDate is null ) or " + " (ev.fromDate <= :fromDate and ev.toDate >= :toDate) or "
                        + " (ev.toDate <= :toDate and ev.toDate >= :fromDate) or "
                        + " (ev.fromDate >= :fromDate and ev.fromDate <= :toDate))  ";

                qry = getCurrentSession().createQuery(main);

            } else if (fromDate != null && toDate == null) {
                qry = getCurrentSession()
                        .createQuery(
                                "from Assignment ev  where ev.position.id = :posId and ((ev.toDate is null ) or (ev.fromDate <= :fromDate AND ev.toDate >= :fromDate))");

            }
            if (posId != null) {
                qry.setInteger("posId", posId);

            }
            if (empId != null) {
                qry.setInteger("empId", empId);

            }
            if (isPrimary != null) {
                qry.setCharacter("isPrimary", Character.valueOf(isPrimary.charAt(0)));
            }
            if (fromDate != null && toDate != null) {
                qry.setDate("fromDate", new java.sql.Date(fromDate.getTime()));
                qry.setDate("toDate", new java.sql.Date(toDate.getTime()));

            } else if (fromDate != null && toDate == null) {
                qry.setDate("fromDate", new java.sql.Date(fromDate.getTime()));
            }

            if (qry.list() != null && !qry.list().isEmpty()) {
                b = true;
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return b;

    }

    public List getListOfPersonalInformationByEmpIdsList(List empIdsList) {
        List list = null;
        try {
            if (empIdsList != null && !empIdsList.isEmpty()) {
                list = (List) personalInformationDAO.getListOfPersonalInformationByEmpIdsList(empIdsList);
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return list;
    }

    public List getListOfEmployeeWithoutAssignment(Date fromdate) {
        List list = null;
        try {

            list = (List) assignmentDAO.getListOfEmployeeWithoutAssignment(fromdate);

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return list;
    }

    public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId, Date todate) throws Exception {
        Assignment assignment = null;
        try {

            assignment = (Assignment) assignmentDAO.getLatestAssignmentForEmployeeByToDate(empId, todate);

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;
    }

    /**
     * This is used for workflow Getting employee by passing dept,desig,boundary
     * 
     * @param deptId
     * @param designationId
     * @param Boundaryid
     * @return employee
     * @throws TooManyValuesException
     * @throws NoSuchObjectException
     */
    public PersonalInformation getEmployee(Integer deptId, Integer designationId, Long Boundaryid)
            throws TooManyValuesException, NoSuchObjectException {
        PersonalInformation personalInformation = null;

        try {
            if (deptId != 0 && deptId != null && designationId != 0 && designationId != null && Boundaryid != 0
                    && Boundaryid != null) {
                personalInformation = personalInformationDAO.getEmployee(deptId, designationId, Boundaryid);
            }

        } catch (Exception e) {
            throw new ApplicationRuntimeException("system.error", e);
        }
        return personalInformation;
    }

    /**
     * This is used for workflow Getting employee by passing
     * dept,desig,boundary,functionary
     * 
     * @param deptId
     * @param designationId
     * @param Boundaryid
     * @return employee
     * @throws TooManyValuesException
     * @throws NoSuchObjectException
     */
    public PersonalInformation getEmployeeByFunctionary(Long deptId, Long designationId, Long Boundaryid,
            Integer functionaryId) throws TooManyValuesException, NoSuchObjectException {
        PersonalInformation personalInformation = null;

        try {
            if (deptId != 0 && deptId != null && designationId != 0 && designationId != null && Boundaryid != 0
                    && Boundaryid != null && functionaryId != 0 && functionaryId != null) {
                personalInformation = personalInformationDAO.getEmployeeByFunctionary(deptId, designationId,
                        Boundaryid, functionaryId);
            }

        } catch (Exception e) {
            throw new ApplicationRuntimeException("system.error", e);
        }
        return personalInformation;
    }

    public Assignment getLastAssignmentByEmp(Integer empId) {
        Assignment assignment = null;
        try {
            String mainStr = "";
            mainStr = "select ev.assignment from EmployeeView ev  where ev.id = :empId and nvl(ev.toDate,sysdate) in(select max(nvl(ev1.toDate,sysdate)) from EmployeeView ev1 where ev1.id = :empId)";
            Query qry = getCurrentSession().createQuery(mainStr);
            if (empId != null) {
                qry.setInteger("empId", empId);
            }
            if (qry.list() != null && !qry.list().isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    assignment = (Assignment) iter.next();
                    break;
                }
            }
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;
    }

    public ServiceHistory getServiceId(Integer id) {
        Query qry = getCurrentSession().createQuery("from ServiceHistory S where S.idService =:id ");
        qry.setInteger("id", id);
        return (ServiceHistory) qry.uniqueResult();

    }

    public List<PersonalInformation> getAllEmpByGrade(Integer gradeId) {
        List<PersonalInformation> empList = null;
        try {

            empList = personalInformationDAO.getAllEmpByGrade(gradeId);

        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return empList;
    }

    public PersonalInformation getEmpForPositionAndDate(Date dateEntered, Integer posId) throws Exception {

        PersonalInformation personalInformation = null;
        try {
            Query qry = null;
            if (dateEntered != null) {
                qry = getCurrentSession()
                        .createQuery(
                                "select ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <= :fromDate AND ev.toDate >= :fromDate))");

            } else if (dateEntered == null) {
                qry = getCurrentSession()
                        .createQuery(
                                "select ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <=  TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >=  TO_DATE(SYSDATE,'dd-MM-yyy')))");
            }
            if (posId != null) {
                qry.setInteger("posId", posId);

            }
            if (dateEntered != null) {
                qry.setDate("fromDate", new java.sql.Date(dateEntered.getTime()));
            }

            if (qry.list() != null && !qry.list().isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    Integer id = (Integer) iter.next();
                    personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
                }
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }

        return personalInformation;
    }

    /**
     * Returns list of employees for a given position and date
     * 
     * @param dateEntered
     * @param posId
     * @return
     * @throws Exception
     */
    public List<PersonalInformation> getEmpListForPositionAndDate(Date dateEntered, Integer posId) throws Exception {

        PersonalInformation personalInformation = null;
        List<PersonalInformation> empList = null;
        try {
            Query qry = null;
            if (dateEntered != null) {
                qry = getCurrentSession()
                        .createQuery(
                                "select distinct ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <= :fromDate AND ev.toDate >= :fromDate))");

            } else if (dateEntered == null) {
                qry = getCurrentSession()
                        .createQuery(
                                "select distinct ev.id from EmployeeView ev  where ev.position = :posId and ((ev.toDate is null ) or (ev.fromDate <=  TO_DATE(SYSDATE,'dd-MM-yyy') AND ev.toDate >=  TO_DATE(SYSDATE,'dd-MM-yyy')))");
            }
            if (posId != null) {
                qry.setInteger("posId", posId);

            }
            if (dateEntered != null) {
                qry.setDate("fromDate", new java.sql.Date(dateEntered.getTime()));
            }

            if (qry.list() != null && !qry.list().isEmpty()) {
                empList = new ArrayList();
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    Integer id = (Integer) iter.next();
                    personalInformation = EisManagersUtill.getEmployeeService().getEmloyeeById(id);
                    empList.add(personalInformation);
                }
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }

        return empList;
    }

    /**
     * return all distinct Designations to which employees are assigned in the
     * given department for current date. This list includes primary as well as
     * secondary assignments If there is No Designation for the given department
     * then returns the empty list
     * 
     * @param departmentId
     * @param givenDate
     * @return DesignationMaster List
     */
    public List getAllDesignationByDept(Integer deptId) {

        Integer departmentId = deptId;
        List<Designation> designationMstrObj = new ArrayList<Designation>();
       
        designationMstrObj = (List<Designation>) eisService.getAllDesignationByDept(departmentId, new Date());
        return designationMstrObj;
    }

    /**
     * Returns the list of active users who are assigned to the given
     * designation. The designation can be a primary or temporary assignment of
     * the user. The API does not currently check for active assignments
     * 
     * @param DesgId
     * @return the list of active users who are assigned to the given
     *         designation.
     * @throws Exception
     */

    public List getAllActiveUsersByGivenDesg(Integer DesgId) throws Exception {
        List userList = null;
        try {
            if (DesgId != null && DesgId != 0) {
                userList = personalInformationDAO.getAllActiveUsersByGivenDesg(DesgId);

            }

        } catch (Exception e) {
            throw new ApplicationRuntimeException("system.error", e);
        }
        return userList;
    }

    /**
     * Returns the list of employees (EmployeeView) who have a temporary
     * assignment as on the given date assigned to the given position
     * 
     * @param givenDate
     * @param posId
     * @return
     */
    public List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate, Integer posId) {

        List list = null;
        try {

            list = (List) assignmentDAO.getEmployeeWithTempAssignment(givenDate, posId);

        } catch (HibernateException he) {

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }

        return list;

    }

    /**
     * Returns the list of employees (EmployeeView) who have a temporary
     * assignment as on the given date assigned to the given position and has
     * the employee code equal to the given code
     * 
     * @param givenDate
     * @param posId
     * @return
     */
    public List<EmployeeView> getEmployeeWithTempAssignment(String code, Date givenDate, Integer posId) {

        List list = null;
        try {

            list = (List) assignmentDAO.getEmployeeWithTempAssignment(code, givenDate, posId);

        } catch (HibernateException he) {

            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }

        return list;

    }

    /**
     * Returns a list of temporary Assignments as on given date for employees
     * that have the given code and position. If any of the parameters are null,
     * the parameter is ignored. For instance, if givenDate is null, all
     * temporary assignments for employee with given code and when assigned to
     * givenPosition will be returned
     * 
     * @param code
     * @param givenDate
     * @param posId
     * @return List of Assignment
     */
    public List getEmpTempAssignment(String code, Date givenDate, Integer posId) {

        List assignment = null;
        try {
            String mainStr = "";
            mainStr = "from Assignment ev  where ev.isPrimary='N'";

            if (code != null && !code.equals("")) {
                mainStr += "and ev.employee.employeeCode =:code ";
            }
            if (givenDate != null) {
                mainStr += " and ev.fromDate <= :givenDate and ev.toDate >=:givenDate";
            }

            if (posId != null && posId != 0) {
                mainStr += " and ev.position.id =:posId ";
            }
            Query qry = getCurrentSession().createQuery(mainStr);
            if (code != null && !code.equals("")) {
                qry.setString("code", code);
            }
            if (givenDate != null) {
                qry.setDate("givenDate", givenDate);
            }
            if (posId != null && posId != 0) {
                qry.setInteger("posId", posId);
            }

            assignment = qry.list();
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;

    }

    /**
     * Returns a list of assignment ids. All assignments for the employee based
     * on employee id and that fall in the given date will be returned.
     * 
     * @param empId
     *            - Required parameter. If null is passed, the API throws an
     *            ApplicationException
     * @param givenDate
     *            . Date as on which the assignments need to be returned. If
     *            this parameter is null, the current date is considered
     * @return List of Assignment Ids
     * @throws ApplicationException
     */
    public List<Integer> getAssignmentsForEmp(Integer empId, Date givenDate) throws ApplicationException {
        List list = null;
        Query query = null;
        try {

            StringBuffer stringbuffer = new StringBuffer(
                    " select 	ASS_ID  from EG_EIS_EMPLOYEEINFO ev  where ev.ID = :empId");

            if (empId == null) {
                throw new ApplicationException("EmployeeId  Not provided");
            } else if (givenDate == null) {
                stringbuffer
                        .append(" and ((ev.to_Date is null and ev.from_Date <= SYSDATE ) OR (ev.from_Date <= SYSDATE AND ev.to_Date >= SYSDATE))");

            } else {
                stringbuffer.append(" and  ev.from_Date <= :givenDate AND ev.to_Date >= :givenDate");
            }
            query = getCurrentSession().createSQLQuery(stringbuffer.toString())
                    .addScalar("ASS_ID", IntegerType.INSTANCE);

            if (query.getQueryString().contains(":givenDate")) {
                query.setDate("givenDate", givenDate);
            }

            query.setInteger("empId", empId);

        } catch (HibernateException hibException) {
            LOGGER.error(hibException.getMessage());
            throw new ApplicationException("HibernateException:" + hibException.getMessage(), hibException);
        }

        return query.list();

    }

    /**
     * API that will return all positions for a user(temporary and permanent)
     * for a date.
     *
     * @param user
     *            . Required. User object for which the positions are queried
     * @param date
     *            Will consider current date if date is not provided
     * @return
     * @throws ApplicationException
     */

    public List<Position> getPositionsForUser(User user, Date date) throws ApplicationException {

        List<Position> positionList = new ArrayList<Position>();
        Integer pos = null;
        try {
            String mainStr = "";

            mainStr = "select a.position.id from Assignment a where a.employee.userMaster.id =:userId";

            if (date != null) {
                mainStr += " and ((a.toDate is null and a.fromDate<= :date) or (a.fromDate <= :date and a.toDate >= :date))";
            } else {
                mainStr += " and ((a.toDate is null and a.fromDate<= TO_DATE(SYSDATE,'dd-MM-yyy')) or (a.fromDate <= TO_DATE(SYSDATE,'dd-MM-yyy') and a.toDate >= TO_DATE(SYSDATE,'dd-MM-yyy')))";
            }
            Query qry = getCurrentSession().createQuery(mainStr);
            if (user != null) {
                qry.setLong("userId", user.getId());
            }
            if (date != null) {
                qry.setDate("date", date);
            }

            if (qry.list() != null && !qry.list().isEmpty()) {

                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    pos = (Integer) iter.next();
                    Position position = EisManagersUtill.getEisCommonsService().getPositionById(pos);
                    positionList.add(position);
                }
            }

        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return positionList;
    }

    /**
     * Returns a list of primary Assignments as on given date for employees that
     * have the given code and position. If any of the parameters are null, the
     * parameter is ignored. For instance, if givenDate is null, all primary
     * assignments for employee with given code and when assigned to
     * givenPosition will be returned
     *
     * @param code
     * @param givenDate
     * @param posId
     * @return
     */
    public List getEmpPrimaryAssignment(String code, Date givenDate, Integer posId) {

        List assignment = null;
        try {
            String mainStr = "";
            mainStr = "from Assignment ev  where ev.isPrimary='Y'";

            if (code != null && !code.equals("")) {
                mainStr += "and ev.employee.employeeCode =:code ";
            }
            if (givenDate != null) {
                mainStr += " and ev.fromDate <= :givenDate and ev.toDate >=:givenDate";
            }

            if (posId != null && posId != 0) {
                mainStr += " and ev.position.id =:posId ";
            }
            Query qry = getCurrentSession().createQuery(mainStr);
            if (code != null && !code.equals("")) {
                qry.setString("code", code);
            }
            if (givenDate != null) {
                qry.setDate("givenDate", givenDate);
            }
            if (posId != null && posId != 0) {
                qry.setInteger("posId", posId);
            }

            assignment = qry.list();
        } catch (HibernateException he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            LOGGER.error(he);
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        }
        return assignment;

    }

    /**
     * Api Used to get the report for employee,Retired,Deceased,Suspended based
     * on the dates. For Employed, Retired and Deceased, the employee's
     * assignment dates are considered. Whereas for Suspended status, the
     * Disciplinary Action details are considered.
     * 
     * @param status
     * @param fromDate
     * @param toDate
     * @return List of Assignment
     * @throws Exception
     */
    public List searchEmployee(Integer status, Date fromDate, Date toDate) throws Exception {
        List<Assignment> employeeList = new ArrayList<Assignment>();
        String mainStr = "";
        try {
            EgwStatus statusType = egwStatusHibernateDAO.findById(status, false);

            if (statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Employed")) {

                if (status.intValue() != 0) {
                    mainStr = "from Assignment ev where ev.employee.StatusMaster.id =:employeeStatus";
                }
                if (fromDate != null && toDate != null) {
                    /*
                     * mainStr +=
                     * " and ev.assignmentPrd.employeeId.dateOfFirstAppointment >= :fromDate "
                     * +
                     * " and ev.assignmentPrd.employeeId.dateOfFirstAppointment <= :toDate and ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate"
                     * ;
                     */
                    // enhanced to avoid to show all assignment
                    mainStr += " and ev.dateOfFirstAppointment >= :fromDate "
                            + " and ev.dateOfFirstAppointment <= :toDate "
                            + " and ((ev.fromDate<=sysdate and ev.toDate >=sysdate) "
                            + " or (ev.fromDate in (select max(prd.fromDate) from Assignment prd where ev.employee.idPersonalInformation = prd.employee.idPersonalInformation "
                            + " and not exists (select prd1.id from Assignment prd1 where prd1.employee.idPersonalInformation=ev.assignment.employee.idPersonalInformation "
                            + " and ( prd1.dateOfFirstAppointment >= :fromDate and prd1.dateOfFirstAppointment <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) ";

                }

            } else if (statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Retired")) {
                if (status.intValue() != 0) {
                    mainStr = "from Assignment ev where ev.employee.StatusMaster.id =:employeeStatus";
                }
                if (fromDate != null && toDate != null) {
                    /*
                     * mainStr +=
                     * " and ev.assignmentPrd.employeeId.retirementDate >= :fromDate "
                     * +
                     * " and ev.assignmentPrd.employeeId.retirementDate <= :toDate and ev.assignmentPrd.fromDate<=sysdate and ev.assignmentPrd.toDate >=sysdate"
                     * ;
                     */

                    // enhanced to avoid to show all assignment
                    mainStr += " and ev.assignmentPrd.employeeId.retirementDate >= :fromDate "
                            + " and ev.employee.retirementDate <= :toDate "
                            + " and ((ev.fromDate<=sysdate and ev.toDate >=sysdate) "
                            + " or (ev.fromDate in (select max(prd.fromDate) from Assignment prd where ev.employee.idPersonalInformation = prd.employee.idPersonalInformation "
                            + " and not exists (select prd1.id from Assignment prd1 where prd1.employee.idPersonalInformation=ev.employee.idPersonalInformation "
                            + " and ( prd1.employee.retirementDate >= :fromDate and prd1.employee.retirementDate <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) ";

                }
            } else if (statusType.getModuletype().equals("Employee") && statusType.getDescription().equals("Deceased")) {
                if (status.intValue() != 0) {
                    mainStr = "from Assignment ev where ev.employee.StatusMaster.id =:employeeStatus";
                }
                if (fromDate != null && toDate != null) {
                    // enhanced to avoid to show all assignment
                    mainStr += " and ev.employee.deathDate >= :fromDate "
                            + " and ev.employee.deathDate <= :toDate "
                            + " and ((ev.fromDate<=sysdate and ev.toDate >=sysdate) "
                            + " or (ev.fromDate in (select max(prd.fromDate) from Assignment prd where ev.employee.idPersonalInformation = prd.employee.idPersonalInformation "
                            + " and not exists (select prd1.id from Assignment prd1 where prd1.employee.idPersonalInformation=ev.employee.idPersonalInformation "
                            + " and ( prd1.employee.deathDate >= :fromDate and prd1.employee.deathDate <= :toDate and prd1.fromDate<= sysdate and prd1.toDate >= sysdate) )))) ";
                }
            }
            // to be handled
            /*
             * else if(statusType.getModuletype().equals("Employee") &&
             * statusType.getDescription().equals("Suspended")) {
             * if(status.intValue() != 0) { mainStr =
             * "from Assignment ev where ev.employee.StatusMaster.id =:employeeStatus"
             * ; } if(fromDate != null && toDate!=null) { mainStr +=
             * " and ev.employee.idPersonalInformation in " +
             * "(select D.employee.idPersonalInformation from DisciplinaryPunishment D where "
             * +
             * " D.dateOfSuspension >= :fromDate and D.dateOfSuspension <= :toDate ) "
             * ; } }
             */

            Query qry = null;

            qry = getCurrentSession().createQuery(mainStr);
            logger.info("Query----" + qry.toString());

            if (status.intValue() != 0) {
                qry.setInteger("employeeStatus", status);
            }
            if (fromDate != null) {
                qry.setDate("fromDate", new java.sql.Date(fromDate.getTime()));
            }

            if (toDate != null) {
                qry.setDate("toDate", new java.sql.Date(toDate.getTime()));
            }

            employeeList = (List) qry.list();

        } catch (HibernateException he) {
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);
        } catch (Exception he) {
            throw new ApplicationRuntimeException("Exception:" + he.getMessage(), he);

        }
        return employeeList;
    }

    /**
     * Returns the list of departments that the user is assigned to. This could
     * be through primary as well as temporary assignments.
     * 
     * @param userName
     * @return List of Department
     */
    public List getListOfDeptBasedOnUserDept(String userName) {
        List deptList = new ArrayList();
        try {
            Query qry = null;
            if (userName != null) {
                qry = getCurrentSession().createQuery(
                        "from Department where id in (select deptId.id from Assignment where userName=:userName "
                                + " and fromDate <= sysdate and toDate >= sysdate )");
            }

            if (userName != null) {
                qry.setString("userName", userName);
            }

            if (qry != null && qry.list() != null && !qry.list().isEmpty()) {
                for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
                    Department deptImpl = (Department) iter.next();
                    deptList.add(deptImpl);
                }
            }

        } catch (Exception e) {

            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return deptList;
    }

    public boolean isFilterByDept() {
        boolean isFilterByDept = false;
        String filterByDept = "NO";
        AppConfigValues configValue = appConfigValuesService.getAppConfigValueByDate("EIS-PAYROLL", "FILTERBYDEPT",
                new Date());
        if (null != configValue) {
            filterByDept = configValue.getValue();
            filterByDept = filterByDept.toUpperCase();
        }
        // if selfApproval is true then single step approval
        if ("YES".equals(filterByDept)) {
            isFilterByDept = true;
        }
        return isFilterByDept;
    }

    /**
     * Returns the list of Employees who are assigned to the given department
     * and designation as of current date.
     * 
     * @param deptId
     *            - Integer representing id of department
     * @param desgId
     *            - Integer representing id of designation
     * @return List of EmployeeView
     */
    public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDesg(Integer deptId, Integer desgId) {
        List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
        Criteria criteria = getCurrentSession().createCriteria(EmployeeView.class)
                .createAlias("deptId", "department").createAlias("desigId", "designation")
                .add(Restrictions.eq("department.id", deptId))
                .add(Restrictions.eq("designation.designationId", desgId))
                .add(Restrictions.and(Restrictions.le("fromDate", new Date()), Restrictions.ge("toDate", new Date())))
                .add(Restrictions.eq("isPrimary", 'Y')).add(Restrictions.eq("isActive", 1));
        return criteria.list();

    }

    /**
     * Returns the list of Employees who are assigned to the given department as
     * of given date.
     * 
     * @param deptId
     *            - Integer representing id of department
     * @param date
     *            - Given date. If null, current date is assumed.
     * @return List of EmployeeView
     */
    public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDate(Integer deptId, Date date) {
        if (date == null)
            date = new Date();
        List<EmployeeView> employeeList = new ArrayList<EmployeeView>();
        Criteria criteria = getCurrentSession().createCriteria(EmployeeView.class)
                .createAlias("deptId", "department").add(Restrictions.eq("department.id", deptId))
                .add(Restrictions.eq("isActive", 1))
                .add(Restrictions.and(Restrictions.le("fromDate", date), Restrictions.ge("toDate", date)));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    /**
     * returns all the employees who has active assignment period
     * 
     * @return
     */
    public List<PersonalInformation> getAllEmployees() {
        return getCurrentSession()
                .createQuery(
                        "" + "select distinct employee from EmployeeView empview "
                                + " where (sysdate between empview.fromDate  and empview.toDate or "
                                + " (empview.toDate is null and empview.fromDate<=sysdate))").list();
    }

    /**
     * List of users not mapped to any of the employees
     * 
     * @return empUserMapList
     */
    public List getListOfUsersNotMappedToEmp() {
        List empUserMapList = null;
        try {

            empUserMapList = personalInformationDAO.getListOfUsersNotMappedToEmp();

        } catch (Exception e) {

            LOGGER.error(e);
            throw new ApplicationRuntimeException("Exception:" + e.getMessage(), e);
        }
        return empUserMapList;
    }

    public Designation getPresentDesignation(Integer idPersonalInformation) {
        Assignment assignment = getLatestAssignmentForEmployee(idPersonalInformation);
        return assignment.getDesignation();
    }

    public EisUtilService getEisService() {
        return eisService;
    }

    public void setEisService(EisUtilService eisService) {
        this.eisService = eisService;
    }

    public PersonalInformationDAO getPersonalInformationDAO() {
        return personalInformationDAO;
    }

    public void setPersonalInformationDAO(PersonalInformationDAO personalInformationDAO) {
        this.personalInformationDAO = personalInformationDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

	public AppConfigValueService getAppConfigValuesService() {
		return appConfigValuesService;
	}

	public void setAppConfigValuesService(
			AppConfigValueService appConfigValuesService) {
		this.appConfigValuesService = appConfigValuesService;
	}

    

}
