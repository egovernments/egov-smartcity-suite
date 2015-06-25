<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<%-- <sx:head /> --%>
		<script type="text/javascript">
			function populateWard() {
				populatewardId( {
					zoneId : document.getElementById("zoneId").value
				});
			}
			function onSubmit(obj,formId){
				var formObj = document.getElementById(formId);
				formObj.action=obj;
				formObj.submit;
			   return true;
			} 

		</script>
		<title><s:text name="searchProp.title"></s:text></title>
	</head>
	<body>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<s:if test="%{hasActionMessages()}">
			    <div id="actionMessages" class="messagestyle">
			    	<s:actionmessage theme="simple"/>
			    </div>
			</s:if>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<s:form action="searchProperty" name="indexform" theme="simple" id="indexform">
							<tr>
								<td width="100%" colspan="4" class="headingbg">												
									<div class="headingbg">					
										<s:text name="search.index.num" />									
									</div>									
								</td>
							</tr>					
							<tr><td colspan="4"><br/></td></tr>							
							<tr>
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">
									<s:text name="prop.Id" />
									<span class="mandatory1">*</span> :
								</td>
								
								<td class="bluebox">
									<s:textfield name="indexNum" id="indexNum" value="%{indexNum}" maxlength="30"/>
								</td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox" colspan="2">
								   <br/>
									<div class="greybox" style="text-align:center">
										<s:hidden id="mode" name="mode" value="index"></s:hidden>
										<s:submit name="search" value="Search" cssClass="buttonsubmit" onclick="return onSubmit('searchProperty-srchByIndex.action', 'indexform');"></s:submit>
									</div>
								</td>
								<td class="greybox">&nbsp;</td>
							</tr>
						</s:form>
					</table>			

					<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<s:form name="zoneform" theme="simple" id="zoneform">
						<tr>
							<td width="100%" colspan="4" class="headingbg">												
								<div class="headingbg">					
									<s:text name="search.zone.ward" />
								</div>
							</td>					
						</tr>
						<tr><td colspan="4"><br/></td></tr>		
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">
								<s:text name="Zone" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="bluebox">
								<s:select name="zoneId" id="zoneId" list="zoneBndryMap"
									listKey="key" listValue="value" headerKey="-1"
									headerValue="%{getText('default.select')}" value="%{zoneId}"
									onchange="populateWard()" />
								<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
									dropdownId="wardId" url="common/ajaxCommon-wardByZone.action" />
							</td>
							<td class="bluebox">&nbsp;</td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox">
								<s:text name="Ward" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="greybox">
								<s:select name="wardId" id="wardId" list="dropdownData.wardList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="%{getText('default.select')}" value="%{wardId}"/>
							</td>
							<td class="greybox">&nbsp;</td>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">
								<s:text name="HouseNo" /> :
							</td>
							<td class="bluebox">
								<s:textfield name="houseNumBndry" />
							</td>
							<td class="bluebox">&nbsp;</td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox">
								<s:text name="OwnerName" />
								:
							</td>
							<td class="greybox">
								<s:textfield name="ownerNameBndry" />
							</td>
							<td class="greybox">&nbsp;</td>
						</tr>
						
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox" colspan="2">
							    <br/>
								<div class="greybox" style="text-align:center">		
									<s:hidden id="mode" name="mode" value="bndry"></s:hidden>
									<s:submit name="search" value="Search" cssClass="buttonsubmit" onclick="return onSubmit('searchProperty-srchByBndry.action', 'zoneform');" ></s:submit>
								</div>
							</td>						
							<td class="greybox">&nbsp;</td>
						</tr>
						<br/>
					</s:form>
				</table>
				
				<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<s:form name="areaform" theme="simple" id="locationform">
					<tr>
						<td width="100%" colspan="4" class="headingbg">												
							<div class="headingbg">					
								<s:text name="search.ownerName" />
							</div>
						</td>
					</tr>
					<tr><td colspan="4"><br/></td></tr>		
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="Location" /><span class="mandatory1">*</span> :
						</td>
						<td class="bluebox">
							<s:select name="locationId" id="locationId" list="dropdownData.Location" cssStyle="width: 150px;" listKey="id" listValue="name"
								headerKey="-1" headerValue="----Choose----" value="%{locationId}" />
						</td>
						<td class="bluebox">&nbsp;</td>
						
					</tr> 
					
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="OwnerName" />
							<span class="mandatory1">*</span> :
						</td>
						<td class="greybox">
							<s:textfield name="ownerName" />
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="HouseNo" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="houseNumArea" />
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" colspan="2">
						    <br/>
							<div class="greybox" style="text-align:center">
								<s:hidden id="mode" name="mode" value="location"></s:hidden>
								<s:submit name="search" value="Search" cssClass="buttonsubmit" onclick="return onSubmit('searchProperty-srchByLocation.action', 'locationform');"></s:submit>
							</div>
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					<br/>
				</s:form>
			</table>
			<!-- objection search details -->
			<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<s:form name="demandForm"  theme="simple" id="demandForm">
					<tr>
						<td width="100%" colspan="4" class="headingbg">												
							<div class="headingbg">					
								<s:text name="search.objection" />
							</div>
						</td>
					</tr>
					
					
					
					<tr>
					  <td class="bluebox" style="text-align:center;" colspan="4">
					   <br/>
					    From <span class="mandatory1">*</span>:  &nbsp;&nbsp;&nbsp; 
					    <s:textfield name="fromdemand"  />
					    &nbsp;&nbsp;&nbsp; To <span class="mandatory1">*</span>: &nbsp;&nbsp;&nbsp;
					     <s:textfield name="todemand"  />
					  </td>
					</tr>
					<tr>
						<td class="bluebox" colspan="4">
							&nbsp; &nbsp; &nbsp;
						</td>
					</tr>				
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox" colspan="2">
							<div class="bluebox" style="text-align:center">
								<s:hidden id="mode" name="mode" value="demand"></s:hidden>
								<s:submit name="search" value="Search" cssClass="buttonsubmit" onclick="return onSubmit('searchProperty-searchByDemand.action', 'demandForm');"></s:submit>
							</div>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<br/>
					</s:form>
					</table>
		
			<div align="left" class="mandatory1" style="font-size: 11px">
			  &nbsp;&nbsp;<s:text name="mandtryFlds"></s:text>
			</div>
					
		</div>
	</body>
</html>
