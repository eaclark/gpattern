package sblbuilder.factory

import sblbuilder.SblPatNode
import static sblbuilder.SblPatNodeType.PRIMITIVE
import static sblbuilder.SblPrimitives.FAIL

/**
 * Created by Ed Clark on 12/9/13.
 */


class FailFactory extends SblBaseFactory {

    def FailFactory() {
        name = 'FAIL'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = FAIL
        return ni
    }
}
