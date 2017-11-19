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
<html>
<head>
<title>  <s:text name="service.master.search.header"></s:text> </title>

<script>
function validate(obj){
	document.getElementById('error_area').innerHTML = '';
	document.getElementById("error_area").style.display="none"
	if(document.getElementById('serviceCategoryid').value == -1){
		document.getElementById("error_area").innerHTML = '<s:text name="error.select.service.category" />';
		document.getElementById("error_area").style.display="block";
		return false;
	}
	document.forms[0].action=obj;
	document.forms[0].submit;
   return true;
}
</script>
</head>

<body>
<s:form action="serviceDetails" theme="simple" name="serviceDetailsForm" method="post">

	 <div class="errorstyle" id="error_area" style="display:none;"></div>
	<div class="formmainbox">
	<div class="subheadnew"><s:text name="service.master.search.header"></s:text> </div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="max-width:960px;margin:0 auto;">

		
		<tr>
			<td class="greybox" width="5%">&nbsp;</td>
			<td class="greybox"><s:text name="service.master.search.category"></s:text> <span class="mandatory1">*</span></td>
			<td class="greybox"><s:select headerKey="-1"
				headerValue="----Choose----"
				name="serviceCategory" id="serviceCategoryid" cssClass="selectwk"
				list="dropdownData.serviceCategoryList" listKey="id" listValue="name"
				value="%{serviceCategory.id}" /></td>
		</tr>
		

	</table>
<div align="left" class="mandatorycoll">&nbsp;&nbsp;&nbsp;<s:text name="common.mandatoryfields"/></div>
<br/>
	</div>
	
	<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="Create Service" 
					onclick="return validate('serviceDetails-beforeCreate.action');" />
			</label>&nbsp;
			
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="List Services" 
					onclick="return validate('serviceDetails-listServices.action');" />
			</label>			
		</div>

</s:form>
</body>
</html>
