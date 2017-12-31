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
var VOUCHERDETAILLIST='billDetailslist';
var SUBLEDGERLIST='subLedgerlist';
var VOUCHERDETAILTABLE='billDetailsTable';
var billDetailsTable;
var subLedgersTable;
var billDetailTableIndex = 0;
var slDetailTableIndex = 0;
var path;
var oAutoCompEntity;
var detailTypeId=0;
var codeTextMessage='Enter 3 letters';

function resetTables(fYearId){

            var length = billDetailsTable.getRecordSet().getLength(); 
             if(length > 1) { 
	                var count = (length > 2) ? length : 2; 
	               billDetailsTable.deleteRows(1,count); 
	            } 
	     
	     for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billDetailslist['+i+'].creditAmountDetail')){
			document.getElementById('billDetailslist['+i+'].creditAmountDetail').value=0;
		//	document.getElementById('billDetailslist['+i+'].debitAmountDetail').value=0;
			var fYearObj=document.getElementById('billDetailslist['+i+'].financialYearId');
			for(var k=0;k<fYearObj.options.length;k++){
					if(fYearObj.options[k].value==fYearId){
						fYearObj.options[k].selected=true;
					}
			}
			
		}
	}
	      var slength = subLedgersTable.getRecordSet().getLength(); 
                if(slength > 1) { 
	                var scount = (slength > 2) ? slength : 2; 
	                subLedgersTable.deleteRows(1,scount); 
	            } 
	            
	          for (var j=0; j<slDetailTableIndex+1; j++ )
				{
					if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
						document.getElementById('subLedgerlist['+j+'].amount').value=0;
						document.getElementById('subLedgerlist['+j+'].detailCode').placeholder=codeTextMessage;
						document.getElementById('subLedgerlist['+j+'].detailCode').style.color='DarkGray';
						document.getElementById('subLedgerlist['+j+'].glcode.id').value=0;
						document.getElementById('subLedgerlist['+j+'].detailType.id').value=0;
						var glcodedropdown=document.getElementById('subLedgerlist['+j+'].glcode.id');
						var detailTypedropdown=document.getElementById('subLedgerlist['+j+'].detailType.id');
						for(k=glcodedropdown.options.length-1;k>=1;k--)
						{
							glcodedropdown.remove(k);
						}
						for(l=detailTypedropdown.options.length-1;l>=1;l--)
						{
							detailTypedropdown.remove(l);
						}
					}
				}

}



function validateAccountDetail(){
	var i, j, debit=0, credit=0,creditTotal=0,debitTotal=0;
	var validate=true;
	for (var i=0; i<billDetailTableIndex+1; i++)
	{
		for (var j=0; j<billDetailTableIndex+1; j++ )
		{	
			if (i!=j)
			{
				if(document.getElementById('billDetailslist['+i+'].glcodeDetail')!=null && document.getElementById('billDetailslist['+j+'].glcodeDetail')!=null  ){
					if ((document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length!=0 && document.getElementById('billDetailslist['+j+'].glcodeDetail').value.length!=0)&&(document.getElementById('billDetailslist['+i+'].glcodeDetail').value == document.getElementById('billDetailslist['+j+'].glcodeDetail').value ))
					{	document.getElementById('challan_error_area').innerHTML+='Duplicate record in Account Details. Please check account code: ' + document.getElementById('billDetailslist['+j+'].glcodeDetail').value+'<br>';
						i=billDetailsTable.getRecordSet().getLength();
						validate=false;
					}
				}
			}
			
		}
		if(document.getElementById('billDetailslist['+i+'].glcodeDetail')!=null){
			if (  document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length > 0 && document.getElementById('billDetailslist['+i+'].accounthead').value.length==0)
			{
				document.getElementById('billDetailslist['+i+'].glcodeDetail').focus();
				document.getElementById('challan_error_area').innerHTML+='Please enter correct data in Account Details for the account code:'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value +'<br>';
				validate=false;
			}
			if(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value.startsWith('+')){
				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
				validate=false;
			}
			if(isNaN(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value)){
				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
				validate=false;
			}
			else{
			credit = eval(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value);
			}
			credit = isNaN(credit)?0:credit;
			if(credit<0){
				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
				validate=false;
			}
			
			creditTotal = creditTotal + credit;
			if (credit>0 &&  document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length == 0)
			{
				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('challan_error_area').innerHTML+='Account code is missing in Account Details for credit supplied field'+'<br>';
				validate=false;
			}
//			if(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value.startsWith('+')){
//				document.getElementById('billDetailslist['+i+'].debitAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
//				validate=false;
//			}
//			if(isNaN(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value)){
//				document.getElementById('billDetailslist['+i+'].debitAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
//				validate=false;
//			}
//			else{
//				debit = eval(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value);
//				}
//			debit = isNaN(debit)?0:debit;
//			if(debit<0){
//				document.getElementById('billDetailslist['+i+'].debitAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Account Details for account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
//				validate=false;
//			
//			}
//			
//			debitTotal = debitTotal + debit;
		
//			if (debit>0 &&  document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length == 0)
//			if (debit>0 &&  document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length == 0)
//			{
//				document.getElementById('billDetailslist['+i+'].debitAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Account code is missing in Account Details for debit supplied field'+'<br>';
//				validate=false;
//			}
//			
//			if ((credit > 0 && debit>0) )
//			{
//				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Please enter either credit/debit amount for the account code : ' +document.getElementById('billDetailslist['+i+'].glcodeDetail').value +'<br>';
//				validate=false;
//				return false;
//			}
//			
//			if ((credit == 0 && debit==0) && document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length!= 0)
//			{
//				document.getElementById('billDetailslist['+i+'].creditAmountDetail').focus();
//				document.getElementById('challan_error_area').innerHTML+='Please enter either credit/debit amount for the account code : ' +document.getElementById('billDetailslist['+i+'].glcodeDetail').value +'<br>';
//				validate=false;
//				return false;
//			}

			if( document.getElementById('billDetailslist['+i+'].financialYearId').value=="0" && document.getElementById('billDetailslist['+i+'].glcodeDetail').value.length!= 0){
				document.getElementById('billDetailslist['+i+'].financialYearId').focus();
				document.getElementById('challan_error_area').innerHTML+='Please select the year for the account code :'+document.getElementById('billDetailslist['+i+'].glcodeDetail').value +'<br>';
				validate=false;
			}
			
		}
	}
	if(creditTotal<=0)
	{
			document.getElementById('challan_error_area').innerHTML+="Please enter credit account details"+"<br>";
			validate=false;
	}
//	if(creditTotal>0 &&  debitTotal>0 && debitTotal>creditTotal){
//		document.getElementById('challan_error_area').innerHTML+="Total credit amount should be greater than total debit amount."+"<br>";
//		validate=false;
//	}
	return validate;
}

