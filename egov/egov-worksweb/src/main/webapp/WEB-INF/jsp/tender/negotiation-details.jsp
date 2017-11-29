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

<style>
#yui-dt0-bodytable {
    Width:100%;
} 
.yui-dt-col-estimatedQuantity{
	text-align:right;
}
.yui-dt-col-estimatedRate{ 
	text-align:right;
}
.yui-dt-col-estTotal{
	text-align:right;
}	
.yui-dt-col-estimatedAmount{
	text-align:right;
}
.yui-dt-col-quotedAmount{
	text-align:right;
}
.yui-dt-col-negotiatedAmount{
	text-align:right;
}
.yui-dt-col-marketRate{
	text-align:right;
}
.yui-dt-col-marketRateAmount{
	text-align:right;
}
</style>
<script type="text/javascript" src="<egov:url path='jsutils/prototype/prototype.js'/>"></script>

<script>

function getNegotiatedAmount(obj) {
	if(obj.value!=''){
		var objVal = obj.value;
	    objVal=assignSignPercNegoAmount(obj);
	    dom.get('percNegotiatedAmountRate').value=objVal;
		document.tenderNegotiationForm.percNegotiatedAmount.value=eval(document.tenderNegotiationForm.estimateAmount.value)+eval(document.tenderNegotiationForm.estimateAmount.value*objVal)/100;
		document.tenderNegotiationForm.percNegotiatedAmount.value=roundTo(document.tenderNegotiationForm.percNegotiatedAmount.value);
	}
	else {
		document.tenderNegotiationForm.percNegotiatedAmount.value='';
		$("percNegotiatedAmount").value="";
		dom.get('percNegotiatedAmountRate').value="";
	}
}

function assignSignPercNegoAmount(obj){
var persign = dom.get('percSignNegoRate').value;
if(persign=='1'){
      objVal = -+obj.value;
      return objVal;
  }else{
     return obj.value;;
  }
}

function getTenderPercAgainstMarketRate() {
	var estimateAmount=document.tenderNegotiationForm.estimateAmount.value;
	var marketRateEstimateAmount=document.tenderNegotiationForm.marketRateEstimateAmount.value;
	var percQuotedAmount=document.tenderNegotiationForm.percQuotedAmount.value;
	var percNegotiatedAmount=document.tenderNegotiationForm.percNegotiatedAmount.value;
	var percNegotiatedRate=document.tenderNegotiationForm.percNegotiatedAmountRate.value; 
	if(estimateAmount!='' && marketRateEstimateAmount!='' && percNegotiatedRate!='' && percNegotiatedAmount!='') {
		document.tenderNegotiationForm.tenderPercAgnstMarketRate.value=roundTo((((estimateAmount*(1+eval(percNegotiatedRate)/100))-marketRateEstimateAmount)/marketRateEstimateAmount)*100);
	}
}

