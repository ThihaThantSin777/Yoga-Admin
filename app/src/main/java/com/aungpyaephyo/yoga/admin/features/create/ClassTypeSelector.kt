@file:OptIn(ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.create

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
import com.aungpyaephyo.yoga.admin.data.model.YogaClassType

@Composable
internal fun ClassTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: YogaClassType,
    onTypeSelected: (YogaClassType) -> Unit,
) {
    YogaClassType.entries.forEach {

        val selected = selectedType == it

        FilterChip(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onTypeSelected(it) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            label = { Text(it.displayName) },
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