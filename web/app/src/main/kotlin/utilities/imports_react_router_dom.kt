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

fun useHistory(): RouteResultHistory = ReactRouterDomModule.useHistory()
fun useLocation(): RouteResultLocation = ReactRouterDomModule.useLocation()

fun RBuilder.browserRouter(handler: RHandler<RProps>) = ReactRouterDomModule.BrowserRouter(handler)

fun RBuilder.switch(handler: RHandler<RProps>) = ReactRouterDomModule.Switch(handler)

fun RBuilder.route(
    path: String,
    exact: Boolean = false,
    strict: Boolean = false,
    render: () -> ReactElement?
): ReactElement {
    return (ReactRouterDomModule.Route as RClass<RouteProps<RProps>>) {
        attrs {
            this.path = path
            this.exact = exact
            this.strict = strict
            this.render = { render() }
        }
    }
}
