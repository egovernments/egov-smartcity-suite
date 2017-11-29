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
<html>
<head>
 <link rel="stylesheet" type="text/css"
	href="/EGF/resources/commonyui/yui2.7/paginator/assets/skins/sam/paginator.css">
<script src="/EGF/resources/commonyui/yui2.7/paginator/paginator-min.js"></script>

<title><s:text name="budget.search" /></title>
<style>
.budgetSearch {
	overflow: hidden;
	font-size: 12px;
	width: 90px;
}
</style>
</head>
<body>
	<jsp:include page="budgetHeader.jsp">
		<jsp:param name="heading" value="Budget Details" />
	</jsp:include>
	<s:if test="%{not savedbudgetDetailList.empty}">
		<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
		<div class="yui-skin-sam">
			<div id="budgetDetailTable"
				style="width: 100%; overflow-x: auto; overflow-y: hidden;"></div>
		</div>
		<script>
			function createAmountFieldFormatter(values,prefix,suffix){
			    return function(el, oRecord, oColumn, oData) {
					var value = (YAHOO.lang.isValue(oData))?oData:"";
				    value = budgetDetailsTable.getRecordIndex(oRecord)>=values.length?0.0:values[budgetDetailsTable.getRecordIndex(oRecord)]
					el.innerHTML = "<label id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right'>"+value+"</label>";
				}
			}
			
			var budgetDetailTable = function() {
				var budgetDetailColumns = [ 
					{key:"budget.id",label:'Budget',width:90,className:"budgetSearch", sortable:true},
					{key:"budgetGroup.id",label:'Budget Group',className:"budgetSearch",sortable:true,width:200},
					<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
						{key:"executingDepartment.id", label:'Executing Department',className:"budgetSearch",sortable:true},
					</s:if>
					<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">				
						{key:"functionary.id",label:'Functionary',className:"budgetSearch",sortable:true} ,
					</s:if>
					<s:if test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
						{key:"function.id",label:'Function',className:"budgetSearch",sortable:true,width:90} ,
					</s:if>
					<s:if test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
						{key:"scheme.id",label:'Scheme',className:"budgetSearch",sortable:true} ,
					</s:if>
					<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
						{key:"subScheme.id",label:'Sub Scheme',className:"budgetSearch",sortable:true} ,
					</s:if>
					<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
						{key:"fund.id",label:'Fund',width:90,className:"budgetSearch",sortable:true} ,
					</s:if>
					<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
						{key:"boundary.id",label:'Field',className:"budgetSearch",sortable:true} ,
					</s:if>
						{key:"anticipatoryAmount",label:'Anticipatory Upto<br/>31 March',className:"budgetSearch"},
						{key:"amount",label:'Estimate Amount',width:"50em"},
						{key:"actual_previous_year",label:'<s:property value="previousfinYearRange"/> Actual <br/> Amount',className:"budgetSearch"},
						<s:if test="%{re}">
							{key:"actual_current_year",label:'<s:property value="currentfinYearRange"/> Revised<br/> Actual Amount',className:"budgetSearch"},
							{key:"estimate_current_year",label:'<s:property value="currentfinYearRange"/> Revised<br/> Approved Amount',className:"budgetSearch"},
						</s:if>
						<s:else>
							{key:"actual_current_year",label:'<s:property value="currentfinYearRange"/> Actual<br/> Amount',className:"budgetSearch"},
							{key:"estimate_current_year",label:'<s:property value="currentfinYearRange"/> Approved<br/> Amount',className:"budgetSearch"},
						</s:else>
						{key:"reappropriation_amount",label:'Total <br/>Reappropriation Amount',className:"budgetSearch"},
						{key:"approved_amount",label:'Total <br/>Approved Amount',className:"budgetSearch"},
						{key:"comment",label:'Comments',className:"budgetSearch"},
						/* {key:"document",label:'Documents',className:"budgetSearch"} */
				];
				var myConfigs = {
    				paginator : new YAHOO.widget.Paginator({
        				rowsPerPage: 10
    				})
				};
			    var budgetDetailDS = new YAHOO.util.DataSource(); 
				budgetDetailsTable = new YAHOO.widget.DataTable("budgetDetailTable",budgetDetailColumns, budgetDetailDS,myConfigs);	
				budgetDetailsTable.on('cellClickEvent',function (oArgs) {
					var target = oArgs.target;
					var record = this.getRecord(target);
					var column = this.getColumn(target);
					if (column.key == 'Delete') { 			
						this.deleteRow(record);
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
					}        
				});
					<s:iterator value="savedbudgetDetailList" status="stat" var="p">
						budgetDetailsTable.addRow({SlNo:budgetDetailsTable.getRecordSet().getLength()+1,
							"budget.id":"<s:property value='budget.name'/>",
							"budgetGroup.id":"<s:property value='budgetGroup.name'/>",
							<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
								"executingDepartment.id":"<s:property value='executingDepartment.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">				
								"functionary.id":"<s:property value='functionary.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
								"function.id":"<s:property value='function.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
								"scheme.id":"<s:property value='scheme.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
								"subScheme.id":"<s:property value='subScheme.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
								"fund.id":"<s:property value='fund.name'/>",
							</s:if>
							<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
								"boundary.id":"<s:property value='boundary.name'/>",
							</s:if>
							"anticipatoryAmount":'<s:property value="anticipatoryAmount"/>',
							"amount":'<s:property value="originalAmount"/>',
							<s:if test="%{previousYearBudgetDetailIdsAndAmount[#p.id.toString()] != null}">
								"actual_previous_year":'<s:property value="%{previousYearBudgetDetailIdsAndAmount[#p.id.toString()]}"/>',
							</s:if>
							<s:else>
								"actual_previous_year":'0.00',
							</s:else>
							<s:if test="%{budgetDetailIdsAndAmount[#p.id.toString()] != null}">
								"actual_current_year":'<s:property value="%{budgetDetailIdsAndAmount[#p.id.toString()]}"/>',
							</s:if>
							<s:else>
								"actual_current_year":'0.00',
							</s:else>
							<s:if test="%{re}">
								"estimate_current_year":'<s:property value="budgetAmountView[#stat.index].currentYearReApproved"/>',
							</s:if>
							<s:else>
								"estimate_current_year":'<s:property value="budgetAmountView[#stat.index].currentYearBeApproved"/>',
							</s:else>
							"reappropriation_amount":'<s:property value="approvedReAppropriationsTotal.setScale(2).toString()"/>',
							"approved_amount":'<s:property value="%{calculateTotal(#p)}"/>',
							"comment":'<s:property value="comment"/>',
							/* "document":'<input type="submit" class="buttonsubmit" value="View" onclick="'+'viewDocumentManager(<s:property value="#p.documentNumber"/>);return false;"/>' */
						});
					</s:iterator>
			}
			budgetDetailTable();
			</script>
	</s:if>
	<s:else>
		<div class="error">
			<s:text name="budget.no.details.found" />
		</div>
	</s:else>
	<br />
	<br />
	<br />
</body>
</html>
