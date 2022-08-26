package com.vrickey123.the_met_impressionist_showcase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vrickey123.model.MetSearchResult
import com.vrickey123.network.MetNetworkClient
import com.vrickey123.the_met_impressionist_showcase.ui.theme.TheMetImpressionistShowcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Remove
        val client = MetNetworkClient.create()
        setContent {
            TheMetImpressionistShowcaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    TestAPICallLaunchedEffect(client)
                }
            }
        }
    }
}

// TODO: Remove
@Composable
fun TestAPICallLaunchedEffect(client: MetNetworkClient) {
    LaunchedEffect(key1 = "impressionism") {
        val response = client.search("impressionism", true, listOf<String>("impressionism"))
        if (response.isSuccessful) {
            val metSearchResult: MetSearchResult? = response.body()
            Log.d("MainActivity", "zzz total: ${metSearchResult?.total}")
        } else {
            val eb = response.errorBody()
            Log.d("MainActivity", "zzz not successful ${response.errorBody().toString()}")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TheMetImpressionistShowcaseTheme {
        Greeting("Android")
    }
}