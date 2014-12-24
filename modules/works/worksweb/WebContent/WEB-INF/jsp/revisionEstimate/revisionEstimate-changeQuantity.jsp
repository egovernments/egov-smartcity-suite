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

.yui-skin-sam th.yui-dt-col-reChangeQtyMSheet{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-reChangeQtyMSheet{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-reCQMSheetDtls{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-reCQMSheetDtls{
 	border-right:none;
}

.yui-skin-sam th.yui-dt-col-{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-{
 	border-right:none;
}


</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
 function checkChangeQuantityRate(elem,recordId){
 dom.get('error'+elem.id).style.display='none';
 if(elem.readOnly==true) return;
 dom.get("revisionEstimate_error").style.display='none'; 
 document.getElementById("revisionEstimate_error").innerHTML='';
	if(!validateNumberInTableCell(changeQuantityDataTable,elem,recordId)) return;
 
	if(elem.value!='' && getNumber(elem.value)>0){
		<s:if test='%{mbAmtAboveApprovedAmt=="false"}'>
			if(getNumber(elem.value)>=getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)){
				dom.get("revisionEstimate_error").style.display=''; 
	          	document.getElementById("revisionEstimate_error").innerHTML="Please enter New Rate lesser than Approved Rate";
	          	dom.get('error'+elem.id).style.display='';
		      	window.scroll(0,0);
		      	return;
			}
		</s:if>
	}
 	calculateChangeQuantity(elem,recordId); 
 }
function calculateChangeQuantity(elem,recordId){ 
	dom.get('error'+elem.id).style.display='none';
	var tempId="CQserviceTaxPerc"+recordId;
	if((elem.value=='' || trim(elem.value)=='') && tempId!=elem.id) {
		dom.get('error'+elem.id).style.display='';
		return false; 
	}
	var quantElemId = 'CQquantity' + recordId;
	  if(elem.id==quantElemId){
	  	if(!checkUptoFourDecimalPlace(elem,dom.get('changeQuantity_error'),"Estimated Quantity")){
	  		return false;  
	  	}
	  }	
    if(elem.readOnly==true) return;
      if(!validateNumberInTableCell(changeQuantityDataTable,elem,recordId)) return; 
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTaxAmt=getNumber(record.getData("TaxAmt"));
      oldTotal=getNumber(record.getData("Total"));
      taxRate=dom.get("CQserviceTaxPerc"+record.getId()).value;
       var quantElemId = 'CQquantity' + recordId;
      if(elem.id==quantElemId){
      	if(dom.get('signValue'+record.getId()).value=='-') {
      		changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('EstdAmt'),roundTo(-1*elem.value*getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)));
      	}else {
      		changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('EstdAmt'),roundTo(elem.value*getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)));
      	}
      }        
      changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('TaxAmt'),roundTo(taxRate*getNumber(record.getData("EstdAmt"))/100.0));
      taxAmt=getNumber(record.getData("TaxAmt"));

      changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('Total'),roundTo(getNumber(record.getData("EstdAmt"))+taxAmt));
      
	  dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") - oldEstAmt +getNumber(record.getData("EstdAmt")));	 
	  dom.get("changeQuantityTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityTaxTotal") -oldTaxAmt +taxAmt);
	  dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -oldTotal +getNumber(record.getData("Total")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));   	
   	  calculateMBAmount(elem,recordId,record);
}

function calculateMBAmount(elem,recordId,record){  
	  clearMessage('revisionEstimate_error');      
      if(dom.get('CQquantity'+record.getId()).value!='') {
	      if(dom.get('signValue'+record.getId()).value=='-') {
	      	if(getNumber(dom.get('CQquantity'+record.getId()).value)>eval(getNumber(record.getData("estQuantity"))-getNumber(record.getData("prevCumlQuantity")))) {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='Cannot enter quantity less than the available Quantity';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get('CQquantity'+record.getId()).id).style.display='';
	      	 	return;
	      	 }
	       
	      }       	 
    }
}

