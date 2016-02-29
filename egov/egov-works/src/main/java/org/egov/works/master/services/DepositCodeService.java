package org.egov.works.master.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("depositCodeServices")
@Transactional
public class DepositCodeService {

    @Autowired
    private WorksService worksService;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;
    @PersistenceContext
    private EntityManager entityManager;

    public DepositCode getDepositCodeById(final Long DepositCodeId) {
        final DepositCode depositCode = entityManager.find(DepositCode.class, DepositCodeId);
        return depositCode;
    }

    public List<DepositCode> getAllDepositCodes() {
        final Query query = entityManager.createQuery("from DepositCode");
        final List<DepositCode> depositCodeList = query.getResultList();
        return depositCodeList;
    }

    public void createAccountDetailKey(final DepositCode dc) {
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(dc.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);
    }
}
