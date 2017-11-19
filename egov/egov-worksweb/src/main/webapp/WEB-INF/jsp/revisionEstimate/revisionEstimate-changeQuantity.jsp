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

.yui-skin-sam th.yui-dt-col-{
 	border-right:none;
}

.yui-skin-sam td.yui-dt-col-{
 	border-right:none;
}


</style>
<script>
 function checkChangeQuantityRate(elem,recordId){
 dom.get('error'+elem.id).style.display='none';
 dom.get("revisionEstimate_error").style.display='none'; 
 document.getElementById("revisionEstimate_error").innerHTML='';
	if(!validateNumberInTableCell(changeQuantityDataTable,elem,recordId)) return;
 
	if(elem.value!='' && getNumber(elem.value)>0){
		<s:if test='%{mbAmtAboveApprovedAmt=="false"}'>
			if(getNumber(elem.value)>=getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)){
				dom.get("revisionEstimate_error").style.display='';
	          	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.enter.newRate.lesserThan.approvedRate"/>';
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
	if((elem.value=='' || trim(elem.value)=='') ) {
		dom.get('error'+elem.id).style.display='';
		return false; 
	}
	var quantElemId = 'CQquantity' + recordId;
	  if(elem.id==quantElemId){
	  	if(!checkUptoFourDecimalPlace(elem,dom.get('changeQuantity_error'),"Estimated Quantity")){
	  		return false;  
	  	}
	  }	
      if(!validateNumberInTableCell(changeQuantityDataTable,elem,recordId)) return; 
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTotal=getNumber(record.getData("Total"));
      var conversionFactor  = 1;
      if(record.getData("UOMFactor")!='')
    	  conversionFactor  = getNumber(record.getData("UOMFactor"));
      var quantElemId = 'CQquantity' + recordId;
      if(elem.id==quantElemId){
      	if(dom.get('signValue'+record.getId()).value=='-') {
      		changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('EstdAmt'),roundTo(-1*elem.value*getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)*conversionFactor));
      	}else {
      		changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('EstdAmt'),roundTo(elem.value*getNumber(dom.get("changeQuantityrate"+(dom.get("rateRecId"+record.getId()).value)).value)*conversionFactor));
      	}
      }        

      changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('Total'),roundTo(getNumber(record.getData("EstdAmt"))));
      
	  dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") - oldEstAmt +getNumber(record.getData("EstdAmt")));	 
	  dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -oldTotal +getNumber(record.getData("Total")));
	  nonSorGrandTotal = 0;
	  grandTotal = 0;
	  if(document.getElementById("nonSorGrandTotal"))
		  nonSorGrandTotal = eval(document.getElementById("nonSorGrandTotal").innerHTML);
	  if(document.getElementById("grandTotal"))
		  grandTotal = eval(document.getElementById("grandTotal").innerHTML);
	  document.getElementById("estimateValue").value=roundTo(grandTotal+nonSorGrandTotal+eval(document.getElementById("changeQuantityGrandTotal").innerHTML));   	
   	  calculateREAmount(elem,recordId,record);
}

function calculateREAmount(elem,recordId,record){  
	  clearMessage('revisionEstimate_error');      
      if(dom.get('CQquantity'+record.getId()).value!='') {
	      if(dom.get('signValue'+record.getId()).value=='-') {
	      	if(getNumber(dom.get('CQquantity'+record.getId()).value)>eval(getNumber(record.getData("estQuantity"))-getNumber(record.getData("prevCumlQuantity")))) {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.quantity.lessThan.availableQuantity"/>';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get('CQquantity'+record.getId()).id).style.display='';
	      	 	return;
	      	 }
	       
	      }       	 
    }
}

