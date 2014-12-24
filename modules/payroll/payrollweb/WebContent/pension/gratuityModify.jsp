<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Gratuity Modify </title>

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
%>
<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;
	
	function initiateRequest() {
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
    }
		
	function onBodyLoad(){  		
	 
	   <%		
		if(!"search".equals(mode)){
		%>			
			populateBankInfo();
			//populateNomineeInfo();			
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
			document.getElementById("bankRowId").style.display = "";
			document.getElementById("bankBranchRowId").style.display = "";
			callBankBranch(document.gratuityForm.bank);
			document.gratuityForm.bankBranch.value = "${gratuityForm.pensionHeader.idBranch.id}";

		}
	}

	function populateNomineeInfo(){
		if("${gratuityForm.payTo}" == "nominee"){	
			if(!validateNomineeExist()){					
				document.getElementById("employeeTable").style.display = "none";
				document.getElementById("gratuityTable").style.display = "none";
				document.getElementById("saveTable").style.display = "none";
				alert("This employee is deceased and there are no eligible nominees for this employee,so gratuity computation being terminated");	
				window.location = "/pension/search.jsp?mode=create";				
			}
			else{
				document.getElementById("nomineeTable").style.display = "";
				document.getElementById("nomineeTotalTable").style.display = "";
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

	function callBank(obj){
		document.getElementById("bankRowId").style.display = "none";
		document.getElementById("bankBranchRowId").style.display = "none";
		if(obj.value == "dbt"){
			document.getElementById("bankRowId").style.display = "";
			document.getElementById("bankBranchRowId").style.display = "";
			callBankBranch(document.gratuityForm.bank);
			document.gratuityForm.bankBranch.value = "${gratuityForm.pensionHeader.idBranch.id}";
		}
	}
  	
  	function callNominee(obj){			
		document.getElementById("nomineeTable").style.display = "none";
		document.getElementById("nomineeTotalTable").style.display = "none";		
		if(obj.value == "nominee" && validateNomineeExist()){
			document.getElementById("nomineeTable").style.display = "";
			document.getElementById("nomineeTotalTable").style.display = "";
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
		req.open("GET", url, false);
		req.send(null);	
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
		return isExist;
	}
	
	function checkForPensionDetailsByEmp(){
		var type = "getPensionDetailsByEmp";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
		req.open("GET", url, false);
		req.send(null);	
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
			
		return isExist;
	}

	function validateEmployeeGratuityEligibility(){
		alert("validate");
		var type = "validateEmployeeGratuityEligibility";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
		req.open("GET", url, false);
		req.send(null);		
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
	     
		
		return isExist;
	}
	//FIXME : Add alerts and labels in ApplicationRes properties file.
	function validateOnSearch(){		
		if(checkForPensionDetailsByEmp() == "exist"){
			alert("Gratuity computation already done for this employee");
			document.gratuityForm.employeeCode.focus();
			return false;
		}
		if(validateEmployeeGratuityEligibility() == "false"){
			alert("Gratuity computation not possible for this employee");
			document.gratuityForm.employeeCode.focus();
			return false;
		}
		if(document.gratuityForm.employeeCode.value == ""){
			alert("Enter employee code");	
			document.gratuityForm.employeeCode.focus();
			return false;
		}
		if(document.gratuityForm.checkEmpCode.value == "undefined"){
			alert("Enter correct employee code");	
			document.gratuityForm.employeeCode.focus();
			return false;
		}
	   
	    if(checkForPensionHeaderExist() == "notExist"){
			alert("Pension header is not there for this employee");
			document.gratuityForm.employeeCode.focus();
			return false;
		}
  	   document.forms("gratuityForm").action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=gratuityView";
	   document.forms("gratuityForm").submit();
  	} 	

	function validateOnSave(obj){		
		if('save'==obj){
			if(document.gratuityForm.comment.value == ''){
				alert("Comment is mandatory for modifying gratuity");
				document.gratuityForm.comment.focus();
				return false;
			}
			document.gratuityForm.actionSubmit.value = "save";
		}
		else if('approve'==obj){
			document.gratuityForm.actionSubmit.value = "approve";
		}
		else{
			document.gratuityForm.actionSubmit.value = "reject";
		}
		if(document.gratuityForm.gratuityAmount.value==""){
			alert("Enter gratuity amount!!");
			document.gratuityForm.gratuityAmount.focus();
			return false;
		}
		if(document.gratuityForm.monthlyPensionPayable.value==""){
			alert("Enter monthly pension amount");
			document.gratuityForm.monthlyPensionPayable.focus();
			return false;
		}
		if(document.gratuityForm.pensionSanctionNumber.value==""){
			alert("Enter pension sanction number");
			document.gratuityForm.pensionSanctionNumber.focus();
			return false;
		}
		if(document.gratuityForm.pensionSanctionAuthority.value==""){
			alert("Enter pension sanction authority");
			document.gratuityForm.pensionSanctionAuthority.focus();
			return false;
		}
		/*if(document.gratuityForm.pensionCommutedPercent.value==""){
			alert("Enter commuted percentage amount");
			document.gratuityForm.pensionCommutedPercent.focus();
			return false;
		}*/
		if(document.gratuityForm.monthlyPensionPayable.value==""){
			alert("Enter monthly pension amount");
			document.gratuityForm.monthlyPensionPayable.focus();
			return false;
		}
		if(document.gratuityForm.disbursementType.value==""){
			alert("Enter disbursement type");
			document.gratuityForm.disbursementType.focus();
			return false;
		}
		if(document.gratuityForm.daComponentPercent.value==""){
			alert("Enter DA ComponentPercent ");
			document.gratuityForm.daComponentPercent.focus();
			return false;
		}

	/*	if(document.gratuityForm.pensionNumber.value==""){
			alert("enter pension number");
			document.gratuityForm.pensionNumber.focus();
			return false;
		}	*/
		document.gratuityForm.action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=gratuityModify";
	    document.gratuityForm.submit();		
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

	/*function checkCommuteCap(obj){
		//alert(${gratuityForm.commuteCap});
		if(${gratuityForm.commuteCap} < obj.value){
			alert("commuted percentage can't be greater than commute cap");
			obj.value = ${gratuityForm.commuteCap};
			obj.focus();
		}
	}*/


	function computeDA(obj){
		if(obj.value != ""){
			var daPct = obj.value;
			var basic = document.gratuityForm.currentBasicPay.value;
			if(basic != ""){
				var da = eval(basic*daPct/100);
				document.gratuityForm.daComponent.value = da;
				document.gratuityForm.monthlyPensionPayable.value = eval(basic)+eval(da);
			}
		}
	}
	
	function closeAndRefreshParent() 
	{	
		//alert("dasda");
		//alert(document.gratuityForm.isSaved.value);
		if(document.gratuityForm.isSaved.value != ""){						
			//window.opener.location.href="${pageContext.request.contextPath}/exception/beforeException.do?submitType=showExceptions&month="+${exception.month}+"&yearId="+${exception.financialyear.id};
			alert(document.gratuityForm.isSaved.value);
			refreshInbox();	
			window.close();		
		}	
	}
	
	function refreshInbox()
    {
    	if(opener.top.document.getElementById('inboxframe')!=null)
    	{    	
    		if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
    		{    		
    			opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    		}
    	
    	}  	
    }



</script>

</head>
<body onLoad="onBodyLoad();closeAndRefreshParent();">
<html:form action ="/pension/gratuityAction" >
	<center>
		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div>
						</div>
					</div>
					<div class="rbcontent2">
						<div class="datewk">	
						    <span class="bold"></span>
							</div>
 	<input type="hidden" name="checkEmpCode"/>
 	<input type="hidden" name="actionSubmit"/> 	
	<input type="hidden" name="pensionDetailsId" value="${gratuityForm.pensionDetails.id}"/>
	<html:hidden name="gratuityForm" property="isSaved" styleId="isSaved" />
	 
   <table width="95%" cellpadding="0" cellspacing="0" border="0" align="center" id="employeeTable">
   		<tr>
        	<td colspan="4" class="headingwk">
	         	<div class="arrowiconwk">
	         		<img src="../common/image/arrow.gif" />
	         	</div>
	           	<div class="headplacer">
		           	Gratuity Processing
				</div>
          	</td>
         </tr>
	   	 <tr>
		    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}" />
		 	<td class="whiteboxwk">Employee Code</td>
		  	<td class="whiteboxwk">
		  		<input type="text"   name="employeeCode" id ="employeeCode" value="${gratuityForm.employeeCode }" readOnly/>
		  	</td>
		  	<td class="whiteboxwk">Employee Name</td>
		  	<td class="whiteboxwk">
			  	<input type="text"  name="employeeName" id ="employeeName" value="${gratuityForm.employeeName }" readOnly />
		  	</td>
	    </tr>
   
	<tr>	    
	 	<td class="greyboxwk">Designation</td>
	  	<td class="greyboxwk">
	  		<input type="text"   name="designation" id ="designation" value="${gratuityForm.designation}" readOnly />
	  	</td>
	  	<td class="greyboxwk">Department</td>
	  	<td class="greyboxwk">			
		  	<input type="text"   name="department" id ="department" value="${gratuityForm.department}" readOnly />
	  	</td>
    </tr>  
    <tr>	    
	 	<td class="whiteboxwk">Birth Date</td>
	  	<td class="whiteboxwk">
	  		<input type="text"   name="birthDate" id ="birthDate" value="${gratuityForm.birthDate}" readOnly />
	  	</td>
	  	<td class="whiteboxwk">Superannuation Date</td>
	  	<td class="whiteboxwk">
		  	<input type="text"   name="supperannuationDate" id ="supperannuationDate" value="${gratuityForm.supperannuationDate}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="greyboxwk">Employee Status</td>
	  	<td class="greyboxwk">
	  		<input type="text"   name="empStatus" id ="enpStatus" value="${gratuityForm.pensionHeader.employee.statusMaster.description}" readOnly />
	  	</td>
	  	<td class="greyboxwk"></td>
	  	<td class="greyboxwk">		  	
	  	</td>
    </tr>
	<tr>
	 	<td class="whiteboxwk" >Disbursement Type<font color="red">*</font></td>
	  	<td class="whiteboxwk" >
			<html:select property="disbursementType" onchange="callBank(this);"	value="${gratuityForm.pensionHeader.disbursementType}">
				<html:option value="">--------Select--------</html:option>
				<html:option value="cheque">Cheque</html:option>
				<html:option value="cash">Cash</html:option>
				<html:option value="dbt">Direct Bank Transfer</html:option>
			</html:select>	  		
	  	</td>
	  	<td class="whiteboxwk">&nbsp;</td>
	  	<td class="whiteboxwk">&nbsp;</td>	  	
    </tr>  
	<tr id="bankRowId" style="display:none">	
		<td class="greyboxwk">Bank</td>
	  	<td class="greyboxwk">
	  		<html:select property="bank"  value="${gratuityForm.pensionHeader.idBranch.bank.id}" onblur="callBankBranch(this)">
				<html:option value="">---------Select---------</html:option>
				<c:forEach var="bankObj" items="${gratuityForm.bankList}">
					<html:option value="${bankObj.id}">${bankObj.name}</html:option>
				</c:forEach>
			</html:select>
	  	</td>
	  	<td class="greyboxwk"></td>
	  	<td class="greyboxwk"></td>
	</tr>
   	<tr id="bankBranchRowId" style="display:none">				  	
	 	<td class="whiteboxwk">Bank Branch</td>
	  	<td class="whiteboxwk">
	  		<html:select property="bankBranch"  value="${gratuityForm.pensionHeader.idBranch.id}">
				<html:option value="">---------Select---------</html:option>			
			</html:select>
	  	</td>	  	
	  	<td class="whiteboxwk">Bank Account</td>
	  	<td class="whiteboxwk">
	  		<input type="text"   name="bankAccount" value="${gratuityForm.pensionHeader.accountNumber}" />
	  	</td>
    </tr> 	
    <tr>	    
	 	<td class="greyboxwk">Pension Sanction Number<font color="red">*</font></td>
	  	<td class="greyboxwk">
	  		<html:text property="pensionSanctionNumber" readonly="true"/>
	  		
	  	</td>	  	
	  	<td class="greyboxwk">Sanction Authority<font color="red">*</font></td>
	  	<td class="greyboxwk">
	  		<html:text property="pensionSanctionAuthority" readonly="true"/>
	  		
	  	</td>
    </tr> 
	
 <!--   <tr>	    
	 	<td class="labelcell">Pension Number(PPO)<font color="red">*</font></td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="pensionNumber" />
	  	</td>	  		
    </tr>		-->
	
    <tr>	    
	 	<td class="whiteboxwk">Basic Pay</td>
	  	<td class="whiteboxwk" > 
	  		<input type="text"   name="currentBasicPay" value="${gratuityForm.currentBasicPay}" readOnly/>
	  	</td>
	  	<td class="whiteboxwk" > </td>
		<td class="whiteboxwk" ></td> 	  		
	</tr>

	<!--<tr> 	
	  	<td class="labelcell">Basic Component</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="basicComponentPercent" value="${gratuityForm.basicComponentPercent}" readOnly/>
		</td>
		<td class="labelcell">			
			<input type="text"  class="fieldcell" name="basicComponent" value="${gratuityForm.basicComponent}" readOnly/>
	  	</td>
	 </tr>	-->
	 <tr> 	
	  	<td class="greyboxwk">DA(%)<span class="mandatory">*</span></td>
	  	<td class="greyboxwk">
	  		<input type="text"   name="daComponentPercent"  value="${gratuityForm.daComponentPercent}" onchange="trim(this,this.value);checkdecimalval(this,this.value);checkForPct(this);computeDA(this);"/>
		</td>
		<td class="greyboxwk">Computed DA</td>
		<td class="greyboxwk">
			<input type="text"   name="daComponent" value="${gratuityForm.daComponent}" readOnly/>
	  	</td>
	  	<td class="greyboxwk"></td>
	 </tr> 
	
	 <tr> 	
	  	<td class="whiteboxwk" >Gratuity Amount<span class="mandatory">*</span></td>
	  	<td class="whiteboxwk">
	  		<input type="text"   name="gratuityAmount"  value="${gratuityForm.gratuityAmount}" onblur="trim(this,this.value);checkdecimalval(this,this.value);"/>
	  	</td>
	  	<td class="whiteboxwk"></td>
	  	<td class="whiteboxwk"></td>
	 </tr> 			
	<!--  <tr> 	
	  	<td class="greyboxwk" >Total Payment(Gross)(A+B)</td>
	  	<td class="greyboxwk">
	  		<input type="text"   name="totalPayment" value="${gratuityForm.totalPayment}" readOnly/>
	  	</td>
	  	<td class="greyboxwk"></td>
	  	<td class="greyboxwk"></td>
	 </tr> 	-->
	 
	 <tr>
		<td class="greyboxwk">Monthly Pension Payable<span class="mandatory">*</span></td>
		<td class="greyboxwk">
			<input type="text"   name="monthlyPensionPayable" id="monthlyPensionPayable" value="${gratuityForm.monthlyPensionPayable}" onblur="trim(this,this.value);checkdecimalval(this,this.value);" />
		</td>
		<td class="greyboxwk">Comment</td>
		<td class="greyboxwk"><input type="text" name="comment" /></td>
	 </tr>
	
	   
	 	 
   </table>

	</div>	
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
		<input type="button" value="Save" onclick="return validateOnSave('save');" class="buttonfinal"/>
		<input type="button" value="Approve" onclick="return validateOnSave('approve');" class="buttonfinal"/>
		<input type="button" value="Reject" onclick="return validateOnSave('reject');" class="buttonfinal"/>
	</div>
				<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</center>	

   
</html:form>
</body>
</html>
