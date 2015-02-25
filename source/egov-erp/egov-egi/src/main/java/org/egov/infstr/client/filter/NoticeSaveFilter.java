/*
 * @(#)NoticeSaveFilter.java 3.0, 18 Jun, 2013 12:55:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.jcr.RepositoryException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.utils.HibernateUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class NoticeSaveFilter implements Filter {
	private static final String NOTICE_OBJECT = "noticeObject";
	private static final Logger LOG = LoggerFactory.getLogger(NoticeSaveFilter.class);
	private FilterConfig filterConfig = null;
	private String documentServiceBean = "documentManagerService";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		if (filterConfig.getInitParameter("documentServiceBean") != null) {
			this.documentServiceBean = filterConfig.getInitParameter("documentServiceBean");
		}
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final PrintWriter out = response.getWriter();
		final ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

		chain.doFilter(request, responseWrapper);
		final String output = responseWrapper.getData();

		if (request.getAttribute(NOTICE_OBJECT) != null) {
			final Integer userId = Integer.valueOf(EGOVThreadLocals.getUserId());
			final Date now = new Date();
			final InputStream inputStream = new ByteArrayInputStream(output.getBytes());
			final Notice notice = (Notice) request.getAttribute(NOTICE_OBJECT);
			if (LOG.isDebugEnabled()) {
				LOG.debug("NoticeSaveFilter: going to save Notice: " + notice);
			}
			notice.setCreatedBy(userId);
			notice.setModifiedBy(userId);
			notice.setCreatedDate(now);
			notice.setModifiedDate(now);
			final AssociatedFile file = new AssociatedFile();
			file.setFileName(notice.getDocumentNumber() + ".htm");
			file.setFileInputStream(inputStream);
			file.setMimeType("text/html");

			file.setCreatedBy(userId);
			file.setModifiedBy(userId);
			file.setCreatedDate(now);
			file.setModifiedDate(now);
			notice.getAssociatedFiles().add(file);
			//HibernateUtil.beginTransaction();
			final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(this.filterConfig.getServletContext());
			final DocumentManagerService docManager = (DocumentManagerService) wac.getBean(this.documentServiceBean);
			try {
				docManager.addDocumentObject(notice);
				if (LOG.isDebugEnabled()) {
					LOG.debug("NoticeSaveFilter: saved Notice in DMS: " + notice);
				}
			} catch (final IllegalAccessException e) {
				throw new EGOVRuntimeException("Error occurred while saving Notice in Document Manager", e);
			} catch (final RepositoryException e) {
				throw new EGOVRuntimeException("Error occurred while saving Notice in Document Manager", e);
			}
		}

		out.write(output);
		out.close();
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

}
