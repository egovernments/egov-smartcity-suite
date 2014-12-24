<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,
                java.sql.*,
                org.egov.infstr.utils.*,org.egov.payroll.utils.PayrollConstants
                " %>
<html>

<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

	<title> <bean:message key="pay.summary.report"/></title>

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

ArrayList finYrList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");
String mode = (String)request.getParameter("mode");
System.out.println("mode-----------"+mode);

String currDate="";
	Connection conn=null;
%>

<c:set var="financialYear" value="<%=finYrList%>" scope="page" />
<script language="JavaScript"  type="text/JavaScript">
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
 	else {

 			neibrObj.value=temp[2];
 			//document.getElementById("monthAndYear").value="${asd}";
 		 }

  }
function autocompleteEmpCode(obj,event)
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
  	   document.getElementById("results").style.display = "none";
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

 <c:choose>
	<c:when test="${ess  == 1 or (not empty param.ess)}" >
	document.payslipSearchForm.action = "selfService.do?reportType=showPaySummaryReport";
	</c:when>
	<c:otherwise>
	document.payslipSearchForm.action = "paysumarryreport.do";
	</c:otherwise>
 </c:choose>
		
  var finYr=document.getElementById("finYr").value;
  
  if(finYr=="-1")
  {
    alert('<bean:message key="alert.year"/>');
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
  var empCodeSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	dom.get("empid").value = oData[1];	 	
	 	dom.get("employeeCode").value = empCode;
	 	employeeName=empName;
	 	empcode=empCode;
 	}
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}
  	
  	function setAction() 
  	{
		
      }
</script>   

</head>
<body onLoad="onBodyLoad();">
<html:form action ='/reports/paysumarryreport' onsubmit="return validation();" method="post">

 <center>	
		
 
  <table width="95%" border="0" cellspacing="0" cellpadding="0" id="paytable" >   	
   	
   	<div id="payslip">
   	<tr>
   	 <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
   	 <div class="headplacer"><bean:message key="pay.summary.report"/></div>
   	 </td>
	   
    	</tr>
   	<tr>
  
   	</tr>
   	<tr>
	  <input type="hidden" name="empid" id="empid" value="${empid}">
	  
	     <td class="whiteboxwk" width="4%"><bean:message key="financial.year"/></td>
	   		    <td class="whitebox2wk">
	   		    	<html:select property="finYr" styleId="finYr" styleClass="selectwk">
	   		    	<html:option value="-1">Choose</html:option>
	   		    	<c:forEach var="financialYearObj" items="${financialYear}">
	   		    	<c:if test = "${financialYearObj.isActive=='1'}">
	   		    	<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
	   		    	</c:if>
	   		    	</c:forEach>
	   		     	</html:select>
	    </td>
		
		<c:choose>
		<c:when test="${(empty ess) and (empty param.ess)}">

	  	<td class="whiteboxwk" width="20%"><span class="mandatory">*</span><bean:message key="emp.code"/></td>
	  	<td  class="whitebox2wk" width="20%" valign="top">  	
  		<div class="yui-skin-sam">
	    	<div id="empSearch_autocomplete">
	    	    <input type="text"  class="selectwk" name="employeeCode" id ="employeeCode" /> 	    
	   	   <div id="empCodeSearchResults"></div> 
	    	</div>
		</div>		    	
		   	    <egovtags:autocomplete name="employeeCode"  field="employeeCode" 
		   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action"   results="empCodeSearchResults" 
		   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
		   	    <span class='warning' id="improperempCodeSelectionWarning"></span>
		</td>
		</c:when>
		<c:otherwise>
		<td class="whiteboxwk" width="20%">
		 <input type="hidden"  name="employeeCode" id ="employeeCode" value="${empcode}"/> 
		 <input type="hidden"  name="ess" id ="ess" value="${ess}"/> 
		</td>
	  	<td  class="whitebox2wk" width="20%" valign="top">  </td>	
		</c:otherwise>
		</c:choose>
	
        </tr>
   	<tr>
   	<td colspan="4" align="center">
   	
   	</td>
   	</tr>
   	<tr>
	  <td><div id="codescontainer"></div></td>
        </tr>
        <c:if test="${paysummarydetails!=null}">
        <div name="results" id="results" style="hidden:true;"> 
        <tr  >

	
	<td class="whiteboxwk">Employee Name</td>
	    <td class="whitebox2wk">
		${employeeName}
   	 </td>
   	 <td class="whiteboxwk">Employee Code</td>
	<td class="whitebox2wk">${empcode}</td>
        </tr>
        
        <tr>
	
		<td class="greyboxwk">Employee Designation</td>
		<td class="greybox2wk">${designation}</td>
		<td class="greyboxwk">Date Of Joining</td>
		    <td class="greybox2wk">
			${yearOfJoining}
	   	 </td>
        </tr>
        <tr >
        <td colspan="4">
        
      
<div class="tbl5-container" id="tbl-container">
	
	<display:table name="${paysummarydetails}" export="false" cellspacing="0" cellpadding="0" id="results" class="its"  
	style="Width:95%" pagesize="20" requestURI="${pageContext.request.contextPath}/reports/paysumarryreport.do" 
	decorator="org.egov.payroll.reports.decorators.PaySummaryDecorator">  
	
	      <div STYLE="display:table-header-group">			      
	      <display:column title="   "  style="width:8%" property="type" group="1"></display:column>	      
	      <display:column title="Pay Details  "  style="width:6%" property="salhead"></display:column>                           	      
	      <display:column title="April  "  style="width:5%" property="april" total="true" ></display:column>
	      <display:column title="May  "  style="width:5%" property="may" total="true" ></display:column>
	      <display:column title="June  "  style="width:5%" property="june" total="true" ></display:column>
	      <display:column title="July  "  style="width:5%" property="july" total="true" ></display:column>
	      <display:column title="August  "  style="width:5%" property="august" total="true" ></display:column>
	      <display:column title="September  "  style="width:5%" property="september"  total="true" ></display:column>
	      <display:column title="October  "  style="width:5%" property="october"  total="true" ></display:column>
	      <display:column title="November  "  style="width:5%" property="november" total="true" ></display:column>
	      <display:column title="Decemeber "  style="width:5%" property="december" total="true"  ></display:column>
	      <display:column title="January  "  style="width:5%" property="january" total="true" ></display:column>
	      <display:column title="February  "  style="width:5%" property="february"  total="true" ></display:column>
	      <display:column title="March  "  style="width:5%" property="march" total="true" ></display:column>
	      <display:column title="YTD  "  style="width:5%" property="YTD" total="true"  ></display:column>
	      
	
	         
	      <display:setProperty name="basic.show.header" value="true" />
	    </div>
    </display:table >
    </div>
        
        </td></tr>
        </div>
        </c:if>
   </div>   
   </table>
  

</center>

		
		<div class="buttonholderwk">
		<html:submit value="Submit" styleClass="buttonfinal" property="b1" />
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
		</div>
		
</html:form>
</body>
</html>
