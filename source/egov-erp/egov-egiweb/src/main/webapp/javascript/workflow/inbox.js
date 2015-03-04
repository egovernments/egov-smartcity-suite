var egovInbox = null;
var windAry = new Array();
var winCntAry  = new Array();
var wincntr = 0;

(egovInbox = {
			'renderInbox' : function () {
				document.getElementById('inboxtitle').innerHTML = this.from;
				document.getElementById('filterContainer').style.display = 'none';
				document.getElementById('historyContainer').style.display='none';
				if(this.from === 'Inbox') {
					if (inboxData === null || inboxData === '') {
						document.getElementById('error').innerHTML = "No Inbox item found !";
						document.getElementById('inbox').style.display ='none';
						document.getElementById('filter').disabled = true;
					} else {
						document.getElementById('error').innerHTML = '';
						this.renderInboxData(inboxData,"inbox","works");
						document.getElementById('inbox').style.display='block';
						document.getElementById('filter').disabled = false;
				   }
					
				} else if (this.from === 'Drafts') {
					if (draftData === null || draftData === '') {
						document.getElementById('error').innerHTML = "No Draft item found !";
						document.getElementById('inbox').style.display ='none';
						document.getElementById('filter').disabled = true;
					} else {
						document.getElementById('error').innerHTML ='';
						try { this.renderInboxData(draftData,"inbox","draft"); } catch (e) {this.refresh();}						
						document.getElementById('inbox').style.display='block';
						document.getElementById('filter').disabled = false;						
					}
				} else  {
					if (inboxData === null || inboxData === '') {
						document.getElementById('error').innerHTML = "No Notifications found !";
						document.getElementById('inbox').style.display ='none';
						document.getElementById('filter').disabled = true;
					} else {
						document.getElementById('filter').disabled = true;		
						document.getElementById('error').innerHTML = '';
						this.renderInboxData(inboxData,"inbox","works");
						document.getElementById('inbox').style.display='block';						
					}
				}			
				
			},
			
			'from':'Inbox',

			'renderHistory' : function (historyData) {
				document.getElementById('filterContainer').style.display = 'none';
				document.getElementById('min').innerHTML = '-';
				document.getElementById('min').title = 'Minimize';
				var hisCont = document.getElementById('historyContainer');
				hisCont.style.display='block';
				hisCont.style.top = '50px';
				hisCont.style.left = '50px';
				hisCont.style.width = '90%';
				hisCont.style.height = '80%';
				this.renderInboxData(historyData,"history","history");
			},

			'renderInboxData' : function (data,container,from) {
			var Inbox = function () {
			var inboxColumnDefs = [ {key:"Id", parser:"number",sortable:true,hidden:true},
									{key:"Date", sortable:true, parser:"date", resizeable:true}, 
									{key:"Sender", hidden:(from == 'draft' ? true : false),sortable:true, resizeable:true,parser:"string"}, 
									{key:"Task", sortable:true, resizeable:true,parser:"string"}, 
									{key:"Status", sortable:true, resizeable:true,parser:"string"}, 
									{key:"Details", label: (container == 'history' ? 'Comments' : 'Details'),sortable:true, resizeable:true,parser:"string"},
									{key:"Link", hidden:true}];
			
					if (container == 'history') {
						for (var i=0;true;i++) {
							var temp = data;
							data = data.replace("~"," - ");
							if (data == temp)
								break;
						}
					}
					
					var inboxDataSource = new YAHOO.util.DataSource(eval("("+data+")"));
					inboxDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
					inboxDataSource.responseSchema = {fields:["Id","Date", "Sender", "Task", "Status", "Details", "Link"]};
					var oConfigs = {
						paginator: new YAHOO.widget.Paginator({rowsPerPage: document.getElementById('pageSize').value,alwaysVisible : false,totalRecords : inboxDataSource.length}),
                		draggableColumns:true
        			};
					var inboxDataTable = new YAHOO.widget.DataTable(container, inboxColumnDefs, inboxDataSource,oConfigs);
					inboxDataTable.subscribe("rowMouseoverEvent", inboxDataTable.onEventHighlightRow);   
					inboxDataTable.subscribe("rowMouseoutEvent", inboxDataTable.onEventUnhighlightRow);
					inboxDataTable.subscribe("rowClickEvent", inboxDataTable.onEventSelectRow); 
					if (container === 'inbox')	{
						 inboxDataTable.subscribe("rowClickEvent", function(oArgs){
															document.getElementById('loadImg').style.display = 'block';
															var tr = oArgs.target;
															var url = tr.lastChild.firstChild.innerText ? tr.lastChild.firstChild.innerText : tr.lastChild.firstChild.textContent;
															var id  = "inboxWin"+(tr.firstChild.firstChild.innerHTML.split("#")[1]);

															if(url.trim !== '') {
																if (windAry[id] && !windAry[id].closed) {
																	windAry[id].focus();
																} else {
																	winCntAry[wincntr++] = windAry[id] = window.open(url,id,"width="+(window.screen.width-200)+",height="+(window.screen.height-100)+",top=0,left=0,resizable=yes,scrollbars=yes");
																}
															} else {
																window.alert("Application URL does not found !");
															}
															document.getElementById('loadImg').style.display = 'none';
														 });  											 
						 var onContextMenuClick = function(p_sType, p_aArgs, p_myDataTable) {   
							document.getElementById('loadImg').style.display = 'block';
							var task = p_aArgs[1];   
							if(task) {   
								 // Extract which TR element triggered the context menu   
								 var elRow = this.contextEventTarget;
								 elRow = p_myDataTable.getTrEl(elRow);   
								 var stateId = elRow.firstChild.firstChild.innerHTML.split("#")[0];
								 if (stateId !== '') {
									 var sUrl = "../workflow/inbox-populateHistory.action?stateId="+stateId+"&rnd="+Math.random();
									 var callback = {
									 success:function (oResponse) {
										 var historyData = oResponse.responseText;
										 if(historyData == "error") {
												responseData = "";
										 }
										 egovInbox.renderHistory(historyData);
										 document.getElementById('loadImg').style.display = 'none';
									 }, 
									 failure:function (oResponse) {document.getElementById('loadImg').style.display = 'none';}, 
									 timeout:300000, 
									 cache:false
									};
									YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
								 } else {
									alert("No History State is available !");
									document.getElementById('loadImg').style.display = 'none';
								 }					 
							}   
						};   
						var inboxContextMenu = new YAHOO.widget.ContextMenu("inboxContextMenu"+Math.random(),  {trigger:inboxDataTable.getTbodyEl()}); 
						inboxContextMenu.addItem("History");
						inboxContextMenu.render(container);
						inboxContextMenu.clickEvent.subscribe(onContextMenuClick, inboxDataTable);   
						
					}
				 return {oDS:inboxDataSource, oDT:inboxDataTable};					 				
				
			}();
		},

		'minimizeHistory' : function(obj) {
			var hisCont = document.getElementById('historyContainer');
			if (obj.innerHTML == '-'){
				obj.innerHTML = '^';
				obj.title='Maximize';
				hisCont.style.width = '21%';
				hisCont.style.height = '25px';
				hisCont.style.left = '79%';
				hisCont.style.top = '90%';
			} else {
				obj.innerHTML = '-';
				obj.title='Minimize';
				hisCont.style.width = '90%';
				hisCont.style.height = '80%';
				hisCont.style.left = '50px';
				hisCont.style.top = '50px';
			}
		},

		'closeHistory' : function () {
			document.getElementById('historyContainer').style.display='none';
			
		},

		'refresh' : function() {
			document.getElementById('loadImg').style.display = 'block';
			var sUrl = null;
			var comingfrom = this.from;
			if (comingfrom  === 'Drafts') {
				sUrl = "../workflow/inbox-pollDraft.action?rnd="+Math.random();
			} else if (comingfrom  === 'Inbox'){
				sUrl = "../workflow/inbox-pollInbox.action?rnd="+Math.random();
			} else {
				sUrl = "/dms/dms/fileNotification!loadFileNotificationData.action?rnd="+Math.random();
			}

			var callback = {
					 success:function (oResponse) {
						 var responseData = oResponse.responseText;
						 if(responseData == "error") {
							responseData = "";
						 }
						 if (comingfrom  === 'Drafts') {
							draftData = responseData;
							egovInbox.renderInbox ();		
						} else {
							inboxData = responseData;
							egovInbox.renderInbox ();
						}
						document.getElementById('loadImg').style.display = 'none';				
					 }, 
					 failure:function (oResponse) {
						 if (comingfrom  === 'Drafts') {
							draftData = '';
						 } else {
							inboxData = '';
						}
						document.getElementById('loadImg').style.display = 'none';
					 }, 
					 timeout:300000, 
					 cache:false
				};
			YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
		},

		'showFilter' : function() {
			if(this.from === 'Drafts') {
				return;
			}
			document.getElementById('filterContainer').style.top = '50px';
			document.getElementById('filterContainer').style.left = '50px';
			document.getElementById('filterContainer').style.display = 'block';
			document.getElementById('historyContainer').style.display='none';
		},

		'closeFilter' : function() {
			document.getElementById('filterContainer').style.display = 'none'; 
		},

		'filterInbox' : function () {
			document.getElementById('loadImg').style.display = 'block';
			document.getElementById('filterContainer').style.display = 'none';
			var task = null;
			if (document.getElementById('task').selectedIndex === 0) {
				task = "";
			} else {
				task = document.getElementById('task').options[document.getElementById('task').selectedIndex].text;
			}
			
			var param = "fromDate="+document.getElementById('fromDate').value+"&toDate="+document.getElementById('toDate').value+
						"&sender="+document.getElementById('sender').value+"&task="+task+"&rnd="+Math.random;
			var sUrl  = "../workflow/inbox!filterInboxData.action?"+param;
			var callback = {
					 success:function (oResponse) {
						 var responseData = oResponse.responseText;
						 if(responseData == "error") {
							responseData = "";
						 }
						 inboxData = responseData;
						 this.from = 'Inbox';
						 egovInbox.renderInbox ();
						 egovInbox.closeFilter();
						 document.getElementById('loadImg').style.display = 'none';				
					 }, 
					 failure:function (oResponse) {
						inboxData = '';
						egovInbox.closeFilter();
						document.getElementById('loadImg').style.display = 'none';
					 }, 
					 timeout:300000, 
					 cache:false
				};
			YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
		},

		'autoPoll' : function () { 
			egovInbox.refresh();
		},		

		'initInbox' : function() {
			var cal = new YAHOO.widget.Calendar("calender", { title:"Choose Date:", close:true, navigator:true } );
			cal.render();
			cal.hide();	
			YAHOO.util.Event.addListener("fromDateBtn", "click", cal.show, cal, true);
			YAHOO.util.Event.addListener("toDateBtn", "click", cal.show, cal, true);
			cal.selectEvent.subscribe(egovInbox.setSelectedDate, cal, true);
			egovInbox.refresh();
			//window.setInterval(egovInbox.autoPoll,900000);
		},
		'forDate' : 'fromDate',
		'setSelectedDate' : function (type,args,obj) {
		  var datedata = args[0][0];
		  var year = datedata[0];
		  var month = datedata[1];
		  var day = datedata[2];  
		  document.getElementById(egovInbox.forDate).value = day+'/'+month+'/'+year;
		  obj.hide();
		},
		'DragAndDrop' : function(obj,eve,onAt) {
			var evnt = window.event ? window.event : eve;
			try {
				if(evnt.srcElement) {
					if (evnt.srcElement.parentNode.parentNode.parentNode.id != onAt) {
						return false;
					}
					evnt.srcElement.parentNode.parentNode.parentNode.style.cursor = 'move';
				} else {
					if (evnt.originalTarget.parentNode.parentNode.parentNode.id != onAt) {
						return false;
					}
					evnt.originalTarget.parentNode.parentNode.parentNode.style.cursor = 'move';
				}
			
				var initX = evnt.clientX ? evnt.clientX : evnt.pageX;
				var initY = evnt.clientY ? evnt.clientY : evnt.pageY;
				var currPosLeft = parseInt(obj.style.left);
				var currPosTop = parseInt(obj.style.top);
				obj.onmousemove = function (evnt) {
					var evnt = window.event ? window.event : evnt;
					this.onmouseup = function (evnt) {
						var evnt = window.event ? window.event : evnt;
						try { evnt.originalTarget.parentNode.parentNode.parentNode.style.cursor = 'default'; } catch (e) {
							try {evnt.srcElement.parentNode.parentNode.parentNode.style.cursor = 'default';}catch(e){}
						}
						this.onmousemove= null;
						try { evnt.preventDefault(); } catch (e) {evnt.returnValue = false;}	
						return false;					
					};
					var currX = evnt.clientX ? evnt.clientX : evnt.pageX;
					var currY = evnt.clientY ? evnt.clientY : evnt.pageY;
					var left  = currPosLeft+currX-initX;
					var top = currPosTop+currY-initY;
					obj.style.top = top;
					obj.style.left = left;
				};
			} catch (e){return false;}
		}
});

YAHOO.util.Event.onDOMReady(egovInbox.initInbox);