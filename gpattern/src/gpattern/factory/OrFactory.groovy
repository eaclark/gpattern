package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.ALTERNATE

/**
 * Created by eac on 12/9/13.
 */


class OrFactory extends SblBaseFactory {

    def OrFactory() {
        name = 'OR'
    }
    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        SblPatNode ni = super.newInstance( builder, name, value, attributes)
        ni.type = ALTERNATE
        ni.getPatString = { -> '(' + ni.children[0] + ' | ' + ni.children[1] + ')' }
        ni.getPatString.delegate = ni
        return ni
    }
}
