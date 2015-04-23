package org.egov.ptis.notice.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.ptis.domain.dao.property.NoticeDAO;
import org.egov.ptis.notice.PtNotice;

/**
 * passes on a subclass of HttpServletResponseWrapper in order to replace the
 * output writer.
 */
public class SaveDocumentFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		NoticeDAO noticeDao = new NoticeDAO();
		SetDomainJndiHibFactNames.setThreadLocals(request);
		// wrap the original response
		HttpServletResponse newResponse = new ReplacementHttpServletResponse(
				(HttpServletResponse) response);
		// pass it to the resource
		chain.doFilter(request, newResponse);
		// get what the resource wrote
		String output = newResponse.toString();
		// put it to the output stream
		PrintWriter out = response.getWriter();
		out.write(output);
		out.close();
		// and then put it where ever else you like
		InputStream inputStream = new ByteArrayInputStream(output.getBytes());
		PtNotice noticeForm = (PtNotice)request.getAttribute("NoticeDetailsForm");
		noticeDao.saveNoticeDetails(noticeForm, inputStream);
	}
}
