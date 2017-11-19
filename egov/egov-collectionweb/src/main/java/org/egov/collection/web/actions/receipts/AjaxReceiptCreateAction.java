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
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.ServiceAccountDetails;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.models.ServiceSubledgerInfo;
import org.hibernate.HibernateException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@ParentPackage("egov")
@Results({ @Result(name = "schemeList", location = "ajaxReceiptCreate-schemeList.jsp"),
        @Result(name = "subSchemeList", location = "ajaxReceiptCreate-subSchemeList.jsp"),
        @Result(name = AjaxReceiptCreateAction.SERVICE_LIST, location = "ajaxReceiptCreate-serviceList.jsp"),
        @Result(name = "serviceAccDtls", location = "ajaxReceiptCreate-serviceAccDtls.jsp"),
        @Result(name = "subledger", location = "ajaxReceiptCreate-subledger.jsp"),
        @Result(name = "entities", location = "ajaxReceiptCreate-entities.jsp"),
        @Result(name = AjaxBankRemittanceAction.USERLIST, location = "ajaxReceiptCreate-userList.jsp"),
        @Result(name = AjaxReceiptCreateAction.RESULT, location = "ajaxReceiptCreate-result.jsp") })
public class AjaxReceiptCreateAction extends BaseFormAction {
    private static final Logger LOGGER = Logger.getLogger(AjaxReceiptCreateAction.class);
    private static final long serialVersionUID = 1L;
    private static final String DETAILTYPEID = "detailtypeid";
    protected static final String RESULT = "result";
    private static final String SERVICECATID = "serviceCatId";
    private static final String SERVICEID = "serviceId";
    protected static final String SERVICE_LIST = "serviceList";
    private String value;
    private List<EntityType> entityList;
    private static final String accountDetailTypeQuery = " from Accountdetailtype where id=?";
    private List<Scheme> schemeList;
    private List<SubScheme> subSchemes;
    private List<ServiceDetails> serviceList;
    private List<ServiceAccountDetails> accountDetails;
    private List<ServiceSubledgerInfo> subledgerDetails;
    private String serviceClass;
    private EgovCommon egovCommon;
    private Long paymentServiceId;
    private List<User> userList;
    protected static final String USER_LIST = "userList";

    public String getAccountForService() {
        setValue(CollectionConstants.BLANK);
        final String serviceId = parameters.get(SERVICEID)[0];
        final String queryString = "select sd.serviceAccount from ServiceDetails sd where sd.id='" + serviceId + "'";
        final List<CChartOfAccounts> list = getPersistenceService().findAllBy(queryString);
        for (final CChartOfAccounts accounts : list)
            value += accounts.getId().toString() + "~" + accounts.getGlcode() + "~" + accounts.getName() + "#";

        return RESULT;
    }

    public String getMISdetailsForService() {
        value = "";
        final String serviceId = parameters.get(SERVICEID)[0];

        final ServiceDetails service = (ServiceDetails) getPersistenceService().find(
                " from ServiceDetails where id=? ", Long.valueOf(serviceId));

        if (null != service)
            for (final Department department : service.getServiceDept())
                value = (null != service.getFund() ? service.getFund().getId() : -1) + "~"
                        + (null != department ? department.getId() : -1) + "#";

        return RESULT;
    }

