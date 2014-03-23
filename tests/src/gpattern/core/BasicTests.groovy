package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class BasicTests {
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()
        SblString str1 = new SblString()
        SblString str2 = new SblString()

        matchCtx.with {
            // Abort and Cancel
            String base = 'this is a test'
            str1.value = '*' + base
            str2.value = ' ' + base
            // pattern that aborts if the first character is a '*'
            pat = ~((('*' + Abort()) | ' ') + Rem())
            assert str1[pat] == null
            assert str2[pat] == ' ' + base
            pat = ~((('*' + Cancel()) | ' ') + Rem())
            assert str1[pat] == null
            assert str2[pat] == ' ' + base

            // Any and NotAny
            str1.value = 'VACUUM'
            vowel = Any('AEIOU')
            dvowel = vowel + vowel
            notvowel = NotAny('AEIOU')
            str1[ vowel.ca('result')]
            assert result == 'A'

            str1[ dvowel.ca('result')]
            assert result == 'UU'

            str1[ (vowel + notvowel).ca('result')]
            assert result == 'AC'

            // Arb
            str1.value = 'MOUNTAIN'
            str1[ ~('O' + Arb().ca('result') + 'A')]
            assert result == 'UNT'

            str1[ ~('O' + Arb().ca('result') + 'U')]
            assert result == ''

            assert str1[ ~('O' + Arb().ca('result') + 'X')] == null

            // Arbno
            str1.value = 'CCBBAAAACC'
            str2.value = 'AABBB'
            pairs = Pos(0) + Arbno( ~('AA' | 'BB' | 'CC')) + RPos(0)
            assert str1[pairs] == 'CCBBAAAACC'
            assert str2[pairs] == null

            // Bal
            str1.value = '((A+(B*C))+D)'
            str2.value = '((a+(b*c))+d)'
            // allbal = Bal().ia('_OUTPUT') + Fail()
            // str1[~allbal]
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
            assert tmp == ['((A+(B*C))+D)',
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

            // Break and BreakX
            str1.value = 'This is a test; for the next 60 seconds...'
            assert str1[ Break(',.;:!?')] == 'This is a test'
            assert str1[ ~(Pos(0) + Break(',.;:!?') + '.')] == null
            assert str1[ ~(BreakX(',.;:!?') + '.')] == 'This is a test; for the next 60 seconds.'

            // Fail
            def ( cv, cn) = getCollection()
            str1.value = 'This is a test.'
            str1[ Len(1).ia( cn) + Fail()]
            assert cv == ['T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', '.']
            releaseCollection( cn)

            ( cv, cn) = getCollection()
            str2.value = 'MISSISSIPPI'
            str2[ ~( 'IS' |
                     'SI' |
                     'IP' |
                     'PI').ia( cn) + Fail()]
            assert cv == ['IS', 'SI', 'IS', 'SI', 'IP', 'PI']
            releaseCollection( cn)

            // Fence
            str1.value = '1AB+'
            assert str1[ ~(Any('AB') + '+')] == 'B+'
            assert str1[ ~(Any('AB') + Fence() + '+')] == null
            str2.value = 'ABC'
            assert str2[ ~(Fence() + 'B')] == null

            // Len
            ( cv, cn) = getCollection()
            str1.value = 'This is a test.'
            str1[ Len(1).ia( cn) + Fail()]
            assert cv == ['T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', '.']
            str2.value = '12 (abcde) 34'
            assert str2[ ~( '(' + Len(5) + ')' )] == '(abcde)'
            releaseCollection( cn)

            // NSpan
            ( cv, cn) = getCollection()
            num = Span("0123456789")
            nums = NSpan(' ') + num.ca( cn) + NSpan(' ,') + num.ca( cn) + NSpan(' ,') + num.ca( cn) + NSpan(' ,')
            str1.value = '124, 257 ,897'
            str1[ nums]
            assert cv == ['124', '257', '897']
            releaseCollection( cn)

            // Pos and RPos
            String testStr = 'ABBBBCDDDDEEEEE'
            str1.value = testStr
            assert str1[ ~( Pos(0) + 'A' + Span('B') + 'C' + Pos(6) +
                            Span('D') + Arbno( 'E') + RPos(1) + 'E' + RPos(0))] == testStr
            testStr = 'ABBBBBBBBBBBCDDDDDDDDDDEE'
            str2.value = testStr
            assert str2[ ~( Pos(0) + 'A' + Span('B') + 'C' +
                            Span('D') + 'E' + RPos(1))] == testStr[0 .. -2]

            // Rem and Rest
            str1.value = 'ABBBBCDDDDEEEEE'
            str1[ ~( 'BCD' + Rem().ca('result'))]
            assert result == 'DDDEEEEE'
            str1[ ~( 'BCD' + Rest().ca('result'))]
            assert result == 'DDDEEEEE'

            // Succeed
            str1.value = 'XXXXXX'
//            str1[ Succeed() + (Len(1) + Arb()).ia('_OUTPUT') + Fail()]         // infinite loop


            // Tab and RTab
            ( cv, cn) = getCollection( { String s -> s.trim() })
            str1.value = '   William T. Miller         IND   Collingswood'
            str1[ Tab(3) + Tab(29).ca(cn) + Tab(35).ca(cn) + RTab(0).ca(cn)]
            assert cv ==  ['William T. Miller', 'IND', 'Collingswood']
            releaseCollection( cn)

        }
    }
}
