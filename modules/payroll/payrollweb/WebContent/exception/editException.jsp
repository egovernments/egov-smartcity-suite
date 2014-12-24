<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,org.egov.payroll.utils.PayrollManagersUtill" %>

<html>
<head>
	<c:if test="${mode == null}">
		<title>Approve/Reject Exception</title>
	</c:if>
	<c:if test="${mode != null}">
		<title>Edit Exception</title>
	</c:if>


<script language="JavaScript"  type="text/javaScript">
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
		var fromdateOnLoad;

	function validation(obj)
	{		
	   	if(document.exceptionForm.financialYear.value=="")
		{
			 alert("Please select the financial year from the drop down");
			 document.exceptionForm.financialYear.focus();
			 return false;
		}
		
		if(document.exceptionForm.month.value=="")
		{
			 alert("Please select the month from the drop down");
			 document.exceptionForm.month.focus();
			 return false;
		}
		if(document.getElementById("createdOn").value=="")
		{
		 	alert("Please Enter the Created On");
		    document.exceptionForm.createdOn.focus();
		    return false;
		 
		}
	    
		if(document.exceptionForm.exType.value=="")
		{
			 alert("Please select the Action from the drop down");
			 document.exceptionForm.exType.focus();
			 return false;
		}
		if(document.exceptionForm.exReason.value=="")
		{
			 alert("Please select the reason from the drop down");
			 document.exceptionForm.exReason.focus();
			 return false;
		}  	
		 
		for(var i=0;i<document.forms[0].length;i++)
		{
			document.forms[0].elements[i].disabled =false;

		}	
		if(document.getElementById("fromDate").value!="")
		{
			if(compareDate(document.getElementById("fromDate").value,document.getElementById("createdOn").value)== 1)
			{
			    alert("From Date should be greater than or equal to Created On ");
			    document.getElementById('fromDate').value="";
			    document.getElementById('fromDate').focus();
			    return false;
			}
			var todate=document.getElementById("toDate").value;
			if(todate!="")
			{
			  if(compareDate(document.getElementById("toDate").value,document.getElementById("fromDate").value)== 1)
			  {
			    alert("To Date should be greater than or equal to From Date ");
			    document.getElementById('toDate').value="";
			    document.getElementById('toDate').focus();
			    return false;
			  }
			}
		}
		else if(document.getElementById("toDate").value!="" && (obj.value=="Approve" || obj.value=="Modify") )
		{
		  	alert("Please Enter From Date");
		  	return false;
		}
		else
		{
		  document.getElementById("toDate").value="";
		}
		
		if((obj.value=="Approve" || obj.value=="Modify")  && document.getElementById("fromDate").value == "")
		{
		  	alert("Please Enter From Date");
		 	return false;
		}		
		
		if(document.getElementById("fromDate").value!="" && fromdateOnLoad!=document.getElementById("fromDate").value && validatefromdate(document.getElementById("fromDate").value)=='false')
		{
		  	alert("From Date for the exception is not valid.This could be either because a payslip exists for the employee after this date or there is another valid exception during this time period.");
		  	document.getElementById("fromDate").value="";
		  	document.getElementById("fromDate").focus();
		  	return false;
		}	

		document.exceptionForm.action = "${pageContext.request.contextPath}/exception/beforeException.do?submitType=afterEditException"; 	
		document.getElementById("fromDate").disabled = false;
		//document.exceptionForm.submit();
	
    } 
  
  	function closeAndRefreshParent() 
	{			
		
		if(document.exceptionForm.isSaved.value != ""){						
			//window.opener.location.href="${pageContext.request.contextPath}/exception/beforeException.do?submitType=showExceptions&month="+${exception.month}+"&yearId="+${exception.financialyear.id};
			alert(document.exceptionForm.isSaved.value);
			refreshInbox();	
			window.close();		
		}	
	}
  	  
  	function callReasons(obj){  	
		for(var resLen=1;resLen<document.exceptionForm.exReason.length;resLen++){
			document.exceptionForm.exReason.options[resLen]=null;
		}
		var count = 1;
		<c:forEach var="exceptionObj" items="${exceptionMstrs}">			
			if(obj.value == "${exceptionObj.type}"){							
 				document.exceptionForm.exReason.options[count] = new 
				Option("${exceptionObj.reason}","${exceptionObj.id}");	
				count=count+1;
			}				
		</c:forEach>
		if(obj.value==""){			
			for(var resLen=document.exceptionForm.exReason.length-1; resLen>0 ;resLen--){				
				document.exceptionForm.exReason.remove(resLen);			
			}	
		}		
  	}	
   
	function populate(){			
		
		<%

		String msg=(String)request.getAttribute("AlertMessage");
		System.out.println("success="+msg);

		if(msg!=null)
		{
		%>
			var msg="<%=msg%>";
			alert(msg);
			window.close();
		<%
		}
		%>
		var count = 1;
		var obj = document.exceptionForm.exType;	
		<c:forEach var="exceptionObj" items="${exceptionMstrs}">			
			if(obj.value == "${exceptionObj.type}"){							
 				document.exceptionForm.exReason.options[count] = new 
				Option("${exceptionObj.reason}","${exceptionObj.id}");	
				count=count+1;
			}				
		</c:forEach>
		
		if(obj.value==""){			
			for(var resLen=document.exceptionForm.exReason.length-1; resLen>0 ;resLen--){				
				document.exceptionForm.exReason.remove(resLen);			
			}	
		}
			
		document.exceptionForm.exReason.value = "${exception.exceptionMstr.id}";
		if(document.getElementById("fromDate").value!="")
		{
		   fromdateOnLoad=document.getElementById("fromDate").value;
		}
		if(document.getElementById("fromDate").value!=null && document.getElementById("fromDate").value!="" &&  validatefromdate(document.getElementById("fromDate").value)=='false')
		{
		
		document.getElementById("fromDate").disabled = true;
		}
	} 
	
	function initiateRequest() {
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
   	}
	
	
	/**
	  * if entered date is valid date this will return true;
	  */
	function validatefromdate(obj)
	{
	
	var empid=document.getElementById("empId").value;	
	var expId=document.getElementById("exceptionId").value;
	var action = "checkExceptionFormDate";
	var link = "<%=request.getContextPath()%>"+"/commons/checkException.jsp?action=" +action+"&fromdate="+obj+"&empId="+empid+"&expid="+expId+ " ";
	var request = initiateRequest();
	var isUnique;
	
	request.onreadystatechange = function()
	{

	if (request.readyState == 4)
	{
	if (request.status == 200)
	{
	var response=request.responseText;
	var result = response.split("^");
	//alert("result "+result[0]);

		if(result[0]=="true")
		{

			isUnique="true";
		}
		else if(result[0]=="false")
		{
			isUnique="false";
		}

	    }
	  }
	};
	request.open("GET", link, false);
	request.send(null);
	
 	return isUnique;
	
	}
	
    function refreshInbox()
    {
    	if(opener.top.document.getElementById('inboxframe')!=null)
    	{    	
    		if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
    		{    		
    			opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    		}
    	
    	}  	
    }
	
