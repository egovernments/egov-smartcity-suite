module Stores_constants

INPUT_OO = path_to_file("Material_Indent.ods")

END_OF_DATA = '#'
#~ Watir::Browser.default = 'firefox'
#~ Watir.options_file = path_to_file("options.yml")

INTERNAL_URL=ENV['APP_URL']
STORES_URL = INTERNAL_URL + '/egi/login/securityLogin.jsp'

####################################     MASTERS URL'S     #######################################

#~ **************************************    ITEM MASTERS     *******************************************************

CREATE_MATERIAL_LINK = 'Create Material'
VIEW_MATERIAL_LINK = 'View Material'
MODIFY_MATERIAL_LINK = 'Modify Material'

#~ ********************************************************   STORES  ****************************************************************************************************

CREATE_STORES_LINK = 'Stores Master'
VIEW_STORES_LINK = 'Stores Master'
MODIFY_STORES_LINK = 'Stores Master'

#~  ************************************************************  SUPPLIER  *************************************************************************************************

CREATE_SUPPLIER_LINK = 'Create Supplier'
VIEW_SUPPLIER_LINK = 'View Supplier'
MODIFY_SUPPLIER_LINK = 'Modify Supplier'

#~ ******************************************************************** ITEM TYPE   *****************************************************************************************

CREATE_ITEM_TYPE_LINK = 'Material Type'
VIEW_ITEM_TYPE_LINK = 'Material Type'
MODIFY_ITEM_TYPE_LINK = 'Material Type'

#~***********************************************************************  UOM CATEGORY   **********************************************************************************

CREATE_UOM_CATEGORY_LINK = 'UOM Category'
VIEW_UOM_CATEGORY_LINK = 'UOM Category'
MODIFY_UOM_CATEGORY_LINK = 'UOM Category'

#~***************************************************************************    UOM    ***************************************************************************************

CREATE_UOM_LINK = 'Unit of Measurement'
VIEW_UOM_LINK = 'Unit of Measurement'
MODIFY_UOM_LINK = 'Unit of Measurement'

#~ ***************************************************************************************************************************************************************************


#####################################   TRANSACTION URL'S      ###########################################

#~ **************************************************************   MATERIAL_INDENT         **********************************************************************************

CREATE_MATERIAL_INDENT_LINK = 'Create IMI'
VIEW_MATERIAL_INDENT_LINK = 'View IMI'
APPROVE_REJECT_MATERIAL_INDENT_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
APPROVE_MATERIAL_INDENT_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
REJECT_MATERIAL_INDENT_URL =  INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'

#~  *********************************************************************     MMRN     ***************************************************************************************

CREATE_MMRN_LINK = 'Create'
VIEW_MMRN_LINK = 'View'

APPROVE_MMRN_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'    
REJECT_MMRN_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'    

#~  ***************************************************************  GENERAL MATERIAL ISSUE    *****************************************************************************

CREATE_GMI_LINK = 'Create General Issue'
VIEW_GMI_LINK = 'View Material Issue'

APPROVE_GMI_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'    
REJECT_GMI_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'    

#~  *******************************************************************    MATERIAL INDENDT ISSUE        ********************************************************************

CREATE_MII_LINK = 'Create Material Indent Issue'
VIEW_MII_LINK = 'View Material Issue'

APPROVE_MII_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
REJECT_MII_URL =  INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action' 


#~  ************************************************************************  MRN **************************************************************************************

STORES_CREATE_MRN_URL = INTERNAL_URL + '/stores/inventory/mrn.do?submitType=loadSearchForMRIN&actionid=94'
STORES_APPROVE_MRN_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
STORES_REJECT_MRN_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'

#~  **********************************************************************  INTERBIN TRANSFER  ******************************************************************

VIEW_INTERBIN_TRANSFER_URL= INTERNAL_URL + '/stores/inventory/interBinTransfer.do?submitType=loadSearchInterBinTransfers&actionid=143'
VIEW_GET_DETAILS=INTERNAL_URL+ '/stores/inventory/interBinTransfer.do?submitType=searchInterBinTransfers#'
CREATE_INTERBIN_TRANSFER_URL=INTERNAL_URL + '/stores/inventory/interBinTransfer.do?submitType=loadInterBinTransferForm&actionid=142'


#~ ********************************************************************* PURCHASE ORDER ***********************************************************

STORES_CREATE_PO_NON_INDENT_LINK = 'Create Purchase Order - Non Indent'
STORES_CREATE_PO_INDENT_LINK = 'Create Purchase Order'
STORES_VIEW_PO_INDENT_AND_NON_INDENT_LINK = 'View Purchase Order'
STORES_MODIFY_PO_NON_INDENT_LINK = ''

STORES_APPROVE_REJECT_PO_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
STORES_REJECT_PO_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
STORES_MODIFY_PO_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'


#~  ********************************************************************  SUPPLIER BILL *******************************************************************************

STORES_CREATE_SUPPLIER_BILL_LINK = 'Create Supplier Bill'
STORES_VIEW_SUPPLIER_BILL_LINK = 'View Supplier Bill'

STORES_APPROVE_REJECT_SUPPLIER_BILL_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'
STORES_MODIFY_SUPPLIER_BILL_URL = INTERNAL_URL + '/egi/common/homepage!redirectHomepage.action'



#~ *************************************************   MATERIAL TRANFER NOTE   **************************************************

CREATE_MTN_INDENT_LINK = 'Indent'
CREATE_MTN_OUTWARD_LINK = 'Create MTN-Outward'
CREATE_MTN_NON_INDENT_TYPE_LINK = 'Issue-Non Indent Type'
CREATE_MTN_INWARD_LINK = 'Create IMRN'


#~ *************************************************  STORES LEDGER   **************************************************

STORES_LEDGER_LINK='Stores Ledger'

#############################################  TEST_DATA_SHEETS  ###########################################
 
# ITEM_SHEET 
CREATE_ITEM_SHEET = 'create_item'
VIEW_ITEM_SHEET = 'view_item'
MODIFY_ITEM_SHEET = 'modify_item'

# SUPPLIER_SHEET 
CREATE_SUPPLIER_SHEET = 'create_supplier'
VIEW_SUPPLIER_SHEET = 'view_supplier'
MODIFY_SUPPLIER_SHEET = 'modify_supplier'

#CREATE STORES SHEET
CREATE_STORES_SHEET = 'create_stores' 
VIEW_STORES_SHEET = 'view_stores'
MODIFY_STORES_SHEET ='modify_stores'

#ITEM_TYPE_SHEET
CREATE_ITEM_TYPE_SHEET = 'create_item_type'
VIEW_ITEM_TYPE_SHEET = 'view_item_type'
MODIFY_ITEM_TYPE_SHEET = 'modify_item_type'

#UOM CATEGORY SHEET
CREATE_UOM_CATEGORY_SHEET = 'create_uom_category'
VIEW_UOM_CATEGORY_SHEET = 'view_uom_category'
MODIFY_UOM_CATEGORY_SHEET = 'modify_uom_category'

#UOM SHEETS
CREATE_UOM_SHEET = 'create_uom'
VIEW_UOM_SHEET = 'view_uom'
MODIFY_UOM_SHEET = 'modify_uom'

#MATERIAL_INDENT_SHEET
CREATE_INDENT_SHEET = 'create_indent'
VIEW_INDENT_SHEET = 'view_indent'
APPROVE_INDENT_SHEET = 'approve_indent'

#CREATE_MMRN_SHEETS
CREATE_MMRN_SHEET = 'create_mmrn' 
VIEW_MMRN_SHEET = 'view_mmrn'
APPROVE_MMRN_SHEET = 'approve_mmrn'

#CREATE GMI SHEET
CREATE_GMI_SHEET = 'create_GMI'
VIEW_GMI_SHEET = 'view_GMI'
APPROVE_GMI_SHEET = 'approve_GMI'

