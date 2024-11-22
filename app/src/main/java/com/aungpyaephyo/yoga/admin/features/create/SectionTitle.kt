package com.aungpyaephyo.yoga.admin.features.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .padding(horizontal = 10.dp),
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun SectionSubtitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = Modifier
    )
    {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.titleLarge,

            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}