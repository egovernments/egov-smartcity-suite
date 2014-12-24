
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}

</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>

function recalculateOverheads(){
  var records= overheadsTable.getRecordSet();
  var i=0;
  while(i<records.getLength()){
    if(records.getRecord(i).getData('Percentage')!=0.0){
        dom.get("amount"+records.getRecord(i).getId()).value= roundTo((getNumericValueFromInnerHTML("grandTotal")+getNumericValueFromInnerHTML("nonSorGrandTotal")+getNumericValueFromInnerHTML("changeQuantityGrandTotal")) * getNumber(records.getRecord(i).getData('Percentage'))/100);
       overheadsTable.updateCell(records.getRecord(i),overheadsTable.getColumn('amount'),roundTo(getNumber(dom.get("amount"+records.getRecord(i).getId()).value)));
    }
    i++;
  }
  recalculateOverHeadTotal(); 
}

function recalculateOverHeadTotal(){
	var recordSet=overheadsTable.getRecordSet();
   	var i=0;
   	var total=0;
    while(i<recordSet.getLength()){
      total=roundTo((eval(dom.get("amount"+recordSet.getRecord(i).getId()).value )+ eval(total))); 
      i++;
    }
    dom.get("overHeadTotalAmnt").innerHTML=total;
    document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}

function validateNumber(elem,recordId){
    validateNumberInTableCell(overheadsTable,elem,recordId);
}

var descriptionOptions=[{label:"-----------------------Select-----------------------", value:"0"}]
overheadLoadHandler = function(req,res){
  results=res.results; 
  descriptionOptions=[{label:"-----------------------Select-----------------------", value:"0"}];
  for(i=0;i<results.length;i++){
     if(results[i].Percentage>0){
    descriptionOptions[i+1]={label:results[i].Text+'-'+results[i].Percentage+'%', value:results[i].Value,percentage:results[i].Percentage,lumpsum:results[i].Lumpsum}
  }else{
  descriptionOptions[i+1]={label:results[i].Text, value:results[i].Value,percentage:results[i].Percentage,lumpsum:results[i].Lumpsum}
  }
  }

  overheadsTable.deleteRows(0,overheadsTable.getRecordSet().getLength());
  overheadsTable.getColumn('Name').dropdownOptions=descriptionOptions;
  overheadsTable.addRow({SlNo:overheadsTable.getRecordSet().getLength()+1});  
}
overheadLoadFailureHandler= function(){
    dom.get("overheads_error").style.display='';
    document.getElementById("overheads_error").innerHTML='<s:text name="estimate.invalid.overhead.parameters"/>';
}

function clearOverheads() {
	descriptionOptions=[{label:"-----------------------Select-----------------------", value:"0"}];
	overheadsTable.deleteRows(0,overheadsTable.getRecordSet().getLength());
	overheadsTable.getColumn('Name').dropdownOptions=descriptionOptions;
	overheadsTable.addRow({SlNo:overheadsTable.getRecordSet().getLength()+1});  
	
	dom.get("overheads_error").style.display='none'
    dom.get("overheads_error").innerHTML=''
    clearMessage('overheads_error')
}

function resetOverheads()
{
   var estimateDate = document.getElementById("estimateDate").value;
   makeJSONCall(["Text","Value","Name","Percentage","Lumpsum"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!overheads.action',{estDate:document.getElementById("estimateDate").value},overheadLoadHandler,overheadLoadFailureHandler) ;
}

function setupDescriptionOptions(natureOfWork) {
}

function createOverheadIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionOverheadValues[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	el.innerHTML = markup;
}
return hiddenFormatter;
}
var overheadIDFormatter = createOverheadIDFormatter(10,10);

function createOverheadTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName="actionOverheadValues[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' readyonly='true' onblur='calculateOverHeadTotal(this,\""+oRecord.getId()+"\");' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var overheadTextboxFormatter = createOverheadTextBoxFormatter(11,11);

var overheadsTable;
var makeOverheadsTable = function() {
	var overheadColumns = [ 
		{key:"overhead", hidden:true, formatter:overheadIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"Name", label:'Name', formatter:"dropdown", dropdownOptions:descriptionOptions,resizeable:true},		
		{key:"Percentage",label:'Percentage', sortable:false, resizeable:false},
		{key:"amount",label:'Amount', formatter:overheadTextboxFormatter,sortable:false, resizeable:false},
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var overheadsDS = new YAHOO.util.DataSource(); 
	overheadsTable = new YAHOO.widget.DataTable("overheadTable",overheadColumns, overheadsDS);	 
	overheadsTable.on('dropdownChangeEvent',function (oArgs) {
		var record = this.getRecord(oArgs.target);
    	var column = this.getColumn(oArgs.target);
    	if(column.key=='Name'){
        	var selectedIndex=oArgs.target.selectedIndex;
    	    for(i=0;i<this.getRecordSet().getLength() && selectedIndex!=0;i++){
    	       if(descriptionOptions[selectedIndex].value==this.getRecordSet().getRecord(i).getData('overhead')){
    	       		dom.get("overheads_error").style.display=''
    	       		dom.get("overheads_error").innerHTML='<s:text name="estimate.overhead.duplicate"/>';
                    this.updateCell(record,this.getColumn('Percentage'),''); 
    	    		this.updateCell(record,this.getColumn('amount'), '');
    	    		this.updateCell(record,this.getColumn('overhead'), '');
    	       		oArgs.target.selectedIndex=0;
    	       		return;
    	       }
    	    }
    	   
    	    dom.get("overheads_error").style.display='none'
    	    dom.get("overheads_error").innerHTML=''
    	    this.updateCell(record,this.getColumn('Percentage'),descriptionOptions[selectedIndex].percentage);
    	    this.updateCell(record,this.getColumn('overhead'),descriptionOptions[selectedIndex].value);
    	    
    	    if(descriptionOptions[selectedIndex].percentage!=0.0){
        	    amount = roundTo((getNumericValueFromInnerHTML("grandTotal")+getNumericValueFromInnerHTML("nonSorGrandTotal")+getNumericValueFromInnerHTML("changeQuantityGrandTotal")) * getNumber(descriptionOptions[selectedIndex].percentage)/100.0);
    	        oldAmount=0;
    	        if(record.getData("amount")!=undefined)
	    	        oldAmount=record.getData("amount");
    	        	
    	        this.updateCell(record,this.getColumn('amount'), amount);
    	        dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") +eval(amount)-eval(oldAmount));
    	      }else{
               if(descriptionOptions[selectedIndex].lumpsum==""){
               this.updateCell(record,this.getColumn('amount'),'0.0');
               this.updateCell(record,this.getColumn('Percentage'),'0.0');
                }else{
                   oldAmount=0;
    	           if(record.getData("amount")!=undefined)
    	               oldAmount=record.getData("amount");
                   this.updateCell(record,this.getColumn('amount'),getNumber(descriptionOptions[selectedIndex].lumpsum));
                   dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") +getNumber(descriptionOptions[selectedIndex].lumpsum)-eval(oldAmount));
                }              
    	    }
    	}	    

	});
	overheadsTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			overheadsTable.addRow({SlNo:overheadsTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') {  	
			if(this.getRecordSet().getLength()>1){	
			    recalculateOverHeadTotalsOnDelete(record);			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
				}
			}
			else
			{
				alert("This row can not be deleted");
			}

		}        
	});
	
	var tfoot = overheadsTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 3;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'overHeadTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'overHeadTotalAmnt','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	overheadsTable.addRow({SlNo:overheadsTable.getRecordSet().getLength()+1});
}