#CREATE MATERIAL INDENT ISSUE
CREATE_MII_SHEET = 'create_MII'
VIEW_MII_SHEET = 'view_MII'
APPROVE_MII_SHEET = 'approve_MII'

#~ INTERBIN_TRANSFER_SHEET
CREATE_INTERBIN_TRANSFER_SHEET ='create_interbin_transfer'

#~ PURCHASE ORDER SHEET
CREATE_PO_SHEET = 'Create PO'
APPROVE_REJECT_PO_SHEET = 'po_approve_reject'
MODIFY_PO_SHEET = 'modify_po'

 #~ SUPPLLIER BILL
STORES_CREATE_SUPPLIER_BILL_SHEET = 'create_supplier_bill'
STORES_APPROVE_REJECT_SUPPLIER_BILL_SHEET = 'approve_reject_supplier_bill'
STORES_MODIFY_SUPPLIER_BILL_SHEET = 'modify_supplier_bill'

#CREATE MTN SHEET
CREATE_MTN_SHEET = 'create_mtn'
APPROVE_MTN_SHEET = 'approve_mtn'

#MODIFY INDENT ISSUE
MODIFY_MII_SHEET='modify_indent_issue'

#MODIFY INDENT 
MODIFY_INDENT_SHEET='modify_indent_sheet'

#STORES LEDGER
STORES_VIEW_LEDGER_SHEET='stores_ledger'

##############################################################################################


# Login  & Logout
STORES_USER_NAME = 'egovernments'
STORES_PASSWORD = 'egovfinancials'
STORES_USER_NAME1 = 'hod'
STORES_PASSWORD1 = 'egovfinancials'

STORES_LOGIN_BUTTON = 'Login'
STORES_LOGIN_USERNAME_FLD = 'j_username'
STORES_LOGIN_PASSWORD_FLD = 'j_password'
STORES_LOGIN_BTN = 'Login'
STORES_LOGOUT_BTN = 'Sign out'

STORES_FRAME_NAME = 'mainFrame'
MAIN_FRAME = 'mainFrame'
LEFT_FRAME_NAME = 'leftFrame'
INBOX_FRAME = 'inboxframe'
INVENTORY_MANAGEMENT_SUCCESS_MESG = 'Sign out'
STORES_LOGIN_SUCCESS_MSG = 'Sign out'
INBOX_SUCCESS_MESG = 'Inbox'
EGI_MENU_INVENTORY_MANAGEMENT_LINK = 'Inventory Management'

STORES_INBOX_DRAFTS_FOLDER = 'Drafts'
STORES_INBOX_WORKS_FOLDER = 'Inbox'

STORES_TRANSACTIONS_LINK = 'rightpersonalboxplain'
STORES_TRANSACTIONS_DRAFTS_LINK = 'Drafts'
STORES_TRANSACTIONS_INBOX_LINK = 'Inbox'


#CREATE ITEM
CREATE_ITEM_NO = 'ITEM_NO'
CREATE_ITEM_TYPE = 'ITEM_TYPE'
CREATE_ITEM_DESCRIPTION = 'ITEM_DESCRIPTION'
CREATE_BASE_UOM = 'BASE_UOM'
CREATE_ITEM_STATUS = 'ITEM_STATUS'
CREATE_ITEM_SCRAP_ITEM = 'SCRAP_ITEM'
CREATE_ITEM_PURCHASED = 'ITEM_PURCHASED'
CREATE_CONTRACT_REQD = 'CONTRACT_REQD'
CREATE_ITEM_LOT_CONTROL = 'LOT_CONTROL'
CREATE_ITEM_SHELF_LIFE_CONTROL = 'SHELF_LIFE_CONTROL'
CREATE_ITEM_BIN_LEVEL_STORAGE = 'BIN_LEVEL_STORAGE'
CREATE_ITEM_BUTTON = 'Save & New'
CREATE_ITEM_SUCCESS_MESG = 'eGov Stores - Material'
CREATE_ITEM_FAILURE_MESG = 'eGov Stores - Material'
INVENTORY_ASSET = 'isInventoryAssest'
CREATE_ITEM_INDENT_REQUIRED_CHECK_BOX = 'CREATE_ITEM_INDENT_REQUIRED_CHECK_BOX'
CREATE_SUCCESS_DATA = 'CREATE_SUCCESS_DATA1'
CREATE_FAILURE_DATA = 'CREATE_FAILURE_DATA1'
CREATE_ITEM_SCREEN = 'eGov Stores - Material'
CREATE_ITEM_DEPARTMENT_FROM_FLD = 'CREATE_ITEM_DEPARTMENT_FROM_FLD'
CREATE_ITEM_ADD_DEPARTMENT_FLD = 'addDept'
CREATE_ITEM_ADD_DEPARTMENT_FIELD = 'CREATE_ITEM_ADD_DEPARTMENT_FIELD'
INDENT_REQUIRED_CHECK_BOX = 'INDENT_REQUIRED_CHECK_BOX'
CREATE_DEPT_MULTIPLE_SELECT_LIST_LOOP = 'CREATE_DEPT_MULTIPLE_SELECT_LIST_LOOP'

#VIEW ITEM
VIEW_ITEM_NUMBER = 'VIEW_ITEM_NUMBER1'
VIEW_BUTTON = 'View'
ITEM1 = 'funcImg'
VIEW_ITEM_TITLE = 'eGov Stores - Material'
VIEW_ITEM_SUCCESS_MSG  = 'eGov Stores - Material'
VIEW_ITEM_FAILURE_MSG = 'eGov Stores - Material'

#MODIFY ITEM
MODIFY_ITEM_NUMBER = 'MODIFY_ITEM_NUMBER1'
MODIFY_ITEM = 'MODIFY_ITEM1'
MODIFY_PURCHASE_UOM = 'MODIFY_PURCHASE_UOM1'
MODIFY_ITEM_DESCRIPTION = 'MODIFY_ITEM_DESCRIPTION1'
MODIFY_ITEM_TYPE = 'MODIFY_ITEM_TYPE1'
MODIFY_BASE_UOM = 'MODIFY_BASE_UOM1'
MODIFY_BUTTON = 'Modify'
MODIFY_CONTRACT_REQD = 'MODIFY_CONTRACT_REQD1'
ALLOW_NEGATIVE_QUANTITIES = 'allowNegetiveQty'
SAVE_BUTTON = 'Save'
MODIFY_ITEM_SUCCESS_MSG = 'eGov Stores - Material'
MODIFY_ITEM_FAILURE_MSG = 'eGov Stores - Material'
MODIFY_SHELF_LIFE_CONTROL = 'isShelfLifeControlled'
MODIFY_SUCCESS_DATA = 'MODIFY_SUCCESS_DATA1'
MODIFY_FAILURE_DATA = 'MODIFY_FAILURE_DATA1'
MODIFY_ITEM_SCREEN = 'eGov Stores - Material'

#~ MODIFY_ITEM_TYPE = 'combocell'


#CREATE STORES 
CREATE_STORES_SUCCESS_MSG = 'eGov Stores - Store'
CREATE_STORES_FAILURE_MSG = 'eGov Stores - Store'
STORES_FLD = 'storeId' 
STORES_NEW_BTN = 'new'
CREATE_STORE_NAME_FLD = 'CREATE_STORE_NAME_FLD1'
CREATE_STORE_NO_FLD = 'CREATE_STORE_NO_FLD1'
CREATE_STORE_DESC_FLD = 'CREATE_STORE_DESC_FLD1'
CREATE_DEPT_NAME_FLD = 'CREATE_DEPT_NAME_FLD1'
CREATE_DELIVERY_ADDRESS_FLD = 'CREATE_DELIVERY_ADDRESS_FLD1'
CREATE_BILLING_ADDRESS_FLD = 'CREATE_BILLING_ADDRESS_FLD1'
RADIO_BTN ='binradio'
CREATE_BIN_CODE_FLD ='CREATE_BIN_CODE_FLD'
CREATE_BIN_NAME_FLD = 'CREATE_BIN_NAME_FLD'
CREATE_BIN_STATUS_FLD = 'CREATE_BIN_STATUS_FLD'
CREATE_STORES_BTN = 'button' 
FOR_LOOP_STORES= 'FOR_LOOP_STORES1'
CREATE_BIN_TABLE = 'binInfo'
CREATE_BIN = 'CREATE_BIN1'
CREATE_STORES_SUCCESS = 'CREATE_STORES_SUCCESS'
CREATE_STORES_FAILURE = 'CREATE_STORES_SUCCESS'
CREATE_STORES_SCREEN = '* - Mandatory Fields'


