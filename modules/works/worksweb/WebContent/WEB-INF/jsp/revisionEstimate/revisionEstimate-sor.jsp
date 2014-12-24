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
<script src="<egov:url path='js/works.js'/>"></script>
<script>


function afterSORResults(sType,results){
    clearMessage('sor_error');
    document.getElementById("loadImage").style.display='none';
    if(results[2].length==0) showMessage('sor_error','No SORs for given Revision Estimate date');
}

function sorSearchParameters(){
	var curDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	var estimateId = dom.get('originalEstimateId').value; 
	if(dom.get('scheduleCategory').value!=-1){
	   	return "estimateDate="+curDate+"&scheduleCategoryId="+dom.get('scheduleCategory').value+"&estimateId="+estimateId;
	}
	else{
		return "estimateDate="+curDate+"&estimateId="+estimateId; 
	}
}

function calculateSOR(elem,recordId){ 
	var quantElemId = 'sorquantity' + recordId;
	  if(elem.id==quantElemId){
	  	if(!checkUptoFourDecimalPlace(elem,dom.get('sor_error'),"Estimated Quantity"))
	  		return false;
	  }	
    if(elem.readOnly==true) return;
      if(!validateNumberInTableCell(sorDataTable,elem,recordId)) return;
      oldEstAmt=getNumber(record.getData("EstdAmt"));
      oldTaxAmt=getNumber(record.getData("TaxAmt"));
      oldTotal=getNumber(record.getData("Total"));
      taxRate=dom.get("sorserviceTaxPerc"+record.getId()).value;
      var quantElemId = 'sorquantity' + recordId;
      if(elem.id==quantElemId){
    	  sorDataTable.updateCell(record,sorDataTable.getColumn('EstdAmt'),roundTo(elem.value*getNumber(dom.get("estimaterate"+(dom.get("rateRecId"+record.getId()).value)).value)));
      }
      sorDataTable.updateCell(record,sorDataTable.getColumn('TaxAmt'),roundTo(taxRate*getNumber(record.getData("EstdAmt"))/100.0));
      taxAmt=getNumber(record.getData("TaxAmt"));

      sorDataTable.updateCell(record,sorDataTable.getColumn('Total'),roundTo(getNumber(record.getData("EstdAmt"))+taxAmt));
      
	  dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - oldEstAmt +getNumber(record.getData("EstdAmt")));
	  dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") -oldTaxAmt +taxAmt);
	  dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -oldTotal +getNumber(record.getData("Total")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}

function recalculateTotalsOnDelete(record){ 
	  dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") -getNumber(record.getData("EstdAmt")));
	  dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") - getNumber(record.getData("TaxAmt")));
	  dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -getNumber(record.getData("Total")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}
var hint='<a href="#" class="hintanchor" onMouseover="showhint(\'@fulldescription@\', this, event, \'300px\')"><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>'

function checkSorRate(obj,recordId,objId){
var isPartRateName="isPRSOR"+objId;
var isReducedRateName="isRRSOR"+objId;
	if(obj.name==isPartRateName && obj.checked==true){
		document.getElementById(objId).removeAttribute('readonly');
		var tb="sorReducedRate"+recordId;
		var cb="isRRSOR"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.name==isReducedRateName && obj.checked==true){
 		document.getElementById(objId).removeAttribute('readonly');
 		var tb="sorPartRate"+recordId;
		var cb="isPRSOR"+tb;
		document.getElementById(cb).checked=false;
		document.getElementById(tb).value="0.0";
		document.getElementById(tb).setAttribute('readonly', true);
	}
	else if(obj.checked==false){
		document.getElementById(objId).value='0.0';
		document.getElementById(objId).setAttribute('readonly', true);
	}
}

function createReadOnlyTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
     //var id="sor"+oColumn.getKey()+oRecord.getId();
    var id="estimate"+oColumn.getKey()+sorDataTable.getRecordSet().getLength();
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
var textboxFormatter = createTextBoxFormatter(10,10);
var stFormatter = createTextBoxFormatter(5,5);
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
    var fieldName = "sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
  	fieldValue=value;
   	markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);
    
function activityRecordIdHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldValue=oRecord.getId();
    var fieldName = "sorActivities[" + oRecord.getCount() + "]." + oColumn.getKey(); 
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var activityRecIdFormatter = activityRecordIdHiddenFormatter(10,10);    


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
    var id=oColumn.getKey()+sorDataTable.getRecordSet().getLength();
    var fieldName="sor"+oColumn.getKey()+sorDataTable.getRecordSet().getLength();
    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var sorRateHiddenFormatter = createSorRateHiddenFormatter(11,13);

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
                getFactor(res.results[0].UOM,records);
            };
            
	        var myFailureHandler = function() {
	            dom.get("sor_error").style.display='';
	            document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        };
	        var curDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	        makeJSONCall(["Id","Description","Code","UOM","UnitRate","FullDescription"],'${pageContext.request.contextPath}/masters/scheduleOfRateSearch!findSORAjax.action',{estimateDate:curDate,sorID:oData[1]},mySuccessHandler,myFailureHandler) ;
		}
		
var sorDataTable;  
var makeSORDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sorColumnDefs = [
            {key:"schedule", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
            {key:"activityRecordId", hidden:true,formatter:activityRecIdFormatter,sortable:false, resizeable:false},
            {key:"rateRecId", hidden:true,formatter:hiddenRateRecIdFormatter,sortable:false, resizeable:false},
            {key:"SlNo",label:'Sl No', sortable:false, resizeable:false},
            {key:"Code",label:'Code', sortable:false, resizeable:false},
            {key:"Description", formatter:descriptionFormatter,sortable:false, resizeable:false},
            {key:"UOM", sortable:false, resizeable:false},
            {key:"sorrate", label:'Rate', formatter:sorRateHiddenFormatter,sortable:false, resizeable:false},
            {key:"rate",label:'Unit Rate',hidden:true,  formatter:readOnlyTextboxFormatter, sortable:false, resizeable:false},
            {key:"quantity",label:'Estimated<br>Quantity<span class="mandatory">*</span>', formatter:textboxFormatter,sortable:false, resizeable:false,width:80},
            {key:'MSheet',label: "Measurement<br>Sheet",formatter:createPageEditFormatter("${pageContext.request.contextPath}")}, 
            {key:"EstdAmt",label:'Estimated  Amount', sortable:false, resizeable:false},
            {key:"serviceTaxPerc",label:'Service VAT%', formatter:stFormatter,sortable:false, resizeable:false},
            {key:"TaxAmt",label:'Service/VAT<br>Amount',sortable:false, resizeable:false},
            {key:"Total",label:'Total', sortable:false, resizeable:false}, 
            {key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"FullDescription",hidden:true,sortable:false, resizeable:false},
            {key:"sorMSheetLength",label:'Estimate<br>MSheetLength',hidden:true,sortable:false, resizeable:false}
        ];

        var sorDataSource = new YAHOO.util.DataSource();
        sorDataTable = new YAHOO.widget.DataTable("sorTable",
                sorColumnDefs, sorDataSource,{MSG_EMPTY:"<s:text name='estimate.sor.initial.table.message'/>"});
         sorDataTable.subscribe("cellClickEvent", sorDataTable.onEventShowCellEditor); 
          
         sorDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if(column.key=='MSheet'){
	            dom.get("estimateUOM").value=record.getData("UOM"); 
	            dom.get("scheduleId").value=record.getData("schedule"); 
	            dom.get("nonSorSlNum").value=""; 
	            dom.get("recordId").value=record.getId();
	            reLoad(record.getData("schedule"),"");
	            var msCont = document.getElementById('mSheetDetailsContainer');
				msCont.style.display='block';
				msCont.style.top = '450px';
				msCont.style.left = '80px';
				msCont.style.width = '75%';
				msCont.style.height = '40%';
				document.getElementById('min').innerHTML='-';
				document.getElementById('min').title='Minimize';
	         	document.getElementById('detailedEstTable').style.display='block';
				document.getElementById('mSheetTable').style.display='block';
				document.getElementById('buttons').style.display='block';
 				window.scroll(0,0);
	      		<s:if test="%{((sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' )}" >
	    			if(record.getData("sorMSheetLength")==0) {
	                       document.getElementById('mSheetDetailsContainer').style.display='none';
	                       alert("No Estimate Measurement details Defined for this Activity");
					}
	            	else
						disableEstimateMSheetTable(); 
	    		</s:if>
      		}
	       
          <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
            if (column.key == 'Delete') { 
                recalculateTotalsOnDelete(record);
                removeDetails(record.getData("schedule"),"");
                this.deleteRow(record);
                allRecords=this.getRecordSet(); 
                for(i=0;i<allRecords.getLength();i++){
                  this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                }
            }
            </s:if>
      		
      		if(column.key=='quantity'){
	       	if(record.getData("sorMSheetLength")!=0 && record.getData("sorMSheetLength")!="" &&  record.getData("sorMSheetLength")!=undefined) {
	       		dom.get("sor"+"quantity"+record.getId()).disabled=true; 
	       		dom.get("sor"+"quantity"+record.getId()).readOnly=true; 
	       	}
	       }
      		
        });
            var tfoot = sorDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 10; 
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

