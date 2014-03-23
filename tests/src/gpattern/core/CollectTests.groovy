package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class CollectTests {
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()
        SblString str1 = new SblString()
        SblString str2 = new SblString()
        SblString str3 = new SblString('xy[ab{cd}]')

        matchCtx.with {
            str1.value = '((A+(B*C))+D)'
            str2.value = '((a+(b*c))+d)'

            assert str1.collect( Bal()) == ['((A+(B*C))+D)',
                                            '(A+(B*C))',
                                            '(A+(B*C))+',
                                            '(A+(B*C))+D',
                                            'A',
                                            'A+',
                                            'A+(B*C)',
                                            '+',
                                            '+(B*C)',
                                            '(B*C)',
                                            'B',
                                            'B*',
                                            'B*C',
                                            '*',
                                            '*C',
                                            'C',
                                            '+',
                                            '+D',
                                            'D']
            assert str1.collect( Bal()) { String s -> s.toLowerCase() } ==
                           ['((a+(b*c))+d)',
                            '(a+(b*c))',
                            '(a+(b*c))+',
                            '(a+(b*c))+d',
                            'a',
                            'a+',
                            'a+(b*c)',
                            '+',
                            '+(b*c)',
                            '(b*c)',
                            'b',
                            'b*',
                            'b*c',
                            '*',
                            '*c',
                            'c',
                            '+',
                            '+d',
                            'd']
            def tmp = []
            str2.each( Bal()) { String s -> tmp << s.toUpperCase() }
            assert tmp == [ '((A+(B*C))+D)',
                            '(A+(B*C))',
                            '(A+(B*C))+',
                            '(A+(B*C))+D',
                            'A',
                            'A+',
                            'A+(B*C)',
                            '+',
                            '+(B*C)',
                            '(B*C)',
                            'B',
                            'B*',
                            'B*C',
                            '*',
                            '*C',
                            'C',
                            '+',
                            '+D',
                            'D']

            // this is a riff on the test jpat.Test4
            ttest = ~('[' + Len(1))
            Element = ~( NotAny("[]{}") |
                         ( '[' +  Defer( 'Balanced_String') + ']') |
                         ( '{' +  Defer( 'Balanced_String') + '}')
                       )
            Balanced_String = Defer('Element') + Arbno(Defer('Element'))

            assert str3.collect( Balanced_String) == ['x', 'xy', 'xy[ab{cd}]', 'y', 'y[ab{cd}]', '[ab{cd}]', 'a',
                                                      'ab', 'ab{cd}', 'b', 'b{cd}', '{cd}', 'c', 'cd', 'd']


            str1 = new SblString('   William T. Miller         IND   Collingswood')
            ( cv, cn) = getCollection( { String s -> s.trim() })
            str1[ Tab(3) + Tab(29).ca(cn) + Tab(35).ca(cn) + RTab(0).ca(cn)]
            println cv
        }
    }
}
