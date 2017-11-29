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
function populateNarration(accnumObj){
    
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('bankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
}

/*function loadBalance(obj)
{
	if(dom.get('voucherdate').value=='')
	{
		bootbox.alert("Please Select the Voucher Date!!");
		obj.options.value=-1;
		return;
	}
	if(obj.options[obj.selectedIndex].value==-1)
		dom.get('balance').value='';
	else
		populatebalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:dom.get('voucherdate').value+'&date='+new Date()});
}
*/

function populateAvailableBalance(accnumObj){
	if(document.getElementById('voucherDate').value=='')
	{
		bootbox.alert("Please Select the Voucher Date!!");
		accnumObj.options.value=-1;
		return;
	}
	if(accnumObj.options[accnumObj.selectedIndex].value==-1)
		document.getElementById('availableBalance').value='';
	else
		populateavailableBalance({bankaccount:accnumObj.options[accnumObj.selectedIndex].value,voucherDate:document.getElementById('voucherDate').value+'&date='+new Date()});

}

var callback = {
		success: function(o){
			bootbox.alert(o.responseText.value);
			document.getElementById('availableBalance').value=o.responseText;
			},
			failure: function(o) {
		    }
		}                  
		
var postTypeFrom = {
success: function(o) {
		document.getElementById('accnumnar').value= o.responseText;
		},
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function loadBank(fund)
{
	var vTypeOfAccount = document.getElementById('typeOfAccount').value;
populatebankId({fundId:fund.options[fund.selectedIndex].value, typeOfAccount:vTypeOfAccount})	
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

function balanceCheck(obj, name, value)   
		{
			
			if(!validateAppoveUser(name,value))
				return false;
	
			if(obj.id=='wfBtn1') // in case of Reject
				return true;
			if(document.getElementById('balanceAvl') && document.getElementById('balanceAvl').style.display=="block" )
			{
				if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('availableBalance').value))
				{
					bootbox.alert(insuffiecientBankBalance);
					return false;
				}
			}
			return true;
		}


var bankBranchId = 0;
var bankAccountId =0;

jQuery(document).ready(function(){
	bankBranchId = jQuery('#bankBranch').val();
	bankAccountId = jQuery('#bankAccount').val();

	jQuery('#bank').change(function () {
	bankBranchId = "";
	jQuery('#bankBranch').empty();
	jQuery('#bankBranch').append(jQuery('<option>').text('Select from below').attr('value', ''));
	if(jQuery('#bank').val()!="")
		loadBankBranches(jQuery('#bank').val());
	else
		loadBankBranches(0);	
});
	
	jQuery('#bankBranch').change(function () {
		bankBranchId = "";
		jQuery('#bankAccount').empty();
		jQuery('#bankAccount').append(jQuery('<option>').text('Select from below').attr('value', ''));
			loadBankAccounts(jQuery('#bankBranch').val());
	});
});

function loadBankBranches(bankId){
	jQuery.ajax({
			method : "GET",
			url : "/EGF/common/getbankbranchesbybankid",
			data : {
				bankId : bankId
			},
			async : true
		}).done(
				function(response) {
					jQuery('#bankBranch').empty();
					jQuery('#bankBranch').append(jQuery("<option value=''>Select from below</option>"));
					jQuery.each(response, function(index, value) {
						var selected="";
						if(bankBranchId && bankBranchId==value.id)
						{
								selected="selected";
						}
						jQuery('#bankBranch').append(jQuery('<option '+ selected +'>').text(value.branchname).attr('value', value.id));
					});
				});
}

function loadBankAccounts(branchId){
	jQuery.ajax({
			method : "GET",
			url : "/EGF/common/getbankaccountbybranchid",
			data : {
				branchId : branchId
			},
			async : true
		}).done(
				function(response) {
					jQuery('#bankAccount').empty();
					jQuery('#bankAccount').append(jQuery("<option value=''>Select from below</option>"));
					jQuery.each(response, function(index, value) {
						var selected="";
						if(bankAccountId && bankAccountId==value.id)
						{
								selected="selected";
						}
						jQuery('#bankAccount').append(jQuery('<option '+ selected +'>').text(value.accountnumber).attr('value', value.id));
					});
				});
}