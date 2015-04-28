/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.documents.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
			final Integer userId = EGOVThreadLocals.getUserId().intValue();
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
