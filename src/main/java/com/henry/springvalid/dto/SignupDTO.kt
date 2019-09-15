package com.henry.springvalid.dto

import com.henry.springvalid.custom.Phone
import org.hibernate.validator.constraints.Range
import javax.validation.Valid
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.groups.Default

interface SignupApiValidator {

    fun validationGroups(): Array<Class<*>?> = emptyArray()
}

data class SignUpStep1(
    @field:NotBlank
    val name: String?,

    @field:NotBlank
    val address: String?,

    @field:Range(min = 1, max = 150)
    val age: Int?,

    @field:Size(min = 1, message = "하나 이상의 값이 입력되어야 한다.")
    val route: List<String>?,

    @field:AssertTrue
    val agreed: Boolean?,

    @field:Phone(groups = [Adult::class])
    @field:NotBlank(groups = [Adult::class])
    val phoneNumber: String?
) : SignupApiValidator {
    interface Adult : Default

    override fun validationGroups(): Array<Class<*>?> {
        return if (age!! > 19) {
            listOf<Class<*>?>(Adult::class.java).toTypedArray()
        } else {
            emptyArray()
        }
    }
}

data class SignUpStep2(

    @field:NotNull
    val man: Boolean?,

    @field:NotNull(groups = [Man::class])
    val army: Boolean?,

    @field:NotBlank
    val occName: String?,

    @field:NotBlank
    val occAddress: String?
) : SignupApiValidator {
    interface Man : Default

    override fun validationGroups(): Array<Class<*>?> {
        return if (man!!) {
            listOf<Class<*>?>(Man::class.java).toTypedArray()
        } else {
            emptyArray()
        }
    }
}

data class SignUpRequest(
    @field:Valid
    val step1: SignUpStep1,
    @field:Valid
    val step2: SignUpStep2,

    @field:NotNull
    val agreed: Boolean?
) : SignupApiValidator {

    override fun validationGroups(): Array<Class<*>?> {
        return step1.validationGroups() + step2.validationGroups()
    }
}