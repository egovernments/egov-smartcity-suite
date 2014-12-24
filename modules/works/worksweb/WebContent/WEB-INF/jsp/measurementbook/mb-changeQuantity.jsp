<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 

<style type="text/css">
.yui-dt0-col-TaxAmt { 
  width: 5%;
}
.yui-dt-col-rate{
	text-align:right;
}
.yui-dt-col-estQuantity{
	text-align:right;
}
.yui-dt-col-quantity{
	text-align:right;
}
.yui-dt-col-mbQuantity{
	text-align:right;
}
.yui-dt-col-EstdAmt{
	text-align:right;
}	
.yui-dt-col-serviceTaxPerc{
	text-align:right;
}		
.yui-dt-col-TaxAmt{
	text-align:right;
}
.yui-dt-col-Total{
	text-align:right;
}

.yui-skin-sam th.yui-dt-col-sign{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-sign{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-quantity .yui-dt-liner{
	margin-left: -40px;
	align-text: left;
} 

.yui-skin-sam td.yui-dt-col-quantity .yui-dt-liner{
	margin-left: -10px;
} 

.yui-skin-sam th.yui-dt-col-mbChangeQtyMSheet{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-mbChangeQtyMSheet{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-mbCQMSheetDtls{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-mbCQMSheetDtls{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-mbConsumedQtyMSheetDtls{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-mbConsumedQtyMSheetDtls{
 	border-right:none;
}


</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
 function checkExtraItemsRate(elem,recordId){
 dom.get('error'+elem.id).style.display='none';
 if(elem.readOnly==true) return;
 dom.get("mb_error").style.display='none'; 
 document.getElementById("mb_error").innerHTML='';
	if(!validateNumberInTableCell(extraItemsDataTable,elem,recordId)) return;
 
	if(elem.value!='' && getNumber(elem.value)>0){
		<s:if test='%{mbAmtAboveApprovedAmt=="false"}'>
			if(getNumber(elem.value)>=getNumber($F("extraItemsrate"+dom.get("rateRecId"+record.getId()).value))){
				dom.get("mb_error").style.display=''; 
	          	document.getElementById("mb_error").innerHTML="Please enter New Rate lesser than Approved Rate";
	          	dom.get('error'+elem.id).style.display='';
		      	window.scroll(0,0);
		      	return;
			}
		</s:if>
	}
 	calculateExtraItems(elem,recordId);
 }
function calculateExtraItems(elem,recordId){ 
	dom.get('error'+elem.id).style.display='none';
	var tempId="extraItemsserviceTaxPerc"+recordId;
	if((elem.value=='' || trim(elem.value)=='') && tempId!=elem.id) {
		dom.get('error'+elem.id).style.display='';
		return false; 
	}

    if(elem.readOnly==true) return;
      if(!validateNumberInTableCell(extraItemsDataTable,elem,recordId)) return; 
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTaxAmt=getNumber(record.getData("TaxAmt"));
      oldTotal=getNumber(record.getData("Total"));
      taxRate=dom.get("extraItemsserviceTaxPerc"+record.getId()).value;
       var quantElemId = 'extraItemsquantity' + recordId;
      if(elem.id==quantElemId){
      	if(dom.get('signValue'+record.getId()).value=='-') 
      		extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('EstdAmt'),roundTo(-1*elem.value*getNumber($F("extraItemsrate"+dom.get("rateRecId"+record.getId()).value))));
      	else
      		extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('EstdAmt'),roundTo(elem.value*getNumber($F("extraItemsrate"+dom.get("rateRecId"+record.getId()).value))));
      }        
      
      extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('TaxAmt'),roundTo(taxRate*getNumber(record.getData("EstdAmt"))/100.0));
      taxAmt=getNumber(record.getData("TaxAmt"));

      extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('Total'),roundTo(getNumber(record.getData("EstdAmt"))+taxAmt));
      
	  dom.get("extraItemsEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsEstTotal") - oldEstAmt +getNumber(record.getData("EstdAmt")));	 
	  dom.get("extraItemsTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsTaxTotal") -oldTaxAmt +taxAmt);
	  dom.get("extraItemsGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsGrandTotal") -oldTotal +getNumber(record.getData("Total")));
   	  //document.getElementById("mbValue").value=roundTo(eval(document.getElementById("amtTotal").innerHTML)+eval(document.getElementById("mbTotal").innerHTML)+eval(document.getElementById("nonSorMbTotal").innerHTML)+eval(document.getElementById("extraItemsEstTotal").innerHTML));
   	  calculateMBAmount(elem,recordId,record); 	 
}

