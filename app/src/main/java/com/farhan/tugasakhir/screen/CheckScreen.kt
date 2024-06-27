package com.farhan.tugasakhir.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farhan.tugasakhir.components.HeadingToolsTextComponent
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.CheckScreenViewModel
import com.farhan.tugasakhir.viewmodel.ListScreenViewModel
import com.farhan.tugasakhir.data.model.Child
import com.farhan.tugasakhir.data.model.GrowthData
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckScreen(
    checkScreenViewModel: CheckScreenViewModel = viewModel(),
    listScreenViewModel: ListScreenViewModel = viewModel(),
    navigateBack: () -> Unit
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    if (userId != null) {
        listScreenViewModel.fetchChildren(userId)
    }

    val children by listScreenViewModel.children.observeAsState(emptyList())
    val isLoading by listScreenViewModel.isLoading.observeAsState(false)

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var ageInMonths by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var recommendationText by remember { mutableStateOf("") }

    val context = LocalContext.current

    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = navigateBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = colorScheme.onBackground
                        )
                    }

                    HeadingToolsTextComponent(value = "Periksa Anak")
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Text(
                        text = "Ingat! Pastikan Anda mengukur tinggi/panjang anak dengan benar dan menimbang anak dengan timbangan yang akurat.\n\nPemeriksaan hanya valid untuk anak 0-60 Bulan\n\nStatus Gizi yang digunakan Berdasarkan Indeks Berat Badan menurut umur (BB/U) dan Tinggi Badan menurut umur (TB/U) ",
                        color = colorScheme.onSurface,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(13.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var boxWidth by remember { mutableStateOf(0) }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { expanded = !expanded }
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .onGloballyPositioned { coordinates ->
                                        boxWidth = coordinates.size.width
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = selectedChild?.name ?: "Pilih Anak",
                                        color = if (selectedChild == null) Color.Gray else colorScheme.onSurface
                                    )
                                    Icon(
                                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { boxWidth.toDp() })
                                        .background(colorScheme.surface) // Set dropdown background
                                ) {
                                    children.forEach { child ->
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedChild = child
                                                ageInMonths = child.birthDate?.let { calculateAgeInMonths(it).toString() } ?: ""
                                                gender = child.gender ?: ""
                                                height = if (child.height != 0.0) child.height.toString() else ""
                                                weight = if (child.weight != 0.0) child.weight.toString() else ""
                                                expanded = false
                                            },
                                            modifier = Modifier.background(colorScheme.surface) // Set item background
                                        ) {
                                            Text(
                                                text = child.name ?: "",
                                                color = colorScheme.onSurface // Set item text color
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = ageInMonths,
                                maxLines = 1,
                                onValueChange = { ageInMonths = it },
                                label = { Text("Umur (bulan)") },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(colorScheme.surface)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = height,
                                maxLines = 1,
                                onValueChange = { height = it },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                label = { Text("Tinggi/Panjang Badan (cm)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(colorScheme.surface)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = weight,
                                maxLines = 1,
                                onValueChange = { weight = it },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                label = { Text("Berat Badan (kg)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(colorScheme.surface)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val age = ageInMonths.toIntOrNull() ?: 0
                        val heightValue = height.toDoubleOrNull() ?: Double.NaN
                        val weightValue = weight.toDoubleOrNull() ?: Double.NaN

                        if (height.isBlank() || weight.isBlank() || heightValue.isNaN() || weightValue.isNaN() || age <= 0) {
                            resultText = "Data tidak valid"
                            recommendationText = ""
                        } else {
                            resultText = checkScreenViewModel.classifyChild(age, gender, heightValue, weightValue)

                            if (resultText != "Data tidak valid") {
                                // Set recommendation based on nutritional status
                                recommendationText = when {
                                    age in 0..5 -> context.getString(R.string.recommendation_below_6_months)
                                    resultText.contains("Gizi Buruk") -> context.getString(R.string.gizi_buruk_anjuran)
                                    resultText.contains("Gizi Lebih (Obesitas)") -> context.getString(R.string.gizi_lebih_anjuran)
                                    resultText.contains("Gizi Kurang") -> context.getString(R.string.gizi_kurang_anjuran)
                                    else -> context.getString(R.string.gizi_baik_anjuran)
                                }

                                // Additional recommendation based on age
                                val ageRecommendationText = when (age) {
                                    in 6..8 -> context.getString(R.string.recommendation_6_8_months)
                                    in 9..11 -> context.getString(R.string.recommendation_9_11_months)
                                    in 12..23 -> context.getString(R.string.recommendation_12_23_months)
                                    in 24..60 -> context.getString(R.string.recommendation_2_5_years)
                                    else -> ""
                                }

                                // Combine both recommendations if applicable
                                if (ageRecommendationText.isNotEmpty()) {
                                    recommendationText += "\n\n" + ageRecommendationText
                                }

                                val growthData = GrowthData(date = System.currentTimeMillis(), height = heightValue)


                                selectedChild?.let { child ->
                                    child.height = heightValue
                                    child.weight = weightValue
                                    child.result = resultText
                                    child.recommendation = recommendationText

                                    userId?.let { uid ->
                                        listScreenViewModel.updateChildData(uid, child, onSuccess = {
                                            listScreenViewModel.fetchChildren(uid)
                                        }, onFailure = {
                                        })
                                        listScreenViewModel.updateGrowthData(uid, child, growthData, onSuccess = {
                                            // Handle success
                                        }, onFailure = {
                                            // Handle failure
                                        })
                                    }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue)),
                    shape = RoundedCornerShape(16.dp), // Set the corner radius here
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        .height(56.dp)
                ) {
                    Text("Cek Status",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

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
                    Card(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Text(
                            text = "Anjuran",
                            color = colorScheme.onSurface,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )

                        Text(
                            text = recommendationText,
                            color = colorScheme.onSurface,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        )
                    }
                } else if (resultText == "Data tidak valid") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(color = Color.Red, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
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
    }
}

fun calculateAgeInMonths(birthDate: String): Int {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val birthLocalDate = LocalDate.parse(birthDate, formatter)
    val currentDate = LocalDate.now()
    return Period.between(birthLocalDate, currentDate).toTotalMonths().toInt()
}


