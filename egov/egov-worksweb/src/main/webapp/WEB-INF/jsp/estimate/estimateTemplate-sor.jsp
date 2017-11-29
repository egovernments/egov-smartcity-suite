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
.yui-dt-col-Code{
	text-align:left;
}
.yui-dt-col-Description{
	text-align:left;
}
.yui-dt-col-SlNo{
	text-align:left;
}
.yui-dt-col-rate{
	text-align:left;
}
.yui-dt-col-UOM{
	text-align:left;
}
.yui-dt-col-Delete{
	text-align:left;
}
</style>

<script>

function afterSORResults(sType,results){
    clearMessage('sor_error');
    document.getElementById("loadImage").style.display='none';
    if(results[2].length==0) showMessage('sor_error','No SORs found')
}

function sorSearchParameters(){
	if(dom.get('scheduleCategory').value!=-1){
	   	return "scheduleCategoryId="+dom.get('scheduleCategory').value;
	}
}

function calculateSOR(elem,recordId){
      if(!validateNumberInTableCell(sorDataTable,elem,recordId)) return;
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTaxAmt=getNumber(record.getData("TaxAmt"));
      oldTotal=getNumber(record.getData("Total"));
      taxRate=dom.get("sorserviceTaxPerc"+record.getId()).value;
      var quantElemId = 'sorquantity' + recordId;
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
    var fieldName = "sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    var fieldValue=value;
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
    var fieldName=oColumn.getKey()+oRecord.getId();
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
                getFactor(res.results[0].UOM,sorDataTable.getRecord(sorDataTable.getRecordSet().getLength()-1));
            };
            
	        var myFailureHandler = function() {
	            dom.get("sor_error").style.display='';
	            document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        };
	var date=new Date();
var month=date.getMonth()+1;
var dd=date.getDate()+"/"+month+"/"+date.getFullYear();

	        makeJSONCall(["Id","Description","Code","UOM","UnitRate","FullDescription"],'${pageContext.request.contextPath}/masters/scheduleOfRateSearch-findSORAjax.action',{sorID:oData[1]},mySuccessHandler,myFailureHandler) ;

		}

var sorDataTable;
var makeSORDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sorColumnDefs = [
            {key:"schedule", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false},
            {key:"SlNo",label:'Sl No', width:50,sortable:false, resizeable:false},
            {key:"Code",label:'Code', width:150,sortable:false, resizeable:false},
            {key:"sorrate", label:'Rate', hidden:true, formatter:sorRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'Unit Rate',hidden:true,  formatter:readOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"Description", width:450,formatter:descriptionFormatter,sortable:false, resizeable:false},
            {key:"UOM", width:250,sortable:false, resizeable:false},
            <s:if test="%{mode!='view'}">
            {key:'Delete',width:60,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            </s:if>
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
                 this.deleteRow(record);
                allRecords=this.getRecordSet();
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
                
            }
            
        });
           

			

        return {
            oDS: sorDataSource,
            oDT: sorDataTable
        };
      }

function resetSorTable(){
	sorDataTable.deleteRows(0,sorDataTable.getRecordSet().getLength());
	
}

function getFactor(sorUomId,record) {
	var url2 = '${pageContext.request.contextPath}/estimate/ajaxEstimate!getFactor.action';
	var params = 'uomVal='+sorUomId+'&rate='+dom.get("rate"+record.getId()).value;
	var estimaterateVal = "estimaterate"+record.getId();
	
	var ajaxcall = new Ajax.Request(url2, {
		method:'get',parameters:params,onSuccess:function(transport){
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

<div id="baseSORTable" class="panel panel-primary" data-collapsed="0" style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title">
			<s:text name="page.title.estimate.SOR" />
		</div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> <s:text
					name="estimate.scheduleCategory.name" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select onchange="clearCategoryMessage();" headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategory" id="scheduleCategory" cssClass="form-control" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code+' : '+description" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> 
				<s:text	name="estimate.addSOR" />
			</label>
			<div class="col-sm-8 add-margin">
				<div id="sorSearch_autocomplete">
					<div class="right-inner-addon">
						<input id="search" type="text" name="item" class="form-control"	onkeypress="if(event.keyCode==13) return false;return showProcessImage(event);">
						<i id="loadImage" style="display: none" class="fa fa-circle-o-notch fa-spin"></i>
					</div>
					<span id="searchResults"></span>
					<egov:autocomplete name="sorSearch" width="50" field="search" url="../masters/scheduleOfRateSearch-searchAjax.action" results="searchResults" handler="searchSelectionHandler" paramsFunction="sorSearchParameters" afterHandler="afterSORResults" />
				</div>
			</div>
		</div>

		<div class="form-group no-margin-bottom">
			<div class="col-sm-offset-2 col-sm-8">
				<div class="alert alert-danger no-margin mt-5" id="sor_error" style="display: none;"></div>
			</div>
		</div>

		<div class="form-group" id="sorHeaderTable">
			<hr />
			<div class="yui-skin-sam">
				<div id="sorTable"></div>
				<div id="sorTotals"></div>
			</div>
		</div>
	</div>
</div>

<script>
makeSORDataTable();
<s:iterator id="soriterator" value="SORActivities" status="row_status">
    sorDataTable.addRow({schedule:'<s:property value="schedule.id"/>',
                        SlNo:'<s:property value="#row_status.count"/>',
                        Code:'<s:property value="schedule.code"/>',
                        Description:'<s:property value="schedule.summaryJS"/>',
                        UOM:'<s:property value="schedule.uom.uom"/>',
                        rate:'<s:property value="rate"/>',
                        Delete:'X',
                        FullDescription:'<s:property value="schedule.descriptionJS"/>'});
                        
     
    var record = sorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
</s:iterator>
<s:if test="%{mode=='view'}">
for(i=0;i<document.estimateTemplateForm.elements.length;i++){
	document.estimateTemplateForm.elements[i].disabled=true;
	document.estimateTemplateForm.elements[i].readonly=true;
} 
</s:if>
    
</script>  
