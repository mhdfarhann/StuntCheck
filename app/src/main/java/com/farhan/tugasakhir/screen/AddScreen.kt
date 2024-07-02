package com.farhan.tugasakhir.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farhan.tugasakhir.components.HeadingToolsTextComponent
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.AddScreenViewModel
import com.farhan.tugasakhir.data.model.Child
import com.farhan.tugasakhir.data.di.Gender
import com.farhan.tugasakhir.utils.Tools
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(addScreenViewModel: AddScreenViewModel = viewModel(), navigateBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    val selectedGender = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    var dateResult by remember { mutableStateOf("Belum memilih tanggal") }
    var showChildCountDialog by remember { mutableStateOf(true) }
    var childCount by remember { mutableStateOf(1) }
    var currentChildIndex by remember { mutableStateOf(1) }

    val context = LocalContext.current

    val isChildAdded by addScreenViewModel.isChildAdded.observeAsState()

    fun resetFields() {
        name = ""
        selectedGender.value = ""
        dateResult = "Belum memilih tanggal"
    }

    LaunchedEffect(isChildAdded) {
        if (isChildAdded == true) {
            Toast.makeText(context, "Berhasil menambah anak", Toast.LENGTH_SHORT).show()
            addScreenViewModel.resetChildAddedState() // Reset the state after showing the toast
            resetFields()
            currentChildIndex++
            if (currentChildIndex > childCount) {
                navigateBack()
            }
        }
    }

    val colorScheme = MaterialTheme.colorScheme

    BackHandler(enabled = showChildCountDialog) {
        navigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        if (showChildCountDialog) {
            AlertDialog(
                onDismissRequest = { navigateBack() },
                title = { Text(
                    fontWeight = FontWeight.Bold,
                    text = "Jumlah Anak") },
                text = {
                    Column {
                        Text(text = "Berapa anak yang ingin ditambahkan?")
                        TextField(
                            value = if (childCount == 0) "" else childCount.toString(),
                            onValueChange = {
                                childCount = it.toIntOrNull() ?: 0
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showChildCountDialog = false
                        }
                    ) {
                        Text("Lanjut")
                    }
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.background)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = colorScheme.onSurface
                        )
                    }

                    HeadingToolsTextComponent(value = "Tambah Anak")
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    text = "Anak ke $currentChildIndex dari $childCount anak",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(colorScheme.surface),
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nama Anak") }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Jenis Kelamin",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            RadioButton(
                                selected = selectedGender.value == Gender.male,
                                onClick = { selectedGender.value = Gender.male },
                                colors = RadioButtonDefaults.colors(colorScheme.onSurface)
                            )
                            Text(
                                Gender.male,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            RadioButton(
                                selected = selectedGender.value == Gender.female,
                                onClick = { selectedGender.value = Gender.female },
                                colors = RadioButtonDefaults.colors(colorScheme.onSurface)
                            )
                            Text(
                                Gender.female,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Tanggal Lahir",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(dateResult)
                            Spacer(modifier = Modifier.width(40.dp))
                            Button(
                                onClick = { openDialog.value = true },
                                contentPadding = PaddingValues(),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue)),
                                modifier = Modifier.align(Alignment.Top)
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                    text = "Pilih tanggal",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                            }
                            if (openDialog.value) {
                                val datePickerState = rememberDatePickerState()
                                val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
                                DatePickerDialog(
                                    onDismissRequest = { openDialog.value = false },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                openDialog.value = false
                                                var date = "No selection"
                                                datePickerState.selectedDateMillis?.let {
                                                    date = Tools.convertLongToTime(it)
                                                }
                                                dateResult = date
                                            },
                                            enabled = confirmEnabled.value
                                        ) {
                                            Text(text = "Oke")
                                        }
                                    }
                                ) {
                                    DatePicker(state = datePickerState)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && selectedGender.value.isNotEmpty() && dateResult != "Belum memilih tanggal") {
                            val newChild = Child(name, selectedGender.value, dateResult)
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            if (userId != null) {
                                addScreenViewModel.addChild(newChild, userId)
                            }
                        }
                    },
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .height(56.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                        text = "Simpan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    }
}