function recalculateChangeQuantityTotalsOnDelete(record){
	  dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") -getNumber(record.getData("EstdAmt")));
	  //dom.get("changeQuantityMbTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityMbTotal") -getNumber(record.getData("MbAmt")));
	  dom.get("changeQuantityTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityTaxTotal") - getNumber(record.getData("TaxAmt")));
	  dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -getNumber(record.getData("Total")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
	 	  
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createRateRecIdHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = changeQuantityDataTable.getRecordSet().getLength();
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName=oColumn.getKey()+oRecord.getId();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var hiddenRateRecIdFormatter = createRateRecIdHiddenFormatter(5,5);

function createChangeQuantityReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
     //var id="changeQuantity"+oColumn.getKey()+oRecord.getId();
    var id="estimate"+oColumn.getKey()+changeQuantityDataTable.getRecordSet().getLength();
    var fieldName="changeQuantityActivities[" + oRecord.getCount() + "].activity." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var changeQuantityReadOnlyTextboxFormatter = createChangeQuantityReadOnlyTextBoxFormatter(5,5);

function createChangeQuantityTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="CQ"+oColumn.getKey()+oRecord.getId();
    var fieldName="changeQuantityActivities[" + oRecord.getCount() + "].activity." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateChangeQuantity(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var qtytextboxFormatter = createChangeQuantityTextBoxFormatter(6,10);
var stChangeQuantityFormatter = createChangeQuantityTextBoxFormatter(5,5);
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
    var fieldName = "changeQuantityActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
  	fieldValue=value;
   	markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);
 
var signDropdownOptions=[{label:"+", value:"+"},{"label":"-",value:"-" }]

function createChangeQuantityRateHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+changeQuantityDataTable.getRecordSet().getLength();
    var fieldName="changeQuantity"+oColumn.getKey()+changeQuantityDataTable.getRecordSet().getLength();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var changeQuantityRateHiddenFormatter = createChangeQuantityRateHiddenFormatter(8,13);

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
     fieldName = "changeQuantityActivities[" + oRecord.getCount() + "].id";
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
    var fieldName = "changeQuantityActivities[" + oRecord.getCount() + "].activity."+ oColumn.getKey();
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

		
var changeQuantityDataTable;  
var makeChangeQuantityDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var changeQuantityColumnDefs = [
            {key:"workOrderActivity", hidden:true,formatter:activityIDFormatter,sortable:false, resizeable:false},
            {key:"workOrderMsheetSize",hidden:true,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false}, 
            {key:"SlNo",label:'Sl No', sortable:false, resizeable:false},
            {key:"Code",label:'Code', sortable:false, resizeable:false},
            {key:"Description", formatter:descriptionFormatter,sortable:false, resizeable:false,width:120},
            {key:"UOM", sortable:false, resizeable:false},
            {key:"changeQuantityrate", label:'Rate', formatter:changeQuantityRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'Unit Rate',hidden:true,  formatter:changeQuantityReadOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"reChangeQtyMSheet",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"estQuantity",label:'Estimated<br>Quantity', sortable:false, resizeable:false,width:50},
            {key:"prevCumlQuantity",label:'Consumed<br>Quantity', sortable:false, resizeable:false},
            {key:"signValue",hidden:true,formatter:signHiddenFormatter,sortable:false, resizeable:false},
            {key:"sign",label:'',formatter:"dropdown", dropdownOptions:signDropdownOptions},
            {key:"reCQMSheetDtls",label:'',formatter:createPageEditFormatter("${pageContext.request.contextPath}"), sortable:false, resizeable:false},
            {key:"quantity",label:'Change Quantity<span class="mandatory">*</span>',formatter:qtytextboxFormatter, sortable:false, resizeable:false},
            {key:"EstdAmt",label:'Estimated<br>Amount', sortable:false, resizeable:false},                       
            {key:"serviceTaxPerc",label:'Service VAT%', formatter:stChangeQuantityFormatter,sortable:false, resizeable:false},
            {key:"TaxAmt",label:'Service/VAT<br>Amount',sortable:false, resizeable:false},
            {key:"Total",label:'Total', sortable:false, resizeable:false}, 
            {key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"isExtraItemCQ" ,hidden:true,formatter:isExtraItemCQFormatter,sortable:false, resizeable:false},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false},           
        ];

        var changeQuantityDataSource = new YAHOO.util.DataSource();
        changeQuantityDataTable = new YAHOO.widget.DataTable("itemsTable",
                changeQuantityColumnDefs, changeQuantityDataSource,{MSG_EMPTY:"<s:text name='revisionEstimate.initial.table.message'/>"});
         changeQuantityDataTable.subscribe("cellClickEvent", changeQuantityDataTable.onEventShowCellEditor); 
          
         changeQuantityDataTable.on('cellClickEvent',function (oArgs) { 
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
              
            <s:if test="%{!((sourcepage=='search') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >        
            if (column.key == 'Delete') { 
           //  dom.get("wOActivityIdCQ").value=record.getData("workOrderActivity");
                recalculateChangeQuantityTotalsOnDelete(record); 	
                //removeChangeQtyItemDetails(record.getData("workOrderActivity"));
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
                }
		//updateTotalValue();
            }
            </s:if>
      		      		
             if(column.key=='reChangeQtyMSheet'){
               if(record.getData("workOrderMsheetSize")==0) {
                       alert("No Work Order Measurement details found for this Activity"); 
               }
               else{          
		          	var woActivityId=record.getData("workOrderActivity"); 
		          	var mbHeaderId=dom.get('id').value;
		           	makeJSONCall(["estimateNo","revisionType","description","no","Id","uomLength","width","depthOrHeight","identifier","uom","quantity"],'${pageContext.request.contextPath}/measurementbook/ajaxMeasurementBook!getMbChangeQtyMSheetDetails.action',{woActivityId:woActivityId,mbHeaderId:mbHeaderId},estimateMSheetSuccessHandler,estimateMSheetFailureHandler);
	          	}
	       }
	   
	       if(column.key=='reCQMSheetDtls'){
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
	       			dom.get("CQ"+"quantity"+record.getId()).disabled=true; 
	       			dom.get("CQ"+"quantity"+record.getId()).readOnly=true; 
	       		}
	       	}
        });
        changeQuantityDataTable.on('dropdownChangeEvent', function (oArgs) {
		    var record = this.getRecord(oArgs.target);
	        var column = this.getColumn(oArgs.target);
	        if(column.key=='sign'){	        
	            var selectedIndex=oArgs.target.selectedIndex;
	            this.updateCell(record,this.getColumn('signValue'),signDropdownOptions[selectedIndex].value);
	        }
		});
            var tfoot = changeQuantityDataTable.getTbodyEl().parentNode.createTFoot();
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
			addCell(tr,2,'changeQuantityEstTotal','0.00');
			addCell(tr,3,'strTotal','');
			addCell(tr,4,'changeQuantityTaxTotal','0.00');
			addCell(tr,5,'changeQuantityGrandTotal','0.00');
			addCell(tr,6,'filler','');
			addCell(tr,7,'filler','');

        return {
            oDS: changeQuantityDataSource,
            oDT: changeQuantityDataTable
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


function addChangeQuantity(){
	workorder_no = document.getElementById('workorderNo').value;
	workorder_id = document.getElementById('originalWOId').value;
	estimate_id = document.getElementById('originalEstimateId').value;
	/*if(workorder_id=='-1' || workorder_id==''){
		dom.get("revisionEstimate_error").style.display=''; 
        document.getElementById("revisionEstimate_error").innerHTML='<s:text name="mbheader.workorder.null" />';
        window.scroll(0,0);
		return false;
	}
	else if(estimate_id=='' || estimate_id=='-1'){
			dom.get("revisionEstimate_error").style.display=''; 
            document.getElementById("revisionEstimate_error").innerHTML='<s:text name="mbheader.workOrderEstimate.null" />';  
			window.scroll(0,0);
			return false;
	}*/
	window.open("${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!loadSearchForActivity.action?workorderNo="+workorder_no+"&originalWOId="+workorder_id+"&originalEstimateId="+estimate_id,"",
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
			if(activity_id!=null && workorder_id!=null){
				for(var j=0;j<changeQuantityDataTable.getRecordSet().getLength() && activity_id!='';j++){
    		       if(activity_id==changeQuantityDataTable.getRecordSet().getRecord(j).getData('workOrderActivity')){
        	       		isError=true;
	    	       		break;
	    	       }
	    	       else {
	    	       		isError=false;
	    	       }
	    	    }
	    	    
				if(!isError) {
					setTimeout('setupChangeQuantityDetails('+activity_id+','+workorder_id+')',(i*1000));
				}
			}
		}
	}
}

