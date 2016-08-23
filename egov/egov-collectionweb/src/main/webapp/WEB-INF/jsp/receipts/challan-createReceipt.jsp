
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<head>
<style type="text/css">
#bankcodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#bankcodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#bankcodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#bankcodescontainer ul {padding:5px 0;width:100%;}
	#bankcodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#bankcodescontainer li.yui-ac-highlight {background:#ff0;}
	#bankcodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<script type="text/javascript">
jQuery.noConflict();
jQuery(document).ready(function() {
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();


     jQuery("#instrumentDate").datepicker({ 
     	 format: 'dd/mm/yyyy',
     	 autoclose:true,
         onRender: function(date) {
      	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
      	  }
      }).on('changeDate', function(ev) {
     	  var string=jQuery(this).val();
     	  if(!(string.indexOf("_") > -1)){
     		  isDatepickerOpened=false; 
           	  checkForCurrentDate(this);
     	  }
     	  
      }).data('datepicker');
     
     var nowTemp = new Date();
     var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

      jQuery( "#challanDate").datepicker({ 
     	 format: 'dd/mm/yyyy',
     	 endDate: nowTemp, 
     	 autoclose:true,
         onRender: function(date) {
      	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
      	  }
       }).on('changeDate', function(ev) {
     	  var string=jQuery(this).val();
     	  if(!(string.indexOf("_") > -1)){
     		  isDatepickerOpened=false; 
     	  }
       }).data('datepicker');
      
      jQuery( "#receiptdate").datepicker({ 
      	 format: 'dd/mm/yyyy',
      	 endDate: nowTemp, 
      	 autoclose:true,
          onRender: function(date) {
       	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
       	  }
        }).on('changeDate', function(ev) {
      	  var string=jQuery(this).val();
      	  if(!(string.indexOf("_") > -1)){
      		  isDatepickerOpened=false; 
      	  }
        }).data('datepicker');
 });



jQuery(window).load(function () {
	undoLoadingMask();
});

function getReceipt()
{
    document.challanReceiptForm.action="challan-createReceipt.action";
	document.challanReceiptForm.submit();
}

function searchReceipt()
{
	window.open('${pageContext.request.contextPath}/receipts/searchChallan.action','SearchChallan','resizable=yes,scrollbars=yes,left=20,top=40, width=700, height=550');
}

function showInstrumentDetails(obj){
	if(obj.id=='cashradiobutton'){
		document.getElementById('cashdetails').style.display='block';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="cash";
		clearCardDetails();
		clearChequeDDDetails();
	}
	else  if(obj.id=='chequeradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='block';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="";
		clearCashDetails();
		clearCardDetails();
	}
	else if(obj.id=='cardradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='block';
		document.getElementById('instrumentTypeCashOrCard').value="card";
		clearCashDetails();
		clearChequeDDDetails();
	}
}

function clearCashDetails(){
	dom.get('instrHeaderCash.instrumentAmount').value="";
	
	dom.get("totalamountdisplay").value="";
}

function clearChequeDDDetails(){
	var table=document.getElementById('chequegrid');
	var len=table.rows.length;
	
	dom.get("totalamountdisplay").value="";
	
	for(var j=0;j<len;j++)
	{
	    //clear instrument type
	    if(getControlInBranch(table.rows[j],'instrumentType')!=null){
	    	getControlInBranch(table.rows[j],'instrumentType').value="";
	    }
	    
	    //deselect dd checkbox  
	    if(getControlInBranch(table.rows[j],'instrumenttypedd')!=null){
	    	getControlInBranch(table.rows[j],'instrumenttypedd').checked=false;
    }
	    
	    //deselect cheque checkbox  
	    if(getControlInBranch(table.rows[j],'instrumenttypecheque')!=null){
	    	getControlInBranch(table.rows[j],'instrumenttypecheque').checked=false;
	    }

	    //clear instrument number
	    if(getControlInBranch(table.rows[j],'instrumentChequeNumber')!=null){
	    	getControlInBranch(table.rows[j],'instrumentChequeNumber').value="";
	    }
	    //clear bank name 
	    if(getControlInBranch(table.rows[j],'bankID')!=null){
	    	getControlInBranch(table.rows[j],'bankID').value="-1";
	    }
	    
	   if(getControlInBranch(table.rows[j],'bankName')!=null){
	    	getControlInBranch(table.rows[j],'bankName').value="";
	    }

	    //clear date
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    	getControlInBranch(table.rows[j],'instrumentDate').value="";
	    }

	    //clear instrument amount
	    if(getControlInBranch(table.rows[j],'instrumentChequeAmount')!=null){
	    	getControlInBranch(table.rows[j],'instrumentChequeAmount').value="";
	    }
	    
	    //clear branch name 
	    if(getControlInBranch(table.rows[j],'instrumentBranchName')!=null){
	    	getControlInBranch(table.rows[j],'instrumentBranchName').value="";
	    }
	}
	
	for(var z=5;z<len;z++)
	{
		table.deleteRow(5);
	}
}

function clearCardDetails(){
	dom.get('instrHeaderCard.instrumentAmount').value="";
	dom.get('instrHeaderCard.transactionNumber').value="";
	dom.get('instrHeaderCard.instrumentNumber').value="";
	
	dom.get("totalamountdisplay").value="";
}
<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
	var currDate = "${currDate}";

