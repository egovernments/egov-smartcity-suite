/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.docmgmt.ocm;

import static org.egov.infstr.utils.DateUtils.constructDateRange;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.apache.jackrabbit.ocm.version.Version;
import org.apache.jackrabbit.ocm.version.VersionIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.annotation.Search;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AbstractDocumentManagerService;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentObject;

/**
 * The Class JackRabbitOcmDocumentManager.
 * @param <T> the generic type
 */
public class JackRabbitOcmDocumentManager<T extends DocumentObject> extends AbstractDocumentManagerService<T> {

	private static final Logger LOG = LoggerFactory.getLogger(JackRabbitOcmDocumentManager.class);
	
	/** OCM Mapper used to create OCM object. */
	private transient Mapper ocmMapper;

	/** OCM used for transaction. */
	private transient ObjectContentManager objContentManager;

	/** Used to store all possible IP address user may use to add document */
	private List<String> failProofIPAdrresses;
	
	/**external internal ip address holder*/
	private Map<String,String> ipSwitchs;
	

	public void setIpSwitchs(Map<String, String> ipSwitchs) {
		this.ipSwitchs = ipSwitchs;
	}

	public void setFailProofIPAdrresses(final List<String> failProofIPAdrresses) {
		this.failProofIPAdrresses = failProofIPAdrresses;
	}

	/**
	 * Sets the ocm mapper.
	 * @param ocmMapper the new ocm mapper
	 */
	public void setOcmMapper(final Mapper ocmMapper) {
		this.ocmMapper = ocmMapper;
	}

