package com.tomato.amelia.databinding1.bean

/**
 * author: created by yuqiaodan on 2023/11/6 14:02
 * description:
 */
data class User(
    val name: String,
    val age: Int,
    val gender: Gender
)
enum class Gender {
    Male,
    Female
}

