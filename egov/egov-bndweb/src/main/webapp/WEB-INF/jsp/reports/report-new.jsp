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
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>

<title>Periodic Report For <s:property value ="%{regType}"/> Certificate</title>

<head>
	
</head>

<script language="javascript" type="text/javascript">
	
	

		function Print(){
	
			}
		
	

</script>
		<body onload="">
		
		<s:form action="report" theme="simple" name="reportdetails">
			<s:token/>
		<div class="formmainbox">
		
	
	</div>	 
	
	<div class="formmainbox">
		<h1 class="subhead" ><s:property value="%{subTitle}"/></h1>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

	
	       <tr>
	            <td class="bluebox">&nbsp;</td>
	            <td class="bluebox">&nbsp;</td>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox"><s:text name="from.date.lbl"/><br>
	   			<s:date format="dd/MM/yyyy" name="fromDate" var="TempfromDate"/>
	   			<s:property  value="%{TempfromDate}"/></td>	   		
	   			<td class="bluebox" ><s:text name="to.date.lbl"/><br>
	   			<s:date format="dd/MM/yyyy" name="toDate" var="TemptoDate"/>
	   			<s:property value="%{TemptoDate}"/></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      
	       
	      
	</table>
	
	</div> 
			
		<div id="tableData">
			 <div class="infostyle" id="shoppingcomplex_error" style="display:none;"></div> 
		 <s:if test="%{birthDeathList!=null}">
          		 <div id="displaytbl">	
          		     	 <display:table  name="birthDeathList" export="true" requestURI="" id="birthDeathList"  class="its" uid="currentRowObject"  >
          			 	  <div STYLE="display: table-header-group" align="center">
 						 	
 						 	   	<display:column title=" Srl No" style="text-align:center;"  >
 						 	     <s:property value="%{#attr.currentRowObject_rowNum}"/>
 						 	    </display:column>
 						
 						 		<display:column title="Registration Number" style="text-align:center;" property="registrationno"  />
						 					 							 						 							 
 						 		<display:column title="Registration Date"  style="text-align:center;" property="registrationdate" format="{0,date,dd/MM/yyyy}" />
 						
								 <display:column title="Date of Event" style="text-align:center;" property="eventdate"  format="{0,date,dd/MM/yyyy}"/>	
								 
								  <display:column title="Name" style="text-align:center;" property="citizenFullName"  />
								  
								   <display:column title="Father Name" style="text-align:center;" property="fatherName"  />						 						 						
 							
 							   <display:column title="Mother Name" style="text-align:center;" property="motherName"  />	
 							
 					           <display:column title="Sex" style="text-align:center;" property="sex"  />
								  
 							   <display:column title="Place of Event" style="text-align:center;" property="placeofEvent"  />
 							   <display:caption media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Registration Report
						   </display:caption>	
 							<display:setProperty name="export.pdf" value="true" />
							<display:setProperty name="export.pdf.filename" value="registration.pdf" /> 
							<display:setProperty name="export.excel" value="true" />
					    	<display:setProperty name="export.excel.filename" value="registration.xls"/>	
					        <display:setProperty name="export.csv" value="true" />	
					       <display:setProperty name="export.xml" value="false" />	
						</div>
					</display:table>
					</div>
  	   			</s:if>
  	     </div>
  	 
  	 
  	    
  	     <s:if test="%{birthDeathList!=null}"> 
  	      <div  class="buttonbottom" id="printtab" align="center" >
		<table>
		<tr>    		  	  		  	
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>
   </s:if>
  
  	     
	

	</s:form>
	</body>
	</html>