// 'chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow'
function addChequeGrid(tableId,trId1,trId2,trId3,trId4,obj,trId5)
{
	document.getElementById("challan_error_area").innerHTML="";   
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	 
    if(!verifyChequeDetails(chequetable,chequetablelen1)){
    	instrAmountInvalidErrMsg='<s:text name="billreceipt.missingchequeamount.errormessage" />' + '<br>';
    	document.getElementById("challan_error_area").innerHTML='Please Enter Mandatory Cheque/DD Details Before Adding A New Cheque/DD';  
    	dom.get("challan_error_area").style.display="block";
    }
    
    else{
   		 //To add rows to the cheque grid table
		addtablerow(tableId,trId1,trId2,trId3,trId4,trId5);
		var tbl = dom.get(tableId);
		var rowNumber=getRow(obj).rowIndex;
		var newtablelength=tbl.rows.length;
		
		var count=document.forms[0].instrumentCount.value;
		count=eval(count)+1;
		document.forms[0].instrumentCount.value=count;
		document.forms[0].instrumentChequeNumber[0].value="";
		document.forms[0].instrumentDate[0].value="";
		document.forms[0].instrumentBranchName[0].value="";
		document.forms[0].bankName[0].value="";
		document.forms[0].instrumentChequeAmount[0].value="";
		document.forms[0].bankID[0].value="-1";
		document.forms[0].bankID[0].name="instrumentProxyList["+count+"].bankId.id";
		document.forms[0].bankName[0].name="instrumentProxyList["+count+"].bankId.name";
		document.forms[0].instrumentChequeAmount[0].name="instrumentProxyList["+count+"].instrumentAmount";
		document.forms[0].instrumentBranchName[0].name="instrumentProxyList["+count+"].bankBranchName";
		document.forms[0].instrumentChequeNumber[0].name="instrumentProxyList["+count+"].instrumentNumber";
		document.forms[0].instrumentDate[0].name="instrumentProxyList["+count+"].instrumentDate";
		//document.forms[0].instrumentChequeAmount[0].name="instrumentProxyList["+count+"].instrumentDate";
	
		getControlInBranch(tbl.rows[rowNumber],'addchequerow').style.display="block";
		getControlInBranch(tbl.rows[newtablelength-1],'addchequerow').style.display="none";
		getControlInBranch(tbl.rows[newtablelength-1],'deletechequerow').style.display="block";
		
		getControlInBranch(tbl.rows[newtablelength-4],'instrumentChequeNumber').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-4],'instrumentDate').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'bankName').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'instrumentBranchName').readOnly="true";		
		getControlInBranch(tbl.rows[newtablelength-2],'instrumentChequeAmount').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'bankID').readOnly="true";
		
		if(document.forms[0].instrumenttypecheque[0].checked==true)
		{
			getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').checked=true;
		}
		else if(document.forms[0].instrumenttypedd[0].checked==true)
		{
			getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').checked=true;
		}
		getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=false;
		getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=false;
		document.forms[0].instrumentType[0].value="";
		document.forms[0].instrumentType[0].name="instrumentProxyList["+count+"].instrumentType.type";
	}
}

function addtablerow(tableId,trId1,trId2,trId3,trId4,trId5){
	var tbl = dom.get(tableId);
		var rowObj1 = dom.get(trId1).cloneNode(true);
		var rowObj2 = dom.get(trId2).cloneNode(true);
		var rowObj3 = dom.get(trId3).cloneNode(true);
		var rowObj4 = dom.get(trId4).cloneNode(true);
		var rowObj5 = dom.get(trId5).cloneNode(true);
		addRow(tbl,rowObj1);
		addRow(tbl,rowObj2);
		addRow(tbl,rowObj3);
		addRow(tbl,rowObj4);
		addRow(tbl,rowObj5);
		dom.get("delerror").style.display="none";
}


function addRow(tableObj,rowObj)
{
	var tbody=tableObj.tBodies[0];
	tbody.appendChild(rowObj);
}

function deleteChequeObj(obj,tableId)
{
	var tbl = dom.get(tableId);
	var rowNumber=getRow(obj).rowIndex; 
	if(tbl.rows.length==6)
	{
	   dom.get("delerror").style.display="block";
	}
	else
	{
		dom.get("delerror").style.display="none";
		tbl.deleteRow(rowNumber);
		tbl.deleteRow(rowNumber-1);
		tbl.deleteRow(rowNumber-2);
		tbl.deleteRow(rowNumber-3);
		tbl.deleteRow(rowNumber-4);
	}
	setChequeInstrumentDetails();
	var tbl = dom.get(tableId);
	var rowNumber=getRow(obj).rowIndex;
	var newtablelength=tbl.rows.length;
	var countUI=document.forms[0].instrumentCount.value;
	var count=document.forms[0].instrumentCount.value;
	for(var j=newtablelength;j>0;j=j-5){
		count=eval(count)-1;
		getControlInBranch(tbl.rows[j-5],'instrumentType').name="instrumentProxyList["+count+"].instrumentType.type";
		getControlInBranch(tbl.rows[j-4],'instrumentChequeNumber').name="instrumentProxyList["+count+"].instrumentNumber";
		getControlInBranch(tbl.rows[j-4],'instrumentDate').name="instrumentProxyList["+count+"].instrumentDate";
		getControlInBranch(tbl.rows[j-3],'instrumentBranchName').name="instrumentProxyList["+count+"].bankBranchName";
		getControlInBranch(tbl.rows[j-2],'instrumentChequeAmount').name="instrumentProxyList["+count+"].instrumentAmount";
		getControlInBranch(tbl.rows[j-3],'bankName').name="instrumentProxyList["+count+"].bankId.name";
		getControlInBranch(tbl.rows[j-3],'bankID').name="instrumentProxyList["+count+"].bankId.id";
		}
	count=eval(countUI)-1;
	document.forms[0].instrumentCount.value=count;
}

function process(date){
	   var parts = date.split("/");
	   return new Date(parts[2], parts[1] - 1, parts[0]);
	}

