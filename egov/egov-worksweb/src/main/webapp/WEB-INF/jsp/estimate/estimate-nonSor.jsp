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
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
.yui-dt-col-NonSorEstdAmt{
	text-align:right;
}
.yui-dt-col-NonSorTaxAmt{
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
.yui-dt-col-serviceTaxPerc{
	text-align:right;
}
</style>
<script>

var uomDropdownOptions=[{label:"--- Select ---", value:"0"},

    <s:iterator var="s" value="dropdownData.uomList" status="status">  
    {"label":"<s:property value="%{uomCategory.category}"/> -- <s:property value="%{uom}" />" ,
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
function calculateNonSOR(elem,recordId){
	record=nonSorDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
	if(!validateNumberInTableCell(nonSorDataTable,elem,recordId)) return;
	if(elem.value!=''){
		if(!validateNonSORDescription(recordId)) return;
		if(!validateNonSorUom(recordId)) return;
	}
	 	
    oldNonSorEstAmt=getNumber(record.getData("NonSorEstdAmt"));
	oldNonSorTaxAmt=getNumber(record.getData("NonSorTaxAmt"));
	oldNonSorTotal=getNumber(record.getData("NonSorTotal"));
	nonSorTaxRate=getNumber(dom.get("nonsorserviceTaxPerc"+record.getId()).value);   
	nonSorUnitRate=getNumber(dom.get("nonsorrate"+record.getId()).value);   
	nonSorEstdQty=getNumber(dom.get("nonsorquantity"+record.getId()).value);      
    nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('NonSorEstdAmt'),roundTo(nonSorEstdQty*nonSorUnitRate));      
	nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('NonSorTaxAmt'),roundTo(nonSorTaxRate*record.getData("NonSorEstdAmt")/100.0));
	nonSorTaxAmt=getNumber(record.getData("NonSorTaxAmt"));
	if(isNaN(nonSorTaxAmt)) nonSorTaxAmt=0;      
	nonSorDataTable.updateCell(record,nonSorDataTable.getColumn('NonSorTotal'),roundTo(getNumber(record.getData("NonSorEstdAmt"))+nonSorTaxAmt));
  
	dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") -oldNonSorEstAmt + getNumber(record.getData("NonSorEstdAmt")));
	dom.get("nonSorTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorTaxTotal") -oldNonSorTaxAmt +nonSorTaxAmt);
	dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") -oldNonSorTotal +getNumber(record.getData("NonSorTotal")));
	document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}
