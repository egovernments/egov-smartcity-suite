package org.egov.infra.web.spring.support.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.egov.exceptions.EGOVRuntimeException;

public class DuplicateFormSubmissionTokenTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        try {
            final String tokenName = (String) pageContext.getRequest().getAttribute("tokenName");
            if (tokenName != null) {
                final String tokenValue = (String) pageContext.getRequest().getAttribute(tokenName);
                final JspWriter out = pageContext.getOut();
                out.println("<input type='hidden' name='tokenName' value='" + tokenName + "'/>");
                out.println("<input type='hidden' name='" + tokenName + "' value='" + tokenValue + "'/>");
            }
        } catch (final IOException e) {
            throw new EGOVRuntimeException("Error occurred while adding submission token",e);
        }

        return SKIP_BODY;
    }

}