package gpattern

import jpattern.ExternalMap
import jpattern.ExternalVariable
import jpattern.Matcher

import jpattern.VarMap

import gpattern.SblString
import static gpattern.SblPatNodeType.PENDING
import gpattern.transforms.SblMatchContextMarker

/**
 * Created by Ed Clark on 12/9/13.
 */

@SblMatchContextMarker
class SblMatchContext {
    SblBuilder builder

    VarMap vars = new VarMap()
    ExternalMap extVars = new ExternalMap()
    Map<String, SblPatNode> patterns = new HashMap()
    Matcher matcher
    private modes = [:]

    void init() {
        // this is a placeholder matcher - it will be replaced witch each new match
        if (matcher == null) matcher = new Matcher( builder.Succeed().compile() )
        matcher.varMap = vars

        registerVariable( '_OUTPUT',
                [ get : { vars -> },
                  put : { vars, val -> println val.toString()}
                ] as ExternalVariable )

        registerVariable( '_INPUT',
                [ get : { vars -> println 'testing'; System.in.newReader().readLine() },
                  put : { vars, val -> }
                ] as ExternalVariable )


        StringBuilder tempStr = new StringBuilder(256)
        (0i .. 255i).each { tempStr << (char)it }
        registerVariable( '_ALPHABET',  new SblString(tempStr))

        matcher.setAnchorMode( get_AnchorMode())
    }

    void registerVariable( String name, var) {
        vars.put( name, var)
    }

    void removeVariable( String name) {
        vars.remove( name)
    }

    void registerPattern( String name, SblPatNode node) {
        patterns.put(name, node)
        registerVariable( name, node.component)
    }

    void registerPattern( String name, Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_ONLY
        def nextNode = c()

        if( nextNode instanceof SblPatNode) {
            // println 'matchContext - pattern give closure, got ' + node
            registerPattern( name, node)
        }
    }

    def methodMissing( String name, Object args ) {
        SblPatNode nextNode = builder."$name"( *args)
        nextNode.context = this
        nextNode.deferArgHandling()
        return nextNode
    }

    def getCollection() {
        ArrayList result = []
        String name = UUID.randomUUID().toString()

        registerVariable( name,
                          [ get : { vars -> },
                            put : { vars, val -> result << val }
                          ] as ExternalVariable )

        return [result, name]
    }

    def getCollection( Closure c) {
        ArrayList result = []
        String name = UUID.randomUUID().toString()

        registerVariable( name,
                          [ get : { vars -> },
                            put : { vars, val -> result << c(val) }
                          ] as ExternalVariable )

        return [result, name]
    }

    void releaseCollection( String name) {
        vars.remove( name)
    }

    def propertyMissing( String name) {
        def val

        // Look to see if the name has been registered as a pattern or variable.
        // If so, return the value previously assigned (which might be null).
        if( patterns.containsKey(name)) {
            val = patterns[ name]
            if( val?.class == SblPatNode){
                // whenever you pull a patNode out of the patterns, clone it so it
                // can be used without stepping on other places it might be used.
                val = val.clone()
                // give this copy a unique name
                val.name = val.toString()
            }
        } else if( vars.containsKey(name)) {
            val = vars[ name]
            if( val instanceof ExternalVariable) {
                // get the value for EVs *except* for SblString
                if( !(val instanceof SblString)) val = val.get(vars)
            }
        }

        // If we still haven't found anything we treat this as a pattern node
        // that just hasn't be populated yet
        else val = new SblPatNode( type: PENDING, name: name, context: this, builder: builder)

        return val
    }

    def propertyMissing( String name, Object value) {
        // look to see if the name has been registered, if so, set the value
        if( value instanceof SblPatNode) registerPattern( name, value)
        else if( value instanceof SblString) {
            // copy the value string
            value = new SblString( value.value)
            registerVariable( name, value)
        }
        else if( value instanceof String) registerVariable( name, new SblString( value))
        else if( value instanceof Number) registerVariable( name, value)
        else if( value instanceof ExternalVariable)
            registerVariable( name, value)
        else if( value == null)  registerVariable( name, value)    // handles setting a var to null, e.g. on pattern failure
    }

    boolean get_AnchorMode() {
        return modes[ 'Anchored'] ?: false
    }

    void set_AnchorMode( boolean anchor) {
        modes[ 'Anchored'] = anchor
        if( matcher != null) matcher.setAnchorMode( anchor)
    }

    SblString createSblString( String str) {
        SblString ss = new SblString( str)
        ss._rc = this
        return ss
    }

    @SblMatchContextMarker
    def with( @SblMatchContextMarker Closure c) { super.with(c) }
}
