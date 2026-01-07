package com.kzcse.cms.core.ui
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * Just decorator so that default style and font, theme can manage in single place
 */
@Composable
fun TextView(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        modifier=modifier,
        text=text,
        fontSize=fontSize,
        color=color,
        fontWeight=fontWeight,
        maxLines=maxLines,
        overflow=overflow
    )
}



@Composable
fun SpacerVertical(height:Int)= Spacer(Modifier.height(height.dp))
@Composable
fun SpacerHorizontal(width:Int)= Spacer(Modifier.width(width.dp))
@Composable
fun RowScope.SpacerFillAvailable()= Spacer(Modifier.weight(1f))
@Composable
fun DividerHorizontal(modifier: Modifier = Modifier)=
    HorizontalDivider(modifier.fillMaxWidth(), DividerDefaults.Thickness, DividerDefaults.color)




@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    dashWidth: Dp = 8.dp,
    dashGap: Dp = 8.dp,
    strokeWidth: Dp = 1.dp
) {
    val density = LocalDensity.current
    Canvas(modifier = modifier.height(strokeWidth)) {
        // Convert Dp to Px inside the draw scope
        val dashWidthPx = with(density) { dashWidth.toPx() }
        val dashGapPx = with(density) { dashGap.toPx() }
        val strokeWidthPx = with(density) { strokeWidth.toPx() }

        val totalWidth = size.width
        var startX = 0f

        while (startX < totalWidth) {
            val endX = (startX + dashWidthPx).coerceAtMost(totalWidth)
            drawLine(
                color = color,
                start = Offset(startX, size.height / 2),
                end = Offset(endX, size.height / 2),
                strokeWidth = strokeWidthPx
            )
            startX += dashWidthPx + dashGapPx
        }
    }
}