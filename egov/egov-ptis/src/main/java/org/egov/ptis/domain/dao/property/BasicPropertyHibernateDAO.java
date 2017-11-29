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
package org.egov.ptis.domain.dao.property;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;

@Repository(value = "basicPropertyDAO")
@Transactional(readOnly = true)
public class BasicPropertyHibernateDAO implements BasicPropertyDAO {
    private final static Logger LOGGER = Logger.getLogger(BasicPropertyHibernateDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public BasicProperty getBasicPropertyByRegNum(String RegNum) {
        Query qry = getCurrentSession().createQuery(
                "from BasicPropertyImpl BP where BP.regNum =:RegNum and BP.active='Y' ");

        qry.setString("RegNum", RegNum);
        // qry.setMaxResults(1);
        return (BasicProperty) qry.uniqueResult();
    }

    @Override
    public BasicProperty getBasicPropertyByRegNumNew(String RegNum) {
        Query qry = getCurrentSession().createQuery(
                "from BasicPropertyImpl BP "
                        + "left join fetch BP.property pi "
                        + "left join fetch BP.ptAgent pangt "
                        + "left join fetch pangt.basicProperty bppangt "
                        + "left join fetch BP.address add "
                        +
                        // "left join fetch BP.propertyID pid " +
                        // "left join fetch pid.basicProperty bpid " +
                        "left join fetch pi.ptdcbBroker ptdcb "
                        + "left join fetch pi.propertyOwnerSet prowns  "
                        + "left join fetch ptdcb.dcb dcb "
                        + "left join fetch dcb.currentDemand currDmd "
                        + "left join fetch dcb.aggArrearsDemand arrDmd "
                        + "left join fetch arrDmd.dcb " + "left join fetch currDmd.dcb "
                        + "left join fetch currDmd.cesses " + "left join fetch currDmd.penalties "
                        + "left join fetch currDmd.exemptions " + "left join fetch arrDmd.cesses "
                        + "left join fetch arrDmd.penalties "
                        + "left join fetch arrDmd.exemptions "
                        + "where BP.regNum =:RegNum and BP.active='Y' ");
        qry.setString("RegNum", RegNum);
        // qry.setMaxResults(1);
        return (BasicProperty) qry.uniqueResult();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.ptis.domain.dao.property.BasicPropertyDAO#getBasicPropertyByPropertyID(java.lang.String)
     */
    @Override
    public BasicProperty getBasicPropertyByPropertyID(String propertyId) {
        Query qry = null;
        BasicProperty basicProperty = null;
        if (propertyId != null && !propertyId.equals("")) {
            qry = getCurrentSession().createQuery(
                    "from BasicPropertyImpl BP where BP.upicNo =:propertyId and BP.active='Y' ");
            qry.setString("propertyId", propertyId);
            basicProperty = (BasicProperty) qry.uniqueResult();
        }
        return basicProperty;
    }

    /*
     * By passing propertyId as parameter this method will give BasicProeprty Object.
     */
    @Override
    public BasicProperty getAllBasicPropertyByPropertyID(String propertyId) {
        Query qry = null;
        BasicProperty basicProperty = null;
        if (propertyId != null && !propertyId.equals("")) {
            qry = getCurrentSession().createQuery(
                    "from BasicPropertyImpl BP where BP.upicNo =:propertyId");
            qry.setString("propertyId", propertyId);
            basicProperty = (BasicProperty) qry.uniqueResult();
        }
        return basicProperty;
    }

    @Override
    public BasicProperty getBasicPropertyByPropertyID(PropertyID propertyID) {

        Query qry = getCurrentSession().createQuery(
                "from BasicPropertyImpl BP where BP.propertyID =:PropertyID and BP.active='Y' ");
        qry.setEntity("PropertyID", propertyID);
        return (BasicProperty) qry.uniqueResult();
    }

    /*
     * public BasicProperty getBasicPropertyByID(String ID) { Query qry = getSession().createQuery("from BasicProperty BP where
     * bp.ID =: ID and BP.active='Y' "); qry.setString("ID", ID); return (BasicProperty)qry.uniqueResult(); }
     */

    @Override
    public BasicProperty getInActiveBasicPropertyByPropertyID(String propertyId) {
        Query qry = null;
        BasicProperty basicProperty = null;
        if (propertyId != null && !propertyId.equals("")) {
            qry = getCurrentSession().createQuery(
                    "from BasicPropertyImpl BP where BP.upicNo =:propertyId and BP.active='N' ");
            qry.setString("propertyId", propertyId);
            basicProperty = (BasicProperty) qry.uniqueResult();
        }
        return basicProperty;
    }

    @Override
    public BasicProperty getBasicPropertyByID_PropertyID(String ID_PropertyID) {
        Query qry = getCurrentSession()
                .createQuery(
                        "from BasicPropertyImpl BP where bp.ID_PropertyID =:ID_PropertyID  and BP.active='Y'");
        qry.setString("ID_PropertyID", ID_PropertyID);
        return (BasicProperty) qry.uniqueResult();
    }

    @Override
    public Integer getRegNum() {
        Integer regNum = null;
        ResultSet resultSet = null;
        try {
            Query query = getCurrentSession().createSQLQuery("SELECT REG_NUM.NEXTVAL from dual");

            resultSet = (ResultSet) query.list();
            if (resultSet.next()) {
                regNum = resultSet.getInt(1);
            } else {
                throw new ApplicationException("Could not generate Reg Num. Result is empty.");
            }

        } catch (SQLException e) {
            LOGGER.info("Exception in getRegNum()--- BasicPropertyHibernateDAO---" + e.getMessage());
            throw new ApplicationRuntimeException("Could not generate Reg Num, " + e);
        } catch (Exception e) {
            LOGGER.info("Exception in getRegNum()--- BasicPropertyHibernateDAO---" + e);
            throw new ApplicationRuntimeException("Could not generate Reg Num, " + e);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {

            }
        }
        return regNum;
    }

    @Override
    public Integer getVoucherNum() {
        Integer voucherNum = null;
        try {
            Query query = getCurrentSession().createSQLQuery(
                    "SELECT SEQ_VOUCHER_NUM.NEXTVAL from dual");

            ResultSet resultSet = (ResultSet) query.list();
            if (resultSet.next()) {
                voucherNum = resultSet.getInt(1);
            } else {
                throw new ApplicationException("Could not generate Voucher Num. Result is empty.");
            }

        } catch (SQLException e) {
            LOGGER.info("Exception in getVoucherNum()--- BasicPropertyHibernateDAO---"
                    + e.getMessage());
            throw new ApplicationRuntimeException("Could not generate Voucher Num, " + e);
        } catch (Exception e) {
            LOGGER.info("Exception in getVoucherNum()--- BasicPropertyHibernateDAO---"
                    + e.getMessage());
            throw new ApplicationRuntimeException("Could not generate Voucher Num, " + e);
        }
        return voucherNum;
    }

    /*
     * added by suhasini by passing oldMuncipalNo as parameter this method will give list of BasicProeprty Objects.
     */

    @Override
    public List getBasicPropertyByOldMunipalNo(String oldMuncipalNo) {
        // logger.info(">>>>>>>>>>>>>>>>>> oldMuncipalNo"+oldMuncipalNo);
        Query qry = getCurrentSession()
                .createQuery(
                        "from BasicPropertyImpl BP where BP.oldMuncipalNum =:oldMuncipalNo and BP.active='Y' ");
        qry.setString("oldMuncipalNo", oldMuncipalNo);
        // qry.setMaxResults(1);
        return qry.list();
    }

    @Override
    public List<BasicPropertyImpl> getChildBasicPropsForParent(BasicProperty basicProperty) {
        List<BasicPropertyImpl> basicPropList = new ArrayList<BasicPropertyImpl>();
        if (basicProperty != null) {
            Query qry = getCurrentSession()
                    .createQuery(
                            "from BasicPropertyImpl BP left join fetch BP.propertyStatusValuesSet PSV left join fetch PSV.propertyStatus PS where PSV.referenceBasicProperty =:BasicPropertyId and PS.statusCode = 'CREATE' and PSV.isActive='Y' ");
            qry.setString("BasicPropertyId", basicProperty.getId().toString());
            basicPropList = qry.list();
        }
        return basicPropList;
    }

    /*
     * By passing assessmentNo and parcelID as parameter this method will give BasicProeprty Object.
     */
    @Override
    public BasicProperty getBasicPropertyByIndexNumAndParcelID(String assessmentNo, String parcelID) {
        Query qry = null;
        BasicProperty basicProperty = null;
        Boolean assessmentFound = Boolean.FALSE;
        boolean parcelFound = Boolean.FALSE;
        StringBuffer strquery = new StringBuffer(200);
        strquery.append("from BasicPropertyImpl BP where BP.active='Y' ");
        if (assessmentNo != null && !assessmentNo.equals("")) {
            assessmentFound = Boolean.TRUE;
            strquery.append(" and BP.upicNo=:assessmentNo");
        }
        if (parcelID != null && !parcelID.equals("")) {
            parcelFound = Boolean.TRUE;
            strquery.append(" and BP.gisReferenceNo =:parcelID");
        }
        qry = getCurrentSession().createQuery(strquery.toString());
        if (assessmentFound) {
            qry.setString("assessmentNo", assessmentNo);
        }
        if (parcelFound) {
            qry.setString("parcelID", parcelID);
        }
        basicProperty = (BasicProperty) qry.uniqueResult();
        return basicProperty;
    }

    @Override
    public List<BasicProperty> getBasicPropertiesForTaxDetails(String circleName, String zoneName,
            String wardName, String blockName, String ownerName, String doorNo, String aadhaarNumber, String mobileNumber) {

        List<BasicProperty> basicPropertyList = new ArrayList<BasicProperty>();

        BasicProperty basicProperty = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select * from (select distinct bp.isactive is_active, bp.propertyid, "
                + "pd.zone_num, bdz.name z_name, pd.ward_adm_id, bdw.name w_name, pd.adm1, "
                + "bdb.name b_name, u.name wn, adr.id, adr.housenobldgapt d_no from egpt_basic_property bp "
                + "left join egpt_propertyid pd on bp.id = pd.id "
                + "left join egpt_property_owner_info info on pd.id = info.basicproperty "
                + "left join eg_user u on info.owner =u.id "
                + "left join eg_boundary bdz on pd.zone_num = bdz.id "
                + "left join eg_boundary bdw on pd.ward_adm_id = bdw.id "
                + "left join eg_boundary bdb on pd.adm1 = bdb.id "
                + "left join eg_address adr on bp.addressid = adr.id ) as prop_det "
                + "where prop_det.is_active = 'Y' "
                + "and prop_det.wn like '%" + (ownerName != null ? ownerName.trim() : "") + "%' "
                + "and prop_det.z_name like '%" + (zoneName != null ? zoneName.trim() : "") + "%' "
                + "and prop_det.w_name like '%" + (wardName != null ? wardName.trim() : "") + "%' "
                + "and prop_det.b_name like '%" + (blockName != null ? blockName.trim() : "") + "%' "
                + "and prop_det.d_no like '%" + (doorNo != null ? doorNo.trim() : "") + "%'");

        Query query = getCurrentSession().createSQLQuery(sb.toString());

        List list = query.list();
        if (null != list && !list.isEmpty()) {
            for (Object record : list) {
                Object[] data = (Object[]) record;
                if (null != data[1]) {
                    basicProperty = getBasicPropertyByPropertyID((String) data[1]);
                    basicPropertyList.add(basicProperty);
                }
            }
        }
        return basicPropertyList;
    }

    @Override
    public BasicProperty findById(Integer id, boolean lock) {

        return null;
    }

    @Override
    public List<BasicProperty> findAll() {
        Query qry = getCurrentSession().createQuery("from BasicPropertyImpl BP where BP.active='Y'");
        return qry.list();
    }

    @Override
    public BasicProperty create(BasicProperty entity) {

        return null;
    }

    @Override
    public void delete(BasicProperty entity) {


    }

    @Override
    public BasicProperty update(BasicProperty entity) {

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> getBoundaryIds(String boundaryName) {
        List<Long> boundryIds = null;
        Query bndryQuery = getCurrentSession().createQuery("from Boundary b where b.name like :boundaryName");
        bndryQuery.setString("boundaryName", "%" + boundaryName.trim() + "%");
        List<Boundary> boundaries = bndryQuery.list();
        if (null != boundaries && !boundaries.isEmpty()) {
            boundryIds = new ArrayList<Long>();
            for (Boundary boundary : boundaries) {
                boundryIds.add(boundary.getId());
            }
        }
        return boundryIds;
    }

    @Override
    public Boolean isBoundaryExist(String boundaryName) {
        Boolean isBoundaryExist = Boolean.FALSE;
        if (null != boundaryName && !boundaryName.trim().equals("")) {
            Query bndryQuery = getCurrentSession().createQuery("from Boundary b where b.name like :boundaryName");
            bndryQuery.setString("boundaryName", "%" + boundaryName.trim() + "%");
            if (null != bndryQuery.list() && !bndryQuery.list().isEmpty()) {
                isBoundaryExist = Boolean.TRUE;
            }
        }
        return isBoundaryExist;
    }

    @Override
    public Boolean isOwnerNameExist(String ownerName) {
        Boolean isOwnerNameExist = Boolean.FALSE;
        if (null != ownerName && !ownerName.trim().equals("")) {
            Query ownerNameQuery = getCurrentSession().createQuery(
                    "from PropertyOwnerInfo info where info.owner.name like :ownerName");
            ownerNameQuery.setString("ownerName", "%" + ownerName.trim() + "%");
            if (null != ownerNameQuery.list() && !ownerNameQuery.list().isEmpty()) {
                isOwnerNameExist = Boolean.TRUE;
            }
        }
        return isOwnerNameExist;
    }

    @Override
    public Boolean isDoorNoExist(String doorNo) {
        Boolean isDoorNoExist = Boolean.FALSE;
        if (null != doorNo && !doorNo.trim().equals("")) {
            Query doorNoQuery = getCurrentSession().createQuery(
                    "from BasicPropertyImpl bp where bp.address.houseNoBldgApt like :doorNo");
            doorNoQuery.setString("doorNo", "%" + doorNo.trim() + "%");
            if (null != doorNoQuery.list() && !doorNoQuery.list().isEmpty()) {
                isDoorNoExist = Boolean.TRUE;
            }
        }
        return isDoorNoExist;
    }

    @Override
    public Boolean isAssessmentNoExist(String assessmentNo) {
        Boolean isAssessmentNoExist = Boolean.FALSE;
        if (null != assessmentNo && !assessmentNo.trim().equals("")) {
            Query doorNoQuery = getCurrentSession().createQuery("from BasicPropertyImpl bp where bp.upicNo =:assessmentNo and bp.active = 'Y' ");
            doorNoQuery.setString("assessmentNo", assessmentNo.trim());
            if (null != doorNoQuery.list() && !doorNoQuery.list().isEmpty()) {
                isAssessmentNoExist = Boolean.TRUE;
            }
        }
        return isAssessmentNoExist;
    }

    /**
     * Returns Parent basic property of a basic property if it is bifurcated else returns null
     */
    @Override
    public BasicProperty getParentBasicPropertyByBasicPropertyId(Long basicPropertyId) {
        BasicProperty basicProperty = (BasicProperty) getCurrentSession()
                .createQuery("select psv.referenceBasicProperty from PropertyStatusValues psv where psv.basicProperty.id = :id")
                .setParameter("id", basicPropertyId).uniqueResult();
        return basicProperty;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BasicProperty> getBasicPropertiesForTaxDetails(String assessmentNo, String ownerName, String mobileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct bp.propertyid from egpt_basic_property bp left join egpt_property_owner_info info on bp.id = info.basicproperty "
                + "left join eg_user u on info.owner = u.id where bp.isactive = 'Y' and bp.propertyid is not null ");
        Map<String, String> params = new HashMap<String, String>();
        if (assessmentNo != null && !assessmentNo.trim().isEmpty()) {
            sb.append(" and bp.propertyId=:assessmentNo ");
            params.put("assessmentNo", assessmentNo);
        }
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            sb.append(" and upper(trim(u.name)) like :OwnerName ");
            params.put("OwnerName", "%" + ownerName.toUpperCase() + "%");
        }
        if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
            sb.append(" and u.mobileNumber like :MobileNumber ");
            params.put("MobileNumber", mobileNumber);
        }
        final Query query = getCurrentSession().createSQLQuery(sb.toString());
        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
        }
        List<String> list = query.list();
        List<BasicProperty> basicProperties = new ArrayList<BasicProperty>();
        if (null != list && !list.isEmpty()) {
            for (String propertyid : list) {
                basicProperties.add(getBasicPropertyByPropertyID(propertyid));
            }
        }
        return basicProperties;
    }

