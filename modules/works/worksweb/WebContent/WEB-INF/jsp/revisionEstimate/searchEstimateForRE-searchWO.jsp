<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<html>
	<head>
		<title><s:text name='page.title.search.workorder' /></title>
	</head>
<script src="<egov:url path='js/works.js'/>"></script>
<style>
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<script type="text/javascript">
function validateAndSubmit(){
	   if($F('contractorId') == -1 && $F('deptId') == -1 && $F('typeId') == -1 && $F('parentCategory') == -1 
			   				&& $F('category') == -1
							&& $F('workOrderNumber').blank() && $F('estimateNumber').blank()
							&& $F('fromDate').blank()
							&& $F('toDate').blank()){
							var bool=true;
							if(document.getElementById('estimateNumber')){
								if(document.getElementById('estimateNumber').value=="")
									bool=false;
								}
		if(!bool){
		$('workOrder_error').show();					
	  	$('mandatory_error').show();
	  	$('mandatory_length_error').hide();
	  	return false;
	  	}
	  }
	  
	 
	   if(!$F('workordernumber').blank()  && $F('workordernumber').length < 4){
			  	$('workOrder_error').show();
			  	$('mandatory_length_error').show();
			  	$('mandatory_error').hide();
			     return false;
		 }
		    $('workOrder_error').hide();
		}
function gotoCreateRE(){
	var table=document.getElementById('currentRow');
	var lastRow = table.rows.length-1;
	var wo="";
	var est="";
	
	if(lastRow==1){
		if(document.getElementById("radio").checked)
			wo=document.getElementById("woId").value;	
		    est=document.getElementById("estId").value;							
	}
	else{
		for(i=0;i<lastRow;i++){			
			if(document.forms[0].radio[i].checked){
				wo=document.forms[0].woId[i].value;
				est=document.forms[0].estId[i].value;						
			}
		}
	}

	if(wo!='' && est!=''){
		window.open('${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!newform.action?originalWOId='+wo+'&originalEstimateId='+est+'&sourcepage=createRE','_self');
	}
	else{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="revisionEstimate.select.row" />';
		window.scroll(0, 0);
		return false;
	  }
	  dom.get("workOrder_error").style.display='none';
	  document.getElementById("workOrder_error").innerHTML=''; 

	  
	}

function setupSubTypes(elem){
	categoryId=elem.options[elem.selectedIndex].value;
	populatecategory({category:categoryId});
}
</script>
	<body onload="init();">
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchWorkOrderForReForm" 
			id="searchWorkOrderForReForm" theme="simple" onsubmit="return validateAndSubmit(); ">
			
			<div class="errorstyle" id="workOrder_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    </div>
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								<s:hidden id="workOrderId" name="workOrderId" />
								<s:hidden id="woNumber" name="woNumber" />	
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.workorder" />
													</div>
												</td>
											</tr>
											<tr>
											<td class="whiteboxwk" width="25%"><s:text
													name="estimate.executing.department" /></td>
											<td class="whitebox2wk" width="25%">
											<s:select headerKey="-1"
													headerValue="%{getText('list.default.select')}"
													name="deptId" id="deptId" cssClass="selectwk"
													list="dropdownData.executingDepartmentList" listKey="id"
													listValue='deptName'/></td>
											<td  width="25%" class="whiteboxwk"><s:text name="estimate.number" />
												:</td>
											<td  width="25%" class="whitebox2wk"><s:textfield
													name="estimateNumber" id="estimateNumber" value="%{estimateNumber}"
													cssClass="selectboldwk" /></td>
										</tr>
										<tr>
											<td class="greyboxwk"><s:text
													name="estimate.work.nature" />:</td>
											<td class="greybox2wk"><s:select
													headerKey="-1" headerValue="ALL" name="typeId" id="typeId"
													cssClass="selectwk" list="dropdownData.typeList"
													listKey="id" listValue="name" /></td>

											<td class="greyboxwk"><s:text
													name="workorder.search.workordernumber" />:</td>
											<td class="greybox2wk"><s:textfield
													name="workOrderNumber" value="%{workOrderNumber}"
													id="workOrderNumber" cssClass="selectwk" /></td>
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="estimate.work.type" />
												:</td>
											<td class="whitebox2wk"><s:select headerKey="-1"
													headerValue="ALL" name="parentCategory" id="parentCategory"
													cssClass="selectwk" list="dropdownData.parentCategoryList"
													listKey="id" listValue="description"
													value="%{parentCategory.id}"
													onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown" 
													fields="['Text','Value']" dropdownId='category'
													url='estimate/ajaxEstimate!subcategories.action' 
													selectedValue="%{category.id}" /></td>

											<td class="whiteboxwk"><s:text
													name="estimate.work.subtype" /> :</td>
											<td class="whitebox2wk"><s:select headerKey="-1"
													headerValue="ALL" name="category" value="%{category.id}"
													id="category" cssClass="selectwk"
													list="dropdownData.categoryList" listKey="id"
													listValue="description" /></td>
										</tr>
										<tr>
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderFromdate" />:
												</td>
												<td class="greybox2wk">
													<s:date name="fromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="fromDate" id="fromDate"
														cssClass="selectwk" value="%{fromDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderTodate" />:
												</td>
												<td class="greybox2wk">
													<s:date name="toDate" var="toDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toDateFormat}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
										<tr>
											<td class="whiteboxwk"><s:text
													name="workorder.search.contractor" />:</td>

											<td colspan="3" class="whitebox2wk"><s:select
													id="contractorId" name="contractorId" cssClass="selectwk"
													list="%{contractorForApprovedWorkOrder}" headerKey="-1"
													headerValue="" value="%{contractorId}" /></td>

										</tr>
										<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"  onclick="return validateAndSubmit(); "method="search" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
											</td>
											</tr>
											<%@ include file='woListSearchForRE-list.jsp'%>
										</table>
									<P align="center">
										<input type="button" class="buttonadd"
											value="Create Revision Estimate" id="addButton"
											name="createRevisionEstimateButton" onclick="gotoCreateRE();"
											align="center" />
									</P>
								</td>
								</tr>
								
							</table>
							
						</div>
						<!-- end of rbroundbox2 -->
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>

