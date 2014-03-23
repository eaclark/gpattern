package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE
import static gpattern.SblPrimitives.REM

/**
 * Created by Ed Clark on 12/9/13.
 */


class RemFactory extends SblBaseFactory {

    def RemFactory() {
        name = 'REM'
        argCount = 1
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = PRIMITIVE
        ni.prim = REM
        ni.getPatString = { -> return 'Rem(' + value + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
