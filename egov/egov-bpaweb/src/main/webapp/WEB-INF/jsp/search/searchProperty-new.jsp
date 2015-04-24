<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ page buffer="none"%>
<%@ page import= "org.egov.commons.utils.EgovInfrastrUtilInteface,org.egov.commons.utils.EgovInfrastrUtil,org.egov.infstr.utils.*,org.egov.lib.rjbac.dept.ejb.api.*,org.egov.lib.rjbac.dept.*,org.egov.lib.admbndry.ejb.api.*,org.egov.lib.admbndry.*,java.util.*,javax.naming.*,javax.ejb.*"
%>

<html>
<head>
<title>Search Property</title>

	
<script type="text/javascript">

   		function checkOnSubmitForBillNum()
   		{
   			var zoneNumber=document.getElementById('zoneNo').value;
   			var wardNumber=document.getElementById('wardNo').value;
   			var billNumber=document.getElementById('billNum').value;
   			var subNumber=document.getElementById('subNum').value;
   			
   			if(zoneNumber==null || zoneNumber=="")
	   		 {
	   		 	alert("Please Enter Zone Number");
	   		 	document.getElementById('zoneNo').focus();
	   		 	return false;
	   		 }
	   		 
	   		if(wardNumber==null || wardNumber=="")
	   		{
	   			alert("Please Enter Ward Number");
	   			document.getElementById('wardNo').focus();
	   			return false;
	   		}
  		
	   		if(billNumber==null || billNumber=="")
	   		 {
	   		 	alert("Please Enter Bill Number");
	   		 	document.getElementById('billNum').focus();
	   		 	return false;
	   		 }
   		} 

   function checkBeforeSubmitforAreaLocation()
   	{
   		var area=document.getElementById('area').value;
   		var location=document.getElementById('location').value;
   		var ownerName=document.getElementById('ownerName').value;
   		var houseNumber=document.getElementById('doorNum').value;
   		if(area==null || area=="" || area=="0")
   		{
   		alert("Please Select an Area");
   		return false;
   		}
   		if(location==null || location=="" || location=="0")
   		{
   		alert("Please Select a Location");
   		return false;
   		}
   		if(ownerName!=null && ownerName!=""){
   		   if(ownerName.length < 4){
   		       alert("Please Enter minimum 4 digit Owner Name");
   		    document.getElementById('ownerName').focus();
   		       return false;
   		   }
   		}
   
      }

	function checkDemandBalance(propertyId,currentTaxDue,arrearTaxDue,zone,ward,area,location,street,ownerName) {
	// Commented coz collection pending need not be checked 
	
	if( document.getElementById('checkPropertyForDues').value=='true'){
		if (currentTaxDue > 0 || arrearTaxDue > 0) {
			alert("This Property has Collection Pending");
		}else {                           
			window.opener.callSetPropertyId(propertyId,zone,ward,area,location,street,ownerName);
			window.close(); 
		}
	}
	else{
	window.opener.callSetPropertyId(propertyId,zone,ward,area,location,street,ownerName);
			window.close();
    }
	  }
</script>
               
