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

<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%
 if(width==null) width="25";
 if(queryLength==null) queryLength="1";
 if(maxResults==null) maxResults="20";
%>
<style type="text/css">
#<%=name%>_autocomplete {
    width:<%=width%>em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
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
 