function getMarketValueAsOnDate(){
	var date=document.tenderNegotiationForm.asOnDate.value;
	if(date=='') {
		dom.get("negotiation_error").style.display='';
    	document.getElementById("negotiation_error").innerHTML='<s:text name="tenderResponse.negotiationDate.null" />';
    	return;
	}
	else if(!validateDateFormat(document.tenderNegotiationForm.asOnDate)) {
    	dom.get('errorasOnDate').style.display='none';
    	return;
	}
	else {
		dom.get("negotiation_error").style.display='none';
	}
	document.tenderNegotiationForm.negotiationDate.value=document.tenderNegotiationForm.asOnDate.value;
    if(document.tenderNegotiationForm.estimateId.value!="")
    	makeJSONCall(["MarketRateEstimateAmount"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!getMarketValue.action',{asOnDate:date, estimateId:document.tenderNegotiationForm.estimateId.value},marketRateEstimateAmountLoadHandler ,marketRateEstimateAmountLoadFailureHandler) ;
    else if(document.tenderNegotiationForm.worksPackageId.value!="")
        makeJSONCall(["MarketRateEstimateAmount"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!getMarketValue.action',{asOnDate:date, packageId:document.tenderNegotiationForm.worksPackageId.value},marketRateEstimateAmountLoadHandler ,marketRateEstimateAmountLoadFailureHandler) ;
}

function msgSorDiffPer(sorDifPer,sorDifPerAppconfig){
	var ans=true;
	if(sorDifPer > sorDifPerAppconfig){
	  ans=confirm('Negotiated rate is'+ sorDifPerAppconfig +'% greater than SOR rate');
	}
	return ans;
}

function sorDiff(negotiatedRate,sorRate){
	var sorDifPer=((negotiatedRate - sorRate)/sorRate)*100;
	return sorDifPer;
}


function populateSorPerDiff(negotiatedRate,sorRate){
	var sorDifPer=((negotiatedRate - sorRate)/sorRate)*100;
	var sorDifPerAppconfig='<s:property value="%{sorPerDiff}"/>';
	itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('SORDiff'),roundTo(getNumber(sorDifPer)));
}


var descriptionFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>"+hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('FullDescription')))
    el.innerHTML = markup;
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="/egworks/resources/erp2/images/help.gif" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
  var fieldName = "actionTenderResponseActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
   // var fieldName = "activityId[" + oRecord.getCount() + "]";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
function createHiddenFormatterSchNo(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionTenderResponseActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
   // var fieldName = "activityId[" + oRecord.getCount() + "]";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);
var hiddenFormatterForSchNo = createHiddenFormatterSchNo(10,10);

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionTenderResponseActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(11,8);


function createQRTextBoxFormatter(size,maxlength){
var textQRboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionTenderResponseActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateQRAmountNew(this,\""+oRecord.getId()+"\",\""+oColumn.getKey()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textQRboxFormatter;
}
var textboxQRFormatter = createQRTextBoxFormatter(11,13);

function createEstQtyHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="actionTenderResponseActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var estimateQtyHiddenFormatter = createEstQtyHiddenFormatter(11,13);

function createUomFactorHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    //var id="estimate"+oColumn.getKey()+sorDataTable.getRecordSet().getLength();
    var fieldName="";
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var uomFactorHiddenFormatter = createUomFactorHiddenFormatter(5,5);


function createSchIdHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="";
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
createSchIdHiddenFormatter

var schIdHiddenFormatter=createSchIdHiddenFormatter(5,5);

var contractorDetailsFormatter=createContractorDetailsFormatter();
function createContractorDetailsFormatter(){

var contractorDetailsFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var cont_details;
    if(value!=""){
 		cont_details=value.split("~");
	}
    var content="<div id='contractorT["+ oRecord.getCount() + "]' style='border-style: none;'>";
      var contractorListObj=dom.get('contractorList');
      var count;
      var quotedRateValue="";
      var quotedAmountValue="";
      var totalQuotedAmountValue=0.0;
     
       	 count=0;  
       	
    for(var l=1;l<contractorListObj.length;l++){

    	var contractorId=contractorListObj.options[l].value;
    	if(cont_details[count].split("|")[0]==contractorId || cont_details[count].split("|")[0]==""){
    		quotedRateValue=roundTo(cont_details[count].split("|")[1]);
    		quotedAmountValue=roundTo(eval(cont_details[count].split("|")[2]));
    	}
    	var contractorCode=contractorListObj.options[l].text;
    	contractorCode=contractorCode.split("~")[0];
    	var contractor_id="contractorId_"+contractorId+"_"+oColumn.getKey()+oRecord.getId();
    	var contractor_code="contractorCode_"+contractorId+"_"+oColumn.getKey()+oRecord.getId();
    	var quotedrate_id="quotedRate_"+contractorId+"_"+oColumn.getKey()+oRecord.getId();
    	var quotedAmount_id="quotedAmount_"+contractorId+"_"+oColumn.getKey()+oRecord.getId();
    	
    	var contractorIdfield="actionTenderResponseActivities[" + oRecord.getCount() + "].tenderResponseQuotes["+count+"].contractor.id";
    	var contractorCodefield="actionTenderResponseActivities[" + oRecord.getCount() + "].tenderResponseQuotes["+count+"].contractor.code";
    	var quotedRatefield="actionTenderResponseActivities[" + oRecord.getCount() + "].tenderResponseQuotes["+count+"].quotedRate";
    	var quotedAmountfield="actionTenderResponseActivities[" + oRecord.getCount() + "].tenderResponseQuotes["+count+"].quotedAmount";
    	
    	if(contractorId!='ALL'){
   		 	content+="<div id='contractorTable"+contractorId+"["+ oRecord.getCount() + "]' style='border-style: none;'><table style='border-style: none;' cellspacing='0' cellpadding='0'><tr><td width='30%' style='border-style: none;'><input type='hidden' id='"+contractor_id+"' name='"+contractorIdfield+"' value='"+contractorId+"'/> <input type='text' class='selectwk' id='"+contractor_code+"' name='"+contractorCodefield+"' value='"+contractorCode+"' size='10' readOnly='true'/></td><td width='35%' style='border-style: none;'><input type='text' class='selectamountwk' id='"+quotedrate_id+"' name='"+quotedRatefield+"' value='"+quotedRateValue+"' size='10' maxlength='10' onblur='calculateQRAmountNew(this,\""+oRecord.getId()+"\",\""+oColumn.getKey()+"\");' /><span id='error"+quotedrate_id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></td><td style='border-style: none;' width='35%'><input type='text' class='selectamountwk' id='"+quotedAmount_id+"' name='"+quotedAmountfield+"' value='"+quotedAmountValue+"' size='10' maxlength='10' readOnly='true' /></td></tr></table></div>      ";
   		 	if(!document.getElementById("quotedTotal_"+contractorId)){
   		 		var newdivTag = document.createElement("div");
         		newdivTag.id = "quotedTotal_"+contractorId;
            	newdivTag.innerHTML = "<table border='0' width='100%' style='border-style: none;'><tr><td style='border-style: none;' width='65%'>"+contractorCode+"</td><td width='35%' align='right' style='border-style: none;text-align:right;' id='quotedTotalValue_"+contractorId+"'>"+totalQuotedAmountValue+"</td></tr></table>";
            	newdivTag.innerHTML = "";
            	document.getElementById("quotedTotal").appendChild(newdivTag);
             }
   		 	count++;
   		 }
    
    }
    content+="</div>";
   //alert(content);
   
    el.innerHTML = content;


}
return contractorDetailsFormatter;
}



var itemRateDataTable;
var makeItemRateDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var itemRateColumnDefs = [
 	    	{key:"schId", hidden:true, formatter:schIdHiddenFormatter,sortable:false, resizeable:false},
            {key:"activity", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
            {key:"schCode",hidden:true,formatter:hiddenFormatterForSchNo, sortable:false, resizeable:false},
            {key:"slNo",label:'<s:text name='tenderNegotiation.slNo.title'/>', sortable:false, resizeable:false},
            {key:"schNo",label:'<s:text name='tenderNegotiation.schNo'/>',sortable:false, resizeable:false},
            {key:"description", label:'<s:text name='tenderNegotiation.item'/>', formatter:descriptionFormatter,sortable:false, resizeable:false},
            {key:"estimatedQuantity",label:'<s:text name='tenderNegotiation.estimatedQty.title'/>', sortable:false, resizeable:false},
            {key:"estimatedQty",hidden:true, sortable:false, formatter:estimateQtyHiddenFormatter,resizeable:false},
            {key:"estimatedRate", label:'<s:text name='tenderNegotiation.estimatedRate.title'/>', sortable:false, resizeable:false},
            {key:"uomFactor",label:'Unit Rate', hidden:true,  formatter:uomFactorHiddenFormatter, sortable:false, resizeable:false},
            {key:"uom", label:'<s:text name='tenderNegotiation.uom'/>', sortable:false, resizeable:false},           
            {key:"estimatedAmount",label:'<s:text name='tenderNegotiation.estimatedAmount'/>', sortable:false, resizeable:false},  
            {label:'Contractor Details', sortable:false, resizeable:false,children: [{label:"  Contractor Code   |   Quoted Rate   |   Quoted Amount  ",key:"contractordetails",formatter:contractorDetailsFormatter}] },
            {key:"negotiatedRate",label:'<s:text name='tenderNegotiation.quotedRate_after_negotiation'/>', formatter:textboxQRFormatter, sortable:false, resizeable:false},
            {key:"negotiatedAmount",label:'<s:text name='tenderNegotiation.negotiatedAmount'/>', sortable:false, resizeable:false},
            {key:"marketRate",label:'<s:text name='tenderNegotiation.marketRate_asOn'/>:<br><span id="asOnDateLabel" name="asOnDateLabel">'+document.tenderNegotiationForm.negotiationDate.value, sortable:false, resizeable:false},
            {key:"marketRateAmount",label:'<s:text name='tenderNegotiation.marketRateAmount'/>', sortable:false, resizeable:false},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false},
            {key:"SORDiff",label:'<s:text name='tenderNegotiation.sorperdiff'/>', sortable:false, resizeable:false}
        ];

        var itemRateDataSource = new YAHOO.util.DataSource();
        itemRateDataTable = new YAHOO.widget.DataTable("itemRateTable",
                itemRateColumnDefs, itemRateDataSource,{MSG_EMPTY:""});
         itemRateDataTable.subscribe("cellClickEvent", itemRateDataTable.onEventShowCellEditor); 
	
            var tfoot = itemRateDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 10;
			th.className= 'whitebox4wk';
			th.innerHTML = '&nbsp;';

			var td = tr.insertCell(1);
			td.className= 'whitebox4wk';
			td.id = 'subTotal';
			td.innerHTML = '<span class="bold">Total:</span>';
			addCell(tr,2,'estTotal','0.00');
			addCell(tr,3,'quotedTotal','');
			addCell(tr,4,'','');
			addCell(tr,5,'negotiatedTotal','0.00');
			addCell(tr,6,'','');
			addCell(tr,7,'marketRateTotal','0.00');	
			var th2 = tr.appendChild(document.createElement('td'));
			th2.colSpan = 2;
			th2.className= 'whitebox4wk';
			th2.innerHTML = '&nbsp;';		

        return {
            oDS: itemRateDataSource,
            oDT: itemRateDataTable
        };
    }

function getMarketRatesAsOnDateChange(){
	if(document.tenderNegotiationForm.tenderType.value!='-1') {
		if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[2].value) {					
			var schIds="";
			var count=0;
			for(i=0;i<itemRateDataTable.getRecordSet().getLength();i++){
				if(itemRateDataTable.getRecordSet().getRecord(i).getData('schId')!='') {					
					if(schIds!='')
						schIds=schIds+"~"+itemRateDataTable.getRecordSet().getRecord(i).getData('schId');
					else
						schIds=itemRateDataTable.getRecordSet().getRecord(i).getData('schId');
				}
			}
			if(schIds!="")getMarketRate(schIds);		
		}
	}
}

function getMarketRate(scheduleIds){
	getCurrentDate();
	document.getElementById("asOnDateLabel").innerHTML=document.tenderNegotiationForm.negotiationDate.value;
	var asOnDate = document.tenderNegotiationForm.negotiationDate.value;
	makeJSONCall(["MarketRate","ScheduleId"],'${pageContext.request.contextPath}/masters/ajaxScheduleOfRate!getMarketValueAsOnDate.action',
		{asOnDate:asOnDate,scheduleIds:scheduleIds},marketRateLoadHandler,marketRateLoadFailureHandler) ;
}

function updateMarketRate(schId,marketRate)
{
  for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++){
      if(schId==itemRateDataTable.getRecordSet().getRecord(i).getData('schId') && marketRate>0)
      {
      	 itemRateDataTable.updateCell(itemRateDataTable.getRecord(i),itemRateDataTable.getColumn('marketRate'),roundTo(marketRate)); 
      }
      else  if(schId==itemRateDataTable.getRecordSet().getRecord(i).getData('schId') && marketRate<=0)
      {
      	 itemRateDataTable.updateCell(itemRateDataTable.getRecord(i),itemRateDataTable.getColumn('marketRate'),roundTo(itemRateDataTable.getRecordSet().getRecord(i).getData('estimatedRate')));
      }
   }
}
marketRateLoadHandler = function(req,res){
	results=res.results;
	var marketRates=results[0].MarketRate.split(', ');		
	var schIds=results[0].ScheduleId.split('~');
	var len =schIds.length;
	var records=itemRateDataTable.getRecordSet(); 
	for(var i=0;i<len;i++){	
	       updateMarketRate(schIds[i],marketRates[i])
	}
}

function calculateMarketRateAmount(){
	if(document.tenderNegotiationForm.tenderType.value!='-1') {
		if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[2].value) {
			YAHOO.lang.later(300,null,function(){
				var marketRateAmountTotal=0.0;
				for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++){
				     var marketRateAmount=itemRateDataTable.getRecordSet().getRecord(i).getData('marketRate')*itemRateDataTable.getRecordSet().getRecord(i).getData('uomFactor')*itemRateDataTable.getRecordSet().getRecord(i).getData('estimatedQuantity');
					  itemRateDataTable.updateCell(itemRateDataTable.getRecord(i),itemRateDataTable.getColumn('marketRateAmount'),roundTo(marketRateAmount));
					  marketRateAmountTotal=marketRateAmountTotal+marketRateAmount;
					  dom.get("marketRateTotal").innerHTML=roundTo(marketRateAmountTotal);
				}
			});	
		}
	}
}

