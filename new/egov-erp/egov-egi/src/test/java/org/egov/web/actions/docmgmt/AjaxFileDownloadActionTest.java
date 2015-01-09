package org.egov.web.actions.docmgmt;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.junit.Test;
import org.mockito.Mockito;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.util.ValueStack;

public class AjaxFileDownloadActionTest extends XWorkTestCase {

	private AjaxFileDownloadAction action;
	DocumentManagerService<DocumentObject> docManager;

	@Test
	public void testExecuteWithNoParams() {
		final ValueStack stack = ActionContext.getContext().getValueStack();
		ActionContext.setContext(Mockito.mock(ServletActionContext.class));
		Mockito.when(
				this.docManager.getFileFromDocumentObject(null, null, null))
				.thenThrow(new EGOVRuntimeException(""));
		stack.push(this.action);
		try {
			this.action.getContentLength();
			this.action.getFileName();
			this.action.getModel();
			this.action.getFileStream();
			this.action.getDocNumber();
			this.action.execute();
			fail();
		} catch (final EGOVRuntimeException e) {
			// do nothing
		}

	}

	public void testExecuteWithParams() throws IOException {
		this.action.setDocNumber("1");
		this.action.setFileName("test.properties");
		this.action.setModuleName("egi");
		Mockito.when(
				this.docManager.getFileFromDocumentObject("1", "egi",
						"test.properties")).thenReturn(null);
		String result = this.action.execute();
		assertEquals(Action.ERROR, result);
		final AssociatedFile file = new AssociatedFile();
		file.setFileName("test.properties");
		file.setMimeType("text/plain");
		Mockito.when(
				this.docManager.getFileFromDocumentObject("1", "egi",
						"test.properties")).thenReturn(file);
		result = this.action.execute();
		assertEquals(Action.SUCCESS, result);
		assertEquals(this.action.getContentType(), "text/plain");
	}

	public void testValidateWithParams() throws IOException {

		this.action.setDocNumber("1");
		this.action.setFileName("test.properties");
		this.action.setModuleName("egi");
		this.action.validate();
		assertEquals(0, this.action.getActionErrors().size());
	}

	public void testValidateWithNoParams() throws IOException {

		this.action.setDocNumber(null);
		this.action.setFileName(null);
		this.action.validate();
		assertEquals(3, this.action.getActionErrors().size());
	}

	public void testNoticeDownload() {
		Mockito.when(
				this.docManager.getFileFromDocumentObject("1", "egi",
						"test.html")).thenReturn(null);
		String result = this.action.downloadNotice();
		assertEquals(Action.ERROR, result);
		final ServletActionContext servletActionContext = Mockito
				.mock(ServletActionContext.class);
		final ServletContext servletContext = Mockito
				.mock(ServletContext.class);
		ActionContext.setContext(servletActionContext);
		Mockito.when(ServletActionContext.getServletContext()).thenReturn(
				servletContext);
		this.action.setDocNumber("1");
		this.action.setFileName("test.html");
		this.action.setModuleName("egi");
		final AssociatedFile file = new AssociatedFile();
		file.setFileName("test.html");
		file.setMimeType("text/html");
		Mockito.when(
				this.docManager.getFileFromDocumentObject("1", "egi",
						"test.html")).thenReturn(file);
		result = this.action.downloadNotice();
		assertEquals("RENDER_NOTICE", result);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.docManager = Mockito.mock(DocumentManagerService.class);
		this.action = new AjaxFileDownloadAction();
		this.action.setDocumentManagerService(this.docManager);
	}

}
