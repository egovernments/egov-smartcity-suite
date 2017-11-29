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
</style>

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function validateOverheadFormAndSubmit(){
    clearMessage('overheads_error')
	links=document.overhead.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++)
    {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors){
        dom.get("overheads_error").style.display='';
    	document.getElementById("overheads_error").innerHTML='<s:text name="overhead.validate_x.message" />';
    	return;
    }
    else{
    document.overhead.action='${pageContext.request.contextPath}/masters/overhead!create.action';
    	document.overhead.submit();
    }
}

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var fieldName = "overheadRates[" + oRecord.getCount() + "]." +  oColumn.getKey();
   var id = oColumn.getKey()+oRecord.getId();
   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'" 
   + " onblur='validateNumberInTableCell(overheadRateDataTable,this,\"" + oRecord.getId()+ "\");'/>"
   + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
return textboxFormatter;
}
var percentageTextboxFormatter = createTextBoxFormatter(11,5);
var lumpsumAmountTextboxFormatter = createTextBoxFormatter(11,7);

var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "overheadRates[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
	var markup= "<input type='text' id='"+id+"' class='datepicker selectmultilinewk' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "' onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

var overheadRateDataTable;
var makeOverheadRateDataTable = function() 
{
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var overheadRateColumnDefs = [ 
		{key:"overheadId", hidden:true,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
		{key:"lumpsumAmount", label:'Lump sum Amount', formatter:lumpsumAmountTextboxFormatter, sortable:false, resizeable:false, width:180},		
		{key:"percentage", label:'Percentage', formatter:percentageTextboxFormatter, sortable:false, width:180},
		{key:"startDate", label:'Start Date<span class="mandatory"></span>', formatter:dateFormatter,sortable:false, resizeable:false, width:160},
		{key:"endDate",label:'End Date', formatter:dateFormatter,sortable:false, resizeable:false, width:160}		  
	];
	
	var overheadRateDataSource = new YAHOO.util.DataSource(); 
	overheadRateDataTable = new YAHOO.widget.DataTable("overheadRateTable",overheadRateColumnDefs, overheadRateDataSource, {MSG_EMPTY:"<s:text name='master.overhead.initial.table.message'/>"});
	overheadRateDataTable.subscribe("cellClickEvent", overheadRateDataTable.onEventShowCellEditor); 
}
	
</script>
<div class="alert alert-danger" id="overhead_error" style="display: none;"></div>

<div id="overheadTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
				<div class="panel-heading">
					<div class="panel-title">
					    <s:text name='page.title.overheads' />
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">
						    <s:text name="master.overhead.name" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:textfield label="name" name="name" value="%{name}" id="name" cssClass="form-control" maxlength="255"/>
						</div>
						<label class="col-sm-2 control-label text-right">
						    <s:text name="master.overhead.description" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:textfield label="Description" name="description" value="%{description}" id="description" cssClass="form-control" maxlength="255"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">
						    <s:text name="master.overhead.account" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="account" id="account" cssClass="form-control" list="dropdownData.accountList" listKey="id" listValue='glcode  + " : " + name' value="" />
						</div>
						<label class="col-sm-2 control-label text-right">
						    <s:text name="master.overhead.expenditure" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:select headerKey="-1"  headerValue="%{getText('estimate.default.select')}" name="expenditure" id="expenditure" cssClass="form-control" list="expenditureTypeList" listKey="value" listValue="value" value="" />
						</div>
					</div>
				</div>
</div>

<div id="overheadRatesTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
				<div class="panel-heading">
					<div class="panel-title">
					    <s:text name='page.title.overheadrate' />
					    <div class="pull-right">
		   
					      <a href="javascript:void(0);" class="btn btn-primary" 
				   	       onclick="overheadRateDataTable.addRow({SlNo:overheadRateDataTable.getRecordSet().getLength()+1});reinitializeDatepicker();return false;">
				   	       <i class="fa fa-plus"></i> Add Overhead Rate
				   	      </a>
					   
					   </div>
					</div>
				</div>
				<div  class="panel-body">
					 <div class="form-group">
					    <div class="yui-skin-sam"><div id="overheadRateTable"></div></div>
			   		    <script>
								makeOverheadRateDataTable();		    	
								<s:iterator id="overheadRateIterator" value="model.overheadRates" status="rate_row_status">
							        overheadRateDataTable.addRow(
						        						{overheadId:'<s:property value="id"/>',
						                                SlNo:'<s:property value="#rate_row_status.count"/>',
						                                lumpsumAmount:'<s:property value="lumpsumAmount"/>',
						                                percentage:'<s:property value="percentage"/>',
						                                startDate:'<s:property value="validity.startDate"/>',
						                                endDate:'<s:property value="validity.endDate"/>'});
						                                    
						        var record = overheadRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));
						    
						        var column = overheadRateDataTable.getColumn('lumpsumAmount');  
						        dom.get(column.getKey()+record.getId()).value = '<s:property value="lumpsumAmount"/>';
						        
						        var column = overheadRateDataTable.getColumn('percentage');  
						        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
						        
						        var column = overheadRateDataTable.getColumn('startDate');  
						        <s:date name='validity.startDate' var="startDateFormat" format="dd/MM/yyyy"/> 
						        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
						        
						        <s:if test="%{id!=null}">
						        	dom.get(column.getKey()+record.getId()).disabled = true;
						        </s:if>
						        var imgId1 = "img" + column.getKey()+record.getId();
						        
						        var column = overheadRateDataTable.getColumn('endDate'); 
						        <s:date name='validity.endDate' var="endDateFormat" format="dd/MM/yyyy"/>
						        <s:if test="%{validity.endDate!=null}"> 
						        	dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
						        </s:if>
						        <jsp:useBean id="today" class="java.util.Date" /> 
						        <fmt:formatDate var = "currDate" pattern="yyyy/MM/dd" value="${today}"/>
								var currDate = '${currDate}';
								var endDate = '<s:date name="validity.endDate" format="yyyy/MM/dd" var="tDate"/><s:property value="tDate"/>';
								<s:if test="%{validity.endDate!=null}">
									if (currDate >= endDate) {
										dom.get(column.getKey()+record.getId()).disabled = true;
									}
						        </s:if>
						                
						        var imgId2 = "";
						        
						        links=document.overhead.getElementsByTagName("img");
						        
								for(i=0;i<links.length;i++)
						        {
						            <s:if test="%{id!=null}">
						        		if(links[i].id.indexOf(imgId1)==0)
						        		{
						        			links[i].onclick=function(){return false;};
						        		}
						        		if(links[i].id.indexOf(imgId2)==0)
						        		{
						        			links[i].onclick=function(){return false;};
						        		}
						            </s:if>
						        }
						       </s:iterator>
							   
			       
			        </script>
				   </div>
				</div>
</div>