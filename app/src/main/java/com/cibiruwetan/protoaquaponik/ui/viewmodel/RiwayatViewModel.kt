package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cibiruwetan.protoaquaponik.data.toHistoryPoints
import com.cibiruwetan.protoaquaponik.model.HistoryPoint
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RiwayatViewModel : ViewModel() {

    private val database = Firebase.database
    private var activeReference: DatabaseReference? = null
    private var activeListener: ValueEventListener? = null

    private val _historyPoints = MutableStateFlow<List<HistoryPoint>>(emptyList())
    val historyPoints: StateFlow<List<HistoryPoint>> = _historyPoints

    fun observeKolam(kolamId: String) {
        stopObserving()

        if (kolamId.isBlank()) {
            _historyPoints.value = emptyList()
            return
        }

        val reference = database.getReference("kolam/$kolamId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _historyPoints.value = snapshot.toHistoryPoints()
            }

            override fun onCancelled(error: DatabaseError) {
                _historyPoints.value = emptyList()
            }
        }

        reference.addValueEventListener(listener)
        activeReference = reference
        activeListener = listener
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
