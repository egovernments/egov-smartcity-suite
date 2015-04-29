<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title><s:text name='page.title.workprogressabstract.showWO' /><s:property value="departmentName" /></title>
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
	         width : 1100, 
	         minColWidth	 : 10 , 
	         resizeCol	 : true, 
	         colratio    : [30,110,90,110,110,150,100,100,80,100,100]
	    });
	});
	function viewWO(id){
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewTN(negoNumber){
		window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!edit.action?negoNumber="+negoNumber+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWP(pckgNumber) {
		window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?packageNumber="+pckgNumber,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function viewWODrillDownPdf(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&woDrillDownType='+dom.get("woDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
		
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewWODrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWODrillDownExcel(){
		var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&woDrillDownType='+dom.get("woDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
	
	window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewWODrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
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
		<s:hidden name="woDrillDownType" id="woDrillDownType"/>
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
		<s:hidden name="budgetHeadsFirstReportStr" id="budgetHeadsFirstReportStr" />
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
						<s:if test="%{pagedResults.fullListSize!=0}">
	  				        <display:table name="pagedResults" uid="currentRow" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI=""  class="table-header-fix" >
             					 
             					 		
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								   			<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
   								</display:column>
								
       							<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Order Number"  style="text-align:left;">
								 		<a href="#" onclick="viewWO('<s:property value="%{#attr.currentRow.id}" />')" >
						 					<s:property value="%{#attr.currentRow.workOrderNumber}" />	</a>
						 		</display:column>   	
       							
       							<display:column headerClass="pagetableth" class="pagetabletd"   title="Work Order Date" style="text-align:left"	>
								     	<s:date name="#attr.currentRow.workOrderDate" format="dd/MM/yyyy" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd"   title="Tender Negotiation Number" style="text-align:left"	>
						    			<a href="#" onclick="viewTN('<s:property value="%{#attr.currentRow.negotiationNumber}" />')" >
						    				<s:property value="%{#attr.currentRow.negotiationNumber}"/></a>
						     	</display:column>
						     	
						     	<display:column headerClass="pagetableth" class="pagetabletd"   title="Works Package Number" style="text-align:left"	>
							  			<a href="#" onclick="viewWP('<s:property value="%{#attr.currentRow.packageNumber}" />')" >
						     				<s:property value="%{#attr.currentRow.packageNumber}"/></a>
						     	</display:column>
						     	
						     	<display:column headerClass="pagetableth" class="pagetabletd"   title="Contractor Name" style="text-align:left"	>
						     			<s:property value="%{#attr.currentRow.contractor.name}"/>
						     	</display:column>
						     	
						     	<display:column headerClass="pagetableth" class="pagetabletd"   title="WO Prepared By" style="text-align:left"	>
						    		 	<s:property value="%{#attr.currentRow.workOrderPreparedBy.employeeName}"/>
						     	</display:column>  
								
								<display:column headerClass="pagetableth" class="pagetabletd"   title="WO Allocated To" style="text-align:left"	>
						    			<s:property value="%{#attr.currentRow.engineerIncharge.employeeName}"/>
						    	</display:column> 
						     	
						     	<display:column headerClass="pagetableth" class="pagetabletd"   title="No. of Estimates" style="text-align:right"	>
						    			<s:property value="%{#attr.currentRow.workOrderEstimates.size}"/>
						     	</display:column>   						
								
								<display:column headerClass="pagetableth" class="pagetabletd"   title="EMD (Rs)" style="text-align:right"	>
						    		<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.emdAmountDeposited}"/>
									</s:text>
						     	</display:column>
						     	
						     	<display:column title="Work Order Amount(Rs)" headerClass="pagetableth" class="pagetabletd"	style="text-align:right">
						    		<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.workOrderAmount}"/>
									</s:text>
						     	</display:column>
						     									
								<display:footer>
								  	<tr style="font-weight: bold;">
								  		<td></td>
								  		<td></td>
								  		<td colspan="6" align="right"><s:text name="workprogressabstract.grandtotal"/> :</td>
								  		<td align="right"><s:property value="%{totalEstCount}"/></td>
								  		<td align="right"><s:property value="%{totalEMDAmount}"/></td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  		<td align="right"><s:property value="%{totalWOAmount}"/></td>
								   	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewWODrillDownPdf();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewWODrillDownExcel();" align="center" />
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