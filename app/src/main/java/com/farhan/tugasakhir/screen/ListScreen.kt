package com.farhan.tugasakhir.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farhan.tugasakhir.components.HeadingToolsTextComponent
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.ListScreenViewModel
import com.farhan.tugasakhir.data.model.Child
import com.farhan.tugasakhir.data.model.GrowthData
import com.farhan.tugasakhir.item.GrowthChart
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ListScreen(listScreenViewModel: ListScreenViewModel = viewModel(), navigateBack: () -> Unit, navigateToPdfViewer: (String) -> Unit) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            listScreenViewModel.startListening(userId)
        }
    }


    val children by listScreenViewModel.children.observeAsState(emptyList())
    val isLoading by listScreenViewModel.isLoading.observeAsState(false)
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = navigateBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = colorScheme.onBackground
                )
            }

            HeadingToolsTextComponent(value = "Daftar Anak")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorScheme.primary)
            }
        } else {
            LazyColumn {
                items(children) { child ->
                    ChildCard(child, listScreenViewModel, navigateToPdfViewer )
                }
            }
        }
    }
}


@Composable
fun ChildCard(child: Child, listScreenViewModel: ListScreenViewModel,  navigateToPdfViewer: (String) -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val lastUpdatedTime = child.lastUpdated?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).format(formatter)
    } ?: "N/A"

    var showChart by rememberSaveable { mutableStateOf(false) }

    var ageInMonths by remember { mutableStateOf("") }
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid
    val colorScheme = MaterialTheme.colorScheme

    val isDarkMode = isSystemInDarkTheme()

    val backgroundColor = if (isDarkMode) {
        MaterialTheme.colorScheme.surface
    } else {
        colorResource(id = R.color.light_blue)
    }

    var expanded by rememberSaveable { mutableStateOf(false) }

    ageInMonths = child.birthDate?.let { calculateAgeInMonths(it).toString() } ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "${child.name}",
                    fontSize = 20.sp,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(R.string.show_more)
                        },
                        tint = colorScheme.onSurface
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    child.gender?.let { ChildInfoText(label = "Jenis Kelamin:", value = it) }
                    child.birthDate?.let { ChildInfoText(label = "Tanggal Lahir:", value = it) }
                    ChildInfoText(label = "Usia:", value = "$ageInMonths Bulan")
                    ChildInfoText(label = "Tinggi/Panjang Badan:", value = "${child.height} cm")
                    ChildInfoText(label = "Berat Badan:", value = "${child.weight} kg")
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Box untuk resultText
                child.result?.let { resultText ->
                    if (resultText.isNotEmpty() && resultText != "Data tidak valid") {
                        val boxColor = when {
                            (resultText.contains("Sangat Pendek (Stunting)") && resultText.contains("Gizi Baik")) -> Color(0xFFFFA500) // Oranye
                            resultText.contains("Sangat Pendek (Stunting)") ||
                                    resultText.contains("Pendek") &&
                                    (resultText.contains("Gizi Buruk") || resultText.contains("Gizi Kurang")) -> Color(0xFFFF0000) // Merah
                            (resultText.contains("Normal") || resultText.contains("Tinggi")) &&
                                    (resultText.contains("Gizi Buruk") || resultText.contains("Gizi Kurang") || resultText.contains("Gizi Lebih")) -> Color(0xFFFFA500) // Oranye
                            (resultText.contains("Normal") || resultText.contains("Tinggi")) && resultText.contains("Gizi Baik") -> Color(0xFF178036) // Hijau
                            else -> Color(0xFFFFFFFF) // default putih
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(color = boxColor, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = resultText,
                                    color = Color.White,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                child.recommendation?.let {
                    Column {
                        Text(
                            text = "Anjuran:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colorScheme.onSurface
                        )
                        BulletList(text = it)
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    val annotatedText = buildAnnotatedString {
                        append("Kemenkes juga sudah menyiapkan panduan bagi para ibu dalam menyiapkan makanan lokal yang kaya dengan protein hewani, yaitu buku Resep Makanan Lokal yang kaya dengan protein hewani bagi Bayi, Balita dan Ibu Hamil, buku tersebut dapat dilihat ")
                        pushStringAnnotation(tag = "URL", annotation = "https://drive.google.com/file/d/1wctAgirlkJoRb5V4KTIt66pDXp5gmq8r/view")
                        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                            append("Disini")
                        }
                        pop()
                    }

                    JustifiedClickableText(
                        text = annotatedText,
                        navigateToPdfViewer = navigateToPdfViewer
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Terakhir Diperbarui: $lastUpdatedTime",
                    fontSize = 16.sp,
                    color = colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        userId?.let { uid ->
                            listScreenViewModel.deleteChild(uid, child, onSuccess = {
                                // Refresh the children list after deletion
                                listScreenViewModel.fetchChildren(uid)
                            }, onFailure = {
                                // Handle the error
                            })
                        }
                    }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = colorScheme.error)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Button(
                    onClick = { showChart = !showChart }

                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        Text(text = if (showChart) "Sembunyikan Grafik" else "Tampilkan Grafik")
                        Icon(
                            imageVector = if (showChart) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (showChart) {
                    GrowthChart(child = child)
                }
            }
        }
    }
}
}

@Composable
fun ChildInfoText(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label ")
            }
            append(value)
        },
        fontSize = 18.sp,
        color = colorScheme.onSurface
    )
}


@Composable
fun JustifiedClickableText(
    text: AnnotatedString,
    navigateToPdfViewer: (String) -> Unit
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    ClickableText(
        text = text,
        style = TextStyle(textAlign = TextAlign.Justify, color = colorScheme.onSurface),
        onClick = { offset ->
            layoutResult.value?.let {
                text.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        navigateToPdfViewer(annotation.item)
                    }
            }
        },
        onTextLayout = { layoutResult.value = it },
        modifier = Modifier.padding(8.dp)
    )
}