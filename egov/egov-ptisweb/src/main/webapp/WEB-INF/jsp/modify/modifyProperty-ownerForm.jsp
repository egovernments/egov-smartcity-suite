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
					alert("Please enter valid owner name");
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
