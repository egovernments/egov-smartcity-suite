<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Journal voucher Create</title>

</head>

	
<body onload="loadDropDownCodes();loadDropDownCodesFunction();onloadtask()">

<s:form action="journalVoucher" theme="simple" name="jvcreateform" >
<div id="loading" style="position:absolute; left:25%; top:70%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
    <div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
        <img src="/EGF/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Loading...
    </div>
</div>
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Journal voucher Create" />
			</jsp:include>
			
			<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Journal Voucher</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>

	<table border="0" width="100%">
	<tr>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="greybox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
		</s:if>
			<td class="greybox"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:date name="voucherDate" id="voucherDateId" format="dd/MM/yyyy"/>
			<s:textfield name="voucherDate" id="voucherDate" value="%{voucherDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('jvcreateform.voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
			</td>
		<s:else>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</s:else>	
	</tr>
	<jsp:include page="loadYIDataTable.jsp"/>
	
	<jsp:include page="voucherSubType.jsp"/>
	<jsp:include page="vouchertrans-filter.jsp"/>
	
		
		<tr>
			<td class="greybox"><s:text name="voucher.narration" /></td>
			<td class="greybox" colspan="3"><s:textarea  id="narration" name="description" style="width:580px" onblur="checkVoucherNarrationLen(this)"/></td>
		</tr>	
	</tr>
	</table>
	</div>
	<br/>
	<div id="labelAD" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Account Details</th></table>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
     </div>
     <script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
	 <div id="codescontainer"></div>
	 <br/>
	 	<div id="labelSL" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Sub-Ledger Details</th></table>
	 	</div>
	 	
		<div class="yui-skin-sam" align="center">
	       <div id="subLedgerTable"></div>
	     </div>
		<script>
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>
		<br/>
		<div class="subheadsmallnew"/></div>
		<div class="mandatory" align="left">* Mandatory Fields</div>
		
	<div class="buttonbottom" style="padding-bottom:10px;">
		<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close" id="save&close" name="save&close" method="saveAndClose" onclick="return validateJV()"/> 
		<s:submit type="submit" cssClass="buttonsubmit" value="Save & New" id="save&new" method="saveAndNew" onclick="return validateJV()"/>
		<s:submit type="submit" cssClass="buttonsubmit" value="Save & View" id="save&View" method="saveAndView" onclick="return validateJV()"/>
		<s:submit type="submit" cssClass="buttonsubmit" value="Save & Print" id="save&Print" method="saveAndPrint" onclick="return validateJV()"/>
		<input type="reset" id="Reset" value="Cancel" class="buttonsubmit"/>
		<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="buttonsubmit"/>
	</div>
	<br/>
	</div>
</div>
</div>
<div id="codescontainer"></div>
<input type="hidden" id="voucherTypeBean.voucherName" name="voucherTypeBean.voucherName" value="${voucherTypeBean.voucherName}"/>
<input type="hidden" id="voucherTypeBean.voucherType" name="voucherTypeBean.voucherType" value="Journal Voucher"/>
<input type="hidden" id="voucherTypeBean.voucherNumType" name="voucherTypeBean.voucherNumType" value="${voucherTypeBean.voucherNumType}" />
<input type="hidden" id="voucherTypeBean.cgnType" name="voucherTypeBean.cgnType" value="JVG"/>

</s:form>

<s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>
		<script>
		
			if(dom.get('targetvalue').value=='success')
			{
				document.getElementById('voucherDate').value=""; 
				if(document.getElementById('voucherNumber')){
					document.getElementById('voucherNumber').value="";
				}
				document.getElementById('narration').value="";
				if(document.getElementById('fundId')){
					document.getElementById('fundId').value=-1;
				}
				if(document.getElementById('vouchermis.departmentid')){
					document.getElementById('vouchermis.departmentid').value=-1;
				}
				if(document.getElementById('schemeid')){
					document.getElementById('schemeid').value=-1;
				}
				if(document.getElementById('subschemeid')){
					document.getElementById('subschemeid').value=-1;
				}
				if(document.getElementById('vouchermis.functionary')){
					document.getElementById('vouchermis.functionary').value=-1;
				}
				if(document.getElementById('fundsourceId')){
					document.getElementById('fundsourceId').value=-1;
				}
				if(document.getElementById('vouchermis.divisionid')){
					document.getElementById('vouchermis.divisionid').value=-1;
				}
			}	
