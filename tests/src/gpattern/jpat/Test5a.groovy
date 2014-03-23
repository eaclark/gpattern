package gpattern.jpat

import gpattern.SblMatchContext
import jpattern.ExternalVariable
import jpattern.VarMap
import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/13/14.
 */

//
//   This is the pattern from the Jpattern Test5a.pat file
//
//    public Pattern makePattern()
//    {
//        vars.put("ExternalVar",extvar);
//        Pattern p = @(+ExternalVar & span(" ")) $ +ExternalVar & "after" = +ExternalVar@;
//        return p; // top level pattern
//    }

public class Test5a
{

    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        SblString subject = new SblString('before    after')
        ExternalVar extvar = new ExternalVar( 'before')
        matchCtx.registerVariable('ExtVar', extvar)

        matchCtx.with {
            // top level 'after'.cr('ExtVar') doesn't transform properly
//            p = (Defer('ExtVar') + Span(' ')).ca('ExtVar') + ~('after').cr('ExtVar')
            p = ~( (Defer('ExtVar') + Span(' ')).ca('ExtVar') + 'after'.cr('ExtVar'))
            println '|>' + subject[ p] + '<|'
        }
    }
}

class ExternalVar implements ExternalVariable
{
    Object current = null

    public ExternalVar(Object o) { current=o }

    public String toString() {return "ExternalVar(" + current + ")"}

    // Interface Extvar methods
    public Object get(VarMap vars) {
        println "extvar.get: #" + current + "#"
        return current
    }
    public void put(VarMap vars, Object o) {
        println "extvar.put: " + current + "->*" + o + "*"
        current = o
    }
}