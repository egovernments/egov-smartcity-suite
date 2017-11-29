
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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/receiptinstrument.js?rnd=${app_release_no}"></script>
<style type="text/css">
#bankcodescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#bankcodescontainer .yui-ac-content {
	position: absolute;
	width: 350px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#bankcodescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#bankcodescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#bankcodescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#bankcodescontainer li.yui-ac-highlight {
	background: #ff0;
}

#bankcodescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
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
	   //validate if cheque/DD number has been entered
	    if(getControlInBranch(table.rows[j],'instrumentChequeNumber')!=null){
	    	var instrNo=getControlInBranch(table.rows[j],'instrumentChequeNumber').value;
	    	instrNo = trimAll(instrNo);
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
		if(getControlInBranch(chequetable.rows[3],'addchequerow')!=null)
			getControlInBranch(chequetable.rows[3],'addchequerow').style.display="block";
		if(getControlInBranch(chequetable.rows[3],'deletechequerow')!=null)
			getControlInBranch(chequetable.rows[3],'deletechequerow').style.display="none";
		
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
<title><s:text name="challan.pagetitle" /></title>
</head>
<body onload="onBodyLoad();">

	<div class="errorstyle" id="challan_error_area" style="display: none;"></div>


	<span align="center" style="display: none" id="invaliddateformat">
		<li><font size="2" color="red"><b> <s:text
						name="common.dateformat.errormessage" />
			</b></font></li>
	</span>

	<span align="center" style="display: none" id="delerror">
		<li><font size="2" color="red"><b><s:text
						name="common.lastrow.error" /></b></font></li>
	</span>

	<s:if test="%{hasErrors()}">
		<div id="actionErrors" class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div id="actionMessages" class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form theme="simple" name="challanReceiptForm">
		<s:token />
		<s:push value="model">

			<s:hidden label="cashAllowed" id="cashAllowed" value="%{cashAllowed}"
				name="cashAllowed" />
			<s:hidden label="cardAllowed" id="cardAllowed" value="%{cardAllowed}"
				name="cardAllowed" />
			<s:hidden label="chequeAllowed" id="chequeAllowed"
				value="%{chequeAllowed}" name="chequeAllowed" />
			<s:hidden label="bankAllowed" id="bankAllowed" value="%{bankAllowed}"
				name="bankAllowed" />
			<s:hidden label="ddAllowed" id="ddAllowed" value="%{ddAllowed}"
				name="ddAllowed" />
			<s:hidden label="onlineAllowed" id="onlineAllowed" value="%{onlineAllowed}" name="onlineAllowed"/>
				
			<s:hidden id="receiptId" name="receiptId" value='%{model.id}' />

			<div class="subheadnew">
				<s:text name="challan.title.createReceipt" />
			</div>

			<div class="formmainbox">


				<s:if test="%{sourcePage!='cancelReceipt'}">
					<div class="subheadsmallnew">
						<span class="subheadnew"><s:text
								name="challan.title.findChallan" /></span>
					</div>
					<div class="boxnew">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td class="bluebox" style="text-align: center;"><s:text
										name="challan.challanNumber" /> &nbsp;&nbsp; <s:textfield
										name="challanNumber" id="challanNumber"
										value="%{challanNumber}" onblur="getReceipt();" /></td>
							</tr>
							<tr>
								<td class="bluebox" style="text-align: center;"><b>OR</b></td>
							</tr>
							<tr>
								<td class="bluebox" style="text-align: center;"><input
									name="button32" type="button" class="button" id="searchChallan"
									value="Search" onclick="searchReceipt()" /></td>
							</tr>
						</table>
						<div class="highlight2">
							<s:text name="challan.findchallan.message" />
						</div>
					</div>

				</s:if>
				<s:if
					test='%{model.id!=null && model.status.code=="PENDING" && model.challan.status.code=="VALIDATED"}'>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<!-- main table -->
						<tr>
							<td><%@ include file='challandetails.jsp'%>
							</td>
						</tr>
						<div>
							<tr>
								<td>
									<div class="subheadnew">
										<span class="subheadsmallnew"><s:text
												name="challan.receipt.title.createReceipt" /></span>
									</div>
								</td>
							</tr>
							<tr>
								<div class="billhead2" align="center">
									<td class="bluebox"><s:text name="viewReceipt.receiptdate" /><span
										class="mandatory" /> <s:date name="receiptdate"
											var="cdFormat" format="dd/MM/yyyy" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <s:textfield id="receiptdate"
											name="receiptdate" value="%{cdFormat}"
											data-inputmask="'mask': 'd/m/y'" /></td>
								</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="subheadsmallnew">
										<s:text name="challan.receipt.payment.details" />
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="billhead2">
										<s:text name="billreceipt.payment.totalamt.tobereceived" />
										<span class="bold"> <input
											style="border: 0px; background-color: #FFFFCC; font-weight: bold;"
											type="label" name="totalamounttobepaid"
											id="totalamounttobepaid" readonly="readonly"
											value='<s:property value="%{totalAmount}" />'>
										</span>
										<s:text name="billreceipt.payment.totalamt.received" />
										<span> <input
											style="border: 0px; background-color: #FFFFCC; font-weight: bold;"
											type="label" name="totalamountdisplay"
											id="totalamountdisplay" readonly="readonly">
										</span>
									</div> <s:hidden label="totalAmountToBeCollected"
										name="totalAmountToBeCollected"
										value="%{totalAmountToBeCollected}" />
								</td>
							</tr>
							<tr>
								<td><div class="subheadsmallnew"></div></td>
							</tr>
							<tr>
								<td>

									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tablebottom">
										<div class="errorstyle" id="challan_dateerror_area"
											style="display: none;"></div>
										<div class="errorstyle" id="error_area" style="display: none;"></div>
										<%@include file="receipt-instrumentdetails.jsp"%>
									</table> <!-- End of mode of payments table -->
								</td>
							</tr>
						</div>
					</table>
					<!--  main table ends -->
					<div align="left" class="mandatorycoll">* Mandatory Fields</div>
					<!-- </div> -->
					<!--  supposed to end of div tag for formmainbox -->

					<div id="loadingMask"
						style="display: none; overflow: hidden; text-align: center">
						<img src="/collection/resources/images/bar_loader.gif" /> <span
							style="color: red">Please wait....</span>
					</div>

					<div class="buttonbottom" align="center">
						<label><input align="center" type="button"
							class="buttonsubmit" id="button2" value="Pay"
							onclick="return validate();" /></label> &nbsp;
						<!--     <s:submit type="submit" cssClass="buttonsubmit" id="button" value="Reset" method="resetChallanReceipt" /> -->
						<input name="button" type="button" class="button" id="button"
							value="Reset" onclick="checkreset();" /> &nbsp; <input
							name="button" type="button" class="button" id="button"
							value="Close" onclick="window.close();" />
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
			<script
				src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
			<script>
jQuery(":input").inputmask();
</script>
		</s:push>
	</s:form>
</body>
