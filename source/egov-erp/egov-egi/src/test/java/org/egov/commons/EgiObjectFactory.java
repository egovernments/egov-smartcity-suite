/**
 * 
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.lib.rjbac.role.Role;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author eGov
 * 
 */
public class EgiObjectFactory {

	public final Session session;
	public static final String CREATOR = "creator1";
	public static final String APPROVER = "approver1";

	public EgiObjectFactory() {
		this.session = null;
	}

	public EgiObjectFactory(final Session session) {

		this.session = session;
	}

	public Relationtype createRelationtype(final String name, final String code) {
		final Relationtype reltType = new Relationtype();
		reltType.setName(name);
		reltType.setCode(code);
		reltType.setDescription(name);
		reltType.setIsactive(true);
		reltType.setCreated(new Date());
		reltType.setModifiedby(BigDecimal.ONE);
		this.session.saveOrUpdate(reltType);
		return reltType;
	}

	public Relation createRelation(final Integer relationtypeId,
			final String Name, final String addr1, final String addr2,
			final String code) {
		final Relationtype relType = (Relationtype) this.session.get(
				Relationtype.class, new Integer(1));
		final Relation relation = new Relation();
		relation.setName(Name);
		relation.setRelationtype(relType);
		relation.setAddress1(addr1);
		relation.setAddress2(addr2);
		relation.setCode(code);
		relation.setCreated(new Date());
		relation.setCreatedby(BigDecimal.ONE);
		relation.setIsactive(Boolean.TRUE);
		this.session.saveOrUpdate(relation);
		return relation;
	}

	/*
	 * added by Prashanth on 14th august 09
	 * 
	 * @param userName
	 * 
	 * @return Role
	 */
	public Set<Role> getRolesForSuperuser(final String roleName) {
		final Query qry = this.session
				.createQuery("from RoleImpl role where role.roleName=:roleName");
		qry.setString("roleName", roleName);
		return new HashSet<Role>(qry.list());
	}

	public Script createScript(final String scriptName,
			final String scriptType, final String scriptCode) {
		final Script script = new Script(scriptName, scriptType, scriptCode);
		this.session.saveOrUpdate(script);
		return script;
	}

	public CChartOfAccounts createCOA(final String glcode,
			final String purposeId, final Long parentId,
			final Long isActiveForPosting, final Long classification,
			final Character type, final String name) {
		PersistenceService<CChartOfAccounts, Long> cChartOfAccountsSer;
		final CChartOfAccounts account = new CChartOfAccounts();
		cChartOfAccountsSer = new PersistenceService<CChartOfAccounts, Long>();
		cChartOfAccountsSer.setSessionFactory((new SessionFactory()));
		cChartOfAccountsSer.setType(CChartOfAccounts.class);
		account.setGlcode(glcode);
		account.setPurposeId(purposeId);
		account.setParentId(parentId);
		account.setIsActiveForPosting(isActiveForPosting);
		account.setClassification(classification);
		account.setType(type);
		account.setName(name);
		cChartOfAccountsSer.persist(account);
		return account;
	}
}