package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.NSPAN

/**
 * Created by Ed Clark on 12/9/13.
 */


class NSpanFactory extends SblBaseFactory {

    def NSpanFactory() {
        name = 'NSPAN'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = NSPAN
        return ni
    }
}
