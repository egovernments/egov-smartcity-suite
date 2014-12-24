require 'win32ole'

begin
 autoit = WIN32OLE.new('AutoItX3.Control')

 loop do
  autoit.ControlClick("Windows Internet Explorer",'', 'OK')
  autoit.ControlClick("Windows Internet Explorer",'', '&Yes')
  autoit.ControlClick("Security Information",'', '&Yes')
  autoit.ControlClick("Security Alert",'', '&Yes')
  sleep(2)
 end

rescue Exception => e
 puts e
end