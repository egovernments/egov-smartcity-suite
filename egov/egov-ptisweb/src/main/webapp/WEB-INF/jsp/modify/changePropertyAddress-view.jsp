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
<%@include file="/includes/taglibs.jsp"%>


<html>
	<head>
		<title><s:text name="chPropAdd" /></title>
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
		</script>
	</head>
	<body>
		
		<s:form method="post" action="../modify/changePropertyAddress.action"
			theme="simple" name="modifyPropertyForm">
			<s:token />
			<div class="formmainbox">
			<div align="left">
				<span class="mandatory"><s:actionerror /> </span>
			</div>
			
			<center>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="headingbg" width="25%">							
							<div class="formheading"></div>
							<div class="headingbg">&nbsp;</div>
						</td>
						<td class="headingbg"  colspan="2">							
							<div class="formheading"></div>
							<div class="headingbg">								
							<s:text name="chPropAdd"></s:text>
							</div>				
						</td>
						<td class="headingbg" width="25%">							
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
							<b><s:property value="%{basicProperty.upicNo}" /> </b>
							<s:hidden name="indexNumber" value="%{basicProperty.upicNo}"></s:hidden>
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
							<s:text name="HouseNo" />
							:
						</td>

						<td class="greybox">
							<b><s:property value="%{basicProperty.address.houseNo}"
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
							<s:text name="Zone" />
							:
						</td>

						<td class="bluebox">
							<b><s:property
									value="%{basicProperty.propertyID.zone.name}" default="N/A" />
							</b>
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
							<s:text name="Ward" />
							:
						</td>

						<td class="greybox">
							<b><s:property
									value="%{basicProperty.propertyID.ward.name}" default="N/A" />
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
							<s:text name="Area" />
							:
						</td>

						<td class="bluebox">
							<b><s:property
									value="%{basicProperty.propertyID.area.name}" default="N/A" />
							</b>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>		
					</tr>
					<tr>
						<td class="greybox" width="25%">
							&nbsp;
						</td>
						<td class="greybox"  style="text-align: left" 
							colspan="2">
							<b><s:text name="newAddress" /></b>
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
							<s:text name="Address" />
						</td>

						<td class="bluebox">
							<b><s:property value="%{streetAddress1}" default="N/A"/></b>
							<s:hidden name="streetAddress1"></s:hidden>
						</td>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>		
					</tr>

					<tr>
						<td class="bluebox" width="25%">
							&nbsp;
						</td>
						<td class="bluebox" >
							<s:text name="HouseNo" />											
						</td>
						<td class="bluebox">
							<b><s:property value="%{houseNo}" default="N/A" /></b>
							<s:hidden name="houseNo"></s:hidden>
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
							<s:text name="OldNo" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{doorNumOld}"  default="N/A"/></b>
							<s:hidden name="doorNumOld"></s:hidden>
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
							<s:text name="address.khasraNumber" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{extraField1}"  default="N/A"/></b>
							<s:hidden name="extraField1"></s:hidden>
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
							<s:text name="address.Mauza" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{extraField2}"  default="N/A"/></b>
							<s:hidden name="extraField2"></s:hidden>
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
							<s:text name="address.citySurveyNumber" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{extraField3}"  default="N/A"/></b>
							<s:hidden name="extraField3"></s:hidden>
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
							<s:text name="address.sheetNumber" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{extraField4}"  default="N/A"/></b>
							<s:hidden name="extraField4"></s:hidden>
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
							<s:text name="PinCode" />
							:
						</td>
						<td class="bluebox">
							<b><s:property value="%{pinCode}" default="N/A" /></b>
							<s:hidden name="pinCode"></s:hidden>
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
							<s:text name="MobileNumber" />
							:
						</td>
						<td class="greybox">
							<b><s:property value="%{mobileNo}" default="N/A"/></b>
							<s:hidden name="mobileNo"></s:hidden>
						</td>
						<td class="greybox" width="25%">
							&nbsp;
						</td>			
					</tr>
					<c:if test="${docNumber!=null && docNumber!='' }">
		<tr>
	<td class="bluebox"></td><td class="greybox">Documents Uploaded:</td>
       <td class="bluebox">
       <span class="bold">
								<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=ptis&docNumber=${docNumber}'
									,'ViewDocuments','resizable=yes,scrollbars=yes,height=650,width=700,status=yes');">View Document</a>
        </span>
        </td>
         <td class="bluebox" colspan="2">
							&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
	</tr>
	 </c:if>					
					
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
					<s:hidden name="modelId" id="modelId" value="%{modelId}" />
					</table>
			</center>
			<div id="loadingMask" style="display:none"><p align="center"><img src="/ptis/resources/erp2/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
		    <div class="buttonbottom" align="center">		    	
				<s:submit cssClass="buttonsubmit" value="Approve" id="ChangeAddress:Approve" theme="simple" method="approve" onclick="setWorkFlowInfo(this);doLoadingMask();"/>								
				<s:if test="%{isApprPageReq}">
					<s:submit cssClass="buttonsubmit" value="Forward" id="ChangeAddress:Forward" theme="simple" method="forward" onclick="setWorkFlowInfo(this);doLoadingMask();" />						
				</s:if>		    									
				<s:submit cssClass="buttonsubmit" value="Reject" id="ChangeAddress:Reject" theme="simple" method="reject" onclick="setWorkFlowInfo(this);doLoadingMask();"/>				
				<input type="button" class="button" value="Close"	onclick="return confirmClose();" />		    	
			</div>
			</div>								
		</s:form>
	</body>
</html>

