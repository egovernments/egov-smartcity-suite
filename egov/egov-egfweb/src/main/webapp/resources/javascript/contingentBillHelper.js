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

function validate()
{

	
	if(!validateForm_contingentBill())
	{
		document.getElementById("tabber0").tabber.tabShow(0);
		undoLoadingMask() ;
		return false;
	}
	if(document.getElementById("contingentBill_commonBean_billNumber")){
		var billNumber = document.getElementById("contingentBill_commonBean_billNumber").value;
		if(billNumber== null || billNumber == ""){
			bootbox.alert("Please select bill number");
			return false;
		}
	}
	var len=billDetailsTableFinal.getRecordSet().getLength();
	if( len!=undefined && len>=1)
	{
		document.getElementById("commonBean.subledgerType").disabled=false;
		document.getElementById("commonBean.payto").disabled=false;
		enableAll();
		//return true;
	}
	else
	{
		bootbox.alert("No Account code is selected for Save . Please add Account code and click done and then try to save again");	
		undoLoadingMask() ;
		return false;
	}
	var billDate = document.getElementById("billDate").value;
  

//    if (myDate > today) {
//        bootbox.alert("Bill date is greater than today's date ");
//        return false
//    }
	
return true;
}

//it takes account detail type id and creates 
function load_COA_Entities(obj)
{
	//bootbox.alert("load_COA_Entities");
	detailTypeId=obj.value;
	loadDropDownCodesForAccountDetailType(obj);
	if(obj.value!="")
	{
	document.getElementById('entitycode').innerHTML=obj.options[obj.selectedIndex].text+" Code ";
	}
	//loadDropDownCodesForEntities(obj);   
}
function functionFormatter1(tableName,columnName,type){
	bootbox.alert("<input type='"+type+"'  id='"+tableName+"["+billDetailTableIndex+"]"+columnName+"' name='"+tableName+"["+billDetailTableIndex+"]"+columnName+"' onkeyup='autocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcode(this)' ");
	}
		

function splitDetailName()
{
	
}



function netglcodeFormatter(tableName,columnName,type){
return function(el, oRecord, oColumn, oData) {
var table_name=eval(tableName);
var value = (YAHOO.lang.isValue(oData))?oData:"";
el.innerHTML = "<select   id='"+tableName+"["+billDetailTableIndex+"]"+columnName+"' name='"+tableName+"["+billDetailTableIndex+"]"+columnName+"'   onchange='splitNetGlCode(this)' style='width:123px'/>";
el.innerHTML =el.innerHTML + "<input type='hidden'   id='"+tableName+"["+billDetailTableIndex+"].isSubledger' name='"+tableName+"["+billDetailTableIndex+"].isSubledger'/>";
el.innerHTML =el.innerHTML + "<input type='hidden'   id='"+tableName+"["+billDetailTableIndex+"].glcodeIdDetail' name='"+tableName+"["+billDetailTableIndex+"].glcodeIdDetail' />";
el.innerHTML =el.innerHTML + "<input type='hidden'   id='"+tableName+"["+billDetailTableIndex+"].detailTypes' name='"+tableName+"["+billDetailTableIndex+"].detailTypes' />";
}
}

function checkListNameFormatter(tableName,columnName,type,isReadonly){
	return function(el, oRecord, oColumn, oData) {
	var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    	 }
	var value = (YAHOO.lang.isValue(oData))?oData:"";
	el.innerHTML =el.innerHTML + "<input type='"+type+"' wraptext  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' readonly='"+isReadonly+"' size='100'/>";
	}
	}

function checkListValueFormatter(tableName,columnName,type){
	return function(el, oRecord, oColumn, oData) {
	var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    	 }
	var value = (YAHOO.lang.isValue(oData))?oData:"";
	el.innerHTML = "<select   id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' />";
	el.innerHTML =el.innerHTML + "<input type='hidden'   id='"+tableName+"["+index+"].id' name='"+tableName+"["+index+"].id'/>";
	}
	}
function deleteYUIRow(tableName,obj)
{
if(obj.disabled==true)
{
return;
}

var table_name=eval(tableName);		
var record = table_name.getRecord(obj);
if(table_name.getRecordSet().getLength()>1)
{			
table_name.deleteRow(record);

allRecords=table_name.getRecordSet();
for(var i=0;i<allRecords.getLength();i++){
	table_name.updateCell(table_name.getRecord(i),table_name.getColumn('SlNo'),""+(i+1));

}

}
else{
bootbox.alert("This row cannot be deleted");
}
}	


