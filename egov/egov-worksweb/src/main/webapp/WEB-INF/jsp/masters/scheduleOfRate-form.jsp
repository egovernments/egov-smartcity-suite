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
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
ul {
list-style-type: none;
}
</style>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>
function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL="/resources/erp2/images/cancel.png";
	    markup='<img height="16" border="0" width="16" alt="Delete" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function validateSORFormAndSubmit() {
    clearMessage('sor_error')
	links=document.scheduleOfRate.getElementsByTagName("span");
	errors=false;
	for(i=0; i<links.length; i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none') {
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("sor_error").style.display = '';
    	document.getElementById("sor_error").innerHTML='<s:text name="sor.validate_x.message" />';
    	return false;
    } else {
		var lenFrm = document.scheduleOfRate.elements.length;
		for(i=0; i<lenFrm; i++) {
			document.scheduleOfRate.elements[i].readonly=false;	
			document.scheduleOfRate.elements[i].disabled=false;	
		}   	
   	}
    doLoadingMask();
}

function enableLastEndDate(){
	var persistedRatesCnt = '<s:property value="%{editableRateList.size()}"/>';
	var i;
	var records= scheduleOfRateDataTable.getRecordSet();

	hideColumn('deleteRate');
	disablePrevRates();
	for(i=0;i<records.getLength();i++){
		if(i <= (persistedRatesCnt-1)) {
			// dont do anything
		} else {
			//enable all fields of new rows
			dom.get("rate"+records.getRecord(i).getId()).readonly=false;
			dom.get("rate"+records.getRecord(i).getId()).disabled=false;
			dom.get("startDate"+records.getRecord(i).getId()).readonly=false;
			dom.get("startDate"+records.getRecord(i).getId()).disabled=false;
			dom.get("endDate"+records.getRecord(i).getId()).readonly=false;
			dom.get("endDate"+records.getRecord(i).getId()).disabled=false;
		}
	}
	dom.get("endDate"+records.getRecord(persistedRatesCnt-1).getId()).readonly=false;
	dom.get("endDate"+records.getRecord(persistedRatesCnt-1).getId()).disabled=false;	
}

function hideColumn(colKey) {
	scheduleOfRateDataTable.hideColumn(colKey);
}

function disableEnablePrevRateDetails(records,j) {
	var endDate;
	if(dom.get("endDate"+records.getRecord(j).getId()).value!=null) {
		endDate = dom.get("endDate"+records.getRecord(j).getId()).value;
	}
	dom.get("rate"+records.getRecord(j).getId()).readonly=false;
	dom.get("rate"+records.getRecord(j).getId()).disabled=false;
	dom.get("startDate"+records.getRecord(j).getId()).readonly=false;
	dom.get("startDate"+records.getRecord(j).getId()).disabled=false;

	<jsp:useBean id="today" class="java.util.Date" />
	<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${today}"/>

	var currDate = '${currDate}';
	if(dom.get("endDate"+records.getRecord(j).getId()).value=="" || (compareDate(endDate,currDate) != 1)){
		dom.get("endDate"+records.getRecord(j).getId()).readonly=false;
		dom.get("endDate"+records.getRecord(j).getId()).disabled=false;
	}
	else{
		dom.get("endDate"+records.getRecord(j).getId()).readonly=true;
		dom.get("endDate"+records.getRecord(j).getId()).disabled=true;
	}
}

function disablePreviousRatesOnLoad() {
	<s:if test="%{id!=null && mode=='edit'}">
	hideColumn('deleteRate');
		disablePrevRates();
	</s:if>
} 

function disablePrevRates(){
	var records= scheduleOfRateDataTable.getRecordSet();
	var j;
	if(records.getLength()>1) {
		for(j=0;j<records.getLength();j++) {
			disableEnablePrevRateDetails(records,j);
		}
	} else {
		j=records.getLength()-1;
		disableEnablePrevRateDetails(records,j);
	}
}

