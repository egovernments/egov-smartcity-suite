<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<%@ include file="/includes/taglibs.jsp"%>

<s:if test="pagedResults != null && pagedResults.getList() != null && !pagedResults.getList().isEmpty()">
	<br />
	<fieldset>
		<legend align="center">
			<b>Search Result</b>
		</legend>
		
		<display:table name="pagedResults" uid="license" style="background-color:#e8edf1;width:98%;padding:0px;margin:10 0 0 5px;" pagesize="20" export="true" requestURI="searchTrade!search.action?reportSize=${reportSize}" excludedParams="reportSize" cellpadding="0" cellspacing="0">
			<display:column class="blueborderfortd" title="S.No" style="border-left:1px solid #E9E9E9">
				<s:property value="%{#attr.license_rowNum+(page == 0  ? 0: (page-1))*10}" />
			</display:column>
			<display:column class="blueborderfortd" title="Application Number" media="html">
				<c:if test="${license.applicationNumber == null || license.applicationNumber == ''}">
					&nbsp;
				</c:if>
				<c:choose>
					<c:when test='${license.applicationNumber != null && license.applicationNumber != ""}'>
						<a href="../newtradelicense/web/editTradeLicense-beforeEdit.action?model.id=${license.id}" target="_blank"> ${license.applicationNumber} </a>
					</c:when>
				</c:choose>
			</display:column>
			<display:column class="blueborderfortd" title="Application Number" media="excel pdf">
				<c:choose>
					<c:when test='${license.applicationNumber != null && license.applicationNumber != ""}'>
					 ${license.applicationNumber}
					</c:when>
				</c:choose>
			</display:column>
			<display:column class="blueborderfortd" title="Application Date">
				<fmt:formatDate value="${license.applicationDate}" pattern="dd/MM/yyyy" />
			</display:column>
			<display:column class="blueborderfortd" title="Applicant Name">
				<c:if test="${license.licensee.applicantName == null || license.licensee.applicantName ==''}">
					&nbsp;
				</c:if>
				<c:out value="${license.licensee.applicantName}" />
			</display:column>
			<display:column class="blueborderfortd" title="Establishment Name">
				<c:choose>
					<c:when test="${license.nameOfEstablishment != null || license.nameOfEstablishment !=''}">
						<c:out value="${license.nameOfEstablishment}" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column class="blueborderfortd" title="Address">
				<c:out value="${license.address}" />
			</display:column>
			<display:column class="blueborderfortd" title="Zone">
				<s:if test="%{#attr.license.boundary.boundaryType.name!='Ward'}">
					<c:out value="${license.boundary.name}" />
				</s:if>
				<s:elseif test="%{#attr.license.boundary.parent.name == null || #attr.license.boundary.parent.name ==''}">
					&nbsp;
				</s:elseif>
				<s:else>
					<c:out value="${license.boundary.parent.name}" />
			   </s:else>
			</display:column>
			<display:column class="blueborderfortd" title="Ward">
				<c:if test="${license.boundary.name == null || license.boundary.name ==''}">
					&nbsp;
				</c:if>
				<c:if test="${license.boundary.boundaryType.name =='Ward'  }">
				<c:out value="${license.boundary.name}" />
				</c:if>
			</display:column>
			<display:column class="blueborderfortd" title="Trade Name">
				<c:if test="${license.tradeName.name == null || license.tradeName.name ==''}">
					&nbsp;
				</c:if>
				<c:out value="${license.tradeName.name}" />
			</display:column>
			<display:column class="blueborderfortd" title="License Issued Date">
				<fmt:formatDate value="${license.dateOfCreation}" pattern="dd/MM/yyyy" />
			</display:column>
			<display:setProperty name="basic.show.header" value="true" />
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:setProperty name="export.excel.class" value="org.egov.infstr.displaytag.export.EGovExcelView" />
			<display:setProperty name="export.pdf.class" value="org.egov.infstr.displaytag.export.EGovPdfView" />
			<display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.excel" value="true" />
			<display:setProperty name="export.excel.filename" value="tradeLicense-searchTrade.xls" />
			<display:setProperty name="export.pdf" value="false" />
			<display:setProperty name="export.pdf.filename" value="tradeLicense-searchTrade.pdf" />
			<display:setProperty name="export.xml" value="false" />
			<display:setProperty name="paging.banner.placement" value="top" />
		</display:table>

	</fieldset>
	
</s:if>

<% if(request.getAttribute("hasResult") != null && (!(Boolean)request.getAttribute("hasResult"))){%>
<fieldset>
	<legend align="center">
		<b>Search Result</b>
	</legend>
	<div class="subheadnew">
		<s:text name="search.result.notrades" />
	</div>
</fieldset>
<%} %>
