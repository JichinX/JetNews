package com.xujichang.jetnews.res

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable


private val JetNewsShapes = Shapes()
private val JetNewsTypography = Typography()
private val DarkThemeColors = darkColors()
private val LightThemeColors = lightColors()

@Composable
fun JetNewsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) {
            DarkThemeColors
        } else {
            LightThemeColors
        },
        typography = JetNewsTypography,
        shapes = JetNewsShapes,
        content = content
    )
}