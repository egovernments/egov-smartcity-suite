package org.egov.pgr.entity;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.validation.regex.Constants;
import org.egov.search.domain.Searchable;
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
@Searchable
public class Complainant extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5691022600220045218L;

    @Length(max=150)
    @SafeHtml
    @Searchable
    private String name;

    @Length(max=20)
    @SafeHtml
    @Pattern(regexp = Constants.MOBILE_NUM)
    @Searchable
    private String mobile;

    @Length(max=100)
    @SafeHtml
    @Email(regexp=Constants.EMAIL)
    @Searchable
    private String email;

    @ManyToOne
    @Valid
    @JoinColumn(name="userDetail",nullable = true)
    private User userDetail;

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

    public User getUserDetail() {
	return userDetail;
    }

    public void setUserDetail(final User userDetail) {
	this.userDetail = userDetail;
    }
}
