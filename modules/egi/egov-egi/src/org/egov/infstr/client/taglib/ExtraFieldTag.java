package org.egov.infstr.client.taglib;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ExtraFieldTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map extraFieldMap = null;

	public ExtraFieldTag() {
		super();
	}

	public Map getExtraFieldMap() {
		return this.extraFieldMap;
	}

	public void setExtraFieldMap(final Map extraFieldMap) {
		this.extraFieldMap = extraFieldMap;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {
		try {
			final Map extFieldMap = this.getExtraFieldMap();
			final Set keySet = extFieldMap.keySet();
			final Iterator setitr = keySet.iterator();
			String extString = "";
			while (setitr.hasNext()) {
				final String fieldname = (String) setitr.next();
				String value = (String) extFieldMap.get(fieldname);
				if (value == null || value.equals(" ")) {
					value = "N/A";
				}
				extString += "<TR><TD colspan = \"2\"><font id=\"normal_font\" >" + fieldname + "</font></td>" + "<TD colspan = \"2\" ><font id=\"normal_font\" >" + value + "</font></td></tr>";
			}
			final JspWriter out = this.pageContext.getOut();

			out.print(extString);
		} catch (final Exception ioe) {
			

		}

		return EVAL_PAGE;
	}

}