</head> 
<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<h1 class="subhead">
					<font color="red"><s:text name="searchCondn"/></font>
			</h1>
			<tr>
				<td>
				<s:form action="searchProperty" theme="simple" name="searchPropertyForm">
			
				<div class="headingbg" align="center">
					<s:text name="searchByBillNum" /> 
				</div>
				<div align="center">
						<table width="100%" border="0" cellpadding="2" cellspacing="0">

							<tr>
								
								<td class="bluebox" width="5%">&nbsp;</td>
								 <s:hidden id="checkPropertyForDues" name="checkPropertyForDues" value="%{checkPropertyForDues}"/>
								<td class="bluebox" width="2%"><s:text name="zone"/> :<span class="mandatory">*</span></td>
								<td class="bluebox" width="2%"><s:text name="ward" /> :<span class="mandatory">*</span></td>
								<td class="bluebox" width="2%"><s:text name="billNumber" /> :<span class="mandatory">*</span></td>
								<td class="bluebox" width="2%"><s:text name="subNum" /> :</td>
								<td class="bluebox" width="2%">&nbsp;</td>
							</tr>
							<tr>
								<td class="greybox" width="5%">&nbsp;</td>
								<td class="greybox" width="2%">
									<s:textfield id="zoneNo" name="zoneNo" value="%{zoneNo}" size="2" maxlength="2" 
										onKeyUp="return autoTab(this, 2, event);"/>
								</td>
								<td class="greybox" width="2%">
									<s:textfield id="wardNo" name="wardNo" value="%{wardNo}" size="3" maxlength="3" 
										onKeyUp="return autoTab(this, 3, event);"/>
								</td>
								<td class="greybox" width="2%">
									<s:textfield id="billNum" name="billNum" value="%{billNum}" size="5" maxlength="5" 
										onKeyUp="return autoTab(this, 5, event);"/>
								</td>
								<td class="greybox" width="2%">
									<s:textfield id="subNum" name="subNum" value="%{subNum}" size="3" maxlength="3" 
										onKeyUp="return autoTab(this, 3, event);"/>
								</td>
								<td class="greybox" width="2%">&nbsp;</td>
							</tr>
						</table>
				</div>
						<div class="buttonbottom" align="center">
									<s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  
										method="searchByBillNumber" onclick="return checkOnSubmitForBillNum();"/>
						</div>
					</s:form>
				</td>
			</tr>

			<tr>
				
				<td>
					<s:form action="searchProperty" theme="simple" name="searchPropertyForm">
					<div class="headingbg">
						<s:text name="searchByAreaLoc" />
					</div>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<%
								try {
										ServiceLocator serviceloc = ServiceLocator.getInstance();
										BoundaryManagerHome bhome = (BoundaryManagerHome) serviceloc
												.getLocalHome("BoundaryManagerHome");
										BoundaryManager bmang = bhome.create();
										BoundaryTypeManagerHome bthome = (BoundaryTypeManagerHome) serviceloc
												.getLocalHome("BoundaryTypeManagerHome");
										BoundaryTypeManager btmang = bthome.create();
										HeirarchyTypeManagerHome hhome = (HeirarchyTypeManagerHome) serviceloc
												.getLocalHome("HeirarchyTypeManagerHome");
										HeirarchyTypeManager hmang = hhome.create();
										HeirarchyType hType = null;
										Set hSet = hmang.getAllHeirarchyTypes();
										String type = "LOCATION";
										String bType = "AREA";

										for (Iterator iter = hSet.iterator(); iter.hasNext();) {
											HeirarchyType hierarchyType = (HeirarchyType) iter.next();
											if (hierarchyType.getName().equalsIgnoreCase(type))
												hType = hierarchyType;
										}
										if (hType != null) {
											BoundaryType BT = btmang.getBoundaryType(bType, hType);
											List bndryList = new BoundaryDAO()
													.getAllBoundariesByBndryTypeId(BT.getId());
							%>
								<tr>
								<td class="bluebox" colspan="2" width="30%">&nbsp;</td>
								<td class="bluebox"><s:text name="area" />:<span class="mandatory">*</span></td>
								<td class="bluebox">
								<select id="area" name="area" class="selectnew" tabindex="14" onchange="loadSelectData('<%=request.getContextPath()%>/commonyui/egov/loadComboAjax.jsp', 'eg_boundary', 'ID_BNDRY', 'name', 'parent=#1 order by name', 'area', 'location');"  >
								<option   value ="0" > <s:text name="choose" /> </option>
								<%
									for (Iterator iter = bndryList.iterator(); iter.hasNext();) {
													Boundary bndry = (Boundary) iter.next();
								%>
								<option value="<%=bndry.getId()%>" ><%=bndry.getName()%> </option>
								<%
									}
											}

										} catch (Exception e) {
										}
								%>
								</select>
								</td>
								<td class="bluebox" colspan="2">&nbsp;</td>
								</tr>
								<tr>
								<td class="greybox" colspan="2" width="30%">&nbsp;</td>
								<td class="greybox"><s:text name="locality" />:<span class="mandatory">*</span></td>
								<td class="greybox">
									<select id="location" name="location" class="selectnew" tabindex="15" onchange="loadSelectData('<%=request.getContextPath()%>/commonyui/egov/loadComboAjax.jsp', 'eg_boundary', 'ID_BNDRY', 'name', 'parent=#1 order by name', 'location', 'streetForArea');"  >
										<option  value = "0"> <s:text name="choose" /> </option>
									</select>
								</td>
								<td class="greybox" colspan="2">&nbsp;</td>
								</tr>
								<tr>
								<td class="bluebox" colspan="2" width="30%">&nbsp;</td>
								<td class="bluebox"><s:text name="streetName" />:</td>
								<td class="bluebox">
									<select id="streetForArea" name="streetForArea" tabindex="16" class="selectnew" >
										<option  value = "0"><s:text name="choose" /> </option>
									</select>
								</td>
								<td class="bluebox" colspan="2">&nbsp;</td>
								</tr>
							<tr>
								<td class="greybox" colspan="2" width="30%">&nbsp;</td>
								<td class="greybox"><s:text name="ownerName" /> :</td>
								<td class="greybox">
									<s:textfield id="ownerName" name="ownerName" value="%{ownerName}" maxlength="256" size="20"/> 
									Enter Min 4 Chars</td>
								<td class="greybox" colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td class="bluebox" colspan="2" width="30%">&nbsp;</td>
								<td class="bluebox"><s:text name="drNo" /> :</td>
								<td class="bluebox">
									<s:textfield id="doorNum" name="doorNum" value="%{doorNum}" maxlength="16" size="20"/>
								</td>
								<td class="bluebox" colspan="2">&nbsp;</td>
							</tr>
						
						</table>
							<div class="buttonbottom" align="center">
											<s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  
												method="searchByAreaLocation" onclick="return checkBeforeSubmitforAreaLocation();"/>
											<input type="button" name="button2" id="button2" value="Close"
												class="button" onclick="window.close();" />
							</div>
					</s:form>
				</td>
			</tr>
		</table>
		<div align="left" class="mandatory" style="font-size: 11px">
			<s:text name="mandtryFlds" /><span class="mandatory">*</span>
		</div>

	<div>

		<c:if test="${fn:length(totalSearchResult) > 0  && initialSearch == 'NO'}">
			<br />
				<s:text name="searchCriteria" /> : <span class="mandatory"> <s:property value="searchCriteria" /> </span> </p>
				Total Properties : <span class="mandatory"> <c:out value="${fn:length(totalSearchResult)}" /> </span>
			<br />
			<div>
				Search Value: <span class="mandatory"> <s:property value="searchValue" /> </span>
			</div>
		</c:if>

	<c:if test="${fn:length(totalSearchResult) <= 0  && initialSearch == 'NO'}">
		<%
			String msgStr = "Sorry, No property matches the selected criteria";
		%>
		<div align="center">
			<font color="red"><b><%=msgStr%></b></font>
		</div>
	</c:if>

	<c:if test = "${fn:length(totalSearchResult) > 0}">
    	<tr>
    		<display:table name="totalSearchResult" id="linksTable" class="tablebottom" style="width:100%" uid="curRObj">
    		<div STYLE="display: table-header-group" align="center">
    			<display:column class="hidden" headerClass="hidden">
    				<s:hidden id="zone" name="zone" value="%{zone}" />
    			</display:column>
    			<display:column class="hidden" headerClass="hidden">
    				<s:hidden id="ward" name="ward" value="%{ward}" />
    			</display:column>
    			<display:column class="hidden" headerClass="hidden">
    				<s:hidden id="area" name="area" value="%{area}" />
    			</display:column>
    			<display:column class="hidden" headerClass="hidden">
    				<s:hidden id="location" name="location" value="%{location}" />
    			</display:column>
    			<display:column class="hidden" headerClass="hidden">
    				<s:hidden id="street" name="street" value="%{street}" />
    			</display:column>
    			
				<display:column title="Property Id"  sortable="true" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center">
					<a href="#" onclick="checkDemandBalance('${curRObj.propertyId}','${curRObj.currentTaxDue}','${curRObj.arrearTax}',
								'${curRObj.zone}','${curRObj.ward}','${curRObj.area}','${curRObj.location}','${curRObj.street}','${curRObj.ownerName}')">
 						 		 ${curRObj.propertyId}
 					</a>
 				</display:column>
				<display:column property="oldPropertyId" title="Old Bill No" headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
				<display:column property="ownerName"  title="Owner Name" headerClass="bluebgheadtd" class="blueborderfortd" style="width:17%;text-align:left" />	
				<display:column property="propertyAddress"  title="Property Address"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:35%;text-align:left" />
				<display:column property="currentTax"  title="Current Tax"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:15%;text-align:center"/>
				<display:column property="currentTaxDue"  title="Current TaxDue"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:10%;text-align:center"/>
				<display:column property="arrearTax"  title="Arrear Tax Due"  headerClass="bluebgheadtd" class="blueborderfortd" style="width:13%;text-align:center" />
				
			</div>
    		</display:table>
    	</tr>
  		</c:if>
		</div>
</body>
</html>