function setupChangeQuantityDetails(activity_id,workorder_id,count){
    dom.get("revisionEstimate_error").style.display='none';
	dom.get("revisionEstimate_error").innerHTML='';
	var revEstId = -1;
	if(document.getElementById('revEstimateId')!=null){
		revEstId = dom.get('revEstimateId').value;
	}
	if(revEstId == null || revEstId == '')
		revEstId = -1;
	var myDataSource = new YAHOO.util.XHRDataSource('${pageContext.request.contextPath}/revisionEstimate/ajaxRevisionEstimate!activityDetails.action');
	myDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
	myDataSource.connXhrMode = "queueRequests";
	myDataSource.responseSchema = {			
                resultsList: "ResultSet.Result",
               	fields: ["xActId","xSORCode","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor","woMsheetSize","xIsExtraItem"]
    };
	var callbackObj = {
            success : function(req,res){
                var resultdatas=res.results;
               	addChangeQuantityInYUI(resultdatas);               
            	
            },
            failure : function(){
                dom.get("revisionEstimate_error").style.display='block';
            	dom.get("revisionEstimate_error").innerHTML='Unable to load activity details';
            }
        };
	myDataSource.sendRequest("?"+toQuery({woActivityId:activity_id,workOrderId:workorder_id,revEstId:revEstId}),callbackObj);
	
}



