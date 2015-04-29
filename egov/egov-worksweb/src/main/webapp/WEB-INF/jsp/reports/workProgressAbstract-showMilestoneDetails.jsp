<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title>
	<s:if test="%{milestoneDrillDownType=='InProgress25'}">
		<s:text name='page.title.workprogressabstract.inprogress25' />
	</s:if>
	<s:if test="%{milestoneDrillDownType=='InProgress50'}">
		<s:text name='page.title.workprogressabstract.inprogress50' />
	</s:if>
	<s:if test="%{milestoneDrillDownType=='InProgress75'}">
		<s:text name='page.title.workprogressabstract.inprogress75' />
	</s:if>
	<s:if test="%{milestoneDrillDownType=='InProgress99'}">
		<s:text name='page.title.workprogressabstract.inprogress99' />
	</s:if>
	<s:if test="%{milestoneDrillDownType=='WorksCompleted'}">
		<s:text name='page.title.workprogressabstract.workCompleted' />
	</s:if>
	<s:if test="%{milestoneDrillDownType=='WorksNotCompleted'}">
		<s:text name='page.title.workprogressabstract.workNotCompleted' />
	</s:if>
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
	width			: 139%;
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

div.t_fixed_header_main_wrapper {
	position 		: relative; 
	overflow 		: auto; 
}