function validateSubLedgerDetail(){

	var subledgerselected = new Array();

		for (var i=0; i<billDetailTableIndex+1; i++ )
		{
			var accountDetailamount=0;
			var accountDetailRebateAmount=0;
			var subledgerTotalAmt=0;
			if(document.getElementById('billDetailslist['+i+'].glcodeDetail')!=null){
				for (var j=0; j<slDetailTableIndex+1; j++ )
				{
					if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
				
						var accountCode = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
						var subledgerid = document.getElementById('subLedgerlist['+j+'].glcode.id');
						var detailtypeid = document.getElementById('subLedgerlist['+j+'].detailType.id');
						var detailKeyid = document.getElementById('subLedgerlist['+j+'].detailKeyId').value
						var subledgerAccCode =0;
						if(subledgerid!='null')
							subledgerAccCode= subledgerid.options[subledgerid.selectedIndex].value;
						
						if( ( subledgerAccCode !=0) && (detailtypeid.value == "" || detailtypeid.value == 0 || detailKeyid ==""))
						{
								document.getElementById('challan_error_area').innerHTML += "Please enter subledger details correctly<br>";
								return false;
						}
						if(document.getElementById('subLedgerlist['+j+'].amount').value.startsWith('+')){
							document.getElementById('subLedgerlist['+j+'].amount').focus();
							document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Subledger Details '+'<br>';
							return false;
						}
						if(isNaN(document.getElementById('subLedgerlist['+j+'].amount').value)){
							document.getElementById('subLedgerlist['+j+'].amount').focus();
							document.getElementById('challan_error_area').innerHTML+='Please enter correct amount in Subledger Details '+'<br>';
							return false;
						}
						if (accountCode == subledgerid.options[subledgerid.selectedIndex].text.trim())
						{
							if(eval(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value) > 0){
			
								accountDetailamount = document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
							}
							//if(eval(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value) > 0){
								
								//accountDetailRebateAmount = document.getElementById('billDetailslist['+i+'].debitAmountDetail').value;
							//}
							subledgerTotalAmt = subledgerTotalAmt + eval(document.getElementById('subLedgerlist['+j+'].amount').value);
						}
						if(subledgerselected == 0){
							subledgerselected[0] = subledgerAccCode;
						}
						else {
							var found =0;
							for (var k=0; k<subledgerselected.length;k++ )
							{
								if(subledgerselected[k] == subledgerAccCode) found=1
							}
							if(found == 0)subledgerselected[subledgerselected.length+1] = subledgerAccCode;
						}
						//document.getElementById('subLedgerlist['+j+'].subledgerCode').value= subledgerid.options[subledgerid.selectedIndex].text;
						//document.getElementById('subLedgerlist['+j+']'+'.detailTypeName').value =  detailtypeid.options[detailtypeid.selectedIndex].text;
					
					}
				}
				if(accountDetailamount>0){
					if(Math.round(accountDetailamount*100)/100  != Math.round(subledgerTotalAmt*100)/100)
					{
						document.getElementById('challan_error_area').innerHTML += "Total subledger amount is not matching for account code :"+ document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
						return false;
					}
				}
				/*if(accountDetailRebateAmount>0){
					if(Math.round(accountDetailRebateAmount*100)/100  != Math.round(subledgerTotalAmt*100)/100)
					{
						document.getElementById('challan_error_area').innerHTML += "Total subledger amount is not matching for account code :"+ document.getElementById('billDetailslist['+i+'].glcodeDetail').value+'<br>';
						return false;
					}
				}*/
			}
		}
		
		return true;
	}
function getAccountDetails(obj){
	var serviceId=obj.value;
	
		var url = path+'/receipts/ajaxReceiptCreate-getAccountForService.action?serviceId='+serviceId;
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeNew, null);
	
}

