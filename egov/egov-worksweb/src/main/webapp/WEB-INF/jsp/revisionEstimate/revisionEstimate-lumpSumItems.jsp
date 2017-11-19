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
	.yui-dt-col-NonSorEstdAmt{
		text-align:right;
	}
	.yui-dt-col-NonSorTotal{
		text-align:right;
	}	
	.yui-dt-col-rate{
		text-align:right;
	}		
	.yui-dt-col-quantity{
		text-align:right;
	}
</style>


<script>
function calculateNonSORRate(elem,recordId){
 dom.get('error'+elem.id).style.display='none';
 if(elem.readOnly==true) return;
 record=nonSorDataTable.getRecord(recordId);
 dom.get("nonsor_error").style.display='none'; 
 document.getElementById("nonsor_error").innerHTML='';
	if(!validateNumberInTableCell(nonSorDataTable,elem,recordId)) return;
	
 	calculateNonSOR(elem,recordId);
 }

var uomDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.uomList" status="status">  
    {"label":"<s:property value="%{egUomcategory.category}"/> -- <s:property value="%{uom}" />" ,
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]

function calculateNonSOR(elem,recordId){
    if(elem.readOnly==true) return;
	record=nonSorDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
	if(!validateNumberInTableCell(nonSorDataTable,elem,recordId)) return;
	if(elem.value!=''){
		if(!validateNonSORDescription(recordId)) return;
		if(!validateNonSorUom(recordId)) return;
	}
	var nonsorQuantElemId = 'nonsorquantity' + recordId;
 	var nonsorRateElemId='nonsorrate'+recordId;
	if(elem.id==nonsorQuantElemId){
  		if(!checkUptoFourDecimalPlace(elem,dom.get('nonsor_error'),"Estimated Quantity"))
  			return false;  
  	}
  	else if(elem.id==nonsorRateElemId){
  		if(!checkUptoFourDecimalPlace(elem,dom.get('nonsor_error'),"Unit Rate"))
  			return false;  
  	}	
	 	
    oldNonSorEstAmt=getNumber(record.getData("NonSorEstdAmt"));
	oldNonSorTotal=getNumber(record.getData("NonSorTotal"));   
	nonSorUnitRate=getNumber(dom.get("nonsorrate"+record.getId()).value);   
	nonSorEstdQty=getNumber(dom.get("nonsorquantity"+record.getId()).value);      
	 
    nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('NonSorEstdAmt'),roundTo(nonSorEstdQty*nonSorUnitRate));          
	nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('NonSorTotal'),roundTo(getNumber(record.getData("NonSorEstdAmt"))));
  
	dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") -oldNonSorEstAmt + getNumber(record.getData("NonSorEstdAmt")));
	dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") -oldNonSorTotal +getNumber(record.getData("NonSorTotal")));
	changeQuantityGrandTotal = 0;
	if(document.getElementById("changeQuantityGrandTotal"))
		changeQuantityGrandTotal = eval(document.getElementById("changeQuantityGrandTotal").innerHTML);
	document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+changeQuantityGrandTotal);
}

function recalculateNonSorTotalsOnDelete(record){
	if(dom.get("nonsorrate"+record.getId()).value !="" && dom.get("nonsorquantity"+record.getId()).value!=""){
	  dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") -getNumber(record.getData("NonSorEstdAmt")));
	  dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") - getNumber(record.getData("NonSorTotal")));
	  changeQuantityGrandTotal = 0;
	      if(document.getElementById("changeQuantityGrandTotal"))
			  changeQuantityGrandTotal = eval(document.getElementById("changeQuantityGrandTotal").innerHTML);
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+changeQuantityGrandTotal);
	}
}

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="nonsor"+oColumn.getKey()+oRecord.getId();
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='calculateNonSOR(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(10,13);
var stFormatter = createTextBoxFormatter(5,5);

var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
   var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + "nonSor.description";
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='4000' name='"+fieldName+"' onblur='validateNonSORDescription(\""+oRecord.getId()+"\");'/>"
	el.innerHTML = markup;	 	
}

function createNonSorHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + "nonSor.uom.id";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var nonSorHiddenFormatter = createNonSorHiddenFormatter(10,10);

function activityRecordIdHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldValue=oRecord.getId();
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + oColumn.getKey(); 
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var activityRecIdFormatter = activityRecordIdHiddenFormatter(10,10);

var slNoFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldValue=value;
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "].srlNo"; 
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' size='6' />";
    el.innerHTML = markup;
}

var nonSorDataTable;
var makeNonSORDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var nonSorColumnDefs = [ 
		{key:"NonSorId", hidden:true, sortable:false, resizeable:false} ,
		{key:"Sl_No", label:'Sl No', sortable:false, resizeable:false}, 
		{key:"SlNo", label:'Sl No', hidden:true,formatter:slNoFormatter,sortable:false, resizeable:false}, 
		{key:"activityRecordId", hidden:true,formatter:activityRecIdFormatter,sortable:false, resizeable:false},
		{key:"nonSordescription", label:'Description<span class="mandatory">*</span>', formatter:textboxDescFormatter, sortable:false, resizeable:false},		
		{key:"Uom", label:'UOM<span class="mandatory">*</span>', formatter:"dropdown", dropdownOptions:uomDropdownOptions},
		{key:"nonSorUom", hidden:true, formatter:nonSorHiddenFormatter, sortable:false, resizeable:false},
		{key:"nonSorUomValue", hidden:true, sortable:false, resizeable:false},
		{key:"rate",label:'Unit Rate<span class="mandatory">*</span>', formatter:textboxFormatter,sortable:false, resizeable:false},
		{key:"quantity",label:'Estimated<br>Quantity<span class="mandatory">*</span>', formatter:textboxFormatter,sortable:false, resizeable:false,width:80},
		{key:"NonSorEstdAmt",label:'Estimated  Amount', sortable:false, resizeable:false},
		{key:"NonSorTotal",label:'Total', sortable:false, resizeable:false},
		{key:'NonSorDelete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")} 
	];
	var nonSorDataSource = new YAHOO.util.DataSource(); 
	nonSorDataTable = new YAHOO.widget.DataTable("nonSorTable",nonSorColumnDefs, nonSorDataSource, {MSG_EMPTY:"<s:text name='estimate.nonsor.initial.table.message'/>"});
	nonSorDataTable.subscribe("cellClickEvent", nonSorDataTable.onEventShowCellEditor); 
	nonSorDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		
        <s:if test="%{!((sourcepage=='search' ) || (sourcepage=='inbox' 
        	&& model.egwStatus!=null && model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED'))}" >         
        if (column.key == 'NonSorDelete') { 			
				recalculateNonSorTotalsOnDelete(record); 
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('Sl_No'),""+(i+1));
				}
		}
		</s:if>

	});
	
	nonSorDataTable.on('dropdownChangeEvent', function (oArgs) {
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Uom'){
            var selectedIndex=oArgs.target.selectedIndex;
            this.updateCell(record,this.getColumn('nonSorUom'),uomDropdownOptions[selectedIndex].value);
            this.updateCell(record,this.getColumn('nonSorUomValue'),uomDropdownOptions[selectedIndex].label);
            if(!validateNonSorUom(record)) return;
        }
	});
    
	var tfoot = nonSorDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 9; 
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'nonSorSubTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'nonSorEstTotal','0.00');
	addCell(tr,3,'nonSorGrandTotal','0.00');
	addCell(tr,4,'filler','');
	return {
	    oDS: nonSorDataSource,
	    oDT: nonSorDataTable
	};        
}

function validateNonSORDescription(recordId){
	record=nonSorDataTable.getRecord(recordId);
	if(dom.get("nonSordescription"+record.getId()).value==''){  		
  		document.getElementById("nonsor_error").innerHTML='<s:text name="estimate.nonsor.description.null"/>';
  		document.getElementById("nonsor_error").style.display='';
  		dom.get("nonsorrate"+record.getId()).value='';
      	return false;
  	}
  	else{
       	 document.getElementById("nonsor_error").style.display='none';
       	 document.getElementById("nonsor_error").innerHTML='';	
       	 }
	return true;   
}

function validateNonSorUom(recordId){
	record=nonSorDataTable.getRecord(recordId);
	if((dom.get("nonSorUom"+record.getId()).value=='0' || dom.get("nonSorUom"+record.getId()).value=='') && dom.get("nonsorrate"+record.getId()).value!=''){  		
  		document.getElementById("nonsor_error").innerHTML='<s:text name="estimate.nonsor.uom.null"/>';
  		document.getElementById("nonsor_error").style.display='';
  		dom.get("nonsorrate"+record.getId()).value='';
      	return false;
  	}
  	else{
       	 document.getElementById("nonsor_error").style.display='none';
       	 document.getElementById("nonsor_error").innerHTML='';	
       	 }
	return true;   
}