function recalculateChangeQuantityTotalsOnDelete(record){
	  dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") -getNumber(record.getData("EstdAmt")));
	  dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -getNumber(record.getData("Total")));
	  nonSorGrandTotal = 0;
	  grandTotal = 0;
	  if(document.getElementById("nonSorGrandTotal"))
		  nonSorGrandTotal = eval(document.getElementById("nonSorGrandTotal").innerHTML);
	  if(document.getElementById("grandTotal"))
		  grandTotal = eval(document.getElementById("grandTotal").innerHTML);
	  document.getElementById("estimateValue").value=roundTo(grandTotal+nonSorGrandTotal+eval(document.getElementById("changeQuantityGrandTotal").innerHTML));
	 	  
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="/egworks/resources/erp2/images/help.gif" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

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
	    fieldName = "changeQuantityActivities[" + oRecord.getCount() + "].id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
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

var changeQuantityDataTable;  
var makeChangeQuantityDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var changeQuantityColumnDefs = [
            {key:"workOrderActivity", hidden:true,formatter:activityIDFormatter,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false}, 
            {key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
            {key:"Code",label:'<s:text name="column.title.code"/>', sortable:false, resizeable:false},
            {key:"Description", label:'<s:text name="estimate.description"/>',formatter:descriptionFormatter,sortable:false, resizeable:false,width:120},
            {key:"UOM", label:'<s:text name="column.title.UOM"/>',sortable:false, resizeable:false},
            {key:"UOMFactor",hidden:true,sortable:false, resizeable:false},
            {key:"changeQuantityrate", label:'<s:text name="mb.tenderpercentagerate"/>', formatter:changeQuantityRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'<s:text name="column.title.unitrate"/>',hidden:true,  formatter:changeQuantityReadOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"estQuantity",label:'<s:text name="revisionEstimate.changeQuantity.estQty"/>', sortable:false, resizeable:false,width:50},
            {key:"prevCumlQuantity",label:'<s:text name="mb.measurementSheet.Estimate.consumedQty"/>', sortable:false, resizeable:false},
            {key:"signValue",hidden:true,formatter:signHiddenFormatter,sortable:false, resizeable:false},
            {key:"sign",label:'',formatter:"dropdown", dropdownOptions:signDropdownOptions},
            {key:"quantity",label:'<s:text name="re.title.changeQty"/><span class="mandatory">*</span>',formatter:qtytextboxFormatter, sortable:false, resizeable:false},
            {key:"EstdAmt",label:'<s:text name="column.title.estimated.amt"/>', sortable:false, resizeable:false},                       
            {key:"Total",label:'<s:text name="estimate.search.total"/>', sortable:false, resizeable:false}, 
            {key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false}           
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
                recalculateChangeQuantityTotalsOnDelete(record); 	
                this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
                }
            }
            </s:if>
      		      		
        });
        changeQuantityDataTable.on('dropdownChangeEvent', function (oArgs) {
		    var record = this.getRecord(oArgs.target);
	        var column = this.getColumn(oArgs.target);
	        if(column.key=='sign'){	        
	            var selectedIndex=oArgs.target.selectedIndex;
	            this.updateCell(record,this.getColumn('signValue'),signDropdownOptions[selectedIndex].value);
	            calculateChangeQuantity(dom.get('CQquantity'+record.getId()),record.getId());
	        }
		});
            var tfoot = changeQuantityDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 10;
			th.className= 'whitebox4wk'; 
			th.innerHTML = '&nbsp;'; 

			var td = tr.insertCell(1);
			td.className= 'whitebox4wk';
			td.id = 'subTotal';
			td.innerHTML = '<span class="bold">Total:</span>';
			td.colSpan=2;
			addCell(tr,2,'filler','');
			addCell(tr,3,'strTotal','');
			addCell(tr,4,'changeQuantityEstTotal','0.00');
			addCell(tr,5,'changeQuantityGrandTotal','0.00');
			addCell(tr,6,'filler','');

        return {
            oDS: changeQuantityDataSource,
            oDT: changeQuantityDataTable
        };
      }
      
      



