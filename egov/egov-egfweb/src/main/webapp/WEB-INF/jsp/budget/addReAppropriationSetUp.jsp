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

<SCRIPT type="text/javascript">
var BUDGET_REAPPROPRIATION_LIST='newBudgetReAppropriationList';

function dropdownFormatter(tableName,prefix){
    return function(el, oRecord, oColumn, oData) {
		table = eval(tableName);
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+table.getRecordIndex(oRecord)+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+table.getRecordIndex(oRecord)+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}

function makeTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0;
		el.innerHTML = "<input type='text' id='"+prefix+"["+budgetReAppropriationTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetReAppropriationTable.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;'/>";
	}
}

function updateReAppGrid(field,index){
	if(budgetReAppropriationTable != null){
		len = budgetReAppropriationTable.getRecordSet().getLength()
		count=0;
		i=0;
		while(count < len){
			element=document.getElementById('newBudgetReAppropriationList['+i+'].'+field)
			if(element){
				element.selectedIndex = index;
				count++;
			}
			i++;
		}
	}
}
function updateAllReAppGridValues(){
	if(document.getElementById('budgetReAppropriation_executingDepartment')){
		if(defaultDept != null && defaultDept!='' && document.getElementById('budgetReAppropriation_executingDepartment').selectedIndex==0){
			document.getElementById('budgetReAppropriation_executingDepartment').selectedIndex = defaultDept;
		}
		updateReAppGrid('budgetDetail.executingDepartment.id',document.getElementById('budgetReAppropriation_executingDepartment').selectedIndex);
	}
	if(document.getElementById('budgetReAppropriation_function'))
		updateReAppGrid('budgetDetail.function.id',document.getElementById('budgetReAppropriation_function').selectedIndex);
	if(document.getElementById('budgetReAppropriation_functionary'))
		updateReAppGrid('budgetDetail.functionary.id',document.getElementById('budgetReAppropriation_functionary').selectedIndex);
	if(document.getElementById('budgetReAppropriation_scheme'))
		updateReAppGrid('budgetDetail.scheme.id',document.getElementById('budgetReAppropriation_scheme').selectedIndex);
	if(document.getElementById('budgetReAppropriation_subScheme'))
		updateReAppGrid('budgetDetail.subScheme.id',document.getElementById('budgetReAppropriation_subScheme').selectedIndex);
	if(document.getElementById('budgetReAppropriation_fund'))
		updateReAppGrid('budgetDetail.fund.id',document.getElementById('budgetReAppropriation_fund').selectedIndex);
	if(document.getElementById('budgetReAppropriation_boundary'))
		updateReAppGrid('budgetDetail.boundary.id',document.getElementById('budgetReAppropriation_boundary').selectedIndex);

}

function makeAmountFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<div id='"+prefix+"["+budgetReAppropriationTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetReAppropriationTable.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right'>0.00</div>";
	}
}

	var budgetOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.budgetList">
	    budgetOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
	</s:iterator>
	var budgetGroupOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.budgetGroupList">
	    budgetGroupOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
	</s:iterator>
	<s:if test="%{shouldShowField('executingDepartment')}">		
	   	var executingDepartmentOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.executingDepartmentList">
			executingDepartmentOptions.push({label:"<s:property value="deptName"/>", value:'<s:property value="id"/>'})
		</s:iterator>
	</s:if>
	<s:if test="%{shouldShowField('function')}">		
		var functionOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.functionList">
	    	functionOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
  	</s:if>
   	<s:if test="%{shouldShowField('functionary')}">		
		var functionaryOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.functionaryList">
	    	functionaryOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('scheme')}">		
		var schemeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.schemeList">
	    	schemeOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('subScheme')}">		
		var subSchemeOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.subSchemeList">
	    	subSchemeOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('fund')}">		
		var fundOptions=[{label:"--- Select ---", value:"0"}];
		<s:iterator value="dropdownData.fundList">
	    	fundOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
   	</s:if>
   	<s:if test="%{shouldShowField('boundary')}">		
		var boundaryOptions=[{label:"--- Select ---", value:"0"}];		
		<s:iterator value="dropdownData.boundaryList">
			boundaryOptions.push({label:"<s:property value="name"/>", value:'<s:property value="id"/>'})
		</s:iterator>
	</s:if>
		function addReAppGridRows(){
			<s:iterator value="newBudgetReAppropriationList" status="stat">
				budgetReAppropriationTable.addRow({SlNo:budgetReAppropriationTable.getRecordSet().getLength()+1,
					"budgetDetail.budget.id":'<s:property value="budgetDetail.budget.id"/>',
					"budgetDetail.budgetGroup.id":'<s:property value="budgetDetail.budgetGroup.id"/>',
					<s:if test="%{shouldShowField('executingDepartment')}">				
						"budgetDetail.executingDepartment.id":'<s:property value="budgetDetail.executingDepartment.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('functionary')}">				
						"budgetDetail.functionary.id":'<s:property value="budgetDetail.functionary.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('function')}">				
						"budgetDetail.function.id":'<s:property value="budgetDetail.function.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('scheme')}">				
						"budgetDetail.scheme.id":'<s:property value="budgetDetail.scheme.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('subScheme')}">				
						"budgetDetail.subScheme.id":'<s:property value="budgetDetail.subScheme.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('fund')}">				
						"budgetDetail.fund.id":'<s:property value="budgetDetail.fund.id"/>',
					</s:if>
					<s:if test="%{shouldShowField('boundary')}">				
						"budgetDetail.boundary.id":'<s:property value="budgetDetail.boundary.id"/>',
					</s:if>
					"amount":'<s:property value="deltaAmount"/>',
					"planningPercent":'<s:property value="planningPercent"/>',
					"planningBudgetApproved":'<s:property value="planningBudgetApproved"/>',
					"total_available":'<s:property value="originalAmount"/>'
				});
			</s:iterator>
		}

	var makeBudgetReAppropriationTable = function() {
		var budgetReAppropriationColumns = [ 
			{key:"budgetDetail.budget.id",label:'Budget <span class="mandatory1">*</span>',width:90, formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),dropdownOptions:budgetOptions},
			{key:"budgetDetail.budgetGroup.id",label:'Budget Group <span class="mandatory1">*</span>',width:120, formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),dropdownOptions:budgetGroupOptions},
			<s:if test="%{shouldShowField('executingDepartment')}">				
				{key:"budgetDetail.executingDepartment.id", label:'Executing Department'+mandatorySign('executingDepartment'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST), dropdownOptions:executingDepartmentOptions},				
			</s:if>
			<s:if test="%{shouldShowField('functionary')}">				
				{key:"budgetDetail.functionary.id",label:'Functionary'+mandatorySign('functionary'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:functionaryOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('function')}">				
				{key:"budgetDetail.function.id",label:'Function'+mandatorySign('Function'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:functionOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('scheme')}">				
				{key:"budgetDetail.scheme.id",label:'Scheme'+mandatorySign('scheme'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:schemeOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('subScheme')}">				
				{key:"budgetDetail.subScheme.id",label:'Sub Scheme'+mandatorySign('subScheme'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:subSchemeOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('fund')}">				
				{key:"budgetDetail.fund.id",label:'Fund'+mandatorySign('fund'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:fundOptions} ,
			</s:if>
			<s:if test="%{shouldShowField('boundary')}">				
				{key:"budgetDetail.boundary.id",label:'Field'+mandatorySign('boundary'),width:90,formatter:dropdownFormatter('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST),  dropdownOptions:boundaryOptions} ,
			</s:if>
			{key:"amount",label:'Budget Estimate(Rs)',width:"50em", formatter:createTextFieldFormatterWithOnblur('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST,".deltaAmount")},
			{key:"planningPercent",label:'Planning Budget<br/>Percentage',width:"50em", formatter:createTextFieldFormatterWithOnblur('budgetReAppropriationTable',BUDGET_REAPPROPRIATION_LIST,".planningPercent")},
			{key:"planningBudgetApproved",label:'Planning Budget Approved(Rs)',width:"50em", formatter:makeAmountFieldFormatter(BUDGET_REAPPROPRIATION_LIST,".planningBudgetApproved")},
			{key:"total_available",label:'Budget Provision avail<br/> after Additional Appropriation(Rs)',width:"90", formatter:makeAmountFieldFormatter(BUDGET_REAPPROPRIATION_LIST,".remainingAmount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var budgetReAppropriationDS = new YAHOO.util.DataSource(); 
		budgetReAppropriationTable = new YAHOO.widget.DataTable("budgetReAppropriationsTable",budgetReAppropriationColumns, budgetReAppropriationDS);	
		budgetReAppropriationTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				budgetReAppropriationTable.addRow({SlNo:budgetReAppropriationTable.getRecordSet().getLength()+1});
				updateAllReAppGridValues();
				updateBudgetDropDownForRow(record.getData('SlNo'));
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
		<s:if test="%{newBudgetReAppropriationList.size() == 0 && getActionErrors().size()==0 && getFieldErrors().size()==0}">
			budgetReAppropriationTable.addRow({SlNo:budgetReAppropriationTable.getRecordSet().getLength()+1});
			updateAllReAppGridValues();
		</s:if>
	}


	var budgetReAppListValues = new Array(<s:property value="newBudgetReAppropriationList.size"/>);
	<s:iterator value="newBudgetReAppropriationList" status="stat">
		budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.budgetGroup.id"] = <s:if test="budgetDetail.budgetGroup.id"><s:property value="budgetDetail.budgetGroup.id"/></s:if><s:else>0</s:else>;
		<s:if test="%{shouldShowField('executingDepartment')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.executingDepartment.id"] = <s:if test="budgetDetail.executingDepartment.id"><s:property value="budgetDetail.executingDepartment.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('function')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.function.id"] = <s:if test="budgetDetail.function.id"><s:property value="budgetDetail.function.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('functionary')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.functionary.id"] = <s:if test="budgetDetail.functionary.id"><s:property value="budgetDetail.functionary.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('scheme')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.scheme.id"] = <s:if test="budgetDetail.scheme.id"><s:property value="budgetDetail.scheme.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('subScheme')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.subScheme.id"] = <s:if test="budgetDetail.subScheme.id"><s:property value="budgetDetail.subScheme.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('fund')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.fund.id"] = <s:if test="budgetDetail.fund.id"><s:property value="budgetDetail.fund.id"/></s:if><s:else>0</s:else>;
		</s:if>
		<s:if test="%{shouldShowField('boundary')}">				
			budgetReAppListValues["newBudgetReAppropriationList[<s:property value='#stat.index'/>].budgetDetail.boundary.id"] = <s:if test="budgetDetail.boundary.id"><s:property value="budgetDetail.boundary.id"/></s:if><s:else>0</s:else>;
		</s:if>
	</s:iterator>

	function setValuesForReAppropriation(){
		for (key in budgetReAppListValues){
			setSelectedIndex(key)
		} 
	}

	function hideReAppropriationTableColumns(){
		<s:if test="%{!gridFields.contains('executingDepartment')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.executingDepartment.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('function')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.function.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('functionary')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.functionary.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('scheme')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.scheme.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('subScheme')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.subScheme.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('fund')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.fund.id'))
		</s:if>
		<s:if test="%{!gridFields.contains('boundary')}">
			budgetReAppropriationTable.hideColumn(budgetReAppropriationTable.getColumn('budgetDetail.boundary.id'))
		</s:if>
	}
	
</SCRIPT>
