package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cibiruwetan.protoaquaponik.model.Kolam
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KolamViewModel : ViewModel() {

    private val dbRef = Firebase.database.getReference("kolam")

    private val _listKolam = MutableStateFlow<List<Kolam>>(emptyList())
    val listKolam: StateFlow<List<Kolam>> = _listKolam

    init {
        fetchKolamData()
    }

    private fun fetchKolamData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Kolam>()
                for (kolamSnapshot in snapshot.children) {
                    val kolam = kolamSnapshot.getValue(Kolam::class.java)
                    if (kolam != null) {
                        items.add(kolam.copy(id = kolamSnapshot.key ?: ""))
                    }
                }
                _listKolam.value = items
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}