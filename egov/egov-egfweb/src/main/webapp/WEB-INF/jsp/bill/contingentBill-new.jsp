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
<title><s:text name="contingent.bill" /></title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contingentBillHelper.js?rnd=${app_release_no}"></script>
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/autocomplete-debug.js"></script>
<link rel="stylesheet" href="/EGF/struts/xhtml/styles.css"
	type="text/css" />

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

}
.yui-dt7-col-name .yui-dt-liner {
	overflow: hidden;
	width: 560px;
}
</style>

<script type="text/javascript">
var detailTypeId;
mode='new';
<s:iterator value="commonBean.checkListValuesMap" status="stat">
var option<s:property value="#stat.index"/>=new Option('<s:property value="key"/>','<s:property value="value"/>');
</s:iterator>




var succesMessage='<s:text name="transaction.success"/>';
var 	button='<s:property value="button"/>';
var enterpayto='<s:text name="enter.pato"/>';
var entityNotSelected='<s:text name="entity.notselected"/>';
var invalidEntityselected='<s:text name="invalidEntityselected"/>';
var invalidAccountCode='<s:text name="invalidAccountCode"/>';
path="${pageContext.request.contextPath}";
var totaldbamt=0,totalcramt=0;
var makeVoucherDetailTable = function() {
 var voucherDetailColumns = [ 
{key:"glcode",label:'Account Code', formatter:glcodeFormatter("billDetailsTable",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head', width:600, formatter:accountheadFormatter("billDetailsTable",".accounthead","text")},				
{key:"amount",label:'Debit', formatter:amountFormatter("billDetailsTable",".debitAmountDetail","text")}, 
{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow('billDetailsTable',this)")},
{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow('billDetailsTable',this)")}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
<s:iterator value="billDetailslist" status="stat">

	billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
		
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"isSubledger":'<s:property value="isSubledger"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"amount":'<s:property value="%{debitAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTable','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTable','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTable','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTable','debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>


	

}
var totaldbamt=0,totalcramt=0;
var makeVoucherDetailTableCredit = function() {
 var voucherDetailColumnsCredit = [ 
{key:"glcode",label:'Account Code', disable:true, formatter:glcodeFormatter("billDetailsTableCredit",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head', width:600,disable:true,formatter:accountheadFormatter("billDetailsTableCredit",".accounthead","text")},				
{key:"amount",label:'Credit', formatter:amountFormatter("billDetailsTableCredit",".debitAmountDetail","text")}, 
{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow('billDetailsTableCredit',this)")},
{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow('billDetailsTableCredit',this)")}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableCredit = new YAHOO.widget.DataTable("billDetailTableCredit",voucherDetailColumnsCredit, voucherDetailDS);
<s:iterator value="billDetailslist" status="stat">

	billDetailsTableCredit.addRow({SlNo:billDetailsTableCredit.getRecordSet().getLength()+1,
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"amount":'<s:property value="%{debitAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTableCredit','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTableCredit','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTableCredit','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableCredit','debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>


}
var makeVoucherDetailTableNet = function() {
 var voucherDetailColumnsNet = [ 
{key:"glcode",label:'Account Code', formatter:netglcodeFormatter("billDetailsTableNet",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head',width:600,formatter:accountheadFormatter("billDetailsTableNet",".accounthead","text")},				
{key:"amount",label:'Net',formatter:amountFormatter("billDetailsTableNet",".debitAmountDetail","text")},
{key:'Add',label:'Add'},
{key:'Delete',label:'Delete'}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableNet = new YAHOO.widget.DataTable("billDetailTableNet",voucherDetailColumnsNet, voucherDetailDS);
<s:iterator value="billDetailslist" status="stat">

	billDetailsTableNet.addRow({SlNo:billDetailsTableNet.getRecordSet().getLength()+1,
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"amount":'<s:property value="%{debitAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTableNet','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTableNet','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTableNet','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableNet','debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>
}


var makeVoucherDetailTableFinal = function() {
 var voucherDetailColumns = [ 
{key:"glcode",label:'Account Code', formatter:glcodeFormatterCbillModify("billDetailsTableFinal",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head', width:600, formatter:accountheadFormatter("billDetailsTableFinal",".accounthead","text")},				
{key:"amount",label:'Debit', formatter:amountFormatterForGrid("billDetailsTableFinal",".debitAmountDetail","text")}, 
{key:'Add',label:'Add',formatter:createAddImageFormatterEmpty("${pageContext.request.contextPath}","addYUIRow('billDetailsTableFinal',this)")},
{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow1('billDetailsTableFinal',this)")}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableFinal = new YAHOO.widget.DataTable("billDetailTableFinal",voucherDetailColumns, voucherDetailDS);
<s:iterator value="billDetailsTableFinal" status="stat">

	billDetailsTableFinal.addRow({SlNo:billDetailsTableFinal.getRecordSet().getLength()+1,
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"isSubledger":'<s:property value="isSubldger"/>',
		"amount":'<s:property value="%{debitAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTableFinal','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTableFinal','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTableFinal','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableFinal','isSubledger',index,'<s:property value="isSubledger"/>');
	updateGridCbill('billDetailsTableFinal','debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>


	

}
var totaldbamt=0,totalcramt=0;
var makeVoucherDetailTableCreditFinal = function() {
 var voucherDetailColumnsCredit = [ 
{key:"glcode",label:'Account Code', formatter:glcodeFormatterCbillModify("billDetailsTableCreditFinal",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head', width:600, formatter:accountheadFormatter("billDetailsTableCreditFinal",".accounthead","text")},				
{key:"amount",label:'Credit', formatter:amountFormatterForGrid("billDetailsTableCreditFinal",".creditAmountDetail","text")},
{key:'Add',label:'Add',formatter:createAddImageFormatterEmpty("${pageContext.request.contextPath}","addYUIRow('billDetailsTableCreditFinal',this)")},
{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow1('billDetailsTableCreditFinal',this)")}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableCreditFinal = new YAHOO.widget.DataTable("billDetailTableCreditFinal",voucherDetailColumnsCredit, voucherDetailDS);
<s:iterator value="billDetailsTableCreditFinal" status="stat">

	billDetailsTableCreditFinal.addRow({SlNo:billDetailsTableCredit.getRecordSet().getLength()+1,
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"accounthead":'<s:property value="accounthead"/>',
	    "isSubledger":'<s:property value="isSubldger"/>',
		"amount":'<s:property value="%{creditAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	
	updateGridCbill('billDetailsTableCreditFinal','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTableCreditFinal','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTableCreditFinal','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableCreditFinal','isSubledger',index,'<s:property value="isSubledger"/>');
	updateGridCbill('billDetailsTableCreditFinal','creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>


}
var makeVoucherDetailTableNetFinal = function() {
 var voucherDetailColumnsNet = [ 
{key:"glcode",label:'Account Code', formatter:glcodeFormatter("billDetailsTableNetFinal",".glcodeDetail","text")},
{key:"accounthead", label:'Account Head', width:600, formatter:accountheadFormatter("billDetailsTableNetFinal",".accounthead","text")},				
{key:"amount",label:'Net',formatter:amountFormatter("billDetailsTableNetFinal",".creditAmountDetail","text")},
{key:'Add',label:'Add'},
{key:'Delete',label:'Delete'}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableNetFinal = new YAHOO.widget.DataTable("billDetailTableNetFinal",voucherDetailColumnsNet, voucherDetailDS);
<s:iterator value="billDetailsTableNetFinal" status="stat">
	billDetailsTableNetFinal.addRow({SlNo:billDetailsTableNetFinal.getRecordSet().getLength()+1,
		"glcodeid":'<s:property value="glcodeIdDetail"/>',
		"glcode":'<s:property value="glcodeDetail"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"isSubledger":'<s:property value="isSubldger"/>',
		"amount":'<s:property value="%{creditAmountDetail}"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTableNetFinal','glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
	updateGridCbill('billDetailsTableNetFinal','glcodeDetail',index,'<s:property value="glcodeDetail"/>');
	updateGridCbill('billDetailsTableNetFinal','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableNetFinal','isSubledger',index,'<s:property value="isSubledger"/>');
	updateGridCbill('billDetailsTableNetFinal','creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>
}

var makeVoucherDetailTableSubledger = function() {
 var voucherDetailColumnsSubledger = [ 
{key:"subledgerCode",label:'Subledger Code',  formatter:subledgerFormatter("billDetailsTableSubledger",".subledgerCode","text")},
{key:"detailCode",label:'Entity Code', formatter:detailcodeFormatter("billDetailsTableSubledger",".detailCode","text")},
{key:"detailName",label:'Entity Name', formatter:detailnameFormatter("billDetailsTableSubledger",".detailName","text")},
{key:"accounthead", label:'Account Head', width:400,formatter:accountheadFormatter1("billDetailsTableSubledger",".accounthead","text")},				
{key:"amount",label:'Amount',formatter:amountFormatter("billDetailsTableSubledger",".debitAmountDetail","text")},
{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}","addYUIRow('billDetailsTableSubledger',this)")},
{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}","deleteYUIRow('billDetailsTableSubledger',this)")}
];
var voucherDetailDS = new YAHOO.util.DataSource(); 
billDetailsTableSubledger = new YAHOO.widget.DataTable("billDetailTableSubledger",voucherDetailColumnsSubledger, voucherDetailDS);
<s:iterator value="billDetailsTableSubledger" status="stat">

	billDetailsTableSubledger.addRow({SlNo:billDetailsTableSubledger.getRecordSet().getLength()+1,
		"subledgerCode":'<s:property value="subledgerCode"/>',
		"detailName":'<s:property value="detailName"/>',
		"detailCode":'<s:property value="detailCode"/>',
		"detailKey":'<s:property value="detailKey"/>',
		"accounthead":'<s:property value="accounthead"/>',
		"amount":'<s:property value="amount"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateGridCbill('billDetailsTableSubledger','subledgerCode',index,'<s:property value="subledgerCode"/>');
	updateGridCbill('billDetailsTableSubledger','detailCode',index,'<s:property value="detailCode"/>');
	updateGridCbill('billDetailsTableSubledger','detailName',index,'<s:property value="detailName"/>');
	updateGridCbill('billDetailsTableSubledger','detailKey',index,'<s:property value="detailKey"/>');
	updateGridCbill('billDetailsTableSubledger','accounthead',index,'<s:property value="accounthead"/>');
	updateGridCbill('billDetailsTableSubledger','debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
	//updateAccountTableIndex();	
</s:iterator>
}


var makeCheckListTable = function() {
 var checkListColumns = [ 
{key:"name",label:'Check List Name',width:680, formatter:checkListNameFormatter("checkListsTable",".name","text")},
{key:"val", label:'Check List Value',width:120,formatter:checkListValueFormatter("checkListsTable",".val","text")}			
];
var checkListDS = new YAHOO.util.DataSource(); 
checkListsTable = new YAHOO.widget.DataTable("checkListTable",checkListColumns, checkListDS);
<s:iterator value="checkListsTable" status="stat">

	checkListsTable.addRow({SlNo:checkListsTable.getRecordSet().getLength()+1,
		"name":'<s:property value="name"/>',
		"id":'<s:property value="id"/>',
		"val":'<s:property value="value"/>'
		});
	var index = '<s:property value="#stat.index"/>';
	updateDropdown('checkListsTable','val',index);
	updateGridCbill('checkListsTable','name',index,'<s:property value="name"/>');
	updateGridCbill('checkListsTable','id',index,'<s:property value="id"/>');
	updateGridCbill('checkListsTable','val',index,'<s:property value="val"/>');
	
	//updateAccountTableIndex();	
</s:iterator>
}


function onLoadTask_new(){


if(button!=undefined && button!="")
{

if(document.getElementById("Errors").innerHTML=='')  
{
bootbox.alert(succesMessage);

if(button=="Save_Close")
{
window.close();
}
else if(button=="Save_View")
{
//	var vhId='<s:property value="voucherHeader.id"/>';
//	document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+vhId;
//	document.forms[0].submit();
}
else if(button=="Save_New")

{      	
document.forms[0].button.value='';
document.forms[0].action = "contingentBill!newform.action";
document.forms[0].submit();
}
}
}

}

function loadCheckList(obj)
{

var table_name=eval("checkListsTable");
			var Len=table_name.getRecordSet().getLength();

if(table_name.getRecordSet().getLength()>=1)
{			
	allRecords=table_name.getRecordSet();
	for(var i=allRecords.getLength();i>=0;i--)
	{
	table_name.deleteRow(i);
	}
}	
if(obj.value!=0)
{

var	url = path+"/voucher/common-ajaxLoadCheckList.action?billSubtypeId="+obj.value;
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var entity=req2.responseText;
			var a = entity.split("^");
			var eachEntity = a[0];
			chekListArray=eachEntity.split("+");
			if(chekListArray.length>0)
			{
			//var tab=document.getElementById("checkListTable");
			for(var i=0;i<chekListArray.length-1;i++)
			{
			var s_table_name=eval("checkListsTable");
			var Len=s_table_name.getRecordSet().getLength();
			s_table_name.addRow({SlNo:Len});
		    var v=chekListArray[i].split("`-`");
			document.getElementById("checkListsTable["+(Len)+"].name").value=v[0];
			document.getElementById("checkListsTable["+(Len)+"].id").value=v[1];
			<s:iterator value="commonBean.checkListValuesMap" status="stat">
				document.getElementById("checkListsTable["+(Len)+"].val").options[<s:property value="#stat.index"/>]=new Option("<s:property value='value'/>", "<s:property value='key'/>");
			</s:iterator>
			document.getElementById("checkListsTable["+(Len)+"].val").value='na';
			//bootbox.alert("1");
			}
			}
			
			
		  }
	  }
 	};
	req2.open("GET", url, true);
	//bootbox.alert(url)
	req2.send(null);

}




}
function updateDropdown(tab,field,idx)
{
<s:iterator value="commonBean.checkListValuesMap" status="stat">
document.getElementById(tab+"["+idx+"]."+field).options[<s:property value="#stat.index"/>]=new Option('<s:property value="value"/>','<s:property value="key"/>');
</s:iterator>
}

</script>
</head>
<body>
	<s:form action="contingentBill" theme="css_xhtml" name="cbill"
		validate="true">
		<s:push value="model">
			<div id="loading"
				style="position: absolute; left: 25%; top: 70%; padding: 2px; z-index: 20001; height: auto; width: 500px; display: none;">
				<div class="loading-indicator"
					style="background: white; color: #444; font: bold 13px tohoma, arial, helvetica; padding: 10px; margin: 0; height: auto;">
					<img src="/egi/resources/erp2/images/loading.gif" width="32"
						height="32" style="margin-right: 8px; vertical-align: top;" />
					Loading...
				</div>
			</div>
			<div class="formmainbox">
				<div class="formheading">
					<div class="subheadnew">
						<s:text name="contingent.bill" />
					</div>
				</div>
				<div class="tabber" id="main" align="left">
					<div class="tabbertab" id="maintab">
						<h2>Header</h2>
						<jsp:include page="../budget/budgetHeader.jsp">
							<jsp:param value="Contingent Bill" name="heading" />
						</jsp:include>
						<center>
							<span class="mandatory1">
								<div id="Errors">
									<s:actionerror />
									<s:fielderror />
								</div> <s:actionmessage />
							</span>
						</center>
						<table border="0" width="100%" cellspacing="0" cellpadding="0">
							<tr>
								<td class="bluebox"></td>
								<s:if test="%{!billNumberGenerationAuto}">
									<td class="bluebox"><s:text name="bill.Number" /><span
										class="mandatory1"> *</span></td>
									<td class="bluebox"><s:textfield
											name="commonBean.billNumber" /></td>
								</s:if>
								<td class="bluebox"><s:text name="bill.Date" /><span
									class="mandatory1">*</span></td>
								<s:date name='commonBean.billDate' var="commonBean.billDateId"
									format='dd/MM/yyyy' />
								<td class="bluebox"><s:textfield id="billDate"
										name="commonBean.billDate" value="%{commonBean.billDateId}"
										data-date-end-date="0d"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
										data-inputmask="'mask': 'd/m/y'" /></td>
							</tr>
							<%@include file="contingentBill-form.jsp"%>
						</table>
					</div>
					<div class="tabbertab" id="checkList">
						<h2>Check List</h2>
						<div class="yui-skin-sam" align="center">
							<div id="checkListTable"></div>
						</div>

						<script>
			   				makeCheckListTable();
			   				document.getElementById('checkListTable').getElementsByTagName('table')[0].width="800";
						</script>

					</div>
					<s:hidden name="actionName" id="actionName" />
					<div class="tabbertab" id="approval">
						<h2>Approval Information</h2>
						<font style='color: red;'>
							<div align="center" class="error-block" id="lblError"
								style="font: bold; text-align: center"></div>
							<div align="center" class="error-block" id="Errors"
								style="font: bold; text-align: center"></div>
						</font>
						<%-- <s:if test='%{! "END".equalsIgnoreCase(nextLevel)}'>
	<%@include file="../voucher/workflowApproval-contingent.jsp"%>
</s:if> --%>
						<s:hidden name="billDate" id="billDate" />
						<s:hidden id="cutOffDate" name="cutOffDate" />
						<s:hidden name="nextLevel" id="nextLevel" />
						<%@ include file='../bill/commonWorkflowMatrix.jsp'%>
						<%@ include file='../payment/commonWorkflowMatrix-button.jsp'%>
					</div>
				</div>
			</div>
		</s:push>
		<s:token />
	</s:form>

	<script type="text/javascript">
autocompleteEntitiesBy20();
document.getElementById("budgetReappRow").style.display="none";
document.getElementById("billDetailsTableNet[0].detailTypes").value='<s:property value="%{detailTypeIdandName}"/>';
var net=document.getElementById('billDetailsTableNet[0].glcodeDetail');
//bootbox.alert("hii"+net.value+"text"+net.text);   
net.options[0] =new Option("----Choose----","-1");
var i=1;           
<s:iterator value="netPayList" status="stat">

	<s:if test="%{chartOfAccountDetails.size()>0}">
	net.options[i] =new Option('<s:property value="glcode"/>','<s:property value="glcode"/>~#<s:property value="name"/>~#true');
	
	</s:if> 
	<s:else>
	net.options[i] =new Option('<s:property value="glcode"/>','<s:property value="glcode"/>~#<s:property value="name"/>~#false');
	</s:else>  
i++;
</s:iterator>
	<s:if test="%{billDetailsTableNetFinal==null}">
	document.getElementById('billDetailsTableNet[0].accounthead').value='<s:property value="defaultNetPayCode.name"/>';
	document.getElementById('billDetailsTableNet[0].glcodeIdDetail').value='<s:property value="defaultNetPayCode.id"/>';
	<s:if test="%{defaultNetPayCode.chartOfAccountDetails.size()>0}">
	net.value='<s:property value="defaultNetPayCode.glcode"/>~#<s:property value="defaultNetPayCode.name"/>~#true';	
	document.getElementById('billDetailsTableNet[0].isSubledger').value='true';
	</s:if>
	<s:else>
	net.value='<s:property value="defaultNetPayCode.glcode"/>~#<s:property value="defaultNetPayCode.name"/>~#false';	
	document.getElementById('billDetailsTableNet[0].isSubledger').value='false';
	</s:else>
</s:if>
<s:else>
document.getElementById('billDetailsTableNet[0].accounthead').value='<s:property value="billDetailsTableNetFinal[0].name"/>';
document.getElementById('billDetailsTableNet[0].glcodeIdDetail').value='<s:property value="billDetailsTableNetFinal[0].glcodeIdDetail"/>';
net.value='<s:property value="billDetailsTableNetFinal[0].glcodeDetail"/>~#<s:property value="billDetailsTableNetFinal[0].accounthead"/>~#true';	
document.getElementById('billDetailsTableNet[0].isSubledger').value='<s:property value="billDetailsTableNetFinal[0].isSubledger"/>';
</s:else>
loadDropDownCodesFunction();
if(button!=undefined && button!="")
{

if(document.getElementById("Errors").innerHTML=='')  
{
bootbox.alert(succesMessage);

if(button=="Save_Close")
{
window.close();
}
else if(button=="Save_View")
{
//	var vhId='<s:property value="voucherHeader.id"/>';
//	document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+vhId;
//	document.forms[0].submit();
}
else  if(button=="Save_New")
{  
document.forms[0].button.value='';
document.forms[0].action = "contingentBill!newform.action";
document.forms[0].submit();
}
}
}
if(document.getElementById('commonBean.subledgerType').value!=undefined)
{
//loadDropDownCodesForAccountDetailType(document.getElementById('commonBean.subledgerType'));
load_COA_Entities(document.getElementById('commonBean.subledgerType'));
}else
{
loadDropDownCodesForAccountDetailType(null);
}

</script>
	<script type="text/javascript">
function onSubmit()
{
	if(validate()){
		document.cbill.action='${pageContext.request.contextPath}/bill/contingentBill-create.action';
    	return true;
	}else{
		return false;
	}
}
function validateCutOff()
{
var cutOffDatePart=document.getElementById("cutOffDate").value.split("/");
var billDatePart=document.getElementById("billDate").value.split("/");
var cutOffDate = new Date(cutOffDatePart[1] + "/" + cutOffDatePart[0] + "/"
		+ cutOffDatePart[2]);
var billDate = new Date(billDatePart[1] + "/" + billDatePart[0] + "/"
		+ billDatePart[2]);
if(billDate<=cutOffDate)
{
	return true;
}
else{
	var msg1='<s:text name="wf.cutoffdate.message"/>';
	var msg2='<s:text name="wf.cutoffdate.msg"/>';
	bootbox.alert(msg1+" "+document.getElementById("cutOffDate").value+" "+msg2);
		return false;
	}
}
</script>
</body>
</html>