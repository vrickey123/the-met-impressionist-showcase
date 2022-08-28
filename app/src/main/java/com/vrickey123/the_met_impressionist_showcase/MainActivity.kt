package com.vrickey123.the_met_impressionist_showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vrickey123.router.Router
import com.vrickey123.router.RouterImpl
import com.vrickey123.router.di.MetRouterImpl
import com.vrickey123.the_met_impressionist_showcase.ui.theme.TheMetImpressionistShowcaseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @MetRouterImpl
    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(router = router)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    TheMetImpressionistShowcaseTheme {
        App(router = RouterImpl())
    }
}