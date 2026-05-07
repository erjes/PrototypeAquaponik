package com.cibiruwetan.protoaquaponik.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import com.cibiruwetan.protoaquaponik.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cibiruwetan.protoaquaponik.ui.components.WarningCard
import com.cibiruwetan.protoaquaponik.ui.theme.AquaponikTheme
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun WarningPage(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val kolamId by sharedViewModel.selectedKolamId

    val database = remember(kolamId) {
        Firebase.database.getReference("kolam/$kolamId")
    }
    val warningList = listOf(
        WarningItem("1", "KLM-01", "Sensor Mati", "10:30"),
        WarningItem("2", "KLM-02", "PPM Tidak Wajar", "09:15"),
        WarningItem("3", "KLM-01", "Pompa Mati", "Yesterday")
    )

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.label_recent_notifications),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(warningList) { warning ->
                WarningCard(warning)
            }
        }
    }
}


data class WarningItem(val id: String, val kolamId: String, val issueType: String, val time: String)

//@Preview(showBackground = true)
//@Composable
//fun WarningPagePreview() {
//    AquaponikTheme {
//        WarningPage(rememberNavController())
//    }
//}