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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<SCRIPT type="text/javascript">
var defaultDept = '<s:property value="executingDepartment.id"/>'

function updateAllGridValues(){
	if(document.getElementById('budgetDetail_budget')){
		updateHiddenFields('budget.id',document.getElementById('budgetDetail_budget').value);
	}
	if(document.getElementById('budgetDetail_executingDepartment')){
		if(defaultDept != null && defaultDept!='' && document.getElementById('budgetDetail_executingDepartment').selectedIndex==0){
			document.getElementById('budgetDetail_executingDepartment').selectedIndex = defaultDept;
		}
		updateGrid('executingDepartment.id',document.getElementById('budgetDetail_executingDepartment').selectedIndex);
	}
	if(document.getElementById('budgetDetail_function'))
		updateGrid('function.id',document.getElementById('budgetDetail_function').selectedIndex);
	if(document.getElementById('budgetDetail_functionary'))
		updateGrid('functionary.id',document.getElementById('budgetDetail_functionary').selectedIndex);
	if(document.getElementById('budgetDetail_scheme'))
		updateGrid('scheme.id',document.getElementById('budgetDetail_scheme').selectedIndex);
	if(document.getElementById('budgetDetail_subScheme'))
		updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex);
	if(document.getElementById('budgetDetail_fund'))
		updateGrid('fund.id',document.getElementById('budgetDetail_fund').selectedIndex);
	if(document.getElementById('budgetDetail_boundary'))
		updateGrid('boundary.id',document.getElementById('budgetDetail_boundary').selectedIndex);

}

var gridReappropriation = [];
var gridPreviousYearActuals = [];
var gridOldActuals = [];
var gridCurrentYearActuals = [];
var gridCurrentYearApproved = [];
var gridCurrentYearTotal = [];
<s:iterator value="budgetAmountView" status="stat">
	gridReappropriation.push('<s:property value="budgetAmountView[#stat.index].reappropriation"/>');
	gridPreviousYearActuals.push('<s:property value="budgetAmountView[#stat.index].previousYearActuals"/>');
	gridOldActuals.push('<s:property value="budgetAmountView[#stat.index].oldActuals"/>');
	gridCurrentYearActuals.push('<s:property value="budgetAmountView[#stat.index].currentYearBeActuals"/>');
	gridCurrentYearApproved.push('<s:property value="budgetAmountView[#stat.index].currentYearBeApproved"/>');
	gridCurrentYearTotal.push('<s:property value="budgetAmountView[#stat.index].total"/>');
</s:iterator>

