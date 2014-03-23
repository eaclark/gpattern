package gpattern

/**
 * Created by Ed Clark on 12/9/13.
 */

class SblBuilder extends FactoryBuilderSupport {
    private primitiveEnum = SblPrimitives
    private Map primitives = new HashMap()

    private SblMatchContext _context

    public SblBuilder() {
        registerPrimitives()

        _context = new SblMatchContext([builder: this])
        _context.init()
    }

//    def printPatTree( node, depth) {
//
//        def buffer = '    '
//
//        switch( node.type) {
//        case ROOT:
//            println '\n\n\n'
//            println 'node tree'
//            println '\n\n\n'
//            println 'matchContext - type = ' + node.type + ' name = ' + node.name
//            node.children.each { n-> printPatTree( n, depth+1) }
//            break
//        case CONCATENATE:
//        case ALTERNATE:
//            println ' ' + buffer*depth + node.type + ' name = ' + node.name
//            node.children.each { printPatTree( it, depth+1) }
//            break;
//        case PRIMITIVE:
//            println ' ' + buffer*depth + node.type + ' name = ' + node.name + ' arg = ' + node.arg
//            while (node.nextNode != null) {
//                node = node.nextNode
//                println ' ' + buffer*depth + node.type + ' ' + node.name + ' arg = ' + node.arg
//            }
//            break;
//        }
//    }

    void registerPrimitive( String name, Factory factory) {
        primitives.put(name, factory)
        registerFactory( name, factory)
    }

    void registerPrimitives() {
        String name
        Factory factory
        primitiveEnum.values().each { prim ->
            name = prim
            factory = prim.factory
            registerPrimitive( name, factory)
        }
    }

    SblMatchContext matchContext() {
        //return _context
        SblMatchContext ctx = new SblMatchContext( [builder: this])
        ctx.init()
        return ctx
    }
}
