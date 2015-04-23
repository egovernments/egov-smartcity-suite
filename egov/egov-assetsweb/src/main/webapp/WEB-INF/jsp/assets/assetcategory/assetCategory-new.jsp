<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>  
	    <s:if test="%{userMode=='new'}">
	    <title>- <s:text name="page.title.asset.cat.create" /></title>
	    </s:if>
	    <s:elseif test="%{userMode=='view'}">
	    <title>- <s:text name="page.title.asset.cat.view" /></title>
	    </s:elseif>
	    <s:elseif test="%{userMode=='edit'}">
	    <title>- <s:text name="page.title.asset.cat.edit" /></title>
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
				var pattern=/[^0-9a-zA-Z-&:/ ]/;
				var namepattern=/[^0-9a-zA-Z-&:/ ]/;
				if(code.match(pattern)){
				dom.get("category_error").style.display='';
				   document.getElementById("category_error").innerHTML='<s:text name='assetcat.code.alphaNumericwithspecialchar' />'
				    	return;
				}else if(name.match(namepattern) ){
				  dom.get("category_error").style.display='';
				   document.getElementById("category_error").innerHTML='<s:text name='assetcat.name.alphaNumericwithspecialchar' />'
				    	return;
				}else{
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
				    	//alert(document.getElementById('catTemVal').value);
						//alert($F('catAttrTemplate'));
						//$('catAttrTemplate').value = $F('catTemVal').toJSON();
						document.getElementById('catAttrTemplate').value=document.getElementById('catTemVal').value;
				    	enableFields();
				    	document.assetCategoryForm.action='${pageContext.request.contextPath}/assetcategory/assetCategory!save.action';
				    	document.assetCategoryForm.submit();
				    }
				 }
				}
		</script>
		<s:form action="assetCategory" theme="simple" name="assetCategoryForm">
		<s:token/>
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
								<div class="datewk">
									<span class="bold">Today</span>
									<egov:now />
								</div>
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
							onclick="validateFormAndSubmit();" />
						<!--input type="button" class="buttonfinal" value="CANCEL"
							id="cancelButton" name="button"
							onclick="alert('Pressed!');" /-->
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
						<!-- input type="button" class="buttonfinal" value="CANCEL"
							id="cancelButton" name="button"
							onclick="alert('Pressed!');" /-->
						<input type="button" class="buttonfinal" value="CLOSE"
							id="closeButton" name="button"
							onclick="window.close();" />
						<input type="button" class="buttonfinal" value="SEARCH"
							id="backButton" name="button"
							onclick="goBack();" />
						<!-- input type="button" class="buttonfinal" value="NEW"
							id="newButton" name="button"
							onclick="goNewForm();" /-->
					</div>
				</s:elseif>
			</s:push>
		</s:form>
	</body>
</html>