#VIEW STORES
VIEW_STORES_BTN = 'view'
VIEW_STORES_NUM_FLD = 'VIEW_STORES_NUM_FLD1'
VIEW_STORES_SUCCESS_MESG = 'eGov Stores - Store'
VIEW_STORES_FAILURE_MESG = 'eGov Stores - Store'
VIEW_STORES_SCREEN = 'eGov Stores - Store'

#MODIFY_STORES
MODIFY_STORES_NUM_FLD = 'MODIFY_STORES_NUM_FLD1'
MODIFY_STORES_BTN = 'modify'
MODIFY_STORE_NAME_FLD = 'MODIFY_STORE_NAME_FLD1'
MODIFY_STORE_NO_FLD = ' MODIFY_STORE_NO_FLD1'
MODIFY_STORE_DESC_FLD = ' MODIFY_STORE_DESC_FLD1'
MODIFY_DEPT_NAME_FLD = ' MODIFY_DEPT_NAME_FLD1'
MODIFY_DELIVERY_ADDRESS_FLD = ' MODIFY_DELIVERY_ADDRESS_FLD1'
MODIFY_BILLING_ADDRESS_FLD = ' MODIFY_BILLING_ADDRESS_FLD1'
MODIFY_BIN_CODE_FLD = ' MODIFY_BIN_CODE_FLD1'
MODIFY_BIN_NAME_FLD = ' MODIFY_BIN_NAME_FLD1'
MODIFY_STATUS_FLD = ' MODIFY_STATUS_FLD1'
MODIFY_STORES_BTTN = 'save'
MODIFY_STORES_SUCCESS_MESG = 'eGov Stores - Store'
MODIFY_STORES_SUCCESS = 'MODIFY_STORES_SUCCESS'
MODIFY_STORES_FAILURE = 'MODIFY_STORES_FAILURE'

#CREATE SUPPLIER
CREATE_SUPPLIER_CODE_FLD = 'CREATE_SUPPLIER_CODE_FLD1'
CREATE_SUPPLIER_NAME_FLD = 'CREATE_SUPPLIER_NAME_FLD1'
CREATE_SUPPLIER_CORS_ADDRESS_FLD = 'CREATE_SUPPLIER_CORS_ADDRESS_FLD1'
CREATE_SUPPLIER_STATUS_FLD = 'CREATE_SUPPLIER_STATUS_FLD1'
CREATE_SUPPLIER_PAN_NO_FLD = 'CREATE_SUPPLIER_PAN_NO_FLD1'
CREATE_SUPPLIER_BTN = 'b4'
CREATE_SUPPLIER_SUCCESS_MESG = 'eGov Stores - Supplier'
CREATE_SUPPLEIR_SUCCESS = 'CREATE_SUPPLEIR_SUCCESS'
CREATE_SUPPLIER_FAILURE = 'CREATE_SUPPLIER_FAILURE'
CREATE_SUPPLIER_SCREEN = 'eGov Stores - Supplier'

#VIEW SUPPLIER
VIEW_SUPPLIER_NAME_FLD = ' VIEW_SUPPLIER_NAME_FLD1'
VIEW_SUPPLIER_SEARCH_BTN = 'b1'
VIEW_SUPPLIER_SUCCESS_MESG = 'Code'
VIEW_SUPPLIER_FAILURE_MESG = 'View Supplier'
VIEW_SUPPLIER_AUTOCOMPLETE_VALUE = 'name'
VIEW_SUPPLIER_SCREEN = 'eGov Stores - Supplier'

# MODIFY SUPPLIER
MODIFY_SUPPLIER_SEARCH_FLD = 'MODIFY_SUPPLIER_SEARCH_FLD1'
MODIFY_SUPPLIER_SEARCH_BTN = 'b4'
MODFIY_SUPPLIER_CODE_FLD = 'MODFIY_SUPPLIER_CODE_FLD1'
MODIFY_SUPPLIER_NAME_FLD = 'MODIFY_SUPPLIER_NAME_FLD1'
MODIFY_SUPPLIER_CORS_ADDRESS_FLD = 'MODIFY_SUPPLIER_CORS_ADDRESS_FLD1'
MODIFY_SUPPLIER_PAN_NO_FLD = 'MODIFY_SUPPLIER_PAN_NO_FLD1'
MODIFY_SUPPLIER_STATUS_FLD = 'MODIFY_SUPPLIER_STATUS_FLD1'
MODIFY_SUPPLIER_DATE_FLD = 'MODIFY_SUPPLIER_DATE_FLD1'
MODIFY_SUPPLIER_BTN = 'buttonsubmit'
MODIFY_SUPPLIER_SUCCEESS_MESG = 'Code'
MODIFY_SUPPLIER_FIALURE_MESG = 'Supplier'
MODIFY_SUPPLIER_SUCCESS = 'MODIFY_SUPPLIER_SUCCESS'
MODIFY_SUPPLIER_FAILURE = 'MODIFY_SUPPLIER_FAILURE'
MODIFY_SUPPLIER_SCREEN = 'eGov Stores - Supplier'



#CREATE ITEM TYPE CONSTANTS
CREATE_NEW_ITEM_TYPE_BTN = 'new'
CREATE_ITEM_TYPE_FLD = 'CREATE_ITEM_TYPE_FLD1'
CREATE_ITEM_TYPE_DESC_FLD = 'CREATE_ITEM_TYPE_DESC_FLD1'
CREATE_ITEM_TYPE_STOCK_CODE_FLD = 'CREATE_ITEM_TYPE_STOCK_CODE_FLD1'
CREATE_ITEM_TYPE_PARENT_TYPE_FLD = 'CREATE_ITEM_TYPE_PARENT_TYPE_FLD1'
CREATE_NEW_ITEM_TYPE_BUTN = 'button'
CREATE_ITEM_TYPE_SUCCESS_MESG = 'eGov Stores - Material Type'
CREATE_ITEM_TYPE_FAILURE_MESG = 'eGov Stores - Material Type'
CREATE_ITEMTYPE_SUCCESS = 'CREATE_ITEMTYPE_SUCCESS1'
CREATE_ITEMTYPE_FIALURE = 'CREATE_ITEMTYPE_FIALURE1'
CREATE_ITEM_TYPE_SCREEN ='eGov Stores - Material Type'


#VIEW ITEM TYPE CONSTANTS
VIEW_ITEM_TYPE_FLD = 'VIEW_ITEM_TYPE_FLD1'
VIEW_ITEM_TYPE_BUTTON = 'view'
VIEW_ITEM_TYPE_SUCCESS_MESG = '* - Mandatory Fields'
VIEW_ITEM_TYPE_FAILURE_MESG = 'eGov Stores - Material Type'
VIEW_ITEM_TYPE_TITLE = 'eGov Stores - Material Type'


