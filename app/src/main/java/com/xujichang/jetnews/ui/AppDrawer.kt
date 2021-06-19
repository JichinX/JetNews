package com.xujichang.jetnews.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xujichang.jetnews.R
import com.xujichang.jetnews.res.JetNewsTheme

@Composable
fun AppDrawer(
    currentRoute: String,
    navigationToHome: () -> Unit,
    navigationToInterests: () -> Unit,
    closeDrawer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))
        JetNewsLogo(Modifier.padding(16.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        DrawerButton(
            icon = Icons.Filled.Home,
            label = "Home",
            isSelected = currentRoute == MainDestinations.HOME_ROUTE,
            action = { navigationToHome();closeDrawer() }
        )
        DrawerButton(
            icon = Icons.Filled.ListAlt,
            label = "Interests",
            isSelected = currentRoute == MainDestinations.INTERESTS_ROUTE,
            action = { navigationToInterests();closeDrawer() }
        )
    }
}

@Composable
fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) 1f else .6f
    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = .6f)
    val backgroundColor = if (isSelected) colors.primary.copy(alpha = .12f) else Color.Transparent
    val surfaceModifier = modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)

    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(onClick = action, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Composable
fun JetNewsLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.ic_jetnews_logo),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_jetnews_wordmark),
            contentDescription = stringResource(id = R.string.app_name),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
    }
}

@Preview("App Drawer")
@Preview("App Drawer Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    JetNewsTheme {
        Surface {
            AppDrawer(
                currentRoute = MainDestinations.HOME_ROUTE,
                navigationToHome = {},
                navigationToInterests = { }) {
            }
        }
    }
}
