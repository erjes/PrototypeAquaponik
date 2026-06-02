package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cibiruwetan.protoaquaponik.data.toKolamModel
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
    private var kolamListener: ValueEventListener? = null

    private val _listKolam = MutableStateFlow<List<Kolam>>(emptyList())
    val listKolam: StateFlow<List<Kolam>> = _listKolam

    init {
        fetchKolamData()
    }

    private fun fetchKolamData() {
        kolamListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _listKolam.value = snapshot.children.map { it.toKolamModel() }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        dbRef.addValueEventListener(kolamListener as ValueEventListener)
    }

    override fun onCleared() {
        kolamListener?.let { dbRef.removeEventListener(it) }
    }
}
