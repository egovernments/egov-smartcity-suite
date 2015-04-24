<%@ include file="/includes/taglibs.jsp" %>

<!-- 	 <s:url id="checklistajax" value="/register/registerBpa!showCheckList.action">   
	  <s:param name="servicId" value='<s:property value="#newval"/>'></s:param>	    
	 </s:url>
     <sj:div href="%{checklistajax}" indicator="indicator"  cssClass="formmainbox"   dataType="html" onCompleteTopics="completeDiv" reloadTopics="reloadDiv">
        <img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/>
     </sj:div>
     <sj:a id="reloadLink" onClickTopics="reloadDiv">Refresh Div</sj:a> 
     
 -->


<div id="showjsp">
   	<%@ include file="../register/registerBpaExtn-checklist.jsp"%>
</div>

<div id="hidejsp">
	<div id="loading" class="loading" style="width: 700; height: 700;display: none " align="center" >	
		<blink style="color: red">loading, Please wait...</blink>
	</div> 
		
  	<div id="regnchecklist">
	<script>
		document.getElementById('loading').style.display ='none';
	</script>
	</div>
</div>



	