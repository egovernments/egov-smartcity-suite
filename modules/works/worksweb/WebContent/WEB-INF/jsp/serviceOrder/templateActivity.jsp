<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
	
.yui-dt-col-rateValue{
	text-align:right;
}		

</style>

<script>

var isValidRate=true;
function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var fieldName = "soTemplateActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();
    if(oColumn.getKey()=='rateValue'){
    	el.innerHTML="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectamountwk' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='validateNumberInSOTableCell(this);' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    }else{
   		el.innerHTML="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='slnowk' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='validateNumberInSOTableCell(this);' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    }
}
return textboxFormatter;
}
var sTextboxFormatter = createTextBoxFormatter(3,3);
var rTextBoxFormatter = createTextBoxFormatter(5,5);
var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
   var fieldName = "soTemplateActivities[" + oRecord.getCount() + "]." + "description";
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='4000' name='"+fieldName+"'/>"
	el.innerHTML = markup;	 	
}
var temptActvDataTable;
var makeTmptActvDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var tmptActvColumnDefs = [ 
		{key:"stageNo", label:'Srl No<span class="mandatory">*</span>', width:50,formatter:sTextboxFormatter},
		{key:"description", width:475,label:'Description<span class="mandatory">*</span>', formatter:textboxDescFormatter, resizeable:true},		
		{key:"rateValue",label:'Rate<span class="mandatory">*</span>',width:50, formatter:rTextBoxFormatter},
		{key:'TemptActvDelete',label:'Delete',width:50,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var temptActvDataSource = new YAHOO.util.DataSource(); 
	temptActvDataTable = new YAHOO.widget.DataTable("tmptAtctTable",tmptActvColumnDefs, temptActvDataSource);
	temptActvDataTable.subscribe("cellClickEvent", temptActvDataTable.onEventShowCellEditor); 
	temptActvDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'TemptActvDelete') { 			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	
	return {
	    oDS: temptActvDataSource,
	    oDT: temptActvDataTable
	};        

}

function validateNumberInSOTableCell(elem){
		dom.get('error'+elem.id).style.display='none';
		if(isNaN(elem.value) || getNumber(elem.value)<=0){
			dom.get('error'+elem.id).style.display='';
			isValidRate=false;
		}else{
			isValidRate=true;
		}
}

function validateRate(){

	if(temptActvDataTable.getRecordSet().getLength()==0){
		isValidRate=true;
	}

	if(isValidRate){
		temptActvDataTable.addRow({SlNo:temptActvDataTable.getRecordSet().getLength()+1});
	}else{
		alert("Please enter Valid Rate and Srl No");
		return false;
	}
}
</script>		
      
       
		<table id="temptActvTable" width="100%" border="0" cellspacing="0" cellpadding="0">              	
              	<tr>
                	<td colspan="9" class="headingwk" style="border-right-width: 0px" align="left">
                		<div class="arrowiconwk"><image src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                		<div class="headplacer" >Template Activity Details</div>
                	</td>
                	<td  align="right" class="headingwk" style="border-left-width: 0px">
                	<s:if test="%{mode!='view'}">
                	<a id="temptActvRow" href="#" onclick="validateRate();return false;"><img height="16" border="0" width="16" alt="Add Template Activity" src="${pageContext.request.contextPath}/image/add.png" /></a>
                	</s:if>
                	</td>
              	</tr>
              		<tr>
                	<td>
                	<div class="yui-skin-sam">
                	    <div id="tmptAtctTable" align="center"></div>  
                	</div>                  	
                    	</td>
                 </tr>
              	</table>
              	
	<script>
		makeTmptActvDataTable();
		<s:iterator  value="soTemplateActivities" status="row_status">
            temptActvDataTable.addRow({
                                    stageNo:'<s:property value="stageNo"/>',
                                    description:'<s:property value="description" escape="false"/>',
                                    rateValue:'<s:property value="rateValue"/>',
                                    Delete:'X'});
        var record = temptActvDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = temptActvDataTable.getColumn('description');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="description" escape="false"/>';
	
	 var column = temptActvDataTable.getColumn('stageNo');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="stageNo" />';
        
 	var column = temptActvDataTable.getColumn('rateValue');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="rateValue" />';

        </s:iterator>
	</script> 
	
 