function deleteYUIRow1(tableName,obj)
{
if(obj.disabled==true)
{
return;
}

var table_name=eval(tableName);		
var record = table_name.getRecord(obj);
if(table_name.getRecordSet().getLength()>1)
{			
table_name.deleteRow(record);

allRecords=table_name.getRecordSet();
for(var i=0;i<allRecords.getLength();i++){
	table_name.updateCell(table_name.getRecord(i),table_name.getColumn('SlNo'),""+(i+1));

}
calculatenNetFordelete();
}
else{
bootbox.alert("This row cannot be deleted");
}
}	

function addYUIRow(tableName,obj)
{
if(obj.disabled==true)
{
return;
}
var table_name=eval(tableName);

table_name.addRow({SlNo:table_name.getRecordSet().getLength()+1});
}
function validateNetPaySubledgerAndDetailTypes()
{
	
	var detailType=document.getElementById("commonBean.subledgerType").value;
	var netType=document.getElementById("billDetailsTableNet[0].glcodeDetail").options[document.getElementById("billDetailsTableNet[0].glcodeDetail").selectedIndex].text;
	var a=document.getElementById("billDetailsTableNet[0].detailTypes").value;
	var isSub=document.getElementById("billDetailsTableNet[0].isSubledger").value;
	//bootbox.alert(a);
	var x=a.split("^");
	//bootbox.alert(x);
	var found=false;
	if(isSub=='true')
	{
		if(x.length>0)
		{
	out:		for (var n=0;n<x.length-1;n++)
			{
				var each=x[n].split("~");
				if(each[0]==netType)
				{
					var adids=each[1].split("`-`");
					//bootbox.alert(adids);
					for(var t=0;t<adids.length-1;t++)
					{
						if(detailType==adids[t])
						{
							found=true;
							break out;
						}
					}
				}

			}
		}
		if(found==false)
		{
			bootbox.alert("Selected net payable's detailtypes and selected subledgertype does not match ");
			return false;
		}
	}else
	{
		return true;
	}
                                  
}                            
function updateTabels()
{
	if(document.getElementById('billDetailsTableNet[0].debitAmountDetail').value<0)
	{
		bootbox.alert('Negative Net payable is not allowed');
		return false;
	}                           
var netpayablevalue=document.getElementById("billDetailsTableNet[0].glcodeDetail").value

	if(netpayablevalue=="-1"){
		bootbox.alert('Please Select a  Net payable Code');
		return false;
	}
var debitTable=document.getElementById('billDetailTable').getElementsByTagName('table')[0];
var dtLen=debitTable.rows.length;

var validNet= validateNetPaySubledgerAndDetailTypes();
if(validNet==false)
{
	return false;
}
if(document.getElementById("billDetailsTableNet[0].isSubledger").value=='true')
{
	if(document.getElementById("detailCode").value.trim()=="")
	{
		bootbox.alert("Subledger is not selected for net payable");
		return false;
	}
}

document.getElementById("commonBean.subledgerType").disabled=true;

var i=0;
var j=0;
var netbilltableLen=billDetailsTable.getRecordSet().getLength();
while(i<dtLen)
{
while(j<netbilltableLen)
{
//bootbox.alert("j"+j);
if(document.getElementById("billDetailsTable["+j+"].glcodeDetail"))
{
i++;
var k=0;
var found=false;
//bootbox.alert("net row Length"+document.getElementById("billDetailTableFinal").getElementsByTagName('table')[0].rows.length);
while(k<25)
	{
//bootbox.alert("k="+k);
	if(document.getElementById("billDetailsTableFinal["+k+"].glcodeDetail")==null)
	{
		k++;
		continue;
	}
	
	if( document.getElementById("billDetailsTable["+j+"].glcodeDetail").value!="" && document.getElementById("billDetailsTableFinal["+k+"].glcodeDetail").value==document.getElementById("billDetailsTable["+j+"].glcodeDetail").value)
		{
		found=true;
		document.getElementById("billDetailsTableFinal["+k+"].debitAmountDetail").value= parseFloat(document.getElementById("billDetailsTableFinal["+k+"].debitAmountDetail").value)+parseFloat(document.getElementById("billDetailsTable["+j+"].debitAmountDetail").value);
		break;
		}
		k++;
	}
if(found==false && document.getElementById("billDetailsTable["+j+"].glcodeDetail").value!="" )
	{
	//addrow and make readonly
	billDetailsTableFinal.addRow({SlNo:billDetailsTableFinal.getRecordSet().getLength()+1});
	var len=billDetailsTableFinal.getRecordSet().getLength()-1;
	
	document.getElementById("billDetailsTableFinal["+len+"].glcodeDetail").value=document.getElementById("billDetailsTable["+j+"].glcodeDetail").value;
	document.getElementById("billDetailsTableFinal["+len+"].glcodeIdDetail").value=document.getElementById("billDetailsTable["+j+"].glcodeIdDetail").value;
	document.getElementById("billDetailsTableFinal["+len+"].accounthead").value=document.getElementById("billDetailsTable["+j+"].accounthead").value;
	document.getElementById("billDetailsTableFinal["+len+"].debitAmountDetail").value=document.getElementById("billDetailsTable["+j+"].debitAmountDetail").value;
	document.getElementById("billDetailsTableFinal["+len+"].isSubledger").value=document.getElementById("billDetailsTable["+j+"].isSubledger").value;
	if(mode!='modify')
	{
		document.getElementById("billDetailsTableFinal["+len+"].glcodeDetail").disabled=true;
		document.getElementById("billDetailsTableFinal["+len+"].glcodeDetail").tabIndex="-1";
		document.getElementById("billDetailsTableFinal["+len+"].accounthead").disabled=true;
		document.getElementById("billDetailsTableFinal["+len+"].accounthead").tabIndex="-1";
		document.getElementById("billDetailsTableFinal["+len+"].debitAmountDetail").disabled=true;
		document.getElementById("billDetailsTableFinal["+len+"].debitAmountDetail").tabIndex=-1;
	}
	}	
}
//add subledgers 
if(document.getElementById("billDetailsTable["+j+"].isSubledger") && document.getElementById("billDetailsTable["+j+"].isSubledger").value=='true')
{
var s_table_name=eval("billDetailsTableSubledger");
var subledgerLen=s_table_name.getRecordSet().getLength()+1;
s_table_name.addRow({SlNo:subledgerLen})	
//bootbox.alert(subledgerLen);
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").value=document.getElementById("billDetailsTable["+j+"].glcodeDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].glcodeIdDetail").value=document.getElementById("billDetailsTable["+j+"].glcodeIdDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].accounthead").value=document.getElementById("billDetailsTable["+j+"].accounthead").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").value=document.getElementById('detailName').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").value=document.getElementById('detailCode').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").value=document.getElementById('detailKey').value;	
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").value=document.getElementById("billDetailsTable["+j+"].debitAmountDetail").value;
if(mode!='modify')
	{
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").tabIndex="-1";
	}
}	
j++;
}
i++;
}
var table_name=eval("billDetailsTable");
//table_name.getRecordSet().reset();
if(table_name.getRecordSet().getLength()>=1)
{			
	allRecords=table_name.getRecordSet();
	for(var i=allRecords.getLength();i>=0;i--)
	{
	table_name.deleteRow(i);
	}
table_name.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1})		
}

