package sblbuilder

import jpattern.Pattern
import jpattern.Variable

import static sblbuilder.SblPatNodeType.*
import static sblbuilder.SblPatNode.CompiledState.*
import static sblbuilder.SblPrimitives.*

/**
 * Created by Ed Clark on 12/9/13.
 */

class SblPatNode {
    SblPatNodeType type
    SblPrimitives prim
    def name
    CompiledState compiled = NOTCOMPILED
    def component
    def arg
    boolean handleDefer = false
    def attribs

    SblBuilder builder
    SblMatchContext context


    SblPatNode parent = null
    def children = []
    SblPatNode nextNode = null
    SblPatNode prevNode = null

    private _iAssign = [ active: false, target: null, value: null]    // immediate assignment
    private _cAssign = [ active: false, target: null, value: null]    // conditional assignment
    private _cReplace = [ active: false, target: null, value: null]   // conditional replacement

    def methodMissing( String name, Object args ) {
        SblPatNode nNode
        // look to see if there are any args passed in or not
        nNode = (args == [] ) ? builder."$name"() : builder."$name"(*args)

        if( nNode instanceof SblPatNode) {

            nNode.context = context

            switch ( nNode.type) {
            case ROOT:
                // this should only be at the end of the pattern specification
                // tie this new node in as the root of this pattern

                // find the top, leftmost pattern node
                SblPatNode pNode = this
                while (pNode.parent != null) pNode = pNode.parent
                while( pNode.prevNode != null) pNode = pNode.prevNode

                pNode.parent = nNode
                nNode.children << pNode
                while( pNode.nextNode != null) {
                    pNode = pNode.nextNode
                    pNode.parent = nNode
                    nNode.children << pNode
                }

                nNode.compile()
//                nNode.context.registerPattern( nNode.name, nNode)
                nNode.context.matcher = nNode.component.matcher()
                nNode.context.init()
                break
            case CONCATENATE:
            case ALTERNATE:
                // add this node and the value node as children of the newly
                // created node
                def val = args[0]

                // first, look to see if the value is a string.  If so, create a String node for it
                if( val.class == String) {
                    val = builder.StringPat( val)
                } else if( val.class == char) {
                    val = builder.CharPat( val)
                }
                nNode.children += [ this, val]
                break
            case UNEVAL:
                def val = args[0]

                break
            case PENDING:
                def val = args[0]

                break
            default:
                println 'I am in default for patNode missing method'
                this.nextNode = nNode
                nNode.prevNode = this
                nNode.parent = parent
            }
        }

        return nNode
    }

    SblPatNode positive() {
        SblPatNode nNode = builder.Defer( this)
        nNode.context = this.context
        return nNode
    }

    def compile() {
        if( compiled == INPROGRESS) {
            // if this node is already being compiled, then there is a
            // pattern loop somewhere.  Return a deferred reference to this node
            component = Pattern.Defer( new Variable( this.name))
        } else if( compiled == NOTCOMPILED) {
            compiled = INPROGRESS
            switch (type) {
            case ROOT:
                // this is only encountered for the top level ROOT
                // lower level ROOT nodes should be removed
                //
                // step through the vars looking for patNodes - compile them when you find them
                context.patterns.each { name, val ->
                    if( val instanceof SblPatNode) {
                        def res = val.compile()
                        context.registerVariable( name, res)
                    }
                }
                // should only be one child for ROOT
                component = children[0].compile()
                break

            case CONCATENATE:
                // go through and compile the children

                // marking this node as compiled first is done to keep from revisiting
                // this node (as INPROGRESS) in a pattern loop
                children[0].compile()
                children[1].compile()
                component = Pattern.Concat( children[0].component, children[1].component)
                break

            case ALTERNATE:
                // go through and compile the children

                // marking this node as compiled first is done to keep from revisiting
                // this node (as INPROGRESS) in a pattern loop
                children[0].compile()
                children[1].compile()
                component = Pattern.Alternate( children[0].component, children[1].component)
                break

            case UNEVAL:
                // if the child is a patNode, compile it
                if( arg instanceof SblPatNode) {
                    // DO WE NEED TO COPY THIS NODE????
                    arg.compile()
                    context.registerPattern( arg.name, arg.component)
                    component = Pattern.Defer( new Variable( arg.name))
                } else if( arg?.class == String) {
                    component = Pattern.Defer( new Variable( arg))
                }
                break

            case PRIMITIVE:
                // the 'prim' field was populated with appropriate method call when the
                // node was instantiated.  Just have to look to see if there's an argument

                // one wrinkle to note is the argument might be a SblPatNode.  If so,
                // it probably needs to be compiled as well.
                if( arg?.class == SblPatNode) {
                    arg.compile()
                    component = prim.method( arg.component)
                } else {
                    if( arg instanceof SblString) arg = arg.toString()
                    component = (arg == null) ? prim.method() : prim.method( arg)
                }

                break

            default:
                println 'do nothing for ' + this.name
            }

            compiled = COMPILED
        }

        // look to see if there are any assignments.  If so, wrap the newly
        // compiled jpattern in an assignment pattern
        if( _iAssign.active == true) {
            component = Pattern.IAssign( component, Variable.create( _iAssign.target))
        }
        if( _cAssign.active == true) {
            component = Pattern.Assign( component, Variable.create( _cAssign.target))
        }
        if( _cReplace.active == true) {
            component = Pattern.Replace( component, Variable.create( _cReplace.target))
        }

        return component
    }

