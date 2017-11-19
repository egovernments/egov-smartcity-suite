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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Journal Voucher Reverse</title>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
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

.yui-skin-sam tr.yui-dt-odd {
	background-color: #FFF;
}
</style>
</head>
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
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")}
			//{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			//{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
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
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}
			
			        
		});
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
			{key:"glcode",hidden:true,width:90, formatter:createSLTextFieldFormatterPJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterPJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:90, formatter:createSLTextFieldFormatterPJV(SUBLEDGERLIST,".detailCode","validateDetailCodeForJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterPJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterPJV(SUBLEDGERLIST,".amount")}
			//{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			//{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
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
				updateSLGridPJV('subledgerCode',index,'<s:property value="subledgerCode"/>');
				updateSLGridPJV('detailTypeName',index,'<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKey"/>');
				updateSLGridPJV('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
		
	}

	function validateReverseInput(arg) { 
		var numGenerationMode = document.getElementById('voucherNumGenMode').value;
		if(numGenerationMode == 'manual') {
			if(null !=document.getElementById('reversalVoucherNumber') && document.getElementById('reversalVoucherNumber').value.trim().length == 0){
				document.getElementById('lblError').innerHTML = "Please enter Reverse Voucher number";
				return false;
			}
		}
		
		if(document.getElementById('reversalVoucherDate').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please enter reverse voucher date ";
			return false;
		}
		
		document.getElementById('lblError').innerHTML = "";
		if(arg == 'Reverse_View') {
			document.getElementById('button').value='Reverse_View';
		} else  {
			document.getElementById('button').value='Reverse_Close'
		}
		enableAll();
		return true;
	}
	
	function enableAll()
	{
		for(var i=0;i<document.forms[0].length;i++)
			document.forms[0].elements[i].disabled =false;
	}	

	function disableControls(frmIndex, isDisable)
	{
		for(var i=0;i<document.forms[frmIndex].length;i++)
			document.forms[frmIndex].elements[i].disabled =isDisable;
	}


	function onLoadTask_reverse() {
			var numGenerationMode = document.getElementById('voucherNumGenMode').value;
			var button = '<s:property value="button"/>';
			if (button != null && button != "") {
				var trgtmsg = '<s:property value="target"/>';
				//bootbox.alert(trgtmsg);
				if (button == "Reverse_Close") {
					var message = '<s:property value="message"/>';
					if(trgtmsg == "success") {
						bootbox.alert(message);
						window.close();
					} else {
						disableControls(0,true);
						document.getElementById('button').disabled=false;
						document.getElementById('Reverse_View').disabled=false;
						document.getElementById('Reverse_Close').disabled=false;
						document.getElementById('Close').disabled=false;
						document.getElementById('reversalVoucherDate').disabled=false;
						if(numGenerationMode == 'manual') {
							document.getElementById('reversalVoucherNumber').disabled=false;
						}
					}
				} else if (button == "Reverse_View") {
					var message = '<s:property value="message"/>';
					var vhid = document.getElementById('voucherHeader.id').value;
					var url = 'preApprovedVoucher!loadvoucherview.action?vhid='+ vhid;
					if(trgtmsg == "success") {
						bootbox.alert(message);
						window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
					} else {
						disableControls(0,true);
						document.getElementById('button').disabled=false;
						document.getElementById('Reverse_View').disabled=false;
						document.getElementById('Reverse_Close').disabled=false;
						document.getElementById('Close').disabled=false;
						document.getElementById('reversalVoucherDate').disabled=false;
						if(numGenerationMode == 'manual') {
							document.getElementById('reversalVoucherNumber').disabled=false;
						}
					}
				}
			}
			else
			{
				disableControls(0,true);
				document.getElementById('button').disabled=false;
				document.getElementById('Reverse_View').disabled=false;
				document.getElementById('Reverse_Close').disabled=false;
				document.getElementById('Close').disabled=false;
				document.getElementById('reversalVoucherDate').disabled=false;
				if(numGenerationMode == 'manual') {
					document.getElementById('reversalVoucherNumber').disabled=false;
				}
			}
	}
	
</script>
<body
	onload="loadDropDownCodes();loadDropDownCodesFunction();onLoadTask_reverse();">
	<s:form action="journalVoucherReverse" theme="simple"
		name="JVReverseForm">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Journal Voucher Reverse" />
			</jsp:include>
			<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
			</span>

			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Journal Voucher Reverse</div>
				<div id="listid" style="display: block">

					<font style='color: red; font-weight: bold'>
						<p class="error-block" id="lblError"></p>
					</font> <input type="hidden" name="selectedDate" id="selectedDate">

					<table border="0" width="100%">
						<tr>

							<td class="bluebox"><s:text name="voucher.number" /></td>
							<td class="bluebox"><s:textfield name="voucherNumber"
									id="voucherNumber" readonly="true" /></td>

							<td class="bluebox"><s:text name="voucher.date" /></td>
							<td class="bluebox"><s:date name="voucherDate"
									var="voucherDateId" format="dd/MM/yyyy" /> <s:textfield
									name="voucherDate" id="voucherDate" value="%{voucherDateId}"
									maxlength="10" readonly="true" size="10" /> (dd/mm/yyyy)</td>
						</tr>

						<%@include file="journalVoucherReverse-form.jsp"%>

						<br />

						<table border="0" width="80%" id="reversalVoucherId">
							<tr>
								<s:if test="%{shouldShowHeaderField('vouchernumber')}">
									<td class="bluebox"><s:text name="reversalVoucherNumber" /><span
										class="mandatory">*</span></td>
									<td class="bluebox"><s:textfield
											name="reversalVoucherNumber" id="reversalVoucherNumber" /></td>
									<s:hidden id="voucherNumGenMode" name="voucherNumGenMode"
										value="manual" />
								</s:if>
								<s:else>
									<s:hidden id="voucherNumGenMode" name="voucherNumGenMode"
										value="auto" />
								</s:else>
								<td class="bluebox"><s:text name="reversalVoucherDate" /><span
									class="mandatory">*</span></td>
								<td class="bluebox"><s:textfield name="reversalVoucherDate"
										value='%{getFormattedNewDate()}' id="reversalVoucherDate"
										size="10"
										onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
									href="javascript:show_calendar('JVReverseForm.reversalVoucherDate');"
									style="text-decoration: none">&nbsp;<img
										src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
							</tr>
						</table>

						<div class="subheadsmallnew" /></div>
						<div class="mandatory" align="left">* Mandatory Fields</div>

						<div align="center" class="buttonbottom">
							<s:hidden name="button" id="button" />
							<s:submit type="submit" cssClass="buttonsubmit" id="Reverse_View"
								name="Reverse_View" value="Reverse & View"
								onclick="return validateReverseInput('Reverse_View');"
								method="reverse" />
							<s:submit type="submit" cssClass="buttonsubmit"
								id="Reverse_Close" name="Reverse_Close" value="Reverse & Close"
								onclick="return validateReverseInput('Reverse_Close');"
								method="reverse" />
							<input type="button" id="Close" value="Close"
								onclick="javascript:window.close()" class="button" />
						</div>
						<br />

						</div>
						</div>
						<div id="codescontainer"></div>
						<s:hidden id="cgn" name="cgn"></s:hidden>
						<s:hidden name="showMode" id="showMode" />
						<s:hidden name="saveMode" id="saveMode" />

						<input type="hidden" id="voucherTypeBean.voucherName"
							name="voucherTypeBean.voucherName" value="JV General" />
						<input type="hidden" id="voucherTypeBean.voucherType"
							name="voucherTypeBean.voucherType" value="Journal Voucher" />
						<input type="hidden" id="voucherTypeBean.voucherNumType"
							name="voucherTypeBean.voucherNumType" value="Journal" />
						<input type="hidden" id="voucherTypeBean.cgnType"
							name="voucherTypeBean.cgnType" value="JV" />

						<input type="hidden" id="voucherHeader.id" name="voucherHeader.id"
							value='<s:property value="voucherHeader.id"/>' />
						<input type="hidden" id="voucherHeader.name"
							name="voucherHeader.name"
							value='<s:property value="voucherHeader.name"/>' />
						<input type="hidden" id="voucherHeader.type"
							name="voucherHeader.type"
							value='<s:property value="voucherHeader.type"/>' />


						</s:push>
						</s:form>
</body>
</html>
