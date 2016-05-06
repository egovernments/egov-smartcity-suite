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

<script>

var isValidPercentage=true;
function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();
    if(oColumn.getKey()=='complPercentage'){
    	el.innerHTML="<center><input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectamountwk' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' onblur='validateNumberInTableCell(this);calculateActualPerc(this)' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span><span id='errormsg"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;<br/>values between 0 and 100</span></center>";
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
   	var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + "Description";
	markup="<center><textarea id='"+oColumn.getKey()+oRecord.getId()+"' class='selecwk' cols='25' value='"+value+"' maxlength='1024' readonly='true'/></center>"
	el.innerHTML = markup;	 	
}

var textboxRemarksFormatter = function(el, oRecord, oColumn, oData) {
	var value = (YAHOO.lang.isValue(oData))?oData:"";
   	var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + "remarks";
	markup="<center><textarea id='"+oColumn.getKey()+oRecord.getId()+"' class='selecwk' cols='25' value='"+value+"' maxlength='1024' name='"+fieldName+"'/></center>"
	el.innerHTML = markup;	 	
}

var textboxReadonlyFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();  
      el.innerHTML="<center><input type='text' id='"+id+"' class='selectamountwk' size='6' value='"+value+"' maxlength='5' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
}

var textboxStageReadonlyFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    var id=oColumn.getKey()+oRecord.getId();  
      el.innerHTML="<center><input type='text' id='"+id+"' class='slnowk' size='6' value='"+value+"' maxlength='5' readonly='true' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></center>";
}


function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." + oColumn.getKey()+".id";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10);

var statusDropdownOptions=[{label:"NOT YET STARTED", value:"NOT YET STARTED"},
    					   {label:"IN PROGRESS", value:"IN PROGRESS"}, 
    					   {label:"COMPLETED", value:"COMPLETED"},
    					   {label:"INVALID", value:"INVALID"}
    					   ] 

var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var value = (YAHOO.lang.isValue(oData))?oData:"";
	var fieldName = "trackMilestoneActivities[" + oRecord.getCount() + "]." +  oColumn.getKey();
	var idt=oColumn.getKey()+oRecord.getId();
	var id=idt.replace("-","");
	var CALENDERURL="/resources/erp2/images/calendar.png";
	var HREF='javascript:show_calendar("forms[0].'+id+'")';
	markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' class='selectmultilinewk' size='15'  maxlength='10' style=\"width:75px\" onkeyup='DateFormat(this,this.value,event,false,3)' onblur='validateDateFormat(this);'/><a href='#' style='text-decoration:none' onclick='"+HREF+"'><img src='"+CALENDERURL+"' border='0' align='absmiddle' /></a>"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

var temptActvDataTable;
var makeTmptActvDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var tmptActvColumnDefs = [
		{key:"milestoneActivity",hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false},
		{key:"StageOrderNo", label:'<center>Stage Order<br/> No<center/>', width:55,formatter:textboxStageReadonlyFormatter,resizeable:false},
		{key:"Description", width:200,label:'<center>Stage Description<center/>', formatter:textboxDescFormatter, resizeable:false},		
		{key:"Percentage",label:'<center>Percentage <br/>of Stage(A)<center/>',width:70,formatter:textboxReadonlyFormatter,className:'right',resizeable:false},
		{key:"status",label:'Current status<br/> of Stage',width:125, formatter:createDropdownFormatter('trackMilestoneActivities',''), dropdownOptions:statusDropdownOptions,resizeable:false},
		{key:"complPercentage",label:'<center>Completed <br/>Percentage of Stage(B)<center/>',width:120, formatter:pTextBoxFormatter,resizeable:false},
		{key:"ActualPercentage",label:'<center>Actual Percentage<br/>of Stage(C)=B/100*A<center/>',width:100,formatter:textboxReadonlyFormatter,resizeable:false},
		{key:"remarks", width:200,label:'Remarks',formatter:textboxRemarksFormatter,resizeable:false},	
		{key:"completionDate",label:'Completion Date',width:120, formatter:dateFormatter,resizeable:false}
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
		}        
	});

            var tfoot = temptActvDataTable.getTbodyEl().parentNode.createTFoot();
			var tr = tfoot.insertRow(-1);
			var th = tr.appendChild(document.createElement('td'));
			th.colSpan = 2;
			th.className= 'whitebox4wk';
			th.innerHTML = '&nbsp;';

			var td = tr.insertCell(1);
			td.className= 'whitebox4wk';
			td.id = 'totalPerc';
			td.innerHTML = '<span class="bold" >Total:</span>';

			addCell(tr,2,'totalPercentage','100');
			addCell(tr,3,'filler1','');
			addCell(tr,4,'totalCompletionPerclabel','<span class="bold" >Work Completion<br/>Percentage:</span>');
			addCell(tr,5,'totalCompletionPerc',"0.00");
			addCell(tr,6,'filler2','');
			addCell(tr,7,'filler','');

	
	return {
	    oDS: temptActvDataSource,
	    oDT: temptActvDataTable
	};        

}


