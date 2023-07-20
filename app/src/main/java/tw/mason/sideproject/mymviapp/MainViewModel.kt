package tw.mason.sideproject.mymviapp

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    /**
     * Generate a random number
     */
    private fun generateRandomNumber() {
        viewModelScope.launch {
            // Set Loading
            setState { copy(randomNumberState = MainContract.RandomNumberState.Loading) }
            try {
                // Add delay for simulate network call
                delay(3000)
                val random = (0..10).random()
                if (random % 2 == 0) {
                    // If error happens set state to Idle
                    // If you want create a Error State and use it
                    setState { copy(randomNumberState = MainContract.RandomNumberState.Idle) }
                    throw RuntimeException("Number is even")
                }
                // Update state
                setState { copy(randomNumberState = MainContract.RandomNumberState.Success(number = random)) }
            } catch (exception: Exception) {
                // Show error
                setEffect { MainContract.Effect.ShowSnackBar }
            }
        }
    }

    /**
     * Create initial State of Views
     */
    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            MainContract.RandomNumberState.Idle
        )
    }

    /**
     * Handle each event
     */
    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnRandomNumberClicked -> {
                generateRandomNumber()
            }

            is MainContract.Event.OnShowSnackBarClicked -> {
                setEffect { MainContract.Effect.ShowSnackBar }
            }
        }
    }

}