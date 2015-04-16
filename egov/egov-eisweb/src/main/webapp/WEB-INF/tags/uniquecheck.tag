<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="id" required="true"%>
<%@ attribute name="fields" required="true"%>
<%@ attribute name="url" required="true"%>
<%@ attribute name="fieldtoreset" required="false" %>
<script>

 ${id}SuccessHandler=function(req,res){
    var result =res.results[0].Value;
    
    if(result=='true'){
    dom.get("${id}").style.display = "";
	   if(document.getElementById('${fieldtoreset}')){
	   	 dom.get("${fieldtoreset}").value = "";
	   	 }
	}
    else
    dom.get("${id}").style.display = "none";

}
 ${id}FailureHandler=function(){
  alert('Connection Lost');

}

function populate${id}(params){
    makeJSONCall(${fields},'${pageContext.request.contextPath}/${url}',params, ${id}SuccessHandler, ${id}FailureHandler) ;
}
</script>