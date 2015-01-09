def path_to(current,*path)
   path_to_file(path[-1])
end

def path_to_file(file)
    file="#{file}.rb" if file.rindex(".").nil?
    files=Dir.glob(File.join(ENV['QA_HOME'].gsub(/\\/,"/"),"**",file))
    files[0]
end
