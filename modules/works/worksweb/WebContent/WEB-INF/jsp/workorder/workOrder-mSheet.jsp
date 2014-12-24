<style type="text/css">
</style>
<script src="<egov:url path='js/works.js'/>"></script> 
<script type="text/javascript">

var checkboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
	markup="<input type='checkbox' 	 id='"+id+"' />"; 
    el.innerHTML = markup; 
}

function createTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;

    if(oColumn.getKey()=="estimateMSheetId" || oColumn.getKey()=="length" || oColumn.getKey()=="width" || oColumn.getKey()=="depthOrHeight" || oColumn.getKey()=="no" || oColumn.getKey()=="quantity"){
			fieldName="woMeasurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	}
	else if(oColumn.getKey()=="hiddenEstimateMSheetId"){
			fieldName="woMeasurementSheetList[" + oRecord.getCount() + "].measurementSheet.id";
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	}  
    el.innerHTML = markup;
	}
	return textboxFormatter;
} 
var textboxFormatter = createTextBoxFormatter(11,7);
var textboxQtyFormatter = createTextBoxFormatter(12,12);
var textboxNumberFormatter = createTextBoxFormatter(9,7); 


function createTextBoxFormatterForWOMSheet(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
	fieldName= oColumn.getKey()+oRecord.getId();
	markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateWOMSheetQty(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
} 
var textboxFormatterForWOMSheet=createTextBoxFormatterForWOMSheet(11,7);
var textboxQtyFormatterForWOMSheet=createTextBoxFormatterForWOMSheet(12,12);
var textboxNumberFormatterWOMSheet = createTextBoxFormatterForWOMSheet(11,7);

var activityIDFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
	fieldName = "woMeasurementSheetList[" + oRecord.getCount() + "].woActivity.activity.id";
   	markup="<input type='text' id='"+id+"' name='"+fieldName+ "' value='"+value+"' size='6'/>"; 
    el.innerHTML = markup; 
}

var woEstimateMSheetDataTable;
var woEstimateMSheetColumnDefs;
var makeWOEstimateMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    woEstimateMSheetColumnDefs = [ 
	   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
	   	{key:"description",label:'<s:text name="workorder.estimate.msheet.description"/>',sortable:false, resizeable:false},
	   	{key:"number",label:'<s:text name="workorder.estimate.msheet.no"/>',sortable:false, resizeable:false},
	   	{key:"length",label:'<s:text name="workorder.estimate.msheet.length"/>',sortable:false, resizeable:false},
	   	{key:"width",label:'<s:text name="workorder.estimate.msheet.width"/>',sortable:false, resizeable:false},
	   	{key:"depthOrHeight",label:'<s:text name="workorder.estimate.msheet.depthorheight"/>',sortable:false, resizeable:false},
	   	{key:"quantity",label:'<s:text name="workorder.estimate.msheet.quantity"/>',sortable:false, resizeable:false},
	   	{key:"uom",label:'<s:text name="workorder.estimate.msheet.uom"/>',sortable:false, resizeable:false},
	   	{key:"deduction",label:'<s:text name="workorder.estimate.msheet.deduction"/>',formatter:checkboxFormatter,sortable:false, resizeable:false}
	 ];
    var woEstimateMSheetDataSource = new YAHOO.util.DataSource();
    woEstimateMSheetDataTable = new YAHOO.widget.DataTable("woEstimateMSheetTable",woEstimateMSheetColumnDefs, woEstimateMSheetDataSource);
    woEstimateMSheetDataTable.subscribe("cellClickEvent", woEstimateMSheetDataTable.onEventShowCellEditor); 
    woEstimateMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
    });
    var tfoot = woEstimateMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'woEstimateMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'quantityTotal','0');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	return {
        oDS: woEstimateMSheetDataSource,
        oDT: woEstimateMSheetDataTable 
    };
}