marketRateLoadFailureHandler= function(){
    dom.get("negotiation_error").style.display='';
	document.getElementById("negotiation_error").innerHTML='Unable to load Market Rate';
}

/* Added for story card #1110. Providing a drop down to select the +/- sign for negotiated rate in the Negotiation Details */
function checkPercNegotiatedRate(elem){
	if(!validate_Number(elem))
	 return false; 
	if(!validateNegativeNumber(elem))
	 return false;
	getNegotiatedAmount(elem); 
	getTenderPercAgainstMarketRate();
}

function validateDetailsBeforeSubmit(tenderNegotiationForm){
	 if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[1].value){
	 	if(tenderNegotiationForm.percNegotiatedRate.value==""){
	 		dom.get("negotiation_error").style.display='';
	 		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.negotiatedAmount" />';
	 		return false;
	 	}
	 	if(tenderNegotiationForm.percNegotiatedRate.value!=""){
	 		if(!validate_Number(tenderNegotiationForm.percNegotiatedRate))
	 			return false;
	 	}
	 	
	
	}
	else{
		<s:if test="%{ model.id==null 
			|| (model.egwStatus!=null && (model.egwStatus.code=='NEW'|| model.egwStatus.code=='REJECTED'))}">
		if(document.tenderNegotiationForm.actionName.value!='cancel'){
	 		if(!validateTotalQuotedRateForContractors())
				return false;
		}
		</s:if>
		return true;
	}
		
	return true;
}
function validateTotalQuotedRateForContractors(){
	var contractorListObj=document.getElementById('contractorList');
	var totals=new Array();
	var sametotal=true;
	var ans=true;
	for(var j=1;j<contractorListObj.length;j++)
	{
		var contrId=contractorListObj.options[j].value;
		
		totals[j]=eval(document.getElementById("quotedTotalValue_"+contrId).innerHTML);
		
		if(j!=1 && totals[j]!=totals[j-1]){
			sametotal=false;
		}
	}
	if(!sametotal){
		ans=confirm("The Tender value quoted by the contractors is not the same. Do you want to continue?");
	}	
	return ans;
	
}
function calculateQuotedTotalForContractors(){

	var contractorListObj=document.getElementById('contractorList');
	for(var j=1;j<contractorListObj.length;j++)
	{
		var totalQuotedAmountValue=0.0; 
		var contrId=contractorListObj.options[j].value;
		var contractorCode=contractorListObj.options[j].text;
    	contractorCode=contractorCode.split("~")[0];
    	
		
 		for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++)
 		{
 			
 			if(document.getElementById('quotedAmount_'+contrId+'_contractordetails'+itemRateDataTable.getRecord(i).getId())){
 				totalQuotedAmountValue+=eval(document.getElementById('quotedAmount_'+contrId+'_contractordetails'+itemRateDataTable.getRecord(i).getId()).value);
 			}
 		}
 		

	 	var quotedTotaldiv=document.getElementById("quotedTotal_"+contrId);
	 	quotedTotaldiv.innerHTML="";
	    quotedTotaldiv.innerHTML = "<table style='border-style: none;' width='100%'><tr><td width='65%' style='border-style: none;'>"+contractorCode+"</td><td width='35%' style='border-style: none;text-align:right;' id='quotedTotalValue_"+contrId+"'>"+roundTo(totalQuotedAmountValue)+"</td></tr></table>";
    }
 
}

