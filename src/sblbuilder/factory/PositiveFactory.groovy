package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.UNEVAL
import static sblbuilder.SblPrimitives.POSITIVE

/**
 * Created by Ed Clark on 12/9/13.
 */


class PositiveFactory extends SblBaseFactory {

    def PositiveFactory() {
        name = 'POSITIVE'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = UNEVAL
        ni.prim = POSITIVE
        return ni
    }
}
