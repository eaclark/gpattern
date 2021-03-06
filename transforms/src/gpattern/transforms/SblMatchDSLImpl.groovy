package gpattern.transforms

/**
 * Created by eac on 3/11/14.
 */

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.EmptyStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.transform.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.syntax.Types

/**
 * This ASTTransformation
 *
 * @author Ed Clark
 */
@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
public class SblMatchDSLImpl implements ASTTransformation {

    def withVisitor = createWithVisitor()
    def innerVisitor = createInnerVisitor()

    @Override
    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        ModuleNode moduleNode = sourceUnit.getAST()
        def classes = moduleNode.getClasses()
        ClassNode clazz = classes[0]
        ClassNode cn

        clazz.visitContents( new ClassCodeVisitorSupport() {
            public void visitMethodCallExpression( MethodCallExpression mce) {
                // a test for proper annotation
                def testSblAnnotation = { a -> a.getClassNode().getNameWithoutPackage() == 'SblMatchContextMarker' }

                // get the class node that defined this method
                cn = mce.getReceiver().getType()

                // get its class annotations
                AnnotationNode[] cna = cn.getAnnotations()

                // get the class methods with this method name
                AnnotatedNode[] mns = cn.getDeclaredMethods( mce.method.text)

                if( cna.any( testSblAnnotation) &&                                       // target class is annotated properly
                    mns.every { m -> m.getAnnotations().any( testSblAnnotation) }        // target method(s) is annotated properly
                  ) {
                    mce.arguments.eachWithIndex{ a, i ->
                        if( a.class == ClosureExpression) {
                            a.visit( withVisitor)
                        }
                    }
                } else super.visitMethodCallExpression( mce)
            }

            protected SourceUnit getSourceUnit() { unit }
        })
    }

    private CodeVisitorSupport createWithVisitor() {
        // this is for visiting the top level with statements

        new CodeVisitorSupport() {
            @Override
            void visitBlockStatement(BlockStatement blockStatement) {
                blockStatement.statements.eachWithIndex { stmt, idx ->
                    IfStatement ifStatement

                    if (stmt instanceof ExpressionStatement) {
                        if ( stmt.expression instanceof BinaryExpression &&
                            !(stmt.expression instanceof DeclarationExpression) ) {
                            BinaryExpression expr = (BinaryExpression) stmt.expression
                            Expression lhs = expr.leftExpression
                            Token oper = expr.operation
                            Expression rhs = expr.rightExpression

                            if ( lhs instanceof BinaryExpression &&
                                 oper.text == '='
                               ) {
                                Expression left_lhs = lhs.leftExpression
                                Token left_oper = lhs.operation
                                Expression left_rhs = lhs.rightExpression

                                if ( left_oper.text == '[') {
                                    ifStatement = createIfStatement( 'it', lhs, left_lhs, rhs)
                                    blockStatement.statements[idx] = ifStatement
                                }

                            }
                        }
                    }
                }

                super.visitBlockStatement( blockStatement)
            }

            @Override
            void visitBinaryExpression( BinaryExpression be) {
                /* This handles the case that a pattern creation/assignment is just a String
                 *
                 * There are two cases for this.
                 *
                 * 1) assignment of a string
                 *    pat = 'asdf'
                 *
                 * 2) substring access
                 *    sbStr[ 'asdf' ]
                 *
                 * The method also handles a combination of the two cases:
                 *    sbStr[ 'asdf' ] = 'fdsa'
                 */

                Expression lhs = be.leftExpression
                Expression rhs = be.rightExpression
//                println '\n\n---------------- in binary -------------'
//                println 'oper = ' + be.operation.text
//                println '\nlhs = ' + lhs.text + '       class = ' + lhs.class
//                println '\nrhs = ' + rhs.text + '       class = ' + rhs.class

                if( be.operation.type == Types.ASSIGN ||
                    be.operation.type == Types.PLUS ||
                    be.operation.type == Types.BITWISE_OR ||
                    be.operation.type == Types.LEFT_SQUARE_BRACKET ) {

                    // have to check for a few things at this level because this is where we can
                    // replace the expression.  Can't do it at a lower level
                    // 1) ~'string'  (BitwiseNegation on a String)
                    // 2) 'string'.ca('asdf')  ('ca', 'ia', or 'cr' MethodCall on a String)

                    // start with the RHS
                    if( rhs instanceof BitwiseNegationExpression &&
                        ( (rhs.getExpression() instanceof ConstantExpression &&
                           rhs.getExpression().value instanceof String) ||
                          rhs.getExpression() instanceof GStringExpression)) {
                            be.rightExpression = new BitwiseNegationExpression( createStringPat( rhs.getExpression()))
                    } else if( rhs instanceof MethodCallExpression &&
                               ( (rhs.getReceiver() instanceof ConstantExpression &&
                                  rhs.getReceiver().value instanceof String) ||
                                 rhs.getReceiver() instanceof GStringExpression) &&
                               rhs.methodAsString in ['ca', 'ia', 'cr']) {
                            be.rightExpression = new MethodCallExpression(
                                createStringPat( rhs.getReceiver()),
                                new ConstantExpression( rhs.methodAsString),
                                rhs.getArguments())
//        println '\n  ---> new rhs = ' + be.rightExpression
                    } else rhs.visit( this)

                    // now visit the LHS
                    if( lhs instanceof BitwiseNegationExpression &&
                        ( (lhs.getExpression() instanceof ConstantExpression &&
                           lhs.getExpression().value instanceof String) ||
                          lhs.getExpression() instanceof GStringExpression)) {
                            be.leftExpression = new BitwiseNegationExpression( createStringPat( lhs.getExpression()))
                    } else if( lhs instanceof MethodCallExpression &&
                               ( ( lhs.getReceiver() instanceof ConstantExpression &&
                                   lhs.getReceiver().value instanceof String) ||
                                 lhs.getReceiver() instanceof GStringExpression) &&
                               lhs.methodAsString in ['ca', 'ia', 'cr']) {
                            be.leftExpression = new MethodCallExpression(
                                createStringPat( lhs.getReceiver()),
                                new ConstantExpression( lhs.methodAsString),
                                lhs.getArguments())
//        println '\n  ---> new lhs = ' + be.leftExpression
                    } else lhs.visit( this)
                } else super.visitBinaryExpression( be)
            }

            @Override
            void visitMethodCallExpression( MethodCallExpression mce) {
                /*
                 *  Handle the case of
                 *       SblString.putAt( ~'asdf', something)
                 *  and
                 *       SblString.getAt( ~'asdf')
                 */
//                println '\n\n***************** in mce *************'
//                println '\nmethod = ' + mce.method
//                println '\nreceiver = ' + mce.receiver + '       class = ' + mce.receiver.class
//                println '\nparams = ' + mce.getArguments().getExpressions()
                if( mce.getReceiver().getType().getNameWithoutPackage() == 'SblString' &&
                    (mce.getMethodAsString() == 'putAt' || mce.getMethodAsString() == 'getAt')
                ) {
                    // if the first argument is a ~'string', then do the xform here
                    // otherwise, visit the argument
                    List argList = mce.getArguments().getExpressions()
                    def arg = argList[0]
                    if( arg instanceof BitwiseNegationExpression &&
                        ( (arg.getExpression() instanceof ConstantExpression &&
                           arg.getExpression().value instanceof String) ||
                          arg.getExpression() instanceof GStringExpression)) {
                        // rebuild the argument list --- replacing the first argument
                        argList[0] = new BitwiseNegationExpression( createStringPat( arg.getExpression()))
                        mce.setArguments( new ArgumentListExpression( argList))
                    } else mce.getArguments().visit( this)
                } else super.visitMethodCallExpression( mce)
            }

            @Override
            void visitBitwiseNegationExpression(BitwiseNegationExpression bne) {
//                println '\n\n---------------- in unary -------------'
//                println 'expr = ' + bne.expression.text

                bne.getExpression().visit( innerVisitor)
            }
        }
    }

    private CodeVisitorSupport createInnerVisitor() {
        // this is for visiting complex patterns within a unary pattern expression
        new CodeVisitorSupport() {
            @Override
            void visitBinaryExpression(BinaryExpression be) {
                Expression lhs = be.leftExpression
                Expression rhs = be.rightExpression
//                println '\ninside unary, binary oper = ' + be.operation.text
//                println '\ninside unary, binary lhs = ' + lhs.text + '       class = ' + lhs.class
//                println '\ninside unary, binary rhs = ' + rhs.text + '       class = ' + rhs.class

                if( be.operation.type == Types.PLUS || be.operation.type == Types.BITWISE_OR) {
                    if( ( lhs instanceof ConstantExpression && lhs.value instanceof String) ||
                        lhs instanceof GStringExpression) {
                        be.leftExpression = createStringPat( lhs)
                    } else
                    if( lhs instanceof MethodCallExpression &&
                        ( (lhs.getReceiver() instanceof ConstantExpression &&
                           lhs.getReceiver().value instanceof String) ||
                          lhs.getReceiver() instanceof GStringExpression) &&
                        lhs.methodAsString in ['ca', 'ia', 'cr']) {
                        be.leftExpression = new MethodCallExpression(
                                createStringPat( lhs.getReceiver()),
                                new ConstantExpression( lhs.methodAsString),
                                lhs.getArguments())
//        println '\n  ---> new lhs = ' + be.leftExpression
                    } else {
                        lhs.visit( innerVisitor)
                    }

                    if( ( rhs instanceof ConstantExpression && rhs.value instanceof String) ||
                        rhs instanceof GStringExpression) {
                        be.rightExpression = createStringPat( rhs)
                    } else
                    if( rhs instanceof MethodCallExpression &&
                        (( rhs.getReceiver() instanceof ConstantExpression &&
                           rhs.getReceiver().value instanceof String) ||
                           rhs.getReceiver() instanceof GStringExpression) &&
                        rhs.methodAsString in ['ca', 'ia', 'cr']) {
                        be.rightExpression = new MethodCallExpression(
                                createStringPat( rhs.getReceiver()),
                                new ConstantExpression( rhs.methodAsString),
                                rhs.getArguments())
//        println '\n  ---> new rhs = ' + be.rightExpression
                    }  else {
                        rhs.visit( innerVisitor)
                    }
                } else {
                    println 'other operator = ' + be.operation.text
                    println 'other operator type = ' + be.operation.type
                    println 'Types.ASSIGN = ' + Types.ASSIGN
                    println "GO THE OTHER WAY"
                    super.visitBinaryExpression( be)
                }
            }

            @Override
            void visitMethodCallExpression( MethodCallExpression mce) {
                /*
                 *  Handle the case of
                 *       SblString.putAt( ~'asdf', something)
                 *  and
                 *       SblString.getAt( ~'asdf')
                 *
                 *  Also handle 'string'.cr('fdsa')
                 */
//                println '\ninside unary, method = ' + mce.method
//                println '\ninside unary, receiver = ' + mce.receiver + '       class = ' + mce.receiver.class
//                println '\ninside unary, params = ' + mce.getArguments().getExpressions()
                if( mce.getReceiver().getType().getNameWithoutPackage() == 'SblString' &&
                    ( mce.getMethodAsString() == 'putAt' || mce.getMethodAsString() == 'getAt')
                ) {
                    // if the first argument is a ~'string', then do the xform here
                    // otherwise, visit the argument
                    List argList = mce.getArguments().getExpressions()
                    def arg = argList[0]
                    if( ( arg.getExpression() instanceof ConstantExpression &&
                          arg.getExpression().value instanceof String) ||
                        arg.getExpression() instanceof GStringExpression) {
                        // rebuild the argument list --- replacing the first argument
                        argList[0] = new BitwiseNegationExpression(createStringPat(arg.getExpression()))
                        mce.setArguments(new ArgumentListExpression(argList))
                    } else mce.getArguments().visit(innerVisitor)
                } else super.visitMethodCallExpression( mce)
            }
        }
    }


    private static IfStatement createIfStatement( ctx, subjectMatch, subject, replacement_str) {
        // this changes the code "subj[ pat] = replacement_str"
        // to " if ( subj[ pat]) subj[ it ] = replacement_str"
        return new IfStatement( new BooleanExpression( subjectMatch),
                                new ExpressionStatement( new BinaryExpression( new BinaryExpression( subject,
                                                                                                     Token.newSymbol( Types.LEFT_SQUARE_BRACKET, 0, 0),
                                                                                                     new VariableExpression( 'it')
                                                                                                    ),
                                                                               Token.newSymbol(Types.EQUALS, 0, 0),
                                                                               replacement_str
                                                                             )
                                                       ),
                                new EmptyStatement()
                              )
    }

    private static MethodCallExpression createStringPat( ASTNode value) {
//println 'creating stringPat with text = ' + value.text
//println 'for expression class = ' + value.class
        def call = new MethodCallExpression(
                new VariableExpression( "this"),
                new ConstantExpression( "StringPat"),
                new ArgumentListExpression( value as Expression)
        )
//        call.implicitThis = true
//        call.safe = exp.safe
//        call.spreadSafe = exp.spreadSafe
//        call.methodTarget = METHOD_MISSING
        return call
    }
}