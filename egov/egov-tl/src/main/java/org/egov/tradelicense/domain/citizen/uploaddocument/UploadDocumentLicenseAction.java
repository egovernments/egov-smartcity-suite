package org.egov.tradelicense.domain.citizen.uploaddocument;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.tradelicense.utils.Constants;
import org.egov.web.actions.docmgmt.BasicDocumentManagerAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;

/**
 * the class upload document
 * 
 * @author iffath
 */

@ParentPackage("egov")
public class UploadDocumentLicenseAction extends BasicDocumentManagerAction implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;

	@Override
	public String execute() {
		super.execute();
		setUserDetails();
		return SUCCESS;
	}
	
	@Override
	@ValidationErrorPage(SUCCESS)
	public String addDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		setUserDetails();
		return super.addDocument();
	}
	
	@Override
	public String editDocument() throws RuntimeException {
		setUserDetails();
		return super.editDocument();
	}
	
	@Override
	@ValidationErrorPage(SUCCESS)
	public String viewDocument() {
		setUserDetails();
		return super.viewDocument();
	}
	
	@Override
	@ValidationErrorPageExt(action = SUCCESS, makeCall = true, toMethod = "editDocument")
	public String updateDocument() throws IllegalAccessException, RepositoryException, RuntimeException {
		setUserDetails();
		return super.updateDocument();
	}
	@Override
    public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }
	
	private void setUserDetails() {
		session = request.getSession();
		String userName = (String) session.getAttribute("com.egov.user.LoginUserName");
		final User user;
		if (userName != null) {
			user = new UserDAO().getUserByUserName(userName);
		} else {
			user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
			session.setAttribute("com.egov.user.LoginUserName",	user.getUserName());
		}
		userId = user.getId();
		EGOVThreadLocals.setUserId(userId.toString());
	}
}
