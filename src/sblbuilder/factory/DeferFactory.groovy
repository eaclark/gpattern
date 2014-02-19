package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.UNEVAL
import static sblbuilder.SblPrimitives.DEFER

/**
 * Created by Ed Clark on 12/9/13.
 */


class DeferFactory extends SblBaseFactory {

    def DeferFactory() {
        name = 'DEFER'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = UNEVAL
        ni.prim = DEFER
        return ni
    }
}