//Deductions-------------------------------------------------
var debitTable=document.getElementById('billDetailTable').getElementsByTagName('table')[0];
var dtLen=debitTable.rows.length;
//bootbox.alert(dtLen);

var i=0;
var j=0;
var netbilltableLen=billDetailsTableCredit.getRecordSet().getLength();
while(i<dtLen)
{
while(j<netbilltableLen)
{
//bootbox.alert("j"+j);
if(document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail"))
{
i++;
var k=0;
var found=false;
//bootbox.alert("net row Length"+document.getElementById("billDetailTableFinal").getElementsByTagName('table')[0].rows.length);
while(k<25)
	{
	if(document.getElementById("billDetailsTableCreditFinal["+k+"].glcodeDetail")==null)
	{
		k++;
		continue;
	}
	
	if(document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail").value!="" && document.getElementById("billDetailsTableCreditFinal["+k+"].glcodeDetail").value==document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail").value)
		{
		found=true;
		document.getElementById("billDetailsTableCreditFinal["+k+"].creditAmountDetail").value= parseFloat(document.getElementById("billDetailsTableCreditFinal["+k+"].creditAmountDetail").value)+parseFloat(document.getElementById("billDetailsTableCredit["+j+"].debitAmountDetail").value);
		break;
		}
		k++;
	}
if(found==false && document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail").value!="")
	{
	//addrow 
	billDetailsTableCreditFinal.addRow({SlNo:billDetailsTableCreditFinal.getRecordSet().getLength()+1});
	var len=billDetailsTableCreditFinal.getRecordSet().getLength()-1;
	document.getElementById("billDetailsTableCreditFinal["+len+"].glcodeDetail").value=document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail").value;
	document.getElementById("billDetailsTableCreditFinal["+len+"].glcodeIdDetail").value=document.getElementById("billDetailsTableCredit["+j+"].glcodeIdDetail").value;
	document.getElementById("billDetailsTableCreditFinal["+len+"].accounthead").value=document.getElementById("billDetailsTableCredit["+j+"].accounthead").value;
	document.getElementById("billDetailsTableCreditFinal["+len+"].creditAmountDetail").value=document.getElementById("billDetailsTableCredit["+j+"].debitAmountDetail").value;
	document.getElementById("billDetailsTableCreditFinal["+len+"].isSubledger").value=document.getElementById("billDetailsTableCredit["+j+"].isSubledger").value;
	if(mode!='modify')
		{
		document.getElementById("billDetailsTableCreditFinal["+len+"].glcodeDetail").disabled=true;
		document.getElementById("billDetailsTableCreditFinal["+len+"].glcodeDetail").tabIndex="-1";
		document.getElementById("billDetailsTableCreditFinal["+len+"].creditAmountDetail").disabled=true;
		document.getElementById("billDetailsTableCreditFinal["+len+"].creditAmountDetail").tabIndex="-1";
		}
	}	
}
if(document.getElementById("billDetailsTableCredit["+j+"].isSubledger") && document.getElementById("billDetailsTableCredit["+j+"].isSubledger").value=='true')
{
var s_table_name=eval("billDetailsTableSubledger");
var subledgerLen=s_table_name.getRecordSet().getLength()+1;
s_table_name.addRow({SlNo:subledgerLen});	
//bootbox.alert(subledgerLen);
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").value=document.getElementById("billDetailsTableCredit["+j+"].glcodeDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].glcodeIdDetail").value=document.getElementById("billDetailsTableCredit["+j+"].glcodeIdDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].accounthead").value=document.getElementById("billDetailsTableCredit["+j+"].accounthead").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").value=document.getElementById('detailName').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").value=document.getElementById('detailCode').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").value=document.getElementById('detailKey').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").value=document.getElementById("billDetailsTableCredit["+j+"].debitAmountDetail").value;
if(mode!='modify')
	{
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").tabIndex="-1";
	}
}
j++;
}
i++;
}
var table_name=eval("billDetailsTableCredit");
//table_name.getRecordSet().reset();
if(table_name.getRecordSet().getLength()>=1)
{			
	allRecords=table_name.getRecordSet();
	for(var i=allRecords.getLength();i>=0;i--)
	{
	table_name.deleteRow(i);
	}
table_name.addRow({SlNo:billDetailsTableCredit.getRecordSet().getLength()+1})		
}

