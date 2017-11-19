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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>
<head>
<title><s:text name="scheme.search.title" /></title>
<SCRIPT type="text/javascript">
   
    function validateAndSubmit()
	{
		var fromDate=document.getElementById('validfromId').value;
		var toDate=document.getElementById('validtoId').value;

		var fundId=document.getElementById('fundId').value;
		

		if(fundId == "-1"){
			bootbox.alert("Please select fund");
			return false;
			}
		
		 
		if(!validateDate(fromDate)){
			bootbox.alert("Invalid Date! Start date is greater than current date");
			return false;
		}
		var fdateParts=	fromDate.split("/");
		var tdateParts=	toDate.split("/");
		var fdate=new Date(fdateParts[1]+"/"+fdateParts[0]+"/"+fdateParts[2]);
		var tdate=new Date(tdateParts[1]+"/"+tdateParts[0]+"/"+tdateParts[2]);
		if (fdate > tdate) {
			bootbox.alert("Invalid Date Range! Start Date should be less than End Date!")
			return false;
			} 

		
    	document.schemeForm.action='${pageContext.request.contextPath}/masters/scheme-search.action';
    	document.schemeForm.submit();
    	//return true;
    	
	document.getElementById('type').disabled=false;
	return true;
	}
	function validateDate(date)
	{
		var todayDate = new Date();
		 var todayMonth = todayDate.getMonth() + 1;
		 var todayDay = todayDate.getDate();
		 var todayYear = todayDate.getFullYear();
		 var todayDateText = todayDay + "/" + todayMonth + "/" +  todayYear;
		if (Date.parse(date) > Date.parse(todayDateText)) {
			return false;
			}
		return true; 
		}

   
    </SCRIPT>
</head>
<body>
	<s:form name="schemeForm" action="scheme" theme="simple"
		validate="true">
		<div class="formmainbox">
			<div class="subheadnew">
				<s:if test="%{mode=='edit'}">
					<s:text name="scheme.searchmodify.title" />
				</s:if>
				<s:else>
					<s:text name="scheme.searchview.title" />
				</s:else>
			</div>
			<s:hidden name="mode" id="mode" value="%{mode}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td style="width: 10%"></td>
					<td class="bluebox"><s:text name="scheme.fund" /><span
						class="mandatory1"> *</span></td>
					<td class="bluebox"><s:select name="fund" id="fundId"
							list="dropdownData.fundDropDownList" listKey="id"
							listValue="name" headerKey="-1" headerValue="----Select----"
							value="scheme.fund.id" /></td>
				</tr>
				<tr>
					<td style="width: 10%"></td>
					<td class="greybox"><s:text name="scheme.startDate" /></td>
					<td class="greybox"><s:date name="validfrom" var="validfromId"
							format="dd/MM/yyyy" />
						<s:textfield id="validfromId" name="validfrom"
							value="%{validfromId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>

					<td class="greybox"><s:text name="scheme.endDate" /></td>
					<td class="greybox"><s:date name="validto" var="validtoId"
							format="dd/MM/yyyy" /> <s:textfield id="validtoId"
							name="validto" value="%{validtoId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>

			</table>
			<br />



		</div>
		<div class="buttonbottom">
			<table align="center">
				<tr>
					<td><input type="submit" class="buttonsubmit" value="Search"
						id="search" name="button" onclick="return validateAndSubmit();" />&nbsp;</td>
					<td><input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
			</table>
		</div>

		<s:if test="%{schemeList.size!=0}">
			<table width="100%" border="1" align="center" cellpadding="0"
				cellspacing="0" class="setborder" style="border-collapse: inherit;">
				<tr>
					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center">Sl No.</th>
					<th class="bluebgheadtd" style="width: 4%; text-align: center"
						align="center">Scheme Code</th>
					<th class="bluebgheadtd" style="width: 8%; text-align: center"
						align="center">Scheme Name</th>
					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center">Start Date</th>
					<th class="bluebgheadtd" style="width: 4%; text-align: center"
						align="center">End Date</th>
					<th class="bluebgheadtd" style="width: 4%; text-align: center"
						align="center">Active Y/N</th>
				</tr>
				<c:set var="trclass" value="greybox" />
				<s:iterator var="scheme" value="schemeList" status="f">
					<tr>

						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:property value="#f.index+1" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><a href="#"
							onclick="urlLoad('<s:property value="%{id}" />','<s:property value="%{mode}" />');"
							id="sourceLink" /> <s:label value="%{code}" /> </a></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:property value="name" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:date name="%{validfrom}"
								format="dd/MM/yyyy" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:date name="%{validto}" format="dd/MM/yyyy" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:if test="%{isactive==true}">Yes</s:if>
						<s:else>No</s:else></td>
						<c:choose>
							<c:when test="${trclass=='greybox'}">
								<c:set var="trclass" value="bluebox" />
							</c:when>
							<c:when test="${trclass=='bluebox'}">
								<c:set var="trclass" value="greybox" />
							</c:when>
						</c:choose>
					</tr>
				</s:iterator>

			</table>
		</s:if>

		<s:if test="%{schemeList.size==0}">
			<div id="msgdiv" style="display: block">
				<table align="center" class="tablebottom" width="80%">
					<tr>
						<th class="bluebgheadtd" colspan="7">No Records Found
						</td>
					</tr>
				</table>
			</div>
		</s:if>
	</s:form>


	<script type="text/javascript">
	function urlLoad(id,showMode) {
		if(showMode=='edit')
			 url = "../masters/scheme-beforeEdit.action?id="+id+"&mode=edit";
		else          
			 url = "../masters/scheme-beforeView.action?id="+id+"&mode=view"; 
		window.open(url,'schemeView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	</script>
</body>
</html>