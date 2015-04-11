package org.egov.infra.admin.master.entity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "eg_user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@Cacheable
public class User extends AbstractAuditable<User, Long> {

    private static final long serialVersionUID = -2415368058955783970L;

    @NotNull
    @Column(name = "username", unique = true)
    @Length(min = 2, max = 64)
    private String username;

    @NotNull
    @Length(min = 8, max = 64)
    private String password;

    private String salutation;

    @Expose
    @NotNull
    @SafeHtml
    @Length(min = 2, max = 100)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Pattern(regexp = Constants.MOBILE_NUM)
    @SafeHtml
    @Length(max = 15)
    private String mobileNumber;

    @Email(regexp = Constants.EMAIL)
    @SafeHtml
    @Length(max = 128)
    private String emailId;

    @SafeHtml
    private String altContactNumber;

    @SafeHtml
    @Length(max = 10)
    private String pan;

    @SafeHtml
    @Length(max = 20)
    private String aadhaarNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> address = Collections.emptyList();

    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "user"), inverseJoinColumns = @JoinColumn(name = "role"))
    private Set<Role> roles = Collections.emptySet();

    @Temporal(TemporalType.DATE)
    private Date dob;

    @NotNull
    private Date pwdExpiryDate;

    @NotNull
    private String locale;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    private UserType type;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

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
        this.getAddress().add(address);
    }

    public void removeAddress(final Address address) {
        this.getAddress().remove(address);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(final Role role) {
        this.getRoles().add(role);
    }

    public void removeRole(final Role role) {
        this.getRoles().remove(role);
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

    public Locale getLocale() {
        return new Locale(locale);
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public UserType getType() {
        return type;
    }

}
