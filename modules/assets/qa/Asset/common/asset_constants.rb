module Asset_constants

INPUT_OO = path_to_file("asset.ods")


INTERNAL_URL=ENV['APP_URL']

ASSET_URL = INTERNAL_URL + '/egi'


#openoffice sheet

    ASSET_CREATE_ASSET_SHEET='create_asset_master'

#user logins & passworrds

    ASSET_USER_NAME='egovernments'
    ASSET_PASSWORD='egovfinancials'
    ASSET_LOGIN_USERNAME_FLD='j_username'
    ASSET_LOGIN_PASSWORD_FLD='j_password'
    ASSET_LOGIN_BUTTON='Login'
    ASSET_LINK='Asset Management'
    
    ASSET_LOGIN_SUCCESS_MSG='Sign out'
    
    ASSET_SIGN_OUT_LINK='Sign out'
    
    
#create asset master

    ASSET_MASTER_LINK='Asset Master'
    ASSET_MASTER_LINK_FOUND_SUCCESS_MSG='Modify Asset'
    
    ASSET_CREATE_ASSET_LINK='Create Asset'
    ASSET_CREATE_ASSET_SUCCESS_MSG='Create Asset'
    
    ASSET_CODE_FLD='code'
    ASSET_NAME_FLD='name'
    ASSET_CATEGORY_TYPE_FLD='catTypeIdDummy'
    ASSET_CATEGORY_FLD='assetCategory'
    ASSET_DESCRIPTION_FLD='description'
    ASSET_AREA_FLD='area'
    ASSET_LOCATION_FLD='location'
    ASSET_STREET_FLD='street'
    ASSET_DETAILS_FLD='assetDetails'
    ASSET_MODE_OF_ACQUISITION_FLD='modeOfAcquisition'
    ASSET_DATE_OF_COMMISSIONING_FLD='commdate'
    ASSET_STATUS_FLD='status'
    ASSET_GROSS_VALUE_FLD='grossvalue'
    ASSET_ACCUMULATIVE_DEPRECIATION_FLD='accDepreciation'
    ASSET_WRITTEN_DOWN_VALUE_FLD='writtenDownValue'
    ASSET_CREATION_SUCCESS_MSG='saved Successfully!'
    ASSET_CHECK_DUPLICATE_ASSET_SUCCESS_MSG='Asset Code already exist' 

    
    ASSET_SAVE_BUTTON='saveButton'
    







end#module end


