<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

<title>Disburse Gratuity Report</title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String mode = request.getParameter("mode");
	System.out.println("mode----------"+mode);
	//String autoGenerateChqNo =  EGovConfig.getProperty("payroll_egov_config.xml","AUTOGENERATECHQNO","",EGOVThreadLocals.getDomainName()+".ChequeGeneration");
%>
<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;	
	function onBodyLoad(){  	
	
	  loadEmpCodes(); 
	   <%		
		if(!"search".equals(mode)){
		%>			
			populateBankInfo();
			populateNomineeInfo();			
		<%}%>
	   
	}
	
	function validateNomineeExist(){
		if("${fn:length(gratuityForm.eligibleNomineeSet)}" == "0")
			return false;
		else
			return true;
	}

	function populateBankInfo(){		
		if("${gratuityForm.pensionHeader.disbursementType}" == "dbt"){
			document.getElementById("bankRowId").style.display = "block";
			document.getElementById("bankBranchRowId").style.display = "block";
			callBankBranch(document.gratuityForm.bank);
			document.gratuityForm.bankBranch.value = "${gratuityForm.pensionHeader.idBranch.id}";

		}
	}

	function populateNomineeInfo(){
		if("${gratuityForm.payTo}" == "nominee"){	
			if(!validateNomineeExist()){
				alert("This employee is deceased and there are no eligible nominees for this employee,so gratuity computation being terminated");				
				window.location = "${pageContext.request.contextPath}/pension/search.jsp?mode=create";				
			}
			else{
				document.getElementById("nomineeTable").style.display = "block";
				document.getElementById("nomineeTotalTable").style.display = "block";
				callNominee(document.gratuityForm.payTo);
			}
		}
	}
	
    function autocompleteEmpCode(obj){
	 	// set position of dropdown
	 	var src = obj;
	 	var target = document.getElementById('codescontainer');
	 	var posSrc=findPos(src);
	 	target.style.left=posSrc[0];
	 	target.style.top=posSrc[1]+25;
	 	if(obj.name=='employeeCode') target.style.left=posSrc[0]+0;
	
	 	target.style.width=500;
	
	 	var currRow=getRow(obj);
	 	var coaCodeObj = obj;
	 	if(yuiflag1[currRow.rowIndex] == undefined)
	 	{
	 	//40 --> Down arrow, 38 --> Up arrow
	 	if(event.keyCode != 40 )
	 	{
	 		if(event.keyCode != 38 )
	 		{
 				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
 				oAutoComp1.queryDelay = 0;
 				oAutoComp1.useShadow = true;
 				oAutoComp1.maxResultsDisplayed = 15;
   				oAutoComp1.useIFrame = true;
	 		}
	 	}
	 	yuiflag1[currRow.rowIndex]=1;
	  }
   }
   
   
 	
 	function loadEmpCodes() { 	
		 var type='getAllEmployeeCodes';
			var url = "${pageContext.request.contextPath}/commons/process.jsp?type=" +type+ " ";
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
							empCodeArray=codes.split("+");
							selectedEmpCode = new YAHOO.widget.DS_JSArray(empCodeArray);
						  }
					  }
			};
			req2.open("GET", url, true);
		req2.send(null);
 	} 	
  	

	function callBank(obj){
		document.getElementById("bankRowId").style.display = "none";
		document.getElementById("bankBranchRowId").style.display = "none";
		if(obj.value == "dbt"){
			document.getElementById("bankRowId").style.display = "block";
			document.getElementById("bankBranchRowId").style.display = "block";
			callBankBranch(document.gratuityForm.bank);
			document.gratuityForm.bankBranch.value = "${gratuityForm.pensionHeader.idBranch.id}";
		}
	}
  	
  	function callNominee(obj){			
		document.getElementById("nomineeTable").style.display = "none";
		document.getElementById("nomineeTotalTable").style.display = "none";		
		if(obj.value == "nominee" && validateNomineeExist()){
			document.getElementById("nomineeTable").style.display = "block";
			document.getElementById("nomineeTotalTable").style.display = "block";
			populateNomineeAmount();
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
	

	

	function deleteRow(table,obj){		
		if(table=='nomineeTable'){
			var tbl = document.getElementById(table);
			var rowNumber=getRow(obj).rowIndex;	
			//alert(tbl.rows.length);
		//	alert("{gratuityForm.nomineeName.length);
			if(tbl.rows.length > 3)
			   tbl.deleteRow(rowNumber);
			else{
				alert("You cannot delete this row");
				return false;
			}
		}		
	}

	function addRowToTable(tbl,obj){
		  tableObj=document.getElementById(tbl);
		  var rowObj1=getRow(obj);
		  var tbody=tableObj.tBodies[0];
		  var lastRow = tableObj.rows.length;		 
		  var checkRowLength = eval(getControlInBranch(tableObj.rows[rowObj1.rowIndex],'nomineeName').options.length) + 1;
		  if(tbl=='nomineeTable' && lastRow< checkRowLength){	
			   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
			   tbody.appendChild(rowObj);	
			   var remlen = document.gratuityForm.nomineeAmount.length;			  
			   document.gratuityForm.nomineeAmount[remlen-1].value="";
			   document.gratuityForm.nomineeMonthlyPensionPayable[remlen-1].value="";			   
		  }
		  else
		  {
			  if(tbl=='nomineeTable')
			  {
				alert("No nominee Available to insert");
				return false;
			  }
		  }	 
	}

	function callNomineeAmount(obj){
		var table = document.getElementById("nomineeTable");
		var rowObj=getRow(obj);
		var totalPayment = eval(document.gratuityForm.totalPayment.value);
		var monthlyPensionPayable = eval(document.gratuityForm.monthlyPensionPayable.value); 
	//FIXME : compute amount depending upon list size
		<c:forEach var="eligibleNomineeObj" items="${gratuityForm.eligibleNomineeSet}">
			if("${eligibleNomineeObj.id}" == obj.value ){
				if("${eligibleNomineeObj.nomineeType.nomineeType}" == "WIFE"){										
					getControlInBranch(table.rows[rowObj.rowIndex],'nomineeAmount').value = totalPayment;
					getControlInBranch(table.rows[rowObj.rowIndex],'nomineeMonthlyPensionPayable').value = totalPayment;
				}
				else{
					var noOfNominee = ${fn:length(gratuityForm.eligibleNomineeSet)};
					var nomineeAmount = totalPayment/eval(noOfNominee-1);
					var nomineeMonthlyPensionPayable = monthlyPensionPayable/eval(noOfNominee-1);					
					getControlInBranch(table.rows[rowObj.rowIndex],'nomineeAmount').value = nomineeAmount;
					getControlInBranch(table.rows[rowObj.rowIndex],'nomineeMonthlyPensionPayable').value = nomineeMonthlyPensionPayable;

				}
			}
		</c:forEach>
	}
 
	function populateNomineeAmount(){		
		if(document.gratuityForm.payTo.value == "nominee" && validateNomineeExist()){
			var tbl = document.getElementById("nomineeTable");
			var length = tbl.rows.length		
			var totalPayment = eval(document.gratuityForm.totalPayment.value);			
			var monthlyPensionPayable = eval(document.gratuityForm.monthlyPensionPayable.value); 			
			var gratuityAmount = eval(document.gratuityForm.gratuityAmount.value); 
			document.gratuityForm.total.value = totalPayment;
			document.gratuityForm.totalMonthlyPensionPayable.value = monthlyPensionPayable;
			if(length == 3){			
				document.gratuityForm.nomineeAmount.value = totalPayment;
				if(monthlyPensionPayable != undefined)
					document.gratuityForm.nomineeMonthlyPensionPayable.value = monthlyPensionPayable;
				document.gratuityForm.nomineeGratuityAmount.value = gratuityAmount;
			}
			else{
				//will work only if the eligibleNominee set is correct. That is, if Spouse is a nominee, Son/Daughter should not be returned
				var noOfNominee = ${fn:length(gratuityForm.eligibleNomineeSet)};
				var nomineeAmount = totalPayment/eval(noOfNominee);
				var nomineeMonthlyPensionPayable = monthlyPensionPayable/noOfNominee;
				var nomineeGratuityAmount = gratuityAmount/eval(noOfNominee);
				for(var i=0; i<=length-3; i++){
					document.gratuityForm.nomineeAmount[i].value = nomineeAmount;
					if(monthlyPensionPayable != undefined)
						document.gratuityForm.nomineeMonthlyPensionPayable[i].value = nomineeMonthlyPensionPayable;
					document.gratuityForm.nomineeGratuityAmount[i].value = gratuityAmount;
				}
			}
			callTotalAmount();
		}
	}

	function callTotalAmount(){		
		var sum=0;
		var sum1=0;
		if(document.gratuityForm.nomineeAmount.length>0){
			 for(var i=0;i<document.gratuityForm.nomineeAmount.length;i++){
				if(document.gratuityForm.nomineeAmount[i].value!=""){
					sum = sum+ eval(document.gratuityForm.nomineeAmount[i].value);
				}
				else{
					document.gratuityForm.nomineeAmount[i].value=0;
				}
			 }
	   }
	   else{
		   if(document.gratuityForm.nomineeAmount.value!=""){
			   sum = sum+ eval(document.gratuityForm.nomineeAmount.value);
		   }
		   else{
			   document.gratuityForm.nomineeAmount.value=0;
		   }
	   }	  
	   document.getElementById("total").value=sum;
	   if(document.gratuityForm.nomineeMonthlyPensionPayable.length>0){
			 for(var i=0;i<document.gratuityForm.nomineeMonthlyPensionPayable.length;i++){
				if(document.gratuityForm.nomineeMonthlyPensionPayable[i].value!=""){
					sum1 = sum1+ eval(document.gratuityForm.nomineeMonthlyPensionPayable[i].value);
				}
				else{
					document.gratuityForm.nomineeMonthlyPensionPayable[i].value=0;
				}
			 }
	   }
	   else{
		   if(document.gratuityForm.nomineeMonthlyPensionPayable.value!=""){
			   sum1 = sum1+ eval(document.gratuityForm.nomineeMonthlyPensionPayable.value);
		   }
		   else{
			   document.gratuityForm.nomineeMonthlyPensionPayable.value=0;
		   }
	   }	   
	   document.gratuityForm.totalMonthlyPensionPayable.value=sum1;

    }
	  	
  	

	function checkForPensionHeaderExist(){
		var type = "getPensionHeaderDetails";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var response = req.responseText
					var result = response.split("/");
                   	if(result[0]=="exist"){
                   		isExist = "exist";
					}
                   	else if(result[0]=="notExist"){
         				isExist = "notExist";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);		
		return isExist;
	}
	
	function checkForPensionDetailsByEmp(){
		var type = "getPensionDetailsByEmp";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var response = req.responseText
					var result = response.split("/");
                   	if(result[0]=="exist"){
                   		isExist = "exist";
					}
                   	else if(result[0]=="notExist"){
         				isExist = "notExist";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);		
		return isExist;
	}

	function validateEmployeeGratuityEligibility(){
		alert("validate");
		var type = "validateEmployeeGratuityEligibility";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var response = req.responseText
					var result = response.split("/");
                   	if(result[0]=="true"){
                   		isExist = "ture";
					}
                   	else if(result[0]=="false"){
         				isExist = "false";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);		
		return isExist;
	}
	
	function validateOnSave(){	
		if(document.gratuityForm.voucherNo.value==""){
			alert("Enter voucher no!!!");
			document.gratuityForm.voucherNo.focus();
			return false;
		}		
		if(document.gratuityForm.voucherDate.value==""){
			alert("Enter voucher date!!!");
			document.gratuityForm.voucherDate.focus();
			return false;
		}	
		if(document.gratuityForm.voucherNo.value != "" && document.gratuityForm.voucherDate.value != ""){
			if(checkUniqueVN() == "false"){
				document.gratuityForm.voucherNo.value = "";
				document.gratuityForm.voucherNo.focus();
				return false;
			}
		}
		if(document.gratuityForm.payerBank.value==""){
			alert("Enter payer bank");
			document.gratuityForm.payerBank.focus();
			return false;
		}
		if(document.gratuityForm.payerBankBranch.value==""){
			alert("Enter payer bank branch");
			document.gratuityForm.payerBankBranch.focus();
			return false;
		}
		if(document.gratuityForm.payerBankAccount.value==""){
			alert("Enter payer bank account");
			document.gratuityForm.payerBankAccount.focus();
			return false;
		}		
		<c:if test = "${gratuityForm.autoGenerateChqNO == 'N'}">
			var tbl = document.getElementById('payeeTable').rows.length;
			if(tbl <4){
				if(document.gratuityForm.chequeNo.value==""){
					alert("Enter cheque no");
					document.gratuityForm.chequeNo.focus();
					return false;
				}
				if(document.gratuityForm.chequeDate.value==""){
					alert("Enter cheque date");
					document.gratuityForm.chequeDate.focus();
					return false;
				}
				if(document.gratuityForm.payerBankAccount.value != "" && document.gratuityForm.chequeNo.value != ""){
					if(checkUniqueCheque(document.gratuityForm.chequeNo) == "false"){
						document.gratuityForm.chequeNo.value = "";
						document.gratuityForm.chequeNo.focus();
						return false;
					}
					if(checkInRange(document.gratuityForm.chequeNo) == "false"){
						document.gratuityForm.chequeNo.value = "";
						document.gratuityForm.chequeNo.focus();
						return false;
					}
				}
			}
			else{
				for(var i=0; i<tbl-2 ; i++)	{
					if(document.gratuityForm.chequeNo[i].value==""){
						alert("Enter cheque no");
						document.gratuityForm.chequeNo[i].focus();
						return false;
					}
					if(document.gratuityForm.chequeDate[i].value==""){
						alert("Enter cheque date");
						document.gratuityForm.chequeDate[i].focus();
						return false;
					}
					if(document.gratuityForm.payerBankAccount.value != "" && document.gratuityForm.chequeNo[i].value != ""){
						if(checkUniqueCheque(document.gratuityForm.chequeNo[i]) == "false"){
							document.gratuityForm.chequeNo[i].value = "";
							document.gratuityForm.chequeNo[i].focus();
							return false;
						}
						if(checkInRange(document.gratuityForm.chequeNo[i]) == "false"){
							document.gratuityForm.chequeNo[i].value = "";
							document.gratuityForm.chequeNo[i].focus();
							return false;
						}
					}
				}
			}	
		</c:if>
		document.gratuityForm.action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=saveGratuityDisburse";
	    ///document.forms("gratuityForm").submit();		
	}

	function callBankBranch(obj){		
		var bankId = obj.value;		
		for(var resLen=1;resLen<document.gratuityForm.bankBranch.length;resLen+1){
				document.gratuityForm.bankBranch.options[resLen]=null;
		}	
		var count = 1;
		<c:forEach var="bankBranchObj" items="${gratuityForm.bankBranchList}">	
			if("${bankBranchObj.bank.id}" == bankId){				
				document.gratuityForm.bankBranch.options[count] = new Option("${bankBranchObj.branchname}","${bankBranchObj.id}");
				count=count+1;
			}					
		</c:forEach>
	}

	function changeCommute(obj){		
		if(obj.value != ""){
			var basic = eval(document.gratuityForm.currentBasicPay.value);
			var comutedPercentage = eval(obj.value);
			var commutPeriod = eval(document.gratuityForm.commutePeriod.value);
			var gratuityAmount = eval(document.gratuityForm.gratuityAmount.value);
			var pensionEligible = eval(document.gratuityForm.pensionEligible.value);

			var pensionCommute = eval(basic*comutedPercentage/100);
			var commutedAmount = eval(pensionCommute*12*commutPeriod);
			document.gratuityForm.pensionCommuted.value = pensionCommute;
			document.gratuityForm.commuteAmount.value = commutedAmount;
			document.gratuityForm.totalPayment.value = eval(commutedAmount+gratuityAmount);
			document.gratuityForm.monthlyPensionPayable.value = eval(pensionEligible-pensionCommute);	
			populateNomineeAmount();
		}
	}
//FIXME : Use struts validator
	function checkForPct(obj){
		var objt = obj;
	    var amt = obj.value;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 || amt >100)
	        {
	            alert("Please enter value (0-100) for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}
//FIXME : Use struts validator
	function checkdecimalval(obj,amount){		
	    var objt = obj;
	    var amt = amount;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 )
	        {
	            alert("Please enter positive value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}

	function callPayerBankBranch(obj){	
		var bankId = obj.value;		
		for(var resLen=1;resLen<document.gratuityForm.payerBankBranch.length;resLen+1){
				document.gratuityForm.payerBankBranch.options[resLen]=null;
		}	
		var count = 1;
		<c:forEach var="bankBranchObj" items="${gratuityForm.bankBranchList}">	
			if("${bankBranchObj.bank.id}" == bankId){				
				document.gratuityForm.payerBankBranch.options[count] = new Option("${bankBranchObj.branchname}","${bankBranchObj.id}");
				count=count+1;
			}					
		</c:forEach>	
		callPayerBankAccount(document.gratuityForm.payerBankAccount);
	}

	function callPayerBankAccount(obj){		
		var bankBranchId = obj.value;		
		for(var resLen=1;resLen<document.gratuityForm.payerBankAccount.length;resLen+1){
				document.gratuityForm.payerBankAccount.options[resLen]=null;
		}	
		var count = 1;
		<c:forEach var="bankAccountObj" items="${gratuityForm.bankAccountList}">	
			if("${bankAccountObj.bankbranch.id}" == bankBranchId){				
				document.gratuityForm.payerBankAccount.options[count] = new Option("${bankAccountObj.accountnumber}","${bankAccountObj.id}");
				count=count+1;
			}					
		</c:forEach>
	}
	
	function nextChqNo(){
		//document.AdvanceDisbursementByChequeForm.chequeNo.value="";
		//var mozillaFirefox=document.getElementById&&!document.all;
		var payerBank = document.gratuityForm.payerBank;
		var payerBankBranch = document.gratuityForm.payerBankBranch;
		var payerBankAccount = document.gratuityForm.payerBankAccount;

		if(payerBankBranch.value=="")
		{
		  alert("Select bank branch");
		  return;
		}

		if(payerBankAccount.value=="")
		{
		  alert("select bank account");
		  return;
		}
		var accNoId=payerBankAccount.value;	
		window.open("${pageContext.request.contextPath}/pension/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=500pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");		
	}

	function checkDublicateCheque(obj){
		var tbl = document.getElementById('payeeTable').rows.length;			
		var rowObj=getRow(obj);
		if(tbl>3)
		{
			for(var i=0;i<tbl;i++)
			{
				for(var j=i+1;j<tbl-2;j++)
				{
					if(i!=j)
					{	
						if(document.gratuityForm.chequeNo[i].value != "" && document.gratuityForm.chequeNo[j].value != "")
						{
							if(document.gratuityForm.chequeNo[i].value==document.gratuityForm.chequeNo[j].value)
							{
								alert("Duplicate Selection of Cheque number!!!");
								document.gratuityForm.chequeNo[j].value = "";
								document.gratuityForm.chequeNo[j].focus();
								return false;
							 }
						}
					}
				}
			 }
		 }
    }

	function checkUniqueCheque(obj){		
		var type = "isUniqueChequeNo";
		var bankAccountId = document.getElementById("payerBankAccount").value;		
		if(obj.value != "" && bankAccountId != ""){
			var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&chequeNo="+obj.value+"&bankAccountId="+bankAccountId ;
			var isUnique;
			var req = initiateRequest();
			req.onreadystatechange = function(){
			  if (req.readyState == 4){
					if (req.status == 200){
						var response = req.responseText
						var result = response.split("/");
						if(result[0]=="true"){
							isUnique = "ture";
						}
						else if(result[0]=="false"){
							isUnique = "false";
							alert("Duplicate cheque no!!");
							//obj.focus();
						}
					}
				}
			};
			req.open("GET", url, false);
			req.send(null);		
			return isUnique;
		}
	}

	function checkInRange(obj){		
		var type = "isChqNoWithinRange";
		var bankAccountId = document.getElementById("payerBankAccount").value;		
		if(obj.value != "" && bankAccountId != ""){
			var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&chequeNo="+obj.value+"&bankAccountId="+bankAccountId ;
			var isInRange;
			var req = initiateRequest();
			req.onreadystatechange = function(){
			  if (req.readyState == 4){
					if (req.status == 200){
						var response = req.responseText
						var result = response.split("/");
						if(result[0]=="true"){
							isInRange = "ture";
						}
						else if(result[0]=="false"){
							isInRange = "false";
							alert("Invalid cheque no!!");
							//obj.focus();
						}
					}
				}
			};
			req.open("GET", url, false);
			req.send(null);		
			return isInRange;
		}
	}

	function checkUniqueVN(){		
		var type = "isUniqueVN";
		var voucherNo = document.gratuityForm.voucherNo.value;
		var voucherDate = document.gratuityForm.voucherDate.value;
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&voucherNo="+voucherNo+"&voucherDate="+voucherDate ;
		var isUnique;
		var req = initiateRequest();
		req.onreadystatechange = function(){
		  if (req.readyState == 4){
				if (req.status == 200){
					var response = req.responseText
					var result = response.split("/");
					if(result[0]=="true"){
						isUnique = "ture";
					}
					else if(result[0]=="false"){
						isUnique = "false";
						alert("Duplicate voucher no!!");
						//obj.focus();
					}
				}
			}
		};
		req.open("GET", url, false);
		req.send(null);		
		return isUnique;		
	}
	

</script>

</head>
<body onLoad="onBodyLoad();">
<div class="navibarshadowwk"></div>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <div class="datewk"></div>
<html:form action ="/pension/gratuityAction" >
	
 	<input type="hidden" name="checkEmpCode"/>
 	
 	
  <table width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="employeeTable" >
   	<tr>
	    <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Disburse Gratuity</div></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.pensionDetails.pensionHeader.employee.idPersonalInformation }" />
	 	<td class="whiteboxwk">Employee Code</td>
	  	<td class="whitebox2wk">
	  		<input type="text"   name="employeeCode" id ="employeeCode" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeCode }" readOnly/>
	  	</td>
	  	<td class="whiteboxwk">Employee Name</td>
	  	<td class="whitebox2wk">
		  	<input type="text"  class="fieldcell" name="employeeName" id ="employeeName" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeName }" readOnly />
	  	</td>
    </tr>
  	<tr>
  		<td><div id="codescontainer"></div></td>
    </tr>
    
   </table>
   

   <table width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="gratuityTable" >	
    <tr>	    
	 	<td class="greyboxwk">Birth Date</td>
	  	<td class="greybox2wk">
	  		<input type="text"   name="birthDate" id ="birthDate" value="${gratuityForm.birthDate}" readOnly />
	  	</td>
	  	<td class="greyboxwk">Superannuation Date</td>
	  	<td class="greybox2wk">
		  	<input type="text"   name="supperannuationDate" id ="supperannuationDate" value="${gratuityForm.supperannuationDate}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="whiteboxwk">Designation</td>
	  	<td class="whitebox2wk">
	  		<input type="text"   name="designation" id ="designation" value="${gratuityForm.designation}" readOnly />
	  	</td>
	  	<td class="whiteboxwk">Department</td>
	  	<td class="whitebox2wk">			
		  	<input type="text"   name="department" id ="department" value="${gratuityForm.department}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="greyboxwk">Fund</td>
	  	<td class="greybox2wk">
	  		<input type="text"   name="empFund" id ="empFund" value="${gratuityForm.empFund}" readOnly />
	  	</td>
	  	<td class="greyboxwk"></td>
	  	<td class="greybox2wk">			
		  	
	  	</td>
    </tr>  
	
<!--	<tr>	    
	 	<td class="labelcell">Payment Method</td>
	  	<td class="labelcell">
			<c:if test = "${gratuityForm.pensionDetails.payTo == 'nominee'}">
				<input type="text" name="disbursementType" value="Cheque" />
			</c:if>	
			<c:if test = "${gratuityForm.pensionDetails.payTo == 'employee'}">
				<input type="text" name="disbursementType" value="${gratuityForm.pensionDetails.pensionHeader.disbursementType}" />
			</c:if>									
	  	</td>	  	
    </tr>	-->
	
	 	
	<tr>	    
	 	<td class="whiteboxwk">Payment Method</td>
		<td class="whitebox2wk">
			<c:if test = "${gratuityForm.pensionDetails.payTo == 'nominee'}">
				<input type="text" class="fieldcell" value="Cheque" readOnly/>
			</c:if>	
			<c:if test = "${gratuityForm.pensionDetails.payTo == 'employee'}">
				<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'dbt'}">
					<input type="text" class="fieldcell" value="Direct bank transfer" readOnly/>
				</c:if>
				<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'cheque'}">
					<input type="text" class="fieldcell" value="Cheque" readOnly/>
				</c:if>
				<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'cash'}">
					<input type="text" class="fieldcell" value="Cash" readOnly/>
				</c:if>
			</c:if>	
		</td>
    </tr> 
	
	
							
		
