package org.egov.pgr.entity;

import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pgr.utils.constants.EntityValidationRegex;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "pgr_complainant")
public class Complainant extends AbstractPersistable<Long> {

	@Length(max = 150, message = "{error-max-length-exceed}")
	@SafeHtml(message = "{error-input-unsafe}")
	private String name;
	
	@Length(max = 20, message = "{error-max-length-exceed}")
	@SafeHtml(message = "{error-input-unsafe}")
	@Pattern(regexp = EntityValidationRegex.MOBILE_NUM, message = "{error-invalid-mob}")
	private String mobile;
	
	@Length(max = 100, message = "{error-max-length-exceed}")
	@SafeHtml(message = "{error-input-unsafe}")
	@Email(message = "{error-invalid-email}")
	private String email;
	
	@ManyToOne
	@Valid
	@JoinColumn(nullable=true)
	private UserImpl userDetail;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserImpl getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserImpl userDetail) {
		this.userDetail = userDetail;
	}
}
