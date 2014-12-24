	
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

.yui-skin-sam th.yui-dt-col-mbMSheetDtls{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-mbMSheetDtls{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-quantity .yui-dt-liner{
	margin-left: -40px;
	align-text: left;
} 

.yui-skin-sam td.yui-dt-col-quantity .yui-dt-liner{
	margin-left: -10px;
} 


</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>

function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\r\n/g, "<br>");
	return str2;
}

var mbdHeaderDtl_MSheet=new Array();
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
var isExtraItemFormatter= createUOMFormatter(10,10);

var activityDescFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>"+hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('fullDescription')))
    el.innerHTML = markup;
}

var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png"	alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>';

var temp=createPageEditFormatter("${pageContext.request.contextPath}");

function createQuantityTextboxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+
	    	"' onblur='calculateMB(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML =markup;
	}
	return textboxFormatter;
}
var quantityTextboxFormatter = createQuantityTextboxFormatter(11,13);

function checkCalMB() {
	var result = true;
}


function calculateMB(elem,recordId){
	dom.get('error'+elem.id).style.display='none';
	if(elem.value=='' || trim(elem.value)=='') {
		dom.get('error'+elem.id).style.display='';
		return false; 
	}
    var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
    var errorMessage;
    
    var record=mbDataTable.getRecord(recordId);
    var approved_quantity = getNumber(record.getData("approvedQuantity"));
    
    if(approved_quantity>0){
	    if(quantity_factor > 0){
		  quantity_factor = 100 + quantity_factor; 
		  errorMessage='<s:text name="measurementbook.currMBEntry.quantityFactor.error"/>' + ' ' + quantity_factor + '<s:text name="measurementbook.currMBEntry.approvedQuantity.error"/>';
		 }else{
		  errorMessage='<s:text name="measurementbook.currMBEntry.error"/>';
		 }
	 }
	 
	if(!validateNumberInTableCell(mbDataTable,elem,recordId)) return;
	if(elem.value!='') {
		if(!isValidQuantity(elem,recordId)){
			 <s:if test="%{mborderNumberRequired=='Required'}">
	            show(recordId);
	          </s:if>
	          <s:else>
	          		dom.get("mb_error").style.display=''; 
	          		document.getElementById("mb_error").innerHTML=errorMessage;
	          		dom.get('error'+elem.id).style.display=''; 
		      		window.scroll(0,0);
		      		
		      		return;
		      </s:else>
		   }else{
			 <s:if test="%{mborderNumberRequired=='Required'}">
		        mbDataTable.updateCell(record,mbDataTable.getColumn('orderNumber'),"");
	            mbDataTable.updateCell(record,mbDataTable.getColumn('mbdetailsDate'),"");
		        hide(recordId);
			</s:if>
		        dom.get("mb_error").style.display='none'; 
	            document.getElementById("mb_error").innerHTML='';
	       }
	    oldAmount=getNumber(record.getData("amount"));
	    oldCumlvAmt=getNumber(record.getData("cumlvAmt"));
	    var approvedRate=getNumber(record.getData("approvedRate"));
	    if(getNumber(dom.get('reducedRate'+recordId).value)>0 && dom.get('isReducedRatereducedRate'+recordId).checked==true){
	    	approvedRate=dom.get('reducedRate'+recordId).value;
	    }
	    
	    mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlv'),getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
	    if(dom.get('uomFactor'+recordId).value!='') {
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*approvedRate));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(approvedRate*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
	    }
	    else{
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*approvedRate));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(approvedRate*getNumber(record.getData("currCumlv"))));    
	    }
	        
	    mbDataTable.updateCell(record,mbDataTable.getColumn('currCumlvval'),getNumber(elem.value)+getNumber(record.getData("prevCumlv")));
	    if(dom.get('uomFactor'+recordId).value!='') {
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*approvedRate));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(approvedRate*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
	    }
	    else {
	     	 mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*approvedRate));
	    	 mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(approvedRate*getNumber(record.getData("currCumlv"))));
	    }

	    if(dom.get('isExtraItem'+recordId).value=='true') {
		    dom.get("extraItemsAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsAmtTotal") - oldAmount + getNumber(record.getData("amount")));
			dom.get("extraItemsCulmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsCulmvAmtTotal") - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
		}
		else {
			 dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - oldAmount + getNumber(record.getData("amount")));
			 dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
		}
		updateTotalValue();
	}
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
	var cumulv_quant_including_currentry = getNumber(elem.value)+getNumber(record.getData("prevCumlv"));
	var approved_quantity = getNumber(record.getData("approvedQuantity"));
	var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
	if(quantity_factor>0){
		quantity_factor=quantity_factor/100;
		approved_quantity =approved_quantity+(quantity_factor*approved_quantity)
	}
	if(approved_quantity==0){
		return true;
	}
	else{
		if(eval(cumulv_quant_including_currentry)<=eval(approved_quantity)){
			return true;
		}
		else{
			return false;
		}
	}	
}

