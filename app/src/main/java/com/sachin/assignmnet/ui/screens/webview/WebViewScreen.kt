package com.sachin.assignmnet.ui.screens.webview

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sachin.assignmnet.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    var displayUrl by remember { mutableStateOf(url) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .height(36.dp),
                        onClick = {}
                    ) {
                        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = displayUrl,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BluePrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                url?.let { displayUrl = it }
                            }
                        }
                        loadUrl(url)
                    }
                },
                update = { webView ->
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
