<%@page import="java.util.*, org.egov.lib.admbndry.*"%>
<%@ include file="/includes/taglibs.jsp"%>
<% 
	BoundaryType btObj = (BoundaryType)request.getAttribute("boundryTypeObj");
	List bndryList = (List)request.getAttribute("boundariesList");%>

<c:set var="btObj" value="<%=btObj%>" scope="page" />
<c:set var="bndryList" value="<%=bndryList%>" scope="page" />
<html>
	<head>
		<title>Search Boundary</title>
		<script>
			var a="";
			function createOnlyForRootNode(heirarchyType,boundaryType) {
				var type='createOnlyForRootNode';
				var url = "${pageContext.request.contextPath}/commons/Process.jsp?type="+type+"&heirarchyType="+heirarchyType+"&boundaryType="+boundaryType+" ";
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200) {
					var result=req2.responseText;
					result = result.split("/");
					if(result[0]!= null && result[0]!= "") {
						a=result[0];
					}
				}			
				return a;			
			}
			
			function goTo(arg) {
				if(arg == "view") {
					if(document.getElementById("heirarchyType").options[document.getElementById("heirarchyType").selectedIndex].value =="0") {
		   				alert("Please select the Heirarchy Type!!!");
		   				document.getElementById("heirarchyType").focus();
		   				return false;
	   				}
	    			if(document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="0" || document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="") {
		   				alert("Please select the Boundary Type!!!");
		   				document.getElementById("name").focus();
		   				return false;
					}
					if(document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value =="0" || document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value==""){
		   				alert("Please select the Boundary Value!!!");
		   				document.getElementById("bndryId").focus();
		   				return false;
   					}
   					document.getElementById("heirarchyType").disabled=false;
					document.getElementById("name").disabled=false;
   					var bndryId = document.getElementById("bndryId").value;
			   		var heirarchyType = document.getElementById("heirarchyType").value;
			   		var boundaryType = document.getElementById("name").value;
					var checkForParent = createOnlyForRootNode(heirarchyType,boundaryType);
			   		window.location = "<c:url value='/BndryAdmin/viewBoundary.jsp?heirarchyType="+heirarchyType+"&boundaryType="+boundaryType+"&BoundaryId="+bndryId+"&checkForParent="+checkForParent'/>;
				} else {
					if (document.getElementById("heirarchyType").options[document.getElementById("heirarchyType").selectedIndex].value =="0") {
					   alert("Please select the Heirarchy Type!!!");
					   document.getElementById("heirarchyType").focus();
					   return false;
				   }
				   if (document.getElementById("name").options[document.getElementById("name").selectedIndex].value =="0" ||  document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="") {
					   alert("Please select the Boundary Type!!!");
					   document.getElementById("name").focus();
					   return false;
					}
					if(document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].text !="--Choose--") {
					   alert("Sorry, can't select the Boundary Value for Root Creation!!!");
					   document.getElementById("bndryId").focus();
					   return false;
			   		}
			   		var heirarchyType = document.getElementById("heirarchyType").value;
			   		var boundaryType = document.getElementById("name").value;			
			  		var checking = createOnlyForRootNode(heirarchyType,boundaryType);

					if(checking=='false') {
						alert("Sorry! You can't create root for the Child Boundary");
						return false;
					}
					document.getElementById("heirarchyType").disabled=false;
					document.getElementById("name").disabled=false;
					window.location = "<c:url value='/BndryAdmin/addUpdateDeleteBoundarywithCity.jsp?parentBndryNum=0&topLevelBoundaryID=0&BndryTypeHeirarchyLevel=1&heirarchyType="+heirarchyType+"&boundaryType="+boundaryType+"&operation=create'/>";
				}
			}

			function goToModify() {
				if(document.getElementById("heirarchyType").options[document.getElementById("heirarchyType").selectedIndex].value =="0") {
		   			alert("Please select the heirarchyType!!!");
		   			document.getElementById("heirarchyType").focus();
		   			return false;
	   			}
			    if(document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="0" || document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="") {
				   alert("Please select the Boundary Type!!!");
				   document.getElementById("name").focus();
				   return false;
				}
				if(document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value =="0" || document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value=="") {
				   alert("Please select the Boundary Value!!!");
				   document.getElementById("bndryId").focus();
				   return false;
		   		}
				document.getElementById("heirarchyType").disabled=false;
				document.getElementById("name").disabled=false;
				document.forms[0].action = "<c:url value='/SetupBoundry.do?bool=UPDATE'/>"+"&myBoundaryId="+document.boundryForm.bndryId.value;
				document.forms[0].submit();			
			}

			function goToDelete() {
				if(document.getElementById("heirarchyType").options[document.getElementById("heirarchyType").selectedIndex].value =="0") {
				   alert("Please select the Heirarchy Type!!!");
				   document.getElementById("heirarchyType").focus();
				   return false;
			   }
			    if(document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="0" || document.getElementById("name").options[document.getElementById("name").selectedIndex].value=="") {
				   alert("Please select the Boundary Type!!!");
				   document.getElementById("name").focus();
				   return false;
				}
				if(document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value =="0" || document.getElementById("bndryId").options[document.getElementById("bndryId").selectedIndex].value=="") {
				   alert("Please select the Boundary Value!!!");
				   document.getElementById("bndryId").focus();
				   return false;
		   		}
				document.getElementById("heirarchyType").disabled=false;
				document.getElementById("name").disabled=false;
				document.forms[0].action = "<c:url value='/SetupBoundry.do?bool=DELETE'/>"+"&myBoundaryId="+document.boundryForm.bndryId.value;
				document.forms[0].submit();
			}
			
			function onBodyLoad() {
				document.getElementById("heirarchyType").options[0] = new Option('${btObj.heirarchyType.name}','${btObj.heirarchyType.id}');
				document.getElementById("name").options[0] = new Option('${btObj.name}','${btObj.id}');
			}
	</script>
	</head>
	<body onload="onBodyLoad()">
		<html:form action="/Boundry" >
			<table align="center" width="400px">
				<tr>
					<td class="tableheader" align="center"  height="23" colspan="4"> Search Boundary </td>
				</tr>
				<tr>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="labelcell" width="40%" height="23"> 
						Hierarchy Type<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:select property="heirarchyType" styleId="heirarchyType" disabled="true">
							<html:option value="0">--Choose--</html:option>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelcell" width="40%" height="23">
						Boundary Type<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<select name="name" id="name" disabled="disabled">
							<option value="0">--Choose--</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="labelcell" width="40%" height="23">
						Boundary Values
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:select styleId="bndryId" property="bndryId">
							<html:option value="0">--Choose--</html:option>
							<c:forEach var="obj" items="${bndryList}">
								<html:option value="${obj.id}">${obj.name}</html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td colspan="4" height="25px">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td height="23" class="button2" align="center" colspan="4">					
						<input type=button  value="Edit" onclick="goToModify()" />
						<input type=button  value="Delete" onclick="goToDelete()" />
						<input type="button" value="View" onclick="goTo('view');" />
						<input type="button" value="Create Root" onclick="goTo('create');" />
						<input type="button" value="Back" onclick = "window.location ='BndryAdmin/boundarySearch.jsp'"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>