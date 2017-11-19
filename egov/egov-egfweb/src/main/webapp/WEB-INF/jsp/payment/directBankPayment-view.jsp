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
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<head>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/directBankPaymentHelper.js"></script>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
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
function showHistory(stateId)
{
var url="../voucher/common-showHistory.action?stateId="+stateId;
		window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}

	path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		var makeVoucherDetailTable = function() {  
		<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>                
		var voucherDetailColumns = [        
			{key:"functionid",hidden:true, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",hidden:true,label:'Function Name', formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","hidden")},
			{key:"glcodeid",hidden:true, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory1">*</span>', formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
			{key:"creditamount",label:'Credit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow_billDetailTable(this)")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow(this)")}
		];	
		</s:if>
		<s:else>
		var voucherDetailColumns = [ 
   			{key:"functionid",hidden:true,  formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
   			{key:"function",label:'Function Name', formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","text")},         
   			{key:"glcodeid",hidden:true, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
   			{key:"glcode",label:'Account Code <span class="mandatory1">*</span>',   formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
   			{key:"accounthead", label:'Account Head',formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
   			{key:"debitamount",label:'Debit Amount', formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
   			{key:"creditamount",label:'Credit Amount',formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
   			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
   			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
   		];
	</s:else>   
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
            td.width = "90";
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
            td.width = "90";
            td.align = "right";
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		document.getElementById('totaldbamount').value=totaldbamt;
		document.getElementById('totalcramount').value=totalcramt;
        };
		var glcodeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.glcodeList">
glcodeOptions.push({label: '<s:property value="glcode"/>', value: '<s:property value="id"/>'});
	</s:iterator>
	var detailtypeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
detailtypeOptions.push({label: '<s:property value="name"/>', value: '<s:property value="id"/>'});
	</s:iterator>
	
	
	
		
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
			{key:"glcode",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory1">*</span>', formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory1">*</span>', formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","validateDetailCodeForJV(this)")},
			{key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount', formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
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

    };
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
			bootbox.alert("This row can not be deleted");
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
	
	document.forms[0].action='../report/billPaymentVoucherPrint-print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}

	</script>
</head>
<body>
	<s:form action="directBankPayment" theme="simple" name="dbpform">
	<div class="formmainbox">
		<s:push value="model">
			<div align="center">
				<font style='color: red;'>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory1">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>
			<div align="left">
				<div class="tabber">
					<div class="tabbertab">
						<h2>Payment Details</h2>

						<div id="budgetSearchGrid"
							style="display: block; width: 100%; border-top: 1px solid #ccc;">
							<br />

							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td class="bluebox" width="10%"></td>
									<td class="bluebox" width="22%"><s:text
											name="voucher.number" /></td>
									<td class="bluebox" width="22%"><s:textfield
											name="voucherNumber" id="voucherNumber" /></td>
									<s:hidden name="id" />
									<td class="bluebox" width="18%"><s:text
											name="voucher.date" /><span class="mandatory1">*</span></td>
									<td class="bluebox" width="38%"><input type="text"
										name="voucherDate"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										value='<s:date name="voucherDate" format="dd/MM/yyyy"/>' /> <a
										href="javascript:show_calendar('cbtbform.voucherDate');"
										style="text-decoration: none">&nbsp;<img tabIndex="-1"
											src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A></td>
								</tr>
								<div class="buttonbottom"></div>

								<%@include file="directBankPayment-form.jsp"%>





							</table>
						</div>
					</div>

					<div class="tabbertab" id="chequetab">
						<h2>Cheque Details</h2>
						<span>
							<table align="center" border="0" cellpadding="0" cellspacing="0"
								class="newtable">
								<tr>
									<td colspan="6"><div class="subheadsmallnew">Cheque
											Details</div></td>
								</tr>
								<tr>
									<td colspan="4">
										<div style="float: left; width: 100%;">
											<table id="chequeTable" align="center" border="0"
												cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<s:if
														test="%{paymentheader.type == 'cash' || paymentheader.type == 'Cash' || paymentheader.type == 'Cheque' || paymentheader.type == 'cheque'}">
														<th class="bluebgheadtdnew">Cheque Number
														</td>
														<th class="bluebgheadtdnew">Cheque Date
														</td>
													</s:if>
													<s:else>
														<th class="bluebgheadtdnew">RTGS Number
														</td>
														<th class="bluebgheadtdnew">RTGS Date
														</td>
													</s:else>
													<th class="bluebgheadtdnew">Party Code
													</td>
													<th class="bluebgheadtdnew">Cheque Amount(Rs)
													</td>
													<th class="bluebgheadtdnew">Cheque Status
													</td>
												</tr>
												<s:if test="%{instrumentHeaderList.size()>0}">
													<s:iterator var="p" value="instrumentHeaderList" status="s">
														<tr>
															<s:if
																test="%{paymentheader.type == 'cash' || paymentheader.type == 'Cash' || paymentheader.type == 'Cheque' || paymentheader.type == 'cheque'}">
																<td style="text-align: center"
																	class="blueborderfortdnew"><s:property
																		value="%{instrumentNumber}" /></td>
																<td style="text-align: center"
																	class="blueborderfortdnew"><s:date
																		name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
															</s:if>
															<s:else>
																<td style="text-align: center"
																	class="blueborderfortdnew"><s:property
																		value="%{transactionNumber}" /></td>
																<td style="text-align: center"
																	class="blueborderfortdnew"><s:date
																		name="%{transactionDate}" format="dd/MM/yyyy" /></td>
															</s:else>
															<td style="text-align: center" class="blueborderfortdnew"><s:property
																	value="%{payTo}" /></td>
															<td style="text-align: right" class="blueborderfortdnew"><s:text
																	name="format.number">
																	<s:param value="%{instrumentAmount}" />
																</s:text></td>
															<td style="text-align: center" class="blueborderfortdnew"><s:property
																	value="%{statusId.description}" /></td>
														</tr>
													</s:iterator>
												</s:if>
											</table>
											<s:if
												test="%{instrumentHeaderList==null || instrumentHeaderList.size==0}">
												<div class="bottom" align="center">
													<s:text name="chq.not.found"></s:text>
												</div>
											</s:if>
										</div>
									</td>
								</tr>
							</table>
						</span>
					</div>

				</div>

				<div class="buttonbottom" id="viewButton" align="center">
					<input type="button" id="closeButton" value="Close"
                           onclick="window.close()" class="button"/>
					<s:submit cssClass="button" id="printPreview2"
						value="Print Preview" onclick="printVoucher()" />

				</div>
				</br>
				</br>
				</br>
				</br>
				<s:if test="%{showApprove}">
				<div class="commentsTab" align="center">
					<table border="0" width="100%">
						<tr>
							<td class="bluebox">Comments</td>
							<td class="bluebox"><s:textarea name="comments"
									id="comments" cols="150" rows="3" onblur="checkLength(this)"
									value="%{getComments()}" /></td>
						</tr>
						<br />
					</table>
				</div>
					</br>
					</br>
					</br>
					</br>
					<script>
						document.getElementById('viewButton').style.display="none";
					</script>

				</s:if>


				<script type="text/javascript">

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
		
	<s:if test="%{showMode!='view'}" >
		
		<s:if test="%{balance=='-1'}">
			for(var i=0;i<document.forms[0].length;i++)
			{
				if(document.forms[0].elements[i].id!='closeButtonNew' || document.forms[0].elements[i].id!='comments')
				document.forms[0].elements[i].disabled =true;
				
			}
				bootbox.alert("FundFlow Report not Generated to check Bank Balance. Please generate Report First");
	
		</s:if>	
	</s:if>	
	
</script>

				<s:if test="%{showApprove}">
					<s:if test="%{showMode!='create' && showMode!='view' }">
						<%@ include file='../payment/commonWorkflowMatrix.jsp'%>
						<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
						<div class="buttonbottom" id="newbuttondiv" align="center">
							<s:submit cssClass="button" id="printPreview1"
								value="Print Preview" onclick="printVoucher()" />
						</div>
					</s:if>
					<s:else>
						<div class="buttonbottom" id="newbuttondiv" align="center">
							<s:submit cssClass="button" id="printPreview1"
								value="Print Preview" onclick="printVoucher()" />
							<input type="button" name="button2" id="button2" value="Close"
								class="button" onclick="window.close();" />
						</div>
					</s:else>
					<s:hidden id="paymentid" name="paymentid"
						value="%{paymentheader.id}" />
					<s:hidden name="actionname" id="actionName" value="%{action}" />
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
		
		 <s:if test="%{canCheckBalance==true}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
	    </s:if>	    
	    function onSubmit()
	    {
	    	document.forms[0].action='${pageContext.request.contextPath}/payment/directBankPayment-sendForApproval.action';
    		document.forms[0].submit();
	    }
</script>
				</s:if>

				<div class="subheadsmallnew" />
			</div>
			<div class="mandatory1" align="left">* Mandatory Fields</div>
			</div>

		</s:push>
		</div>
	</s:form>
</body>



</html>
