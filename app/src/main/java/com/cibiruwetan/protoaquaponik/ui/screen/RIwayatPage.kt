package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.theme.StatusRed
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.RiwayatViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart

@Composable
fun RiwayatPage(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: RiwayatViewModel = viewModel()
) {
    val kolamId by sharedViewModel.selectedKolamId
    val historyPoints by viewModel.historyPoints.collectAsState()
    val modelProducer = remember { CartesianChartModelProducer() }

    DisposableEffect(kolamId) {
        viewModel.observeKolam(kolamId)
        onDispose { viewModel.stopObserving() }
    }

    LaunchedEffect(historyPoints) {
        modelProducer.runTransaction {
            if (historyPoints.isNotEmpty()) {
                lineSeries {
                    series(
                        x = historyPoints.indices.toList(),
                        y = historyPoints.map { it.ppm }
                    )
                }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.label_grafik_ppm),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer = modelProducer,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    placeholder = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.empty_chart),
                                color = StatusRed,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun RiwayatPagePreview() {
//    AquaponikTheme {
//        RiwayatPage(rememberNavController())
//    }
//}