#MODFIY ITEM TYPE CONSTANTS
MODFIY_ITEM_TYPE_FIELD = 'MODFIY_ITEM_TYPE_FIELD1'
MODIFY_ITEM_TYPE_BTN = 'modify'
MODIFY_ITEM_TYPE_BTTN = 'save'
MODIFY_ITEM_TYPE_FLD = 'MODIFY_ITEM_TYPE_FLD1'
MODIFY_ITEM_TYPE_DESC_FLD = 'MODIFY_ITEM_TYPE_DESC_FLD1'
MODIFY_ITEM_TYPE_STOCK_CODE_FLD = 'MODIFY_ITEM_TYPE_STOCK_CODE_FLD1'
MODIFY_ITEM_TYPE_PARENT_TYPE_FLD = 'MODIFY_ITEM_TYPE_PARENT_TYPE_FLD1'
MODIFY_ITEM_TYPE_SUCCESS_MESG = 'eGov Stores - Material Type'
MODIFY_ITEM_TYPE_FAILURE_MESG = 'eGov Stores - Material Type'
MODIFY_ITEMTYPE_SUCCESS = 'MODIFY_ITEMTYPE_SUCCESS'
MODIFY_ITEMTYPE_FAILURE = 'MODIFY_ITEMTYPE_FAILURE'
MODIFY_ITEM_TYPE_SCREEN = 'eGov Stores - Material Type'


#CREATE UOM CATEGORY
CREATE_NEW_UOM_CATEGORY_BTN = 'new'
CREATE_UOM_CATEGORY_FLD = 'CREATE_UOM_CATEGORY_FLD1'
CREATE_UOM_CATEGORY_DESC_FLD = 'CREATE_UOM_CATEGORY_DESC_FLD1'
CREATE_UOM_CATEGORY_BTN = 'b1'
CREATE_UOM_CATEGORY_SUCCESS_MESG = 'eGov Stores - UOM Category'
CREATE_UOM_CATEGORY_FAILURE_MESG = 'eGov Stores - UOM Category'
CREATE_UOM_CATEGORY_FAILURE = 'CREATE_UOM_CATEGORY_FAILURE'
CREATE_UOM_CATEGORY_SUCCESS = 'CREATE_UOM_CATEGORY_SUCCESS'
CREATE_UOM_CATEGORY_SCREEN = 'eGov Stores - UOM Category'

#VIEW UOM CATEGORY
VIEW_UOM_CATEGORY_FLD = 'VIEW_UOM_CATEGORY_FLD1'
VIEW_UOM_CATEGORY_BTN = 'view'
VIEW_UOM_CATEGORY_SUCCESS_MESG = '* - Mandatory Fields'
VIEW_UOM_CATEGORY_FAILURE_MESG = 'eGov Stores - UOM Category'
VIEW_UOM_CATEGORY_SCREEN = 'eGov Stores - UOM Category'


#MODIFY UOM CATEGORY
MODIFY_UOM_CATEGORY_FLD = 'MODIFY_UOM_CATEGORY_FLD1'
MODIFY_UOM_CATEGORY_BTN = 'modify'
MODIFY_UOM_CATEGORY_FIELD = 'MODIFY_UOM_CATEGORY_FIELD1'
MODIFY_UOM_CATEGORY_DESC_FLD = 'MODIFY_UOM_CATEGORY_DESC_FLD1'
MODIFY_UOM_CATEGORY_BTTN = 'save'
MODIFY_UOM_CATEGORY_SUCCESS_MESG = 'eGov Stores - UOM Category'
MODIFY_UOM_CATEGORY_FAILURE_MESG = 'eGov Stores - UOM Category'
MODIFY_UOM_CATEGORY_FAILURE = 'MODIFY_UOM_CATEGORY_FAILURE'
MODIFY_UOM_CATEGORY_SUCCESS = 'MODIFY_UOM_CATEGORY_SUCCESS'
MODIFY_UOM_CATEGORY_SCREEN = 'eGov Stores - UOM Category'

#CREATE_UOM
CREATE_NEW_UOM_BTN = 'new'
CREATE_NEW_UOM_FLD = 'CREATE_NEW_UOM_FLD1'
CREATE_UOM_DESC_FLD = 'CREATE_UOM_DESC_FLD1'
CREATE_UOMCATEGORY_FLD = 'CREATE_UOMCATEGORY_FLD1'
CREATE_UOM_BASE_UOM_FLD = 'CREATE_UOM_BASE_UOM_FLD1'
CREATE_UOM_CONVERSION_FACTOR_FLD = 'CREATE_UOM_CONVERSION_FACTOR_FLD1'
CREATE_UOM_BTN = 'b1'
CREATE_UOM_SUCCESS_MESG = 'Unit Of Measurement'
CREATE_UOM_FAILURE_MESG = '* - Mandatory Fields'
CREATE_UOM_SUCCESS = 'CREATE_UOM_SUCCESS'
CREATE_UOM_FAILURE = 'CREATE_UOM_FAILURE'
CREATE_UOM_SCREEN = 'eGov Stores - UOM'

#VIEW UOM 
VIEW_UOM_FLD = 'VIEW_UOM_FLD1'
VIEW_UOM_BTN = 'view'
VIEW_UOM_SUCCESS_MESG = 'UOM'
VIEW_UOM_FAILURE_MESG = 'UOM'
VIEW_UOM_SCREEN = 'eGov Stores - UOM'

#MODIFY UOM
MODIFY_UOM_BTN = 'modify'
MODIFY_UOM_FLD = 'MODIFY_UOM_FLD1'
MODIFY_UOM_FIELD = 'MODIFY_UOM_FIELD1'
MODIFY_UOM_DESC_FLD = 'MODIFY_UOM_DESC_FLD1'
MODIFY_UOMCATEGORY_FLD = 'MODIFY_UOMCATEGORY_FLD1'
MODIFY_BASE_UOM_FLD = 'MODIFY_BASE_UOM_FLD1'
MODIFY_UOM_CONVERSION_FACTOR_FLD = 'MODIFY_UOM_CONVERSION_FACTOR_FLD1'
MODIFY_UOM_BUTTON = 'b1'
MODIFY_UOM_SUCCESS_MESG = 'Unit Of Measurement'
MODIFY_UOM_FAILURE_MESG = 'UOM'
MODIFY_UOM_SUCCESS = 'MODIFY_UOM_SUCCESS'
MODIFY_UOM_FAILURE = 'MODIFY_UOM_FAILURE'
MODIFY_UOM_SCREEN = 'eGov Stores - UOM'

#~ ************************************************************CREATE INDENT***********************************************************

INDENT_DATE = 'reqDate'
CREATE_MI_TYPE_FLD = 'CREATE_MI_TYPE_FLD1'
CREATE_MI_DEPARTMENT_FLD = 'CREATE_MI_DEPARTMENT_FLD1'
CREATE_MI_INDENTING_STORE_FLD = 'CREATE_MI_INDENTING_STORE_FLD1'
CREATE_MI_INDENT_PURPOSE_FLD = 'CREATE_MI_INDENT_PURPOSE_FLD1'
CREATE_MI_ISSUING_STORE_FLD = 'CREATE_MI_ISSUING_STORE_FLD1'
CREATE_MI_ACCOUNT_CODE_FLD = 'CREATE_MI_ACCOUNT_CODE_FLD1'
CREATE_MI_ACCOUNT_CODE_DESCRIPTION = 'accountCodeDesc'
CREATE_MI_SERIAL_NO = 'lineNo'
CREATE_MI_ITEM_FLD = 'CREATE_MI_ITEM_FLD'
CREATE_MI_ITEM_UOM = 'uomId'
CREATE_MI_QUANTITY_REQUIRED_FLD = 'CREATE_MI_QUANTITY_REQUIRED_FLD'
CREATE_MI_BTN = 'button'
ASSET ='assetImg'
SEARCH_ASSET_NAME = 'search'
INDENT_NUMBER_FLD  = 'indentnno'
CREATE_INDENT_NUMBER  = 'requisitionno'
CREATE_INDENT_NUMBER_FLD = 'reqNo'
FOR_LOOP_MATERIAL_INDENT = 'FOR_LOOP_MATERIAL_INDENT'
CREATE_MI_TABLE = 'MRDetails'
CREATE_INDENT_ADD_IMG = 'addRowImg'
ASSET_SEARCH_BUTTON = 'srcButton'
CREATE_INDENT_SUCCESS_MSG = 'eGov Stores - Inventory Material Indent'
CREATE_INDENT_FAILURE_MSG = 'eGov Stores - Inventory Material Indent'
CREATE_INDENT_NUMBER_LABEL = 'Indent Number *'
CREATE_INDENT_SCREEN = 'Material Information'
CREATE_MI_SEARCH_ITEM_FLD = 'CREATE_MI_SEARCH_ITEM_FLD1'
CREATE_MI_SEARCH_BTN = 'Search'
#new aded for capital
CREATE_MI_PROJECT_CODE_FLD='CREATE_MI_PROJECT_CODE_FLD1'
CREATE_MI_ASSET_CODE_FLD='CREATE_MI_ASSET_CODE_FLD1'
CREATE_MI_FUND_FLD = 'CREATE_MI_FUND_FLD1'
FINANCING_SOURCE = 'CREATE_MI_FINANCING_SOURCE_FLD1'
CREATE_MI_FINANCING_SOURCE_FLD = 'CREATE_MI_FINANCING_SOURCE_FLD1'
CREATE_MI_FUNCTION_FLD = 'CREATE_MI_FUNCTION_FLD1'
CREATE_MI_FUNCTIONARY_FLD = 'CREATE_MI_FUNCTIONARY_FLD1'
CREATE_INDENT_PRINT_BUTTON = 'Print'
STORES_CREATE_MI_DEGIGNATION_FLD='CREATE_MI_DESIGNATION_FLD'
STORES_CREATE_MI_APPROVER_FLD='CREATE_MI_APPROVER_FLD'
STORES_APPROVE_MI_APPROVER_FLD='STORES_APPROVE_MI_APPROVER_FLD'
STORES_APPROVE_MI_DESIGNATION_FLD='STORES_APPROVE_MI_DESIGNATION_FLD'



