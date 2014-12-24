<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Record Recoveries </title>

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
	//Set to remember the rows being deleted
	Set delRecoveries = new HashSet();
	session.setAttribute("delRecoveries", delRecoveries);

%>
<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;	
	var acctCodeArray;
	var selectedAcctCode;
	var yuiflag = new Array();

   function onBodyLoad(){  		
	   loadAccountCodes(); 
	   <c:if test = "${gratuityForm.errorMessage != null}">
		   alert("Recoveries are there in another fund,First need to run IUT");
			window.location = "${pageContext.request.contextPath}/recordRecovery/search.jsp?mode=create";
		</c:if>
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
	

	function whichButtonRecovery(e,tbl,obj,objr){
		var F2 = 113;
		var del = 46;
		var code;
		if ( !e )
		 var e = window.event;
		if ( e.keyCode ) code = e.keyCode ;
		else if ( e.which ) code = e.which ;	
		//alert(obj.value);	
		if(checkRecovery())
			addRowToTable(tbl,obj);
	}

	function checkRecovery(){
		var tbl = document.getElementById("recoveryTable");		
		var len = tbl.rows.length	
		if(len == 2){			
			if(document.gratuityForm.recoveryCodes.value == ""){
				alert("Eneter recovery code");
				document.gratuityForm.recoveryCodes.focus();
				return false;
			}	
			if(document.gratuityForm.recoveryAmounts.value == ""){
				alert("Eneter recovery amount");
				document.gratuityForm.recoveryAmounts.focus();
				return false;
			}	
		}
		else if(len > 2){
			for(var i=0 ; i<len-1 ; i++ ){
				if(document.gratuityForm.recoveryCodes[i].value == ""){
					alert("Eneter recovery code");
					document.gratuityForm.recoveryCodes[i].focus();
					return false;
				}
				if(document.gratuityForm.recoveryAmounts[i].value == ""){
					alert("Eneter recovery amount");
					document.gratuityForm.recoveryAmounts[i].focus();
					return false;
				}				
			}
		}
		return true;
	}

	function deleteRow(table,obj){		
		if(table=='recoveryTable'){
			var tbl = document.getElementById(table);
			var rowNumber=getRow(obj).rowIndex;	
			var rowObj = getRow(obj);
			if(tbl.rows.length > 2){
				var recoveryCode = getControlInBranch(tbl.rows[rowObj.rowIndex],'recoveryCodes').value;
				populateDeleteSet("delRecoveries" , recoveryCode);
			    tbl.deleteRow(rowNumber);
			}
			else{
				alert("You cannot delete this row");
				return false;
			}
		}		
	}
	
	function populateDeleteSet(setName, delId){
		var http = initiateRequest();   
		var url = "${pageContext.request.contextPath}/commons/updateDelSets.jsp?type="+setName+"&id="+delId;		      
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4) 
			{
				if (http.status == 200) 
				{
				       var statusString =http.responseText.split("^");
	
				       
				 } 
			} 
		};
		http.send(null);   
	}

	function addRowToTable(tbl,obj){
		  tableObj=document.getElementById(tbl);
		  var rowObj1=getRow(obj);
		  var tbody=tableObj.tBodies[0];
		  var lastRow = tableObj.rows.length;		 
		  //var checkRowLength = eval(getControlInBranch(tableObj.rows[rowObj1.rowIndex],'recoveryCode').options.length) + 1;
		  //FIXME: if tbl has no rows, cloneNode will not work
		  if(tbl=='recoveryTable'){	
			   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
			   tbody.appendChild(rowObj);	
			   var remlen = document.gratuityForm.recoveryCodes.length;	
			   document.gratuityForm.glcodes[remlen-1].value="0";
			   document.gratuityForm.recoveryCodes[remlen-1].value="";
			   document.gratuityForm.recoveryAmounts[remlen-1].value="";			   
			   document.gratuityForm.recoveryReferenceNos[remlen-1].value="";
		  }
		 
	}

	

	function validateOnSave(){	
		var tbl = document.getElementById("recoveryTable");		
		var len = tbl.rows.length;		
		if(len == 2){
			if(document.gratuityForm.checkRecoveryCode.value == "undefined"){
				alert("Enter correct recovery code");
				document.gratuityForm.recoveryCodes.focus();
				return false;
			}
		}
		if(len > 2){
			for(var i=0; i< len-1; i++){
				if(document.gratuityForm.recoveryCodes[i].value == ""){
					alert("Enter recovery code");
					document.gratuityForm.recoveryCodes[i].focus();
					return false;
				}
				if(document.gratuityForm.recoveryAmounts[i].value == ""){
					alert("Enter recovery amount");
					document.gratuityForm.recoveryAmounts[i].focus();
					return false;
				}	
				if(document.gratuityForm.checkRecoveryCode[i].value == "undefined"){
					alert("Enter correct recovery code");
					document.gratuityForm.recoveryCodes[i].focus();
					return false;
				}
			}
		}		
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
		if(obj.name=='recoveryCodes') target.style.left=posSrc[0]+0;

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
		yuiflag1[currRow.rowIndex]=undefined;
	  }
   }

   function fillNeibrAfterSplit1(obj){
		var currRow=getRow(obj);		
		yuiflag[currRow.rowIndex] = undefined;		
		var temp = obj.value;
		temp = temp.split("`-`");		
		var xyz = getControlInBranch(currRow,'recoveryCodes');
		getControlInBranch(currRow,'recoveryCodes').value = temp[0];		
		getControlInBranch(currRow,'checkRecoveryCode').value = temp[1];
		if(temp[1] == undefined){
			//alert("Enter correct glcode");
			//getControlInBranch(currRow,'recoveryCode').focus();
			return false;
		}
		if(obj.value==null || obj.value=="") {
			//neibrObj1.value="";
			return; 
		}		
		else {			
			//getControlInBranch(currRow,'recoveryAmounts').value="";
			getControlInBranch(currRow,'recoveryAmounts').focus();
		 }
   }

