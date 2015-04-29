<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<script src="<egov:url path='js/works.js'/>"></script>
<script type="text/javascript" src="<egov:url path='jsutils/prototype/prototype.js'/>"></script>

<script>
function setOpeningFinancialBidDate()
{
	var elem = document.getElementById("tenderInvitationType");
	var SINGLE_COVER_SYSTEM =   '<s:property value="@org.egov.works.models.tender.TenderInvitationType@SINGLE_COVER_SYSTEM" />';
	var openingFinanObj =  document.getElementById("openingFinBidDate");
	var openingFinanCalObj =  document.getElementById("openingFinBidDateCal");
	openingFinanObj.disabled=false;
	openingFinanCalObj.href="javascript:show_calendar('forms[0].openingFinBidDate',null,null,'DD/MM/YYYY');";
	if(elem.value==SINGLE_COVER_SYSTEM)
	{
		openingFinanObj.value = document.getElementById("openingTechBidDate").value;
		openingFinanObj.disabled=true;
		openingFinanCalObj.href="#";
	}
}
function setMarketRate(obj) {
	if(obj.value!=''){
		var objVal = obj.value;
	    objVal=assignMrktRateValue(obj);
	    document.getElementById('currentMarketRate').value=objVal;
	}
	else {
		document.getElementById('currentMarketRateVal').value='';
		document.getElementById('currentMarketRateVal').value='';
	}
}

function assignMrktRateValue(obj){
var persign = dom.get('tenderScrutinizingSign').value;
if(persign=='1'){
      objVal = -+obj.value;
      return objVal;
  }else{
     return obj.value;;
  }
}
function  setL1BidderName()
{
	var codeNameStr='';
	var contractorsRecords= contractorsDataTable.getRecordSet();
	var contractorName;
	var contractorCode;
	for(i=0;i<contractorsRecords.getLength();i++){
		contractorName = dom.get("name"+contractorsRecords.getRecord(i).getId()).value;
		contractorCode = dom.get("code"+contractorsRecords.getRecord(i).getId()).value;
		if(contractorName!='')
		{
			if(codeNameStr=='')
				codeNameStr = contractorCode+"/"+contractorName;
			else
				codeNameStr = codeNameStr+"; "+ contractorCode+"/"+contractorName;  			
		}	
	}
	document.getElementById("nameOfL1Bidder").value=codeNameStr;
}
// client side validation for no of eligible bidders against no of bidders
  function validateBidders(){
	if(document.getElementById("noOfEligibleBidders").value > document.getElementById("noOfBidders").value){
		document.getElementById('negotiation_error').style.display ='';	
		document.getElementById('negotiation_error').innerHTML ="";
		document.getElementById('negotiation_error').innerHTML ='<s:text name="workflow.noOfEligibleBidders.greaterThan.noOfBidders"/>';
		return false;
	}
	return true;
}    
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="scrutinizingSheetTable" name="scrutinizingSheetTable">
	<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name="tenderNegotiation.scrutinizing.commt.tab.details" /></div></td>
	</tr>
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.opening.tech.bid.date'/></td>
        <td class="greybox2wk"><s:select cssClass="selectwk" name="tenderScrutinizingAbstract.tenderInvitationType" id="tenderInvitationType" list="%{tenderInvitationTypeMap}" headerKey="" headerValue="%{getText('list.default.select')}" 
        	value='%{tenderScrutinizingAbstract.tenderInvitationType}' onchange="setOpeningFinancialBidDate()"/></td>                         
	 	<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.scrutinizing.date'/>: </td>
	 	<td class="greybox2wk">
	 		<s:date name="tenderScrutinizingAbstract.openingTechBidDate" var="openingTechBidDateFormat" format="dd/MM/yyyy"/>
	 		<s:textfield name="tenderScrutinizingAbstract.openingTechBidDate" onblur="setOpeningFinancialBidDate()" value="%{openingTechBidDateFormat}" id="openingTechBidDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
         	<a href="javascript:show_calendar('forms[0].openingTechBidDate',null,null,'DD/MM/YYYY');"  onchange="setOpeningFinancialBidDate()" onblur="setOpeningFinancialBidDate()" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
        </td>  
	</tr>
	<tr>
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.no.of.bidders'/></td>
        <td class="whitebox2wk"><s:textfield name="tenderScrutinizingAbstract.noOfBidders" id="noOfBidders" size="15" value='%{tenderScrutinizingAbstract.noOfBidders}' cssClass="selectboldwk" /></td>                         
	 	<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.no.of.eligible.bidders'/>: </td>
	 	<td class="whitebox2wk"><s:textfield name="tenderScrutinizingAbstract.noOfEligibleBidders" id="noOfEligibleBidders" size="15" value='%{tenderScrutinizingAbstract.noOfEligibleBidders}'  cssClass="selectboldwk" /></td>  
	</tr>
	<tr>
	 	<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.opening.fin.bid.date'/>: </td>
	 	<td class="greybox2wk">
	 		<s:date name="tenderScrutinizingAbstract.openingFinBidDate" var="openingFinBidDateFormat" format="dd/MM/yyyy"/>
	 		<s:textfield name="tenderScrutinizingAbstract.openingFinBidDate" value='%{openingFinBidDateFormat}' id="openingFinBidDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
        	<a id="openingFinBidDateCal" href="javascript:show_calendar('forms[0].openingFinBidDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
        </td>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.l1.bidder.name'/></td>
        <td class="greybox2wk"><textarea rows='3' cols='30' name="nameOfL1Bidder" id="nameOfL1Bidder"  readonly="readonly" class="selectboldwk"  ><s:property value='%{nameOfL1Bidder}' /></textarea></td>                         
	</tr>
	<tr>
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.bid.capacity.required'/></td>
        <td class="whitebox2wk"><s:textfield name="tenderScrutinizingAbstract.bidCapacityRequired" id="bidCapacityRequired" value='%{tenderScrutinizingAbstract.bidCapacityRequired}'  cssClass="selectboldwk"  /></td>                         
	 	<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.bid.capacity.available'/>: </td>
        <td class="whitebox2wk"><s:textfield name="tenderScrutinizingAbstract.bidCapacityAvailable" id="bidCapacityAvailable" value='%{tenderScrutinizingAbstract.bidCapacityAvailable}' cssClass="selectboldwk"  /></td>
	</tr>
	<tr>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.justification.for.excess.or.less'/>: </td>
        <td class="greybox2wk"><textarea rows='3' cols='30' name="tenderScrutinizingAbstract.justificationDetails" id="justificationDetails" class="selectboldwk"   ><s:property value='%{tenderScrutinizingAbstract.justificationDetails}' /></textarea></td>                         
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.current.mkt.rate'/>: </td>
        <td class="greybox2wk">
        <s:select name="tenderScrutinizingSign" cssClass="selectwk" id="tenderScrutinizingSign" list="#{'0':'+','1':'-'}"
				value="%{tenderScrutinizingSign}" onchange="setMarketRate(dom.get('currentMarketRateVal'));"/>
        <s:textfield name="currentMarketRateVal" value="%{currentMarketRateVal}" id="currentMarketRateVal" onchange="setMarketRate(this);" cssClass="selectamountwk" maxlength="10" />
		<s:hidden id="currentMarketRate" name="tenderScrutinizingAbstract.currentMarketRate" value='%{tenderScrutinizingAbstract.currentMarketRate}' />
	</tr>
	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>			
 </table>