function calculateQuotedAmount(elem,recordId,column){
      if(!validateNumberInTableCell(itemRateDataTable,elem,recordId)) return;
      //quotedRate_1_contractordetailsyui-rec0
      if(column=='contractordetails'){
      	var elemId=elem.id;
      	var contrId=elemId.split("quotedRate_")[1].split("_contractordetails"+recordId)[0];
     	oldQuotedAmount=getNumber(document.getElementById('quotedAmount_'+contrId+'_contractordetails'+recordId).value);
     	newQuotedAmount=roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("estimatedQuantity")));
      	document.getElementById('quotedAmount_'+contrId+'_contractordetails'+recordId).value=newQuotedAmount;
    }
      oldNegotiatedAmount=getNumber(record.getData("negotiatedAmount"));
      itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('negotiatedAmount'),roundTo(getNumber(dom.get('negotiatedRate'+recordId).value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("estimatedQuantity")))); 
	  calculateQuotedTotalForContractors();     
      dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - oldNegotiatedAmount + getNumber(record.getData("negotiatedAmount")));
}

function calculateQRAmountNew(elem,recordId,column){

  	 calculateQuotedAmount(elem,recordId,column);
  	  checkSorPerDiffNew(elem,getNumber(dom.get('negotiatedRate'+recordId).value),getNumber(record.getData("estimatedRate")),recordId,column);
	 
}
function checkSorPerDiffNew(elem,negotiatedRate,sorRate,recordId,column){
	
	if(dom.get("sorRateDiffItemRateChk").checked==true && column=='contractordetails'){
	 quotedRate=roundTo(getNumber(elem.value));
      itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('negotiatedRate'),quotedRate);
	   negotiatedRate=getNumber(dom.get('negotiatedRate'+recordId).value);
	}
	
	var sorDifPer=sorDiff(negotiatedRate,sorRate);
	var sorDifPerAppconfig='<s:property value="%{sorPerDiff}"/>';
	itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('SORDiff'),roundTo(getNumber(sorDifPer)));
	
	var ans=true;
	if((dom.get("sorRateDiffItemRateChk").checked==true && column=='contractordetails') || column=='negotiatedRate'){
		ans=msgSorDiffPer(sorDifPer,sorDifPerAppconfig);
	}
	
	if(!ans){
		  itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('negotiatedRate'),"0.00");	
	}
	oldNegotiatedAmount=getNumber(record.getData("negotiatedAmount"));
    itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('negotiatedAmount'),roundTo(getNumber(dom.get('negotiatedRate'+recordId).value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("estimatedQuantity"))));
    dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - oldNegotiatedAmount + ((dom.get('negotiatedRate'+recordId).value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("estimatedQuantity"))));  
    sorDifPer=sorDiff(getNumber(dom.get('negotiatedRate'+recordId).value),sorRate);
    itemRateDataTable.updateCell(record,itemRateDataTable.getColumn('SORDiff'),roundTo(getNumber(sorDifPer)));
}

