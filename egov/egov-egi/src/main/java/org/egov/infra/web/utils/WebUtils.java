package org.egov.infra.web.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

    public static String extractRequestedDomainName(final HttpServletRequest httpRequest) {
        final String requestURL = httpRequest.getRequestURL().toString();
        final int domainNameStartIndex = requestURL.indexOf("://") + 3;
        String domainName = requestURL.substring(domainNameStartIndex, requestURL.indexOf('/', domainNameStartIndex));
        if (domainName.contains(":"))
            domainName = domainName.split(":")[0];
        return domainName;
    }
}
