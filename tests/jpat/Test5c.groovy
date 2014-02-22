package jpat

import jpattern.ExternalVariable
import jpattern.VarMap
import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 2/2/14.
 */

//
//   This is the pattern from the Jpattern Test5c.pat file
//
//    public Pattern makePattern()
//    {
//        vars.put("Check_1",extvar);
//        Pattern p = @len(1)**Check_1@;
//        return p; // top level pattern
//    }


class Check_1 implements ExternalVariable
{
    Object current = null

    public Check_1(Object o) { current=o }

    public String toString() { return "Check_1($current)" }

    // Interface Extvar methods
    public Object get(VarMap vars) {
        println "extvar.get: $current"
        return current
    }
    public void put(VarMap vars, Object o) {
        println "extvar.put: $current->$o"
        current = o
    }
}

public class Test5c
{
    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('before    after')
        def extvar = new Check_1( 'before')
        matchCtx.registerVariable('ExtVar', extvar)

        matchCtx.with {
            p = Len(1).ca('ExtVar')
            println '|>' + subject[ p] + '<|'
        }
    }

}