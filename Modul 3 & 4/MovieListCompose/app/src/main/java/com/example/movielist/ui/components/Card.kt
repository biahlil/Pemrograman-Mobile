package com.example.movielist.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielist.R
import com.example.movielist.ui.theme.MovieListTheme

@Composable
fun MovieCard(
    @DrawableRes image: Int,
    @StringRes titleText: Int,
    @StringRes yearText: Int,
    @StringRes descriptionText: Int,
    detailOnclick: () -> Unit,
    openBrowserClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 12.dp, end = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(250.dp)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            CardDetails(
                openBrowserClick = openBrowserClick,
                detailOnclick = detailOnclick,
                titleText = titleText,
                yearText = yearText,
                descriptionText = descriptionText,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun CardDetails(
    detailOnclick: () -> Unit,
    openBrowserClick: () -> Unit,
    @StringRes titleText: Int,
    @StringRes yearText: Int,
    @StringRes descriptionText: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row (
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(titleText),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 3,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(yearText),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.plot),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(descriptionText),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.End
            )
        ) {
            OpenUrlButton(
                openBrowserClick = openBrowserClick,
                displayText = R.string.imdb,
                modifier = modifier
                    .weight(1f)
                    .height(45.dp)
            )
            Button(
                onClick = detailOnclick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(3.dp),
                modifier = modifier
                    .weight(1f)
                    .height(45.dp)
            ) { Text(text = stringResource(R.string.detail),
                style = MaterialTheme.typography.labelMedium,
                ) }
        }
    }
}

@Preview
@Composable
private fun MovieCardPrev() {
    MovieListTheme(darkTheme = true) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MovieCard(
                    image = R.drawable.venom_cover,
                    titleText = R.string.venom_title,
                    yearText = R.string.venom_year,
                    descriptionText = R.string.venom_detail,
                    detailOnclick = {},
                    openBrowserClick = {}
                )
            }
        }

    }
}