function changeContractor(contractorObj){
var contentText;
 var contractorListObj=document.getElementById('contractorList');

for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++){
	for(var j=1;j<contractorObj.length;j++){
		var div='contractorTable'+contractorObj.options[j].value+'['+ itemRateDataTable.getRecord(i).getCount() + ']';
		var totaldiv='quotedTotal_'+contractorObj.options[j].value;
		if(document.getElementById(div)){
			if(contractorObj.value=='ALL'){
  				document.getElementById(div).style.display = "block";
  				document.getElementById(totaldiv).style.display = "block";
  			}
  			else if(contractorObj.options[j].value==contractorObj.value){
	  			document.getElementById(div).style.display = "block";
  				document.getElementById(totaldiv).style.display = "block";
  			}
  			else{
	  			document.getElementById(div).style.display = "none";
  				document.getElementById(totaldiv).style.display = "none";
  			}
  		}
  	}
  }
}

function confirmDefaultRate(obj){
	if(obj.checked==true){
		var ans=confirm("The negotiated rate will be defaulted to the quoted rate entered. Do you want to continue?");
		if(!ans)
			obj.checked=false;
	}

}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rateContractTable" name="rateContractTable">
	<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
		<div class="headplacer"><s:text name="tenderNegotiation.tab.details" /></div></td>
	</tr>
	
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderPercAftrNegotiation'/>: </td>
        <td class="greybox2wk">
        <s:select name="percSignNegoRate" id="percSignNegoRate" list="#{'0':'+','1':'-'}"
				value="%{percSignNegoRate}" onchange="getNegotiatedAmount(dom.get('percNegotiatedRate'));getTenderPercAgainstMarketRate();"/>
        <s:textfield name="percNegotiatedRate" value="%{percNegotiatedRate}" id="percNegotiatedRate" cssClass="selectamountwk" onblur="checkPercNegotiatedRate(this);"  maxlength="10" size="10"/>
        <span id='errorpercNegotiatedRate' style="display:none;color:red;font-weight:bold">&nbsp;x</span></td>
         <s:hidden id="percNegotiatedAmountRate" name="percNegotiatedAmountRate" value="%{formattedPercNegotiatedAmountRate}"/>
        <td class="greyboxwk"><s:text name='tenderNegotiation.tenderValueAftrNegotiation'/>: </td>
        <td class="greybox2wk"><input type="text" name="percNegotiatedAmount" id="percNegotiatedAmount" value='<s:property value="%{percNegotiatedAmount}" />'  readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>                         
	</tr>
	<tr>
		<td class="whiteboxwk"><s:text name='tenderNegotiation.tenderPerc_against_marketRate'/><br>(<s:text name='tenderNegotiation.aftr_negotiation'/>): </td>
        <td class="whitebox2wk"><input type="text" name="tenderPercAgnstMarketRate" id="tenderPercAgnstMarketRate" value='<s:property value="%{tenderPercAgnstMarketRate}" />'  readonly="readonly" tabIndex="-1" class="selectamountwk" size="10"/></td>                         
	 	<td class="whiteboxwk"><s:text name='tenderNegotiation.asOnDate'/>: </td>
	 	<td class="whitebox2wk"><input type="text" name="asOnDate" value='<s:property value="%{asOnDate}" />' id="asOnDate" class="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
         <a href="javascript:show_calendar('forms[0].asOnDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
         <span id='errorasOnDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
         <input type='button' class="buttonadd" value='Re-Calculate' onClick='getMarketValueAsOnDate();getTenderPercAgainstMarketRate(this);'/>
        </td>  
	</tr>

	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>			
 </table>
 
 <table width="100%" border="0" cellspacing="0" cellpadding="0" id="itemRateContractTable" name="itemRateContractTable">
	<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
		<div class="headplacer"><s:text name="tenderNegotiation.tab.details" /></div></td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<input type="checkbox" name="sorRateDiffItemRateChk" id="sorRateDiffItemRateChk" onclick="confirmDefaultRate(this)"/>&nbsp;<s:text name='tenderNegotiation.defaultsortonegotiatedrate' />
       	</td>
	</tr>
	<tr>
		<td colspan="4" class="headingwk">
		<div class="headplacer"><s:text name="tenderNegotiation.nameOfContractor" /></div></td>
		<td align="right" class="headingwk" style="border-left-width: 1px">
       		<s:select name="contractorList" id="contractorList" headerKey="ALL"
				headerValue="--All Contractors--"
				list="%{dropdownData.contractorList}"  listKey="id" listValue="%{code + '~' +  name}"
				onChange="changeContractor(this);"/>
       	</td>
	</tr>
    <tr>
      <td colspan="5">
      	<div class="yui-skin-sam"> 
          <div id="itemRateTable"></div>
          <div id="itemRateTotals"></div>                                
      	</div>
      </td>
    </tr>
    <tr>
    	<td colspan="11" class="shadowwk"></td>
    </tr>
 </table>
