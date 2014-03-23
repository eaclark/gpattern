package gpattern.jpat

import jpattern.Matcher
import jpattern.Pattern
import jpattern.PatternBuilder
import jpattern.VarMap
import jpattern.Variable

/**
 * Created by Ed Clark on 1/11/14.
 */

public class Test4raw extends PatternBuilder
{
    public VarMap buildPattern() {return makePattern()}

    static VarMap vars = new VarMap()
    static Pattern Capture = null
    static Pattern Element = null
    static Pattern Balanced_String = null

    static VarMap makePattern()
    {
        println "Example 4"
        Element = Alternate(
                      NotAny("[]{}"),
                      Alternate(
                          Concat(
                              StringPattern("["),
                              Concat(
                                  Defer(Variable.create("Balanced_String")),
                                  StringPattern("]")
                              )
                          ),
                          Concat(
                              StringPattern("{"),
                              Concat(
                                  Defer(Variable.create("Balanced_String")),
                                  StringPattern("}")
                              )
                          )
                      )
                  )
        Balanced_String = Concat(
                              Defer(Variable.create("Element")),
                              Arbno(Pattern.Defer(Variable.create("Element")))
                          )
        Capture = Concat(
                      IAssign(Defer(Variable.create("Balanced_String")),
                              Variable.create("Output")),
                      Fail()
                  )
        vars.put("Element",Element)
        vars.put("Balanced_String",Balanced_String)
        vars.put("Capture",Capture)
        vars.put("Output",new ArrayList()) // capture everything
        return vars
    }

//Match("xy[ab{cd}]", Balanced_String*Current_Output Fail")
//x xy xy[ab{cd}] y y[ab{cd}] [ab{cd}] a ab ab{cd} b b{cd} {cd} c cd d

    static main(args) {
        Matcher matcher

        def helloWorld = new Test4raw()


        vars.put("Output",new ArrayList()); // capture everything

        helloWorld.buildPattern()
        println "Example 4: vars=$vars"

        matcher = vars.get('Capture').matcher()
        matcher.subject = 'xy[ab{cd}]'
        matcher.varMap = vars
        def ok = matcher.match()
        println 'ok = ' + ok
        println 'result = |^' + matcher + '^|'
    }
}

