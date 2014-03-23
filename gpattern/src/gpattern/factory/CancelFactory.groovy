package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.CANCEL

/**
 * Created by Ed Clark on 12/9/13.
 */


class CancelFactory extends SblBaseFactory {

    def CancelFactory() {
        name = 'CANCEL'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = CANCEL
        ni.getPatString = { -> return 'Cancel(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
