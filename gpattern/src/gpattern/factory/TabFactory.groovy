package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.TAB

/**
 * Created by eac on 12/9/13.
 */


class TabFactory extends SblBaseFactory {

    def TabFactory() {
        name = 'TAB'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {

        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = TAB
        ni.handleDefer = true
        ni.getPatString = { -> return 'Tab(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
