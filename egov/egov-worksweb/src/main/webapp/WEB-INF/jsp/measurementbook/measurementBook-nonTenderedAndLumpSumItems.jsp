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

<style type="text/css">
.yui-dt table{
  width:100%;
}

.yui-dt-col-approvedQuantity{
	text-align:right;
}
.yui-dt-col-approvedRate{
	text-align:right;
}
.yui-dt-col-prevCumlv{
	text-align:right;
}	
.yui-dt-col-quantity{
	text-align:right;
}		
.yui-dt-col-currCumlv{
	text-align:right;
}
.yui-dt-col-amount{
	text-align:right;
}
.yui-dt-col-cumlvAmt{
	text-align:right;
}
.yui-dt-col-approvedAmt{
	text-align:right;
}

</style>
<script>
var NON_TEDERED = 'nonTendered';
var LUMP_SUM = 'lumpSum';

//----HERE-----
function createActivityIDFormatter(prefix){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var activityIDFormatterNT = createActivityIDFormatter("NT");
var activityIDFormatterLS = createActivityIDFormatter("LS");

function createUOMFormatter(prefix){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    
    if(oColumn.getKey() == 'uomval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.activity.schedule.uom.uom";
    else if(oColumn.getKey() == 'woval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.workOrder.workOrderNumber";
    else if(oColumn.getKey() == 'descriptionval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.activity.schedule.description";
    if(oColumn.getKey() == 'nonsoruomval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.activity.nonSor.uom.uom";
    else if(oColumn.getKey() == 'nonsordescriptionval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.activity.nonSor.description";
    else if(oColumn.getKey() == 'prevCumlvval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"prevCumlvQuantity";
    else if(oColumn.getKey() == 'currCumlvval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"currCumlvQuantity";
    else if(oColumn.getKey() == 'amountval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"amtForCurrQuantity";
    else if(oColumn.getKey() == 'cumlvAmtval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"cumlvAmtForCurrCumlvQuantity";
    else if(oColumn.getKey() == 'approvedQuantityval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.approvedQuantity";
    else if(oColumn.getKey() == 'approvedRateval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.approvedRate";
    else if(oColumn.getKey() == 'approvedAmtval')
    	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]."+"workOrderActivity.approvedAmount";
    
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var uomFormatterNT 			= createUOMFormatter('actionNonTenderedMbDetails');
var descFormatterNT 			= createUOMFormatter('actionNonTenderedMbDetails');
var nonsoruomFormatterNT 		= createUOMFormatter('actionNonTenderedMbDetails');
var nonsordescFormatterNT 	= createUOMFormatter('actionNonTenderedMbDetails');
var approvedQuantityFormatterNT = createUOMFormatter('actionNonTenderedMbDetails');
var approvedRateFormatterNT 	= createUOMFormatter('actionNonTenderedMbDetails');
var prevCumlvFormatterNT 		= createUOMFormatter('actionNonTenderedMbDetails');
var currCumlvFormatterNT 		= createUOMFormatter('actionNonTenderedMbDetails');
var amountFormatterNT 		= createUOMFormatter('actionNonTenderedMbDetails');
var cumlvAmtvalFormatterNT 	= createUOMFormatter('actionNonTenderedMbDetails');
var approvedAmtFormatterNT 	= createUOMFormatter('actionNonTenderedMbDetails');
var woFormatterNT 		= createUOMFormatter('actionNonTenderedMbDetails');

var uomFormatterLS 			= createUOMFormatter('actionLumpSumMbDetailValues');
var descFormatterLS 			= createUOMFormatter('actionLumpSumMbDetailValues');
var nonsoruomFormatterLS 		= createUOMFormatter('actionLumpSumMbDetailValues');
var nonsordescFormatterLS 	= createUOMFormatter('actionLumpSumMbDetailValues');
var approvedQuantityFormatterLS = createUOMFormatter('actionLumpSumMbDetailValues');
var approvedRateFormatterLS 	= createUOMFormatter('actionLumpSumMbDetailValues');
var prevCumlvFormatterLS 		= createUOMFormatter('actionLumpSumMbDetailValues');
var currCumlvFormatterLS 		= createUOMFormatter('actionLumpSumMbDetailValues');
var amountFormatterLS 		= createUOMFormatter('actionLumpSumMbDetailValues');
var cumlvAmtvalFormatterLS 	= createUOMFormatter('actionLumpSumMbDetailValues');
var approvedAmtFormatterLS 	= createUOMFormatter('actionLumpSumMbDetailValues');
var woFormatterLS 		= createUOMFormatter('actionLumpSumMbDetailValues');

var activityDescFormatterLS = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>"+hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('fullDescription')))
    el.innerHTML = markup;
}
var activityDescFormatterNT = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>"+hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('fullDescription')))
    el.innerHTML = markup;
}

var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="/egworks/resources/erp2/images/help.gif"	alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>';


function createQuantityTextboxFormatter(prefix){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
		if(prefix=='actionNonTenderedMbDetails')
		{
			markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='11' maxlength='13' onblur='calculateMBOtherActivity(this,\""+oRecord.getId()+"\",\"nonTendered\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		}
		else
		{
			markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='11' maxlength='13' onblur='calculateMBOtherActivity(this,\""+oRecord.getId()+"\",\"lumpSum\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		}			  
	    
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var quantityTextboxFormatterNT = createQuantityTextboxFormatter('actionNonTenderedMbDetails');
var quantityTextboxFormatterLS = createQuantityTextboxFormatter('actionLumpSumMbDetailValues');

function createRemarkTextboxFormatter(prefix){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
		markup="<textarea class='selectmultilinewk' style='font-size:9px' id='"+id+"' name='"+fieldName+"' rows='2' cols='20'>"+value+"</textarea>";
		    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var remarkTextboxFormatterNT = createRemarkTextboxFormatter('actionNonTenderedMbDetails');
var remarkTextboxFormatterLS = createRemarkTextboxFormatter('actionLumpSumMbDetailValues');

function createUomFactorHiddenFormatter(prefix){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="";
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='5' maxlength='5' readonly='true' />";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var uomFactorHiddenFormatterNT = createUomFactorHiddenFormatter('actionNonTenderedMbDetails');
var uomFactorHiddenFormatterLS = createUomFactorHiddenFormatter('actionLumpSumMbDetailValues');

function createTextBoxFormatter(prefix){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
     markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"'  name='"+fieldName+"' size='11' onblur=\"validateOrderNumber(this)\" maxlength='8' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatterNT = createTextBoxFormatter('actionNonTenderedMbDetails');
var textboxFormatterLS = createTextBoxFormatter('actionLumpSumMbDetailValues');

var dateformatterMBDateNT = function(e2, oRecord, oColumn, oData) {
 var value = (YAHOO.lang.isValue(oData))?oData:"";
	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();

	 var markup= "<input type='text' id='"+id+"' value='"+value+"'   class='selectamountwk' size='15' maxlength='10' style=\"width:60px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this);chkDate(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
  
	
}
var dateformatterMBDateLS = function(e2, oRecord, oColumn, oData) {
	 var value = (YAHOO.lang.isValue(oData))?oData:"";
		var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." +  oColumn.getKey();
		var id = oColumn.getKey() + oRecord.getId();

		 var markup= "<input type='text' id='"+id+"' value='"+value+"'   class='selectamountwk' size='15' maxlength='10' style=\"width:60px\" name='"+fieldName 
		            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this);chkDate(this)\" />"
					+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		 e2.innerHTML = markup;
	  
		
}

//----HERE ENDS-----
//---------------------
function calculateMBOtherActivity(elem,recordId,mode){
	clearErrorMessage();
	var dataTable,totalSuffix;
	if(mode==NON_TEDERED)
	{
		dataTable = mbNTenderedDataTable;
		totalSuffix = 'NT';
	}
	else
	{
		dataTable = mbLSDataTable;
		totalSuffix = 'LS';
	}
	if(!validateNumberInTableCell(dataTable,elem,recordId)) return;
	if(!isValidQuantity(elem,recordId)){

		var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
		quantity_factor = 100 + quantity_factor; 
		errorMessage='<s:text name="measurementbook.currMBEntry.quantityFactor.error"/>' + ' ' + quantity_factor + '<s:text name="measurementbook.currMBEntry.approvedQuantity.error"/>';

		dom.get("mb_error").style.display=''; 
  		document.getElementById("mb_error").innerHTML=errorMessage;
  		dom.get('error'+elem.id).style.display=''; 
  		window.scroll(0,0);
		
	}else{
	  	if(!isValidQtyForZeroQtyFactor(elem,recordId))
		{
			 <s:if test="%{mborderNumberRequired=='Required'}">
				showOther(recordId,mode);
	          </s:if>
	          <s:else>
	          		dom.get("mb_error").style.display='';     
		    		document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.currMBEntry.error"/>';
		      		window.scroll(0,0);
		      </s:else>
		}
	  	else
		{
	        dataTable.updateCell(record,dataTable.getColumn('orderNumber'),"");
            dataTable.updateCell(record,dataTable.getColumn('mbdetailsDate'),"");
            hideOther(recordId,mode);
	        dom.get("mb_error").style.display='none'; 
            document.getElementById("mb_error").innerHTML='';
		}
	}
    oldAmount=getNumber(record.getData("amount"));
    oldCumlvAmt=getNumber(record.getData("cumlvAmt")); 
    dataTable.updateCell(record,dataTable.getColumn('currCumlv'),getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
    if(dom.get('uomFactor'+recordId).value!='') {
    	dataTable.updateCell(record,dataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("approvedRate"))));
    	dataTable.updateCell(record,dataTable.getColumn('cumlvAmt'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
    }
    else{
    	dataTable.updateCell(record,dataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(record.getData("approvedRate"))));
    	dataTable.updateCell(record,dataTable.getColumn('cumlvAmt'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(record.getData("currCumlv"))));    
    }
        
    dataTable.updateCell(record,dataTable.getColumn('currCumlvval'),roundTo(getNumber(elem.value)+getNumber(record.getData("prevCumlv"))));
    if(dom.get('uomFactor'+recordId).value!='') {
    	dataTable.updateCell(record,dataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("approvedRate"))));
    	dataTable.updateCell(record,dataTable.getColumn('cumlvAmtval'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
    }
    else {
     	 dataTable.updateCell(record,dataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(record.getData("approvedRate"))));
    	 dataTable.updateCell(record,dataTable.getColumn('cumlvAmtval'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(record.getData("currCumlv"))));
    }
    
    dom.get("amtTotal"+totalSuffix).innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal"+totalSuffix) - oldAmount + getNumber(record.getData("amount")));
	dom.get("culmvAmtTotal"+totalSuffix).innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal"+totalSuffix) - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
	calculateMBTotal();
	return false;
}

function clearCumulativeDataIfCancelledNTLS(){
	
	var records = mbNTenderedDataTable.getRecordSet();
	var record ;
	for(i=0;i<records.getLength();i++){ 
		record = records.getRecord(i);
		mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('prevCumlv'),'-NA-');
	    mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('prevCumlvval'),'');
		mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('currCumlv'),'-NA-');
	    mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('currCumlvval'),'');
	    mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('cumlvAmt'),'-NA-');
	    mbNTenderedDataTable.updateCell(record,mbNTenderedDataTable.getColumn('cumlvAmtval'),'');
	}
	dom.get("culmvAmtTotalNT").innerHTML='';
	
	records = mbLSDataTable.getRecordSet();
	for(i=0;i<records.getLength();i++){ 
		record = records.getRecord(i);
		mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('prevCumlv'),'-NA-');
	    mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('prevCumlvval'),'');
		mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('currCumlv'),'-NA-');
	    mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('currCumlvval'),'');
	    mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('cumlvAmt'),'-NA-');
	    mbLSDataTable.updateCell(record,mbLSDataTable.getColumn('cumlvAmtval'),'');
	}
	dom.get("culmvAmtTotalLS").innerHTML='';
	return;
}

function recalculateTotalsOnDeleteNTLS(record, mode){
	var totalSuffix;
	if(mode==NON_TEDERED)
	{
		totalSuffix = 'NT';
	}
	else
	{
		totalSuffix = 'LS';
	}
	dom.get("amtTotal"+totalSuffix).innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal"+totalSuffix) - getNumber(record.getData("amount")));
	dom.get("culmvAmtTotal"+totalSuffix).innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal"+totalSuffix) - getNumber(record.getData("cumlvAmt")));
	document.getElementById("mbAmount").value=roundTo(eval(document.getElementById("amtTotal").innerHTML)+eval(document.getElementById("amtTotalNT").innerHTML)+eval(document.getElementById("amtTotalLS").innerHTML));
}

function disableEnableOther(mode){
	var dataTable;
	if(mode==NON_TEDERED)
	{
		dataTable = mbNTenderedDataTable;
	}
	else
	{
		dataTable = mbLSDataTable;
	}
	hideOther('',mode);
	var records = dataTable.getRecordSet();
	<s:if test="%{mborderNumberRequired=='Required'}">
		for(var i=0;i<records.getLength();i++){ 
			var record = records.getRecord(i);
			<s:if test="%{sourcepage=='inbox' || mode=='view'}">
			if(isValidQuantityCheckNew(record.getData("quantity"),record,mode)){
			    var cumulv_quant_including_currentry;
				cumulv_quant_including_currentry = roundTo(getNumber(record.getData("quantity"))+getNumber(record.getData("prevCumlv")));
				var approved_quantity = getNumber(record.getData("approvedQuantity"));
				if(eval(cumulv_quant_including_currentry)>eval(approved_quantity)){
					showOther(record.getId(),mode);
				}	
	  	 	}
	   		</s:if>
	   		<s:else>
			   hideOther('',mode);
			   if(isValidQuantityCheckNew(record.getData("quantity"),record,mode)){
				    var cumulv_quant_including_currentry;
					cumulv_quant_including_currentry = roundTo(getNumber(record.getData("quantity"))+getNumber(record.getData("prevCumlv")));
					var approved_quantity = getNumber(record.getData("approvedQuantity"));
					if(eval(cumulv_quant_including_currentry)>eval(approved_quantity)){
						showOther(record.getId(),mode);
					}
			   }
			   else{
			        hideOther(record.getId(),mode);
			   }
		  </s:else>
  	}
	</s:if> 
}

var mbNTenderedDataTable;
var makeMBNonTendetedTable = function() {
	var mbColumns = [
	                 {key:"workOrderActivity", hidden:true,formatter:activityIDFormatterNT,sortable:false, resizeable:false},
	                 {key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
	                 {key:"description" ,label:'<s:text name="measurementbook.itemdesc"/>', formatter:activityDescFormatterNT,sortable:false, resizeable:false},
	                 {key:"uom", label:'<s:text name="measurementbook.uom"/>', sortable:false, resizeable:false},
	                 {key:"approvedQuantity",label:'<s:text name="measurementbook.approved.quantity"/>', sortable:false, resizeable:false},
	                 {key:"approvedRate",label:'<s:text name="measurementbook.sor.rate"/>', sortable:false, resizeable:false},
	                 {key:"quantity",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.entry"/>', formatter:quantityTextboxFormatterNT, sortable:false, resizeable:false},
	                 {key:"currCumlv",label:'<s:text name="measurementbook.cumul.current.entry"/>', sortable:false, resizeable:false},
	                 {key:"amount",label:'<s:text name="measurementbook.current.entry.amount"/>', sortable:false, resizeable:false},
	                 {key:"cumlvAmt",label:'<s:text name="measurementbook.cumul.amount"/>', sortable:false, resizeable:false},
	                 {key:"orderNumber",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.ordernumbe"/>',hidden:false, formatter:textboxFormatterNT, sortable:false, resizeable:false},
	                 {key:"mbdetailsDate", label:'<span class="mandatory">*</span>Order Date',formatter:dateformatterMBDateNT,sortable:false, resizeable:false},
	                 {key:"approvedAmt",label:'<s:text name="measurementbook.approved.amount"/>', sortable:false, resizeable:false},
	                 {key:"remarks",label:'<s:text name="measurementbook.remark"/>', formatter:remarkTextboxFormatterNT, sortable:false, resizeable:true},
	                 {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
	                 {key:"descriptionval" ,hidden:true,formatter:descFormatterNT,sortable:false, resizeable:false},
	                 {key:"nonsordescriptionval" ,hidden:true,formatter:nonsordescFormatterNT,sortable:false, resizeable:false},
	                 {key:"fullDescription",hidden:true,sortable:false, resizeable:false},
	                 {key:"uomFactor",label:'Unit Rate', hidden:true,  formatter:uomFactorHiddenFormatterNT, sortable:false, resizeable:false},
	                 {key:"uomval", hidden:true,formatter:uomFormatterNT,sortable:false, resizeable:false},
	                 {key:"woval", hidden:true,formatter:woFormatterNT,sortable:false, resizeable:false},
	                 {key:"nonsoruomval", hidden:true,formatter:nonsoruomFormatterNT,sortable:false, resizeable:false},
	                 {key:"approvedQuantityval" ,hidden:true,formatter:approvedQuantityFormatterNT,sortable:false, resizeable:false},
	                 {key:"approvedRateval" ,hidden:true,formatter:approvedRateFormatterNT,sortable:false, resizeable:false},
	                 {key:"prevCumlvval" ,hidden:true,formatter:prevCumlvFormatterNT,sortable:false, resizeable:false},
	                 {key:"currCumlvval" ,hidden:true,formatter:currCumlvFormatterNT,sortable:false, resizeable:false},
	                 {key:"amountval" ,hidden:true,formatter:amountFormatterNT,sortable:false, resizeable:false},
	                 {key:"cumlvAmtval" ,hidden:true,formatter:cumlvAmtvalFormatterNT,sortable:false, resizeable:false},
	                 {key:"prevCumlv",hidden:true, sortable:false, resizeable:false},
	                 {key:"approvedAmtval" ,hidden:true,formatter:approvedAmtFormatterNT,sortable:false, resizeable:false}
	                ];

        var mbDataSource = new YAHOO.util.DataSource();
        mbNTenderedDataTable = new YAHOO.widget.DataTable("mbNTenderedTable",
                mbColumns, mbDataSource,{MSG_EMPTY:"<s:text name='measurementbook.initial.table.message'/>"});
        mbNTenderedDataTable.subscribe("cellClickEvent", mbNTenderedDataTable.onEventShowCellEditor);
         
         mbNTenderedDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Delete') { 
                recalculateTotalsOnDeleteNTLS(record,NON_TEDERED);
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });
        
        var tfoot = mbNTenderedDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan = 7;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		var td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'subTotal';
		td.innerHTML = '<span class="bold">Total:</span>';
		addCell(tr,2,'amtTotalNT','0.00');
		addCell(tr,3,'culmvAmtTotalNT','0.00');
		var th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 9;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';
    };
    
var mbLSDataTable;
var makeMBLumpSumTable = function() {
	var mbColumns = [
	                 {key:"workOrderActivity", hidden:true,formatter:activityIDFormatterLS,sortable:false, resizeable:false},
	                 {key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
	                 {key:"description" ,label:'<s:text name="measurementbook.itemdesc"/>', formatter:activityDescFormatterLS,sortable:false, resizeable:false},
	                 {key:"uom", label:'<s:text name="measurementbook.uom"/>', sortable:false, resizeable:false},
	                 {key:"approvedQuantity",label:'<s:text name="measurementbook.approved.quantity"/>', sortable:false, resizeable:false},
	                 {key:"approvedRate",label:'<s:text name="measurementbook.unit.rate"/>', sortable:false, resizeable:false},
	                 {key:"quantity",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.entry"/>', formatter:quantityTextboxFormatterLS, sortable:false, resizeable:false},
	                 {key:"currCumlv",label:'<s:text name="measurementbook.cumul.current.entry"/>', sortable:false, resizeable:false},
	                 {key:"amount",label:'<s:text name="measurementbook.current.entry.amount"/>', sortable:false, resizeable:false},
	                 {key:"cumlvAmt",label:'<s:text name="measurementbook.cumul.amount"/>', sortable:false, resizeable:false},
	                 {key:"orderNumber",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.ordernumbe"/>',hidden:false, formatter:textboxFormatterLS, sortable:false, resizeable:false},
	                 {key:"mbdetailsDate", label:'<span class="mandatory">*</span>Order Date',formatter:dateformatterMBDateLS,sortable:false, resizeable:false},
	                 {key:"approvedAmt",label:'<s:text name="measurementbook.approved.amount"/>', sortable:false, resizeable:false},
	                 {key:"remarks",label:'<s:text name="measurementbook.remark"/>', formatter:remarkTextboxFormatterLS, sortable:false, resizeable:true},
	                 {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
	                 {key:"descriptionval" ,hidden:true,formatter:descFormatterLS,sortable:false, resizeable:false},
	                 {key:"nonsordescriptionval" ,hidden:true,formatter:nonsordescFormatterLS,sortable:false, resizeable:false},
	                 {key:"fullDescription",hidden:true,sortable:false, resizeable:false},
	                 {key:"uomFactor",label:'Unit Rate', hidden:true,  formatter:uomFactorHiddenFormatterLS, sortable:false, resizeable:false},
	                 {key:"uomval", hidden:true,formatter:uomFormatterLS,sortable:false, resizeable:false},
	                 {key:"woval", hidden:true,formatter:woFormatterLS,sortable:false, resizeable:false},
	                 {key:"nonsoruomval", hidden:true,formatter:nonsoruomFormatterLS,sortable:false, resizeable:false},
	                 {key:"approvedQuantityval" ,hidden:true,formatter:approvedQuantityFormatterLS,sortable:false, resizeable:false},
	                 {key:"approvedRateval" ,hidden:true,formatter:approvedRateFormatterLS,sortable:false, resizeable:false},
	                 {key:"prevCumlvval" ,hidden:true,formatter:prevCumlvFormatterLS,sortable:false, resizeable:false},
	                 {key:"currCumlvval" ,hidden:true,formatter:currCumlvFormatterLS,sortable:false, resizeable:false},
	                 {key:"amountval" ,hidden:true,formatter:amountFormatterLS,sortable:false, resizeable:false},
	                 {key:"cumlvAmtval" ,hidden:true,formatter:cumlvAmtvalFormatterLS,sortable:false, resizeable:false},
	                 {key:"prevCumlv",hidden:true, sortable:false, resizeable:false},
	                 {key:"approvedAmtval" ,hidden:true,formatter:approvedAmtFormatterLS,sortable:false, resizeable:false}
	                ];

        var mbDataSource = new YAHOO.util.DataSource();
        mbLSDataTable = new YAHOO.widget.DataTable("mbLSTable",
                mbColumns, mbDataSource,{MSG_EMPTY:"<s:text name='measurementbook.initial.table.message'/>"});
        mbLSDataTable.subscribe("cellClickEvent", mbLSDataTable.onEventShowCellEditor);
        
         mbLSDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Delete') { 
            	recalculateTotalsOnDeleteNTLS(record,LUMP_SUM);
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });
        
        var tfoot = mbLSDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan = 7;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		var td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'subTotal';
		td.innerHTML = '<span class="bold">Total:</span>';
		addCell(tr,2,'amtTotalLS','0.00');
		addCell(tr,3,'culmvAmtTotalLS','0.00');
		var th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 9;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';
    }
    
function addOtherActivity(mode){
	workorder_no = document.getElementById('workOrderNumber').value;
	workorder_id = document.getElementById('workOrder').value;
	estimate_id = document.getElementById('estimateId').value;
	if(workorder_id=='-1' || workorder_id==''){
		dom.get("mb_error").style.display=''; 
        document.getElementById("mb_error").innerHTML='<s:text name="mbheader.workorder.null" />';
        window.scroll(0,0);
		return false;
	}
	else if(estimate_id=='' || estimate_id=='-1'){
			dom.get("mb_error").style.display=''; 
            document.getElementById("mb_error").innerHTML='<s:text name="mbheader.workOrderEstimate.null" />';  
			window.scroll(0,0);
			return false;
	}
	if(mode==NON_TEDERED)
	 window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!loadSerachForActivity.action?activitySearchMode=nonTendered&workorderNo="+workorder_no+"&workOrderId="+workorder_id+"&estimateId="+estimate_id,"",
	 			"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
	else
		window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!loadSerachForActivity.action?activitySearchMode=lumpSum&workorderNo="+workorder_no+"&workOrderId="+workorder_id+"&estimateId="+estimate_id,"",
			"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");	
}

function updateOtherActivity(elemValue,mode) {
	var isError=false;
	if(elemValue!="" || elemValue!=null || elemValue.length!=0) {
		var i;
		var dataTable;
		if(mode==NON_TEDERED)
			dataTable = mbNTenderedDataTable;
		else
			dataTable = mbLSDataTable;		 
		for(i=0;i<elemValue.length;i++) {
			isError=false;	
			var row = elemValue[i];
			var a=row.split("~");
			var activity_id=a[0];
			var workorder_id=a[1];
			// This validation is not required as the workorderid and estimate id are from revision estimate and workorder
			/*if(workorder_id!=document.getElementById('workOrder').value){
				dom.get("mb_error").style.display='block';
				dom.get("mb_error").innerHTML='<s:text name="measurementbook.activity.wo.invalid"/>';
				window.scroll(0,0);
				return false;
			}*/

			if(activity_id!=null && workorder_id!=null){
				var j;
				for(j=0;j<dataTable.getRecordSet().getLength() && activity_id!='';j++){
						if(activity_id==dataTable.getRecordSet().getRecord(j).getData('workOrderActivity')){
		    	       		//commented for simply skipping duplicate entries
		    	       		//dom.get("mb_error").style.display='block';
							//dom.get("mb_error").innerHTML='<s:text name="measurementbook.activity.duplicate"/>';
							window.scroll(0,0);
		    	       		isError=true;
		    	       		break;
		    	       }
		    	       else {
		    	       		isError=false;
		    	       }
	    	    }
				if(!isError) {
					setupOtherActivity(activity_id,workorder_id,mode);
				}
			}
		}
	}
}

function setupOtherActivity(activity_id,workorder_id, mode){

    dom.get("mb_error").style.display='none';
	dom.get("mb_error").innerHTML='';
	var mbHeaderVal = $F('measurementBookForm_id');
	var modelType = "MB";
	if(mbHeaderVal == null || mbHeaderVal == '')
		mbHeaderVal = -1;
	if(mode==NON_TEDERED)
	{
		makeJSONCall(["xActId","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","activityRemarks"], 
		    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!activityDetails.action',
		    	{woActivityId:activity_id,workOrderId:workorder_id,mbHeaderId:mbHeaderVal,modelType:modelType},myNTActSuccessHandler,myNTActFailureHandler) ;
	}
	else
	{
		makeJSONCall(["xActId","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","activityRemarks"], 
		    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!activityDetails.action',
		    	{woActivityId:activity_id,workOrderId:workorder_id,mbHeaderId:mbHeaderVal,modelType:modelType},myLSActSuccessHandler,myLSActFailureHandler) ;
	}			
    
	
}
myNTActSuccessHandler = function(req,res){
    var resultdatas=res.results;
	 {
		addOtherActivityInYUI(resultdatas,NON_TEDERED);
     }
};

myNTActFailureHandler= function(){
    dom.get("mb_error").style.display='block';
	dom.get("mb_error").innerHTML='Unable to load activity details';
};

myLSActSuccessHandler = function(req,res){
    var resultdatas=res.results;
	 {
		addOtherActivityInYUI(resultdatas,LUMP_SUM);
     }
};

myLSActFailureHandler= function(){
    dom.get("mb_error").style.display='block';
	dom.get("mb_error").innerHTML='Unable to load activity details';
};

function addOtherActivityInYUI(resultdatas,mode){
	if(mode==NON_TEDERED)
	{
		if(resultdatas[0].xSORUOM!=''){
			mbNTenderedDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
                    SlNo:mbNTenderedDataTable.getRecordSet().getLength()+1,
                    description:resultdatas[0].xSORSummary,
                    descriptionval:resultdatas[0].xSORSummary,
			        nonsordescriptionval:'',
                    uom:resultdatas[0].xSORUOM,
                    uomFactor:resultdatas[0].xUomFactor,
                    uomval:resultdatas[0].xSORUOM,
                    woval:resultdatas[0].xWorkOrderNo,
                    nonsoruomval:'',
                    approvedQuantity:roundTo(resultdatas[0].xApprdQunty),
                    approvedQuantityval:resultdatas[0].xApprdQunty,
                    approvedRate:roundTo(resultdatas[0].xApprdRate),
                    approvedRateval:resultdatas[0].xApprdRate,
                    prevCumlv:resultdatas[0].xPrevCulmvEntry,
                    prevCumlvval:resultdatas[0].xPrevCulmvEntry,
                    approvedAmt:roundTo(eval(resultdatas[0].xApprdQunty)*eval(resultdatas[0].xApprdRate)*eval(resultdatas[0].xUomFactor)),
                    approvedAmtval:eval(eval(resultdatas[0].xApprdQunty)*eval(resultdatas[0].xApprdRate)*eval(resultdatas[0].xUomFactor)),                 
                    fullDescription:resultdatas[0].xSORDesc,
                    remarks:resultdatas[0].activityRemarks});
   		}
	}
	else
	{
		if(resultdatas[0].xNonSORUOM!=''){
			mbLSDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
                    SlNo:mbLSDataTable.getRecordSet().getLength()+1,
                    description:resultdatas[0].xNonSORSummary,
                    descriptionval:'',
                    nonsordescriptionval:resultdatas[0].xNonSORSummary,
                    uom:resultdatas[0].xNonSORUOM,
                    uomFactor:resultdatas[0].xUomFactor,
                    uomval:'',
                    woval:resultdatas[0].xWorkOrderNo,
                    nonsoruomval:resultdatas[0].xNonSORUOM,
                    approvedQuantity:roundTo(resultdatas[0].xApprdQunty),
                    approvedQuantityval:resultdatas[0].xApprdQunty,
                    approvedRate:roundTo(resultdatas[0].xApprdRate),
                    approvedRateval:resultdatas[0].xApprdRate,
					prevCumlv:resultdatas[0].xPrevCulmvEntry,
					prevCumlvval:resultdatas[0].xPrevCulmvEntry,
	                approvedAmt:roundTo(eval(resultdatas[0].xApprdQunty)*eval(resultdatas[0].xApprdRate)*eval(resultdatas[0].xUomFactor)),
	                approvedAmtval:eval(eval(resultdatas[0].xApprdQunty)*eval(resultdatas[0].xApprdRate)*eval(resultdatas[0].xUomFactor)),                   
                    fullDescription:resultdatas[0].xNonSORDesc,
                    remarks:resultdatas[0].activityRemarks});
   		}
	}		
}
    
function resetMBNTDataTable(){
	mbNTenderedDataTable.deleteRows(0,mbNTenderedDataTable.getRecordSet().getLength());
	dom.get("amtTotalNT").innerHTML="0.00";
	dom.get("culmvAmtTotalNT").innerHTML="0.00";
}

function resetMBLSDataTable(){
	mbNTenderedDataTable.deleteRows(0,mbLSDataTable.getRecordSet().getLength());
	dom.get("amtTotalLS").innerHTML="0.00";
	dom.get("culmvAmtTotalLS").innerHTML="0.00";
}

function hideOther(recordId,mode){
	var dataTable;
	if(mode==NON_TEDERED)
	{
		dataTable = mbNTenderedDataTable;
	}
	else
	{
		dataTable = mbLSDataTable;
	}	
	var value=dataTable.getColumn('orderNumber');
	var index=value.getIndex();
	var value2=dataTable.getColumn('mbdetailsDate');
	var index1=value2.getIndex();
	dataTable.hideColumn(parseInt(index));
	dataTable.hideColumn(parseInt(index1));
	var records = dataTable.getRecordSet();
	var tbLen = records.getLength();
	if(recordId!=''){
	  for(var i=0;i<tbLen;i++){  
	   	var record = records.getRecord(i);
			var cumulv_quant_including_currentry = roundTo(getNumber(dom.get('quantity'+record.getId()).value)+getNumber(record.getData("prevCumlvval")));
			var approved_quantity = getNumber(record.getData("approvedQuantityval"));
			if(!(eval(cumulv_quant_including_currentry)<=eval(approved_quantity))){
				showOther(records.getRecord(i),mode);
	   			dom.get('orderNumber'+record.getId()).readOnly=false;
				dom.get('mbdetailsDate'+record.getId()).readOnly=false;
			}else{
				dom.get('orderNumber'+record.getId()).readOnly=true;
				dom.get('mbdetailsDate'+record.getId()).readOnly=true;
			}
		  }
	   }
	};
	
	function showOther(recordId,mode){
		var dataTable;
		if(mode==NON_TEDERED)
		{
			dataTable = mbNTenderedDataTable;
		}
		else
		{
			dataTable = mbLSDataTable;
		}
		var value=dataTable.getColumn('orderNumber');
		var index=value.getIndex();
		var value2=dataTable.getColumn('mbdetailsDate');
		var index1=value2.getIndex();
		dataTable.showColumn(parseInt(index));
		dataTable.showColumn(parseInt(index1));
		var records = dataTable.getRecordSet();
			for(i=0;i<records.getLength();i++){
				var record = records.getRecord(i);
				if(record.getId()==recordId){
					dom.get('orderNumber'+record.getId()).readOnly=false;
					dom.get('mbdetailsDate'+record.getId()).readOnly=false;
				}else{
					dom.get('orderNumber'+record.getId()).readOnly=true;
					dom.get('mbdetailsDate'+record.getId()).readOnly=true;
				}
				
		   }
	};
	function calculateMBTotal()
	{
		var records = mbDataTable.getRecordSet();
		var record, column, amountTotal;
		amountTotal= 0;
		for(var i=0;i<records.getLength();i++){
			record = mbDataTable.getRecord(i);
			amountTotal = amountTotal+ getNumber(record.getData("amount"));
		}
		
		if(mbNTenderedDataTable!=null && mbNTenderedDataTable.getRecordSet()!=null )
		{
			records = mbNTenderedDataTable.getRecordSet();
			if(records.getLength()>0)
			{
				for(var i=0;i<records.getLength();i++){
					record = mbNTenderedDataTable.getRecord(i);								
					amountTotal = amountTotal+ getNumber(record.getData("amount"));
				}		
			}	
		}
		if(mbLSDataTable!=null && mbLSDataTable.getRecordSet()!=null )
		{
			records = mbLSDataTable.getRecordSet();
			if(records.getLength()>0)
			{
				for(var i=0;i<records.getLength();i++){
					record = mbLSDataTable.getRecord(i);
					amountTotal = amountTotal+ getNumber(record.getData("amount"));
				}		
			}	
		}
		document.getElementById("mbAmount").value =roundTo( amountTotal);
	}
    
</script>
<table id="mbDetailsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="/egworks/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer"><s:text name="measurementbook.nontendered.details"/></div>
					</td>
					<td align="right" class="headingwk" style="border-left-width: 0px">
                		<a href="#" onclick="addOtherActivity('nonTendered');">
                		<img height="16" border="0" width="16" alt="Add Non-SOR" src="/egworks/resources/erp2/images/add.png" />
                		</a>
                	</td>
				</tr>
				<tr>
					<td colspan="7">
						<div class="yui-skin-sam">
							<div id="mbNTenderedTable"></div> 
						</div>
						<script>
			                makeMBNonTendetedTable();
			                var record;
			                var rowCount=0;
			                <s:iterator id="mbIterator" value="mbDetails" status="row_status">
			                <s:if test="%{workOrderActivity.activity.schedule && workOrderActivity.activity.schedule.uom.uom != ''}">
			                	if('<s:property value="workOrderActivity.activity.revisionType"/>'=='NON_TENDERED_ITEM')
				                {
			                		mbNTenderedDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:''+(rowCount+1),
				                        description:'<s:property value="workOrderActivity.activity.schedule.summaryJS"/>',
				                        descriptionval:'<s:property value="workOrderActivity.activity.schedule.summaryJS"/>',
				                        nonsordescriptionval:'',
				                        uom:'<s:property value="workOrderActivity.activity.schedule.uom.uom"/>',
				                        uomval:'<s:property value="workOrderActivity.activity.schedule.uom.uom"/>',
				                        uomFactor:'<s:property value="workOrderActivity.conversionFactor"/>',
				                        woval:'<s:property value="workOrderActivity.workOrder.workOrderNumber"/>',
				                        nonsoruomval:'',
				                        quantity:'<s:property value="quantity"/>',
				                        orderNumber:'<s:property value="orderNumber"/>',
                                        <s:if test="%{mbdetailsDate!=null}">
                                        mbdetailsDate: '<s:text name="format.date"><s:param value="%{mbdetailsDate}"/></s:text>',
                                        </s:if>
                                        <s:else>
                                        mbdetailsDate: '<s:property value="mbdetailsDate"/>',
                                        </s:else>
				                        remarks:'<s:property value="remarks"/>',
				                        prevCumlv:'<s:property value="prevCumlvQuantity"/>',
				                        prevCumlvval:'<s:property value="prevCumlvQuantity"/>',
				                        currCumlv:'<s:property value="currCumlvQuantity"/>',
				                        currCumlvval:'<s:property value="currCumlvQuantity"/>',
				                         <s:if test="%{workOrderActivity.conversionFactor!=null && !hasErrors()}">
					                        amount:roundTo('<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.conversionFactor"/>'),
					                        amountval:'<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.conversionFactor"/>',
					                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.conversionFactor"/>'),
					                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.conversionFactor"/>',
					                        approvedAmt:roundTo('<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="workOrderActivity.conversionFactor"/>'*'<s:property value="totalEstQuantity"/>'),
					                        approvedAmtval:'<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="workOrderActivity.conversionFactor"/>'*'<s:property value="totalEstQuantity"/>',
					                     </s:if>
					                     <s:elseif test="%{hasErrors()}">
					                       amount:roundTo('<s:property value="amtForCurrQuantity"/>'),
					                        amountval:'<s:property value="amtForCurrQuantity"/>',
					                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'),
					                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>',
					                        approvedAmt:roundTo('<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="totalEstQuantity"/>'),
					                        approvedAmtval:'<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="totalEstQuantity"/>',
					                     </s:elseif>
				                        approvedQuantity:roundTo('<s:property value="totalEstQuantity"/>'),
				                        approvedQuantityval:'<s:property value="totalEstQuantity"/>',
				                        approvedRate:roundTo('<s:property value="workOrderActivity.approvedRate"/>'),
				                        approvedRateval:'<s:property value="workOrderActivity.approvedRate"/>',
				                        fullDescription:'<s:property value="workOrderActivity.activity.schedule.descriptionJS"/>'});
									    record = mbNTenderedDataTable.getRecord(rowCount);										
				          				dom.get("amtTotalNT").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotalNT") + 
				          								getNumber(record.getData("amount")));
										dom.get("culmvAmtTotalNT").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotalNT") + 
														getNumber(record.getData("cumlvAmt")));
										rowCount++;
					            }	
							</s:if>
							</s:iterator>
			        	</script>
					</td>
				</tr>
				<tr>
					<td colspan="7" class="shadowwk"></td>
				</tr>
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="/egworks/resources/erp2/images/arrow.gif" />
						</div>
						<div class="headplacer"><s:text name="measurementbook.lumpsum.details"/></div>
					</td>
					<td align="right" class="headingwk" style="border-left-width: 0px">
                		<a href="#" onclick="addOtherActivity('lumpSum');">
                		<img height="16" border="0" width="16" alt="Add Non-SOR" src="/egworks/resources/erp2/images/add.png" />
                		</a>
                	</td>
				</tr>
				<tr>
					<td colspan="7">
						<div class="yui-skin-sam">
							<div id="mbLSTable"></div> 
						</div>
						<script>
						makeMBLumpSumTable();
						var record;
		                var rowCount=0;
		                <s:iterator id="mbIterator" value="mbDetails" status="row_status">
	          			<s:if test="%{workOrderActivity.activity.nonSor && workOrderActivity.activity.nonSor.uom.uom != '' }">
		          			if('<s:property value="workOrderActivity.activity.revisionType"/>'=='LUMP_SUM_ITEM')
			                {
		          				mbLSDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:''+(rowCount+1),
				                        description:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>',
				                        descriptionval:'',
				                        nonsordescriptionval:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>',
				                        uom:'<s:property value="workOrderActivity.activity.nonSor.uom.uom"/>',
				                        uomval:'',
				                        uomFactor:'<s:property value="workOrderActivity.conversionFactor"/>',
				                        woval:'<s:property value="workOrderActivity.workOrder.workOrderNumber"/>',
				                        nonsoruomval:'<s:property value="workOrderActivity.activity.nonSor.uom.uom"/>',
				                        quantity:'<s:property value="quantity"/>',
				                        orderNumber:'<s:property value="orderNumber"/>',
	                                    <s:if test="%{mbdetailsDate!=null}">
	                                    mbdetailsDate: '<s:text name="format.date"><s:param value="%{mbdetailsDate}"/></s:text>',
	                                    </s:if>
	                                    <s:else>
	                                    mbdetailsDate: '<s:property value="mbdetailsDate"/>',
	                                    </s:else>
				                        remarks:'<s:property value="remarks"/>',
				                        prevCumlv:'<s:property value="prevCumlvQuantity"/>',
				                        prevCumlvval:'<s:property value="prevCumlvQuantity"/>',
				                        currCumlv:'<s:property value="currCumlvQuantity"/>',
				                        currCumlvval:'<s:property value="currCumlvQuantity"/>',
				                        amount:roundTo('<s:property value="amtForCurrQuantity"/>'),
				                        amountval:'<s:property value="amtForCurrQuantity"/>',
				                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'),
				                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>',
				                        approvedQuantity:roundTo('<s:property value="totalEstQuantity"/>'),
				                        approvedQuantityval:'<s:property value="totalEstQuantity"/>',
				                        approvedRate:roundTo('<s:property value="workOrderActivity.approvedRate"/>'),
				                        approvedRateval:'<s:property value="workOrderActivity.approvedRate"/>',
				                        approvedAmt:roundTo('<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="totalEstQuantity"/>'),
				                        approvedAmtval:'<s:property value="workOrderActivity.approvedRate"/>'*'<s:property value="totalEstQuantity"/>',
				                        fullDescription:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>'});
				          				record = mbLSDataTable.getRecord(rowCount);										
				          				dom.get("amtTotalLS").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotalLS") + 
				          								getNumber(record.getData("amount")));
										dom.get("culmvAmtTotalLS").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotalLS") + 
														getNumber(record.getData("cumlvAmt")));
										rowCount++;
			                }	
	          			</s:if>
						</s:iterator>
			        	</script>
					</td>
				</tr>
				<tr>
					<td colspan="7" class="shadowwk"></td>
				</tr>
			</table>
		</td>
	</tr>
 	<tr>
 		<td>&nbsp;</td>
 	</tr>
</table>

