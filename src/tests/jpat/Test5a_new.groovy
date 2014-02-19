package tests.jpat

import jpattern.ExternalVariable
import jpattern.VarMap
import sblbuilder.SblBuilder
import sblbuilder.SblString

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


public class Test5a_new
{
    def ev = [ current: 'before',
               get: { VarMap vars -> println "extvar.get: ${ev.current}"; return ev.current},
               put: { VarMap var, Object o -> println "extvar.put: ${ev.current}->*$o*"; ev.current = o},
               toString: { return "ExternalVar(${ev.current})" }
             ]

    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('before    after')
        def local = new Test5a_new()
        def extvar = local.ev as ExternalVariable
        matchCtx.registerVariable('ExtVar', extvar)

        matchCtx.with {
            p = (Defer('ExtVar') + Span(' ')).ca('ExtVar') + StringPat('after').cr('ExtVar')
            println '|>' + subject[ p] + '<|'
        }
    }

}