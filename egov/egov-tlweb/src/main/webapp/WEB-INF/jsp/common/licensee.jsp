<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name="licensee.applicantname" />
		<span class="mandatory"></span>
	</td>
	<td class="greybox">
		<s:textfield name="licensee.applicantName" id="applicantName" maxlength="100"/>
	<td class="greybox">
		<s:text name="licensee.nationality" />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.nationality" id="nationality" maxlength="50"/>
	</td>
</tr>
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name="licensee.fatherorspousename" />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.fatherOrSpouseName" maxlength="100" id="fatherOrSpouseName" />
	<td class="greybox">
		<s:text name="licensee.qualification" />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.qualification" id="qualification" maxlength="50" />
	</td>
</tr>
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name='licesee.age' />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.age" id="age" size="3" onKeyPress="return numbersonly(this, event)"  maxlength="3"/>
	</td>
	<td class="greybox">
		<s:text name="licensee.gender" />
		<span class="mandatory"></span>
	</td>
	<td class="greybox">
		<s:radio name="licensee.gender" list="genderList" id="gender" />
	</td>
</tr>
<tr>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		<s:text name='licensee.pannumber' />
	</td>
	<td class="greybox">
		<s:textfield name="licensee.panNumber" id="licensee.panNumber" onblur="validatePan(this)" maxlength="10"/>
	</td>
	<td class="greybox">
		&nbsp;
	</td>
	<td class="greybox">
		&nbsp;
	</td>
</tr>
