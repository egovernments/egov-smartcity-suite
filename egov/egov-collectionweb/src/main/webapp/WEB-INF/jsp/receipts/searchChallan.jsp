
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
<head>
	<title><s:text name="searchchallan.title"/></title>
	<script>
	
	jQuery.noConflict();
	jQuery(document).ready(function() {
	  	 
	     jQuery(" form ").submit(function( event ) {
	    	 doLoadingMask();
	    });
	     doLoadingMask();
	 });

	jQuery(window).load(function () {
		undoLoadingMask();
	});
	
	function openChallan(receiptId){
	var leftPos=document.body.clientWidth;
	window.open ("${pageContext.request.contextPath}"+"/receipts/challan-viewChallan.action?sourcePage=search&receiptId="+receiptId,"ViewChallan","resizable=yes,scrollbars=yes,left="+leftPos+",top=40, width=900, height=650"); 
	}

   function  populateService(serviceCategory){
    	populateserviceId({serviceCatId:serviceCategory.options[serviceCategory.selectedIndex].value});	
    }

   <jsp:useBean id="now" class="java.util.Date" />

   <fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
   	var currDate = "${currDate}";
	
	function validate()
	{
		if(dom.get("actionErrorMessages")!=null){
			dom.get("actionErrorMessages").style.display="none";}
		if(dom.get("actionMessages")!=null){
			dom.get("actionMessages").style.display="none";}
		var fromdate=dom.get("fromDate").value;
		var todate=dom.get("toDate").value;
		var deptId=dom.get("departmentId").value;
		var challanNo=dom.get("challanNumber").value;
		var serviceId=dom.get("serviceId").value;
		var serviceCategoryId = dom.get("serviceCategoryId").value;
		var status=dom.get("status").value;
		if((deptId=="-1")&& (challanNo=="")&& (fromdate=="")&& (todate=="")&&(serviceId=="-1")&&(status=="-1")&&(serviceCategoryId=="-1")){
			dom.get("errorMessages").style.display="block";
			document.getElementById("errorMessages").innerHTML='<s:text name="searchchallan.selectonecriteria" />'+ '<br>';
			return false;
		
		}
		var valSuccess = true;
		document.getElementById("errorMessages").innerHTML = "";
		document.getElementById("errorMessages").style.display="none"; 
	
			if (fromdate != "" && todate != "" && fromdate != todate) {
				if (!checkFdateTdate(fromdate, todate)) {
					document.getElementById("errorMessages").style.display = "block";
					document.getElementById("errorMessages").innerHTML += '<s:text name="common.comparedate.errormessage" />'
							+ '<br>';
					valSuccess = false;
				}
				if (!validateNotFutureDate(fromdate, currDate)) {
					document.getElementById("errorMessages").style.display = "block";
					document.getElementById("errorMessages").innerHTML += '<s:text name="reports.fromdate.futuredate.message" />'
							+ '<br>';
					valSuccess = false;
				}
				if (!validateNotFutureDate(todate, currDate)) {
					document.getElementById("errorMessages").style.display = "block";
					document.getElementById("errorMessages").innerHTML += '<s:text name="reports.todate.futuredate.message" />'
							+ '<br>';
					valSuccess = false;
				}
			}
	
			return valSuccess;
	
		doLoadingMask('#loadingMask');
}
	</script>
</head>
<body>
<div class="errorstyle" id="errorMessages" style="display:none;"></div>
<s:if test="%{hasErrors()}">
    <div id="actionErrorMessages" class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<s:if test="%{hasActionMessages()}">
    <div id="actionMessages" class="messagestyle">
    	<s:actionmessage theme="simple"/>
    </div>
