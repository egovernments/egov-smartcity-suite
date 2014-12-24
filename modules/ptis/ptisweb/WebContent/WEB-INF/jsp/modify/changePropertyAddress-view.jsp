<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp"%>


<html>
	<head>
		<title><s:text name="chPropAdd" /></title>
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
					
					<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
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
		    <div class="buttonbottom" align="center">
		    	<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
					<s:submit cssClass="buttonsubmit" value="Forward" id="ChangeAddress:Forward" theme="simple" method="forward" onclick="setWorkFlowInfo(this);" />						
				</s:if>		    	
				<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">		
					<s:submit cssClass="buttonsubmit" value="Approve" id="ChangeAddress:Approve" theme="simple" method="approve" onclick="setWorkFlowInfo(this);"/>
				</s:if>
				
				<s:submit cssClass="buttonsubmit" value="Reject" id="ChangeAddress:Reject" theme="simple" method="reject" onclick="setWorkFlowInfo(this);"/>
				
		    	<input type="button" class="button" value="Close"	onclick="return confirmClose();" />
			</div>
			</div>								
		</s:form>
	</body>
</html>

