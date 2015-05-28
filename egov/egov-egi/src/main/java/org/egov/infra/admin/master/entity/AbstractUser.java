package org.egov.infra.admin.master.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.infra.admin.master.entity.enums.Gender;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class AbstractUser extends AbstractAuditable<User, Long> {
    private static final long serialVersionUID = -2415368058955783970L;

    @NotNull
    @Column(name = "username", unique = true, table = "eg_user")
    @Length(min = 2, max = 64)
    private String username;

    @NotNull
    @Length(min = 8, max = 64)
    @Column(table = "eg_user")
    private String password;

    @Column(table = "eg_user")
    private String salutation;

    @NotNull
    @SafeHtml
    @Length(min = 2, max = 100)
    @Column(table = "eg_user")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(table = "eg_user")
    private Gender gender;

    @Pattern(regexp = Constants.MOBILE_NUM)
    @SafeHtml
    @Length(max = 15)
    @Column(table = "eg_user")
    private String mobileNumber;

    @Email(regexp = Constants.EMAIL)
    @SafeHtml
    @Length(max = 128)
    @Column(table = "eg_user")
    private String emailId;

    @SafeHtml
    @Column(table = "eg_user")
    private String altContactNumber;

    @SafeHtml
    @Length(max = 10)
    @Column(table = "eg_user")
    private String pan;

    @SafeHtml
    @Length(max = 20)
    @Column(table = "eg_user")
    private String aadhaarNumber;

    // TODO Make Embedded
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(table = "eg_user")
    private List<Address> address = new ArrayList<>();

    @Column(table = "eg_user")
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "userid") , inverseJoinColumns = @JoinColumn(name = "roleid") )
    private Set<Role> roles = new HashSet<>();

    @Temporal(TemporalType.DATE)
    @Column(table = "eg_user")
    private Date dob;

    @NotNull
    @Column(table = "eg_user")
    private Date pwdExpiryDate;

    @NotNull
    @Column(table = "eg_user")
    private String locale;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", table = "eg_user")
    protected UserType type;

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(final String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    public String getAltContactNumber() {
        return altContactNumber;
    }

    public void setAltContactNumber(final String altContactNumber) {
        this.altContactNumber = altContactNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(final String pan) {
        this.pan = pan;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(final String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(final List<Address> address) {
        this.address = address;
    }

    public void addAddress(final Address address) {
        getAddress().add(address);
    }

    public void removeAddress(final Address address) {
        getAddress().remove(address);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(final Role role) {
        getRoles().add(role);
    }

    public void removeRole(final Role role) {
        getRoles().remove(role);
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(final Date dob) {
        this.dob = dob;
    }

    public DateTime getPwdExpiryDate() {
        return null == pwdExpiryDate ? null : new DateTime(pwdExpiryDate);
    }

    public void setPwdExpiryDate(final Date pwdExpiryDate) {
        this.pwdExpiryDate = pwdExpiryDate;
    }

    public String getLocale() {
        return locale;
    }

    public Locale locale() {
        return new Locale(locale);
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public UserType getType() {
        return type;
    }

    public void setType(final UserType type) {
        this.type = type;
    }
}
