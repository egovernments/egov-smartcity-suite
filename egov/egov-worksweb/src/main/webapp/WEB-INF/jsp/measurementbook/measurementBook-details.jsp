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

function createActivityIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var activityIDFormatter = createActivityIDFormatter(10,10);

function createUOMFormatter(el, oRecord, oColumn){
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
var uomFormatter 			= createUOMFormatter(10,10);
var descFormatter 			= createUOMFormatter(10,10);
var nonsoruomFormatter 		= createUOMFormatter(10,10);
var nonsordescFormatter 	= createUOMFormatter(10,10);
var approvedQuantityFormatter = createUOMFormatter(10,10);
var approvedRateFormatter 	= createUOMFormatter(10,10);
var prevCumlvFormatter 		= createUOMFormatter(10,10);
var currCumlvFormatter 		= createUOMFormatter(10,10);
var amountFormatter 		= createUOMFormatter(10,10);
var cumlvAmtvalFormatter 	= createUOMFormatter(10,10);
var approvedAmtFormatter 	= createUOMFormatter(10,10);
var woFormatter 		= createUOMFormatter(10,10);

var activityDescFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>"+hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('fullDescription')))
    el.innerHTML = markup;
}

var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="/egworks/resources/erp2/images/help.gif"	alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>';

function createQuantityTextboxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
	  
	    markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+
	    	"' onblur='calculateMB(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var quantityTextboxFormatter = createQuantityTextboxFormatter(11,13);

function checkCalMB() {
	var result = true;
	
}
function clearErrorMessage()
{
	var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
	quantity_factor = 100 + quantity_factor;
	var errMsg = '<s:text name="measurementbook.currMBEntry.quantityFactor.error"/>' + ' ' + quantity_factor + '<s:text name="measurementbook.currMBEntry.approvedQuantity.error"/>';
	if(document.getElementById("mb_error").innerHTML==errMsg)
	{
        dom.get("mb_error").style.display='none'; 
        document.getElementById("mb_error").innerHTML='';
	}	
}

function calculateMB(elem,recordId){
	var rcEstimate = dom.get("isRCEstimate").value;
	clearErrorMessage();
	if(!validateNumberInTableCell(mbDataTable,elem,recordId)) return;
	if(!isValidQuantity(elem,recordId)){
		var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
		quantity_factor = 100 + quantity_factor; 
		if(rcEstimate=='yes'){
			errorMessage='<s:text name="measurementbook.currMBEntry.quantityFactor.rcEstimate.errorMsg1"/>' + ' ' + quantity_factor + '<s:text name="measurementbook.currMBEntry.quantityFactor.rcEstimate.errorMsg2"/>';
		}
		else{
			errorMessage='<s:text name="measurementbook.currMBEntry.quantityFactor.error"/>' + ' ' + quantity_factor + '<s:text name="measurementbook.currMBEntry.approvedQuantity.error"/>';
		}
		
		dom.get("mb_error").style.display=''; 
  		document.getElementById("mb_error").innerHTML=errorMessage;
  		dom.get('error'+elem.id).style.display=''; 
  		window.scroll(0,0);
	}else{
		  	if(!isValidQtyForZeroQtyFactor(elem,recordId))
			{
				 <s:if test="%{mborderNumberRequired=='Required'}">
		            show(recordId);
		          </s:if>
		          <s:else>
		          		dom.get("mb_error").style.display='';     
			    		document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.currMBEntry.error"/>';
			      		window.scroll(0,0);
			      </s:else>
			}
		  	else
			{
		  		mbDataTable.updateCell(record,mbDataTable.getColumn('orderNumber'),"");
	            mbDataTable.updateCell(record,mbDataTable.getColumn('mbdetailsDate'),"");
		        hide(recordId);
		        dom.get("mb_error").style.display='none'; 
	            document.getElementById("mb_error").innerHTML='';
			}  	   
	}
    oldAmount=getNumber(record.getData("amount"));
    oldCumlvAmt=getNumber(record.getData("cumlvAmt")); 
    mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlv'),getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
    if(dom.get('uomFactor'+recordId).value!='') {
    	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("approvedRate"))));
    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
    }
    else{
    	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(record.getData("approvedRate"))));
    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(record.getData("currCumlv"))));    
    }
        
    mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlvval'),roundTo(getNumber(elem.value)+getNumber(record.getData("prevCumlv"))));
    if(dom.get('uomFactor'+recordId).value!='') {
    	mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("approvedRate"))));
    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
    }
    else {
     	 mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(record.getData("approvedRate"))));
    	 mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(getNumber(record.getData("approvedRate"))*getNumber(record.getData("currCumlv"))));
    }
    
    dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - oldAmount + getNumber(record.getData("amount")));
	dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
	calculateMBTotal();
	return false;
}

