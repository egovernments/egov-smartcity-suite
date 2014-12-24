<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<%@ page import="org.egov.payroll.dao.*,org.egov.infstr.*,org.egov.commons.dao.*,org.egov.commons.*,org.egov.payroll.model.*,org.egov.payroll.utils.PayrollExternalInterface,org.egov.payroll.utils.PayrollExternalImpl"%>

<html>
<head>
	<title>Search Exception</title>
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
	PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
		List years = new ArrayList();
	    years = payrollExternalInterface.getAllActiveFinancialYearList();
	
	    List status = new ArrayList();
		status = payrollExternalInterface.getStatusByModule("EmpException");
	%>

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
	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
	else {
			document.getElementById("employeeName").value=temp[1];
			neibrObj.value=temp[2];			
		 }
 }
 
 function loadEmpCodes()
 { 	
	 var type='getEmployedEmpCodes';
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
  
  function callSearch(){
	if(document.exceptionForm.financialYear.value==""){
		alert("Enter financial year");
		document.exceptionForm.financialYear.focus();
		return false;
	}
	if(document.exceptionForm.month.value == ""){
		alert("Enter month");
		document.exceptionForm.month.focus();
		return false;
	}
  	document.exceptionForm.action ="${pageContext.request.contextPath}/exception/beforeException.do?submitType=showExceptions";
  }
 		
 	
</script>
</head>

<body onLoad="onBodyLoad();" >
<html:form  action="/exception/beforeException">
	<center>
	<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
								    <span class="bold">Today:</span> <egovtags:now/>
								</div>
									<table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
									    <tr>
									    	<td colspan="4"   class="headingwk">
									    	<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
									    	<div class="headplacer">Show exceptions &nbsp;&nbsp;&nbsp;</div></td>
									    </tr>
											   
									    <tr>
									    	<td class="whiteboxwk"><span class="mandatory">*</span>Year</td>
									    	<td class="whitebox2wk">
									    		<html:select property="financialYear">
													<html:option value="">-----------Select------------</html:option>
												  	<%
												  		for(int i=0;i<years.size();i++){
												  			CFinancialYear year = (CFinancialYear)years.get(i);
													%>
														<html:option value="<%=year.getId().toString()%>"><%=year.getFinYearRange()%></html:option>
								
													<%
														}
													%>
												</html:select>
									    	</td>
									    	<td class="whiteboxwk"><span class="mandatory">*</span>Month</td>
									    	<td class="whitebox2wk">
									    		<html:select property="month">
												<html:option value="">-----------Select------------</html:option>
												<html:option value="1">JAN</html:option>
												<html:option value="2">FEB</html:option>				
												<html:option value="3">MARCH</html:option>
												<html:option value="4">APRIL</html:option>
												<html:option value="5">MAY</html:option>
												<html:option value="6">JUNE</html:option>
												<html:option value="7">JULY</html:option>
												<html:option value="8">AUG</html:option>
												<html:option value="9">SEPT</html:option>
												<html:option value="10">OCT</html:option>
												<html:option value="11">NOV</html:option>
												<html:option value="12">DEC</html:option>
												</html:select>
									    	</td>
									    </tr>
									    <tr>
									    	<td class="greyboxwk">Exception state</td>
									    	<td class="greybox2wk" colspan="4">
									    		<html:select property="exceptionStatus">
													<html:option value="">-----------Select------------</html:option>
												  	<%
												  		for(int i=0;i<status.size();i++){
												  			EgwStatus es = (EgwStatus)status.get(i);
													%>
														<html:option value="<%=es.getId().toString()%>"><%=es.getDescription()%></html:option>
								
													<%
														}
													%>
												</html:select>
									    	</td>
									    </tr>
									    <tr>
						               		<td colspan="4" class="shadowwk"></td>
							            </tr>
										
										<tr>
										 	<td colspan="4" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
										</tr>
							            
							           <tr>
									       <td align="center" colspan="4"> </td>
							           </tr>	
									</table> 
								</div>
							</div>
						</div>
					</div>
					
					<div class="buttonholderwk">
						<html:submit value="View/Modify" onclick="return callSearch();" />
						<html:button property="actionType" value="Close" onclick="window.close();" />	    		
					</div>	
					<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
			</center>
		</html:form>
	</body>
</html>
