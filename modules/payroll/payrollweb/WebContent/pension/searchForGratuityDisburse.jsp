<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Gratuity compute </title>

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

%>
<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;	
	function onBodyLoad(){  	
		var target="<%=(request.getAttribute("alertMessage"))%>";
		if(target!="null"){
			alert("<%=request.getAttribute("alertMessage")%>");		
		}	
	   loadEmpCodes(); 
	   
	}	
	
    function autocompleteEmpCode(obj,event){
	 	// set position of dropdown
	 	var src = obj;
	 	var target = document.getElementById('codescontainer');
	 	var posSrc=findPos(src);
	 	target.style.left=posSrc[0];
	 	target.style.top=posSrc[1]+25;
	 	if(obj.name=='employeeCode') 
	 	{
	 	   target.style.left=posSrc[0]+0;
	 	 }
	
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
				var oAutoCompPos = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
				oAutoCompPos.queryDelay = 0;
				oAutoCompPos.useShadow = true;
				oAutoCompPos.maxResultsDisplayed = 15;
				//oAutoCompPos.useIFrame = true;
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
		document.gratuityForm.checkEmpCode.value = temp[1];
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
		 var type='getAllGratuityDisbursableEmployee';
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
 	
 	function getEmployeeByEnteringCode(code,empId){
 	    if(code!='Auto fill')
 	    {
			var url = "${pageContext.request.contextPath}/commons/process.jsp?type=getGratuityDisbursableEmployeeByCode&code="+code;
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
							document.gratuityForm.checkEmpCode.value = emp[1];  	
					  		document.getElementById("employeeName").value = emp[1];
						}
		           }
		       }
	        };
		   req.open("GET", url, true);
		   req.send(null);
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
	
	function checkForGratuityDisburseByEmp(){
		var type = "getDisbursedPensionDetailsByEmp";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/pension/gratuityAJAXAction.do?type="+type+ "&empId="+empId ;
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
		var type = "validateEmployeeGratuityEligibility";
		var empId = document.getElementById("employeeCodeId").value;		
		var url = "${pageContext.request.contextPath}/pension/gratuityAJAXAction.do?type="+type+ "&empId="+empId ;
		var isExist;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var response = req.responseText
					var result = response.split("/");
                   	if(result[0]=="true"){
                   		isExist = "true";
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
		var mode = "<%= mode%>";	
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

	<%	if("create".equals(mode)){	%>
			if(checkForGratuityDisburseByEmp() == "exist"){
				alert("Gratuity disburse already done for this employee");
				document.gratuityForm.employeeCode.focus();
				return false;
			}
			
			 document.gratuityForm.action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=beforeGratuityDisburse";
	<%	}	
		if("view".equals(mode)){	%>    	 		
			if(checkForGratuityDisburseByEmp() == "notExist"){
				alert("Gratuity disburse not done for this employee");
				document.gratuityForm.employeeCode.focus();
				return false;
			}
			 document.gratuityForm.action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=viewGratuityDisburse&mode="+mode;
	<%	}	%>		

	  
  	   ///document.forms("gratuityForm").action ="/pension/gratuityAction.do?submitType=beforeGratuityDisburse";
	   document.gratuityForm.submit();
  	} 	

function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='black';
		}
}
function setBlur(obj,defaultval)
{
	if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
		document.getElementById(obj.id).style.color='';
	}
}

</script>

</head>
<body onLoad="onBodyLoad();">
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
<html:form action ="/pension/gratuityAction" >
	
 	<input type="hidden" name="checkEmpCode"/>
 	
 	
  <table width="95%" cellpadding="0" cellspacing="0" border="0" border="0" id="employeeTable" >
   	<tr>
	    
			
			<%	if("create".equals(mode)){	%>
    	 		<td colspan="4" class="headingwk">
	         	<div class="arrowiconwk">
	         		<img src="../common/image/arrow.gif" />
	         	</div>
	           	<div class="headplacer">
		           	Gratuity Disburse
				</div>
          	</td>
			<%	}			
			if("view".equals(mode)){	%>
    	 		<td colspan="4" class="headingwk">
	         	<div class="arrowiconwk">
	         		<img src="../common/image/arrow.gif" />
	         	</div>
	           	<div class="headplacer">
		           	View
				</div>
          	</td>
			<%	}	%>
									
	
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}" />
	 	<td class="whiteboxwk" width="15%"><font color="red">*</font>Employee Code</td>
	  	<td class="whitebox2wk" width="26%">
	  		<input type="text"   name="employeeCode" id ="employeeCode" autocomplete="off"
	  		 onkeyup="autocompleteEmpCode(this,event);" onblur="setBlur(this,'Auto fill');fillNeibrAfterSplit(this,'employeeCodeId');
	  		 trim(this,this.value);" value="Auto fill" class="selectwk grey" onfocus="setFocus(this,'Auto fill')"/>
	  	</td>
	  	<td class="whiteboxwk" width="14%">Employee Name</td>
	  	<td class="whitebox2wk" width="45%">
		  	<input type="text"  name="employeeName" id ="employeeName" value="${gratuityForm.employeeName }" readOnly />
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
			
			
		
    	</td>
   	</tr>
   </table>
   
   </div>	
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
		<%	if("create".equals(mode)){	%>
    	 		<html:submit property="actionType" styleClass="buttonfinal" value="Disburse Gratuity" onclick="return validateOnSearch();"/>
			<%	}
				if("view".equals(mode)){	%>
    	 		<html:submit property="actionType" styleClass="buttonfinal" value="Disburse View" onclick="return validateOnSearch();"/>
			<%	}	%>	
	</div>
				<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</center>
</html:form>
</body>
</html>
