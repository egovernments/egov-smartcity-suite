<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,
                org.egov.pims.model.*,
				org.egov.pims.commons.DesignationMaster,
				org.egov.lib.rjbac.dept.Department" %>
<html>
<head>

	<title>Nominee Transfer</title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>


<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;	
	function onBodyLoad(){  		
	   loadEmpCodes();
	   execute();
			
	   
	   
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
   
   function fillNeibrAfterSplit(obj,neibrObjName){
		var currRow=getRow(obj);	
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj=getControlInBranch(currRow,neibrObjName);
		var temp = obj.value;
		temp = temp.split("`-`");
		document.pensionNomineeTransferForm.checkEmpCode.value = temp[1];
		obj.value=temp[0];		
		if(temp[1]!=null){			
	  		document.getElementById("employeeName").value = temp[1];
			neibrObj.value = temp[2]
		}
		else{
			getEmployeeByEnteringCode(temp[0],neibrObj);
			document.getElementById("employeeName").value = "";
		}

 	}
 	
 	function loadEmpCodes() { 	
		 var type='getAllPensionEligibleEmployee';
			var url = "<%=request.getContextPath()%>/commons/process.jsp?type=" +type+ " ";
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
 	
 	function getEmployeeByEnteringCode(code,empId){
		var url = "<%=request.getContextPath()%>/commons/process.jsp?type=getPensionEligibleEmployeeByCode&code="+code;
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText;					
                   	var a = glcodes.split("^");
                   	var codes = a[0];								
					var emp = codes.split("`-`");					
					if(codes == ""){
						//alert("Enter correct glcode");
						//document.payheadForm.glcode.focus();
					}
					else{
						empId.value = emp[2];
						document.pensionNomineeTransferForm.checkEmpCode.value = emp[1];  	
				  		document.getElementById("employeeName").value = emp[1];
					}
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
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
	

   function validateOnSearch(){	
	   
	  
		if(document.pensionNomineeTransferForm.employeeCode.value == ""){
			alert("Enter employee code");	
			document.pensionNomineeTransferForm.employeeCode.focus();
			return false;
		}
		if(document.pensionNomineeTransferForm.checkEmpCode.value == "undefined"){
			alert("Enter correct employee code");	
			document.pensionNomineeTransferForm.employeeCode.focus();
			return false;
		}
		
	   var empId = document.pensionNomineeTransferForm.employeeCodeId.value;
	   
  	   document.forms("pensionNomineeTransferForm").action ="<%=request.getContextPath()%>/nomineeTransfer/AfterNomineeAction.do?submitType=searchNominee&empId="+empId;
	   document.forms("pensionNomineeTransferForm").submit();
  	} 	


function execute()
{
	
	var target="<%=(request.getAttribute("alertMessage"))%>";

	 if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}
	


</script>

</head>
<body onLoad="onBodyLoad();">

<html:form action ="/nomineeTransfer/AfterNomineeAction" >
	
 	<input type="hidden" name="checkEmpCode"/>
 	
 	
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>
		
		&nbsp;&nbsp;&nbsp;</b></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId"  />
	 	<td class="labelcell"><b>Employee Code</b><font color="red">*</font></td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="employeeCode" id ="employeeCode" autocomplete="off"
	  		 onkeyup="autocompleteEmpCode(this);" onblur="fillNeibrAfterSplit(this,'employeeCodeId');
	  		 trim(this,this.value);" value="${pensionNomineeTransferForm.employeeCode }" />
	  	</td>
	  	<td class="labelcell"><b>Employee Name</b></td>
	  	<td class="labelcell">
		  	<input type="text"  class="fieldcell" name="employeeName" id ="employeeName" value="${pensionNomineeTransferForm.employeeName }" readOnly />
	  	</td>
    </tr>
  	<tr>
  		<td><div id="codescontainer"></div></td>
    </tr>
    
   </table>
   
   <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="0" id="sumbitTable" >
    <tr>
	    <td class="labelcell"></td>
    	<td class="labelcell" align="center">
			<html:submit property="actionType" value="Search" onclick="return validateOnSearch();"/>
    	</td>
   	</tr>
   </table>

  
</html:form>
</body>
</html>
