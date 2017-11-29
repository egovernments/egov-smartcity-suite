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
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-rate{
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

</style>
<script>

function afterSORResults(sType,results){
    clearMessage('sor_error');
    document.getElementById("loadImage").style.display='none';
    if(document.abstractEstimateForm.isRateContract.checked==true){
	    if(dom.get('workDetailType').value=="sor")
	    	document.getElementById("loadImageforSor").style.display='none';
	    else if(dom.get('workDetailType').value=="nonSor")
	    	document.getElementById("loadImageforNonSor").style.display='none'; 
    }
    else{
    document.getElementById("loadImage").style.display='none';
    }
    if(results[2].length==0){
     if(dom.get('workDetailType').value=="nonSor"){
     	showMessage('sor_error','No non-SORs for the given estimate date');
     }else{
     	showMessage('sor_error','No SORs for the given estimate date');
    	}
	}
}

function sorSearchParameters(){
	
	if(dom.get('scheduleCategory').value!=-1){
	   	return "estimateDate="+dom.get('estimateDate').value+"&scheduleCategoryId="+dom.get('scheduleCategory').value;
	}
	else{
		return "estimateDate="+dom.get('estimateDate').value+rcRelatedParam;
	}
}
function calculateSOR(elem,recordId){
  	  var quantElemId = 'sorquantity' + recordId;
  	  if(elem.id==quantElemId){
	  	if(!checkUptoFourDecimalPlace(elem,dom.get('sor_error'),"Estimated Quantity"))
	  		return false;
	  }	
      if(!validateNumberInTableCell(sorDataTable,elem,recordId)) return;
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTaxAmt=getNumber(record.getData("TaxAmt"));
      oldTotal=getNumber(record.getData("Total"));
      taxRate=dom.get("sorserviceTaxPerc"+record.getId()).value;
    
      if(elem.id==quantElemId){
        sorDataTable.updateCell(record,sorDataTable.getColumn('EstdAmt'),roundTo(elem.value*getNumber(dom.get("estimaterate"+record.getId()).value)));
      }

      sorDataTable.updateCell(record,sorDataTable.getColumn('TaxAmt'),roundTo(taxRate*getNumber(record.getData("EstdAmt"))/100.0));
      taxAmt=getNumber(record.getData("TaxAmt"));

      sorDataTable.updateCell(record,sorDataTable.getColumn('Total'),roundTo(getNumber(record.getData("EstdAmt"))+taxAmt));
      
	  dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - oldEstAmt +getNumber(record.getData("EstdAmt")));
	  dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") -oldTaxAmt +taxAmt);
	  dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -oldTotal +getNumber(record.getData("Total")));
   	
    	
   	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));   	 
}

