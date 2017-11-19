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
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script type="text/javascript">
var departmentDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.departmentList" status="status">  
    {"label":"<s:property value="%{name}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
var gradeDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.gradeList" status="status">   
    {"label":"<s:property value="%{grade}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
var statusDropdownOptions=[
    <s:iterator var="s" value="dropdownData.statusList" status="status">   
    {"label":"<s:property value="%{description}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]

var categoryDropdownOptions=[{label:"--- Select ---", value:"0"},
	<s:iterator var="s" value="dropdownData.contractorDetailsCategoryValues" status="status">   
	{"label":"<s:text name="%{s}"/>" ,
		"value":"<s:text name="%{s}" />"
	}<s:if test="!#status.last">,</s:if>
	</s:iterator>       
	]
     
function createTextBoxFormatter(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var fieldName = "actionContractorDetails[" + oRecord.getCount() + "]." +  oColumn.getKey();   
	   var id = oColumn.getKey()+oRecord.getId();
	
       markup="<input type='text' id='"+id+"'  class='selectmultilinewk' size='"+size+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
	   el.innerHTML = markup; 
	}
	return textboxFormatter;	
}

function categorySelectDropDownFormatter(){
	var selectFormatter = function(el,oRecord,oColumn,oDate){
		var fieldName = "contractorCategorydetails[" + oRecord.getCount() + "]." +  oColumn.getKey();
		var id = oColumn.getKey()+oRecord.getId();
		
		markup = "<select name = '"+fieldName+"' id = '"+id+"'>\
					<option value = '0'>---Select---</option>\
					</select>";
		el.innerHTML = markup;
	}
	return selectFormatter;
}

var regNoTextboxFormatter= createTextBoxFormatter(11,50);
var categorySelectFormatter = categorySelectDropDownFormatter();

var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "actionContractorDetails[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
    var markup= "<input type='text' id='"+id+"'   class='selectmultilinewk' size='15' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
  
	
}

function createHiddenFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionContractorDetails[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

function createCategoryHiddenFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionContractorDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

var departmentIdHiddenFormatter= createHiddenFormatter(5,5);
var gradeIdHiddenFormatter= createHiddenFormatter(5,5);
var statusIdHiddenFormatter= createHiddenFormatter(5,5);
var categoryHiddenFormatter = createCategoryHiddenFormatter(5,5);

var contractorDataTable;
var makeContractorDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var contractorColumnDefs = [ 
		{key:"department", hidden:true, formatter:departmentIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"grade", hidden:true, formatter:gradeIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"status", hidden:true, formatter:statusIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"category", hidden:true, formatter:categoryHiddenFormatter, sortable:false, resizeable:false},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"departmentName", label:'<label for="department"><s:text name="contractor.department"/><span class="mandatory1">*</span>', formatter:"dropdown",dropdownOptions:departmentDropdownOptions, sortable:false, resizeable:false},	
		{key:"registrationNumber", label:'<label for="registrationNumber"><s:text name="contractor.registrationNo"/></label>', formatter:regNoTextboxFormatter, sortable:false, resizeable:false},		
		{key:"categoryName", label:'<label for="category"><s:text name="contractor.category" /></label>',formatter:"dropdown",dropdownOptions:categoryDropdownOptions,sortable:false, resizeable:false},
		{key:"gradeName", label:'<label for="grade"><s:text name="contractor.grade" /></label>',formatter:createDropdownFormatter('contractorGradedetails','id'), dropdownOptions:gradeDropdownOptions, sortable:false, resizeable:false},			
		{key:"statusDesc", label:'<label for="status"><s:text name="contractor.status" /><span class="mandatory1">*</span>',formatter:createDropdownFormatter('contractorStatusdetails','id'),dropdownOptions:statusDropdownOptions, sortable:false, resizeable:false},		
		{key:"startDate", label:'<label for="fromDate"><s:text name="contractor.fromDate" /><span class="mandatory1">*</span>', formatter:dateFormatter,sortable:false, resizeable:false},
		{key:"endDate",label:'<label for="toDate"><s:text name="contractor.toDate"/>', formatter:dateFormatter,sortable:false, resizeable:false},
		{key:'deleteRow',label:'<s:text name="contractorDetails.row.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	
	var contractorDataSource = new YAHOO.util.DataSource(); 
	contractorDataTable = new YAHOO.widget.DataTable("contractorTable",contractorColumnDefs, contractorDataSource, {MSG_EMPTY:"<s:text name='contractor.initial.table.message'/>"});
	contractorDataTable.subscribe("cellClickEvent", contractorDataTable.onEventShowCellEditor); 
	contractorDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'deleteRow') { 	
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	contractorDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='departmentName'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('department'),departmentDropdownOptions[selectedIndex].value);
        }
        if(column.key=='gradeName'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('grade'),gradeDropdownOptions[selectedIndex].value);
        }
        if(column.key=='statusDesc'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('status'),statusDropdownOptions[selectedIndex].value);
        }
        if(column.key=='categoryName'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('category'),categoryDropdownOptions[selectedIndex].value);
        }
	});
	
	contractorDataTable.addRow({SlNo:contractorDataTable.getRecordSet().getLength()+1,status:statusDropdownOptions[0].value,department:"${defaultDepartmentId}"});	
	dom.get("status"+contractorDataTable.getRecordSet().getRecord(0).getId()).value=statusDropdownOptions[0].value;

	return {
	    oDS: contractorDataSource,
	    oDT: contractorDataTable
	};  
}
</script>