function getFactor(sorUomId,records) {
	var url2 = '${pageContext.request.contextPath}/estimate/ajaxEstimate!getFactor.action';
	var params = 'uomVal='+sorUomId+'&rate='+dom.get("sorrate"+records.getLength()).value;
	var estimaterateVal = "estimaterate"+records.getLength();
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
<div class="errorstyle" id="sor_error" style="display:none;"></div>

<table id="baseSORTable" width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
    	<td colspan="3" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer">SOR</div></td>
    </tr>
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
		 	 <egov:autocomplete name="sorSearch" width="50" field="search" url="../masters/scheduleOfRateSearch!searchAjax.action" results="searchResults" handler="searchSelectionHandler" paramsFunction="sorSearchParameters" afterHandler="afterSORResults" />          
             <label></label>
             <td width="20%" class="whitebox2wk"><div id="loadImage" style="display:none"><image src="<egov:url path='/images/loading.gif'/>" />Loading SOR's. Please wait..</div></td>
        </td>
      </tr>      		
</table>
   
<table id="sorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
  <td>
  <div class="yui-skin-sam">
      <div id="sorTable"></div>
      <div id="sorTotals"></div>                                
  </div>
  </td>
</tr>
<tr>
	<td colspan="11" class="shadowwk"></td>
</tr>
<tr><td>&nbsp;</td></tr>
</table> 
<script>
makeSORDataTable();
var sorCount=1;
<s:iterator id="soriterator" value="SORActivities" status="row_status">
  <s:if test="%{revisionType.toString().equals('EXTRA_ITEM')}">
    sorDataTable.addRow({schedule:'<s:property value="schedule.id"/>',
                        SlNo:sorCount,
                        Code:'<s:property value="schedule.code"/>',
                        Description:'<s:property value="schedule.summaryJS"/>', 
                        UOM:'<s:property value="schedule.uom.uom"/>',
                        sorrate:'<s:property value="sORCurrentRate"/>',
                        rate:'<s:property value="rate"/>',
                        quantity:'<s:property value="quantityString"/>',
                        EstdAmt:roundTo('<s:property value="amount.value"/>'),
                        serviceTaxPerc:'<s:property value="serviceTaxPerc"/>',
                        TaxAmt:roundTo('<s:property value="taxAmount.value"/>'),
                        Total:roundTo('<s:property value="amountIncludingTax.value"/>'),
                        Delete:'X',
                        sorMSheetLength:'<s:property value="measurementSheetList.size"/>',
                        FullDescription:'<s:property value="schedule.descriptionJS"/>'});
    var record = sorDataTable.getRecord(parseInt(sorCount-1)); 
    dom.get("activityRecordId"+record.getId()).value=record.getId();
    var column = sorDataTable.getColumn('quantity');  
    dom.get("sor"+column.getKey()+record.getId()).value = '<s:property value="quantityString"/>'; 
    column= sorDataTable.getColumn('serviceTaxPerc');
    dom.get("sor"+column.getKey()+record.getId()).value = '<s:property value="serviceTaxPerc"/>'; 
    dom.get("estTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("estTotal") - 0.0 + getNumber(record.getData("EstdAmt")));
    dom.get("taxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("taxTotal") - 0.0 + <s:property value="taxAmount.value"/>);
    dom.get("grandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("grandTotal") -0.0 +<s:property value="amountIncludingTax.value"/>);
    sorCount++;	
  </s:if>    
</s:iterator>  
</script>