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

/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var dom=YAHOO.util.Dom;

function roundTo(value,decimals,decimal_padding){
  if(!decimals) decimals=2;
  if(!decimal_padding) decimal_padding='0';
  if(isNaN(value)) value=0;
  value=Math.round(value*Math.pow(10,decimals));
  var stringValue= (value/Math.pow(10,decimals)).toString();
  var padding=0;
  var parts=stringValue.split(".")
  if(parts.length==1) {
  	padding=decimals;
  	stringValue+=".";
  } 
  else padding=decimals-parts[1].length
  for(i=0;i<padding;i++) stringValue+=decimal_padding
  return stringValue
}


function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL="/egi/resources/erp2/images/removerow.gif";
	    markup='<p align="center"><img height="16" border="0" width="16" alt="Delete" src="'+imageURL+'"/></p>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatter(baseURL){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL="/egi/resources/erp2/images/addrow.gif";
	    markup='<p align="center"><img height="16" border="0" width="16" alt="Add" src="'+imageURL+'"/></p>'
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}


function makeJSONCall(fields,url,params,onSuccess,onFailure){
 dataSource=new YAHOO.util.DataSource(url);
            dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
            dataSource.connXhrMode = "queueRequests";
            dataSource.responseSchema = {
                resultsList: "ResultSet.Result",
                fields: fields
            };
	        var callbackObj = {
            success : onSuccess,
            failure : onFailure
        };
        dataSource.sendRequest("?"+toQuery(params),callbackObj);
}

function toQuery(params){
   var query="";
   for(var f in params){
     query+=f+"="+params[f]+"&"
   }
   if(query.lastIndexOf('&')==query.length-1) query=query.substring(0,query.length-1);
   return query;
}

function getNumericValueFromInnerHTML(id){
    value=dom.get(id).innerHTML;
    return getNumber(value);
}

function getNumber(value){
    return isNaN(value)?0.0:parseFloat(value);
}

function createTextFieldFormatter(size, maxlength, columnName,onBlur){
    var textboxFormatter = function(el, oRecord, oColumn, oData) {
                            var value = (YAHOO.lang.isValue(oData))?oData:"";
                            var id=oColumn.getKey()+oRecord.getId();
                            var fieldName=oColumn.getKey()+'_'+oRecord.getData(columnName)
                            var recordId=oRecord.getId();
                            markupTemplate="<input type='text' id='@id@' name='@fieldName@' size='@size@' maxlength='@maxlength@' class='selectamountwk' @onblur@ /><span id='error@id@' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
                            var markup=markupTemplate.replace(/@id@/g,id).
                                   replace(/@fieldName@/g,fieldName).
                                   replace(/@size@/g,size).
                                   replace(/@maxlength@/g,maxlength);

                            var onblurAttrib=''
                             if(onBlur){
                              onblurAttrib="onblur='"+onBlur+"(this,\""+recordId+"\");'";
                             }
                             markup= markup.replace(/@onblur@/g,onblurAttrib);                                   

                             el.innerHTML = markup;
                            }
    return textboxFormatter;
}
function validateNumberInTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}

function addCell(tr,index,divId,initialValue){
	var cell = tr.insertCell(index);
	cell.setAttribute('className','selectamountwk whitebox4wknoalign');
	cell.setAttribute('class','selectamountwk whitebox4wknoalign');
	cell.innerHTML = '<div class="yui-dt-liner" id="'+divId+'">'+initialValue+'</div>';
}

function showMessage(id,msg){
    dom.get(id).style.display='';
    dom.get(id).innerHTML=msg;
}
function clearMessage(id){
    dom.get(id).style.display='none';
    dom.get(id).innerHTML='';
}
function getControlInBranch(obj,controlName)
{
	if (!obj || !(obj.getAttribute)) return null;
	// check if the object itself has the name
	if (obj.getAttribute('id') == controlName) return obj;

	// try its children
	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

// this is to get the current row
function getRow(obj)    
{
 if(!obj)return null;
 tag = obj.nodeName.toUpperCase();
 while(tag != 'BODY'){
  if (tag == 'TR') return obj;
  obj=obj.parentNode ;
  tag = obj.nodeName.toUpperCase();
 }
 return null;
}

function getRowNum(obj){
	var par=obj.parentNode;
	while(par.nodeName.toLowerCase()!='tr'){
	par=par.parentNode;
	}
	bootbox.alert(par.rowIndex);
	return par.rowIndex;
} 

function validateNotFutureDate(inputDate,currDate){
	var currentYear = currDate.substr(6,4);
	var inputYear  = inputDate.substr(6,4);
	var currentMonth = currDate.substr(3,2);
	var inputMonth = inputDate.substr(3,2);
	var currentDay = currDate.substr(0,2);
	var inputDay = inputDate.substr(0,2);
	
	//if entered year is ahead return false
	if(inputYear > currentYear)
	{
		return false;
	}
	else if((eval(inputYear) == eval(currentYear)) && (eval(inputMonth) > eval(currentMonth)))
	{
		return false;
	}
	else if((eval(inputYear) >= eval(currentYear)) && (eval(inputMonth) == eval(currentMonth)) && (eval(inputDay) > eval(currentDay)))
	{
		return false;
	}
	return true;
}


function validatedays(chqDate,receiptDate)
{
	var cd = process(chqDate);
	var rd = process(receiptDate);
	   
	var timeDiff = Math.abs(cd.getTime()- rd.getTime());
	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
	if(diffDays>90)
		{
		return false;
		}
	return true;

}


function process(date){
   var parts = date.split("/");
   var date = new Date(parts[1] + "/" + parts[0] + "/" + parts[2]);
   return date;
}

function validateChequeDate(chqDate,currDate){
	var currentYear = currDate.substr(6,4);
	var chequeYear  = chqDate.substr(6,4);
	var currentMonth = currDate.substr(3,2);
	var chequeMonth = chqDate.substr(3,2);
	var currentDay = currDate.substr(0,2);
	var chequeDay = chqDate.substr(0,2);
	//if entered year is ahead return false 
	var financialDate = new Date('2012-04-01');
	 var mReceiptDate = new Date(currentYear + '-' + currentMonth+'-' +currentDay);
	if(chequeYear > currentYear)
	{
		return false;
	}
	if((eval(chequeYear) == eval(currentYear)) && (eval(chequeMonth) > eval(currentMonth)))
	{
		return false;
	}
	var monthDiff = eval(12-chequeMonth)+eval(currentMonth)+eval((currentYear-chequeYear-1)*12);

	if(mReceiptDate < financialDate) {
	if(eval(monthDiff) > 6){
		  return false; 
		}
	}
	else {
		if(eval(monthDiff) > 3){
			return false;
		}
	}
	if(monthDiff==6){
		if(eval(chequeDay<currentDay)){
			return false;
		}
	}
	if(monthDiff==0){
		if(eval(chequeDay>currentDay)){
			return false;
		}
	}
	return true;
}


function getNumber(value){
    return isNaN(value)?0.0:parseFloat(value);
}

function validateNumber(elem){
	bootbox.alert('helper validateNumber ');
	if(isNaN(elem) || getNumber(elem)<0 || elem.startsWith('+')){
		bootbox.alert('invalid number');
      	return false;
      }
	bootbox.alert('valid number');
	return true;
}


