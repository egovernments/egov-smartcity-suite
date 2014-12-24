<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ page language="java" import="java.util.*,java.sql.*,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.infstr.utils.*,
									org.egov.payroll.utils.PayrollConstants,org.egov.payroll.utils.PayrollManagersUtill" %>


<%
ArrayList functionaryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-functionary");
List financialYears = PayrollManagersUtill.getCommonsService().getAllActivePostingFinancialYear();
List columnNamesArr =new ArrayList();
List payslipHistoryDetailsList = (ArrayList)request.getAttribute("payslipSet");
	
if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()>0){								      	
	Map payslipMap = (HashMap) payslipHistoryDetailsList.get(0);
	columnNamesArr.addAll(payslipMap.keySet());
}


%>

<c:set var="functionaryList" value="<%=functionaryList%>" scope="page" />
<c:set var="financialYears" value="<%=financialYears%>" scope="page" />

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
		var left = document.getElementById("functionaryOrgIds");  
		var right = document.getElementById("functionaryIds"); 
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
		for (var i = from.length - 1; i >= 0; i--)  
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
		if(document.payslipSearchForm.month.value==""){
			 alert('<bean:message key="alert.month"/>');
			return false;
		}
		if(document.payslipSearchForm.finYr.value==""){
			alert('<bean:message key="alert.year"/>');
			return false;
		}
		if(document.getElementById("functionaryIds").length==0){
			alert('<bean:message key="alert.functionary"/>');
			return false;
		}
		else{
			var funcGroupIdLength = document.getElementById("functionaryIds").length;			
			for (var i = 0; i<funcGroupIdLength; i++){				    
				document.getElementById("functionaryIds").options[i].selected=true;
			}
		}
		if(document.getElementById('billNumber').value == ""){
			document.getElementById('billNumberId').value = "";
		}
		
		return true;
	}	  
	
	
	
	function populateParent(obj)
		{
			populatepId({id:obj.value});
		}
function checkResults()
{
<% if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()==0)
{ %>
alert('<bean:message key="alert.search.result"/>');

<%}%>
	


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

