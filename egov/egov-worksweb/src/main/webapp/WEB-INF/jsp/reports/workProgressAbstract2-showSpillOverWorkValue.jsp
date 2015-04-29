<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />

<html>
<title>
	<s:text name='page.title.workprogressabstract.spillOverWorkValue' />
	<s:property value="departmentName" />
</title>

<style type="text/css">
th.pagetableth {
	background-color: #E8EDF1;
	height: 30px;
	overflow: hidden;
	border-left: 1px solid #D1D9E1;
	color: #00639B;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 13px;
	font-weight: bold;
	line-height: 14px;
	padding: 3px;
	text-align: left;
	word-wrap: break-word;
}

td.pagetabletd{
	color: #333333;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	text-align: left;
	line-height: 13px;
	overflow: hidden;
	border: 1px solid #D1D9E1;
	padding: 3px;
}	

div.t_fixed_header {
	position		: relative;
	margin 			: 0; 
	width			: 100%;
}

div.t_fixed_header.ui .headtable th {
	padding			: 5px;
	text-align 		: center;
	border-width 	: 0 1px 0 0;
	border-style 	: solid;
	line-height		: 12px;
	font-size		: 11px;
	word-wrap       : normal;
}
.ui-state-default a, .ui-state-default a:link { 
	 text-decoration: underline;
	 font-size: 11px; 
}

.ui-state-default a:visited {
	color: #2e6e9e;
} 

div.t_fixed_header div.body {
	padding			: 0;
	width			: 100%;
	overflow-x		: hidden;
}

</style>
	<script type="text/javascript">
	jQuery.noConflict();

	jQuery(document).ready(function() {
		
		    jQuery('.table-header-fix').fixheadertable({
		    	caption : '<s:property value="%{subHeader}" />', 
		         height  : 400,
		         minColWidth	 : 10 , 
		         resizeCol	 : true, 
		         width : 1115, 
		         colratio    : [30,100,90,308,90,110,90,100,100,75]
		    });
	            
	});

	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewSpillOverWorkValuePdf(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
		
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewSpillOverWorkValueExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewSpillOverWorkValueExcel(){
		var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
	
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewSpillOverWorkValueExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function openWorkPrgReport(id,estId){
		window.open("${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action?woId="+id+
			"&sourcePage=deptWiseReport&estId="+estId,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
</script>

<body>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" theme="simple">
		<s:hidden name="fromDate" id="fromDate" />
	 	<s:hidden name="toDate" id="toDate" />
		<s:hidden name="worksType" id="worksType" />
		<s:hidden name="worksSubType" id="worksSubType" />
		<s:hidden name="executingDepartment" id="executingDepartment" />
		<s:hidden name="natureOfWork" id="natureOfWork" />
		<s:hidden name="fund" id="fund" />
		<s:hidden name="function" id="function" />
		<s:hidden name="preparedBy" id="preparedBy" />
		<s:hidden name="scheme" id="scheme" />
		<s:hidden name="subScheme" id="subScheme" />
		<s:hidden name="budgetHeads" id="budgetHeads" />
		<s:hidden name="coa" id="coa" />
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
		<s:hidden name="depositCodesStr" id="depositCodesStr" />
		<s:hidden name="depositCodeIdsStr" id="depositCodeIdsStr" />
		<s:hidden name="budgetHeadIdsStr" id="budgetHeadIdsStr" />
		
		
		<div class="formmainbox">
		<div class="insidecontent">
		<div id="printContent" class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr id="resultRow" ><td colspan="4"> 
						 <s:if test="%{spillOverEstPaginatedList.fullListSize!=0}">
						 
						 	<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
							<s:text id="estNumber" name="%{getText('label.estimateNumber')}"></s:text>
							<s:text id="nameOfEst" name="%{getText('label.work.name')}"></s:text>
							<s:text id="estDate" name="%{getText('label.estimateDate')}"></s:text>
							<s:text id="adminSancDate" name="%{getText('label.estimate.admin.sanction.date')}"></s:text>
							<s:text id="estJurisdiction" name="%{getText('estimate.ward')}"></s:text>
							<s:text id="estAmount" name="%{getText('label.estimate.amount')}"></s:text>
							<s:text id="estCreatedDate" name="%{getText('label.created.date')}"></s:text>
							<s:text id="workProgressLink" name="%{getText('label.work.progress.link')}"></s:text>
							<s:text id="spillOverValue" name="%{getText('label.spillOver.work.value')}"></s:text>
							
							
						    <display:table name="spillOverEstPaginatedList" uid="currentRow" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" export="false" class="table-header-fix" >
             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="${SlNo}"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								     	<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${estNumber}">
       								<s:if test="%{#attr.currentRow.estNumber!=null}">
										<a href="Javascript:viewEstimate('<s:property  value='%{#attr.currentRow.estimateId}' />')">
												<s:property value="#attr.currentRow.estNumber" />
										</a> 
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
       							
       							<display:column headerClass="pagetableth" class="pagetabletd" title="${estDate}" titleKey='estimate.search.estDate' style="text-align:left" >
       								<s:if test="%{#attr.currentRow.estDate!=null}">
										<s:date name="#attr.currentRow.estDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${nameOfEst}" >
									<s:if test="%{#attr.currentRow.estName!=null}">
										<s:property value="#attr.currentRow.estName" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${estCreatedDate}" titleKey='estimate.search.estimateDate' style="text-align:left" >
										<s:if test="%{#attr.currentRow.estCreatedDate!=null}">
											<s:date name="#attr.currentRow.estCreatedDate" format="dd/MM/yyyy" />
										</s:if>
										<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${estJurisdiction}">
										<s:property value="#attr.currentRow.jurisdiction" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${adminSancDate}" titleKey='estimate.search.approvedDate' style="text-align:left" >
       								<s:if test="%{#attr.currentRow.adminSanctionDate!=null}">
										<s:date name="#attr.currentRow.adminSanctionDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${estAmount}" titleKey='estimate.search.name' style="text-align:right" >
									<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.estAmount}"/>
									</s:text>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${spillOverValue}" titleKey='estimate.search.name' style="text-align:right" >
									<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.spillOverWorkValue}"/>
									</s:text>
								</display:column>
								
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${workProgressLink}"
	                    							style="text-align:center" >
	                    		<s:if test="%{#attr.currentRow.woId!=null}">
	                    							<a href="Javascript:openWorkPrgReport(<s:property  value='%{#attr.currentRow.woId}' />,<s:property  value='%{#attr.currentRow.estimateId}' />)">
	                   										<img
															src="${pageContext.request.contextPath}/image/book_open.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
								</s:if>					
								</display:column>
								<display:footer>
								  	<tr style="font-weight: bold;">
								  		<td></td>
								  		<td></td>
								  		<td></td>
								  		<td colspan="5" align="right"><s:text name="workprogressabstract.grandtotal"/> :</td>
								  		<td align="right">
									  		<s:text name="contractor.format.number" >
												<s:param name="rate" value="%{totalForSpillOverValue}"/>
											</s:text>
								  		</td>
								   	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewSpillOverWorkValuePdf();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewexcel" onclick="viewSpillOverWorkValueExcel();" align="center" />
								<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();"/>
							</div>
						</s:if>
						<s:else>
							<div align="center"><font color="red">No record Found.</font></div>
						</s:else>
						</td></tr>
				</table>
			</td>
		</tr>
		</table>

	</div>
	</div>
	</div>
	</div>									
</s:form>
</body>
</html>