function calculateMBAmount(elem,recordId,record){  
	  clearMessage('mb_error');      
      if(dom.get('extraItemsquantity'+record.getId()).value!='') {
	      if(dom.get('signValue'+record.getId()).value=='-') {
	      	if(getNumber(dom.get('extraItemsquantity'+record.getId()).value)>eval(getNumber(record.getData("estQuantity"))-getNumber(record.getData("prevCumlQuantity")))) {
	      	 	dom.get("mb_error").style.display=''; 
	        	document.getElementById("mb_error").innerHTML='Cannot enter quantity less than the available Quantity';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get('extraItemsquantity'+record.getId()).id).style.display='';
	      	 	return;
	      	 }
	        
	      	if(dom.get('extraItemsmbQuantity'+record.getId()).value!='' && getNumber(dom.get('extraItemsmbQuantity'+record.getId()).value)>eval(getNumber(record.getData("estQuantity"))-getNumber(dom.get('extraItemsquantity'+record.getId()).value)-getNumber(record.getData("prevCumlQuantity")))) {
	      	 	dom.get('error'+dom.get('extraItemsmbQuantity'+record.getId()).id).style.display='';
	      	 	return;
	      	 }
	      }
       if(dom.get('extraItemsmbQuantity'+record.getId()).value!='') {
      	 if(getNumber(record.getData("estQuantity"))!=0 && getNumber(dom.get('extraItemsmbQuantity'+record.getId()).value)>eval(getNumber(record.getData("estQuantity"))+getNumber(dom.get('extraItemsquantity'+record.getId()).value)-getNumber(record.getData("prevCumlQuantity")))) {
      	 	dom.get('error'+dom.get('extraItemsmbQuantity'+record.getId()).id).style.display='';
      	 	return;
      	 }
      	 if(getNumber(record.getData("estQuantity"))==0 && getNumber(dom.get('extraItemsmbQuantity'+record.getId()).value)>eval(getNumber(dom.get('extraItemsquantity'+record.getId()).value))) {
      	 	dom.get('error'+dom.get('extraItemsmbQuantity'+record.getId()).id).style.display='';
      	 	return;
      	 }
      	  var extraItemsRateToBeUsed=getNumber($F("extraItemsrate"+dom.get("rateRecId"+record.getId()).value));
	     if(getNumber(dom.get('extraItemsReducedRate'+recordId).value)>0 && dom.get('isEQRRextraItemsReducedRate'+recordId).checked==true){
	    	extraItemsRateToBeUsed=dom.get('extraItemsReducedRate'+recordId).value;
	    }
      	 oldMbAmt=getNumber(record.getData("MbAmt"))
      	 extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('MbAmt'),roundTo(dom.get('extraItemsmbQuantity'+record.getId()).value*extraItemsRateToBeUsed));
      	 dom.get("extraItemsMbTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsMbTotal") - oldMbAmt +getNumber(record.getData("MbAmt")));
      	 updateTotalValue();     	 
      }	 
    }
}

function recalculateExtraItemsTotalsOnDelete(record){
	  dom.get("extraItemsEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsEstTotal") -getNumber(record.getData("EstdAmt")));
	  dom.get("extraItemsMbTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsMbTotal") -getNumber(record.getData("MbAmt")));
	  dom.get("extraItemsTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsTaxTotal") - getNumber(record.getData("TaxAmt")));
	  dom.get("extraItemsGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsGrandTotal") -getNumber(record.getData("Total")));
	 	  
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createRateRecIdHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = extraItemsDataTable.getRecordSet().getLength();
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName=oColumn.getKey()+oRecord.getId();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var hiddenRateRecIdFormatter = createRateRecIdHiddenFormatter(5,5);

function createExtraItemsReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
     //var id="extraItems"+oColumn.getKey()+oRecord.getId();
    var id="estimate"+oColumn.getKey()+extraItemsDataTable.getRecordSet().getLength();
    var fieldName="extraItemsActivities[" + oRecord.getCount() + "].activity." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var extraItemsReadOnlyTextboxFormatter = createExtraItemsReadOnlyTextBoxFormatter(5,5);

function createExtraItemsTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="extraItems"+oColumn.getKey()+oRecord.getId();
    var fieldName="extraItemsActivities[" + oRecord.getCount() + "].activity." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateExtraItems(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createExtraItemsTextBoxFormatter(6,10);
var stExtraItemsFormatter = createExtraItemsTextBoxFormatter(5,5);
function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\r\n/g, "<br>");
	return str2;
}
var descriptionFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var divId="full-"+oColumn.getKey()+oRecord.getId();
    markup="<span class='yui-dt-liner'>"+value+"</span>" + hint.replace(/@fulldescription@/g,escapeSpecialChars(oRecord.getData('FullDescription')));
    el.innerHTML = markup;
}

function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldValue;
    var fieldName = "extraItemsActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
  	fieldValue=value;
   	markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);
 
var signDropdownOptions=[{label:"+", value:"+"},{"label":"-",value:"-" }]

function createExtraItemsRateHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+extraItemsDataTable.getRecordSet().getLength();
    var fieldName="extraItems"+oColumn.getKey()+extraItemsDataTable.getRecordSet().getLength();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var extraItemsRateHiddenFormatter = createExtraItemsRateHiddenFormatter(8,13);

function createActivityIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="changeQtyWOActivityId"){
     fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].woActivity.id";
    	markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='6'/>"; 
    } 
    else{
     fieldName = "extraItemsActivities[" + oRecord.getCount() + "].id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
     }
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var activityIDFormatter = createActivityIDFormatter(10,10);

function createSignHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "extraItemsActivities[" + oRecord.getCount() + "].activity."+ oColumn.getKey();
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var signHiddenFormatter = createSignHiddenFormatter(10,10);

var isExtraItemCQFormatter=createIsExtraItemCQFormatter(10,10);

function createIsExtraItemCQFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="";
     markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}

