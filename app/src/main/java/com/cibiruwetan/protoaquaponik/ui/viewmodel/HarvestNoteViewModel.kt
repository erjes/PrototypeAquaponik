package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cibiruwetan.protoaquaponik.data.toHarvestNote
import com.cibiruwetan.protoaquaponik.model.HarvestNote
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HarvestNoteViewModel : ViewModel() {

    private val _notes = MutableStateFlow<List<HarvestNote>>(emptyList())
    val notes: StateFlow<List<HarvestNote>> = _notes.asStateFlow()

    private var listener: ValueEventListener? = null
    private var currentRef: com.google.firebase.database.DatabaseReference? = null

    fun observeKolam(kolamId: String) {
        stopObserving()
        if (kolamId.isBlank()) return

        val ref = Firebase.database.getReference("kolam/$kolamId/harvestNotes")
        currentRef = ref
        listener = ref.orderByChild("tanggalPanen")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children
                        .mapNotNull { it.toHarvestNote().takeIf { n -> n.id.isNotBlank() } }
                        .sortedByDescending { it.tanggalPanen }
                    _notes.value = list
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun addNote(
        kolamId: String,
        jenisPanen: String,
        namaPanen: String,
        beratKg: Double,
        tanggalPanen: Long,
        deskripsi: String
    ) {
        val ref = Firebase.database.getReference("kolam/$kolamId/harvestNotes")
        val key = ref.push().key ?: return
        val note = mapOf(
            "kolamId" to kolamId,
            "jenisPanen" to jenisPanen,
            "namaPanen" to namaPanen,
            "beratKg" to beratKg,
            "tanggalPanen" to tanggalPanen,
            "deskripsi" to deskripsi,
            "createdAt" to System.currentTimeMillis()
        )
        ref.child(key).setValue(note)
    }

    fun stopObserving() {
        listener?.let { currentRef?.removeEventListener(it) }
        listener = null
        currentRef = null
    }

    override fun onCleared() {
        super.onCleared()
        stopObserving()
    }
}