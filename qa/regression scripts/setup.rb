require 'ci/reporter/rake/test_unit_loader'
ENV['DT_IE_AGENT_ACTIVE'] = 'true'
ENV['DT_IE_SESSION_NAME'] = 'Watir regression Tests'
 librbfiles = File.join(ENV['QA_HOME'].gsub(/\\/,"/"),"**", "/regression/**","test_*.rb")
 Dir.glob(librbfiles).each {|test| require test}             
   