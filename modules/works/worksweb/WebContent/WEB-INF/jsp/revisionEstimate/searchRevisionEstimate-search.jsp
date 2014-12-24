<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
<html>
<head>

	<title>Search Revision Estimate</title>
</head>
<script src="<egov:url path='js/works.js'/>"></script> 
<script>

function validateSearch()
{	
	var bool=false;
	if($F('type') == -1  && $F('reStatus') == -1  &&
	$F('estimatenumber').blank() && $F('execDept')==-1 && 
	$F('fromDate').blank() && $F('toDate').blank()  &&  $F('preparedBy')==-1 &&
	$F('parentCategory')==-1 && $F('category')==-1  && $F('workOrderNumber').blank())
	{	bool=true;
		dom.get("searchEstimate_error").innerHTML='Please Select any one of the Search Parameters'; 
		dom.get("searchEstimate_error").style.display='block';
	} 
		if(!bool){ 
		  	return true;
		}else{return false;}
		
	 
}
function enableSelect(){
	document.getElementById('reStatus').disabled=false;
	return true;
	
}

function disableSelect(){
	document.getElementById('reStatus').disabled=true
}



</script>
<body class="yui-skin-sam">
	<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>

	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="searchRevisionEstimate" theme="simple" name="searchRevisionEstimateForm" onsubmit="return validateSearch();enableSelect()">
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
		<s:push value="model">
		<s:hidden id="source" name="source" value="%{source}" />
		<s:hidden id="workOrderId" name="workOrderId" /> 
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										 Search
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<tr>
				
					<td class="greyboxwk"  width="25%" >From Date:</td>
						<td class="greybox2wk" width="25%" > <s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
							<s:textfield name="fromDate" id="fromDate" cssClass="selectwk" value="%{fromDateFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
							 <a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
								onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/image/calendar.png"
																			alt="Calendar" width="16" height="16" border="0"align="absmiddle" />
							</a>

												</td>
				<td class="greyboxwk" width="25%" >To Date:</td>	
				<td class="greybox2wk" width="25%">
				<s:date name="toDate" var="toDateFormat"format="dd/MM/yyyy" />
				<s:textfield name="toDate" id="toDate" value="%{toDateFormat}" cssClass="selectwk" onfocus="javascript:vDateType='3';"onkeyup="DateFormat(this,this.value,event,false,'3')" />
				<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
				</a></td>
				</tr>
				  <tr>
					 <td class="whiteboxwk"><s:text name="revisionEstimate.search.revisionEstimateStatus" /></td>
					 <s:if test="%{source.equals('cancelRE') }">												
						 <td class="whitebox2wk">	<s:select id="reStatus" name="reStatus" headerKey="-1"
							headerValue="%{getText('list.default.select')}" cssClass="selectwk"
							list="%{dropdownData.statusList}" value="%{reStatus}" 
							listKey="id" listValue="description" /> </td>
							<script>disableSelect();</script> 
					</s:if>
					<s:else>
						 <td class="whitebox2wk"><s:select headerKey="-1"headerValue="%{getText('list.default.select')}"
							name="reStatus" id="reStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id"
							listValue='description' value="%{reStatus}" />
						</td> 	
					</s:else>  
				
					<td class="whiteboxwk"><s:text name="estimate.executing.department" /></td>
						<td class="whitebox2wk"><s:select headerKey="-1"headerValue="%{getText('list.default.select')}"
						name="execDept" id="execDept" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id"
						listValue='deptName' onChange="setupPreparedByList(this);"  value="%{execDept}" />
					<egov:ajaxdropdown id="preparedBy" fields="['Text','Value','Designation']" dropdownId='preparedBy' optionAttributes='Designation' url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
				</td> 
				</tr>
				<tr>
					<td class="greyboxwk"> <s:text name="revisionEstimate.search.estimateNo" />:</td>
					<td class="greybox2wk"><s:textfield name="estimateNumber" id="estimatenumber"  value="%{estimateNumber}"  cssClass="selectwk" />
					</td>
					<td class="greyboxwk"><s:text name="estimate.work.nature" />:</td>
					<td class="greybox2wk">
							<s:select headerKey="-1" headerValue="ALL" name="type" id="type" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name"
										value="%{type.id}" /></td> 
				</tr>
				
												
			<tr>
				<td class="whiteboxwk"><s:text name="estimate.work.type" />	:	</td>
				<td class="whitebox2wk">
					<s:select headerKey="-1" headerValue="ALL" name="parentCategory" id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList"
					listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);" />
					<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}" />
				</td>
				<td class="whiteboxwk"><s:text name="estimate.work.subtype" />:</td>
				<td class="whitebox2wk"><s:select headerKey="-1" headerValue="ALL" name="category" value="%{category.id}" id="category" cssClass="selectwk"
					list="dropdownData.categoryList" listKey="id" listValue="description" />
				</td>
			</tr>
				
					<tr>
						<td class="greyboxwk"> <s:text name="workorder.search.workordernumber" />:</td>
						<td class="greybox2wk"><s:textfield name="workOrderNumber" value="%{workOrderNumber}" id="workOrderNumber" cssClass="selectwk" />
						</td>
						<td class="greyboxwk"><s:text name="estimate.preparedBy" />:</td>
						<td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="estimatePreparedBy" value="%{estimatePreparedBy.id}" id="preparedBy"
								cssClass="selectwk" list="dropdownData.preparedByList" listKey="id" listValue='#this.employeeName+" - "+#this.desigId.designationName' /></td>
							
				</tr>
				</table>
				</table>
				
		
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonadd" method="list"  
									onClick="return validateSearch();enableSelect();"/>
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
					<jsp:include page="searchRevisionEstimate-list.jsp" /> 
							</s:push>
						</table>
					</div>
				</div>
			</div>
		</div>
	</s:form>

<script>


function setupPreparedByList(elem){
 
    deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
   
}
function setupSubTypes(elem){
	categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

</script>
</body>
</html>