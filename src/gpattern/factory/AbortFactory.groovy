package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.ABORT

/**
 * Created by Ed Clark on 12/9/13.
 */


class AbortFactory extends SblBaseFactory {

    def AbortFactory() {
        name = 'ABORT'
    }

    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = ABORT
        return ni
    }
}
