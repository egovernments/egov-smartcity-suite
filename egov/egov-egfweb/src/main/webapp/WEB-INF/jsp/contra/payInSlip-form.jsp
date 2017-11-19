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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script>

path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		
		
		
		var makeVoucherDetailTable = function() {
		var voucherDetailColumns = [ 
			
			{key:"serialNo",label:'Sl no',width:90, formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".serialNo")},
			{key:"chequeNo",label:'Cheque No' ,width:100, formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".chequeNo")},
			{key:"chequeDate", label:'chequeDate',width:100,formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".chequeDate")},				
			{key:"voucherNumber",label:'Voucher Number',width:90, formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".voucherNumber")}, 
			{key:"voucherDate",label:'Voucher Date',width:90, formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".voucherDate")},
			{key:"amount",label:'Amount',width:90, formatter:createLongTextFieldFormatterPayin(INSTRUMENTLIST,".amount")},
			{key:"selectChq",label:'Select',width:90, formatter:createcheckbox(INSTRUMENTLIST,".selectChq","calcTotal()")},
			{key:"instrumentId",hidden:true,width:90, formatter:createTextFieldFormatterPJV(INSTRUMENTLIST,".instrumentId","hidden")}
		];
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		
		<s:iterator value="iHeaderList" status="stat">
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
					"serialNo":'<s:property value="serialNo"/>',
					"chequeNo":'<s:property value="instrumentNumber"/>',
					"chequeDate":'<s:property value="instrumentDate"/>',
					"voucherNumber":'<s:property value="voucherNumber"/>',
					"voucherDate":'<s:property value="voucherDate"/>',
					"amount":'<s:property value="instrumentAmount"/>',
					"instrumentId":'<s:property value="instId"/>',
					"selectChq":'<s:property value="selectChq"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridPayInSlip('serialNo',index,'<s:property value="serialNo"/>');
				updateGridPayInSlip('chequeNo',index,'<s:property value="instrumentNumber"/>');
				updateGridPayInSlip('chequeDate',index,'<s:property value="instrumentDate"/>');
				updateGridPayInSlip('voucherNumber',index,'<s:property value="voucherNumber"/>');
				updateGridPayInSlip('voucherDate',index,'<s:property value="voucherDate"/>');
				updateGridPayInSlip('amount',index,'<s:property value="instrumentAmount"/>');
				updateGridPayInSlip('instrumentId',index,'<s:property value="instId"/>');
				updatecheckBox('selectChq',index,'<s:property value="selectChq"/>');
				updateInstrTableIndex();
			</s:iterator>
				
	
		}
</script>


<jsp:include page="../voucher/vouchertrans-filter.jsp" />
<tr>
	<egov:ajaxdropdown id="bankId" fields="['Text','Value']"
		dropdownId="bankId" url="voucher/common!ajaxLoadBanks.action" />
	<td class="greybox"><s:text name="payin.bank" /> <span
		class="bluebox"><span class="mandatory">*</span></span></td>
	<td class="greybox"><s:select name="contraBean.bankBranchId"
			id="bankId" list="dropdownData.bankList" listKey="bankBranchId"
			listValue="bankBranchName" headerKey="-1"
			headerValue="----Choose----" onChange="populateAccNum(this);"
			onClick="validateFundSelected();" /></td>
	<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
		dropdownId="accountNumber"
		url="voucher/common!ajaxLoadAccountNumbers.action" />
	<td class="greybox"><s:text name="payin.accountNum" /> <span
		class="bluebox"><span class="mandatory">*</span></span></td>
	<td class="greybox"><s:select name="contraBean.accountNumberId"
			id="accountNumber" list="dropdownData.accNumList" listKey="id"
			listValue="accountnumber" headerKey="-1" headerValue="----Choose----"
			onChange="populateNarration(this);" /> <s:textfield name="accnumnar"
			id="accnumnar" value="%{accnumnar}" /></td>
</tr>

<tr id="voucherNumId">
	<td class="bluebox"><s:text name="payin.vouchernumfrom" /></td>
	<td class="bluebox"><s:textfield
			name="voucherTypeBean.voucherNumFrom" id="voucherNumFrom" /></td>
	<td class="bluebox"><s:text name="payin.vouchernumto" /></td>
	<td class="bluebox"><s:textfield
			name="voucherTypeBean.voucherNumTo" id="voucherNumTo" /></td>
