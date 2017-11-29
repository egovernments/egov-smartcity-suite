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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>
<s:if test="basicProperty.recoveries.size()>0">
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<s:text name="recoveryDet" />
				</div>
			</td>
		</tr>
		<tr>
				<td colspan="5" class="bluebgheadtd">

				<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tablebottom">
        	 <tr> 
        	 		<th class="bluebgheadtd"><s:text name="slno" /> </th>
        	 		<th class="bluebgheadtd"><s:text name="recovery.start.date" /> </th>
        			 <th class="bluebgheadtd"><s:text name="recovery.status"/></th></tr>
        	 <s:iterator var="s" value="basicProperty.recoveries" status="status">
			<tr>
				<td class="greybox"><div align="center"><s:property value="#status.index+1" /></div></td>
				 <s:date name="intimationNotice.createdDate" var="createdDateId" format="dd/MM/yyyy" />
				 <td  class="greybox" ><div align="center"> <s:property default="N/A" value="%{createdDateId}" /></div></td>
			<td class="greybox" ><div align="center">	
			 <a href="${pageContext.request.contextPath}/recovery/recovery!viewDetails.action?model.id=<s:property value="%{id}"/>">
			<s:if test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_NOTICE155CREATED)">
				<s:text name="notice155.created" />
        	</s:if>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_NOTICE155GENERATED)">
				<s:text name="notice155.issued" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_WARRANTPREPARED)">
				<s:text name="warrantApp.created" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_WARRANTAPPROVED)">
				<s:text name="warrantApp.approved" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_WARRANTNOTICECREATED)">
				<s:text name="notice156.issued" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_WARRANTNOTICEISSUED)">
				<s:text name="notice156.created" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_CEASENOTICECREATED)">
				<s:text name="notice159.created" />
        	</s:elseif>
        	<s:elseif test="status.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@RECOVERY_CEASENOTICEISSUED)">
				<s:text name="notice159.issued" />
        	</s:elseif>
			 </a> </div>
			 </td>	
			

			</tr></s:iterator>
			</table>
			</td>
			</tr>
</s:if>