#~ **********************************************************VIEW INDENT******************************************************************
MENU_TREE_VIEW_MATERIAL_INDENT = 'View'
VIEW_MI_TYPE_FLD = 'VIEW_MI_TYPE_FLD1'
INDENT_SEARCH_BTN ='Search'
INDENT_NUMBER = 'iNumber'
VIEW_INDENT_SUCCESS_MSG = 'eGov Stores - Inventory Material Indent'
VIEW_INDENT_FAILURE_MSG = 'eGov Stores - Material Requisition Note'
VIEW_INDENT_SCREEN = 'eGov Stores - Material Requisition Note'


#~ ***********************************************************APPROVE INDENT****************************************************************

APPROVE_MATERIAL_INDENT_LINK_CLASS = 'rightpersonalboxplain'
APPROVE_MATERIAL_INDENT_DRAFTS_LINK = 'My Drafts'
APPROVE_MATERIAL_INDENT_WORKS_LINK = 'My Works'
MATERIAL_INDENT_APPROVE_BTN = 'Approve'
MATERIAL_INDENT_CLOSE_BTN = 'Close'
APPROVE_MATERIAL_INDENT_BOX = 'acceptAll'
APPROVE_MATERIAL_INDENT_SUBMIT_BTN = '3'
APPROVE_REJECT_INDENT_PRINT_BUTTON = 'Print'

#~ **********************************************************REJECT MATERIAL INDENT*******************************************************
REJECT_MATERIAL_INDENT_LINK_CLASS = 'rightpersonalboxplain'
REJECT_MATERIAL_INDENT_DRAFTS_LINK = 'My Drafts'
REJECT_MATERIAL_INDENT_WORKS_LINK = 'My Works'
MATERIAL_INDENT_REJECT_BTN = 'Reject'
REJECT_MATERIAL_INDENT_CHECK_BOX = 'rejectAll'
REJECT_MATERIAL_SUBMIT_BTN = '3'
APPROVE_MATERIAL_INDNET_SUCCESS_MSG = 'Approve Material Indent/Approve Material Indent'
APPROVE_MATERIAL_INDNET_SUCCESS_MSG1 = 'APPROVE MATERIAL INDENT/APPROVE MATERIAL INDENT'


#~ *********************************************************CREATE MMRN**********************************************************************
CREATE_MMRNDATE_FLD = 'receiptDate'
CREATE_MMRN_DATE_FLD = 'CREATE_MMRN_DATE_FLD'
CREATE_MMRN_DATE = 'mmrndate'
CREATE_MMRN_DEPARTMENT_FLD = 'CREATE_MMRN_DEPARTMENT_FLD1'
CREATE_MMRN_STORE_FLD = 'CREATE_MMRN_STORE_FLD1'
CREATE_MMRN_RECEIPT_TYPE_FLD = 'CREATE_MMRN_RECEIPT_TYPE_FLD1'
CREATE_MMRN_ACCOUNT_CODE_FLD = 'CREATE_MMRN_ACCOUNT_CODE_FLD1'
CREATE_MMRN_ITEM_NO_FLD = 'CREATE_MMRN_ITEM_NO_FLD1'
CREATE_MMRN_QUANTITY_FLD = 'CREATE_MMRN_QUANTITY_FLD1'
ITEM_UOM_FLD = 'uomId'
CREATE_MMRN_RATE_PER_UNIT_FLD = 'CREATE_MMRN_RATE_PER_UNIT_FLD1'
TOTAL_PRICE_FLD = 'enteredPrice'
OTHER_DETAILS_LINK = 'otherdetails'
CREATE_MMRN_BREAKUP_QUANTITY_FLD= 'CREATE_MMRN_BREAKUP_QUANTITY_FLD1'
CREATE_MMRN_LOT_NO_FLD = 'CREATE_MMRN_LOT_NO_FLD1'
CREATE_MMRN_EXPIRY_DATE_FLD = 'CREATE_MMRN_EXPIRY_DATE_FLD1'
CREATE_MMRN_BIN_NO_FLD  = 'CREATE_MMRN_BIN_NO_FLD1'
RECEIPT_NUMBER_FLD = 'mrn'
MMRN_NUMBER_FLD = 'mrn'
CREATE_MMRN_BTN = 'Save & Close'
ITEM_OTHER_DETAILS_BTN = 'button1'
CREATE_MMRN_SUCCESS_MESG = 'eGov Stores - Miscellaneous Material Receipt Note'
CREATE_MMRN_FAILURE_MESG = 'eGov Stores - Miscellaneous Material Receipt Note'
BREAK_UP_DETAILS = 'BREAK_UP_DETAILS1'
MMRN_SUPPLIER = 'MMRN_SUPPLIER'
BREAK_UP_LINK = 'Add'
CREATE_MMRN_NUMBER_LABEL = 'Receipt Number:*'
CREATE_MMRN_SCREEN = 'eGov Stores - Miscellaneous Material Receipt Note'



#~ ************************************************** VIEW_MMRN **************************************************************

SEARCH_MMRN_BTN = 'button'
MMRN_NUMBER_LINK = 'mmrnNumber'
#~ MMRN_NUMBER_FLD = 'mrn'
#~ RECEIPT_NUMBER_FLD = 'mrn'
VIEW_MMRN_SEARCH_SCREEN = 'eGov Stores - Search Miscellaneous Materials Receipt Note'
VIEW_MMRN_SUCCESS_MSG = 'eGov Stores - Miscellaneous Material Receipt Note'
VIEW_MMRN_FAILURE_MSG = 'eGov Stores - Search Miscellaneous Material Receipt Note'

#~ ********************************************************* APPROVE MMRN **********************************************************
APPROVE_MMRN_LINK_CLASS = 'rightpersonalboxplain'
APPROVE_MMRN_DRAFTS_LINK = 'My Drafts'
APPROVE_MMRN_WORKS_LINK = 'My Works'
MMRN_APPROVE_BUTTON = 'Approve'
MMRN_CLOSE_BUTTON = 'Close'
#~ APPROVE_MMRN_LINK = 'Approve MMRN/Approve MMRN'
APPROVE_MMRN_CHECK_BOX = 'acceptAll'
APPROVE_MMRN_SUBMIT_BTN = '3'

#~  ******************************************************* REJECT MMRN ***************************************************************