var woMSheetDataTable;
var woMSheetColumnDefs;
var makeWOMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor(); 
    woMSheetColumnDefs = [ 
	   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"description",label:'<s:text name="workorder.estimate.msheet.description"/>',sortable:false, resizeable:false,readOnly:true},
		{key:"number",label:'<s:text name="workorder.estimate.msheet.no"/>',formatter:textboxNumberFormatterWOMSheet,sortable:false, resizeable:false,readOnly:true},
		{key:"length",label:'<s:text name="workorder.estimate.msheet.length"/>',formatter:textboxFormatterForWOMSheet,sortable:false, resizeable:false},
		{key:"width",label:'<s:text name="workorder.estimate.msheet.width"/>',formatter:textboxFormatterForWOMSheet,sortable:false, resizeable:false},
		{key:"depthOrHeight",label:'<s:text name="workorder.estimate.msheet.depthorheight"/>',formatter:textboxFormatterForWOMSheet,sortable:false, resizeable:false},
		{key:"quantity",label:'<s:text name="workorder.estimate.msheet.quantity"/>',formatter:textboxQtyFormatterForWOMSheet,sortable:false, resizeable:false},
		{key:"uom",label:'<s:text name="workorder.estimate.msheet.uom"/>',sortable:false, resizeable:false},
		{key:"apprvdEstimateMSheetDeduction",label:'<s:text name="workorder.estimate.msheet.deduction" />',hidden:false,formatter:checkboxFormatter,sortable:false, resizeable:false,readOnly:true},
		{key:"estimateMSheetId", hidden:true,sortable:false, resizeable:false},
	    {key:"activityId", formatter:activityIDFormatter,hidden:true,sortable:false, resizeable:false},
	    {key:"apprvdEstimateMSheetNo", label:'<s:text name="workorder.estimate.msheet.no"/>',hidden:true,sortable:false, resizeable:false},
		{key:"apprvdEstimateMSheetLength", label:'<s:text name="workorder.estimate.msheet.length"/>',hidden:true,sortable:false, resizeable:false},
		{key:"apprvdEstimateMSheetWidth", label:'<s:text name="workorder.estimate.msheet.width"/>',hidden:true,sortable:false, resizeable:false},
		{key:"apprvdEstimateMSheetDH", label:'<s:text name="workorder.estimate.msheet.depthorheight"/>',hidden:true,sortable:false, resizeable:false}
		
  	];
    var woMSheetDataSource = new YAHOO.util.DataSource();
    woMSheetDataTable = new YAHOO.widget.DataTable("woDetailedMSheetTable",woMSheetColumnDefs, woMSheetDataSource); 
    woMSheetDataTable.subscribe("cellClickEvent", woMSheetDataTable.onEventShowCellEditor); 
    woMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
    });
    var tfoot = woMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'woDetailedMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'woMSheetQtyTotal','0');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	return {
        oDS: woMSheetDataSource,
        oDT: woMSheetDataTable 
    };
}

