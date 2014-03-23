package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.ARB

/**
 * Created by Ed Clark on 12/9/13.
 */


class ArbFactory extends SblBaseFactory {

    def ArbFactory() {
        name = 'ARB'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = ARB
        ni.getPatString = { -> return 'Arb(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