    @Override
    public List<BasicProperty> getBasicPropertiesForTaxDetails(String assessmentNo, String ownerName, String mobileNumber,
            String propertyType, String doorNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("select propertyId from PropertyMaterlizeView where propertyId is not null");
        Map<String, String> params = new HashMap<String, String>();
        if (assessmentNo != null && !assessmentNo.trim().isEmpty()) {
            sb.append(" and propertyId=:assessmentNo ");
            params.put("assessmentNo", assessmentNo);
        }
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            sb.append(" and upper(trim(ownerName)) like :OwnerName ");
            params.put("OwnerName", "%" + ownerName.toUpperCase() + "%");
        }
        if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
            sb.append(" and mobileNumber like :MobileNumber ");
            params.put("MobileNumber", mobileNumber);
        }
        if (propertyType != null && !propertyType.trim().isEmpty()) {
            if (propertyType.equals(CATEGORY_TYPE_VACANTLAND_TAX)) {
                sb.append(" and propTypeMstrID.code = :propertyType ");
            } else if (propertyType.equals(CATEGORY_TYPE_PROPERTY_TAX)) {
                sb.append(" and propTypeMstrID.code <> :propertyType ");
                if(StringUtils.isNotBlank(doorNo)){
                	sb.append(" and houseNo like :DoorNo ");
                    params.put("DoorNo", "%"+(StringUtils.isNotBlank(doorNo) ? doorNo.trim() : "")+"%");
                }
            }
            params.put("propertyType", OWNERSHIP_TYPE_VAC_LAND);
        }

        final Query query = getCurrentSession().createQuery(sb.toString());
        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
        }
        List<String> list = query.setMaxResults(100).list();
        List<BasicProperty> basicProperties = new ArrayList<BasicProperty>();
        if (null != list && !list.isEmpty()) {
            for (String propertyid : list) {
                basicProperties.add(getBasicPropertyByPropertyID(propertyid));
            }
        }
        return basicProperties;
    }
    
    /**
     * API to fetch properties belonging to a particular ward
     */
    @Override
    public List<BasicProperty> getActiveBasicPropertiesForWard(Long wardId, String upicNo, String doorNo, String oldUpicNo){
        String queryStr = "select bp from BasicPropertyImpl bp where bp.active = 'Y' and bp.upicNo is not null ";
        if (wardId != null)
            queryStr = queryStr.concat(" and bp.propertyID.ward.id=:wardId ");
        if (StringUtils.isNotBlank(upicNo))
            queryStr = queryStr.concat(" and bp.upicNo =:upicNo ");
        if (StringUtils.isNotBlank(doorNo))
            queryStr = queryStr.concat(" and bp.address.houseNoBldgApt like :doorNo ");
        if(StringUtils.isNotBlank(oldUpicNo))
            queryStr = queryStr.concat(" and bp.oldMuncipalNum =:oldUpicNo ");

        queryStr = queryStr.concat(" order by bp.id desc ");
        Query query = getCurrentSession().createQuery(queryStr);
        if (wardId != null)
            query.setLong("wardId", wardId);
        if (StringUtils.isNotBlank(upicNo))
            query.setString("upicNo", upicNo);
        if (StringUtils.isNotBlank(doorNo))
            query.setString("doorNo", "%" + doorNo.trim() + "%");
        if (StringUtils.isNotBlank(oldUpicNo))
            query.setString("oldUpicNo", oldUpicNo);
    	    
    	return query.list();
    }
    
    @Override
    public BasicProperty getBasicPropertyByProperty(Long propertyId){
        BasicProperty basicProperty;
        String queryStr = "select prop.basicProperty from PropertyImpl prop where prop.id=:propertyId ";
        Query query = getCurrentSession().createQuery(queryStr);
        query.setLong("propertyId", propertyId);
        basicProperty = (BasicProperty) query.uniqueResult();
        return basicProperty;
    }

    @Override
    public BasicProperty getBasicPropertyForUpicNoOrOldUpicNo(String upicNo, String oldUpicNo){
        String queryStr = "select bp from BasicPropertyImpl bp where bp.active = 'Y'";

        if (StringUtils.isNotBlank(upicNo))
            queryStr = queryStr.concat(" and bp.upicNo =:upicNo ");
        if(StringUtils.isNotBlank(oldUpicNo))
            queryStr = queryStr.concat(" and bp.oldMuncipalNum =:oldUpicNo ");

        Query query = getCurrentSession().createQuery(queryStr);

        if (StringUtils.isNotBlank(upicNo))
            query.setString("upicNo", upicNo);
        if (StringUtils.isNotBlank(oldUpicNo))
            query.setString("oldUpicNo", oldUpicNo);

        return (BasicProperty) query.uniqueResult();
    }
}
