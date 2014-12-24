<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="id" required="true"%>
<%@ attribute name="fields" required="true"%>
<%@ attribute name="url" required="true"%>

<script>

 ${id}SuccessHandler=function(req,res){
    dom.get("${id}").value = res.results[0].Text;
}
 ${id}FailureHandler=function(){
  alert('Unable to load ${id}');
}

function populate${id}(params){
    makeJSONCall(${fields},'${pageContext.request.contextPath}/${url}',params, ${id}SuccessHandler, ${id}FailureHandler) ;
}
</script>