function checkEQRate(obj,recordId,objId){
var isPartRateName="isEQPR"+objId;
var isReducedRateName="isEQRR"+objId;
	if(obj.name==isPartRateName && obj.checked==true){
		document.getElementById(objId).removeAttribute('readonly');
		var tb="extraItemsReducedRate"+recordId;
		var cb="isEQRR"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.name==isReducedRateName && obj.checked==true){
 		document.getElementById(objId).removeAttribute('readonly');
 		var tb="extraItemsPartRate"+recordId;
		var cb="isEQPR"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.checked==false){
		document.getElementById(objId).value='0.0';
		document.getElementById(objId).setAttribute('readonly', true);
	}
}

function createPartRateEQFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="extraItemsActivities[" + oRecord.getCount() + "].activity.partRate";
    var checkBoxId="isEQPR"+id;
    markup="<input type='checkbox' id='"+checkBoxId+"' name='"+checkBoxId+"' onclick=\"checkEQRate(this,'"+oRecord.getId()+"','"+id+"')\" /><input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true'  onblur='checkExtraItemsRate(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var partRateEQFormatter=createPartRateEQFormatter(4,20);

function createReducedRateEQFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id= oColumn.getKey()+oRecord.getId();
    var fieldName="extraItemsActivities[" + oRecord.getCount() + "].activity.reducedRate";
    var checkBoxId="isEQRR"+id;
    markup="<input type='checkbox' id='"+checkBoxId+"' name='"+checkBoxId+"' onclick=\"checkEQRate(this,'"+oRecord.getId()+"','"+id+"')\" /><input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true'  onblur='checkExtraItemsRate(this,\""+oRecord.getId()+"\");' /><span id='error"+id+
	    	"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
  }
  return textboxFormatter;
}
var reducedRateEQFormatter=createReducedRateEQFormatter(4,20);

function enableEQPartandReducedRate(){
var records = extraItemsDataTable.getRecordSet();
var tbLen = records.getLength();

  for(var i=0;i<tbLen;i++){  
   	var record = records.getRecord(i);
		var partRate= getNumber(dom.get('extraItemsPartRate'+record.getId()).value);
		if(partRate>0){
			var isPartRatecb="isEQPRextraItemsPartRate"+record.getId();
			document.getElementById(isPartRatecb).checked=true;
			document.getElementById('extraItemsPartRate'+record.getId()).removeAttribute('readonly');
		}
		var reducedRate= getNumber(dom.get('extraItemsReducedRate'+record.getId()).value);
		if(reducedRate>0){
			var isReducedRatecb="isEQRRextraItemsReducedRate"+record.getId();
			document.getElementById(isReducedRatecb).checked=true;
			document.getElementById('extraItemsReducedRate'+record.getId()).removeAttribute('readonly');
		}
	}

}
		