<script>
makeItemRateDataTable();
<s:if test="%{id==null && tenderSource=='estimate' && !hasErrors() && !hasActionMessages()}"> 
<s:iterator id="itemRateIterator" value="abstractEstimate.activities" status="row_status">	
	<s:if test="%{schedule!=null}">	
    	itemRateDataTable.addRow({schId:'<s:property value="schedule.id"/>',
						activity:'<s:property value="id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'<s:property value="schedule.code"/>',
                        schCode:'<s:property value="schedule.code"/>',
                        description:'<s:property value="schedule.summaryJS"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                         estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="sORCurrentRate"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                           
                        uom:'<s:property value="schedule.uom.uom"/>',
                        estimatedAmount:roundTo('<s:property value="amount.value"/>'),
                        contractordetails:'|<s:property value="sORCurrentRate"/>|<s:property value="amount.value"/>',
                        negotiatedRate:roundTo('<s:property value="sORCurrentRate"/>'),
                        negotiatedAmount:roundTo('<s:property value="amount.value"/>'),
                        marketRate:'',
                        marketRateAmount:'',                       
                        SORDiff:''});	
        dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") );
   </s:if>
   <s:else>	
   		itemRateDataTable.addRow({
			schId:'',			
			activity:'<s:property value="id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'',
                        schCode:'',
                        description:'<s:property value="nonSor.descriptionJS" escape="false"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                         estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="rate.value"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                        
                        uom:'<s:property value="nonSor.uom.uom"/>',
                        estimatedAmount:roundTo('<s:property value="amount.value"/>'), 
                        contractordetails:'|<s:property value="rate.value"/>|<s:property value="amount.value"/>',
                        negotiatedRate:roundTo('<s:property value="rate.value"/>'), 
                        negotiatedAmount:roundTo('<s:property value="amount.value"/>'),  
                        marketRate:roundTo('<s:property value="rate.value"/>'),
                        marketRateAmount:roundTo('<s:property value="amount.value"/>'),                    
                        FullDescription:'<s:property value="nonSor.descriptionJS" escape="false"/>',
                        SORDiff:''});
   	    dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") - 0.0 + getNumber('<s:property value="amount.value"/>'));
   </s:else>
    var record = itemRateDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
  
    dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber('<s:property value="amount.value"/>'));  
    dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="amount.value"/>'));
    calculateQuotedTotalForContractors();
