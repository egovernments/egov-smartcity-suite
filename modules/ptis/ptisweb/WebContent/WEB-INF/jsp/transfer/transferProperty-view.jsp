<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.egov.ptis.nmc.constants.NMCPTISConstants" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name='transferProperty' /></title>
		<script type="text/javascript">

		function generateMutationCertificate() {
			window.open("../notice/propertyTaxNotice!generateNotice.action?indexNumber=<s:property value='%{indexNumber}'/>&noticeType=MutationCertificate",
					"","resizable=yes, scrollbars=yes, top=40, width=900, height=650");
		}

		</script>
	</head>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
				</div>
			</s:if>
			<!-- Area for error display -->
			<div class="errorstyle" id="property_error_area" style="display:none;"></div>
			<s:form action="transferProperty" name="transferform" theme="simple">
			 	<s:push value="model">
			 	<s:token/>
<!--			<s:hidden name="modelId" id="modelId" value="%{modelId}"></s:hidden>-->
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<div class="formheading"></div>
					<div class="headingbg">
						<s:text name="transferProperty" />
					</div>
					<tr>
						<td class="bluebox">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="prop.Id"></s:text>
						</td>
						<td class="bluebox">
							<span class="bold"><s:property  default="N/A" value="indexNumber" /></span>
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="assesseeName"></s:text>
						</td>
						<td class="greybox">
							<span class="bold"><s:property value="oldOwnerName" /></span>
						</td>
						<td class="greybox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="PropertyAddress"></s:text>
						</td>
						<td class="bluebox">
							<span class="bold"><s:property value="propAddress" /></span>
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<s:text name="transferDtls" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="transferreason"></s:text> :
						</td>
						<s:if test="%{propertyMutation.propMutationMstr.mutationName.equalsIgnoreCase('OTHERS')}">
							<td class="bluebox">
								<span class="bold"> <s:property default="N/A"
										value="%{propertyMutation.extraField4}" />
								</span>
							</td>
						</s:if>	
						<s:else>
							<td class="bluebox">
								<span class="bold"> <s:property  default="N/A" value="%{propertyMutation.propMutationMstr.mutationName}"/></span>
							</td>
						</s:else>	
						<td class="bluebox">
							<s:text name="applDate"></s:text> :
						</td>
						<td class="bluebox">
							<span class="bold"><s:date name="%{propertyMutation.noticeDate}" format="dd/MM/yyyy"/></span>
						</td>
					</tr>
					
					<s:if test="%{propertyMutation.propMutationMstr.mutationName.equalsIgnoreCase('SALE DEED')}">			
						<tr>
							<td class="greybox">
								&nbsp;
							</td>
							<td class="greybox">
								<s:text name="saleDetls" /> :
							</td>
							<td class="greybox">
								<span class="bold">	<s:property  default="N/A" value="%{propertyMutation.extraField3}"/></span>
							</td>
							<td class="greybox" colspan="2">
								&nbsp;
							</td>
						</tr>
					</s:if>
					<tr>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="mutationDate" />
							:
						</td>
						<td class="greybox">
							<span class="bold"> <s:date name="%{propertyMutation.mutationDate}" format="dd/MM/yyyy"/>
							</span>
						</td>
						<td class="greybox" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="crtOrderNum" /> :
						</td>
						<td class="bluebox">
							<span class="bold">	<s:property  default="N/A" value="%{propertyMutation.mutationNo}"/></span>
						</td>
						<td class="bluebox">
							<s:text name="subregoffName" /> :
						</td>
						<td class="bluebox">
							<span class="bold"><s:property default="N/A"  value="%{propertyMutation.extraField2}"/></span>
						</td>
					</tr>
					<tr>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="docNum" /> :
						</td>
						<td class="greybox">
							<span class="bold"><s:property default="N/A"  value="%{propertyMutation.deedNo}"/></span>
						</td>
						<td class="greybox">
							<s:text name="docDate" /> :
						</td>
						<td class="greybox">
							<span class="bold"><s:date name="%{propertyMutation.deedDate}" format="dd/MM/yyyy"/></span>
						</td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">
							<s:text name="mutationFee"/> :
						</td>
						<td class="bluebox">
							<span class="bold">	<s:property  default="N/A" value="%{propertyMutation.mutationFee}"/></span>
						</td>
						<td class="bluebox">
							<s:text name="receiptNum"/> :
						</td>
						<td class="bluebox">
							<span class="bold">	<s:property  default="N/A" value="%{propertyMutation.receiptNum}"/></span>
						</td>
					</tr>
					<tr>
						<td class="greybox">
							&nbsp;
						</td>
						<td class="greybox">
							<s:text name="MobileNumber" /> :
						</td>
						<td class="greybox">
							<div>
								<span class="bold"><s:property default="N/A"  value="%{mobileNo}"/></span>
							</div>
						</td>
						<td class="greybox">
							<s:text name="EmailAddress" /> :
						</td>
						<td class="greybox">
							<span class="bold"><s:property default="N/A"  value="%{email}"/></span>
						</td>
					</tr>
					<tr>
						<td class="bluebox">
							&nbsp;
						</td>
						<td class="bluebox">
							<s:text name="Document Details" /> :
						</td>
						<td class="bluebox">
							<c:if test="${docNumber!=null && docNumber!='' }">
							<span class="bold">
								<a href='#' target="_parent" 
									onclick="window.open('/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=ptis&docNumber=${docNumber}'
									,'ViewDocuments','resizable=yes,scrollbars=yes,height=650,width=700,status=yes');">View Document
								</a>
							</span>
							</c:if>
						</td>
						<td class="bluebox" colspan="2">
							&nbsp;
						</td>
					</tr>
					
					<tr>
						<td>
							<div id="CorrAddrDiv">
								<%@ include file="../common/CorrAddressView.jsp"%>
							</div>
						</td>
					</tr>
				</table>
			
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
							<td class="bluebox" width="8%">
								<s:textarea name="workflowBean.comments" id="comments"rows="3" cols="80" onblur="checkLength(this);"/>
							</td>
							<td class="bluebox" width="15%" colspan="2"></td>
						</tr>
						<s:hidden name="workflowBean.actionName" id="workflowBean.actionName"/>
					</table>
				</s:else>
       			<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<tr>
					<div class="buttonbottom">
						<td>
							<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}"></s:hidden>
							<s:hidden label="noticeType" id="noticeType" name="noticeType" value="%{noticeType}" />
							<s:hidden id="oldOwnerName" name="oldOwnerName"	value="%{oldOwnerName}"></s:hidden>
							<s:hidden id="propAddress" name="propAddress" value="%{propAddress}"></s:hidden>
							<tr>
								<s:if test="%{statvalue.endsWith(@org.egov.ptis.nmc.constants.NMCPTISConstants@WF_STATE_NOTICE_GENERATION_PENDING)}">
									<s:if test="%{property.extra_field3!='Yes'}">
										<td><input type="button" name="MutationCertificate" id="MutationCertificate" value="Mutation Certificate"
										class="button" onclick="return generateMutationCertificate()" /></td>
									</s:if>
								</s:if>
								<s:else>
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
										<td>
											<s:submit value="Forward" id="Mutation:Forward" name="Forward" cssClass="buttonsubmit" 
												align="center" method="forward" onclick="setWorkFlowInfo(this);"/>
										</td>
									</s:if>
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">		
										<td>
											<s:submit value="Approve" id="Mutation:Approve" name="Approve" cssClass="buttonsubmit" 
													align="center" method="approve" onclick="setWorkFlowInfo(this);"/>
										</td>
									</s:if>
											<s:submit value="Reject" id="Mutation:Reject" name="Reject" cssClass="buttonsubmit" 
													align="center" method="reject" onclick="setWorkFlowInfo(this);"/>
								</s:else>
								<td>
									<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
								</td>
							</tr>  
						</td>
					</div>
				</tr>
				</s:push>
			</s:form>
			<div align="left" class="mandatory" style="font-size: 11px">
				* <s:text name="mandtryFlds"></s:text>
			</div>
		</div>
	</body>
</html>
