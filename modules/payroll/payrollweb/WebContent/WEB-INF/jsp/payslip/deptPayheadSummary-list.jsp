<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ page buffer = "16kb" %>
<%@ page language="java" 
	import="java.util.*,java.sql.*,org.egov.infstr.utils.EgovMasterDataCaching,
			org.egov.infstr.utils.*,
			org.egov.payroll.utils.PayrollConstants,
			org.egov.payroll.utils.PayrollManagersUtill" %>

<html> 

<head>

	<title><bean:message key="dept.payhead.summary"/> </title>
<%
List columnNamesArr =new ArrayList();
List payslipHistoryDetailsList = (ArrayList)request.getAttribute("payslipSet");

if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()>1){	
	System.out.println("size============="+payslipHistoryDetailsList.size());							      	
	Map payslipMap = (LinkedHashMap) payslipHistoryDetailsList.get(0);
	columnNamesArr.addAll(payslipMap.keySet());
}

%>

<script language="JavaScript"  type="text/JavaScript">
function getReport(format) 
{
	if(checkOnSubmit())
	{
		document.deptPayheadSumForm.fileFormat.value=format;	
		return true;
	}
	else
	   return false;
}

var empCodeArray;
var selectedEmpCode;
var yuiflag = new Array();
var employeeName;	
		
function fillNeibrAfterSplit(obj,neibrObjName)
{
 	var currRow=getRow(obj);
 	yuiflag[currRow.rowIndex] = undefined;
 	neibrObj=getControlInBranch(currRow,neibrObjName);
 	var temp = obj.value;temp = temp.split("`-`");
 	
 	if(temp[1] == null || temp[1]=='undefined')
	   getEmployeeByEnteringCode(temp[0],currRow);	
 	
 	obj.value=temp[0];
 	employeeName=temp[1];	
	designation=temp[3];
	department=temp[4];
	yearOfJoining=temp[5];
	payScaleName=temp[6];
	empcode=temp[0];
 	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else{
		neibrObj.value=temp[2];
 			
 	}

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
  	if(yuiflag[currRow.rowIndex] == undefined)
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
  	yuiflag[currRow.rowIndex]=1;
   }
}

function onBodyLoad()
{
  	<%
  	String msg=(String)request.getAttribute("alertMessage");
  	if( msg!= null )
  	{
  	%>
  	   alert("<%=msg %>");
  	   //document.getElementById("results").style.display = "none";
 	<%
  	}
  	%>
 	loadEmpCodes();
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
		{	//alert(obj.nodeName);
			curleft =curleft + obj.offsetLeft;
			curtop =curtop + obj.offsetTop; //alert(curtop);
		}
	}
	return [curleft,curtop];
}

/**
* this function validates the form fields
**/
function validation()
{
	 if(document.getElementById("employeeCode").value==null || document.getElementById("employeeCode").value=="")
	 {
		 alert('<bean:message key="alert.employee"/>');
	 	return false;
	 }
	 if(document.getElementById("fromMonth").value=="-1" || document.getElementById("toMonth").value=="-1" || document.getElementById("fromFinYr").value=="-1" || document.getElementById("toFinYr").value=="-1" )
	 {
		 alert('<bean:message key="alert.fromTo.date"/>'); 
	    return false;
	 }
	   
  	return true;
}

function getEmployeeByEnteringCode(code,currRow){

	var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=getActiveEmployeeByCode&code="+code;
    var req = initiateRequest();
    req.onreadystatechange = function(){
      if (req.readyState == 4){
            if (req.status == 200){
                  	var glcodes=req.responseText
                  	var a = glcodes.split("^");
                  	var codes = a[0];								
				var emp = codes.split("`-`");
				if(codes == "false"){					
				}
				else{
				document.getElementById('empid').value = emp[2]; 
				}
           }
       }
   };
   req.open("GET", url, true);
   req.send(null);
  }
  
 function move(inputControl)
	{  
		//alert('inside---');
		var left = document.getElementById("deptOrgId");  
		var right = document.getElementById("deptGroupId"); 
		var from, to;  
		var bAll = false;  
		switch (inputControl.value)  
		{  
			case '<':    
			from = right; 
			to = left;    
			break;  
			case '>':    
			from = left; 
			to = right;    
			break;  
		}  
		for (var i = from.length-1; i >= 0; i--)  
		{    
			var o = from.options[i];    
			if (o.selected)    
			{      
				//document.forms[0].deptPayheadSumForm.value=''; 
				try      
				{        
					var clone = o.cloneNode(true);
					to.appendChild(clone);
				// Standard method, fails in IE (6&7 at least)      
				}      
				catch (e)  
				{ 
				 to.add(o); 
				// IE only  
				} 
				from.remove(i);        
			}  
		}
	}
	
	function checkOnSubmit(){
		//alert("inside");
		//alert(document.deptPayheadSumForm.fromDate.value);
		if(document.deptPayheadSumForm.month.value=="0"){
			alert('<bean:message key="alert.month"/>'); 
			return false;
		}		
		if(document.deptPayheadSumForm.year.value=="0"){
					alert('<bean:message key="alert.year"/>'); 
					return false;
		}
		if(document.getElementById("deptGroupId").length==0){
			alert('<bean:message key="alert.dept"/>'); 
			return false;
		}
		else{
			var deptGroupIdLength = document.getElementById("deptGroupId").length;			
			for (var i = 0; i<deptGroupIdLength; i++){				    
				document.getElementById("deptGroupId").options[i].selected=true;
			}
		}
		if(document.getElementById('billNumber').value == ""){
			document.getElementById('billNumberId').value = "";
		}	
		return true;
	}	
	var codeSelectionHandler = function(sType, arguments) {
	    var oData = arguments[2];
	    var billDetails = oData[0];
	    var billNumberId = oData[1];
	    document.getElementById('billNumber').value = billDetails;
	    document.getElementById('billNumberId').value = billNumberId;
	}
	var codeSelectionEnforceHandler = function(sType, arguments) {
	    warn('impropercodeSelection');
	}

	
