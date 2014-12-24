<%@ include file="/includes/taglibs.jsp" %>
<head>
	<title><s:text name="searchchallan.title"/></title>
	<script>
	function openChallan(receiptId){
	var leftPos=document.body.clientWidth;
	window.open ("${pageContext.request.contextPath}"+"/receipts/challan!viewChallan.action?sourcePage=search&receiptId="+receiptId,"ViewChallan","resizable=yes,scrollbars=yes,left="+leftPos+",top=40, width=900, height=650"); 
	}
	
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
	var status=dom.get("status").value;
	if((deptId=="-1")&& (challanNo=="")&& (fromdate=="")&& (todate=="")&&(serviceId=="-1")&&(status=="-1")){
		dom.get("errorMessages").style.display="block";
		document.getElementById("errorMessages").innerHTML='<s:text name="searchchallan.selectonecriteria" />'+ '<br>';
		return false;
	
	}
	if(fromdate!="" && todate!="" && fromdate!=todate)
	{
		if(!checkFdateTdate(fromdate,todate))
		{
			dom.get("errorMessages").style.display="block";
			document.getElementById("errorMessages").innerHTML='<s:text name="common.comparedate.errormessage" />'+ '<br>';
			return false;
		}
	}
	
}
	</script>
</head>
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
<body>
<s:form theme="simple" name="searchChallanForm" action="searchChallan">
<div class="formmainbox"><div class="subheadnew"><s:text name="searchchallan.title"/></div>
<div class="subheadsmallnew"><span class="subheadnew"><s:text name="searchreceipts.criteria"/></span></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

	    <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.department"/></td>
	      <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="departmentId" id="departmentId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName"  /> </td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.challanNumber"/></td>
	   	  <td width="30%" class="bluebox2"><s:textfield id="challanNumber" name="challanNumber"/></td>
	     </tr>
	     <tr>
	      <td width="4%" class="bluebox">&nbsp;</td>
	      <td width="21%" class="bluebox"><s:text name="searchchallan.fromdate"/></td>
		  <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy"/>
		  <td width="24%" class="bluebox"><s:textfield id="fromDate" name="fromDate" value="%{cdFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	      <td width="21%" class="bluebox"><s:text name="searchchallan.todate"/></td>
	      <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy"/>
		  <td width="30%" class="bluebox"><s:textfield id="toDate" name="toDate" value="%{cdFormat1}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	    </tr>
	     <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.service"/></td>
	      <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="serviceId" id="serviceId" cssClass="selectwk" list="dropdownData.serviceList" listKey="id" listValue="serviceName"  /></td>
	      <td width="21%" class="bluebox2"><s:text name="searchchallan.status"/></td>
	      <td width="30%" class="bluebox2"><s:select id="status" name="status" headerKey="-1" headerValue="%{getText('searchreceipts.status.select')}" cssClass="selectwk" list="%{challanStatuses}" value="%{status}" listKey="id" listValue="description" /> </td>
	   
	    </tr>	
</table>
</div>

    <div class="buttonbottom">
      <label><s:submit type="submit" cssClass="buttonsubmit" id="button" value="Search" method="search" onclick="return validate();"/></label>&nbsp;
      <label><s:submit type="submit" cssClass="button" value="Reset" method="reset"/></label>&nbsp;
      <input name="closebutton" type="button" class="button" id="closebutton" value="Close" onclick="window.close();"/>
</div>
<logic:notEmpty name="results">
<display:table name="results" uid="currentRow" pagesize = "30" style="border:1px;width:100%" cellpadding="0" cellspacing="0" export="false" requestURI="">
<display:caption media="pdf">&nbsp;</display:caption>

<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Demand No." style="width:10%">
<div align="center">
	<s:hidden name="currentRow.challan.challanNumber" value="%{currentRow.challan.challanNumber}"/> 
	 <c:if test="${not empty currentRow.challan.challanNumber}">  <a href="#" onclick="openChallan('${currentRow.id}')"><c:out value="${currentRow.challan.challanNumber}"/></a></c:if>
</div>
</display:column>
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="challan.challanDate" title="Demand Date" format="{0,date,dd/MM/yyyy}" style="width:10%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Service" style="width:20%" ><div align="center"><s:hidden name="currentRow.challan.service.serviceName" value="%{currentRow.challan.service.serviceName}"/>  <c:if test="${not empty currentRow.challan.service.serviceName}">   <c:out value="${currentRow.challan.service.serviceName}"/></c:if>&nbsp;</div></display:column>
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="receiptMisc.fund.name" title="Fund" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="receiptMisc.department.deptName" title="Department" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" property="totalAmount" title="Amount (Rs.)" format="{0, number, #,##0.00}" style="width:20%;text-align: center" />
<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Status" style="width:20%" ><div align="center"><s:hidden name="currentRow.challan.status.description" value="%{currentRow.challan.status.description}"/>  <c:if test="${not empty currentRow.challan.status.description}">   <c:out value="${currentRow.challan.status.description}"/></c:if>&nbsp;</div></display:column>

</display:table>
</logic:notEmpty>
<logic:empty name="results">
	<s:if test="target=='searchresult'">
	
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		<tr> 
			<div>&nbsp;</div>
			<div class="subheadnew"><s:text name="searchresult.norecord"/></div>
		</tr>
		</table>
	
	</s:if>
</logic:empty>
</s:form>
</body>
