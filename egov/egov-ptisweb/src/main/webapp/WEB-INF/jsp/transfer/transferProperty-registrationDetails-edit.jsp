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
			<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.seller"
						value="%{mutationRegistrationDetails.seller}" id="seller" /></span></td>
			<td class="greybox"><s:text name="regst.details.buyerName" /> :</td>
			<td class="greybox"><span class="bold">
						<s:textfield name="mutationRegistrationDetails.buyer" value="%{mutationRegistrationDetails.buyer}" id="buyer" />
			</span></td>
		</tr>
		<tr>
			<td class="greybox2">&nbsp;</td>
			<td class="greybox"><s:text name="regst.details.doorNo" />:</td>
			<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.doorNo"
						value="%{mutationRegistrationDetails.doorNo}" id="doorNo" /></span></td>
			<td class="greybox"><s:text name="regst.details.address" />:</td>
			<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.address"
						value="%{mutationRegistrationDetails.address}" id="address" /></span></td>
		</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text
				name="regst.details.registeredPlotArea" /> :</td>
		<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.plotArea"
					value="%{mutationRegistrationDetails.plotArea}" id="plotArea" onblur="trim(this,this.value);checkForTwoDecimals(this,'Registered Plot Area');checkZero(this,'Registered Plot Area');"/></span></td>
		<td class="greybox"><s:text
				name="regst.details.registeredPlinthArea" /> :</td>
		<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.plinthArea"
					value="%{mutationRegistrationDetails.plinthArea}" id="plinthArea" onblur="trim(this,this.value);checkForTwoDecimals(this,'Registered Plinth Area');checkZero(this,'Registered Plinth Area');"/></span></td>
	</tr>

	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="regst.details.eastBoundary" />
			:</td>
		<td class="bluebox"><span class="bold"><s:textfield name="mutationRegistrationDetails.eastBoundary"
					value="%{mutationRegistrationDetails.eastBoundary}" id="eastBoundary" /></span></td>
		<td class="bluebox"><s:text name="regst.details.westBoundary" />:</td>
		<td class="bluebox"><span class="bold"><s:textfield name="mutationRegistrationDetails.westBoundary"
					value="%{mutationRegistrationDetails.westBoundary}" id="westBoundary" /></span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.northBoundary" />
			:</td>
		<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.northBoundary"
					value="%{mutationRegistrationDetails.northBoundary}" id="northBoundary" /></span></td>
		<td class="greybox"><s:text name="regst.details.southBoundary" />
			:</td>
		<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.southBoundary"
					value="%{mutationRegistrationDetails.southBoundary}" id="southBoundary" /></span></td>
	</tr>
	<tr>
		<td class="greybox2">&nbsp;</td>
		<td class="greybox"><s:text name="regst.details.sroName" />
			:</td>
		<td class="greybox"><span class="bold"><s:textfield name="mutationRegistrationDetails.sroName"
					value="%{mutationRegistrationDetails.sroName}" id="sroName" /></span></td>
	</tr>
</table>