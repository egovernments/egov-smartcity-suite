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

<%@ include file="/includes/taglibs.jsp" %>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox">&nbsp;</td>
  	<td class="greybox"><s:text name="locality"/> : </td>
	<td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.locality.name}" /> </span>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="zone"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.zone.name}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="revwardno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.ward.name}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="blockno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.area.name}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="Street"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.Street.name}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="elec.wardno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.electionBoundary.name}" /> </span></td>
	    <td class="greybox"><s:text name="doorno"></s:text></td>
	    <td class="greybox">
	    	<span class="bold">
	    		<s:if test='%{houseNumber == ""}'>N/A</s:if>
	    		<s:else><s:property default="N/A" value="%{houseNumber}"/></s:else>
	    	</span>
	    </td>	
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="enumerationblock"/> : </td>
	    <td class="greybox"><span class="bold">N/A</td>
	    <td class="greybox"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{pinCode}" /> </span></td>
	</tr>
	
</div>