<div class="errorstyle" id="contractor_error" class="alert alert-danger" style="display: none;"></div>
<div class="new-page-header">
	<s:text name="contractor.master.title" />
</div>
<div class="panel panel-primary" data-collapsed="0"
	style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title"></div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<s:if test="%{contractorCodeAutoGeneration && mode=='edit'}">
				<label class="col-sm-2 control-label text-right" for="code"> <s:text
						name="contractor.code" /></span>
				</label>
				<div class="col-xs-3 add-margin view-content">
					<s:property value="%{code}" />
				</div>
			</s:if>
			<s:else>
				<label class="col-sm-2 control-label text-right" for="code"> <s:text
						name="contractor.code" /><span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<s:textfield name="code" disabled="%{sDisabled}"
						maxlength="50" cssClass="form-control" value="%{code}" />
				</div>
			</s:else>
			<label class="col-sm-2 control-label text-right" for="name"> <s:text
					name="contractor.name" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="name" id="name" size="40" maxlength="100"
					cssClass="form-control" value="%{name}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="correspondenceAddress" > <s:text
					name="contractor.correspondenceAddress" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="correspondenceAddress" cols="35"
					cssClass="form-control textfieldsvalidate" id="correspondenceAddress"
					value="%{correspondenceAddress}" maxlength = "250" />
			</div>
			<label class="col-sm-2 control-label text-right" for="paymentAddress"> <s:text
					name="contractor.paymentAddress" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="paymentAddress" cols="35" cssClass="form-control textfieldsvalidate"
					id="paymentAddress" value="%{paymentAddress}" maxlength = "250" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="contactPerson"> <s:text
					name="contractor.contactPerson" />
			</label>
			<div class="col-sm-3 add-margin contactPerson"> 
				<s:textfield name="contactPerson" id="contactPerson" size="40"
					maxlength="100" cssClass="form-control" value="%{contactPerson}" />
			</div>
			<label class="col-sm-2 control-label text-right" for="email"> <s:text
					name="contractor.email" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="email" id="email" maxlength="100"
					cssClass="form-control" value="%{email}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="narration"> <s:text
					name="contractor.narration" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="narration" cols="35" cssClass="form-control textfieldsvalidate"
					id="narration" value="%{narration}" maxlength = "250" />
			</div>
			<label class="col-sm-2 control-label text-right" for="mobileNumber"> <s:text
					name="depositworks.applicant.mobile" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="mobileNumber" id="mobileNumber" maxlength="10"
					cssClass="form-control" value="%{mobileNumber}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="panNumber"> <s:text
					name="contractor.panNo" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="panNumber" id="panNumber" maxlength="10"
					cssClass="form-control" value="%{panNumber}" />
			</div>
			<label class="col-sm-2 control-label text-right" for="tinNumber"> <s:text
					name="contractor.tinNo" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="tinNumber" id="tinNumber" maxlength="14"
					cssClass="form-control" value="%{tinNumber}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="bank"> <s:text
					name="contractor.bank" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="select"
				list="dropdownData.bankList" name="bank.id" id="bank"
				cssClass="form-control" listKey="id" listValue="name"
				value="%{bank.id}" />
			</div>
			<label class="col-sm-2 control-label text-right" for="ifscCode"> <s:text
					name="contractor.ifscCode" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="ifscCode" id="ifscCode" maxlength="15"
					cssClass="form-control" value="%{ifscCode}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="bankAccount"> <s:text
					name="contractor.bankAccount" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="bankAccount" id="bankAccount" maxlength="22"
					size="24" cssClass="form-control" value="%{bankAccount}" />
			</div>
			<label class="col-sm-2 control-label text-right" for="pwdApprovalCode"> <s:text
					name="contractor.pwdApprovalCode" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="pwdApprovalCode" id="pwdApprovalCode"
					maxlength="50" cssClass="form-control" value="%{pwdApprovalCode}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label text-right" for="exemptionForm"> 
			<s:text	name="contractor.exemptionFrom" /></label>
			<div class="col-sm-3 add-margin">

				<s:select headerKey="" headerValue="select" list="exmptionMap"
					name="exemptionForm" id="exemptionForm" cssClass="form-control"
					value="%{exemptionForm}" />
			</div>
		</div>
	</div>
	<div data-collapsed="0" style="text-align: left">
		<div class="panel-heading">
			<div class="panel-title">
				<s:text name="contractor.contDetails" />
			</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<div class="text-right add-margin">
				<button class="btn btn-primary" id = "addContractorDetails"
				onclick="contractorDataTable.addRow({SlNo:contractorDataTable.getRecordSet().getLength()+1,status:statusDropdownOptions[0].value,category:categoryDropdownOptions[0].value});return false;">
				<s:text name="contractor.addContractorDetail" /></button>
				</div>

			<div class="yui-skin-sam">
				<div id="contractorTable"></div>
