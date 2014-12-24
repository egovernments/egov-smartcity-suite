Dir.glob(File.join(ENV['QA_HOME'].gsub("\\","/"),"/regression/**","test_*.rb")).each {|test| require test}
