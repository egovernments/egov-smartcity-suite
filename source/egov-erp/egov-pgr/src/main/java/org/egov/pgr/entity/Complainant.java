package org.egov.pgr.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.validation.regex.Constants;
import org.egov.lib.rjbac.user.UserImpl;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "pgr_complainant")
public class Complainant extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5691022600220045218L;

    @Max(150)
    @SafeHtml
    private String name;

    @Max(20)
    @SafeHtml
    @Pattern(regexp = Constants.MOBILE_NUM)
    private String mobile;

    @Max(100)
    @SafeHtml
    @Email(regexp=Constants.EMAIL)
    private String email;

    @ManyToOne
    @Valid
    @JoinColumn(name="userDetail",nullable = true)
    private UserImpl userDetail;

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(final String mobile) {
	this.mobile = mobile;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(final String email) {
	this.email = email;
    }

    public UserImpl getUserDetail() {
	return userDetail;
    }

    public void setUserDetail(final UserImpl userDetail) {
	this.userDetail = userDetail;
    }
}
