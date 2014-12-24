<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 


<html>
 	 <head>
	   <title>
	   		<s:if test="%{mode=='reversion'}">
	   			<s:text name="emp.search.reversion"/> 
	   		</s:if>	
	   		<s:if test="%{mode=='reversionReport'}">
	   			<s:text name="emp.search.reversionreport"/>
	   		</s:if>
	   		<s:if test="%{mode=='demotion'}">
	   			<s:text name="emp.search.demotion"/>
	   		</s:if>
	   	</title>
   		<script type="text/javascript">
	   		var codeSelectionHandler = function(sType, arguments) {
	   			var oData = arguments[2];
	   			var empDetails = oData[0];
	   			var splitResult = empDetails.split("~");
	   			document.getElementById('empCode').value = splitResult[0];
	   			document.getElementById('empName').value = splitResult[1];
	   		}
	
	   		var codeSelectionEnforceHandler = function(sType, arguments) {
	   			warn('impropercodeSelection');
	   		}

	   		function checkAlphaNumeric(obj) {
	   			var num = obj.value;
	   			var objRegExp = /^[a-zA-Z0-9_-]+$/;
	   			if (num!="" && !(objRegExp.test(num))) {
	   				document.getElementById("emp_error").style.display = '';
	   		        document.getElementById("emp_error").innerHTML = '<s:text name="enter.proper.code"/>';
	   			}
	   		}

	   		function initiateProcess(obj)
	   		{
		   		if(document.getElementById('mode').value=='reversion')
			   	{	
	               window.location.assign('${pageContext.request.contextPath}/reversion/processReversionWorkflow!beforeCreate.action?empId='+obj);
			   	}   
	        } 



	   		function checkOnsubmit() {
	   			
	   			if (document.getElementById("fromDate").value == "") {                        
	   				alert('<s:text name="fromDate.required"/>');                          
	   				return false;          
	   			}
	   			
	   			if (document.getElementById("toDate").value == "") {      
	   				alert('<s:text name="toDate.required"/>');        
	   				return false;   
	   			}
	   			return true;
	   		}

	   		function compareDates(){

	   			if(document.getElementById("toDate").value != ""){
	   				var toDate=document.getElementById("toDate").value;
	   				var fromDate=document.getElementById("fromDate").value;
	   				
	   				if(compareDate(toDate,fromDate) == 1 || 
	   						compareDate(toDate,fromDate) == 0){
	   					alert('<s:text name="comparedates"/>');
	   					document.getElementById("toDate").value="";
	   					document.getElementById("fromDate").value="";
	   					return false;
	   					}
	   				
	   				}
	   		}

	   		function viewReversionDetails(obj){
	   			window.open('','window','scrollbars=yes,resizable=no,height=800,width=900,status=yes');
	   			}
		</script>
	</head>
  
  
	<body> 
  		<div class="errorcss" id="emp_error" style="display: none;" ></div>
		<s:form action="employeeSearch" theme="simple" name="searchForm">  
		<s:hidden id="mode" name="mode" value="%{mode}"/>
		   <div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"></div>
			<div class="rbcontent2">
			<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
				<tbody>
					<tr><td>&nbsp;</td></tr>
					  <tr>
						   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
						   <p><div class="headplacer"><s:text name="search.emp"/></div></p></td><td></td> 
					   </tr>
				</tbody>
					  
			</table>
			<br/>
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tbody>	
					<s:if test="%{mode== '' || mode=='reversion' || mode=='demotion'}">
							<%@ include file="commonEmpSearch.jsp" %>
					</s:if>	
					<s:if test="%{mode =='reversionReport'}">
					    <%@ include file="/WEB-INF/jsp/reports/reversionReportSearch.jsp" %>
					</s:if>		
					</tbody>
				</table>
				<br/>
				<center><s:submit method="search" value="Search" cssClass="buttonfinal" onclick=" return checkOnsubmit();"/></center>
				<br/>
			</div>
			</div>
			</div>
			</div>
			<s:if test="%{mode== '' || mode=='reversion'}">
				<div align="right" class="mandatory"><s:text name="reversion.mandatory"/></div>
			</s:if>	
			
			<s:if test="%{searchResult != null}"> 
			<div id="leftNaviProposal"   style="overflow-y:auto;overflow-x:auto"> 
				<table width="80%" align="center" border="0" cellspacing="0" cellpadding="0">
						<display:table name="searchResult" export="false"  id="eid" uid="currentRowObject" cellpadding="0" cellspacing="0"
					    requestURI="" sort="external"  class="its" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
							<s:if test="%{mode== '' || mode=='reversion' || mode=='demotion'}">
								<%@ include file="commonEmpSearchResults.jsp" %>
							</s:if>
							<s:if test="%{mode =='reversionReport'}">
					    <%@ include file="/WEB-INF/jsp/reports/reversionReportResults.jsp" %>
					</s:if>	
						
						</display:table>
				</table>	
			</div>
			</s:if>		
		</s:form>
	
  </body>
</html>