</s:iterator>
</s:if>  
<s:elseif  test="%{id==null && tenderSource=='package' && !hasErrors() && !hasActionMessages()}"> 
<s:iterator id="itemRateIterator" value="worksPackage.activitiesForEstimate" status="row_status">	

	<s:if test="%{code!=''}">
	 	itemRateDataTable.addRow({schId:'<s:property value="activity.schedule.id"/>',
			activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'<s:property value="code"/>',
                        schCode:'<s:property value="code"/>',
                        description:'<s:property value="activity.schedule.summaryJS"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                        estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="rate"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                           
                        uom:'<s:property value="uom"/>',
                        estimatedAmount:roundTo('<s:property value="amt"/>'),
                        contractordetails:roundTo('<s:property value="rate"/>'),
                        negotiatedRate:roundTo('<s:property value="rate"/>'),
                        negotiatedAmount:roundTo('<s:property value="amt"/>'),
                        marketRate:'',
                        marketRateAmount:'',                       
                        FullDescription:'<s:property value="activity.schedule.descriptionJS"/>',
                        SORDiff:''});
	    dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") );	

   </s:if>
   <s:else>
   		itemRateDataTable.addRow({
			schId:'',			
			activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'',
                        schCode:'',
                        description:'<s:property value="activity.nonSor.descriptionJS"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                        estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="rate"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                        
                        uom:'<s:property value="uom"/>',
                        estimatedAmount:roundTo('<s:property value="amt"/>'), 
                        contractordetails:roundTo('<s:property value="rate"/>'),
                        negotiatedRate:roundTo('<s:property value="rate"/>'), 
                        negotiatedAmount:roundTo('<s:property value="amt"/>'),  
                        marketRate:roundTo('<s:property value="rate"/>'),
                        marketRateAmount:roundTo('<s:property value="amt"/>'),                    
                        FullDescription:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
                        SORDiff:''});
   	    dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") - 0.0 + getNumber('<s:property value="amt"/>'));
   </s:else>
    var record = itemRateDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));

    dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber('<s:property value="amt"/>'));  
    dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="amt"/>'));
    
</s:iterator>

</s:elseif>
 <s:elseif test="%{id!=null}">
<s:iterator id="itemRateIterator1" value="tenderResponse.negotiationDetails" status="row_status">
	<s:if test="%{activity.schedule!=null}"> 
    	itemRateDataTable.addRow({schId:'<s:property value="activity.schedule.id"/>',
						activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'<s:property value="code"/>',
                        schCode:'<s:property value="code"/>',
                        description:'<s:property value="activity.schedule.summaryJS"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                        estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="rate"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                        
                        uom:'<s:property value="uom"/>',
                        estimatedAmount:roundTo('<s:property value="amt"/>'),
                        contractordetails:'<s:iterator id="trqIterator1" value="tenderResponseQuotes" status="rate_status1"><s:property value="contractor.id"/>|<s:property value="quotedRate"/>|<s:property value="quotedRate"/>*<s:property value="quantity"/>*<s:property value="conversionFactor"/><s:if test="!#rate_status1.last">~</s:if></s:iterator> ',  
                        negotiatedRate:roundTo('<s:property value="negotiatedRate"/>'),
                        negotiatedAmount:roundTo('<s:property value="negotiatedRate"/>' * '<s:property value="quantity"/>' * '<s:property value="conversionFactor"/>'), 
                        marketRate:'',
                        marketRateAmount:'',                       
                        FullDescription:'<s:property value="activity.schedule.descriptionJS"/>',
                        SORDiff:''});
                        dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="negotiatedRate"/>' * '<s:property value="quantity"/>' * '<s:property value="conversionFactor"/>'));
                        dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal"));
   </s:if>
   <s:else>
      		itemRateDataTable.addRow({schId:'',
			activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'',
                        schCode:'',
                        description:'<s:property value="activity.nonSor.descriptionJS"/>',
                        estimatedQuantity:'<s:property value="quantity"/>',
                        estimatedQty:'<s:property value="quantity"/>',
                        estimatedRate:roundTo('<s:property value="rate"/>'),
                        uomFactor:'<s:property value="conversionFactor"/>',                        
                        uom:'<s:property value="uom"/>',
                        estimatedAmount:roundTo('<s:property value="amt"/>'), 
                        contractordetails:'<s:iterator id="trqIterator2" value="tenderResponseQuotes" status="rate_status2"><s:property value="contractor.id"/>|<s:property value="quotedRate"/>|<s:property value="quotedRate"/>*<s:property value="quantity"/><s:if test="!#rate_status2.last">~</s:if></s:iterator> ', 
                        negotiatedRate:roundTo('<s:property value="negotiatedRate"/>'), 
                        negotiatedAmount:roundTo('<s:property value="negotiatedRate"/>' * '<s:property value="activity.quantity"/>'),   
                        marketRate:roundTo('<s:property value="rate"/>'),
                        marketRateAmount:roundTo('<s:property value="amount"/>'),                    
                        FullDescription:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
                        SORDiff:''});
                        dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="negotiatedRate"/>' * '<s:property value="activity.quantity"/>'));
                        dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") - 0.0 + getNumber('<s:property value="amount"/>'));
   </s:else>
    var record = itemRateDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>')); 
    
    var column = itemRateDataTable.getColumn('schId');  
    if(dom.get(column.getKey()+record.getId()).value!=''){
    	populateSorPerDiff('<s:property value="negotiatedRate"/>','<s:property value="activity.sORCurrentRate"/>');
    }
    else
    {
    	populateSorPerDiff('<s:property value="negotiatedRate"/>','<s:property value="activity.rate.value"/>');
    }
    dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber('<s:property value="amt"/>'));  
	calculateQuotedTotalForContractors();
