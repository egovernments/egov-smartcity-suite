package org.egov.bpms.web.filter;

import org.egov.bpms.config.multitenant.ProcessEngineThreadLocal;
import org.egov.infra.config.core.ApplicationThreadLocals;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class ProcessEngineTenantResolverFilter implements Filter {
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        ProcessEngineThreadLocal.setTenant(ApplicationThreadLocals.getTenantID());
        chain.doFilter(request, response);
        ProcessEngineThreadLocal.clearTenant();
    }

    @Override
    public void destroy() {

    }
}