//polulate net if Subledger
if(document.getElementById("billDetailsTableNet[0].isSubledger").value=='true')
{
var s_table_name=eval("billDetailsTableSubledger");
var subledgerLen=s_table_name.getRecordSet().getLength()+1;
s_table_name.addRow({SlNo:subledgerLen});	
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").value=document.getElementById("billDetailsTableNet[0].glcodeDetail").options[document.getElementById("billDetailsTableNet[0].glcodeDetail").selectedIndex].text;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].glcodeIdDetail").value=document.getElementById("billDetailsTableNet[0].glcodeIdDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].accounthead").value=document.getElementById("billDetailsTableNet[0].accounthead").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").value=document.getElementById('detailName').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").value=document.getElementById('detailCode').value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").value=document.getElementById("billDetailsTableNet[0].debitAmountDetail").value;
document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").value=document.getElementById('detailKey').value;

if(mode=!'modify')
	{
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].subledgerCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailName").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailCode").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].detailKey").tabIndex="-1";
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").disabled=true;
	document.getElementById("billDetailsTableSubledger["+(subledgerLen-1)+"].debitAmountDetail").tabIndex="-1";
	}
}

//--populate net	
if(billDetailsTableNetFinal.getRecordSet().getLength()==0)
{
billDetailsTableNetFinal.addRow({SlNo:billDetailsTableNetFinal.getRecordSet().getLength()+1});
}
//{
document.getElementById("billDetailsTableNetFinal[0].glcodeDetail").value=document.getElementById("billDetailsTableNet[0].glcodeDetail").options[document.getElementById("billDetailsTableNet[0].glcodeDetail").selectedIndex].text;
document.getElementById("billDetailsTableNetFinal[0].glcodeDetail").disabled=true;
document.getElementById("billDetailsTableNetFinal[0].glcodeDetail").tabIndex="-1";
document.getElementById("billDetailsTableNetFinal[0].glcodeIdDetail").value=document.getElementById("billDetailsTableNet[0].glcodeIdDetail").value;
document.getElementById("billDetailsTableNetFinal[0].accounthead").value=document.getElementById("billDetailsTableNet[0].accounthead").value;
document.getElementById("billDetailsTableNetFinal[0].isSubledger").value=document.getElementById("billDetailsTableNet[0].isSubledger").value;
//}
//---calculate net	
var debitTotal=0;
var creditTotal=0;
var netPayTotal=0;
var debit_table_name=eval("billDetailsTableFinal");
var credit_table_name=eval("billDetailsTableCreditFinal");
var debitLen=debit_table_name.getRecordSet().getLength();
var creditLen=credit_table_name.getRecordSet().getLength();
var i=0;
for(i=0;i<25;i++)
{
	if(document.getElementById("billDetailsTableFinal["+i+"].debitAmountDetail")==null)
	{
		i++;continue;
	}
if(document.getElementById("billDetailsTableFinal["+i+"].debitAmountDetail").value!="")
{
debitTotal=debitTotal+(parseFloat(eval(document.getElementById("billDetailsTableFinal["+i+"].debitAmountDetail").value)*10/10));
}
}
for(i=0;i<creditLen;i++)                      
{
	if(document.getElementById("billDetailsTableCreditFinal["+i+"].creditAmountDetail")==null)
	{
		i++;continue;
	}
if(document.getElementById("billDetailsTableCreditFinal["+i+"].creditAmountDetail").value!="")
{
creditTotal=creditTotal+(parseFloat(eval(document.getElementById("billDetailsTableCreditFinal["+i+"].creditAmountDetail").value)*10/10));
}
}	

