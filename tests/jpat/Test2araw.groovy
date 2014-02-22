package jpat

import jpattern.MatchResult
import jpattern.Matcher
import jpattern.Pattern
import jpattern.PatternBuilder
import jpattern.VarMap

import jpattern.Variable

/**
 * Created by Ed Clark on 1/2/14.
 */

//
//   This is the pattern from the Jpattern Test2a.pat file
//
//        public Pattern makePattern()
//        {
//            B = @nspan(" ")@;
//            N = @span("0123456789")@;
//            T = @nspan(" ") +N*Num1 span(" ,") +N*Num2@;
//            vars.put("B",B);
//            vars.put("N",N);
//            vars.put("T",T);
//            return T; // top level pattern
//        }


public class Test2araw extends PatternBuilder
{
    public VarMap buildPattern() {return makePattern()}

    static VarMap vars = new VarMap()
    static Pattern B = null
    static Pattern N = null
    static Pattern T = null

    static Matcher matcher //= new Matcher()
    static MatchResult result //= matcher

    static VarMap makePattern()
    {
        println "Example 2a"
        B = NSpan(" ")
        N = Span("0123456789")
        T = Concat(
                NSpan(" "),
                Concat(
                    IAssign( Defer(Variable.create("N")), Variable.create("Num1")),
                    Concat(
                        Span(" ,"),
                        IAssign( Defer(Variable.create("N")), Variable.create("Num2"))
                    )
                )
            )
        vars.put("B",B)
        vars.put("N",N)
        vars.put("T",T)
        println "Example 2a: vars=$vars"
        return vars
    }

    static main(args) {
        def helloWorld = new Test2araw()
        helloWorld.buildPattern()

        matcher = vars.get('T').matcher()
        matcher.subject = ' 124, 257  '
        matcher.varMap = vars
        def ok = matcher.match()

        println 'ok = ' + ok
        println 'result = |^' + matcher + '^|'
        println 'Num1 = ' + vars.get('Num1')
        println 'Num2 = ' + vars.get('Num2')
    }
}

