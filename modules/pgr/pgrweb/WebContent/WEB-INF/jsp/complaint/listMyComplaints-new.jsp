<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page isELIgnored="false" %>

<html>
<s:if test="%{mode=='completed'}">
<title>List My Completed Complaints</title>
</s:if>
<s:else>
<title>List My Complaints</title>
</s:else>
<head>
</head>

<script type="text/javascript">

function viewComplaint(id){
	
	window.open("${pageContext.request.contextPath}/coplaint/complaint!view.action?model.id="+id,"","height=800,width=700,scrollbars=yes,left=0,top=0,status=yes");

}

</script>

 <body> 
<link type="text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/displaytag.css" rel="stylesheet"  /> 
 <s:form action="listMyComplaints" theme="simple" name="listMyComplaintsForm">
	<s:hidden id="mode" name="mode"></s:hidden>	
		<div id="tableData" align="center">
       		 <div id="displaytbl" align="center">	
    		  	<display:table  name="searchResult" export="true" requestURI="" id="myComplaintsListid"  class="its" uid="currentRowObject" >
    			<div STYLE="display: table-header-group" align="center">
      			 
      			 <display:caption  media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List My Complaints
				   </display:caption>
				    <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="listMyComplaints-Report.pdf" /> 
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.excel.filename" value="listMyComplaints-Report.xls"/>	
					<display:setProperty name="export.csv" value="false" />	
					<display:setProperty name="export.xml" value="false" />	
					
      			 	  
			 	<display:column title=" Sl No" style="width:5%;text-align:center;" >
			 	 		<s:property value="%{#attr.currentRowObject_rowNum}"/>
			 	</display:column>						

				
				<display:column title="Complaint Number " style="width:5%;text-align:center;" >	
	 				<a href="#" onclick="viewComplaint('${currentRowObject.id}')">
		 			 ${currentRowObject.complaintNumber}
		 			</a>
		 		</display:column>
			
				<display:column title="Complaint Type " style="width:12%;text-align:center;" property="complaintType.complaintGroup.complaintGroupName">	 								
				</display:column>							
				
				<display:column title="Complaint Title" style="width:12%;text-align:center;" property="title" >
			 	</display:column>
			 		
			 	<display:column title="Department " style="width:10%;text-align:center;" property="department.deptName" >	 						 
				</display:column> 						 	
				 	
				<display:column title="Location" style="width:10%;text-align:center;" property="complaintlocation" >
			 	</display:column>
			 	
			 	<display:column title="Status" style="width:8%;text-align:center;" property="redressal.complaintStatus.name" >
			 	</display:column>
			 		
			 	<display:column title="Registration Date" style="width:8%;text-align:center;" property="complaintDate" format="{0,date,dd-MM-yyyy}">						
				</display:column>
				
				<display:column title="Expiry Date" style="width:8%;text-align:center;" property="expiryDate" format="{0,date,dd-MM-yyyy}">						
				</display:column>						 		
					  			 						 
				</div>						
			</display:table>						
		</div>
  	    
  	   </div>
  	 <div>
  			
  		<s:if test="%{geoLocationList!=null && !geoLocationList.isEmpty() && geoLocationList.size()!=0 }">	
  		<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="header.map"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			
			<table border="1" width="870" align="center" bgcolor="#EFEFEF"  cellspacing="0"  height="5"><tr>
				<TD width="500" height="300"><div id="map_canvas" style="width:100%; height:100%"></div> </TD></tr>
		         	 <c:import url="/commons/googleMapReport.jsp" context="/egi"></c:import>
			</table> 
			
		</table>
		</s:if>	
  	 </div>
  	 
  	 <div class="buttonbottom">
		<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close" />
	</div>
  	</s:form>
  	
  </body>	
  	     
</html>
