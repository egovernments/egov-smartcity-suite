<style type="text/css">
</style> 
<script src="<egov:url path='js/works.js'/>"></script>
<script type="text/javascript">

function minimizeMBExtraMSheet(obj) {
       var msCont;
       if(obj.id=='minimizeEstimatedMsheet'){
       	msCont = document.getElementById('mbExtraMSheetDetailsContainer');
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
	               msCont.style.top = '750px'; 
	       }
      }
      else if(obj.id=='minimizeConsumedMsheet'){
      	msCont = document.getElementById('mbExtraConsumedMSheetDetailsContainer');
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
    else if(obj.id=='minEstimateMSheet'){
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
	else if(obj.id=='minimizeMBExtraCQMsheet'){
		msCont = document.getElementById('mbExtraCQMSheetDetailsContainer');
	       if (obj.innerHTML == '-'){
	               obj.innerHTML = '^';
	               obj.title='Maximize';
	               msCont.style.width = '21%';
	               msCont.style.height = '45px';
	               msCont.style.left = '49%';
	               msCont.style.top = '37%';
	       } else {
	               obj.innerHTML = '-';
	               obj.title='Minimize';
	               msCont.style.width = '75%';
	               msCont.style.height = '40%';
	               msCont.style.left = '50px';
	               msCont.style.top = '425px'; 
	       }
	}
	else if(obj.id=='minimizeCQConsumedMsheet'){
		msCont = document.getElementById('mbCQConsumedMSheetDetailsContainer');
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
	               msCont.style.width = '75%';
	               msCont.style.height = '40%';
	               msCont.style.left = '50px';
	               msCont.style.top = '425px'; 
	       }
	}
}

function closeMBExtraMSheet(obj) {
 	if(obj.id=='closeEstimatedMsheet'){
		dom.get("mbExtraMSheetError").style.display='none';
		document.getElementById("mbExtraMSheetError").innerHTML=''; 
		document.getElementById('mbExtraMSheetDetailsContainer').style.display='none';
		document.getElementById('minimizeEstimatedMSheet').innerHTML='-'; 
		document.getElementById('minimizeEstimatedMSheet').title='Minimize';
	}
	else if(obj.id=='closeConsumedMsheet'){
		dom.get("mbExtraConsumedMSheetError").style.display='none'; 
		document.getElementById("mbExtraConsumedMSheetError").innerHTML=''; 
		document.getElementById('mbExtraConsumedMSheetDetailsContainer').style.display='none';
		document.getElementById('minimizeConsumedMsheet').innerHTML='-'; 
		document.getElementById('minimizeConsumedMsheet').title='Minimize'; 
	}
	else if(obj.id=='closeEstimateMSheet'){
		document.getElementById('estimateMSheetContainer').style.display='none';
		document.getElementById('minEstimateMSheet').innerHTML='-';
		document.getElementById('minEstimateMSheet').title='Minimize';
	}
	else if(obj.id=='closeMBExtraCQMsheet'){
		dom.get("mbExtraCQMSheetError").style.display='none';
		document.getElementById("mbExtraCQMSheetError").innerHTML=''; 
		document.getElementById('mbExtraCQMSheetDetailsContainer').style.display='none';
		document.getElementById('minimizeMBExtraCQMsheet').innerHTML='-'; 
		document.getElementById('minimizeMBExtraCQMsheet').title='Minimize';
	}
	else if(obj.id=='closeCQConsumedMsheet'){
		dom.get("mbCQConsumedMSheetError").style.display='none';
		document.getElementById("mbCQConsumedMSheetError").innerHTML=''; 
		document.getElementById('mbCQConsumedMSheetDetailsContainer').style.display='none';
		document.getElementById('minimizeCQConsumedMsheet').innerHTML='-'; 
		document.getElementById('minimizeCQConsumedMsheet').title='Minimize'; 
	}
} 

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



var extraItemCheckboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    if(oColumn.getKey()=="mbExtraMSheetDeduction"){
		var qType="EstimateQty";
		markup="<input type='checkbox' 	class='selectamountwk' id='"+id+"' onClick='calculateExtraMSheetTotal(\""+qType+"\");' />";
	} 
	else if(oColumn.getKey()=="mbExtraCQMSheetDeduction"){
		var qType="ChangeQty";
		markup="<input type='checkbox' 	class='selectamountwk' id='"+id+"' onClick='calculateExtraMSheetTotal(\""+qType+"\");' />";
	} 
	else
		markup="<input type='checkbox' id='"+id+"' />"; 	
    el.innerHTML = markup; 
}


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
   	else if(oColumn.getKey()=="extraItemMsheetSlNo")
   		 fieldName = "measurementSheetList[" + oRecord.getCount() + "].mbExtraItemSlNo"; 
   	else if(oColumn.getKey()=="changeQuantityMsheetSlNo")
   		 fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.mbExtraItemSlNo";   
   	else if(oColumn.getKey()=="hiddenEstimateMSheetId")
   		 fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.id";   	 
   	else
   		fieldName =  oColumn.getKey()+"."+oRecord.getId();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' size='6' />";
    el.innerHTML = markup; 
}

