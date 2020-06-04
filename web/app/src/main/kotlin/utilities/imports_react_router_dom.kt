package utilities

import react.*
import react.router.dom.*
import kotlin.browser.window

external interface ReactRouterDom {
    fun useHistory(): RouteResultHistory
    fun useLocation(): RouteResultLocation
    fun useParams(): dynamic
    fun <T : RProps> useRouteMatch(options: dynamic): RouteResultMatch<T>
    fun <T : RProps> withRouter(component: dynamic): RClass<T>
    val HashRouter: RClass<RProps>
    val BrowserRouter: RClass<RProps>
    val Switch: RClass<RProps>
    val Route: RClass<dynamic>
    val Link: RClass<LinkProps>
    val NavLink: RClass<dynamic>
    val Redirect : RClass<RedirectProps>
}

@JsModule("react-router-dom")
external val ReactRouterDomModule: ReactRouterDom

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal val Module: ReactRouterDom =
    if (ReactRouterDomModule != undefined)
        ReactRouterDomModule
    else
        window.asDynamic().ReactRouterDOM as ReactRouterDom

internal fun hashRouterComponent() = Module.HashRouter
internal fun browserRouterComponent() = Module.BrowserRouter
internal fun switchComponent() = Module.Switch
internal fun <T: RProps> routeComponent() = Module.Route as RClass<T>
internal fun linkComponent() = Module.Link
internal fun <T: RProps> navLinkComponent() = Module.NavLink as RClass<T>
internal fun redirectComponent() = Module.Redirect

fun useHistory(): RouteResultHistory = Module.useHistory()
fun useLocation(): RouteResultLocation = Module.useLocation()

fun RBuilder.browserRouter(handler: RHandler<RProps>) = browserRouterComponent()(handler)

fun RBuilder.switch(handler: RHandler<RProps>) = switchComponent()(handler)

fun RBuilder.route(
    path: String,
    exact: Boolean = false,
    strict: Boolean = false,
    render: () -> ReactElement?
): ReactElement {
    return (routeComponent<RouteProps<RProps>>()) {
        attrs {
            this.path = path
            this.exact = exact
            this.strict = strict
            this.render = { render() }
        }
    }
}