var woMSheetHiddenDataTable;
var woMSheetHiddenColumnDefs;
var makeDetailedWOMSheetHiddenDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    woMSheetHiddenColumnDefs = [ 
   	{key:"hiddenActivityId", formatter:activityIDFormatter,sortable:false, resizeable:false},
	{key:"hiddenEstimateMSheetId", formatter:textboxNumberFormatter,sortable:false, resizeable:false},
	{key:"no", label:'<s:text name="mb.measurementSheetDataTable.number"/>',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
	{key:"length", label:'<s:text name="mb.measurementSheetDataTable.length"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"width", label:'<s:text name="mb.measurementSheetDataTable.width"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"depthOrHeight", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"quantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',formatter:textboxQtyFormatter,sortable:false, resizeable:false}
	];
    var woMSheetHiddenDataSource = new YAHOO.util.DataSource(); 
    woMSheetHiddenDataTable = new YAHOO.widget.DataTable("woDetailedMSheetHiddenTable",woMSheetHiddenColumnDefs, woMSheetHiddenDataSource,{MSG_EMPTY:""});
    woMSheetHiddenDataTable.subscribe("cellClickEvent", woMSheetHiddenDataTable.onEventShowCellEditor); 
    return {
        oDS: woMSheetHiddenDataSource,
        oDT: woMSheetHiddenDataTable 
    };
}

function minimizeContainer(obj) {
	var msCont;
	if(obj.id=='minEstimateMSheet'){
	     msCont = document.getElementById('estimateMSheetContainer');
	     if (obj.innerHTML == '-'){
	             obj.innerHTML = '^';
	             obj.title='Maximize';
	             msCont.style.width = '21%';
	             msCont.style.height = '45px';
	             msCont.style.left = '25%';
	             msCont.style.top = '37%';
	     } else {
	             obj.innerHTML = '-';
	             obj.title='Minimize';
	             msCont.style.width = '65%';
	             msCont.style.height = '40%';
	             msCont.style.left = '80px';
	             msCont.style.top = '425px'; 
	     }
	}
	else if(obj.id=='minWOMSheet'){
	   msCont = document.getElementById('woMSheetDetailsContainer');
       if (obj.innerHTML == '-'){
               obj.innerHTML = '^';
               obj.title='Maximize';
               msCont.style.width = '21%';
               msCont.style.height = '45px';
               msCont.style.left = '72%';
               msCont.style.top = '37%';
       } else {
               obj.innerHTML = '-';
               obj.title='Minimize';
               msCont.style.width = '65%';
               msCont.style.height = '40%';
               msCont.style.left = '80px';
               msCont.style.top = '425px'; 
       }
	
	}
}

function closeContainer(obj) {
	if(obj.id=='closeEstimateMSheet'){
		document.getElementById('estimateMSheetContainer').style.display='none';
		document.getElementById('minEstimateMSheet').innerHTML='-';
		document.getElementById('minEstimateMSheet').title='Minimize';
	}
	else if(obj.id=='closeWOMSheet'){	
		document.getElementById('woMSheetDetailsContainer').style.display='none';
		document.getElementById('minWOMSheet').innerHTML='-';
		document.getElementById('minWOMSheet').title='Minimize';
	}
		
	
}

function calculateWOMSheetQty(elem,recId){
	var woMSQuantElemId = 'quantity' + recId;
 	if(elem.id==woMSQuantElemId){
  		if(!checkUptoFourDecimalPlace(elem,dom.get('woMSError'),"Quantity")) 
  			return false;
  	}
	var name=(elem.name).split('.');
	dom.get('error'+elem.id).style.display='none';
	if(name==('number'+recId)){
		if(!validateNumberForNonDecimalPlace(elem)) return;
	}
	else{
		if(!validateNumberInWOTableCell(woMSheetDataTable,elem,recId)) return;
	}
	
	if(elem.name==("quantity"+recId)){	
		calculateWOMSheetTotal();
	}
	else
		findQuantity(recId);
}

function validateNumberForNonDecimalPlace(elem){
	 if(validateNumberForDecimal(elem))
	 {
	   var woMeasurement=elem.value;
	   var n=woMeasurement.split('.');
	   if(n.length>1){
	       dom.get('error'+elem.id).style.display='';      
	       return false;
	   }
	 }
	 return true;
}

function validateNumberInWOTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<=0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}

function findQuantity(recId){
	var record=woMSheetDataTable.getRecord(recId);
	var woMSNo;
	var woMSLength;
	var woMSWidth;
	var woMSDepthOrHeight;
	var woMSQuantity=1;
	
	woMSNo=dom.get("number"+record.getId()).value;
	woMSLength=dom.get("length"+record.getId()).value;
	woMSWidth=dom.get("width"+record.getId()).value;
	woMSDepthOrHeight=dom.get("depthOrHeight"+record.getId()).value;
	
	if(!isNaN(getNumber(woMSNo))){
		woMSQuantity*=woMSNo;
	}
	if(!isNaN(getNumber(woMSLength))){
		woMSQuantity*=woMSLength;
	}
	if(!isNaN(getNumber(woMSWidth))){	
		woMSQuantity*=woMSWidth;
	}
	if(!isNaN(getNumber(woMSDepthOrHeight))){
		woMSQuantity*=woMSDepthOrHeight;
	}
	
	if(validateForEmptyWOMsheet(recId))
		woMSheetDataTable.updateCell(record,woMSheetDataTable.getColumn('quantity'),"0");
	else
		dom.get("quantity" + record.getId()).value=roundTo(woMSQuantity,5,'0');
	calculateWOMSheetTotal();
}

