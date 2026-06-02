package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.R

@Composable
fun PondDropdownSelector(
    selectedId: String,
    listOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TextButton(
            enabled = listOptions.isNotEmpty(),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.Waves,
                contentDescription = stringResource(R.string.content_description_pond_selector),
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(20.dp)
            )
            Text(
                text = selectedId,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOptions.forEach { id ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = id,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (id == selectedId) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onOptionSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}