var extraItemsDataTable;  
var makeExtraItemsDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var extraItemsColumnDefs = [
            {key:"workOrderActivity", hidden:true,formatter:activityIDFormatter,sortable:false, resizeable:false},
            {key:"workOrderMsheetSize",hidden:true,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false}, 
            {key:"SlNo",label:'Sl No', sortable:false, resizeable:false},
            {key:"Code",label:'Code', sortable:false, resizeable:false},
            {key:"Description", formatter:descriptionFormatter,sortable:false, resizeable:false,width:120},
            {key:"UOM", sortable:false, resizeable:false},
            {key:"extraItemsrate", label:'Rate', formatter:extraItemsRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'Unit Rate',hidden:true,  formatter:extraItemsReadOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"mbChangeQtyMSheet",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"estQuantity",label:'Estimated<br>Quantity', sortable:false, resizeable:false,width:50},
            {key:"prevCumlQuantity",label:'Cumulative<br>Quantity', sortable:false, resizeable:false},
            {key:"signValue",hidden:true,formatter:signHiddenFormatter,sortable:false, resizeable:false},
            {key:"sign",label:'',formatter:"dropdown", dropdownOptions:signDropdownOptions},
            {key:"mbCQMSheetDtls",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"quantity",label:'Change Quantity<span class="mandatory">*</span>',formatter:textboxFormatter, sortable:false, resizeable:false},
            {key:"EstdAmt",label:'Estimated<br>Amount', sortable:false, resizeable:false},
            {key:"mbConsumedQtyMSheetDtls",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"mbQuantity",label:'Consumed<br>Quantity<span class="mandatory">*</span>',formatter:textboxFormatter, sortable:false, resizeable:false,width:60},            
            {key:"MbAmt",label:'MB Amount', sortable:false, resizeable:false},            
            {key:"serviceTaxPerc",label:'Service VAT%', formatter:stExtraItemsFormatter,sortable:false, resizeable:false},
            {key:"TaxAmt",label:'Service/VAT<br>Amount',sortable:false, resizeable:false},
            {key:"extraItemsPartRate" ,label:'<s:text name="measurementbook.partRate"/>',formatter:partRateEQFormatter,sortable:false, resizeable:false},
            {key:"extraItemsReducedRate" ,label:'<s:text name="measurementbook.reducedRate"/>',formatter:reducedRateEQFormatter,sortable:false, resizeable:false},
            {key:"Total",label:'Total', sortable:false, resizeable:false}, 
            {key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"isExtraItemCQ" ,hidden:true,formatter:isExtraItemCQFormatter,sortable:false, resizeable:false},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false},           
        ];

        var extraItemsDataSource = new YAHOO.util.DataSource();
        extraItemsDataTable = new YAHOO.widget.DataTable("itemsTable",
                extraItemsColumnDefs, extraItemsDataSource,{MSG_EMPTY:"<s:text name='measurementbook.initial.table.message'/>"});
         extraItemsDataTable.subscribe("cellClickEvent", extraItemsDataTable.onEventShowCellEditor); 
          
         extraItemsDataTable.on('cellClickEvent',function (oArgs) { 
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
              
            <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >        
            if (column.key == 'Delete') { 
             dom.get("wOActivityIdCQ").value=record.getData("workOrderActivity");
                recalculateExtraItemsTotalsOnDelete(record); 	
                removeChangeQtyItemDetails(record.getData("workOrderActivity"));
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
                }
		updateTotalValue();
            }
            </s:if>
      		      		
             if(column.key=='mbChangeQtyMSheet'){
               if(record.getData("workOrderMsheetSize")==0) {
                       alert("No Work Order Measurement details found for this Activity"); 
               }
               else{          
		          	var woActivityId=record.getData("workOrderActivity"); 
		          	var mbHeaderId=dom.get('id').value;
		           	makeJSONCall(["estimateNo","revisionType","description","no","Id","uomLength","width","depthOrHeight","identifier","uom","quantity"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!getMbChangeQtyMSheetDetails.action',{woActivityId:woActivityId,mbHeaderId:mbHeaderId},estimateMSheetSuccessHandler,estimateMSheetFailureHandler);
	          	}
	       }
	   
	       if(column.key=='mbCQMSheetDtls'){
	            dom.get("activityUOMForCQ").value=record.getData("UOM");
	            dom.get("recordIdForCQ").value=record.getId();
	            dom.get("wOActivityIdCQ").value=record.getData("workOrderActivity");
	            
	            if(record.getData("workOrderMsheetSize")==0 && record.getData("estQuantity")!=0) {
                       alert("No Work Order Measurement details found for this Activity"); 
               	}
               	else{
		            reLoadMBChangeQtyMsheetTable(dom.get("wOActivityIdCQ").value);
		            var msCont = document.getElementById('mbExtraCQMSheetDetailsContainer'); 
					msCont.style.display='block';
					msCont.style.top = '450px';
					msCont.style.left = '80px';
					msCont.style.width = '75%';
					msCont.style.height = '40%';
					document.getElementById('minimizeMBExtraCQMsheet').innerHTML='-';
					document.getElementById('minimizeMBExtraCQMsheet').title='Minimize'; 
		         	document.getElementById('detailedMBExtraCQMSheetTable').style.display='block';
					document.getElementById('mbExtraCQMSheetTable').style.display='block';
					document.getElementById('buttonsExtraCQItem').style.display='block';
	 				window.scroll(0,0);
	 			
					<s:if test="%{((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
		    			disableExtraMSheetTable("ChangeQty"); 
		    		</s:if>
	      		}
      		}
      		
	   		 if(column.key=='mbConsumedQtyMSheetDtls'){
	           
	            dom.get("recordIdForCQ").value=record.getId();
	            dom.get("wOActivityIdCQ").value=record.getData("workOrderActivity");
	            dom.get("activityUOMForCQ").value=record.getData("UOM"); 
	            
	            if(record.getData("workOrderMsheetSize")==0 && record.getData("estQuantity")!=0) {
                       alert("No Work Order Measurement details found for this Activity"); 
               	}
               	else{
               		var woActivityId=record.getData("workOrderActivity"); 
		          	var mbHeaderId=dom.get('id').value;
		          	makeJSONCall(["cumulativeQuantity","estimateNo","revisionType","description","no","Id","uomLength","width","depthOrHeight","identifier","uom","quantity","msheetSlNo","estimateMsheetId"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!getMbChangeQtyMSheetDetails.action',{woActivityId:woActivityId,mbHeaderId:mbHeaderId},consumedChangeQtyMSheetSuccessHandler,consumedChangeQtyMSheetFailureHandler);
	      		}
	   		 }
	       	if(record.getData("workOrderMsheetSize")!=0 && record.getData("workOrderMsheetSize")!="" &&  record.getData("workOrderMsheetSize")!=undefined) {
	       		if(column.key=='quantity'){  
	       			dom.get("extraItems"+"quantity"+record.getId()).disabled=true; 
	       			dom.get("extraItems"+"quantity"+record.getId()).readOnly=true; 
	       		}
	       		if(column.key=='mbQuantity'){
	       			dom.get("extraItems"+"mbQuantity"+record.getId()).disabled=true; 
	       			dom.get("extraItems"+"mbQuantity"+record.getId()).readOnly=true; 
	       		}
	       	}
        });
        extraItemsDataTable.on('dropdownChangeEvent', function (oArgs) {
		    var record = this.getRecord(oArgs.target);
	        var column = this.getColumn(oArgs.target);
	        if(column.key=='sign'){	        
	            var selectedIndex=oArgs.target.selectedIndex;
	            this.updateCell(record,this.getColumn('signValue'),signDropdownOptions[selectedIndex].value);
	        }
		});
            var tfoot = extraItemsDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 13;
			th.className= 'whitebox4wk';
			th.innerHTML = '&nbsp;';

			var td = tr.insertCell(1);
			td.className= 'whitebox4wk';
			td.id = 'subTotal';
			td.innerHTML = '<span class="bold">Total:</span>';
			td.colSpan=3;
			addCell(tr,2,'extraItemsEstTotal','0.00');
			addCell(tr,3,'filler','');
			addCell(tr,4,'strTotal','');
			addCell(tr,5,'extraItemsMbTotal','0.00');
			addCell(tr,6,'strTotal','');
			addCell(tr,7,'extraItemsTaxTotal','0.00');
			addCell(tr,8,'filler','');
			addCell(tr,9,'filler','');
			addCell(tr,10,'extraItemsGrandTotal','0.00');
			addCell(tr,11,'filler','');
			addCell(tr,12,'filler','');

        return {
            oDS: extraItemsDataSource,
            oDT: extraItemsDataTable
        };
      }
      
      
estimateMSheetSuccessHandler=function(req,res){
	woEstimateMSheetDataTable.deleteRows(0,woEstimateMSheetDataTable.getRecordSet().getLength()); 
	var estimateQtyTotal=0;
	var allresults=res.results;
	var quantity=0;
    for(var i=0;i<allresults.length;i++){
	  woEstimateMSheetDataTable.addRow({SlNo:woEstimateMSheetDataTable.getRecordSet().getLength()+1,
	  	 estimateNo:allresults[i].estimateNo,
	  	 revisionType:allresults[i].revisionType,  
	  	 description:allresults[i].description,
	     number:allresults[i].no,
	     length:allresults[i].uomLength,
	     width:allresults[i].width,
		 depthOrHeight:allresults[i].depthOrHeight,
		 quantity:allresults[i].quantity,
		 uom:allresults[i].uom,
		 deduction:''
		});
		var record = woEstimateMSheetDataTable.getRecord(parseInt(i));
		if(allresults[i].identifier=='D'){
		   	 dom.get("deduction"+record.getId()).checked=true;
		   	 estimateQtyTotal=estimateQtyTotal-getNumber(record.getData("quantity"));
	   	} 
	   	else {
	   	 dom.get("deduction"+record.getId()).checked=false;
	   	 estimateQtyTotal=estimateQtyTotal+getNumber(record.getData("quantity"));
	   	 }	
	   	dom.get("deduction"+record.getId()).disabled='true';
	    dom.get("deduction"+record.getId()).readOnly='true';
    }
    
	var estimateMSCont = document.getElementById('estimateMSheetContainer');
	estimateMSCont.style.display='block';
	estimateMSCont.style.top = '425px';
	estimateMSCont.style.left = '80px';
	estimateMSCont.style.width = '50%';
	estimateMSCont.style.height = '40%';	
	document.getElementById('estimateMSheetTable').style.display='block';
   	dom.get("quantityTotal").innerHTML=estimateQtyTotal;
} 

estimateMSheetFailureHandler=function(req,res) {
	alert("Unable to load Estimate Measurement Sheet");
}      

consumedChangeQtyMSheetSuccessHandler=function(req,res) {
	dom.get("mbCQConsumedMSheetError").style.display='none';
	dom.get("mbCQConsumedMSheetError").innerHTML='';
	mbChangeQtyConsumedMSheetTable.deleteRows(0,mbChangeQtyConsumedMSheetTable.getRecordSet().getLength());
	var allresults=res.results;
	var deductionTotal=0;
	var nonDeductionTotal=0;
	
	for(var i=0;i<allresults.length;i++){
	  mbChangeQtyConsumedMSheetTable.addRow({SlNo:mbChangeQtyConsumedMSheetTable.getRecordSet().getLength()+1,
	  	 estimateNo:allresults[i].estimateNo,
	  	 revisionType:allresults[i].revisionType,  
	  	 mbCQConsumedMSheetSlNo:allresults[i].msheetSlNo,
	  	 mbCQConsumedWOAId:allresults[i].Id, 
	  	 estimateMSheetId:allresults[i].estimateMsheetId, 
	  	 
	     apprvdMSheetNo:allresults[i].no,
	     apprvdMSheetLength:allresults[i].uomLength,
	     apprvdMSheetWidth:allresults[i].width,
		 apprvdMSheetDH:allresults[i].depthOrHeight,
		 apprvdMSheetQuantity:allresults[i].quantity,
		 apprvdMSheetDeduction:'',
		 
		 consumedCQMBMSheetNo:'',
		 consumedCQMBMSheetLength:'',
		 consumedCQMBMSheetWidth:'',
		 consumedCQMBMSheetDH:'',
		 consumedCQMBMSheetQuantity:'',
		 
		 curCumlvCQMBMSheetQuantity:allresults[i].cumulativeQuantity,
		 hiddenCurCumlvCQMBMSheetQuantity:allresults[i].cumulativeQuantity,
		 hiddenCurCQMBMSheetQuantity:''
		});
	
		var record = mbChangeQtyConsumedMSheetTable.getRecord(parseInt(i)); 
		disableMBExtraConsumedMsheetFields(record,"ConsumedChangeQty");
		if(allresults[i].identifier=='D'){
		   	 dom.get("apprvdMSheetDeduction"+record.getId()).checked=true;
		   	 deductionTotal=deductionTotal+getNumber(allresults[i].quantity);
	   	} 
	   	else{
	   	 	dom.get("apprvdMSheetDeduction"+record.getId()).checked=false;
	   	 	nonDeductionTotal=nonDeductionTotal+getNumber(allresults[i].quantity); 
	   	 }		
	   	 
	   	 if(record.getData("hiddenCurCumlvCQMBMSheetQuantity")=="")
	   	 	mbChangeQtyConsumedMSheetTable.updateCell(record,mbChangeQtyConsumedMSheetTable.getColumn('hiddenCurCumlvCQMBMSheetQuantity'),"0.00");
	   	 if(record.getData("hiddenCurCQMBMSheetQuantity")=="")
	   	 	mbChangeQtyConsumedMSheetTable.updateCell(record,mbChangeQtyConsumedMSheetTable.getColumn('hiddenCurCQMBMSheetQuantity'),"0.00"); 
	   	 
	   	dom.get("apprvdMSheetDeduction"+record.getId()).disabled='true';
	    dom.get("apprvdMSheetDeduction"+record.getId()).readOnly='true';
    }
    
 
    var records= mbExtraCQMSheetHiddenDataTable.getRecordSet();
    var count=mbChangeQtyConsumedMSheetTable.getRecordSet().getLength();
    var recCount=0;
    for(j=0;j<records.getLength();j++){
     if(dom.get("changeQtyWOActivityId"+records.getRecord(j).getId()).value==dom.get("wOActivityIdCQ").value && dom.get("hiddenEstimateMSheetId"+records.getRecord(j).getId()).value==''){ 
		 recCount++;
		 mbChangeQtyConsumedMSheetTable.addRow({SlNo:mbChangeQtyConsumedMSheetTable.getRecordSet().getLength()+1,
	  	 estimateNo:'',
	  	 revisionType:'',
	  	 
	  	 mbCQConsumedMSheetSlNo:dom.get("changeQuantityMsheetSlNo" + records.getRecord(j).getId()).value,
	  	 mbCQConsumedWOAId:dom.get("wOActivityIdCQ").value, 
	  	 estimateMSheetId:'', 
	  	  
	  	 apprvdMSheetDesc:dom.get("remarks" + records.getRecord(j).getId()).value,  
	     apprvdMSheetNo:dom.get("no" + records.getRecord(j).getId()).value,
	     apprvdMSheetLength:dom.get("uomLength" + records.getRecord(j).getId()).value,
	     apprvdMSheetWidth:dom.get("width" + records.getRecord(j).getId()).value,
		 apprvdMSheetDH:dom.get("depthOrHeight" + records.getRecord(j).getId()).value,
		 apprvdMSheetQuantity:dom.get("quantity" + records.getRecord(j).getId()).value, 

		 apprvdMSheetDeduction:'',
		 consumedCQMBMSheetNo:'',
		 consumedCQMBMSheetLength:'',
		 consumedCQMBMSheetWidth:'',
		 consumedCQMBMSheetDH:'',
		 consumedCQMBMSheetQuantity:'',
		 
		 curCumlvCQMBMSheetQuantity:'0.00',
		 hiddenCurCumlvCQMBMSheetQuantity:'',
		 hiddenCurCQMBMSheetQuantity:''
		}); 
		var rec = mbChangeQtyConsumedMSheetTable.getRecord(parseInt(count));
		disableMBExtraConsumedMsheetFields(rec,"ConsumedChangeQty");
		count++;
		if(dom.get("identifier" + records.getRecord(j).getId()).value=='D'){
		   	 dom.get("apprvdMSheetDeduction"+rec.getId()).checked=true;
		   	 deductionTotal=deductionTotal+getNumber(dom.get("quantity"+records.getRecord(j).getId()).value);
	   	} 
	   	else{
	   	 	dom.get("apprvdMSheetDeduction"+rec.getId()).checked=false;
	   	 	nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("quantity"+records.getRecord(j).getId()).value);  
	   	 }	
	   	 
	   	 if(rec.getData("hiddenCurCumlvCQMBMSheetQuantity")=="")
	   	 	mbChangeQtyConsumedMSheetTable.updateCell(rec,mbChangeQtyConsumedMSheetTable.getColumn('hiddenCurCumlvCQMBMSheetQuantity'),"0.00");
	   	 if(rec.getData("hiddenCurCQMBMSheetQuantity")=="")
	   	 	mbChangeQtyConsumedMSheetTable.updateCell(rec,mbChangeQtyConsumedMSheetTable.getColumn('hiddenCurCQMBMSheetQuantity'),"0.00"); 

		dom.get("apprvdMSheetDeduction"+rec.getId()).disabled='true';
	    dom.get("apprvdMSheetDeduction"+rec.getId()).readOnly='true';
	    
    }
   } 
  	if(recCount==0){	
		alert("No Estimate Measurement details Defined for this Activity");
		return false; 
  	}
	else{
	    dom.get("estimatedChangeQtyTotal").innerHTML=nonDeductionTotal-deductionTotal;
	    var msCont = document.getElementById('mbCQConsumedMSheetDetailsContainer'); 
		msCont.style.display='block';
		msCont.style.top = '450px';
		msCont.style.left = '80px';
		msCont.style.width = '75%';
		msCont.style.height = '40%';
		document.getElementById('minimizeCQConsumedMsheet').innerHTML='-';
		document.getElementById('minimizeCQConsumedMsheet').title='Minimize'; 
	    document.getElementById('detailedMBCQConsumedMSheetTable').style.display='block';
		document.getElementById('mbCQConsumedMSheetTable').style.display='block';
		document.getElementById('buttonsCQConsumedItem').style.display='block';
		reLoadCQConsumedMsheet(dom.get("wOActivityIdCQ").value);		
	    calculateExtraMSheetTotal("ConsumedChangeQty");
		window.scroll(0,0);
		
		<s:if test="%{((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
	   	 	disableExtraMSheetTable("ConsumedChangeQty"); 
	   	</s:if>
   	}
}

consumedChangeQtyMSheetFailureHandler=function(req,res) {
	alert("Unable to load Estimate Measurement Sheet");
}      


function addExtraItems(){
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
	window.open("${pageContext.request.contextPath}/measurementbook/measurementBook!loadSerachForActivity.action?activitySearchType=ExtraItems&workorderNo="+workorder_no+"&workOrderId="+workorder_id+"&estimateId="+estimate_id,"",
				"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
}

function updateExtraItems(elemValue) {
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
				for(var j=0;j<extraItemsDataTable.getRecordSet().getLength() && activity_id!='';j++){
    		       if(activity_id==extraItemsDataTable.getRecordSet().getRecord(j).getData('workOrderActivity')){
	    	       		isError=true;
	    	       		break;
	    	       }
	    	       else {
	    	       		isError=false;
	    	       }
	    	    }
	    	    if(!isError) {
					for(var k=0;k<mbDataTable.getRecordSet().getLength() && activity_id!='';k++){
    	       			if(activity_id==mbDataTable.getRecordSet().getRecord(k).getData('workOrderActivity')){
		    	       		isError=true;
	    	       			break;
	    	       		}
	    	       		else {
	    	       			isError=false;
	    	       		}
 	    	    	 }
	    	    }
	    	    
				if(!isError) {
					setupExtraItemsDetails(activity_id,workorder_id);
				}
			}
		}
	}
}