function isValidQuantityCheck(elem,recordId){
	var cumulv_quant_including_currentry = getNumber(elem)+getNumber(record.getData("prevCumlv"));
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
	var cumulv_quant_including_currentry = getNumber(elem)+getNumber(recordObj.getData("prevCumlv"));
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

function recalculateMBTotalsOnDelete(record){
 	if(record.getData('isExtraItem')=='true') {
	    dom.get("extraItemsAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsAmtTotal") - getNumber(record.getData("amount")));
		dom.get("extraItemsCulmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsCulmvAmtTotal") - getNumber(record.getData("cumlvAmt")));
	}
 	else {
		dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - getNumber(record.getData("amount")));
		dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") - getNumber(record.getData("cumlvAmt")));
 	}

}

function calculateMBTotal(elem,recordId){
dom.get('error'+elem.id).style.display='none';
    if(elem.readOnly==true) return;
    dom.get("mb_error").style.display='none'; 
	document.getElementById("mb_error").innerHTML='';
	if(!validateNumberInTableCell(mbDataTable,elem,recordId)) return;
	
	if(elem.value!='' && getNumber(elem.value)>0){
		<s:if test='%{mbAmtAboveApprovedAmt=="false"}'>
			if(getNumber(elem.value)>=getNumber(record.getData("approvedRate"))){
				dom.get("mb_error").style.display=''; 
	          	document.getElementById("mb_error").innerHTML="Please enter New Rate lesser than Approved Rate";
	          	dom.get('error'+elem.id).style.display='';
		      	window.scroll(0,0);
		      	return;
			}
		</s:if>
	}

	if(elem.value!='' && elem.id=='reducedRate'+recordId && getNumber(dom.get('reducedRate'+recordId).value)>0 && dom.get('isReducedRatereducedRate'+recordId).checked==true) {
	    oldAmount=getNumber(record.getData("amount"));
	    oldCumlvAmt=getNumber(record.getData("cumlvAmt"));
	    if(dom.get('uomFactor'+recordId).value!='') {
	      	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(dom.get('quantity'+recordId).value)));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
	    }
	    else{
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('amount'),roundTo(getNumber(elem.value)*getNumber(dom.get('quantity'+recordId).value)));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmt'),roundTo(getNumber(elem.value)*getNumber(record.getData("currCumlv"))));    
	    }
	        
	    if(dom.get('uomFactor'+recordId).value!='') {
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(dom.get('quantity'+recordId).value)));
	    	mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(getNumber(elem.value)*getNumber(dom.get('uomFactor'+recordId).value)*getNumber(record.getData("currCumlv"))));
	    }
	    else {
	     	 mbDataTable.updateCell(record,mbDataTable.getColumn('amountval'),roundTo(getNumber(elem.value)*getNumber(dom.get('quantity'+recordId).value)));
	    	 mbDataTable.updateCell(record,mbDataTable.getColumn('cumlvAmtval'),roundTo(getNumber(elem.value)*getNumber(record.getData("currCumlv"))));
	    }
	    
	    if(record.getData('isExtraItem')=='true') {
		    dom.get("extraItemsAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsAmtTotal") - oldAmount + getNumber(record.getData("amount")));
			dom.get("extraItemsCulmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsCulmvAmtTotal") - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
		}
	    else {
	    	dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") - oldAmount + getNumber(record.getData("amount")));
			dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") - oldCumlvAmt + getNumber(record.getData("cumlvAmt")));
	    }
		
		updateTotalValue();
	}
	return false;
	
}
function updateTotalValue(){
	<s:if test='%{mbPercentagelevel=="TotalMBValue" && isTenderPercentageType==true}'>
		var recordsa = mbDataTable.getRecordSet();
		var extraItemTotal=0;
		var totalWithoutExtraItem=0;
		for(i=0;i<recordsa.getLength();i++){ 
			var recorda = recordsa.getRecord(i);
			var recordIda=recorda.getId();
			
			if(dom.get('isExtraItem'+recordIda).value=="true"){
				extraItemTotal=extraItemTotal+getNumber(recorda.getData("amount"));
			}
			else{
				totalWithoutExtraItem=totalWithoutExtraItem+getNumber(recorda.getData("amount"));
			}
		}
		 var recordsb = extraItemsDataTable.getRecordSet();
		 for(i=0;i<recordsb.getLength();i++){ 
		 	var recordb = recordsb.getRecord(i);
				var recordIdb=recordb.getId();
				
				if(dom.get('isExtraItemCQ'+recordIdb).value=="true"){
					extraItemTotal=extraItemTotal+getNumber(recordb.getData("MbAmt"));
				}
				else{
					totalWithoutExtraItem=totalWithoutExtraItem+getNumber(recordb.getData("MbAmt"));
				}
		 }
		
		var percentAppliedTotal;
		
		var tenderPercent=eval(document.getElementById("tenderPercentage").value);
		var sign=document.getElementById("sign").value;
		if(sign=="+"){
			percentAppliedTotal=eval(totalWithoutExtraItem)+(eval(totalWithoutExtraItem)*tenderPercent/100)+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML);
	  	      //  document.getElementById("mbValue").value=roundTo(eval(document.getElementById("amtTotal").innerHTML)+(eval(document.getElementById("amtTotal").innerHTML)*tenderPercent/100)+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML)+eval(document.getElementById("extraItemsMbTotal").innerHTML)+(eval(document.getElementById("extraItemsMbTotal").innerHTML)*tenderPercent/100));
		}
	  	else{	//tenderPercent=tenderPercent * -1;
	  			percentAppliedTotal=eval(totalWithoutExtraItem)-(eval(totalWithoutExtraItem)*tenderPercent/100)+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML);
	  	      //   document.getElementById("mbValue").value=roundTo((eval(document.getElementById("amtTotal").innerHTML)-(eval(document.getElementById("amtTotal").innerHTML)*tenderPercent/100))+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML)+(eval(document.getElementById("extraItemsMbTotal").innerHTML)-(eval(document.getElementById("extraItemsMbTotal").innerHTML)*tenderPercent/100)));
	     }
	    	percentAppliedTotal=percentAppliedTotal+extraItemTotal;
		 
			document.getElementById("mbValue").value=roundTo(percentAppliedTotal);
		</s:if>
		<s:else>
			document.getElementById("mbValue").value=roundTo(eval(document.getElementById("amtTotal").innerHTML)+eval(document.getElementById("extraItemsAmtTotal").innerHTML)+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML)+eval(document.getElementById("extraItemsMbTotal").innerHTML));
		</s:else>
}