function resetNonSorTable(){
	nonSorDataTable.deleteRows(0,nonSorDataTable.getRecordSet().getLength());
	dom.get("nonSorEstTotal").innerHTML="0.00";
	dom.get("nonSorGrandTotal").innerHTML="0.00";
}

function addRowToNonSor(){
	if(nonSorDataTable.getRecordSet().getLength()=="" || nonSorDataTable.getRecordSet().getLength()<1){
		nonSorDataTable.addRow({Sl_No:nonSorDataTable.getRecordSet().getLength()+1,SlNo:nonSorDataTable.getRecordSet().getLength()+1});return false;
	}else{ 
			var Records= nonSorDataTable.getRecordSet();
			nonSorDataTable.addRow({Sl_No:nonSorDataTable.getRecordSet().getLength()+1,SlNo:eval(dom.get('SlNo' + Records.getRecord(Records.getLength()-1).getId()).value)+1});return false;
		}
}

</script>	
	
<div class="errorstyle" id="nonsor_error" style="display:none;"></div>
<table id="nonSorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">              	
   <tr>
      	<td colspan="9" class="headingwk" style="border-right-width: 0px">
			<div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div>
			<div class="headplacer" ><s:text name="revisionEstimate.lumpSumItems" /></div>
		</td>
		<td  align="right" class="headingwk" style="border-left-width: 0px">
	   		<a id="addnonSorRow" href="#" onclick="addRowToNonSor();"><img height="16" border="0" width="16" alt="Add Non-SOR" src="/egworks/resources/erp2/images/add.png" /></a>
       	</td>
   	</tr> 
   	<tr>
     	<td colspan="10">
     	<div class="yui-skin-sam">
     	    <div id="nonSorTable"></div>                    	
         	<div id="nonSorTotals"></div>  
     	</div>
     	</td>
     </tr>
     <tr>
     	<td colspan="10" class="shadowwk"></td>
     </tr>
	<tr><td>&nbsp;</td></tr>
</table> 
		
<script>
	makeNonSORDataTable();
	var count=1;
	 <s:iterator id="nonsoriterator" value="NonSORActivities" status="row_status">
	 <s:if test="%{revisionType.toString().equals('LUMP_SUM_ITEM')}">
           nonSorDataTable.addRow({NonSorId:'<s:property value="nonSor.id"/>',
           						Sl_No:count,
                                   SlNo:count,
                                   nonSordescription:'<s:property value="nonSor.descriptionJS" escape="false"/>',
                                   Uom:'<s:property value="nonSor.uom.id"/>',
                                   nonSorUom:'<s:property value="nonSor.uom.id"/>',
                                   rate:'<s:property value="rate"/>',
                                   quantity:'<s:property value="quantityString"/>',
                                   NonSorEstdAmt:roundTo('<s:property value="amount"/>'),
                                   NonSorTotal:roundTo('<s:property value="amountIncludingTax"/>'),
                                   Delete:'X'});
        var record = nonSorDataTable.getRecord(parseInt(count-1));
        dom.get("activityRecordId"+record.getId()).value=record.getId();
        var column = nonSorDataTable.getColumn('nonSordescription');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="nonSor.descriptionJS" escape="false"/>';
       
       column = nonSorDataTable.getColumn('rate');  
       dom.get("nonsor"+column.getKey()+record.getId()).value = '<s:property value="rate"/>';
       
       column = nonSorDataTable.getColumn('quantity');  
       dom.get("nonsor"+column.getKey()+record.getId()).value = '<s:property value="quantity"/>';
       
       column = nonSorDataTable.getColumn('Uom');
       for(i=0; i < uomDropdownOptions.length; i++) {
           if (uomDropdownOptions[i].value == '<s:property value="nonSor.uom.id"/>') {
                nonSorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
                nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('nonSorUomValue'),uomDropdownOptions[i].label);
           }
       } 
       dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") - 0.0 + getNumber(record.getData("NonSorEstdAmt")));
       dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") - 0.0 + getNumber(record.getData("NonSorTotal")));
       count++;	
   </s:if>        
   </s:iterator>  
</script>
