<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.egov.ptis.nmc.constants.NMCPTISConstants"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><s:text name='NewProp.title' />
		</title>
		<sx:head />
		<script type="text/javascript">
	function loadOnStartUp() {
   		setCorrCheckBox();
   		<s:if test="%{extra_field4 != 'Yes'}">
		    var btnPVR = document.getElementById("GeneratePrativrutta");
		    if (btnPVR != null) {
		    	btnPVR.disabled = false;
		    }
		</s:if>
	}
 function setCorrCheckBox(){
    
     <s:if test="%{isAddressCheck()}">
			document.getElementById("chkIsCorrIsDiff").checked=true;
	</s:if>
   }
  
 function generatenotice(){
   	document.CreatePropertyForm.action="../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&isPreviewPVR=false";
	document.CreatePropertyForm.submit();
 }
			  
  function generatePrativrutta(){
  		window.open("../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Prativrutta&isPreviewPVR=false","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
  		document.getElementById("GeneratePrativrutta").disabled = true;
  }

  function previewPrativrutta() {
	  window.open("../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Prativrutta&isPreviewPVR=true","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
		document.getElementById("GeneratePrativrutta").disabled = true;
	}
</script>
	</head>

	<body onload="loadOnStartUp();">
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
		<!-- Area for error display -->
		<div class="errorstyle" id="property_error_area"
			style="display: none;"></div>
		<s:form name="CreatePropertyForm" action="createProperty"
			theme="simple" validate="true">
			<s:token />
			<s:push value="model">

				<s:hidden label="noticeType" id="noticeType" name="noticeType"
					value="%{extra_field2}" />
				<div class="formmainbox">
					<div class="formheading"></div>
					<div class="headingbg">
						<s:text name="CreatePropertyHeader" />
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<%@ include file="../create/createPropertyView.jsp"%>
						</tr>
						<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
							<tr>
								<%@ include file="../workflow/property-workflow.jsp"%>
							</tr>
						</s:if>
						<s:else>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="bluebox" width="6%">
										&nbsp;
									</td>
									<td class="bluebox" width="10%">
										<s:text name='approver.comments' />
									</td>
									<td class="bluebox" width="8%">
										<s:textarea name="workflowBean.comments" id="comments"
											rows="3" cols="80" onblur="checkLength(this);" />
									</td>
									<td class="bluebox" width="15%" colspan="2"></td>
								</tr>
								<s:hidden name="workflowBean.actionName"
							id="workflowBean.actionName" />
							</table>
						</s:else>
						
						<s:hidden name="modelId" id="modelId" value="%{modelId}" />
						<tr>
							<font size="2"><div align="left" class="mandatory">
									<s:text name="mandtryFlds" />
								</div>
							</font>
						</tr>
						<div class="buttonbottom" align="center">
						<tr>
							<s:if
								test="%{model.state.value.endsWith(@org.egov.ptis.nmc.constants.NMCPTISConstants@WF_STATE_NOTICE_GENERATION_PENDING)}">
								<s:if test="%{extra_field3!='Yes'}">
									<input type="button" name="GenerateNotice" id="GenerateNotice"
										value="Generate Notice" class="button"
										onclick="return generatenotice()" />
								</s:if>

								<s:if test="%{extra_field4!='Yes'}">
									<input type="button" name="GeneratePrativrutta"
										id="GeneratePrativrutta" value="Generate Prativrutta"
										class="button" onclick="return generatePrativrutta()" />
								</s:if>

							</s:if>
							<s:else>
								<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
									<td>
										<s:submit value="Forward" name="Forward" id='Create:Forward'
											cssClass="buttonsubmit" method="forward"
											onclick="setWorkFlowInfo(this);" />
									</td>
								</s:if>
								<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">
									<td>
										<s:submit value="Approve" name="Approve" id='Create:Approve'
											cssClass="buttonsubmit" method="approve"
											onclick="setWorkFlowInfo(this);" />
									</td>
								</s:if>
								<td>
										<s:submit value="Reject" name="Reject" id='Create:Reject'
											cssClass="buttonsubmit" method="reject"
											onclick="setWorkFlowInfo(this);" />
									</td>
							</s:else>
							
								<input type="button" name="PreviewPrativrutta" id="PreviewPrativrutta" value="Preview Prativrutta"
										class="button" onclick="return previewPrativrutta()" />
										
							<td>
								<input type="button" name="button2" id="button2" value="Close"
									class="button" onclick="window.close();" />
							</td>
						</tr>
						</div>
					</table>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
