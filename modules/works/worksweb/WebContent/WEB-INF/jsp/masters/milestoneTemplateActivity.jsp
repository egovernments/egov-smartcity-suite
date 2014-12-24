<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
	
.yui-dt-col-percentage{
	text-align:right;
}		

</style>

<script>

var isValidPercentage=true;
function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var fieldName = "templateActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();
    if(oColumn.getKey()=='percentage'){
    	el.innerHTML="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectamountwk' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='validateNumberInTableCell(this);calculateTotalPercentage();' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
    }else{
   		el.innerHTML="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='slnowk' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='validateNumberInTableCell(this);' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
    }
}
return textboxFormatter;
}
var sTextboxFormatter = createTextBoxFormatter(6,5);
var pTextBoxFormatter = createTextBoxFormatter(6,5);
var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
   var fieldName = "templateActivities[" + oRecord.getCount() + "]." + "description";
   var id=oColumn.getKey()+oRecord.getId();
	markup="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='100' maxlength='1024' onblur='validateDescription(this);' name='"+fieldName+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>"
	el.innerHTML = markup;	 	
}
var temptActvDataTable;
var makeTmptActvDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var tmptActvColumnDefs = [ 
		{key:"stageOrderNo", label:'Stage Order No<span class="mandatory">*</span>', width:100,formatter:sTextboxFormatter},
		{key:"description", width:650,label:'Stage Description<span class="mandatory">*</span>', formatter:textboxDescFormatter, resizeable:true},		
		{key:"percentage",label:'Percentage of Stage<span class="mandatory">*</span>',width:100, formatter:pTextBoxFormatter},
		{key:'TemptActvDelete',label:'Delete',width:50,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var temptActvDataSource = new YAHOO.util.DataSource(); 
	temptActvDataTable = new YAHOO.widget.DataTable("tmptAtctTable",tmptActvColumnDefs, temptActvDataSource,{MSG_EMPTY:"<s:text name='milestoneTemplate.activity.initial.table.message'/>"});
	temptActvDataTable.subscribe("cellClickEvent", temptActvDataTable.onEventShowCellEditor); 
	temptActvDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'TemptActvDelete') { 			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('Stage Order No'),""+(i+1));
				}
				calculateTotalPercentage();
		}        
	});
	
	return {
	    oDS: temptActvDataSource,
	    oDT: temptActvDataTable
	};        

}

function validateNumberInTableCell(elem){
		elem.value=trim(elem.value);
		dom.get('error'+elem.id).style.display='none';
		if(isNaN(elem.value) || getNumber(elem.value)<=0 ||trim(elem.value)==""){
			dom.get('error'+elem.id).style.display='';
			isValidPercentage=false;
		}else{
			isValidPercentage=true;
		}
}

function validateDescription(elem){
		dom.get('error'+elem.id).style.display='none';
		if(trim(elem.value)==""){
			dom.get('error'+elem.id).style.display='';
			isValidPercentage=false;
		}else{
			isValidPercentage=true;
		}
}

function validatePercentage(){

	if(temptActvDataTable.getRecordSet().getLength()==0){
		isValidPercentage=true;
	}

	if(isValidPercentage){
		temptActvDataTable.addRow({SlNo:temptActvDataTable.getRecordSet().getLength()+1});
	}else{
		alert("Please enter Valid Template Activity Details.");
		return false;
	}
}

function calculateTotalPercentage(){
	records=temptActvDataTable.getRecordSet();
    total=0;
    for(i=0;i<records.getLength();i++){
    	if(!isNaN(parseFloat(dom.get('percentage'+temptActvDataTable.getRecord(i).getId()).value))){
          	total=total+parseFloat(dom.get('percentage'+temptActvDataTable.getRecord(i).getId()).value);
     	}
    }
	dom.get('totalValue').innerHTML=total;
}

</script>		
      
       
		<table id="temptActvTable" width="100%" border="0" cellspacing="0" cellpadding="0">              	
              	<tr>
                	<td colspan="9" class="headingwk" style="border-right-width: 0px" align="left">
                		<div class="arrowiconwk"><image src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                		<div class="headplacer" >Template Activity Details</div>
                	</td>
                	<td  align="right" class="headingwk" style="border-left-width: 0px">
                	<s:if test="%{mode=='view' || model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED'}">
                	</s:if>
                	<s:else>
                		<a id="temptActvRow" href="#" onclick="validatePercentage();return false;"><img height="16" border="0" width="16" alt="Add Template Activity" src="${pageContext.request.contextPath}/image/add.png" /></a>
                	</s:else>
                	</td>
              	</tr>
              	<tr>
                	<td>
                		<div class="yui-skin-sam">
                	    	<div id="tmptAtctTable" align="center"></div>  
                		</div>                  	
                    </td>
                 </tr>
              	<tr>
                	<td>
						<div id="totalPercentage" align="right">
							<table>
								<tr>
									<td width="30%" class="whiteboxwk"><s:text name="milestone.template.activity.label.total.percentage" />:</td>
                					<td width="30%" class="whitebox2wk"><div id="totalValue"></div>
								</tr>
							</table>
						</div>
                    </td>
                </tr>
             </table>
              	
	<script>
		makeTmptActvDataTable();
		<s:iterator  value="stages" status="row_status">
            temptActvDataTable.addRow({
                                    stageOrderNo:'<s:property value="stageOrderNo"/>',
                                    description:'<s:property value="description" escape="false"/>',
                                    percentage:'<s:property value="percentage"/>',
                                    Delete:'X'});
        var record = temptActvDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = temptActvDataTable.getColumn('description');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="description" escape="false"/>';
	
	 var column = temptActvDataTable.getColumn('stageOrderNo');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="stageOrderNo" />';
        
 	var column = temptActvDataTable.getColumn('percentage');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage" />';
        </s:iterator>
        calculateTotalPercentage();
	</script> 
	
 