REJECT_MMRN_LINK_CLASS = 'rightpersonalboxplain'
REJECT_MMRN_DRAFTS_LINK = 'My Drafts'
REJECT_MMRN_WORKS_LINK = 'My Works'
REJECT_APPROVE_BUTTON = 'Reject'
REJECT_MMRN_LINK = 'Approve MMRN/Approve MMRN'
REJECT_MMRN_CHECK_BOX = 'rejectAll'
REJECT_MMRN_SUBMIT_BTN = '3'

#~ *********************************************************** CREATE GMI **************************************************************

CREATE_GMI_DEPT_FLD = 'CREATE_GMI_DEPT_FLD1'
CREATE_GMI_ISSUING_STORE_FLD = 'CREATE_GMI_ISSUING_STORE_FLD1'
CREATE_GMI_PURPOSE_FLD = 'CREATE_GMI_PURPOSE_FLD1'
CREATE_GMI_ACCOUNTCODE_FLD = 'CREATE_GMI_ACCOUNTCODE_FLD1'
CREATE_GMI_INDENTING_STORE_FLD = 'CREATE_GMI_INDENTING_STORE1'
CREATE_GMI_ITEM_FLD = 'CREATE_GMI_ITEM_FLD1'
CREATE_GMI_QUANTIY_FLD = 'CREATE_GMI_QUANTIY_FLD1'
CREATE_GMI_SALE_PRICE_FLD = 'CREATE_GMI_SALE_PRICE_FLD'
CREATE_GMI_SCRAP_ITEM_FLD = 'CREATE_GMI_SCRAP_ITEM_FLD'
CREATE_GMI_BTN = 'Save & Close'
CREATE_GMI_SUCCESS_MESG = 'eGov Stores - General Material Issue'
GMI_NUMBER_FLD = 'gmi'
CREATE_GMI_RECEIPT_NUMBER_FLD = 'mrinNo'
CREATE_GMI_CONTRACTOR_IMG = 'contractorImg'
CREATE_GMI_PROJECT_CODE_FLD = 'CREATE_GMI_PROJECT_CODE_FLD'
CREATE_GMI_ASSET_CODE_FLD = 'CREATE_GMI_ASSET_CODE_FLD'
CREATE_GMI_SCREEN = 'Material Information '
CREATE_GMI_NUMBER_LABEL = 'Issue Number'

#~ ************************************************* VIEW GMI ***************************************************************************

VIEW_GMI_BTN = 'Search'
VIEW_GMI_SUCCESS_MESG = 'eGov Stores - General Material Issue'
VIEW_GMI_FAILURE_MESG = 'eGov Stores - General Material Issue'
VIEW_GMI_SCREEN = 'eGov Stores - General Material Issue'
VIEW_GMI_SEARCH_SCREEN = 'eGov Stores - Material Requisition Issue Note - Search'

#~ ***************************************************** APPROVE GMI ************************************************************************
GMI_CLOSE_BUTTON = 'Close'
GMI_APPROVE_BUTTON = 'Approve'

TRANSCATION_LINK_CLASS = 'rightpersonalboxplain'
TRANSCATION_DRAFTS_LINK = 'My Drafts'
TRANSCATION_WORKS_LINK = 'My Works'

APPROVE_GMI_SUCCESS_MESG = 'Welcome egovernments'

#~ ********************************************************* REJECT GMI **************************************************************

GMI_REJECT_BUTTON = 'Reject'

#~ ************************************************** CREATE MATERIAL INDENT ISSUE *************************************************

SEARCH_CREATE_MII_BTN = "Search"
CREATE_MII_TABLE = 'MrinDetails'
CREATE_MII_DATE_FLD = 'CREATE_MII_DATE_FLD'
CREATE_MII_ISSUED_QUANTITY_FLD = 'CREATE_MII_ISSUED_QUANTITY_FLD'
CREATE_MII_NUMBER_FLD ='mrinNo'
CREATE_MII_RECEIPT_NUMBER_FLD = 'mrinNo'
CREATE_MII_BTN = 'save'
CREATE_MII_SUCCESS_MESG = 'Material Information'
CREATE_MII_SEARCH = 'eGov Stores - Material Indent Issue Search Screen'
CREATE_MII_LABEL = 'Issue Number'
MATERIAL_INDENT_ISSUE_LOOP = 'MATERIAL_INDENT_ISSUE_LOOP'
CREATE_MII_PRINT_BUTTON = 'Print'

#~  ************************************************** VIEW MATERIAL INDEENT ISSUE *************************************************

VIEW_MII_BTN = 'Search'
VIEW_MII_SUCCESS_MESG = 'Material Information'
VIEW_MII_FAILURE_MESG = 'eGov Stores - Material Requisition Issue Note - Search'
VIEW_MII_SEARCH = 'eGov Stores - Material Requisition Issue Note - Search'


#~  ******************************************************** APPROVE MATERIAL INDENT ISSUE *********************************************

MII_APPROVE_BUTTON = 'Approve'
MII_CLOSE_BUTTON = 'Close'


#~  ******************************************************** REJECT MATERIAL INDENT ISSUE ************************************************

MII_REJECT_BUTTON = 'Reject'
#~ MII_CLOSE_BUTTON = 'Close'

#~  ***************************************************************** SUPPLIER BILL ***********************************************************

CREATE_SUPPLIER_BILL_SEARCH_BUTTON = 'b1'
CREATE_SUPPLIER_BILL_CHECKBOX = 'postTxn'
CREATE_SUPPLIER_BILL_SEARCH_BUTTON1 = 'Submit'


#~  ************************************************************ CREATE INTERBIN TRANSFER **************************************************

CREATE_INTERBIN_TRANSFER_ITEM_FLD = 'CREATE_INTERBIN_TRANSFER_ITEM_FLD1'
CREATE_INTERBIN_TRANSFER_STORE_FLD = 'CREATE_INTERBIN_TRANSFER_STORE_FLD1'
CREATE_INTERBIN_TRANSFER_TOBIN_FLD = 'CREATE_INTERBIN_TRANSFER_TOBIN_FLD1'
CREATE_INTERBIN_TRANSFER_QUANTITY_FLD = 'CREATE_INTERBIN_TRANSFER_QUANTITY_FLD1'
#~ CREATE_INTERBIN_TRANSFER_FROM_FLD = 'CREATE_INTERBIN_TRANSFER_FROM_FLD1'
CREATE_INTERBIN_TRANSFER_FROM_FLD = '1'
CREATE_INTERBIN_TRANSFER_NUMBER_FLD = 'interBinId'
INTERBIN_TRANSFER_NUMBER_FLD = 'interBinId'
CREATE_INTERBIN_TRANFER_MESG_SCREEN = 'eGov Stores - Inter Bin Transfer'
INTER_BIN_TRANSFER_ID='Inter Bin Transfer Id'
#~ VIEW_TRANSFER_SEARCH_MATERIAL_BTN='buttonsubmit'
CREATE_INTERBIN_TRANSFER_LINK='Create Inter Bin Transfer'

#~  ***************************************************** VIEW INTERBIN TRANSFER ************************************************************

VIEW_INTERBIN_TRANSFER_SEARCH_BUTTON = 'button'
CREATE_INTERBIN_TRANFER_SUCCESS_MESG='eGov Stores - Inter Bin Transfer'
CREATE_INTERBIN_TRANSFER_SAVE_BUTTON='Save'


