<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>
	<title>Salary advance sanction</title>
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
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();


  
 function onBodyLoad()
 {  
   loadEmpCodes();   
 }

 function initiateRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
   }
/*
* This function returns absolue left and top position of the object
*/
	function findPos(obj)
	{
		var curleft = curtop = 0;
		if (obj.offsetParent)
		{
			curleft = obj.offsetLeft;
			curtop = obj.offsetTop;
			while (obj = obj.offsetParent)
			{	
				curleft =curleft + obj.offsetLeft;
				curtop =curtop + obj.offsetTop; //alert(curtop);
			}
		}
		return [curleft,curtop];
	}
function getRow(obj)
 {
 	if(!obj)return null;
 	tag = obj.nodeName.toUpperCase();
 	while(tag != 'BODY'){
 		if (tag == 'TR') return obj;
 		obj=obj.parentNode;
 		tag = obj.nodeName.toUpperCase();
 	}
 	return null;
 }

 function autocompleteEmpCode(obj)
 {
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

function getControlInBranch(obj,controlName)
{
	if (!obj || !(obj.getAttribute)) return null;
	if (obj.getAttribute('name') == controlName) return obj;

	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}
function fillNeibrAfterSplit(obj,neibrObjName)
{
	var currRow=getRow(obj);	
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;temp = temp.split("`-`");
	obj.value=temp[0];
	document.advanceForm.checkEmpCode.value = temp[1];
	if(temp[1] == null)
		getEmployeeByEnteringCode(temp[0],neibrObj);
	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
	else {
			document.getElementById("employeeName").value=temp[1];
			neibrObj.value=temp[2];			
		 }
 }

 function getEmployeeByEnteringCode(code,empId){
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=getEmployeeByCode&code="+code;
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
						document.advanceForm.checkEmpCode.value = emp[1];  	
				  		document.getElementById("employeeName").value = emp[1];
					}
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  }
 
 function loadEmpCodes()
 { 	
	 var type='getAllEmployeeCodes';
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +type+ " ";
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
   
   function removeZero(obj){
		if(obj.value==0)
		{	
			obj.value="";
			obj.focus();	
		}
		return true;
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
	
	function addZero(obj){
		if(obj.value=="")
		{
			obj.value=0;
		}
		collectionSum();
		calOnchangeAmount(obj);
		return;
	}   
	
	function checkOnSubmit(){
		if(document.advanceForm.checkEmpCode.value == "undefined"){
			alert('<bean:message key="alertCorrectEmployeeCode"/>');	
			document.advanceForm.employeeCode.focus();
			return false;
		}
	}
 	
</script>
</head>

<body onLoad="onBodyLoad();" >
<html:form  action="/salaryadvance/beforeSanction">
	<input type="hidden" name="checkEmpCode"/>
	<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		
	    <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Sanction Advance</div></td>
	</tr>
		
	
	    <tr>
	 	 <input type="hidden" name="employeeCodeId" id="employeeCodeId">
	  		<td class="whiteboxwk"><bean:message key="EmployeeCode"/></td>
	  		<td class="whitebox2wk">
	  			<input type="text" class="selectwk" name="employeeCode" id="employeeCode" autocomplete="off"
	  			 onkeyup="autocompleteEmpCode(this);" onblur="fillNeibrAfterSplit(this,'employeeCodeId');
		  			 trim(this,this.value);" >
  			</td>
		  	<td class="whiteboxwk"><bean:message key="EmployeeName"/></td>
		  	<td class="whitebox2wk"><input type="text"  class="selectwk" name="employeeName" id ="employeeName" readOnly></td>
	    </tr>
	  	<tr>
	  		<td><div id="codescontainer"></div></td>
	    </tr>  
	    <tr>
	    	<td class="labelcell" colspan="4" >&nbsp;</td>	    	
	    </tr>
	</table> 
<table align='center' id="table2">	
 		<tr>
	    	<td class="whiteboxwk">
	    		<html:submit property="action" value="search" onclick="return checkOnSubmit();" styleClass="buttonfinal"/>	
	    		
			</td>    
	    </tr>	
</table>	
</html:form>
</body>
</html>
