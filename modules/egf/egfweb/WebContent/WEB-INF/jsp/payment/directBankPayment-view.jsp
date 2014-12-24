<html>

<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="EGF" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/directBankPaymentHelper.js"></script>

<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}

@media print {
input#printbutton {
display: none;
}
}
@media print {
input#closeButton {
display: none;
}
}
@media print {
div.commontopyellowbg {
display: none;
}
}
@media print {
div.commontopbluebg {
display: none;
}
}

</style>
<script>
	path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		var makeVoucherDetailTable = function() {
		var voucherDetailColumns = [ 
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow_billDetailTable(this)")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow(this)")}
		];
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		
		<s:iterator value="billDetailslist" status="stat">
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
					"functionid":'<s:property value="functionIdDetail"/>',
					"function":'<s:property value="functionDetail"/>',
					"glcodeid":'<s:property value="glcodeIdDetail"/>',
					"glcode":'<s:property value="glcodeDetail"/>',
					"accounthead":'<s:property value="accounthead"/>',
					"debitamount":'<s:property value="%{debitAmountDetail}"/>',
					"creditamount":'<s:property value="%{creditAmountDetail}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridPJV('functionIdDetail',index,'<s:property value="functionIdDetail"/>');
				updateGridPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				updateGridPJV('glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
				updateGridPJV('glcodeDetail',index,'<s:property value="glcodeDetail"/>');
				updateGridPJV('accounthead',index,'<s:property value="accounthead"/>');
				updateGridPJV('debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
				updateGridPJV('creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
				totaldbamt = totaldbamt+parseFloat('<s:property value="debitAmountDetail"/>');
				totalcramt = totalcramt+parseFloat('<s:property value="creditAmountDetail"/>');
				updateAccountTableIndex();	
			</s:iterator>
				

		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 5;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		document.getElementById('totaldbamount').value=totaldbamt;
		document.getElementById('totalcramount').value=totalcramt; 
		}
		var glcodeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	
	
	
		
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
			{key:"glcode",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","validateDetailCodeForJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow_subLedgersTable(this)")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow(this)")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		
	
		<s:iterator value="subLedgerlist" status="stat">
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKey"/>',
					"detailKey":'<s:property value="detailKey"/>',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridSLDropdownJV('glcode.id',index,'<s:property value="glcode.id"/>','<s:property value="subledgerCode"/>');
				updateGridSLDropdownJV('detailType.id',index,'<s:property value="detailType.id"/>','<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKey"/>');
				updateSLGridPJV('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
	
	}
	function addYUIRow_subLedgersTable(obj)
		{
		if(obj.disabled==true)
		{
		 return;
		}
		subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
		updateSLTableIndex();
				check();
		}
	function deleteYUIRow(obj)
		{
		if(obj.disabled==true)
		{
		 return;
		}
		if(this.getRecordSet().getLength()>1)
		{			
			this.deleteRow(record);
			allRecords=this.getRecordSet();
			for(var i=0;i<allRecords.getLength();i++){
				this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
			}
		}
		else{
			alert("This row can not be deleted");
		}
		}	
		
function addYUIRow_billDetailTable(obj)
	{
	if(obj.disabled==true)
		{
		 return;
		}
		billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
		updateAccountTableIndex();
	}
		
		
	var insuffiecientBankBalance ='<s:text name="insuffiecientBankBalance"/>';
	
	
		function validateAppoveUser(name,value){
			document.getElementById('lblError').innerHTML ="";
			document.getElementById("actionName").value= name;
			
			<s:if test="%{wfitemstate =='END'}">
				if(value == 'Approve' || value == 'Reject') {
					document.getElementById("approverUserId").value=-1;
					return true;
				}
			</s:if>
			<s:else>
				if( (value == 'Approve' || value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
					document.getElementById('lblError').innerHTML ="Please Select the user";
					return false;
				}
			</s:else>
			
			return true;
		
		}
function printVoucher(){
	
	document.forms[0].action='../report/billPaymentVoucherPrint!print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}

	</script>
</head>
<body onload="onLoadTask_view();">
<s:form  action="directBankPayment" theme="simple" name="dbpform">
<s:push value="model">
<div class="formmainbox"><div class="formheading"/><div class="subheadnew">View Direct  Bank Payment</div>
		<div id="listid" style="display:block">
		<br/>
		</div></div>
		<div align="center">
<font  style='color: red ;'> 
<p class="error-block" id="lblError" ></p>
</font>
</div>
<span class="mandatory" >
				<div id="Errors" ><s:actionerror /><s:fielderror /></div>
				<s:actionmessage />
			</span>
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
		<tr>
		<td class="bluebox" width="10%"></td>
			<td class="bluebox" width="22%"><s:text name="voucher.number"/></td>
			<td class="bluebox" width="22%"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
			<s:hidden name="id"/>
			<td class="bluebox" width="18%"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="38%"><input type="text" name="voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
			<a href="javascript:show_calendar('cbtbform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A></td>
		</tr>
		<div class="buttonbottom" >

</div>
	<%@include file="directBankPayment-form.jsp"%>
<div  class="buttonbottom" id="buttondiv">
	<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>
	<s:submit cssClass="button" id="printPreview2" value="Print Preview"  onclick="printVoucher()"/>
</div>	
  	<tr>
  		<td  colspan="6"> 
			<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
				<%@include file="../voucher/workflowApproval.jsp"%>
			</s:if>
		</td>
  	</tr>

	<s:if test="%{showApprove}">
	<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox" ><strong>Comments</strong></td>
							<td class="bluebox" colspan="4"><s:textarea name="comments" id="comments" cols="100" rows="3" onblur="checkLength(this)" value="%{getComments()}"/></td>
						</tr>

<script>
document.getElementById('buttondiv').style.display="none";
</script>

</s:if>						
	</table>
	
	

<SCRIPT type="text/javascript">
function onLoadTask_view()
		{
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =true;
		disableYUIAddDeleteButtons(true);
		document.getElementById("closeButton").disabled=false;
		if(null != document.getElementById("printPreview1")){
			document.getElementById("printPreview1").disabled=false;
		}
		if(null != document.getElementById("printPreview2")){
			document.getElementById("printPreview2").disabled=false;
		}
		if(document.getElementById("closeButtonNew"))
			document.getElementById("closeButtonNew").disabled=false;
		if(document.getElementById("comments"))
			document.getElementById("comments").disabled=false;
		if(document.getElementById("paymentid"))
			document.getElementById("paymentid").disabled=false;
		if(document.getElementById("actionName"))
			document.getElementById("actionName").disabled=false;
		if(null != document.getElementById("departmentid") ){
			document.getElementById("departmentid").disabled=false;    
			document.getElementById("designationId").disabled=false;
			document.getElementById("approverUserId").disabled=false;
		}
				
		
	
		
		if(document.getElementById("wfBtn0"))
		{
		document.getElementById("wfBtn0").disabled=false;
		}
		if(document.getElementById("wfBtn1"))
		{
		document.getElementById("wfBtn1").disabled=false;
		}
		if(document.getElementById("wfBtn2"))
		{
		document.getElementById("wfBtn3").disabled=false;
		}
		}
	
		
</SCRIPT>

<s:if test="%{showApprove}">

<s:if test="%{paymentheader.state.value != 'NEW'}">
	<s:if test="%{paymentheader.state.id!=null}">
		<div id="labelAD" align="center">
 			<div class="subheadsmallnew"><strong><s:text name="inbox.payment.history"/></strong></div>
		</div>
	  	<div id="wfHistoryDiv">
		  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
		        <c:param name="stateId" value="${paymentheader.state.id}"></c:param>
		    </c:import>
	  	</div>
	</s:if>
</s:if>

<div  class="buttonbottom" id="newbuttondiv">
		<s:hidden id="paymentid" name="paymentid" value="%{paymentheader.id}"/>
		<s:hidden  name="actionname" id="actionName" value="%{action}"/>
		<s:iterator value="%{getValidActions()}" var="p"  status="s">
		  <s:submit type="submit"  cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="sendForApproval" onclick="return balanceCheck(this, '%{name}','%{description}');return false;"/>
		</s:iterator>
		<input type="submit" id="closeButtonNew" value="Close" onclick="javascript:window.close()" class="button"/>
		<s:submit cssClass="button" id="printPreview1" value="Print Preview"  onclick="printVoucher()"/>
	</div>
<script>
if(document.getElementById('actionName').value!='')
		{
			if(document.getElementById('wfBtn0'))
				document.getElementById('wfBtn0').style.display='none';
			if(document.getElementById('wfBtn1'))
				document.getElementById('wfBtn1').style.display='none';
		}
		{
			if(opener && opener.top && opener.top.document.getElementById('inboxframe'))
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
		}
		
		 <s:if test="%{validateUser('balancecheck')}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
	</s:if>	
	<s:if test="%{showMode!='view'}" >	
	<s:if test="%{balance=='-1'}">
	alert("FundFlow Report not Generated to check Bank Balance. Please generate Report First");
	for(var i=0;i<document.forms[0].length;i++)
	{
	if(document.forms[0].elements[i].id!='closeButtonNew')
		document.forms[0].elements[i].disabled =true;
	}
	</s:if>	
	</s:if>		
</script>						  
</s:if>	
	
	<div class="subheadsmallnew"/> </div>
	<div class="mandatory" align="left">* Mandatory Fields</div>
	</div>

</s:push><s:token/>
</s:form>				  
</body>



</html>