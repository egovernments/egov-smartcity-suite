<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br/>
<table width="100%">
	<tr>
		<td colspan="5" class="headingwk">
			<div class="subheadnew text-left">
				<s:text name='license.title.motordetail' />
			</div>
		</td>
	</tr>
</table>
<table width="50%" border="1" cellspacing="0" cellpadding="0" align="center">
<br/>
	<tr>
		<th class="greybox">
			<s:text name='license.horsepower' />
		</th>
		<th class="greybox">
			<s:text name="license.noofmachines" />
		</th>
	</tr>
	<s:iterator var="p" value="installedMotorList">
		<tr>
			<td text-align="center" class="greybox">
				<s:property value="#p.hp" />
			</td>
			<td text-align="center" class="greybox">
				<s:property value="#p.noOfMachines" />
			</td>

		</tr>
	</s:iterator>
	<tr>
		<td align="center" class="greybox">
			<b><s:text name="license.total.horsepower" /> </b>
		</td>
		<td align="center" class="greybox">
			<b><s:property value="totalHP" /> </b>
		</td>
	</tr>
</table>