#~  *********************************************************** CREATE MTN ***********************************************************************
#~ STORES_CREATE_MTN_SEARCH_PAGE = 'Search MRINS To Create Material Transfer Note'
STORES_MTN_TABLE = 'MRNLTable'
STORES_CREATE_MTN_SEARCH_PAGE = 'eGov Stores - Search MRINS To Create Material Transfer Note'
STORES_CREATE_MTN_SEARCH_BTN = 'Find'
STORES_CREATE_MTN_SCREEN = 'Material Information '
STORES_CREATE_MTN_BTN = 'Save & Close'
STORES_CREATE_MTN_RECEIVED_QTY_FLD = 'STORES_CREATE_MTN_RECEIVED_QTY_FLD'       
STORES_CREATE_MTN_ACCEPTED_QTY_FLD = 'STORES_CREATE_MTN_ACCEPTED_QTY_FLD'       
STORES_CREATE_MTN_OTHER_CHARGES_FLD = 'STORES_CREATE_MTN_OTHER_CHARGES_FLD'
STORES_CREATE_MTN_BREAK_UP_DETAILS_LINK = 'STORES_CREATE_MTN_BREAK_UP_DETAILS_LINK'
STORES_CREATE_MTN_BREAKUP_QTY_FLD = 'STORES_CREATE_MTN_BREAKUP_QTY_FLD'
STORES_CREATE_MTN_BREAKUP_LOT_FLD = 'STORES_CREATE_MTN_BREAKUP_LOT_FLD'
STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD = 'STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD'
STORES_CREATE_MTN_BIN_NO_FLD = 'STORES_CREATE_MTN_BIN_NO_FLD'
STORES_CREATE_MTN_ITEM_OTHER_DETAILS_BTN = 'Submit'
STORES_CREATE_MTN_RECEIPT_NUMBER_FLD = 'mrn'
STORES_CREATE_IMRN_LABEL = 'Receipt Number:*'
STORES_CREATE_MTN_NUMBER = 'imrn'
CREATE_MRN_BTN = 'submit'
STORES_CREATE_MTN_FOR_LOOP = 'STORES_CREATE_MTN_FOR_LOOP'


# ******************************************************   APPROVE  & REJECT IMRN ********************************************************************

STORES_IMRN_APPROVE_BUTTON = 'Approve'
STORES_IMRN_CLOSE_BUTTON = 'button'
STORES_IMRN_REJECT_BUTTON = 'Reject'

#~ ----------------------------- INTERBIN TRANSFER DETAILS----------------------------
#~ CREATE_INTERBIN_TRANSFER_URL='http://192.168.1.44:9080/stores/inventory/interBinTransfer.do?submitType=loadInterBinTransferForm&actionid=142'
CREATE_ITEM_FLD='itemNo'
CREATE_DEPT_FLD='departmentId'
CREATE_STORE_FLD='storeId'
CREATE_BIN_TRANSFER_RADIO_BTN_1='1'
CREATE_BIN_TRANSFER_RADIO_BTN_2='2'
CREATE_BIN_TRANSFER_RADIO_BTN_3='3'
CREATE_TRANSFER_TO_BIN_FLD='toBinId'
CREATE_QUANTITY_TRANSFER_FLD='qty'
CREATE_BIN_TRANSFER_SAVE_BTN='buttonsubmit'
CREATE_INTER_BIN_TRANSFER_FLD='interBinId'



#~ ----------------------------- INTERBIN TRANSFER VIEW DETAILS----------------------------

#~ VIEW_INTERBIN_TRANSFER_LINK='http://192.168.1.44:9080/stores/inventory/interBinTransfer.do?submitType=loadSearchInterBinTransfers&actionid=143'
VIEW_TRANSFER_SEARCH_MATERIAL_BTN='buttonsubmit'
VIEW_INTERBIN_TRANSFER_LINK='View Inter Bin Transfer'

#~ VIEW_GET_DETAILS='http://192.168.1.44:12180/stores/inventory/interBinTransfer.do?submitType=searchInterBinTransfers#'



#~ ********************************************************      CREATE PURCHASE ORDER  INDENT AND NON INDENT TYPE    *******************************************************************


CREATE_PO_SEARCH_SCREEN = 'eGov Stores - Purchase Order - Search'
CREATE_PO_SEARCH_BTN = '1'
CREATE_PO_INDENT_CHECK_BOX = 'postTxn'
CREATE_PO_SUBMIT_BTN = '3'
CREATE_PO_INDENT_TABLE= 'searchDetails'
CREATE_PO_SCREEN_INDENT = 'eGov Stores - Purchase Order'
CREATE_PO_NUMBER_FLD = 'pono'
CREATE_PO_NUM_FLD = 'po'
CREATE_PO_NUMBER_LABEL = 'Purchase Order No.*'
CREATE_PO_SAVE_BUTTON = 'buttonsubmit'
CREATE_PO_SUPPLIER_IMG = 'suppImg'
CREATE_PO_ORDER_DATE_FLD = 'CREATE_PO_ORDER_DATE_FLD'
CREATE_PO_DELIVERY_DATE_FLD = 'CREATE_PO_DELIVERY_DATE_FLD'
CREATE_PO_MATERIAL_DETAILS_TABLE = 'PoDetails'
CREATE_PO_ADD_MATERIAL_BTN = 'addRowImg'
CREATE_PO_UNIT_PRICE_FLD = 'CREATE_PO_UNIT_PRICE_FLD'
CREATE_PO_NON_INDENT_SCREEN = 'eGov Stores - Purchase Order General'
PO_NON_INDENT = 'PO_NON_INDENT'
PO_INDENT = 'PO_INDENT'
CREATE_PO_NON_INDENT_STORE_FLD = 'CREATE_PO_NON_INDENT_STORE_FLD'
CREATE_PO_NON_INDENT_ITEM_FLD = 'CREATE_PO_NON_INDENT_ITEM_FLD'
CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD = 'CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD'
FOR_LOOP_PO_MATERIAL_DETAILS = 'FOR_LOOP_PO_MATERIAL_DETAILS'
PO_NON_INDENT_MATERIAL_DETAILS = 'PO_NON_INDENT_MATERIAL_DETAILS'
PO_INDENT_MATERIAL_DETAILS = 'PO_INDENT_MATERIAL_DETAILS'
FOR_LOOP_PO_MATERIAL_UNIT_PRICE = 'FOR_LOOP_PO_MATERIAL_UNIT_PRICE'
CREATE_PO_SUPPLIER_NAME_FLD='CREATE_PO_SUPPLIER_NAME_FLD'
STORES_SEARCH_SUPP_BUTTON='srcButton'
STORES_MODIFY_PO_TAX_PRICE_FLD='STORES_MODIFY_PO_TAX_PRICE_FLD'
STORES_CREATE_PO_TAX_PRICE_FLD='STORES_CREATE_PO_TAX_PRICE_FLD'
FOR_LOOP_PO_MATERIAL_TAX_PRICE_FLD='FOR_LOOP_PO_MATERIAL_TAX_PRICE_FLD'
#~ ********************************************************    VIEW PURCHASE ORDER     ******************************************************

STORES_VIEW_PO_SEARCH_SCREEN = 'eGov Stores - Purchase Order - Search'
STORES_VIEW_PO_SEARCH_BUTTON = 'Submit'
STORES_VIEW_PO_SUCCESS_MESG = 'Material Details'


#~ ************************************************  APPEOVE & REJECT PURCHASE ORDER  ********************************

STORES_PO_REVISED_NOTES_FLD = 'STORES_PO_REVISED_NOTES_FLD'
STORES_PO_APPROVE_BUTTON = 'Approve'
STORES_PO_CLOSE_BUTTON = 'Close'
STORES_PO_APPROVE_PRINT_BUTTON = 'Print'
STORES_PO_REJECT_BUTTON = 'Reject'
APPROVE_PO_NON_INDENT = 'APPROVE_PO_NON_INDENT'
APPROVE_PO_INDENT = 'APPROVE_PO_INDENT'
APPROVE_PO = 'APPROVE_PO'
REJECT_PO ='REJECT_PO'

#~ **********************************************************   MODIFY PURCHASE ORDER     **************************************************************************************

