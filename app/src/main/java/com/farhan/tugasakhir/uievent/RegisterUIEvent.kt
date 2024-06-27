package com.farhan.tugasakhir.uievent

sealed class RegisterUIEvent{
    data class NameChanged(val nama:String) : RegisterUIEvent()
    data class EmailChanged(val email:String) : RegisterUIEvent()
    data class PasswordChanged(val password:String) : RegisterUIEvent()

    object RegisterButtonClicked : RegisterUIEvent()
}
