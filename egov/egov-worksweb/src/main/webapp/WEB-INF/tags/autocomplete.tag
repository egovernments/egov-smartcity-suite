<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="field" required="true"%>
<%@ attribute name="results" required="true"%>
<%@ attribute name="url" required="true"%>
<%@ attribute name="handler" required="false"%>
<%@ attribute name="queryQuestionMark" required="false" %>
<%@ attribute name="forceSelectionHandler" required="false" %>
<%@ attribute name="paramsFunction" required="false" %>
<%@ attribute name="beforeHandler" required="false" %>
<%@ attribute name="afterHandler" required="false" %>
<%@ attribute name="maxResults" required="false" %>
<%@ attribute name="queryLength" required="false" %>
<%@ attribute name="width"%>

<%
 if(width==null) width="25";
 if(queryLength==null) queryLength="1";
 if(maxResults==null) maxResults="20";
%>
<style type="text/css">
#<%=name%>_autocomplete {
    /*width:<%=width%>em; 
    padding-bottom:2em;*/
}
</style>
<script type="text/javascript">

<%=name%>Request = function() {
	
    var <%=name%>DS = new YAHOO.util.XHRDataSource("<%=url%>");
    // Set the responseType
    <%=name%>DS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
    // Define the schema of the JSON results
    <%=name%>DS.responseSchema = {
        resultsList : "ResultSet.Result",
        fields : ["value","key"]
    };
    var <%=name%>AC = new YAHOO.widget.AutoComplete("<%=field%>", "<%=results%>", <%=name%>DS);
    <%if(beforeHandler!=null &&!"".equals(beforeHandler.trim())){%>
        <%=name%>AC.dataRequestEvent.subscribe(<%=beforeHandler%>);
    <%}%>
    <%if(afterHandler!=null &&!"".equals(afterHandler.trim())){%>
        <%=name%>AC.dataReturnEvent.subscribe(<%=afterHandler%>);
    <%}%>
    <%=name%>AC.generateRequest = function(sQuery){
        var dataType = this.dataSource.dataType;
        
        // Transform query string in to a request for remote data
        // By default, local data doesn't need a transformation, just passes along the query as is.
        if(dataType === YAHOO.util.DataSourceBase.TYPE_XHR) {
            // By default, XHR GET requests look like "{scriptURI}?{scriptQueryParam}={sQuery}&{scriptQueryAppend}"
            if(!this.dataSource.connMethodPost) {
                sQuery = (this.queryQuestionMark ? "?" : "") + (this.dataSource.scriptQueryParam || "query") + "=" + sQuery + 
                    (this.dataSource.scriptQueryAppend ? ("&" + this.dataSource.scriptQueryAppend) : "");        
            }
            // By default, XHR POST bodies are sent to the {scriptURI} like "{scriptQueryParam}={sQuery}&{scriptQueryAppend}"
            else {
                sQuery = (this.dataSource.scriptQueryParam || "query") + "=" + sQuery + 
                    (this.dataSource.scriptQueryAppend ? ("&" + this.dataSource.scriptQueryAppend) : "");
            }
        }
        // By default, remote script node requests look like "{scriptURI}&{scriptCallbackParam}={callbackString}&{scriptQueryParam}={sQuery}&{scriptQueryAppend}"
        else if(dataType === YAHOO.util.DataSourceBase.TYPE_SCRIPTNODE) {
            sQuery = "&" + (this.dataSource.scriptQueryParam || "query") + "=" + sQuery + 
                (this.dataSource.scriptQueryAppend ? ("&" + this.dataSource.scriptQueryAppend) : "");    
        }
        
       var params=''
       <%if(paramsFunction!=null && !"".equals(paramsFunction.trim())){%>
       	  params="&"+<%=paramsFunction%>();
	<%}%>
       return sQuery+params;
      
    }
    <%=name%>AC.minQueryLength=<%=queryLength%>  
    <%=name%>AC.queryDelay = .5;
	<%=name%>AC.useIFrame =  true;
	<%=name%>AC.maxResultsDisplayed=<%=maxResults%>
    <%if(handler!=null && !"".equals(handler.trim())){%>
        <%=name%>AC.itemSelectEvent.subscribe(<%=handler%>);
    <%}%>   
    <%if(queryQuestionMark!=null && !"".equals(queryQuestionMark.trim())){%>
        <%=name%>AC.queryQuestionMark=<%=queryQuestionMark%>;
    <%}%>  
    <%if(forceSelectionHandler!=null && !"".equals(forceSelectionHandler.trim())){%>
        <%=name%>AC.forceSelection = true;  
    <%}%>  

    return {
        <%=name%>DS: <%=name%>DS,
        <%=name%>AC: <%=name%>AC
    };
}();
</script>
 