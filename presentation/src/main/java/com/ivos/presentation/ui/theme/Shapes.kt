package com.ivos.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val Shapes
    @Composable get() = MaterialTheme.shapes.copy(
        medium = RoundedCornerShape(16.dp)
    )