function checkRate(obj,recordId,objId){
var isPartRateName="isPartRate"+objId;
var isReducedRateName="isReducedRate"+objId;
	if(obj.name==isPartRateName && obj.checked==true){
		document.getElementById(objId).removeAttribute('readonly');
		var tb="reducedRate"+recordId;
		var cb="isReducedRate"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		var crossmark="errorreducedRate"+recordId;
		document.getElementById(crossmark).style.display='none';
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.name==isReducedRateName && obj.checked==true){
 		document.getElementById(objId).removeAttribute('readonly');
 		var tb="partRate"+recordId;
		var cb="isPartRate"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		var crossmark="errorpartRate"+recordId;
		document.getElementById(crossmark).style.display='none';
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.checked==false){
		document.getElementById(objId).value='0.0';
		var crossmark="errorreducedRate"+recordId;
		var crossmark1="errorpartRate"+recordId;
		document.getElementById(crossmark1).style.display='none';
		document.getElementById(crossmark).style.display='none';
		document.getElementById(objId).setAttribute('readonly', true);
		var qtyElem="quantity"+recordId;
		calculateMB(document.getElementById(qtyElem),recordId)
	}
}

function createPartRateFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
    var checkBoxId="isPartRate"+id;
    markup="<input type='checkbox' id='"+checkBoxId+"' name='"+checkBoxId+"' onclick=\"checkRate(this,'"+oRecord.getId()+"','"+id+"')\" /><input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true'  onblur='calculateMBTotal(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var partRateFormatter=createPartRateFormatter(4,20);

function createReducedRateFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
    var checkBoxId="isReducedRate"+id;
    markup="<input type='checkbox' id='"+checkBoxId+"' name='"+checkBoxId+"' onclick=\"checkRate(this,'"+oRecord.getId()+"','"+id+"')\" /><input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' ' onblur='calculateMBTotal(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var reducedRateFormatter=createReducedRateFormatter(4,20);


function createRemarkTextboxFormatter(rows,cols){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionMbDetailValues[" + oRecord.getCount() + "]." + oColumn.getKey();
		markup="<textarea class='selectmultilinewk' id='"+id+"' name='"+fieldName+"' rows='"+rows+"' cols='"+cols+"'>"+value+"</textarea>";
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
		for(i=0;i<records.getLength();i++){ 
			var record = records.getRecord(i);
			<s:if test="%{sourcepage=='inbox' || mode=='view'}">
			if(!isValidQuantityCheckNew(record.getData("quantity"),record)){
				show(record.getId());
	  	 	}
	   		</s:if>
	   		<s:else>
		   hide();
		   if(!isValidQuantityCheckNew(record.getData("quantity"),record)){
		    	show(record.getId());
		   }
		   else{
		        hide(record.getId());
		  	}
		  </s:else>
  	}
	</s:if> 
}

