<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title><s:text name="tender.justification"/></title>

<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 

.yui-dt0-col-bidRate{
text-align:right;
}

.yui-skin-sam .yui-dt td.align-right  { 
		text-align:right;
	}
</style>
<head>
	
</head>
<script>
    var PERCENTAGE="Percentage";
	var responseLineTable;
	var makeResponseLineTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var responseColumnDefs = [ 
	    {key:"id", hidden:true,sortable:false, resizeable:false},
	    {key:"SlNo", label:'Srl No', sortable:false, resizeable:false},
		{key:"item", label:'<div align="center"><s:text name="entity"/></div>', sortable:false, resizeable:false,minWidth:200},
		{key:"uomName",  label:'<div align="center"><s:text name="uom"/></div>',sortable:false, resizeable:false,minWidth:150},
		{key:"quantity", label:'<div align="center"><s:text name="quantity"/></div>', sortable:false, resizeable:false,minWidth:150,className:'align-right'},
		{key:"estimatedRate", label:'<div align="center"><s:text name="estimatedRatePerUnit"/></div>',sortable:false, resizeable:false,minWidth:150,className:'align-right'},
		{key:"totalEstimatedRate", label:'<div align="center"><s:text name="totalEstimatedRate"/></div>',sortable:false, resizeable:false,minWidth:150,className:'align-right'},
		{key:"bidRate", label:'<div align="center"><s:text name="quotedRatePerUnit"/></div>', sortable:false, resizeable:false,minWidth:150,className:'align-right'},
		{key:"totalBidRate", label:'<div align="center"><s:text name="totalQuotedRate"/></div>', sortable:false, resizeable:false,minWidth:150,className:'align-right'}
		
	];
	
	var responseDataSource = new YAHOO.util.DataSource(); 
	responseLineTable = new YAHOO.widget.DataTable("responseLineTable",responseColumnDefs, responseDataSource, {MSG_EMPTY:"<s:text name='nothing to display'/>"});
	responseLineTable.subscribe("cellClickEvent", responseLineTable.onEventShowCellEditor); 
	responseLineTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		
	});
	
	return {
	    oDS: responseDataSource,
	    oDT: responseLineTable
	};  
}

function init()
{
     if(document.getElementById('mode').value == "view")
  	 {
  		      for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].name != 'close' )
					document.forms[0].elements[i].disabled = true;
			}
  	  }
  	  else 
  	  {
  		      for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].name !='justifiedDate' &&
				       document.forms[0].elements[i].name!='remarks' && document.forms[0].elements[i].id != 'save')
					document.forms[0].elements[i].disabled = true;
			}
  	 }
  	 <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
		      if(document.getElementById('tenderType').value == PERCENTAGE){
		         	dom.get('tenderQuotedratelbldiv').style.display='';
		         	dom.get('tenderQuotedrateDiv').style.display='';
}	
	  </s:if>
}	

function validateForm()
{
    if(!checkStringMandatoryField('remarks','<s:text name="remarks" />','tenderResponse_error'))
	    		return false;
	if(!checkStringMandatoryField('justifiedDate','Justification Date','tenderResponse_error'))
	 			return false;
	return true;
}

function enablingFields()
{
    for(var i=0;i<document.forms[0].length;i++)
	{
			document.forms[0].elements[i].disabled =false;
	}
}
</script>

<body onload="init()" >
<div class="errorstyle" id="tenderResponse_error" style="display:none;"></div>
		
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
		
<s:form action="tenderJustification" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enablingFields();">
<s:token name="%{tokenName()}"/>
<s:push value="model">

