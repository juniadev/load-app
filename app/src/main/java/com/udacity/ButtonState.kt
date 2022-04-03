package com.udacity

import androidx.annotation.StringRes

sealed class ButtonState(@StringRes val buttonText: Int) {
    object Initial : ButtonState(R.string.button_name)
    object Loading : ButtonState(R.string.button_loading)
    object Completed : ButtonState(R.string.button_completed)
}