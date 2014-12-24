<!--
	Program Name : Provident Fund
	Author		: Ilayaraja P
	Created	on	: 16-May-2008
	Purpose 	: 
 -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@include file="/includes/taglibs.jsp" %>
<%@page import="org.egov.payroll.client.providentfund.PFSetupForm,java.text.SimpleDateFormat"%>

<html>
	<head>
		<title>Provident Fund</title>

		<style type="text/css">
				#codescontainer {position:absolute;left:11em;width:9%}
				#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:80%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>
		
			
		<%
			PFSetupForm setupForm = new PFSetupForm();
		%>
		<SCRIPT LANGUAGE="javascript">
			
			var codeObj=null;
			var expCodeObj=null;
			var yuiflag = new Array();
			
			function onLoad()
			{
				var target="<%=(request.getAttribute("alertMessage"))%>";
				onloadDataForLiability();
				onloadDataForExpenses();
								
				if(target!="null")
				{
					alert(target);		
					window.close();
				}
				<%
					Object object = request.getAttribute("PFSetupForm");
					if(object != null)
					{
						setupForm = (PFSetupForm)object;
					}
				%>
				var submitType = "<%=(request.getParameter("submitType"))%>";
				setFieldsForIntExpAccOnLoad();
			}

			function ButtonPress(name)
			{
				var prcolor = document.getElementById('pfAccount').style.backgroundColor;
				if(document.getElementById('pfAccount').value=='')
				{
					document.getElementById('pfAccount').style.backgroundColor = 'red';
					alert('PF Liability account code can not be empty');
					document.getElementById('pfAccount').style.backgroundColor = prcolor;
					document.getElementById('pfAccount').focus();
					return false;
				}

				// comparing date	
				if(document.getElementById('pfIntExpAccount').value!=''){
					var table = document.getElementById('PF_details');
					
					for(var j=0;j<table.rows.length-1;j++)
					{
						var strtDate = document.getElementsByName('dateFrom')[j].value;
						if(strtDate=='')
						{
							document.getElementsByName('dateFrom')[j].style.backgroundColor = 'red';
							alert('DateFrom can not be empty');
							document.getElementsByName('dateFrom')[j].style.backgroundColor = prcolor;
							document.getElementsByName('dateFrom')[j].focus();
							return false;
						}
						var endDate = document.getElementsByName('dateTo')[j].value;
						if(compareDate(strtDate,endDate) == -1 )
						{
							var prevcolor = document.getElementsByName('dateTo')[j].style.backgroundColor;
							document.getElementsByName('dateTo')[j].style.backgroundColor = 'red';
							alert('From Date cannot be greater than To Date');
							document.getElementsByName('dateTo')[j].style.backgroundColor = prevcolor;
							document.getElementsByName('dateTo')[j].select();
							return false;
						}
						if(document.getElementsByName('annualRateOfInterest')[j].value=='')
						{
							document.getElementsByName('annualRateOfInterest')[j].style.backgroundColor = 'red';
							alert('Annual Rate of Interest can not be empty');
							document.getElementsByName('annualRateOfInterest')[j].style.backgroundColor = prcolor;
							document.getElementsByName('annualRateOfInterest')[j].focus();
							return false;
						}
					}
					
					// date overlapping check
					var table = document.getElementById('PF_details');
					var tmpStartDate='',tmpEndDate='',curStartDate='',curEndDate='';
					for(var j=0;j<table.rows.length-1;j++)
					{
						curStartDate = document.getElementsByName('dateFrom')[j].value;
						curEndDate = document.getElementsByName('dateTo')[j].value;
						if(j!=0)
						{
							for(var i=0;i<j;i++)
							{
								var s = document.getElementsByName('dateFrom')[i].value.split('/');
								var e = document.getElementsByName('dateTo')[i].value.split('/');
								var f = curStartDate.split('/');
								
								var dt1= new Date(s[2],s[1]-1,s[0]);
								var dt2= new Date(e[2],e[1]-1,e[0]);
								var dt3= new Date(f[2],f[1]-1,f[0]);
								
								// from date checking
								if(dt3.getTime()>=dt1.getTime() && dt3.getTime() <= dt2.getTime())
								{
									var prevcolor = document.getElementsByName('dateFrom')[j].style.backgroundColor;
									document.getElementsByName('dateFrom')[j].style.backgroundColor = 'red';
									alert('From date is overlapping with other date');
									document.getElementsByName('dateFrom')[j].style.backgroundColor = prevcolor;
									document.getElementsByName('dateFrom')[j].select();
									return false;
								}
								
								// todate checking
								if(curEndDate=='')
									continue;
								var t = curEndDate.split('/');
								var dt4= new Date(t[2],t[1]-1,t[0]);
								
								if(dt4.getTime()>=dt1.getTime() && dt4.getTime() <= dt2.getTime())
								{
									var prevcolor = document.getElementsByName('dateTo')[j].style.backgroundColor;
									document.getElementsByName('dateTo')[j].style.backgroundColor = 'red';
									alert('To date is overlapping with other date');
									document.getElementsByName('dateTo')[j].style.backgroundColor = prevcolor;
									document.getElementsByName('dateTo')[j].select();
									return false;
								}
							} // for loop
						} // if
					} // outer for loop
				}
				if(document.getElementById('id').value=='')
					document.forms[0].action="pfsetup.do?submitType=createNew";
				else
					document.forms[0].action="pfsetup.do?submitType=modify";
				document.forms[0].submit();
			}
			
			function showCalendar(obj,dateObjId)
			{
				var rowobj = getRow(obj);
				var index = parseInt(rowobj.rowIndex)-1;
				show_calendar("getElementsByName('"+dateObjId+"')["+index+"]");
			}

			function onloadDataForLiability(type)
			{
				var url = "${pageContext.request.contextPath}/commons/process.jsp?type=getPFAccountCodes";
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
							acccodeArray=codes.split("+");
							codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
	                  	}
	              	}
			   };
			   req2.open("GET", url, true);
			   req2.send(null);
			}
			
			function onloadDataForExpenses(type)
			{
				var url = "${pageContext.request.contextPath}/commons/process.jsp?type=getPFExpAccountCodes";
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
							acccodeArray=codes.split("+");
							expCodeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
	                  	}
	              	}
			   };
			   req2.open("GET", url, true);
			   req2.send(null);
			}
			
			/*
			* This function returns absolue left and top position of the object
			*/
			function findPos(obj)
			{
				var curleft = curtop = 0;
				if (obj.offsetParent)
				{
					curleft = obj.offsetLeft;
					curtop = obj.offsetTop;
					while (obj = obj.offsetParent)
					{	//alert(obj.nodeName);
						curleft =curleft + obj.offsetLeft;
						curtop =curtop + obj.offsetTop; //alert(curtop);
					}
				}
				return [curleft,curtop];
			}
			
			//based on key input displays the matching list of glcodes
			function autocompletecodeForLiability(obj,event)
			{
				var src = obj;
			  	var target = document.getElementById('codescontainer');
			  	var posSrc=findPos(src);
			  	target.style.left=posSrc[0]+0;
			  	target.style.top=posSrc[1]+5;
			  	target.style.width=500;
			
			  	var currRow=getRow(obj);
			  	var coaCodeObj = obj;
				if(yuiflag[currRow.rowIndex] == undefined)
   				{
					if(event.keyCode != 40 )
					{
						if(event.keyCode != 38 )
						{
							var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
							oAutoComp.queryDelay = 0;
							oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
							oAutoComp.useShadow = true;
						}
					}
					yuiflag[currRow.rowIndex] = 1;
				}
			}
			
			//based on key input displays the matching list of glcodes
			function autocompletecodeForExpenses(obj,event)
			{
				// set position of dropdown
				var src = obj;
				var target = document.getElementById('codescontainer');
				var posSrc=findPos(src);
				target.style.left=posSrc[0];
				target.style.top=posSrc[1]+5;
				target.style.width=450;
				var currRow=getRow(obj);
				var coaCodeObj = obj;
				if(yuiflag[currRow.rowIndex] == undefined)
   				{
					if(event.keyCode != 40 )
					{
						if(event.keyCode != 38 )
						{
							var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', expCodeObj);
							oAutoComp.queryDelay = 0;
							oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
							oAutoComp.useShadow = true;
						}
					}
					yuiflag[currRow.rowIndex] = 1;
				}
			}

			//fills the related neighboor object after splitting
			 function fillNeibrAfterSplit(obj,neibrObjName,neibrObjId)
			 {
				if(obj.value=='')
				{
					document.getElementById(neibrObjName).value='';
					document.getElementById(neibrObjId).value='';
					setFieldsForIntExpAcc();
					return;
				}
			 	var currRow=getRow(obj);
			 	yuiflag[currRow.rowIndex] = undefined;
			 	neibrObj=getControlInBranch(currRow,neibrObjName);
			 	var temp = obj.value;
			 	
			 	temp = temp.split("`-`");
			 	obj.value=temp[0];
			 	if(temp.length>1)
			 	{
			 		document.getElementById(neibrObjName).value=temp[1];
			 		document.getElementById(neibrObjId).value=temp[2];
			 	}
			 	else
			 	{
			 		getIdAndNameAjax(neibrObjName,obj);
			 	}
				setFieldsForIntExpAcc();
			 }
			 
			 function getIdAndNameAjax(neibrObjName,neibrObj)
			 {
			 	var request = initRequest();
			 	var link = "pfSetupAjax.jsp?fieldName="+neibrObjName+"&fieldValue="+neibrObj.value;
			 	request.onreadystatechange = function() 
				{
					if (request.readyState == 4) 
					{
						if (request.status == 200) 
						{
							var response=request.responseText;
							response = trimAll(response);
							if(response=='false')
							{
								//alert('Invalid code');
								document.getElementById(neibrObj.id).value='';
								document.getElementById(neibrObj.id+'Name').value='';
								document.getElementById(neibrObj.id+'Id').value='';
								
							}
							else
							{
								response = response.split('-');
								document.getElementById(neibrObj.id+'Name').value=response[1];
								document.getElementById(neibrObj.id+'Id').value=response[0];
							}							
						}
					}
				};
				request.open("GET", link, true);
				request.send(null);
			 }
			 
			 function setFieldsForIntExpAcc()
			 {
				if(document.getElementById("pfIntExpAccount").value!='')
				 {
					document.getElementById('freqMandateDiv').style.display='inline';
					document.getElementById('freqSelectDiv').style.display='inline';
					document.getElementById('tbl-header3').style.display='inline';	

				 }
				else
				 {
					document.getElementById('freqMandateDiv').style.display='none';
					document.getElementById('freqSelectDiv').style.display='none';
					document.getElementById('tbl-header3').style.display='none';
				 }
			 }

			 function setFieldsForIntExpAccOnLoad()
			 {
				if(document.getElementById("pfIntExpAccount").value!='')
				 {
					document.getElementById('freqMandateDiv').style.display='inline';
					document.getElementById('freqSelectDiv').style.display='inline';
					document.getElementById('tbl-header3').style.display='inline';	

				 }
				else
				 {
					document.getElementById('freqMandateDiv').style.display='none';
					document.getElementById('freqSelectDiv').style.display='none';
					document.getElementById('tbl-header3').style.display='none';	

				 }
			 }


			 function initRequest() 
			 {
				if (window.XMLHttpRequest) 
				{
					return new XMLHttpRequest();
				} 
				else if (window.ActiveXObject) 
				{
					isIE = true;
					return new ActiveXObject("Microsoft.XMLHTTP");
				}
			 }
			 
			 function formCalendarRef(type,rowNo)
			 {
				 if(type=='dateFrom')
				 	show_calendar("getElementById('dateFrom["+rowNo+"]')");
				 else if(type=='dateTo')
				 	show_calendar("getElementById('dateTo["+rowNo+"]')");				 
			 }
			 
			 function addRow()
			 {
				 var tbl=document.getElementById("PF_details");
    			 var rows=tbl.rows;
    			 var tbody=tbl.tBodies[0];
    			 var rowObj = rows[1].cloneNode(true);
    			 tbody.appendChild(rowObj);
    			
				 var rows = parseInt(tbl.rows.length)-2;
				 document.getElementsByName('dateFrom')[rows].value='';
				 document.getElementsByName('dateFrom')[rows].setAttribute("id","dateFrom["+rows+"]");
				 document.getElementsByName('dateFromAnchor')[rows].setAttribute("id","dateFromAnchor["+rows+"]");
				 document.getElementsByName('dateFromAnchor')[rows].setAttribute("href","javascript:show_calendar("+"\"getElementById('dateFrom["+rows+"]')"+"\")");
				 
				 document.getElementsByName('dateTo')[rows].value='';
				 document.getElementsByName('dateTo')[rows].setAttribute("id","dateTo["+rows+"]");
				 document.getElementsByName('dateToAnchor')[rows].setAttribute("id","dateToAnchor["+rows+"]");
				 document.getElementsByName('dateToAnchor')[rows].setAttribute("href","javascript:show_calendar("+"\"getElementById('dateTo["+rows+"]')"+"\")");
				 
				 document.getElementsByName('annualRateOfInterest')[rows].value='';
				 document.getElementsByName('detailId')[rows].value='';
			 }
			 
			 function deleteRow()
			 {
				  var tbl = document.getElementById('PF_details');
				  var lastRow = (tbl.rows.length)-1;
	  			  if(lastRow ==1)
				  {
					 alert("This row can not be deleted");
					 return false;
				  }
				  else
				  {
					  var deletedRowsId = document.getElementById('deletedRowsId').value;
					  if(document.getElementsByName('detailId')[parseInt(lastRow)-1].value!='')
					  {
					  	if(deletedRowsId!='')
					  		deletedRowsId = deletedRowsId+',';
					  	deletedRowsId = deletedRowsId+document.getElementsByName('detailId')[parseInt(lastRow)-1].value;
					  	document.getElementById('deletedRowsId').value = deletedRowsId;
					  }
					  tbl.deleteRow(lastRow);
					  return true;
				  }
			 }

			 function checkNumber(obj)
			 {
			 	if(parseFloat(obj.value)<0 || parseFloat(obj.value)>100)
			 	{
			 		alert('Value should be greater than or equal to zero and less than or equal to 100');
			 		obj.value='';
			 	}
			 }
		</SCRIPT>
	</head>
	
	<body onload="onLoad()">

		<html:form action="/providentfund/pfsetup" >
			<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
									<span class="bold">Today:</span><egovtags:now/>
								</div>	
						
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk">
												<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
											</div>
											<div class="headplacer">Provident Fund Setup</div>
										</td>
									</tr>
									
									<tr>
										<td colspan="4">
											<input type="hidden" name="id" id="id" value="<%=setupForm.getId()%>">
											<input type="hidden" name="deletedRowsId" id="deletedRowsId" value="<%=setupForm.getDeletedRowsId()%>">
										</td>
									</tr>
		
									<tr>
										<td class="whiteboxwk">
											<span class="mandatory">*</span><b>PF Liability Account</b>
										</td>
										
										<td class="whitebox2wk">
											<input class="fieldinput" name="pfAccount" id="pfAccount"
											 value="<%=setupForm.getPfAccount()%>" autocomplete="off"  
											  onkeypress="autocompletecodeForLiability(this,event)" 
											  onblur="fillNeibrAfterSplit(this,'pfAccountName','pfAccountId');" tabindex="1">
										</td>
										<td class="whitebox2wk">
											<input class="fieldinputlarge" name="pfAccountName" id="pfAccountName"
										 		value="<%=setupForm.getPfAccountName()%>" style="width:310;" readonly="true" tabindex="-1">
										 </td>
										<td>
											<input type="hidden" name="pfAccountId" id="pfAccountId" value="<%=setupForm.getPfAccountId()%>">
										</td>
									</tr>
		
									<tr>
										<td class="greyboxwk">
											<b>PF Interest Expense Account</b>
										</td>
										<td class="greybox2wk">
											<input class="fieldinput" name="pfIntExpAccount" id="pfIntExpAccount" 
											 value="<%=setupForm.getPfIntExpAccount()%>" autocomplete="off"  
											 onkeypress="autocompletecodeForExpenses(this,event);" 
											 onblur="fillNeibrAfterSplit(this,'pfIntExpAccountName','pfIntExpAccountId')" tabindex="1">
										</td>
										<td class="greybox2wk">
											<input class="fieldinputlarge" name="pfIntExpAccountName" id="pfIntExpAccountName"
										 	value="<%=setupForm.getPfIntExpAccountName()%>" class="selectwk" style="width:310;"  readonly="true" tabindex="-1">
										 </td>
										<td>
											<input type="hidden" name="pfIntExpAccountId" id="pfIntExpAccountId" 	value="<%=setupForm.getPfIntExpAccountId()%>" tabindex="-1">
										</td>
									</tr>
												
									<div id="codescontainer"></div>
															
									<tr>
										<td class="whiteboxwk">
											<div id="freqMandateDiv" >
												<span class="mandatory">*</span><b>Frequency</b>
											</div>
										</td>
		
										<td class="whitebox2wk" colspan="3">
											<div id="freqSelectDiv" >
												<select class="fieldinput" name="frequency" id="frequency" class="selectwk" >        
													<%
														if(setupForm.getFrequency()!=null && setupForm.getFrequency().equals("Annual"))
														{
															%>
															<option value="Annual">Annual</option>
															<option value="Monthly">Monthly</option>
															<%
														}
														else
														{
															%>
															<option value="Monthly">Monthly</option>
															<option value="Annual">Annual</option>
															<%
														}
													%>
												</select>
											</div>
										</td>
									</tr>
								</table>

								<div class="tbl-header2" id="tbl-header3">
									<center>
										<table width="100%" border="0" cellpadding="3" cellspacing="0" id="dtlHeading">
											<tr>
												<td colspan="4" class="headingwk"><div class="arrowiconwk">
													<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
													<div class="headplacer">PF Interest Rates</div>
												</td>
											</tr>
										</table>
								
										<table  width="95%" border="0" cellpadding="0" cellspacing="0" id="PF_details" name="PF_details">
									        <tr>
												<td class="whitebox2wk">
													<span class="mandatory">*</span>Date From
												</td>
												<td class="whitebox2wk">Date To</td>
												<td class="whitebox2wk">
													<span class="mandatory">*</span>Annual Rate of Interest
												</td>
									        </tr>
				                         		<%
				             					if(setupForm.getDateFrom()!=null && setupForm.getDateFrom().length>0)
				             					{
				             						for(int i=0;i<setupForm.getDateFrom().length;i++)
				             						{
				             					%>
				             							<tr id="row">
						                					<td class="greybox2wk">
						                						<input    name="dateFrom" id="dateFrom[<%=i%>]" value="<%=setupForm.getDateFrom()[i]%>" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" >
						                						<a name="dateFromAnchor" id="dateFromAnchor" href="javascript:formCalendarRef('dateFrom','<%=i%>')"	onmouseover="window.status='Date Picker';return true;" 
																	onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
						                					</td>
						                					<td class="greybox2wk">
						                						<input class="datefieldinput"   name="dateTo" id="dateTo[<%=i%>]" value="<%=setupForm.getDateTo()[i]%>" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" >
						                					 	<a name="dateToAnchor" id="dateToAnchor" href="javascript:formCalendarRef('dateTo','<%=i%>');"	onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
																	<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
															</td>
						                					<td class="greybox2wk" >
						                						<input style="text-align: right" name="annualRateOfInterest" id="annualRateOfInterest" 
						                						value="<%=setupForm.getAnnualRateOfInterest()[i]%>" onblur="checkNumber(this)" >
						                					</td>
						                					<td>
						                						<input type="hidden" name="detailId" id="detailId" 	value="<%=setupForm.getDetailId()[i]%>" >
						                					 </td>
						                					<td>
						                						<input type="hidden" name="pfHeaderId" id="pfHeaderId" value="<%=setupForm.getPfHeaderId()[i]%>">
						                					</td>
						                					<td class="greybox2wk">
						                					  <a href="#">
						                					  	<img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
						                					   	border="0" onclick="addRow();" />
						                					  </a>
															  <a href="#">
															  	<img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
									 							border="0" onclick="deleteRow();" />
									 						  </a>
						                					</td>
						                				</tr>
				             							<%
				             						}
				             					}
				             					else
				             					{
				             						%>
				             							<tr id="row">
				                							<td class="greybox2wk">
				                								<input name="dateFrom" id="dateFrom[0]"  	class="datefieldinput"			                						 		onkeyup="DateFormat(this,this.value,event,false,'3')"  onBlur = "validateDateFormat(this);">
				                								<a name="dateFromAnchor" id="dateFromAnchor[0]" href="javascript:formCalendarRef('dateFrom','0');"
																onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
																	<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
													 		</td>
						                					<td class="greybox2wk">
						                						<input name="dateTo" id="dateTo[0]"  
						                							onkeyup="DateFormat(this,this.value,event,false,'3')" onBlur = "validateDateFormat(this);">
						                					 	<a name="dateToAnchor" id="dateToAnchor[0]" href="javascript:formCalendarRef('dateTo','0');"
																	onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
																	<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
															</td>
						                					<td class="greybox2wk">
						                						<input style="text-align: right"  name="annualRateOfInterest" id="annualRateOfInterest" onblur="checkNumber(this)" class="selectwk">
						                					</td>
						                  					<td><input type="hidden" name="detailId" id="detailId" ></td>
						                					<td><input type="hidden" name="pfHeaderId" id="pfHeaderId" ></td>
						                					<td class="greybox2wk">
						                					  <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
						                					   	border="0" onclick="addRow()" />
						                					  </a>
															  <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
									 							border="0" onclick="deleteRow()" />
									 						  </a>
						                					</td>
						                				</tr>
				             						<%
				             					}
				             				%>
				          				</table>
				          			</center>
				          		</div>
				          	</div>
						</div>
					</div>
				</div>
				
				<div class="buttonholderwk">
					<input type="button" class="buttonfinal" id="saveBut"  value="Save" onclick="return ButtonPress('savenew');" href="#">
					<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()" />
				</div>
				
				<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
				<!--  after submitting the form -->
			</html:form>
		</body>
</html>