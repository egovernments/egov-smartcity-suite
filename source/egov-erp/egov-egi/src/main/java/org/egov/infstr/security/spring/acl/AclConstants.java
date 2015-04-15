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
package org.egov.infstr.security.spring.acl;


public final class AclConstants {
public static String GET_ACL_OBJID="getAclByObjId";
public static String GET_ACL_SIDTYPE="getAclSidType";
public static String GET_ACL_OBJCLASS="getAclObjClass";

public static String GET_USER_BYID="getUserById";
public static String GET_ROLE_BYID="getRoleById"; 

public static String GET_USERS_BYIDS="findUsersByIds";
public static String GET_ROLES_BYIDS="findRolesByIds";
public static String GET_EMPS_BYIDS="findEmpviewsByIds";
public static final String GET_GROUPS_BYIDS="findGroupsByIds";

public static final String GET_EMPBYUSERID="getEmpByUserId";


public static String GETMASKFORACLENTRY="getMaskForAclEntry";
public static String GET_ACL_SID_FORUSER="getAclSidForUser";
public static String GET_ACL_SID_FORROLE="getAclSidsForRole";
public static String GET_ACL_SID_FOREMP="getAclSidsForEmp";
public static String GET_ACL_SID_FORGROUP="getAclSidsForGroup";

public static String CLASS_GENERIC_FILE="org.egov.dms.models.GenericFile";
public static String SIDUSER="USER";
public static String SIDROLE="ROLE";
public static String SIDEMP="EMPLOYEE";
public static String SIDGROUP="GROUP";
public static String SIDWORKFLOW="WORKFLOW";

public static final String PERMISSIONACTION="PermissionsAction";
public static final String GETEMPLOYEES="getEmployees";

}