<!--	<tr id="PayerBankRowId" >	
		<td class="labelcell">Voucher No<font color="red">*</font></td>
	  	<td class="labelcell">
	  		<input type="text" name="voucherNo" />
	  	</td>
		<td class="labelcell">Voucher DAte<font color="red">*</font></td>
		<td class="smallfieldcell">
				<html:text property= "voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('gratuityForm.voucherDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a> 
		</td>	  	
	</tr>
	<tr id="PayerBankRowId" >	
		<td class="labelcell">Payer Bank<font color="red">*</font></td>
	  	<td class="labelcell">
	  		<html:select property="payerBank" styleClass="fieldcell" onblur="callPayerBankBranch(this)">
				<html:option value="">-----------Select-----------</html:option>
				<c:forEach var="bankObj" items="${gratuityForm.bankList}">
					<html:option value="${bankObj.id}">${bankObj.name}</html:option>
				</c:forEach>
			</html:select>
	  	</td>
	</tr>
   	<tr id="PayerBankBranchRowId" >
	 	<td class="labelcell">Payer Bank Branch<font color="red">*</font></td>
	  	<td class="labelcell">
	  		<html:select property="payerBankBranch" styleClass="fieldcell" onchange="callPayerBankAccount(this);">
				<html:option value="">-----------Select-----------</html:option>			
			</html:select>
	  	</td>	  	
	  	<td class="labelcell">Bank Account<font color="red">*</font></td>
	  	<td class="labelcell">
			<html:select property="payerBankAccount" styleClass="fieldcell" >
				<html:option value="">-----------Select-----------</html:option>			
			</html:select>	  		
	  	</td>
    </tr> 		-->
	
 <!--   <table style="width: 800;" align="center" cellpadding="0" cellspacing="0" border="1" id="payeeTable" >			
		 <tr>	
			<td class="labelcell" >Payee</td>
			<td class="labelcell" >Relationship</td>
		<c:if test = "${gratuityForm.autoGenerateChqNO == 'N'}">
			<td class="labelcell" >Cheque No<font color="red">*</font>&nbsp;&nbsp;&nbsp;<A onclick=nextChqNo() href="#"><IMG id="img" height=22 src="../img/arrowright.gif" width=22 ></A></td>
			<td class="labelcell">Cheque Date<font color="red">*</font></td>
		</c:if>
			<td class="labelcell" >Cheque Amount</td>
		 </tr>		
		 <c:if test = "${gratuityForm.pensionDetails.payTo == 'employee'}">
		  <tr id="payeeRowId">
				<td class="labelcell">		
					<input type="hidden"  name="payeeId" value="${gratuityForm.pensionDetails.pensionHeader.employee.idPersonalInformation}" />				
					<input type="text" class="fieldcell" name="payeeName" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeName}" readOnly/>
				</td>
				<td class="labelcell">
					<input type="text" class="fieldcell" name="relationship" value="Self" readOnly/>
				</td>
			<c:if test = "${gratuityForm.autoGenerateChqNO == 'N'}">
				<td class="labelcell">
					<input type="text" class="fieldcell" name="chequeNo"  />
				</td>
				<td class="smallfieldcell">
				<html:text property= "chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('gratuityForm.chequeDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a> 
				</td>
			</c:if>		
				<td class="labelcell">
					<input type="text" class="fieldcell" name="chequeAmount" value="${gratuityForm.payAmount}" />
				</td>
		 </c:if>	
		 <c:if test = "${gratuityForm.pensionDetails.payTo == 'nominee'}">
			<%	int i = 0;	%>

			<c:forEach var="eligibleNomineeObj" items="${gratuityForm.eligibleNomineeSet}">
			 <tr id="payeeRowId">
				<td class="labelcell">
					<input type="hidden"  name="payeeId" value="${eligibleNomineeObj.id}" />
					<input type="text" class="fieldcell" name="payeeName" value="${eligibleNomineeObj.firstName}" readOnly/>			
				</td>
				<td class="labelcell">
					<input type="text" class="fieldcell" name="relationship" value="${eligibleNomineeObj.nomineeType.nomineeType}" readOnly/>
				</td>
			<c:if test = "${gratuityForm.autoGenerateChqNO == 'N'}">
				<td class="labelcell">
					<input type="text" class="fieldcell" name="chequeNo" value="" onblur="checkDublicateCheque(this);"/>
				</td>
				<td class="smallfieldcell">
					<html:text property= "chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('gratuityForm.chequeDate[<%= i %>]');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../images/calendar.gif" width=24 height=22 border=0></a> 
				</td>
			</c:if>
				<td class="labelcell">
					<input type="text" class="fieldcell" name="chequeAmount" value="${gratuityForm.payAmount}" />
				</td>		
			  </tr> 
			  <%	i = i+1;	%>
			</c:forEach>	  
		 </c:if>		
		 <tr>	
		   <c:if test = "${gratuityForm.autoGenerateChqNO == 'N'}">	
			<td class="labelcell"></td>
			<td class="labelcell"></td>
		   </c:if>	
			<td class="labelcell"></td>
			<td class="labelcell"><b>Total</b></td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="total" id="total" value="${gratuityForm.total}" readOnly/>
			</td>	
		 </tr>
	</table>		
	<table style="width: 800;" align="center" cellpadding="0" cellspacing="0" border="1" id="payeeBankTable" style="display: none">
		<tr id="bankRowId" style="display:none">	
			<td class="labelcell">Payee Bank</td>
			<td class="labelcell">
				<html:select property="bank" styleClass="fieldcell" onblur="callBankBranch(this)">
					<html:option value="">-----------Select-----------</html:option>
					<c:forEach var="bankObj" items="${gratuityForm.bankList}">
						<html:option value="${bankObj.id}">${bankObj.name}</html:option>
					</c:forEach>
				</html:select>
			</td>
		</tr>
		<tr id="bankBranchRowId" style="display:none">				  	
			<td class="labelcell">Payee Bank Branch</td>
			<td class="labelcell">
				<html:select property="bankBranch" styleClass="fieldcell" value="${gratuityForm.pensionDetails.pensionHeader.idBranch.id}">
					<html:option value="">-----------Select-----------</html:option>				
				</html:select>
			</td>	  	
			<td class="labelcell">Payee Bank Account</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="bankAccount" value="${gratuityForm.pensionDetails.pensionHeader.accountNumber}" />
			</td>
		</tr> 						
	</table>		-->
	   
		<table width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="payeeBankTable" >
		<tr>
		<td class="greyboxwk">Gratuity Amount</td>
			<td class="greybox2wk">
				<input type="text"    name="gratuityAmount" value="${gratuityForm.pensionDetails.gratuityAmount}" readOnly/>
			</td>
		</tr>						
	</table>
	
	<table width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="nomineeTable" >
		<tbody>
		
	<!-- validation should be for Deceased...  -->
    <c:if test="${gratuityForm.pensionDetails.pensionHeader.employee.statusMaster.description=='Deceased'}">
    <tr>
    
		<td  class="tablesubheadwk" width="10%" >Nominee Name<br></td>
		<td  class="tablesubheadwk" width="8%" ><bean:message key="Relation"/></td>
		<td  class="tablesubheadwk" width="8%" ><bean:message key="alive"/></td>
		<td  class="tablesubheadwk" width="6%" ><bean:message key="married"/></td>
		<td  class="tablesubheadwk" width="10%" ><bean:message key="EmpDob"/></td>
		<td  class="tablesubheadwk" width="8%" ><bean:message key="employed"/></td>
		<td  class="tablesubheadwk" width="9%" ><bean:message key="Status"/></td>
		<td  class="tablesubheadwk" width="10%" ><bean:message key="DisType"/></td>
		<td id="1Row" class="tablesubheadwk" width="12%"><bean:message key="Bank"/></td>
		<td id="3Row" class="tablesubheadwk" width="10%"><bean:message key="BranchName"/></td>
		<td id="3Row" class="tablesubheadwk" width="10%"><bean:message key="AccountNumber"/></td>
		<td id="3Row" class="tablesubheadwk" width="3%">Percentage</td>
		<td id="3Row" class="tablesubheadwk" width="7%">Amount</td>
			
		</tr>
		<br>
		<tr>
		
		   
				<c:forEach var="nomineeObj" items="${gratuityForm.eligibleNomineeSet}">
				<tr>
				  <td><input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.nomineeName}" readonly/></td>
				  <td> <input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.relationType.nomineeType}" readonly/><td>
				    
				  <c:choose>
				  
				    <c:when test="${nomineeObj.nomineeMstr.isActive==1}">	
				       <input type="text" class="selectwk textmxwidth2" value="Yes" readonly/>
				     </c:when>
				     <c:when test="${nomineeObj.nomineeMstr.isActive==2}">	
				       <input type="text" class="selectwk textmxwidth2" value="No" readonly/>
				     </c:when>
				    
				   </c:choose>
				   
				     <c:choose>
				    <c:when test="${nomineeObj.nomineeMstr.maritalStatus==1}">	
				       <td> <input type="text" class="selectwk textmxwidth2" value="Yes" readonly/><td>
				     </c:when>
				     <c:when test="${nomineeObj.nomineeMstr.maritalStatus==2}">	
				      <td> <input type="text" class="selectwk textmxwidth2" value="No" readonly/><td>
				     </c:when>
				   </c:choose>
				   
				  <fmt:formatDate type="DATE" pattern="dd/MM/yyyy"  value="${nomineeObj.nomineeMstr.nomineeDob}" />
				   
				   <c:choose>
				    <c:when test="${nomineeObj.nomineeMstr.isWorking==1}">	
				        <td><input type="text" class="selectwk textmxwidth2" value="Yes" readonly/></td>
				     </c:when>
				     <c:when test="${nomineeObj.nomineeMstr.isWorking==2}">	
				      <td><input type="text" class="selectwk textmxwidth2" value="No" readonly/></td>
				     </c:when>
				   </c:choose>
				   
				   <c:choose>
					    <c:when test="${nomineeObj.isEligible==1}">	
					       <td> <input type="text" class="selectwk textmxwidth2" value="Eligible" readonly/></td>
					     </c:when>
				    </c:choose>
				    
				   <td> <input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeDisType}" readonly/></td>
				   <td> <input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.bankBranch.bank.name}" readonly/></td>
				    <td><input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.bankBranch.branchname}" v/></td>
				    <td><input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.accountNumber}"readonly /></td>
				  <td>  <input type="text" class="selectwk textmxwidth2" value="${nomineeObj.percentage}" readonly/></td>
				   <td><input type="text" class="selectwk textmxwidth2" name="nomineeGratuityAmount" id="nomineeGratuityAmount" value="${nomineeObj.gratutiyAmount}"/><td>
				   </td>
				   </tr>
		      </c:forEach>
				
			
			
		</tr>
		</c:if>
		</tbody>
		</table>
	 	 <br>
	 	 </div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
   </table>


   
</html:form>
<div class="buttonholderwk">
	
<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close()"/></div>

<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
</body>
</html>
