@file:OptIn(ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.create

import com.aungpyaephyo.yoga.admin.data.model.DifficultyLevel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
internal fun DifficultySelector(
    modifier: Modifier = Modifier,
    selectedValue: DifficultyLevel,
    onDifficultySelected: (DifficultyLevel) -> Unit,
) {
    DifficultyLevel.entries.forEach {

        val selected = selectedValue == it

        FilterChip(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onDifficultySelected(it) },
            label = {
                Text(it.displayName)
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            selected = selected,
            leadingIcon = if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Selected icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            },
        )
    }
}