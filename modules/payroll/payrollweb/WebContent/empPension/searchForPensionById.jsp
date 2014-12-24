<%@ include file="/includes/taglibs.jsp" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Serach Pension Details</title>
    <style type="text/css">
			#codescontainer {position:absolute;left:11em;width:9%}
			#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
			#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
			#codescontainer ul {padding:5px 0;width:80%;}
			#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			#codescontainer li.yui-ac-highlight {background:#ff0;}
			#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

		<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
		<script>
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
		var mode= "view";
		function submitForm()
		{
			if(document.employeePensionForm.employeeCode!=null && document.employeePensionForm.employeeCode.value != "")
			{
				mode="<%=(request.getParameter("mode"))%>";
		        document.employeePensionForm.action="/empPension/AfterPensionSearchAction.do?submitType="+mode;
				document.employeePensionForm.submit();
			 }
			else
			{
				 alert("please fill the employee Code");
				 document.employeePensionForm.employeeCode.focus();
				 return false;
			}
		}
		function checkMode()
		{
		   var target="<%=(request.getAttribute("alertMessage"))%>";
		   if(target!="null")
		   {
			alert("<%=request.getAttribute("alertMessage")%>");

		   }
		}

 function onBodyLoad()
 {
   loadEmpCodes();
 }
 function loadEmpCodes()
 {
	 var type='getAllActiveOrInactiveEmployeeCodes';
		var url = "/commons/process.jsp?type=" +type+ " ";
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
 function fillNeibrAfterSplit(obj,neibrObjName)
 {
 	var currRow=getRow(obj);
 	yuiflag[currRow.rowIndex] = undefined;
 	neibrObj=getControlInBranch(currRow,neibrObjName);
 	var temp = obj.value;
 	temp = temp.split("`-`");
 	document.employeePensionForm.employeeId.value = temp[2];
 	obj.value=temp[0];
 	if(temp[0] == null)
 		getEmployeeByEnteringCode(temp[0],neibrObj);
 	if(temp[2]==null || temp[2]=="")
 	{
		neibrObj.value="";
 	return;
 	}
 	if(temp[0]==null && (neibrObj.value!='' || neibrObj.value!=null) )
 	{  return ;
 	}
 	else {

 		neibrObj.value=temp[2];
 	}

 }
 function getEmployeeByEnteringCode(code,empId){
 		var url = "/commons/process.jsp?type=getEmployeeByCode&code="+code;
       	var req = initiateRequest();
       	req.onreadystatechange = function(){
 	      if (req.readyState == 4){
 	            if (req.status == 200){
                    	var glcodes=req.responseText
                    	var a = glcodes.split("^");
                    	var codes = a[0];
 					var emp = codes.split("`-`");
 					if(codes == "false"){
 						//alert("Enter correct glcode");
 						//document.payheadForm.glcode.focus();
 					}
 					else{
 						empId.value = emp[2];
 						document.employeePensionForm.employeeId.value  = emp[2];

 					}
 	           }
 	       }
         };
 	   req.open("GET", url, true);
 	   req.send(null);
  }

  function findPos(obj)
  	{
  		var curleft = curtop = 0;
  		if (obj.offsetParent)
  		{
  			curleft = obj.offsetLeft;
  			curtop = obj.offsetTop;
  			while (obj = obj.offsetParent)
  			{	//alert(obj.nodeName);
  				curleft =curleft + obj.offsetLeft;
  				curtop =curtop + obj.offsetTop; //alert(curtop);
  			}
  		}
  		return [curleft,curtop];
	}

 function autocompleteEmpCode(obj)
  {
  	// set position of dropdown
  	var src = obj;
  	var target = document.getElementById('codescontainer');

  	var posSrc=findPos(src);

  	target.style.left=posSrc[0];
  	target.style.top=posSrc[1]+25;
  	if(obj.name=='code') target.style.left=posSrc[0]+0;

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


</script>
</head>
<body onload="checkMode();onBodyLoad()" >
<html:form  action="/empPension/AfterPensionSearchAction.do">

<table  style="width: 930;" cellpadding ="0" cellspacing ="0" border = "1"  >
<tbody>
<tr>
  <td colspan="10" height=20 bgcolor=#dddddd align=middle  class="tableheader">
<p>Search Employee For Pension</p></td>
  </tr>
  <tr>
  <input type="hidden" name="employeeId" id="employeeId">
  <td class="labelcell">Employee Code<font color="red">*</font></td>
  <td class="labelcell"><input type="text"  class="fieldcell" name="employeeCode" id ="employeeCode" autocomplete="off"
   onkeyup="autocompleteEmpCode(this);"onblur="fillNeibrAfterSplit(this,'employeeId');trim(this,this.value);">
   </td>
  <tr>
  	  		<td><div id="codescontainer"></div></td>
	    </tr>
</tbody>
</table>


<table id = "submit" style="width: 930;"  cellpadding ="0" cellspacing ="0" border = "1" >
<tr align = "center">
  <td><html:button styleClass="button" value="Search" property="b4" onclick="return submitForm();" /></td>

</tr>
</table>
</html:form>
</body>
</html>