function enablePartandReducedRate(){
var records = mbDataTable.getRecordSet();
var tbLen = records.getLength();

  for(var i=0;i<tbLen;i++){  
   	var record = records.getRecord(i);
		var partRate= getNumber(dom.get('partRate'+record.getId()).value);
		if(partRate>0){
			var isPartRatecb="isPartRatepartRate"+record.getId();
			document.getElementById(isPartRatecb).checked=true;
			document.getElementById('partRate'+record.getId()).removeAttribute('readonly');
		}
		var reducedRate= getNumber(dom.get('reducedRate'+record.getId()).value);
		if(reducedRate>0){
			var isReducedRatecb="isReducedRatereducedRate"+record.getId();
			document.getElementById(isReducedRatecb).checked=true;
			document.getElementById('reducedRate'+record.getId()).removeAttribute('readonly');
		}
	}

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
		var cumulv_quant_including_currentry = getNumber(dom.get('quantity'+record.getId()).value)+getNumber(record.getData("prevCumlv"));
		var approved_quantity = getNumber(record.getData("approvedQuantity"));
		var quantity_factor = getNumber(document.getElementById("quantityFactor").value);
		if(quantity_factor>0){
		quantity_factor=quantity_factor/100;
		approved_quantity =approved_quantity+(quantity_factor*approved_quantity)
		}
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
            {key:"woMsheetSize",hidden:true,sortable:false, resizeable:false},
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
          	{key:"mbMSheetDtls",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"quantity",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.entry"/>', formatter:quantityTextboxFormatter, sortable:false, resizeable:false},
            {key:"currCumlv",label:'<s:text name="measurementbook.cumul.current.entry"/>', sortable:false, resizeable:false},
            {key:"amount",label:'<s:text name="measurementbook.current.entry.amount"/>', sortable:false, resizeable:false},
            {key:"cumlvAmt",label:'<s:text name="measurementbook.cumul.amount"/>', sortable:false, resizeable:false},
            {key:"currCumlvval" ,hidden:true,formatter:currCumlvFormatter,sortable:false, resizeable:false},
            {key:"amountval" ,hidden:true,formatter:amountFormatter,sortable:false, resizeable:false},
            {key:"cumlvAmtval" ,hidden:true,formatter:cumlvAmtvalFormatter,sortable:false, resizeable:false},   
            {key:"partRate" ,label:'<s:text name="measurementbook.partRate"/>',formatter:partRateFormatter,sortable:false, resizeable:false},
            {key:"reducedRate" ,label:'<s:text name="measurementbook.reducedRate"/>',formatter:reducedRateFormatter,sortable:false, resizeable:false},
            {key:"approvedAmt",label:'<s:text name="measurementbook.approved.amount"/>', sortable:false, resizeable:false},
            {key:"approvedAmtval" ,hidden:true,formatter:approvedAmtFormatter,sortable:false, resizeable:false},
            {key:"isExtraItem" ,hidden:true,formatter:isExtraItemFormatter,sortable:false, resizeable:false},
            {key:"orderNumber",label:'<span class="mandatory">*</span><s:text name="measurementbook.mb.ordernumbe"/>',hidden:false, formatter:textboxFormatter, sortable:false, resizeable:false},
            {key:"mbdetailsDate", label:'<span class="mandatory">*</span>Order Date',formatter:dateformatterMBDate,sortable:false, resizeable:false},
            {key:"remark",label:'<s:text name="measurementbook.remark"/>', formatter:remarkTextboxFormatter, sortable:false, resizeable:true},
            {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
           ];

        var mbDataSource = new YAHOO.util.DataSource();
        mbDataTable = new YAHOO.widget.DataTable("mbTable",
                mbColumns, mbDataSource,{MSG_EMPTY:"<s:text name='measurementbook.initial.table.message'/>"});
         
         mbDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
           	dom.get("woActivityId").value=record.getData("workOrderActivity"); 
         	dom.get("mbDtlsRecId").value=record.getId();
            <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
            if (column.key == 'Delete') {  
                recalculateMBTotalsOnDelete(record);
                removeDetails(dom.get("woActivityId").value);
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                	updateTotalValue();
            }
            </s:if>
            
            if(column.key=='mbMSheetDtls'){
    			var approved_quantity = getNumber(record.getData("approvedQuantity"));
            
               if(record.getData("woMsheetSize")==0 && approved_quantity!=0) {
                       alert("No Work Order Measurement details found for this Activity"); 
               }
               else{          
		          	var woActivityId=record.getData("workOrderActivity");
		          	var mbHeaderId=dom.get('id').value;
		         	makeJSONCall(["estimateNo","revisionType","no","Id","uomLength","width","depthOrHeight","identifier","quantity","curNo","curLength","curWidth","curDH","curQuantity","curCumQuantity"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!getMbMSheetDetails.action',{woActivityId:woActivityId,mbHeaderId:mbHeaderId},estimateMSheetLoadHandler,estimateMSheetLoadFailureHandler);
	          	}
	       }
	       
	       if(column.key=='quantity'){
	       	if(record.getData("woMsheetSize")!=0) {
	       		dom.get("quantity"+record.getId()).disabled=true;
	       		dom.get("quantity"+record.getId()).readOnly=true; 
	       	}
	       }
        });
        
        var tfoot = mbDataTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('td'));
		th.colSpan =19;
		th.className= 'whitebox4wk';
		th.innerHTML = '&nbsp;';

		var td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'subTotal';
		td.colSpan =2;
		td.innerHTML = '<span class="bold">Total:<br>(Excluding Extra Items)</span>';
		td.setAttribute("border-right",0);
		addCell(tr,2,'amtTotal','0.00');
		addCell(tr,3,'culmvAmtTotal','0.00');
		var th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 12;
		th1.className= 'whitebox4wk'; 
		th1.innerHTML = '&nbsp;';

		tfoot = mbDataTable.getTbodyEl().parentNode.createTFoot();
		tr = tfoot.insertRow(-1);
		th = tr.appendChild(document.createElement('td'));
		th.colSpan =19;
		th.className= 'whitebox4wk'; 
		th.innerHTML = '&nbsp;';

		td = tr.insertCell(1);
		td.className= 'whitebox4wk';
		td.id = 'extraItemsSubTotal';
		td.colSpan =2;
		td.innerHTML = '<span class="bold">Extra Items Total:</span>';
		td.setAttribute("border-right",0);
		addCell(tr,2,'extraItemsAmtTotal','0.00');
		addCell(tr,3,'extraItemsCulmvAmtTotal','0.00');
		th1 = tr.appendChild(document.createElement('td'));
		th1.colSpan = 12;
		th1.className= 'whitebox4wk';   
		th1.innerHTML = '&nbsp;';
    }
  
