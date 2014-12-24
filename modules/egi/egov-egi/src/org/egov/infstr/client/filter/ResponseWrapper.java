package org.egov.infstr.client.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public class ResponseWrapper extends HttpServletResponseWrapper {
	StringWriter strout;
	PrintWriter writer;
	//ServletOutputStream sout;
	//private CharArrayWriter charWriter;

	ResponseWrapper(HttpServletResponse aResponse) {
		super(aResponse);
		strout = new StringWriter();
		//sout = new ServletOutputStreamWrapper(strout);
		writer = new PrintWriter(strout);
	}

	public String getData() {
		//writer.flush();

		return strout.toString();
	}

	/*public ServletOutputStream getOutputStream() {
		return sout;
	}*/

	public PrintWriter getWriter() throws IOException {
		return writer;
	}
}

	/*class ServletOutputStreamWrapper extends ServletOutputStream {
	StringWriter writer;

	ServletOutputStreamWrapper(StringWriter aWriter) {
		writer = aWriter;
	}

	public void write(int aByte) {
		writer.write(aByte);
	}
	
}*/