function validateForEmptyWOMsheet(recordId){
	var woMSheet_No=dom.get("number" + recordId).value;
	var woMSheet_Length=dom.get("length" + recordId).value;
	var woMSheet_Width=dom.get("width" + recordId).value;
	var woMSheet_DH=dom.get("depthOrHeight" + recordId).value;
	var woMSheet_Qty=dom.get("quantity" + recordId).value;
		
	if(((woMSheet_No!="" && woMSheet_No!=0) || (woMSheet_Length!="" && woMSheet_Length!=0) || (woMSheet_Width!="" && woMSheet_Width!=0) || (woMSheet_DH!="" && woMSheet_DH!=0) || (woMSheet_Qty!="" && woMSheet_Qty!=0)) &&
	 ((woMSheet_No!="" && woMSheet_No!=0) || (woMSheet_Length!="" && woMSheet_Length!=0) || (woMSheet_Width!="" && woMSheet_Width!=0) || (woMSheet_DH!="" && woMSheet_DH!=0)))
		return false;
	else
		return true;	
}


function calculateWOMSheetTotal(){
	var deductionTotal=0;
	var nonDeductionTotal=0;
	var records= woMSheetDataTable.getRecordSet();
   	for(var i=0;i<woMSheetDataTable.getRecordSet().getLength();i++){
		if(!isNaN(getNumber(dom.get("quantity"+records.getRecord(i).getId()).value))){
			if(dom.get("apprvdEstimateMSheetDeduction" + records.getRecord(i).getId()).checked == true)
	    	  	deductionTotal=deductionTotal+getNumber(dom.get("quantity" + records.getRecord(i).getId()).value);
    	    else
	    	  	nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("quantity" + records.getRecord(i).getId()).value);
		}
	}
	var val=roundTo((nonDeductionTotal-deductionTotal),5,'0');
	dom.get("woMSheetQtyTotal").innerHTML=val;
}

