<style type="text/css">
</style> 
<script src="<egov:url path='js/works.js'/>"></script>
<script type="text/javascript">

function createTextBoxFormatter2(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="uomLength" || oColumn.getKey()=="width" || oColumn.getKey()=="depthOrHeight" || oColumn.getKey()=="no" || oColumn.getKey()=="quantity" ){
		fieldName="measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	}
	else{
		fieldName= oColumn.getKey()+oRecord.getId();
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateQty(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	}
    el.innerHTML = markup;
	}
	return textboxFormatter;
} 
var textboxFormatter = createTextBoxFormatter2(11,7);
var textboxQtyFormatter = createTextBoxFormatter2(13,13);
var textboxNumberFormatter = createTextBoxFormatter2(9,7);

var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
 	var value = (YAHOO.lang.isValue(oData))?oData:"";
 	var fieldName;
 	 if(oColumn.getKey()=="remarks")
 		 fieldName = "measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
   	 else
    	 fieldName = oColumn.getKey()+"."+oRecord.getId();
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='1024' value='"+value+"' name='"+fieldName+"' />";
	el.innerHTML = markup;	 	
}

var checkboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
	markup="<input type='checkbox' 	class='selectamountwk' id='"+id+"' onClick='calculateMSheetTotal();' />"; 
    el.innerHTML = markup; 
}

function createMSheetHiddenFormatter(size,maxlength){
	var hiddenFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="identifier" || oColumn.getKey()=="recId" )
      	fieldName = "measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
    else
    	fieldName =oColumn.getKey()+"."+oRecord.getId();
    	markup="<input type='text' id='"+id+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+"' />";
    el.innerHTML = markup;
	}  
return hiddenFormatter;
}
var mSheetHiddenFormatter = createMSheetHiddenFormatter(6,0);

var sorOrNonSorIdHiddenFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
 	 if(oColumn.getKey()=="sorId")
 		 fieldName = "measurementSheetList[" + oRecord.getCount() + "].activity.schedule.id";
   	 else if(oColumn.getKey()=="nonSorId")
    	 fieldName =  "measurementSheetList[" + oRecord.getCount() + "].activity.nonSor.id";
     else	 
    	 fieldName = oColumn.getKey()+"."+oRecord.getId();
    	 
	markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='6'/>"; 
    el.innerHTML = markup; 
}

var nonSorSlNoFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldValue=value;
    var fieldName;
    if(oColumn.getKey()=="nonSorSlNo")
   		 fieldName = "measurementSheetList[" + oRecord.getCount() + "].slNo"; 
   	else
   		fieldName =  oColumn.getKey()+"."+oRecord.getId();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' size='6' />";
    el.innerHTML = markup; 
}

