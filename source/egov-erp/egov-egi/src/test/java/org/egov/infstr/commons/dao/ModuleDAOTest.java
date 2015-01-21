package org.egov.infstr.commons.dao;

import org.apache.log4j.Logger;
import org.egov.commons.EgiObjectFactory;
import org.egov.exceptions.DuplicateElementException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.lib.rjbac.role.Role;
import org.junit.Ignore;

import java.util.List;
import java.util.Set;

@Ignore
public class ModuleDAOTest extends EgovHibernateTest {
    public final Logger logger = Logger.getLogger(ModuleDAOTest.class);

    public void testGetModuleInfoForRoleIdsForNull() {
        final ModuleDao objDao = new ModuleHibDao(ModuleDao.class, null);
        List modulInfoList = null;
        try {
            modulInfoList = objDao.getModuleInfoForRoleIds(null);
        } catch (final NullPointerException e) {
        }
        assertNull(modulInfoList);
    }

    public void testGetModuleInfoForRoleIdsForSuperUser()
            throws DuplicateElementException {
        final ModuleDao objDao = new ModuleHibDao(ModuleDao.class, null);
        final EgiObjectFactory egObjFactory = new EgiObjectFactory( null);
        final Set<Role> roles = egObjFactory.getRolesForSuperuser("SuperUser");
        final List modulInfoList = objDao.getModuleInfoForRoleIds(roles);
        assertTrue("record exist for superuser", modulInfoList.size() > 0);
    }

}
