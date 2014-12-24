<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:if test="%{mode=='view'}">
		 <s:text name="View Bid Response"/>
	</s:if>
	<s:else>
	   <s:text name="create.tenderresponse.Title"/>
	</s:else>
</title>
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}







</style>
<head>
  
  <link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery-ui-1.8.22.custom.css'/>" />
   <script type="text/javascript" src="<c:url value='/commonyui/yui2.8/yahoo/yahoo.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/jquery-1.7.2.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/jquery-ui-1.8.22.custom.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/common/js/combobox.js'/>"></script>
	<jsp:include page='/WEB-INF/jsp/tenderresponse/tenderResponse.jsp'/> 



<script>
 jQuery.noConflict();


jQuery(function()
{

jQuery(window).bind('load', function()
{

jQuery("#bidderId").combobox();
	jQuery('.ui-autocomplete-input').css('width', '90%');

});
});



function resetcombo(){

jQuery(".ui-autocomplete-input").val(jQuery("#bidderId option:selected").text());


}

function disablecombo(){

jQuery(document).ready(function() {	
	jQuery("#bidderId").closest(".ui-widget").find("input, button" ).prop("disabled", true);
	
}

);
 

 
}
</script>
</head>
<body onload="init();refreshInbox();" >
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
<s:form action="tenderResponse" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enablingFields();" name="responseForm">
<s:token/>

