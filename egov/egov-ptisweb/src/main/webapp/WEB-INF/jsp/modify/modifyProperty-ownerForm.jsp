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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><s:text name='edit.ownername'/></title>
<script type="text/javascript">
	function ownernamemandatory() {
		var all = document.getElementsByTagName("input");
		for (var i=0, max=all.length; i < max; i++) {
			if (all[i].name.substr(18,9)=="firstName") {
				if (all[i].value==null || all[i].value=="") {
					bootbox.alert("Please enter valid owner name");
					all[i].focus();
					return false;
				}
			}		
		}
		return true;
	}
</script>
</head>
  
<body>
	<div align="left">
		<s:actionerror />
	</div>
	<s:if test="%{hasActionMessages()}">
		<div id="actionMessages" class="messagestyle" align="center">
			<s:actionmessage theme="simple" />
		</div>
		<div class="blankspace">
			&nbsp;
		</div>
	</s:if>
	<s:form name="ModifyPropertyForm" action="modifyProperty" validate="true" theme="simple">
	<s:push value="model"> 
	<s:token/>
	<div class="formmainbox">
	<div class="formheading"></div>
	<div class="headingbg"><s:text name="edit.propertyData"/></div>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="bluebox" width="15%">
				&nbsp;
			</td>
			<td class="bluebox" width="20%">
				<s:text name="prop.Id" />
				:
			</td>
			<td class="bluebox" width="15%">
				<span class="bold"><s:property default="N/A"
						value="%{basicProp.upicNo}" /> </span>						
			</td>
			<td class="bluebox" width="20%">
				&nbsp;
			</td>
			<td class="bluebox" width="20%">
				&nbsp;
			</td>
		</tr>
	    <s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}"/>
		<tr>
			<td class="greybox" width="15%">
				&nbsp;
			</td>
			<td class="greybox" width="20%">
				<s:text name="Zone" />
				:
			</td>
			<td class="greybox" width="15%">
				<span class="bold"><s:property value="%{basicProp.boundary.parent.boundaryNum}"/>-<s:property default="N/A"
						value="%{basicProp.boundary.parent.name}" /> </span>
			</td>
			<td class="greybox" colspan="2">&nbsp;</td>
		
		</tr>
		<tr>
			<td class="bluebox" width="15%">&nbsp;</td>
			<td class="bluebox" width="20%">
				<s:text name="Ward" />
				:
			</td>
			<td class="bluebox" width="20%">
				<span class="bold"><s:property value="%{basicProp.boundary.boundaryNum}"/>-<s:property default="N/A"
						value="%{basicProp.boundary.name}" /> </span>
			</td>
			<td class="bluebox" colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="greybox" width="15%">
				&nbsp;
			</td>
			<td class="greybox" width="8%">
				<s:text name="PropertyAddress" />
				:
			</td>
			<td class="greybox">
				<span class="bold"><s:property default="N/A"
						value="%{propAddress}" /> </span>
			</td>
			<td class="greybox">
				&nbsp;
			</td>
			<td class="greybox">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="15%">
				&nbsp;
			</td>
			<td class="bluebox">
				<s:text name="OwnerName" />
				:
			</td>
			<td class="bluebox">
				<span class="bold"><s:property default="N/A"
						value="%{ownerName}" /> </span>
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
			<td class="bluebox">
				&nbsp;
			</td>
		</tr>		
		<tr>
			<td class="greybox" width="5%">&nbsp;&nbsp;&nbsp;</td>
			<td class="greybox" width=""><s:text name="partNo" />:</td>
			<td class="greybox" width="">
				<s:property value="%{basicProp.partNo}" default="N/A"/>
			</td>
			<td class="greybox" colspan="2">&nbsp;</td>
		</tr>

		<s:if test="%{basicProp.isMigrated != null && basicProp.isMigrated == 'Y'}">
			<tr>
			<td class="greybox" width="15%">&nbsp;</td>
			<td class="greybox"><s:text name="OwnerName" /><span class="mandatory1">*</span> : </td>
			<td class="greybox">
				<table width="" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable"">
				<s:iterator value="(propertyOwners.size).{#this}" status="ownerStatus">
					<tr id="nameRow">
						<td class="greybox" align="center">
							<s:textfield name="propertyOwners[%{#ownerStatus.index}].firstName" maxlength="512" size="20" id="ownerName" value="%{propertyOwners[#ownerStatus.index].firstName}" 
								onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
						</td>
						<td class="greybox">&nbsp;</td>
					</tr>
				</s:iterator>
				</table>
			</td>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
			</tr>
		</s:if>
		<tr>
			<td class="bluebox2" width="5%">&nbsp;&nbsp;&nbsp;</td>
			<td class="bluebox" width=""><s:text name="partNo" />  :</td>
			<td class="bluebox" width="">
				<s:textfield id="partNo" name="partNo" value="%{basicProp.partNo}" maxlength="12"/>
			</td>
			<td class="bluebox" colspan="2">&nbsp;</td>
		</tr>
	</table>
        <div class="buttonbottom" align="center">
			<s:submit value="Update Data" name="UpdateOwner" id="Modify:updateOwner"  method="updateOwner" cssClass="buttonsubmit" onclick="return ownernamemandatory();"/>
		    <input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
		 </div>
	</div>
	</s:push>
  </s:form>
  </body>
</html>
