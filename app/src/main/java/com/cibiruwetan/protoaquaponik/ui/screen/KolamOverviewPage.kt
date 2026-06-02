package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.ui.components.KolamCard
import com.cibiruwetan.protoaquaponik.ui.components.RecordingMenu
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary
import com.cibiruwetan.protoaquaponik.ui.viewmodel.KolamViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel

@Composable
fun KolamOverviewPage(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: KolamViewModel = viewModel()
) {
    val listKolam by viewModel.listKolam.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ToscaPrimary,
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.header_location),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = CircleShape
                ) {
                    Text(
                        text = stringResource(R.string.tag_kolam),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (listKolam.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ToscaPrimary, strokeWidth = 3.dp)
            }
        } else {
            val activeKolamId = sharedViewModel.selectedKolamId.value.ifBlank {
                listKolam.firstOrNull()?.id.orEmpty()
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RecordingMenu(
                            selectedKolamId = activeKolamId,
                            enabled = activeKolamId.isNotBlank(),
                            fishIcon = Icons.Default.WaterDrop,
                            plantIcon = Icons.Default.CheckCircle,
                            onFishHarvestClick = {
                                sharedViewModel.updateSelectedKolam(activeKolamId)
                                navController.navigate("ikan/$activeKolamId")
                            },
                            onPlantHarvestClick = {
                                sharedViewModel.updateSelectedKolam(activeKolamId)
                                navController.navigate("panen/$activeKolamId")
                            }
                        )
                    }
                }

                items(listKolam) { kolam ->
                    KolamCard(
                        kolam = kolam,
                        onClick = {
                            sharedViewModel.updateSelectedKolam(kolam.id)

                            navController.navigate("realtime/${kolam.id}")
                        }
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun KolamOverviewPreview() {
//    AquaponikTheme {
//        KolamOverviewPage(
//            navController = rememberNavController(),
//            sharedViewModel = SharedViewModel()
//        )
//    }
//}
