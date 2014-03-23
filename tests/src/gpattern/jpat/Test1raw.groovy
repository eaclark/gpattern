package gpattern.jpat

import jpattern.Matcher
import jpattern.Pattern
import jpattern.PatternBuilder
import jpattern.VarMap
import jpattern.Variable

/**
 * Created by Ed Clark on 1/2/14.
 */

//
//   This is the pattern from the Jpattern Test1.pat file
//
//        public Pattern makePattern()
//        {
//            // Use the ability to embed java code to include Digs
//            Pattern Digs = @span("0123456789")@;
//            Pattern Lnum = @pos(0) (`Digs` ".") span(" ")@;
//            vars.put("Digs",Digs);
//            vars.put("Lnum",Lnum);
//            return Lnum;
//        }

public class Test1raw extends PatternBuilder
{
    public VarMap buildPattern() {return makePattern()}

    static VarMap vars = new VarMap()
    static Pattern Digs = null
    static Pattern Lnum = null

    static VarMap makePattern()
    {
        println "Example 1"
        Lnum = Concat(
                   Pos(0),
                   Concat(
                       Concat(
                           Defer(Variable.create("Digs")),
                           StringPattern(".")
                       ),
                       Span(" ")
                   )
               )
        Digs = Span("0123456789")
        vars.put("Digs",Digs)
        vars.put("Lnum",Lnum)
        println "Example 1: vars=$vars"
        return vars
    }
    
    static main(args) {

        Matcher matcher

        new Test1raw().buildPattern()
        matcher = vars.get('Lnum').matcher()
        matcher.subject = '41234.  '
        matcher.varMap = vars
        def ok = matcher.match()
        println 'ok = ' + ok
        println 'result = |^' + matcher + '^|'
    }
}

