<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xDepreciationMethod":"<s:property value="%{parentCategory.depreciationMethod.id}" />",
    		"xAssetAccountCode":"<s:property value="%{parentCategory.assetAccountCode.id}" />",
    		"xAccDepAccountCode":"<s:property value="%{parentCategory.accDepAccountCode.id}" />",
    		"xRevAccountCode":"<s:property value="%{parentCategory.revAccountCode.id}" />",
    		"xDepExpAccountCode":"<s:property value="%{parentCategory.depExpAccountCode.id}" />",
    		"xUom":"<s:property value="%{parentCategory.uom.id}" />",
    		"xCatAttrTemplate":"<s:property value="%{parentCategory.catAttrTemplate}" />"
    	}
    ]
  }
}