function addChangeQuantity(){
	workorder_no = document.getElementById('workorderNo').value;
	workorder_id = document.getElementById('originalWOId').value;
	estimate_id = document.getElementById('originalEstimateId').value;
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
               	fields: ["xActId","xSORCode","xSORDesc","xSORSummary","xSORUOM","xNonSORDesc","xNonSORSummary","xNonSORUOM","xApprdQunty","xApprdRate","xPrevCulmvEntry","xApprdAmt","xWorkOrderNo","xUomFactor"]
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
                        SlNo:changeQuantityDataTable.getRecordSet().getLength()+1,
                        Code:resultdatas[0].xSORCode,
                        Description:resultdatas[0].xSORSummary,
                        UOM:resultdatas[0].xSORUOM,
                        UOMFactor:resultdatas[0].xUomFactor,                        
                        changeQuantityrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
						estQuantity:resultdatas[0].xApprdQunty,						
						prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
                        FullDescription:resultdatas[0].xSORDesc});
       }
       if(resultdatas[0].xNonSORUOM!=''){
     			changeQuantityDataTable.addRow({workOrderActivity:resultdatas[0].xActId,
     					SlNo:changeQuantityDataTable.getRecordSet().getLength()+1,
                        Description:resultdatas[0].xNonSORSummary,
                        UOM:resultdatas[0].xNonSORUOM,
                        UOMFactor:resultdatas[0].xUomFactor,                        
                        changeQuantityrate:resultdatas[0].xApprdRate,
                        rate:resultdatas[0].xApprdRate,
						estQuantity:resultdatas[0].xApprdQunty,						
						prevCumlQuantity:resultdatas[0].xPrevCulmvEntry,
                        FullDescription:resultdatas[0].xNonSORDesc});
        }
}

function validateQuantities(obj,tableName){
	clearMessage('revisionEstimate_error');
	var records= obj.getRecordSet();
    var i,indexRow,qty;
    for(i=0;i<records.getLength();i++){
    	indexRow = records.getRecord(i).getId();
    	domName = tableName+indexRow;
	    if(dom.get(domName)) {
	      	if(getNumber(dom.get(domName).value)<0) {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.lessthan.zero.quantity.error" />';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get(domName).id).style.display='';
	      	 	return false;
	      	}
	      	if(getNumber(dom.get(domName).value)==0) {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.zero.quantity.error" />';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get(domName).id).style.display='';
	      	 	return false;
	      	}
	      	if(dom.get(domName).value=='') {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.zero.quantity.error" />';
	        	window.scroll(0,0);
	      	 	dom.get('error'+dom.get(domName).id).style.display='';
	      	 	return false;
	      	}
		}
    }
    return true;
}

function validateLumpSumItems(obj,tableName, error){
	clearMessage('revisionEstimate_error');
	var records= obj.getRecordSet();
    var i,indexRow,qty;
    for(i=0;i<records.getLength();i++){
    	indexRow = records.getRecord(i).getId();
    	domName = tableName+indexRow;
	    if(dom.get(domName)) {
	      	if(dom.get(domName).value=='') {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML=error;
	        	window.scroll(0,0);
	      	 	return false;
	      	}
		}
	    if(error=='<s:text name="revisionEstimate.enter.nonsor.rate" />')
		{
	    	if(getNumber(dom.get(domName).value)<=0) {
	      	 	dom.get("revisionEstimate_error").style.display=''; 
	        	document.getElementById("revisionEstimate_error").innerHTML='<s:text name="revisionEstimate.enter.nonsor.rate.greater.than.zero" />';
	        	window.scroll(0,0);
	      	 	return false;
	      	}
		}    
    }
    return true;
}
</script>
<div class="errorstyle" id="changeQuantity_error" style="display:none;"></div>

