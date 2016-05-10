<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
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
    var fieldName = "milestoneActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();
    if(oColumn.getKey()=='percentage'){
    	el.innerHTML="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectamountwk' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' onblur='validateNumberInTableCell(this);calculateTotalPercentage();' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
    }else{
   		el.innerHTML="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='slnowk' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' onblur='validateNumberInTableCell(this);' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
    }
}
return textboxFormatter;
}
var sTextboxFormatter = createTextBoxFormatter(6,5);
var pTextBoxFormatter = createTextBoxFormatter(6,5);
var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
	var value = (YAHOO.lang.isValue(oData))?oData:"";
   	var fieldName = "milestoneActivities[" + oRecord.getCount() + "]." + "description";
   	var id=oColumn.getKey()+oRecord.getId();
	markup="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='100' value='"+value+"' maxlength='1024' onblur='validateDescription(this);' name='"+fieldName+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>"
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
	temptActvDataTable = new YAHOO.widget.DataTable("tmptAtctTable",tmptActvColumnDefs, temptActvDataSource,{MSG_EMPTY:"<s:text name='milestone.activity.initial.table.message'/>"});
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
		alert("Please enter Valid Milestone Details.");
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
                		<div class="arrowiconwk"><image src="/egworks/resources/erp2/images/arrow.gif" /></div>
                		<div class="headplacer" >Milestone Details</div>
                	</td>
                	<td  align="right" class="headingwk" style="border-left-width: 0px">
                	<s:if test="%{mode=='view' || model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED'}">
                	</s:if>
                	<s:else>
                		<a id="temptActvRow" href="#" onclick="validatePercentage();return false;"><img height="16" border="0" width="16" alt="Add Template Activity" src="/egworks/resources/erp2/images/add.png" /></a>
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
                                    description:'<s:property value="descriptionJS" escape="false"/>',
                                    percentage:'<s:property value="percentage"/>',
                                    Delete:'X'});
        var record = temptActvDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = temptActvDataTable.getColumn('description');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="descriptionJS" escape="false"/>';
	
	 var column = temptActvDataTable.getColumn('stageOrderNo');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="stageOrderNo" />';
        
 	var column = temptActvDataTable.getColumn('percentage');
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage" />';
        </s:iterator>
        calculateTotalPercentage();
	</script> 
	
 
