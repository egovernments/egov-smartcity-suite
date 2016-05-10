<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<c:url value='/resources/javascript/prototype.js'/>"></script>
<html>
	<head>  
	    <s:if test="%{userMode=='new'}">
	    <title> <s:text name="page.title.asset.cat.create" /></title>
	    </s:if>
	    <s:elseif test="%{userMode=='view'}">
	    <title> <s:text name="page.title.asset.cat.view" /></title>
	    </s:elseif>
	    <s:elseif test="%{userMode=='edit'}">
	    <title> <s:text name="page.title.asset.cat.edit" /></title>
	    </s:elseif>  
	</head> 
	<body id="home">

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
		<script>
			function enableFields(){
				for(i=0;i<document.assetCategoryForm.elements.length;i++){
				        document.assetCategoryForm.elements[i].disabled=false;
				}   
			}
			
			function validateFormAndSubmit(){
				var code= dom.get("code").value;
				var name= dom.get("name").value;
				var assetType = dom.get("assetType").value;
				var revAccountCode = dom.get("revAccountCode").value;
				var assetAccountCode  = dom.get("assetAccountCode").value;
				var uom  = dom.get("uom").value;
				var depreciationMethod  = dom.get("depreciationMethod").value;
				var pattern=/[^0-9a-zA-Z-&:/ ]/;
				var namepattern=/[^0-9a-zA-Z-&:/ ]/;
				if(code.match(pattern)){
				   showMessage('category_error', '<s:text name="assetcat.code.alphaNumericwithspecialchar" />');
					return false;
				}else if(name.match(namepattern) ){
				   showMessage('category_error', '<s:text name="assetcat.name.alphaNumericwithspecialchar" />');
					return false;
				} else if (name == '' || name == null){
					showMessage('category_error', '<s:text name="assetcat.name.null" />');
					return false;
				} else if (assetType == -1){
					showMessage('category_error', '<s:text name="assetcat.assettype.null" />');
					return false;
				} else if (assetAccountCode == -1){
					showMessage('category_error', '<s:text name="assetcat.assetaccountcode.null" />');
					return false;
				} else if (revAccountCode == -1){
					showMessage('category_error', '<s:text name="assetcat.revaccountcode.null" />');
					return false;
				} else if (uom == -1){
					showMessage('category_error', '<s:text name="assetcat.uom.null" />');
					return false;
				}
				else {
				    clearMessage('category_error')
					links=document.assetCategoryForm.getElementsByTagName("span");
					errors=false;
					for(i=0;i<links.length;i++){
				        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
				            errors=true;
				            break;
				        }
				    }
				    if(errors){
				        dom.get("category_error").style.display='';
				    	document.getElementById("category_error").innerHTML='<s:text name='assetcat.null'/>';
				    	return;
				    }
				    else{
						document.getElementById('catAttrTemplate').value=document.getElementById('catTemVal').value;
				    	enableFields();
				    	document.assetCategoryForm.action='${pageContext.request.contextPath}/assetcategory/assetCategory-save.action';
				    	document.assetCategoryForm.submit();
				    }
				 }
				}
		</script>
		<s:form action="assetCategory" theme="simple" name="assetCategoryForm">
		<s:token name="%{tokenName()}"/> 
			<div class="errorstyle" id="category_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2">
							<div></div>
							</div>
							<div class="rbcontent2">
								<s:hidden name="id" />
								<s:hidden name="userMode" />
								<%@ include file='assetCategory-form.jsp'%>
								<s:if test="%{userMode=='view'}">
									<%@ include file='assetCategory-depdetailview.jsp'%>
								</s:if>
								<s:else>
									<%@ include file='assetCategory-depdetailform.jsp'%>
								</s:else>
							</div>
							<div class="rbbot2">
							<div></div>
							</div>
						</div>
					</div>
				</div>
				<s:if test="%{userMode=='new'}">
					<div class="buttonholderwk" id="divButRow1" name="divButRow1">
						<input type="submit" class="buttonfinal" value="SAVE"
							id="saveButton" name="button"
							onclick="return validateFormAndSubmit();" />
						<input type="button" class="buttonfinal" value="CLOSE"
							id="closeButton" name="button"
							onclick="window.close();" />
					</div>
				</s:if>
				<s:elseif test="%{userMode=='view'}">
					<div class="buttonholderwk" id="divButRow2" name="divButRow2">
						<input type="button" class="buttonfinal" value="BACK"
							id="backButton" name="button"
							onclick="history.go(-1);" />
						<input type="button" class="buttonfinal" value="CLOSE"
							id="closeButton" name="button"
							onclick="window.close();" />
					</div>
				</s:elseif>
				<s:elseif test="%{userMode=='edit'}">
					<div class="buttonholderwk" id="divButRow3" name="divButRow3">
						<input type="submit" class="buttonfinal" value="SAVE"
							id="saveButton" name="button"
							onclick="validateFormAndSubmit();" />
						<input type="button" class="buttonfinal" value="CLOSE"
							id="closeButton" name="button"
							onclick="window.close();" />
						<input type="button" class="buttonfinal" value="SEARCH"
							id="backButton" name="button"
							onclick="goBack();" />
					</div>
				</s:elseif>
			</s:push>
		</s:form>
	</body>
</html>