var postTypeNew = {
success: function(o) {
		var accounts= o.responseText;
		var accountDetails = accounts.split('#');
		var eachItem;
		var row;
		
			if(billDetailsTable.getRecordSet().getLength()>1){
				for(var l=billDetailsTable.getRecordSet().getLength();l>1;l--){
					billDetailsTable.deleteRows(0,l); 
				}
			}
			else if(billDetailsTable.getRecordSet().getLength()==1){
				billDetailsTable.deleteRow(0);
			}
			billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
			updateAccountTableIndex();
			
			if(subLedgersTable.getRecordSet().getLength()>1){
				for(var l=subLedgersTable.getRecordSet().getLength();l>1;l--){
					subLedgersTable.deleteRows(0,l); 
				}
			}
			else if(subLedgersTable.getRecordSet().getLength()==1){
				subLedgersTable.deleteRow(0);
			}
			subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
			updateSLTableIndex();
			document.getElementById('totalcramount').value = 0;
			// document.getElementById('totaldbamount').value = 0;
		for(var i=0;i<accountDetails.length;i++)
		{	
		
			if(accountDetails[i].trim().length>0){
				eachItem =accountDetails[i].split('~');
				var firstRowNo=billDetailTableIndex-1;
				var firstRow=document.getElementById('billDetailslist['+firstRowNo+']'+'.glcodeIdDetail');
				if(firstRow!=null && firstRow.value!=""){
					billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
					updateAccountTableIndex();
				}
				row=billDetailTableIndex-1;
				document.getElementById('billDetailslist['+row+']'+'.glcodeIdDetail').value=eachItem[0].trim();
				document.getElementById('billDetailslist['+row+']'+'.glcodeDetail').value=eachItem[1];
				document.getElementById('billDetailslist['+row+']'+'.accounthead').value=eachItem[2];
				check();
			}
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}



function getServiceMISDetails(obj){
	var serviceId=obj.value;
	var newurl = path+'/receipts/ajaxReceiptCreate-getMISdetailsForService.action?serviceId='+serviceId;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', newurl, getMISdetails, null);
		
}

var getMISdetails = {
success: function(o) {
		var misdetails= o.responseText;
		var details = misdetails.split('#');
		var eachItem;
		document.getElementById('receiptMisc.fund.id').value="-1";
		//document.getElementById('deptId').value="-1";
			
		for(var i=0;i<details.length;i++)
		{	
		
			if(details[i].trim().length>0){
				eachItem =details[i].split('~');
				document.getElementById('receiptMisc.fund.id').value=eachItem[0].trim();
				//document.getElementById('deptId').value=eachItem[1];
			}
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function updateGrid(prefix,field,index,value){
	document.getElementById(prefix+'['+index+'].'+field).value=value;
}

function updateSLGrid(field,index,value){
	if(field=='detailCode' && value==''){
		document.getElementById('subLedgerlist['+index+'].'+field).placeholder=codeTextMessage;
		document.getElementById('subLedgerlist['+index+'].'+field).style.color='DarkGray';
	}
	else{
		document.getElementById('subLedgerlist['+index+'].'+field).value=value;
	}
}

function updateGridSLDropdown(field,index,value,text){
	var element = document.getElementById('subLedgerlist['+index+'].'+field)
	if(value != "" ){
	element.options.length=2;
	element.options[1].text=text;
	element.options[1].value=value;
	element.options[1].selected = true;
	}
}

function updateGridDropdown(field,index,value,text){
	var element = document.getElementById('billDetailslist['+index+'].'+field)
	if(value != "" ){
	element.options.length=2;
	element.options[1].text=text;
	element.options[1].value=value;
	element.options[1].selected = true;
	}
}

function createTextFieldFormatter(prefix,suffix,type,table){

    return function(el, oRecord, oColumn, oData) {
    var rec=billDetailTableIndex;
    var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' readOnly style='width:80;' tabIndex='-1' />";
	
	}
}

function createTextFieldFormatterForFunction(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
    	  var rec=billDetailTableIndex;
    		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:90px;' onfocus='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunction(this)' />";
	}
}
function createLongTextFieldFormatter(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
     var rec=billDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"'  style='width:350px;' onfocus='autocompletecode(this,event)' autocomplete='off'  onblur='fillNeibrAfterSplitGlcode(this)'/>";
	}
}

function createAmountFieldFormatter(prefix,suffix,onblurfunction,table){
    return function(el, oRecord, oColumn, oData) {
    var rec=billDetailTableIndex;
	var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='text-align:right;width:80px;' maxlength='10' class='form-control patternvalidation text-right' data-pattern='number' onblur='"+onblurfunction+";updatetotalAmount()'/>";
	}
}

function createSLTextFieldFormatter(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:100px;' onblur='"+onblurfunction+"'/>";
	}
}

function createSLHiddenFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"'/>";
	}
}

function createSLLongTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' readOnly style='width:200px;' tabindex='-1'/>";
	}
}
function createSLAmountFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' maxlength='10' style='text-align:right;width:90px;' class='form-control patternvalidation text-right' data-pattern='number'/>";
	}
}

function createSLDetailCodeTextFieldFormatter(prefix,suffix,onblurfunction){
 return function(el, oRecord, oColumn, oData) {
			el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompleteForEntity(this,event)' autocomplete='off' onblur = 'waterMarkTextOut(\""+prefix+"["+slDetailTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");"+onblurfunction+"' onfocus='onFocusDetailCode(this);waterMarkTextIn(\""+prefix+"["+slDetailTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");' />";
			
		}
}

var lang=YAHOO.lang;

function createDropdownFormatterFYear(prefix,currentFYearId){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+billDetailTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+billDetailTableIndex+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            selectEl.disabled=true;
            YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
			
        }
       
        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == currentFYearId) {
                    	optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}

function createDropdownFormatterDetailCode(prefix){
	 return function(el, oRecord, oColumn, oData) {
	        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
	            options = (lang.isArray(oColumn.dropdownOptions)) ?
	                oColumn.dropdownOptions : null,
	            selectEl,
	            collection = el.getElementsByTagName("select");
	        if(collection.length === 0) {
	            selectEl = document.createElement("select");
	            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
	            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
				selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
	            selectEl = el.appendChild(selectEl);
	            var selectedIndex = {value: slDetailTableIndex }; 
	            YAHOO.util.Event.addListener(selectEl,"change",onDropdownDetailCodeChange,selectedIndex,this);
				
	        }

	        selectEl = collection[0];

	        if(selectEl) {
	            selectEl.innerHTML = "";
	            if(options) {
	                for(var i=0; i<options.length; i++) {
	                    var option = options[i];
	                    var optionEl = document.createElement("option");
	                    optionEl.value = (lang.isValue(option.value)) ?
	                            option.value : option;
	                    optionEl.innerHTML = (lang.isValue(option.text)) ?
	                            option.text : (lang.isValue(option.label)) ? option.label : option;
	                    optionEl = selectEl.appendChild(optionEl);
	                    if (optionEl.value == selectedValue) {
	                        optionEl.selected = true;
	                    }
	                }
	            }
	            else {
	                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
	            }
	        }
	        else {
	            el.innerHTML = lang.isValue(oData) ? oData : "";
	        }
	    }
}
	
	var onDropdownDetailCodeChange = function(index,obj) { 
		var detailtypeid = document.getElementById('subLedgerlist['+obj.value+'].detailType.id').value;
			var selecteddetailcode1=document.getElementById('subLedgerlist['+obj.value+'].detailCode').value;
			var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+selecteddetailcode1+'&detailtypeid='+detailtypeid+'&index='+obj.value;
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeDetailCode, null);
	};
	var postTypeDetailCode = {
	success: function(o) {
		var res= o.responseText;
		res = res.split('~');
		if(res.length>2)
		{
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value=res[1];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value=res[2];
			var glcodenew=document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.glcode.id');
			populatesubledgeramount(glcodenew.options[glcodenew.selectedIndex].text,parseInt(res[0]));
		}
		else
		{
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value='';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value='';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.amount').value='0';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.amount').readOnly=false;
			return;
		}
	    },
	    failure: function(o) {
	    	bootbox.alert('failure');
	    }
	}	

