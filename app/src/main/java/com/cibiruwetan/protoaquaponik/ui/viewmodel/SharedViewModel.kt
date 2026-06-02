package com.cibiruwetan.protoaquaponik.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SharedViewModel : ViewModel() {

    private val dbRef = Firebase.database.getReference("kolam")
    private var kolamListener: ValueEventListener? = null

    private val _selectedKolamId = mutableStateOf("")
    val selectedKolamId: State<String> = _selectedKolamId

    private val _isLoadingKolam = mutableStateOf(true)
    val isLoadingKolam: State<Boolean> = _isLoadingKolam

    private val _hasDatabaseError = mutableStateOf(false)
    val hasDatabaseError: State<Boolean> = _hasDatabaseError

    private val _daftarKolam = mutableStateListOf<String>()
    val daftarKolam: List<String> = _daftarKolam

    init {
        fetchDaftarKolam()
    }

    private fun fetchDaftarKolam() {
        kolamListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentSelected = _selectedKolamId.value
                _isLoadingKolam.value = false
                _hasDatabaseError.value = false

                _daftarKolam.clear()
                snapshot.children.forEach { child ->
                    child.key?.let { _daftarKolam.add(it) }
                }

                if (_daftarKolam.isNotEmpty()) {
                    if (currentSelected.isBlank() || !_daftarKolam.contains(currentSelected)) {
                        _selectedKolamId.value = _daftarKolam[0]
                    }
                } else {
                    _selectedKolamId.value = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoadingKolam.value = false
                _hasDatabaseError.value = true
                _selectedKolamId.value = ""
            }
        }
        dbRef.addValueEventListener(kolamListener as ValueEventListener)
    }

    fun updateSelectedKolam(newId: String) {
        if (_daftarKolam.contains(newId)) {
            _selectedKolamId.value = newId
        }
    }

    override fun onCleared() {
        kolamListener?.let { dbRef.removeEventListener(it) }
    }
}