function addChangeQuantityInYUI(resultdatas){
	if(resultdatas[0].xSORUOM!=''){
				changeQuantityDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
						workOrderMsheetSize:resultdatas[0].woMsheetSize,
                        SlNo:changeQuantityDataTable.getRecordSet().getLength()+1,
                        Code:resultdatas[0].xSORCode,
                        Description:resultdatas[0].xSORSummary,
                        UOM:resultdatas[0].xSORUOM,                        
                        changeQuantityrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
			estQuantity:resultdatas[0].xApprdQunty,						
			prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
			isExtraItemCQ:resultdatas[0].xIsExtraItem,
                        FullDescription:resultdatas[0].xSORDesc});
       }
     // Add Non-SOR Activity
       if(resultdatas[0].xNonSORUOM!=''){
     			changeQuantityDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
     					workOrderMsheetSize:resultdatas[0].woMsheetSize,
     					SlNo:changeQuantityDataTable.getRecordSet().getLength()+1,
                        Description:resultdatas[0].xNonSORSummary,
                        UOM:resultdatas[0].xNonSORUOM,                        
                        changeQuantityrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
			estQuantity:resultdatas[0].xApprdQunty,						
			prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
			isExtraItemCQ:resultdatas[0].xIsExtraItem,
                        FullDescription:resultdatas[0].xNonSORDesc});
        }
}

</script>
<div class="errorstyle" id="changeQuantity_error" style="display:none;"></div>

<table id="changeQuantityHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
	    	<td colspan="9" class="headingwk" style="border-right-width: 0px">
	    		<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	    		<div class="headplacer" ><s:text name="revisionEstimate.changeQuantity" /></div>
	    	</td>
           	<td  align="right" id="addChangeQuantityButtn" class="headingwk" style="border-left-width: 0px">
           	   	<a id="addChangeQuantityRow" href="#" onclick="addChangeQuantity();"><img height="16" border="0" width="16" alt="Add Items" src="${pageContext.request.contextPath}/image/add.png" /></a>
            </td>
        </tr> 
              
        <tr>
			<td>
                <div class="yui-skin-sam">
                    <div id="itemsTable"></div>
                    <div id="changeQuantityTotal"></div>                                
                </div>
           	</td>
		</tr>
        <tr><td colspan="11" class="shadowwk"></td></tr>
		<tr><td>&nbsp;</td></tr>
</table> 
<script>
makeChangeQuantityDataTable();