//bootbox.alert(debitTotal-creditTotal);

var resultNumTableNetFinal = debitTotal-creditTotal;
document.getElementById("billDetailsTableNetFinal[0].creditAmountDetail").value = resultNumTableNetFinal.toFixed(2);
document.getElementById("billDetailsTableNetFinal[0].creditAmountDetail").disabled=true;
document.getElementById("billDetailsTableNetFinal[0].creditAmountDetail").tabIndex="-1";
document.getElementById("detailName").value="";
document.getElementById("detailKey").value="";
document.getElementById("detailCode").value="";
document.getElementById("billDetailsTableNet[0].debitAmountDetail").value ="";

//disable payto if more than one subleder is used
var s_table_name=eval("billDetailsTableSubledger");
var subledgerLen=s_table_name.getRecordSet().getLength();
var previousCode="";
var currentCode="";
entityCount=1;
for(var l=0;l<subledgerLen;l++)
{
	
	//bootbox.alert(l);
	currentCode=document.getElementById("billDetailsTableSubledger["+l+"].detailCode").value;
	if(previousCode!="" && previousCode!=currentCode)
	{
		entityCount++;
		if(entityCount>1)
		{
		document.getElementById("commonBean.payto").disabled=true;
		break;
		}
			
	}
	previousCode=currentCode;
	
}
document.getElementById("billDetailsTableNet[0].glcodeDetail").value="";
document.getElementById("billDetailsTableNet[0].glcodeDetail").disabled=true;
document.getElementById("billDetailsTableNet[0].accounthead").value="";

}