function createMBExtraMSheetHiddenFormatter(size,maxlength){
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
var mbExtraMSheetHiddenFormatter = createMBExtraMSheetHiddenFormatter(6,0);


var mbExtraMSheetHiddenDataTable; 
var mbExtraMSheetHiddenColumnDefs;
var makeDetailedMBMSheetHiddenDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor(); 
    mbExtraMSheetHiddenColumnDefs = [ 
   	{key:"remarks", label:'<s:text name='mb.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"no",label:'<s:text name='mb.measurementSheetDataTable.number' />',formatter:extraItemNoFormatter,sortable:false, resizeable:false},
   	{key:"uomLength",label:'<s:text name='mb.measurementSheetDataTable.length' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"width",label:'<s:text name='mb.measurementSheetDataTable.width' />',formatter:extraItemTextBoxFormatter, resizeable:false},
   	{key:"depthOrHeight",label:'<s:text name='mb.measurementSheetDataTable.depthorheight' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"quantity",label:'<s:text name='mb.measurementSheetDataTable.quantity' />',formatter:extraItemQtyFormatter,sortable:false, resizeable:false},
   	{key:"UOM",label:'<s:text name='mb.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"identifier",formatter:mbExtraMSheetHiddenFormatter,sortable:false, resizeable:true},
   	{key:"sorId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false, resizeable:true},
   	{key:"nonSorSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:true},
   	{key:"extraItemMsheetSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:true},
   	{key:"consumedNo",formatter:extraItemNoFormatter,sortable:false, resizeable:false},
   	{key:"consumedLength",formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"consumedWidth",formatter:extraItemTextBoxFormatter, resizeable:false},
   	{key:"consumedDH",formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"consumedQuantity",formatter:extraItemQtyFormatter,sortable:false, resizeable:false} 
    ];
    
    var mbExtraMSheetHiddenDataSource = new YAHOO.util.DataSource();
    mbExtraMSheetHiddenDataTable = new YAHOO.widget.DataTable("mbExtraMSheetHiddenTable",mbExtraMSheetHiddenColumnDefs, mbExtraMSheetHiddenDataSource,{MSG_EMPTY:""}); 
    mbExtraMSheetHiddenDataTable.subscribe("cellClickEvent", mbExtraMSheetHiddenDataTable.onEventShowCellEditor); 
       
    return {
        oDS: mbExtraMSheetHiddenDataSource,
        oDT: mbExtraMSheetHiddenDataTable
    };
}


var mbExtraMSheetDataTable;
var mbExtraMSheetColumnDefs;
var makeDetailedMBMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbExtraMSheetColumnDefs = [ 
   	{key:"Sl_No", label:'<s:text name='column.title.SLNo' />', sortable:false, resizeable:false},
   	{key:"description",label:'<s:text name='mb.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetNo",label:'<s:text name='mb.measurementSheetDataTable.number' />',formatter:extraItemNoFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetUomLength",label:'<s:text name='mb.measurementSheetDataTable.length' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetWidth",label:'<s:text name='mb.measurementSheetDataTable.width' />',formatter:extraItemTextBoxFormatter,sortable:false,resizeable:false},
   	{key:"mbExtraMSheetDepthOrHeight",label:'<s:text name='mb.measurementSheetDataTable.depthorheight' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetQuantity",label:'<s:text name='mb.measurementSheetDataTable.quantity' />',formatter:extraItemQtyFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetUOM",label:'<s:text name='mb.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"mbExtraMSheetDeduction",label:'<s:text name='mb.measurementSheetDataTable.deduction' />',formatter:extraItemCheckboxFormatter,sortable:false, resizeable:false},
   	{key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
   	
   	{key:"mbExtraMSheetSorId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetNonSorSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetSlNo",label:"mbExtraMSheetSlNo",formatter:nonSorSlNoFormatter,hidden:true,sortable:false, resizeable:false},
   	{key:"mbExtraMSheetIdentifier",formatter:mbExtraMSheetHiddenFormatter,sortable:false, resizeable:false}
    ];
    
    var mbExtraMSheetDataSource = new YAHOO.util.DataSource();
    mbExtraMSheetDataTable = new YAHOO.widget.DataTable("mbExtraMSheetTable",mbExtraMSheetColumnDefs, mbExtraMSheetDataSource,{MSG_EMPTY:"<s:text name='mb.measurementSheet.initial.Detailedtable.message'/>"});
    mbExtraMSheetDataTable.subscribe("cellClickEvent", mbExtraMSheetDataTable.onEventShowCellEditor); 
    
    mbExtraMSheetDataTable.hideColumn("mbExtraMSheetIdentifier");
    mbExtraMSheetDataTable.hideColumn("mbExtraMSheetSorId");
    mbExtraMSheetDataTable.hideColumn("mbExtraMSheetNonSorSlNo"); 
    mbExtraMSheetDataTable.hideColumn("mbExtraMSheetSlNo");
    
    mbExtraMSheetDataTable.on('cellClickEvent',function (oArgs) { 
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
        <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
        if (column.key == 'Delete'){  
	        	var type="EstimateQty";
	           	recalculateTotalOnDelete(record,type);
            this.deleteRow(record);
            allRecords=this.getRecordSet();
            for(i=0;i<allRecords.getLength();i++){
              this.updateCell(this.getRecord(i),this.getColumn('Sl_No'),""+(i+1));
            }
        }
        </s:if>
        
    });
    
    var tfoot = mbExtraMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'mbExtraMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';

	addCell(tr,2,'qtyTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
    return {
        oDS: mbExtraMSheetDataSource,
        oDT: mbExtraMSheetDataTable 
    };
}

function calculateExtraMsheetQty(elem,type,recId){
	if(type=="EstimateQty"){
		var msheetQuantElemId = 'mbExtraMSheetQuantity' + recId;
	  	if(elem.id==msheetQuantElemId){
		  	if(!checkUptoFourDecimalPlace(elem,dom.get('mbExtraMSheetError'),"Quantity"))
		  		return false;
		}	
		  
		var name=(elem.name).split('.');
		dom.get('error'+elem.id).style.display='none';
		if(name==("mbExtraMSheetNo"+recId)){
			if(!validateNumberForNonDecimalPlace(elem)) return; 
		}
		else{
			if(!validateNumberInTableCell(mbExtraMSheetDataTable,elem,recId)) return;
		}
		if(elem.name==("mbExtraMSheetQuantity"+recId))	
			calculateExtraMSheetTotal(type);
		else
			findMBExtraMsheetQuantity(recId,type);	
	}	
	else if(type=="ConsumedQty"){
		var msheetQuantElemId = 'consumedMBMSheetQuantity' + recId;
	  	if(elem.id==msheetQuantElemId){
		  	if(!checkUptoFourDecimalPlace(elem,dom.get('mbExtraConsumedMSheetError'),"Quantity"))
		  		return false;
		}	
		var name=(elem.name).split('.');
		dom.get('error'+elem.id).style.display='none';
		if(name==("consumedMBMSheetNo"+recId)){
			if(!validateNumberForNonDecimalPlace(elem)) return; 
		}
		else{
			if(!validateNumberInTableCell(mbExtraConsumedMSheetDataTable,elem,recId)) return;
		}
		if(elem.name==("consumedMBMSheetQuantity"+recId))	
			calculateExtraMSheetTotal(type);
		else
			findMBExtraMsheetQuantity(recId,type);	
	}	
	else if(type=="ChangeQty"){
		var msheetQuantElemId = 'mbExtraCQMSheetQuantity' + recId;
	  	if(elem.id==msheetQuantElemId){
		  	if(!checkUptoFourDecimalPlace(elem,dom.get('mbExtraCQMSheetError'),"Quantity"))
		  		return false;
		}	
		var name=(elem.name).split('.');
		dom.get('error'+elem.id).style.display='none';
		if(name==("mbExtraCQMSheetNo"+recId)){
			if(!validateNumberForNonDecimalPlace(elem)) return; 
		}
		else{
			if(!validateNumberInTableCell(mbExtraCQMSheetDataTable,elem,recId)) return;
		}
		if(elem.name==("mbExtraCQMSheetQuantity"+recId))	
			calculateExtraMSheetTotal(type);
		else
			findMBExtraMsheetQuantity(recId,type);	
	}
	else if(type=="ConsumedChangeQty"){
		var msheetQuantElemId = 'consumedCQMBMSheetQuantity' + recId;
	  	if(elem.id==msheetQuantElemId){
		  	if(!checkUptoFourDecimalPlace(elem,dom.get('mbCQConsumedMSheetError'),"Quantity"))
		  		return false;
		}	
		var name=(elem.name).split('.');
		dom.get('error'+elem.id).style.display='none';
		if(name==("consumedCQMBMSheetNo"+recId)){
			if(!validateNumberForNonDecimalPlace(elem)) return; 
		}
		else{
			if(!validateNumberInTableCell(mbChangeQtyConsumedMSheetTable,elem,recId)) return;
		}
		
		
		var tempCurQty=0;
		if(!isNaN(getNumber(dom.get("consumedCQMBMSheetQuantity"+recId).value)))
			tempCurQty=dom.get("consumedCQMBMSheetQuantity"+recId).value;
			
		var cqRecord=mbChangeQtyConsumedMSheetTable.getRecord(recId);	
		var curCumQty=(eval(getNumber(cqRecord.getData("hiddenCurCumlvCQMBMSheetQuantity")))-eval(getNumber(cqRecord.getData("hiddenCurCQMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
		mbChangeQtyConsumedMSheetTable.updateCell(cqRecord,mbChangeQtyConsumedMSheetTable.getColumn('curCumlvCQMBMSheetQuantity'),curCumQty);
	
		
		if(elem.name==("consumedCQMBMSheetQuantity"+recId)){	
			calculateExtraMSheetTotal(type);
		}
		else
			findMBExtraMsheetQuantity(recId,type);	
			
	} 
}

function validateNumberInTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      var tmpId="extraItemsserviceTaxPerc"+recordId;
      if((tmpId!=elem.id && (isNaN(elem.value) || getNumber(elem.value)<=0)) || (tmpId==elem.id && (isNaN(elem.value) || getNumber(elem.value)<0))){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}

function findMBExtraMsheetQuantity(recId,type){ 
	if(type=="EstimateQty"){
		var record=mbExtraMSheetDataTable.getRecord(recId);
		var msNo;
		var msLength;
		var msWidth;
		var msDepthOrHeight;
		var msQuantity=1;
		var flag=false;
		msNo=dom.get("mbExtraMSheetNo"+record.getId()).value;
		msLength=dom.get("mbExtraMSheetUomLength"+record.getId()).value;
		msWidth=dom.get("mbExtraMSheetWidth"+record.getId()).value;
		msDepthOrHeight=dom.get("mbExtraMSheetDepthOrHeight"+record.getId()).value;
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
		if(flag!=true)
			msQuantity=0.00; 
		mbExtraMSheetDataTable.updateCell(record,mbExtraMSheetDataTable.getColumn('mbExtraMSheetQuantity'),msQuantity);
		calculateExtraMSheetTotal(type);
	}
	else if(type=="ConsumedQty"){
		var record=mbExtraConsumedMSheetDataTable.getRecord(recId);
		var msNo;
		var msLength;
		var msWidth;
		var msDepthOrHeight;
		var msQuantity=1;
		var flag=false;
		msNo=dom.get("consumedMBMSheetNo"+record.getId()).value;
		msLength=dom.get("consumedMBMSheetLength"+record.getId()).value;
		msWidth=dom.get("consumedMBMSheetWidth"+record.getId()).value;
		msDepthOrHeight=dom.get("consumedMBMSheetDH"+record.getId()).value;
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
		if(flag!=true)
			msQuantity=0.00; 
		mbExtraConsumedMSheetDataTable.updateCell(record,mbExtraConsumedMSheetDataTable.getColumn('consumedMBMSheetQuantity'),msQuantity);
		calculateExtraMSheetTotal(type);
	}
	else if(type=="ChangeQty"){
		var record=mbExtraCQMSheetDataTable.getRecord(recId);
		var msNo;
		var msLength;
		var msWidth;
		var msDepthOrHeight;
		var msQuantity=1;
		var flag=false;
		msNo=dom.get("mbExtraCQMSheetNo"+record.getId()).value;
		msLength=dom.get("mbExtraCQMSheetUomLength"+record.getId()).value;
		msWidth=dom.get("mbExtraCQMSheetWidth"+record.getId()).value;
		msDepthOrHeight=dom.get("mbExtraCQMSheetDepthOrHeight"+record.getId()).value;
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
		if(flag!=true)
			msQuantity=0.00; 
		mbExtraCQMSheetDataTable.updateCell(record,mbExtraCQMSheetDataTable.getColumn('mbExtraCQMSheetQuantity'),msQuantity);
		calculateExtraMSheetTotal(type);
	}
	else if(type=="ConsumedChangeQty"){
		var record=mbChangeQtyConsumedMSheetTable.getRecord(recId);
		var msNo;
		var msLength;
		var msWidth;
		var msDepthOrHeight;
		var msQuantity=1;
		var flag=false;
		msNo=dom.get("consumedCQMBMSheetNo"+record.getId()).value;
		msLength=dom.get("consumedCQMBMSheetLength"+record.getId()).value;
		msWidth=dom.get("consumedCQMBMSheetWidth"+record.getId()).value;
		msDepthOrHeight=dom.get("consumedCQMBMSheetDH"+record.getId()).value;
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
		
		if(flag!=true){
			msQuantity="";
		} 
		
		mbChangeQtyConsumedMSheetTable.updateCell(record,mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetQuantity'),msQuantity);
		
		
		var tempCurQty=0;
		if(!isNaN(getNumber(dom.get("consumedCQMBMSheetQuantity"+record.getId()).value)))
			tempCurQty=dom.get("consumedCQMBMSheetQuantity"+record.getId()).value;
		
		var curCumQty=(eval(getNumber(record.getData("hiddenCurCumlvCQMBMSheetQuantity")))-eval(getNumber(record.getData("hiddenCurCQMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
		mbChangeQtyConsumedMSheetTable.updateCell(record,mbChangeQtyConsumedMSheetTable.getColumn('curCumlvCQMBMSheetQuantity'),curCumQty);
		calculateExtraMSheetTotal(type);
	}
}

function calculateExtraMSheetTotal(type){ 
	if(type=="EstimateQty"){
		var deductionTotal=0;
		var nonDeductionTotal=0;
		var Records= mbExtraMSheetDataTable.getRecordSet();
	   	for(var i=0;i<mbExtraMSheetDataTable.getRecordSet().getLength();i++)
	   	{
	   		if(!isNaN(getNumber(dom.get("mbExtraMSheetQuantity" + Records.getRecord(i).getId()).value))){
		    	  if(dom.get("mbExtraMSheetDeduction" + Records.getRecord(i).getId()).checked == true){
		    	  		deductionTotal=deductionTotal+getNumber(dom.get("mbExtraMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  		dom.get("mbExtraMSheetIdentifier" + Records.getRecord(i).getId()).value="D";
		    	  }
		    	  else{
		    	  		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("mbExtraMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  		dom.get("mbExtraMSheetIdentifier" + Records.getRecord(i).getId()).value="A";
		    	  }	
		    }	  
	   	}
		dom.get("qtyTotal").innerHTML=(nonDeductionTotal-deductionTotal);
		dom.get("totalMBExtraMSheetQty").value=dom.get("qtyTotal").innerHTML;
	}
	else if(type=="ConsumedQty"){
		var deductionTotal=0;
		var nonDeductionTotal=0;
		var Records= mbExtraConsumedMSheetDataTable.getRecordSet();
	   	for(var i=0;i<mbExtraConsumedMSheetDataTable.getRecordSet().getLength();i++)
	   	{
	   		if(!isNaN(getNumber(dom.get("consumedMBMSheetQuantity" + Records.getRecord(i).getId()).value))){
		    	  if(dom.get("apprvdMSheetDeduction" + Records.getRecord(i).getId()).checked == true){
		    	  		deductionTotal=deductionTotal+getNumber(dom.get("consumedMBMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  }
		    	  else{
		    	  		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("consumedMBMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  }	
		    }	  
	   	}
		dom.get("consumedQtyTotal").innerHTML=(nonDeductionTotal-deductionTotal);
	}	
	else if(type=="ChangeQty"){
		var deductionTotal=0;
		var nonDeductionTotal=0;
		var Records= mbExtraCQMSheetDataTable.getRecordSet();
	   	for(var i=0;i<mbExtraCQMSheetDataTable.getRecordSet().getLength();i++)
	   	{
	   		if(!isNaN(getNumber(dom.get("mbExtraCQMSheetQuantity" + Records.getRecord(i).getId()).value))){
		    	  if(dom.get("mbExtraCQMSheetDeduction" + Records.getRecord(i).getId()).checked == true){
		    	  		deductionTotal=deductionTotal+getNumber(dom.get("mbExtraCQMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  		dom.get("mbExtraCQMSheetIdentifier" + Records.getRecord(i).getId()).value="D";
		    	  }
		    	  else{
		    	  		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("mbExtraCQMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  		dom.get("mbExtraCQMSheetIdentifier" + Records.getRecord(i).getId()).value="A";
		    	  }	
		    }	  
	   	}
		dom.get("changeQtyTotal").innerHTML=(nonDeductionTotal-deductionTotal);
	}
	else if(type=="ConsumedChangeQty"){
		var deductionTotal=0;
		var nonDeductionTotal=0;
		var cumlvDeductionTotal=0;
		var cumlvNonDeductionTotal=0;
		var Records= mbChangeQtyConsumedMSheetTable.getRecordSet();
	   	for(var i=0;i<mbChangeQtyConsumedMSheetTable.getRecordSet().getLength();i++) 
	   	{
	   		if(!isNaN(getNumber(dom.get("consumedCQMBMSheetQuantity" + Records.getRecord(i).getId()).value))){
		    	  if(dom.get("apprvdMSheetDeduction" + Records.getRecord(i).getId()).checked == true)
		    	  		deductionTotal=deductionTotal+getNumber(dom.get("consumedCQMBMSheetQuantity" + Records.getRecord(i).getId()).value);
		    	  else
		    	  		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("consumedCQMBMSheetQuantity" + Records.getRecord(i).getId()).value);
		    }	  
		    if(dom.get("apprvdMSheetDeduction" + Records.getRecord(i).getId()).checked == true)
		    	cumlvDeductionTotal=cumlvDeductionTotal+getNumber(Records.getRecord(i).getData("curCumlvCQMBMSheetQuantity"));
		    else
		    	cumlvNonDeductionTotal=cumlvNonDeductionTotal+getNumber(Records.getRecord(i).getData("curCumlvCQMBMSheetQuantity"));
	   	}
		dom.get("consumedChangeQtyTotal").innerHTML=(nonDeductionTotal-deductionTotal);
		dom.get("cumulativeCQTotal").innerHTML=(cumlvNonDeductionTotal-cumlvDeductionTotal);
	}
}

function recalculateTotalOnDelete(record,type){
	if(type=="EstimateQty"){
	var oldQuantity=dom.get("mbExtraMSheetQuantity" + record.getId()).value;
	var oldTotal=dom.get("qtyTotal").innerHTML;
 	if(dom.get("mbExtraMSheetDeduction" + record.getId()).checked == true)
		dom.get("qtyTotal").innerHTML=(eval(getNumber(oldTotal))+eval(getNumber(oldQuantity)));
	else
		dom.get("qtyTotal").innerHTML=(getNumber(oldTotal)-getNumber(oldQuantity));
	dom.get("totalMBExtraMSheetQty").value=dom.get("qtyTotal").innerHTML;	
	}	
	else if(type=="ChangeQty"){
		var oldQuantity=dom.get("mbExtraCQMSheetQuantity" + record.getId()).value;
		var oldTotal=dom.get("changeQtyTotal").innerHTML;
	 	if(dom.get("mbExtraCQMSheetDeduction" + record.getId()).checked == true)
			dom.get("changeQtyTotal").innerHTML=(eval(getNumber(oldTotal))+eval(getNumber(oldQuantity)));
		else
			dom.get("changeQtyTotal").innerHTML=(getNumber(oldTotal)-getNumber(oldQuantity));
	}
}

function clearMBExtraMsheetErrorMessage(type){
	if(type=="EstimateQty"){
		dom.get("mbExtraMSheetError").style.display='none';
		document.getElementById("mbExtraMSheetError").innerHTML=''; 
	}
	else if(type=="ConsumedQty"){
		dom.get("mbExtraConsumedMSheetError").style.display='none';
		document.getElementById("mbExtraConsumedMSheetError").innerHTML=''; 
	}
	else if(type=="ChangeQty"){
		dom.get("mbExtraCQMSheetError").style.display='none';
		document.getElementById("mbExtraCQMSheetError").innerHTML=''; 
	}
	else if(type=="ConsumedChangeQty"){
		dom.get("mbCQConsumedMSheetError").style.display='none';
		document.getElementById("mbCQConsumedMSheetError").innerHTML=''; 
	}
}

function validateBeforeSubmitForMBExtraMSheet(type){
	if(type=="EstimateQty"){
		clearMBExtraMsheetErrorMessage(type);
		if(mbExtraMSheetDataTable.getRecordSet().getLength()==0){
			dom.get("mbExtraMSheetError").style.display='';
		    dom.get("mbExtraMSheetError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.zeroLength' />';
			return false;
		}
		else{
			var Records=mbExtraMSheetDataTable.getRecordSet();
			for(var i=0;i<mbExtraMSheetDataTable.getRecordSet().getLength();i++)
	   		{
	   			if(validateForEmptyMBExtraMsheet(Records.getRecord(i).getId(),type)){
	   					dom.get("mbExtraMSheetError").style.display='';
					    dom.get("mbExtraMSheetError").innerHTML=' Please Enter L,W,D/H,Quantity  Details at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   			}
	   			
	   			if(dom.get("mbExtraMSheetQuantity" + Records.getRecord(i).getId()).value==""){
	   					dom.get("mbExtraMSheetError").style.display='';
					    dom.get("mbExtraMSheetError").innerHTML=' Please Input Quantity at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   			}
	   				
	   	  		 for (var j = 2; j < mbExtraMSheetColumnDefs.length-7; j++) { 
	   	  			 if(dom.get("error"+mbExtraMSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display=="block" ||  dom.get("error"+mbExtraMSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display==""){
	   				  	dom.get("mbExtraMSheetError").style.display='';
					    dom.get("mbExtraMSheetError").innerHTML='Please enter a valid value for the '+mbExtraMSheetColumnDefs[j].label.split('<br>')[0]+' at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   				 } 
	  		  	}
	   		}
		}
		if(dom.get("qtyTotal").innerHTML<=0){
			dom.get("mbExtraMSheetError").style.display='';
		    dom.get("mbExtraMSheetError").innerHTML='<s:text name='mb.measurementSheetDataTable.negativeTotal' />';
			return false;
		}


		var activityType;
		if(dom.get("scheduleId").value!="" && dom.get("scheduleId").value!=null){ 
			activityType="sormbQuantity";
		}
		else{
			activityType="nonsormbQuantity";
		}
		if(dom.get(activityType+dom.get("recordId").value).value!=""){
		var msg='<s:text name="mb.measurementSheetDataTable.extraMsheet.resetEntries"/>';
		var ans=confirm(msg);
		if(ans){		
			goToMBExtraItems(type);
			document.getElementById('mbExtraMSheetDetailsContainer').style.display='none';
			return false; 
		}
		else
			 return false;
		}
		else{
			goToMBExtraItems(type);   
			document.getElementById('mbExtraMSheetDetailsContainer').style.display='none';
		 return false;	
		}	
	}
	else if(type=="ConsumedQty"){
		clearMBExtraMsheetErrorMessage(type);
		var flag;
		var Records=mbExtraConsumedMSheetDataTable.getRecordSet();
		for(var i=0;i<mbExtraConsumedMSheetDataTable.getRecordSet().getLength();i++)
		{
				flag=false;
				if(!((dom.get("consumedMBMSheetNo"+Records.getRecord(i).getId()).value=="") &&
	  				(dom.get("consumedMBMSheetLength"+Records.getRecord(i).getId()).value=="") &&
	  				(dom.get("consumedMBMSheetWidth"+Records.getRecord(i).getId()).value=="") &&
	  				(dom.get("consumedMBMSheetDH"+Records.getRecord(i).getId()).value=="") &&	
	  				(dom.get("consumedMBMSheetQuantity"+Records.getRecord(i).getId()).value==""))){
	  					flag=true;	
	  			}
			 	if(dom.get("errorconsumedMBMSheetNo"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedMBMSheetNo"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetNo")!="" &&  dom.get("consumedMBMSheetNo"+Records.getRecord(i).getId()).value=="")){
	 				dom.get("mbExtraConsumedMSheetError").style.display='';
				    dom.get("mbExtraConsumedMSheetError").innerHTML='Please enter a valid value for the Number at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				    dom.get("mbExtraConsumedMSheetError").focus=true;
					return false;	
	 			}
	  		
		  		 if(dom.get("errorconsumedMBMSheetLength"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedMBMSheetLength"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetLength")!="" &&  dom.get("consumedMBMSheetLength"+Records.getRecord(i).getId()).value=="")){
		  			dom.get("mbExtraConsumedMSheetError").style.display='';
				    dom.get("mbExtraConsumedMSheetError").innerHTML='Please enter a valid value for the Length at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
					return false;	mbExtraConsumedMSheetError
		  		}
	  		
		  		 if(dom.get("errorconsumedMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetWidth")!="" &&  dom.get("consumedMBMSheetWidth"+Records.getRecord(i).getId()).value=="")){
		  			dom.get("mbExtraConsumedMSheetError").style.display='';
				    dom.get("mbExtraConsumedMSheetError").innerHTML='Please enter a valid value for the Width at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
					return false;	
		  		}
	  		
	  		 	if(dom.get("errorconsumedMBMSheetDH"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedMBMSheetDH"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetDH")!="" &&  dom.get("consumedMBMSheetDH"+Records.getRecord(i).getId()).value=="")){
		   			 dom.get("mbExtraConsumedMSheetError").style.display='';
		   			 dom.get("mbExtraConsumedMSheetError").innerHTML='Please enter a valid value for the Depth/Height at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
					return false;	
	  			}
	  		
	  			if(dom.get("errorconsumedMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="" || (flag==true && dom.get("consumedMBMSheetQuantity"+Records.getRecord(i).getId()).value=="")){
				 	dom.get("mbExtraConsumedMSheetErrormbExtraConsumedMSheetError").style.display='';
				    dom.get("mbExtraConsumedMSheetError").innerHTML='Please enter a valid value for Quantity at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
					return false;
				 }
	  	}
		if(dom.get("consumedQtyTotal").innerHTML<=0){
			dom.get("mbExtraConsumedMSheetError").style.display='';
		    dom.get("mbExtraConsumedMSheetError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.negativeTotal' />';
			return false;
		}
		goToMBExtraItems(type);
		document.getElementById('mbExtraConsumedMSheetDetailsContainer').style.display='none';
		return false;
	}
	else if(type=="ChangeQty"){
		clearMBExtraMsheetErrorMessage(type);
		if(mbExtraCQMSheetDataTable.getRecordSet().getLength()==0){
			dom.get("mbExtraCQMSheetError").style.display='';
		    dom.get("mbExtraCQMSheetError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.zeroLength' />';
			return false;
		}
		else{
			var Records=mbExtraCQMSheetDataTable.getRecordSet();
			for(var i=0;i<mbExtraCQMSheetDataTable.getRecordSet().getLength();i++)
	   		{
	   			if(validateForEmptyMBExtraMsheet(Records.getRecord(i).getId(),type)){
	   					dom.get("mbExtraCQMSheetError").style.display='';
					    dom.get("mbExtraCQMSheetError").innerHTML=' Please Enter L,W,D/H,Quantity  Details at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   			}
	   			
	   			if(dom.get("mbExtraCQMSheetQuantity" + Records.getRecord(i).getId()).value==""){
	   					dom.get("mbExtraCQMSheetError").style.display='';
					    dom.get("mbExtraCQMSheetError").innerHTML=' Please Input Quantity at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   			}
	   				
	   	  		 for (var j = 2; j < mbExtraCQMSheetColumnDefs.length-7; j++) { 
	   	  			 if(dom.get("error"+mbExtraCQMSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display=="block" ||  dom.get("error"+mbExtraCQMSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display==""){
	   				  	dom.get("mbExtraCQMSheetError").style.display='';
					    dom.get("mbExtraCQMSheetError").innerHTML='Please enter a valid value for the '+mbExtraCQMSheetColumnDefs[j].label.split('<br>')[0]+' at line '+Records.getRecord(i).getData("Sl_No")+'<br>Value should be greater than 0';
						return false;
	   				 } 
	  		  	}
	   		}
		}
		if(dom.get("changeQtyTotal").innerHTML<=0){
			dom.get("mbExtraCQMSheetError").style.display='';
		    dom.get("mbExtraCQMSheetError").innerHTML='<s:text name='mb.measurementSheetDataTable.negativeTotal' />';
			return false;
		}
		
		if(dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).value!=""){
			var msg='<s:text name="mb.measurementSheetDataTable.changeQuantityMsheet.resetEntries"/>';
			var ans=confirm(msg);
			if(ans){		
				goToMBExtraItems(type);
				document.getElementById('mbExtraCQMSheetDetailsContainer').style.display='none';
				return false; 
			}
			else
			 return false;	
		}
		else{ 
			goToMBExtraItems(type);
			document.getElementById('mbExtraCQMSheetDetailsContainer').style.display='none';
		return false;
	}	
	}	
}

function validateForEmptyMBExtraMsheet(recordId,type){
	if(type=="EstimateQty"){
	var mSheet_No=dom.get("mbExtraMSheetNo" + recordId).value;
	var mSheet_Length=dom.get("mbExtraMSheetUomLength" + recordId).value;
	var mSheet_Width=dom.get("mbExtraMSheetWidth" + recordId).value;
	var mSheet_DH=dom.get("mbExtraMSheetDepthOrHeight" + recordId).value;
	var mSheet_Qty=dom.get("mbExtraMSheetQuantity" + recordId).value;
	if(((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0) || (mSheet_Qty!="" && mSheet_Qty!=0)) && 
	((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0)))
		return false; 
	else
			return true;
	}
	else if(type=="ChangeQty"){
		var mSheet_No=dom.get("mbExtraCQMSheetNo" + recordId).value;
		var mSheet_Length=dom.get("mbExtraCQMSheetUomLength" + recordId).value;
		var mSheet_Width=dom.get("mbExtraCQMSheetWidth" + recordId).value;
		var mSheet_DH=dom.get("mbExtraCQMSheetDepthOrHeight" + recordId).value;
		var mSheet_Qty=dom.get("mbExtraCQMSheetQuantity" + recordId).value;
		if(((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0) || (mSheet_Qty!="" && mSheet_Qty!=0)) && 
		((mSheet_No!="" && mSheet_No!=0) || (mSheet_Length!="" && mSheet_Length!=0) || (mSheet_Width!="" && mSheet_Width!=0) || (mSheet_DH!="" && mSheet_DH!=0)))
			return false; 
		else
			return true;
	}	
}

function goToMBExtraItems(type){
	if(type=="EstimateQty"){
		removeExtraItemDetails(dom.get("scheduleId").value,dom.get("nonSorSlNum").value);   
		var records= mbExtraMSheetDataTable.getRecordSet();
	   	for(var i=0;i<mbExtraMSheetDataTable.getRecordSet().getLength();i++){
	   	 	 mbExtraMSheetHiddenDataTable.addRow({
	   	 	 remarks:dom.get("description" + records.getRecord(i).getId()).value, 
	   	 	 no:dom.get("mbExtraMSheetNo" + records.getRecord(i).getId()).value,
	   	 	 uomLength:dom.get("mbExtraMSheetUomLength" + records.getRecord(i).getId()).value,
	   	 	 width:dom.get("mbExtraMSheetWidth" + records.getRecord(i).getId()).value,
	   	 	 depthOrHeight:dom.get("mbExtraMSheetDepthOrHeight" + records.getRecord(i).getId()).value,
	   	 	 identifier:dom.get("mbExtraMSheetIdentifier" + records.getRecord(i).getId()).value,
	   	  	 quantity:dom.get("mbExtraMSheetQuantity" + records.getRecord(i).getId()).value,
	   	  	 sorId:dom.get("mbExtraMSheetSorId" + records.getRecord(i).getId()).value,
	   	  	 extraItemMsheetSlNo:dom.get("mbExtraMSheetSlNo" + records.getRecord(i).getId()).value,
	   	 	 nonSorSlNo:dom.get("mbExtraMSheetNonSorSlNo" + records.getRecord(i).getId()).value,
	   	 	 consumedNo:'',
	   	 	 consumedLength:'',
	   	 	 consumedWidth:'',
	   	 	 consumedDH:'',
	   	 	 consumedQuantity:'',
	   	 	 UOM:dom.get("activityUOM").value});    	 	  
	   	}
	  
	   	if(dom.get("sorquantity"+dom.get("recordId").value)!=null) {
	   		dom.get("sorquantity"+dom.get("recordId").value).value=dom.get("qtyTotal").innerHTML;
	   		calculateSOR(dom.get("sorquantity"+dom.get("recordId").value),dom.get("recordId").value); 
	   		dom.get("sorquantity"+dom.get("recordId").value).disabled=true;
	   		dom.get("sormbQuantity"+dom.get("recordId").value).disabled=true;
	   		dom.get("sormbQuantity"+dom.get("recordId").value).value="";
	   	}
	   	if(dom.get("nonsorquantity"+dom.get("recordId").value)!=null) {
		   	dom.get("nonsorquantity"+dom.get("recordId").value).value=dom.get("qtyTotal").innerHTML;
		   	calculateNonSOR(dom.get("nonsorquantity"+dom.get("recordId").value),dom.get("recordId").value);	 
		   	dom.get("nonsorquantity"+dom.get("recordId").value).disabled=true; 
		   	dom.get("nonsormbQuantity"+dom.get("recordId").value).disabled=true; 
		   	dom.get("nonsormbQuantity"+dom.get("recordId").value).value="";
		}
	}
	else if(type=="ConsumedQty"){
	
		var records= mbExtraConsumedMSheetDataTable.getRecordSet();
		var hidTabRecords= mbExtraMSheetHiddenDataTable.getRecordSet();
		var record;
		var param1;
		var param2;
		if(dom.get("scheduleId").value!="" && dom.get("scheduleId").value!=null){ 
			param1="mbExtraConsumedMSheetSorId";
			param2="sorId";
		}
		else{
			param1="mbExtraConsumedMSheetNonSorSlNo";
			param2="nonSorSlNo";
		}

		
	   	for(var i=0;i<mbExtraConsumedMSheetDataTable.getRecordSet().getLength();i++){
	   	 	for(var j=0;j<mbExtraMSheetHiddenDataTable.getRecordSet().getLength();j++){
	   	 		record=mbExtraMSheetHiddenDataTable.getRecord(hidTabRecords.getRecord(j).getId());
	   	 		if(dom.get(param1+records.getRecord(i).getId()).value==dom.get(param2+hidTabRecords.getRecord(j).getId()).value && 
	   	 			dom.get("mbExtraConsumedMSheetSlNo"+records.getRecord(i).getId()).value==dom.get("extraItemMsheetSlNo"+hidTabRecords.getRecord(j).getId()).value){
	   	 				mbExtraMSheetHiddenDataTable.updateCell(record,mbExtraMSheetHiddenDataTable.getColumn('consumedNo'),dom.get("consumedMBMSheetNo"+records.getRecord(i).getId()).value);
	   	 				mbExtraMSheetHiddenDataTable.updateCell(record,mbExtraMSheetHiddenDataTable.getColumn('consumedLength'),dom.get("consumedMBMSheetLength"+records.getRecord(i).getId()).value);
	   	 				mbExtraMSheetHiddenDataTable.updateCell(record,mbExtraMSheetHiddenDataTable.getColumn('consumedWidth'),dom.get("consumedMBMSheetWidth"+records.getRecord(i).getId()).value);
	   	 				mbExtraMSheetHiddenDataTable.updateCell(record,mbExtraMSheetHiddenDataTable.getColumn('consumedDH'),dom.get("consumedMBMSheetDH"+records.getRecord(i).getId()).value);
	   	 				mbExtraMSheetHiddenDataTable.updateCell(record,mbExtraMSheetHiddenDataTable.getColumn('consumedQuantity'),dom.get("consumedMBMSheetQuantity"+records.getRecord(i).getId()).value);
	   	 		}
		   	} 	
	   	}
	  
	   	if(dom.get("sormbQuantity"+dom.get("recordId").value)!=null) {
	   		dom.get("sormbQuantity"+dom.get("recordId").value).value=dom.get("consumedQtyTotal").innerHTML;
	   		calculateSOR(dom.get("sormbQuantity"+dom.get("recordId").value),dom.get("recordId").value);  
	   		dom.get("sormbQuantity"+dom.get("recordId").value).disabled=true;
	   	}
	   	if(dom.get("nonsormbQuantity"+dom.get("recordId").value)!=null) {
		   	dom.get("nonsormbQuantity"+dom.get("recordId").value).value=dom.get("consumedQtyTotal").innerHTML;
		   	calculateNonSOR(dom.get("nonsormbQuantity"+dom.get("recordId").value),dom.get("recordId").value);	 
		   	dom.get("nonsormbQuantity"+dom.get("recordId").value).disabled=true;  
		}
		 
	}
	else if(type=="ChangeQty"){ 
		removeChangeQtyItemDetails(dom.get("wOActivityIdCQ").value);
		var records= mbExtraCQMSheetDataTable.getRecordSet();
	   	for(var i=0;i<mbExtraCQMSheetDataTable.getRecordSet().getLength();i++){
	   	 	 mbExtraCQMSheetHiddenDataTable.addRow({
	   	 	 remarks:dom.get("description" + records.getRecord(i).getId()).value, 
	   	 	 no:dom.get("mbExtraCQMSheetNo" + records.getRecord(i).getId()).value,
	   	 	 uomLength:dom.get("mbExtraCQMSheetUomLength" + records.getRecord(i).getId()).value,
	   	 	 width:dom.get("mbExtraCQMSheetWidth" + records.getRecord(i).getId()).value,
	   	 	 depthOrHeight:dom.get("mbExtraCQMSheetDepthOrHeight" + records.getRecord(i).getId()).value,
	   	  	 quantity:dom.get("mbExtraCQMSheetQuantity" + records.getRecord(i).getId()).value,
	   	  	 identifier:dom.get("mbExtraCQMSheetIdentifier" + records.getRecord(i).getId()).value,
	   	  	 changeQtyWOActivityId:dom.get("mbExtraCQWOAId" + records.getRecord(i).getId()).value,
	   	  	 changeQuantityMsheetSlNo:dom.get("mbExtraCQMSheetSlNo" + records.getRecord(i).getId()).value,
	   	 	 UOM:dom.get("activityUOMForCQ").value});    	 	  
	   	}
	
	   	if(dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value)!=null) {
	   		dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value).value=dom.get("changeQtyTotal").innerHTML;
	   		calculateExtraItems(dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value),dom.get("recordIdForCQ").value); 
	   		dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value).disabled=true;
	   		dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).disabled=true;
	   		dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).value="";
	   	} 
	}
}

function removeExtraItemDetails(scheduleId,nonSorSlNo){
	if(mbExtraMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbExtraMSheetHiddenDataTable.getRecordSet();
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
				mbExtraMSheetHiddenDataTable.deleteRow(records.getRecord(i--)); 
		}
	}
}

function reLoadMBExtraMsheetTable(scheduleId,nonSorSlNo){
	var recordIndex=0; 
	if(mbExtraMSheetHiddenDataTable.getRecordSet().getLength()>0){
		mbExtraMSheetDataTable.deleteRows(0,mbExtraMSheetDataTable.getRecordSet().getLength()); 
		dom.get("qtyTotal").innerHTML="0.00";
		var records= mbExtraMSheetHiddenDataTable.getRecordSet();
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
				mbExtraMSheetDataTable.addRow({
									      Sl_No:mbExtraMSheetDataTable.getRecordSet().getLength()+1,
										  description:dom.get("remarks" + records.getRecord(i).getId()).value, 
				 						  mbExtraMSheetNo:dom.get("no" + records.getRecord(i).getId()).value,
				 						  mbExtraMSheetUomLength:dom.get("uomLength" + records.getRecord(i).getId()).value,
				 						  mbExtraMSheetWidth:dom.get("width" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetDepthOrHeight:dom.get("depthOrHeight" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetQuantity:dom.get("quantity" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetUOM: dom.get("activityUOM").value,
   	 	 								  mbExtraMSheetIdentifier:dom.get("identifier" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetDeduction:'',
   	 	 								  mbExtraMSheetSorId:dom.get("sorId" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetNonSorSlNo:dom.get("nonSorSlNo" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraMSheetSlNo:dom.get("extraItemMsheetSlNo" + records.getRecord(i).getId()).value,
   	 	 								  Delete:'X' 
   	 	 								});
   	 	 								var record = mbExtraMSheetDataTable.getRecord(parseInt(recordIndex++));
				                    	if(dom.get("mbExtraMSheetIdentifier"+record.getId()).value=='D')
				                    	 dom.get("mbExtraMSheetDeduction"+record.getId()).checked=true;
				                    	else
				                    	 dom.get("mbExtraMSheetDeduction"+record.getId()).checked=false;
				                    	calculateExtraMSheetTotal("EstimateQty");  
   	 			}
   	 			
		} 
   	}
   	else{
	   	mbExtraMSheetDataTable.deleteRows(0,mbExtraMSheetDataTable.getRecordSet().getLength());
	   	dom.get("qtyTotal").innerHTML="0.00";
   	}
   
}

function resetMBExtraMSheet() {
	var msg='<s:text name="mb.measurementSheetDataTable.resetEntries"/>';
	var ans=confirm(msg);
		
	if(ans) {
		if(mbExtraMSheetDataTable.getRecordSet().getLength()>0){
			removeExtraItemDetails(dom.get("scheduleId").value,dom.get("nonSorSlNum").value);
		}
		mbExtraMSheetDataTable.deleteRows(0,mbExtraMSheetDataTable.getRecordSet().getLength());
		document.getElementById('mbExtraMSheetDetailsContainer').style.display='none';
		
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
		else if(dom.get("nonSorSlNum").value!="" && dom.get("nonSorSlNum").value!=null){
		   	if(dom.get("nonsorquantity"+dom.get("recordId").value)!=null) {
		   		var record = nonSorDataTable.getRecord(dom.get("recordId").value);
			   	dom.get("nonsorquantity"+dom.get("recordId").value).value="";
			   	calculateNonSOR(dom.get("nonsorquantity"+dom.get("recordId").value),dom.get("recordId").value);	 
			   	dom.get("nonsorquantity"+dom.get("recordId").value).disabled=false; 
			   	dom.get("nonsorquantity"+dom.get("recordId").value).readOnly=false; 
		   	 	nonSorDataTable.updateCell(record,nonSorDataTable.getColumn("nonSorMSheetLength"),0);  
			}
		}
		
	}
	else {
		return false;		
	}
}



function createTextBoxFormatter_ExtraItem(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="uomLength" || oColumn.getKey()=="width" || oColumn.getKey()=="depthOrHeight" || oColumn.getKey()=="no" || oColumn.getKey()=="quantity" || oColumn.getKey()=="consumedNo" || oColumn.getKey()=="consumedWidth" || oColumn.getKey()=="consumedLength" || oColumn.getKey()=="consumedDH" || oColumn.getKey()=="consumedQuantity"){
		fieldName="measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	}
	else if(oColumn.getKey()=="mbExtraMSheetNo" || oColumn.getKey()=="mbExtraMSheetUomLength" || oColumn.getKey()=="mbExtraMSheetWidth" || oColumn.getKey()=="mbExtraMSheetDepthOrHeight" || oColumn.getKey()=="mbExtraMSheetQuantity"){
		fieldName= oColumn.getKey()+oRecord.getId();
		var qType="EstimateQty";
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateExtraMsheetQty(this,\""+qType+"\",\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	}
   	else if(oColumn.getKey()=="consumedMBMSheetNo" || oColumn.getKey()=="consumedMBMSheetLength" || oColumn.getKey()=="consumedMBMSheetWidth" || oColumn.getKey()=="consumedMBMSheetDH" || oColumn.getKey()=="consumedMBMSheetQuantity"){
   		fieldName= oColumn.getKey()+oRecord.getId();
   		var qType="ConsumedQty";
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur=calculateExtraMsheetQty(this,\""+qType+"\",\""+oRecord.getId()+"\");'' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	}
   	else if(oColumn.getKey()=="mbExtraCQMSheetNo" || oColumn.getKey()=="mbExtraCQMSheetUomLength" || oColumn.getKey()=="mbExtraCQMSheetWidth" || oColumn.getKey()=="mbExtraCQMSheetDepthOrHeight" || oColumn.getKey()=="mbExtraCQMSheetQuantity"){
   		fieldName= oColumn.getKey()+oRecord.getId();
   		var qType="ChangeQty";
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur=calculateExtraMsheetQty(this,\""+qType+"\",\""+oRecord.getId()+"\");'' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	}
   	else if(oColumn.getKey()=="consumedCQMBMSheetNo" || oColumn.getKey()=="consumedCQMBMSheetLength" || oColumn.getKey()=="consumedCQMBMSheetWidth" || oColumn.getKey()=="consumedCQMBMSheetDH" || oColumn.getKey()=="consumedCQMBMSheetQuantity"){
   		fieldName= oColumn.getKey()+oRecord.getId();
   		var qType="ConsumedChangeQty";
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur=calculateExtraMsheetQty(this,\""+qType+"\",\""+oRecord.getId()+"\");'' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	}
    el.innerHTML = markup; 
	}
	return textboxFormatter;
} 
var extraItemTextBoxFormatter = createTextBoxFormatter_ExtraItem(11,7);
var extraItemQtyFormatter = createTextBoxFormatter_ExtraItem(13,13);
var extraItemNoFormatter = createTextBoxFormatter_ExtraItem(9,7);

var workOrderActivityIDFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
	fieldName = "mbMeasurementSheetList[" + oRecord.getCount() + "].mbDetails.workOrderActivity.id";
   	markup="<input type='text' id='"+id+"' name='"+fieldName+ "' value='"+value+"' size='6'/>"; 
    el.innerHTML = markup; 
}

// For Consumed Quantity
var mbExtraConsumedMSheetDataTable;
var mbExtraConsumedMSheetColumnDefs;
var makeDetailedConsumedMBMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbExtraConsumedMSheetColumnDefs = [ 
   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
   	
   	{key:"mbExtraConsumedMSheetSorId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false,hidden:true,resizeable:false},
   	{key:"mbExtraConsumedMSheetNonSorSlNo",formatter:nonSorSlNoFormatter,sortable:false,hidden:true,resizeable:false},
   	{key:"mbExtraConsumedMSheetSlNo",label:"mbExtraMSheetSlNo",formatter:nonSorSlNoFormatter,hidden:true,sortable:false, resizeable:false},
   	
	{label:'<b><s:text name="mbMSheet.approved.quantity"/></b>',
        children: [ 
            {key:"apprvdMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDeduction",label:'<s:text name="mb.measurementSheetDataTable.deduction" />',formatter:checkboxFormatter,sortable:false,resizeable:false,readOnly:true},
            {key:"apprvdMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',sortable:false, resizeable:false}
        ] 
	},

	{label:'<span class="mandatory">*</span><b><s:text name="measurementbook.mb.entry"/></b>',
		children: [ 
			{key:"consumedMBMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',formatter:extraItemNoFormatter,sortable:false, resizeable:false},
		    {key:"consumedMBMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedMBMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedMBMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedMBMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',formatter:extraItemQtyFormatter,sortable:false, resizeable:false}
         ] 
	}
  ];
    var mbExtraConsumedMSheetDataSource = new YAHOO.util.DataSource();
    mbExtraConsumedMSheetDataTable = new YAHOO.widget.DataTable("mbExtraConsumedMSheetTable",mbExtraConsumedMSheetColumnDefs, mbExtraConsumedMSheetDataSource);
    mbExtraConsumedMSheetDataTable.subscribe("cellClickEvent", mbExtraConsumedMSheetDataTable.onEventShowCellEditor); 
    mbExtraConsumedMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target); 
        var column = this.getColumn(target); 
    });
    var tfoot = mbExtraConsumedMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 8;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk'; 
	td.id = 'mbExtraConsumedMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	
	addCell(tr,2,'estimatedQtyTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
	addCell(tr,6,'filler','');
	addCell(tr,7,'consumedQtyTotal','0.00');
    return {
        oDS: mbExtraConsumedMSheetDataSource,
        oDT: mbExtraConsumedMSheetDataTable 
    };
}

function reLoadMBExtraConsumedMsheetTable(scheduleId,nonSorSlNo){
	var recordIndex=0;
	dom.get("mbExtraConsumedMSheetError").innerHTML='';
	if(mbExtraMSheetHiddenDataTable.getRecordSet().getLength()>0){
		mbExtraConsumedMSheetDataTable.deleteRows(0,mbExtraConsumedMSheetDataTable.getRecordSet().getLength()); 
		var records= mbExtraMSheetHiddenDataTable.getRecordSet();
		var param1;
		var param2;
		var deductionTotal=0;
		var nonDeductionTotal=0;
		if(scheduleId!="" && scheduleId!=null){ 
			param1="sorId";
			param2=scheduleId;
		}
		else{
			param1="nonSorSlNo";
			param2=nonSorSlNo;
		}
		var count=0;
		for(var i=0;i<records.getLength();i++){ 
				if(dom.get(param1+records.getRecord(i).getId()).value==param2){
				count++;
					mbExtraConsumedMSheetDataTable.addRow({
										      SlNo:mbExtraConsumedMSheetDataTable.getRecordSet().getLength()+1,
					 						  apprvdMSheetNo:dom.get("no" + records.getRecord(i).getId()).value,
					 						  apprvdMSheetLength:dom.get("uomLength" + records.getRecord(i).getId()).value,
					 						  apprvdMSheetWidth:dom.get("width" + records.getRecord(i).getId()).value,
	   	 	 								  apprvdMSheetDH:dom.get("depthOrHeight" + records.getRecord(i).getId()).value,
	   	 	 								  apprvdMSheetQuantity:dom.get("quantity" + records.getRecord(i).getId()).value,
	   	 	 								  apprvdMSheetDeduction:'',
	   	 	 								  mbExtraConsumedMSheetSorId:dom.get("sorId" + records.getRecord(i).getId()).value,
	   	 	 								  mbExtraConsumedMSheetNonSorSlNo:dom.get("nonSorSlNo" + records.getRecord(i).getId()).value,
	   	 	 								  mbExtraConsumedMSheetSlNo:dom.get("extraItemMsheetSlNo" + records.getRecord(i).getId()).value,
	   	 	 								  
	   	 	 								  consumedMBMSheetNo:dom.get("consumedNo" + records.getRecord(i).getId()).value,
	   	 	 								  consumedMBMSheetLength:dom.get("consumedLength" + records.getRecord(i).getId()).value,
	   	 	 								  consumedMBMSheetWidth:dom.get("consumedWidth" + records.getRecord(i).getId()).value,
	   	 	 								  consumedMBMSheetDH:dom.get("consumedDH" + records.getRecord(i).getId()).value,
	   	 	 								  consumedMBMSheetQuantity:dom.get("consumedQuantity" + records.getRecord(i).getId()).value
	   	 	 								});
	   	 	 								
	   	 	 								var record = mbExtraConsumedMSheetDataTable.getRecord(parseInt(recordIndex++));
	   	 	 								disableMBExtraConsumedMsheetFields(record,"ConsumedQty");
					                    	if(dom.get("identifier" + records.getRecord(i).getId()).value=='D'){
					                    	 dom.get("apprvdMSheetDeduction"+record.getId()).checked=true;
					                    	 deductionTotal=deductionTotal+getNumber(dom.get("quantity" + records.getRecord(i).getId()).value);
					                    	} 
					                    	else{
					                    	 dom.get("apprvdMSheetDeduction"+record.getId()).checked=false;
					                    	 nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("quantity" + records.getRecord(i).getId()).value);
					                    	} 
					                    	dom.get("apprvdMSheetDeduction"+record.getId()).disabled='true';
	    									dom.get("apprvdMSheetDeduction"+record.getId()).readOnly='true';
					                    	calculateExtraMSheetTotal("ConsumedQty");  
	   	 			}
	   	 			
			} 
		if(count==0){	
			alert("No Estimate Measurement details Defined for this Activity");
			return false; 
		}
		else{
			dom.get("estimatedQtyTotal").innerHTML=nonDeductionTotal-deductionTotal;
			return true;
		}				 
	}
	else{
		alert("No Estimate Measurement details Defined for this Activity");
		return false; 
	}
}

function addRowToMBExtraEstimatedMSheet(){
	if(mbExtraMSheetDataTable.getRecordSet().getLength()=="" || mbExtraMSheetDataTable.getRecordSet().getLength()<1){
		mbExtraMSheetDataTable.addRow({Sl_No:mbExtraMSheetDataTable.getRecordSet().getLength()+1,mbExtraMSheetUomLength:'1',mbExtraMSheetWidth:'1',mbExtraMSheetSorId:dom.get('scheduleId').value,mbExtraMSheetSlNo:mbExtraMSheetDataTable.getRecordSet().getLength()+1,mbExtraMSheetNonSorSlNo:dom.get('nonSorSlNum').value,mbExtraMSheetUOM:dom.get('activityUOM').value});calculateExtraMsheetQty(dom.get(mbExtraMSheetColumnDefs[3].key+mbExtraMSheetDataTable.getRecordSet().getRecord(mbExtraMSheetDataTable.getRecordSet().getLength()-1).getId()),"EstimateQty",mbExtraMSheetDataTable.getRecordSet().getRecord(mbExtraMSheetDataTable.getRecordSet().getLength()-1).getId());return false;
	}else{ 
			var Records= mbExtraMSheetDataTable.getRecordSet();
			mbExtraMSheetDataTable.addRow({Sl_No:mbExtraMSheetDataTable.getRecordSet().getLength()+1,mbExtraMSheetUomLength:'1',mbExtraMSheetWidth:'1',mbExtraMSheetSorId:dom.get('scheduleId').value,mbExtraMSheetSlNo:eval(dom.get('mbExtraMSheetSlNo' + Records.getRecord(Records.getLength()-1).getId()).value)+1,mbExtraMSheetNonSorSlNo:dom.get('nonSorSlNum').value,mbExtraMSheetUOM:dom.get('activityUOM').value});calculateExtraMsheetQty(dom.get(mbExtraMSheetColumnDefs[3].key+mbExtraMSheetDataTable.getRecordSet().getRecord(mbExtraMSheetDataTable.getRecordSet().getLength()-1).getId()),"EstimateQty",mbExtraMSheetDataTable.getRecordSet().getRecord(mbExtraMSheetDataTable.getRecordSet().getLength()-1).getId());return false;
		}
}


function disableExtraMSheetTable(type){
	if(type=="EstimateQty"){
		document.getElementById('buttonsExtraItem').style.display="none";
		var records= mbExtraMSheetDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			dom.get("description"+records.getRecord(i).getId()).disabled=true;
			dom.get("description"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetNo"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetNo"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetUomLength"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetUomLength"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetWidth"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetWidth"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetDepthOrHeight"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetDepthOrHeight"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetQuantity"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetQuantity"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraMSheetDeduction"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraMSheetDeduction"+records.getRecord(i).getId()).readonly=true;
		}  
	}
	else if(type=="ConsumedQty"){
		document.getElementById('buttonsExtraConsumedItem').style.display="none";
		var records= mbExtraConsumedMSheetDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			dom.get("consumedMBMSheetNo"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedMBMSheetNo"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedMBMSheetLength"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedMBMSheetLength"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedMBMSheetWidth"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedMBMSheetWidth"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedMBMSheetDH"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedMBMSheetDH"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedMBMSheetQuantity"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedMBMSheetQuantity"+records.getRecord(i).getId()).readonly=true;
		}  
	}
	else if(type=="ChangeQty"){
		document.getElementById('buttonsExtraCQItem').style.display="none";
		var records= mbExtraCQMSheetDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			dom.get("description"+records.getRecord(i).getId()).disabled=true;
			dom.get("description"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetNo"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetNo"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetUomLength"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetUomLength"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetWidth"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetWidth"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetDepthOrHeight"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetDepthOrHeight"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetQuantity"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetQuantity"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("mbExtraCQMSheetDeduction"+records.getRecord(i).getId()).disabled=true;
			dom.get("mbExtraCQMSheetDeduction"+records.getRecord(i).getId()).readonly=true;
		}  
	}
	else if(type=="ConsumedChangeQty"){
		document.getElementById('buttonsCQConsumedItem').style.display="none";
		var records= mbChangeQtyConsumedMSheetTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			dom.get("consumedCQMBMSheetNo"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedCQMBMSheetNo"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedCQMBMSheetLength"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedCQMBMSheetLength"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedCQMBMSheetWidth"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedCQMBMSheetWidth"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedCQMBMSheetDH"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedCQMBMSheetDH"+records.getRecord(i).getId()).readonly=true;
			
			dom.get("consumedCQMBMSheetQuantity"+records.getRecord(i).getId()).disabled=true;
			dom.get("consumedCQMBMSheetQuantity"+records.getRecord(i).getId()).readonly=true;
		}  
	}
} 

</script>
 
<s:hidden name="totalMBExtraMSheetQty" id="totalMBExtraMSheetQty"/>
<s:hidden name="scheduleId" id="scheduleId" value="%{scheduleId}"></s:hidden>
<s:hidden name="nonSorSlNum" id="nonSorSlNum" value="%{nonSorSlNum}"></s:hidden>
<s:hidden name="activityUOM" id="activityUOM" value="%{activityUOM}"></s:hidden>
<s:hidden name="recordId" id="recordId" value="%{recordId}"></s:hidden>

<div style="position: obsolute;" id="mbExtraMSheetDetailsContainer" class="tableContainer">
	
	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBExtraMSheet(this)" id="minimizeEstimatedMsheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBExtraMSheet(this)" id="closeEstimatedMsheet" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
		  
	<div id="mbExtraMSheetError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedMBExtraMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="19" class="shadowwk"></td>
        </tr>
        <tr>
          <td colspan="19" class="headingwk" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
          </td>
          <td class="headingwk" id="showAdd" name="showAdd" align="right" style="border-left-width: 0px;">
			<a  id="addMBMSheet" name="addMBMSheet" onclick="addRowToMBExtraEstimatedMSheet();" href="#">  
			<img  width="16" height="16" border="0" src="/egworks/image/add.png"/> 
			</a>
		  </td>
        </tr>
      
        <tr>
          <td  colspan="10">
          <div class="yui-skin-sam">
              <div id="mbExtraMSheetTable"></div> 
              <div id="mbExtraMSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttonsExtraItem" name="buttonsExtraItem" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitMBExtraMSheet" onclick="return validateBeforeSubmitForMBExtraMSheet('EstimateQty');" /> 
	 				<input type="button" class="buttonfinal" value="RESET" id="resetButton" name="resetButton" onclick="resetMBExtraMSheet()"/>  
       			 </div>
        	</td>
        </tr>
        
	</table>
</div>	
					
<table id="detailedMBExtraMSheetHiddenTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none;">
	<tr>
	  <td colspan="9" class="headingwk" style="border-right-width: 0px;">
	   <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div> 
	   <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
	  </td>
	</tr>
         
    <tr>
      <td  colspan="10">
      <div class="yui-skin-sam">
          <div id="mbExtraMSheetHiddenTable"></div> 
      </div>
      </td>
    </tr>
</table> 


<div style="position: obsolute;" id="mbExtraConsumedMSheetDetailsContainer" class="tableContainer">
	
	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBExtraMSheet(this)" id="minimizeConsumedMsheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBExtraMSheet(this)" id="closeConsumedMsheet" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
		  
	<div id="mbExtraConsumedMSheetError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedMBConsumedMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="19" class="shadowwk"></td>
        </tr>
        <tr>
          <td colspan="19" class="headingwk" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
          </td>
      	  
        </tr>
      
        <tr>
          <td  colspan="10">
          <div class="yui-skin-sam">
              <div id="mbExtraConsumedMSheetTable"></div> 
              <div id="mbExtraConsumedMSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttonsExtraConsumedItem" name="buttonsExtraConsumedItem" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitMBExtraConsumedMSheet" onclick="return validateBeforeSubmitForMBExtraMSheet('ConsumedQty');" /> 
       			 </div>
        	</td>
        </tr>
        
	</table>
</div>	

<script>
  makeDetailedMBMSheetDataTable();
  makeDetailedMBMSheetHiddenDataTable();
  makeDetailedConsumedMBMSheetDataTable();
  count=1;
  recordIndex=0;
						  <s:iterator id="mSheetIterator" value="measurementSheetList" status="row_status">
						  <s:if test="%{activity!=null}">		
						   	mbExtraMSheetHiddenDataTable.addRow({	                        
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
							 uomLength:'<s:property value="uomLength"/>',
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
							
							<s:if test="%{quantity==0.0}">
							 quantity:'',
							</s:if> 
							<s:elseif test="%{quantity>0.0}">
							 quantity:'<s:property value="quantity"/>',
							</s:elseif>
	                             
	                        UOM: '<s:property value="activity.Uom.uom"/>',
	                        identifier:'<s:property value="identifier"/>', 
	                    
	                         <s:if test="%{activity.nonSor!=null}">
	                         	nonSorSlNo: '<s:property value="activity.srlNo" />',
	                         </s:if> 
	                         
	                        extraItemMsheetSlNo: '<s:property value="mbExtraItemSlNo" />',
	                         
	                        sorId: '<s:property value="activity.schedule.id" />',
	                        
	                        
	                        <s:if test="%{consumedNo==0.0}">
	                       	 consumedNo:'',
	                       	</s:if> 
	                       	<s:elseif test="%{consumedNo>0.0}">
	                       	 consumedNo:'<s:property value="consumedNo"/>',
	                       	</s:elseif>
	                       	
	                       	<s:if test="%{consumedLength==0.0}">
							 consumedLength:'',
							</s:if> 
							<s:elseif test="%{consumedLength>0.0}">
							 consumedLength:'<s:property value="consumedLength"/>',
							</s:elseif>
							
							<s:if test="%{consumedWidth==0.0}">
							 consumedWidth:'',
							</s:if> 
							<s:elseif test="%{consumedWidth>0.0}">
							 consumedWidth:'<s:property value="consumedWidth"/>',
							</s:elseif>
							
	                      	<s:if test="%{consumedDH==0.0}">
							 consumedDH:'',
							</s:if> 
							<s:elseif test="%{consumedDH>0.0}">
							 consumedDH:'<s:property value="consumedDH"/>',
							</s:elseif>
							
							<s:if test="%{consumedQuantity==0.0}">
							 consumedQuantity:''
							</s:if> 
							<s:elseif test="%{consumedQuantity>0.0}">
							 consumedQuantity:'<s:property value="consumedQuantity"/>'
							</s:elseif>
	                    	}); 
	                    	</s:if>
						  </s:iterator>
</script>