function validateBeforeSubmitForWOMSheet(){
	clearErrorMessage();
	var records=woMSheetDataTable.getRecordSet();
	for(var i=0;i<woMSheetDataTable.getRecordSet().getLength();i++)
   	{
   	  	for (var j = 2; j < woMSheetColumnDefs.length-8; j++) {
   	  		if(dom.get("error"+woMSheetColumnDefs[j].key+records.getRecord(i).getId()).style.display=="block" ||  dom.get("error"+woMSheetColumnDefs[j].key+records.getRecord(i).getId()).style.display==""){
   				dom.get("woMSError").style.display='';
				dom.get("woMSError").innerHTML='Please enter a valid value for the '+woMSheetColumnDefs[j].label.split('<br>')[0]+' at line '+records.getRecord(i).getData("SlNo")+'<br>Value should be greater than 0';
				return false;
   			} 
  		 }
   	}
   	for(var i=0;i<woMSheetDataTable.getRecordSet().getLength();i++)
  	{
  			if((!((dom.get("number"+records.getRecord(i).getId()).value=="") &&
  				(dom.get("length"+records.getRecord(i).getId()).value=="") &&
  				(dom.get("width"+records.getRecord(i).getId()).value=="") &&
  				(dom.get("depthOrHeight"+records.getRecord(i).getId()).value=="") && 
  				(dom.get("quantity"+records.getRecord(i).getId()).value==""))))
  				{
	   			 if(records.getRecord(i).getData("apprvdEstimateMSheetNo")!="" &&  dom.get("number"+records.getRecord(i).getId()).value==""){
	   				dom.get("woMSError").style.display='';
				    dom.get("woMSError").innerHTML='Please Enter a valid value for the Number at line '+records.getRecord(i).getData("SlNo");
					return false;	
				 }	
				 if(records.getRecord(i).getData("apprvdEstimateMSheetLength")!="" &&  dom.get("length"+records.getRecord(i).getId()).value==""){
	   				dom.get("woMSError").style.display='';
				    dom.get("woMSError").innerHTML='Please Enter a valid value for the Length at line '+records.getRecord(i).getData("SlNo");
					return false;
				 }
				 if(records.getRecord(i).getData("apprvdEstimateMSheetWidth")!="" &&  dom.get("width"+records.getRecord(i).getId()).value==""){
	   				dom.get("woMSError").style.display='';
				    dom.get("woMSError").innerHTML='Please Enter a valid value for the Width at line '+records.getRecord(i).getData("SlNo");
					return false;
				 }
				 if(records.getRecord(i).getData("apprvdEstimateMSheetDH")!="" &&  dom.get("depthOrHeight"+records.getRecord(i).getId()).value==""){
	   				dom.get("woMSError").style.display='';
				    dom.get("woMSError").innerHTML='Please Enter a valid value for the Depth/Height at line '+records.getRecord(i).getData("SlNo");
					return false;
				 }
				 if(dom.get("quantity"+records.getRecord(i).getId()).value==""){
				 	dom.get("woMSError").style.display='';
				    dom.get("woMSError").innerHTML='Please Enter a valid value for Quantity at line '+records.getRecord(i).getData("SlNo");
					return false;
				 }
			}
  	}
	if(dom.get("woMSheetQtyTotal").innerHTML<1){
		dom.get("woMSError").style.display='';
	    dom.get("woMSError").innerHTML='<s:text name='workorder.measurementSheetDataTable.negativeTotal' />';
		return false;
	}
	addToHiddenTable();
	document.getElementById('woMSheetDetailsContainer').style.display='none';
	dom.get("approvedQuantity"+	dom.get("woDetailsRecId").value).value=dom.get("woMSheetQtyTotal").innerHTML;
	calculateAmount(dom.get("approvedQuantity"+dom.get("woDetailsRecId").value),dom.get("woDetailsRecId").value,"approvedQuantity");
    dom.get("approvedQuantity"+dom.get("woDetailsRecId").value).disabled=true;
	return false;
}

function clearErrorMessage(){
	dom.get("woMSError").style.display='none';
	document.getElementById("woMSError").innerHTML=''; 
}

function addToHiddenTable(){
	removeDetails(dom.get("activity_Id").value);   
	var records= woMSheetDataTable.getRecordSet();
	var temp;
   	for(var i=0;i<woMSheetDataTable.getRecordSet().getLength();i++){
   		if(!validateForEmptyWOMsheet(records.getRecord(i).getId())){
   		 woMSheetHiddenDataTable.addRow({
	   	 	 hiddenActivityId:dom.get("activity_Id").value,
	   	 	 hiddenEstimateMSheetId:records.getRecord(i).getData("estimateMSheetId"),
	   	 	 
	   	 	 no: dom.get("number" + records.getRecord(i).getId()).value,
	   	 	 length:dom.get("length" + records.getRecord(i).getId()).value,
	   	 	 width:dom.get("width" + records.getRecord(i).getId()).value,
	   	 	 depthOrHeight:dom.get("depthOrHeight" + records.getRecord(i).getId()).value,
	   	 	 quantity:dom.get("quantity" + records.getRecord(i).getId()).value
   		});
   		}
  	}
}

function removeDetails(activity_Id){
	if(woMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= woMSheetHiddenDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			if(dom.get("hiddenActivityId"+records.getRecord(i).getId()).value==activity_Id)
				woMSheetHiddenDataTable.deleteRow(records.getRecord(i--));  
		}
	}
}