function validateLineBreaks() {
	var codeName = dom.get('code').value;
	codeName = codeName.replace(/([\n]|'|<br \>)/g,'');
	dom.get("code").value = codeName;
}

function createHiddenFormatter(el, oRecord, oColumn, oData) {
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    	var value = (YAHOO.lang.isValue(oData))?oData:"";
    	var id=oColumn.getKey()+oRecord.getId();
    	var fieldName = "actionRates[" + oRecord.getCount() + "]." + oColumn.getKey();
    	var fieldValue=value;
    	markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    	el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10); 
 
function createTextBoxFormatter(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var fieldName = "actionRates[" + oRecord.getCount() + "]." +  oColumn.getKey();   
	   var id = oColumn.getKey()+oRecord.getId();
	   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'" 
	   + " onblur='validateNumberInTableCell(scheduleOfRateDataTable,this,\"" + oRecord.getId()+ "\");'/>"
	   + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	   el.innerHTML = markup; 
	}
	return textboxFormatter;	
}

var rateTextboxFormatter = createTextBoxFormatter(11,10);
var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "actionRates[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
	var markup= "<input type='text' id='"+id+"' class='selectmultilinewk datepicker' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

var scheduleOfRateDataTable;
var makeScheduleOfRateDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var scheduleOfRateColumnDefs = [ 
		{key:"id", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
		{key:"rate", label:'<span class="mandatory"></span>Rate', formatter:rateTextboxFormatter, sortable:false, resizeable:false, width:180},		
		{key:"startDate", label:'<span class="mandatory"></span>Start Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
		{key:"endDate",label:'End Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
		{key:'deleteRate',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	
	var scheduleOfRateDataSource = new YAHOO.util.DataSource(); 
	scheduleOfRateDataTable = new YAHOO.widget.DataTable("scheduleOfRateTable",scheduleOfRateColumnDefs, scheduleOfRateDataSource, {MSG_EMPTY:"<s:text name='master.sor.initial.table.message'/>"});
	scheduleOfRateDataTable.subscribe("cellClickEvent", scheduleOfRateDataTable.onEventShowCellEditor); 
	scheduleOfRateDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'deleteRate') { 	
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	return {
	    oDS: scheduleOfRateDataSource,
	    oDT: scheduleOfRateDataTable
	};  
}

</script>
<div class="errorstyle" id="sor_error" style="display: none;"></div>

<span align="center" style="display:none" id="selectcategory">
 	<div class="errorstyle" >
         <s:text name="sor.code.categoryType.null"/>
	</div>
</span>

<div class="new-page-header">
	<s:text name="sor.header" />
</div>

<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		</div>
	</div>

	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.category" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategory" id="scheduleCategory" cssClass="form-control" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code" value="%{scheduleCategory.id}" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.code" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="code" cssClass="form-control" id="code" value = "%{code}" maxlength = "50" autocomplete="off" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.description" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="description" cols="45"  rows="3" cssClass="form-control" id="description" value = "%{description}"  maxlength = "4000"  /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.uom" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="uom" id="uom" cssClass="form-control" list="dropdownData.uomlist" listKey="id" listValue="uom" value="%{uom.id}" />
			</div>
		</div>
     </div>
</div>

<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   <s:text name="master.sor.rateDetails" />
		</div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<div class="text-right add-margin">
	   	       <button class="btn btn-primary" onclick="scheduleOfRateDataTable.addRow({SlNo:scheduleOfRateDataTable.getRecordSet().getLength()+1}); initializeDatePicker(); return false;"><s:text name="sor.addsor.rate" /></button>
	   	   </div>
			<div class="yui-skin-sam">
			<div id="scheduleOfRateTable"></div>

<script>
var imgURL="/resources/erp2/images/cancel.png";	
function validateInput(){ 
		var elems = document.getElementsByTagName("input");
		for (var i=0; i<elems.length; i++) {
		if(elems[i].id != '') {
			var val = document.getElementById(elems[i].id).value;
			  if ((elems[i].id.indexOf("rateyui") == 0 || elems[i].id.indexOf("marketRateyui") == 0) && val == '0.0')
				  document.getElementById(elems[i].id).value = '';
		}
	}
}
function initializeDatePicker()
{
	jQuery(".datepicker").datepicker({
		format : "dd/mm/yyyy",
		autoclose:true
	});
}
            makeScheduleOfRateDataTable();
            <s:iterator id="rateIterator" value="model.sorRates" status="rate_row_status">
            <s:if test="#rate_row_status == 1">
	       ScheduleOfRateDataTable.updateRow(0,
	    		   {id:'<s:property value="id"/>',											
               SlNo:'<s:property value="#rate_row_status.count"/>',
               rate:'<s:property value="rate"/>',
               startDate:'<s:property value="validity.startDate"/>',
               endDate:'<s:property value="validity.endDate"/>'
				}
				);
	          </s:if>
	          <s:else>
	          scheduleOfRateDataTable.addRow(
	             	{id:'<s:property value="id"/>',											
                    SlNo:'<s:property value="#rate_row_status.count"/>',
                    rate:'<s:property value="rate"/>',
                    startDate:'<s:property value="validity.startDate"/>',
                    endDate:'<s:property value="validity.endDate"/>'
						}
						);
			</s:else>
				var record = scheduleOfRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));			  									
				var rateidValue='<s:property value="id"/>';		
			<s:if test="%{estimateDtFlag=='yes'}">
				<s:iterator status="stat" value="deletFlagMap" >				
					var key='<s:property value="key"/>';
					var value='<s:property value="value"/>';	
				if(key==rateidValue && value=='yes'){
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';					
					dom.get(column.getKey()+record.getId()).readonly='true';
			        dom.get(column.getKey()+record.getId()).disabled='true';
					
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/> 
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
					dom.get(column.getKey()+record.getId()).readonly='true';
					dom.get(column.getKey()+record.getId()).disabled='true';
					<s:if test="%{validity.endDate!=null}">
				        var column = scheduleOfRateDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/> 
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
						dom.get(column.getKey()+record.getId()).readonly='true';
						dom.get(column.getKey()+record.getId()).disabled='true';
					</s:if>
				}else{	
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
			        
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/> 
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
			        <s:if test="%{validity.endDate!=null}">			        
				        var column = scheduleOfRateDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/> 
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';	
				    </s:if>  				
				}
				</s:iterator>		
			</s:if>
			<s:if test="%{woDateFlag=='yes'}">
				<s:iterator status="stat" value="deleteFlagMap2" >				
					var key='<s:property value="key"/>';
					var value='<s:property value="value"/>';	
				if(key==rateidValue && value=='yes'){
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';					
					dom.get(column.getKey()+record.getId()).readonly='true';
			        dom.get(column.getKey()+record.getId()).disabled='true';
					
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/> 
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
					dom.get(column.getKey()+record.getId()).readonly='true';
					dom.get(column.getKey()+record.getId()).disabled='true';
					<s:if test="%{validity.endDate!=null}">
				        var column = scheduleOfRateDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/> 
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
						dom.get(column.getKey()+record.getId()).readonly='true';
						dom.get(column.getKey()+record.getId()).disabled='true';
					</s:if>
				}else{	
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
			        
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/> 
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
			        <s:if test="%{validity.endDate!=null}">		        
				        var column = scheduleOfRateDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/> 
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';	
				    </s:if>  				
				}
				</s:iterator>		
			</s:if>			
			<s:else>		
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
			        
			        var column = scheduleOfRateDataTable.getColumn('startDate');
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/>   
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
			        <s:if test="%{validity.endDate!=null}">			        
			        	var column = scheduleOfRateDataTable.getColumn('endDate');  
			        	<s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/> 
			        	dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
			        </s:if> 	  				

			</s:else>

		</s:iterator>
</script>
  		</div>
		</div>
	</div>
</div>

<%@ include file='scheduleOfRate-marketRate.jsp'%>

<script>

<s:if test="%{mode=='view'}">
	for(i=0;i<document.scheduleOfRate.elements.length;i++){
		document.scheduleOfRate.elements[i].disabled=true;
		document.scheduleOfRate.elements[i].readonly=true;
	} 
	scheduleOfRateDataTable.removeListener('cellClickEvent');		       
	links=document.scheduleOfRate.getElementsByTagName("a");
	for(i=0;i<links.length;i++){    
	links[i].onclick=function(){return false;};
	}
</s:if>
<s:if test="%{mode=='edit'}">
scheduleOfRateDataTable.removeListener('cellClickEvent');
hideColumn('deleteRate');
</s:if>
<s:if test="%{estimateDtFlag=='yes' || woDateFlag=='yes' || hasErrors()}">
scheduleOfRateDataTable.removeListener('cellClickEvent');	
var len=document.scheduleOfRate.elements.length;		
for(i=0;i<5;i++){		
if(i==3){
}
else
	{			
		document.scheduleOfRate.elements[i].readonly=false;	
		document.scheduleOfRate.elements[i].disabled=false;	
	}
}
enableLastEndDate();
</s:if>	
	
	function handleSuccess(transport) {
		alert('status :'+transport.status+' ,Result: '+transport.responseText);
	}
</script>
