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

function selectAllCheckbox(e){
    var table= $(e.target).closest('table');
    $('td input:checkbox',table).prop('checked',e.target.checked);  
}

//This function is used to show the report which is digitally signed.
function downloadSignedNotice(signedFileStoreId) {
	var params = [
		'height='+screen.height, 
	    'width='+screen.width,
	    'fullscreen=yes' 
	].join(',');
	window.open('/wtms/digitalSignature/waterTax/downloadSignedWorkOrderConnection?signedFileStoreId='+signedFileStoreId, "NoticeWindow", params);
}
//Generate notice for the pending water connection document
function generateNotice(obj, actionName, currentState){
	var rowobj=getRow(obj);
	var tbl = document.getElementById('digSignDetailsTab');
	var applicationNumber=getControlInBranch(tbl.rows[rowobj.rowIndex],'objectId').value;
	var noticeType = 'Special Notice';
	var params = [
		   			'height='+screen.height, 
		   		    'width='+screen.width,
		   		    'fullscreen=yes' 
		   		].join(',');
	var noticeType='Special Notice';  
	var type = currentState.split(":");
	var url = "";
	if (actionName == 'Preview') {
		if(currentState == 'CLOSECONNECTION') {
			url = "/wtms/application/acknowlgementNotice?pathVar="+applicationNumber+"&workFlowAction="+actionName+"&isDigSignPending=true";
		} else if(currentState == 'RECONNECTION') {
			url = "/wtms/application/ReconnacknowlgementNotice?pathVar="+applicationNumber+"&workFlowAction="+actionName+"&isDigSignPending=true";
		} else {
			url = "/wtms/application/workorder?pathVar="+applicationNumber+"&workFlowAction="+actionName+"&isDigSignPending=true";
		}
		window.open(url, "NoticeWindow", params);
		return false; 
	} 
	else {
		$('<form>.').attr({
			method: 'post',
			action: '/wtms/digitalSignature/waterTax/signWorkOrder?pathVar='+applicationNumber+'&currentState='+currentState,
			target: '_self'
		})
		.appendTo(document.body).submit();
		return false; 
	}
}

function signAllPendingDigitalSignature(actionName) {
	if (jQuery('#digSignDetailsTab').find('input[type=checkbox]:checked').length == 0) {
		bootbox.alert('Please select atleast one document to sign');
		return false;
	} else {
		var tbl = document.getElementById("digSignDetailsTab");
		var lastRow = (tbl.rows.length) - 1;
		var idArray = new Array();
		var applicationNoStatePair = new Array();
		var j = 0, k = 0;
		for (var i = 1; i <= lastRow; i++) {
			if (getControlInBranch(tbl.rows[i], 'rowCheckBox').checked) {
				idArray[j++] = getControlInBranch(tbl.rows[i],'objectId').value;
				applicationNoStatePair[k++] = getControlInBranch(tbl.rows[i],'objectId').value + ':' + getControlInBranch(tbl.rows[i],'applicationState').value;
			}
		}
		$('<form>.').attr({
			method: 'post',
			action: '/wtms/digitalSignature/waterTax/signWorkOrder?pathVar='+idArray.toString()+'&signAll='+actionName+'&applicationNoStatePair='+applicationNoStatePair.toString(),
			target: '_self'
		})
		.appendTo(document.body).submit();
	}
}