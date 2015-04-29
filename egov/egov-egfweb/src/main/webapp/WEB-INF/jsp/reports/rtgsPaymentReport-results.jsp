#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>

<s:if test="%{searchResult.fullListSize != 0}">
<display:table name="paginatedList" id ="currentRowObject" uid="currentRowObject" pagesize = "30" class="tablebottom" style="width:100%;" cellpadding="0" cellspacing="0" export="true" requestURI="">
			<display:caption>
			<div class="headingsmallbgnew" align="center" style="text-align:center;width:98%;">
				<s:property value="%{heading}" />
			</div>
			</display:caption>	
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Sl.No" style="width:4%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
			</display:column>			
					
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="RTGS Number" style="width:6%;text-align:center" property="rtgsNumber" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="RTGS Date" style="width:6%;text-align:center" property="rtgsDate" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="RTGS Payment Amount" style="width:5%;text-align:right" property="paymentAmount" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="TNEB Region" style="width:7%;text-align:center" property="region" />	
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Bill Month" style="width:3%;text-align:center" property="month" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="IOB Paid Amount" style="width:5%;text-align:right" property="IOBPaidAmount" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Un-paid Amount" style="width:5%;text-align:right" property="unPaidAmount" />				
			<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Number of Bills Unpaid" style="width:5%;text-align:right" property="numOfBillsUnpaid" />
			
				
				<display:caption  media="pdf">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:property value="%{heading}" />  
			   </display:caption>
			   <display:caption  media="excel">
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  <s:property value="%{heading}" />
				</display:caption>
					<display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="RTGS Payment Report.pdf" /> 
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.excel.filename" value="RTGS Payment Report.xls"/>	
					<display:setProperty name="export.csv" value="false" />	
					<display:setProperty name="export.xml" value="false" />							
</display:table>
				 	
</s:if>
<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
</s:elseif>