function createDropdownFormatterDetail(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            var selectedIndex = {value: slDetailTableIndex }; 
            YAHOO.util.Event.addListener(selectEl,"change",onDropdownDetailChange,selectedIndex,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}


var selecteddetailcode;
var onDropdownDetailChange = function(index,obj) {
	document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailCode').placeholder=codeTextMessage;
	document.getElementById('subLedgerlist['+obj.value+'].detailCode').style.color='DarkGray';
	document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailKeyId').value='';
	document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailKey').value='';
	document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.amount').value='0';
	document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.amount').readOnly=false;
	var detailtypeidObj=document.getElementById('subLedgerlist['+obj.value+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){ // checks if the subledgercodes already loaded for that detail type
		detailTypeId = detailtypeidObj.value;
	}

	if(entities)
					{
						entities=null;
						if(oAutoCompEntity){
							YAHOO.util.Event.purgeElement(obj, true);
							oAutoCompEntity._elContainer.innerHTML="";
							yuiflag1[obj.value] =undefined;
						}
					}
				
};

function onFocusDetailCode(obj){
	var currRow=getRowIndex(obj);
	var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		//loadDropDownCodesForEntities(detailtypeidObj); 
	}
}

function createDropdownFormatterCode(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			//selectEl.onfocus=check;
            selectEl = el.appendChild(selectEl);
			var selectedIndex = {value: slDetailTableIndex }; 
            YAHOO.util.Event.addListener(selectEl,"change",onDropdownChange,selectedIndex,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}
var selecteddetailtype;
var onDropdownChange = function(index,obj) { 
		var subledgerid=document.getElementById('subLedgerlist['+obj.value+'].glcode.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		if(subledgerid.options[subledgerid.selectedIndex].value==0){
			accountCode=0;
		}
		selecteddetailtype=document.getElementById('subLedgerlist['+obj.value+'].detailType.id').value;
		document.getElementById('subLedgerlist['+obj.value+'].amount').value="";
		//document.getElementById('subLedgerlist['+obj.value+'].subledgerCode').value= subledgerid.options[subledgerid.selectedIndex].text;
		var url = path+'/receipts/ajaxReceiptCreate-getDetailType.action?accountCode='+accountCode+'&index='+obj.value+'&selectedDetailType='+selecteddetailtype+'&onload=false';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
};
var postType = {
success: function(o) {
		var detailType= o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj;
		var rowid;
		var selectedval;
		var onload ='false';
		for(var i=0;i<detailRecord.length;i++)
		{
			eachItem =detailRecord[i].split('~');
			if(eachItem!=""){
				if(obj==null)
				{
					obj = document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailType.id');
					rowid=parseInt(eachItem[0]);
					selectedval=eachItem[1];
					for(k=obj.options.length-1;k>=1;k--)
					{
						obj.remove(k);
					}
				if(entities)
					{
						entities=null;
						if(oAutoCompEntity){
							YAHOO.util.Event.purgeElement(obj, true);
							oAutoCompEntity._elContainer.innerHTML="";
							yuiflag1[parseInt(eachItem[0])] =undefined;
						}
					}
				}
				if(eachItem[1]!="error"){
					try{
						obj.add(new Option(eachItem[3],eachItem[4]), null);
					}catch(ex){
						obj.add(new Option(eachItem[3],eachItem[4]));
					}
				onload=eachItem[2];
				}
			}
		}
		for(var k=0;k<obj.options.length;k++){
			if(obj.options[k].value==selectedval){
				obj.options[k].selected=true;
			}
		}	

	if(onload=='false')
	{
		document.getElementById('subLedgerlist['+rowid+'].detailCode').placeholder=codeTextMessage;
		document.getElementById('subLedgerlist['+rowid+'].detailCode').style.color='DarkGray';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailKeyId').value='';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailKey').value='';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.amount').value='0';
	}
		
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function updateAccountTableIndex(){
	
	billDetailTableIndex = billDetailTableIndex +1 ;
    patternvalidation();
}

function updateSLTableIndex(){
	
	 slDetailTableIndex = slDetailTableIndex +1 ;
}

function updateCreditAmount()
{
	var amt=0;
	for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billDetailslist['+i+'].creditAmountDetail')){
			var val = document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
			if(val=='') val=0;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value);
			}
		}
	}
	document.getElementById('totalcramount').value = amt;
	populatesubledgeramount1();
}

function updateDebitAmount()
{
	var amt=0;
	for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billDetailslist['+i+'].debitAmountDetail')){
			var val = document.getElementById('billDetailslist['+i+'].debitAmountDetail').value;
			if(val=='') val=0;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value);
			}
		}
	}
	// document.getElementById('totaldbamount').value = amt;
}

