

require path_to_file('stores_constants.rb')


#require 'win32ole'

module Stores_interbin_methods

include Stores_constants
include Test::Unit::Assertions


def create_inter_bin_transfer_success(tc, test_data, attributes)

  #~ ie_create_inter_bin_transfer = start_browser(CREATE_INTERBIN_TRANSFER_URL)
       #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
      $browser.dt(:text,'Transactions').click
          $browser.link(:text, CREATE_INTERBIN_TRANSFER_LINK).click
          ie_create_inter_bin_transfer=Watir::IE.attach(:url,/loadInterBinTransferForm*/)
  assert_not_nil(ie_create_inter_bin_transfer.contains_text(CREATE_INTERBIN_TRANFER_MESG_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_INTERBIN_TRANFER_MESG_SCREEN+ ". Not in create Interbin transfer screen."))
  populate_create_inter_bin_transfer_details(tc, test_data, attributes, ie_create_inter_bin_transfer)
  ie_create_inter_bin_transfer.button(:value, CREATE_INTERBIN_TRANSFER_SAVE_BUTTON).click
  
  #~ assert_true(ie_create_inter_bin_transfer.contains_text(INTER_BIN_TRANSFER_ID), ("Couldn't find bin transfered field no. "))

if ie_create_inter_bin_transfer.text_field(:id, CREATE_INTERBIN_TRANSFER_NUMBER_FLD).exists?
  test_data[INTERBIN_TRANSFER_NUMBER_FLD] = ie_create_inter_bin_transfer.text_field(:id,CREATE_INTERBIN_TRANSFER_NUMBER_FLD).value
end
 
ie_create_inter_bin_transfer.close 
end



def inter_bin_transfer_failure(tc, test_data, attributes)

ie_create_inter_bin_transfer= start_browser(CREATE_INTERBIN_TRANSFER_URL)
assert_not_nil(ie_create_inter_bin_transfer.text_field(:id, CREATE_ITEM_FLD), ("Couldn't find item no. input field."))
populate_create_inter_bin_transfer_details(tc, test_data, attributes, ie_create_inter_bin_transfer)
ie_create_inter_bin_transfer.button(:value, CREATE_INTERBIN_TRANSFER_SAVE_BUTTON).click
#~ assert_nil(ie_create_inter_bin_transfer.contains_text('Inter Bin Transfer Id'), ("BIN TRNSFER IS NOT SUCCESSFUL "))
assert_false(ie_create_inter_bin_transfer.contains_text(INTER_BIN_TRANSFER_ID), (" found bin transfered field no. transferred not failed "))

ie_create_inter_bin_transfer.close

end


def populate_create_inter_bin_transfer_details(tc,test_data, attributes, ie_create_inter_bin_transfer)

#~ ie_create_interbin_transfer.speed = :slow

if test_data[CREATE_INTERBIN_TRANSFER_ITEM_FLD] && attributes[CREATE_INTERBIN_TRANSFER_ITEM_FLD]!=nil
ie_create_inter_bin_transfer.text_field(:id, attributes[CREATE_INTERBIN_TRANSFER_ITEM_FLD]).set test_data[CREATE_INTERBIN_TRANSFER_ITEM_FLD]
end


if test_data[CREATE_INTERBIN_TRANSFER_STORE_FLD] && attributes[CREATE_INTERBIN_TRANSFER_STORE_FLD]!=nil
ie_create_inter_bin_transfer.select_list(:name,attributes[CREATE_INTERBIN_TRANSFER_STORE_FLD]).set test_data[CREATE_INTERBIN_TRANSFER_STORE_FLD]
#~ ie_create_inter_bin_transfer_success.select_list(:name,CREATE_INTERBIN_TRANSFER_STORE_FLD).fire_event("onblur")
end

#~ if test_data[CREATE_INTERBIN_TRANSFER_FROM_FLD]== ('y'||'Y')
ie_create_inter_bin_transfer.radio(:value, CREATE_INTERBIN_TRANSFER_FROM_FLD).set
#~ end


if test_data[CREATE_INTERBIN_TRANSFER_TOBIN_FLD] && attributes[CREATE_INTERBIN_TRANSFER_TOBIN_FLD]!=nil
ie_create_inter_bin_transfer.select_list(:id, attributes[CREATE_INTERBIN_TRANSFER_TOBIN_FLD]).set test_data[CREATE_INTERBIN_TRANSFER_TOBIN_FLD]
end

if test_data[CREATE_INTERBIN_TRANSFER_QUANTITY_FLD] && attributes[CREATE_INTERBIN_TRANSFER_QUANTITY_FLD]!=nil
ie_create_inter_bin_transfer.text_field(:id,attributes[CREATE_INTERBIN_TRANSFER_QUANTITY_FLD]).set test_data[CREATE_INTERBIN_TRANSFER_QUANTITY_FLD]
end


end


def view_interbin_transfer(tc, test_data, attributes)

#~ ie_view_interbin_transfer = start_browser(VIEW_INTERBIN_TRANSFER_URL)

 #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
 $browser.dt(:text,'Transactions').click
          $browser.link(:text, VIEW_INTERBIN_TRANSFER_LINK).click
          ie_view_interbin_transfer=Watir::IE.attach(:url,/loadSearchInterBinTransfers*/)

ie_view_interbin_transfer.button(:type, VIEW_INTERBIN_TRANSFER_SEARCH_BUTTON).click

ie_view_interbin_transfer.link(:text, test_data[CREATE_INTERBIN_TRANSFER_NUMBER_FLD]).click

assert_not_nil(ie_view_interbin_transfer.contains_text(CREATE_INTERBIN_TRANFER_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + CREATE_INTERBIN_TRANFER_SUCCESS_MESG+ ". View INTERBIN TRANSFER Screen."))

assert_true(test_data[CREATE_INTERBIN_TRANSFER_NUMBER_FLD]== ie_view_interbin_transfer.textField(:id, 'interBinId').value, 'item not transferred') 

#~ ensure 
#~ ie_view_interbin_transfer.close

end



end#Module end
