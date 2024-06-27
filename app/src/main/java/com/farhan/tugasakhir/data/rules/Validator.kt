package com.farhan.tugasakhir.data.rules

object Validator {

    fun validateEmail(email: String): ValidationResult {
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return ValidationResult(
            status = (email.isNotEmpty() && emailPattern.matches(email))
        )
    }

    fun validatePassword(password: String):ValidationResult{
        return ValidationResult(
            (password.isNotEmpty() && password.length>=8)
        )
    }
}

data class ValidationResult(
    val status : Boolean =false
)