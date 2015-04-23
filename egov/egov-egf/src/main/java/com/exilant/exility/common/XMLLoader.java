package com.exilant.exility.common;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author raghu.bhandi
 * 
 *         XMLLoader loads an object from an XML source file. It can load a
 *         complete object map from a corresponding XML file
 * 
 *         1. XML uses only attributes of an node. e.g. <type> or <type
 *         attr1="vaal1" attr2=Val2 /> 2. Values of the type <tag>Some text
 *         value of tag</tag> are not used. If found, they are ignored 3.
 *         Hierarchies are possible. e.g. <group attr1="val1" attr2="val2">
 *         <member1 attr="val" junk="junk"/> .... </group>
 * 
 *         For an object to be 'loadable' follow these guidelines. (No interface
 *         is defined as these are naming conventions, and not methods) 1. For
 *         each possible atribute, define a field (data member) of appropriate
 *         data type. primitive data types, and string are the only ones to be
 *         used. 2. Alternately, you can define a HashMap named 'attributes'.
 *         All attributes, for which there is no corresponding field name, are
 *         stored in this hashmap as name/value pairs. 3. If the class/object
 *         can have children (child nodes) you should plan to load them as well
 *         4. Note that each node in the XML is loaded into one object by
 *         default. Each node will have a corresponding class 5. If a child node
 *         occurs only once, you can define a field for that with the name
 *         matching the tag name. Loader will load the child node into this
 *         object. If you just declare it without instantiatiing it, Loader
 *         tries teh defalt constructor to iinstantiate it 6. If a type of child
 *         node occurs more than once, you can either plan to store them in an
 *         array or a HashMap If you need to locate them using key, then HashMap
 *         is the preferred way, while array is effecient. You can also store
 *         them in ArrayList if you have to add child nodes later (after loader
 *         loads them) Loader looks for a field with the name =tagname + 's' .
 *         For e.g. <main > <child id="a1" someattribute="value"/> <child
 *         id="a2" someattribute="somevalue"/> </main> in this case, loader
 *         looks for a field with name childs and tries to load all child nodes
 *         into this field 7. If array is defined, Loader can determine the
 *         calss (Type) of child obbjects. However, if it is HashMap or
 *         ArrayList, Loader can not figure out the calss (type) to be used to
 *         instantiate teh child object. You have to declare a field with the
 *         name of the tag for this. For example, for the above xml, define
 *         ChildClass child; HashMap children; 8. The key for HashMap: attribute
 *         with name id, key, and name are searched, in that order and used as
 *         key for HashMap. 9. If your needs to store child objects are more
 *         complex than this, then you can write your own method. a method
 *         newChild(String tag, String key) is called by the loader whenever a
 *         child node is encountered you can instantiate a child object, store
 *         it appropriately, and return the reference for the loader to load it.
 *         Loader calls endChild(String tag) when the end tag is encountered.
 *         This method is optional and you need not provide this. It is called
 *         only if it is defined. 10. Tag name of the node is stored in a String
 *         field with name as 'node' if such a field is defined for the object
 *         (note: if you also have an attribute with name as 'type', the
 *         attribute value over-rides the tag name) 11. If any field is of type
 *         boolean, then you should specify "true", "yes" or "1" as the value
 *         for true. else it is considered to be false;
 * 
 */
public class XMLLoader extends DefaultHandler {
	/*
	 * Each open node is pushed to the stack. Information about each type of
	 * child nodes is stored and saved along with the object in teh stack. Look
	 * at StackedObject and ChildInfo classes for details
	 */
	Stack openNodes; // keeps track of open nodes in the XML.

	Object rootObject; // object to which the XML is to be loaded
	private static final Logger LOGGER = Logger.getLogger(XMLLoader.class);

	public XMLLoader() {
	}

	public void load(String fileName, Object root) {
		this.rootObject = root;
		this.openNodes = new Stack();
		load(fileName);
		this.rootObject = null;
		this.openNodes = null;
	}

	/**************************************
	 * 
	 * @param fileName
	 *            Uses a SAXParser to parse and load the XML into the object
	 *            that was set using setObject() method
	 * 
	 ***************************************/
	private void load(String fileName) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();

			spf.setNamespaceAware(false);

			// Validation part 1: set whether validation is on
			spf.setValidating(false);

			// Create a JAXP SAXParser
			SAXParser saxParser = spf.newSAXParser();

			// Get the encapsulated SAX XMLReader
			XMLReader xmlReader = saxParser.getXMLReader();
			// Set the ContentHandler of the XMLReader
			xmlReader.setContentHandler(this);

			// Set an ErrorHandler before parsing. This error handler is
			// defifined as a subClass in this class
			xmlReader.setErrorHandler(new MyErrorHandler(System.err));

