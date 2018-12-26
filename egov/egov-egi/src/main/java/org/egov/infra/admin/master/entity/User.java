/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.admin.master.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.LocaleUtils;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.overlay;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PAN_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PERSON_NAME;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_PHONE_NUMBER;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_SALUTATION;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_USERNAME;
import static org.egov.infra.validation.constants.ValidationRegex.EMAIL;
import static org.egov.infra.validation.constants.ValidationRegex.MOBILE_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.PAN_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.PERSON_NAME;
import static org.egov.infra.validation.constants.ValidationRegex.PHONE_NUMBER;
import static org.egov.infra.validation.constants.ValidationRegex.SALUTATION;
import static org.egov.infra.validation.constants.ValidationRegex.USERNAME;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "eg_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@SequenceGenerator(name = User.SEQ_USER, sequenceName = User.SEQ_USER, allocationSize = 1)
@Unique(fields = {"username", "pan", "emailId"}, enableDfltMsg = true, isSuperclass = true)
@CompositeUnique(fields = {"type", "mobileNumber"}, enableDfltMsg = true, message = "{user.exist.with.same.mobileno}")
@JsonIgnoreProperties({"createdBy", "lastModifiedBy"})
public class User extends AbstractAuditable {
    protected static final String SEQ_USER = "SEQ_EG_USER";
    private static final long serialVersionUID = -2415368058955783970L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_USER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(min = 2, max = 64)
    @SafeHtml
    @Column(unique = true, updatable = false)
    @Pattern(regexp = USERNAME, message = INVALID_USERNAME)
    private String username;

    @NotNull
    @Length(min = 4, max = 64)
    @Audited
    @SafeHtml
    private String password;

    @SafeHtml
    @Length(max = 10)
    @Pattern(regexp = SALUTATION, message = INVALID_SALUTATION)
    private String salutation;

    @SafeHtml
    @Length(max = 64)
    @Pattern(regexp = PERSON_NAME, message = INVALID_PERSON_NAME)
    private String guardian;

    @SafeHtml
    @Length(max = 64)
    private String guardianRelation;

    @NotBlank
    @SafeHtml
    @Length(min = 2, max = 100)
    @Audited
    @Pattern(regexp = PERSON_NAME, message = INVALID_PERSON_NAME)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @SafeHtml
    @Length(max = 10)
    @Audited
    @Pattern(regexp = MOBILE_NUMBER, message = INVALID_MOBILE_NUMBER)
    private String mobileNumber;

    @SafeHtml
    @Length(max = 128)
    @Audited
    @Email(regexp = EMAIL)
    private String emailId;

    @SafeHtml
    @Length(max = 15)
    @Pattern(regexp = PHONE_NUMBER, message = INVALID_PHONE_NUMBER)
    private String altContactNumber;

    @SafeHtml
    @Length(max = 10)
    @Pattern(regexp = PAN_NUMBER, message = INVALID_PAN_NUMBER)
    private String pan;

    @SafeHtml
    @Length(max = 12)
    @Type(type = "encryptedString")
    private String aadhaarNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Address> address = new ArrayList<>();

    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    @Audited(targetAuditMode = NOT_AUDITED)
    @AuditJoinTable
    private Set<Role> roles = new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date dob;

    @NotNull
    private Date pwdExpiryDate = new Date();

    @NotBlank
    @Length(max = 15)
    @SafeHtml
    private String locale = "en_IN";

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false)
    private UserType type;

    private byte[] signature;

    private boolean accountLocked;

    private boolean useMultiFA;

    @NotAudited
    @NaturalId
    @Column(nullable = false, unique = true, updatable = false)
    @Length(max = 36)
    private String uid;

    public User() {
        //Default constructor
    }

    public User(UserType type) {
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAltContactNumber() {
        return altContactNumber;
    }

    public void setAltContactNumber(String altContactNumber) {
        this.altContactNumber = altContactNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMaskedAadhaarNumber() {
        return overlay(getAadhaarNumber(), repeat('*', 9), 1, 10);
    }

    @JsonIgnore
    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public void addAddress(Address address) {
        address.setUser(this);
        this.address.add(address);
    }

    public void removeAddress(Address address) {
        getAddress().remove(address);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        getRoles().add(role);
    }

    public void removeRole(Role role) {
        getRoles().remove(role);
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public DateTime getPwdExpiryDate() {
        return null == pwdExpiryDate ? null : new DateTime(pwdExpiryDate);
    }

    public void setPwdExpiryDate(Date pwdExpiryDate) {
        this.pwdExpiryDate = pwdExpiryDate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Locale locale() {
        return LocaleUtils.toLocale(locale);
    }

    public UserType getType() {
        return type;
    }

    protected void setType(UserType userType) {
        type = userType;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    @JsonIgnore
    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isUseMultiFA() {
        return useMultiFA;
    }

    public void setUseMultiFA(boolean useMultiFA) {
        this.useMultiFA = useMultiFA;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void generateUID() {
        this.setUid(UUID.randomUUID().toString());
    }

    public void updateNextPwdExpiryDate(Integer passwordExpireInDays) {
        this.setPwdExpiryDate(new DateTime().plusDays(passwordExpireInDays).toDate());
    }

    public boolean hasRole(String roleName) {
        return roles.stream().map(Role::getName).anyMatch(roleName::equals);
    }

    public boolean hasAnyRole(String... roleName) {
        List<String> roleNames = Arrays.asList(roleName);
        return roles.stream().anyMatch(role -> roleNames.contains(role.getName()));
    }

    public boolean hasAnyInternalRole() {
        return roles.stream().anyMatch(role -> role.isInternal());
    }

    public boolean hasAnyType(UserType... types) {
        return Stream.of(types).anyMatch(userType -> getType().equals(userType));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof User))
            return false;
        User user = (User) other;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
