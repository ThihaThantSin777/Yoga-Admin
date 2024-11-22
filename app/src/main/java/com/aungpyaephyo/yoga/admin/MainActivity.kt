package com.aungpyaephyo.yoga.admin
import com.aungpyaephyo.yoga.admin.utils.LocationHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aungpyaephyo.yoga.admin.navigation.YogaNavHost
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import com.aungpyaephyo.yoga.admin.utils.ConnectionChecker

class MainActivity : ComponentActivity() {

    private lateinit var connectionChecker: ConnectionChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        val yogaApp = application as YogaApp

        val repo = yogaApp.repo
       // val imageUtils = yogaApp.imageUtils
        val syncDataUseCase = yogaApp.syncDataUseCase

        val locationHelper = LocationHelper(applicationContext)
        connectionChecker = ConnectionChecker(applicationContext)

        setContent {
            UniversalYogaTheme {
                YogaNavHost(
                    repo = repo,
                    syncDataUseCase = syncDataUseCase,
                    connectionChecker = connectionChecker,
                    //imageUtils = imageUtils,
                    locationHelper = locationHelper
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connectionChecker.start()
    }

    override fun onStop() {
        super.onStop()
        connectionChecker.stop()
    }
}