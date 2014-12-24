<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  

		<div id="blanket" style="display:none;"></div>
		<div id="popLoadingDiv" style="display:none;font-size: 12px;font-weight: bold;color: #cc0000;position:absolute;
			width:350px;height:150px;z-index: 9002;text-align: center;" >
			
			<img src="<egov:url path='/images/loading.gif'/>" alt="Help" width="100" height="100" border="0" />
			
		</div>
		<table id="docTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<!-- 
					<input type="button" value="Load Template" onclick="getTemplateByDepName()" />
					<input type="button" value="Print" onclick="printSpecial()" />
						<a id="printButton" href="javascript:printSpecial()" >
							<img src="<egov:url path='/images/print.gif'/>" alt="Print" border="0" /> 
						</a>
					-->
					<s:hidden id="documentId" name="model.document"  />					
				</td>
			</tr>
		</table>
		<table width="400" border="0" cellpadding="0" cellspacing="0" align="center" id="yuiTab">
			<tr >
				<td width="15%" class="tablesubheadwka">
					<span id="yui-button-1"></span>
				</td>
			</tr>
			<tr >
				<td width="15%" class="tablesubheadwka">
					 <textarea name="msgpost" id="msgpost" cols="50" rows="10" escape="true">
					</textarea>
				</td>
			</tr>
		</table>
		<script type="text/javascript">
			var dataVal ='Hiiii';
			
			function createUsingRTE() {
				$('rteDiv').show();
				rteModeType('rte_design_mode');
				scriptUsed = 'rte';
			}
			
			function showValues() {
				document.getElementById(rteName).contentWindow.document.getElementById('wardSpan').innerHTML='Dummy';			
			}
			
			function setDocumentValues() {

				$('documentId').value = escape(myEditor.getEditorHTML());
			}
			
			function getTemplateByDepName() {
				myEditor.setEditorHTML('');
				$('documentId').value = escape(myEditor.getEditorHTML());
				var depName = $('executingDepartment').options[$('executingDepartment').selectedIndex].text;
				if(depName == '--------Select--------')
					return false;
				//document.getElementById(rteName).contentWindow.document.body.innerHTML = '';
				popup('popLoadingDiv');
				var depName = $('executingDepartment').options[$('executingDepartment').selectedIndex].text;
				var actionUrl = '${pageContext.request.contextPath}/estimate/ajaxEstimate!getTemplateByDepName.action?decorate=false';
				var params    = 'departmentName='+depName;
				var ajaxcall = new Ajax.Request(actionUrl, {
						method:'get',parameters:params,onSuccess:function(transport){
						if( transport.responseText.strip() != 'docTemplate.template') {
//							document.getElementById(rteName).contentWindow.document.body.innerHTML = transport.responseText;
							myEditor.setEditorHTML(unescape(transport.responseText));
							setDocumentValues();
						} else {
							alert('No Enclosure found for department: '+depName);
						}
						popup('popLoadingDiv');
					}
				});
			}
			
			
    
		    function printSpecial(p_oEvent) {		
				var printWin = window.open("","printSpecial","menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes");
				printWin.document.open();
				printWin.document.write(myEditor.getEditorHTML());
				printWin.document.close();
				printWin.print();
			}	
			
			//initRTE($F('documentId'));

			//$('msgpost').innerHTML = $F('documentId');
	var oPushButton2 = new YAHOO.widget.Button({
			id:'printButton',
            type: 'push', 
            label: 'Print', 
            container:"yui-button-1",
            onclick: { fn: printSpecial}
    		}); 

	
	var myEditor = new YAHOO.widget.Editor('msgpost', {
		height: '300px',
		width: '1000px',
		animate: true //Animates the opening, closing and moving of Editor windows
	});		

	//var buttonConfig = 
	//	{
	//		id:'printButton1',
      //      type: 'push', 
        //    label: 'Print', 
          //  onclick: { fn: printSpecial}
    	//};
//    myEditor.on('toolbarLoaded', function() { 
  //      myEditor.toolbar.addButtonToGroup(oPushButton2, 'insertitem');
        //myEditor.toolbar.addButton(buttonConfig);
    //});
    
    myEditor.render();
    
   
</script>
