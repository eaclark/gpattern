package gpattern.transforms

/**
 * Created by eac on 3/11/14.
 */

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * <p>This annotation can be used to mark that a closure used as the
 * parameter to a 'with' method call will contain SblMatch DSL
 * statements.  The global SblMatchSDLImpl transform will only be
 * applied if this annotation is present.</p>
 *
 *
 * @author Ed Clark
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE])
public @interface SblMatchContextMarker {
}
