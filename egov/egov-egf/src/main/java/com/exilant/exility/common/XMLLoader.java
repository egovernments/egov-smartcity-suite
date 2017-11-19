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
package com.exilant.exility.common;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author raghu.bhandi
 *
 * XMLLoader loads an object from an XML source file. It can load a complete object map from a corresponding XML file
 *
 * 1. XML uses only attributes of an node. e.g. <type> or <type attr1="vaal1" attr2=Val2 /> 2. Values of the type <tag>Some text
 * value of tag</tag> are not used. If found, they are ignored 3. Hierarchies are possible. e.g. <group attr1="val1" attr2="val2">
 * <member1 attr="val" junk="junk"/> .... </group>
 *
 * For an object to be 'loadable' follow these guidelines. (No interface is defined as these are naming conventions, and not
 * methods) 1. For each possible atribute, define a field (data member) of appropriate data type. primitive data types, and string
 * are the only ones to be used. 2. Alternately, you can define a HashMap named 'attributes'. All attributes, for which there is
 * no corresponding field name, are stored in this hashmap as name/value pairs. 3. If the class/object can have children (child
 * nodes) you should plan to load them as well 4. Note that each node in the XML is loaded into one object by default. Each node
 * will have a corresponding class 5. If a child node occurs only once, you can define a field for that with the name matching the
 * tag name. Loader will load the child node into this object. If you just declare it without instantiatiing it, Loader tries teh
 * defalt constructor to iinstantiate it 6. If a type of child node occurs more than once, you can either plan to store them in an
 * array or a HashMap If you need to locate them using key, then HashMap is the preferred way, while array is effecient. You can
 * also store them in ArrayList if you have to add child nodes later (after loader loads them) Loader looks for a field with the
 * name =tagname + 's' . For e.g. <main > <child id="a1" someattribute="value"/> <child id="a2" someattribute="somevalue"/>
 * </main> in this case, loader looks for a field with name childs and tries to load all child nodes into this field 7. If array
 * is defined, Loader can determine the calss (Type) of child obbjects. However, if it is HashMap or ArrayList, Loader can not
 * figure out the calss (type) to be used to instantiate teh child object. You have to declare a field with the name of the tag
 * for this. For example, for the above xml, define ChildClass child; HashMap children; 8. The key for HashMap: attribute with
 * name id, key, and name are searched, in that order and used as key for HashMap. 9. If your needs to store child objects are
 * more complex than this, then you can write your own method. a method newChild(String tag, String key) is called by the loader
 * whenever a child node is encountered you can instantiate a child object, store it appropriately, and return the reference for
 * the loader to load it. Loader calls endChild(String tag) when the end tag is encountered. This method is optional and you need
 * not provide this. It is called only if it is defined. 10. Tag name of the node is stored in a String field with name as 'node'
 * if such a field is defined for the object (note: if you also have an attribute with name as 'type', the attribute value
 * over-rides the tag name) 11. If any field is of type boolean, then you should specify "true", "yes" or "1" as the value for
 * true. else it is considered to be false;
 *
 */
public class XMLLoader extends DefaultHandler {
    /*
     * Each open node is pushed to the stack. Information about each type of child nodes is stored and saved along with the object
     * in teh stack. Look at StackedObject and ChildInfo classes for details
     */
    Stack openNodes; // keeps track of open nodes in the XML.

    Object rootObject; // object to which the XML is to be loaded
    private static final Logger LOGGER = Logger.getLogger(XMLLoader.class);

    public XMLLoader() {
    }

    public void load(final String fileName, final Object root) {
        rootObject = root;
        openNodes = new Stack();
        load(fileName);
        rootObject = null;
        openNodes = null;
    }

