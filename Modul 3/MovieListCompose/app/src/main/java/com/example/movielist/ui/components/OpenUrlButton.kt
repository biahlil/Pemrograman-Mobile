package com.example.movielist.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun OpenUrlButton(
    openBrowserClick: () -> Unit,
    @StringRes displayText: Int,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = openBrowserClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(3.dp),
        modifier = modifier
    ) {
        Text(text = stringResource(displayText),
            style = MaterialTheme.typography.labelMedium
        )
    }
}