package com.farhan.tugasakhir.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farhan.tugasakhir.data.model.Standard

@Composable
fun TableItem(standards: List<Standard>) {
    val colorScheme = MaterialTheme.colorScheme
    val textStyle = TextStyle(
        color = colorScheme.onBackground,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TableCell("Age (Months)", Modifier.weight(1f), textStyle)
            TableCell("Median Height", Modifier.weight(1f), textStyle)
            TableCell("Std Dev Height", Modifier.weight(1f), textStyle)
            TableCell("Median Weight", Modifier.weight(1f), textStyle)
            TableCell("Std Dev Weight", Modifier.weight(1f), textStyle)
        }
        standards.forEach { standard ->
            StandardRow(standard, textStyle)
            Divider(color = colorScheme.onBackground)
        }
    }
}

@Composable
fun StandardRow(standard: Standard, textStyle: TextStyle) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TableCell(standard.ageInMonths.toString(), Modifier.weight(1f), textStyle)
        TableCell(standard.medianHeight.toString(), Modifier.weight(1f), textStyle)
        TableCell(standard.stdDevHeight.toString(), Modifier.weight(1f), textStyle)
        TableCell(standard.medianWeight.toString(), Modifier.weight(1f), textStyle)
        TableCell(standard.stdDevWeight.toString(), Modifier.weight(1f), textStyle)
    }
}

@Composable
fun TableCell(text: String, modifier: Modifier = Modifier, textStyle: TextStyle) {
    BasicText(
        text = text,
        modifier = modifier.padding(8.dp),
        style = textStyle
    )
}

