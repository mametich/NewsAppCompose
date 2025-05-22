package com.example.newsaggregator.ui.screens.DetailScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.size.Dimension
import com.example.newsaggregator.fromHtmlToPlainText
import com.example.newsaggregator.ui.theme.Dimens

@Composable
fun NewsScreen(
    guid: String,
    viewModel: NewsScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val detailUiState by viewModel.newsUiState.collectAsState()

    LaunchedEffect(guid) {
        if (guid.isNotEmpty()) {
            viewModel.loadDetailNews(guid)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge)
            .verticalScroll(rememberScrollState())
    ) {

        detailUiState.image?.let { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(
                        imageUrl,
                        onError = { Log.e("Coil", "Failed to load image: $imageUrl") }
                    ),
                    contentDescription = detailUiState.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(Dimens.paddingMedium),
                            ),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        Text(
            text = detailUiState.title ?: "",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        Text(
            text = fromHtmlToPlainText(detailUiState.description ?: ""),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

