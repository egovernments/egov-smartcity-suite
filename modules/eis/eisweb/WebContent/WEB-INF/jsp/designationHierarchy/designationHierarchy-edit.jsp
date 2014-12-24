<!--
	Program Name : designationHierarchy-edit.jsp
	Author		: Jagadeesan M
	Created	on	: 06-04-2010
	Purpose 	: To modify designation hierarchy.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
  	<head>
	  	<title>Modify Designation Hierarchy</title>
	  	<style type="text/css">
			.codescontainer {position:absolute;left:11em;width:9%}
			.codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid	#404040;background:#fff;overflow:hidden;z-index:9050;}
			.codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
			.codescontainer ul {padding:5px 0;width:80%;}
			.codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			.codescontainer li.yui-ac-highlight {background:#ff0;}
			.codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>
		
	  	<script language="JavaScript"  type="text/JavaScript">
	  	
	  	var yuiflag1 = new Array();
	  	var yuiflag2 = new Array();
	  			
		var desigDtlArray;
		var desigDtlDataSource;
		
		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
		}
		
		function checkDuplicate(obj)
		{
			var tbl=document.getElementById("desigDtl");
			var rowCount = parseInt(tbl.rows.length)-2;
			var rowIndex = getRow(obj).rowIndex;
			var currentRow=rowIndex-1;
			
			for(var i=0;i<=rowCount;i++)
			{
				if(i!=currentRow)//for current row, no need to check
				{
					if(obj.value== document.getElementsByName('fromDesigSearch')[i].value)
					{
						alert("From Designation should not be duplicate");
						document.getElementsByName('fromDesigSearch')[currentRow].value='';
						document.getElementsByName('fromDesig')[currentRow].value='';
						break;
					}
				}
			}
		}
		
		function onSubmit()
	  	{
			//For checking mandatory field with empty value
			var tbl=document.getElementById("desigDtl");
			var rowCount = parseInt(tbl.rows.length)-2;

			for(var i=0;i<=rowCount&&rowCount!=0;i++)
			{
				if(document.getElementsByName('fromDesigSearch')[i].value=='')
				{
					alert("From Designation should not be Empty");
					document.getElementsByName('fromDesigSearch')[i].focus();
					return false;
				}
				
				if(document.getElementsByName('toDesigSearch')[i].value=='')
				{
					alert("To Designation should not be Empty");
					document.getElementsByName('toDesigSearch')[i].focus();
					return false;
				}
			}
			
			if(document.getElementById("departmentIds").length!=0)
			{
				var deptIdsLength = document.getElementById("departmentIds").length;			
				for (var i = 0; i<deptIdsLength; i++){				    
					document.getElementById("departmentIds").options[i].selected=true;
				}
			}
			document.getElementById("objectType").disabled=false;
			document.getElementById("departmentIds").disabled=false;
			return true;
	  	}
	  	
 		function addRow()
		{
			var tbl=document.getElementById("desigDtl");
   			var tbody=tbl.tBodies[0];
			var lastRowIndex = parseInt(tbl.rows.length)-1;
			var curTblLen=tbl.rows.length;
			var tblRowCell= tbl.rows[lastRowIndex].cells.length; 
		 	var row = tbl.insertRow(curTblLen);
		 	var rowObj=null;

			for(var i=0;i<tblRowCell;i++)
			{
				if(i==0)
				{
					var cell0 = document.createElement("TD");
					cell0.innerHTML = '<s:hidden  id="desigHierarchyId" name="desigHierarchyId" value=""/>';
					cell0.className='whitebox2wk';
					tbl.rows[curTblLen].appendChild(cell0);	  
				}
				else if(i==1)
			 	{
			 		var cell1 = document.createElement("TD");
					cell1.innerHTML = ' <input type="text" id="fromDesigSearch'+lastRowIndex+'" name="fromDesigSearch" value="" autocomplete="off"  onkeypress="autocompleteFromDesig(this,event,\'fromDesigContainer'+lastRowIndex+'\')" '+
				   					  ' onblur="fillNeibrFromDesig(this,\'fromDesigIds\');checkduplicate(\'desigDtl\',this,this.value,\'From Designation\',\'1\',\'Yes\',\'text\'); " />'+
				   					  ' <s:hidden id="fromDesigIds" name="fromDesigIds" value=""/>'+
				   					  ' <div class="codescontainer" id="fromDesigContainer'+lastRowIndex+'"></div> ';
					cell1.className='whitebox2wk';
					tbl.rows[curTblLen].appendChild(cell1);	  
			 	}
			 	else if(i==2)
			 	{
			 		var cell2 = document.createElement("TD");
					cell2.innerHTML = ' <input type="text" id="toDesigSearch'+lastRowIndex+'" name="toDesigSearch" value="" autocomplete="off" onkeypress="autocompleteToDesig(this,event,\'toDesigContainer'+lastRowIndex+'\');" '+
				   					  ' onblur="fillNeibrToDesig(this,\'toDesigIds\') " />'+
				   					  ' <s:hidden id="toDesigIds" name="toDesigIds" value=""/>'+
				   					  ' <div class="codescontainer" id="toDesigContainer'+lastRowIndex+'"></div> ';
					cell2.className='whitebox2wk';
					tbl.rows[curTblLen].appendChild(cell2);	  
			 	}
			 	else if(i==3)
			  	{
			  		rowObj=tbl.rows[1].cells[i].cloneNode(true);
			  		rowObj.className='whitebox2wk';	
			 	 	tbl.rows[curTblLen].appendChild(rowObj);	  
			 	}
			}
		}
		 
		function deleteRow()
		{
			var tbl = document.getElementById('desigDtl');
			var lastRow = (tbl.rows.length)-1;

  			if(lastRow ==1)
			{
				if(confirm("Are you sure want to delete default designation heirarchy for selected object Type."))
				{
					document.getElementsByName('fromDesigSearch')[lastRow-1].value='';
					document.getElementsByName('fromDesigIds')[lastRow-1].value='';
					document.getElementsByName('toDesigSearch')[lastRow-1].value='';
					document.getElementsByName('toDesigIds')[lastRow-1].value='';
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if(document.getElementsByName('desigHierarchyId')[lastRow-1].value!="")
				{
				  	if(document.getElementById('desigHierarchyDelIds').value==''){
				  		document.getElementById('desigHierarchyDelIds').value = document.getElementsByName('desigHierarchyId')[parseInt(lastRow-1)].value;
				  	}
				  	else if(document.getElementById('desigHierarchyDelIds').value!=''){
				  		document.getElementById('desigHierarchyDelIds').value = document.getElementById('desigHierarchyDelIds').value+'@'+document.getElementsByName('desigHierarchyId')[parseInt(lastRow-1)].value;
				  	}
				}
				tbl.deleteRow(lastRow);
				yuiflag1[lastRow] = undefined;
				yuiflag2[lastRow] = undefined;
				return true;
			}
		}

		function loadEmpDesignation() { 
			var type='getAllDesignation';
			var url = "${pageContext.request.contextPath}/pims/designationHierarchyAjax.jsp?type="+type;
			var req2 = initiateRequest();
			req2.onreadystatechange = function()
			{
				if (req2.readyState == 4)
				{
					if (req2.status == 200)
					{
						var codes2=req2.responseText;
						var a = codes2.split("^");
						var codes = a[0];
						desigDtlArray=codes.split("+");
						desigDtlDataSource = new YAHOO.widget.DS_JSArray(desigDtlArray);
					}
				}
			};
			req2.open("GET", url, true);
			req2.send(null);
		}
		
		function autocompleteFromDesig(obj,event,container)
		{
		 	// set position of dropdown
		 	var src = obj;
		 	var target = document.getElementById(container);
		 	var posSrc=findPos(src);
		 	target.style.left=posSrc[0];
		 	target.style.top=posSrc[1]+25;
		 	target.style.left=posSrc[0]+0;
		
		 	target.style.width=400;
		
		 	var currRow=getRow(obj);
		 	var desigObj = obj;
		 	if(yuiflag1[currRow.rowIndex] == undefined)
		 	{
			 	//40 --> Down arrow, 38 --> Up arrow
			 	if(event.keyCode != 40 )
			 	{
			 		if(event.keyCode != 38 )
			 		{
		 				var oAutoComp1 = new YAHOO.widget.AutoComplete(desigObj,container, desigDtlDataSource);
		 				oAutoComp1.queryDelay = 0;
		 				oAutoComp1.useShadow = true;
		 				oAutoComp1.maxResultsDisplayed = 15;
		   				oAutoComp1.useIFrame = true;
			 		}
			 	}
			 	yuiflag1[currRow.rowIndex]=1;
		  	}
		}
		
		function autocompleteToDesig(obj,event,container)
		{
		 	// set position of dropdown
		 	var src = obj;
		 	var target = document.getElementById(container);
		 	var posSrc=findPos(src);
		 	target.style.left=posSrc[0];
		 	target.style.top=posSrc[1]+25;
		 	target.style.left=posSrc[0]+0;
		
		 	target.style.width=400;
		
		 	var currRow=getRow(obj);
		 	var desigObj = obj;
		 	if(yuiflag2[currRow.rowIndex] == undefined)
		 	{
			 	//40 --> Down arrow, 38 --> Up arrow
			 	if(event.keyCode != 40 )
			 	{
			 		if(event.keyCode != 38 )
			 		{
		 				var oAutoComp2 = new YAHOO.widget.AutoComplete(desigObj,container, desigDtlDataSource);
		 				oAutoComp2.queryDelay = 0;
		 				oAutoComp2.useShadow = true;
		 				oAutoComp2.maxResultsDisplayed = 15;
		   				oAutoComp2.useIFrame = true;
			 		}
			 	}
			 	yuiflag2[currRow.rowIndex]=1;
		  	}
		}
		
		
		function fillNeibrFromDesig(obj,neibrObjName)
		{
			markYuiflagUndefined(1);
			var currRow=getRow(obj);
			neibrObj1=getControlInBranch(currRow,neibrObjName);
			var temp = obj.value;
			temp = temp.split("`-`");

			var xyz = getControlInBranch(currRow,'fromDesigSearch');
			xyz.value=temp[0];
		
			if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) 
			{  
				return ;
			}
			else 
			{
				neibrObj1.value=temp[1];
			}
		}
		
		function fillNeibrToDesig(obj,neibrObjName)
		{
			markYuiflagUndefined(1);
			var currRow=getRow(obj);
			neibrObj2=getControlInBranch(currRow,neibrObjName);
			var temp = obj.value;
			temp = temp.split("`-`");

			var xyz = getControlInBranch(currRow,'toDesigSearch');
			xyz.value=temp[0];
		
			if(temp[1]==null && (neibrObj2.value!='' || neibrObj2.value!=null) ) 
			{  
				return ;
			}
			else 
			{
				neibrObj2.value=temp[1];
			}
		}
		
		function loadInit()
		{
			document.getElementById("desigHierarchyDelIds").value="";
		}
 		
	</script>
   
  </head>
  
  <body onload="loadEmpDesignation();loadInit();">
  
   	<s:form action="designationHierarchy" theme="simple" onsubmit="return onSubmit()">  
   		<s:token/>
  		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
			  		<div class="rbcontent2">
			  			<span id="msg"  style="height:1px">
							<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
							<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
							<s:actionmessage cssClass="actionmessage"/>
						</span>

			  			<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
	  						<tbody>
									<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="desigHeader" name="desigHeader">
										<tr>
											<td colspan="4" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								              	<div class="headplacer"><s:text name="DesignationHierarchy.Heading"/></div>
								        	</td>
								        </tr>
										
										<s:push value="model">
										<tr>
											<td class="whiteboxwk" ><s:text name="DesignationHierarchy.ObjectType"/> </td>
											<td class="whitebox2wk" colspan="3">
											<s:select name="objectType" id="objectType" list="dropdownData.objectTypeList" disabled="true" listKey="id" 
											listValue="type" headerKey="-1" headerValue="----Select----" value="%{objectType.id}"/> </td>
										</tr>
										</s:push>
										<tr>
											<td  class="greyboxwk"><s:text name="DesignationHierarchy.Department"/> </td>
											<s:if test="#attr.departmentIds[0]!=-1">
												<td class="greybox2wk">
													<s:select name="departmentIds" id="departmentIds" multiple="true" disabled="true" list="dropdownData.departmentList" listKey="id" 
													listValue="deptName"  size="4" cssStyle="width:200px" />
												</td>
											</s:if>
											<s:else>
												<td class="greybox2wk">
													<select name="departmentIds" id="departmentIds" multiple="true" readonly="true" size="4" style="width:200px" ></select>
												</td>
											</s:else>
							
										</tr>
									</table>
									
									<table  width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td colspan="4" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								              	<div class="headplacer"><s:text name="DesignationHierarchy.DtlHeading"/></div>
								        	</td>
								        </tr>
								         <tr>
								        	<td colspan="4">
								        		<s:hidden name="desigHierarchyDelIds"  id="desigHierarchyDelIds" value=""/>
								        	</td>
								        </tr>
									</table>
									
									<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="desigDtl" name="desigDtl">
										<tr>
											<td class="greybox2wk"/>
											<td class="greybox2wk">
												<span class="mandatory">*</span><s:text name="DesignationHierarchy.FromDesig"/>
											</td>
											<td class="greybox2wk">
												<span class="mandatory">*</span><s:text name="DesignationHierarchy.ToDesig"/>
											</td>
											<td class="greybox2wk"/>
								        </tr>
								        								       
							        	<s:if test="!designationHierarchyList.isEmpty()">
								        	<s:iterator status="status" value="designationHierarchyList">
									        	<tr>
													<td><s:hidden name="desigHierarchyId" id="desigHierarchyId" value="%{designationHierarchyList[#status.index].id}"/></td>
													<td class="whitebox2wk">
														 <s:textfield id="fromDesigSearch%{#status.index}"  name="fromDesigSearch" value="%{designationHierarchyList[#status.index].fromDesig.designationName}" autocomplete="off" onkeypress="autocompleteFromDesig(this,event,'fromDesigContainer%{#status.index}');" 
															onblur="fillNeibrFromDesig(this,'fromDesigIds');checkduplicate('desigDtl',this,this.value,'From Designation','1','Yes','text');"/>
														 <s:hidden id="fromDesigIds" name="fromDesigIds" value="%{designationHierarchyList[#status.index].fromDesig.designationId}"/>
														 <s:div id="fromDesigContainer%{#status.index}" cssClass="codescontainer" ></s:div>
													</td>
													
													<td class="whitebox2wk">
														<s:textfield id="toDesigSearch%{#status.index}"  name="toDesigSearch" value="%{designationHierarchyList[#status.index].toDesig.designationName}" autocomplete="off" onkeypress="autocompleteToDesig(this,event,'toDesigContainer%{#status.index}');" 
															onblur="fillNeibrToDesig(this,'toDesigIds');"/>
														<s:hidden id="toDesigIds" name="toDesigIds" value="%{designationHierarchyList[#status.index].toDesig.designationId}"/>
														<s:div id="toDesigContainer%{#status.index}" cssClass="codescontainer" ></s:div>
													</td>
													<td class="whitebox2wk" width="40%">
				                					  <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="addRow()" /></a>
													  <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteRow()" /></a>
				                					</td>
												</tr>
											</s:iterator>
								        </s:if>
									</table>
								
									<tr>
					               		<td colspan="4" class="shadowwk"></td>
					            	</tr>
								
									<tr>
								 		<td colspan="4" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
					            
					           		<tr>
							       		<td align="center" colspan="4"> 
											<s:hidden  name="mode" value="Modify" />
								   		</td>
					           		</tr>
						      </tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<s:submit method="edit" value="Modify" cssClass="buttonfinal"/>
			<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	