    /**************************************
     *
     * @param fileName Uses a SAXParser to parse and load the XML into the object that was set using setObject() method
     *
     ***************************************/
    private void load(final String fileName) {
        try {
            final SAXParserFactory spf = SAXParserFactory.newInstance();

            spf.setNamespaceAware(false);

            // Validation part 1: set whether validation is on
            spf.setValidating(false);

            // Create a JAXP SAXParser
            final SAXParser saxParser = spf.newSAXParser();

            // Get the encapsulated SAX XMLReader
            final XMLReader xmlReader = saxParser.getXMLReader();
            // Set the ContentHandler of the XMLReader
            xmlReader.setContentHandler(this);

            // Set an ErrorHandler before parsing. This error handler is
            // defifined as a subClass in this class
            xmlReader.setErrorHandler(new MyErrorHandler(System.err));

            // Tell the XMLReader to parse the XML document
            xmlReader.parse(fileName);
        } catch (final ParserConfigurationException e) {
            LOGGER.error("Exp=" + e.getMessage());
        } catch (final SAXException e) {
            LOGGER.error("Exp=" + e.getMessage());
        } catch (final IOException e) {
            LOGGER.error("Exp=" + e.getMessage());
        } finally {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Finally in load");
        }
    }

    /**********************************
     * Call back methods used by SaxLoader. startElement() and endElement()
     */

    /*
     * Start element is called by the parser whenever the beginning tag is parsed Our assumption is that the tags contian only
     * attribute, and no text value. i.e we do not expect a case like <type>Some value </type> qName is the tag name, and atts
     * contains all name/value pairs of attribute say <tag name1="value1" name2="value2"...
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(final String namespaceURI, final String localName,
            final String qName, final Attributes atts) throws SAXException {

        Object objectToLoad = null;

        if (openNodes.size() == 0)
            // node
            objectToLoad = rootObject; // load the root node to root object
        else
            objectToLoad = createChild(qName, atts);

        if (objectToLoad != null) { // if it is null, the node is not loaded at
            // all..
            if (atts.getIndex("type") < 0)
                ObjectGetSetter.set(objectToLoad, "type", qName); // try saving
            // tag as
            // 'type'
            // attribute
            ObjectGetSetter.setAll(objectToLoad, atts); // set all attributes
            // from atts to members
            // of childobject
        }

        openNodes.push(new StackedObject(objectToLoad));
    }

    /**********************
     * endElement() is called by the parser when the /> or </element> is encountered
     */
    @Override
    public void endElement(final String namespaceURI, final String localName, final String qName)
            throws SAXException {

        final StackedObject so = (StackedObject) openNodes.pop();
        final Object endingObject = so.object;
        if (endingObject == null)
            return;

        // Call endChild() for all of the childInfo() that we may have
        if (so.childInfos != null && so.childInfos.size() > 0) {
            final Iterator iter = so.childInfos.values().iterator();
            while (iter.hasNext())
                ((ChildInfo) iter.next()).endChild(endingObject);
        }
    }

    /*
     * Whenevr a child node is detected by the parser, a new Object is to be supplied to set all the attributes. This helper class
     * tries different conventions to create a child object
     */
    private Object createChild(final String tag, final Attributes atts) {
        if (openNodes.size() == 0)
            return null;

        final StackedObject stackedObject = (StackedObject) openNodes.peek();
        final Object parentObject = stackedObject.object;
        if (null == stackedObject.childInfos)
            stackedObject.childInfos = new HashMap();
        ChildInfo childInfo = null;

        if (stackedObject.childInfos.size() > 0)
            try {
                childInfo = (ChildInfo) stackedObject.childInfos.get(tag);
            } catch (final Exception e) {
                LOGGER.error("Error in getting child info" + e.getMessage());
            }

        if (null == childInfo) {// no info. Probably this tag is encountered for
            // the first time
            childInfo = new ChildInfo(tag);
            childInfo.addInfo(parentObject);
            stackedObject.childInfos.put(tag, childInfo);
        }
        // is there a key to this node? name=key or name=id or name=name are
        // considered to be keys
        String key = null;
        try {
            key = atts.getValue("id");
        } catch (final Exception e) {
            LOGGER.error("Exp=" + e.getMessage());
        }

        if (null == key)
            try {
                key = atts.getValue("key");
            } catch (final Exception e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Exp=" + e.getMessage());
            }

        if (null == key)
            try {
                key = atts.getValue("name");
            } catch (final Exception e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Exp=" + e.getMessage());
            }

        return childInfo.createChild(parentObject, key);

    }

