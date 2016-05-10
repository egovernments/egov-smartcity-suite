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
	    <title> <s:text name="page.title.asset.create" /></title>
	    </s:if>
	    <s:elseif test="%{userMode=='view'}">
	    <title> <s:text name="page.title.asset.view" /></title>
	    </s:elseif>
	    <s:elseif test="%{userMode=='edit'}">
	    <title> <s:text name="page.title.asset.edit" /></title>
	    </s:elseif>  
	</head> 
	<body id="home" >

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
				for(i=0;i<document.assetForm.elements.length;i++){
				        document.assetForm.elements[i].disabled=false;
				}   
			}
		</script>
		<s:form action="asset" theme="simple" name="assetForm">
		<s:token name="%{tokenName()}"/> 	
		<div class="errorstyle" id="asset_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div>
							</div>
							<div class="rbcontent2">								
								<s:hidden name="id" />
								<s:hidden name="userMode" />
								<%@ include file='asset-form.jsp'%>
							</div>
							<div class="rbbot2">
							<div></div>
							</div>
						</div>
					</div>
				</div>
				<s:if test="%{userMode=='new'}">
					<div class="buttonholderwk" id="divButRow1" name="divButRow1">
						<input type="submit" class="buttonfinal" value="SAVE" id="saveButton" 
							onclick="return validateFormAndSubmit();"/>
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
						<input type="submit" class="buttonfinal" value="SAVE" id="saveButton" 
							onclick="return validateFormAndSubmit();"/>
						<input type="button" class="buttonfinal" value="CLOSE"
							id="closeButton" name="button"
							onclick="window.close();" />
					</div>
				</s:elseif>
			</s:push>
		</s:form>
		<script>
			<s:if test="%{userMode=='view'}">
				links=document.assetForm.getElementsByTagName("a"); 
		        disableLinks(links,[]);
			</s:if>
			<s:if test="%{userMode=='edit'}">
				document.getElementById('grossvalue').readOnly=true;
				//document.getElementById('accdepreciation').readOnly=true;
				<s:if test="%{sourcePath!=null}">
					document.getElementById('assetcat').disabled=true;
					document.getElementById('catTypeIdDummy').disabled=true;
					document.getElementById('status').disabled=true;
				</s:if>
			</s:if>
					
		</script>
	</body>
</html>