function setupExtraItemsDetails(activity_id,workorder_id){

    dom.get("mb_error").style.display='none';
	dom.get("mb_error").innerHTML='';
	var isTenderPercentageTypeValue=document.getElementById("isTenderPercentageType").value;
	var mbPercentagelevelValue=document.getElementById("mbPercentagelevel").value;
	var mbHeaderVal = $F('id');
	if(mbHeaderVal == null || mbHeaderVal == '')
		mbHeaderVal = -1;
    makeJSONCall(["xActId","xSORCode","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","woMsheetSize","xIsExtraItem"], 
    	'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!activityDetails.action',
    	{woActivityId:activity_id,workOrderId:workorder_id,mbHeaderId:mbHeaderVal,isTenderPercentageType:isTenderPercentageTypeValue,mbPercentagelevel:mbPercentagelevelValue},myExtraItemsSuccessHandler,myExtraItemsFailureHandler) ;
}


myExtraItemsSuccessHandler = function(req,res){
    var resultdatas=res.results;
    addExtraItemsInYUI(resultdatas);
}
function addExtraItemsInYUI(resultdatas){
	if(resultdatas[0].xSORUOM!=''){
				extraItemsDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
						workOrderMsheetSize:resultdatas[0].woMsheetSize,
                        SlNo:extraItemsDataTable.getRecordSet().getLength()+1,
                        Code:resultdatas[0].xSORCode,
                        Description:resultdatas[0].xSORSummary,
                        UOM:resultdatas[0].xSORUOM,                        
                        extraItemsrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
			estQuantity:resultdatas[0].xApprdQunty,						
			prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
			isExtraItemCQ:resultdatas[0].xIsExtraItem,
                        FullDescription:resultdatas[0].xSORDesc});
       }
     // Add Non-SOR Activity
       if(resultdatas[0].xNonSORUOM!=''){
     			extraItemsDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
     					workOrderMsheetSize:resultdatas[0].woMsheetSize,
     					SlNo:extraItemsDataTable.getRecordSet().getLength()+1,
                        Description:resultdatas[0].xNonSORSummary,
                        UOM:resultdatas[0].xNonSORUOM,                        
                        extraItemsrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
			estQuantity:resultdatas[0].xApprdQunty,						
			prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
			isExtraItemCQ:resultdatas[0].xIsExtraItem,
                        FullDescription:resultdatas[0].xNonSORDesc});
        }
 
}
myExtraItemsFailureHandler= function(){
    dom.get("mb_error").style.display='block';
	dom.get("mb_error").innerHTML='Unable to load activity details';
}