function recalculateTotalsOnDelete(record){
	  dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") -getNumber(record.getData("EstdAmt")));
	  dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") - getNumber(record.getData("TaxAmt")));
	  dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -getNumber(record.getData("Total")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="/egworks/resources/erp2/images/help.gif" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function createReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
     //var id="sor"+oColumn.getKey()+oRecord.getId();
    var id="estimate"+oColumn.getKey()+oRecord.getId();
    var fieldName="sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var readOnlyTextboxFormatter = createReadOnlyTextBoxFormatter(5,5);

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="sor"+oColumn.getKey()+oRecord.getId();
    var fieldName="sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateSOR(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(11,13);
var stFormatter = createTextBoxFormatter(5,5);

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
    var fieldName = "sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
  	fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);


function createRateRecIdHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = sorDataTable.getRecordSet().getLength();
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName=oColumn.getKey()+oRecord.getId();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var hiddenRateRecIdFormatter = createRateRecIdHiddenFormatter(5,5);

function createSorRateHiddenFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    //var fieldName="sor"+oColumn.getKey()+oRecord.getId();
    var fieldName = "sorActivities[" + oRecord.getCount() + "].sorRate";
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var sorRateHiddenFormatter = createSorRateHiddenFormatter(5,5);

var searchSelectionHandler = function(sType, arguments) { 
            dom.get("search").value='';
 	        var oData = arguments[2];
            var mySuccessHandler = function(req,res) {
                dom.get("sor_error").style.display='none';
                dom.get("sor_error").innerHTML='';
                records=sorDataTable.getRecordSet();
                
                for(i=0;i<records.getLength();i++){
                    
                   if(sorDataTable.getRecord(i).getData("schedule")==res.results[0].Id){
                      dom.get("sor_error").style.display='';
                      document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.duplicate"/>';
                      return;
                   }
                }
                sorDataTable.addRow({schedule:res.results[0].Id,Code:res.results[0].Code,SlNo:sorDataTable.getRecordSet().getLength()+1,Description:res.results[0].Description,UOM:res.results[0].UOM,sorrate:res.results[0].UnitRate,rate:res.results[0].UnitRate,Delete:'X',FullDescription:res.results[0].FullDescription});
                getUOMFactor(res.results[0].UOM,sorDataTable.getRecord(sorDataTable.getRecordSet().getLength()-1));
                if('<s:property value="%{errorCode}" />'=='estimate.workvalue.null')
            	{
            		dom.get('errorstyle').style.display='none';
            	}

                
            	
            };
            
	        var myFailureHandler = function() {
	            dom.get("sor_error").style.display='';
	            document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        };


	        makeJSONCall(["Id","Description","Code","UOM","UnitRate","FullDescription"],'${pageContext.request.contextPath}/masters/scheduleOfRateSearch-findSORAjax.action',{estimateDate:document.getElementById("estimateDate").value,sorID:oData[1]},mySuccessHandler,myFailureHandler) ;

		}

var sorDataTable;
var makeSORDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sorColumnDefs = [
            {key:"schedule", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false},
            {key:"SlNo",label:'Sl No', sortable:false, resizeable:false},
            {key:"Code",label:'Code', sortable:false, resizeable:false},
            {key:"Description", formatter:descriptionFormatter,sortable:false, resizeable:false},
            {key:"UOM", sortable:false, resizeable:false},
            {key:"sorrate", label:'Rate', formatter:sorRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'Unit Rate',hidden:true,  formatter:readOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"quantity",label:'Estimated Quantity<span class="mandatory"></span>', formatter:textboxFormatter,sortable:false, resizeable:false},
            {key:"EstdAmt",label:'Estimated  Amount', sortable:false, resizeable:false},
            {key:"serviceTaxPerc",label:'Service VAT%', formatter:stFormatter,sortable:false, resizeable:false},
            {key:"TaxAmt",label:'Service/ VAT Amount',sortable:false, resizeable:false},
            {key:"Total",label:'Total', sortable:false, resizeable:false},
            {key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false}
        ];

        var sorDataSource = new YAHOO.util.DataSource();
        sorDataTable = new YAHOO.widget.DataTable("sorTable",
                sorColumnDefs, sorDataSource,{MSG_EMPTY:"<s:text name='estimate.sor.initial.table.message'/>"});
         sorDataTable.subscribe("cellClickEvent", sorDataTable.onEventShowCellEditor); 
         
         sorDataTable.on('cellClickEvent',function (oArgs) {
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
            var tfoot = sorDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 8;
			th.className= 'whitebox4wk';
			th.innerHTML = '&nbsp;';

			var td = tr.insertCell(1);
			td.className= 'whitebox4wk';
			td.id = 'subTotal';
			td.innerHTML = '<span class="bold">Total:</span>';
			addCell(tr,2,'estTotal','0.00');
			addCell(tr,3,'strTotal','');
			addCell(tr,4,'taxTotal','0.00');
			addCell(tr,5,'grandTotal','0.00');
			addCell(tr,6,'filler','');

        return {
            oDS: sorDataSource,
            oDT: sorDataTable
        };
      }

function resetSorTable(){
	sorDataTable.deleteRows(0,sorDataTable.getRecordSet().getLength());
	dom.get("estTotal").innerHTML="0.00";
	dom.get("taxTotal").innerHTML="0.00";
	dom.get("grandTotal").innerHTML="0.00";
}

function getUOMFactor(sorUomId,record) {
	var url2 = '${pageContext.request.contextPath}/estimate/ajaxEstimate-getUOMFactor.action';
	var params = 'uomVal='+sorUomId+'&rate='+dom.get("sorrate"+record.getId()).value;
	var estimaterateVal = "estimaterate"+record.getId();
	
	var ajaxcall = new Ajax.Request(url2, {
		method:'get',parameters:params,onSuccess:function(transport){
			console.log('received!');
			$(estimaterateVal).value = transport.responseText;
			
		}
	});
}

function checkForCategorySelected(){
	if(dom.get("scheduleCategory").value==-1){
		dom.get("sor_error").style.display='';
		document.getElementById("sor_error").innerHTML='Please select the Schedule Category.';
		return false;
	}
	return true;
}

function clearCategoryMessage(){
	dom.get("sor_error").style.display='none';
	document.getElementById("sor_error").innerHTML='';
}

function showProcessImage(event) {
	if(!checkForCategorySelected())
		return false;

	var unicode=event.keyCode? event.keyCode : event.charCode;
	if((unicode==46 || unicode==8) && dom.get("search").value.length==1){
	   document.getElementById("loadImage").style.display='none';
	}
	else if(unicode!=9 && unicode!=37 && unicode!=38 && unicode!=39 && unicode!=40){
	   document.getElementById("loadImage").style.display='';
	}

	return true;
}

</script>

<div id="baseSORTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   SOR
		   <div class="pull-right mb-5 small-note-title"><s:text name="estimate.rate.disclaimer"/></div>
		</div>
	</div>
	<div class="panel-body">
	    <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.scheduleCategory.name" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select onchange="clearCategoryMessage();" headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategory" id="scheduleCategory" cssClass="form-control" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code+' : '+description"/>
			</div>
			<!-- <label class="col-sm-5 control-label add-margin">
			</label> -->

		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    Add SOR
			</label>
			<div class="col-sm-8 add-margin">
				<div id="sorSearch_autocomplete">
				   <div class="right-inner-addon">
					    <input id="search" type="text" name="item" class="form-control" onkeypress="if(event.keyCode==13) return false;return showProcessImage(event);">
					    <i id="loadImage" style="display:none" class="fa fa-circle-o-notch fa-spin"></i>
	    			</div>    
			    	<span id="searchResults"></span>
			    	<egov:autocomplete name="sorSearch" width="70" field="search" url="../masters/scheduleOfRateSearch-searchAjax.action" results="searchResults" handler="searchSelectionHandler" paramsFunction="sorSearchParameters" afterHandler="afterSORResults" />
			    </div> 
			</div>
		</div>
		
		<div class="form-group no-margin-bottom">
			<div class="col-sm-offset-2 col-sm-8">
				<div class="alert alert-danger no-margin mt-5" id="sor_error" style="display:none;"></div>
			</div>
		</div>	
		
		<div class="form-group" id="sorHeaderTable">
			<hr/>
			<div class="yui-skin-sam">
		         <div id="sorTable"></div>
		         <div id="sorTotals"></div>                                
		     </div>
				
		</div>	
		
	</div>
</div>
<!-- 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
         <td colspan="3" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div><div class="headplacer">SOR</div></td>
    </tr>
</table>
<table id="baseSORTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>        
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.scheduleCategory.name" />:</td>
        <td class="whitebox2wk" colspan="2"><s:select onchange="clearCategoryMessage();" headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategory" id="scheduleCategory" cssClass="selectwk" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code+' : '+description"/>
        </td>
	</tr>
    <tr>
      <td width="30%" class="whiteboxwk"><span class="bold">Add SOR:</span></td>
      <td width="50%" class="whitebox2wk">
	    <div class="yui-skin-sam">
		    <div id="sorSearch_autocomplete">
		    	<div><input id="search" type="text" name="item" class="selectwk" onkeypress="if(event.keyCode==13) return false;return showProcessImage(event);"></div>
		    	<span id="searchResults"></span>
		    </div>
	    </div>    
		          
		<td width="20%" class="whitebox2wk"><div><img src="/egworks/resources/erp2/images/loading.gif" />Loading SOR's. Please wait..</div></td>
      </tr>
</table>
<table id="sorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">              
  <tr>
    <td>
     
    </td>
  </tr>
  <tr>
  	<td colspan="11" class="shadowwk"></td>
  </tr>
  <tr><td>&nbsp;</td></tr>
</table>  -->
<script>
makeSORDataTable();
<s:iterator id="soriterator" value="SORActivities" status="row_status">
    sorDataTable.addRow({schedule:'<s:property value="schedule.id"/>',
                        SlNo:'<s:property value="#row_status.count"/>',
                        Code:'<s:property value="schedule.code"/>',
                        Description:'<s:property value="schedule.summaryJS"/>',
                        UOM:'<s:property value="schedule.uom.uom"/>',
                        sorrate:roundTo('<s:property value="sORCurrentRate"/>'),
                        rate:'<s:property value="rate"/>',
                        quantity:roundTo('<s:property value="quantity"/>'),
                        EstdAmt:roundTo('<s:property value="amount.value"/>'),
                        serviceTaxPerc:'<s:property value="serviceTaxPerc"/>',
                        TaxAmt:roundTo('<s:property value="taxAmount.value"/>'),
                        Total:roundTo('<s:property value="amountIncludingTax.value"/>'),
                        Delete:'X',
                        FullDescription:'<s:property value="schedule.descriptionJS"/>'});
                        
     
    var record = sorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
    var column = sorDataTable.getColumn('quantity');  
    dom.get("sor"+column.getKey()+record.getId()).value = roundTo('<s:property value="quantity"/>');
    
    column= sorDataTable.getColumn('serviceTaxPerc');
    dom.get("sor"+column.getKey()+record.getId()).value = '<s:property value="serviceTaxPerc"/>';
    
    dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber(record.getData("EstdAmt")));
    dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") - 0.0 + <s:property value="taxAmount.value"/>);
    dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -0.0 +<s:property value="amountIncludingTax.value"/>);
  
    
</s:iterator>
    
</script>  