estimateMSheetLoadHandler = function(req,res) {
	dom.get("mb_error").style.display='none';
	dom.get("mb_error").innerHTML='';

	var detailRec=mbDataTable.getRecord(dom.get("mbDtlsRecId").value); 
    var approved_quantity = getNumber(detailRec.getData("approvedQuantity"));
	
	if(approved_quantity>0){
		mbMSheetDataTable.deleteRows(0,mbMSheetDataTable.getRecordSet().getLength()); 
		var apprvdEstimateQtyTotal=0;
		var allresults=res.results;
		var deductionTotal=0;
		var nonDeductionTotal=0; 
		for(var i=0;i<allresults.length;i++){
		     mbMSheetDataTable.addRow({SlNo:mbMSheetDataTable.getRecordSet().getLength()+1,
		     workOrderActivityId:dom.get("woActivityId").value,
		     workOrderMSheetId:allresults[i].Id,
		     estimateNo:allresults[i].estimateNo,
		  	 revisionType:allresults[i].revisionType, 
		     apprvdMSheetNo:allresults[i].no,
		     apprvdMSheetLength:allresults[i].uomLength,
		     apprvdMSheetWidth:allresults[i].width,
			 apprvdMSheetDH:allresults[i].depthOrHeight,
			 apprvdMSheetDeduction:'',
			 apprvdMSheetQuantity:allresults[i].quantity,
			 
			 curMBMSheetNo:allresults[i].curNo,
			 curMBMSheetLength:allresults[i].curLength,
			 curMBMSheetWidth:allresults[i].curWidth,
			 curMBMSheetDH:allresults[i].curDH,
			 curMBMSheetDeduction:'',
			 curMBMSheetQuantity:allresults[i].curQuantity,
			 
			 hiddenCurMBMSheetQuantity:allresults[i].curQuantity,
			 curCumlvMBMSheetQuantity:allresults[i].curCumQuantity,
			 hiddenCurCumlvMBMSheetQuantity:allresults[i].curCumQuantity
			 });
			 
			
			var record = mbMSheetDataTable.getRecord(parseInt(i));
			disableMBMsheetFields(record);
			
			if(allresults[i].identifier=='D'){
			   	 dom.get("apprvdMSheetDeduction"+record.getId()).checked=true;
			   	 deductionTotal=deductionTotal+getNumber(allresults[i].quantity);
			}
		   	else{
		   	 	dom.get("apprvdMSheetDeduction"+record.getId()).checked=false;
		   	 	nonDeductionTotal=nonDeductionTotal+getNumber(allresults[i].quantity);
		   	}	
		   	
		   	if(record.getData("hiddenCurCumlvMBMSheetQuantity")=="")
		   	 	mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('hiddenCurCumlvMBMSheetQuantity'),"0.00");
		   	 if(record.getData("hiddenCurMBMSheetQuantity")=="")
		   	 	mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('hiddenCurMBMSheetQuantity'),"0.00");
		   	
		   	dom.get("apprvdMSheetDeduction"+record.getId()).disabled='true';
		    dom.get("apprvdMSheetDeduction"+record.getId()).readOnly='true';
	    }
	    var val1=roundTo((nonDeductionTotal-deductionTotal),5,'0');
	    dom.get("apprvdQtyTotal").innerHTML=val1;
	    mbMSheetDataTable.showColumn("estimateNo");
		mbMSheetDataTable.showColumn("revisionType");
		mbMSheetDataTable.showColumn("level1"); 
		mbMSheetDataTable.showColumn("level2");
		mbMSheetDataTable.hideColumn("curMBMSheetRemarks");
		mbMSheetDataTable.hideColumn("curMBMSheetDeduction");
		mbMSheetDataTable.hideColumn("Delete");
		
		dom.get("showAdd").style.display='none'; 
		dom.get('mbMSheetTotal').innerHTML='Total';
		dom.get('zeroQtyTotal').innerHTML='';
		dom.get("apprvdQtyTotal").style.display='block';
	}
	else if(approved_quantity==0){
		mbMSheetDataTable.hideColumn("estimateNo");
		mbMSheetDataTable.hideColumn("revisionType");
		mbMSheetDataTable.hideColumn("level1"); 
		mbMSheetDataTable.showColumn("curMBMSheetRemarks");
		mbMSheetDataTable.showColumn("curMBMSheetDeduction");
		mbMSheetDataTable.showColumn("Delete");
		mbMSheetDataTable.showColumn("curCumlvMBMSheetQuantity");
        
        mbMSheetDataTable.hideColumn("apprvdMSheetDeduction");
        mbMSheetDataTable.hideColumn("apprvdMSheetQuantity");
        dom.get("apprvdQtyTotal").style.display='none';
         
		dom.get("showAdd").style.display='block';
		dom.get('mbMSheetTotal').innerHTML='';
		dom.get('zeroQtyTotal').innerHTML='Total';
	}    
	var mbMSCont = document.getElementById('mbMSheetDetailsContainer');
	mbMSCont.style.display='block'; 
	mbMSCont.style.top = '400px';
	mbMSCont.style.left = '80px';
	mbMSCont.style.width = '75%'; 
	mbMSCont.style.height = '40%';	
	document.getElementById('detailedMBMSheetTable').style.display='block';
	document.getElementById('buttons').style.display='block'; 
	reLoad(dom.get("woActivityId").value);		
    calculateMBMSheetTotal();
    val2=roundTo((nonDeductionTotal-deductionTotal),5,'0');
   	dom.get("apprvdQtyTotal").innerHTML=val2;
   	
   	 <s:if test="%{((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
   	 	disableMBMSheetTable(); 
   	 </s:if>
   	
}