function verifyChequeDetails(table,len1)
{
	var check=true;

	var instrTypeErrMsg = "";
	var instrNoErrMsg = "";
	var bankNameErrMsg = "";
	var instrAmountErrMsg = "";
	var instrDateErrMsg = "";
	var instrumentType = "";
	var instrAmountErrMsg = "";
	var instrAmountInvalidErrMsg ="";
	for(var j=0;j<len1;j++)
	{
	    //validate if one of the instrument types - cheque/DD has been ticked
	    if(getControlInBranch(table.rows[j],'instrumentType')!=null){
	    	instrumentType=getControlInBranch(table.rows[j],'instrumentType').value;
	    	if(instrumentType==null || instrumentType==""){
	    		if(instrTypeErrMsg==""){
	    		    instrTypeErrMsg='<s:text name="billreceipt.selectinstrumenttype.errormessage"/>' + '<br>';
	    			document.getElementById("challan_error_area").innerHTML+=instrTypeErrMsg;
	    		}
	    		check=false;
	    	}
	    }

	    //validate if cheque/DD number has been entered
	    if(getControlInBranch(table.rows[j],'instrumentChequeNumber')!=null){
	    	var instrNo=getControlInBranch(table.rows[j],'instrumentChequeNumber').value;
	    	if(instrNo==null || instrNo=="" || isNaN(instrNo) || instrNo.length!=6){
	    		if(instrNoErrMsg==""){
	    		    instrNoErrMsg='<s:text name="billreceipt.invalidchequenumber.errormessage" />' + '<br>';
	    			document.getElementById("challan_error_area").innerHTML+=instrNoErrMsg;
	    		}
	    		check=false;
	    	}
	    }

	    //validate if bank name has been entered
	    if(getControlInBranch(table.rows[j],'bankName')!=null){
	    	var bankName=getControlInBranch(table.rows[j],'bankName').value;
	    	if(bankName==null || bankName==""){
	    		if(bankNameErrMsg==""){
	    		    bankNameErrMsg='<s:text name="billreceipt.missingbankid.errormessage" />' + '<br>';
	    			document.getElementById("challan_error_area").innerHTML+=bankNameErrMsg;
	    		}
	    		check=false;
	    	}
	    }

	    //validate if valid date has been entered
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    	var instrDate=getControlInBranch(table.rows[j],'instrumentDate');
	    	if(instrDate.value==null || instrDate.value=="" || instrDate.value=="DD/MM/YYYY"){
	    		if(instrDateErrMsg==""){
	    		    instrDateErrMsg='<s:text name="billreceipt.missingchequedate.errormessage" />' + '<br>';
	    			document.getElementById("challan_error_area").innerHTML+=instrDateErrMsg;
	    		}
	    		check=false;
	    	}else {
	    		var receiptDate = document.getElementById("receiptdate").value;
		   	   	 if(receiptDate !=null && receiptDate != '' && instrDate.value != null && instrDate.value!= '' && check==true ){
		   				if(process(instrDate.value) > process(receiptDate)){
		   		    		document.getElementById("challan_error_area").innerHTML+=
		   						'<s:text name="miscreceipt.error.instrumentdate.greaterthan.receiptdate" />'+ '<br>';   	
		   					window.scroll(0,0);
		   					check=false;
		   		 	   	}
		   			}
	    	      checkForCurrentDate(instrDate);
	    	    } 	                 
	    }
	    
	    if(getControlInBranch(table.rows[j],'instrumentChequeAmount')!=null)
		{
				var chequeamount=getControlInBranch(table.rows[j],'instrumentChequeAmount').value;
				if(chequeamount==null || chequeamount=="" || isNaN(chequeamount) || chequeamount<0 || chequeamount.startsWith('+')){
					if(instrAmountInvalidErrMsg==""){
						instrAmountInvalidErrMsg='<s:text name="billreceipt.invalidchequeamount.errormessage" />' + '<br>';
	    				document.getElementById("challan_error_area").innerHTML+=instrAmountInvalidErrMsg;
	    				check=false;
	    			}
				}
				else{
					chequeamount=eval(chequeamount);
					if(chequeamount==0){
	    				if(instrAmountErrMsg==""){
	    		    		instrAmountErrMsg='<s:text name="billreceipt.missingchequeamount.errormessage" />' + '<br>';
	    					document.getElementById("challan_error_area").innerHTML+=instrAmountErrMsg;
	    					check=false;
	    				}
	    			}
	    		}
		}
	}
	return check;
} // end of function verifychecqueDetails


function clearPaytModes(){
	//deselect all payt mode radio buttons
	document.getElementById('cashradiobuttonspan').style.display="none";
	document.getElementById('cashdetails').style.display="none";
	
	document.getElementById('cardradiobuttonspan').style.display="none";
	document.getElementById('carddetails').style.display="none";
	
	document.getElementById('chequeradiobuttonspan').style.display="none";
	document.getElementById('chequeDDdetails').style.display="none";
}

// This function is called to display the payt modes at the time of body load and 
// at the time of reset
function displayPaytModes(){
       var cashAllowed=document.getElementById("cashAllowed").value;
       var cardAllowed=document.getElementById("cardAllowed").value;
       var chequeDDAllowed=document.getElementById("chequeDDAllowed").value;
       
	   clearPaytModes();
	   
       if(cashAllowed=='true'){
       		//display cash radio button, set it as checked and display cash details
       		document.getElementById('cashradiobuttonspan').style.display="block";

       		document.getElementById('cashradiobutton').checked=true;
       		document.getElementById('cashdetails').style.display='block';
			document.getElementById('instrumentTypeCashOrCard').value="cash";
       }
       else{
            // do not display cash details
       		document.getElementById('cashradiobuttonspan').style.display="none";
       		document.getElementById('cashdetails').style.display='none';
       }
       if(cardAllowed=='true'){
            //display card radio button
       		document.getElementById('cardradiobuttonspan').style.display="block";
       }
       else{
       		//do not display card radio button
       		document.getElementById('cardradiobuttonspan').style.display="none";
       }
       if(chequeDDAllowed=='true'){
       		//display cheque DD radio button
       		document.getElementById('chequeradiobuttonspan').style.display="block";
       }
       else{
       		//do not display cheque/DD radio button
       		document.getElementById('chequeradiobuttonspan').style.display="none";
       }
       //if cash is not allowed and cheque is allowed, set cheque as the default payt
       if(chequeDDAllowed=='true' && cashAllowed=='false'){
       		document.getElementById('chequeradiobutton').checked=true;
       		document.getElementById('chequeDDdetails').style.display='block';
       		document.getElementById('instrumentTypeCashOrCard').value="";
       }
       //if cash and cheque/DD are both not allowed and card is allowed, set card as the default payt
       if(cardAllowed=='true' && cashAllowed=='false' && chequeDDAllowed=='false'){
       		document.getElementById('cardradiobutton').checked=true;
       		document.getElementById('carddetails').style.display='block';
       		document.getElementById('instrumentTypeCashOrCard').value="card";
       }
}


function checkForCurrentDate(obj)
{
   if(validateDateFormat(obj))
   {
	   //trim(obj,obj.value);
	   dom.get("challan_dateerror_area").style.display="none";
	   document.getElementById("challan_dateerror_area").innerHTML="";
	   if(obj.value!="")
	   if(!validatedays(obj.value,document.getElementById('receiptdate').value))
	   {
	       dom.get("challan_dateerror_area").style.display="block";
	       document.getElementById("challan_dateerror_area").innerHTML+=
					'<s:text name="billreceipt.datelessthanreceiptdate.errormessage" />'+ '<br>';
	       return false;
	   }
   }
}

function onBodyLoad()
{
	<s:if test='%{model.id!=null && model.status.code=="PENDING" && model.challan.status.code=="VALIDATED"}'>
		loadDropDownCodesBank();
	
		// To hide delete button in cheque grid on page load
		var chequetable=document.getElementById('chequegrid');
		if(getControlInBranch(chequetable.rows[4],'addchequerow')!=null)
			getControlInBranch(chequetable.rows[4],'addchequerow').style.display="block";
		if(getControlInBranch(chequetable.rows[4],'deletechequerow')!=null)
			getControlInBranch(chequetable.rows[4],'deletechequerow').style.display="none";
		
		if(document.getElementById('instrHeaderCash.instrumentAmount').value==""){
			document.getElementById('instrHeaderCash.instrumentAmount').value="";
		}
		if(document.getElementById('instrHeaderCard.instrumentAmount').value==""){
			document.getElementById('instrHeaderCard.instrumentAmount').value="";
		}
		if(document.getElementById('instrumentChequeAmount').value==""){
			document.getElementById('instrumentChequeAmount').value="";
		}
		if(document.getElementById('challanDate').value!=""){
			document.getElementById("challanDate").disabled=true;
		}
		displayPaytModes();
		displayPaymentDetails();
		loadchequedetails();
	</s:if>
}

