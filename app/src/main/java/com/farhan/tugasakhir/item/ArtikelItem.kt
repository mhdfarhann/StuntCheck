package com.farhan.tugasakhir.item


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ArtikelItem(
    id: Int,
    judul: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val cardWidth = when {
        screenWidth < 390.dp -> 290.dp
        screenWidth < 400.dp -> 340.dp
        else -> 340.dp
    }

    val cardHeight = when {
        screenWidth < 390.dp -> 160.dp
        screenWidth < 400.dp -> 200.dp
        else -> 200.dp
    }

    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier
            .size(width = cardWidth, height = cardHeight)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(15.dp)
            ),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight * 0.75f)
                    .clip(RoundedCornerShape(15.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = judul,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = contentColor,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun CharacterItemPreview(){
    ArtikelItem(
        id = 2,
        judul = "Mengenal Stunting, Penyebab Hingga Cara Pencegahannya" ,
        imageUrl = "https://rsudblora.blorakab.go.id/wp-content/uploads/2022/12/apa-itu-stunting-1024x576.jpeg",
      )
}