var changeQuantityCount=1;
<s:iterator id="activityIterator" value="revisionWorkOrder.workOrderEstimates[0].workOrderActivities" status="row_status">
 <s:if test="%{!activity.revisionType.toString().equals('EXTRA_ITEM')}">
    changeQuantityDataTable.addRow({    
    					workOrderActivity:'<s:property value="parent.id"/>',
    					<s:if test="%{parent.activity.quantity!='0.0'}">
    						workOrderMsheetSize:'<s:property value="woMeasurementSheetList.size"/>',
    					</s:if> 
    					<s:else>
                        	workOrderMsheetSize:'',
                        </s:else>
                        SlNo:changeQuantityCount,
                        <s:if test="%{activity.schedule}">
                        	Code:'<s:property value="activity.schedule.code"/>',
                        	Description:'<s:property value="activity.schedule.summaryJS"/>',
                        	UOM:'<s:property value="activity.schedule.uom.uom"/>',
                        </s:if>
                        <s:else>
                        	Code:'',
                        	Description:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
                        	UOM:'<s:property value="activity.nonSor.uom.uom"/>',                      
                        </s:else>
                        	
                        changeQuantityrate:'<s:property value="approvedRate"/>',
                        rate:'<s:property value="activity.rate"/>',
                        estQuantity:'<s:property value="totalEstQuantity"/>',
                        prevCumlQuantity:'<s:property value="prevCumlvQuantity"/>', 
                        quantity:'<s:property value="approvedQuantity"/>',
                        <s:if test="%{activity.revisionType.toString().equals('ADDITITONAL_QUANTITY')}">
                        	sign:'+',
                        	signValue:'+',
                        	EstdAmt:roundTo('<s:property value="activity.amount"/>'), 
                        	TaxAmt:roundTo('<s:property value="activity.taxAmount.value"/>'),
                        	Total:roundTo('<s:property value="activity.amountIncludingTax.value"/>'),
                        </s:if>
                        <s:else>
                        	sign:'-',
                        	signValue:'-',
                        	EstdAmt:roundTo('<s:property value="activity.amount"/>')*-1, 
                        	TaxAmt:roundTo('<s:property value="activity.taxAmount.value"/>')*-1,
                        	Total:roundTo('<s:property value="activity.amountIncludingTax.value"/>')*-1,
                        </s:else>
                        serviceTaxPerc:'<s:property value="activity.serviceTaxPerc"/>',                        
                        Delete:'X',     
                        <s:if test='activity.revisionType!=null && activity.revisionType.toString().equals("EXTRA_ITEM")'>
    						isExtraItemCQ:'true',
    		            </s:if>
    					<s:else>
    						isExtraItemCQ:'false',
    					</s:else>                 
                        <s:if test="%{activity.schedule}">
                        FullDescription:'<s:property value="activity.schedule.descriptionJS"/>'});
	                    </s:if>
	                    <s:else>
	                    	FullDescription:'<s:property value="activity.nonSor.descriptionJS" />'});
	                    </s:else>
                        
                        
    var record = changeQuantityDataTable.getRecord(parseInt(changeQuantityCount-1)); 
    
    var column = changeQuantityDataTable.getColumn('quantity');  
    dom.get("CQ"+column.getKey()+record.getId()).value = '<s:property value="approvedQuantity"/>'; 
    
    column= changeQuantityDataTable.getColumn('serviceTaxPerc');
    dom.get("CQ"+column.getKey()+record.getId()).value = '<s:property value="activity.serviceTaxPerc"/>'; 
    
    column = changeQuantityDataTable.getColumn('sign');
	<s:if test="%{activity.revisionType.toString().equals('ADDITITONAL_QUANTITY')}">
         changeQuantityDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 0;
         changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('signValue'),'+');                 
    </s:if>
    <s:else>
     	changeQuantityDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 1;
        changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('signValue'),'-'); 
    </s:else>
    
    dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") - 0.0 + getNumber(record.getData("EstdAmt")));
    //dom.get("changeQuantityMbTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityMbTotal") - 0.0 + getNumber(record.getData("MbAmt")));
    dom.get("changeQuantityTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityTaxTotal") - 0.0 + getNumber(record.getData("TaxAmt")));
    dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -0.0 +getNumber(record.getData("Total")));
    changeQuantityCount++;	
  </s:if>
</s:iterator>  
</script>