estimateMSheetLoadFailureHandler=function(req,res) {
	 alert("Unable to load Measurement Book Measurement MSheet");
}
    
function resetMBDataTable(){
	mbDataTable.deleteRows(0,mbDataTable.getRecordSet().getLength());
	dom.get("amtTotal").innerHTML="0.00";
	dom.get("culmvAmtTotal").innerHTML="0.00";
	dom.get("extraItemsAmtTotal").innerHTML="0.00";
	dom.get("extraItemsCulmvAmtTotal").innerHTML="0.00";
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
	 window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!loadSerachForActivity.action?workorderNo="+workorder_no+"&workOrderId="+workorder_id+"&estimateId="+estimate_id,"",
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
			/*if(workorder_id!=document.getElementById('workOrder').value){
				dom.get("mb_error").style.display='block';
				dom.get("mb_error").innerHTML='<s:text name="measurementbook.activity.wo.invalid"/>';
				window.scroll(0,0);
				return false;
			}*/

			if(activity_id!=null && workorder_id!=null){
				var j;
				for(j=0;j<mbDataTable.getRecordSet().getLength() && activity_id!='';j++){
	    	       if(activity_id==mbDataTable.getRecordSet().getRecord(j).getData('workOrderActivity')){
	    	       		isError=true;
	    	       		break;
	    	       }
	    	       else {
	    	       		isError=false;
	    	       }
	    	    }
	    	    if(!isError) {
 	    	    	for(var k=0;k<extraItemsDataTable.getRecordSet().getLength() && activity_id!='';k++){
    	       			if(activity_id==extraItemsDataTable.getRecordSet().getRecord(k).getData('workOrderActivity')){
		    	       		isError=true;
	    	       			break;
	    	       		}
	    	       		else {
	    	       			isError=false;
	    	       		}
 	    	    	 }
	    	    }
	    	    
				if(!isError) {
					setTimeout('setupActivityDetails('+activity_id+','+workorder_id+')',(i*1000));
				}
			}
		}
	}
}


