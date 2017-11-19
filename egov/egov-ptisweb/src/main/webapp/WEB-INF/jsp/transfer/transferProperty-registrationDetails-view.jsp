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
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="registrationDetails" /></span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.sellerName"></s:text>
			:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.seller}" default="N/A" /></span></td>
		<td class="greybox"><s:text name="regst.details.buyerName" /> :</td>
		<td class="greybox"><span class="bold">
					<s:property value="%{mutationRegistrationDetails.buyer}" default="N/A" />
		</span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.doorNo" />:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.doorNo}" default="N/A" /></span></td>
		<td class="greybox"><s:text name="regst.details.address" />:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.address}" default="N/A" /></span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text
				name="regst.details.registeredPlotArea" /> :</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.plotArea}" default="N/A" /></span></td>
		<td class="greybox"><s:text
				name="regst.details.registeredPlinthArea" /> :</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.plinthArea}" default="N/A" /></span></td>
	</tr>

	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="regst.details.eastBoundary" />
			:</td>
		<td class="bluebox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.eastBoundary}" default="N/A" /></span></td>
		<td class="bluebox"><s:text name="regst.details.westBoundary" />:</td>
		<td class="bluebox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.westBoundary}" default="N/A" /></span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.northBoundary" />
			:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.northBoundary}" default="N/A" /></span></td>
		<td class="greybox"><s:text name="regst.details.southBoundary" />
			:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.southBoundary}" default="N/A" /></span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.sroName" />
			:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.sroName}" default="N/A" /></span></td>
		<s:if test="%{!@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type)}">
		<td class="greybox"><s:text name="transferreason"></s:text>
			:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.typeOfTransfer}" default="N/A" /></span></td>
		</s:if>
	</tr>
	<s:if test="%{!@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type)}">
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="docNum" />:</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{mutationRegistrationDetails.documentNo}" default="N/A" /></span></td>
		<td class="greybox"><s:text name="docDate" />:</td>
		<td class="greybox"><s:date name="mutationRegistrationDetails.documentDate" var="docDate"
				format="dd/MM/yyyy" /> <span class="bold"><s:property
					value="%{#docDate}" default="N/A" /></span></td>
	</tr>
	</s:if>
</table>