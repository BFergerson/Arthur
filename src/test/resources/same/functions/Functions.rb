def function1
end

def function2
end

def function3(param)
    puts "withArg"
end

def function4(*param)
    puts "varargs"
end

def function5(&param)
    puts "ampersandArg"
end

def function6(param = 1)
    puts "keywordArg1"
end

def function7(param: 1)
    puts "keywordArg2"
end

def function8(param:)
    puts "keywordArg3"
end

a = "b"
def a.function9
    puts "dynamicMethod"
end