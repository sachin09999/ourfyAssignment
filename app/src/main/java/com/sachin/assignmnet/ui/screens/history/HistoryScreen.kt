package com.sachin.assignmnet.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sachin.assignmnet.ui.theme.BluePrimary
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Backup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val historyList by viewModel.historyList.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                viewModel.resetUploadState()
            }
            is UploadState.Error -> {
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                viewModel.resetUploadState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "History",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(), // Hack to center if not using CenterAligned, but CenterAligned is better.
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    // Delete Button
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear History", tint = Color.White)
                    }
                    // Upload Button
                    IconButton(onClick = { viewModel.uploadHistory() }) {
                         if (uploadState is UploadState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Icon(Icons.Default.Backup, contentDescription = "Upload", tint = Color.White)
                        }
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary
                )
            )
        }
    ) { padding ->
        if (historyList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No history yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historyList) { item ->
                    HistoryItem(url = item.url, timestamp = item.timestamp)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(url: String, timestamp: Long) {
    val date = Date(timestamp)
    // Format: "11:45 am, 22 Dec 25"
    val formatter = SimpleDateFormat("bb:mm a, dd MMM yy", Locale.getDefault()) 
    // 'bb' is not standard. 'hh' is 12 hour.
    val correctFormatter = SimpleDateFormat("hh:mm a, dd MMM yy", Locale.getDefault())
    val formattedDate = correctFormatter.format(date)

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = formattedDate, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = url, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        }
    }
}
