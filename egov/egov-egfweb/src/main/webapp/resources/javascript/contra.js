/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
function populateAccNum(branch){
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populateaccountNumber({bankId:bankId,branchId:brId})
}


function populateNarration(element){
	var accountNumber =  element.options[element.selectedIndex].value;
	var url = '../voucher/common!loadAccNumNarrationAndFund.action?accnum='+accountNumber;
	YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
}

var postType = {
		success: function(o) {
				var narrationfund= o.responseText;
				var index=narrationfund.indexOf("-");
				document.getElementById('accnumnar').value=narrationfund.substring(o,index);	
				var fundid = narrationfund.substring(index+1,narrationfund.length);	
				document.getElementById('fundId').value = fundid;
				document.getElementById('fundId').disabled =true;
				document.getElementById('accnumnar').disabled =true;
				if(document.getElementById('subschemeid')!=null && document.getElementById('subschemeid')!= 'undefined'){
					populateschemeid({fundId:fundid})
				}
		    },
		    failure: function(o) {
		    	bootbox.alert('Failure');
		    }
	}

var callback = {
		success: function(o){
			document.getElementById('resultGrid').innerHTML=o.responseText;
			document.getElementById('availableBalance').value=document.getElementById('balanceResult').value;
			},
			failure: function(o) {
		    }
		}

function populateAvailableBalance(accnumObj){
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var voucherDate = document.getElementById('voucherDate').value;
	var url = '../contra/contraBTC!ajaxAvailableBalance.action?accountNumberId='+accnum+'&voucherHeader.voucherDate='+voucherDate;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function disableControls(frmIndex, isDisable){
	for(var i=0;i<document.forms[frmIndex].length;i++){
		if(document.forms[frmIndex].elements[i].value != "Close" && document.forms[frmIndex].elements[i].value != "Print"){
			document.forms[frmIndex].elements[i].disabled =isDisable;
		}
	}
}

function nextChqNo(){
	var obj=document.getElementById("accountNumber");
	var bankBr=document.getElementById("bankId");
	if( bankBr.selectedIndex==-1){
		bootbox.alert("Select Bank Branch and Account No!!");
	  return;
	}

	if(obj.selectedIndex==-1){
		bootbox.alert("Select Account No!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.options[obj.selectedIndex].value;
	var sRtn =showModalDialog("../HTML/SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=undefined ) document.getElementById("chequeNumber").value=sRtn;
}
