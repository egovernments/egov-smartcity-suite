<!--  #-------------------------------------------------------------------------------
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
#   In case of any queries, you can reach eGovernments Foundation at contaRct@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<script type="text/javascript">
		function getPropdetails(obj,assessmentNum)
		{
		   var selectedValue = obj.options[obj.selectedIndex].value;	       
	       if(selectedValue=="ViewProperty")
			{
				window.location="../../view/viewDCBProperty-displayPropInfo.action?propertyId="+assessmentNum;
			}
	    }

		function gotoSearchForm(){
			document.viewform.action='${pageContext.request.contextPath}/citizen/search/search-searchForm.action';
			document.viewform.submit(); 
		}
		
	</script>
		<title><s:text name="searchResults.title" /></title>
	</head>
	<body>
		<div class="formmainbox">
			<table width=100% border="0" class="tablebottom">
				<s:form name="viewform" theme="simple">
					<div class="headingsmallbgnew">
						<s:text name="scrhCriteria"></s:text>
						<span class="mandatory"><s:property
								value="%{searchCreteria}" /> </span> /
						<s:text name="totProp"></s:text>
						<span class="mandatory"><s:property
								value="%{searchResultList.size}" /> <s:text
								name="matchRecFound" /> </span>
						<div class="searchvalue1">
							<s:text name="scrhVal"></s:text>
							<s:property value="%{searchValue}" />
						</div>
					</div>
					<s:if
						test="%{searchResultList != null && searchResultList.size >0}">
						<tr>
							<display:table name="searchResultList" id="linksTables"
								pagesize="10" export="true" requestURI="" class="tablebottom"
								style="width:100%" uid="currentRowObject">
								<display:column property="assessmentNum"
									title="Assessment Number"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
								</display:column>
								<display:column property="parcelId"
									title="Parcel Id"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="ownerName"
									title="Owner Name"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="address"
									title="Address"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="currDemand"
									title="Current Tax"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="currDemandDue"
									title="Current Tax Due"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="width:10%;text-align:center" />
								<display:column property="arrDemandDue"
									title="Arrear Tax Due"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column title="Action" headerClass="bluebgheadtd"
									media="html" class="blueborderfortd" style="text-align:center">
									<select id="actionValue" name="actionValue"
										style="align: center" 	
										onchange="getPropdetails(this,'<s:property value="%{#attr.currentRowObject.assessmentNum}"/>')">									
										<option value="">
											<br>
											----Choose----
										</option>										
										<option value="ViewProperty">
							            	View Property
						                </option>																			
									</select>
								</display:column>
								<display:setProperty name="paging.banner.item" value="Record" />
								<display:setProperty name="paging.banner.items_name"
									value="Records" />
								<display:setProperty name="export.pdf" value="false" />
								<display:setProperty name="export.rtf" value="false" />
								<display:setProperty name="export.xml" value="true" />
								<display:setProperty name="export.csv" value="true" />
								<display:setProperty name="export.excel" value="true" />
							</display:table>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td align="center">
								<span class="mandatory"><s:text name="noRecFound"></s:text>
								</span>
							</td>
						</tr>
					</s:else>					
					<tr>
						<td>
							<div class="buttonsearch" align="center">
								<input type="button" value="Close" class="button"
									onClick="window.close()" />
								<input type="submit" value="Search Again" class="button"
+									onClick="gotoSearchForm();" />
							</div>
						</td>
					</tr>
				</s:form>
			</table>
		</div>
	</body>
</html>