</script>
<div class="errorstyle" id="changeQuantity_error" style="display:none;"></div>

<table id="extraItemsHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
	    	<td colspan="9" class="headingwk" style="border-right-width: 0px">
	    		<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	    		<div class="headplacer" ><s:text name="revisionEstimate.changeQuantity" /></div>
	    	</td>
           	<td  align="right" id="addExtraItemsButtn" class="headingwk" style="border-left-width: 0px">
           	   	<a id="addExtraItemsRow" href="#" onclick="addExtraItems();"><img height="16" border="0" width="16" alt="Add Extra Items" src="${pageContext.request.contextPath}/image/add.png" /></a>
            </td>
        </tr> 
              
        <tr>
			<td>
                <div class="yui-skin-sam">
                    <div id="itemsTable"></div>
                    <div id="extraItemsTotal"></div>                                
                </div>
           	</td>
		</tr>
        <tr><td colspan="11" class="shadowwk"></td></tr>
		<tr><td>&nbsp;</td></tr>
</table> 
<script>
makeExtraItemsDataTable();

var extraItemsCount=1;
<s:iterator id="mbIterator" value="mbDetails" status="row_status">
 <s:if test="%{mbHeader.revisionEstimate.id==workOrderActivity.activity.abstractEstimate.id}">
 <s:if test="%{!workOrderActivity.activity.revisionType.toString().equals('EXTRA_ITEM')}">
    extraItemsDataTable.addRow({    
    					workOrderActivity:'<s:property value="workOrderActivity.parent.id"/>',
    					<s:if test="%{workOrderActivity.parent.activity.quantity!='0.0'}">
    						workOrderMsheetSize:'<s:property value="workOrderActivity.woMeasurementSheetList.size"/>',
    					</s:if> 
    					<s:else>
                        	workOrderMsheetSize:'',
                        </s:else>
                        SlNo:extraItemsCount,
                        <s:if test="%{workOrderActivity.activity.schedule}">
                        	Code:'<s:property value="workOrderActivity.activity.schedule.code"/>',
                        	Description:'<s:property value="workOrderActivity.activity.schedule.summaryJS"/>',
                        	UOM:'<s:property value="workOrderActivity.activity.schedule.uom.uom"/>',
                        </s:if>
                        <s:else>
                        	Code:'',
                        	Description:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" escape="false"/>',
                        	UOM:'<s:property value="workOrderActivity.activity.nonSor.uom.uom"/>',                      
                        </s:else>
                        	
                        extraItemsrate:'<s:property value="workOrderActivity.approvedRate"/>',
                        rate:'<s:property value="workOrderActivity.activity.rate"/>',
                        estQuantity:'<s:property value="totalEstQuantity"/>',
                        prevCumlQuantity:'<s:property value="prevCumlvQuantity"/>',
                        quantity:'<s:property value="workOrderActivity.approvedQuantity"/>',
                        mbQuantity:'<s:property value="quantity"/>',
                        <s:if test="%{workOrderActivity.activity.revisionType.toString().equals('ADDITITONAL_QUANTITY')}">
                        	sign:'+',
                        	signValue:'+',
                        	EstdAmt:roundTo('<s:property value="workOrderActivity.activity.amount"/>'), 
                        	TaxAmt:roundTo('<s:property value="workOrderActivity.activity.taxAmount.value"/>'),
                        	Total:roundTo('<s:property value="workOrderActivity.activity.amountIncludingTax.value"/>'),
                        </s:if>
                        <s:else>
                        	sign:'-',
                        	signValue:'-',
                        	EstdAmt:roundTo('<s:property value="workOrderActivity.activity.amount"/>')*-1, 
                        	TaxAmt:roundTo('<s:property value="workOrderActivity.activity.taxAmount.value"/>')*-1,
                        	Total:roundTo('<s:property value="workOrderActivity.activity.amountIncludingTax.value"/>')*-1,
                        </s:else>
                        extraItemsPartRate:'<s:property value="partRate"/>',
                        extraItemsReducedRate:'<s:property value="reducedRate"/>',
                        MbAmt:roundTo('<s:property value="amount.value"/>'),
                        serviceTaxPerc:'<s:property value="workOrderActivity.activity.serviceTaxPerc"/>',                        
                        Delete:'X',     
                        <s:if test='workOrderActivity.activity.revisionType!=null && workOrderActivity.activity.revisionType.toString().equals("EXTRA_ITEM")'>
    						isExtraItemCQ:'true',
    		            </s:if>
    					<s:else>
    						isExtraItemCQ:'false',
    					</s:else>                 
                        <s:if test="%{workOrderActivity.activity.schedule}">
                        FullDescription:'<s:property value="workOrderActivity.activity.schedule.descriptionJS"/>'});
	                    </s:if>
	                    <s:else>
	                    	FullDescription:'<s:property value="workOrderActivity.activity.nonSor.descriptionJS" />'});
	                    </s:else>
                        
                        
    var record = extraItemsDataTable.getRecord(parseInt(extraItemsCount-1)); 
    
    var column = extraItemsDataTable.getColumn('quantity');  
    dom.get("extraItems"+column.getKey()+record.getId()).value = '<s:property value="workOrderActivity.approvedQuantity"/>'; 
       
    var column = extraItemsDataTable.getColumn('mbQuantity');  
    dom.get("extraItems"+column.getKey()+record.getId()).value = '<s:property value="quantity"/>'; 
    
    column= extraItemsDataTable.getColumn('serviceTaxPerc');
    dom.get("extraItems"+column.getKey()+record.getId()).value = '<s:property value="workOrderActivity.activity.serviceTaxPerc"/>'; 
    
    column = extraItemsDataTable.getColumn('sign');
	<s:if test="%{workOrderActivity.activity.revisionType.toString().equals('ADDITITONAL_QUANTITY')}">
         extraItemsDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 0;
         extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('signValue'),'+');                 
    </s:if>
    <s:else>
     	extraItemsDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 1;
        extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn('signValue'),'-'); 
    </s:else>
    
    dom.get("extraItemsEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsEstTotal") - 0.0 + getNumber(record.getData("EstdAmt")));
    dom.get("extraItemsMbTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsMbTotal") - 0.0 + getNumber(record.getData("MbAmt")));
    dom.get("extraItemsTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsTaxTotal") - 0.0 + getNumber(record.getData("TaxAmt")));
    dom.get("extraItemsGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("extraItemsGrandTotal") -0.0 +getNumber(record.getData("Total")));
    extraItemsCount++;	
  </s:if>
  </s:if>    
</s:iterator>  
</script>
