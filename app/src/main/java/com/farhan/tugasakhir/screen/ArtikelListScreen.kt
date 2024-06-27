package com.farhan.tugasakhir.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.ArtikelListScreenViewModel
import com.farhan.tugasakhir.uistate.UIState
import com.farhan.tugasakhir.data.di.Injection
import com.farhan.tugasakhir.viewmodel.ViewModelFactory
import com.farhan.tugasakhir.data.model.Artikel
import com.farhan.tugasakhir.item.ArtikelItem
import com.farhan.tugasakhir.item.EmptyList
import com.farhan.tugasakhir.item.Search

@Composable
fun ArtikelListScreen(
    navigateToDetail: (Int, Boolean, Boolean) -> Unit,
    navigateBack: () -> Unit,
    artikelListScreenViewModel: ArtikelListScreenViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
) {
    val query by artikelListScreenViewModel.query
    artikelListScreenViewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                artikelListScreenViewModel.search(query)
            }
            is UIState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChange = artikelListScreenViewModel::search,
                    artikelList = uiState.data,
                    navigateToDetail = navigateToDetail,
                    navigateBack = navigateBack
                )
            }
            is UIState.Error -> {
                // Handle error state
            }

        }
    }
}

@Composable
fun HomeContent(
    query: String,
    onQueryChange: (String) -> Unit,
    artikelList: List<Artikel>,
    navigateToDetail: (Int, Boolean, Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(8.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
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
            Search(
                query = query,
                onQueryChange = onQueryChange,
            )
        }

        if (artikelList.isNotEmpty()) {
            ArtikelListItem(
                artikelList = artikelList,
                navigateToDetail = navigateToDetail
            )
        } else {
            EmptyList(
                Warning = stringResource(R.string.empty_data),
                modifier = Modifier
                    .testTag("emptyList")
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtikelListItem(
    artikelList: List<Artikel>,
    navigateToDetail: (Int, Boolean, Boolean) -> Unit,
    contentPaddingTop: Dp = 5.dp,
) {
    MaterialTheme.colorScheme

    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .testTag("lazy_list")
    ) {
        itemsIndexed(artikelList) { _, item ->
            key(item.id) {
                ArtikelItem(
                    id = item.id,
                    judul = item.judul,
                    imageUrl = item.imageUrl,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .animateItemPlacement(tween(durationMillis = 200))
                        .clickable {
                            navigateToDetail(item.id, item.hasTable, item.hasPdf)
                        }
                )
            }
        }
    }
}
