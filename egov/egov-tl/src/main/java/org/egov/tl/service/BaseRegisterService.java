package org.egov.tl.service;

import org.egov.tl.entity.dto.BaseRegisterForm;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Service
@Transactional(readOnly = true)
public class BaseRegisterService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<BaseRegisterForm> search(final BaseRegisterForm baseRegisterForm) {
        final SQLQuery finalQuery = prepareQuery(baseRegisterForm);
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(BaseRegisterForm.class));
        if (baseRegisterForm.getCategoryId() != null)
            finalQuery.setParameter("categoryId", baseRegisterForm.getCategoryId());
        if (baseRegisterForm.getSubCategoryId() != null)
            finalQuery.setParameter("subCategoryId", baseRegisterForm.getSubCategoryId());
        if (baseRegisterForm.getStatusId() != null)
            finalQuery.setParameter("statusId", baseRegisterForm.getStatusId());
        if (baseRegisterForm.getWardId() != null)
            finalQuery.setParameter("wardId", baseRegisterForm.getWardId());
        return finalQuery.list();
    }

    private SQLQuery prepareQuery(final BaseRegisterForm baseRegisterForm) {
        StringBuilder whereQry = new StringBuilder();
        final StringBuilder selectQry = new StringBuilder("select \"licenseid\", \"licensenumber\", \"tradetitle\", \"owner\", \"mobile\", \"categoryname\", \"subcategoryname\", \"assessmentno\"," +
                " \"wardname\", \"localityname\", trim(regexp_replace(\"tradeaddress\", '\\s+', ' ', 'g')) as \"tradeaddress\", \"commencementdate\", \"statusname\", cast(arrearlicensefee as bigint) " +
                "AS \"arrearlicensefee\", cast(arrearpenaltyfee as bigint) AS \"arrearpenaltyfee\", cast(curlicensefee as bigint) " +
                "AS \"curlicensefee\", cast(curpenaltyfee as bigint) AS \"curpenaltyfee\",\"unitofmeasure\",\"tradewt\",\"rateval\"  from egtl_mv_baseregister_view where 1=1 ");
        if (baseRegisterForm.getCategoryId() != null)
            whereQry = whereQry.append(" and cat = :categoryId");
        if (baseRegisterForm.getSubCategoryId() != null)
            whereQry = whereQry.append(" and subcat = :subCategoryId");
        if (baseRegisterForm.getStatusId() != null)
            whereQry = whereQry.append(" and status = :statusId");
        else
            whereQry = whereQry.append(" and statusname not in ('Cancelled','Suspended')");
        if (baseRegisterForm.getWardId() != null)
            whereQry = whereQry.append(" and ward = :wardId");
        if (isNotEmpty(baseRegisterForm.getFilterName()) && "Defaulters".equals(baseRegisterForm.getFilterName()))
            whereQry = whereQry.append(" and (arrearlicensefee > 0 or arrearpenaltyfee > 0 or curlicensefee > 0 or curpenaltyfee > 0)");
        return entityManager.unwrap(Session.class).createSQLQuery(selectQry.append(whereQry).toString());
    }


}
