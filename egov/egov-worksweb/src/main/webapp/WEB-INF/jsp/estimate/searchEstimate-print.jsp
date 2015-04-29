<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<head>
	
		<title><s:text name='page.title.search.estimate' /></title> 

</head>
<link href="<egov:url path='/css/works.css'/>" rel="stylesheet" type="text/css" />
<link href="<egov:url path='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
<script type="text/javascript">

function  setValues(){
<s:iterator var="e" value="dropdownData.executingDepartmentList" status="s">
<s:if test="%{id==execDept}">
document.getElementById('spanexecDept').innerHTML = "<s:property value='%{deptName}' />"
</s:if>
</s:iterator>
<s:iterator var="e" value="dropdownData.typeList" status="s">
 <s:if test="%{id==expenditureTypeid}">
document.getElementById('spanexpenditureType').innerHTML = "<s:property value='%{name}' />"
 </s:if>
</s:iterator>
<s:iterator var="e" value="dropdownData.preparedByList" status="s">

 <s:if test="%{id==empId}">
  document.getElementById('spanemployeeName').innerHTML = "<s:property value='%{employeeName}' />"
 </s:if>
</s:iterator>
   <s:iterator var="e" value="estimateStatuses" status="s">
    <s:if test="%{code==status}">
  document.getElementById('statusid').innerHTML = "<s:property value='%{description}' />"
 </s:if>
</s:iterator>
<s:iterator var="e" value="dropdownData.parentCategoryList" status="s">
   <s:if test="%{id==parentCategory.id}">
  document.getElementById('spanparentCategory').innerHTML = "<s:property value='%{description}' />"
 </s:if>
</s:iterator>

<s:iterator var="e" value="dropdownData.categoryList" status="s">
  <s:if test="%{id==category.id}">
  document.getElementById('spancategory').innerHTML = "<s:property value='%{description}' />"
 </s:if>
</s:iterator>

}
function hidebuttons(){
document.getElementById("printButton").style.visibility='hidden';
document.getElementById("BACK").style.visibility='hidden';
 window.print();
 setTimeout("Func1Delay()", 3000);
 return false;
}
function Func1Delay()
{
document.getElementById("printButton").style.visibility='visible';
document.getElementById("BACK").style.visibility='visible';
}

</script>

<body onload="setValues();">
<div class="errorstyle" id="searchEstimate_error" style="display: none;"></div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>
<s:form name="printform" theme="simple" >
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
         
          <tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="page.subheader.search.estimate" /></div></td>
	        </tr>
	  	        <tr>
				 <td width="11%" class="greyboxwk"><s:text name="estimate.search.estimateStatus" />:</td>
				 <td width="21%" class="greybox2wk"><SPAN id="statusid">ALL</SPAN>
				 
				</td>
				  <td width="15%" class="greyboxwk"><s:text name="estimate.executing.department" />:</td>
		         <td width="53%" class="greybox2wk"><SPAN id="spanexecDept"></SPAN>
				</td>
				</tr>
			    <tr>
		         <td width="11%" class="whiteboxwk"><s:text name="estimate.search.estimateNo" />:</td>
		         <td width="21%" class="whitebox2wk"> <s:property value="%{estimateNumber}"/></td>
		         <td width="15%" class="whiteboxwk"><s:text name="estimate.work.nature" />:</td>
		         <td width="53%" class="whitebox2wk"><SPAN id="spanexpenditureType">ALL</SPAN>
		        </tr>		   
		       <tr>		   	 
			      <td class="greyboxwk">Estimate Date From:</td>
				  <td class="greybox2wk">
				  	<s:property value="%{fromDate}"/>
			       </td>
	               <td width="17%" class="greyboxwk">Estimate Date To:</td>
	                <td width="17%" class="greybox2wk">
	                 <s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy"/>
	                <s:property value="%{toDate}"/>
	              </td>
           </tr>
           <tr>
                <td class="whiteboxwk"><s:text name="estimate.work.type" />:</td>
                 <td width="53%" class="whitebox2wk"><SPAN id="spanparentCategory">ALL</SPAN>
		        
                </td>
              <td class="whiteboxwk"><s:text name="estimate.work.subtype" />:</td>
                 <td width="53%" class="whitebox2wk"><SPAN id="spancategory">ALL</SPAN>
		        </td>
            </tr>
           <tr>
                <td class="greyboxwk"><s:text name="estimate.preparedBy" />:</td>
                <td class="greybox2wk"><SPAN id="spanemployeeName" ></SPAN>
             </td>
              <td class="greyboxwk"><s:text name="estimate.description" />:</td>
                <td class="greybox2wk"><s:property value= "%{description}" />
                </td>
              </tr>
          
		   <tr>
	       		<td colspan="4" class="shadowwk"></td>
	       </tr>
	       <tr>
	       		
	      	</tr>
	
			<div class="errorstyle" id="error_search" style="display: none;"></div>
	
			<s:hidden id="estimateId" name="estimateId" />
			<s:hidden id="source" name="source" />
			
					<%@ include file='estimate-printlist.jsp'%>
			
		</table>
		</td>
        </tr>
	</table>
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
</s:form>
</body>
</html>