//FIXME: struts validation
   function checkdecimalval(obj,amount){		
	    var objt = obj;
	    var amt = amount;
	    if(amt != null && amt != "")
	    {
	     /* if(amt < 0 )
	        {
	            alert("Please enter positive value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }*/
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}

	function checkDublicate(obj){
		var tbl = document.getElementById('recoveryTable').rows.length;		
		var rowObj=getRow(obj);
		if(tbl>2)
		{
			for(var i=0;i<tbl;i++)
			{
				for(var j=i+1;j<tbl-1;j++)
				{
					if(i!=j)
					{
						if(document.gratuityForm.recoveryCodes[i].value==document.gratuityForm.recoveryCodes[j].value)
						{
							alert("Duplicate Selection of recovery AccountCode!!!");
							document.gratuityForm.recoveryCodes[j].value = "";
							document.gratuityForm.recoveryCodes[j].focus();
							return false;
						 }
					}
				}
			 }
		 }
    }
	


</script>

</head>
<body onLoad="onBodyLoad();">
<html:form action ="/pension/gratuityAction" >	
 	
 	
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
  <div id="codescontainer"></div>
	 <tr>
		 <td></td>
	</tr>
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>Record Recoveries &nbsp;&nbsp;&nbsp;</b></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}" />
	 	<td class="labelcell"><b>Employee Code</b><font color="red">*</font></td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="employeeCode" id ="employeeCode" value="${gratuityForm.employeeCode }" readOnly/>
	  	</td>
	  	<td class="labelcell"><b>Employee Name</b></td>
	  	<td class="labelcell">
		  	<input type="text"  class="fieldcell" name="employeeName" id ="employeeName" value="${gratuityForm.employeeName }" readOnly />
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
	
 <table style="width: 800;" align="center" cellpadding="0" cellspacing="0" border="1" id="recoveryTable">		
	 <tr>	
		<td class="labelcell" >Account Head</td>
		<td class="labelcell" >Amount</td>
		<td class="labelcell" >Reference No</td>
	 </tr>		
	 <c:if test="${fn:length(gratuityForm.recoverySet) == 0}">
			<tr id="recoveryRowId">
			<td class="labelcell">	
				<input type="hidden" name="glcodes"  />			
				<input type="text" class="fieldcell" name="recoveryCodes"  autocomplete="off" onkeyup="autocompleteDeduc(this);" onblur="fillNeibrAfterSplit1(this);checkDublicate(this);" />

				<input type="hidden" name="checkRecoveryCode"/>
			
			</td>

			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryAmounts" onblur="trim(this,this.value);checkdecimalval(this,this.value);"/>
			</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryReferenceNos" />
			</td>
			<td class="labelcell">
				 <input  onclick="whichButtonRecovery(event,'recoveryTable',this,'recoveryRowId');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
				 <input  onclick="deleteRow('recoveryTable',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" >
			 </td>
		  </tr> 
	</c:if>
	<c:if test="${fn:length(gratuityForm.recoverySet) != 0}">
 		 <c:forEach var="recoveryObj" items="${gratuityForm.recoverySet}">
		 <tr id="recoveryRowId">
			<td class="labelcell">	
				<input type="hidden" name="glcodes" value="${recoveryObj.glcode}" />			
				<input type="text" class="fieldcell" name="recoveryCodes" value="${recoveryObj.glcode}" autocomplete="off" onkeyup="autocompleteDeduc(this);" onblur="fillNeibrAfterSplit1(this);checkDublicate(this);" />

				<input type="hidden" name="checkRecoveryCode"/>
			
			</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryAmounts" value="${recoveryObj.amount}" onblur="trim(this,this.value);checkdecimalval(this,this.value);"/>
			</td>
			<td class="labelcell">
				<input type="text"  class="fieldcell" name="recoveryReferenceNos" value="${recoveryObj.reference}" />
			</td>
			<td class="labelcell">
				 <input  onclick="whichButtonRecovery(event,'recoveryTable',this,'recoveryRowId');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
				 <input  onclick="deleteRow('recoveryTable',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" >
			 </td>
		  </tr> 
	</c:forEach>	  
	</c:if>
	 
	 
 </table>

	<table id="saveTable">
		 <tr>
			<td class="labelcell" align="right">
			<input type="button" value="Save" onclick="validateOnSave();"/>			
			</td>	
		</tr>
	</table>
	 	 
  

   
</html:form>
</body>
</html>