function calculateOverHeadTotal(elem,recordId){
	record=overheadsTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';	
	if(!validateNumberInTableCell(overheadsTable,elem,recordId)) return;
	oldOverheadAmount=getNumber(record.getData("amount"));
	overheadsTable.updateCell(record,overheadsTable.getColumn('amount'),roundTo(getNumber(dom.get("amount"+record.getId()).value)));
	if(getNumericValueFromInnerHTML("overHeadTotalAmnt")!=0) {
		dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") -oldOverheadAmount +getNumber(dom.get("amount"+record.getId()).value));
	}
	else {
		dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") +getNumber(dom.get("amount"+record.getId()).value));
	} 
	 document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}

function recalculateOverHeadTotalsOnDelete(record){
	  dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") -getNumber(record.getData("amount")));
	   document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("changeQuantityGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}
</script>

<table id="overheadsHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">

	<div class="errorstyle" id="overheads_error" style="display:none;"></div>
              <tr>
                <td colspan="5" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer">Overheads</div></td><td class="headingwk" align="right" style="border-left-width: 0px"><input type='button' class="buttonadd" value='Recalculate' onClick='recalculateOverheads();'/></td>
              </tr>
              <tr>
                <td   colspan="6" width="100%">
                <div class="yui-skin-sam">
                    <div id="overheadTable"></div>
                </div>
                <script>
                <s:iterator id="overheadsListiterator" value="dropdownData.overheadsList" status="row_status">
               <s:if test="%{overheadRates[0].percentage>0}">
                     descriptionOptions[<s:property value="#row_status.count"/>] = {label:'<s:property value="name"/>'+'-'+'<s:property value="overheadRates[0].percentage"/>'+'%', value:'<s:property value="id"/>',percentage:'<s:property value="overheadRates[0].percentage"/>',lumpsum:'<s:property value="overheadRates[0].lumpsumAmount.value"/>'}
                </s:if>
		          <s:else>
		          descriptionOptions[<s:property value="#row_status.count"/>] = {label:'<s:property value="name"/>', value:'<s:property value="id"/>',percentage:'<s:property value="overheadRates[0].percentage"/>',lumpsum:'<s:property value="overheadRates[0].lumpsumAmount.value"/>'}
		          </s:else>
                </s:iterator>
                                
                makeOverheadsTable(); 
                amnt=0;
                <s:iterator id="overheadsiterator" value="overheadValues" status="row_status">
		          <s:if test="#row_status.count == 1">
		         
		              overheadsTable.updateRow(0, 
		                                   {overhead:'<s:property value="overhead.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    Name:'<s:property value="overhead.name"/>',
		                                    Percentage:'<s:property value="overhead.overheadRates[0].percentage"/>',
		                                    amount:'<s:property value="amount.value"/>',
		                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      overheadsTable.addRow({overhead:'<s:property value="overhead.id"/>',
                                            SlNo:'<s:property value="#row_status.count"/>',
                                            Name:'<s:property value="overhead.name"/>',
                                            Percentage:'<s:property value="overhead.overheadRates[0].percentage"/>',
                                            amount:'<s:property value="amount.value"/>',
                                            Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                            Delete:'X'});
		          </s:else>
		          
		          record = overheadsTable.getRecord(<s:property value="#row_status.index"/>);
		          column = overheadsTable.getColumn('Name');
                  for(i=0; i < descriptionOptions.length; i++) {
                    if (descriptionOptions[i].value == '<s:property value="overhead.id"/>') {
                       overheadsTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
                    }
                   }
                  dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") - 0.0 + getNumber(record.getData("amount")));
                   
		        </s:iterator>
		        </script>
                </td>
                </tr>
                <tr>
                <td colspan="6" class="shadowwk"></td>
              </tr>

</table> 

