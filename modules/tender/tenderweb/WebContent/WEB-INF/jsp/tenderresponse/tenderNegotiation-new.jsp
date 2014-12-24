<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title><s:text name="negotiationdetail.lbl"/></title>
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>
<head>
	<jsp:include page='/WEB-INF/jsp/tenderresponse/tenderResponse.jsp'/> 
</head>
<script>
	
</script>
<body onload="initNegotiation();refreshInbox();" >
	<div class="errorstyle" id="tenderResponse_error" style="display: none;"></div>
		
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="fielderror">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>
<s:form action="tenderNegotiation" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enablingFields();">
<s:token name="%{tokenName()}"/>

<s:push value="model">

<s:hidden id="tenderUnit" name="tenderUnit" value="%{tenderUnit.id}" />
<s:hidden id="workFlowType" name="workFlowType" />
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:if test="%{mode !='new'}">
	<s:hidden id="id" name="id" value="%{id}" />
	<s:hidden id="parent" name="parent" value="%{parent.id}"/>
	<s:hidden id="createdDate" name="createdDate" />
	<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
	<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
</s:if>
<s:else>
	<s:hidden id="parent" name="parent" value="%{id}"/>
</s:else>


 <div class="formheading"></div>
 <div class="formmainbox">
 		 <table width="100%" border="0" cellspacing="0" cellpadding="2">
		      	
			<tr>
				<td class="bluebox" width="18%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="date"/><span class="mandatory">*</span></td>
			    <td class="bluebox" width="26%">
			    	<s:date name="responseDate" format="dd/MM/yyyy" var="responseDateTemp"/>
					<s:textfield name="responseDate" id="responseDate" value="%{responseDateTemp}" maxlength="20" onkeyup = "DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" />
					<s:if test="%{mode!='view' && mode!='modify'}">
							<a href="javascript:show_calendar('forms[0].responseDate');" onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true; ">
										<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0" align="absmiddle"/>
							</a>
					</s:if>	
			    </td>
				<td class="bluebox" width="15%">
					<s:text name="bidResponseNumber" /><s:if test="%{!isAutoGenResponse()}"><span class="mandatory">*</span></s:if> </td>
				<td class="bluebox" width="26%">
					<s:textfield id="number" name="number" value="%{number}" readonly="true"/>
				</td>
				<td class="bluebox" width="18%">&nbsp;</td>
			</tr>
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:text name="tenderNoticeNo"/></td>
					<td  class="greybox"><s:textfield name="noticeNumber" id="noticeNumber"  value="%{notice.number}" readonly="true" /></td>
					<td  class="greybox"><s:text name="tenderNoticeType" /></td>
					<td  class="greybox"><s:select name="noticeType" id="noticeType" list="dropdownData.tenderFileTypeList" listKey="fileType" listValue="description" value="%{notice.tenderFileType.fileType}" headerKey="-1" headerValue="------choose------"/></td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><s:text name="referenceNumberlbl"/></td>
					<td  class="bluebox"><s:textfield name="referenceNumber" id="referenceNumber"  value="%{notice.tenderFileRefNumber}" readonly="true"/></td>
					<td  class="bluebox"><s:text name="statuslbl" /></td>
					<td  class="bluebox"><s:select id="status" name="status" list="dropdownData.statusList" listKey="id" listValue="description" value="%{tempStatus.id}" headerKey="-1" headerValue="--------choose--------"/></td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
			<s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:property value="%{notice.tenderFileType.bidderType}" />:<span class="mandatory">*</span></td>
					<td  class="greybox"><div id="bidderdiv"><s:select name="bidderId" id="bidderId" value="%{bidderId}" list="dropdownData.bidderList" listKey="id" listValue="name" headerKey="-1" headerValue="--------Choose--------"/></div></td>
					<td  class="greybox"><s:text name="tenderType"/><span class="mandatory">*</span></td>
					<td  class="greybox"><div id="tenderTypediv"> <s:select list="tenderBidderType.TENDERTYPE" id="tenderType" name="tenderType" value="%{tenderType}" headerKey="" headerValue="--------Choose--------" onchange="showPercentage();"/></div></td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><div id="tenderQuotedratelbldiv" style="display: none;"><s:text name="tenderQuotedRate" /><span class="mandatory">*</span></div></td>
					<td  class="bluebox">
						<div id="tenderQuotedrateDiv" style="display:none;"><s:select id="sign" name="sign" list="{'+','-'}" value="%{sign}" onchange="calculateBidRateWithPercentage();" /><s:textfield id="percentage" name="percentage" value="%{percentage.abs()}" maxlength="5" size="10" onblur="calculateBidRateWithPercentage();"/></div>
					</td>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
			
			</s:if>
			<s:else>
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:text name="nameOfBidder" /><span class="mandatory">*</span></td>
					<td  class="greybox">
						<s:textfield id="owner.firstName" name="owner.firstName" value="%{owner.firstName}" maxlength="30" onblur="checkSpecialCharactersInName(this,'tenderResponse_error','Name Of Bidder');" />
						<s:hidden id="owner.citizenID" name="owner.citizenID" value="%{owner.citizenID}" />
					</td>
					<td  class="greybox"><s:text name="addressOfBidder"/><span class="mandatory">*</span></td>
					<td  class="greybox">
						<s:textarea id="bidderAddress.streetAddress1" name="bidderAddress.streetAddress1" value="%{bidderAddress.streetAddress1}" rows="4" cols="20"/>
						<s:hidden id="bidderAddress.addressID" name="bidderAddress.addressID" value="%{bidderAddress.addressID}" />
					</td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><s:text name="contactNumber" /><span class="mandatory">*</span></td>
					<td  class="bluebox"><s:textfield id="owner.officePhone" name="owner.officePhone" value="%{owner.officePhone}" onblur="checkPhoneNumberContent(this,'tenderResponse_error');" maxlength="10"/></td>
					<td  class="bluebox"><s:text name="ratePerSqft" /><span class="mandatory">*</span></td>
					<td  class="bluebox"><s:textfield id="ratePerUnit" name="ratePerUnit" value="%{ratePerUnit}" onblur="checkUptoTwoDecimalPlace(this,'tenderResponse_error','Rate Per sq.ft');" maxlength="13"/></td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
		 </s:else>
		</table>
				
			
		</div>
		
		<div class="blueshadow"></div>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" >   
         		 <tr>
                	<td colspan="5"><div align="center"><h1 class="subhead"><s:text name="tenderDetails.lbl" /></h1></div></td>
              	</tr>       
            <tr>
      			<td width="100%" >
					<div class="yui-skin-sam" align="center" >
						<div id="responseLineTable"></div>
					</div>
				</td>
			</tr>
			</table>
			<script>
			 makeResponseLineTable();
			 <s:iterator id="responseLineList" value="responseLineList" status="row_status" >
			var rate,quantity,estimatedRate;
			 <s:if test="%{hasErrors()}">
			 	rate='<s:property value="bidRate"/>';
			 	quantity= '<s:property value="quantity"/>';
			 </s:if>
			 <s:else>
			 	rate='<s:property value="bidRateByUom"/>';
			 	quantity= '<s:property value="quantityByUom"/>';
			 </s:else>
			  responseLineTable.addRow(
	        						   {SlNo:'<s:property value="#row_status.count"/>',
	        						    item:'<s:property value="tenderableEntity.number"/>'+'-'+'<s:property value="tenderableEntity.descriptionJS"/>',
	                                	quantity:quantity,
	                                	bidRate:rate,
	                                	uomName:'<s:property value="uom.uom"/>',
	                                	uom:'<s:property value="uom.id"/>',
	                                	estimatedRate:'<s:property value="estimatedRateByUom" default=""/>',
	                                	tenderableEntity:'<s:property value="tenderableEntity.id"/>',
	                                	totalEstimatedRate:'<s:property value="totalEstimatedRate" default=""/>',
	                                	totalBidRate:'<s:property value="totalBidRateByUom"/>'});
	         </s:iterator> 
			</script>
      
 	
 	
	<div class="buttonbottom" align="center">
		<table>
			<tr>
			 	<td ><s:if test="%{mode!='view'}">
			 			<s:submit type="submit" cssClass="buttonsubmit" value="Save" id="save" name="save" method="create" onclick="return validateNegotiationForm('')"/>
			 		</s:if>
			 	</td>
	  			 <td>
	  			 	<input type="button" name="close" id="close" class="button" value="Close"  onclick="window.close();">
	  			 </td>
			</tr>		 
		</table>
	</div>   
		
	</s:push>	
	</s:form>
	</body>
	</html>