function clearCumulativeDataIfCancelled(){
	var records = mbDataTable.getRecordSet();
	for(i=0;i<records.getLength();i++){ 
		var record = records.getRecord(i);
		mbDataTable.updateCell(record,mbDataTable.getColumn('prevCumlv'),'-NA-');
	    mbDataTable.updateCell(record,mbDataTable.getColumn('prevCumlvval'),'');
		mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlv'),'-NA-');
	    mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlvval'),'');
	    mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),'-NA-');
	    mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),'');
	}
	dom.get("culmvAmtTotal").innerHTML='';
	return;
}

function isValidQuantity(elem,recordId){
	var cumulv_quant_including_currentry;
	cumulv_quant_including_currentry = roundTo(getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
	var approved_quantity = getNumber(record.getData("approvedQuantity"));
	var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
	if(quantity_factor>0){
		quantity_factor=quantity_factor/100;
		approved_quantity =approved_quantity+(quantity_factor*approved_quantity)
	}
	if(eval(cumulv_quant_including_currentry)<=eval(approved_quantity)){
		return true;
	}
	else{
		return false;
	}
}

function isValidQtyForZeroQtyFactor(elem,recordId){
	var cumulv_quant_including_currentry;
	cumulv_quant_including_currentry = roundTo(getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
	var approved_quantity = getNumber(record.getData("approvedQuantity"));
	if(eval(cumulv_quant_including_currentry)<=eval(approved_quantity)){
		return true;
	}
	else{
		return false;
	}
}

function isValidQuantityCheck(elem,recordId){
	var cumulv_quant_including_currentry = roundTo(getNumber(elem)+getNumber(record.getData("prevCumlv")));
	var approved_quantity = getNumber(record.getData("approvedQuantity"));
	var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
	if(quantity_factor>0){
		quantity_factor=quantity_factor/100;
		approved_quantity =approved_quantity+(quantity_factor*approved_quantity)
	}
	if(eval(cumulv_quant_including_currentry)<=eval(approved_quantity)){
		return true;
	}
	else{
		return false;
	}
}


function isValidQuantityCheckNew(elem,recordObj){
	var cumulv_quant_including_currentry = roundTo(getNumber(elem)+getNumber(recordObj.getData("prevCumlv")));
	var approved_quantity = getNumber(recordObj.getData("approvedQuantity"));
	var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
	if(quantity_factor>0){
		quantity_factor=quantity_factor/100;
		approved_quantity =approved_quantity+(quantity_factor*approved_quantity)
	}
	if(eval(cumulv_quant_including_currentry)<=eval(approved_quantity)){
		return true;
	}
	else{
		return false;
	}
}

function recalculateTotalsOnDelete(record){
	dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - getNumber(record.getData("amount")));
	dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") - getNumber(record.getData("cumlvAmt")));
	document.getElementById("mbAmount").value=roundTo(eval(document.getElementById("amtTotal").innerHTML)+eval(document.getElementById("amtTotalNT").innerHTML)+eval(document.getElementById("amtTotalLS").innerHTML));
}

function createRemarkTextboxFormatter(rows,cols){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
		markup="<textarea class='selectmultilinewk' style='font-size:9px' id='"+id+"' name='"+fieldName+"' rows='"+rows+"' cols='"+cols+"'>"+value+"</textarea>";
		    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var remarkTextboxFormatter = createRemarkTextboxFormatter(2,20);

function createUomFactorHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="";
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var uomFactorHiddenFormatter = createUomFactorHiddenFormatter(5,5);

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
     markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"'  name='"+fieldName+"' size='"+size+"' onblur=\"validateOrderNumber(this)\" maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(11,8);

var dateformatterMBDate = function(e2, oRecord, oColumn, oData) {
 var value = (YAHOO.lang.isValue(oData))?oData:"";
	var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();

	 var markup= "<input type='text' id='"+id+"' value='"+value+"'   class='selectamountwk' size='15' maxlength='10' style=\"width:60px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this);chkDate(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
  
	
}

function disableenable(){
	hide('');
	var records = mbDataTable.getRecordSet();
	<s:if test="%{mborderNumberRequired=='Required'}">
		for(var i=0;i<records.getLength();i++){ 
			var record = records.getRecord(i);
			<s:if test="%{sourcepage=='inbox' || mode=='view'}">
				// Less than 125% and greater than 100
				if(isValidQuantityCheckNew(record.getData("quantity"),record) ){
					var cumulv_quant_including_currentry;
					cumulv_quant_including_currentry = roundTo(getNumber(record.getData("quantity"))+getNumber(record.getData("prevCumlv")));
					var approved_quantity = getNumber(record.getData("approvedQuantity"));
					if(eval(cumulv_quant_including_currentry)>eval(approved_quantity)){
						show(record.getId());					
					}	
		  	 	}
	   		</s:if>
	   		<s:else>
		   hide();
		   if(isValidQuantityCheckNew(record.getData("quantity"),record) ){
				// Less than 125% and greater than 100
			    var cumulv_quant_including_currentry;
				cumulv_quant_including_currentry = roundTo(getNumber(record.getData("quantity"))+getNumber(record.getData("prevCumlv")));
				var approved_quantity = getNumber(record.getData("approvedQuantity"));
				if(eval(cumulv_quant_including_currentry)>eval(approved_quantity)){
					show(record.getId());					
				}
		   }
		   else{
		        hide(record.getId());
		  	}
		  </s:else>
  	}
	</s:if> 
}

function hide(recordId){
var value=mbDataTable.getColumn('orderNumber');
var index=value.getIndex();
var value2=mbDataTable.getColumn('mbdetailsDate');
var index1=value2.getIndex();
mbDataTable.hideColumn(parseInt(index));
mbDataTable.hideColumn(parseInt(index1));
var records = mbDataTable.getRecordSet();
var tbLen = records.getLength();
if(recordId!=''){
  for(var i=0;i<tbLen;i++){  
   	var record = records.getRecord(i);
		var cumulv_quant_including_currentry = roundTo(getNumber(dom.get('quantity'+record.getId()).value)+getNumber(record.getData("prevCumlv")));
		var approved_quantity = getNumber(record.getData("approvedQuantity"));
		if(!(eval(cumulv_quant_including_currentry)<=eval(approved_quantity))){
				show(records.getRecord(i));
	   			dom.get('orderNumber'+record.getId()).readOnly=false;
				dom.get('mbdetailsDate'+record.getId()).readOnly=false;
		}else{
				dom.get('orderNumber'+record.getId()).readOnly=true;
				dom.get('mbdetailsDate'+record.getId()).readOnly=true;
			}	
		}
	  }
};
function show(recordId){
var value=mbDataTable.getColumn('orderNumber');
var index=value.getIndex();
var value2=mbDataTable.getColumn('mbdetailsDate');
var index1=value2.getIndex();
mbDataTable.showColumn(parseInt(index));
mbDataTable.showColumn(parseInt(index1));
var records = mbDataTable.getRecordSet();
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


var mbDataTable;
var makeMBDataTable = function() {
        var mbColumns = [
            {key:"workOrderActivity", hidden:true,formatter:activityIDFormatter,sortable:false, resizeable:false},
            {key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
            {key:"description" ,label:'<s:text name="measurementbook.itemdesc"/>', formatter:activityDescFormatter,sortable:false, resizeable:false},
            {key:"descriptionval" ,hidden:true,formatter:descFormatter,sortable:false, resizeable:false},
            {key:"nonsordescriptionval" ,hidden:true,formatter:nonsordescFormatter,sortable:false, resizeable:false},
            {key:"fullDescription",hidden:true,sortable:false, resizeable:false},
            {key:"uom", label:'<s:text name="measurementbook.uom"/>', sortable:false, resizeable:false},
            {key:"uomFactor",label:'Unit Rate', hidden:true,  formatter:uomFactorHiddenFormatter, sortable:false, resizeable:false},
            {key:"uomval", hidden:true,formatter:uomFormatter,sortable:false, resizeable:false},
            {key:"woval", hidden:true,formatter:woFormatter,sortable:false, resizeable:false},
            {key:"nonsoruomval", hidden:true,formatter:nonsoruomFormatter,sortable:false, resizeable:false},
            {key:"approvedQuantity",label:'<s:text name="measurementbook.approved.quantity"/>', sortable:false, resizeable:false},
            {key:"approvedQuantityval" ,hidden:true,formatter:approvedQuantityFormatter,sortable:false, resizeable:false},
            {key:"approvedRate",label:'<s:text name="measurementbook.approved.rate"/>', sortable:false, resizeable:false},
            {key:"approvedRateval" ,hidden:true,formatter:approvedRateFormatter,sortable:false, resizeable:false},
            {key:"prevCumlv",label:'<s:text name="measurementbook.cumul.prev.entry"/>', sortable:false, resizeable:false},
            {key:"prevCumlvval" ,hidden:true,formatter:prevCumlvFormatter,sortable:false, resizeable:false},
            {key:"quantity",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.entry"/>', formatter:quantityTextboxFormatter, sortable:false, resizeable:false},
            {key:"currCumlv",label:'<s:text name="measurementbook.cumul.current.entry"/>', sortable:false, resizeable:false},
            {key:"amount",label:'<s:text name="measurementbook.current.entry.amount"/>', sortable:false, resizeable:false},
            {key:"cumlvAmt",label:'<s:text name="measurementbook.cumul.amount"/>', sortable:false, resizeable:false},
            {key:"currCumlvval" ,hidden:true,formatter:currCumlvFormatter,sortable:false, resizeable:false},
            {key:"amountval" ,hidden:true,formatter:amountFormatter,sortable:false, resizeable:false},
            {key:"cumlvAmtval" ,hidden:true,formatter:cumlvAmtvalFormatter,sortable:false, resizeable:false},            
            {key:"approvedAmt",label:'<s:text name="measurementbook.approved.amount"/>', sortable:false, resizeable:false},
            {key:"approvedAmtval" ,hidden:true,formatter:approvedAmtFormatter,sortable:false, resizeable:false},
            {key:"orderNumber",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.ordernumbe"/>',hidden:false, formatter:textboxFormatter, sortable:false, resizeable:false},
            {key:"mbdetailsDate", label:'<span class="mandatory">*</span>Order Date',formatter:dateformatterMBDate,sortable:false, resizeable:false},
            {key:"remarks",label:'<s:text name="measurementbook.remark"/>', formatter:remarkTextboxFormatter, sortable:false, resizeable:true},
            {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
           ];

        var mbDataSource = new YAHOO.util.DataSource();
        mbDataTable = new YAHOO.widget.DataTable("mbTable",
                mbColumns, mbDataSource,{MSG_EMPTY:"<s:text name='measurementbook.initial.table.message'/>"});
        mbDataTable.subscribe("cellClickEvent", mbDataTable.onEventShowCellEditor);
        
         mbDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Delete') { 
                recalculateTotalsOnDelete(record)
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });
        
        var tfoot = mbDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan = 18;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		var td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'subTotal';
		td.innerHTML = '<span class="bold">Total:</span>';
		addCell(tr,2,'amtTotal','0.00');
		addCell(tr,3,'culmvAmtTotal','0.00');
		var th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 9;
		th1.className= 'whitebox4wk';
		th1.innerHTML = '&nbsp;';
    }
    
function resetMBDataTable(){
	mbDataTable.deleteRows(0,mbDataTable.getRecordSet().getLength());
	dom.get("amtTotal").innerHTML="0.00";
	dom.get("culmvAmtTotal").innerHTML="0.00";
}
    
function addActivity(){
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
	window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!loadSerachForActivity.action?activitySearchMode=tendered&workorderNo="+workorder_no+"&workOrderId="+workorder_id+"&estimateId="+estimate_id,"",
		"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
}

function updateActivity(elemValue) {
	var isError=false;
	if(elemValue!="" || elemValue!=null || elemValue.length!=0) {
		var i;
		for(i=0;i<elemValue.length;i++) {
			isError=false;	
			var row = elemValue[i];
			var a=row.split("~");
			var activity_id=a[0];
			var workorder_id=a[1];
			if(workorder_id!=document.getElementById('workOrder').value){
				dom.get("mb_error").style.display='block';
				dom.get("mb_error").innerHTML='<s:text name="measurementbook.activity.wo.invalid"/>';
				window.scroll(0,0);
				return false;
			}

			if(activity_id!=null && workorder_id!=null){
				var j;
				for(j=0;j<mbDataTable.getRecordSet().getLength() && activity_id!='';j++){
	    	       if(activity_id==mbDataTable.getRecordSet().getRecord(j).getData('workOrderActivity')){
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
					setupActivityDetails(activity_id,workorder_id);
				}
			}
		}
	}
}

function setupActivityDetails(activity_id,workorder_id){

    dom.get("mb_error").style.display='none';
	dom.get("mb_error").innerHTML='';
	var mbHeaderVal = $F('measurementBookForm_id');
	var modelType = "MB";
	if(mbHeaderVal == null || mbHeaderVal == '')
		mbHeaderVal = -1;
    makeJSONCall(["xActId","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","activityRemarks"], 
    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!activityDetails.action',
    	{woActivityId:activity_id,workOrderId:workorder_id,mbHeaderId:mbHeaderVal,modelType:modelType},myActSuccessHandler,myActFailureHandler) ;
	
}

myActSuccessHandler = function(req,res){
    var resultdatas=res.results;
    	
	// Add SOR Activity
	/*if(mbDataTable.getRecordSet().getLength()>0){
	  for(i=0;i<mbDataTable.getRecordSet().getLength();i++){
         if(resultdatas[0].xActId!=mbDataTable.getRecordSet().getRecord(i).getData('workOrderActivity')){
    		addActivityInYUI(resultdatas)
        }
      }
	}
	else*/{
    	addActivityInYUI(resultdatas)
    }
}
function addActivityInYUI(resultdatas){
	if(resultdatas[0].xSORUOM!=''){
				mbDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
                        SlNo:mbDataTable.getRecordSet().getLength()+1,
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
                        remark:resultdatas[0].activityRemarks});
       }
     // Add Non-SOR Activity
       if(resultdatas[0].xNonSORUOM!=''){
     			mbDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
                        SlNo:mbDataTable.getRecordSet().getLength()+1,
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
                        approvedAmt:roundTo(resultdatas[0].xApprdAmt),
                        approvedAmtval:resultdatas[0].xApprdAmt,
                        fullDescription:resultdatas[0].xNonSORDesc,
                        remark:resultdatas[0].activityRemarks});
        }
}
myActFailureHandler= function(){
    dom.get("mb_error").style.display='block';
	dom.get("mb_error").innerHTML='Unable to load activity details';
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
						<div class="headplacer"><s:text name="measurementbook.details"/></div>
					</td>
					<td align="right" class="headingwk" style="border-left-width: 0px">
                		<a href="#" onclick="addActivity();">
                		<img height="16" border="0" width="16" alt="Add Non-SOR" src="/egworks/resources/erp2/images/add.png" />
                		</a>
                	</td>
				</tr>
				<tr>
					<td colspan="7">
						<s:hidden id="quantityFactor" name="quantityFactor" />
						<div class="yui-skin-sam">
							<div id="mbTable"></div> 
						</div>
						<script>
			                makeMBDataTable();
			                var record;
			                var rowCount=0;
			                <s:iterator id="mbIterator" value="mbDetails" status="row_status">
			                <s:if test="%{workOrderActivity.activity.schedule && workOrderActivity.activity.schedule.uom.uom != '' && workOrderActivity.activity.revisionType==null}">
							    mbDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:''+(rowCount+1),
				                        description:'<s:property value="workOrderActivity.activity.schedule.summaryJS" escape="false"/>',
				                        descriptionval:'<s:property value="workOrderActivity.activity.schedule.summaryJS" escape="false"/>',
				                        nonsordescriptionval:'',
				                        uom:'<s:property value="workOrderActivity.activity.schedule.uom.uom"/>',
				                        uomval:'<s:property value="workOrderActivity.activity.schedule.uom.uom"/>',
				                        uomFactor:'<s:property value="workOrderActivity.activity.conversionFactor"/>',
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
				                         <s:if test="%{workOrderActivity.activity.conversionFactor!=null && !hasErrors()}">
					                        amount:roundTo('<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>'),
					                        amountval:'<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>',
					                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>'),
					                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>',
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
				                        fullDescription:'<s:property value="workOrderActivity.activity.schedule.descriptionJS" escape="false" />'});
									    record = mbDataTable.getRecord(rowCount);										
				          				dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") + 
				          								getNumber(record.getData("amount")));
										dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") + 
														getNumber(record.getData("cumlvAmt")));
										rowCount++;
							</s:if>
		          			<s:elseif test="%{workOrderActivity.activity.nonSor && workOrderActivity.activity.nonSor.uom.uom != ''  && workOrderActivity.activity.revisionType==null}">
		          				mbDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:''+(rowCount+1),
				                        description:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>',
				                        descriptionval:'',
				                        nonsordescriptionval:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>',
				                        uom:'<s:property value="workOrderActivity.activity.nonSor.uom.uom"/>',
				                        uomval:'',
				                        uomFactor:'<s:property value="workOrderActivity.activity.conversionFactor"/>',
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
				          				record = mbDataTable.getRecord(rowCount);										
				          				dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") + 
				          								getNumber(record.getData("amount")));
										dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") + 
														getNumber(record.getData("cumlvAmt")));
										rowCount++;
		          			</s:elseif>
								
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

