<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Disburse Gratuity</title>

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
	
	    var nomineesize = ${fn:length(gratuityForm.eligibleNomineeSet)};
        ///calculateNomineeAmount(nomineesize);
		var target="<%=(request.getAttribute("alertMessage"))%>";
		if(target!="null"){
			alert("<%=request.getAttribute("alertMessage")%>");		
		}
	    /// <c:if test = "${gratuityForm.pensionDetails.payTo != 'nominee'}">
			////<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'dbt'}">
				  ///document.getElementById("payeeBanktable").style.display = "block";
		///	</c:if>	
		/// </c:if> -->
	   
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
	
	function calculateNomineeAmount()
	{
	  
	  
	  
	   var pctValue=0;
	   var nomTable= document.getElementById("nomineeTable");
	   var rowLen= nomTable.rows.length;
	   
	   if(rowLen==3)
	   { 
	      
	      if(document.gratuityForm.nomineeGratuityAmount.value=="")
			   {
				   alert('Please fill the Amount');
				   document.gratuityForm.nomineeGratuityAmount.focus();
				   return false;
			   }
			    if(document.gratuityForm.nomineeGratuityAmount.value!=null)
						   {
							    
							   	 	pctValue += eval(document.gratuityForm.nomineeGratuityAmount.value);
							   
						   	}
						
				if(pctValue < document.gratuityForm.gratuityAmount.value)
				{
				     alert('Total Amount has to be '+document.gratuityForm.gratuityAmount.value);
					 return false;
				}
	   }
	   else
	   {
	     for(var i=0;i<rowLen-2;i++)
			   {
			       
				  
				    if(document.gratuityForm.nomineeGratuityAmount[i].value =="")
				   {
					   alert('Please fill the Amount');
					   document.gratuityForm.nomineeGratuityAmount[i].focus();
					   return false;
				   }
				   
				   
				  pctValue += eval(document.gratuityForm.nomineeGratuityAmount[i].value);
				   
				}
				
				if(pctValue < document.gratuityForm.gratuityAmount.value)
				{
				     alert('Total Amount has to be '+document.gratuityForm.gratuityAmount.value);
					 return false;
				}
	   }
	   return true;
	  
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
	
	function validateOnSave()
	{	
	      if(document.gratuityForm.payTo.value=='nominee')
	      {
	        var checkAmount = calculateNomineeAmount();
			if(checkAmount==false)
			{
			  return false;
			}
		}
			
		document.gratuityForm.action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=saveGratuityDisburse";
	    document.gratuityForm.submit();		
	}

	function isChequeAvailable(){
		var type = "isChequeAvailable";
		//alert(document.gratuityForm.payeeId.length);
		var bankAccountId = document.getElementById("payerBankAccount").value;		
		if(bankAccountId != ""){
			var url = "${pageContext.request.contextPath}/commons/gratuityAJAX.jsp?type="+type+ "&bankAccountId="+bankAccountId ;
			var isChequeAvailable;
			var req = initiateRequest();
			req.onreadystatechange = function(){
			  if (req.readyState == 4){
					if (req.status == 200){
						var response = req.responseText
						var result = response.split("/");
						if(result[0]=="true"){
							isChequeAvailable = "ture";
						}
						else if(result[0]=="false"){
							isChequeAvailable = "false";
							//alert("Invalid cheque no!!");
							//obj.focus();
						}
					}
				}
			};
			req.open("GET", url, false);
			req.send(null);		
			return isChequeAvailable;
		}
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
		window.open("../pension/SearchNextChqNo.jsp?accntNoId="+accNoId,"","height=500pt, width=600pt,scrollbars=yes,left=30,top=30,status=yes");		
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
			var url = "/commons/gratuityAJAX.jsp?type="+type+ "&chequeNo="+obj.value+"&bankAccountId="+bankAccountId ;
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
 	
 	
  <table width="96%" cellpadding ="0" cellspacing ="0" border = "0" align="center"  id="employeeTable" >
   	<tr>
   	
   	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Disburse Gratuity</div></td>
   	 <tr>
   	    
   	    
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.pensionDetails.pensionHeader.employee.idPersonalInformation }" />
	 	<td class="whiteboxwk">Employee Code</td>
	  	<td class="whitebox2wk">
	  		<input type="text"   name="employeeCode" id ="employeeCode" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeCode }" readOnly/>
	  	</td>
	  	<td class="whiteboxwk">Employee Name</td>
	  	<td class="whitebox2wk">
		  	<input type="text"  name="employeeName" id ="employeeName" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeName }" readOnly />
	  	</td>
    </tr>
  	<tr>
  		<td><div id="codescontainer"></div></td>
    </tr>
    
   </table>
   

   <table  width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="gratuityTable" >	
    <tr>	    
	 	<td class="greyboxwk">Birth Date</td>
	  	<td class="greybox2wk">
	  		<input type="text"  class="fieldcell" name="birthDate" id ="birthDate" value="${gratuityForm.birthDate}" readOnly />
	  	</td>
	  	<td class="greyboxwk">Superannuation Date</td>
	  	<td class="greybox2wk">
		  	<input type="text"  class="fieldcell" name="supperannuationDate" id ="supperannuationDate" value="${gratuityForm.supperannuationDate}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="whiteboxwk">Designation</td>
	  	<td class="whitebox2wk">
	  		<input type="text"  class="fieldcell" name="designation" id ="designation" value="${gratuityForm.designation}" readOnly />
	  	</td>
	  	<td class="whiteboxwk">Department</td>
	  	<td class="whitebox2wk">			
		  	<input type="text"  class="fieldcell" name="department" id ="department" value="${gratuityForm.department}" readOnly />
	  	</td>
    </tr>  
	<tr>	    
	 	<td class="greyboxwk">Fund</td>
	  	<td class="greybox2wk">
	  		<input type="text"  class="fieldcell" name="empFund" id ="empFund" value="${gratuityForm.empFund}" readOnly />
	  	</td>
	  	
	  	<td class="greyboxwk">Employee Status</td>
	  	<td class="greybox2wk">
	  		<input type="text"  class="fieldcell" name="employeeStatus" id ="employeeStatus" value="${gratuityForm.pensionDetails.pensionHeader.employee.statusMaster.description }" readOnly/>
	  	</td>
    </tr>  
	<tr> 	
	  	<td class="whiteboxwk">Disburse Gratuity to</td>
	  	<td class="whitebox2wk">
			<c:if test ="${gratuityForm.pensionDetails.pensionHeader.employee.statusMaster.description == 'Deceased'}">
				<b>Nominee(s)</b>
				<input type="hidden" name="payTo" value="nominee"  />
			</c:if>	
			<c:if test = "${gratuityForm.pensionDetails.pensionHeader.employee.statusMaster.description == 'Retired'}">
				<b>Employee</b>
				<input type="hidden" name="payTo" value="employee"  />
			</c:if>					
				
	  		
	  	</td>	  	
	 </tr> 	 
	 
	
	
	
   	

	<table width="96%" align="center" cellpadding="0" cellspacing="0" border="0" id="payeeBankTable" >
		<tr>
		<td class="greyboxwk">Gratuity Amount</td>
			<td class="greybox2wk">
				<input type="text"    name="gratuityAmount" value="${gratuityForm.gratuityAmount}" readOnly/>
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
		<td id="3Row" class="tablesubheadwk" width="7%"><font color="red">*</font>Amount</td>
			
		</tr>
		<br>
		<tr>
		
		   
				<c:forEach var="nomineeObj" items="${gratuityForm.eligibleNomineeSet}">
				<tr>
				  <input type="hidden" class="selectwk textmxwidth2" name="nomineeId" value="${nomineeObj.id}" />
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
				    <td><input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.bankBranch.branchname}" readonly/></td>
				    <td><input type="text" class="selectwk textmxwidth2" value="${nomineeObj.nomineeMstr.accountNumber}"readonly /></td>
				  <td>  <input type="text" class="selectwk textmxwidth2" value="${nomineeObj.percentage}" readonly/></td>
				   <td><input type="text" class="selectwk textmxwidth2" name="nomineeGratuityAmount" id="nomineeGratuityAmount" value="${nomineeObj.gratutiyAmount}" onblur="checkdecimalval(this,this.value);"/><td>
				   </td>
				   </tr>
		      </c:forEach>
				
			
			
		</tr>
		</c:if>
		</tbody>
		</table>
		
</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
	    
		<table id="saveTable">
			 <tr>
				<td class="labelcell" align="right">
					
				</td>	
			</tr>
		</table>
	
	 	 
 

   
<div class="buttonholderwk">
<input type="button" value="Save" class="buttonfinal" onclick="validateOnSave();"/>		
<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close()"/></div>

<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
</html:form>
</body>
</html>