function disableWOMsheetFields(record){
	if(record.getData("apprvdEstimateMSheetNo")==""){
		dom.get("number"+record.getId()).readonly='true';
        dom.get("number"+record.getId()).disabled='true';
    }
    if(record.getData("apprvdEstimateMSheetLength")==""){
		dom.get("length"+record.getId()).readonly='true';
        dom.get("length"+record.getId()).disabled='true';
    }
  	if(record.getData("apprvdEstimateMSheetWidth")==""){
		dom.get("width"+record.getId()).readonly='true';
        dom.get("width"+record.getId()).disabled='true';
    }
    if(record.getData("apprvdEstimateMSheetDH")==""){
		dom.get("depthOrHeight"+record.getId()).readonly='true';
        dom.get("depthOrHeight"+record.getId()).disabled='true';
    }
    
    if((record.getData("apprvdEstimateMSheetNo")=="") &&
  				(record.getData("apprvdEstimateMSheetLength")=="") &&
  				(record.getData("apprvdEstimateMSheetWidth")=="") &&
  				(record.getData("apprvdEstimateMSheetDH")=="")) {
  		dom.get("quantity"+record.getId()).readonly='true';
        dom.get("quantity"+record.getId()).disabled='true';	
  	}
       
}

function reLoad(activity_Id){
	var recordIndex=0; 
	var flag;
	if(woMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= woMSheetHiddenDataTable.getRecordSet();
		var woMsheetRecords= woMSheetDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){  
			if(dom.get("hiddenActivityId"+records.getRecord(i).getId()).value==activity_Id){ 
				for(var j=0;j<woMsheetRecords.getLength();j++){
					if(woMsheetRecords.getRecord(j).getData("estimateMSheetId")==dom.get("hiddenEstimateMSheetId"+records.getRecord(i).getId()).value){
						woMSheetDataTable.updateCell(woMsheetRecords.getRecord(j),woMSheetDataTable.getColumn('number'),records.getRecord(i).getData("no"));
						woMSheetDataTable.updateCell(woMsheetRecords.getRecord(j),woMSheetDataTable.getColumn('length'),records.getRecord(i).getData("length"));
						woMSheetDataTable.updateCell(woMsheetRecords.getRecord(j),woMSheetDataTable.getColumn('width'),records.getRecord(i).getData("width"));
						woMSheetDataTable.updateCell(woMsheetRecords.getRecord(j),woMSheetDataTable.getColumn('depthOrHeight'),records.getRecord(i).getData("depthOrHeight"));
						woMSheetDataTable.updateCell(woMsheetRecords.getRecord(j),woMSheetDataTable.getColumn('quantity'),records.getRecord(i).getData("quantity"));
	 					disableWOMsheetFields(woMsheetRecords.getRecord(j));  
						}
	 					
	 				}
	 			}		 									  
   	 		}
		}
	calculateWOMSheetTotal();
}

