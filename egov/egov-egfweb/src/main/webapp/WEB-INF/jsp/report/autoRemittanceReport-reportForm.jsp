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
<head>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tdsReportHelper.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
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
	z-index: 20000;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 19999;
}

.yui-skin-sam .yui-ac-input {
	width: 100%;
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

#detailcodescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#detailcodescontainer .yui-ac-content {
	position: absolute;
	width: 350px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 20000;
}

#detailcodescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 19999;
}

#detailcodescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#detailcodescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#detailcodescontainer li.yui-ac-highlight {
	background: #ff0;
}

#detailcodescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
</head>
<script>
   
		
var remitCallBack = {
		success: function(o){    
			document.getElementById('resultGrid').innerHTML=o.responseText;
			undoLoadingMask();  
			},
			failure: function(o) {
				undoLoadingMask();
		    }
		}  	
   

function getData(){    
	isValid = validateData();    
	if(isValid == false)
		return false;
	var level =document.getElementById('level').value;
	var paymentVoucherFromDate =   document.getElementById('paymentVoucherFromDate').value;
	var paymentVoucherToDate =   document.getElementById('paymentVoucherToDate').value;
	var department =  document.getElementById('department').value;
	var recovery =  document.getElementById('recovery').value;
	var drawingOfficer =  document.getElementById('drawingOfficerId').value;
	var contractorCode =  document.getElementById('contractorCode').value;
	var supplierCode =  document.getElementById('supplierCode').value;
	var fund =  document.getElementById('fund').value;
	var rtgsAssignedFromDate =  document.getElementById('rtgsAssignedFromDate').value;
	var rtgsAssignedToDate =  document.getElementById('rtgsAssignedToDate').value;
	var bank =  document.getElementById('bank').value;    
	var bankbranch =  document.getElementById('bankbranch').value;
	var bankaccount =  document.getElementById('bankaccount').value;
	
	var instrumentnumber =  document.getElementById('instrumentnumber').value;
	if(detailKey == 'undefined' && document.getElementById('partyName').value!='')
		detailKey = 0;
	if(detailKey == 'undefined' && document.getElementById('partyName').value=='')
		detailKey = 0;
	
	doLoadingMask();
	var url ='/EGF/report/autoRemittanceReport!ajaxLoadData.action?level='+level+'&department.id='+department+'&paymentVoucherFromDate='+
				paymentVoucherFromDate+'&paymentVoucherToDate='+paymentVoucherToDate+'&recovery.id='+recovery+'&drawingOfficer.id='+drawingOfficer+
				'&contractorCode='+contractorCode+'&supplierCode='+supplierCode+'&fund.id='+fund+'&bank.id='+bank+'&bankbranch.id='+bankbranch+'&bankaccount.id='+bankaccount;
	YAHOO.util.Connect.asyncRequest('POST', url, remitCallBack, null);
	        
	        
}    
   

function exportPdf(){ 
	isValid = validateData();
	if(isValid == false)
		return false;
	var level =document.getElementById('level').value;
	var paymentVoucherFromDate =   document.getElementById('paymentVoucherFromDate').value;
	var paymentVoucherToDate =   document.getElementById('paymentVoucherToDate').value;
	var department =  document.getElementById('department').value;
	var recovery =  document.getElementById('recovery').value;
	var drawingOfficer =  document.getElementById('drawingOfficerId').value;
	var contractorCode =  document.getElementById('contractorCode').value;
	var supplierCode =  document.getElementById('supplierCode').value;
	var fund =  document.getElementById('fund').value;
	var rtgsAssignedFromDate =  document.getElementById('rtgsAssignedFromDate').value;
	var rtgsAssignedToDate =  document.getElementById('rtgsAssignedToDate').value;
	var bank =  document.getElementById('bank').value;    
	var bankbranch =  document.getElementById('bankbranch').value;
	var bankaccount =  document.getElementById('bankaccount').value; 
	var instrumentnumber =  document.getElementById('instrumentnumber').value;
	

	window.open('/EGF/report/autoRemittanceReport!exportPdf.action?level='+level+'&department.id='+department+'&paymentVoucherFromDate='+
			paymentVoucherFromDate+'&paymentVoucherToDate='+paymentVoucherToDate+'&recovery.id='+recovery+'&drawingOfficer.id='+drawingOfficer+
			'&contractorCode='+contractorCode+'&supplierCode='+supplierCode+'&fund.id='+fund+'&bank.id='+bank+'&bankbranch.id='+bankbranch+'&bankaccount.id='+bankaccount,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
function exportXls(){
	isValid = validateData();
	if(isValid == false)
		return false;
	var level =document.getElementById('level').value;
	var paymentVoucherFromDate =   document.getElementById('paymentVoucherFromDate').value;
	var paymentVoucherToDate =   document.getElementById('paymentVoucherToDate').value;
	var department =  document.getElementById('department').value;
	var recovery =  document.getElementById('recovery').value;
	var drawingOfficer =  document.getElementById('drawingOfficerId').value;
	var contractorCode =  document.getElementById('contractorCode').value;
	var supplierCode =  document.getElementById('supplierCode').value;
	var fund =  document.getElementById('fund').value;
	var rtgsAssignedFromDate =  document.getElementById('rtgsAssignedFromDate').value;
	var rtgsAssignedToDate =  document.getElementById('rtgsAssignedToDate').value;
	var bank =  document.getElementById('bank').value;    
	var bankbranch =  document.getElementById('bankbranch').value;
	var bankaccount =  document.getElementById('bankaccount').value; 
	var instrumentnumber =  document.getElementById('instrumentnumber').value;
	window.open('/EGF/report/autoRemittanceReport!exportXls.action?level='+level+'&department.id='+department+'&paymentVoucherFromDate='+
			paymentVoucherFromDate+'&paymentVoucherToDate='+paymentVoucherToDate+'&recovery.id='+recovery+'&drawingOfficer.id='+drawingOfficer+
			'&contractorCode='+contractorCode+'&supplierCode='+supplierCode+'&fund.id='+fund+'&bank.id='+bank+'&bankbranch.id='+bankbranch+'&bankaccount.id='+bankaccount,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
function viewVoucher(vid){
	var url = '../voucher/preApprovedVoucher!loadvoucherview.action?vhid='+ vid;
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function viewBill(billRegisterId){
	var url = '../bill/contingentBill!beforeView.action?billRegisterId='+ billRegisterId;   
	window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
	
</script>
<body onload="loadEntities();">


	<jsp:include page="autoRemittanceReport-new.jsp"></jsp:include>

</body>
</html>
