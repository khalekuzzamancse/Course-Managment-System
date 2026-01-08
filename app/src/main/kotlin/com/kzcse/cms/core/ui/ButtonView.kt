package com.kzcse.cms.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ButtonView(
    modifier: Modifier = Modifier,
    background: Color = Color.Unspecified,
    label: String,
    enabled: Boolean=true,
    labelStyle: TextStyle= TextStyle(),
    onClick: VoidCallback,
    ) {
    Button(
        onClick = {
            if(enabled)
                onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        modifier = modifier,

    ) {
        Text(text = label, style = labelStyle)
    }
}
@Composable
fun BackIcon(modifier: Modifier = Modifier,onClick: () -> Unit) {
    IconButton(onClick = onClick,modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back"
        )
    }

}
@Composable
fun CloseIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor:Color= MaterialTheme.colorScheme.primary,
    size: Dp = 20.dp,
    borderThickness: Dp = 0.1.dp,
    borderColor: Color = color.contentColor(),
    elevation: Dp = 2.dp
) {
    Surface(
        modifier = modifier
            .size(size)
            .background(
                color = color,
                shape = CircleShape
            )
            .border(
                border = BorderStroke(
                    width = borderThickness,
                    color = borderColor,
                ),
                shape = CircleShape
            )
        ,
        shadowElevation = elevation,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
            ,
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "filter",
                tint = iconColor
            )
        }

    }

}