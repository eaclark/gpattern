package gpattern.core

import gpattern.SblBuilder
import gpattern.SblMatchContext
import gpattern.SblString

/**
 * Created by Ed Clark on 1/10/14.
 */

class ReplaceTests {
    static main(args) {

        SblBuilder builder = new SblBuilder()
        SblMatchContext matchCtx = builder.matchContext()

        matchCtx.with {
            // character replacement
            SblString X = new SblString('Hello World?')
            X[ ~('?')] = '!'
            assert X.value == 'Hello World!'

            // substring replacement
            X[ ~('Hello')] = 'Welcome'
            X[ ~('World')] = 'Earth'
            X[ ~(' ')] = ' to '
            assert X.value == 'Welcome to Earth!'

            // gstring handling
            def world = 'Earth'
            X[ ~("$world")] = 'Mars'
            assert X.value == "Welcome to Mars!"


            // test replacement using variables set using pattern assignment
            SblString rainbow = new SblString('red,orange,yellow,green,blue,indigo,violet')

            def cycle = Break(',').ca('item') + Len(1) + Rem().ca('rest')
            rainbow[ cycle] = rest + ',' + item
            assert rainbow.value == 'orange,yellow,green,blue,indigo,violet,red'

            // test that no change is made if the pattern match fails
            rainbow.value = 'red-orange-yellow-green-blue-indigo-violet'
            rainbow[ cycle] = rest + ',' + item
            assert rainbow.value == 'red-orange-yellow-green-blue-indigo-violet'

            // test replacement within a pattern match
            rainbow.value = 'red,orange,yellow,green,blue,indigo,violet'
         //   rainbow[ ~("yellow").cr( 'ochre')]
            newColor = 'maroon'
            rainbow[ ~(Break(',').cr( 'newColor')) ]
            assert rainbow.value == 'maroon,orange,yellow,green,blue,indigo,violet'

        }
    }
}
