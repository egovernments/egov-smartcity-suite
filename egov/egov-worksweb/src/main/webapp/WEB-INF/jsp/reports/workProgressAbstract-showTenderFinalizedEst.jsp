<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title><s:text name='page.title.workprogressabstract.tenderfinalEst' /> <s:property value="departmentName" /></title>
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
	         colratio    : [50,200,200,200,200,200]
	    });
	});
	function viewTenderNegotiation(negotiationNumber) {
		window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!edit.action?negoNumber="+negotiationNumber+
				"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWorksPackage(packageNumber) {
		window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?packageNumber="+packageNumber+
				"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function viewTenderFinalPDF(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&tenderFinalDrillType='+dom.get("tenderFinalDrillType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewTenderFinalizedPDF.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewTenderFinalExcel(){
	 	var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&tenderFinalDrillType='+dom.get("tenderFinalDrillType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open('${pageContext.request.contextPath}/reports/workProgressAbstract!viewTenderFinalizedPDF.action?'+parameter);
	}
	
</script>

<body>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" theme="simple">
		<s:hidden name="fromDate" id="fromDate" />
		<s:hidden name="toDate" id="toDate" />
		<s:hidden name="worksType" id="worksType" />
		<s:hidden name="worksSubType" id="worksSubType" />
		<s:hidden name="fund" id="fund" />
		<s:hidden name="function" id="function" />
		<s:hidden name="preparedBy" id="preparedBy" />
		<s:hidden name="scheme" id="scheme" />
		<s:hidden name="subScheme" id="subScheme" />
		<s:hidden name="coa" id="coa" />
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="tenderFinalDrillType" id="tenderFinalDrillType"/>
		<s:hidden name="natureOfWork" id="natureOfWork"/>
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
						 <s:if test="%{tenderFinalPagedList.fullListSize!=0}">
	  				        <display:table name="tenderFinalPagedList" uid="currentRowObject" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" export="false" class="table-header-fix" >
             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								     	<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="TN Number">
       								<s:if test="%{#attr.currentRowObject.worksPackage!=null && #attr.currentRowObject.worksPackage.negotiationNumber!=null 
													&& #attr.currentRowObject.worksPackage.negotiationNumber!=''}">
						    			<a href="Javascript:viewTenderNegotiation('<s:property  value='%{#attr.currentRowObject.worksPackage.negotiationNumber}' />')">
						    				<s:property value="#attr.currentRowObject.worksPackage.negotiationNumber" />
										</a>
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="WP Number" >
									<s:if test="%{#attr.currentRowObject.worksPackage!=null && #attr.currentRowObject.worksPackage.wpNumber!=null 
													&& #attr.currentRowObject.worksPackage.wpNumber!=''}">
										<a href="Javascript:viewWorksPackage('<s:property  value='%{#attr.currentRowObject.worksPackage.wpNumber}' />')">
											<s:property value="#attr.currentRowObject.worksPackage.wpNumber" />
										</a>
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Estimate Number">
       								<s:if test="%{#attr.currentRowObject.estimate!=null && #attr.currentRowObject.estimate.estimateNumber!=''}">
       									<a href="Javascript:viewEstimate('<s:property  value='%{#attr.currentRowObject.estimate.id}' />')">
       										<s:property value="#attr.currentRowObject.estimate.estimateNumber" />
       									</a>
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
							
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Project Code">
									<s:if test="%{#attr.currentRowObject.estimate!=null && #attr.currentRowObject.estimate.projectCode!=null
													&& #attr.currentRowObject.estimate.projectCode.code!=''}">
										<s:property value="#attr.currentRowObject.estimate.projectCode.code" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="Work Value (Rs)" >
									<s:if test="%{#attr.currentRowObject.estimate!=null}">
										<s:property value="#attr.currentRowObject.estimate.workValue.formattedString" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:footer>
									<tr align="right" style="font-weight: bold;">
								  		<td colspan="5"><s:text name="workprogressabstract.grandtotal"/> : </td>
								  		<td><s:property value="%{totalEstAmount}"/></td>
								  	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewTenderFinalPDF();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewTenderFinalExcel();" align="center" />
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