</s:if>
<s:form theme="simple" name="searchChallanForm" action="searchChallan">
<div class="formmainbox"><div class="subheadnew"><s:text name="searchchallan.title"/></div>
<div class="subheadsmallnew"><span class="subheadnew"><s:text name="searchreceipts.criteria"/></span></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

	    <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.department"/></td>
	      <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="departmentId" id="departmentId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="name"  /> </td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.challanNumber"/></td>
	   	  <td width="30%" class="bluebox2"><s:textfield id="challanNumber" name="challanNumber"/></td>
	     </tr>
	     <tr>
	      <td width="4%" class="bluebox">&nbsp;</td>
	      <td width="21%" class="bluebox"><s:text name="searchchallan.fromdate"/></td>
		  <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy"/>
		  <td width="24%" class="bluebox"><s:textfield id="fromDate" name="fromDate" value="%{cdFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="/egi/resources/erp2/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	      <td width="21%" class="bluebox"><s:text name="searchchallan.todate"/></td>
	      <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy"/>
		  <td width="30%" class="bluebox"><s:textfield id="toDate" name="toDate" value="%{cdFormat1}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="/egi/resources/erp2/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	    </tr>
	     <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>  <td width="21%" class="bluebox"><s:text name="miscreceipt.service.category" /></td>
        <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="----Choose----" name="serviceCategoryId" id="serviceCategoryId" cssClass="selectwk" list="dropdownData.serviceCategoryList" listKey="id" listValue="name" value="%{serviceCategoryId}" onChange="populateService(this);" />
       	<egov:ajaxdropdown id="service" fields="['Text','Value']" dropdownId="serviceId" url="receipts/ajaxReceiptCreate-ajaxLoadServiceByCategoryForChallan.action" /></td>
	      <td width="21%" class="bluebox2"><s:text name="miscreceipt.service"/></td>
	      <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="serviceId" id="serviceId" cssClass="selectwk" list="dropdownData.serviceList" listKey="id" listValue="name"  /></td>
	    </tr>	
	    <tr>
	     <td width="4%" class="bluebox">&nbsp;</td>
	     <td width="21%" class="bluebox2"><s:text name="searchchallan.status"/></td>
	     <td width="30%" class="bluebox2"><s:select id="status" name="status" headerKey="-1" headerValue="%{getText('searchreceipts.status.select')}" cssClass="selectwk" list="%{challanStatuses}" value="%{status}" listKey="id" listValue="description" /> </td>
	    </tr>
</table>
</div>
	 <div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="/collection/resources/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
    <div class="buttonbottom">
      <label><s:submit type="submit" cssClass="buttonsubmit" id="button" value="Search"  onclick="document.searchChallanForm.action='searchChallan-search.action'; return validate();"/></label>&nbsp;
      <label><s:submit type="submit" cssClass="button" value="Reset" onclick="document.searchChallanForm.action='searchChallan-reset.action';"/></label>&nbsp;
      <input name="closebutton" type="button" class="button" id="closebutton" value="Close" onclick="window.close();"/>
</div>
<s:if test="%{!results.isEmpty()}">
<display:table name="results" uid="currentRow" pagesize = "30" style="border:1px;width:100%" cellpadding="0" cellspacing="0" export="false" requestURI="">
<display:caption media="pdf">&nbsp;</display:caption>
<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Sl.No" style="width:4%;text-align:center">
			<s:property value="%{#attr.currentRow_rowNum}" />
		</display:column>
<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Challan No." style="width:10%">
<div align="center">
	<s:hidden name="currentRow.challan.challanNumber" value="%{currentRow.challan.challanNumber}"/> 
	 <c:if test="${not empty currentRow.challan.challanNumber}">  <a href="#" onclick="openChallan('${currentRow.id}')"><c:out value="${currentRow.challan.challanNumber}"/></a></c:if>
</div>
</display:column>
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="challan.challanDate" title="Challan Date" format="{0,date,dd/MM/yyyy}" style="width:10%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Service Type" style="width:20%" ><div align="center"><s:hidden name="currentRow.service.name" value="%{currentRow.service.name}"/>  <c:if test="${not empty currentRow.service.name}">   <c:out value="${currentRow.service.name}"/></c:if>&nbsp;</div></display:column>
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="receiptMisc.fund.name" title="Fund" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="receiptMisc.department.name" title="Department" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="totalAmount" title="Amount (Rs.)" format="{0, number, #,##0.00}" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Status" style="width:20%" ><div align="center"><s:hidden name="currentRow.challan.status.description" value="%{currentRow.challan.status.description}"/>  <c:if test="${not empty currentRow.challan.status.description}">   <c:out value="${currentRow.challan.status.description}"/></c:if>&nbsp;</div></display:column>

</display:table>
</s:if>
 <s:if test="%{results.isEmpty()}">
	<s:if test="target=='searchresult'">
	
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		<tr> 
			<div>&nbsp;</div>
			<div class="subheadnew"><s:text name="searchresult.norecord"/></div>
		</tr>
		</table>
	
	</s:if>
</s:if>
</s:form>
</body>
