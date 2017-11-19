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

</head>

<body>
<s:form theme="simple" name="serviceDetailsForm" method="post">
<s:push value="model">
	 <div class="errorstyle" id="error_area" style="display:none;"></div>
	<div class="formmainbox">
	<div class="subheadnew"><s:text name="service.list.service"></s:text> </div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="bluebox"  style="font-size:14px;"> <b><s:text name="service.master.search.category"></s:text></b> : &nbsp;&nbsp;&nbsp; <s:property value="model.serviceCategory.name"/>  </td>
			<td class="bluebox"></td>
			<s:hidden id="serviceCategoryId" name="serviceCategoryId" value="%{serviceCategory.id}"/>
		</tr>
	</table>
	<br> <br>
	<s:if test="%{null != serviceCategory.services && serviceCategory.services.size() >0}">
	<div align="center">
		<table width="100%" border="1"colspan="5" >

		
		<tr>
			<th class="bluebgheadtd" width="5%"> <s:text name="service.select.table.header"/></th>
			<th class="bluebgheadtd" width="5%" style="text-align:left;" > <s:text name="service.slNo.table.header"/></th>
			<th class="bluebgheadtd" width="20"> <s:text name="service.create.code"/></th>
			<th class="bluebgheadtd" style="text-align:left;" width="40%"> <s:text name="service.create.name"/></th>
			<th class="bluebgheadtd" width="5%"> <s:text name="service.master.enable"/></th>
		
		</tr>
		<s:iterator var="p" value="%{serviceCategory.services}" status="s"> 
				<tr>
					<s:hidden id="serviceId" name="serviceId"/>
					<td width="5%"  class="bluebox"><input type="radio" onclick='dom.get("serviceId").value = <s:property value="id"/>'  name="radioButton1"/>  </td>
					<td width="5%"  class="bluebox"> <s:property value="#s.index+1" />  </td>
					<td width="30%"  class="bluebox"><div  align="center"><s:property value="code"/></div></td>
					<td width="6%"  class="bluebox"><div  align="left"><s:property value="name"/></div></td>
					<td width="10%"  class="bluebox" ><div  align="center"><s:if test="isEnabled" ><s:text name="text.yes"></s:text> </s:if><s:else><s:text name="text.no"></s:text> </s:else> </div></td>
				</tr>
		</s:iterator>

	</table>	
	</div>
	</s:if>
	<s:else>
		<div align="center">
	 		<s:text name="serviceDetails.createservice.message"/>
	 	</div>
	</s:else>
	</div>
	
	<div class="buttonbottom">
		<s:if test="%{null != serviceCategory.services && serviceCategory.services.size() >0}">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="View" 
					onclick="return validate('serviceDetails-view.action');" />
			</label>&nbsp;
			
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="Modify" 
					onclick="return validate('serviceDetails-beforeModify.action');" />
			</label>	
			</s:if>
			<s:else>
				<input type="button" id="back" value="Back" onclick="window.location='${pageContext.request.contextPath}/service/serviceDetails-newform.action?serviceCategoryId=<s:property value='%{serviceCategoryId}'/>';"  class="buttonsubmit"/>
			</s:else>
			<label>
				<input type="button"  class="buttonsubmit" id="button"
					value="Create Service" 
					onclick="window.location='${pageContext.request.contextPath}/service/serviceDetails-beforeCreate.action?serviceCategoryId=<s:property value='%{serviceCategoryId}'/>';" />
			</label>&nbsp;
			<label>
			<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="buttonsubmit"/>
			</label>	
		</div>
</s:push>
</s:form>
<script>
	function validate(obj){
		dom.get('error_area').innerHTML = '';
		dom.get("error_area").style.display="none";
		<s:if test="%{null != serviceCategory.services && serviceCategory.services.size() >0}">
			if(dom.get('serviceId').value == "") {
				dom.get("error_area").innerHTML = '<s:text name="service.error.select" />';
				dom.get("error_area").style.display="block";
				return false;
			}
		</s:if>
		document.forms[0].action=obj;
		document.forms[0].submit;
	}
</script>
</body>
</html>