MODIFY_PO_NON_INDENT_STORE_FLD = 'MODIFY_PO_NON_INDENT_STORE_FLD'
MODIFY_PO_ORDER_DATE_FLD = 'MODIFY_PO_ORDER_DATE_FLD'
MODIFY_PO_DELIVERY_DATE_FLD = 'MODIFY_PO_DELIVERY_DATE_FLD'
MODIFY_PO_INDENT_MATERIAL_DETAILS = 'MODIFY_PO_INDENT_MATERIAL_DETAILS'
MODIFY_FOR_LOOP_PO_MATERIAL_UNIT_PRICE = 'MODIFY_FOR_LOOP_PO_MATERIAL_UNIT_PRICE'
MODIFY_PO_UNIT_PRICE_FLD = 'MODIFY_PO_UNIT_PRICE_FLD'
MODIFY_PO_NON_INDENT_MATERIAL_DETAILS = 'MODIFY_PO_NON_INDENT_MATERIAL_DETAILS'
MODIFY_FOR_LOOP_PO_MATERIAL_DETAILS = 'MODIFY_FOR_LOOP_PO_MATERIAL_DETAILS'
MODIFY_PO_NON_INDENT_ITEM_FLD = 'MODIFY_PO_NON_INDENT_ITEM_FLD'
MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD = 'MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD'
MODIFY_PO_SUPPLIER_IMG = 'suppImg'
CREATE_PO_DELETE_MATERIAL_BTN = 'removeRowImg'
MODIFY_PO_DELETE_LINE_ITEMS = 'MODIFY_PO_DELETE_LINE_ITEMS'
MODIFY_FOR_LOOP_PO_DELETE_LINE_ITEMS = 'MODIFY_FOR_LOOP_PO_DELETE_LINE_ITEMS'



#~ *******************************************************   CREATE SUPPLIER BILL  *******************************************************

STORES_CREATE_SUPPLIER_BILL_SEARCH_SCREEN = 'eGov Stores - Supplier Bill'
STORES_CREATE_SUPPLIER_BILL_SEARCH_BTN = 'Search'
STORES_CREATE_SUPPLIER_BILL_SUBMIT_BTN= 'Submit'
STORES_CREATE_SUPPLIER_BILL_MRN_TABLE = 'mrnlistTab'
STORES_CREATE_SUPPLIER_BILL_MRN_CHECK_BOX = 'postTxn'
STORES_CREATE_SUPPLIER_BILL_NUMBER_FLD  = 'billNumber'
STORES_CREATE_SUPPLIER_BILL_NUM_FLD = 'SupplierBill'
STORES_CREATE_SUPPLIER_BILL_SCREEN  = 'eGov Stores - Supplier Bill'
STORES_CREATE_SUPPLIER_BILL_DATE_FLD = 'STORES_CREATE_SUPPLIER_BILL_DATE_FLD'
STORES_CREATE_SUPPLLIER_BILL_ACCOUNT_DETAILS_TAB = 'Account Details'
STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD = 'STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD'
STORES_CREATE_SUPPLLIER_BILL_SUPPLIER_BILL_TAB = 'Supplier Bill'
STORES_CREATE_SUPPLIER_BILL_SAVE_AND_CLOSE_BTN = 'Save & Close'
#~ STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON = 'Print'
STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD = 'STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD'
STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD = 'STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD'
STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE = 'accountTable'
STORES_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN = 'addRowImg'
STORES_SUPPLIER_BILL_DELETE_ACC_DETAILS_BTN = 'remvRow'
STORES_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS = 'STORES_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS'
STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE = 'deductionTable'
STORES_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS = 'STORES_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS'
STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD = 'STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD'
STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD = 'STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD'
STORES_SUPPLIER_BILL_ADD_DED_DETAILS_BTN ='addRowImg1'
STORES_SUPPLIER_BILL_DELETE_DED_DETAILS_BTN = 'remvRow1'
STORES_SUPPLIER_BILL_ACCOUNT_DETAILS = 'STORES_SUPPLIER_BILL_ACCOUNT_DETAILS'
STORES_SUPPLIER_BILL_DEDUCTION_DETAILS = 'STORES_SUPPLIER_BILL_DEDUCTION_DETAILS'
SUPPLIER_BILL_DATE_MMRN_DATE = 'SUPPLIER_BILL_DATE_MMRN_DATE'
SUPPLIER_BILL_DATE ='SUPPLIER_BILL_DATE'



#~ ***************************************************************  APPROVE & REJECT SUPPLIER BILL ********************************************

STORES_SUPPLIER_BILL_APPROVE_BUTTON = 'Approve'
STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON = 'Print'
STORES_SUPPLIER_BILL_REJECT_BUTTON = 'Reject'
STORES_SUPPLIER_BILL_CLOSE_BUTTON = 'Close'


#~ ***********************************************   VIEW SUPPLIER BILL  *************************************

STORES_VIEW_SUPPLIER_BILL_SEARCH_SCREEN = 'eGov Stores - Supplier Bill'
STORES_VIEW_SUPPLIER_BILL_SEARCH_BUTTON ='Search'
STORES_VIEW_SUPPLIER_BILL_CLOSE_BTN = 'Close'




#~ ******************************************** MODIFY SUPPLIER BILL  **********************************************

STORES_MODIFY_SUPPLIER_BILL_SCREEN = ''
MODIFY_SUPPLIER_BILL_DATE_MMRN_DATE = 'MODIFY_SUPPLIER_BILL_DATE_MMRN_DATE'
MODIFY_SUPPLIER_BILL_DATE = 'MODIFY_SUPPLIER_BILL_DATE'
STORES_MODIFY_SUPPLIER_BILL_DATE_FLD = 'STORES_MODIFY_SUPPLIER_BILL_DATE_FLD'
STORES_MODIFY_SUPPLIER_BILL_ACCOUNT_DETAILS = 'STORES_MODIFY_SUPPLIER_BILL_ACCOUNT_DETAILS'
STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS = 'STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS'
STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD = 'STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD'
STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD = 'STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD'
STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_DETAILS = 'STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_DETAILS'
STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS = 'STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS'
STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD = 'STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD'
STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD = 'STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD'
STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD = 'STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD'
STORES_MODIFY_SUPPLIER_BILL_DELETE_ACC_DETAILS_LINES = 'STORES_MODIFY_SUPPLIER_BILL_DELETE_ACC_DETAILS_LINES'
STORES_MODIFY_SUPPLIER_BILL_FOR_LOOP_DELETE_ACC_DETAILS = 'STORES_MODIFY_SUPPLIER_BILL_FOR_LOOP_DELETE_ACC_DETAILS'
STORES_MODIFY_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN = 'addRowImg'
STORES_MODIFY_SUPPLIER_BILL_DELETE_ACC_LINE_BTN = 'remvRow'
STORE_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETIALS = 'STORE_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETIALS'
MODIFY_FOR_LOOP_PO_DELETE_DEDUCTION_DETAILS= 'MODIFY_FOR_LOOP_PO_DELETE_DEDUCTION_DETAILS'
STORES_MODIFY_SUPPLIER_BILL_ADD_DEDUCTION_DETAILS_BTN = 'addRowImg1'
STORES_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETAILS_BTN = 'removeRowImg1'


#~ ******************************************** MODIFY INDENT ISSUE  **********************************************
STORES_MODIFY_MII_ISSUED_QUANTITY_FLD='MODIFY_MII_ISSUED_QUANTITY_FLD'
STORES_MODIFY_FOR_LOOP_MATERIAL_INDENT='MODIFY_MATERIAL_INDENT_ISSUE_LOOP'
STORES_MODIFY_MII_DATE_FLD='STORES_MODIFY_MII_DATE_FLD'
#~ STORES_MODIFY_MII_ISSUED_QUANTITY_FLD='MODIFY_MII_ISSUED_QUANTITY_FLD'


#~ ******************************************** STORES LEDGER  **********************************************
STORES_VIEW_LEDGER_BUTTON='buttonsubmit'
STORES_LEDGER_ITEM_FLD='STORES_LEDGER_ITEM_FLD'
STORES_LEDGER_STORE_FLD='STORES_LEDGER_STORE_FLD'
STORES_LEDGER_DATE_FLD='STORES_LEDGER_DATE_FLD'
STORES_LEDGER_BIN_FLD='STORES_LEDGER_BIN_FLD'

end#MODULE END


