function updatetotalAmount(){
	//document.getElementById('misctotalAmount').value=parseFloat(document.getElementById('totalcramount').value)-parseFloat(document.getElementById('totaldbamount').value);
	//document.getElementById('totalamountdisplay').value=document.getElementById('misctotalAmount').value;
	return ;
	}


var funcObj;
var funcArray;
function loadDropDownCodesFunction()
{
	var url = "/EGF/voucher/common-ajaxGetAllFunctionName.action";
	var req4 = initiateRequest();
	req4.onreadystatechange = function()
	{
	  if (req4.readyState == 4)
	  {
		  if (req4.status == 200)
		  {
			var codes2=req4.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			funcArray=codes.split("+");
			funcObj= new YAHOO.widget.DS_JSArray(funcArray);
		  }
	   }
	};
	req4.open("GET", url, true);
	req4.send(null);
}


function autocompletecodeFunction(obj,myEvent)
{
	
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]+"px";
	target.style.width=650;	
		
	var fObj=obj;
	//var  currRow=getRowIndex(obj);

	//40 --> Down arrow, 38 --> Up arrow
	//if(yuiflagFunc[currRow] == undefined)
	//{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(fObj,'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				funcObj.applyLocalFilter = true;
				funcObj.queryMatchContains = true;
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~~`")[0];
				};
			}
		}
		//yuiflagFunc[currRow] = 1;
	//}	
}


function fillNeibrAfterSplitFunction(obj)
{
	var currRow=getRowIndex(obj);
	var temp = obj.value;
	temp = temp.split("`~~`");
	if(temp.length>1)
	{ 
		var temp1=temp[0];
		temp1=temp1.split("`~`");
		obj.value=temp1[0];
		document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value=temp[1];
	} else if(temp == '') 
	{
		obj.value='';
		document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value='';
	}
}

function fillNeibrAfterSplitGlcode(obj)
{

	var temp = obj.value;
	temp = temp.split("`-`");
	var currRow=getRowIndex(obj);
	var glcodeId = document.getElementById('billDetailslist['+currRow+'].glcodeIdDetail').value;
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		document.getElementById('billDetailslist['+currRow+'].glcodeIdDetail').value=temp[2];
		document.getElementById('billDetailslist['+currRow+'].glcodeDetail').value=temp[1];
		check();
	}
	else if(glcodeId==null || glcodeId=="")
	{
		document.getElementById('billDetailslist['+currRow+'].glcodeIdDetail').value="";
		document.getElementById('billDetailslist['+currRow+'].glcodeDetail').value="";
		document.getElementById('billDetailslist['+currRow+'].accounthead').value="";
	}
	
}


function getDetailType(val){
			selecteddetailtype=document.getElementById('subLedgerlist['+val+'].detailType.id').value;
			var detail = document.getElementById('subLedgerlist['+val+'].detailType.id');
			
			for(k=detail.options.length-1;k>=1;k--)
			{
				detail.remove(k);
			}
			
			var subledgerid=document.getElementById('subLedgerlist['+val+'].glcode.id');
			var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
			if( subledgerid.options[subledgerid.selectedIndex].value!=0){
				var url = path+'/receipts/ajaxReceiptCreate-getDetailType.action?accountCode='+accountCode+'&index='+val+'&selectedDetailType='+selecteddetailtype+'&onload=true';
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
			}
			else{
				var detailCodeObj = document.getElementById('subLedgerlist['+val+'].detailCode');
				if(detailCodeObj!=null){
					detailCodeObj.placeholder=codeTextMessage;
					detailCodeObj.style.color='DarkGray';
					document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKeyId').value='';
					document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKey').value='';
					document.getElementById(SUBLEDGERLIST+'['+val+']'+'.amount').value='';
				}
			}
	
}
function getDetailCode(val){
	var selecteddetailcode1=document.getElementById('subLedgerlist['+val+'].detailCode').value;
	var detailCode=document.getElementById('subLedgerlist['+val+'].detailCode');
	detailCode.value='';
	document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKeyId').value='';
	document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKey').value='';
	var subledgerid=document.getElementById('subLedgerlist['+val+'].glcode.id');
	var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
	if( subledgerid.options[subledgerid.selectedIndex].value!=0 & selecteddetailcode1!=0){
		var detailtypeid = document.getElementById('subLedgerlist['+val+'].detailType.id').value;
		var url = path+'/receipts/ajaxReceiptCreate-getCodeNew.action?accountCode='+accountCode+'&index='+val+'&detailTypeId='+detailtypeid+'&selectedDetailCode='+selecteddetailcode1;
		//var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeDetail, null);
	}
	
}

function getDetailCodeValue(val){
	var detailtypeid = document.getElementById('subLedgerlist['+val+'].detailType.id').value;
	var selecteddetailcode1=document.getElementById('subLedgerlist['+val+'].detailCode').value;
	if(selecteddetailcode1!=0){
		var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+selecteddetailcode1+'&detailtypeid='+detailtypeid+'&index='+val+'&codeorname=code';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeDetailCode, null);
	}

}
function check(){
	
	var accountCodes=new Array();
	var count=0;
	for(var i=0;i<billDetailTableIndex+1;i++){
		if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
			accountCodes[count] = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
			count++;
		}
	}
	
		var url =  path+'/receipts/ajaxReceiptCreate-getDetailCode.action?accountCodes='+accountCodes;
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);

	
}
var glcodeObj;
var glcodeArray;
var callbackJV = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		dom.get("challan_error_area").style.display="none";
		var secondSubledgerAccount=false;
		for (var j=0; j<slDetailTableIndex;j++ )
		{
			if(null != document.getElementById('subLedgerlist['+j+'].glcode.id') && null != document.getElementById('subLedgerlist['+j+'].subledgerCode') && test.length >1 )
			{
				var selectedglcode=document.getElementById('subLedgerlist['+j+'].glcode.id').value;
				d=document.getElementById('subLedgerlist['+j+'].glcode.id');
				for(var k=d.options.length-1;k>=1;k--)
				{
					d.remove(k);
				}
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					if(secondSubledgerAccount){
						for(var j=0;j<billDetailTableIndex+1;j++){
							if(null != document.getElementById('billDetailslist['+j+'].glcodeDetail')){
								if(test[i*2-2].trim() == document.getElementById('billDetailslist['+j+'].glcodeDetail').value){
									 document.getElementById('challan_error_area').innerHTML = "Two accounts with subledger details are not allowed.Please enter different account code<br>";
									 dom.get("challan_error_area").style.display="block";
									 document.getElementById('billDetailslist['+j+'].glcodeDetail').value='';
									 document.getElementById('billDetailslist['+j+'].glcodeIdDetail').value="";
									 document.getElementById('billDetailslist['+j+'].accounthead').value="";
									 		
								}
								
							}
						}
						break;
					}
					try{
						d.add(new Option(test[i*2-2].trim(),test[i*2-1]), null);
						secondSubledgerAccount=true;}
					catch(ex){
						d.add(new Option(test[i*2-2].trim(),test[i*2-1]));
						secondSubledgerAccount=true;
					}
					populatesubledgeramount(test[i*2-2].trim(),j);
					if(test[i*2-1].trim()==selectedglcode){
						d.options[i].selected=true;
					}
				}
								
				getDetailType(j);
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				if(d!=null){
					for(var k=d.options.length-1;k>=1;k--)
					{
						d.remove(k);
					}
				}
				
				var detail = document.getElementById('subLedgerlist['+j+'].detailType.id');
				if(detail!=null){
					for(var k=detail.options.length-1;k>=1;k--)
					{
						detail.remove(k);
					}
				}
				/*var detailCode = document.getElementById('subLedgerlist['+j+'].detailCode');
				if(detailCode!=null){
					for(var k=detailCode.options.length-1;k>=1;k--)
					{
						detailCode.remove(k);
					}*/
					document.getElementById('subLedgerlist['+j+'].detailCode').placeholder=codeTextMessage;
					document.getElementById('subLedgerlist['+j+'].detailCode').style.color='DarkGray';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailKeyId').value='';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailKey').value='';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.amount').value='';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.amount').readOnly=false;
			//}
				
			}
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function populatesubledgeramount1(){
	for(var i=0;i<billDetailTableIndex+1;i++){
		if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
			for (var j=0; j<slDetailTableIndex+1; j++ )
				{
					if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
						var subglcode=document.getElementById('subLedgerlist['+j+'].glcode.id');
						if( document.getElementById('billDetailslist['+i+'].glcodeDetail').value==subglcode.options[subglcode.selectedIndex].text){
						document.getElementById('subLedgerlist['+j+'].amount').value=document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
						document.getElementById('subLedgerlist['+j+'].amount').readOnly=true;
						}
					}
				}
			
			
		}
	}
}
function populatesubledgeramount(glcode,index){
	for(var i=0;i<billDetailTableIndex+1;i++){
		if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
			if( document.getElementById('billDetailslist['+i+'].glcodeDetail').value==glcode){
				
				 document.getElementById('subLedgerlist['+index+'].amount').value=document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
				 document.getElementById('subLedgerlist['+index+'].amount').readOnly=true;
			}
			
		}
	}
}
function validateDetailCode(obj)
{
	var index = getRowIndex(obj);
	var element = document.getElementById(SUBLEDGERLIST+'['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index='+index+'&codeorname=both';
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackCode, null);
}
var callbackCode = {
	success: function(o) {
		var res= o.responseText;

		res = res.split('~');
		if(res.length>3)
		{
			dom.get("challan_error_area").style.display="none";
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value=res[1];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value=res[2];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').value=res[3];
		}
		else
		{
			dom.get("challan_error_area").style.display="block";
			//document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').focus();
			document.getElementById('challan_error_area').innerHTML='Please enter correct code/name in Subledger Details <br>';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value='';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value='';
			//document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').value='';
			return;
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}


var codeObj;
var acccodeArray;
function loadDropDownCodes()
{
	var	url = "/EGF/voucher/common-ajaxGetAllCoaNames.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			acccodeArray=codes.split("+");
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}


var yuiflag = new Array();
var yuiflag1 = new Array();
function autocompletecode(obj,myEvent)
{
var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 

	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-40;
	target.style.width=450;	
	codeObj
	var coaCodeObj=obj;
		var  currRow=getRowIndex(obj);

	//40 --> Down a+rrow, 38 --> Up arrow
	if(yuiflag[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode; 

		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				codeObj.applyLocalFilter = true;
				codeObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
			}
		}
		yuiflag[currRow] = 1;
	}	
}
function getRow(obj){
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}


function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

function validateAlphaNumeric( strValue )
{
	var objRegExp  = /^[0-9a-zA-Z]+$/;
	return objRegExp.test(strValue)
}

function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength)
	obj.value=obj.value.substring(0,mlength)
	}

var entities;
var entitiesArray;
var firstthreechars;
var savedthreechars;

var entobj;
var entevent;
function autocompleteForEntity(obj,myEvent){
var  currRow=getRowIndex(obj);
		
	
	if(obj.value.length<3){
		if(entities)
			{
				entities=null;
				if(oAutoCompEntity){
					YAHOO.util.Event.purgeElement(obj, true);//To remove the listners of the autocomplete instance
					oAutoCompEntity._elContainer.innerHTML="";
					yuiflag1[currRow] =undefined;
				}
			}
		savedthreechars="";
	}
	if(obj.value.length>=3){
		firstthreechars=obj.value.substring(0,3);
		var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
		if(savedthreechars!=firstthreechars && detailtypeidObj.value!=0){
			savedthreechars=firstthreechars;
			if(entities)
			{
				entities=null;
				if(oAutoCompEntity){
					YAHOO.util.Event.purgeElement(obj, true);
					oAutoCompEntity._elContainer.innerHTML="";
					yuiflag1[currRow] =undefined;
				}
			}
entobj=obj;
entevent=myEvent;

			if(onElementFocused(obj)){
				ShowImage(obj);//To start loading image
				var url =   path+ "/receipts/ajaxReceiptCreate-getCodeNew.action?detailTypeId="+detailtypeidObj.value+"&filterKey="+obj.value;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackAutoCompleteEntities, null);
			}
		}
		
	}
}


var callbackAutoCompleteEntities = {
	success: function(o) {
		var res= o.responseText;
		var entity=res.trim();
		var a = entity.split("^");
		var eachEntity = a[0];
		entitiesArray=eachEntity.split("+");
		entities = new YAHOO.widget.DS_JSArray(entitiesArray);
		var src = entobj;	
	var target = document.getElementById('subledgercodescontainer');	
	var posSrc=findPos(src); 
	
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]-40+"px";
	target.style.width=650;	
	      		
	
	var coaCodeObj=entobj;
if(onElementFocused(entobj))//To check if the element is still under focus
{
	var  currRow=getRowIndex(entobj);
	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflag1[currRow] == undefined)//To make sure autocomplete instance is created only once with that event 
	{
		var key = window.event ? window.event.keyCode : entevent.charCode;  

		if(key != 40 )
		{
			if(key != 38 )
			{
				
				oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'subledgercodescontainer', entities);
				oAutoCompEntity.queryDelay = 0;
				oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompEntity.useShadow = true;
				oAutoCompEntity.maxResultsDisplayed = 15;
				oAutoCompEntity.useIFrame = true;
				if(entities){
					entities.applyLocalFilter = true;
					entities.queryMatchContains = true;
				}
				oAutoCompEntity.minQueryLength = 0;
			oAutoCompEntity.sendQuery(coaCodeObj.value);
		oAutoCompEntity.itemSelectEvent.subscribe(function fnCallback(e, args) {//This is to set the values on mouse click
    			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=args[2];
			dom.get("challan_error_area").style.display="none";
 		});
			}
		}
		yuiflag1[currRow] = 1;
	}

}
HideImage(entobj);
	
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function splitEntitiesDetailCode(obj)
{

HideImage(obj);
	var currRow=getRowIndex(obj);
	var entity=obj.value;
	var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
	if(entity.trim()!="" && detailtypeidObj.value!=0 && entity.trim()!=codeTextMessage)
	{
		var entity_array=entity.split("`-`");
		if(entity_array.length==3)
		{
			dom.get("challan_error_area").style.display="none";
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=entity_array[0];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value=entity_array[2];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value=entity_array[1];
			var glcodenew=document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.glcode.id');
			populatesubledgeramount(glcodenew.options[glcodenew.selectedIndex].text,currRow);
		}
		else{
				validateDetailCode(obj);// to validate for the code/name on blur
		}
	}
	if(entity.trim()==codeTextMessage){
		document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value='';
		document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value='';
	}

}
function ShowImage(obj)
{
 obj.style.backgroundImage  = 'url('+path+'/images/LoadingV2.gif)';
 
 obj.style.backgroundRepeat= 'no-repeat';
                    
 obj.style.backgroundPosition = 'right';
}
function HideImage(obj)
{
 obj.style.backgroundImage  = 'none';
} 
function onElementFocused(e)
{
    return document.activeElement ==e?true:false;
       
} 




function  populateService(serviceCategory){
	dom.get('receiptMisc.fund.id').value="-1";
	dom.get('functionId').value="-1";
	populateserviceId({serviceCatId:serviceCategory.options[serviceCategory.selectedIndex].value});	
}

function loadFinDetails(service){

	var dept = dom.get('deptId').value;
	var service = dom.get('serviceId').value;
	
	var path = '/collection';
	
	var url1 = path+"/receipts/ajaxReceiptCreate-ajaxFinMiscDtlsByService.action?serviceId="+service+"&deptId="+dept;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url1,loadMiscDetails, null);
	
	
	var url2 = path+"/receipts/ajaxReceiptCreate-ajaxFinAccDtlsByService.action";
	makeJSONCall(["glcodeIdDetail","glcodeDetail","accounthead","creditAmountDetail"]
	,url2,{serviceId:service,deptId:dept},loadFinAccSuccessHandler,loadFinAccFailureHandler);

	var url3 = path+"/receipts/ajaxReceiptCreate-ajaxFinSubledgerByService.action";
	makeJSONCall(["subledgerCode","glcodeId","detailTypeId","detailTypeName","detailCode","detailKeyId",
	"detailKey","amount"],url3,{serviceId:service,deptId:dept},loadFinSubledgerSuccessHandler,loadFinSubledgerFailureHandler);
}

var miscArray;
var loadMiscDetails = {
success: function(o) {

var result = o.responseText;

if(null != result && result.length !=0){
	 miscArray = result.split('~');
		if(null != dom.get('receiptMisc.fund.id') ) {	
				 dom.get('receiptMisc.fund.id').value = parseInt(miscArray[0]);		
		}
		if(null != dom.get('schemeId') ){
				var url= "/EGF/voucher/common-ajaxLoadSchemes.action";
				var fundId = dom.get('receiptMisc.fund.id').value;
				makeJSONCall(["Text","Value"],url,{fundId:miscArray[0]},schemeDropDownSuccessHandler,schemeDropDownFailureHandler);
		}
		if(null != dom.get('subschemeId')  ){

				var url= "/EGF/voucher/common-ajaxLoadSubSchemes.action";
				var schemeId = dom.get('schemeId').value;
				makeJSONCall(["Text","Value"],url,{schemeId:miscArray[1]},subschemeDropDownSuccessHandler,subschemeDropDownFailureHandler);
				
		}
		
		if(null != dom.get('fundSourceId') ){
				var url= "/EGF/voucher/common-ajaxLoadFundSource.action";
				var subschemeId = dom.get('subschemeId').value;
				makeJSONCall(["Text","Value"],url,{subSchemeId:miscArray[2]},fundsourceDropDownSuccessHandler,fundsourceDropDownFailureHandler);

		}
		
		if(null != dom.get('receiptMisc.idFunctionary.id') ){
				 dom.get('receiptMisc.idFunctionary.id').value = parseInt(miscArray[4]);
		}
		if(null != dom.get('functionId') ) {	
			 dom.get('functionId').value = parseInt(miscArray[5]);	
		}
}

},
failure: function(o) {
alert('failure');
}
}

schemeDropDownSuccessHandler=function(req,res){

var schemeid = dom.get('schemeId');
var dropDownLength = schemeid.length;
var resLength =res.results.length+1;
for(i=0;i<res.results.length;i++){
		 schemeid.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
}
while(dropDownLength>resLength)
{
	schemeid.options[res.results.length+1] = null;
	dropDownLength=dropDownLength-1;
}

schemeid.value = miscArray[1];
setSchemeId();
}

schemeDropDownFailureHandler=function(){
alert('failure while loading scheme drop down');
}


subschemeDropDownSuccessHandler=function(req,res){

var subschemeId = dom.get('subschemeId');
var dropDownLength = subschemeId.length;
var resLength =res.results.length+1;
for(i=0;i<res.results.length;i++){
		 subschemeId.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
}
while(dropDownLength>resLength)
{
	subschemeId.options[res.results.length+1] = null;
	dropDownLength=dropDownLength-1;
}
subschemeId.value = miscArray[2];
setFundSourceId();
}

subschemeDropDownFailureHandler=function(){
alert('failure while loading sub scheme drop down');
}


fundsourceDropDownSuccessHandler=function(req,res){

var fundSourceId = dom.get('fundSourceId');
var dropDownLength = fundSourceId.length;
var resLength =res.results.length+1;
for(i=0;i<res.results.length;i++){
		 fundSourceId.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
}
while(dropDownLength>resLength)
{
	fundSourceId.options[res.results.length+1] = null;
	dropDownLength=dropDownLength-1;
}
fundSourceId.value = miscArray[3];
setSubSchemeId();
}

fundsourceDropDownFailureHandler=function(){
alert('failure while loading fundource drop down');
}



loadFinAccSuccessHandler=function(req,res){
var noOfRows =  billDetailsTable.getRecordSet().getLength();
billDetailsTable.deleteRows(0,noOfRows); 
billDetailTableIndex = 0;
billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
     "glcodeid":"",
     "glcode":"",
     "accounthead":"",
     "creditamount":""
 });      
updateGrid(VOUCHERDETAILLIST,'creditAmountDetail',0,0);
totalcramt = 0;          
billDetailTableIndex = 1;
for(i=0;i<res.results.length-1;i++){
	 billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
            "glcodeid":res.results[i].glcodeIdDetail,
            "glcode":res.results[i].glcodeDetail,
            "accounthead":res.results[i].accounthead,
            "creditamount":res.results[i].creditAmountDetail
        });
        updateAccountTableIndex();  
}

for(i=0;i<res.results.length;i++){  
        updateGrid(VOUCHERDETAILLIST,'glcodeIdDetail',i,res.results[i].glcodeIdDetail);
        updateGrid(VOUCHERDETAILLIST,'glcodeDetail',i,res.results[i].glcodeDetail);
        updateGrid(VOUCHERDETAILLIST,'accounthead',i,res.results[i].accounthead);
        updateGrid(VOUCHERDETAILLIST,'creditAmountDetail',i,res.results[i].creditAmountDetail);
        totalcramt = parseFloat(totalcramt)+parseFloat(res.results[i].creditAmountDetail);
        if(totalcramt>0){
    		totalcramt=parseInt(totalcramt);
		 }
}
if(totalcramt>0){
 document.getElementById('totalcramount').value=totalcramt;
}
if(document.getElementById('billDetailslist[0].accounthead').value!="")
{
	document.getElementById('billDetailslist[0].accounthead').disabled=true;
}
patternvalidation();
}
loadFinAccFailureHandler=function(){
alert('failure');
}


loadFinSubledgerSuccessHandler=function(req,res){

var noOfRows =  subLedgersTable.getRecordSet().getLength();
subLedgersTable.deleteRows(0,noOfRows); 
slDetailTableIndex = 0;
subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
 "glcode":"",
 "glcode.id":"",
 "detailType.id":"",
 "detailTypeName":"",
 "detailCode":"",
 "detailKeyId":"",
 "detailKey":"",
 "amount":""

});
updateSLGrid('amount',0,0);
slDetailTableIndex = 1;
for(i=0;i<res.results.length-1;i++){
			 subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
            "glcode":res.results[i].subledgerCode,
            "glcode.id":res.results[i].glcodeId,
            "detailType.id":res.results[i].detailTypeId,
            "detailTypeName":res.results[i].detailTypeName,
            "detailCode":res.results[i].detailCode,
            "detailKeyId":res.results[i].detailKeyId,
            "detailKey":res.results[i].detailKey,
            "amount":res.results[i].amount
          
        });
         updateSLTableIndex();
    }
for(i=0;i<res.results.length;i++){
      
        updateGridSLDropdown('glcode.id',i,res.results[i].glcodeId,res.results[i].subledgerCode);
        updateGridSLDropdown('detailType.id',i,res.results[i].detailTypeId,res.results[i].detailTypeName);
        updateSLGrid('detailCode',i,res.results[i].detailCode);
        updateSLGrid('detailKeyId',i,res.results[i].detailKeyId);
        updateSLGrid('detailKey',i,res.results[i].detailKey);
        updateSLGrid('amount',i,res.results[i].amount);
}
patternvalidation();
}

loadFinSubledgerFailureHandler=function(){
alert('failure');
}

function updateGrid(prefix,field,index,value){
	document.getElementById(prefix+'['+index+'].'+field).value=value;
}