function displayPaymentDetails(){

	if(dom.get("instrHeaderCard.instrumentAmount")!=null && dom.get("instrHeaderCard.instrumentAmount").value!=""){
		document.getElementById('cardradiobutton').checked=true;
		document.getElementById('carddetails').style.display='block';
       		document.getElementById('instrumentTypeCashOrCard').value="card";
       		document.getElementById('cashdetails').style.display="none";
	}
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	for(var m=0;m<chequetablelen1;m++)
	{
		var chequeAmt=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount');
		if(chequeAmt!=null && chequeAmt.value!="")
		{
			document.getElementById('chequeradiobutton').checked=true;
			document.getElementById('chequeDDdetails').style.display='block';
    		document.getElementById('instrumentTypeCashOrCard').value="";
       		document.getElementById('cashdetails').style.display="none";
	    }
	}
		
//	loadchequegrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow','chequeaddrow');	 
}


function loadchequedetails(){
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	var tbl = dom.get("chequegrid");
	
	for(var j=chequetablelen1;j>5;j=j-5){
		if(chequetablelen1>5){
		getControlInBranch(tbl.rows[j-1],'deletechequerow').style.display="block";
		}
	}
	for(var j=chequetablelen1;j>=5;j=j-5){
		
		if(getControlInBranch(tbl.rows[j-5],'instrumentType').value=="cheque"){
			getControlInBranch(tbl.rows[j-5],'instrumenttypecheque').checked=true;
		}
		else if(getControlInBranch(tbl.rows[j-5],'instrumentType').value=="dd"){
			getControlInBranch(tbl.rows[j-5],'instrumenttypedd').checked=true;
		}
	}
}

function loadchequegrid(tableId,trId1,trId2,trId3,trId4,trId5){
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	var chqAmtArray=new Array();
	var chqNumberArray=new Array();
	var bankIdArray=new Array();
	var chqDateArray=new Array();
	var bankBranchArray=new Array();
	var bankNameArray=new Array();
	var instrumentTypeArray=new Array();
	for(var m=0;m<chequetablelen1;m++)
	{
		var chequeAmt=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount');
		var chequeNo=getControlInBranch(chequetable.rows[m],'instrumentChequeNumber');
		var chequeDate=getControlInBranch(chequetable.rows[m],'instrumentDate');
		var bankName=getControlInBranch(chequetable.rows[m],'bankName');
		var bankId=getControlInBranch(chequetable.rows[m],'bankID');
		var bankBranch=getControlInBranch(chequetable.rows[m],'instrumentBranchName');
		var instrumentType=getControlInBranch(chequetable.rows[m],'instrumentType');
		
		if(chequeNo!=null&&chequeNo.value!=""){
			chqNumberArray=chequeNo.value.split(',');
		}
		if(chequeAmt!=null&&chequeAmt.value!=""){
			chqAmtArray=chequeAmt.value.split(',');
		}
		if(chequeDate!=null&&chequeDate.value!="DD/MM/YYYY"){
			chqDateArray=chequeDate.value.split(',');
		}
		if(bankId!=null&&bankId.value!=""){
			bankIdArray=bankId.value.split(',');
		}
		if(bankBranch!=null&&bankBranch.value!=""){
			bankBranchArray=bankBranch.value.split(',');
		}
		if(bankName!=null&&bankName.value!=""){
			bankNameArray=bankName.value.split(',');
		}
		if(instrumentType!=null&&instrumentType.value!=""){
			instrumentTypeArray=instrumentType.value.split(',');
		}
	}
				
	if(chqAmtArray.length>1){
		document.forms[0].instrumentChequeAmount.value=chqAmtArray[0];
		document.forms[0].instrumentChequeNumber.value=chqNumberArray[0];//.trim();
		document.forms[0].instrumentDate.value=chqDateArray[0];
		document.forms[0].instrumentBranchName.value=bankBranchArray[0];
		document.forms[0].bankName.value=bankNameArray[0];
		document.forms[0].bankID.value=bankIdArray[0];
		document.forms[0].instrumentType.value=instrumentTypeArray[0];
		
		if(instrumentTypeArray[0].trim()=="dd"){
			document.forms[0].instrumenttypedd.checked=true;
		}
		else if(instrumentTypeArray[0].trim()=="cheque"){
			document.forms[0].instrumenttypecheque.checked=true;
		}
		for(var k=1;k<chqAmtArray.length;k++){
			addtablerow(tableId,trId1,trId2,trId3,trId4,trId5);
			var tbl = dom.get(tableId);
			var rowNumber=4;
			var newtablelength=tbl.rows.length;
	
			document.forms[0].instrumentChequeNumber[0].value=chqNumberArray[k].trim();
			document.forms[0].instrumentDate[0].value=chqDateArray[k];
			document.forms[0].instrumentBranchName[0].value=bankBranchArray[k];
			document.forms[0].instrumentBankName[0].value=bankNameArray[k];
			document.forms[0].instrumentBankId[0].value=bankIdArray[k];
			document.forms[0].instrumentChequeAmount[0].value=chqAmtArray[k];
	
			getControlInBranch(tbl.rows[rowNumber],'addchequerow').style.display="block";
			getControlInBranch(tbl.rows[newtablelength-1],'addchequerow').style.display="none";
			getControlInBranch(tbl.rows[newtablelength-1],'deletechequerow').style.display="block";
			getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=false;
			getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=false;
			if(instrumentTypeArray[k].trim()=="cheque")
			{
				getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=true;
			}
			else if(instrumentTypeArray[k].trim()=="dd")
			{
				getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=true;
			}
			document.forms[0].instrumentType[0].value=instrumentTypeArray[k];
							
		}
	}
			
}

