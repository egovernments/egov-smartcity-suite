<%@ page language="java" pageEncoding="UTF-8" import="org.egov.ptis.nmc.constants.*"%>
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
				<div id="loadingMask" style="display:none"><p align="center"><img src="/egi/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
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