</script>   

</head>

<body>
	
					<s:actionmessage />
					<s:form name="deptPayheadSumForm" action ="deptPayheadSummary"  theme="simple">	
					<s:hidden name="fileFormat" styleId="fileFormat" />
					 	<center>
					 		<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" id="DeptPayheadSummary">
								<tr>
				                	<td colspan="5" class="headingwk">
					                	<div class="arrowiconwk">
					                		<img src="../common/image/arrow.gif" />
					                	</div>
					                  	<div class="headplacer"> <bean:message key="dept.payhead.summary"/></div>
				                  	</td>
				              	</tr>
				   				
				   				<tr>
							    	<td class="whiteboxwk" ><bean:message key="month"/><font color="red">*</font></td>
							    	<td class="whitebox2wk">
										<s:select name="month" id="month"  cssClass="selectwk" headerValue="Choose" headerKey="0"
										list="#{'1':'JAN','2':'FEB','3':'MAR','4':'APR','5':'MAY','6':'JUN','7':'JUL',
										'8':'AUG','9':'SEP','10':'OCT','11':'NOV','12':'DEC'}" />	
							    	</td>	
							    	</td>	
							    	<td class="whiteboxwk"><bean:message key="year"/><font color="red">*</font></td>
							    	<td class="whitebox2wk">
							    		<s:select name="year" id="year" cssClass="selectwk"  list="dropdownData.finYearList" 
											listKey="id" listValue="finYearRange" headerValue="Choose" headerKey="0" onselect="yearString"/>				    		
							    	</td>
							    </tr>
							    
							    <tr>
							    	<td class="greyboxwk"><bean:message key="dept"/><font color="red">*</font></td>
							    	<td class="greybox2wk">
							    		
											<s:select cssStyle="width:200px" multiple="true" size="4"   name="deptOrgId" id="deptOrgId" cssClass="selectwk" list="dropdownData.deptList" listKey="id" listValue="deptName"/>
							    	</td>
							    	<td align="center" class="greyboxwk" >				    		
										<input type="button" value='&gt;' class="button"  id="left" onclick="move(this);"/>
										<br>
										<input type="button" value='&lt' class="button" id="right" onclick="move(this);"/>
									</td>
									<td class="greybox2wk" align="left" ><label></label>
										<s:select cssStyle="width:200px" multiple="true" size="4"  name="deptGroupId" id="deptGroupId" cssClass="selectwk" list="dropdownData.deptDummyList" listKey="id" listValue="deptName"/>
									</td>
									<td class="grey" width="20%" >&nbsp;</td>
								</tr>	
								<tr>
								        <td class="whiteboxwk"><bean:message key="bill.no"/></td>
										<td  class="whitebox2wk" width="20%" valign="top" >  	
							  			<div class="yui-skin-sam">
								    	<div id="billNumberSearch_autocomplete" class="yui-ac" >
								    	    <s:textfield id="billNumber" name="billNumber" size="10" value="%{billNumber}" class="selectwk"/> 	    
								   	   	<div id="codeSearchResults"></div> 
								    	</div>
										</div>
										<egovtags:autocomplete name="billNumber"  field="billNumber" 
									   	    	url="${pageContext.request.contextPath}/billNumber/billNumberMaster!getBillNumberList.action" queryQuestionMark="true"  results="codeSearchResults" 
									   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
									   		   <span class='warning' id="impropercodeSelectionWarning"></span>
										</td>
										<s:hidden name="billNumberId" id="billNumberId"/>
								</tr>
							  
							   	
							  <tr><td>&nbsp;</td></tr>
							  <tr><td>&nbsp;</td></tr>
							   	<tr>	
							   		<td colspan="5">	
							   		<div class="buttonholderwk">
							   				<s:submit  name="action" value="Show HTML" cssClass="buttonfinal" method="showPayslipList" onclick="return getReport('HTM');"/>																			
									
											<s:submit id="submit" name="submit" cssClass="buttonfinal" value="Show PDF" method="showPayslipList" onclick="return getReport('PDF');"/>
										
											<s:submit id="submit" name="submit" cssClass="buttonfinal" value="Show EXCEL" method="showPayslipList" onclick="return getReport('XLS');"/>
									</div>
									</td>
							   	</tr>
							   	</table>
							   	 <s:if test="%{noRecordsFound!=''}">
									<div>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td align="center">
												<font color="red"><bean:message key="no.record.found"/></font>
												</td>
											</tr>
										</table>
									</div>
								</s:if> 
							   
				   		</table>
					    </center>
					</s:form>
				
</body>
</html>