function createAmountFieldFormatter(values,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
	    value = budgetDetailsTable.getRecordIndex(oRecord)>=values.length?0.0:values[budgetDetailsTable.getRecordIndex(oRecord)]
		el.innerHTML = "<label id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right'>"+value+"</label>";
	}
}


	var budgetGroupOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.budgetGroupList">
	    budgetGroupOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
	</s:iterator>
	<s:if test="%{shouldShowField('executingDepartment')}">		
	   	var executingDepartmentOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.executingDepartmentList">
			executingDepartmentOptions.push({label:"<s:property value="deptName.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
	</s:if>
	<s:if test="%{shouldShowField('function')}">		
		var functionOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.functionList">
	    	functionOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
  	</s:if>
   	<s:if test="%{shouldShowField('functionary')}">		
		var functionaryOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.functionaryList">
	    	functionaryOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('scheme')}">		
		var schemeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.schemeList">
	    	schemeOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('subScheme')}">		
		var subSchemeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.subSchemeList">
	    	subSchemeOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('fund')}">		
		var fundOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.fundList">
	    	fundOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('boundary')}">		
		var boundaryOptions=[{label:"--- Select ---", value:"0"}];		
		<s:iterator value="dropdownData.boundaryList">
			boundaryOptions.push({label:"<s:property value="name.replaceAll('\n',' ')"/>", value:'<s:property value="id"/>'})
		</s:iterator>
	</s:if>
		
		function addGridRows(){
			<s:iterator value="budgetDetailList" status="stat">
				budgetDetailsTable.addRow({SlNo:budgetDetailsTable.getRecordSet().getLength()+1,
					"id":'<s:property value="id"/>',
					"documentNumber":'<s:property value="documentNumber"/>',
					"budget.id":'<s:property value="budget.id"/>',
					"budgetGroup.id":'<s:property value="budgetGroup.id"/>',
					<s:if test="%{shouldShowField('executingDepartment')}">				
						"executingDepartment.id":'<s:property value="executingDepartment.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('functionary')}">				
						"functionary.id":'<s:property value="functionary.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('function')}">				
						"function.id":'<s:property value="function.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('scheme')}">				
						"scheme.id":'<s:property value="scheme.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('subScheme')}">				
						"subScheme.id":'<s:property value="subScheme.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('fund')}">				
						"fund.id":'<s:property value="fund.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('boundary')}">				
						"boundary.id":'<s:property value="boundary.id"/>',
					</s:if>
					"old_actuals":'<s:property value="budgetAmountView[#stat.index].oldActuals"/>',
					"actual_previous_year":'<s:property value="budgetAmountView[#stat.index].previousYearActuals"/>',
					"approved_current_year":'<s:property value="budgetAmountView[#stat.index].currentYearBeApproved"/>',
					"reappropriation":'<s:property value="budgetAmountView[#stat.index].reappropriation"/>',
					"total":'<s:property value="budgetAmountView[#stat.index].total"/>',
					"actual_current_year":'<s:property value="budgetAmountView[#stat.index].currentYearBeActuals"/>',
					"anticipatoryAmount":'<s:property value="anticipatoryAmount"/>',
					
					"re_amount":'<s:property value="originalAmount"/>',
					"be_next_year_amount":'<s:property value="beAmounts[#stat.index]"/>'
				});
			</s:iterator>
		}

		function mandatorySign(field){
			var mandatoryFields = {}
			mandatoryFields.executingDepartment = <s:if test="%{isFieldMandatory('executingDepartment')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.Function = <s:if test="%{isFieldMandatory('function')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.functionary = <s:if test="%{isFieldMandatory('functionary')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.scheme = <s:if test="%{isFieldMandatory('scheme')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.subScheme = <s:if test="%{isFieldMandatory('subScheme')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.boundary = <s:if test="%{isFieldMandatory('boundary')}">true</s:if><s:else>false</s:else>;
			mandatoryFields.fund = <s:if test="%{isFieldMandatory('fund')}">true</s:if><s:else>false</s:else>;
			if(mandatoryFields[field] == true)
				return '<span class="mandatory"></span>';
			else 
				return ''; 
		}

	var currentYearRange = '<s:property value="currentYearRange"/>(Rs)'; 
	var nextYearRange = '<s:property value="nextYearRange"/>(Rs)';
	var currentYear = 'Actuals Up To(Rs)';
	var curentYearRangeWithoutRs='<s:property value="currentYearRange"/>';
	var currentFullYear=curentYearRangeWithoutRs.substr(0,2)+curentYearRangeWithoutRs.substr(5,7);
	var currentHalfYear=curentYearRangeWithoutRs.substr(0,4);
	var previousYear = 'Actuals<br/><s:property value="previousYearRange"/>(Rs)';
	var lastButOneYear = 'Actuals<br/><s:property value="lastButOneYearRange"/>(Rs)';
	var currentYearApproved = 'BE <br/><s:property value="currentYearRange"/>(Rs)(A)';
	var anticipatoryAmountLable='Expected<br>Expenditure<br>from 01 Oct '+currentHalfYear+'<br/> 31 March '+currentFullYear+' (Rs)';
	var makeBudgetDetailTable = function() {
		var budgetDetailColumns = [ 
			{key:"id",label:'documentNumber',hidden:true, formatter:createIdFieldFormatter("budgetDetailList",".id")},
			{key:"documentNumber",label:'documentNumber',hidden:true, formatter:createHiddenTextFieldFormatter("budgetDetailList",".documentNumber")},
			<s:if test="%{shouldShowField('fund')}">				
				{key:"fund.id",label:'<s:text name="fund"/>'+mandatorySign('fund'),width:130,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:fundOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('function')}">				
				{key:"function.id",label:'<s:text name="function"/>'+mandatorySign('Function'),width:450,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:functionOptions} ,
			</s:if>
			{key:"budget.id",label:'<s:text name="budget.budget"/>',width:90, formatter:createHiddenTextFieldFormatter("budgetDetailList",".budget.id")},
			{key:"budgetGroup.id",label:'Budget Group <span class="mandatory">*</span>',width:250, formatter:createDropdownFormatter(BUDGETDETAILLIST),dropdownOptions:budgetGroupOptions},
			<s:if test="%{shouldShowField('executingDepartment')}">				
				{key:"executingDepartment.id", label:'Executing Department'+mandatorySign('executingDepartment'),width:90,formatter:createDropdownFormatter(BUDGETDETAILLIST), dropdownOptions:executingDepartmentOptions},				
			</s:if>
			<s:if test="%{shouldShowField('functionary')}">				
				{key:"functionary.id",label:'<s:text name="functionary"/>'+mandatorySign('functionary'),width:90,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:functionaryOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('scheme')}">				
				{key:"scheme.id",label:'<s:text name="scheme"/>'+mandatorySign('scheme'),width:90,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:schemeOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('subScheme')}">				
				{key:"subScheme.id",label:'<s:text name="subscheme"/>'+mandatorySign('subScheme'),width:90,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:subSchemeOptions} ,
			</s:if>
			
			<s:if test="%{shouldShowField('boundary')}">				
				{key:"boundary.id",label:'<s:text name="field"/>'+mandatorySign('boundary'),width:90,formatter:createDropdownFormatter(BUDGETDETAILLIST),  dropdownOptions:boundaryOptions} ,
			</s:if>
			{key:"old_actuals",label:lastButOneYear,width:90, formatter:createAmountFieldFormatter(gridOldActuals,"budgetAmountView",".oldActuals")},
			{key:"actual_previous_year",label:previousYear,width:90, formatter:createAmountFieldFormatter(gridPreviousYearActuals,"budgetAmountView",".previousYearActuals")},
			{key:"approved_current_year",label:currentYearApproved,width:"40em", formatter:createAmountFieldFormatter(gridCurrentYearApproved,"budgetAmountView","currentYearBeApproved")},
			{key:"reappropriation",label:'<s:text name="budget.reappropriation"/>(B)',width:150, formatter:createAmountFieldFormatter(gridReappropriation,"budgetAmountView",".reappropriation")},
			{key:"total",label:'Total(A+B)<br/>'+currentYearRange,width:150, formatter:createAmountFieldFormatter(gridCurrentYearTotal,"budgetAmountView",".total")},
			{key:"actual_current_year",label:currentYear, width:"40em",formatter:createAmountFieldFormatter(gridCurrentYearActuals,"budgetAmountView","currentYearBeActuals")},
			{key:"anticipatoryAmount",label:anticipatoryAmountLable,width:"50em", formatter:createAnticipatoryFieldFormatter(BUDGETDETAILLIST,".anticipatoryAmount")},
			{key:"re_amount",label:'RE '+'<br/>'+currentYearRange+'<span class="mandatory">*</span>',width:"50em", formatter:createTextFieldFormatterOnblur(BUDGETDETAILLIST,".originalAmount")},
			{key:"be_next_year_amount",label:'BE '+'<br/>'+nextYearRange+'<span class="mandatory">*</span>',width:"50em", formatter:createTextFieldFormatter("beAmounts","")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Upload',label:'Upload',width:70,formatter:createDocUploadFormatter("budgetDetailsTable","budgetDetailList",".documentNumber")}
		];
	    var budgetDetailDS = new YAHOO.util.DataSource(); 
		budgetDetailsTable = new YAHOO.widget.DataTable("budgetDetailTable",budgetDetailColumns, budgetDetailDS);	
		budgetDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				budgetDetailsTable.addRow({SlNo:budgetDetailsTable.getRecordSet().getLength()+1});
				updateAllGridValues();
			}
			if (column.key == 'Delete') { 			
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
				}
				else{
					bootbox.alert("This row can not be deleted");
				}
			}        
		});
		<s:if test="%{budgetDetailList.size() == 0 && getActionErrors().size()==0 && getFieldErrors().size()==0}">
			budgetDetailsTable.addRow({SlNo:budgetDetailsTable.getRecordSet().getLength()+1});
			updateAllGridValues();
		</s:if>
	}


	var listValues = new Array(<s:property value="budgetDetailList.size"/>);
	<s:iterator value="budgetDetailList" status="stat">
		listValues["budgetDetailList[<s:property value='#stat.index'/>].budgetGroup.id"] = <s:if test="budgetGroup.id"><s:property value="budgetGroup.id"/></s:if><s:else>0</s:else>;
		listValues["budgetDetailList[<s:property value='#stat.index'/>].id"] = <s:if test="id"><s:property value="id"/></s:if><s:else>0</s:else>;
		<s:if test="%{shouldShowField('executingDepartment')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].executingDepartment.id"] = <s:if test="executingDepartment.id"><s:property value="executingDepartment.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('function')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].function.id"] = <s:if test="function.id"><s:property value="function.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('functionary')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].functionary.id"] = <s:if test="functionary.id"><s:property value="functionary.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('scheme')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].scheme.id"] = <s:if test="scheme.id"><s:property value="scheme.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('subScheme')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].subScheme.id"] = <s:if test="subScheme.id"><s:property value="subScheme.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('fund')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].fund.id"] = <s:if test="fund.id"><s:property value="fund.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('boundary')}">				
			listValues["budgetDetailList[<s:property value='#stat.index'/>].boundary.id"] = <s:if test="boundary.id"><s:property value="boundary.id"/></s:if><s:else>0</s:else>;
		</s:if>
	</s:iterator>

	function setValues(){
		for (key in listValues){
			setSelectedIndex(key)
		} 
	}

	function hideColumns(){
		<s:if test="%{!gridFields.contains('budget')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('budget.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('executingDepartment')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('executingDepartment.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('function')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('function.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('functionary')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('functionary.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('scheme')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('scheme.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('subScheme')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('subScheme.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('fund')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('fund.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('boundary')}">
			budgetDetailsTable.hideColumn(budgetDetailsTable.getColumn('boundary.id'))
		</s:if>
	}

	function makeJSONCall(fields,url,params,onSuccess,onFailure){
		 dataSource=new YAHOO.util.DataSource(url);
		            dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
		            dataSource.connXhrMode = "queueRequests";
		            dataSource.responseSchema = {
		                resultsList: "ResultSet.Result",
		                fields: fields
		            };
			        var callbackObj = {
		            success : onSuccess,
		            failure : onFailure
		        };
		        dataSource.sendRequest("?"+toQuery(params),callbackObj);
		}

		function toQuery(params){
		   var query="";
		   for(var f in params){
		     query+=f+"="+params[f]+"&"
		   }
		   if(query.lastIndexOf('&')==query.length-1) query=query.substring(0,query.length-1);
		   return query;
		}	
		
	function updateREamount(obj)
	{
	var name=obj.name;
	var actualName=name.replace('.anticipatoryAmount','currentYearBeActuals');
	actualName=actualName.replace('budgetDetailList','budgetAmountView');
	var actualAmt=0;
	var anticipAmt=0;
	if(parseFloat(document.getElementById(actualName).innerHTML)!=NaN)
	{
	actualAmt=parseFloat(document.getElementById(actualName).innerHTML);
	}
	if(parseFloat(document.getElementById(name).value)!=NaN)
	{
	anticipAmt=parseFloat(document.getElementById(name).value);
	}
	var reName=obj.name.replace('anticipatoryAmount','originalAmount');
	document.getElementById(reName).value=actualAmt+anticipAmt;   
	}
		
		
</SCRIPT>
