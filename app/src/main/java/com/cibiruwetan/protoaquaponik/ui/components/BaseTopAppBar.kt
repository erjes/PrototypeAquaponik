package com.cibiruwetan.protoaquaponik.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title: String,
    showBackButton: Boolean = false,
    showDropdown: Boolean = false,
    selectedId: String = "",
    listOptions: List<String> = emptyList(),
    onOptionSelected: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_back),
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            if (showDropdown) {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    PondDropdownSelector(
                        selectedId = selectedId,
                        listOptions = listOptions,
                        onOptionSelected = onOptionSelected
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = ToscaPrimary)
    )
}
