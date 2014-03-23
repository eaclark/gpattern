package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.RTAB

/**
 * Created by Ed Clark on 12/9/13.
 */


class RtabFactory extends SblBaseFactory {

    def RtabFactory() {
        name = 'RTAB'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = RTAB
        ni.getPatString = { -> return 'RTab(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
