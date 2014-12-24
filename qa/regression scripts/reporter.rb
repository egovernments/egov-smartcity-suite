require 'delegate'
require 'stringio'
require 'fileutils'
require 'test/unit'
require 'test/unit/ui/console/testrunner'
#~ require 'ClassAttr'
puts "am in reporter"
class Class

       def class_attr_reader(*symbols)
               symbols.each do |symbol|
                       self.class.send(:define_method, symbol) do
                               class_variable_get("@@#{symbol}")
                       end
               end
       end
       def class_attr_writer(*symbols)
               symbols.each do |symbol|
                       self.class.send(:define_method, "#{symbol}=") do |value|
                               class_variable_set("@@#{symbol}", value)
                       end
               end
       end

       def class_attr_accessor(*symbols)
               class_attr_reader(*symbols)
               class_attr_writer(*symbols)
       end

end

module YNOT
  module YNOTE

class OutputCapture < DelegateClass(IO)
      # Start capturing IO, using the given block to assign self to the proper IO global.
      def initialize(io, &assign)
        super
        @delegate_io = io
        @captured_io = StringIO.new
        @assign_block = assign
        @assign_block.call self
      end

      # Finalize the capture and reset to the original IO object.
      def finish
        @assign_block.call @delegate_io
        @captured_io.string
      end

      # setup tee methods
      %w(<< print printf putc puts write).each do |m|
        module_eval(<<-EOS, __FILE__, __LINE__)
          def #{m}(*args, &block)
            @delegate_io.send(:#{m}, *args, &block)
            @captured_io.send(:#{m}, *args, &block)
          end
        EOS
      end
    end


    # Basic structure representing the running of a test suite.  Used to time tests and store results.
    class TestSuite < Struct.new(:name, :tests, :time, :failures, :errors, :assertions, :passed)
      attr_accessor :testcases
      attr_accessor :stdout, :stderr
      def initialize(name)
        super(name.to_s) # RSpec passes a "description" object instead of a string
        @testcases = []
      end

      # Starts timing the test suite.
      def start
        @start = Time.now
        unless ENV['CI_CAPTURE'] == "off"
          @capture_out = OutputCapture.new($stdout) {|io| $stdout = io }
          @capture_err = OutputCapture.new($stderr) {|io| $stderr = io }
        end
      end

      # Finishes timing the test suite.
      def finish
        self.tests = testcases.size
        self.time = Time.now - @start 
        #self.failures = testcases.inject(0) {|sum,tc| sum += tc.failures.select{|f| f.failure? }.size }
        self.failures = 0
        testcases.each { |tc|
          self.failures += 1 if tc.failure?
        }
        self.errors = testcases.inject(0) {|sum,tc| sum += tc.failures.select{|f| f.error? }.size }
        ####
        self.passed =0
        testcases.each { |tc|
          self.passed += 1 if !(tc.failure? || tc.error?)
        }
        #puts "PASSED WAS - #{self.passed}"
        ####
        self.stdout = @capture_out.finish if @capture_out
        self.stderr = @capture_err.finish if @capture_err
      end

      def passed?
        testcases.each { |tc|
          return false if (tc.failure? || tc.error?)
        }
        return true
      end


      # Creates the xml builder instance used to create the report xml document.
      def create_builder
        require 'rubygems'
        gem 'builder'
        require 'builder'
        # :escape_attrs is obsolete in a newer version, but should do no harm
        Builder::XmlMarkup.new(:indent => 2, :escape_attrs => true)
      end

      # Creates an xml string containing the test suite results.
      def to_xml
        builder = create_builder
        # more recent version of Builder doesn't need the escaping
        def builder.trunc!(txt)
          txt.sub(/\n.*/m, '...')
        end
        #builder.instruct!
        attrs = {}
        each_pair {|k,v| attrs[k] = builder.trunc!(v.to_s) unless v.nil? || v.to_s.empty? }
        builder.testsuite(attrs) do
          @testcases.each do |tc|
            tc.to_xml(builder)
          end
          builder.tag! "system-out" do
            builder.cdata! self.stdout
          end
          builder.tag! "system-err" do
            builder.cdata! self.stderr
          end
        end
      end

    end


class TestCase < Struct.new(:name, :time, :assertions)
      attr_accessor :failures

      def initialize(*args)
        super
        @failures = []
      end

      # Starts timing the test.
      def start
        @start = Time.now
      end

      # Finishes timing the test.
      def finish
        #self.time = Time.now - @start
        self.time = (Time.now - @start).to_i # roundoff the time to an integer
      end

      # Returns non-nil if the test failed.
      def failure?
        !failures.empty? && failures.detect {|f| f.failure? }
      end

      # Returns non-nil if the test had an error.
      def error?
        !failures.empty? && failures.detect {|f| f.error? }
      end

      # Writes xml representing the test result to the provided builder.
      def to_xml(builder)
        attrs = {}
        each_pair {|k,v| attrs[k] = builder.trunc!(v.to_s) unless v.nil? || v.to_s.empty?}
        builder.testcase(attrs) do
          failures.each do |failure|

            # - This is where the type of failure is checked .. error or failure
            if failure.kind_of?(YNOT::YNOTE::TestUnitFailure)
              builder.failure(:type => builder.trunc!(failure.name), :message => builder.trunc!(failure.message)) do
                builder.text!(failure.message + " (#{failure.name})\n")
                builder.text!(failure.location)
              end
            else

              builder.error(:type => builder.trunc!(failure.name), :message => builder.trunc!(failure.message)) do
                builder.text!(failure.message + " (#{failure.name})\n")
                builder.text!(failure.location)
              end
            end

          end
        end
      end

    end

  class ReportManager
      @@current_suites = Array.new()
      class_attr_accessor(:current_suites)
      def initialize(prefix)
        @basedir = ENV['CI_REPORTS'] || File.expand_path("#{Dir.getwd}/#{prefix.downcase}/reports")
        @basename = "#{@basedir}/#{prefix.upcase}"
        FileUtils.mkdir_p(@basedir)
      end

      def write_report(suite)
        File.open("#{@basename}-#{suite.name.gsub(/[^a-zA-Z0-9]+/, '-')}.xml", "w") do |f|
          f << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
          f << suite.to_xml
        end
      end

      # will do runtime reporting
      def write_newreport_allsuites()
        # my check

          passed = true
          str=''
          ReportManager.current_suites.each { |st|
            passed = false if !(st.passed?)
            str << st.to_xml
          }
          #puts "Suite has - #{passed}"
          File.open("#{@basename}-#{"MYREPORTFILE".gsub(/[^a-zA-Z0-9]+/, '-')}.xml", "w") do |f|
            f << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tests passed='#{passed}'>\n"
            f << str
            f << "</tests>"
          end
      end

  end

    class Failure
      def self.new(fault)
        fault.kind_of?(Test::Unit::Failure) ? TestUnitFailure.new(fault) : TestUnitError.new(fault)
      end
    end

    # Wrapper around a <code>Test::Unit</code> error to be used by the test suite to interpret results.
    class TestUnitError
      def initialize(fault)
        @fault = fault
      end
      def failure?() false end
      def error?() true end
      def name() @fault.exception.class.name end
      def message() @fault.exception.message end
      def location() @fault.exception.backtrace.join("\n") end
    end

    # Wrapper around a <code>Test::Unit</code> failure to be used by the test suite to interpret results.
    class TestUnitFailure
      def initialize(fault)
        @fault = fault
      end
      def failure?() true end
      def error?() false end
      def name() Test::Unit::AssertionFailedError.name end
      def message() @fault.message end
      def location() @fault.location.join("\n") end
    end

    # Replacement Mediator that adds listeners to capture the results of the <code>Test::Unit</code> runs.
    class TestUnit < Test::Unit::UI::TestRunnerMediator
      def initialize(suite, report_mgr = nil)
        super(suite)
        @report_manager = report_mgr || ReportManager.new("test")
        add_listener(Test::Unit::UI::TestRunnerMediator::STARTED, &method(:started))
        add_listener(Test::Unit::TestCase::STARTED, &method(:test_started))
        add_listener(Test::Unit::TestCase::FINISHED, &method(:test_finished))
        add_listener(Test::Unit::TestResult::FAULT, &method(:fault))
        add_listener(Test::Unit::UI::TestRunnerMediator::FINISHED, &method(:finished))
      end

      def started(result)
        @suite_result = result
        @last_assertion_count = 0
        @current_suite = nil
        @unknown_count = 0
        @result_assertion_count = 0
      end

      def test_started(name)
        test_name, suite_name = extract_names(name)
        unless @current_suite && @current_suite.name == suite_name
          finish_suite
          start_suite(suite_name)
        end
        start_test(test_name)
      end

      def test_finished(name)
        finish_test
      end

      def fault(fault)
        tc = @current_suite.testcases.last
        tc.failures << Failure.new(fault)
      end

      def finished(elapsed_time)
        finish_suite
      end

      private
      def extract_names(name)
        match = name.match(/(.*)\(([^)]*)\)/)
        if match
          [match[1], match[2]]
        else
          @unknown_count += 1
          [name, "unknown-#{@unknown_count}"]
        end
      end

      def start_suite(suite_name)
        @current_suite = TestSuite.new(suite_name)
        @current_suite.start
        ReportManager.current_suites << @current_suite
=begin
        @current_suite = TestSuite.new(suite_name)
        @current_suite.start
=end
      end

      def finish_suite
        if @current_suite
          @current_suite.finish 
          @current_suite.assertions = @suite_result.assertion_count - @last_assertion_count
          @last_assertion_count = @suite_result.assertion_count
          ##### remove this after you make the changes
          ######@report_manager.write_report(@current_suite) #old report -- creates a xmlfile for each class
          ################################
          #@report_manager.write_newreport(@current_suites)
          @report_manager.write_newreport_allsuites()
        end
      end

      def start_test(test_name)
        tc = TestCase.new(test_name)
        tc.start
        @current_suite.testcases << tc

#=begin --- This is written for returning results in runtime.. after each testcase is run...  when running only name will be present
        @current_suite.assertions = @suite_result.assertion_count - @last_assertion_count
        @last_assertion_count = @suite_result.assertion_count
        #######@report_manager.write_report(@current_suite) #old report -- creates a xmlfile for each class
        @report_manager.write_newreport_allsuites()
        #@report_manager.write_newreport_testcaselevel(@current_suites)
        #@report_manager.write_newreport(@current_suites)
#=end
      end

      def finish_test
        tc = @current_suite.testcases.last
        tc.finish
        tc.assertions = @suite_result.assertion_count - @result_assertion_count
        @result_assertion_count = @suite_result.assertion_count
        @report_manager.write_newreport_allsuites()
      end
    end

  end
end


module Test #:nodoc:all
  module Unit
    module UI
      module Console
        class TestRunner
          def create_mediator(suite)
            # swap in our custom mediator
            return YNOT::YNOTE::TestUnit.new(suite)
          end
        end
      end
    end
  end
end