function setupActivityDetails(activity_id,workorder_id,count){
    dom.get("mb_error").style.display='none';
	dom.get("mb_error").innerHTML='';
	var isTenderPercentageTypeValue=document.getElementById("isTenderPercentageType").value;
	var mbPercentagelevelValue=document.getElementById("mbPercentagelevel").value;
	var mbHeaderVal = document.getElementById("id").value;
	if(mbHeaderVal == null || mbHeaderVal == '')
		mbHeaderVal = -1;

	var myDataSource = new YAHOO.util.XHRDataSource('${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!activityDetails.action');
	myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
	myDataSource.connXhrMode = "queueRequests";
	myDataSource.responseSchema = {			
				resultsList: "ResultSet.Result",
				fields: ["xActId","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","woMsheetSize","xIsExtraItem","xRemarks"]
	};
	var callbackObj = {
	        success : function(req,res){
	            var resultdatas=res.results;
	            addActivityInYUI(resultdatas);               
	        	
	        },
	        failure : function(){
	        	dom.get("mb_error").style.display='block';
	        	dom.get("mb_error").innerHTML='Unable to load activity details';
	        	
	        }
};
	myDataSource.sendRequest("?"+toQuery({woActivityId:activity_id,workOrderId:workorder_id,mbHeaderId:mbHeaderVal,isTenderPercentageType:isTenderPercentageTypeValue,mbPercentagelevel:mbPercentagelevelValue}),callbackObj);
	        
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
                        approvedQuantity:resultdatas[0].xApprdQunty,
                        approvedQuantityval:resultdatas[0].xApprdQunty,
                        approvedRate:resultdatas[0].xApprdRate,
                        approvedRateval:resultdatas[0].xApprdRate,
						prevCumlv:resultdatas[0].xPrevCulmvEntry,
						prevCumlvval:resultdatas[0].xPrevCulmvEntry,
						woMsheetSize:resultdatas[0].woMsheetSize,
						isExtraItem:resultdatas[0].xIsExtraItem,
						remark:resultdatas[0].xRemarks,
                        approvedAmt:roundTo(resultdatas[0].xApprdAmt),
                        approvedAmtval:resultdatas[0].xApprdAmt,
                        fullDescription:resultdatas[0].xSORDesc});
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
                        approvedQuantity:resultdatas[0].xApprdQunty,
                        approvedQuantityval:resultdatas[0].xApprdQunty,
                        approvedRate:resultdatas[0].xApprdRate,
                        approvedRateval:resultdatas[0].xApprdRate,
						prevCumlv:resultdatas[0].xPrevCulmvEntry,
						prevCumlvval:resultdatas[0].xPrevCulmvEntry,
						woMsheetSize:resultdatas[0].woMsheetSize,
						isExtraItem:resultdatas[0].xIsExtraItem,
						remark:resultdatas[0].xRemarks,
                        approvedAmt:roundTo(resultdatas[0].xApprdAmt),
                        approvedAmtval:resultdatas[0].xApprdAmt,
                        fullDescription:resultdatas[0].xNonSORDesc});
        }
 
}
myActFailureHandler= function(){
    dom.get("mb_error").style.display='block';
	dom.get("mb_error").innerHTML='Unable to load activity details';
}

function checkActivity(){
	if(mbDataTable.getRecordSet().getLength()==0){
		dom.get("mb_error").style.display='';     
    	document.getElementById("mb_error").innerHTML='<s:text name="measurementbook.item.mandatory" />';
	}
}

