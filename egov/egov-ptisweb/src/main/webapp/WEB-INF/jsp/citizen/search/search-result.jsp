<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<script type="text/javascript">
		function getPropdetails(obj,indexNum)
		{
		   var selectedValue = obj.options[obj.selectedIndex].value;	       
	       if(selectedValue=="ViewProperty")
			{
				window.location="../../view/viewDCBProperty!displayPropInfo.action?propertyId="+indexNum;
			}
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
								<display:column property="indexNum"
									title="Index Number"
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
										onchange="getPropdetails(this,'<s:property value="%{#attr.currentRowObject.indexNum}"/>')">									
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
								<s:submit value="Search Again" cssClass="buttonsubmit"
									method="searchForm"></s:submit>
							</div>
						</td>
					</tr>
				</s:form>
			</table>
		</div>
	</body>
</html>