<s:push value="model">
<s:hidden id="tenderUnit" name="tenderUnit" value="%{tenderUnit.id}" />
<s:hidden id="state" name="state" value="%{state.id}" />
<s:hidden id="createdDate" name="createdDate" />
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="id" name="id"  value="%{id}"/>
<s:hidden id="idTemp" name="idTemp"  value="%{idTemp}"/>
<s:hidden id="workFlowType" name="workFlowType" />
<s:hidden id="autoGenerateNumberFlag" name="autoGenerateNumberFlag" value="%{isAutoGenResponse()}"/>
<s:hidden id="mode" name="mode" />
<s:hidden id="wfStatus" name="wfStatus" value="%{wfStatus}"/>
<s:hidden id="negotiationId" name="negotiationId" />
<s:hidden id="negotiationStatus" name="negotiationStatus"/>
 <div class="formheading"></div>
 <div class="formmainbox">
 		 <table width="100%" border="0" cellspacing="0" cellpadding="2">
		      	
			<tr>
				<td class="bluebox" width="18%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="date"/><span class="mandatory">*</span></td>
			    <td class="bluebox" width="26%">
			    	<s:date name="responseDate" format="dd/MM/yyyy" var="responseDateTemp"/>
						<s:textfield name="responseDate" id="responseDate" value="%{responseDateTemp}" maxlength="20" onkeyup = "DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);validateResponseWithNoticeDate(this);" />
						<s:hidden name="paramDate" id="paramDate" value="%{responseDateTemp}"/>	  
								<s:if test="%{ (idTemp==null || idTemp=='') && mode!='view' && mode!='notmodify' }">
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
					<td  class="greybox">
						<s:textfield name="noticeNumber" id="noticeNumber"  value="%{notice.number}" readonly="true" />
						<s:date name="notice.noticeDate" format="dd/MM/yyyy" var="noticeDateTemp"/>
						<s:hidden id="noticeDate" name="noticeDate" value="%{noticeDateTemp}"/>
					</td>
					<td  class="greybox"><s:text name="tenderNoticeType" /></td>
					<td  class="greybox"><s:select name="noticeType" id="noticeType" list="dropdownData.tenderFileTypeList" listKey="fileType" listValue="description" value="%{notice.tenderFileType.fileType}" headerKey="-1" headerValue="------choose------"/></td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><s:text name="referenceNumberlbl"/></td>
					<td  class="bluebox"><s:textfield name="referenceNumber" id="referenceNumber"  value="%{notice.tenderFileRefNumber}" readonly="true"/></td>
					<td  class="bluebox"><s:text name="statuslbl" /></td>
					<td  class="bluebox"><s:select id="status" name="status" list="dropdownData.statusList" listKey="id" listValue="description" value="%{status.id}" headerKey="-1" headerValue="--------choose--------" onchange="validateWithHeaderStatus();"/></td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
			<s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:property value="%{notice.tenderFileType.bidderType}" />:<span class="mandatory">*</span></td>
					<td  class="greybox"><div id="bidderdiv"><s:select name="bidderId" id="bidderId" value="%{bidderId}" list="dropdownData.bidderList" listKey="id" listValue="name" headerKey="-1" headerValue="--------Choose---------" onchange="validateBidderForResponse(this); " /></div></td>
					<td  class="greybox"><s:text name="tenderType"/><span class="mandatory">*</span></td>
					<td  class="greybox"><div id="tenderTypediv"> <s:select list="tenderBidderType.TENDERTYPE" id="tenderType" name="tenderType" value="%{tenderType}" headerKey="" headerValue="--------choose--------" onchange="showPercentage();"/></div></td>
					<td  class="greybox">&nbsp;</td>
					
					
				
    		
			</tr>
			
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><div id="tenderQuotedratelbldiv" style="display: none;"><s:text name="tenderQuotedRate" /><span class="mandatory">*</span></div></td>
					<td  class="bluebox">
						<div id="tenderQuotedrateDiv" style="display:none;"><s:select id="sign" name="sign" list="{'+','-'}" value="%{sign}" onchange="calculateBidRateWithPercentage();"/><s:textfield id="percentage" name="percentage" value="%{percentage.abs()}" maxlength="9" size="10" onblur="calculateBidRateWithPercentage();"/></div>
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
                	<td colspan="5"><div align="center"><h1 class="subhead"><s:text name="Tender Details" /></h1></div></td>
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
			 	var desc = '<s:property value="tenderableEntity.descriptionJS" escape="false"/>';
			 	responseLineTable.addRow(
	        						   {id:'<s:property value="id"/>',
	        						    SlNo:'<s:property value="#row_status.count"/>',
	        						    item:'<s:property value="tenderableEntity.number" escape="false" />'+'-'+escapeSpecialChars(desc),
	                                	quantity:quantity,
	                                	bidRate:rate,
	                                	uom:'<s:property value="uom.id"/>',
	                                	uomName:'<s:property value="uom.uom"/>',
	                                	estimatedRate:'<s:property value="estimatedRateByUom" default=""/>',
	                                	tenderableEntity:'<s:property value="tenderableEntity.id"/>',
	                                	totalEstimatedRate:'<s:property value="totalEstimatedRate" default=""/>',
	                                	totalBidRate:'<s:property value="totalBidRateByUom"/>'
	                                	});
	         </s:iterator> 
	         <s:if test="%{mode!='view'}">
	          <s:iterator id="item" value="item" status="item_status">
		       var index = '<s:property value="#item_status.index"/>';
		       updateTextBoxFormatterForName('item',index,'<s:property/>');
              </s:iterator>
               <s:iterator id="uomName" value="uomName" status="item_status">
		       var index = '<s:property value="#item_status.index"/>';
		       updateTextBoxFormatterForName('uomName',index,'<s:property/>');
              </s:iterator>
              <s:iterator id="estimatedRate" value="estimatedRate" status="item_status">
		       var index = '<s:property value="#item_status.index"/>';
		       updateTextBoxFormatterForName('estimatedRate',index,'<s:property />');
              </s:iterator>
              
              <s:iterator id="totalBidRate" value="totalBidRate" status="item_status">
		       var index = '<s:property value="#item_status.index"/>';
		       updateTextBoxFormatterForName('totalBidRate',index,'<s:property />');
              </s:iterator>
              
              <s:iterator id="totalEstimatedRate" value="totalEstimatedRate" status="item_status">
		       var index = '<s:property value="#item_status.index"/>';
		       updateTextBoxFormatterForName('totalEstimatedRate',index,'<s:property />');
              </s:iterator>
              </s:if>
			</script>
			
    <jsp:include page='/WEB-INF/jsp/tenderresponse/negotiationDetails.jsp'/>  
 	<s:if test="%{mode!='view'}" >
	 		  
		<div id="approvalInfo">
			<s:if test="%{wfStatus!='APPROVED'}">	
				<table width="100%" border="0" cellspacing="0" cellpadding="0" >
							<jsp:include page='/WEB-INF/jsp/common/approverInfo.jsp'/>
				 </table>
			</s:if>						 
			<s:if test="%{status.code =='Justification' && justifiedDate!=null}">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" >
						<tr>
						    <td class="bluebox" width="18%">&nbsp;</td>	  
							<td class="bluebox" width="14%"><s:text name="justification.remarks" /> </td>
							<td class="bluebox" colspan="3"  width="33%"> <s:textarea rows="2" cols="50"  id="justifiedRemarks" name="justifiedRemarks" value="%{justifiedRemarks}" readonly="true"/> </td>
							<td class="bluebox" width="10%"><s:text name="justifiction.date"/></td>	
							<td class="bluebox" width="33%">
							    <s:date name="justifiedDate" var="justifictiondateTemp" format="dd/MM/yyyy"/>
								<s:textfield name="justifiedDate" id="justifiedDate" value="%{justifictiondateTemp}" readonly="true"/>
						    </td>	  
						</tr>	
					</table>
			</s:if>	
		</div>
	</s:if> 
 	
	<div class="buttonbottom" align="center">
		<table>
			<tr>
			 	<td ><s:if test="%{mode!='view'}">
			 			<s:if test="%{mode!='modify' && mode!='notmodify'}">
			 				<input type="button" id="reset" name="reset" value="Reset" onclick="resetForm();" class="buttonsubmit"  />
			 			</s:if>
						<s:iterator value="%{getValidActions(wfStatus,'GenericTenderResponse','response.validbuttons')}" var="p">
		  					<s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="%{name}" method="create" onclick="return validateForm('%{name}')"/>
						</s:iterator>
			 		</s:if>
			 	</td>
	  			 <td>
	  			  <s:if test="%{status.code=='Negotiated' && mode=='view'}">
			 		      <s:submit type="submit" cssClass="noticebutton" value="Generate Negotiation Notice" id="generateNotice" name="generateNotice" onclick="return generateNegotiationReport();"/>
			 	  </s:if>
			 	  <s:if test="%{status.code=='Justification' && mode=='view'}">
			 		      <s:submit type="submit" cssClass="noticebutton" value="Generate Justification Notice" id="generatejusNotice" name="generatejusNotice" onclick="return generateJustificationReport();"/>
			 	  </s:if>
	  			 <input type="button" name="close" id="close" class="button" value="Close"  onclick="window.close();">
			 	 </td>
			</tr>		 
		</table>
	</div>   
		
	</s:push>	
	</s:form>
	</body>
	</html>