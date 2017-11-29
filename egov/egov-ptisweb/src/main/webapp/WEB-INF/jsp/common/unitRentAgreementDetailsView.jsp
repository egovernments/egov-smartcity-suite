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

<html>
<head>
<title> Unit rent agreement details </title>
<link href="/ptis/css/propertytax.css" rel="stylesheet" type="text/css" />
<link href="/ptis/css/commonegov.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/ptis/css/jquery/jquery-ui-1.8.4.custom.css"  />

<script src="../javascript/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/ptis/javascript/jquery/jquery-ui-1.8.22.custom.min.js" ></script>
<script type="text/javascript" src="/ptis/javascript/jquery/ajax-script.js"></script>

<script type="text/javascript" src="/ptis/javascript/validations.js"></script>
	
	<script type="text/javascript">
	
	jQuery(function() {
		if (window.opener.agreementPeriod != undefined || window.opener.agreementPeriod != null) {
			jQuery('#divAgreementPeriod').html(window.opener.agreementPeriod);
			jQuery('#divAgreementDate').html(window.opener.agreementDate);
			jQuery('#divIncrementInRent').html(window.opener.incrementInRent);
		}
	}) 
		
	</script>
	
	<style type="text/css">
		td.zeroBorder { border: 0px; }	
	</style>
</head>

<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			Unit Rent Agreement Details
		</div>
		<form>
			<br />
			<table width="100%" border="0" align="center" cellspacing="0"
				cellpadding="0">
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement period : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divAgreementPeriod" style="font-weight: bold" ></div>
					</td>
					<td class="bluebox" width="25%" />
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement date : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divAgreementDate" style="font-weight: bold"></div>
					</td>
					<td class="bluebox" width="25%">
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Increment in Rent (%) : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divIncrementInRent" style="font-weight: bold"></div>
					</td>
					<td class="bluebox" width="25%">
				</tr>
			</table>

			<div class="buttonbottom" align="center">
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</form>
	</div>
</body>
</html>
