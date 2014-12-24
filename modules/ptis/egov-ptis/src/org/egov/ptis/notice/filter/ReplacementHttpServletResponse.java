package org.egov.ptis.notice.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
 
/**
 * returns specialized writer to capture jsp output
 */
public class ReplacementHttpServletResponse extends HttpServletResponseWrapper {
  private CharArrayWriter replacementWriter;
 
  public ReplacementHttpServletResponse (HttpServletResponse response) {
    super(response);
    replacementWriter = new CharArrayWriter();
  }
 
  public PrintWriter getWriter() throws IOException {
    return new PrintWriter(replacementWriter);
  }
 
  public String toString() {
    return replacementWriter.toString();
  }
}