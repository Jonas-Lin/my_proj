package com.example.tripapp.ui.feature.trip.notes.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripapp.ui.feature.member.GetUid
import com.example.tripapp.ui.feature.member.MemberRepository
import com.example.tripapp.ui.feature.trip.dataObjects.Notes
import com.example.tripapp.ui.restful.RequestVM
import com.ron.restdemo.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class NotesViewModel : ViewModel() {
    val tag = "tag_NotesViewModel"
    val requestVM =  RequestVM()

    private var _notesState = MutableStateFlow(Notes(drText = ""))
    val notesState = _notesState.asStateFlow()

    suspend fun GetNotes(id: Int): Notes? {
        return try {
            RetrofitInstance.api.GetNotes(id)
        } catch (e: IOException) {
            Log.e(tag, "Network error: ${e.message}", e)
            null
        }  catch (e: Exception) {
            Log.e(tag, "Unexpected error: ${e.message}", e)
            null
        }
    }

    suspend fun UpdateNotes(notes: Notes): Notes? {
        try {
            val response = RetrofitInstance.api.UpdateNotes(notes)
            Log.d(tag, "data: ${response}")
            return response
        } catch (e: Exception) {
            Log.e(tag, "error: ${e.message}")
            return null
        }
    }

    suspend fun CreateNotes(notes: Notes): Notes? {
        try {
            val response = RetrofitInstance.api.CreateNotes(notes)
            Log.d(tag, "data: ${response}")
            return response
        } catch (e: Exception) {
            Log.e(tag, "error: ${e.message}")
            return null
        }
    }

    fun createNotes(notes: Notes) {
        Log.d(tag, "Creating notes: $notes")
        viewModelScope.launch {
            val response = CreateNotes(notes)
            if (response != null) {
                Log.d("createNotes", "Notes created successfully: $response")
            } else {
                Log.e("createNotes", "Failed to create notes. Response is null.")
            }
        }
    }

    fun setNotes(notes: Notes) {
        viewModelScope.launch {
            val response = GetNotes(notes.dstNo)
            if (response != null) {
                _notesState.update { response }
                Log.d("setNotes", "Notes fetched successfully: $response")
            } else {
                Log.e("setNotes", "Failed to fetch notes. Response is null.")
            }
        }
    }

    fun updateNotes(notes: Notes) {
            Log.d(tag, "Updating notes: $notes")
        viewModelScope.launch {
            val response = UpdateNotes(notes)
            if (response != null) {
                _notesState.update { response }
                Log.d("updateNotes", "Notes updated successfully: $response")
            } else {
                Log.e("updateNotes", "Failed to update notes. Response is null.")
            }
        }
    }

    fun setNotesByApi(id: Int) {
        viewModelScope.launch {
            try {
                val response = GetNotes(id)
                if (response != null) {
                    _notesState.update { response }
                    Log.d("setNotesByApi", "Notes fetched successfully: $response")
                } else {
                    Log.e("setNotesByApi", "Notes API returned null. Setting default state.")
                    _notesState.update { Notes() }
                }
            } catch (e: Exception) {
                Log.e("setNotesByApi", "Failed to fetch notes: ${e.message}")
                _notesState.update { Notes() } // 設定安全的默認值
            }
        }
    }
}