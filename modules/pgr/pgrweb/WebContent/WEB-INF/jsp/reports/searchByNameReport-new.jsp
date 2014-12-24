<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page isELIgnored="false" %>
<html>

<head>

<title>Search By Name</title>

</head>

<script type="text/javascript">

	function viewComplaint(id){
		
		window.open("${pageContext.request.contextPath}/common/complaint!view.action?model.id="+id,"","height=800,width=700,scrollbars=yes,left=0,top=0,status=yes");
	
	}

	function validate(){
		
		 if(trimAll(document.getElementById('name').value)=="" &&
				trimAll(document.getElementById('initials').value)=="" && 
				trimAll(document.getElementById('lastName').value)==""  && 
			 	trimAll(document.getElementById('phoneNo').value)=="" && 
			 	trimAll(document.getElementById('email').value)=="" && 
			 	trimAll(document.getElementById('address').value)==""  )
				{
			 		dom.get("searchRecords_error").style.display = '';
					document.getElementById("searchRecords_error").innerHTML = '<s:text name="select.atleast.one.value" />';			  					 		 		
					return false;
				}	 
	}

	function resetValues(){
		(document.getElementById('name').value)="" ;
		(document.getElementById('initials').value)="" ; 
		(document.getElementById('lastName').value)=""  ; 
	 	(document.getElementById('phoneNo').value)="" ; 
	 	(document.getElementById('email').value)="" ; 
	 	(document.getElementById('address').value)="" ;
	 	document.getElementById("tableData").style.display='none';
	}


</script>

<bodyclass="yui-skin-sam">
<link type="text/css"  rel="stylesheet" href="${pageContext.request.contextPath}/css/displaytag.css" rel="stylesheet"  />
<div class="errorstyle" id="searchRecords_error" style="display: none;" align="center"></div>
		<div class="errorstyle" style="display: none" >			
		</div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>
<s:form theme="simple" name="searchByNameReportForm" action="searchByNameReport" validate="true"  method="post">
	
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										
										<s:text name="Search Complaints"></s:text>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
			<table width="100%">
				<tr>
					
					<td width="25%" class="greyboxwk"> <s:text name="complainant.name" /></td>
					<td width="25%" class="greyboxwk"><s:textfield id="name" name="name" />
					</td>
				
					<td width="25%" class="greyboxwk"><s:text name="complainant.initials" /></td>
					<td width="25%" class="greyboxwk">
						<s:textfield id="initials" name="initials" />
					</td>
				</tr>
				<tr>
					
					<td width="25%" class="whiteboxwk"> <s:text name="complainant.lastname" /></td>
					<td width="25%" class="whiteboxwk"><s:textfield id="lastName" name="lastName" />
					</td>
					
					<td width="25%" class="whiteboxwk"><s:text name="phoneNo" /></td>
					<td width="25%" class="whiteboxwk">
						<s:textfield id="phoneNo" name="phoneNo"/>
					</td>
					
				</tr>
				<tr>
					
					<td width="25%" class="greyboxwk"> <s:text name="complainant.email" /></td>
					<td width="25%" class="greyboxwk"><s:textfield id="email" name="email" />
					</td>
					
					<td width="25%" class="greyboxwk"><s:text name="complainant.address" /></td>
					<td width="25%" class="greyboxwk">
						<s:textfield id="address" name="address"/>
					</td>
				</tr>
		 	</table>
		</table>
		 <div class="rbroundbox2">
		
			
		</div>
	
			
			<table width="100%">
							
				<tr>
					<td colspan="4">
						<div class="buttonholderwk">
						<s:submit type="submit" id="save" value="SEARCH" cssClass="buttonfinal" method="search" onclick="return validate();" />
						<input type="button" class="buttonfinal" id="resetbutton" value="RESET" onclick="return resetValues();" />
							<input type="button" class="buttonfinal" value="CLOSE"
								id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>
							
			</table>
				
			
			<div id="tableData" align="center">
			<s:if test="%{searchResult!=null && searchResult.getFullListSize()!=0}">
       		<div id="displaytbl" align="center">	
    		  	<display:table  name="searchResult" export="true" requestURI="" id="myComplaintsListid"  class="its" uid="currentRowObject" >
    			<div STYLE="display: table-header-group" align="center">
      			 
      			 <display:caption  media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List My Complaints
				   </display:caption>
				    <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="searchByName-Report.pdf" /> 
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.excel.filename" value="searchByName-Report.xls"/>	
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
										
				
				<display:column title="Complaint Title" style="width:12%;text-align:center;" property="title" >
			 	</display:column>
			 		
			 	<display:column title="Name " style="width:10%;text-align:center;" property="fullName" >	 						 
				</display:column> 						 	
				 	
				<display:column title="Address" style="width:10%;text-align:center;" property="fullAddress" >
			 	</display:column>
			 	
			 	<display:column title="Phone No" style="width:8%;text-align:center;" property="phoneNumber" >
			 	</display:column>
			 		
			 	<display:column title="Email" style="width:8%;text-align:center;" property="email" >						
				</display:column>
				
				<display:column title="Date" style="width:8%;text-align:center;" property="complaintDate" format="{0,date,dd-MM-yyyy}">						
				</display:column>						 		
					  			 						 
				</div>						
			</display:table>						
		</div>
  	    
  	   </div>
  	   </s:if>
  	 <div>
  	 
				
				
			</s:form>
		</body>

</html>