package com.example.movielist.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun OpenUrlButton(
    url: String,
    @StringRes displayText: Int,
    modifier: Modifier = Modifier
) {
    // 1. Dapatkan context dari Composable
    val context = LocalContext.current

    Button(
        onClick = {
            // Pastikan URL tidak kosong
            if (url.isNotBlank()) {
                // 2. Buat Intent dengan aksi VIEW dan data Uri dari URL
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                try {
                    // 3. Luncurkan Activity (browser)
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Tangani error jika tidak ada aplikasi browser yang terpasang
                    Toast.makeText(
                        context,
                        "Tidak ada aplikasi browser yang ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "URL tidak valid", Toast.LENGTH_SHORT).show()
            }
        },
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