function validateJV()
{

	
	document.getElementById('lblError').innerHTML ="";
	var cDate = new Date();
	var currDate = cDate.getDate()+"/"+(parseInt(cDate.getMonth())+1)+"/"+cDate.getYear();
	var vhDate=document.getElementById('voucherDate').value;
	if(vhDate == '' )	{
		document.getElementById('lblError').innerHTML = "Please enter a voucher date ";
		document.getElementById('voucherDate').focus();
		return false;
	}

	var vVoucherSubType = document.getElementById('vType').value;
	if(vVoucherSubType != 'JVGeneral' && vVoucherSubType != '-1' )	{
		if(document.getElementById('voucherTypeBean.partyName').value == '' ) {
			document.getElementById('lblError').innerHTML = "Please enter a Party Name ";
			document.getElementById('voucherTypeBean.partyName').focus();
			return false;
		}
	}
	// Javascript validation of the MIS Manadate attributes.
			<s:if test="%{isFieldMandatory('vouchernumber')}"> 
				 if(null != document.getElementById('voucherNumber') && document.getElementById('voucherNumber').value.trim().length == 0 ){

					document.getElementById('lblError').innerHTML = "Please enter a voucher number";
					return false;
				 }
			 </s:if>
		 <s:if test="%{isFieldMandatory('voucherdate')}"> 
				 if(null != document.getElementById('voucherDate') && document.getElementById('voucherDate').value.trim().length == 0){

					document.getElementById('lblError').innerHTML = "Please enter a voucher date";
					return false;
				 }
			 </s:if>
		 <s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){

					document.getElementById('lblError').innerHTML = "Please Select a fund";
					return false;
				 }
			 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a field";
					return false;
				 }
			</s:if>
	return true;
}function loadBank(fund){
	}
function onloadtask(){
//autocompleteEntities1By20();

	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	if(document.getElementById('voucherDate').value  =="")  
		document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	var VTypeFromBean = '<s:property value="voucherTypeBean.voucherSubType"/>';
	if(VTypeFromBean == "") 
		VTypeFromBean = '-1';
	document.getElementById('vType').value = VTypeFromBean;
	if('<s:property value="voucherTypeBean.voucherSubType"/>' == 'JVGeneral' || '<s:property value="voucherTypeBean.voucherSubType"/>'== ""){
		document.getElementById('voucherTypeBean.partyBillNum').readOnly=true;
		document.getElementById('voucherTypeBean.partyName').readOnly=true;
		document.getElementById('partyBillDate').readOnly=true;
		document.getElementById('voucherTypeBean.billNum').readOnly=true;
		document.getElementById('billDate').readOnly=true;
	}
	var message = '<s:property value="message"/>';
	if(message != null && message != '')
		showMessage(message);
	<s:if test="%{voucherTypeBean.voucherNumType == null}">
		document.getElementById('voucherTypeBean.voucherNumType').value ="Journal";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherName == null}">
		document.getElementById('voucherTypeBean.voucherName').value ="JVGeneral";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherSubType == null}">
		document.getElementById('voucherTypeBean.voucherSubType').value = "JVGeneral";
	</s:if>
	if(message == null || message == '')
		populateslDropDown(); // to load the subledger detils when page loads, required when validation fails.		
	
}
function showMessage(message){
	var buttonValue = '<s:property value="buttonValue"/>';
	var voucherHeaderId = '<s:property value="voucherHeader.id"/>';
	alert(message);
	if(buttonValue == 'close'){
		self.close();
	}else if(buttonValue == 'view'){
		document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+voucherHeaderId;
		document.forms[0].submit();
	}else if(buttonValue == 'print'){
		window.location = "${pageContext.request.contextPath}/voucher/journalVoucherPrint!print.action?id="+voucherHeaderId;
	}
}

</script>
</body>

</html>