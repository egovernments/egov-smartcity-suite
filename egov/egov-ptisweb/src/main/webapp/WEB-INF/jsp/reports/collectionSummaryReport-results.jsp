<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------   -->
<%@ include file="/includes/taglibs.jsp" %>

<s:if test="%{srchFlag}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="tablebottom" style="max-width:960px;margin:0 auto;border:1px solid #d4d4d4;"> 
		 <tbody>
		  <tr> 
			 <s:if test="%{dateSelected == 'currentDate'}">
				  <td style="padding:5px;"><s:text name="collectionsummary.notetext"></s:text></td>
			</s:if>
		  </tr>	
		 <tr>
	        <th class="bluebgheadtd" style="text-align:center;font-size:14px;"><s:text name="collectionsummary.pagetitle" /></th>
	    </tr>
		
		<s:if test="%{resultList != null && !resultList.isEmpty()}">	   
		<tr>
			<td>
				<display:table name="resultList"  pagesize = "30" export="true" requestURI="" 
					 class="tablebottom" uid="currentRowObject">
					 
					 <display:caption>
						<div class="headingbg">
							<s:if test="%{mode == 'zoneWise'}">
								<s:text name="collectionsummary.zoneWiseCollSummReport" />
							</s:if>
							<s:elseif test="%{mode == 'wardWise'}">
								<s:text name="collectionsummary.wardWiseCollSummReport" />
							</s:elseif>
							<s:elseif test="%{mode == 'blockWise'}">
								<s:text name="collectionsummary.blockWiseCollSummReport" />
							</s:elseif>
							<s:elseif test="%{mode == 'localityWise'}">
								<s:text name="collectionsummary.localityWiseCollSummReport" />
							</s:elseif>
							  (From Date : <s:property value="%{fromDate}"/> Till  To Date : <s:property value="%{toDate}"/>)
						</div>
					</display:caption>
					
					<s:if test="%{mode == 'zoneWise'}">
						<display:column title="Zone" property="Zone"
							style="text-align:center;width:10%" headerClass="bluebgheadtd"
							class="blueborderfortd" />
					</s:if> 
					<s:elseif test="%{mode == 'wardWise'}">
						<display:column title="Ward" property="Ward"
							style="text-align:center;width:10%" headerClass="bluebgheadtd"
							class="blueborderfortd" />
					</s:elseif>
					<s:elseif test="%{mode == 'blockWise'}">
						<display:column title="Block" property="Block"
							style="text-align:center;width:10%" headerClass="bluebgheadtd"
							class="blueborderfortd" />
					</s:elseif>
					<s:elseif test="%{mode == 'localityWise'}">
						<display:column title="Locality" property="Locality"
							style="text-align:center;width:10%" headerClass="bluebgheadtd"
							class="blueborderfortd" />
					</s:elseif>
					<display:column title="Arrear Tax Amount" property="ArrearTaxAmount"
						style="text-align:right;width:10%" headerClass="bluebgheadtd"
						class="blueborderfortd"/>
					<display:column title="Arrear LibraryCess Amount" property="ArrearLibraryCess"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Arrear Total" property="ArrearTotal"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Current Tax Amount" property="TaxAmount"
						style="text-align:right;width:10%" headerClass="bluebgheadtd"
						class="blueborderfortd"/>
					<display:column title="Current LibraryCess Amount" property="LibraryCess"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Current Total" property="CurrentTotal"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Penalty" property="Penalty" style="text-align:right;width:10%"
						headerClass="bluebgheadtd" class="blueborderfortd"/>
					<display:column title="Arrear Penalty Amount" property="ArrearPenalty"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Penalty Total" property="PenaltyTotal"
					style="text-align:right;width:10%" headerClass="bluebgheadtd"
					class="blueborderfortd"/>
					<display:column title="Grand Total" property="Total" headerClass="bluebgheadtd"
						class="blueborderfortd" style="width:10%;text-align:right"/>
					<display:setProperty name="export.csv" value="false" />
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.xml" value="false" />
					<display:setProperty name="export.pdf" value="true" />
				</display:table>
			</td>
		</tr>
		</s:if>
		<s:else>
		<tr>
			<td style="padding:5px;">
				<s:text name="noRecsFound"></s:text>
			</td>
		</tr>
		</s:else>
		</tbody>
	</table>
</s:if>