
require path_to_file("stores_constants.rb")

module Stores_supplier_methods

include Stores_constants
include Test::Unit::Assertions
  


def create_supplier(tc, test_data, attributes)

  #~ ie_create_supplier = start_browser(CREATE_SUPPLIER_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, CREATE_SUPPLIER_LINK).click
  ie_create_supplier=Watir::IE.attach(:url,/CreateSupplier*/)
  
  assert_not_nil(ie_create_supplier.contains_text(CREATE_SUPPLIER_SCREEN),("TC" + tc + ".Could not find the text: " + CREATE_SUPPLIER_SCREEN + " - Create Supplier screen"))
  populate_create_supplier_details(tc, test_data, attributes, ie_create_supplier)
  ie_create_supplier.button(:name,CREATE_SUPPLIER_BTN).click
  ensure
  ie_create_supplier.close

end


def populate_create_supplier_details(tc, test_data, attributes, ie_create_supplier)
  
  if test_data[CREATE_SUPPLIER_CODE_FLD] && attributes[CREATE_SUPPLIER_CODE_FLD] !=nil
    ie_create_supplier.text_field(:name,attributes[CREATE_SUPPLIER_CODE_FLD]).set test_data[CREATE_SUPPLIER_CODE_FLD]
  end

  if test_data[CREATE_SUPPLIER_NAME_FLD] && attributes[CREATE_SUPPLIER_NAME_FLD]!=nil
    ie_create_supplier.text_field(:name,attributes[CREATE_SUPPLIER_NAME_FLD]).set test_data[CREATE_SUPPLIER_NAME_FLD]
  end

  if test_data[CREATE_SUPPLIER_CORS_ADDRESS_FLD] && attributes[CREATE_SUPPLIER_CORS_ADDRESS_FLD]!=nil
    ie_create_supplier.text_field(:name,attributes[CREATE_SUPPLIER_CORS_ADDRESS_FLD]).set test_data[CREATE_SUPPLIER_CORS_ADDRESS_FLD]
  end

  if test_data[CREATE_SUPPLIER_STATUS_FLD] && attributes[CREATE_SUPPLIER_STATUS_FLD]!=nil
    assert(ie_create_supplier.select_list(:id,attributes[CREATE_SUPPLIER_STATUS_FLD]).exists?)
    ie_create_supplier.select_list(:id,attributes[CREATE_SUPPLIER_STATUS_FLD]).set test_data[CREATE_SUPPLIER_STATUS_FLD]
  end

  if test_data[CREATE_SUPPLIER_PAN_NO_FLD] && attributes[CREATE_SUPPLIER_PAN_NO_FLD]!=nil
    ie_create_supplier.text_field(:name,attributes[CREATE_SUPPLIER_PAN_NO_FLD]).set test_data[CREATE_SUPPLIER_PAN_NO_FLD]
  end