var mSheetHiddenDataTable;
var mSheetHiddenColumnDefs;
var makeDetailedMSheetHiddenDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor(); 
    mSheetHidddenColumnDefs = [ 
   	{key:"remarks", label:'<s:text name='estimate.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"no",label:'<s:text name='estimate.measurementSheetDataTable.number' />',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
   	{key:"uomLength",label:'<s:text name='estimate.measurementSheetDataTable.length' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"width",label:'<s:text name='estimate.measurementSheetDataTable.width' />',formatter:textboxFormatter, resizeable:false},
   	{key:"depthOrHeight",label:'<s:text name='estimate.measurementSheetDataTable.depthorheight' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"quantity",label:'<s:text name='estimate.measurementSheetDataTable.quantity' />',formatter:textboxQtyFormatter,sortable:false, resizeable:false},
   	{key:"UOM",label:'<s:text name='estimate.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"identifier",formatter:mSheetHiddenFormatter,sortable:false, resizeable:true},
   	{key:"sorId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false, resizeable:true},
   	{key:"nonSorSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:true}
    ];
    
    var mSheetHiddenDataSource = new YAHOO.util.DataSource();
    mSheetHiddenDataTable = new YAHOO.widget.DataTable("mSheetHiddenTable",mSheetHidddenColumnDefs, mSheetHiddenDataSource,{MSG_EMPTY:""}); 
    mSheetHiddenDataTable.subscribe("cellClickEvent", mSheetHiddenDataTable.onEventShowCellEditor); 
       
    return {
        oDS: mSheetHiddenDataSource,
        oDT: mSheetHiddenDataTable
    };
}

var mSheetDataTable;
var mSheetColumnDefs;
var makeDetailedMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mSheetColumnDefs = [ 
   	{key:"Sl_No", label:'<s:text name='estimate.measurementSheetDataTable.slno' />', sortable:false, resizeable:false},
   	{key:"description",label:'<s:text name='estimate.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"mSheetNo",label:'<s:text name='estimate.measurementSheetDataTable.number' />',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
   	{key:"mSheetUomLength",label:'<s:text name='estimate.measurementSheetDataTable.length' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"mSheetWidth",label:'<s:text name='estimate.measurementSheetDataTable.width' />',formatter:textboxFormatter,sortable:false,resizeable:false},
   	{key:"mSheetDepthOrHeight",label:'<s:text name='estimate.measurementSheetDataTable.depthorheight' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"mSheetQuantity",label:'<s:text name='estimate.measurementSheetDataTable.quantity' />',formatter:textboxQtyFormatter,sortable:false, resizeable:false},
   	{key:"mSheetUOM",label:'<s:text name='estimate.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"mSheetDeduction",label:'<s:text name='estimate.measurementSheetDataTable.deduction' />',formatter:checkboxFormatter,sortable:false, resizeable:false},
   	{key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
   	{key:"mSheetSorId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false, resizeable:false},
   	{key:"mSheetNonSorSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:false},
   	{key:"mSheetIdentifier",formatter:mSheetHiddenFormatter,sortable:false, resizeable:false}
    ];
    
    var mSheetDataSource = new YAHOO.util.DataSource();
    mSheetDataTable = new YAHOO.widget.DataTable("mSheetTable",mSheetColumnDefs, mSheetDataSource,{MSG_EMPTY:"<s:text name='estimate.measurementSheet.initial.Detailedtable.message'/>"});
    mSheetDataTable.subscribe("cellClickEvent", mSheetDataTable.onEventShowCellEditor); 
    
    mSheetDataTable.hideColumn("mSheetIdentifier");
    mSheetDataTable.hideColumn("mSheetSorId");
    mSheetDataTable.hideColumn("mSheetNonSorSlNo"); 
    
    mSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
        if (column.key == 'Delete'){  
           	recalculateTotalOnDelete(record);
            this.deleteRow(record);
            allRecords=this.getRecordSet();
            for(i=0;i<allRecords.getLength();i++){
              this.updateCell(this.getRecord(i),this.getColumn('Sl_No'),""+(i+1));
            }
        }
    });
    
    var tfoot = mSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'mSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';

	addCell(tr,2,'qtyTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
    return {
        oDS: mSheetDataSource,
        oDT: mSheetDataTable 
    };
}

function calculateQty(elem,recId){

	var msheetQuantElemId = 'mSheetQuantity' + recId;
  	if(elem.id==msheetQuantElemId){
	  	if(!checkUptoFourDecimalPlace(elem,dom.get('msError'),"Quantity"))
	  		return false;
	}	
	  
	var name=(elem.name).split('.');
	dom.get('error'+elem.id).style.display='none';
	if(name==("mSheetNo"+recId)){
		if(!validateNumberForNonDecimalPlace(elem)) return; 
	}
	else{
		if(!validateNumberInEstimateTableCell(mSheetDataTable,elem,recId)) return;
	}
	if(elem.name==("mSheetQuantity"+recId))	
		calculateMSheetTotal();
	else
		findQuantity(recId);	
}

function validateNumberForNonDecimalPlace(elem){
	 if(validateNumberForDecimal(elem))
	 {
	   var measurement=elem.value;
	   var n=measurement.split('.');
	   if(n.length>1){
	       dom.get('error'+elem.id).style.display='';      
	       return false;
	   }
	 }
	 return true;
}

function validateNumberInEstimateTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<=0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}


function findQuantity(recId){
	var record=mSheetDataTable.getRecord(recId);
	var msNo;
	var msLength;
	var msWidth;
	var msDepthOrHeight;
	var msQuantity=1;
	var flag=false;
	msNo=dom.get("mSheetNo"+record.getId()).value;
	msLength=dom.get("mSheetUomLength"+record.getId()).value;
	msWidth=dom.get("mSheetWidth"+record.getId()).value;
	msDepthOrHeight=dom.get("mSheetDepthOrHeight"+record.getId()).value;
	if(!isNaN(getNumber(msNo))){
		msQuantity*=msNo;
		flag=true;
	}
	if(!isNaN(getNumber(msLength))){
		msQuantity*=msLength;
		flag=true; 
	}
	if(!isNaN(getNumber(msWidth))){	
		msQuantity*=msWidth;
		flag=true;
	}
	if(!isNaN(getNumber(msDepthOrHeight))){
		msQuantity*=msDepthOrHeight;
		flag=true;	
	}
	oldQuantity=record.getData("mSheetQuantity");
	if(flag!=true)
		msQuantity=0.00; 
	mSheetDataTable.updateCell(record,mSheetDataTable.getColumn('mSheetQuantity'),roundTo(msQuantity,5,'0'));
	calculateMSheetTotal();
}

function calculateMSheetTotal(){ 
	var deductionTotal=0;
	var nonDeductionTotal=0;
	var Records= mSheetDataTable.getRecordSet();
   	for(var i=0;i<mSheetDataTable.getRecordSet().getLength();i++)
   	{
   		if(!isNaN(getNumber(dom.get("mSheetQuantity" + Records.getRecord(i).getId()).value))){
	    	  if(dom.get("mSheetDeduction" + Records.getRecord(i).getId()).checked == true){
	    	  		deductionTotal=deductionTotal+getNumber(dom.get("mSheetQuantity" + Records.getRecord(i).getId()).value);
	    	  		dom.get("mSheetIdentifier" + Records.getRecord(i).getId()).value="D";
	    	  }
	    	  else{
	    	  		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("mSheetQuantity" + Records.getRecord(i).getId()).value);
	    	  		dom.get("mSheetIdentifier" + Records.getRecord(i).getId()).value="A";
	    	  }	
	    }	  
   	}
   	var val=roundTo((nonDeductionTotal-deductionTotal),5,'0');
	dom.get("qtyTotal").innerHTML=val;
	dom.get("totalMSheetQty").value=dom.get("qtyTotal").innerHTML;
}
 
function recalculateTotalOnDelete(record){
	var oldQuantity=dom.get("mSheetQuantity" + record.getId()).value;
	var oldTotal=dom.get("qtyTotal").innerHTML;
 	if(dom.get("mSheetDeduction" + record.getId()).checked == true)
		dom.get("qtyTotal").innerHTML=(eval(getNumber(oldTotal))+eval(getNumber(oldQuantity)));
	else
		dom.get("qtyTotal").innerHTML=(getNumber(oldTotal)-getNumber(oldQuantity));
	dom.get("totalMSheetQty").value=dom.get("qtyTotal").innerHTML;	
}

function validateBeforeSubmitForMSheet(){
	clearErrorMessage();
	if(mSheetDataTable.getRecordSet().getLength()==0){
		dom.get("msError").style.display='';
	    dom.get("msError").innerHTML='<s:text name='estimate.measurementSheetDataTable.zeroLength' />';
		return false;
	}
	else{
		var Records=mSheetDataTable.getRecordSet();
		for(var i=0;i<mSheetDataTable.getRecordSet().getLength();i++)
   		{
   			if(validateForEmptyEstimateMsheet(Records.getRecord(i).getId())){
   					dom.get("msError").style.display='';
				    dom.get("msError").innerHTML=' Please Enter L,W,D/H,Quantity  Details at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
					return false;
   			}
   			
   			if(dom.get("mSheetQuantity" + Records.getRecord(i).getId()).value==""){
   					dom.get("msError").style.display='';
				    dom.get("msError").innerHTML=' Please Input Quantity at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
					return false;
   			}
   				
   	  		 for (var j = 2; j < mSheetColumnDefs.length-6; j++) { 
   	  			 if(dom.get("error"+mSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display=="block" ||  dom.get("error"+mSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display==""){
   				  	dom.get("msError").style.display='';
				    dom.get("msError").innerHTML='Please enter a valid value for the '+mSheetColumnDefs[j].label.split('<br>')[0]+' at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
					return false;
   				 } 
  		  	}
   		}
	}
	if(dom.get("qtyTotal").innerHTML<=0){
		dom.get("msError").style.display='';
	    dom.get("msError").innerHTML='<s:text name='estimate.measurementSheetDataTable.negativeTotal' />';
		return false;
	}
	goToEstimate();
	document.getElementById('mSheetDetailsContainer').style.display='none';
	return false;
}

function validateForEmptyEstimateMsheet(recordId){
	var mSheet_No=dom.get("mSheetNo" + recordId).value;
	var mSheet_Length=dom.get("mSheetUomLength" + recordId).value;
	var mSheet_Width=dom.get("mSheetWidth" + recordId).value;
	var mSheet_DH=dom.get("mSheetDepthOrHeight" + recordId).value;
	var mSheet_Qty=dom.get("mSheetQuantity" + recordId).value;
	if(((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0) || (mSheet_Qty!="" && mSheet_Qty!=0)) && 
	((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0)))
		return false; 
	else
		return true;	
}

function goToEstimate(){
	removeDetails(dom.get("scheduleId").value,dom.get("nonSorSlNum").value);   
	var records= mSheetDataTable.getRecordSet();
   	for(var i=0;i<mSheetDataTable.getRecordSet().getLength();i++){
   	 	 mSheetHiddenDataTable.addRow({remarks:dom.get("description" + records.getRecord(i).getId()).value, no:dom.get("mSheetNo" + records.getRecord(i).getId()).value,
   	 	 uomLength:dom.get("mSheetUomLength" + records.getRecord(i).getId()).value,width:dom.get("mSheetWidth" + records.getRecord(i).getId()).value,
   	 	 depthOrHeight:dom.get("mSheetDepthOrHeight" + records.getRecord(i).getId()).value,identifier:dom.get("mSheetIdentifier" + records.getRecord(i).getId()).value,
   	  	 quantity:dom.get("mSheetQuantity" + records.getRecord(i).getId()).value,
   	  	 sorId:dom.get("mSheetSorId" + records.getRecord(i).getId()).value,
   	 	 nonSorSlNo:dom.get("mSheetNonSorSlNo" + records.getRecord(i).getId()).value,
   	 	 UOM:dom.get("estimateUOM").value});    	 	  
   	}
   	if(dom.get("sorquantity"+dom.get("recordId").value)!=null) {
   		dom.get("sorquantity"+dom.get("recordId").value).value=dom.get("qtyTotal").innerHTML;
   		calculateSOR(dom.get("sorquantity"+dom.get("recordId").value),dom.get("recordId").value); 
   		dom.get("sorquantity"+dom.get("recordId").value).disabled=true;
   	}
   	if(dom.get("nonsorquantity"+dom.get("recordId").value)!=null) {
	   	dom.get("nonsorquantity"+dom.get("recordId").value).value=dom.get("qtyTotal").innerHTML;
	   	calculateNonSOR(dom.get("nonsorquantity"+dom.get("recordId").value),dom.get("recordId").value);	 
	   	dom.get("nonsorquantity"+dom.get("recordId").value).disabled=true; 
	}	
}

function clearErrorMessage(){
	dom.get("msError").style.display='none';
	document.getElementById("msError").innerHTML=''; 
}

function removeDetails(scheduleId,nonSorSlNo){
	if(mSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mSheetHiddenDataTable.getRecordSet();
		var param1;
		var param2;
		if(scheduleId!="" && scheduleId!=null){ 
			param1="sorId";
			param2=scheduleId;
		}
		else{
			param1="nonSorSlNo";
			param2=nonSorSlNo;
		}
		for(var i=0;i<records.getLength();i++){ 
			if(dom.get(param1+records.getRecord(i).getId()).value==param2)
				mSheetHiddenDataTable.deleteRow(records.getRecord(i--)); 
		}
	}
}

function reLoad(scheduleId,nonSorSlNo){
	var recordIndex=0; 
	if(mSheetHiddenDataTable.getRecordSet().getLength()>0){
		mSheetDataTable.deleteRows(0,mSheetDataTable.getRecordSet().getLength()); 
		dom.get("qtyTotal").innerHTML="0.00";
		var records= mSheetHiddenDataTable.getRecordSet();
		var param1;
		var param2;
		if(scheduleId!="" && scheduleId!=null){ 
			param1="sorId";
			param2=scheduleId;
		}
		else{
			param1="nonSorSlNo";
			param2=nonSorSlNo;
		}
		 
   		for(var i=0;i<records.getLength();i++){  
			if(dom.get(param1+records.getRecord(i).getId()).value==param2){
				mSheetDataTable.addRow({
									      Sl_No:mSheetDataTable.getRecordSet().getLength()+1,
										  description:dom.get("remarks" + records.getRecord(i).getId()).value, 
				 						  mSheetNo:dom.get("no" + records.getRecord(i).getId()).value,
				 						  mSheetUomLength:dom.get("uomLength" + records.getRecord(i).getId()).value,
				 						  mSheetWidth:dom.get("width" + records.getRecord(i).getId()).value,
   	 	 								  mSheetDepthOrHeight:dom.get("depthOrHeight" + records.getRecord(i).getId()).value,
   	 	 								  mSheetQuantity:dom.get("quantity" + records.getRecord(i).getId()).value,
   	 	 								  mSheetUOM: dom.get("estimateUOM").value,
   	 	 								  mSheetIdentifier:dom.get("identifier" + records.getRecord(i).getId()).value,
   	 	 								  mSheetDeduction:'',
   	 	 								  mSheetSorId:dom.get("sorId" + records.getRecord(i).getId()).value,
   	 	 								  mSheetNonSorSlNo:dom.get("nonSorSlNo" + records.getRecord(i).getId()).value,
   	 	 								  Delete:'X' 
   	 	 								});
   	 	 								var record = mSheetDataTable.getRecord(parseInt(recordIndex++));
				                    	if(dom.get("mSheetIdentifier"+record.getId()).value=='D')
				                    	 dom.get("mSheetDeduction"+record.getId()).checked=true;
				                    	else
				                    	 dom.get("mSheetDeduction"+record.getId()).checked=false;
				                    	calculateMSheetTotal();  
   	 			}
   	 			
		} 
   	}
   	else{
	   	mSheetDataTable.deleteRows(0,mSheetDataTable.getRecordSet().getLength());
	   	dom.get("qtyTotal").innerHTML="0.00";
   	}
}

function minimizeMSheet(obj) {
       var msCont = document.getElementById('mSheetDetailsContainer');
       if (obj.innerHTML == '-'){
               obj.innerHTML = '^';
               obj.title='Maximize';
               msCont.style.width = '21%';
               msCont.style.height = '45px';
               msCont.style.left = '79%';
               msCont.style.top = '90%';
       } else {
               obj.innerHTML = '-';
               obj.title='Minimize';
               msCont.style.width = '75%';
               msCont.style.height = '40%';
               msCont.style.left = '50px';
               msCont.style.top = '450px'; 
       }
}


function closeMSheet() {
	clearErrorMessage();
	document.getElementById('mSheetDetailsContainer').style.display='none';
	document.getElementById('min').innerHTML='-';
	document.getElementById('min').title='Minimize';
}

function resetMSheet() {
	var msg='<s:text name="estimate.measurementSheetDataTable.resetEntries"/>';
	var ans=confirm(msg);
		
	if(ans) {
		if(mSheetDataTable.getRecordSet().getLength()>0){
			removeDetails(dom.get("scheduleId").value,dom.get("nonSorSlNum").value);
		}
		mSheetDataTable.deleteRows(0,mSheetDataTable.getRecordSet().getLength());
		document.getElementById('mSheetDetailsContainer').style.display='none';
		
		if(dom.get("scheduleId").value!="" && dom.get("scheduleId").value!=null){
			if(dom.get("sorquantity"+dom.get("recordId").value)!=null) {
				var record = sorDataTable.getRecord(dom.get("recordId").value);
		   		dom.get("sorquantity"+dom.get("recordId").value).value="";
		   		calculateSOR(dom.get("sorquantity"+dom.get("recordId").value),dom.get("recordId").value); 
		   		dom.get("sorquantity"+dom.get("recordId").value).disabled=false;
		   		dom.get("sorquantity"+dom.get("recordId").value).readOnly=false;
		   	 	sorDataTable.updateCell(record,sorDataTable.getColumn("sorMSheetLength"),0);
		   	} 
		}
		else if(dom.get("nonSorSlNum").value!="" && dom.get("nonSorSlNum").value!=null)
		   	if(dom.get("nonsorquantity"+dom.get("recordId").value)!=null) {
		   		var record = nonSorDataTable.getRecord(dom.get("recordId").value);
			   	dom.get("nonsorquantity"+dom.get("recordId").value).value="";
			   	calculateNonSOR(dom.get("nonsorquantity"+dom.get("recordId").value),dom.get("recordId").value);	 
			   	dom.get("nonsorquantity"+dom.get("recordId").value).disabled=false; 
			   	dom.get("nonsorquantity"+dom.get("recordId").value).readOnly=false; 
		   	 	nonSorDataTable.updateCell(record,nonSorDataTable.getColumn("nonSorMSheetLength"),0);
			}
	}
	else {
		return false;		
	}
}


function disableEstimateMSheetTable(){
	document.getElementById('submitMSheet').style.display="none";
	document.getElementById('resetButton').style.display="none";
	var records= mSheetDataTable.getRecordSet();
	for(var i=0;i<records.getLength();i++){ 
		dom.get("description"+records.getRecord(i).getId()).disabled=true;
		dom.get("description"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetNo"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetNo"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetUomLength"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetUomLength"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetWidth"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetWidth"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetDepthOrHeight"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetDepthOrHeight"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetQuantity"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetQuantity"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("mSheetDeduction"+records.getRecord(i).getId()).disabled=true;
		dom.get("mSheetDeduction"+records.getRecord(i).getId()).readonly=true;
	} 
	  mSheetDataTable.removeListener('cellClickEvent');
	 
}




</script> 
<s:hidden name="scheduleId" id="scheduleId" value="%{scheduleId}"></s:hidden>
<s:hidden name="nonSorSlNum" id="nonSorSlNum" value="%{nonSorSlNum}"></s:hidden>
<s:hidden name="recordId" id="recordId" value="%{recordId}"></s:hidden>
<s:hidden name="estimateUOM" id="estimateUOM" value="%{estimateUOM}"></s:hidden>

	<div style="position: obsolute;" id="mSheetDetailsContainer" class="tableContainer">
	<s:hidden name="totalMSheetQty" id="totalMSheetQty"/>
	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMSheet(this)" id="min" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMSheet()" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
		  
	<div id="msError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedEstTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="19" class="shadowwk"></td>
        </tr>
        <tr>
          <td colspan="19" class="headingwk" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='estimate.measurementSheet.detailedEstimate.name'/></div>
          </td>
          <td class="headingwk" id="showAdd" name="showAdd" align="right" style="border-left-width: 0px;">
			<a  id="addMSheet" name="addMSheet" onclick="mSheetDataTable.addRow({Sl_No:mSheetDataTable.getRecordSet().getLength()+1,mSheetUomLength:'1',mSheetWidth:'1',mSheetSorId:dom.get('scheduleId').value,mSheetNonSorSlNo:dom.get('nonSorSlNum').value,mSheetUOM:dom.get('estimateUOM').value});calculateQty(dom.get(mSheetColumnDefs[3].key+mSheetDataTable.getRecordSet().getRecord(mSheetDataTable.getRecordSet().getLength()-1).getId()),mSheetDataTable.getRecordSet().getRecord(mSheetDataTable.getRecordSet().getLength()-1).getId());return false;" href="#">  
			<img  width="16" height="16" border="0" src="/egworks/image/add.png"/> 
			</a>
		  </td>
        </tr>
      
        <tr>
          <td  colspan="10">
          <div class="yui-skin-sam">
              <div id="mSheetTable"></div> 
              <div id="mSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttons" name="buttons" style="display: none;">
            	 <br><input type="submit" class="buttonadd" value="SUBMIT" id="submitMSheet" onclick="return validateBeforeSubmitForMSheet();" /> 
	 				<input type="button" class="buttonfinal" value="RESET" id="resetButton" name="resetButton" onclick="resetMSheet()"/>  
       			 </div>
        	</td>
        </tr>
        
	</table>
</div>	
					
<table id="detailedEstHiddenTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none">
	<tr>
	  <td colspan="9" class="headingwk" style="border-right-width: 0px;">
	   <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	   <div class="headplacer"><s:text name='estimate.measurementSheet.detailedEstimate.name'/></div>
	  </td>
	</tr>
         
    <tr>
      <td  colspan="10">
      <div class="yui-skin-sam">
          <div id="mSheetHiddenTable"></div> 
      </div>
      </td>
    </tr>
</table> 


<script>
	makeDetailedMSheetDataTable();
	makeDetailedMSheetHiddenDataTable();
						count=1;
						recordIndex=0;
						  <s:iterator id="mSheetIterator" value="measurementSheetList" status="row_status">
						  <s:if test="%{activity!=null}">		
						   	mSheetHiddenDataTable.addRow({	                        
	                        remarks:'<s:property value="remarks"/>', 
	                        <s:if test="%{no==0.0}">
	                       	 no:'',
	                       	</s:if> 
	                       	<s:elseif test="%{no>0.0}">
	                       	 no:'<s:property value="no"/>',
	                       	</s:elseif>
	                       	
	                       	<s:if test="%{uomLength==0.0}">
							 uomLength:'',
							</s:if> 
							<s:elseif test="%{uomLength>0.0}">
							 uomLength:'<s:property value="uomLengthString"/>',
							</s:elseif>
							
							<s:if test="%{width==0.0}">
							 width:'',
							</s:if> 
							<s:elseif test="%{width>0.0}">
							 width:'<s:property value="widthString"/>',
							</s:elseif>
							
	                      	<s:if test="%{depthOrHeight==0.0}">
							 depthOrHeight:'',
							</s:if> 
							<s:elseif test="%{depthOrHeight>0.0}">
							 depthOrHeight:'<s:property value="depthOrHeightString"/>',
							</s:elseif>
							
							<s:if test="%{quantity==0.0}">
							 quantity:'',
							</s:if> 
							<s:elseif test="%{quantity>0.0}"> 
							 quantity:'<s:property value="quantityString"/>',
							</s:elseif>
	                             
	                        UOM: '<s:property value="activity.Uom.uom"/>',
	                        identifier:'<s:property value="identifier"/>', 
	                    
	                         <s:if test="%{activity.nonSor!=null}">
	                         	nonSorSlNo: '<s:property value="activity.srlNo" />',
	                         </s:if>
	                         
	                        sorId: '<s:property value="activity.schedule.id" />'
	                        
	                    	}); 
	                    	</s:if>
	                      </s:iterator>
</script>