function recalculateNonSorTotalsOnDelete(record){
	if(dom.get("nonsorrate"+record.getId()).value !="" && dom.get("nonsorquantity"+record.getId()).value!=""){
	  dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") -getNumber(record.getData("NonSorEstdAmt")));
	  dom.get("nonSorTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorTaxTotal") - getNumber(record.getData("NonSorTaxAmt")));
	  dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") - getNumber(record.getData("NonSorTotal")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
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


var nonSorDataTable;
var makeNonSORDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var nonSorColumnDefs = [ 
		{key:"NonSorId", hidden:true,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"nonSordescription", label:'Description<span class="mandatory"></span>', formatter:textboxDescFormatter, sortable:false, resizeable:true},		
		{key:"Uom", label:'UOM<span class="mandatory"></span>', formatter:"dropdown", dropdownOptions:uomDropdownOptions},
		{key:"nonSorUom", hidden:true, formatter:nonSorHiddenFormatter, sortable:false, resizeable:false},
		{key:"rate",label:'Unit Rate<span class="mandatory"></span>', formatter:textboxFormatter,sortable:false, resizeable:false},
		{key:"quantity",label:'Estimated Quantity<span class="mandatory"></span>', formatter:textboxFormatter,sortable:false, resizeable:false},
		{key:"NonSorEstdAmt",label:'Estimated  Amount', sortable:false, resizeable:false},
		{key:"serviceTaxPerc",label:'Service VAT%', formatter:stFormatter,sortable:false, resizeable:false},
		{key:"NonSorTaxAmt",label:'Service/ VAT Amount',sortable:false, resizeable:false},
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
		if (column.key == 'NonSorDelete') { 			
				recalculateNonSorTotalsOnDelete(record);	
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	
	nonSorDataTable.on('dropdownChangeEvent', function (oArgs) {
	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Uom'){
            var selectedIndex=oArgs.target.selectedIndex;
            this.updateCell(record,this.getColumn('nonSorUom'),uomDropdownOptions[selectedIndex].value);
            if(!validateNonSorUom(record)) return;
        }
	});
	
    
	var tfoot = nonSorDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 6;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'nonSorSubTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'nonSorEstTotal','0.00');
	addCell(tr,3,'strTotal','');
	addCell(tr,4,'nonSorTaxTotal','0.00');
	addCell(tr,5,'nonSorGrandTotal','0.00');
	addCell(tr,6,'filler','');
		

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
	dom.get("nonSorTaxTotal").innerHTML="0.00";
	dom.get("nonSorGrandTotal").innerHTML="0.00";
}

function validateNonSorUomDropDown() {
	var i = 0;
	while (nonSorDataTable.getRow(i) != null) {
		if (nonSorDataTable.getRow(i).childNodes[3].childNodes[0].childNodes[0].value == "0") {
			alert('Please select Non Sor Uom value of Sl No ' + (i+1));
			return false;
		}
		i++;
	}
	return true;
}
</script>		
      

<div id="nonSorHeaderTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   Non-SOR
		   <div class="pull-right">
		   
		      <a id="addnonSorRow" href="javascript:void(0);" class="btn btn-primary" 
	   	       onclick="nonSorDataTable.addRow({SlNo:nonSorDataTable.getRecordSet().getLength()+1});return false;">
	   	       <i class="fa fa-plus"></i> Add Non-SOR
	   	    </a>
		   
		   </div>
		</div>
	</div>
	<div class="panel-body">		
		<div class="alert alert-danger" id="nonsor_error" style="display:none;"></div>
		
	    <div class="form-group">
	      <div class="yui-skin-sam">
                <div id="nonSorTable"></div>                    	
               <div id="nonSorTotals"></div>  
          </div>
	    </div>
	</div>
</div>

	<script>
		makeNonSORDataTable();
		<s:iterator id="nonsoriterator" value="NonSORActivities" status="row_status">
            nonSorDataTable.addRow({NonSorID:'<s:property value="nonSor.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    nonSordescription:'<s:property value="nonSor.descriptionJS" escape="false"/>',
                                    Uom:'<s:property value="nonSor.uom.id"/>',
                                    nonSorUom:'<s:property value="nonSor.uom.id"/>',
                                    rate:'<s:property value="rate"/>',
                                    quantity:'<s:property value="quantity"/>',
                                    NonSorEstdAmt:roundTo('<s:property value="amount"/>'),
                                    serviceTaxPerc:'<s:property value="serviceTaxPerc"/>',
                                    NonSorTaxAmt:roundTo('<s:property value="taxAmount"/>'),
                                    NonSorTotal:roundTo('<s:property value="amountIncludingTax"/>'),
                                    Delete:'X'});
        var record = nonSorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = nonSorDataTable.getColumn('nonSordescription');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="nonSor.descriptionJS" escape="false"/>';
        
        var column = nonSorDataTable.getColumn('rate');  
        dom.get("nonsor"+column.getKey()+record.getId()).value = roundTo('<s:property value="rate"/>');
        
        var column = nonSorDataTable.getColumn('quantity');  
        dom.get("nonsor"+column.getKey()+record.getId()).value = roundTo('<s:property value="quantity"/>');
    
        column= nonSorDataTable.getColumn('serviceTaxPerc');
        dom.get("nonsor"+column.getKey()+record.getId()).value = '<s:property value="serviceTaxPerc"/>';
        
        column = nonSorDataTable.getColumn('Uom');
        for(i=0; i < uomDropdownOptions.length; i++) {
            if (uomDropdownOptions[i].value == '<s:property value="nonSor.uom.id"/>') {
                nonSorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
            }
        }
        
        dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") - 0.0 + getNumber(record.getData("NonSorEstdAmt")));
        dom.get("nonSorTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorTaxTotal") - 0.0 + getNumber(record.getData("NonSorTaxAmt")));
        dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") - 0.0 + getNumber(record.getData("NonSorTotal")));       
        </s:iterator>
	</script>       