    // setup the conditional assignment during pattern construction
    SblPatNode ca( String var) {
//        SblPatNode newNode = this.clone()
//        newNode.name = newNode.toString()
//        newNode._cAssign.active = true
//        newNode._cAssign.target = var
//        return newNode
        this._cAssign.active = true
        this._cAssign.target = var
        return this
    }

    // setup the immediate assignment during pattern construction
    SblPatNode ia( String var) {
//        SblPatNode newNode = this.clone()
//        newNode.name = newNode.toString()
//        newNode._iAssign.active = true
//        newNode._iAssign.target = var
//        return newNode
        this._iAssign.active = true
        this._iAssign.target = var
        return this
    }

    // setup the conditional replacement during pattern construction
    SblPatNode cr( String var) {
//        SblPatNode newNode = this.clone()
//        newNode.name = newNode.toString()
//        newNode._cReplace.active = true
//        newNode._cReplace.target = var
//        return newNode
        this._cReplace.active = true
        this._cReplace.target = var
        return this
    }

    SblPatNode clone() {
        SblPatNode newNode = new SblPatNode()
        newNode._iAssign = [:]
        _iAssign.each { k,v -> newNode._iAssign[k] = v}
        newNode._cAssign = [:]
        _cAssign.each { k,v -> newNode._cAssign[k] = v}
        newNode._cReplace = [:]
        _cReplace.each { k,v -> newNode._cReplace[k] = v}
        newNode.type = type
        newNode.prim = prim
        newNode.name = name
        newNode.compiled = compiled
        newNode.component = component
        newNode.arg = arg
        newNode.attribs = attribs

        newNode.builder = builder
        newNode.context = context

        newNode.parent = parent
        newNode.children = children*.clone()
        newNode.nextNode = nextNode
        newNode.prevNode = prevNode
        return newNode
    }

    void copy( SblPatNode src) {
        src._iAssign.each { k,v -> _iAssign[k] = v}
        src._cAssign.each { k,v -> _cAssign[k] = v}
        src._cReplace.each { k,v -> _cReplace[k] = v}
        type = src.type
        prim = src.prim
//        name = src.name
        compiled = src.compiled
        component = src.component
        arg = src.arg
        attribs = src.attribs

        builder = src.builder
        context = src.context

        parent = src.parent
        children = src.children
        nextNode = src.nextNode
        prevNode = src.prevNode
    }

    void deferArgHandling() {
        // if this node does special handling for having a Defer node
        // as an argument, look to see if arg is a Deferred node.
        // If so, create a Variable to hold the arg of the Defer node
        if( handleDefer == true && arg instanceof SblPatNode && arg.prim == DEFER) {
            Variable v = Variable.create(arg.arg)
            // don't forget to register the new Variable
            context.registerVariable(arg.arg, '')
            arg = v
        }
    }

    enum CompiledState { NOTCOMPILED, INPROGRESS, COMPILED }
}