<s:hidden id="tenderResponse" name="tenderResponse" value="%{tenderResponse.id}" />
<s:hidden id="mode" name="mode" value="%{mode}"/>
 <div class="formheading"></div>
 <div class="formmainbox">
                  
 		 <table width="100%" border="0" cellspacing="0" cellpadding="2">
		      	
			<tr>
				<td class="bluebox" width="18%">&nbsp;</td>
				<td class="bluebox" width="10%"><s:text name="date.bid"/></td>
			    <td class="bluebox" width="26%">
			    	<s:date name="tenderResponse.responseDate" format="dd/MM/yyyy" var="responseDateTemp"/>
					<s:textfield name="responseDate" id="responseDate" value="%{responseDateTemp}" maxlength="20" onkeyup = "DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" />
				</td>
				<td class="bluebox" width="15%">
					<s:text name="bidResponseNumber" /></td>
				<td class="bluebox" width="26%">
					<s:textfield id="number" name="number" value="%{tenderResponse.number}" readonly="true"/>
				</td>
				<td class="bluebox" width="18%">&nbsp;</td>
			</tr>
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:text name="tenderNoticeNo"/></td>
					<td  class="greybox"><s:textfield name="noticeNumber" id="noticeNumber"  value="%{tenderResponse.notice.number}" readonly="true" /></td>
					<td  class="greybox"><s:text name="tenderNoticeType" /></td>
					<td  class="greybox"><s:select name="noticeType" id="noticeType" list="dropdownData.tenderFileTypeList" listKey="fileType" listValue="description" value="%{tenderResponse.notice.tenderFileType.fileType}" headerKey="-1" headerValue="------choose------"/></td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><s:text name="referenceNumberlbl"/></td>
					<td  class="bluebox"><s:textfield name="referenceNumber" id="referenceNumber"  value="%{tenderResponse.notice.tenderFileRefNumber}" readonly="true"/></td>
					<td  class="bluebox"><s:text name="statuslbl" /></td>
					<td  class="bluebox"><s:select id="status" name="status" list="dropdownData.statusList" listKey="id" listValue="description" value="%{tenderResponse.status.id}" headerKey="-1" headerValue="--------choose--------"/></td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
			<s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:property value="%{tenderResponse.notice.tenderFileType.bidderType}" />:</td>
					<td  class="greybox"><div id="bidderdiv"><s:select name="bidderId" id="bidderId" value="%{tenderResponse.bidderId}" list="dropdownData.bidderList" listKey="id" listValue="name" headerKey="-1" headerValue="--------Choose--------"/></div></td>
					<td  class="greybox"><s:text name="tenderTypelbl"/></td>
					<td  class="greybox"><div id="tenderTypediv"> <s:select list="tenderBidderType.TENDERTYPE" id="tenderType" name="tenderType" value="%{tenderType}" headerKey="" headerValue="--------Choose--------" onchange="showPercentage();"/></div></td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			
			<tr>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox"><div id="tenderQuotedratelbldiv" style="display: none;"><s:text name="tenderQuotedRate" /><span class="mandatory">*</span></div></td>
					<td  class="bluebox">
						<div id="tenderQuotedrateDiv" style="display:none;"><s:select id="sign" name="sign" list="{'+','-'}" value="%{sign}" /><s:textfield id="percentage" name="percentage" value="%{tenderResponse.percentage.abs()}" maxlength="5" size="10" onblur="checkUptoTwoDecimalPlace(this,'tenderResponse_error','Tender/Quoted Rate');"/></div>
					</td>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox">&nbsp;</td>
					<td  class="bluebox">&nbsp;</td>
			</tr>
			
			</s:if>
			<s:else>
			<tr>
					<td  class="greybox">&nbsp;</td>
					<td  class="greybox"><s:text name="nameOfBidder" /></td>
					<td  class="greybox">
						<s:textfield id="owner.firstName" name="owner.firstName" value="%{tenderResponse.owner.firstName}" maxlength="30" onblur="checkSpecialCharactersInName(this,'tenderResponse_error','Name Of Bidder');" />
						<s:hidden id="owner.citizenID" name="owner.citizenID" value="%{tenderResponse.owner.citizenID}" />
					</td>
					<td  class="greybox"><s:text name="addressOfBidder"/></td>
					<td  class="greybox">
						<s:textarea id="bidderAddress.streetAddress1" name="bidderAddress.streetAddress1" value="%{tenderResponse.bidderAddress.streetAddress1}" rows="4" cols="20"/>
						<s:hidden id="bidderAddress.addressID" name="bidderAddress.addressID" value="%{tenderResponse.bidderAddress.addressID}" />
					</td>
					<td  class="greybox">&nbsp;</td>
			</tr>
			<tr>
					<td  class="bluebox" >&nbsp;</td>
					<td  class="bluebox"><s:text name="contactNumber" /></td>
					<td  class="bluebox"><s:textfield id="owner.officePhone" name="owner.officePhone" value="%{tenderResponse.owner.officePhone}" onblur="checkPhoneNumberContent(this,'tenderResponse_error');" maxlength="10"/></td>
					<td  class="bluebox"><s:text name="ratePerSqft" /></td>
					<td  class="bluebox"><s:textfield id="ratePerUnit" name="ratePerUnit" value="%{tenderResponse.ratePerUnit}" onblur="checkUptoTwoDecimalPlace(this,'tenderResponse_error','Rate Per sq.ft');" maxlength="13"/></td>
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
			 <s:iterator value="responseLineList" id="responseLineList"  status="row_status">
			 responseLineTable.addRow(
	        						   {SlNo:'<s:property value="#row_status.count"/>',
	        						    item:'<s:property value="tenderableEntity.number"/>'+'-'+'<s:property value="tenderableEntity.description"/>',
	                                	quantity:'<s:property value="quantityByUom"/>',
	                                	bidRate:'<s:property value="bidRateByUom"/>',
	                                	uomName:'<s:property value="uom.uom"/>',
	                                	uom:'<s:property value="uom.id"/>',
	                                	estimatedRate:'<s:property value="estimatedRateByUom"/>',
	                                	tenderableEntity:'<s:property value="tenderableEntity.id"/>',
	                                	totalEstimatedRate:'<s:property value="totalEstimatedRate" default=""/>',
	                                	totalBidRate:'<s:property value="totalBidRateByUom"/>'});
	         </s:iterator> 
			</script>
     <div class="blueshadow"></div> 
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
 	<tr>
		 <td  class="bluebox" width="18%">&nbsp;</td>
		 <td  class="bluebox"><s:text name="remarks.lbl" /><span class="mandatory">*</span></td>
		 <td  class="bluebox"  width="27%"><s:textarea id="remarks" name="remarks" value="%{remarks}" rows="4" cols="40"/></td>
		 <td  class="bluebox" widht="10%"><s:text name="justifiction.date"/><span class="mandatory">*</span></td>
         <td  class="bluebox">
         	<s:date name="justifiedDate" var="justifiedDateTemp" format="dd/MM/yyyy"/>
         	<s:textfield name="justifiedDate" id="justifiedDate"  value="%{justifiedDateTemp}" onkeyup = "DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" />
         	<s:if test="%{mode!='view'}">
				<a href="javascript:show_calendar('forms[0].justifiedDate');" onmouseover="window.status='Date Picker';return true;"
									onmouseout="window.status='';return true; ">
					<img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0" align="absmiddle"/>
				</a>
			</s:if>	
         </td>
		 <td  class="bluebox" width="20%">&nbsp;</td>
		 
	</tr>
		 
					
		</table>
 	
	<div class="buttonbottom" align="center">
		<table>
			<tr>
			 	<td ><s:if test="%{mode!='view'}">
			 			<s:submit type="submit" cssClass="buttonsubmit" value="Save" id="save" name="save" method="create" onclick="return validateForm();"/>
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