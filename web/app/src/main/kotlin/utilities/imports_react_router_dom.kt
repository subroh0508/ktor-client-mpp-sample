package utilities

import react.*
import react.router.dom.*
import kotlin.browser.window

external interface ReactRouterDom {
    @JsName("useHistory")
    fun rawUseHistory(): RouteResultHistory
    @JsName("useLocation")
    fun rawUseLocation(): RouteResultLocation
    @JsName("useParams")
    fun rawUseParams(): dynamic
    @JsName("useRouteMatch")
    fun <T : RProps> rawUseRouteMatch(options: dynamic): RouteResultMatch<T>
    @JsName("withRouter")
    fun <T : RProps> rawWithRouter(component: dynamic): RClass<T>
    @Suppress("PropertyName")
    val HashRouter: RClass<RProps>
    @Suppress("PropertyName")
    val BrowserRouter: RClass<RProps>
    @Suppress("PropertyName")
    val Switch: RClass<RProps>
    @Suppress("PropertyName")
    val Route: RClass<dynamic>
    @Suppress("PropertyName")
    val Link: RClass<LinkProps>
    @Suppress("PropertyName")
    val NavLink: RClass<dynamic>
    @Suppress("PropertyName")
    val Redirect : RClass<RedirectProps>
}

private val Module get() = kotlinext.js.require("react-router-dom")

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal val ReactRouterDomModule: ReactRouterDom =
    if (Module != undefined)
        Module as ReactRouterDom
    else
        window.asDynamic().ReactRouterDOM as ReactRouterDom

internal fun hashRouterComponent() = ReactRouterDomModule.HashRouter
internal fun browserRouterComponent() = ReactRouterDomModule.BrowserRouter
internal fun switchComponent() = ReactRouterDomModule.Switch
internal fun <T: RProps> routeComponent() = ReactRouterDomModule.Route as RClass<T>
internal fun linkComponent() = ReactRouterDomModule.Link
internal fun <T: RProps> navLinkComponent() = ReactRouterDomModule.NavLink as RClass<T>
internal fun redirectComponent() = ReactRouterDomModule.Redirect

fun useHistory(): RouteResultHistory = ReactRouterDomModule.rawUseHistory()
fun useLocation(): RouteResultLocation = ReactRouterDomModule.rawUseLocation()

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