function validateNumberInTableCell(elem){
		elem.value=trim(elem.value);
		dom.get('error'+elem.id).style.display='none';
		dom.get('errormsg'+elem.id).style.display='none';
		if(trim(elem.value)==""){
			elem.value=roundTo(0.00);
		}		
		if(isNaN(elem.value) || getNumber(elem.value)<0 ||getNumber(elem.value)>100){
			dom.get('error'+elem.id).style.display='';
			dom.get('errormsg'+elem.id).style.display='';
		}
}

function validateDescription(elem){
		dom.get('error'+elem.id).style.display='none';
		if(trim(elem.value)==""){
			dom.get('error'+elem.id).style.display='';
		}
}

function calculateActualPerc(){
	var records=temptActvDataTable.getRecordSet();
    var totalPerc=0;
    var enteredPerc=0;
    var actualPerc=0;
    for(var i=0;i<records.getLength();i++){
    	dom.get('ActualPercentage'+temptActvDataTable.getRecord(i).getId()).value=roundTo(0.00);
    	if(trim(dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value)==""){
    		dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value=roundTo(0.00);
    	}
    	if(!isNaN(parseFloat(dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value))){
          	enteredPerc=parseFloat(dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value);
          	actualPerc=(enteredPerc/100)*(parseFloat(dom.get('Percentage'+temptActvDataTable.getRecord(i).getId()).value));
          	dom.get('ActualPercentage'+temptActvDataTable.getRecord(i).getId()).value=roundTo(actualPerc);
          	totalPerc=getNumber(roundTo(totalPerc))+getNumber(roundTo(actualPerc));
     	}
    }
	dom.get("totalCompletionPerc").innerHTML=roundTo(totalPerc);
}

function validateCompletionDate(){
	var records=temptActvDataTable.getRecordSet();
    for(var i=0;i<records.getLength();i++){
    	var actualId='completionDate'+temptActvDataTable.getRecord(i).getId();
    	var dateId=actualId.replace("-","");
    	if(trim(dom.get(dateId).value)=="" && dom.get('status'+temptActvDataTable.getRecord(i).getId()).value=='COMPLETED'){
 			return true;
     	}
 }
}

function validateProjectCompletionFlag(){
	var records=temptActvDataTable.getRecordSet();
    for(var i=0;i<records.getLength();i++){
    	if(!((getNumber(dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value)==100.00 && dom.get('status'+temptActvDataTable.getRecord(i).getId()).value=='COMPLETED') || (dom.get('status'+temptActvDataTable.getRecord(i).getId()).value=='INVALID' && getNumber(dom.get('complPercentage'+temptActvDataTable.getRecord(i).getId()).value)==0))){
 			return false;
     	}
 }
return true;
}


</script>		
      <div>
       
		<table id="temptActvTable" width="100%" border="0" cellspacing="0" cellpadding="0">              	
              	<tr>
                	<td colspan="8" class="headingwk" style="border-right-width: 0px" align="left">
                		<div class="arrowiconwk"><image src="/egworks/resources/erp2/images/arrow.gif" /></div>
                		<div class="headplacer" >Milestone Details</div>
                	</td>
              	</tr>
              	<tr>
                	<td colspan="8">
                		<div class="yui-skin-sam">
                	    	<div id="tmptAtctTable" align="center"></div>  
                		</div>                  	
                    </td>
                 </tr>
             </table>
       </div>       	
	<script>
		makeTmptActvDataTable();
		<s:iterator  value="stages" status="row_status">
             temptActvDataTable.addRow({
            						milestoneActivity:'<s:property value="milestoneActivity.id"/>',
                                    StageOrderNo:'<s:property value="milestoneActivity.stageOrderNo"/>',
                                    Description:'<s:property value="milestoneActivity.descriptionJS" escape="false"/>',
                                    Percentage:'<s:property value="milestoneActivity.percentage"/>',
                                    status:'<s:property value="status"/>',
                                    complPercentage:'<s:property value="complPercentage"/>',
                                    ActualPercentage:'<s:property value="%{(complPercentage/100)*percentage}"/>',
                                    remarks:'<s:property value="remarksJS" escape="false"/>',
                                    completionDate:'<s:date name="completionDate"  format="dd/MM/yyyy" />'
                                   });
        var record = temptActvDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = temptActvDataTable.getColumn('Description');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="milestoneActivity.descriptionJS" escape="false"/>';

        var column = temptActvDataTable.getColumn('remarks');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="remarksJS" escape="false"/>';

		calculateActualPerc();
        </s:iterator>
 	</script> 
	
 
