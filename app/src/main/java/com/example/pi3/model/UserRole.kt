package com.example.pi3.model

enum class UserRole(val senhaPadrao: String) {
    APOIO("123"),
    COORDENADOR("123"),
    GESTOR("123");

    companion object {
        fun fromString(value: String): UserRole {
            return when (value.lowercase()) {
                "apoio" -> APOIO
                "coordenador" -> COORDENADOR
                "gestor" -> GESTOR
                else -> throw IllegalArgumentException("Papel inválido: $value")
            }
        }
    }
}