	/**
	 * Inits the ocm.
	 */
	public void initOcm() {
		this.objContentManager = new ObjectContentManagerImpl(this.getSession(), this.ocmMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T addDocumentObject(final T docObject) {
		try {
			docObject.setCreatedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
			docObject.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
			docObject.setDomainName(DocumentObject.escapeSpecialChars(ipSwitchs.containsKey(EGOVThreadLocals.getDomainName()) ? ipSwitchs.get(EGOVThreadLocals.getDomainName()) : EGOVThreadLocals.getDomainName()));
			final Date currentDate = new Date();
			docObject.setCreatedDate(currentDate);
			docObject.setModifiedDate(currentDate);
			this.setOcmPath(docObject);
			this.checkMandatoryProperty(docObject);
			this.objContentManager.insert(docObject);
			this.objContentManager.save();
			this.objContentManager.checkout(docObject.getPath());
			this.objContentManager.save();
			this.objContentManager.checkin(docObject.getPath());
		} catch (final ValidationException e) {
			throw e;
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while trying to add Document.", e);
		}
		return docObject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T updateDocumentObject(final T docObject) {
		try {
			this.objContentManager.checkout(docObject.getPath());
			docObject.setModifiedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
			docObject.setModifiedDate(new Date());
			this.checkMandatoryProperty(docObject);
			this.objContentManager.update(docObject);
			this.objContentManager.save();
			this.objContentManager.checkin(docObject.getPath());
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while trying to update Document.", e);
		}
		return docObject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getDocumentObject(final String docNumber) {
		T document = this.getDocumentObjectFromDomain(docNumber, DocumentObject.escapeSpecialChars(EGOVThreadLocals.getDomainName()));
		if (document == null) {
			for (final String failPoofIP : failProofIPAdrresses) {
				document = this.getDocumentObjectFromDomain(docNumber,DocumentObject.escapeSpecialChars(failPoofIP));
				if (document != null) {
					break;
				}
			}
		}
		return document;
	}
	
	public T getDocumentObjectFromDomain(final String docNumber, final String domainName) {
		final QueryManager queryManager = this.objContentManager.getQueryManager();
		final Filter filter = queryManager.createFilter(DocumentObject.class);
		filter.addEqualTo(PROP_DOC_NUM, docNumber);
		filter.addEqualTo(PROP_DOMAIN_NAME, domainName);
		final Query query = queryManager.createQuery(filter);
		return (T) this.objContentManager.getObject(query);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getDocumentObject(final String docNumber, final String moduleName) {
		T document = this.getDocumentObjectFromDomain(docNumber, moduleName, DocumentObject.escapeSpecialChars(EGOVThreadLocals.getDomainName()));
		if (document == null) {
			for (final String failPoofIP : failProofIPAdrresses) {
				document = this.getDocumentObjectFromDomain(docNumber, moduleName, DocumentObject.escapeSpecialChars(failPoofIP));
				if (document != null) {
					break;
				} else {
					LOG.debug("Document not found in Fail Proof IP Address : "+failPoofIP);
				}
			}
		}
		return document;
	}
	
	public T getDocumentObjectFromDomain(final String docNumber, final String moduleName, final String domainName) {
		final QueryManager queryManager = this.objContentManager.getQueryManager();
		final Filter filter = queryManager.createFilter(DocumentObject.class);
		filter.setScope(this.getSearchScope(moduleName,domainName));
		filter.addEqualTo(PROP_DOC_NUM, docNumber);
		filter.addEqualTo(PROP_DOMAIN_NAME, domainName);
		filter.addEqualTo(PROP_MODULE_NAME, moduleName);
		final Query query = queryManager.createQuery(filter);
		return (T) this.objContentManager.getObject(query);
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getDocumentObjectByUuid(final String uuid) {
		return (T) this.objContentManager.getObjectByUuid(uuid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AssociatedFile getFileFromDocumentObject(final String docNumber, final String moduleName, final String fileName) {
		final DocumentObject documentObject = this.getDocumentObject(docNumber, moduleName);
		final Iterator<AssociatedFile> associatedFiles = documentObject.getAssociatedFiles().iterator();
		while (associatedFiles.hasNext()) {
			final AssociatedFile file = associatedFiles.next();
			if (file.getFileName().equals(fileName)) {
				return file;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> getVersionHistory(final String path) {
		final List<T> documentHistory = new ArrayList<T>();
		try {
			final VersionIterator versionIterator = this.objContentManager.getAllVersions(path);
			while (versionIterator.hasNext()) {
				final Version version = (Version) versionIterator.next();
				if (!JcrConstants.JCR_ROOTVERSION.equals(version.getName())) {
					documentHistory.add((T) this.objContentManager.getObject(path, version.getName()));
				}
			}
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while getting version history for Document in Path : " + path, e);
		}
		return documentHistory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> searchDocumentObject(final Class<?> searchClass, final Map<String, String> searchArgs) {
		List<T> documents = this.searchDocumentObject(searchClass, searchArgs,EGOVThreadLocals.getDomainName());		
		if (documents.isEmpty()) {
			for(String failPoofIP : failProofIPAdrresses) {
				documents = this.searchDocumentObject(searchClass, searchArgs,failPoofIP);
				if(!documents.isEmpty()) {
					break;
				}
			}
		}
		return documents;
	}
	
	private List<T> searchDocumentObject(final Class<?> searchClass, final Map<String, String> searchArgs, final String domainName) {
		try {
			final QueryManager queryManager = this.objContentManager.getQueryManager();
			final Filter filter = queryManager.createFilter(searchClass);
			final String domain = DocumentObject.escapeSpecialChars(ipSwitchs.containsKey(domainName) ? ipSwitchs.get(domainName) : domainName);
			filter.setScope(this.getSearchScope(searchArgs.get(PROP_MODULE_NAME),domain));
			final BeanInfo beanInfo = Introspector.getBeanInfo(searchClass);
			final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (final PropertyDescriptor propDesc : props) {
				String value = null;
				final Method field = propDesc.getReadMethod();
				if (searchArgs.containsKey(propDesc.getName())) {
					value = searchArgs.get(propDesc.getName());
				}
				if ((value != null) && !value.isEmpty() && field.isAnnotationPresent(Search.class)) {
					final Search search = field.getAnnotation(Search.class);
					if (search.searchOp().equals(Search.Operator.equals)) {
						filter.addEqualTo(propDesc.getName(), value);
					} else if (search.searchOp().equals(Search.Operator.startsWith)) {
						filter.addLike(propDesc.getName(), value);
					} else if (search.searchOp().equals(Search.Operator.between)) {
						final String[] vals = value.split("#");
						if (vals.length == 2) {
							final Date[] dateVals = constructDateRange(vals[0], vals[1]);
							filter.addBetween(propDesc.getName(), dateVals[0], dateVals[1]);
						} else {
							final Date[] dateVals = constructDateRange(vals[0], vals[0]);
							filter.addBetween(propDesc.getName(), dateVals[0], dateVals[1]);
						}
					} else if (search.searchOp().equals(Search.Operator.contains)) {
						filter.addContains(propDesc.getName(), value);
					}
				}
			}
			filter.addEqualTo(PROP_DOMAIN_NAME, domain);
			final Query query = queryManager.createQuery(filter);
			return new ArrayList<T>(this.objContentManager.getObjects(query));
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while Searching Document", e);
		}
	}

	/**
	 * Sets the ocm path.
	 * @param docObject the new ocm path
	 * @throws ValidationException the validation exception
	 * @throws RepositoryException the repository exception
	 */
	private void setOcmPath(final DocumentObject docObject) throws ValidationException, RepositoryException {
		if (this.getDocumentNode(docObject.getDocumentNumber(), docObject.getModuleName(),docObject.getDomainName()) != null) {
			throw new ValidationException(PROP_DOC_NUM, "DocMngr.DocNum.Exist", docObject.getDocumentNumber());
		}
		final Node root = this.objContentManager.getSession().getRootNode();
		Node domain = null;
		if (root.hasNode(docObject.getDomainName())) {
			domain = root.getNode(docObject.getDomainName());
		} else {
			domain = root.addNode(docObject.getDomainName());
		}
		Node module = null;
		if (domain.hasNode(docObject.getModuleName())) {
			module = domain.getNode(docObject.getModuleName());
		} else {
			module = domain.addNode(docObject.getModuleName());
		}
		docObject.setPath(module.getPath() + PATH_SEPARATOR + NODE_DOC_OBJECT);
	}

	/**
	 * Gets the search scope.
	 * @param moduleName the module name
	 * @return the search scope
	 */
	private String getSearchScope(final String moduleName, final String domainName) {
		final StringBuffer searchScope = new StringBuffer();
		searchScope.append(PATH_SEPARATOR).append(domainName).append(PATH_SEPARATOR);
		searchScope.append(moduleName).append(PATH_SEPARATOR).append(PATH_SEPARATOR);
		return searchScope.toString();
	}
}
