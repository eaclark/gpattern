package jpat

import jpattern.ExternalVariable
import jpattern.VarMap
import gpattern.SblBuilder
import gpattern.SblString

/**
 * Created by Ed Clark on 1/20/14.
 */

//
//   This is the pattern from the Jpattern Test5b.pat file
//
//    public Pattern makePattern()
//    {
//        vars.put("GTS",gts);
//        vars.put("Digit","0123456789");
//        Pattern Digs = @span(+Digit)@;
//        // Note that the comments are inside the backslash
//        Pattern Find = @""*Max fence           // initialize Max to null \
//                        & breakx(+Digit)       // scan looking for digits \
//                        & ((span(+Digit)*Cur   // assign next string to Cur \
//                            & +GTS             // check size(Cur) > Size(Max) \
//                            & setcur(+Loc))    // if so, save location \
//                           * Max)              // and assign to Max. \
//                        & fail@;               // seek all alternatives
//        return Find; // top level pattern
//    }


public class Test5b_new
{
    def gts = [ get: { VarMap vars ->
                         String Cur = vars.getString("Cur","")
                         String Max = vars.getString("Max","")
                         return Boolean.valueOf(Cur.length() > Max.length())},
                put: {}
              ]

    static main(args) {

        def builder = new SblBuilder()
        def matchCtx = builder.matchContext()

        SblString subject = new SblString('ab123cd4657ef23')
        def local = new Test5b_new()
        def extvar = local.gts as ExternalVariable
        matchCtx.registerVariable('ExtVar', extvar)

        matchCtx.with {
            registerVariable("Digit","0123456789");
            Find = StringPat('').ia('Max') + Fence() +       // initialize Max to null
                   BreakX( Digit) +                          // scan looking for digits
                   ((Span( Digit).ia( 'Cur') +               // assign next string to Cur
                     Defer( 'ExtVar') +                      // check size(Cur) > Size(Max)
                     Setcur( 'Loc')).ia( 'Max')) +           // if so, save location
                   Fail()                                    // seek all alternatives

            subject[ Find]
            println 'Cur = ' + Cur
            println 'Loc = ' + Loc
            println 'Digit = ' + Digit
            println 'Max = ' + Max
        }
    }

}