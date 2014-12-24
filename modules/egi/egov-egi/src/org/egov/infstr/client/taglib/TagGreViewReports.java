package org.egov.infstr.client.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TagGreViewReports extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TagGreViewReports() {
		super();
	}

	private String getScript(final StringBuffer s) {
		final String lscriptStr1 = "<SCRIPT>";
		final String lscriptStr2 = "</SCRIPT>";
		final String lresutantStr = lscriptStr1 + s + lscriptStr2;
		return lresutantStr;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {
		try {
			final javax.servlet.http.HttpSession gSession = this.pageContext.getSession();
			gSession.getAttribute("org.egov.topBndryID");
			// FIXME ConstructStringBndryDept is obsolate use some other way
			// final StringBuffer finalString = new StringBuffer(ConstructStringBndryDept.getFnString(toplevelID));
			final StringBuffer finalString = new StringBuffer("");
			final JspWriter out = this.pageContext.getOut();
			out.print(this.getScript(finalString));
		} catch (final Exception ioe) {
			

		}

		return EVAL_PAGE;
	}
}
