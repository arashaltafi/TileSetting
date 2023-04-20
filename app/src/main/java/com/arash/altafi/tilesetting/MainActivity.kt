package com.arash.altafi.tilesetting

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.arash.altafi.tilesetting.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            init()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun init() = binding.apply {
        val statusBarManager: StatusBarManager = getSystemService(StatusBarManager::class.java)
        Log.d(TAG, "onCreate: statusBarManager $statusBarManager")

        val resultSuccessExecutor = Executor {
            Log.d(TAG, "requestAddTileService result success")
            runOnUiThread {
                binding.txtResult.text = "requestAddTileService result success"
            }
        }

        btnStartService.setOnClickListener {
            Log.d(TAG, "onclick buttonStartService")
            startService(Intent(this@MainActivity, TestTile::class.java))
        }

        btnRequestAddTileService.setOnClickListener {
            Log.d(TAG, "onclick buttonRequestAddTileService")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                statusBarManager.requestAddTileService(
                    ComponentName(
                        this@MainActivity,
                        TestTile::class.java
                    ),
                    getString(R.string.tile_label),
                    Icon.createWithResource(this@MainActivity, R.drawable.ic_baseline_tag_faces_24),
                    resultSuccessExecutor,
                ) { resultCodeFailure ->
                    Log.d(TAG, "requestAddTileService failure: $resultCodeFailure")
                    val resultFailureText =
                        when (val ret = RequestResult.findByCode(resultCodeFailure)) {
                            RequestResult.TILE_ADD_REQUEST_ERROR_APP_NOT_IN_FOREGROUND,
                            RequestResult.TILE_ADD_REQUEST_ERROR_BAD_COMPONENT,
                            RequestResult.TILE_ADD_REQUEST_ERROR_MISMATCHED_PACKAGE,
                            RequestResult.TILE_ADD_REQUEST_ERROR_NOT_CURRENT_USER,
                            RequestResult.TILE_ADD_REQUEST_ERROR_NO_STATUS_BAR_SERVICE,
                            RequestResult.TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS,
                            RequestResult.TILE_ADD_REQUEST_RESULT_TILE_ADDED,
                            RequestResult.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED,
                            RequestResult.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED -> {
                                ret.name
                            }
                            null -> {
                                "unknown resultCodeFailure: $resultCodeFailure"
                            }
                        }
                    runOnUiThread {
                        binding.txtResult.text = resultFailureText
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Your Device is Not Api 33", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private companion object {
        const val TAG = "MainActivity"
    }

}