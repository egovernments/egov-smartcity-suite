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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<title><s:text name="registrationupdated.report"/></title>
<head>
    <jsp:include page='/WEB-INF/jsp/reports/report.jsp'/> 
</head>
  
  <body>
  <s:form action="report" theme="simple" name="reportForm">
	<div class="formheading"/></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" border="0">
	
		<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="recordtype.lbl"/>:</td> 
				<td class="bluebox" colspan="2"><s:radio list="registrationTypeList" value="%{regType}" name="regType" id="regType" /></td>
				<td class="bluebox" width="20%">&nbsp;</td>
		</tr>
			
        <tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="from.date.lbl"/>:<span class="mandatory">*</span></td>
				<td class="bluebox">
					<s:date format="dd/MM/yyyy" name="fromDate" var="TempDate"/>
					<s:textfield name="fromDate" id="fromDate"  maxlength="20" value="%{TempDate}"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0" align="absmiddle"/></a>				
				</td>  
				<td class="bluebox" width="12%"><s:text name="to.date.lbl"/>:<span class="mandatory">*</span></td>
				<td class="bluebox">				
					<s:date name="toDate" format="dd/MM/yyyy" var="dateTemp"/>
					<s:textfield name="toDate" id="toDate" maxlength="20" value="%{dateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0" align="absmiddle"/></a>			
				</td>			
				<td class="bluebox">&nbsp;</td>
		</tr>
		<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox"><s:text name="registration.number.lbl"/><span class="mandatory">* </span></td>
				<td class="bluebox"><s:textfield id="regNo" name="regNo" value="%{regNo}" /></td> 
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="20%">&nbsp;</td>
		</tr>
	 </table>
	 
	 <div class="buttonbottom" align="center">
			<table>
				<tr>			
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="updatedRegistrationReportResult" /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
   </div>
   <s:if test="%{searchMode=='result'}">
   
	  <div id="tableData">
			 <div id="displaytbl">
          		   	 <display:table  name="pagedResults" export="true" requestURI="" id="reportID"  class="its" uid="currentRowObject"  >
          		     	<display:column title="Registration Number" style="text-align:center;" property="registrationno"  />
					 	<display:column title="Modified Date" style="text-align:center;" property="modifiedDate" format="{0,date,dd/MM/yyyy}" />	
					 	<display:column title="Changed Values Detail" style="text-align:center;" property="changedValue" />
                     </display:table>
			 </div>
	  <div>
			  	</div>
  	   		</div>
	  	</s:if>
	</s:form>
  </body>
</html>