function setinstrumenttypevalue(obj)
{
	var currRow=getRow(obj);
	if(obj.value=="cheque")
	{
		getControlInBranch(currRow,'instrumenttypedd').checked=false;
		getControlInBranch(currRow,'instrumentType').value=obj.value;

	}
	else if(obj.value=="dd")
	{
		getControlInBranch(currRow,'instrumenttypecheque').checked=false;
		getControlInBranch(currRow,'instrumentType').value=obj.value;
	}
	if(getControlInBranch(currRow,'instrumenttypedd').checked==false && getControlInBranch(currRow,'instrumenttypecheque').checked==false)
	{
		getControlInBranch(currRow,'instrumentType').value="";
	}

}

function checkreset()
{
	document.forms[0].reset();
	dom.get("delerror").style.display="none";
	dom.get("invaliddateformat").style.display="none";
	dom.get("challan_dateerror_area").style.display="none";
	dom.get("challan_error_area").style.display="none";
	
	clearCashDetails();
	clearCardDetails();
	clearChequeDDDetails();
	displayPaytModes();
	var paidby =  '<s:property value="%{payeeName}" escapeJavaScript="true"/>';
	paidby = paidby.replace('&amp;','&');
	document.getElementById('paidBy').value=paidby;
	<s:if test="%{isBillSourcemisc()}"> 
		//To load the account codes if only a misc receipt request
		if(resetMisc){
			resetMisc();
		}
	</s:if>
}

function setCashInstrumentDetails(elem){
     dom.get("instrHeaderCash.instrumentAmount").value=elem.value;
     dom.get("instrumentTypeCashOrCard").value="cash";
     var amt=dom.get("instrHeaderCash.instrumentAmount").value;
	if(amt!="" && !(isNaN(amt))){
		amt=eval(amt);
		amt=amt.toFixed(2);	
	}
     dom.get("totalamountdisplay").value=amt;
}

function setCardInstrumentDetails(elem){
     dom.get("instrHeaderCard.instrumentAmount").value=elem.value;
     dom.get("instrumentTypeCashOrCard").value="card";
      var amt=dom.get("instrHeaderCard.instrumentAmount").value;
	if(amt!="" && !(isNaN(amt))){
		amt=eval(amt);
		amt=amt.toFixed(2);	
	}
     dom.get("totalamountdisplay").value=amt;
}

function setChequeInstrumentDetails(){
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	var chequeamount=0;
	var chequeTotal=0;
	for(var m=0;m<chequetablelen1;m++)
	{
		if(getControlInBranch(chequetable.rows[m],'instrumentChequeAmount')!=null)
		{
			chequeamount=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount').value;
			chequeamount=eval(chequeamount);
   			chequeTotal=chequeTotal+chequeamount;
   		}
	}//end of for loop
	dom.get("totalamountdisplay").value=eval(chequeTotal).toFixed(2);	;
}

var bankfuncObj;
var bankArray;
function loadDropDownCodesBank()
{
	var url = "/EGF/voucher/common-ajaxGetAllBankName.action";
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
			bankArray=codes.split("+");
			bankfuncObj= new YAHOO.widget.DS_JSArray(bankArray);
		  }
	   }
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflagBank = new Array();
function autocompletecodeBank(obj,myEvent)
{
	
	var src = obj;	
	var target = document.getElementById('bankcodescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-35;
	target.style.width=450;	
		
	var coaCodeObj=obj;
	var  currRow=getRow(obj);
	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflagBank[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode; 
		if(key != 40 )
		{
			if(key != 38 )
			{ var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'bankcodescontainer', bankfuncObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				bankfuncObj.applyLocalFilter = true;
				bankfuncObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
			}
		}
		yuiflagBank[currRow] = 1;
	}	
}
function fillAfterSplitBank(obj)
{

	var temp = obj.value;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		var currRow=getRow(obj);
		getControlInBranch(currRow,'bankID').value=temp[1];
		getControlInBranch(currRow,'bankName').value=temp[0];
	}
	
}