<table id="changeQuantityHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<tr>
	    	<td colspan="9" class="headingwk" style="border-right-width: 0px">
	    		<div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div>
	    		<div class="headplacer" ><s:text name="revisionEstimate.changeQuantity" /></div>
	    	</td>
           	<td  align="right" id="addChangeQuantityButtn" class="headingwk" style="border-left-width: 0px">
           	   	<a id="addChangeQuantityRow" href="#" onclick="addChangeQuantity();"><img height="16" border="0" width="16" alt="Add Items" src="/egworks/resources/erp2/images/add.png" /></a>
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
	<s:if test="%{!activity.revisionType.toString().equals('NON_TENDERED_ITEM') && !activity.revisionType.toString().equals('LUMP_SUM_ITEM')}">
	    changeQuantityDataTable.addRow({    
	    					workOrderActivity:'<s:property value="parent.id"/>',
	                        SlNo:changeQuantityCount,
	                        <s:if test="%{activity.schedule}">
	                        	Code:'<s:property value="activity.schedule.code"/>',
	                        	Description:'<s:property value="activity.schedule.summaryJS"/>',
	                        	UOM:'<s:property value="activity.schedule.uom.uom"/>',
	                        	UOMFactor:'<s:property value="activity.conversionFactor"/>',
	                        </s:if>
	                        <s:else>
	                        	Code:'',
	                        	Description:'<s:property value="activity.nonSor.descriptionJS" escape="false"/>',
	                        	UOM:'<s:property value="activity.nonSor.uom.uom"/>',
	                        	UOMFactor:'<s:property value="activity.conversionFactor"/>',                      
	                        </s:else>
	                        	
	                        changeQuantityrate:'<s:property value="approvedRate"/>',
	                        rate:'<s:property value="activity.rate"/>',
	                        estQuantity:'<s:property value="totalEstQuantity"/>',
	                        prevCumlQuantity:'<s:property value="prevCumlvQuantity"/>', 
	                        quantity:'<s:property value="approvedQuantity"/>',
	                        <s:if test="%{activity.revisionType.toString().equals('ADDITIONAL_QUANTITY')}">
	                        	sign:'+',
	                        	signValue:'+',
	                        	EstdAmt:roundTo('<s:property value="activity.amount"/>'), 
	                        	Total:roundTo('<s:property value="activity.amountIncludingTax.value"/>'),
	                        </s:if>
	                        <s:else>
	                        	sign:'-',
	                        	signValue:'-',
	                        	EstdAmt:roundTo('<s:property value="activity.amount"/>')*-1, 
	                        	Total:roundTo('<s:property value="activity.amountIncludingTax.value"/>')*-1,
	                        </s:else>
	                        Delete:'X',     
	                        <s:if test="%{activity.schedule}">
	                        FullDescription:'<s:property value="activity.schedule.descriptionJS"/>'});
		                    </s:if>
		                    <s:else>
		                    	FullDescription:'<s:property value="activity.nonSor.descriptionJS" />'});
		                    </s:else>
	                        
	                        
	    var record = changeQuantityDataTable.getRecord(parseInt(changeQuantityCount-1)); 
	    
	    var column = changeQuantityDataTable.getColumn('quantity');  
	    dom.get("CQ"+column.getKey()+record.getId()).value = '<s:property value="approvedQuantity"/>'; 
	    
	    
	    column = changeQuantityDataTable.getColumn('sign');
		<s:if test="%{activity.revisionType.toString().equals('ADDITIONAL_QUANTITY')}">
	         changeQuantityDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 0;
	         changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('signValue'),'+');                 
	    </s:if>
	    <s:else>
	     	changeQuantityDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = 1;
	        changeQuantityDataTable.updateCell(record,changeQuantityDataTable.getColumn('signValue'),'-'); 
	    </s:else>
	    
	    dom.get("changeQuantityEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityEstTotal") - 0.0 + getNumber(record.getData("EstdAmt")));
	    dom.get("changeQuantityGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("changeQuantityGrandTotal") -0.0 +getNumber(record.getData("Total")));
	    changeQuantityCount++;
	</s:if>    	
</s:iterator>  
</script>