    @Action(value = "/receipts/ajaxReceiptCreate-getDetailCode")
    public String getDetailCode() throws ApplicationException {
        value = "";
        final String accountCodes = parameters.get("accountCodes")[0];
        final String arr[] = accountCodes.split(",");

        for (final String element : arr) {
            final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
                    " from CChartOfAccountDetail" + " where glCodeId=(select id from CChartOfAccounts where glcode=?)",
                    element);

            if (null != chartOfAccountDetail)
                value += element + "~" + chartOfAccountDetail.getGlCodeId().getId().toString() + "~";
        }
        if (StringUtils.isNotBlank(value))
            value = value.substring(0, value.length() - 1);
        return RESULT;
    }

    @Action(value = "/receipts/ajaxReceiptCreate-getDetailType")
    public String getDetailType() throws ApplicationException {
        value = "";
        final String accountCode = parameters.get("accountCode")[0];
        final String index = parameters.get(INDEX)[0];
        final String selectedDetailType = parameters.get("selectedDetailType")[0];
        final String onload = parameters.get("onload")[0];
        final List<Accountdetailtype> list = getPersistenceService()
                .findAllBy(
                        " from Accountdetailtype"
                                + " where id in (select detailTypeId from CChartOfAccountDetail where glCodeId=(select id from CChartOfAccounts where glcode=?))  ",
                        accountCode);
        if (list == null || list.isEmpty())
            value = index + "~" + ERROR + "#";
        else
            for (final Accountdetailtype accountdetailtype : list)
                value = value + index + "~" + selectedDetailType + "~" + onload + "~" + accountdetailtype.getName()
                        + "~" + accountdetailtype.getId().toString() + "#";
        if (StringUtils.isNotBlank(value))
            value = value.substring(0, value.length() - 1);
        return RESULT;
    }

    /**
     * This method is accessed from challan.js and MiscReceipts.js
     *
     * @return
     * @throws Exception
     */
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew")
    public String ajaxValidateDetailCodeNew() throws Exception {
        final String code = parameters.get("code")[0];
        final String index = parameters.get(INDEX)[0];
        final String codeorname = parameters.get("codeorname")[0];

        final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(accountDetailTypeQuery,
                Integer.valueOf(parameters.get(DETAILTYPEID)[0]));
        if (adt == null) {
            value = index + "~" + ERROR + "#";
            return RESULT;
        }

        final String table = adt.getFullQualifiedName();
        final Class<?> service = Class.forName(table);
        String simpleName = service.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

        final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                .getServletContext());
        final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
        entityList = (List<EntityType>) entityService.filterActiveEntities(code, -1, adt.getId());

        if (entityList == null || entityList.isEmpty())
            value = index + "~" + ERROR + "#";
        else {
            if (entityList.size() > 1) {// To Check with same code/name if more
                // than one entity is returned
                value = index + "~" + ERROR + "#";
                return RESULT;
            }
            for (final EntityType entity : entityList)
                if (entity == null) {
                    value = index + "~" + ERROR + "#";
                    break;
                } else if (codeorname.equalsIgnoreCase("both")) {// To Check if
                    // both name
                    // and code has
                    // to be
                    // compared
                    if (entity.getName().equals(code) || entity.getCode().equals(code)) {
                        value = index + "~" + entity.getEntityId() + "~" + entity.getName() + "~" + entity.getCode();
                        break;
                    } else
                        value = index + "~" + ERROR + "#";
                } else if (entity.getCode().equals(code)) {// In case of view
                    // mode, to get the
                    // details from the
                    // code
                    value = index + "~" + entity.getEntityId() + "~" + entity.getName() + "~" + entity.getCode();
                    break;
                } else
                    value = index + "~" + ERROR + "#";
        }

        return RESULT;
    }

    @Action(value = "/receipts/ajaxReceiptCreate-getCodeNew")
    public String getCodeNew() throws Exception {
        value = "";
        final String detailTypeId = parameters.get("detailTypeId")[0];
        final String filterKey = parameters.get("filterKey")[0];
        final Integer accountDetailTypeId = Integer.valueOf(detailTypeId);
        final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(accountDetailTypeQuery,
                accountDetailTypeId);
        if (adt == null)
            return RESULT;
        final String table = adt.getFullQualifiedName();
        final Class<?> service = Class.forName(table);
        String simpleName = service.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

        final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                .getServletContext());
        final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
        final List<EntityType> tempEntityList = (List<EntityType>) entityService.filterActiveEntities(filterKey, -1,
                adt.getId());
        entityList = new ArrayList<EntityType>();
        for (final EntityType e : tempEntityList) {
            if (e.getName().contains("@") || e.getName().contains("#") || e.getName().contains("$")
                    || e.getName().contains("%") || e.getName().contains("^") || e.getName().contains("&")
                    || e.getName().contains("*"))
                e.getName().replace("@", " ").replace("#", " ").replace("$", " ").replace("%", " ").replace("^", " ")
                        .replace("&", " ").replace("*", " ");
            entityList.add(e);
        }
        return "entities";
    }

    /*
     * This code has to be deleted once autocomplete feature is changed in misc Receipts also
     */
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxValidateDetailCode")
    public String ajaxValidateDetailCode() {
        final String code = parameters.get("code")[0];
        final String index = parameters.get(INDEX)[0];
        try {

            final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(accountDetailTypeQuery,
                    Integer.valueOf(parameters.get(DETAILTYPEID)[0]));
            if (adt == null) {
                value = index + "~" + ERROR + "#";
                return RESULT;
            }

            final List<EntityType> entityList = getPersistenceService().findAllBy(
                    " from " + adt.getFullQualifiedName() + ""
                            + " where id in (select detailkey from Accountdetailkey where accountdetailtype.id=?)  ",
                    Integer.valueOf(parameters.get(DETAILTYPEID)[0]));

            if (getEntityList() == null || getEntityList().isEmpty())
                value = index + "~" + ERROR + "#";
            else
                for (final EntityType entity : entityList)
                    if (entity == null) {
                        value = index + "~" + ERROR + "#";
                        break;
                    } else if (entity.getCode().equals(code)) {
                        final Accountdetailkey key = (Accountdetailkey) getPersistenceService().find(
                                " from Accountdetailkey where accountdetailtype.id=? and detailkey=? ",
                                Integer.valueOf(parameters.get(DETAILTYPEID)[0]), entity.getEntityId());
                        if (key == null)
                            value = index + "~" + ERROR + "#";
                        else
                            value = index + "~" + key.getId() + "~" + entity.getName();
                        break;
                    } else
                        value = index + "~" + ERROR + "#";

        } catch (final HibernateException e) {
            value = index + "~" + ERROR + "#";
        } catch (final Exception e) {
            value = index + "~" + ERROR + "#";
        }
        return RESULT;
    }

    public String getCode() throws ApplicationException {
        value = "";
        final String detailTypeId = parameters.get("detailTypeId")[0];
        final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(accountDetailTypeQuery,
                Integer.valueOf(detailTypeId));

        if (adt == null)
            return RESULT;

        setEntityList(getPersistenceService().findAllBy(
                "select entity from " + adt.getFullQualifiedName() + " entity,Accountdetailkey adk"
                        + " where entity.id =adk.detailkey and adk.accountdetailtype.id=? ",
                Integer.valueOf(detailTypeId)));

        return "entities";
    }

    @Action(value = "/receipts/ajaxReceiptCreate-getDetailTypeForService")
    public String getDetailTypeForService() throws ApplicationException {

        value = "";
        final String accountCode = parameters.get("accountCode")[0];
        final String index = parameters.get(INDEX)[0];

        final List<Accountdetailtype> list = getPersistenceService()
                .findAllBy(
                        " from Accountdetailtype"
                                + " where id in (select detailTypeId from CChartOfAccountDetail where glCodeId=(select id from CChartOfAccounts where glcode=?))  ",
                        accountCode);

        for (final Accountdetailtype accountdetailtype : list)
            value = value + index + "~" + accountdetailtype.getDescription() + "~"
                    + accountdetailtype.getId().toString() + "#";
        if (!"".equals(value))
            value = value.substring(0, value.length() - 1);

        return RESULT;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadSchemes")
    public String ajaxLoadSchemes() {

        final Integer fundId = Integer.valueOf(parameters.get("fundId")[0]);
        if (null == fundId || fundId == -1)
            schemeList = getPersistenceService().findAllBy(
                    " from Scheme where fund.id=? and isActive=true order by name", -1);
        else
            schemeList = getPersistenceService().findAllBy(
                    " from Scheme where fund.id=? and isActive=true order by name", fundId);

        return "schemeList";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadSubSchemes")
    public String ajaxLoadSubSchemes() {
        final Integer schemeId = Integer.valueOf(parameters.get("schemeId")[0]);
        if (null != schemeId && schemeId != -1)
            subSchemes = getPersistenceService().findAllBy(
                    "from SubScheme where scheme.id=? and isActive=true order by name", schemeId);
        else
            subSchemes = Collections.emptyList();

        return "subSchemeList";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadServiceByCategory")
    public String ajaxLoadServiceByCategory() {

        if (null != parameters.get(SERVICECATID) && null != parameters.get(SERVICECATID)[0]
                && Integer.valueOf(parameters.get(SERVICECATID)[0]) != -1)
            serviceList = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_DETAIL_BY_CATEGORY,
                    Long.valueOf(parameters.get(SERVICECATID)[0]), Boolean.TRUE);
        else
            serviceList = Collections.emptyList();

        return SERVICE_LIST;

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadServiceByCategoryForChallan")
    public String ajaxLoadServiceByCategoryForChallan() {

        if (null != parameters.get(SERVICECATID) && null != parameters.get(SERVICECATID)[0]
                && Integer.valueOf(parameters.get(SERVICECATID)[0]) != -1)
            serviceList = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE,
                    Long.valueOf(parameters.get(SERVICECATID)[0]), CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION,
                    Boolean.TRUE);
        else
            serviceList = Collections.emptyList();

        return SERVICE_LIST;

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadServiceByCategoryForMisc")
    public String ajaxLoadServiceByCategoryForMisc() {

        if (null != parameters.get(SERVICECATID) && null != parameters.get(SERVICECATID)[0]
                && Integer.valueOf(parameters.get(SERVICECATID)[0]) != -1)
            serviceList = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE,
                    Long.valueOf(parameters.get(SERVICECATID)[0]), CollectionConstants.SERVICE_TYPE_MISC_COLLECTION,
                    Boolean.TRUE);
        else
            serviceList = Collections.emptyList();

        return SERVICE_LIST;

    }

    @SuppressWarnings("unchecked")
    @Action(value = "/receipts/ajaxReceiptCreate-ajaxLoadServiceByClassification")
    public String ajaxLoadServiceByClassification() {
        if (serviceClass != null && !serviceClass.equals("-1"))
            serviceList = getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE,
                    serviceClass);
        else
            serviceList = Collections.emptyList();

        return SERVICE_LIST;

    }

    @Action(value = "/receipts/ajaxReceiptCreate-ajaxFinMiscDtlsByService")
    public String ajaxFinMiscDtlsByService() {

        final Long serviceId = Long.valueOf(parameters.get(SERVICEID)[0]);
        final ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_ID, serviceId);

        final StringBuilder miscDetails = new StringBuilder();
        if (null != service)
            miscDetails.append(null != service.getFund() ? service.getFund().getId() : "-1").append('~') // fund
                    .append(null != service.getScheme() ? service.getScheme().getId() : "-1").append('~') // scheme
                    .append(null != service.getSubscheme() ? service.getSubscheme().getId() : "-1").append('~') // subscheme
                    .append(null != service.getFundSource() ? service.getFundSource().getId() : "-1").append('~') // fundsource
                    .append(null != service.getFunctionary() ? service.getFunctionary().getId() : "-1").append('~') // functionary
                    .append(null != service.getFunction() ? service.getFunction().getId() : "-1"); // function
        else
            miscDetails.append("-1").append('~') // fund
                    .append("-1").append('~') // scheme
                    .append("-1").append('~') // subscheme
                    .append("-1").append('~') // fundsource
                    .append("-1").append('~') // functionary
                    .append("-1"); // function
        value = miscDetails.toString();
        return RESULT;

    }

    @Action(value = "/receipts/ajaxReceiptCreate-ajaxFinAccDtlsByService")
    public String ajaxFinAccDtlsByService() {

        final Long serviceId = Long.valueOf(parameters.get(SERVICEID)[0]);
        final ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_ID, serviceId);
        accountDetails = new ArrayList<>();
        if (null != service)
            accountDetails.addAll(service.getServiceAccountDtls());
        else
            accountDetails.addAll(Collections.emptyList());

        return "serviceAccDtls";

    }

    @Action(value = "/receipts/ajaxReceiptCreate-ajaxFinSubledgerByService")
    public String ajaxFinSubledgerByService() {
        final Long serviceId = Long.valueOf(parameters.get(SERVICEID)[0]);
        final ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_ID, serviceId);
        subledgerDetails = new ArrayList<>();
        ServiceSubledgerInfo servicInfo;
        if (null != service)
            for (final ServiceAccountDetails account : service.getServiceAccountDtls()) {
                subledgerDetails.addAll(account.getSubledgerDetails());
                if (subledgerDetails.isEmpty()) {
                    final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService()
                            .find("from CChartOfAccountDetail cd where cd.glCodeId.id=?", account.getGlCodeId().getId());
                    servicInfo = new ServiceSubledgerInfo();
                    if (chartOfAccountDetail != null) {
                        servicInfo.setDetailType(chartOfAccountDetail.getDetailTypeId());
                        servicInfo.setServiceAccountDetail(account);
                        subledgerDetails.add(servicInfo);
                    } else
                        subledgerDetails.addAll(Collections.emptyList());
                } else
                    for (final ServiceSubledgerInfo serviceSubledgerInfo : subledgerDetails)
                        if (serviceSubledgerInfo.getDetailType() != null && serviceSubledgerInfo.getDetailKeyId() != null) {
                            EntityType entityType = null;
                            try {
                                entityType = egovCommon.getEntityType(serviceSubledgerInfo.getDetailType(),
                                        serviceSubledgerInfo.getDetailKeyId());
                            } catch (final ApplicationException e) {
                                LOGGER.error("Exception while setting subledger details", e);
                                throw new ApplicationRuntimeException("Exception while setting subledger details", e);
                            }
                            if (entityType != null) {
                                serviceSubledgerInfo.setDetailCode(entityType.getCode());
                                serviceSubledgerInfo.setDetailKey(entityType.getName());
                            }
                        }
            }
        return "subledger";
    }

    @Action(value = "/receipts/ajaxReceiptCreate-ajaxOnlineReceiptCreatedByList")
    public String ajaxOnlineReceiptCreatedByList() {
        if (paymentServiceId != null)
            userList = persistenceService.findAllByNamedQuery(
                    CollectionConstants.QUERY_CREATEDBYUSERS_OF_PAYMENT_RECEIPTS, paymentServiceId);
        return USER_LIST;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public List<EntityType> getEntityList() {
        return entityList;
    }

    public void setEntityList(final List<EntityType> entityList) {
        this.entityList = entityList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public List<Scheme> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(final List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }

    public List<SubScheme> getSubSchemes() {
        return subSchemes;
    }

    public void setSubSchemes(final List<SubScheme> subSchemes) {
        this.subSchemes = subSchemes;
    }

    public List<ServiceDetails> getServiceList() {
        return serviceList;
    }

    public List<ServiceAccountDetails> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(final List<ServiceAccountDetails> accountDetails) {
        this.accountDetails = accountDetails;
    }

    public List<ServiceSubledgerInfo> getSubledgerDetails() {
        return subledgerDetails;
    }

    public void setSubledgerDetails(final List<ServiceSubledgerInfo> subledgerDetails) {
        this.subledgerDetails = subledgerDetails;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(final String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public Long getPaymentServiceId() {
        return paymentServiceId;
    }

    public void setPaymentServiceId(final Long paymentServiceId) {
        this.paymentServiceId = paymentServiceId;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(final List<User> userList) {
        this.userList = userList;
    }

}