</script>
</head>


<body onLoad="populate();closeAndRefreshParent();">

  <html:form method="POST" action="exception/beforeException" >

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
								   <html:hidden name="exceptionForm" property="exceptionId" styleId="exceptionId" value="${exception.id}"/>
								   <html:hidden name="exceptionForm" property="empId" styleId="empId" value="${exception.employee.idPersonalInformation}"/>
								   <html:hidden name="exceptionForm" property="isSaved" styleId="isSaved" />
								  
								  <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="editExcTbl">
								    <tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							               	<div class="headplacer">
							               		<c:if test="${mode == null}">
												<b>Approve/Reject Exception</b>
												</c:if>
												<c:if test="${mode != null}">
													<b>Modify Exception</b>
												</c:if>
							               	</div>
							            </td>
							        </tr>
								
									<tr>
										<td class="greyboxwk" ><span class="mandatory">*</span>Year</td>
										<td class="greybox2wk" >
											<html:select property="financialYear" value="${exception.financialyear.id}">
												<html:option value="">-------Select----------</html:option>
												<c:forEach var="yearObj" items="${financialYears}">
												 <html:option value="${yearObj.id}">${yearObj.finYearRange}</html:option>
												</c:forEach>
											</html:select>
										</td>
										<td class="greyboxwk" ><span class="mandatory">*</span>Month</td>
										<td class="greybox2wk">
											<html:select property="month" value="${exception.month}">
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
										<td class="whiteboxwk" ><span class="mandatory">*</span>Created On&nbsp;(dd/mm/yyyy)</td>
										<td class="whitebox2wk" colspan="3">
											<input type="text" name="createdOn"  id="createdOn" class="datefieldinput" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" value="<fmt:formatDate value="${exception.expDate}" pattern="dd/MM/yyyy" />" />
										</td>					
									</tr>
									<tr>
										<td class="greyboxwk" >From Date(dd/mm/yyyy)</td>
										<td class="greybox2wk" >
											<input type="text" name="fromDate"  id="fromDate" class="datefieldinput" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" value="<fmt:formatDate value="${exception.fromDate}" pattern="dd/MM/yyyy" />" />
										</td>
										<td class="greyboxwk" >To Date(dd/mm/yyyy)</b></td>
										<td class="greybox2wk">
										    <input type="text"  name="toDate" id="toDate" class="datefieldinput" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" value="<fmt:formatDate value="${exception.toDate}" pattern="dd/MM/yyyy" />" />
										</td>		
									</tr>
									<tr>
										<td class="whiteboxwk" colspan="4">&nbsp;</td>
									</tr>
								</table>
								
								<table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="editDtlTbl">
									<tr>
								        <td class="dtlTblHeader">Code</td>
								        <td class="dtlTblHeader">Name</td>
								        <td class="dtlTblHeader">Action</td>
								        <td class="dtlTblHeader">Reason</td>
								        <td class="dtlTblHeader">Comments</td>
									</tr>
								    <tr>
								   	 	<td class="greybox2wk">
								   	 		<html:text property="employeeCode" value="${exception.employee.employeeCode}" readonly="true" />
								     	</td>
									   	 <td class="greybox2wk">
									   	 	<html:text property="employeeName"  value="${exception.employee.employeeFirstName}" readonly="true"/>
									  	 </td>
									   	 <td class="greybox2wk">
									  		<html:select property="exType" 
									  				 onchange="callReasons(this);" value="${exception.exceptionMstr.type}">
									       	<html:option value="">------------Select-------------</html:option>
											<c:forEach var="actionObj" items="${exceptionTypes}" >
									 		<html:option value="${actionObj}">${actionObj}</html:option>
											</c:forEach>
									   	    </html:select>
									   	 </td>
									   	 <td class="greybox2wk">
									  		<html:select property="exReason" value="${exception.exceptionMstr.id}">
									       	<html:option value="">------------Select-------------</html:option>
									   	    </html:select>
									  	 </td>
									   	 <td class="greybox2wk">
									      	<html:text  property="exComments" value="${exception.comments}" />
									     </td>
								  	</tr>
								  	<tr>
						               	<td colspan="5" class="shadowwk"></td>
							      	</tr>
										
								  	<tr>
								 	   	<td colspan="5" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
								  	</tr>
					            
					              	<tr>
							           <td align="center" colspan="5"></td>
					              	</tr>
								 </table>   
							</div>
						</div>
					</div>
				</div>
		
				<div class="buttonholderwk">
					<c:if test="${mode != null}">	
						<html:submit property="actionType" value="Modify" styleId="modify" onclick="return validation(this);"/>
					</c:if>	
					<!-- To include role based approve and reject   -->
					<c:if test="${mode == null}">
						<html:submit property="actionType" value="Approve" styleId="approve" onclick="return validation(this);"/>
						<html:submit property="actionType" value="Reject" styleId="reject" onclick="return validation(this);"/>
					</c:if>
					<html:button property="actionType" value="Close" styleId="paySlip" onclick="window.close();"/>
				</div>
					
				<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
			</center>
	</html:form>
</body>
</html>
