package com.farhan.tugasakhir.uistate

data class RegisterUIState(
    var nama : String = "",
    var email : String = "",
    var password : String = "",

    var namaError : Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false
)
