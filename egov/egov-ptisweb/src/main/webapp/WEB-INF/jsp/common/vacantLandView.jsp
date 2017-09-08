<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!-- <div align="center" class="overflow-x-scroll"> -->
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="vacantLandTable">
	
	<tr>
	    <th class="bluebgheadtd"><s:text name="surveyNumber" /></th>
	    <th class="bluebgheadtd"><s:text name="pattaNumber" /></th>
		<th class="bluebgheadtd"><s:text name="vacantLandArea" /></th>
		<th class="bluebgheadtd"><s:text name="MarketValue" /></th>
		<th class="bluebgheadtd"><s:text name="currentCapitalValue"/></th>
		<th class="bluebgheadtd"><s:text name="constCompl.date"/></th>
		<th class="bluebgheadtd"><s:text name="lbl.vl.plotarea"/></th>
		<th class="bluebgheadtd"><s:text name="lbl.layout.authority"/></th>
		<th class="bluebgheadtd"><s:text name="lbl.layout.permitno"/></th>
		<th class="bluebgheadtd"><s:text name="lbl.layout.permitdate"/></th>
    </tr>
     <s:if test="%{property != null}">
     <tr id="vacantLandRow">
        <td class="blueborderfortd" align="center">
		 	<span class="bold"><s:property value="%{propertyDetail.surveyNumber}" default="N/A"/></span>
		</td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><s:property value="%{propertyDetail.pattaNumber}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center"> 
        	<span class="bold"><s:property value="%{propertyDetail.sitalArea.area}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><fmt:formatNumber type = "number" value = "${propertyDetail.marketValue}" /></span>
		</td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><fmt:formatNumber type = "number" value = "${propertyDetail.currentCapitalValue}" /></span>
        </td>
        <td class="blueborderfortd" align="center">
        <s:date name="%{property.propertyDetail.dateOfCompletion}" var="occupationDate" format="dd/MM/yyyy" />
        	<span class="bold"><s:property value="%{#occupationDate}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><s:property value="%{propertyDetail.vacantLandPlotArea.name}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><s:property value="%{propertyDetail.layoutApprovalAuthority.name}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><s:property value="%{propertyDetail.layoutPermitNo}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        <s:date name="%{propertyDetail.layoutPermitDate}" var="layoutPermitDate" format="dd/MM/yyyy" />
        	<span class="bold"><s:property value="%{#layoutPermitDate}" default="N/A"/></span>
        </td>
    </tr>
     </s:if>
     <s:else>
     <tr id="vacantLandRow">
        <td class="blueborderfortd" align="center">
		 	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.surveyNumber}" default="N/A"/></span>
		</td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.pattaNumber}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center"> 
        	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.sitalArea.area}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd" align="center">
        	<span class="bold"><fmt:formatNumber type = "number" value = "${basicProperty.property.propertyDetail.marketValue}" /></span>
		</td>
        <td class="blueborderfortd">
        	<span class="bold"><fmt:formatNumber type = "number" value = "${basicProperty.property.propertyDetail.currentCapitalValue}" />" default="N/A"/></span>
        </td>
        <td class="blueborderfortd">
        <s:date name="%{basicProperty.property.propertyDetail.dateOfCompletion}" var="occupationDate" format="dd/MM/yyyy" />
        	<span class="bold"><s:property value="%{#occupationDate}" default="N/A"/></span>
        </td>
         <td class="blueborderfortd">
        	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.vacantLandPlotArea.name}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd">
        	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.layoutApprovalAuthority.name}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd">
        	<span class="bold"><s:property value="%{basicProperty.property.propertyDetail.layoutPermitNo}" default="N/A"/></span>
        </td>
        <td class="blueborderfortd">
        <s:date name="%{basicProperty.property.propertyDetail.layoutPermitDate}" var="layoutPermitDate" format="dd/MM/yyyy" />
        	<span class="bold"><s:property value="%{#layoutPermitDate}" default="N/A"/></span>
        </td>
    </tr>
     </s:else>
   	</table>
   	<!-- </div> -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="vacantLandTable">
   <tr>
   <td colspan="6">
      <table class="tablebottom" style="width: 100%;">
         <tbody>
            <tr>
				  <td colspan="4">
				      <div class="headingsmallbg">
				         <span class="bold"><s:text name="boundaries" /></span> 
			          </div>
			         </td>
			  </tr>
            <tr>
               <th class="bluebgheadtd"><s:text name="North" /></th>
               <th class="bluebgheadtd"><s:text name="East" /></th>
               <th class="bluebgheadtd"><s:text name="West" /></th>
               <th class="bluebgheadtd"><s:text name="South" /></th>
            </tr>
           <tr>
		        <td class="blueborderfortd" align="center">
				   <span class="bold"><s:property value="%{basicProperty.propertyID.northBoundary}" default="N/A"/></span>
				</td>
				<td class="blueborderfortd" align="center">
				   <span class="bold"><s:property value="%{basicProperty.propertyID.eastBoundary}" default="N/A"/></span>
				</td>
				<td class="blueborderfortd" align="center">
				   <span class="bold"><s:property value="%{basicProperty.propertyID.westBoundary}" default="N/A"/></span>
				</td>
				<td class="blueborderfortd" align="center">
				   <span class="bold"><s:property value="%{basicProperty.propertyID.southBoundary}" default="N/A"/></span>
				</td>
		    </tr>
            
         </tbody>
      </table>
   </td>
</tr>
	
</table>
