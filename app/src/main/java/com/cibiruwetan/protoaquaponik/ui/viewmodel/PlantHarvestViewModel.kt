package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cibiruwetan.protoaquaponik.data.toPlantHarvestRecord
import com.cibiruwetan.protoaquaponik.model.PlantHarvestRecord
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlantHarvestViewModel : ViewModel() {

    private val database = Firebase.database
    private var activeReference: DatabaseReference? = null
    private var activeListener: ValueEventListener? = null

    private val _records = MutableStateFlow<List<PlantHarvestRecord>>(emptyList())
    val records: StateFlow<List<PlantHarvestRecord>> = _records

    fun observeKolam(kolamId: String) {
        stopObserving()

        if (kolamId.isBlank()) {
            _records.value = emptyList()
            return
        }

        val reference = database.getReference("kolam/$kolamId/panen_tanaman")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _records.value = snapshot.children
                    .map { it.toPlantHarvestRecord() }
                    .sortedByDescending { it.createdAt }
            }

            override fun onCancelled(error: DatabaseError) {
                _records.value = emptyList()
            }
        }

        reference.addValueEventListener(listener)
        activeReference = reference
        activeListener = listener
    }

    fun addRecord(
        kolamId: String,
        namaTanaman: String,
        beratKg: Double,
        jumlahIkat: Int,
        catatan: String
    ) {
        if (kolamId.isBlank()) return

        val recordReference = database.getReference("kolam/$kolamId/panen_tanaman").push()
        val payload = mapOf(
            "namaTanaman" to namaTanaman,
            "beratKg" to beratKg,
            "jumlahIkat" to jumlahIkat,
            "catatan" to catatan,
            "createdAt" to ServerValue.TIMESTAMP
        )
        recordReference.setValue(payload)
    }

    fun stopObserving() {
        val listener = activeListener ?: return
        activeReference?.removeEventListener(listener)
        activeReference = null
        activeListener = null
    }

    override fun onCleared() {
        stopObserving()
    }
}
