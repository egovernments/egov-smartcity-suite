/*
 * @(#)JackRabbitDocumentManager.java 3.0, 17 Jun, 2013 11:52:33 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.docmgmt.jcr;

public class JackRabbitDocumentManager{
	// If moving away from OCM to raw JCR uncomment the below class implementation 
}
/*import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.apache.jackrabbit.JcrConstants;
import org.egov.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.AbstractDocumentManagerService;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentObject;

*//**
 * Implementation of the DocumentManagerService for a JackRabbit content repository. 
 * The JackRabbit can be deployed as a Model 1 Web application bundle/ Model 2 Shared J2EE resource. 
 * This is abstracted out from this implementation.
 * 
 * @author: Sahina Bose
 *//*

public class JackRabbitDocumentManager<T extends DocumentObject> extends AbstractDocumentManagerService<T> {
    
    *//**
     * {@inheritDoc DocumentManagerService}
     *//*
    public T addDocumentObject(T docObject) {
	
	try {
	    // check if a Document object with the documentNumber already exists
	    if (getDocumentNode(docObject.getDocumentNumber(),docObject.getDomainName()) != null) {
		throw new ValidationException(PROP_DOC_NUM,"DocMngr.DocNum.Exist",docObject.getDocumentNumber());
	    }
	    
	    Node root = getTemplate().getRootNode();
	    Node domain = null;
	    if (root.hasNode(EGOVThreadLocals.getDomainName())) {
		domain = root.getNode(EGOVThreadLocals.getDomainName());
	    } else {
		domain = root.addNode(EGOVThreadLocals.getDomainName());
	    }
	    
	    Node docNode = domain.addNode(NODE_DOC_OBJECT);
	    Calendar cal = Calendar.getInstance();
	    docNode.setProperty(PROP_CREATEDDATE, cal);
	    docNode.setProperty(PROP_CREATEDBY, docObject.getCreatedBy());
	    docNode.setProperty(PROP_MODIFIEDDATE, cal);
	    docNode.setProperty(PROP_MODIFIEDBY, docObject.getModifiedBy());
	    docNode.setProperty(PROP_DOC_NUM, docObject.getDocumentNumber());
	    docNode.setProperty(PROP_META_TAGS, docObject.getTags());
	    docNode.addMixin(JcrConstants.MIX_VERSIONABLE);
	    Node folderNode = docNode.addNode(NODE_ASSOCIATEDFILES, JcrConstants.NT_FOLDER);
	    addRemoveFile(folderNode, docObject.getAssociatedFiles());
	    getTemplate().save();
	    docObject.setJcrUUID(docNode.getUUID());
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to add Document", e);
	    throw new EGOVRuntimeException("Error occurred while trying to add Document", e);
	}
	
	return docObject;
    }
    
    *//**
     * {@inheritDoc DocumentManagerService}
     *//*
    public T updateDocumentObject(T docObject) {
	
	try {
	    // Check if a Document object with the Document Number exist
	    Node docNode = getTemplate().getNodeByUUID(docObject.getJcrUUID());
	    if (docNode == null) {
		throw new ValidationException(PROP_DOC_NUM,"DocMngr.DocNum.NotExist",docObject.getDocumentNumber());
	    }
	    
	    Calendar cal = Calendar.getInstance();
	    docNode.setProperty(PROP_MODIFIEDDATE, cal);
	    docNode.setProperty(PROP_MODIFIEDBY, docObject.getModifiedBy());
	    docNode.setProperty(PROP_META_TAGS, docObject.getTags());
	    Node folderNode = docNode.getNode(NODE_ASSOCIATEDFILES);
	    addRemoveFile(folderNode, docObject.getAssociatedFiles());
	    getTemplate().save();
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to update Document", e);
	    throw new EGOVRuntimeException("Error occurred while trying to update Document",e);
	}
	
	return docObject;
    }
    
    *//**
     * {@inheritDoc DocumentManagerService}
     *//*
    public T getDocumentObject(String documentNumber) {
	try {
	    Node docNode = checkDocNodeExist(documentNumber);	    
	    return createDocumentObject(docNode);
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to fetch Document", e);
	    throw new EGOVRuntimeException("Error occurred while trying to fetch Document", e);
	}
    }

    private T createDocumentObject(Node docNode) throws RepositoryException {
	DocumentObject docObject = new DocumentObject();
	docObject.setJcrUUID(docNode.getUUID());
	docObject.setDocumentNumber(docNode.getProperty(PROP_DOC_NUM).getString());
	docObject.setCreatedDate(docNode.getProperty(PROP_CREATEDDATE).getDate().getTime());
	docObject.setCreatedBy((int) docNode.getProperty(PROP_CREATEDBY).getLong());
	docObject.setModifiedBy((int) docNode.getProperty(PROP_MODIFIEDBY).getLong());
	docObject.setModifiedDate(docNode.getProperty(PROP_MODIFIEDDATE).getDate().getTime());
	docObject.setTags(docNode.getProperty(PROP_META_TAGS).getString());
	Set <AssociatedFile> files = new HashSet<AssociatedFile>();
	Node folderNode = null;
	if (docNode.hasNode(NODE_ASSOCIATEDFILES)) {
	    folderNode = docNode.getNode(NODE_ASSOCIATEDFILES);
	    NodeIterator fileNodeIterator = folderNode.getNodes();
	    while (fileNodeIterator.hasNext()) {
		Node fileNode = (Node) fileNodeIterator.next();
		Node resourceNode = fileNode.getNode(JcrConstants.JCR_CONTENT);
		AssociatedFile file = new AssociatedFile();
		file.setFileName(fileNode.getName());
		file.setRemarks(resourceNode.getProperty(PROP_FILE_REMARKS).getString());
		file.setCreatedBy((int) resourceNode.getProperty(PROP_CREATEDBY).getLong());
		file.setModifiedBy((int) resourceNode.getProperty(PROP_MODIFIEDBY).getLong());
		file.setCreatedDate(resourceNode.getProperty(PROP_CREATEDDATE).getDate().getTime());
		file.setModifiedDate(resourceNode.getProperty(PROP_MODIFIEDDATE).getDate().getTime());
		files.add(file);
	    }
	docObject.setAssociatedFiles(files);
	}
	return (T)docObject;
    }
    
    *//**
     * {@inheritDoc DocumentManagerService}
     *//*
    public List<Node> getVersionHistory(String docNumber) {
	
	try {
	    Node docNode = getDocumentNode(docNumber,null);
	    VersionHistory history = docNode.getVersionHistory();
	    VersionIterator ito = history.getAllVersions();
	    List<Node> documentHistory = new ArrayList<Node>();
	    while (ito.hasNext()) {
		Version v = ito.nextVersion();
		// The version node will have a "frozen" child node that contains
		// the corresponding version's node data
		NodeIterator it = v.getNodes(JcrConstants.JCR_FROZENNODE);
		if (it.hasNext()) {
		    Node node = it.nextNode();
		    documentHistory.add(node);
		}
	    }
	    return documentHistory;
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to fetch Document history", e);
	    throw new EGOVRuntimeException("Error occurred while trying to fetch Document history", e);
	}
	
    }
    
    *//**
     * {@inheritDoc}
     **//*
    public AssociatedFile getFileFromDocumentObject(String docNumber, String fileName) {
	Node docNode = checkDocNodeExist(docNumber);
	AssociatedFile file = null;
	try {
	    Node folderNode = docNode.getNode(NODE_ASSOCIATEDFILES);
	    Node fileNode = folderNode.getNode(fileName);	    
	    Node fileContent = fileNode.getNode(JcrConstants.JCR_CONTENT);
	   	file = new AssociatedFile();
		file.setFileName(fileNode.getName());
		file.setFileInputStream(fileContent.getProperty(JcrConstants.JCR_DATA).getStream());
		file.setLength(fileContent.getProperty(PROP_FILE_LENGTH).getLong());
		file.setMimeType(fileContent.getProperty(JcrConstants.JCR_MIMETYPE).getString());
		
	} catch (PathNotFoundException e) {
	    throw new EGOVRuntimeException("Error occurred while trying to fetch file", e);
	} catch (RepositoryException e) {
	    throw new EGOVRuntimeException("Error occurred while trying to fetch file", e);
	}
	return file;
    }
    
    private void addRemoveFile(Node folderNode, Set<AssociatedFile> files) {
	for (AssociatedFile file : files) {
	    addRemoveFile(folderNode, file);
	}
    }
    
    private void addRemoveFile(Node folderNode, AssociatedFile file) {
	try {
	    
	    if (file != null) {
		if (!file.isMarkForDelete()) {
		    if (folderNode.hasNode(file.getFileName())) {
			throw new ValidationException(PROP_DOC_NUM,"DocMngr.File.Exist",file.getFileName());
		    }
		    
		    Node fileNode = folderNode.addNode(file.getFileName(), JcrConstants.NT_FILE);
		    Node resourceNode = fileNode.addNode(JcrConstants.JCR_CONTENT, NT_RESOURCE);
		    resourceNode.setProperty(PROP_FILE_REMARKS, file.getRemarks());
		    resourceNode.setProperty(PROP_FILE_LENGTH, file.getLength());
		    resourceNode.setProperty(PROP_CREATEDBY, file.getCreatedBy());
		    resourceNode.setProperty(PROP_MODIFIEDBY, file.getModifiedBy());
		    resourceNode.setProperty(PROP_CREATEDDATE, file.getCreatedDate().getTime());
		    resourceNode.setProperty(PROP_MODIFIEDDATE, file.getModifiedDate().getTime());
		    resourceNode.setProperty(JcrConstants.JCR_MIMETYPE, file.getMimeType());
		    resourceNode.setProperty(JcrConstants.JCR_ENCODING, "UTF-8");
		    resourceNode.setProperty(JcrConstants.JCR_DATA, file.getFileInputStream());
		    Calendar lastModified = Calendar.getInstance();
		    lastModified.setTimeInMillis(file.getModifiedDate().getTime());
		    resourceNode.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);
		} else {
		    Node node = folderNode.getNode(file.getFileName());
		    node.remove();
		}
	    }
	    
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to add file", e);
	    throw new EGOVRuntimeException("Error occurred while trying to add file",e);
	}
    }
    
    private Node checkDocNodeExist(String docNumber) {
	Node docNode = getDocumentNode(docNumber,null);
	if (docNode == null) {
	    throw new ValidationException(PROP_DOC_NUM,"DocMngr.DocNum.NotExist",docNumber);
	}
	return docNode;
    }
    
    
    @Override
    public T getDocumentObjectByUuid(String uuid) {
	try {
	    return createDocumentObject(getTemplate().getNodeByUUID(uuid));
	} catch (RepositoryException e) {
	    logger.error("Error occurred while trying to fetch Document", e);
	    throw new EGOVRuntimeException("Error occurred while trying to fetch Document", e);
	}
	 
    }

    @Override
    public T getDocumentObject(String docNumber, String moduleName) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<T> searchDocumentObject(Class<?> searchClass, Map<String, String> searchArgs) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public AssociatedFile getFileFromDocumentObject(String docNumber, String moduleName, String fileName) {
	// TODO Auto-generated method stub
	return null;
    }
    
}*/
