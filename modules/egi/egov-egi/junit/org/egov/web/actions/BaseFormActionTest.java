package org.egov.web.actions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.commonMasters.EgUomcategory;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.services.PersistenceService;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.XWorkTestCase;

public class BaseFormActionTest extends XWorkTestCase {
	private BaseFormAction action;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object getModel() {
				return null;
			}

		};
		ActionContext.getContext().getValueStack().push(this.action);
	}

	@Test
	public void testBaseAction() throws Exception {
		this.action.getModel();
		this.action.getRelationships();
		this.action.getDropdownData();
		this.action.setRequest(null);
		this.action.setSession(null);
		this.action.getSession();
		this.action.getPersistenceService();
		this.action.getOrdering();
		Method mm = BaseFormAction.class.getDeclaredMethod("setValue",
				String.class, Object.class);
		mm.setAccessible(true);
		mm.invoke(this.action, "", "");
		mm = BaseFormAction.class.getDeclaredMethod("addRelatedEntity",
				String.class, Class.class);
		mm.setAccessible(true);
		mm.invoke(this.action, "Object", BaseModel.class);
		mm = BaseFormAction.class.getDeclaredMethod("addDropdownData",
				String.class, List.class);
		mm.setAccessible(true);
		mm.invoke(this.action, "Object", Collections.emptyList());
		mm = BaseFormAction.class.getDeclaredMethod("acceptableParameterName",
				String.class);
		mm.setAccessible(true);
		mm.invoke(this.action, "Object");
		mm.invoke(this.action, "xxc");
		mm = BaseFormAction.class.getDeclaredMethod("session");
		mm.setAccessible(true);
		mm.invoke(this.action);

		final Map<String, String[]> parameters = new HashMap<String, String[]>();
		final String[] values = { "1" };
		parameters.put("Object", values);
		this.action.setParameters(parameters);
		final PersistenceService persistenceService = Mockito
				.mock(PersistenceService.class);
		this.action.setPersistenceService(persistenceService);
		Mockito.when(persistenceService.find(Matchers.anyString())).thenReturn(
				new Object());
		this.action.prepare();
		final Method method = BaseFormAction.class.getDeclaredMethod(
				"setupDropdownDataExcluding", String[].class);
		method.setAccessible(true);
		final String[] args = { "xyz" };
		method.invoke(this.action, new Object[] { args });
		mm = BaseFormAction.class.getDeclaredMethod("addRelatedEntity",
				String.class, Class.class, String.class);
		mm.setAccessible(true);
		mm.invoke(this.action, "Object", BaseModel.class, "ASC");
		method.invoke(this.action, new Object[] { args });
		this.action.prepare();
		class XYZ {
			public int getId() {
				return 0;
			}
		}
		new XYZ().getId();
		mm.invoke(this.action, "Object", XYZ.class, "ASC");
		this.action.prepare();

		mm.invoke(this.action, "Object", Object.class, "ASC");
		try {
			this.action.prepare();
			fail();
		} catch (final RuntimeException e) {
		}

	}

	@Test
	public void testSetsRelationshipsAndDropdownData() throws Exception {
		final AbstractPersistenceServiceTest absService = new AbstractPersistenceServiceTest();
		absService.setup();
		this.action = new BaseFormAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			protected EgUom uom = new EgUom();

			@Override
			protected void setValue(final String relationshipName,
					final Object relation) {
				try {
					Ognl.setValue(relationshipName,
							BaseFormActionTest.this.action.getModel(), relation);
				} catch (final OgnlException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public Object getModel() {
				return this.uom;
			}

			@Override
			public void prepare() {
				addRelatedEntity("egUomcategory", EgUomcategory.class);
				super.prepare();
				setupDropdownDataExcluding();
			}

		};
		final Field f = AbstractPersistenceServiceTest.class
				.getDeclaredField("genericService");
		f.setAccessible(true);
		final PersistenceService service = (PersistenceService) f
				.get(absService);
		this.action.setPersistenceService(service);
		final EgUomcategory uomCat = new EgUomcategory(null, "test",
				new Date(), new Date(), new BigDecimal(1));
		uomCat.setId(1);
		service.getSession().saveOrUpdate(uomCat);
		final HashMap<String, String[]> req = new HashMap<String, String[]>();
		this.action.setParameters(req);
		this.action.prepare();
		assertNull(((EgUom) this.action.getModel()).getEgUomcategory());
		req.put("egUomcategory", new String[] { uomCat.getId().toString() });
		this.action.setParameters(req);
		this.action.prepare();
		assertNotNull(((EgUom) this.action.getModel()).getEgUomcategory());
		assertNotNull(this.action.getDropdownData().containsKey(
				"egUomcategoryList"));
		absService.tearDown();
	}

}