			// Tell the XMLReader to parse the XML document
			xmlReader.parse(fileName);
		} catch (ParserConfigurationException e) {
			LOGGER.error("Exp=" + e.getMessage());
		} catch (SAXException e) {
			LOGGER.error("Exp=" + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Exp=" + e.getMessage());
		} finally {
			if(LOGGER.isInfoEnabled())     LOGGER.info("Finally in load");
		}
	}

	/**********************************
	 * Call back methods used by SaxLoader. startElement() and endElement()
	 */

	/*
	 * Start element is called by the parser whenever the beginning tag is
	 * parsed
	 * 
	 * Our assumption is that the tags contian only attribute, and no text
	 * value. i.e we do not expect a case like <type>Some value </type> qName is
	 * the tag name, and atts contains all name/value pairs of attribute say
	 * <tag name1="value1" name2="value2"...
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		Object objectToLoad = null;

		if (openNodes.size() == 0) { // nothing in the stack. This is the root
										// node
			objectToLoad = this.rootObject; // load the root node to root object
		} else { // create a child object for this tag
			objectToLoad = createChild(qName, atts);
		}

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
	 * endElement() is called by the parser when the /> or </element> is
	 * encountered
	 */
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		StackedObject so = (StackedObject) openNodes.pop();
		Object endingObject = so.object;
		if (endingObject == null)
			return;

		// Call endChild() for all of the childInfo() that we may have
		if (so.childInfos != null && so.childInfos.size() > 0) {
			Iterator iter = so.childInfos.values().iterator();
			while (iter.hasNext()) {
				((ChildInfo) iter.next()).endChild(endingObject);
			}
		}
	}

	/*
	 * Whenevr a child node is detected by the parser, a new Object is to be
	 * supplied to set all the attributes. This helper class tries different
	 * conventions to create a child object
	 */
	private Object createChild(String tag, Attributes atts) {
		if (openNodes.size() == 0)
			return null;

		StackedObject stackedObject = (StackedObject) openNodes.peek();
		Object parentObject = stackedObject.object;
		if (null == stackedObject.childInfos)
			stackedObject.childInfos = new HashMap();
		ChildInfo childInfo = null;

		if (stackedObject.childInfos.size() > 0) {
			try {
				childInfo = (ChildInfo) stackedObject.childInfos.get(tag);
			} catch (Exception e) {
				LOGGER.error("Error in getting child info" + e.getMessage());
			}
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
		} catch (Exception e) {
			LOGGER.error("Exp=" + e.getMessage());
		}

		if (null == key) {
			try {
				key = atts.getValue("key");
			} catch (Exception e) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp=" + e.getMessage());
			}
		}

		if (null == key) {
			try {
				key = atts.getValue("name");
			} catch (Exception e) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp=" + e.getMessage());
			}
		}

		return childInfo.createChild(parentObject, key);

	}

	/*
	 * 
	 * Error handler is defined and supplied to the parser to report errors
	 * encountered during parsing Note that we output the error message, and
	 * continue..
	 */
	private class MyErrorHandler implements ErrorHandler {
		/** Error handler output goes here */
		private PrintStream out;

		MyErrorHandler(PrintStream out) {
			this.out = out;
		}

		/**
		 * Returns a string describing parse exception details
		 */
		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
			if (systemId == null) {
				systemId = "null";
			}
			String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
					+ ": " + spe.getMessage();
			return info;
		}

		// The following methods are standard SAX ErrorHandler methods.
		// See SAX documentation for more info.

		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	}

	/*
	 * Holds all information required to instantiate an object whenever a tag of
	 * this type is encountered. Has method to create child, as well as extract
	 * and store information
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

		ChildInfo(String tag) {
			this.tag = tag;
		}

		/*
		 * Extracts information about the way the child Object is to be created.
		 * Uses reflection to check what the parentObject has planned to create
		 * add child nodes This method is long, as we explore different
		 * options..
		 */
		public void addInfo(Object parentObject) {

			// class arrays required for getMethod() calls
			Class[] stringClass = { String.class };
			Class[] stringClass2 = { String.class, String.class };
			Class[] objClass = { Object.class };
			Class[] objClass2 = { Object.class, Object.class };
			/*
			 * Is this an advanced programmer who has written her own
			 * addChild(tag, key) method?
			 */
			try {
				this.newChildMethod = parentObject.getClass().getMethod(
						"newChild", stringClass2);
				// thrown out if the above line does not succeed
				// now that we got the method, let us set other properties of
				// childInfo
				this.collectionObject = parentObject;
				this.addRequiresKey = true;
				this.storageType = 1;
				/*
				 * check if she has defined the optional method endChild(tag)
				 */
				try {
					this.endChildMethod = parentObject.getClass().getMethod(
							"endChild", stringClass);
				} catch (Exception e) {
					LOGGER.error("Exp=" + e.getMessage());
				}

				return; // if this method is available, we do not look for
						// anything else.

			} catch (Exception e) {
				LOGGER.error("Exp=" + e.getMessage());
			}

			/*
			 * Is there a field with name = tag?
			 */
			try {
				this.field = ObjectGetSetter.getField(parentObject, this.tag);// throws
																				// exception
																				// if
																				// field
																				// not
																				// found
				this.field.setAccessible(true);
				this.fieldType = this.field.getType();
				this.storageType = 2; // object class. If it is to be a single
										// child
				// this will be over-ridden subsequently if collection is
				// discovered..
				// no return. We should explore further....
			} catch (Exception e) {
				LOGGER.error("Exp=" + e.getMessage());
			}

			/*
			 * Is there a collection object that holds all children? Common ones
			 * are Array and HashMap. name is assumed to be plural of tag.
			 * example, if tag=lion, we look for a field with name lions Bare
			 * with me: we do not understand English Grammer to make child to
			 * children. We will look for 'childs' if tag is 'child'
			 */

			try {
				Field f = ObjectGetSetter
						.getField(parentObject, this.tag + 's'); // thrown out
																	// if field
																	// not found
				Class c = f.getType();

				// is it an array?
				if (c.isArray()) {
					this.storageType = 3; // array
					this.collectionObject = new ArrayList(); // store here and
																// create array
																// later
					this.addRequiresKey = false;
					this.addMethod = ArrayList.class.getMethod("add", objClass);
					this.field = f;
					this.fieldType = c.getComponentType();
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
						this.addMethod = c.getMethod("add", objClass);
						this.collectionObject = o; // o stores all children
						this.addRequiresKey = false;
						this.storageType = 4;
					} catch (Exception e) {
						try {// may be it has put(key, Object) method..
							this.addMethod = c.getMethod("put", objClass2);
							this.collectionObject = o;
							this.addRequiresKey = true;
							this.storageType = 4;
						} catch (Exception e1) {
							LOGGER.error("Exp=" + e1.getMessage());
						}
						LOGGER.error("Exp ObjectGetSetter=" + e.getMessage());
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exp=" + e.getMessage());
			}
			// if all exploration failed, storageType will continue to be 0
			// implying inability to create child object
		}

		/*
		 * Instantiate a child object based on this info for the supplied
		 * parentObject
		 */
		public Object createChild(Object parentObject, String key) {

			Object childObject = null;
			Object[] arr1 = { null }; // for method.invoke()..
			Object[] arr2 = { null, null };

			switch (this.storageType) {
			case 1: // newChild(tag) method
				try {
					arr2[0] = this.tag;
					arr2[1] = key;
					childObject = this.newChildMethod
							.invoke(parentObject, arr2);
				} catch (Exception e) {
					LOGGER.error("Exp=" + e.getMessage());
				}
				break;

			case 2: // field - single child . get it, and instantiate it if
					// required
				try {
					childObject = this.field.get(parentObject);
					if (childObject == null) {
						childObject = this.fieldType.newInstance();
						this.field.set(parentObject, childObject);
					}
				} catch (Exception e) {
					LOGGER.error("Exp=" + e.getMessage());
				}
				break;

			case 3: // array. But a temporary ArrayList is created, and hence
					// adding is same as 4
			case 4:
				try { // instantaite an object and add it to the collection
					childObject = this.fieldType.newInstance();
					if (this.addRequiresKey) {
						arr2[0] = key;
						arr2[1] = childObject; // arr2[0] is already set to tag
						this.addMethod.invoke(this.collectionObject, arr2);
					} else {
						arr1[0] = childObject;
						this.addMethod.invoke(this.collectionObject, arr1);
					}
				} catch (Exception e) {
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
		void endChild(Object parentObject) {
			switch (this.storageType) {
			case 1: // see if there is endChild() method
				if (this.endChildMethod != null) {
					try {
						Object[] a = { this.tag };
						this.endChildMethod.invoke(parentObject, a);
					} catch (Exception e) {
						LOGGER.error("Exp in endChild=" + e.getMessage());
					}
				}
				break;

			case 3: // array. convert ArrayList to array
				try {
					ArrayList al = (ArrayList) (this.collectionObject);
					int len = al.size();
					int[] a = { len };
					Object o = Array.newInstance(this.fieldType, a);
					for (int i = 0; i < len; i++)
						Array.set(o, i, al.get(i));
					this.field.setAccessible(true);
					this.field.set(parentObject, o);
				} catch (Exception e) {
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

		StackedObject(Object object) {
			this.object = object;
			this.childInfos = null;
		}
	}
}