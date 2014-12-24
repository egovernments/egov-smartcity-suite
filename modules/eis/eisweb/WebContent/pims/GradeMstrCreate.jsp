			<%@ include file="/includes/taglibs.jsp" %>
			<%@ page import="java.util.*,org.egov.pims.model.*,java.text.SimpleDateFormat,org.egov.exceptions.EGOVRuntimeException"%>
			
			
			<html>
			<head>

			<%!

			String mode=null;
			%>
			<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
			<title>Grade Master</title>


			<LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ccMenu.css">
			<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>

			<script>
			var gradeVal ;
			function checkMode()
			{
				gradeVal = document.getElementById("gradeVal").value;
				<% String modeCheck=(String)request.getAttribute("mode");
				%>
					var mode="<%=modeCheck%>"
				var target="<%=(request.getAttribute("alertMessage"))%>";
				if(target=="null" && mode!='view')
				{
					var target="<%=(session.getAttribute("alertMessage"))%>";
						if(target!="null" && mode!='view')
						{
								alert("<%=session.getAttribute("alertMessage")%>");
							
						}
				}

				else if(target!="null")
				{
						alert("<%=request.getAttribute("alertMessage")%>");
						/*document.gradeMstrForm.gradeVal.value="";
						document.gradeMstrForm.startDate.value="";
						document.gradeMstrForm.endDate.value="";
						document.gradeMstrForm.gradeAge.value="";*/
				}
				<%session.removeAttribute("alertMessage");%>

			}
			function uniqueGradeMaster()
			{
				
				if(gradeVal!=document.getElementById("gradeVal").value)
				{
					uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EGEIS_GRADE_MSTR', 'GRADE_VALUE', 'gradeVal', 'no', 'no');
				}

			}
			function validateAge(obj)
			{
				var age=obj.value;
				if(age>65)
				{
					alert('Entered age should be less than 65');
					obj.value="";
					obj.focus();

				}
			}
			
		function checkNumericForOrderNo(obj)
		{

			if(obj.value!="")
			{
				var num=obj.value;
				var objRegExp  = /^[0-9]+$/;
				if(!objRegExp.test(num))
				{
					alert('Please enter only numbers');
					obj.value="";
					obj.focus();
				}
			}	
		}
			
		function checkNumericForAge(obj)
		{

		if(obj.value!="")
		{
		var num=obj.value;
		var objRegExp  = /^[0-9]+$/;
		if(!objRegExp.test(num))
		{
		alert('Please fill the proper Age');
		obj.value="";
		obj.focus();
		}
		}
		}

		//Function to validate Start date > end date
		function CompareDate(obj)
		{
			if(document.gradeMstrForm.endDate.value !="")
			{
				if(compareDate(document.gradeMstrForm.endDate.value,document.gradeMstrForm.startDate.value) == 1||compareDate(document.gradeMstrForm.endDate.value,document.gradeMstrForm.startDate.value)==0)
					{

							alert('End Date should be greater than Start Date');
							document.gradeMstrForm.startDate.value="";
							document.gradeMstrForm.endDate.value="";
							document.gradeMstrForm.startDate.focus();
							return false;
					}
			}
		}

          function ButtonPress(arg)
			{
				
					var submitType="";
					if(document.gradeMstrForm.gradeVal.value == "" )
					{
						alert('<bean:message key="alertFillNme"/>');
						document.gradeMstrForm.gradeVal.focus();
						return false;
					}
					if(document.gradeMstrForm.startDate.value == "" )
					{
						alert('<bean:message key="alertEnterFromDate"/>');
						document.gradeMstrForm.startDate.focus();
						return false;
					}
					if(document.gradeMstrForm.endDate.value == "" )
					{
						alert('<bean:message key="alertFillToDate"/>');
						document.gradeMstrForm.endDate.focus();
						return false;
					}
					if(document.gradeMstrForm.gradeAge.value == "" )
					{
						alert('Please enter Age');
						document.gradeMstrForm.gradeAge.focus();
						return false;
					}
					if(document.gradeMstrForm.orderNo.value=="")
					{
						alert('Please enter Order Number ');
						document.gradeMstrForm.orderNo.focus();
						return false;
					}	
				if(arg == "savenew")
				{
					document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/AfterGradeMasterAction.do?submitType=createGradeMaster";
					document.gradeMstrForm.submit();
				}
				if(arg == "modify")
				{
					document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/AfterGradeMasterAction.do?submitType=afterModify";
					document.gradeMstrForm.submit();
				}
			}

			function goto(arg)
			{
				
				if(arg == "view")
				{
					
				document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/BeforeGradeMasterAction.do?submitType=beforeView";
				document.gradeMstrForm.submit();
				}
				else if(arg == "modify")
				{
					document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/BeforeGradeMasterAction.do?submitType=beforeModify";
					document.gradeMstrForm.submit();
				}
				else if(arg=="delete")
				{
					document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/BeforeGradeMasterAction.do?submitType=beforeDelete";
					document.gradeMstrForm.submit();
				}

			}

			function changeDelete(arg)
			{
				if(arg == "delete")
				{
					document.gradeMstrForm.action = "${pageContext.request.contextPath}/pims/AfterGradeMasterAction.do?submitType=afterDelete";
					document.gradeMstrForm.submit();
				}
			}
			
			</script>

			</head>

			<body onload="checkMode();">
			
		<table align="center" id="table3" width="100%" cellpadding ="0" cellspacing ="0" border = "0">
		<tr><td>
		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
		
		<html:form action="/pims/AfterGradeMasterAction" >
			<%
				GradeMaster gradeDet=null;
				if(request.getAttribute("gradeMstrDet")!=null)
				{
					gradeDet =(GradeMaster)request.getAttribute("gradeMstrDet");
				}
				else
				{
					gradeDet = new GradeMaster();
				}
			   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    mode=(String)request.getAttribute("mode");
				if(mode==null)
				{
					mode=(String)session.getAttribute("mode");
				}
				
			%>
				
			<table WIDTH=95% colspan="7" cellpadding ="0" cellspacing ="0" border = "0">
			<tr><td>

				
				<input type=hidden name="id" id="id" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">



					<input type = hidden name="viewMode" id="viewMode" value="mode" />
					<input type = hidden name="gradeId" id="gradeId" value="<%=gradeDet.getId()==null?"":gradeDet.getId().toString()%>" />
					<tr><td>&nbsp;</td></tr>
					<tr>

					    <td colspan="8" class="headingwk"><div class="arrowiconwk">
						<img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
						<div class="headplacer">Grade Master</div></td>
						</tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td class="whiteboxwk" align='center'><span class="mandatory">*</span>Grade Name</td>
					<td class="whitebox2wk"  align="center"><input type="text"  class="selectwk" id="gradeVal" 
					value="<%=gradeDet.getName()==null?"":gradeDet.getName().toString()%>" name="gradeVal" onblur="uniqueGradeMaster();"/></td>
					 </tr>
					<tr id = "fromDateRow"><td class="greyboxwk"><span class="mandatory">*</span>Start Date</td>
					<td class="greybox2wk" align="center"><input type="text"  class="selectwk" id="startDate" value="<%=gradeDet.getFromDate()==null?"":sdf.format(gradeDet.getFromDate())%>"name="startDate" onBlur = "validateDateFormat(this);CompareDate(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
					</tr>
					<tr id = "toDateRow"><td class="whiteboxwk"><span class="mandatory">*</span>End Date</td>
					<td class="whitebox2wk" align="center"><input type="text"  class="selectwk" id="endDate" value="<%=gradeDet.getToDate()==null?"":sdf.format(gradeDet.getToDate())%>" name="endDate" onBlur = "validateDateFormat(this);CompareDate(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
					</tr>

					<tr id = "toDateRow"><td class="greyboxwk"><span class="mandatory">*</span>Age</td>
					<td class="greybox2wk" align="center"><input type="text"  class="selectwk" id="gradeAge" value="<%=gradeDet.getAge()==null?"":gradeDet.getAge().toString() %>" name="gradeAge" onBlur="checkNumericForAge(this),validateAge(this)"/></td>
					</tr>
					
					<tr ><td class="whiteboxwk"><span class="mandatory">*</span>OrderNo</td>
					<td class="whitebox2wk" align="center"><input type="text" align="right" class="selectwk" id="orderNo" value="<%=gradeDet.getOrderNo()==null?"":gradeDet.getOrderNo().toString() %>" name="orderNo" maxlength="3" size="1" onBlur="checkNumericForOrderNo(this);"/></td>
					</tr>
					
					<tr><td colspan=2>&nbsp;</td></tr>
					<tr>
            		<td colspan="2"><div align="right" class="mandatory">* Mandatory Fields</div></td>
          			</tr>
          			
			</table>
			</td></tr></table>
			
		</html:form>
		
		</div>
			<div class="rbbot2"><div></div></div>
		</div>
		
		</td></tr>
		<tr><td>
			<table align='center' name="buttonTable" id="buttonTable">
				<tr>
			
					<%
					 
					if(!(mode.trim().equals("view")) && !(mode.trim().equals("modify")) && !(mode.trim().equals("delete")) )
					{
					%>
					
					
					<td><html:button styleClass="buttonfinal" value="Create" property="b4" onclick="ButtonPress('savenew')" /></td>
		
					<td><html:button styleClass="buttonfinal" value="Modify" property="b4" onclick="goto('modify')" /></td>
					<td><html:button styleClass="buttonfinal" value="View" property="b4" onclick="goto('view')" /></td>
					<td><html:button styleClass="buttonfinal" value="Delete" property="b4" onclick="goto('delete')" /></td>
					<%
					}if(mode.trim().equals("modify")){
					%>
		
					<td><html:button styleClass="buttonfinal" value="Modify" property="b4" onclick="ButtonPress('modify')"/></td>
					<%}else if(mode.trim().equals("delete")){%>
					<td><html:button styleClass="buttonfinal" value="Delete" property="b4" onclick="changeDelete('delete')"/></td>
					<%}%>
			   <tr>
		   	</table>
			
			
			</td></tr>
			</table>
		
			</body>
			</html>