<body onload = "checkResults();">


	<html:form action ="/reports/funcPayheadSummaryReport">
	 	<center>
	 		<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" id="DeptPayheadSummary">
				<tr>
                	<td colspan="4" class="headingwk">
	                	<div class="arrowiconwk">
	                		<img src="../common/image/arrow.gif" />
	                	</div>
	                  	<div class="headplacer"><bean:message key="funct.payhead.summary"/></div>
                  	</td>
              	</tr>	
				
			<tr>
			    	<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="month"/></td>
					<td class="greybox2wk">
					<select name="month" id="month" styleClass="selectwk"  style="width:160px">
						<option value="">Choose</option>
						<option value="1">JAN</option>
						<option value="2">FEB</option>
						<option value="3">MAR</option>
						<option value="4">APR</option>
						<option value="5">MAY</option>
						<option value="6">JUN</option>
						<option value="7">JUL</option>
						<option value="8">AUG</option>
						<option value="9">SEP</option>
						<option value="10">OCT</option>
						<option value="11">NOV</option>
						<option value="12">DEC</option>
						</select>
					</td>
					<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="year"/></td>
					<td class="greybox2wk">	
						<select name="finYr" id="year" styleClass="selectwk"  style="width:160px">
							<option value="">Choose</option>
							<c:forEach var="financialYearObj" items="${financialYears}">
							<c:if test = "${financialYearObj.isActive=='1'}">
							<option value="${financialYearObj.id}">${financialYearObj.finYearRange}</option>
							</c:if>
						</c:forEach>
						</select>
					</td>
			    </tr>
			    <tr>
			    	<td class="greyboxwk"><bean:message key="functionary"/><font color="red">*</font></td>
			    	<td class="greyboxwk">
			    		<select name="functionaryOrgIds" id="functionaryOrgIds" multiple="true" size="4" style="width:160px">
					    	<option value="">Choose</option>
					    	<c:forEach var="funcObj" items="${functionaryList}">						    	
						    	<option value="${funcObj.id}">${funcObj.name}</option>						    	
					    	</c:forEach>
					     </select>
			    	</td>
			    	<td align="center" class="greyboxwk" >				    		
						<input type="button" value='&gt;' class="button" id="left" onclick="move(this);"/>
						<br>
						<input type="button" value='&lt' class="button" id="right" onclick="move(this);"/>
					</td>
					<td width="30%" class="greyboxwk" align="left" ><label></label>
					
						<select name="functionaryIds" id="functionaryIds" multiple="true" size="4" style="width:160px">
					    	
					     </select>
					</td>
					<td class="grey">&nbsp;</td>
				</tr>	
				
			  
				<tr>
								        <td class="whiteboxwk"><bean:message key="bill.no"/></td>
										<td  class="whitebox2wk" width="20%" valign="top" >  	
							  			<div class="yui-skin-sam">
								    	<div id="billNumberSearch_autocomplete" class="yui-ac" >
								    	    <input type="text" id="billNumber" name="billNumber" size="10" class="selectwk"/> 	    
								   	   	<div id="codeSearchResults"></div> 
								    	</div>
										</div>
										<egovtags:autocomplete name="billNumber"  field="billNumber" 
									   	    	url="${pageContext.request.contextPath}/billNumber/billNumberMaster!getBillNumberList.action" queryQuestionMark="true"  results="codeSearchResults" 
									   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
									   		   <span class='warning' id="impropercodeSelectionWarning"></span>
									   		   
										</td>
										<input type="hidden"  name="billNumberId" id="billNumberId" />
								</tr>
			   	<tr>
			   		<td colspan="2" align="center" class="whiteboxwk">
			   			
			   			<html:submit value="Submit" property="b1" onclick="return checkOnSubmit();" />
			   		</td>
			   	</tr>

			</table>
			<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0">
			   <%
    	
	    		if(payslipHistoryDetailsList!=null && payslipHistoryDetailsList.size()>0 )
				{
				%>
		       <!--  <div id="results" style="overflow-y:scroll; overflow-y:scroll height:100%; overflow-x:scroll width: 100%>	-->
				<tr>
		          <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
						
						<display:table name="${payslipSet}" uid="currentRowObject" class="its" export="true" style="width:780px" requestURI=""  pagesize="50">
					      <%	
							//String functionaryName =(String)((Map)pageContext.findAttribute("currentRowObject")).get("FunctionaryName").toString();
							//String deptName =(String)((Map)pageContext.findAttribute("currentRowObject")).get("DeptName").toString();
							String monthAndYear =(String)((Map)pageContext.findAttribute("currentRowObject")).get("Month & Year").toString();
							System.out.println("month-------"+monthAndYear);
							String billNo  = (String) request.getAttribute("billno");
							 %>


							<display:caption style="text-align: left">Functionary Payhead Summary Report for <%= monthAndYear %>
							<br/>
							<%= billNo %>
							</display:caption>
							<display:caption media="pdf">
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 Functionary Payhead Summary Report for <%= monthAndYear %>
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 
													<%= billNo %>
							</display:caption>	
							<display:caption media="excel">
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 Functionary Payhead Summary Report for <%= monthAndYear %>
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 
													<%= billNo %>
							</display:caption>								
							<!-- first element in map is payid , not displaying here-->
							<display:column style="border:1;" property="PayId" media="none"></display:column>
							<%
									
									for(int i=3;i<columnNamesArr.size();i++)
									{
										String columnName = (String)columnNamesArr.get(i);
									%>
										<display:column style="border:0;" property="<%=columnName%>" ></display:column> 
									<% 
									}
				      		  %>
				      		  
					      	<div STYLE="display:table-header-group;">			      
						  		<display:setProperty name="basic.show.header" value="true" />
						  		<display:setProperty name="export.pdf" value="true" />
								<display:setProperty name="export.pdf.filename" value="FunctionaryPayheadSummary.pdf" />
								<display:setProperty name="export.excel" value="true" />
								<display:setProperty name="export.excel.filename" >FunctionaryPayheadSummaryFor <%= monthAndYear %>.pdf.xls</display:setProperty>
								<display:setProperty name="export.csv" value="false" />
								<display:setProperty name="export.xml" value="false" />
				    		</div>
				    	</display:table >
				    </div>	
		        </td>
		    </tr>
		    
			<!-- </div>	-->
		    <%
		    }
		     %>		  		
   		</table>
	    </center>
	</html:form> 



	
</body>
</html>
