#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<sx:head />
		<script type="text/javascript">
			function populateWard() {
				populatewardId( {
					zoneId : document.getElementById("zoneId").value
				});
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
			<center>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<s:form action="searchProperty" name="indexform" theme="simple">
							<div class="formheading"></div>
							
							<tr>
								<td width="100%" colspan="4" class="headingbg">												
									<div class="headingbg">					
										<s:text name="search.index.num" />									
									</div>									
								</td>
							</tr>					
														
							<tr>
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">
									<s:text name="prop.Id" />
									<span class="mandatory">*</span> :
								</td>
								
								<td class="bluebox">
									<s:textfield name="indexNum" id="indexNum" value="%{indexNum}" maxlength="30"/>
								</td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox">
									<s:text name="ParcelID" />
									:
								</td>
								<td class="greybox">
									<s:textfield name="gisId" id="gisId" value="%{gisId}" maxlength="50"/>
								</td>
								<td class="greybox">&nbsp;</td>
							</tr>
							<tr>
								<td class="bluebox" colspan="4">
									&nbsp; &nbsp; &nbsp;
								</td>
							</tr>
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox" colspan="2">
									<div class="greybox" style="text-align:center">
										<s:hidden id="mode" name="mode" value="index"></s:hidden>
										<s:submit name="search" value="Search" cssClass="buttonsubmit" method="srchByIndex"></s:submit>
									</div>
								</td>
								<td class="greybox">&nbsp;</td>
							</tr>
						</s:form>
					</table>			

					<br>
			
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<s:form name="zoneform" theme="simple">
						<tr>
							<td width="100%" colspan="4" class="headingbg">												
								<div class="headingbg">					
									<s:text name="search.zone.ward" />
								</div>
							</td>					
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">
								<s:text name="Zone" />
								<span class="mandatory">*</span> :
							</td>
							<td class="bluebox">
								<s:select name="zoneId" id="zoneId" list="ZoneBndryMap"
									listKey="key" listValue="value" headerKey="-1"
									headerValue="%{getText('default.select')}" value="%{zoneId}"
									onchange="populateWard()" />
								<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
									dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
							</td>
							<td class="bluebox">&nbsp;</td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox">
								<s:text name="Ward" />
								<span class="mandatory">*</span> :
							</td>
							<td class="greybox">
								<s:select name="wardId" id="wardId" list="dropdownData.wardList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="%{getText('default.select')}" value="%{wardId}" />
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
							<td class="bluebox" colspan="4">
								&nbsp; &nbsp; &nbsp;
							</td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox" colspan="2">
								<div class="greybox" style="text-align:center">		
									<s:hidden id="mode" name="mode" value="bndry"></s:hidden>
									<s:submit name="search" value="Search" cssClass="buttonsubmit" method="srchByBndry"></s:submit>
								</div>
							</td>						
							<td class="greybox">&nbsp;</td>
						</tr>
					</s:form>
				</table>
				</center>
				<br>
				<center>
			
				<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<s:form name="areaform" theme="simple">
					<tr>
						<td width="100%" colspan="4" class="headingbg">												
							<div class="headingbg">					
								<s:text name="search.ownerName" />
							</div>
						</td>
					</tr>
					
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="Area" />
							:
						</td>
						<td class="bluebox">
							<s:select name="areaId" id="areaId" list="dropdownData.Area" style="width: 150px;" listKey="id" listValue="name"
								headerKey="-1" headerValue="----Choose----" value="%{areaId}" />
						</td>
						<td class="bluebox">&nbsp;</td>
						
					</tr> 
					
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="OwnerName" />
							<span class="mandatory">*</span> :
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
							<s:textfield name="ownerNameBndry" />
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" width="150px">
							<s:text name="OldNo" />
							:
						</td>
						<td class="greybox">
							<s:textfield name="oldHouseNum" />
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox" colspan="4">
							&nbsp; &nbsp; &nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align:center">
								<s:hidden id="mode" name="mode" value="area"></s:hidden>
								<s:submit name="search" value="Search" cssClass="buttonsubmit" method="srchByArea"></s:submit>
							</div>
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
				</s:form>
			</table>
			<!-- objection search details -->
			<table  border="0" cellspacing="0" cellpadding="0" width="100%">
				<s:form name="objectionForm"  theme="simple">
					<tr>
						<td width="100%" colspan="4" class="headingbg">												
							<div class="headingbg">					
								<s:text name="search.objection" />
							</div>
						</td>
					</tr>
					
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="property.type" />
						</td>
						<td class="bluebox">
							<s:select name="propertyTypeMasterId" id="propertyTypeMasterId" list="dropdownData.PropTypeMaster" listKey="id" listValue="type"
								headerKey="-1" headerValue="----Choose----" value="%{propertyTypeMasterId}" />
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr> 
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="objection.number" />
						</td>
						<td class="greybox">
							<s:textfield name="objectionNumber"  />
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="objection.fromdate" />
						</td>
						<td class="bluebox">
							<s:textfield name="objectionFromDate" id="objectionFromDate" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
							<a href="javascript:show_calendar('objectionForm.objectionFromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="objection.todate"  />
						</td>
						<td class="greybox">
							<s:textfield name="objectionToDate" id="objectionToDate" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10"/>
								<a href="javascript:show_calendar('objectionForm.objectionToDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox" colspan="4">
							&nbsp; &nbsp; &nbsp;
						</td>
					</tr>				
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align:center">
								<s:hidden id="mode" name="mode" value="objection"></s:hidden>
								<s:submit name="search" value="Search" cssClass="buttonsubmit" method="searchByObjection"></s:submit>
							</div>
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
					</s:form>
					</table>
		
			<div align="left" class="mandatory" style="font-size: 11px">
			* <s:text name="mandtryFlds"></s:text>
			</div>
					
			</center>
		</div>
	</body>
</html>