    /*
     * Error handler is defined and supplied to the parser to report errors encountered during parsing Note that we output the
     * error message, and continue..
     */
    private class MyErrorHandler implements ErrorHandler {
        /** Error handler output goes here */
        private final PrintStream out;

        MyErrorHandler(final PrintStream out) {
            this.out = out;
        }

        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(final SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null)
                systemId = "null";
            final String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
                    + ": " + spe.getMessage();
            return info;
        }

        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.

        @Override
        public void warning(final SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }

        @Override
        public void error(final SAXParseException spe) throws SAXException {
            final String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        @Override
        public void fatalError(final SAXParseException spe) throws SAXException {
            final String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }

    /*
     * Holds all information required to instantiate an object whenever a tag of this type is encountered. Has method to create
     * child, as well as extract and store information
     */
    private class ChildInfo {
        String tag;
        int storageType = 0; // 0=invalid, 1=newChild, 2=singleChild into a
        // field, 3= array 4=collection
        Field field = null;
        Class fieldType = null;
        Object collectionObject = null; // collected children as arraylist to be
        // converted to array
        Method addMethod = null; // execute .add() to add the child
        boolean addRequiresKey = false; // whether add is add(object) or
        // add(tag, object)
        Method newChildMethod = null;
        Method endChildMethod = null;

        ChildInfo(final String tag) {
            this.tag = tag;
        }

        /*
         * Extracts information about the way the child Object is to be created. Uses reflection to check what the parentObject
         * has planned to create add child nodes This method is long, as we explore different options..
         */
        public void addInfo(final Object parentObject) {

            // class arrays required for getMethod() calls
            final Class[] stringClass = { String.class };
            final Class[] stringClass2 = { String.class, String.class };
            final Class[] objClass = { Object.class };
            final Class[] objClass2 = { Object.class, Object.class };
            /*
             * Is this an advanced programmer who has written her own addChild(tag, key) method?
             */
            try {
                newChildMethod = parentObject.getClass().getMethod(
                        "newChild", stringClass2);
                // thrown out if the above line does not succeed
                // now that we got the method, let us set other properties of
                // childInfo
                collectionObject = parentObject;
                addRequiresKey = true;
                storageType = 1;
                /*
                 * check if she has defined the optional method endChild(tag)
                 */
                try {
                    endChildMethod = parentObject.getClass().getMethod(
                            "endChild", stringClass);
                } catch (final Exception e) {
                    LOGGER.error("Exp=" + e.getMessage());
                }

                return; // if this method is available, we do not look for
                // anything else.

            } catch (final Exception e) {
                LOGGER.error("Exp=" + e.getMessage());
            }

            /*
             * Is there a field with name = tag?
             */
            try {
                field = ObjectGetSetter.getField(parentObject, tag);// throws
                                                                    // exception
                                                                    // if
                                                                    // field
                                                                    // not
                                                                    // found
                field.setAccessible(true);
                fieldType = field.getType();
                storageType = 2; // object class. If it is to be a single
                                 // child
                // this will be over-ridden subsequently if collection is
                // discovered..
                // no return. We should explore further....
            } catch (final Exception e) {
                LOGGER.error("Exp=" + e.getMessage());
            }

            /*
             * Is there a collection object that holds all children? Common ones are Array and HashMap. name is assumed to be
             * plural of tag. example, if tag=lion, we look for a field with name lions Bare with me: we do not understand English
             * Grammer to make child to children. We will look for 'childs' if tag is 'child'
             */

            try {
                final Field f = ObjectGetSetter
                        .getField(parentObject, tag + 's'); // thrown out
                                                            // if field
                                                            // not found
                final Class c = f.getType();

                // is it an array?
                if (c.isArray()) {
                    storageType = 3; // array
                    collectionObject = new ArrayList(); // store here and
                                                        // create array
                                                        // later
                    addRequiresKey = false;
                    addMethod = ArrayList.class.getMethod("add", objClass);
                    field = f;
                    fieldType = c.getComponentType();
                } else {
                    Object o = f.get(parentObject);
                    if (null == o) {
                        o = c.newInstance();
                        f.setAccessible(true);
                        f.set(parentObject, o);
                    }
                    try { // It could be ArrayList or HashMap. Instead of
                        // restricting to these classes,
                          // we allow any class so long it has an add(object)
                          // method or put(tag, object) method
                        addMethod = c.getMethod("add", objClass);
                        collectionObject = o; // o stores all children
                        addRequiresKey = false;
                        storageType = 4;
                    } catch (final Exception e) {
                        try {// may be it has put(key, Object) method..
                            addMethod = c.getMethod("put", objClass2);
                            collectionObject = o;
                            addRequiresKey = true;
                            storageType = 4;
                        } catch (final Exception e1) {
                            LOGGER.error("Exp=" + e1.getMessage());
                        }
                        LOGGER.error("Exp ObjectGetSetter=" + e.getMessage());
                    }
                }
            } catch (final Exception e) {
                LOGGER.error("Exp=" + e.getMessage());
            }
            // if all exploration failed, storageType will continue to be 0
            // implying inability to create child object
        }

        /*
         * Instantiate a child object based on this info for the supplied parentObject
         */
        public Object createChild(final Object parentObject, final String key) {

            Object childObject = null;
            final Object[] arr1 = { null }; // for method.invoke()..
            final Object[] arr2 = { null, null };

            switch (storageType) {
            case 1: // newChild(tag) method
                try {
                    arr2[0] = tag;
                    arr2[1] = key;
                    childObject = newChildMethod
                            .invoke(parentObject, arr2);
                } catch (final Exception e) {
                    LOGGER.error("Exp=" + e.getMessage());
                }
                break;

            case 2: // field - single child . get it, and instantiate it if
                // required
                try {
                    childObject = field.get(parentObject);
                    if (childObject == null) {
                        childObject = fieldType.newInstance();
                        field.set(parentObject, childObject);
                    }
                } catch (final Exception e) {
                    LOGGER.error("Exp=" + e.getMessage());
                }
                break;

            case 3: // array. But a temporary ArrayList is created, and hence
                // adding is same as 4
            case 4:
                try { // instantaite an object and add it to the collection
                    childObject = fieldType.newInstance();
                    if (addRequiresKey) {
                        arr2[0] = key;
                        arr2[1] = childObject; // arr2[0] is already set to tag
                        addMethod.invoke(collectionObject, arr2);
                    } else {
                        arr1[0] = childObject;
                        addMethod.invoke(collectionObject, arr1);
                    }
                } catch (final Exception e) {
                    LOGGER.error("Exp=" + e.getMessage());
                }
                break;
            default:
                break;
            }
            return childObject;
        }

        /*
         * end tag recd for the tag. do we have to do anything?
         */
        void endChild(final Object parentObject) {
            switch (storageType) {
            case 1: // see if there is endChild() method
                if (endChildMethod != null)
                    try {
                        final Object[] a = { tag };
                        endChildMethod.invoke(parentObject, a);
                    } catch (final Exception e) {
                        LOGGER.error("Exp in endChild=" + e.getMessage());
                    }
                break;

            case 3: // array. convert ArrayList to array
                try {
                    final ArrayList al = (ArrayList) collectionObject;
                    final int len = al.size();
                    final int[] a = { len };
                    final Object o = Array.newInstance(fieldType, a);
                    for (int i = 0; i < len; i++)
                        Array.set(o, i, al.get(i));
                    field.setAccessible(true);
                    field.set(parentObject, o);
                } catch (final Exception e) {
                    LOGGER.error("Exp in end Child=" + e.getMessage());
                }
                break;

            default:
                break;
            }

        }
    }

    private class StackedObject {
        Object object;
        HashMap childInfos;

        StackedObject(final Object object) {
            this.object = object;
            childInfos = null;
        }
    }
}
