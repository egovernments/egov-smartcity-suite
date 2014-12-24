<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>View Recoveries </title>

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
	var acctCodeArray;
	var selectedAcctCode;
	var yuiflag = new Array();

   function onBodyLoad(){  		
	   loadAccountCodes();

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
				alert("No eligible nominee for this employee");
				
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
	

	function whichButtonNominee(e,tbl,obj,objr){
		var F2 = 113;
		var del = 46;
		var code;
		if ( !e )
		 var e = window.event;
		if ( e.keyCode ) code = e.keyCode ;
		else if ( e.which ) code = e.which ;	
		alert(obj.value);	  
		addRowToTable(tbl,obj);
	}

	function deleteRow(table,obj){		
		if(table=='recoveryTable'){
			var tbl = document.getElementById(table);
			var rowNumber=getRow(obj).rowIndex;	
			if(tbl.rows.length > 2)
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
		  //var checkRowLength = eval(getControlInBranch(tableObj.rows[rowObj1.rowIndex],'recoveryCode').options.length) + 1;
		  if(tbl=='recoveryTable'){	
			   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
			   tbody.appendChild(rowObj);	
			   var remlen = document.gratuityForm.recoveryCode.length;			  
			   document.gratuityForm.recoveryCode[remlen-1].value="";
			   document.gratuityForm.recoveryAmount[remlen-1].value="";			   
		  }
		  else
		  {
			  if(tbl=='recoveryTable')
			  {
				alert("No nominee Available to insert");
				return false;
			  }
		  }	 
	}

	function checkForNomineeSelection(){
		var tbl = document.getElementById('nomineeTable');
		var rCount=tbl.rows.length-1;
		if(tbl.rows.length == 3)
		{
			if(document.gratuityForm.nomineeName.value =="")
			{
				alert("Select nominee");
				document.gratuityForm.nomineeName.focus();
				return false;
			}			
		}
		if(tbl.rows.length>3)
		{
			if(document.gratuityForm.nomineeName[rCount-2].value =="")
			{
				alert("Select nominee");
				document.gratuityForm.nomineeName[rCount-2].focus();
				return false;
			 }				
		}		
		return true;
	}

	function checkDuplicate(obj){
		 var rowObj=getRow(obj);
		 var hit=0;
		 var caught=0;
		 var table= document.getElementById("nomineeTable");
		 var tbl = document.getElementById('nomineeTable').rows.length;			
		 if(tbl>3)
		 {
			for(var i=0;i<tbl;i++)
			{
				for(var j=i+1;j<tbl-2;j++)
				{

					if(i!=j)
					{					
						if(document.gratuityForm.nomineeName[i].value == document.gratuityForm.nomineeName[j].value ){
							alert("Dublicate nominee selection");
							document.gratuityForm.nomineeAmount[j].focus();
							document.gratuityForm.nomineeMonthlyPensionPayable[j].value = "";
							document.gratuityForm.nomineeName[j].value = "";
							return false;
						}						
					}
				}
			 }
		 }	
	}

	function callNomineeAmount(obj){
		var table = document.getElementById("nomineeTable");
		var rowObj=getRow(obj);
		var totalPayment = eval(document.gratuityForm.totalPayment.value);
		var monthlyPensionPayable = eval(document.gratuityForm.monthlyPensionPayable.value); 
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
			var gratuityAmount = eval(document.gratuityForm.gratuityAmmount.value); 
			document.gratuityForm.total.value = totalPayment;
			document.gratuityForm.totalMonthlyPensionPayable.value = monthlyPensionPayable;
			if(length == 3){			
				document.gratuityForm.nomineeAmount.value = totalPayment;
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
	   document.getElementById("total").value=Math.round(sum);
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
	   document.gratuityForm.totalMonthlyPensionPayable.value=Math.round(sum1);

    }
	  	
  	function hideAllTables(){	  	
  	 	<c:if test="${gratuityForm.gratuityEligible!=null}">  
  	 		document.getElementById("gratuityTable").style.display = "none";
  			document.getElementById("nomineeTable").style.display = "none";
			document.getElementById("nomineeTotalTable").style.display = "none";
  			document.getElementById("saveTable").style.display = "none";
  		</c:if>
  		
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

	function validateOnSave(){	
	/*	if(document.gratuityForm.pensionSanctionNumber.value==""){
			alert("enter pension sanction number");
			document.gratuityForm.pensionSanctionNumber.focus();
			return false;
		}
		if(document.gratuityForm.pensionSanctionAuthority.value==""){
			alert("enter pension sanction authoroty");
			document.gratuityForm.pensionSanctionAuthority.focus();
			return false;
		}
		if(document.gratuityForm.pensionNumber.value==""){
			alert("enter pension number");
			document.gratuityForm.pensionNumber.focus();
			return false;
		}	*/
		document.forms("gratuityForm").action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=saveRecovery";
	    document.forms("gratuityForm").submit();		
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

	 function loadAccountCodes(){
 		var type='getAllGlcodesFromAccount';
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
						acctCodeArray=codes.split("+");
						selectedAcctCode = new YAHOO.widget.DS_JSArray(acctCodeArray);
					  }
				  }
		};
		req2.open("GET", url, true);
		req2.send(null);
	}

	function autocompleteDeduc(obj){
		// set position of dropdown
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+25;
		if(obj.name=='recoveryCode') target.style.left=posSrc[0]+0;

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

					var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedAcctCode);
					oAutoComp1.queryDelay = 0;
					oAutoComp1.useShadow = true;
					oAutoComp1.maxResultsDisplayed = 15;
					oAutoComp1.useIFrame = true;
			}
		}
		yuiflag1[currRow.rowIndex]=1;
	  }
   }

   



