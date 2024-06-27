package com.farhan.tugasakhir.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.DetailViewModel
import com.farhan.tugasakhir.uistate.UIState
import com.farhan.tugasakhir.data.di.Injection
import com.farhan.tugasakhir.data.model.Standard
import com.farhan.tugasakhir.item.PDFViewer
import com.farhan.tugasakhir.item.TableItem
import com.farhan.tugasakhir.viewmodel.ViewModelFactory

@Composable
fun DetailScreen(
    artikelId: Int,
    hasTable: Boolean,
    hasPdf: Boolean,
    navigateBack: () -> Unit,
    maleStandards: List<Standard>,
    femaleStandards: List<Standard>,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepository()))
) {
    viewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getArtikelById(artikelId)
            }
            is UIState.Success -> {
                val data = uiState.data
                if (hasPdf && data.pdfUrl != null) {
                    PDFViewer(pdfUrl = data.pdfUrl, navigateBack = navigateBack)
                } else {
                    DetailInfo(
                        imageUrl = data.imageUrl,
                        id = data.id,
                        judul = data.judul,
                        content = data.content,
                        navigateBack = navigateBack,
                        maleStandards = if (hasTable) maleStandards else emptyList(),
                        femaleStandards = if (hasTable) femaleStandards else emptyList()
                    )
                }
            }
            is UIState.Error -> {
                // Handle error state
            }
        }
    }
}



@Composable
fun DetailInfo(
    id : Int,
    judul : String,
    imageUrl : String,
    content : String,
    navigateBack: () -> Unit,
    maleStandards: List<Standard>,
    femaleStandards: List<Standard>,

) {

    val colorScheme = MaterialTheme.colorScheme

    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
                .background(colorScheme.background)
        )
        {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = judul,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = judul,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(5.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = content,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                    color = colorScheme.onBackground,
                )
            }

            if (maleStandards.isNotEmpty()) {
                Text(text = "Laki-laki",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,

                )
                TableItem(standards = maleStandards)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (femaleStandards.isNotEmpty()) {
                Text(text = "Perempuan",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground

                )
                TableItem(standards = femaleStandards)
            }

        }
        IconButton(
            onClick = navigateBack,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(colorResource(id = R.color.white))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.Black
            )

        }

    }

}
