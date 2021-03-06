package net.subroh0508.colormaster.androidapp.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.OutlinedButton as ComposeOutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import net.subroh0508.colormaster.androidapp.R
import net.subroh0508.colormaster.androidapp.themes.ColorMasterTheme
import net.subroh0508.colormaster.androidapp.themes.darkBackground
import net.subroh0508.colormaster.androidapp.themes.lightBackground

@Composable
fun OutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.small,
) = ComposeOutlinedButton(
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colors.onSurface,
    ),
    shape = shape,
    contentPadding = PaddingValues(
        start = (if (painter == null) 16 else 12).dp,
        end = 16.dp,
    ),
    modifier = Modifier.height(36.dp).then(modifier),
) {
    if (painter != null) {
        Icon(
            painter,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
                .align(Alignment.CenterVertically),
        )
    }

    Text(
        label,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.align(Alignment.CenterVertically),
    )
}

@Preview
@Composable
fun PreviewOutlinedButton_Light() {
    ColorMasterTheme(darkTheme = false) {
        Column(Modifier.background(color = lightBackground)) {
            OutlinedButton(
                "Sample Text",
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                painter = painterResource(R.drawable.ic_highlight_24dp),
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                enabled = false,
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                painter = painterResource(R.drawable.ic_highlight_24dp),
                enabled = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
fun PreviewOutlinedButton_Dark() {
    ColorMasterTheme(darkTheme = true) {
        Column(Modifier.background(color = darkBackground)) {
            OutlinedButton(
                "Sample Text",
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                painter = painterResource(R.drawable.ic_highlight_24dp),
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                enabled = false,
                onClick = {},
            )
            OutlinedButton(
                "Sample Text",
                painter = painterResource(R.drawable.ic_highlight_24dp),
                enabled = false,
                onClick = {},
            )
        }
    }
}

