package com.henry.springvalid.custom

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [PhoneValidator::class])
annotation class Phone(
    val message: String = "올바르지 않은 번호입니다.",

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<out Payload>> = []
)