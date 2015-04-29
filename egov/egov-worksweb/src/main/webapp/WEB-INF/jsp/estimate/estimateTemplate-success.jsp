<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>

<title>Estimate Template</title>
<body >
<script>

</script>
	
	<s:if test="%{model.id!=null && mode!='edit'}" >
				The Estimate Template with code '<s:property value="%{code}" />' and name  '<s:property value="%{name}" />' was created successfully 
	</s:if>
    <s:if test="%{mode=='edit'}">   
				The Estimate Template with code '<s:property value="%{code}" />' and name  '<s:property value="%{name}" />' was saved successfully 
	</s:if>
</body>
</html>
