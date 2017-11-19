<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<s:if test="%{billRegisterList.size()>0}">
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<td colspan="7">
								<div class="subheadsmallnew">
									<strong>List of Salary Bills</strong>
								</div>
							</td>
						</tr>
						<tr>
							<th class="bluebgheadtd" width="2%">Sl No</th>
							<th class="bluebgheadtd" width="8%">Bill/File Number</th>
							<th class="bluebgheadtd" width="10%">Bill Date</th>
							<th class="bluebgheadtd" width="10%">Department</th>
							<th class="bluebgheadtd" width="10%">Month</th>
							<th class="bluebgheadtd" width="10%">Net Pay</th>
							<th class="bluebgheadtd" width="10%">Bill Status</th>
						</tr>
						<s:iterator value="billRegisterList" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><s:property
										value="#stat.index+1" />&nbsp;</td>
								<td class="blueborderfortd"><div align="center">
										<a
											href='../bill/salaryBillRegister!view.action?billregisterId=<s:property value="id"/>'><s:property
												value="billnumber" />&nbsp;</a>
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="billdate" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="egBillregistermis.egDepartment.deptName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="egBillregistermis.month" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="billamount" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="billstatus" />
										&nbsp;
									</div></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</td>
		</tr>
	</table>
	</td>
	</tr>
	<tr>
		<input name="button" type="button" class="buttonsubmit"
			id="non-printable" value="Print" onclick="window.print()" />&nbsp;&nbsp;
	</tr>
	</table>
</s:if>
<s:else>No data found</s:else>
