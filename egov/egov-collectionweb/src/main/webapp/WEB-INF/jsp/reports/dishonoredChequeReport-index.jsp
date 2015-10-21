<%@ include file="/includes/taglibs.jsp"%>
<head>

<title><s:text name ="dishonorchequeReport.title"></s:text></title>
<script type="text/javascript">
function validate(){
	var valid = false;
	if (document.getElementById('fromDate').value !="" && document.getElementById('fromDate').value !=null){
		valid = true;
	} 
	else if (document.getElementById('toDate').value !="" && document.getElementById('toDate').value !=null){
		valid = true;
	}
	else if(document.getElementById("service").value != null && document.getElementById("service").value != -1) {
		valid = true;
	}
	else if(document.getElementById("paymentMode").value != null && document.getElementById("paymentMode").value != "ALL") {
		valid = true;
	}
	else if (document.getElementById("locationId").value != null && document.getElementById("locationId").value != -1) {
		valid = true; 
	}
	else if(document.getElementById("statusId").value != null && document.getElementById("statusId").value != -1) {
		valid = true;
	}
	else if(document.getElementById("instrumentNumber").value != null && document.getElementById("instrumentNumber").value != "") {
		valid = true;
	}

	if(!valid){
		dom.get("atleastOneCriteria").style.display="block";
		valid = false;
		}
	else {
		valid = true;
		loadingMask('#loadingMask');
		}
	
	return valid;
	}
</script>
</head>
<span align="center" style="display: none" id="atleastOneCriteria">
<li><font size="2" color="red"><b> <s:text
	name="dishonorchequeReport.atleast.one.criteria" /> </b></font></li>
</span>
<body >
<s:form theme="simple" name="dishonorChequeReportForm"
	action="dishonoredChequeReport-generateReport.action">
	<div class="formmainbox">
	<div class="subheadnew"><s:text name="dishonorchequeReport.title"/></div>
	<div class="subheadsmallnew"><span class="subheadnew"><s:text
		name="dishonorchequeReport.criteria" /></span></div>
	
	<input type="hidden" name="hostName" id="hostName" value="${hostName}"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="4%" class="bluebox">&nbsp;</td>
			<td width="21%" class="bluebox"><s:text
				name="dishonorchequeReport.criteria.fromdate" /></td>
			<s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
			<td width="24%" class="bluebox"><s:textfield id="fromDate"
				name="fromDate" value="%{cdFormat}"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
				href="javascript:show_calendar('forms[0].fromDate');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"><img
				src="/egi/resources/erp2/images/calendaricon.gif"
				alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
			<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
			</td>
			<td width="21%" class="bluebox"><s:text
				name="dishonorchequeReport.criteria.todate" /></td>
			<s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
			<td width="30%" class="bluebox"><s:textfield id="toDate"
				name="toDate" value="%{cdFormat1}"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
				href="javascript:show_calendar('forms[0].toDate');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"><img
				src="/egi/resources/erp2/images/calendaricon.gif"
				alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
			<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
			</td>
		</tr>
		
		<tr>
			<td width="4%" class="bluebox2">&nbsp;</td>
			<td width="21%" class="bluebox2"><s:text
				name="dishonorchequeReport.criteria.service" /></td>
			<td width="24%" class="bluebox2"><s:select headerKey="-1"
				headerValue="%{getText('dishonorchequeReport.criteria.select')}" name="serviceId" id="service" cssClass="selectwk"
				list="dropdownData.servicetypeList" listKey="id" listValue="name"
				value="%{serviceId}" /></td>
				<td width="21%" class="bluebox2">
				<s:text name="dishonorchequeReport.criteria.payment.mode"/></td>
	        	<td width="30%" class="bluebox2"><s:select headerKey="ALL"
				headerValue="%{getText('dishonorchequeReport.criteria.select')}" 
				name="paymentMode" id="paymentMode" cssClass="selectwk" 
				list="paymentModes" value="%{paymentMode}" /> </td>
	        
		</tr>
		<tr>
			<td width="4%" class="bluebox2">&nbsp;</td>
			<td width="21%" class="bluebox"><s:text name="dishonorchequeReport.criteria.location" /></td>
			<td width="24%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('dishonorchequeReport.collectionlocation.all')}" 
					name="locationId" id="locationId" cssClass="selectwk" list="dropdownData.locationList" listKey="id" 
					listValue="name" value="%{locationId}" /></td>
				 <td width="21%" class="bluebox2">
				<s:text name="dishonorchequeReport.criteria.status"/></td>
	        	<td width="30%" class="bluebox2"><s:select headerKey="-1"
				headerValue="%{getText('dishonorchequeReport.criteria.select')}" 
				name="statusId" id="statusId" cssClass="selectwk"  
				list="dropdownData.statusList" listKey="id" 
					listValue="description" value="%{statusId}" /> </td>
	        
		</tr>
		<tr>
		<td width="4%" class="bluebox">&nbsp;</td>
		<td width="21%" class="bluebox"><s:text name="dishonorchequeReport.criteria.chequenumber"/></td>
	      	<td width="24%" class="bluebox"><s:textfield id="instrumentNumber" type="text" name="instrumentNumber"/></td>
	      	<td width="21%" class="bluebox"></td>
	      	<td width="24%" class="bluebox"></td>			
	</tr>
	</table>
<div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img id="removeImage" src="${pageContext.request.contextPath}/images/bar_loader.gif"/> <span  id="removeText" style="color: red">Please wait....</span></div>


	<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="%{getText('collectionReport.create')}"
					onclick="return validate();" />
			</label>&nbsp;
			<label>
				<s:reset type="submit" cssClass="button"
					value="%{getText('collectionReport.reset')}"
					onclick="return clearErrors();"/>
			</label>&nbsp;
			<label>
				<input type="button" class="button" id="buttonClose"
					value="<s:text name='common.buttons.close'/>"
					onclick="window.close()" />
			</label>
		</div>
</s:form>

</body>
</html>
