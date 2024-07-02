package com.farhan.tugasakhir.item


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.farhan.tugasakhir.data.model.Child
import co.yml.charts.ui.linechart.LineChart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GrowthChart(child: Child) {
    val pointsData = remember { mutableStateListOf<co.yml.charts.common.model.Point>() }
    val dateLabels = remember { mutableStateListOf<String>() }
    val isDarkMode = isSystemInDarkTheme()
    val orangeColor = Color(0xFFCC6600)
    val valueTextColor = if (isDarkMode) Color.White else Color.Black

    LaunchedEffect(child) {
        pointsData.clear()
        dateLabels.clear()
        child.growthData?.forEachIndexed { index, growth ->
            pointsData.add(
                co.yml.charts.common.model.Point(
                    index.toFloat(),
                    growth.height.toFloat()
                )
            )
            dateLabels.add(SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date(growth.date)))
        }
    }

    if (pointsData.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No data available", color = valueTextColor, modifier = Modifier.padding(16.dp))
        }
    } else {
        val steps = 5
        val xAxisStepSize = if (pointsData.size > 1) (300.dp - 40.dp) / (pointsData.size - 1) else 300.dp

        val xAxisData = AxisData.Builder()
            .axisStepSize(xAxisStepSize)
            .backgroundColor(Color.Transparent)
            .steps(pointsData.size - 1)
            .labelData { i -> dateLabels.getOrNull(i) ?: "" }
            .labelAndAxisLinePadding(15.dp)
            .axisLineColor(valueTextColor)
            .axisLabelColor(valueTextColor)
            .endPadding(50.dp)
            .axisLabelFontSize(10.sp)
            .axisLabelAngle(15f)
//            .startDrawPadding(20.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yMin = pointsData.minOf { it.y }
                val yMax = pointsData.maxOf { it.y }
                val yScale = (yMax - yMin) / steps
                ((i * yScale) + yMin).toString()
            }
            .axisLineColor(valueTextColor)
            .axisLabelColor(valueTextColor)
            .axisLabelFontSize(10.sp)
            .axisOffset(20.dp)
            .build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsData,
                        LineStyle(
                            color = orangeColor,
                            lineType = LineType.SmoothCurve(isDotted = false)
                        ),
                        IntersectionPoint(
                            color = orangeColor,
                            radius = 5.dp,
                            alpha = 1f,
                            colorFilter = null,
                            blendMode = androidx.compose.ui.graphics.BlendMode.SrcOver,
                            draw = {
                                drawCircle(
                                    color = orangeColor,
                                    radius = 5.dp.toPx(),
                                    center = it
                                )
                            }
                        ),
                        SelectionHighlightPoint(color = orangeColor),
                        ShadowUnderLine(
                            alpha = 0.5f,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    orangeColor.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        ),
                        SelectionHighlightPopUp()
                    )
                ),
            ),
            backgroundColor = Color.Transparent,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant),
            containerPaddingEnd = 30.dp,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp)),
                lineChartData = lineChartData
            )

            // Legend
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(orangeColor, shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tinggi Badan",
                    color = valueTextColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}