</tr>
<tr id="voucherDateId">
	<td class="greybox"><s:text name="payin.voucherDateFrom" /></td>
	<td class="greybox"><s:textfield
			name="voucherTypeBean.voucherDateFrom" id="voucherDateFrom"
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
		href="javascript:show_calendar('payinform.voucherDateFrom');"
		style="text-decoration: none">&nbsp;<img tabIndex=-1
			src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
	<td class="greybox"><s:text name="payin.voucherDateTo" /></td>
	<td class="greybox"><s:textfield
			name="voucherTypeBean.voucherDateTo" id="voucherDateTo"
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
		href="javascript:show_calendar('payinform.voucherDateTo');"
		style="text-decoration: none">&nbsp;<img tabIndex=-1
			src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
</tr>
<tr>
	<td class="bluebox"><s:text name="payin.chequeInHand" /></td>
	<td class="bluebox"><s:textfield name="contraBean.chequeInHand"
			id="chequeInHand" readonly="true" /></td>
	<td class="bluebox"><s:text name="narration" /></td>
	<td colspan="10" class="bluebox"><s:textarea maxlength="250"
			rows="4" cols="60" name="narration" />
</tr>
<tr id="reversenumanddate">
	<s:if test="%{shouldShowHeaderField('vouchernumber')}">
		<td class="greybox"><s:text name="reversalVoucherNumber" /><span
			class="mandatory">*</span></td>
		<td class="greybox"><s:textfield name="reversalVoucherNumber"
				id="reversalVoucherNumber" /></td>
	</s:if>
	<td class="greybox"><s:text name="reversalVoucherDate" /><span
		class="mandatory">*</span></td>
	<td class="greybox"><s:textfield name="reversalVoucherDate"
			id="reversalVoucherDate"
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
		href="javascript:show_calendar('payinform.reversalVoucherDate');"
		style="text-decoration: none">&nbsp;<img tabIndex="-1"
			src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
</tr>

</div>
<br />


<script>



function loadBank(fund){
	
	populatebankId({fundId:fund.options[fund.selectedIndex].value})	

	}
function populateAccNum(branch){
    	var fundObj = document.getElementById('fundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populateaccountNumber({fundId: fundObj.options[fundObj.selectedIndex].value,branchId:brId})
}
function populateNarration(accnumObj){

	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('bankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postType, null);

}
var postType = {
success: function(o) {
		var narration= o.responseText;
		document.getElementById('accnumnar').value=narration;	
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function validateFundSelected(){
	if( document.getElementById('fundId').value == -1){
		bootbox.alert("Enter value for Fund");
	}
	
}

function validateCheque(saveMode)
{
	document.getElementById('saveMode').value=saveMode;
	document.getElementById('lblError').innerHTML = "";
	<s:if test="%{isFieldMandatory('vouchernumber')}"> 
	if(null != document.getElementById('payinNumber') && document.getElementById('payinNumber').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter  payinslip number ";
		return false;
	}
	</s:if>
	if(document.getElementById('voucherDate').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter  payinslip date ";
		return false;
	}
	if(document.getElementById('bankId').value == -1){
		document.getElementById('lblError').innerHTML = "Please select a bank ";
		return false;
	}
	if(document.getElementById('accountNumber').value == -1){
		document.getElementById('lblError').innerHTML = "Please select an account number ";
		return false;
	}
	var atleastOnecheque = false;
	var chequeSlNos = "";
	var chkBox;
	var instId;
	var table = document.getElementById('billDetailTable');
	var row = table.getElementsByTagName('tr');
	
	for(var i=0;i<row.length-2;i++)
	{
		
		chkBox =document.getElementById('iHeaderList['+i+'].selectChq');
		instId = document.getElementById('iHeaderList['+i+'].instrumentId');
		if(chkBox.checked)
		{
			
			atleastOnecheque = true;
			if(chequeSlNos !=""){
				chequeSlNos=chequeSlNos+","+instId.value;
			}
			else {
			chequeSlNos = instId.value;
			}
		 }
	}
	if(atleastOnecheque == false){
		 document.getElementById('lblError').innerHTML = "Please Select atleast one cheque " ;
		 return false;
	}
	document.getElementById('selectedInstr').value=chequeSlNos;
	disableHeader(false);
	return true;
}
function disableHeader(value){
	document.getElementById("fundId").disabled=value;
	document.getElementById("vouchermis.departmentid").disabled=value;
	document.getElementById("schemeid").disabled=value;
	document.getElementById("subschemeid").disabled=value;
	document.getElementById("vouchermis.functionary").disabled=value;
	document.getElementById("fundsourceId").disabled=value;
	document.getElementById("vouchermis.divisionid").disabled=value;
	if(null != document.getElementById("search")){
		document.getElementById("search").disabled=value;
	}
	document.getElementById("voucherDate").disabled=false;
}
	</script>