function disableWOMSheetTable(){
	document.getElementById('submitWOMSheet').style.display="none";
	var records= woMSheetDataTable.getRecordSet();
	for(var i=0;i<records.getLength();i++){ 
		dom.get("number"+records.getRecord(i).getId()).disabled=true;
		dom.get("number"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("length"+records.getRecord(i).getId()).disabled=true;
		dom.get("length"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("width"+records.getRecord(i).getId()).disabled=true;
		dom.get("width"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("depthOrHeight"+records.getRecord(i).getId()).disabled=true;
		dom.get("depthOrHeight"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("quantity"+records.getRecord(i).getId()).disabled=true;
		dom.get("quantity"+records.getRecord(i).getId()).readonly=true;
	}
}

</script>

<s:hidden name="activity_Id" id="activity_Id" />
<s:hidden name="woDetailsRecId" id="woDetailsRecId" />

 <div style="position: obsolute;" id="estimateMSheetContainer" class="tableContainer">
 	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeContainer(this);" id="minEstimateMSheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeContainer(this);" id="closeEstimateMSheet" title="Close">X</span>
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>

	<table id="estimateMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
         	<td colspan="8" class="shadowwk"></td>
     	</tr> 
       	<tr>
         <td  class="headingwk" colspan="8" style="border-right-width: 0px;">
          <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
          <div class="headplacer"><s:text name='workorder.estimate.msheet'/></div>
         </td>
       	</tr>  
      
        <tr>
          <td>
          <div class="yui-skin-sam">
              <div id="woEstimateMSheetTable"></div>  
              <div id="woEstimateMSheetTotal"></div>  
          </div>
          </td>
        </tr>
	</table>
</div>
 
 
 
  <div style="position: obsolute;" id="woMSheetDetailsContainer" class="tableContainer">
 	<span class="titles msheettitle" >WO Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeContainer(this);" id="minWOMSheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeContainer(this);" id="closeWOMSheet" title="Close">X</span>
	
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar1">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	<tr><td>
	  <div id="woMSError" class="errorstyle" style="display:none;"></div> 
	 </td></tr>
	</table>	
	<table id="detailedWOMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
         	<td colspan="8" class="shadowwk"></td>
     	</tr> 
       	<tr>
         <td  class="headingwk" colspan="8" style="border-right-width: 0px;">
          <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
          <div class="headplacer"><s:text name='workorder.wo.msheet'/></div>
         </td>
       	</tr>  
      
        <tr>
          <td>
          <div class="yui-skin-sam">
              <div id="woDetailedMSheetTable"></div>  
              <div id="woDetailedMSheetTotal"></div>  
          </div>
          </td>
        </tr>
       
        <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttons" name="buttons" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitWOMSheet"  onclick="return validateBeforeSubmitForWOMSheet();" />
       			 </div>
        	</td>
        </tr>
     
	</table>
</div>
	
<table id="detailedWOMSheetHiddenTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none">
	<tr>
	  <td colspan="8" class="headingwk" style="border-right-width: 0px;">
	   <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	   <div class="headplacer"><s:text name='workorder.wo.msheet'/></div>
	  </td>
	</tr>
         
    <tr>
      <td  colspan="10">
      <div class="yui-skin-sam">
       <div id="woDetailedMSheetHiddenTable"></div> 
      </div>
      </td>
    </tr> 
</table> 

<script>
  makeWOEstimateMSheetDataTable(); 
  makeWOMSheetDataTable();
  makeDetailedWOMSheetHiddenDataTable();
  <s:iterator id="woActivitiesIterator" value="woActivities" status="row_status">
  	<s:if test="%{woMeasurementSheetList!=null}">	
  		<s:iterator id="woMSheetIterator" value="woMeasurementSheetList" status="row_status">
  			<s:if test="%{woActivity!=null}">
		   		woMSheetHiddenDataTable.addRow({
		   	 	 	 hiddenActivityId:'<s:property value="woActivity.activity.id"/>',
		   	 	 	 hiddenEstimateMSheetId:'<s:property value="measurementSheet.id"/>',
				   	<s:if test="%{no==0.0}">
			       	 	no:'',
			     	</s:if> 
			     	<s:elseif test="%{no>0.0}">
			     		no:'<s:property value="no"/>', 
			     	</s:elseif>
			     	
			     	<s:if test="%{length==0.0}">
					 	length:'',
					</s:if> 
					<s:elseif test="%{length>0.0}">
					 	length:'<s:property value="length"/>',
					</s:elseif>
					
					<s:if test="%{width==0.0}">
					 	width:'',
					</s:if> 
					<s:elseif test="%{width>0.0}">
					 	width:'<s:property value="width"/>',
					</s:elseif>
					
					<s:if test="%{depthOrHeight==0.0}">
					 	depthOrHeight:'', 
					</s:if> 
					<s:elseif test="%{depthOrHeight>0.0}">
					 	depthOrHeight:'<s:property value="depthOrHeight"/>',
					</s:elseif>
				 	quantity:'<s:property value="quantity"/>'
			 	}); 
 	 		</s:if>
 	  </s:iterator>
  	</s:if>
  </s:iterator>
</script>