</script>

</head>
<body onLoad="onBodyLoad();">
<html:form action ="/pension/gratuityAction" >
	
 	
 	
 	
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
	 <tr>
		 <td><div id="codescontainer"></div></td>
	</tr>
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>Recoveries &nbsp;&nbsp;&nbsp;</b></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}" />
	 	<td class="labelcell"><b>Employee Code</b><font color="red">*</font></td>
	  	<td class="labelcell"><c:out value ="${gratuityForm.pensionDetails.pensionHeader.employee.employeeCode}"/>
	  	
	  	</td>
	  	<td class="labelcell"><b>Employee Name</b></td>
	  	<td class="labelcell">
			<c:out value ="${gratuityForm.pensionDetails.pensionHeader.employee.employeeName}"/>		  	
	  	</td>
    </tr>   
   </table>  

   <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
	<tr>	    
	 	<td class="labelcell">Designation</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="designation" id ="designation" value="${gratuityForm.designation}" readOnly />
	  	</td>
	  	<td class="labelcell">Department</td>
	  	<td class="labelcell">			
		  	<input type="text"  class="fieldcell" name="department" id ="department" value="${gratuityForm.department}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="labelcell">Employee Fund</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="empFund" id ="empFund" value="${gratuityForm.empFund}" readOnly />
	  	</td>	  	
    </tr>  
  </table>	
  <br>
	
 <c:if test="${fn:length(gratuityForm.recoverySet) == 0}">	
	No recoveries
 </c:if>
 <c:if test="${fn:length(gratuityForm.recoverySet) != 0}">	
 <table style="width: 800;" align="center" cellpadding="0" cellspacing="0" border="1" id="recoveryTable">		
	 <tr>	
		<td class="labelcell" >Account Head</td>
		<td class="labelcell" >Amount</td>
		<td class="labelcell" >Reference No</td>
	 </tr>		
	 <c:forEach var="recoveryObj" items="${gratuityForm.recoverySet}">
		 <tr id="recoveryRowId">
			<td class="labelcell">				
				<input type="text" class="fieldcell" name="recoveryCodes" value="${recoveryObj.glcode}"  readonly/>

				<input type="hidden" name="checkRecoveryCode"/>
			<!--	<html:select property="nomineeName" styleClass="fieldcell" onchange="checkDuplicate(this);callNomineeAmount(this);callTotalAmount();">
					<html:option value="">-----------Select-----------</html:option>	
					<c:forEach var="eligibleNomineeObj" items="${gratuityForm.eligibleNomineeSet}">
						<html:option value="${eligibleNomineeObj.id}">${eligibleNomineeObj.firstName}</html:option>
					</c:forEach>
				</html:select>	-->
			</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryAmounts" value="${recoveryObj.amount}" readonly />
			</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryReferenceNos" value="${recoveryObj.reference}" readonly/>
			</td>
		<!---<td class="labelcell">
				 <input  onclick="whichButtonNominee(event,'recoveryTable',this,'recoveryRowId');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
				 <input  onclick="deleteRow('recoveryTable',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" >
			 </td>	-->
		  </tr> 
	</c:forEach>	  
 </table>
</c:if>

	
	 	 
  

   
</html:form>
</body>
</html>
