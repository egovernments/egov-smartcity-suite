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
	<s:if test="%{workValueDrillType=='WorkStartedWorkValue'}">
		<s:text name='page.title.workprogressabstract.workValueStarted' />
	</s:if>
	<s:if test="%{workValueDrillType=='SiteHandedOver_WorkNotStartedValue'}">
		<s:text name='page.title.workprogressabstract.siteHandedOverWorkNotStart' />
	</s:if>
	<s:if test="%{workValueDrillType=='WorkNotStartedValue'}">
		<s:text name='page.title.workprogressabstract.workvalueNotStarted' />
	</s:if>
	<s:if test="%{workValueDrillType=='WorkValue'}">
		<s:text name='page.title.workprogressabstract.workvalue' />
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
	         colratio    : [60,350,320,350]
	    });
	});
	
	function viewWorkOrder(woId) {
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+woId+
				"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function viewWorkValuePDF(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&workValueDrillType='+dom.get("workValueDrillType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewWorkValuePDF.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWorkValueExcel(){
	 	var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&workValueDrillType='+dom.get("workValueDrillType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open('${pageContext.request.contextPath}/reports/workProgressAbstract!viewWorkValuePDF.action?'+parameter);
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
		<s:hidden name="workValueDrillType" id="workValueDrillType"/>
		<s:hidden name="natureOfWork" id="natureOfWork" />
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
						 <s:if test="%{workValuePagedList.fullListSize!=0}">
	  				        <display:table name="workValuePagedList" uid="currentRowObject" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" export="false" class="table-header-fix" >
             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="width:1%;text-align:left" >
								     	<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="WO Number">
       								<s:if test="%{#attr.currentRowObject.workOrderNumber!=null && #attr.currentRowObject.workOrderNumber!=''}">
						    			<a href="Javascript:viewWorkOrder('<s:property  value='#attr.currentRowObject.id' />')">
						    				<s:property value="#attr.currentRowObject.workOrderNumber" />
										</a>
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:right" title="No. of Estimates attached" >
									<s:if test="%{#attr.currentRowObject.workOrderEstimates!=null && #attr.currentRowObject.workOrderEstimates.size > 0}">
										<s:property value="#attr.currentRowObject.workOrderEstimates.size" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletdAmount" title="Work Order Value(Rs)">
       								<s:if test="%{#attr.currentRowObject.workOrderAmount!=null}">
       									<s:property value="#attr.currentRowObject.getFormattedString(#attr.currentRowObject.workOrderAmount)" />
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
							
								<display:footer>
									<tr align="right" style="font-weight: bold;">
								  		<td colspan="2"><s:text name="workprogressabstract.grandtotal"/> : </td>
								  		<td><s:property value="%{totalEstCount}"/></td>
								  		<td><s:property value="%{totalWorkValueAmt}"/></td>
								  	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewWorkValuePDF();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewWorkValueExcel();" align="center" />
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