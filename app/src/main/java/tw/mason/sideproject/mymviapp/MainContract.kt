package tw.mason.sideproject.mymviapp

class MainContract {

    // Events that user performed
    sealed class Event : UiEvent {
        object OnRandomNumberClicked : Event()
        object OnShowSnackBarClicked : Event()
    }

    // Ui View States
    data class State(
        val randomNumberState: RandomNumberState
    ) : UiState

    // View State that related to Random Number
    sealed class RandomNumberState {
        object Idle : RandomNumberState()
        object Loading : RandomNumberState()
        data class Success(val number : Int) : RandomNumberState()
    }

    // Side effects
    sealed class Effect : UiEffect {
        object ShowSnackBar : Effect()
    }

}