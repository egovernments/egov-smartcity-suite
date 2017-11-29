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
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title><s:property value="type" /> JV-Create</title>
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
</head>

<script>
		path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		function addGridRows(){
			<s:iterator value="billDetails.tempList" status="stat">
					billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
						"functionid":'<s:property value="functionid"/>',
						"function":'<s:property value="function"/>',
						"glcodeid":'<s:property value="glcodeid"/>',
						"glcode":'<s:property value="glcode"/>',
						"accounthead":'<s:property value="accounthead"/>',
						"debitamount":'<s:property value="%{debitamount}"/>',
						"creditamount":'<s:property value="%{creditamount}"/>'
					});
					var index = '<s:property value="#stat.index"/>';
					updateGridPJV('functionIdDetail',index,'<s:property value="functionid"/>');
					updateGridPJV('functionDetail',index,'<s:property value="function"/>');
					updateGridPJV('glcodeIdDetail',index,'<s:property value="glcodeid"/>');
					updateGridPJV('glcodeDetail',index,'<s:property value="glcode"/>');
					updateGridPJV('accounthead',index,'<s:property value="accounthead"/>');
					updateGridPJV('debitAmountDetail',index,'<s:property value="debitamount"/>');
					updateGridPJV('creditAmountDetail',index,'<s:property value="creditamount"/>');
					totaldbamt = totaldbamt+parseFloat('<s:property value="debitamount"/>');
					totalcramt = totalcramt+parseFloat('<s:property value="creditamount"/>');
			</s:iterator>
			document.getElementById('totaldbamount').value=totaldbamt;
			document.getElementById('totalcramount').value=totaldbamt;
		}
		
		function addGridRowsSL(){
			<s:iterator value="billDetails.subLedgerlist" status="stat">
					subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
						"glcode.id":'<s:property value="glcode"/>',
						"detailType.id":'<s:property value="detailType"/>',
						"detailCode":'<s:property value="detailCode"/>',
						"detailKeyId":'<s:property value="detailKey"/>',
						"detailKey":'<s:property value="detailKey"/>',
						"debitAmount":'<s:property value="%{debitAmount}"/>',
						"creditAmount":'<s:property value="%{creditAmount}"/>'
					});
					var index = '<s:property value="#stat.index"/>';
					updateGridSLDropdownPJV('glcode.id',index,'<s:property value="glcode.id"/>');
					updateGridSLDropdownPJV('detailType.id',index,'<s:property value="detailType.id"/>');
					updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
					updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
					updateSLGridPJV('detailKey',index,'<s:property value="detailKey"/>');
					updateSLGridPJV('debitAmount',index,'<s:property value="debitAmount"/>');
					updateSLGridPJV('creditAmount',index,'<s:property value="creditAmount"/>');
			</s:iterator>
		}
		
		var makeVoucherDetailTable = function() {
		var voucherDetailColumns = [ 
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionPJV(VOUCHERDETAILLIST,".functionDetail")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterPJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'<s:text name="billVoucher.approve.dbtamt"/>',width:90, formatter:createAmountFieldFormatterPJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmount()")}, 
			{key:"creditamount",label:'<s:text name="billVoucher.approve.crdamt"/>',width:90, formatter:createAmountFieldFormatterPJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmount()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		billDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					updateDebitAmount();updateCreditAmount();
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}
			
			        
		});
		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 5;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true'/>";
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
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterPJV(SUBLEDGERLIST),  dropdownOptions:glcodeOptions},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterPJV(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:90, formatter:createSLTextFieldFormatterPJV(SUBLEDGERLIST,".detailCode","validateDetailCode(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterPJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterPJV(SUBLEDGERLIST,".detailKey","")},
			{key:"debitAmount",label:'<s:text name="billVoucher.approve.dbtamt"/>',width:90, formatter:createSLAmountFieldFormatterPJV(SUBLEDGERLIST,".debitAmount")},
			{key:"creditAmount",label:'<s:text name="billVoucher.approve.crdamt"/>',width:90, formatter:createSLAmountFieldFormatterPJV(SUBLEDGERLIST,".creditAmount")},
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
		//subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
	}
	function openSource(){
	var url = '<s:property value='%{getSourcePath()}' />'
	window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')
	}
function validate(name,value){
	document.getElementById("actionName").value= name;
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval'|| value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}
	</script>
<body
	onload="loadDropDownCodes();loadDropDownCodesFunction();checkBillId();refreshInbox()">
	<s:form action="preApprovedVoucher" theme="simple">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="PJV-Create" />
		</jsp:include>
		<font style='color: red;'>
			<p class="error-block" id="lblError" style="font: bold"></p>
		</font>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="formheading"></div>
			<div id="listid" style="display: block">
				<br />
				<jsp:include page="voucherViewHeader.jsp" />

				<s:hidden id="billid" name="billid" value="%{egBillregister.id}" />
				<s:hidden id="id" name="id" value="%{voucherHeader.id}" />
				<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}" />
				<s:hidden name="actionName" id="actionName" />

				<s:if test="%{type == finConstExpendTypeContingency}">
					<table>
						<tr class="bluebox">
							<a href="#" onclick="openSource()">Source</a>
						</tr>
					</table>
				</s:if>
				<br />
				<div id="labelAD" align="center">
					<table width="80%" border=0 id="labelid">
						<th>Account Details</th>
					</table>
				</div>
				<div class="yui-skin-sam" align="center">
					<div id="billDetailTable"></div>
				</div>
				<script>
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
				<div id="codescontainer"></div>
				<br />
				<div id="labelSL" align="center">
					<table width="80%" border=0 id="labelid">
						<th>Sub-Ledger Details</th>
					</table>
				</div>

				<div class="yui-skin-sam" align="center">
					<div id="subLedgerTable"></div>
				</div>
				<script>
			addGridRows();
			makeSubLedgerTable();
			addGridRowsSL();
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>

				<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
					<%@include file="workflowApproval.jsp"%>
				</s:if>
				<div align="center">

					<table border="0" width="80%">
						<tr>
							<td class="bluebox">Comments</td>
							<td class="bluebox"><s:textarea name="comments"
									id="comments" cols="50" rows="3"
									value="%{voucherHeader.state.text1}" onblur="checkLength(this)" /></td>
						</tr>

						<tr>
							<td />
							<td><s:submit type="submit" cssClass="buttonsubmit"
									value="Save as Working Copy" id="save" name="save"
									method="saveAsWorkingCopy" onclick="return validate()" /> <s:iterator
									value="%{getValidActions('')}" var="p">
									<s:submit type="submit" cssClass="buttonsubmit"
										value="%{description}" id="%{name}" name="%{name}"
										method="sendForApprovalForWC"
										onclick="return validate('%{name}','%{description}')" />
								</s:iterator> <s:submit cssClass="button" id="print" value="Print Preview"
									action="journalVoucherPrint" method="print" /> <input
								type="button" value="Close" onclick="javascript:window.close()"
								class="button" /></td>
						</tr>
					</table>

				</div>
			</div>
		</div>
		<div id="codescontainer"></div>
	</s:form>

</body>

</html>