function calculateNet(obj)
{
	var amountshouldbenumeric="Amount should be numeric";

	if(isNaN(obj.value))
	{
		bootbox.alert(amountshouldbenumeric);
		obj.value="";
		return false;
	}
	var debitLen=billDetailsTable.getRecordSet().getLength();
	var creditLen=billDetailsTableCredit.getRecordSet().getLength();
	var debit=0;
	var credit=0;
	var i=0;
	for(i=0;i<25;i++)
	{
		if(document.getElementById("billDetailsTable["+i+"].debitAmountDetail")==null)
		{
			i++;continue;
		}
		if(document.getElementById("billDetailsTable["+i+"].debitAmountDetail").value.trim()!="")
		{
			debit=debit+parseFloat(eval(document.getElementById("billDetailsTable["+i+"].debitAmountDetail").value));
		}
	}
	for(i=0;i<25;i++)
	{
		if(document.getElementById("billDetailsTableCredit["+i+"].debitAmountDetail")==null)
		{
			i++;continue;
		}
		if(document.getElementById("billDetailsTableCredit["+i+"].debitAmountDetail").value.trim()!="")
		{
			credit=credit+parseFloat(eval(document.getElementById("billDetailsTableCredit["+i+"].debitAmountDetail").value));
		}
	}
	var resultNum = parseFloat(debit-credit);
	document.getElementById("billDetailsTableNet[0].debitAmountDetail").value = resultNum.toFixed(2);

}


function hideShow()
{
if(document.getElementById("summary").style.display=='none')
	document.getElementById("summary").style.display="block";
else
	document.getElementById("summary").style.display="none";

}



function splitNetGlCode(obj)
{
//bootbox.alert(obj.value);
var acode=obj.value.split("~#");
if(acode!=null && acode.length==3)
{
document.getElementById('billDetailsTableNet[0].accounthead').value=acode[1];
document.getElementById('billDetailsTableNet[0].isSubledger').value=acode[2];
}
}		

function loadSubledgerGrids(temp)
{

if(document.getElementById('detailCode').value.trim()=="")
{
bootbox.alert(entityNotSelected);
return false;
}
else
{
return true;
}	
}


function splitEntities(obj)
{
	
var entity=obj.value;
var detailKey  = document.getElementById("detailKey").value;
if(entity.trim()!="")
{
var entity_array=entity.split("`~`");
if(entity_array.length==2)
{
obj.value=entity_array[0].split("`-`")[0];
document.getElementById("detailKey").value=entity_array[1];
document.getElementById("detailName").value=entity_array[0].split("`-`")[1];
//bootbox.alert("setting payto");
document.getElementById("commonBean.payto").value=entity_array[0].split("`-`")[1];
//bootbox.alert(document.getElementById("commonBean.payto").value);
}
else if(detailKey == null || detailKey== "")
{
//bootbox.alert(invalidEntityselected);
obj.value="";
document.getElementById("commonBean.payto").value="";
document.getElementById("detailName").value="";
document.getElementById("detailKey").value="";
}
}

}

function loadBank()
{
	
}

function enableAll()
{
	for(var i=0;i<document.forms[0].length;i++)
		document.forms[0].elements[i].disabled =false;
}
//disables except close button
function disableAll()
{
var frmIndex=0;
for(var i=0;i<document.forms[frmIndex].length;i++)
{
	for(var i=0;i<document.forms[0].length;i++)
{
	if(document.forms[0].elements[i].value != 'Save & Forward' && document.forms[0].elements[i].value != 'Approve'
		&& document.forms[0].elements[i].value != 'Reject' && document.forms[0].elements[i].value != 'Cancel' && document.forms[0].elements[i].value != 'Forward'
			&& document.forms[0].elements[i].value != 'Revert for Rectification' && document.forms[0].elements[i].value != 'Proceed' 
			&& document.forms[0].elements[i].value != 'Checked and Proceed'                
				&& document.forms[0].elements[i].name!='struts.token.name' && document.forms[0].elements[i].name!='struts.token'){
		document.forms[frmIndex].elements[i].disabled =true;   
	}						
}	
}
disableYUIAddDeleteButtons(true);
document.getElementById("closeButton").disabled=false;   
}

function disableYUIAddDeleteButtons(value)
{
	var i=0;
	var allAddImages=document.getElementsByName('egov_yui_add_image');
	if(allAddImages)
	{
		for (i=0;i<allAddImages.length;i++)
		{
			allAddImages[i].disabled=value;
		}
	}
	else if(document.getElementById('egov_yui_add_image'))	
	{
		document.getElementById('egov_yui_add_image').disabled=value;
	}
	
	var allDeleteImages=document.getElementsByName('egov_yui_delete_image');
	if(allDeleteImages)
	{
		for (i=0;i<allDeleteImages.length;i++)
		{
			allDeleteImages[i].disabled=value;
		}
	}
	else if(document.getElementById('egov_yui_delete_image'))
	{
		document.getElementById('egov_yui_delete_image').disabled=value;
	}

}

