package gpattern.jpat

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 2/8/14.
 */

//
//   This is the pattern from the Jpattern Test14c.pat file
//
//    public boolean anchorMode() {return true;}
//
//    public Pattern makePattern()
//    {
//        vars.put("Digit", "0123456789");
//        vars.put("result",new ArrayList());
//        vars.put("span",new ArrayList());
//        Pattern P1 = Pattern.BreakX(Variable.create("Digit"));
//        P1 = Pattern.IAssign(P1, Variable.create("result"));
//        Pattern P2 = Pattern.Span(Variable.create("Digit"));
//        P2 = Pattern.IAssign(P2, Variable.create("span"));
//        Pattern PF = Pattern.Fail();
//        Pattern PC1 = Pattern.Concat(P2, PF);
//        Pattern P = Pattern.Concat(P1, PC1);
//        if(false) {
//            String sp = P.graphToString();
//            System.err.println("Breakx: " + sp);
//        }
//        return P;
//    }


public class Test14c
{
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        SblString subject = new SblString('ab123cd4657ef23')

        matchCtx.with {
            // public boolean anchorMode() {return true;}
            _AnchorMode = true

            // vars.put("Digit", "0123456789");
            registerVariable('Digit', '0123456789')
            // vars.put("result",new ArrayList());
            def (rarray, rvar) = getCollection()
            // vars.put("span",new ArrayList());
            def (sarray, svar) = getCollection()

            // Pattern P1 = Pattern.BreakX(Variable.create("Digit"));
            // P1 = Pattern.IAssign(P1, Variable.create("result"));
            P1 = BreakX( Digit).ia( rvar)
            // Pattern P2 = Pattern.Span(Variable.create("Digit"));
            // P2 = Pattern.IAssign(P2, Variable.create("span"));
            P2 = Span( Digit).ia( svar)
            // Pattern PF = Pattern.Fail();
            PF = Fail()
            // Pattern PC1 = Pattern.Concat(P2, PF);
            PC1 = P2 + PF
            // Pattern P = Pattern.Concat(P1, PC1);
            P = P1 + PC1

            result = subject[ P]

            // could clean up the collections if this were a longer program
            //releaseCollection( rvar)
            //releaseCollection( svar)

            // because of the Fail() at the end of the pattern, the match will ultimately fail
            if ( result == null) println 'failed'
            println 'result = ' + rarray
            println 'span = ' + sarray
        }
    }
}

