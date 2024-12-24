package io.github.hanihashemi.eggmaster

import android.Manifest
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.UpdateTimber
import io.github.hanihashemi.eggmaster.services.TimerService
import io.github.hanihashemi.eggmaster.services.TimerService.Companion.BOILING_TIME_PARAM
import io.github.hanihashemi.eggmaster.services.TimerService.Companion.REMAINING_TIME
import io.github.hanihashemi.eggmaster.services.TimerService.Companion.TIME_UPDATE_ACTION
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController
    private lateinit var timerUpdateReceiver: BroadcastReceiver
    private var boilingTime = 0

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startTimerService(boilingTime)
        } else {
            showPermissionRationaleDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(MainViewModel.ViewAction.Init(isTimerServiceRunning()))
        observeViewEvent()
        listenToTimerService()

        setContent {
            val state by viewModel.viewState.collectAsState()
            navController = rememberNavController()

            EggMasterTheme {
                EggMasterApp(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                )
            }
        }
    }

    private fun listenToTimerService() {
        timerUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val remainingTime = intent?.getIntExtra(REMAINING_TIME, 0) ?: 0
                viewModel.dispatch(UpdateTimber(remainingTime))
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            timerUpdateReceiver,
            IntentFilter(TIME_UPDATE_ACTION)
        )
    }

    private fun observeViewEvent() {
        lifecycleScope.launch {
            viewModel.viewEvent.collectLatest { viewEvent ->
                when (viewEvent) {
                    is MainViewModel.ViewEvent.OpenNextPage -> {
                        navController.navigate(Screen.BoilDetail.route)
                    }

                    is MainViewModel.ViewEvent.NavigateBack -> {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    is MainViewModel.ViewEvent.StartTimerService -> {
                        boilingTime = viewEvent.boilingTime
                        askNotificationPermissionAndStartTimerService()
                    }

                    is MainViewModel.ViewEvent.NavigateTo -> {
                        if (viewEvent.clearStack) {
                            navController.navigate(viewEvent.route) {
                                popUpTo(Screen.Intro.Destination.Title.route) {
                                    inclusive = false
                                }
                            }
                        } else {
                            navController.navigate(viewEvent.route)
                        }
                    }

                    is MainViewModel.ViewEvent.CancelTimer -> {
                        val intent = Intent(this@MainActivity, TimerService::class.java)
                        stopService(intent)
                        viewModel.dispatch(MainViewModel.ViewAction.ResetTimerServiceEndTime)
                        navController.navigate(Screen.Intro.route) {
                            popUpTo(Screen.Intro.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun askNotificationPermissionAndStartTimerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            )
            when {
                permission == PackageManager.PERMISSION_GRANTED -> startTimerService(boilingTime)
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startTimerService(boilingTime)
        }
    }

    private fun startTimerService(boilingTime: Int) {
        val intent = Intent(this, TimerService::class.java).apply {
            putExtra(BOILING_TIME_PARAM, boilingTime)
        }
        navController.navigate(Screen.Timer.route)
        startForegroundService(intent)
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Needed")
            .setMessage("We need this permission to display notifications and start the timer.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerUpdateReceiver)
    }

    private fun isTimerServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (TimerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