end

 
def view_supplier_success(tc, test_data, attributes)

  #~ ie_view_supplier = start_browser(VIEW_SUPPLIER_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_SUPPLIER_LINK).click
  ie_view_supplier=Watir::IE.attach(:url,/LoadView*/)
  
  assert_not_nil(ie_view_supplier.contains_text(VIEW_SUPPLIER_SCREEN),("Tc" + tc + ".Could not find the text: " + VIEW_SUPPLIER_SCREEN + " - View Supplier screen" ))
  
  #######################################Autocomete code to select the supplier########################
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).value = test_data[CREATE_SUPPLIER_NAME_FLD]
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyPress')
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyUp')
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onblur')
  #~ ie_view_supplier.send_keys("{tab}")
  
  Watir::Waiter.wait_until{ ie_view_supplier.li(:text,/#{test_data[VIEW_SUPPLIER_NAME_FLD]}/).exists?}
  ie_view_supplier.li(:text,/#{test_data[VIEW_SUPPLIER_NAME_FLD]}/).click
  #~ ie_view_supplier.send_keys("{tab}")
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onblur')

######################################################################################

  ie_view_supplier.button(:id, VIEW_SUPPLIER_SEARCH_BTN).click
  Watir::Waiter.wait_until{ie_view_supplier.contains_text(/#{VIEW_SUPPLIER_SUCCESS_MESG}/)}
  assert_not_nil(ie_view_supplier.contains_text(/#{VIEW_SUPPLIER_SUCCESS_MESG}/), ("Tc" + tc + ".Could not find the text: " + VIEW_SUPPLIER_SUCCESS_MESG + " - Not in View Supplier screen" ))
  
if test_data[CREATE_SUPPLEIR_SUCCESS]=='1'
  supplier_code = ie_view_supplier.text_field(:name,attributes[CREATE_SUPPLIER_CODE_FLD]).value
  assert_equal(test_data[CREATE_SUPPLIER_CODE_FLD], supplier_code)
  #~ assert(ie_view_supplier.text_field(:name,attributes[CREATE_SUPPLIER_CODE_FLD]).verify_contains(test_data[CREATE_SUPPLIER_CODE_FLD]) )
end

if test_data[MODIFY_SUPPLIER_SUCCESS]=='1'
  supplier_name = ie_view_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_NAME_FLD]).value
  assert_equal(test_data[MODIFY_SUPPLIER_NAME_FLD], supplier_name)
  #~ assert(ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_NAME_FLD]).verify_contains(test_data[MODIFY_SUPPLIER_NAME_FLD]) )
end

  ensure
  ie_view_supplier.close

end #End of view supplier success method

def view_supplier_failure(tc, test_data, attributes)

  #~ ie_view_supplier = start_browser(VIEW_SUPPLIER_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_SUPPLIER_LINK).click
  ie_view_supplier=Watir::IE.attach(:url,/LoadView*/)
  
  assert_not_nil(ie_view_supplier.contains_text(VIEW_SUPPLIER_SCREEN),("Tc" + tc + ".Could not find the text: " + VIEW_SUPPLIER_SCREEN + " - View Supplier screen" ))
  
  #######################################Autocomete code to select the supplier########################
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).value = test_data[CREATE_SUPPLIER_NAME_FLD]
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyPress')
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyUp')
  
  Watir::Waiter.wait_until{ ie_view_supplier.li(:text,/#{test_data[VIEW_SUPPLIER_NAME_FLD]}/).exists?}
  ie_view_supplier.li(:text,/#{test_data[VIEW_SUPPLIER_NAME_FLD]}/).click
  #~ ie_view_supplier.send_keys("{tab}")
  ie_view_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onblur')
######################################################################################

  ie_view_supplier.button(:id, VIEW_SUPPLIER_SEARCH_BTN).click

if test_data[CREATE_SUPPLIER_FAILURE]=='1'
  assert_not_nil(ie_view_supplier.contains_text(VIEW_SUPPLIER_FAILURE_MESG),("Tc" + tc + ".Could not find the text: " + VIEW_SUPPLIER_FAILURE_MESG + " - View Supplier Failed" ))
end

if test_data[MODIFY_SUPPLIER_FAILURE]=='1'
  assert_false(ie_view_supplier.text_field(:name,attributes[MODFIY_SUPPLIER_CODE_FLD]).verify_contains(test_data[MODFIY_SUPPLIER_CODE_FLD]) )
end

  ensure
  ie_view_supplier.close

end 
 



def modify_supplier(tc, test_data, attributes)

  #~ ie_modify_supplier = start_browser(MODIFY_SUPPLIER_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_SUPPLIER_LINK).click
  ie_modify_supplier=Watir::IE.attach(:url,/toLoad*/)
  
  assert_not_nil(ie_modify_supplier.contains_text(MODIFY_SUPPLIER_SCREEN),("Tc" + tc + ".Could not find the text: " + MODIFY_SUPPLIER_SCREEN + " - Modify Supplier screen" ))
  populate_modify_supplier_details(tc, test_data, attributes, ie_modify_supplier)
  ie_modify_supplier.button(:class , MODIFY_SUPPLIER_BTN).click

ensure
  ie_modify_supplier.close

end


def populate_modify_supplier_details(tc, test_data, attributes, ie_modify_supplier)
  
  
######################Autocompete code to select the supplier##############################################################################
  ie_modify_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).value = test_data[CREATE_SUPPLIER_NAME_FLD]
  ie_modify_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyPress')
  ie_modify_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onKeyUp')

  Watir::Waiter.wait_until{ ie_modify_supplier.li(:text,/#{test_data[MODIFY_SUPPLIER_SEARCH_FLD]}/).exists?}
  ie_modify_supplier.li(:text,/#{test_data[MODIFY_SUPPLIER_SEARCH_FLD]}/).click
  #~ ie_modify_supplier.send_keys("{tab}")
  ie_modify_supplier.text_field(:id, attributes[CREATE_SUPPLIER_NAME_FLD]).fire_event('onblur')
###############################################################################################################################

  ie_modify_supplier.button(:id, MODIFY_SUPPLIER_SEARCH_BTN).click

  Watir::Waiter.wait_until{ie_modify_supplier.contains_text(/#{MODIFY_SUPPLIER_SUCCEESS_MESG}/)}
  assert_not_nil(ie_modify_supplier.contains_text(/#{MODIFY_SUPPLIER_SUCCEESS_MESG}/), ("Tc" + tc + ".Could not find the text: " + MODIFY_SUPPLIER_SUCCEESS_MESG + " - Modify Supplier screen" ))

if test_data[MODFIY_SUPPLIER_CODE_FLD] && attributes[MODFIY_SUPPLIER_CODE_FLD]!=nil
  ie_modify_supplier.text_field(:name,attributes[MODFIY_SUPPLIER_CODE_FLD]).set test_data[MODFIY_SUPPLIER_CODE_FLD]
end


if test_data[MODIFY_SUPPLIER_NAME_FLD] && attributes[MODIFY_SUPPLIER_NAME_FLD]!=nil
  ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_NAME_FLD]).set test_data[MODIFY_SUPPLIER_NAME_FLD]
end

if test_data[MODIFY_SUPPLIER_CORS_ADDRESS_FLD] && attributes[MODIFY_SUPPLIER_CORS_ADDRESS_FLD]!=nil
  Watir::Waiter.wait_until{ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_CORS_ADDRESS_FLD]).exists?}
  ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_CORS_ADDRESS_FLD]).set test_data[MODIFY_SUPPLIER_CORS_ADDRESS_FLD]
end


if test_data[MODIFY_SUPPLIER_PAN_NO_FLD] && attributes[MODIFY_SUPPLIER_PAN_NO_FLD]!=nil
  ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_PAN_NO_FLD]).set test_data[MODIFY_SUPPLIER_PAN_NO_FLD]
end

if test_data[MODIFY_SUPPLIER_STATUS_FLD] && attributes[MODIFY_SUPPLIER_STATUS_FLD]!=nil
  ie_modify_supplier.select_list(:id,attributes[MODIFY_SUPPLIER_STATUS_FLD]).set test_data[MODIFY_SUPPLIER_STATUS_FLD]
end


if test_data[MODIFY_SUPPLIER_DATE_FLD] && attributes[MODIFY_SUPPLIER_DATE_FLD]!=nil
  ie_modify_supplier.text_field(:name,attributes[MODIFY_SUPPLIER_DATE_FLD]).set test_data[MODIFY_SUPPLIER_DATE_FLD]
end

end # End of modify populate details method



end#Module end
