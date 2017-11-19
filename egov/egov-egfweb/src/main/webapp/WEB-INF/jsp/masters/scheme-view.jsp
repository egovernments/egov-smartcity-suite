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

<html>
<head>

<title><s:text name="scheme.view.title" /></title>
<SCRIPT type="text/javascript">
    function checkuniquenesscode(){
    	document.getElementById('codeuniquecode').style.display ='none';
		var code=document.getElementById('code').value;
		var id=document.getElementById('id').value;
		populatecodeuniquecode({code:code,id:id});		
    }
    
    function checkuniquenessname(){
    	document.getElementById('uniquename').style.display ='none';
		var name=document.getElementById('name').value;
		var id=document.getElementById('id').value;
		populateuniquename({name:name,id:id});		
    }
    
    </SCRIPT>
</head>
<body>
	<s:form name="schemeForm" action="scheme" theme="css_xhtml"
		validate="true">

		<div class="formmainbox">
			<div id="viewhead" class="subheadnew">
				<s:text name="scheme.view.title" />
			</div>
			<div style="color: red">
				<s:actionerror />
			</div>
			<div style="color: green">

				<s:actionmessage />
			</div>
			<div class="errorstyle" style="display: none" id="codeuniquecode">
				<s:text name="scheme.code.already.exists" />
			</div>
			<div class="errorstyle" style="display: none" id="uniquename">
				<s:text name="scheme.name.already.exists" />
			</div>

			<s:hidden name="mode" id="mode" value="%{mode}" />
			<s:hidden name="id" id="id" value="%{id}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" width="10%"><b><s:text
								name="scheme.code" /></b></td>
					<td class="greybox" width="30%"><s:property
							value="%{scheme.code}" /></td>

					<td class="greybox" width="10%"><b><s:text
								name="scheme.name" /></b></td>
					<td class="greybox" width="30%"><s:property
							value="%{scheme.name}" /></td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="bluebox"><b><s:text name="scheme.fund" /></b></td>
					<td class="bluebox"><s:property value="%{scheme.fund.name}" /></td>
					<td class="bluebox"><b>IsActive</b></td>
					<td class="bluebox"><s:property value="%{scheme.isactive}" /></td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><b><s:text name="scheme.startDate" /></b></td>
					<td class="greybox">
					<s:date name="scheme.validfrom" format="dd/MM/yyyy"/></td>

					<td class="greybox"><b><s:text name="scheme.endDate" /></b></td>
					<td class="greybox"><s:date name="scheme.validto" format="dd/MM/yyyy"/></td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="bluebox" width="10%"><b><s:text
								name="scheme.description" /></b></td>
					<td class="bluebox" colspan="3"><s:property
							value="%{scheme.description}" /></td>
				</tr>
			</table>
		</div>
	</s:form>
	<br />
	<div id="viewMode" class="buttonbottom">
		<table table align="center">
			<tr>
				<td><input type="button" name="close" id="Close" value="Close"
					onclick="javascript:window.close()" class="button" /></td>
			</tr>
		</table>
	</div>



</body>
</html>
