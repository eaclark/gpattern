package tests

import sblbuilder.SblBuilder
import sblbuilder.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class DeferTests {
    static main(args) {

        def builder = new SblBuilder()

        def matchCtx = builder.matchContext()
        SblString str = new SblString()

        matchCtx.with {
            ArrayList cv
            String cn

            (cv, cn) = getCollection()
            str.value = '123AABBCC'
            Pat = Tab( Defer('I')).ca(cn) + Span( Defer('S')).ca(cn)
            I = 4
            S = 'AB'
            str[ Pat]
            assert cv == ['123A', 'ABB']
            cv.clear()
            I = 3
            str[ Pat]
            assert cv == ['123', 'AABB']
            cv.clear()

            str.value = '12 x 23434 x 123'
            P = Span('0123456789')
            str[ Defer('P').ia( cn) + Fail()]
            assert cv == ['12', '2', '23434', '3434', '434', '34', '4', '123', '23', '3']
        }
    }
}
