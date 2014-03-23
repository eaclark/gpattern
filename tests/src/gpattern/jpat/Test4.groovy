package gpattern.jpat

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/11/14.
 */

//
//   This is the pattern from the Jpattern Test4.pat file
//
//    public Pattern makePattern()
//    {
//        Element = @notany("[]{}") \
//                               | ("[" Balanced_String "]") \
//                               | ("{" Balanced_String  "}")@;
//        Balanced_String = @Element arbno(Element)@;
//        Capture = @Balanced_String$Output fail@;
//        vars.put("Element",Element);
//        vars.put("Balanced_String",Balanced_String);
//        vars.put("Capture",Capture);
//        vars.put("Output",new ArrayList()); // capture everything
//        return Capture; // top level pattern
//    }
//
//    //Match("xy[ab{cd}]", Balanced_String*Current_Output Fail")
//    //x xy xy[ab{cd}] y y[ab{cd}] [ab{cd}] a ab ab{cd} b b{cd} {cd} c cd d


public class Test4
{
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        SblString subject = new SblString('xy[ab{cd}]')

        matchCtx.with {
            Element = ~(NotAny("[]{}") |
                        ('[' +  Defer( 'Balanced_String') + ']') |
                        ('{' +  Defer( 'Balanced_String') + '}'))
            Balanced_String = Defer('Element') + Arbno(Defer('Element'))
            Capture = Defer('Balanced_String').ia('_OUTPUT') + Fail()

            result = subject[ Capture]
            if ( result == null) println 'failed'
            else println '|^' + result + '^|'
        }
    }
}

