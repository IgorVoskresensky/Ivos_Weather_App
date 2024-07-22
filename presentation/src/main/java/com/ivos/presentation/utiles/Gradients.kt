package com.ivos.presentation.utiles

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.ivos.presentation.ui.theme.night_gradient_first_color
import com.ivos.presentation.ui.theme.night_gradient_fourth_color
import com.ivos.presentation.ui.theme.night_gradient_second_color
import com.ivos.presentation.ui.theme.night_gradient_third_color

data class Gradient(
    val primary: Brush,
    val secondary: Brush,
    val shadow: Color,
) {
    constructor(
        firstColor: Color,
        secondColor: Color,
        thirdColor: Color,
        fourthColor: Color,
    ) : this (
        primary = Brush.linearGradient(listOf(firstColor, secondColor)),
        secondary = Brush.linearGradient(listOf(thirdColor, fourthColor)),
        shadow = firstColor
    )
}

object Gradients {
    val night = Gradient(
        firstColor = night_gradient_first_color,
        secondColor = night_gradient_second_color,
        thirdColor = night_gradient_third_color,
        fourthColor = night_gradient_fourth_color,
    )

    //todo add morning, day and evening
}
