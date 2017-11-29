<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<script type="text/javascript">
		
		
		function getPropdetails(indexNum)
		{
		
				var target="../view/viewProperty!viewForm.action?propertyId="+indexNum;
				
			openWindow(target);
			}
		function openWindow(newPage) 
	{

		 popupwin = window.open(newPage,"ViewPropertyDetails",'height=600,width=850,left=50,top=100,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no');
		 popupwin.focus();
	}
	function gisload(resultString,mgsession)
	{
	var gisVer='<s:property value="%{gisVersion}"/>';
	var gisCity='<s:property value="%{gisCity}"/>';
	var gisUrl=gisVer+gisCity;
		parent.parent.formFrame.Submit(gisUrl+"/ZoomPropertyMarker.jsp?SESSION="+mgsession+"&DomainName="+gisCity+"&resultString="+resultString,null,"scriptFrame");
		}
		function clearSearch(mgSession,mode)
		{
		var gisVer='<s:property value="%{gisVersion}"/>';
		var gisCity='<s:property value="%{gisCity}"/>';
		var gisUrl=gisVer+gisCity;
		parent.parent.formFrame.Submit(gisUrl+"/clearfindresults.jsp?SESSION="+mgSession+"&DomainName="+gisCity+"&mode="+mode,null,"scriptFrame");
		}
	</script>
		<title><s:text name="gisSearchResult.title" /></title>
	</head>
	<body onload="gisload('${searchResultString}','${SESSION}');">
		<div class="formmainbox">
			<table width="320px" border="0">
				<s:form name="viewform" theme="simple">
					<div class="headingsmallbgnew">
						<s:text name="scrhCriteria"></s:text>
						<span class="mandatory"><s:property
								value="%{searchCreteria}" /> </span> / <s:text name="totProp"></s:text>
						<span class="mandatory"><s:property
								value="%{searchResultList.size}" /> <s:text name="matchRecFound"></s:text></span>
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
								uid="currentRowObject">
								<display:column title="Index Number" headerClass="bluebgheadtd"
									class="blueborderfortd" style="text-align:center">

									<a
										href="${gisVersion}${gisCity}/ZoomToParcelSelect.jsp?id=${currentRowObject.parcelId}&DomainName=${gisCity}&SESSION=${SESSION}"
										target="scriptFrame"
										onclick="getPropdetails('${currentRowObject.indexNum}')">
										${currentRowObject.indexNum} </a>
								</display:column>

								<display:column property="ownerName" title="Owner Name"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />

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
								<span class="mandatory"> <s:text name="noRecFound"></s:text></span>
							</td>
						</tr>
					</s:else>
					<input type="button" value="Search Again" class="button"
						onclick="clearSearch('${SESSION}','${mode}');" />

				</s:form>
				
			</table>
		</div>
	</body>
</html>

