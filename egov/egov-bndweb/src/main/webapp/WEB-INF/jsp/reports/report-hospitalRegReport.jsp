#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>
<title>Hopital Registrered Records </title>
<head>
    <jsp:include page='/WEB-INF/jsp/reports/report.jsp'/> 
</head>


<script  type="text/javascript">

   function validation(){
   
  if(populate("fromDate").value==""){
    warn('<s:text name="fromDate.required"/>');
	return false;
	}

   if(populate("toDate").value==""){
    warn('<s:text name="toDate.required"/>');
	return false;
	}
	
	 if(populate("establishment").value=="0"){
    warn('<s:text name="establishment.required"/>');
	return false;
	}
	
	if(!validatecommon())
	return false;
	
	return true;
	
 }
   
   function resetvalues(){
   dom.get("searchRecords_error").style.display = 'none';
   populate("toDate").value="";
    populate("fromDate").value="";
    populate("establishment").value="0";
     populate("status").value="0";
    if(populate("resultdiv")!=null)
   populate("resultdiv").style.display='none';
   }

</script >

<body onload="">

	<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
		<div class="errorstyle" style="display: none" >			
		</div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />	
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>

	<s:form action="report" theme="simple" name="reportForm">
	
	<jsp:include page='/WEB-INF/jsp/reports/report-common.jsp'/> 
	  		
		<div class="buttonbottom" align="center">
			<table>
				<tr>			
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="hospitalRegReportResult"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetvalues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   
	  	<s:if test="%{searchMode=='result'}">
		 <s:if test="%{birthDeathList!=null}">
		   <div id="resultdiv">
          		 <div id="displaytbl">	
          		     	 <display:table  name="birthDeathList" export="true" requestURI="" id="birthDeathList"  class="its" uid="currentRowObject"  >
          			 	  <div STYLE="display: table-header-group" align="center">
 						 	
 						   	<display:column title=" Srl No" style="text-align:center;"  >
 						 	 <s:property value="%{#attr.currentRowObject_rowNum}"/>
 						 	   </display:column>
 						 	   
 						 		<display:column title="Registration Number" style="text-align:center;" property="registrationno"  />
 						 		
 						 		<display:column title="Hospital Registration Number" style="text-align:center;" property="hospitalRegNo"  />
						 					 							 						 							 
 						 		<display:column title="Registration Date"  style="text-align:center;" property="registrationdate" format="{0,date,dd/MM/yyyy}" />
 						
								 <display:column title="Date of Event" style="text-align:center;" property="eventdate"  format="{0,date,dd/MM/yyyy}"/>	
								 
								  <display:column title="Name" style="text-align:center;" property="citizenFullName"  />
								  
								   <display:column title="Father Name" style="text-align:center;" property="fatherName"  />						 						 						
 							
 							   <display:column title="Mother Name" style="text-align:center;" property="motherName"  />	
 							
 					           <display:column title="Sex" style="text-align:center;" property="sex"  />
								  
 							   <display:column title="Place of Event" style="text-align:center;" property="placeofEvent"  />
 							   
 							     <display:column title="Status" style="text-align:center;" property="registrationstatus.code"  />
 							   <display:caption media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Hospital Registration Report
						   </display:caption>	
 							<display:setProperty name="export.pdf" value="true" />
							<display:setProperty name="export.pdf.filename" value="hospitalregistration.pdf" /> 
							<display:setProperty name="export.excel" value="true" />
					    	<display:setProperty name="export.excel.filename" value="hospitalregistration.xls"/>	
					        <display:setProperty name="export.csv" value="true" />	
					       <display:setProperty name="export.xml" value="false" />	
						</div>
					</display:table>
				
					</div>
					<div>
						<tr>						  		 
			  	
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	     </tr>
					</div> 	   			
  	     </div>
  	     </s:if>
	  	</s:if>
	   
	   
	 
	   
	</s:form>
</body>
</html>