</script>
<table id="mbDetailsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="6" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer"><s:text name="measurementbook.details"/></div>
					</td>
					<td align="right" class="headingwk" style="border-left-width: 0px">
                		<a href="#" onclick="addActivity();">
                		<img height="16" border="0" width="16" alt="Add Items" src="${pageContext.request.contextPath}/image/add.png" />
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
			                <s:iterator id="mbIterator" value="mbDetails" status="row_status">
			                <s:if test="%{workOrderActivity.activity.schedule && workOrderActivity.activity.schedule.uom.uom != ''}">
							    mbDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:'<s:property value="#row_status.count"/>',
				                        description:'<s:property value="workOrderActivity.activity.schedule.summaryJS"/>',
				                        descriptionval:'<s:property value="workOrderActivity.activity.schedule.summaryJS"/>',
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
				                        <s:if test="%{remark!=null && workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">					                       
					                        remark:'<s:property value="remark"/>',
					                    </s:if>
					                    <s:elseif test="%{remark==null && workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">
					                        	remark:'Extra Item',
							             </s:elseif>
					                    <s:else>
				                        	remark:'<s:property value="remark"/>',				                        	
						                </s:else>
				                        prevCumlv:'<s:property value="prevCumlvQuantity"/>',
				                        prevCumlvval:'<s:property value="prevCumlvQuantity"/>',
				                        currCumlv:'<s:property value="currCumlvQuantity"/>',
				                        currCumlvval:'<s:property value="currCumlvQuantity"/>',
				                         <s:if test="%{workOrderActivity.activity.conversionFactor!=null && !hasErrors()}">
					                        amount:roundTo('<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>'),
					                        amountval:'<s:property value="amtForCurrQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>',
					                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>'),
					                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>'*'<s:property value="workOrderActivity.activity.conversionFactor"/>',
					                     </s:if>
					                     <s:elseif test="%{hasErrors()}">
					                       amount:roundTo('<s:property value="amtForCurrQuantity"/>'),
					                        amountval:'<s:property value="amtForCurrQuantity"/>',
					                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'),
					                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>',
					                     </s:elseif>
				                        approvedQuantity:'<s:property value="totalEstQuantity"/>',
				                        approvedQuantityval:'<s:property value="totalEstQuantity"/>',
				                        <s:if test='%{mbPercentagelevel=="TotalMBValue" && isTenderPercentageType==true}'>
				                        	approvedRate:'<s:property value="workOrderActivity.activity.rate"/>',
				                        	approvedRateval:'<s:property value="workOrderActivity.activity.rate"/>',
				                        	approvedAmt:roundTo('<s:property value="workOrderActivity.activity.Amount"/>'),
				                        	approvedAmtval:'<s:property value="workOrderActivity.activity.Amount"/>',
				                        </s:if>
				                        <s:else>
				                       	 	approvedRate:'<s:property value="workOrderActivity.approvedRate"/>',
				                        	approvedRateval:'<s:property value="workOrderActivity.approvedRate"/>',
				                        	approvedAmt:roundTo('<s:property value="workOrderActivity.approvedAmount"/>'),
				                        	approvedAmtval:'<s:property value="workOrderActivity.approvedAmount"/>',
				                        </s:else>
				                        partRate:'<s:property value="partRate"/>',
				                        reducedRate:'<s:property value="reducedRate"/>',
				                        woMsheetSize:'<s:property value="workOrderActivity.activity.measurementSheetList.size" />',
				                       <s:if test="%{workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">
    										isExtraItem:'true',
							</s:if>
    									<s:else>
    										isExtraItem:'false',
    									</s:else>
				                        fullDescription:'<s:property value="workOrderActivity.activity.schedule.descriptionJS"/>'});
							</s:if>
		          			<s:elseif test="%{workOrderActivity.activity.nonSor && workOrderActivity.activity.nonSor.uom.uom != ''}">
		          				mbDataTable.addRow({workOrderActivity:'<s:property value="workOrderActivity.id"/>',
				                        SlNo:'<s:property value="#row_status.count"/>',
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
				                        <s:if test="%{remark!=null && workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">					                       
				                        	remark:'<s:property value="remark"/>',
				                        </s:if>
					                    <s:elseif test="%{remark==null && workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">
					                        	remark:'Extra Item',
							             </s:elseif>
					                    <s:else>
				                        	remark:'<s:property value="remark"/>',				                        	
						                </s:else>
				                        prevCumlv:'<s:property value="prevCumlvQuantity"/>',
				                        prevCumlvval:'<s:property value="prevCumlvQuantity"/>',
				                        currCumlv:'<s:property value="currCumlvQuantity"/>',
				                        currCumlvval:'<s:property value="currCumlvQuantity"/>',
				                        amount:roundTo('<s:property value="amtForCurrQuantity"/>'),
				                        amountval:'<s:property value="amtForCurrQuantity"/>',
				                        cumlvAmt:roundTo('<s:property value="cumlvAmtForCurrCumlvQuantity"/>'),
				                        cumlvAmtval:'<s:property value="cumlvAmtForCurrCumlvQuantity"/>',
				                        approvedQuantity:'<s:property value="totalEstQuantity"/>',
				                        approvedQuantityval:'<s:property value="totalEstQuantity"/>',
				                        woMsheetSize:'<s:property value="workOrderActivity.activity.measurementSheetList.size" />',
				                        <s:if test='%{mbPercentagelevel=="TotalMBValue" && isTenderPercentageType==true}'>
				                        	approvedRate:'<s:property value="workOrderActivity.activity.rate"/>',
				                        	approvedRateval:'<s:property value="workOrderActivity.activity.rate"/>',
				                        	approvedAmt:roundTo('<s:property value="workOrderActivity.activity.Amount"/>'),
				                        	approvedAmtval:'<s:property value="workOrderActivity.activity.Amount"/>',
				                        </s:if>
				                        <s:else>
				                       	 	approvedRate:'<s:property value="workOrderActivity.approvedRate"/>',
				                        	approvedRateval:'<s:property value="workOrderActivity.approvedRate"/>',
				                        	approvedAmt:roundTo('<s:property value="workOrderActivity.approvedAmount"/>'),
				                        	approvedAmtval:'<s:property value="workOrderActivity.approvedAmount"/>',
				                        </s:else>
				                        partRate:'<s:property value="partRate"/>',
				                        reducedRate:'<s:property value="reducedRate"/>',
				                        <s:if test="%{workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">
    										isExtraItem:'true',
    								   </s:if>
    									<s:else>
    										isExtraItem:'false',
    									</s:else>
				                        fullDescription:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" />'});
		          			</s:elseif>
								var record = mbDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
								if(record.getData("isExtraItem")=='true') {										
		          					dom.get("extraItemsAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsAmtTotal") + 
		          								getNumber(record.getData("amount")));
									dom.get("extraItemsCulmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsCulmvAmtTotal") + 
												getNumber(record.getData("cumlvAmt")));
								}
								else {
									dom.get("amtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("amtTotal") + 
	          								getNumber(record.getData("amount")));
									dom.get("culmvAmtTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("culmvAmtTotal") + 
											getNumber(record.getData("cumlvAmt")));

								} 
							
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

