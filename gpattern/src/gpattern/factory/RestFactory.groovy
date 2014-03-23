package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.REST

/**
 * Created by eac on 12/9/13.
 */


class RestFactory extends SblBaseFactory {

    def RestFactory() {
        name = 'REST'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = REST
        ni.getPatString = { -> return 'Rest(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
