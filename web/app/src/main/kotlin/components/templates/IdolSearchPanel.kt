package components.templates

import components.atoms.*
import components.organisms.idolColorGridsActions
import components.organisms.idolColorGrids
import components.organisms.idolSearchBox
import kotlinx.css.*
import kotlinx.css.properties.borderTop
import materialui.components.drawer.enums.DrawerAnchor
import materialui.styles.breakpoint.Breakpoint
import materialui.styles.breakpoint.up
import materialui.styles.makeStyles
import materialui.styles.mixins.toolbar
import materialui.styles.palette.divider
import materialui.styles.palette.paper
import materialui.useMediaQuery
import net.subroh0508.colormaster.model.IdolColor
import net.subroh0508.colormaster.model.Titles
import net.subroh0508.colormaster.model.Types
import net.subroh0508.colormaster.model.UiModel
import net.subroh0508.colormaster.model.ui.idol.Filters
import react.*
import react.dom.div

fun RBuilder.idolSearchPanel(handler: RHandler<IdolSearchPanelProps>) = child(IdolSearchPanelComponent, handler = handler)

private val IdolSearchPanelComponent = functionalComponent<IdolSearchPanelProps> { props ->
    val classes = useStyles()
    val uiModel = props.model

    val drawerAnchor = if (useMediaQuery("@media (min-width: 600px)")) DrawerAnchor.right else DrawerAnchor.bottom
    val actionsStyle = "${classes.actions} ${if (props.isOpenedGrids) "" else classes.actionsHide}"

    div(classes.root) {
        div {
            div(classes.toolbar) {}
            div(classes.searchBoxTop) {}
            idolSearchBox {
                attrs.filters = uiModel.filters
                attrs.onChangeIdolName = props.onChangeIdolName
                attrs.onSelectTitle = props.onSelectTitle
                attrs.onSelectType = props.onSelectType
            }
        }

        responsiveDrawer {
            attrs.anchor = drawerAnchor
            attrs.opened = props.isOpenedGrids
            attrs.onClose = props.onCloseGrids
            attrs.onClickExpandIcon = { props.onClickOpenGrids() }

            div(classes.panel) {
                alert(uiModel)
                idolColorGrids {
                    attrs.items = uiModel.items
                    attrs.onClick = props.onClickIdolColor
                    attrs.onDoubleClick = props.onDoubleClickIdolColor
                }
            }

            div(actionsStyle) {
                div(classes.toolbar) {}
                idolColorGridsActions {
                    attrs.selected = uiModel.selected
                    attrs.onClickPreview = props.onClickPreview
                    attrs.onClickPenlight = props.onClickPenlight
                    attrs.onClickSelectAll = props.onClickSelectAll
                }
            }
        }
    }
}

private fun RBuilder.alert(uiModel: UiModel.Search) = when {
    uiModel.isLoading -> warningAlert {
        attrs.message = "検索中..."
    }
    uiModel.filters is Filters.Empty -> infoAlert {
        attrs.message = "ランダムに10人のアイドルを表示しています"
    }
    uiModel.error != null -> errorAlert {
        attrs.title = "エラー"
        attrs.message = uiModel.error?.message ?: ""
    }
    else -> successAlert {
        attrs.message = "検索結果: ${uiModel.items.size}件"
    }
}

external interface IdolSearchPanelProps : RProps {
    var model: UiModel.Search
    var isOpenedGrids: Boolean
    var onClickOpenGrids: () -> Unit
    var onChangeIdolName: (String) -> Unit
    var onSelectTitle: (Titles, Boolean) -> Unit
    var onSelectType: (Types, Boolean) -> Unit
    var onClickIdolColor: (IdolColor, Boolean) -> Unit
    var onDoubleClickIdolColor: (IdolColor) -> Unit
    var onClickPreview: () -> Unit
    var onClickPenlight: () -> Unit
    var onClickSelectAll: (Boolean) -> Unit
    var onCloseGrids: () -> Unit
}

private external interface IdolSearchPanelStyle {
    val root: String
    val panel: String
    val toolbar: String
    val actions: String
    val actionsHide: String
    val searchBoxTop: String
}

private val useStyles = makeStyles<IdolSearchPanelStyle> {
    "root" {
        display = Display.flex
    }
    "panel" {
        flexGrow = 1.0

        (theme.breakpoints.up(Breakpoint.sm)) {
            marginRight = -DRAWER_WIDTH_SM_UP
        }
    }
    "actions" {
        position = Position.fixed
        zIndex = theme.zIndex.drawer.toInt() + 1
        left = 0.px

        (theme.breakpoints.up(Breakpoint.xs)) {
            right = 0.px
            bottom = 0.px
        }

        (theme.breakpoints.up(Breakpoint.sm)) {
            top = 0.px
        }

        children("div") {
            lastChild {
                padding(8.px, 4.px)
                backgroundColor = theme.palette.background.paper
                borderTop(1.px, BorderStyle.solid, theme.palette.divider)
                borderRadius = 0.px

                (theme.breakpoints.up(Breakpoint.sm)) {
                    width = APP_BAR_SM_UP
                    padding(8.px, 16.px)
                }
            }
        }
    }
    "actionsHide" {
        display = Display.none
    }
    "toolbar"(theme.mixins.toolbar)
    "searchBoxTop" {
        height = 0.px

        (theme.breakpoints.up(Breakpoint.sm)) {
            height = 48.px
        }
    }
}
