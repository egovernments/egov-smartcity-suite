<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.*,org.egov.payroll.model.* "%>

<html>
<head>
	<title>Show exceptions</title>

<script language="JavaScript"  type="text/JavaScript">
 
   function showExInfo(exceptionId,status,mode){   
	   var url="${pageContext.request.contextPath}/exception/beforeException.do?submitType=editException&exceptionId="+exceptionId+"&status="+status+"&mode="+mode;
	   window.open(url,'ExceptionInfo','width=900,height=600,top=150,left=30,right=30,scrollbars=yes,resizable=yes');	
   }
    
</script>   

</head>
	<body >
	<html:form action ="/exception/beforeException">

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
									<div class="datewk" width="220px;">	
									    <span class="bold">Today:</span> <egovtags:now/>
									</div>
   									
   									<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="showExcHdr">
								    
									    <tr>
											<td colspan="10" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								               	<div class="headplacer">Exception List</div>
								            </td>
								        </tr>

									   	<tr>
									      <td class="dtlTblHeader">Employee Code</td>
									      <td class="dtlTblHeader">Employee Name</td>
									      <td class="dtlTblHeader">Action</td>
									      <td class="dtlTblHeader">Reason</td>
									      <td class="dtlTblHeader">From Date</td>
									      <td class="dtlTblHeader">To Date</td>
									      <td class="dtlTblHeader">Comment</td>
									      <td class="dtlTblHeader">Status</td>
									      <td class="dtlTblHeader" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									      <td class="dtlTblHeader" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									  </tr>
	
										<c:if test="${fn:length(exceptions) == 0}">
											<tr>
												<td  colspan="6" align="center">
													<font color="red">No employee exception</font>
												</td>
											</tr>
										</c:if>
										
									   	<c:forEach var="exceptionObj" items="${exceptions}">
									   	<tr>   		   		
									   		<td class="whitebox2wk">
												<c:out value="${exceptionObj.employee.employeeCode}"/> 
									   		</td>
									   		<td class="whitebox2wk">
									   			<c:out value="${exceptionObj.employee.employeeFirstName}"/>&nbsp; 
											  		<c:if test="${exceptionObj.employee.employeeMiddleName != null}">
											  			<c:out value="${exceptionObj.employee.employeeMiddleName}"/>&nbsp; 
											  		</c:if>	
										  		<c:out value="${exceptionObj.employee.employeeLastName}"/>
									   		</td>
									   		<td class="whitebox2wk">
									   			<c:out value="${exceptionObj.exceptionMstr.type}"/>
									   		</td>
									   		<td class="whitebox2wk">
									   			<c:out value="${exceptionObj.exceptionMstr.reason}"/>
									   		</td> 
									   		<td class="whitebox2wk">
											   	<c:out value="${exceptionObj.fromDate}"/>
									   		</td>
									   		<td class="whitebox2wk"> 
											   	<c:out value="${exceptionObj.toDate}"/> 
									   		</td>  
									   		<td class="whitebox2wk">
									   			<c:out value="${exceptionObj.comments}"/> 
									   		</td>  
									   		<td class="whitebox2wk">
									   			<c:out value="${exceptionObj.status.description}"/>
									   		</td>
									   		<td class="urlwk">
											   	<a href="#" onclick="showExInfo('${exceptionObj.id}','${exceptionObj.status.description}','view');">View</a>
									   		</td>
									   		<td class="labelcell">
										   		<c:if test="${exceptionObj.status.description == 'Approved'}">	
											   		 <a href="#" onclick="showExInfo('${exceptionObj.id}','${exceptionObj.status.description}','modify');">Modify</a>
											   	</c:if>
									  		</td>   		  		
									   	</tr>   	
   								</c:forEach>	
   							</table>
						</div>
					</div>
				</div>
			</div>
			
			<div class="buttonholderwk">
				<html:button property="actionType" value="Close" onclick="window.close();"/>		    		
			</div>
			<div class="buttonholderwk">
				<tr>
	 				<td class="labelcell"><a href="<%=request.getContextPath()%>/exception/searchException.jsp">Search Exceptions</a></td>		
				</tr>	
			</div>
			<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
		</center>
</html:form>
</body>
</html>
