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

<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
	<head>
		<title><s:text name="deactivate.prop"/></title>
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
		</script>
	</head>

	<body >
		<div align="left" class="mandatory">
			<s:actionerror />
		</div>
		<div class="errorstyle" id="property_error_area" style="display:none;"></div>
		<s:form method="post" theme="simple" name="viewDeactivatePropertyForm"
			validate="true">
			<s:token />
			<center>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="headingbg" width="25%">
							<div class="formmainbox"></div>
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
						<td class="headingbg" colspan="2">
							<div class="formmainbox"></div>
							<div class="formheading"></div>
							<div class="headingbg">								
								<s:text name="deactivate.prop"></s:text>
							</div>
						</td>
						<td class="headingbg" width="25%">
							<div class="formmainbox"></div>
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="prop.Id" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{basicProp.upicNo}" /> </b>
						</td>
						<s:hidden name="propertyId" value="%{basicProp.upicNo}"></s:hidden>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="HouseNo" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{basicProp.address.houseNo}"
									default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="OwnerName" />
							:
						</td>

						<td class="bluebox">
							<b><s:property value="%{ownerName}" default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						 <td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox"  style="vertical-align: top">
							<s:text name="PropertyAddress" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{address}" default="N/A" /> </b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td> 
						<td class="bluebox" >
							<s:text name="PropertyType" />
							:
						</td>

						<td class="bluebox">
							<b><s:property
									value="%{propertyType}"
									default="N/A" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td> 
						<td class="greybox" >
							<s:text name="deactRsn" />
							 :
						</td>

						<td class="greybox">
							<b><s:property value="%{propStatusVal.extraField2}" />
							</b>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td> 
						<td class="bluebox" >
							<s:text name="OrdNo" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{propStatusVal.referenceNo}" default="N/A"/></b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						 <td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox" >
							<s:text name="OrdDate" />
							:
						</td>

						<td class="greybox" style="padding-right: 0; margin-right: 0"
							width="17px">
							<s:date name="referenceDate" format="dd/MM/yyyy"
								var="refDate" />
							<b><s:property value="%{propStatusVal.referenceDate}" default="N/A"/></b>
						</td>

						<td class="greybox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td> 
						<td class="bluebox"  style="vertical-align: top">
							<s:text name="comment" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{propStatusVal.remarks}" /> </b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			 
					</tr>
					<c:if test="${docNumber!=null && docNumber!='' }">
		<tr>
	<td class="greybox"></td><td class="greybox">Documents Uploaded:</td>
       <td class="greybox">
       <span class="bold">
								<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=ptis&docNumber=${docNumber}'
									,'ViewDocuments','resizable=yes,scrollbars=yes,height=650,width=700,status=yes');">View Document</a>
        </span>
        </td>
        <td class="greybox">&nbsp;</td>
	</tr>
	 </c:if>		
					
					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" width="50%" colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>			 
					</tr>
   			        <s:if test="%{isApprPageReq}">
				 		<tr>
				        	<%@ include file="../workflow/property-workflow.jsp" %>  
				        </tr>
					</s:if>
					<s:else>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" >
					<tr>
							<td class="bluebox" width="6%">&nbsp;</td>
							<td class="bluebox" width="10%"><s:text name='approver.comments'/></td>
							<td class="bluebox" width="8%"><s:textarea name="workflowBean.comments" id="comments"rows="3" cols="80" onblur="checkLength(this);"/></td>
							<td class="bluebox" width="15%" colspan="2"></td>
					</tr>
					<s:hidden name="workflowBean.actionName" id="workflowBean.actionName"/>
					</table>
					</s:else>
					<s:hidden name="modelId" id="modelId" value="%{modelId}"/>
				</table>
				<div id="loadingMask" style="display:none"><p align="center"><img src="/ptis/resources/erp2/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
				<div class="buttonbottom" align="center">													
					<s:submit cssClass="buttonsubmit" value="Approve" id="Deactivate:Approve" theme="simple" method="approve" onclick="setWorkFlowInfo(this);doLoadingMask();"/>					
					<s:if test="%{isApprPageReq}">
						<s:submit cssClass="buttonsubmit" value="Forward" id="Deactivate:Forward" theme="simple" method="forward" onclick="setWorkFlowInfo(this);doLoadingMask();" />										
					</s:if>
					<s:submit cssClass="buttonsubmit" value="Reject" id="Deactivate:Reject" theme="simple" method="reject" onclick="setWorkFlowInfo(this);doLoadingMask();"/>	
					<input type="button" class="button" value="Close" onclick="return confirmClose();">			
				</div>
			</center>
		</s:form>
	</body>
</html>
