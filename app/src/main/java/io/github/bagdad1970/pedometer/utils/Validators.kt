package io.github.bagdad1970.pedometer.utils

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[\\w.%+-]+@[\\w.-]+\\.[A-Z]{2,}$".toRegex(RegexOption.IGNORE_CASE)
    return email.matches(emailRegex)
}

fun isValidPassword(password: String): Boolean {
    if (password.length < 8) return false

    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigits = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    return hasUpperCase && hasLowerCase && hasDigits && hasSpecialChar
}