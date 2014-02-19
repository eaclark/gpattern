package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.FENCE

/**
 * Created by Ed Clark on 12/9/13.
 */


class FenceFactory extends SblBaseFactory {

    def FenceFactory() {
        name = 'FENCE'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = FENCE
        return ni
    }
}