div.t_fixed_header div.headtable {
	border			: 0;	
	overflow-x		: hidden;
    overflow-y		: hidden;
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
	         colratio    : [35,100,300,80,80,80,80,100,150,80,80,80,100,80,80]
	    });
	});
	
	function viewEstimate(id) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewMilestoneDrillDownPdf(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&milestoneDrillDownType='+dom.get("milestoneDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
		
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewMilestoneDrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewMilestoneDrillDownExcel(){
		var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&natureOfWork='+dom.get("natureOfWork").value+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&milestoneDrillDownType='+dom.get("milestoneDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;
	
	window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewMilestoneDrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
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
		<s:hidden name="budgetHeadsFirstReportStr" id="budgetHeadsFirstReportStr" />
		<s:hidden name="depositCodesStr" id="depositCodesStr" />
		<s:hidden name="milestoneDrillDownType" id="milestoneDrillDownType"/>
		<s:hidden name="depositCodeIdsStr" id="depositCodeIdsStr" />
		<s:hidden name="budgetHeadIdsStr" id="budgetHeadIdsStr" />
		
		
		<div class="errorstyle" id="error_search"
				style="display: none;"></div>
				
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
							
							<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
							<s:text id="estNumber" name="%{getText('label.estimateNumber')}"></s:text>
							<s:text id="nameOfEst" name="%{getText('label.work.name')}"></s:text>
							<s:text id="adminSancDate" name="%{getText('label.estimate.admin.sanction.date')}"></s:text>
							<s:text id="woDate" name="%{getText('label.woDate')}"></s:text>
							<s:text id="siteHandedOverDate" name="%{getText('label.site.handed.date')}"></s:text>
							<s:text id="workCommencementDate" name="%{getText('label.work.commencement.date')}"></s:text>
							<s:text id="woValue" name="%{getText('label.wo.value')}"></s:text>
							<s:text id="nameOfContractor" name="%{getText('workorder.contractor.code')}"></s:text>
							<s:text id="conPeriod" name="%{getText('con.period')}"></s:text>
							<s:text id="expecComplDate" name="%{getText('label.expected.completion.date')}"></s:text>
							<s:text id="complDate" name="%{getText('label.completion.date')}"></s:text>
							<s:text id="conPeriod" name="%{getText('con.period')}"></s:text>
							<s:text id="msPerc" name="%{getText('label.milestone.perc')}"></s:text>
							<s:text id="paymentRelsd" name="%{getText('label.payment')}"></s:text>
							<s:text id="paymentRelsdPerc" name="%{getText('label.payment.perc')}"></s:text>
							<s:text id="workProgressLink" name="%{getText('label.work.progress.link')}"></s:text>
							
							  				        
	  				        <display:table name="pagedResults" uid="currentRow" pagesize="20" 
            						 cellpadding="0" cellspacing="0"  requestURI="" class="table-header-fix" >
             				
             				 <display:column headerClass="pagetableth" class="pagetabletd" title="${SlNo}"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								   			<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
   								</display:column>														
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${estNumber}" titleKey='estimate.search.name' style="text-align:left" >
									<a href="#" onclick="viewEstimate('<s:property value="%{#attr.currentRow.estimateId}" />')">
						 					<s:property value="#attr.currentRow.estNumber" />	</a>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${nameOfEst}" titleKey='estimate.search.name' style="text-align:left" >
									<s:property value="#attr.currentRow.estName" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${adminSancDate}" titleKey='estimate.search.name' style="text-align:left" >
									<s:date name="#attr.currentRow.adminSanctionDate" format="dd/MM/yyyy" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${woDate}" titleKey='estimate.search.name' style="text-align:left" >
									<s:date name="#attr.currentRow.woDate" format="dd/MM/yyyy" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${siteHandedOverDate}" titleKey='estimate.search.name' style="text-align:left" >
									<s:date name="#attr.currentRow.siteHandedDate" format="dd/MM/yyyy" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${workCommencementDate}" titleKey='estimate.search.name' style="text-align:left" >
									<s:date name="#attr.currentRow.workCommencedDate" format="dd/MM/yyyy" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${woValue}" titleKey='estimate.search.name' style="text-align:right" >
									<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.woValue}"/>
									</s:text>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${nameOfContractor}" titleKey='estimate.search.name' style="text-align:left" >
									<s:property value="#attr.currentRow.contractorName" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${conPeriod}" titleKey='estimate.search.name' style="text-align:left" >
									<s:property value="#attr.currentRow.contractPeriod" />
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${expecComplDate}" titleKey='estimate.search.name' style="text-align:left" >
									<s:date name="#attr.currentRow.expectedCompletionDate" format="dd/MM/yyyy" />
								</display:column>
								
								<s:if test="%{milestoneDrillDownType=='WorksCompleted'}">
									<display:column headerClass="pagetableth" class="pagetabletd"    title="${complDate}"  style="text-align:left;" >
	  								 	<s:date name="#attr.currentRow.completionDate" format="dd/MM/yyyy" />
	  								</display:column>
								</s:if>
								<s:else>
									<display:column headerClass="pagetableth" class="pagetabletd" title="${msPerc}" titleKey='estimate.search.name' style="text-align:right" >
										<s:text name="contractor.format.number" >
											<s:param name="rate" value="%{#attr.currentRow.milestonePerc}"/>
										</s:text>
									</display:column>
								</s:else>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${paymentRelsd}" titleKey='estimate.search.name' style="text-align:right" >
									<s:text name="contractor.format.number" >
											<s:param name="rate" value="%{#attr.currentRow.paymentReleasedAmount}"/>
									</s:text>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${paymentRelsdPerc}" titleKey='estimate.search.name' style="text-align:right" >
									<s:text name="contractor.format.number" >
											<s:param name="rate" value="%{#attr.currentRow.paymentReleasedPerc}"/>
									</s:text>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${workProgressLink}"
	                    							style="text-align:center" >
	                    							<a href="Javascript:openWorkPrgReport(<s:property  value='%{#attr.currentRow.woId}' />,<s:property  value='%{#attr.currentRow.estimateId}' />)">
	                   										<img
															src="${pageContext.request.contextPath}/image/book_open.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
								</display:column>
								
								<display:footer>
								  	<tr style="font-weight: bold;">
								  		<td></td>
								  		<td></td>
								  		<td></td>
								  		<td colspan="4" align="right"><s:text name="workprogressabstract.grandtotal"/> :</td>
								  		<td align="right"><s:property value="%{totalWOAmount}"/></td>
								   	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
					 			<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewMilestoneDrillDownPdf();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewMilestoneDrillDownExcel();" align="center" />
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