package com.example.translatorkmm.android.translate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.translatorkmm.android.R

@Composable
fun SwapLanguagesButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.swap_languages),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(id = R.string.swap_languages))
    }
}

@Preview(showBackground = true)
@Composable
fun SwapLanguagesButtonPreview() {
    SwapLanguagesButton(modifier = Modifier) { }
}