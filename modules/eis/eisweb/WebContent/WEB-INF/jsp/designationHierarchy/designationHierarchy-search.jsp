<!--
	Program Name : designationHierarchy-search.jsp
	Author		: Jagadeesan M
	Created	on	: 06-04-2010
	Purpose 	: To create a Designation Hierarchy.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
  <head>
	  
	  <s:if test="%{#attr.mode=='Create'}">
	  <title>Create Designation Hierarchy</title>
	  </s:if>
	  <s:if test="%{#attr.mode=='Modify'}">
	  <title>Modify Designation Hierarchy</title>
	  </s:if>
	  <s:if test="%{#attr.mode=='View'}">
	  <title>View Designation Hierarchy</title>
	  </s:if>
	  
	  <script type="text/javascript">

	  	function resetForm()
		{
	  		if('<s:property value="#attr.mode"/>'=='Create')
	  		{
		  		if(document.getElementById("departmentIds").length!=0)
				{
					var deptIdsLength = document.getElementById("departmentIds").length;			
					for (var i = 0; i<deptIdsLength; i++){				    
						document.getElementById("departmentIds").options[i].selected=true;
					}
					move(document.getElementById('right'));
				}
			}
			
			document.getElementById('objectType').value ='-1';
			document.getElementById('msg').innerHTML ='';
		}
	  
	  	function onSubmit()
	  	{
			if('<s:property value="#attr.mode"/>'=='Create')
			{
				if(document.getElementById("departmentIds").length!=0)
				{
					var deptIdsLength = document.getElementById("departmentIds").length;			
					for (var i = 0; i<deptIdsLength; i++){				    
						document.getElementById("departmentIds").options[i].selected=true;
					}
				}
			}
			document.getElementById("objectType").disabled=false;
			
			return true;
	  	}
	  	
	  	function isDesigHirAlreadyCreatedForDeptAndObjType()
		{
			var departmentGroup="";
			for(var j=0;j<document.getElementById("departmentIdLeft").length;j++)
			{
				if(document.getElementById("departmentIdLeft").options[j].selected){
					if(departmentGroup=="")
						departmentGroup = document.getElementById("departmentIdLeft").options[j].value;
					else
						departmentGroup = departmentGroup+","+document.getElementById("departmentIdLeft").options[j].value;
				}
			}
			
			var objType=document.getElementById("objectType").value;
			var url = "${pageContext.request.contextPath}/pims/designationHierarchyAjax.jsp?type=isDesigHirAlreadyCreatedForDeptAndObjType&departmentGroup="+departmentGroup+"&objType="+objType;
			var req = initiateRequest();
		
			req.onreadystatechange = function()
			{
			     if (req.readyState == 4)
			     {
				     strResponse = req.responseText;
				 	 if (req.status == 200)
				 	 {
				        var codes=trimAll(req.responseText);
	
				        var deptGrpIds='';
				        var deptGrpNames='';
				       
				        if(codes!="")
				        {
					        var deptGrpIdNameArr= codes.split(",");
					        for(z=0;z< deptGrpIdNameArr.length;z++)
					        {
					        	if(deptGrpIds=="")
					        	{
					        		deptGrpIds = deptGrpIdNameArr[z].split('#')[0];
					        		deptGrpNames =deptGrpIdNameArr[z].split('#')[1];
					        		
					        	}
					        	else
					        	{
					        		deptGrpIds = deptGrpIds+","+deptGrpIdNameArr[z].split('#')[0];
					        		deptGrpNames = deptGrpNames+","+deptGrpIdNameArr[z].split('#')[1];
					        		
					        	}
					        }	
					        
					        alert("Designation hierarchy already defined for these Departments ("+deptGrpNames+"). So, It cannot be added again.");

					    }
				        
				        var left = document.getElementById("departmentIdLeft");  
						var right = document.getElementById("departmentIds"); 
						var from, to;  
						var bAll = false; 
						
						from = left; 
						to = right;   
						
						for (var i = from.length - 1; i >= 0; i--)  
						{    
							var o = from.options[i];
  
							if (o.selected)    
							{      
								if(deptGrpIds!="")
								{
									var existDeptGrpIds= deptGrpIds.split(',');
									var isExist=false;
									for(var j=0;j<existDeptGrpIds.length;j++)
									{
										if(o.value==existDeptGrpIds[j])
										{
											isExist=true;
										}
									}
								}
								
								if(!isExist){
									
									
									try      
									{        
										var clone = o.cloneNode(true);
										to.appendChild(clone);
									    //Standard method, fails in IE (6&7 at least)      
									}      
									catch (e)  
									{ 
									 to.add(o); 
									// IE only  
									} 
									from.remove(i);    
								}    
							}  
						}
				       	
					 }
			     }
			 }//close req.onreadystatechange
		 
		 	req.open("GET", url, true);
		 	req.send(null);
		
		}
		
	  	function move(inputControl)
		{  
			
			if(document.getElementById("objectType").value=="-1")
			{
				alert("Please select Object Type");
				return false;
			}
			
			if(inputControl.value=='>')
			{
				isDesigHirAlreadyCreatedForDeptAndObjType();
			}
			else if(inputControl.value=='<')
			{
				var left = document.getElementById("departmentIdLeft");  
				var right = document.getElementById("departmentIds"); 
				var from, to;  
				var bAll = false; 
				
				switch (inputControl.value)  
				{  
					case '<':    
					from = right; 
					to = left;    
					break;  
					case '>':    
					from = left; 
					to = right;    
					break;  
				}  
				
				for (var i = from.length - 1; i >= 0; i--)  
				{    
					var o = from.options[i];    
					if (o.selected)    
					{      
						try      
						{        
							var clone = o.cloneNode(true);
							to.appendChild(clone);
						    //Standard method, fails in IE (6&7 at least)      
						}      
						catch (e)  
						{ 
						 to.add(o); 
						// IE only  
						} 
						from.remove(i);        
					}  
				}
			}
		}
	  </script>
   
  </head>
  
  <body>
  
   	<s:form action="designationHierarchy" theme="simple" onsubmit="onSubmit()">  
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

			  			<table width="96%" cellpadding ="0" cellspacing ="0" border = "0">
	  						<tbody>
  								    
  								<s:push value="model">
									<tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							              	<div class="headplacer"><s:text name="DesignationHierarchy.Heading"/></div>
							            </td>
							        </tr>
								
									<tr>
										<td class="whiteboxwk" ><span class="mandatory">*</span><s:text name="DesignationHierarchy.ObjectType"/> </td>
										<td class="whitebox2wk">
										<s:select name="objectType" id="objectType" list="dropdownData.objectTypeList" listKey="id" 
										listValue="type" headerKey="-1" headerValue="----Select----"  value="%{objectType.id}"/> </td>
									</tr>
								</s:push>	
							
								<s:if test="%{#attr.mode=='Create'}" >
									<tr>
										<td  class="greyboxwk"><s:text name="DesignationHierarchy.Department"/> </td>
							   			<td class="greybox2wk">
											<s:select name="departmentIdLeft" id="departmentIdLeft" multiple="true" list="dropdownData.departmentListForLeftSelect" listKey="id" 
											listValue="deptName" value="%{departmentListForLeftSelect.id}" size="4" cssStyle="width:200px" />
										</td>
										<td align="center" class="greybox2wk" >				    		
											<input type="button" value='&gt;' style="width:50px" class="button" id="left" onclick="move(this);"/>
											<br>
											<s:textfield value="" cssStyle="width:50px;height:5px;border:0px" readonly="true" />
											<br>
											<input type="button" value='&lt' style="width:50px" class="button" id="right" onclick="move(this);"/>
										</td>
										<td class="greybox2wk">
											<select name="departmentIds" id="departmentIds" multiple="true" size="4" style="width:200px" ></select>
										</td>
									</tr>    
								</s:if>
									
								<s:if test="%{#attr.mode=='Modify'}">
									<td  class="greyboxwk"><s:text name="DesignationHierarchy.Department"/> </td>
						   			<td class="greybox2wk">
										<s:select name="departmentIds" id="departmentIds" list="dropdownData.departmentListForLeftSelect" listKey="id" headerKey="-1" headerValue="---Select---" 
										listValue="deptName" value="%{departmentListForLeftSelect.id}" />
									</td>
								</s:if>
								
								<s:if test="%{#attr.mode=='View'}">
									<td  class="greyboxwk"><s:text name="DesignationHierarchy.Department"/> </td>
						   			<td class="greybox2wk">
										<s:select name="departmentIds" id="departmentIds" list="dropdownData.departmentListForLeftSelect" multiple="true" size="4" listKey="id" 
										listValue="deptName" value="%{departmentListForLeftSelect.id}" />
									</td>
								</s:if>
								
								<tr>
					               	<td colspan="4" class="shadowwk"></td>
					            </tr>
								
								<tr>
								 	<td colspan="4" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
								</tr>

					      </tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			 <s:if test="%{#attr.mode=='Create'}">
	  			<s:submit method="loadToCreate" value="Submit" cssClass="buttonfinal"/>
	  		</s:if>
	  		<s:if test="%{#attr.mode=='Modify'}">
	 			<s:submit method="loadToModify" value="Submit" cssClass="buttonfinal"/>
	  		</s:if>
	  		<s:if test="%{#attr.mode=='View'}">
	 			<s:submit method="loadToView" value="View" cssClass="buttonfinal"/>
	  		</s:if>
			<s:reset name="button" id="button" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/>
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	