<script type="text/javascript">
            makeContractorDataTable();
         <s:iterator value="model.contractorDetails" status="row_status">
	       <s:if test="#row_status.count == 1">
	             contractorDataTable.updateRow(0,
	                                {department:'<s:property value="department.id"/>',
	        						grade:'<s:property value="grade.id"/>',
	        						status:'<s:property value="status.id"/>',
	                                SlNo:'<s:property value="#row_status.count"/>',
	                                departmentName:'<s:property value="department.deptName"/>',
	                                registrationNumber:'<s:property value="registrationNumber"/>',
	                                gradeName:'<s:property value="grade.grade"/>',
	                                statusDesc:'<s:property value="status.description"/>',
	                                startDate:'<s:property value="validity.startDate"/>',
	                                endDate:'<s:property value="validity.endDate"/>',
	                                category:'<s:property value="category"/>',
	                                categoryName:'<s:property value="category"/>'});
	          </s:if>
	          <s:else>
		        contractorDataTable.addRow(
	        						{department:'<s:property value="department.id"/>',
	        						grade:'<s:property value="grade.id"/>',
	        						status:'<s:property value="status.id"/>',
	                                SlNo:'<s:property value="#row_status.count"/>',
	                                departmentName:'<s:property value="department.name"/>',
	                                registrationNumber:'<s:property value="registrationNumber"/>',
	                                gradeName:'<s:property value="grade.grade"/>',
	                                statusDesc:'<s:property value="status.description"/>',
	                                startDate:'<s:property value="validity.startDate"/>',
	                                endDate:'<s:property value="validity.endDate"/>',
	                                category:'<s:property value="category"/>',
	                                categoryName:'<s:property value="category"/>'});
	            </s:else>  
	              
			        var record = contractorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
			    				        
			        var  column = contractorDataTable.getColumn('registrationNumber');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="registrationNumber"/>';
			        <s:if test="%{validity.startDate!=null}">	
				        column = contractorDataTable.getColumn('startDate');
				        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/>  
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
					</s:if>
			        <s:if test="%{validity.endDate!=null}">				        
				        column = contractorDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/>  
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
			        </s:if>
			     	 column = contractorDataTable.getColumn('departmentName');
			        for(i=0; i < departmentDropdownOptions.length; i++) {
			            if (departmentDropdownOptions[i].value == '<s:property value="department.id"/>') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			        column = contractorDataTable.getColumn('gradeName');
			        for(i=0; i < gradeDropdownOptions.length; i++) {
			            if (gradeDropdownOptions[i].value == '<s:property value="grade.id"  />') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			        column = contractorDataTable.getColumn('statusDesc');
			        for(i=0; i < statusDropdownOptions.length; i++) {
			            if (statusDropdownOptions[i].value == '<s:property value="status.id"/>') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			        column = contractorDataTable.getColumn('categoryName');
			        for(i=0; i < categoryDropdownOptions.length; i++) {
			            if (categoryDropdownOptions[i].value == '<s:property value="category"/>') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			       </s:iterator>         
</script>
				</div>
			</div>
		</div>
	</div>
</div>
	
<script type="text/javascript">
<s:if test="%{mode=='view'}">
	for(i=0;i<document.contractor.elements.length;i++){
		document.contractor.elements[i].disabled=true;
		document.contractor.elements[i].readonly=true;
	} 
	
	contractorDataTable.removeListener('cellClickEvent');		       
	links=document.contractor.getElementsByTagName("a");
	for(i=0;i<links.length;i++){    
	links[i].onclick=function(){return false;};
	}
</s:if>
</script>