function validate()
{
    document.getElementById("challan_error_area").innerHTML="";
    dom.get("challan_error_area").style.display="none";
    
	// dom.get("amountoverrideerror").style.display="none";
	dom.get("invaliddateformat").style.display="none";
	dom.get("challan_dateerror_area").style.display="none";
    var	validation = true;
	

	var collectiontotal=0,cashamount=0,chequeamount=0,cardamount=0,billingtotal=0;
 	billingtotal=dom.get("totalamounttobepaid").value; 
 	
	var instrTypeCash = dom.get("cashradiobutton").checked;
	var instrTypeCheque = dom.get("chequeradiobutton").checked;
	var instrTypeCard = dom.get("cardradiobutton").checked;
	var chequetable=document.getElementById('chequegrid')
	var chequetablelen1 =chequetable.rows.length;
	
	//if mode of payment is cash
	if(instrTypeCash){
		if(dom.get("instrHeaderCash.instrumentAmount")!=null)
		{
			cashamount=dom.get("instrHeaderCash.instrumentAmount").value;
			if(cashamount==null || cashamount=="" || isNaN(cashamount) || cashamount<0 || cashamount.startsWith('+')){
				document.getElementById("challan_error_area").innerHTML+=
				'<s:text name="billreceipt.invalidcashamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else
			{
			    cashamount=eval(cashamount);
				if(cashamount==0){
					document.getElementById("challan_error_area").innerHTML+=
					'<s:text name="billreceipt.missingcashamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+cashamount;
			}
		}
	}
	//if mode of payment is card
	if(instrTypeCard){
		if(dom.get("instrHeaderCard.transactionNumber")!=null){
	    	var transNo=dom.get("instrHeaderCard.transactionNumber").value;
		    if(transNo==null || transNo==""){
		    	document.getElementById("challan_error_area").innerHTML+='<s:text name="billreceipt.missingcardtransactionno.errormessage" /> ' + '<br>';
		    	validation=false;
		    }
		}
		if(dom.get("instrHeaderCard.instrumentNumber")!=null){
		    var cardNo=dom.get("instrHeaderCard.instrumentNumber").value;
		    if(cardNo==null || isNaN(cardNo) || cardNo.length < 4){
		    	document.getElementById("challan_error_area").innerHTML+='<s:text name="billreceipt.missingcardno.errormessage" />' + '<br>';
		    	validation=false;
		    }
		}
		if(dom.get("instrHeaderCard.instrumentAmount")!=null){
			cardamount=dom.get("instrHeaderCard.instrumentAmount").value;
			if(cardamount==null || cardamount=="" || isNaN(cardamount) || cardamount<0 || cardamount.startsWith('+')){
				document.getElementById("challan_error_area").innerHTML+='<s:text name="billreceipt.invalidcardamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else{
				cardamount=eval(cardamount);
				if(cardamount==0){
					document.getElementById("challan_error_area").innerHTML+='<s:text name="billreceipt.missingcardamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+cardamount;
			}
		}
	}
	//if mode of payment is cheque/DD
	if(instrTypeCheque){
	    if(!verifyChequeDetails(chequetable,chequetablelen1)){
	         validation=false;
	    }
	    else{
			for(var m=0;m<chequetablelen1;m++)
			{
				if(getControlInBranch(chequetable.rows[m],'instrumentChequeAmount')!=null)
				{
					chequeamount=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount').value;
					chequeamount=eval(chequeamount);
	    			collectiontotal=collectiontotal+chequeamount;
	    		}
			}//end of for loop
		}//end of else
	}
	
	if(collectiontotal!=0){
		if(collectiontotal!=billingtotal)
		{
			document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.wrong.collectionamount.error" />' + '<br>';
			validation=false;
		}
	}
	if(document.getElementById("receiptdate")!=null && document.getElementById("receiptdate").value==""){
		document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.error.receiptdate" />' + '<br>';
		validation=false;
	}
    
	if(validation==false){
		dom.get("challan_error_area").style.display="block";
		window.scroll(0,0);
	}
	if(validation==true){
  //  Validating instrument date for cash payment
		var instrDate = document.getElementById('instrumentDate').value;
    	if(instrDate==null || instrDate==""){
    		document.getElementById('instrumentDate').value="";
    	}

    	doLoadingMask('#loadingMask');
		document.challanReceiptForm.action="challan-saveOrupdate.action";
		document.challanReceiptForm.submit();
	}
	return validation;
}//end of function 'validate'



</script>
<title><s:text name="challan.pagetitle"/>
</title>
</head>
<body onload="onBodyLoad();">

<div class="errorstyle" id="challan_error_area" style="display:none;"></div>


<span align="center" style="display:none" id="invaliddateformat">
  <li>
     <font size="2" color="red"><b>
		<s:text name="common.dateformat.errormessage"/>
	</b></font>
  </li>
</span>

<span align="center" style="display:none" id="delerror">
  <li>
     <font size="2" color="red"><b><s:text name="common.lastrow.error"/></b></font>
  </li>
</span>

<s:if test="%{hasErrors()}">
    <div id="actionErrors" class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<s:if test="%{hasActionMessages()}">
    <div id="actionMessages" class="messagestyle">
    	<s:actionmessage theme="simple"/>
    </div>
</s:if>

<s:form theme="simple" name="challanReceiptForm">
<s:token/>
<s:push value="model">

<s:hidden label="cashAllowed" id="cashAllowed" value="%{cashAllowed}" name="cashAllowed"/>
<s:hidden label="cardAllowed" id="cardAllowed" value="%{cardAllowed}" name="cardAllowed"/>
<s:hidden label="chequeDDAllowed" id="chequeDDAllowed" value="%{chequeDDAllowed}" name="chequeDDAllowed"/>
<s:hidden id="receiptId" name="receiptId" value='%{model.id}'/>

<div class="subheadnew"><s:text name="challan.title.createReceipt"/></div>

<div class="formmainbox">


<s:if test="%{sourcePage!='cancelReceipt'}" >
	<div class="subheadsmallnew"><span class="subheadnew"><s:text name="challan.title.findChallan"/></span></div>
	<div class="boxnew">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
			    <td class="bluebox" style="text-align:center;"><s:text name="challan.challanNumber"/> &nbsp;&nbsp; <s:textfield name="challanNumber" id="challanNumber" value="%{challanNumber}"  onblur="getReceipt();" /></td>
		    </tr>
			<tr>
				<td class="bluebox" style="text-align:center;"><b>OR</b></td>
			</tr>
			<tr>
				<td class="bluebox" style="text-align:center;"><input name="button32" type="button" class="button" id="searchChallan" value="Search" onclick="searchReceipt()"/></td>
			</tr>
		</table>
	 	<div class="highlight2"><s:text name="challan.findchallan.message" /></div>
	</div>

</s:if>
<s:if test='%{model.id!=null && model.status.code=="PENDING" && model.challan.status.code=="VALIDATED"}'>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">   <!-- main table -->
		<tr>
		<td>
		<%@ include file='challandetails.jsp'%>
		</td>
		</tr>
		<div>
 		<tr>
    	<td>
    		<div class="subheadnew">
    		<span class="subheadsmallnew"><s:text name="challan.receipt.title.createReceipt"/></span>
    		</div>
    		</td>
    	</tr>
    	<tr>
    		<div class="billhead2" align="center">
         	<td  class="bluebox"><s:text name="viewReceipt.receiptdate" /><span class="mandatory"/>
                  <s:date name="receiptdate" var="cdFormat" format="dd/MM/yyyy"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <s:textfield id="receiptdate" name="receiptdate" value="%{cdFormat}" data-inputmask="'mask': 'd/m/y'"/></td>
                </div>            
            </td>
          </tr>
    	<tr>
    	<td>
      		<div class="subheadsmallnew"><s:text name="challan.receipt.payment.details"/>  
      		</div>
      	</td>
  		</tr>
  		<tr>
    	<td>
    		<div class="billhead2">
    			<s:text name="billreceipt.payment.totalamt.tobereceived"/>
   				<span class="bold">
   				<input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="label" name="totalamounttobepaid" id="totalamounttobepaid" readonly="readonly" value='<s:property value="%{totalAmount}" />' >
   				</span>
   				<s:text name="billreceipt.payment.totalamt.received"/>
   				<span>
    			<input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="label" name="totalamountdisplay" id="totalamountdisplay" readonly="readonly">
   				</span>
   			</div>
   			 <s:hidden label="totalAmountToBeCollected" name="totalAmountToBeCollected" value="%{totalAmountToBeCollected}"/>
    	</td>
  		</tr>
	  	<tr>
	    	<td><div class="subheadsmallnew"></div></td>
	  	</tr>
   		<tr>
    	<td>
    
     	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
	      <tr>
	        <td class="blueboxnew" width="18%" ><s:text name="billreceipt.payment.mode"/>:<span class="mandatory1">*</span></td>
	        <div class="errorstyle" id="challan_dateerror_area" style="display:none;"></div>
	        <td class="blueboxnew" width="84%" >
        		<table width="100%" border="0" cellspacing="0" cellpadding="0">
            		<tr>
              		<td >
              		<span style="float:left;" id="cashradiobuttonspan">
						<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="cashradiobutton"  name="paytradiobutton"/> Cash&nbsp;&nbsp;
						<s:hidden name="instrumentTypeCashOrCard" id="instrumentTypeCashOrCard" value="cash" />
					</span> 
					<span style="float:left;"  id="chequeradiobuttonspan">
					<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="chequeradiobutton" name="paytradiobutton"/>Cheque/DD&nbsp;&nbsp;
					</span>
					<span style="float:left;" id="cardradiobuttonspan">
					<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="cardradiobutton" name="paytradiobutton"/>Credit  card&nbsp;&nbsp;
                	</span>
                	</td>
            		</tr>
	 	   		</table> <!-- end of radio buttons' inner table -->
          </td>
      	</tr>
      </table><!-- end of radio buttons' outer table -->
      
      <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
		<!--for cash-->
			<tr id="cashdetails" >
			    <td width="1%" class="blueboxnew"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
			    <td class="bluebox" width="6%"><s:textfield label="instrumentAmount" id="instrHeaderCash.instrumentAmount" name="instrHeaderCash.instrumentAmount" maxlength="14" size="18" placeholder="0.0" cssClass="amount" onblur="setCashInstrumentDetails(this);"/></td>
		     </tr>
		<!--for cash-->
		
			
			
			<!--for cheque NEW USING PROXY-->
			<tr id="chequeDDdetails" style="display:none">
		       	<td class="bluebox2cheque" width="11%">
		       		<s:hidden label="instrumentCount" id="instrumentCount" name="instrumentCount"/>
		       		
			   <table width="100%" border="0" cellspacing="0" cellpadding="0" name="chequegrid" id="chequegrid">
		       		<!-- This row contains check boxes to choose between cheque and DD -->
		       		<s:if test="instrumentProxyList==null || instrumentProxyList.size()==0">
		       		<tr id="chequetyperow">
			   			<td class="blueboxcheckbox" width="19%">
			   				<INPUT TYPE="CHECKBOX" NAME="cheque" onclick="setinstrumenttypevalue(this);" value="cheque" id="instrumenttypecheque">Cheque
			   				<INPUT TYPE="CHECKBOX" NAME="dd" onclick="setinstrumenttypevalue(this);" value="dd" id="instrumenttypedd">DD<BR>
			   				<s:hidden label="instrumentType" id="instrumentType" name="instrumentProxyList[0].instrumentType.type"/>
			   			</td>
			       		<td class="bluebox" colspan="3">&nbsp;</td>
					</tr>
					<!-- This row captures the cheque/DD No and the cheque/DD date -->
					<tr id="chequedetailsrow"> 
					    <td class="bluebox2new"><s:text name="billreceipt.payment.chequeddno"/><span class="mandatory1">*</span></td>
					    <td class="bluebox2" width="20%"><s:textfield label="instrumentNumber" id="instrumentChequeNumber" maxlength="6" name="instrumentProxyList[0].instrumentNumber" size="18" /></td>
					    <td class="bluebox2" width="23%"><s:text name="billreceipt.payment.chequedddate"/><span class="mandatory1">*</span></td>
					    <td class="bluebox2"><input type ="text" id="instrumentDate" data-inputmask="'mask': 'd/m/y'" name="instrumentProxyList[0].instrumentDate"  onblur="checkForCurrentDate(this);"  onfocus = "checkForCurrentDate(this);" data-date-end-date="0d"/><div>(DD/MM/YYYY)</div></td>
				    </tr>
				    <!-- This row captures the cheque/DD Bank and Branch names -->
		     		<tr id="chequebankrow">
				       	<td class="blueboxnew"><s:text name="billreceipt.payment.bankname"/><span class="mandatory1">*</span></td>
				       	<td class="bluebox">
					   			<s:textfield id="bankName" type="text" name="instrumentProxyList[0].bankId.name"  onkeyup='autocompletecodeBank(this,event)' onblur='fillAfterSplitBank(this)' />
					   			<s:hidden id="bankID" name="instrumentProxyList[0].bankId.id" />
		   						<div id="bankcodescontainer"></div>
		       			</td>
		       			<td class="bluebox"><s:text name="billreceipt.payment.branchname"/></td>
		       			<td class="bluebox"><s:textfield label="instrumentBranchName" id="instrumentBranchName" maxlength="50" name="instrumentProxyList[0].bankBranchName" size="18" /></td>
		       		</tr>
		       		<!-- This row captures the cheque/DD Amount -->
		       		<tr id="chequeamountrow">
						<td class="bluebox2new"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
						<td class="bluebox2"><s:textfield label="instrumentAmount" id="instrumentChequeAmount" maxlength="14" name="instrumentProxyList[0].instrumentAmount"  placeholder="0.0" size="18"  cssClass="amount" onblur="setChequeInstrumentDetails(this);"/></td>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox2">&nbsp;</td>
					</tr>
					<tr id="chequeaddrow">
						<td colspan="5" class="blueborderfortd4">
							<div id="addchequerow" style="display:none">
								<a href="#" id="addchequelink" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
									<s:text name="billreceipt.payment.add"/></a>
								<img src="../../egi/resources/erp2/images/add.png" id="addchequeimg" alt="Add" width="16" height="16" border="0" align="absmiddle" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')"/>
							</div>
							<div id="deletechequerow" style="display:none">
								<a href="#" id="deletechequelink" onclick="deleteChequeObj(this,'chequegrid','delerror')">
									<s:text name="billreceipt.payment.delete"/></a>
								<img src="../../egi/resources/erp2/images/delete.png" id="deletechequeimg" alt="Delete" width="16" height="16" border="0" align="absmiddle"  onclick="deleteChequeObj(this,'chequegrid','delerror')"/>
							</div>
						</td>
					</tr>
					</s:if>
					<s:else>
					  <s:iterator value="(instrumentProxyList.size).{#this}" status="instrstatus">
					<tr id="chequetyperow">
			   			<td class="blueboxcheckbox" width="19%">
			   				<INPUT TYPE="CHECKBOX" NAME="cheque" onclick="setinstrumenttypevalue(this);" value="cheque" id="instrumenttypecheque">Cheque &nbsp;&nbsp;
			   				<INPUT TYPE="CHECKBOX" NAME="dd" onclick="setinstrumenttypevalue(this);" value="dd" id="instrumenttypedd">DD &nbsp;&nbsp;<BR>
			   				<s:hidden label="instrumentType" id="instrumentType" name="instrumentProxyList[%{#instrstatus.index}].instrumentType.type"/>
			   			</td>
			       		<td class="bluebox" colspan="3">&nbsp;</td>
					</tr>
					<!-- This row captures the cheque/DD No and the cheque/DD date -->
					<tr id="chequedetailsrow"> 
					    <td class="bluebox2new"><s:text name="billreceipt.payment.chequeddno"/><span class="mandatory1">*</span></td>
					    <td class="bluebox2" width="20%"><s:textfield label="instrumentNumber" id="instrumentChequeNumber" maxlength="6" name="instrumentProxyList[%{#instrstatus.index}].instrumentNumber" size="18" /></td>
					    <td class="bluebox2new" width="18%"><s:text name="billreceipt.payment.chequedddate"/><span class="mandatory1">*</span></td>
					    <td class="bluebox2"><input type ="text" id="instrumentDate" name="instrumentProxyList[%{#instrstatus.index}].instrumentDate" data-inputmask="'mask': 'd/m/y'" onblur="checkForCurrentDate(this);"  onfocus = "checkForCurrentDate(this);"/><div>(DD/MM/YYYY)</div></td>
				    </tr>
				    <!-- This row captures the cheque/DD Bank and Branch names -->
		     		<tr id="chequebankrow">
				       	<td class="blueboxnew"><s:text name="billreceipt.payment.bankname"/><span class="mandatory1">*</span></td>
				       	<td class="bluebox">
					   			<s:textfield id="bankName" type="text" name="instrumentProxyList[%{#instrstatus.index}].bankId.name"  onkeyup='autocompletecodeBank(this,event)' onblur='fillAfterSplitBank(this)' />
					   			<s:hidden id="bankID" name="instrumentProxyList[%{#instrstatus.index}].bankId.id" />
		   						<div id="bankcodescontainer"></div>
		       			</td>
		       			<td class="bluebox2new" width="18%"><s:text name="billreceipt.payment.branchname"/></td>
		       			<td class="bluebox"><s:textfield label="instrumentBranchName" id="instrumentBranchName" maxlength="50" name="instrumentProxyList[%{#instrstatus.index}].bankBranchName" size="18" /></td>
		       		</tr>
		       		<!-- This row captures the cheque/DD Amount -->
		       		<tr id="chequeamountrow">
						<td class="bluebox2new"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
						<td class="bluebox2"><s:textfield label="instrumentAmount" id="instrumentChequeAmount" maxlength="14" name="instrumentProxyList[%{#instrstatus.index}].instrumentAmount"  size="18" placeholder="0.0" cssClass="amount" onblur="setChequeInstrumentDetails(this);"/></td>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox2">&nbsp;</td>
					</tr>
					<tr id="chequeaddrow">
						<td colspan="5" class="blueborderfortd4">
							<div id="addchequerow" style="display:none">
								<a href="#" id="addchequelink" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
									<s:text name="billreceipt.payment.add"/></a>
								<img src="../../egi/resources/erp2/images/add.png"  id="addchequeimg" alt="Add" width="16" height="16" border="0" align="absmiddle" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')"/>
							</div>
							<div id="deletechequerow" style="display:none">
								<a href="#" id="deletechequelink" onclick="deleteChequeObj(this,'chequegrid','delerror')">
									<s:text name="billreceipt.payment.delete"/></a>
									<img src="../../egi/resources/erp2/images/delete.png"  id="deletechequeimg" alt="Delete" width="16" height="16" border="0" align="absmiddle"  onclick="deleteChequeObj(this,'chequegrid','delerror')"/>
							</div>
						</td>
					</tr>
					  </s:iterator>
					</s:else>
		       </table>
		       <!-- End of table 'chequegrid' -->
		   	    </td>
			</tr><!--  End of row 'chequeDDdetails' NEW USING PROXY -->
			
			
			
		<!--for cheque-->
		<!--for card-->
			<tr id="carddetails" style="display:none">
		        <td class="bluebox2cheque" width="11%">
		        	<table width="100%" border="0" cellspacing="0" cellpadding="0" name="cardgrid" id="cardgrid">
				  		<tr id="carddetailsrow">
				            <td width="18%" class="blueboxnew"><s:text name="billreceipt.payment.cardno"/><span class="mandatory1">*</span></td>
				            <td class="blueboxnew"><s:textfield label="instrHeaderCard.instrumentNumber" id="instrHeaderCard.instrumentNumber" maxlength="4" name="instrHeaderCard.instrumentNumber" value="%{instrHeaderCard.instrumentNumber}" size="18" /></td>
					        <td class="blueboxnew"><s:text name="billreceipt.payment.transactionnumber"/><span class="mandatory1">*</span></td>
							<td class="blueboxnew"><s:textfield label="instrHeaderCard.transactionNumber" id="instrHeaderCard.transactionNumber" maxlength="14" name="instrHeaderCard.transactionNumber" size="18" value="%{instrHeaderCard.transactionNumber}"/></td>
							<td class="blueboxnew"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
				           <td class="blueboxnew"><s:textfield label="instrHeaderCard.instrumentAmount" id="instrHeaderCard.instrumentAmount" maxlength="14" name="instrHeaderCard.instrumentAmount" size="18"   placeholder="0.0" cssClass="amount" onblur="setCardInstrumentDetails(this);"/></td>
				        </tr>
		            </table> <!-- End of table 'cardgrid' -->
		        </td>
			</tr><!-- End of row 'carddetails' -->
			<!-- for card-->
			</table> <!-- End of mode of payments table -->
     </td></tr>
     </div>
</table> <!--  main table ends -->
<div align="left" class="mandatorycoll">* Mandatory Fields</div>
<!-- </div> --> <!--  supposed to end of div tag for formmainbox -->

 <div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="/collection/resources/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
  	 
<div class="buttonbottom" align="center">
      <label><input align="center" type="button"  class="buttonsubmit" id="button2" value="Pay"  onclick="return validate();"/></label>
      &nbsp;
   <!--     <s:submit type="submit" cssClass="buttonsubmit" id="button" value="Reset" method="resetChallanReceipt" /> -->
      <input name="button" type="button" class="button" id="button" value="Reset" onclick="checkreset();"/> 
       &nbsp;
      <input name="button" type="button" class="button" id="button" value="Close" onclick="window.close();"/>
</div>
</s:if>
</div>


<script type="text/javascript">
// MAIN FUNCTION: new switchcontent("class name", "[optional_element_type_to_scan_for]") REQUIRED
// Call Instance.init() at the very end. REQUIRED
var bobexample=new switchcontent("switchgroup1", "div") //Limit scanning of switch contents to just "div" elements
bobexample.collapsePrevious(true) //Only one content open at any given time
bobexample.init()
</script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script>
jQuery(":input").inputmask();
</script>
</s:push>
</s:form>
</body>
