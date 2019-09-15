package com.henry.springvalid.custom

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PhoneValidator : ConstraintValidator<Phone, String?> {

    private var phoneNumberUtil: PhoneNumberUtil? = null

    override fun initialize(phone: Phone) {
        phoneNumberUtil = PhoneNumberUtil.getInstance()
    }

    override fun isValid(s: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (s.isNullOrBlank()) {
            return true
        }

        return try {
            val number = phoneNumberUtil!!.parse(s, "KR")
            phoneNumberUtil!!.isValidNumber(number)
        } catch (e: NumberParseException) {
            false
        }
    }
}