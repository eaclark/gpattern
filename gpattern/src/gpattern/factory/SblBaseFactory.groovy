package gpattern.factory

import gpattern.SblPatNode
import static gpattern.SblPatNodeType.PRIMITIVE

/**
 * Created by Ed Clark on 12/9/13.
 */


class SblBaseFactory extends AbstractFactory {
    protected int argCount = 0
    protected String name = 'BASE'

    public Object newInstance( FactoryBuilderSupport builder,
                               Object name,
                               Object value,
                               Map attributes) throws InstantiationException, IllegalAccessException {
        def newNode

        newNode = new SblPatNode( type: PRIMITIVE,
                                  component: null,
                                  arg: value,
                                  attribs: attributes,
                                  name: name,
                                  builder: builder,
                                  getPatString : { -> return '' }
                                )
        return newNode
    }

}
