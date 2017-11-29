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

<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/directBankPaymentHelper.js?rnd=${app_release_no}"></script>

<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 350px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
<script>
	path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		var makeVoucherDetailTable = function() {
		<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>
		var voucherDetailColumns = [                     
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",hidden:true,label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","hidden")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
		</s:if>
		<s:else>
		var voucherDetailColumns = [ 
   			{key:"functionid",hidden:true,width:90,  formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
   			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","text")},         
   			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
   			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100,   formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
   			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
   			{key:"debitamount",label:'Debit Amount',width:90, className:'bluebgheadtd' ,formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
   			{key:"creditamount",label:'Credit Amount',width:90,formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
   			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
   			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
   		];
	</s:else>   
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		billDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
				updateAccountTableIndex();
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					updateDebitAmountJV();updateCreditAmountJV();
					check();
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}
			
			        
		}
		);
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
			{key:"subledgerCode",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:120, formatter:createSLDetailCodeTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)", ".search", "openSearchWindowFromJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		subLedgersTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
				updateSLTableIndex();
				check();
			}
			if (column.key == 'Delete') { 			
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}        
		});
	
		<s:iterator value="subLedgerlist" status="stat">
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"subledgerCode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKey"/>',
					"detailKey":'<s:property value="detailKey"/>',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});'<s:property value="glcode.id"/>'
				var index = '<s:property value="#stat.index"/>';
				updateGridSLDropdownJV('glcode.id',index,'<s:property value="glcode.id"/>','<s:property value="subledgerCode"/>');
				updateGridSLDropdownJV('detailType.id',index,'<s:property value="detailType.id"/>','<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('subledgerCode',index,'<s:property value="subledgerCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKey"/>');
				updateSLGridPJV('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
	
	}
	var amountshouldbenumeric='<s:text  name="amount.should.be.numeric"/>';
	var succesMessage='<s:text name="directbank.transaction.succcess"/>';
	var totalsnotmatchingamount='<s:text name="totals.not.matching.amount"/>';
	var 	button='<s:property value="button"/>';
	</script>
</head>
<body
	onload="onLoadTask_edit();loadDropDownCodesExcludingCashAndBank();loadDropDownCodesFunction();">
	<s:form action="directBankPayment" theme="css_xhtml" name="dbpform"
		validate="true">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Bank to Bank Transfer" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Modify Direct Bank Payment</div>
				<div id="listid" style="display: block">
					<br />
				</div>
			</div>
			<div align="center">
				<font style='color: red;'>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>
			<table border="0" width="100%" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox" width="10%"></td>
					<s:if test="%{shouldShowHeaderField('vouchernumber')}">
						<td class="bluebox" width="22%"><s:text name="voucher.number" />
							<span class="mandatory">*</span></td>
						<td class="bluebox" width="22%">
							<table width="100%">
								<tr>
									<td style="width: 25%"><input type="text"
										name="voucherNumberPrefix" id="voucherNumberPrefix"
										readonly="true" style="width: 100%" /></td>
									<td width="75%"><input type="text"
										name="voucherNumberRest" id="voucherNumberRest"
										onblur="updateVoucherNumber();" /></td>
								</tr>
							</table>
						</td>

						<s:hidden name="voucherNumber" id="voucherNumber" />
					</s:if>
					<s:else>
						<td class="bluebox" width="22%"><s:text name="voucher.number" />
							<span class="mandatory">*</span></td>
						<td class="bluebox" width="22%">
							<table width="100%">
								<tr>
									<td style="width: 25%"><input type="text"
										name="voucherNumberPrefix" id="voucherNumberPrefix"
										readonly="true" style="width: 100%" /></td>
									<td width="75%"><input type="text"
										name="voucherNumberRest" id="voucherNumberRest"
										readonly="true" /></td>
								</tr>
							</table>
						</td>
						<s:hidden name="voucherNumber" id="voucherNumber" />
					</s:else>
					<s:hidden name="id" />
					<td class="bluebox" width="18%%"><s:text name="voucher.date" />
						<span class="mandatory">*</span></td>
					<td class="bluebox" width="34%"><s:date name='voucherDate'
							var="voucherDateId" format='dd/MM/yyyy' /> <s:textfield
							name="voucherDate" id="voucherDate" value='%{voucherDateId}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('dbpform.voucherDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" />
					</A></td>
				</tr>
				<%@include file="directBankPayment-form.jsp"%>
				<s:hidden name="typeOfAccount" id="typeOfAccount"
					value="%{typeOfAccount}" />
				<tr>
					<td colspan="6"><%@include
							file="../voucher/workflowApproval.jsp"%>
					</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><strong>Comments</strong></td>
					<td class="bluebox" colspan="4"><s:textarea name="comments"
							id="comments" cols="100" rows="3" onblur="checkLength(this)"
							value="%{getComments()}" /></td>
				</tr>
			</table>
			<table align="center">
				<tr class="buttonbottom" id="buttondiv" style="align: middle">
					<s:hidden name="actionname" id="actionName" value="%{action}" />
					<s:iterator value="%{getValidActions()}" var="p" status="s">
						<td><s:submit type="submit" cssClass="buttonsubmit"
								value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
								method="edit"
								onclick="return validate('%{name}','%{description}')" /></td>
					</s:iterator>
					<td><s:submit method="cancelPayment" value="Cancel Payment"
							cssClass="buttonsubmit" id="updatebtnid"
							onclick="document.getElementById('actionName').value='canccelPayment';" /></td>
					<td><input type="submit" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
				</tr>
			</table>
			<div class="subheadsmallnew" /></div>
			<div class="mandatory" align="left">* Mandatory Fields</div>
			</div>
		</s:push>
		<s:if test="%{validateUser('balancecheck')}">
			<script>
			if(document.getElementById('balanceText'))
			{
				document.getElementById('bankbalanceRow').style.visibility='visible';
				//document.getElementById('balance').innerHTML='<s:property value="%{balance}"/>'
			}
		</script>
		</s:if>
		<script>
		{
			if(opener && opener.top && opener.top.document.getElementById('inboxframe'))
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
		}
	</script>

	</s:form>
	<SCRIPT type="text/javascript">
function validateAppoveUser(name,value){
			//document.getElementById('lblError').innerHTML ="";
			document.getElementById("actionName").value= name;

			<s:if test="%{wfitemstate =='END'}">
				if(value == 'Approve' || value == 'Reject') {
					document.getElementById("approverUserId").value=-1;
					return true;
				}
			</s:if>
			<s:else>
				if( (value == 'Approve' || value == 'Forward' || value=='Save And Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
					bootbox.alert("please select User");
					//document.getElementById('lblError').innerHTML ="Please Select the user";
					return false;
				}
			</s:else>
			
			return true;
		}
		
function onLoadTask_edit() {
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumberRest').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length-1);
			   updateVoucherNumber();
		     if (button != null && button != "") {
					if (document.getElementById("Errors").innerHTML == '') {
					bootbox.alert(succesMessage);
					   if (button == "Save_Close") {
							window.close();
						} else if (button == "Save_View") {
							var vhId = '<s:property value="voucherHeader.id"/>';
							document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="
									+ vhId;
							document.forms[0].submit();
						} else if (button == "Save_New") {
							document.forms[0].button.value = '';
							document.forms[0].action = "directBankPayment!newform.action";
							document.forms[0].submit();
						}
					}
					 
				}

			}
			
	function validate(name,value)
	{
	
	
		if(document.getElementById('balanceAvl') && document.getElementById('balanceAvl').style.display=="block" )
			{
				if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('availableBalance').value))
				{
					bootbox.alert(insuffiecientBankBalance);
					return false;
				}
			}
			if(!validateAppoveUser(name,value))
			{
			return false;
			}
			return true;
	}
			

	
	
	
</SCRIPT>
</body>
</html>
