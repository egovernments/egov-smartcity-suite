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
<SCRIPT type="text/javascript"
	src="/EGF/resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></SCRIPT>
<script type="text/javascript" src="/EGF/exility/PageManager.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/exility/PageValidator.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/exility/data.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/exility/ExilityParameters.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>PJV-Create</title>
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
		
		
		
		
		var makeVoucherDetailTable = function() {
		var voucherDetailColumns = [ 
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionPJV(VOUCHERDETAILLIST,".functionDetail")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterPJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterPJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterPJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmount()")}, 
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterPJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmount()")},
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
		billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
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
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:90, formatter:createSLTextFieldFormatterPJV(SUBLEDGERLIST,".detailCode","validateDetailCodeForJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterPJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterPJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterPJV(SUBLEDGERLIST,".amount")},
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
		subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
	}
	
function getschemelist(obj)
{
var opt=obj.value;
if(opt!="")
{
var obj=document.getElementById('vouchermis.schemeid');

obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','schemelist');
PageManager.DataService.setQueryField("fundId",opt);
PageManager.DataService.callDataService("schemelist");
}
}
function getsubschemelist(obj)
{

var opt=obj.value;
if(opt!=""){
var obj=document.getElementById('vouchermis.subschemeid');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','subschemelist');
PageManager.DataService.setQueryField("schemeId",opt);
PageManager.DataService.callDataService("subschemelist");
}


}

	</script>
<body
	onload="loadDropDownCodes();loadDropDownCodesFunction();refreshInbox()">

	<s:form action="voucher" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="JV-Create" />
		</jsp:include>

		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="formheading"></div>
			<div id="listid" style="display: block">
				<br />
				<div align="center">
					<table border="0" width="80%">
						<jsp:include page="voucher-filter.jsp" />
						<tr>
							<td class="bluebox">Financing Source &nbsp;</td>
							<td class="bluebox"><s:select name="financingSourceId"
									id="financingSourceId" list="dropdownData.subschemeList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="----Choose----" /></td>
							<td width="23%" class="bluebox">Narration &nbsp;</td>
							<td colspan="3" class="bluebox"><s:textarea name="narration" /></td>
						</tr>
					</table>
				</div>
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
			
			makeSubLedgerTable();
		
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>

				<div align="center">

					<table border="0" width="80%">
						<tr>
							<td class="bluebox">Comments</td>
							<td class="bluebox"><s:textarea name="comments"
									id="comments" cols="50" rows="3"
									value="%{voucherHeader.state.text1}" /></td>
						</tr>
						<tr>
							<td />
							<td><s:submit type="submit" cssClass="buttonsubmit"
									value="Save & Close" id="save&close" name="save&close"
									method="create" onclick="return validate()" /> <s:submit
									cssClass="button" id="save&new" name="save&new"
									value="Save & New" method="create" onclick="return validate()" />
								<input type="submit" value="Close"
								onclick="javascript:window.close()" class="button" /></td>
						</tr>
					</table>

				</div>
			</div>
		</div>
		<div id="codescontainer"></div>
	</s:form>

</body>

</html>