function checkLength(obj){
	if(obj.value.length>1024)
	{
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function openSearchWindow(obj, type) {
	var detailType=document.getElementById("commonBean.subledgerType").value;
	//bootbox.alert(detailType);
	if(detailType != '' && detailType != null) {
		var	url = "../voucher/common-searchEntites.action?accountDetailType="+detailType;
		window.open(url, 'Search','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		bootbox.alert("Select the Subledger Type.");
	}
}


function popupCallback(arg0, srchType) {
	var entity_array = arg0.split("^#");
	if(srchType == 'EntitySearch' ) {
		if(entity_array.length==3)
		{
			document.getElementById("detailCode").value=entity_array[0];
			document.getElementById("detailName").value=entity_array[1];
			document.getElementById("detailKey").value=entity_array[2];
	
			//bootbox.alert("setting payto");
			document.getElementById("commonBean.payto").value=entity_array[1];
			//bootbox.alert(document.getElementById("commonBean.payto").value);
		}
		else
		{
			bootbox.alert(invalidEntityselected);
			document.getElementById("commonBean.payto").value="";
			document.getElementById("detailName").value="";
			document.getElementById("detailKey").value="";
		}
	}
}

function splitEntitiesForGrid(obj) {
	var entity = obj.value;
	var entity_array=entity.split("`~`");
	if(entity_array.length>=1)
	{
			//obj.value = entity_array[0];
			var currRow = getRowIndex(obj);
			var detailname = obj.name;
			var detailkey = obj.name;
			detailname = detailname.replace("detailCode", "detailName");
			detailkey = detailkey.replace("detailCode", "detailKey");
			document.getElementById(obj.name).value= entity_array[0].split("`-`")[0];
			document.getElementById(detailname).value = entity_array[0].split("`-`")[1];
			document.getElementById(detailkey).value = entity_array[1];
		} else {
			bootbox.alert(invalidEntityselected);
			obj.value = "";
			var detailname = obj.name;
			var detailkey = obj.name;
			detailname = detailname.replace("detailCode", "detailName");
			detailkey = detailkey.replace("detailCode", "detailKey");
			document.getElementById(obj.name).value = "";
			document.getElementById(detailname).value = "";
			document.getElementById(detailkey).value = "";
		}
	}

function calculateNetForGrid(obj) {
	var amountshouldbenumeric = "Amount should be numeric";
	if (isNaN(obj.value)) {
		bootbox.alert(amountshouldbenumeric);
		obj.value = "";
		return false;
	}
	calculatenNetFordelete();
	

} 
function calculatenNetFordelete()
{
	var debitLen = billDetailsTableFinal.getRecordSet().getLength();
	var creditLen = billDetailsTableCreditFinal.getRecordSet().getLength();
	var debit = 0;
	var credit = 0;
	var i = 0;
	var k=0;
	for (i = 0; i < 25 && k<debitLen ; i++) {
		if(document.getElementById("billDetailsTableFinal[" + i	+ "].debitAmountDetail"))
		{
		if (document.getElementById("billDetailsTableFinal[" + i+ "].debitAmountDetail").value.trim() != "") {
			debit = debit+ parseFloat(eval(document.getElementById("billDetailsTableFinal[" + i	+ "].debitAmountDetail").value));
			k++;
		}
		}else
		{
		
		}
	}
	k=0;
	for (i = 0; i <25&& k< creditLen; i++) {
		if(document.getElementById("billDetailsTableCreditFinal[" + i+ "].creditAmountDetail"))
		  {
		if (document.getElementById("billDetailsTableCreditFinal[" + i+ "].creditAmountDetail").value.trim() != "") 
				{
			credit = credit+ parseFloat(eval(document.getElementById("billDetailsTableCreditFinal[" + i+ "].creditAmountDetail").value));
				}
		  }
		else
		{
			
		}
		}
		
	var resultNum = parseFloat(debit - credit);
	document.getElementById("billDetailsTableNetFinal[0].creditAmountDetail").value = resultNum
			.toFixed(2);
}
