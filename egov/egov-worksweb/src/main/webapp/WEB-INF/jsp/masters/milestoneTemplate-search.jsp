<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script>

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatesubType({category:categoryId});
}

loadSubType= function(req, res){
	if(dom.get('subTypeOfWork').value != ""){
		dom.get('subType').value=dom.get('subTypeOfWork').value;
	}
}

function submitMilestoneTemplateSearchForm() {
	document.milestoneTemplateSearchForm.status.disabled=false;
    document.milestoneTemplateSearchForm.action='${pageContext.request.contextPath}/masters/milestoneTemplate!searchDetails.action';
    document.milestoneTemplateSearchForm.submit();
}

function bodyOnLoad(){
/*	if(dom.get('milestoneTemplateCode').value != ""){
		dom.get('code').value=dom.get('milestoneTemplateCode').value;  
	}*/
	if(dom.get('typeOfWork').value != ""){
		dom.get('workType').value=dom.get('typeOfWork').value;
		populatesubType({category:dom.get('workType').value});
	}
}
</script>

<html>
<title><s:text name='page.title.milestone.template.search'/></title>
<body onload="bodyOnLoad()" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
   <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
   </s:if>
   <s:if test="%{hasActionMessages()}">
       <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
       </div>
   </s:if>
<s:form theme="simple" name="milestoneTemplateSearchForm" action="milestoneTemplate!searchDetails.action">
  <s:hidden name="typeOfWork" id="typeOfWork" />
  <s:hidden name="subTypeOfWork" id="subTypeOfWork" />
<div class="errorstyle" id="milestoneTemplate_error"
				style="display: none;"></div>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2"><div class="datewk">
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
			 <td colspan="4" class="headingwk" align="left">
				<div class="arrowiconwk">
				  <img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
				  <s:text name='title.search.criteria' />
				</div>
			  </td>
			 </tr>
             <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="milestone.template.search.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="workType" id="workType" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subType.id}" afterSuccess="loadSubType" />
                </td>
                <td class="whiteboxwk"><s:text name="milestone.template.search.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/></td>
              </tr>
               <tr>
                <td width="11%" class="greyboxwk"><s:text name="milestone.template.search.code" />:</td>
                <td width="21%" class="greybox2wk"><s:textfield name="code" value="%{code}" id="code" cssClass="selectwk" />
                <td class="greyboxwk"><s:text name="milestone.template.search.description" />:</td>
                <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" value="%{description}"/></td>
               </tr>
               <tr>
                <td class="whiteboxwk"><s:text name="milestone.template.search.name" />:</td>
                <td class="whitebox2wk"><s:textarea name="name" cols="35" cssClass="selectwk" id="name" value="%{name}"/></td>
                <td width="15%" class="whiteboxwk"><s:text name="milestone.template.search.status" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"  list="#{'0':'INACTIVE', '1':'ACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="selectwk"/> </td>
               </tr>
               <tr>
                <td  colspan="4" class="shadowwk"> </td>               
               </tr>
               <tr><td>&nbsp;</td></tr>			
               <tr>
                 <td colspan="4"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="submit" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitMilestoneTemplateSearchForm()" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
              </tr>
              <tr>
				<td colspan="4" align="left">
					 <table width="100%" border="0" cellspacing="0" cellpadding="0">
						 <tr>
							<td class="headingwk">
								<div class="arrowiconwk">
									<img src="${pageContext.request.contextPath}/image/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="milestone.template.searh.result.title" />
								</div>
							</td>
						 </tr>
                      </table> 
                 </td>
               </tr> 
     </table>               
     </div>
        <%@ include file='milestoneTemplate-searchResults.jsp'%> 	
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>
<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
</s:form>
</body>

</html>