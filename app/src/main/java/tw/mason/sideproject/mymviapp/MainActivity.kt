package tw.mason.sideproject.mymviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import tw.mason.sideproject.mymviapp.ui.theme.MyMVIAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<MainViewModel>()
            MyMVIAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }

    @Composable
    fun MainScreen(viewModel: MainViewModel) {

        // 使用remember來保持SnackbarHostState的狀態
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.effect.collect {
                when (it) {
                    MainContract.Effect.ShowSnackBar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Error, number is even")
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                viewModel.setEvent(MainContract.Event.OnRandomNumberClicked)
            }) {
                Text(text = "生成隨機數字")
            }
            val uiState by viewModel.uiState.collectAsState()
            when (val state = uiState.randomNumberState) {
                MainContract.RandomNumberState.Idle -> {
                    Text(text = "IDLE")
                }

                MainContract.RandomNumberState.Loading -> {
                    Text(text = "Loading...")
                }

                is MainContract.RandomNumberState.Success -> {
                    Text(text = "state.number = ${state.number}")
                }
            }

            Button(onClick = {
                viewModel.setEvent(MainContract.Event.OnShowSnackBarClicked)
            }) {
                Text(text = "show Snackbar")
            }
            // 使用SnackbarHost來顯示Snackbar
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data
                    )
                }
            )
        }
    }
}

