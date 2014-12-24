<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"xDepmethord":"<s:property value="%{parentCategory.depMethord.id}" />",
    		"xAssetCode":"<s:property value="%{parentCategory.assetCode.id}" />",
    		"xAccDepCode":"<s:property value="%{parentCategory.accDepCode.id}" />",
    		"xRevCode":"<s:property value="%{parentCategory.revCode.id}" />",
    		"xDepExpCode":"<s:property value="%{parentCategory.depExpCode.id}" />",
    		"xUom":"<s:property value="%{parentCategory.uom.id}" />",
    		"xCatAttrTemplate":"<s:property value="%{parentCategory.catAttrTemplate}" />"
    	}
    ]
  }
}