</s:iterator> 

</s:elseif> 
<s:elseif test="%{hasErrors()}">
<s:iterator id="itemRateIterator2" value="tenderResponseActivityList" status="row_status">
	<s:if test="%{activity.schedule!=null}">
    	itemRateDataTable.addRow({schId:'<s:property value="activity.schedule.id"/>',
			activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'<s:property value="activity.schedule.code"/>',
                        schCode:'<s:property value="activity.schedule.code"/>',
                        description:'<s:property value="activity.schedule.summaryJS"/>',
                        
                        estimatedQuantity:'<s:property value="estimatedQty"/>',
                        estimatedQty:'<s:property value="estimatedQty"/>',
                        estimatedRate:roundTo('<s:property value="activity.sORCurrentRate"/>'),
                        uomFactor:'<s:property value="activity.conversionFactor"/>',                        
                        uom:'<s:property value="activity.schedule.uom.uom"/>',
                        estimatedAmount:roundTo('<s:property value="activity.rate"/>' * '<s:property value="estimatedQty"/>'),
                        contractordetails:'<s:iterator id="trqIterator3" value="tenderResponseQuotes" status="rate_status3"><s:property value="contractor.id"/>|<s:property value="quotedRate"/>|<s:property value="quotedRate"/>*<s:property value="estimatedQty"/>* <s:property value="activity.conversionFactor"/><s:if test="!#rate_status3.last">~</s:if></s:iterator> ', 
                        negotiatedRate:roundTo('<s:property value="negotiatedRate"/>'),
                        negotiatedAmount:roundTo('<s:property value="negotiatedRate"/>' * '<s:property value="estimatedQty"/>' * '<s:property value="activity.conversionFactor"/>'), 
                        marketRate:'',
                        marketRateAmount:'',                       
                        FullDescription:'<s:property value="activity.schedule.descriptionJS"/>',
                        SORDiff:''});
   		dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber('<s:property value="activity.rate"/>' * '<s:property value="estimatedQty"/>'));
   	    dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="negotiatedRate"/>' * '<s:property value="estimatedQty"/>' * '<s:property value="activity.conversionFactor"/>'));
   	    dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") );
   </s:if>
   <s:else>
   		itemRateDataTable.addRow({schId:'',
			activity:'<s:property value="activity.id"/>',
                        slNo:'<s:property value="#row_status.count"/>',
                        schNo:'',
                        schCode:'',
                        description:'<s:property value="activity.nonSor.descriptionJS" escape="false" />',
                        estimatedQuantity:'<s:property value="activity.quantity"/>',
                        estimatedQty:'<s:property value="activity.quantity"/>',
                        estimatedRate:roundTo('<s:property value="activity.rate.value"/>'),
                        uomFactor:'<s:property value="activity.conversionFactor"/>',                        
                        uom:'<s:property value="activity.nonSor.uom.uom"/>',
                        estimatedAmount:roundTo('<s:property value="activity.amount.value"/>'), 
                        contractordetails:'<s:iterator id="trqIterator4" value="tenderResponseQuotes" status="rate_status4"><s:property value="contractor.id"/>|<s:property value="quotedRate"/>|<s:property value="quotedRate"/>*<s:property value="activity.quantity"/><s:if test="!#rate_status4.last">~</s:if></s:iterator> ', 
                        negotiatedRate:roundTo('<s:property value="negotiatedRate"/>'), 
                        negotiatedAmount:roundTo('<s:property value="negotiatedRate"/>' * '<s:property value="activity.quantity"/>'),   
                        marketRate:roundTo('<s:property value="activity.rate.value"/>'),
                        marketRateAmount:roundTo('<s:property value="activity.amount.value"/>'),                    
                        FullDescription:'<s:property value="activity.nonSor.descriptionJS" escape="false" />',
                        SORDiff:''});
   		dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber('<s:property value="activity.amount.value"/>'));
   	    dom.get("negotiatedTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("negotiatedTotal") - 0.0 + getNumber('<s:property value="negotiatedRate"/>' * '<s:property value="activity.quantity"/>'));
   	    dom.get("marketRateTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("marketRateTotal") - 0.0 + getNumber('<s:property value="activity.amount.value"/>'));
   </s:else>
    var record = itemRateDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>')); 
    var column = itemRateDataTable.getColumn('schId');  
    if(dom.get(column.getKey()+record.getId()).value!=''){
    	populateSorPerDiff('<s:property value="negotiatedRate"/>','<s:property value="activity.sORCurrentRate"/>');
    }
    else
    {
    	populateSorPerDiff('<s:property value="negotiatedRate"/>','<s:property value="activity.rate.value"/>');
    }
    calculateQuotedTotalForContractors();
</s:iterator> 

</s:elseif> 

</script>  
