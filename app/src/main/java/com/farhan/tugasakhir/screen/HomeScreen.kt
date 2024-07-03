package com.farhan.tugasakhir.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.farhan.tugasakhir.components.AppBottomNavigation
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.components.ToolComponentAdd
import com.farhan.tugasakhir.components.ToolComponentCheck
import com.farhan.tugasakhir.components.ToolComponentList
import com.farhan.tugasakhir.viewmodel.HomeViewModel
import com.farhan.tugasakhir.viewmodel.ProfileScreenViewModel
import com.farhan.tugasakhir.data.model.Artikel
import com.farhan.tugasakhir.data.model.ArtikelData.artikels
import com.farhan.tugasakhir.item.ArtikelItem
import com.farhan.tugasakhir.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    profileScreenViewModel: ProfileScreenViewModel = viewModel(),
    navigateToDetail: (Int, Boolean, Boolean) -> Unit,
    navController: NavController,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val userName = remember { mutableStateOf("") }
    val context = LocalContext.current

    val profileImageUrl by profileScreenViewModel.profileImageUrl.observeAsState("")

    val imagePainter = if (profileImageUrl.isNotEmpty()) {
        rememberAsyncImagePainter(profileImageUrl)
    } else {
        rememberAsyncImagePainter("https://i.pinimg.com/736x/5e/39/6b/5e396bb1b17681759922dd10f8a9d702.jpg")
    }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName.value = document.getString("name") ?: "User"
                    } else {
                        Toast.makeText(context, "No such document", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                BoxWithConstraints {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorScheme.background)
                    ) {
                        val profileImageSize = when {
                            screenWidth < 400.dp -> 60.dp
                            screenWidth < 600.dp -> 70.dp
                            else -> 80.dp
                        }

                        val fontSize = when {
                            screenWidth < 400.dp -> 22.sp
                            screenWidth < 600.dp -> 22.sp
                            else -> 22.sp
                        }

                        val iconSize = when {
                            screenWidth < 400.dp -> 24.dp
                            screenWidth < 600.dp -> 28.dp
                            else -> 30.dp
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = imagePainter,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(profileImageSize)
                                    .clip(CircleShape)
                                    .border(1.dp, colorScheme.onBackground, CircleShape)
                                    .clickable {
                                        navController.navigate(Screen.ProfileScreen.route) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            restoreState = true
                                        }
                                    }
                            )

                            Text(
                                text = "Hallo,",
                                style = typography.titleLarge.copy(fontSize = fontSize),
                                color = colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .clickable {
                                        navController.navigate(Screen.ProfileScreen.route) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            restoreState = true
                                        }
                                    }
                            )

                            Text(
                                text = userName.value,
                                fontWeight = FontWeight.Bold,
                                style = typography.titleLarge.copy(fontSize = fontSize),
                                color = colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clickable {
                                        navController.navigate(Screen.ProfileScreen.route) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            restoreState = true
                                        }
                                    }
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(iconSize)
                                    .clickable {
                                        homeViewModel.logout(navController)
                                    },
                                tint = colorScheme.onBackground
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Artikel",
                                modifier = Modifier
                                    .padding(bottom = 8.dp),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Normal
                                ),
                                color = colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                painter = painterResource(id = R.drawable.icon_more),
                                contentDescription = "",
                                tint = colorScheme.onBackground,
                                modifier = Modifier
                                    .size(40.dp)
                                    .alignByBaseline()
                                    .clickable {
                                        navController.navigate(Screen.ArtikelListScreen.route)
                                    }
                            )
                        }

                        // LazyRow for Artikel items
                        artikelListItem(artikelList = artikels, navigateToDetail = navigateToDetail)

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    horizontal = when {
                                        screenWidth < 400.dp -> 12.dp
                                        screenWidth < 600.dp -> 16.dp
                                        else -> 24.dp
                                    }
                                )
                        ) {
                            item {
                                ToolComponentAdd(
                                    photoUrl = painterResource(id = R.drawable.icon_add),
                                    nameTool = stringResource(id = R.string.tambah_anak),
                                    description = stringResource(id = R.string.desc_add),
                                    navController = navController
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                ToolComponentCheck(
                                    photoUrl = painterResource(id = R.drawable.icon_check),
                                    nameTool = stringResource(id = R.string.periksa_anak),
                                    description = stringResource(id = R.string.desc_periksa),
                                    navController = navController
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                ToolComponentList(
                                    photoUrl = painterResource(id = R.drawable.icon_list),
                                    nameTool = stringResource(id = R.string.datar_anak),
                                    description = stringResource(id = R.string.desc_list),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun artikelListItem(
    artikelList: List<Artikel>,
    navigateToDetail: (Int, Boolean, Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val paddingHorizontal = when {
        screenWidth < 400.dp -> 8.dp
        screenWidth < 600.dp -> 12.dp
        else -> 16.dp
    }

    LazyRow(
        modifier = Modifier.padding(horizontal = paddingHorizontal)
    ) {
        itemsIndexed(artikelList) { _, item ->
            key(item.id) {
                ArtikelItem(
                    id = item.id,
                    judul = item.judul,
                    imageUrl = item.imageUrl,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .animateItemPlacement(tween(durationMillis = 200))
                        .clickable {
                            navigateToDetail(item.id, item.hasTable, item.hasPdf)
                        }
                )
            }
        }
    }
}




