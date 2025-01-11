package com.example.tripapp.ui.feature.trip.notes.note

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tripapp.R
import com.example.tripapp.ui.feature.trip.dataObjects.Destination
import com.example.tripapp.ui.feature.trip.dataObjects.Notes


@Composable
fun NotesScreen(
    navController: NavController,
    notesViewModel: NotesViewModel,
    dstNo: Int,
    uid: Int
) {
    val notesState by notesViewModel.notesState.collectAsState()
    val context = LocalContext.current

    // 畫面進入時執行
    LaunchedEffect(Unit) {
        notesViewModel.setNotesByApi(dstNo)
        Log.d("NotesScreen", "NotesState: $notesState")
        Log.d("notesViewModel", "dstNo: ${dstNo}")
        if (notesViewModel.notesState.value.drText.isEmpty()) {
            // 如果筆記為空，創建一個新的筆記
            notesViewModel.createNotes(
                Notes(
                    memNo = uid,
                    dstNo = dstNo,
                    drText = ""
                )
            )
        }
    }

    // 使用 `notesState` 作為顯示和更新的資料
    var localDrText by remember { mutableStateOf(notesState?.drText ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.black_200))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "旅遊筆記",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.aaa),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "旅遊筆記內容",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = localDrText,
                onValueChange = { newText ->
                    localDrText = newText
                    notesViewModel.updateNotes(
                        notesState.copy(drText = newText) // 使用新內容更新資料庫
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text(text = "這邊可以輸入文字") },
                singleLine = false,
                maxLines = 6
            )
        }
    }
}


fun saveNote(notes: String) {
    Log.d("TravelNoteScreen", "Note saved: $notes")
}

@Preview
@Composable
fun NotesScreenPreview(){
    NotesScreen(
        navController = rememberNavController(),
        notesViewModel = NotesViewModel(),
        dstNo = viewModel(),
        uid = viewModel()
    )
}