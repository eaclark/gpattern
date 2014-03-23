package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.CONCATENATE

/**
 * Created by Ed Clark on 12/9/13.
 */


class PlusFactory extends SblBaseFactory {

    def PlusFactory() {
        name = 'PLUS'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {

        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = CONCATENATE
        ni.getPatString = { -> '(' + ni.children[0] + ' + ' + ni.children[1] + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
