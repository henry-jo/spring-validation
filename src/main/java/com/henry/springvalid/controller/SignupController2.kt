package com.henry.springvalid.controller

import com.henry.springvalid.dto.SignUpRequest
import com.henry.springvalid.dto.SignUpStep1
import com.henry.springvalid.dto.SignUpStep2
import com.henry.springvalid.dto.SignupApiValidator
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.Locale

@RestController
@RequestMapping("/v2/signup")
class SignupController2(
    private val messageSource: MessageSource,

    private val validator: SmartValidator
) {

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBindException(ex: BindException): ResponseEntity<Map<String, String>> {
        val errors = ex.fieldErrors.map { fieldError -> fieldError.field to messageSource.getMessage(fieldError, Locale.KOREAN) }.toMap()
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/step1")
    fun step1(
        @RequestBody step1: SignUpStep1,
        bindingResult: BindingResult
    ): ResponseEntity<Unit> {
        validate(step1, bindingResult)

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/step2")
    fun step2(
        @RequestBody step2: SignUpStep2,
        bindingResult: BindingResult
    ): ResponseEntity<Unit> {
        validate(step2, bindingResult)

        return ResponseEntity.noContent().build()
    }

    @PostMapping
    fun signup(
        @RequestBody request: SignUpRequest,
        bindingResult: BindingResult
    ): ResponseEntity<Unit> {
        validate(request, bindingResult)

        return ResponseEntity.noContent().build()
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(BindException::class)
    private fun validate(request: SignupApiValidator, bindingResult: BindingResult) {
        val validationGroups = request.validationGroups()

        validator.validate(request, bindingResult, *validationGroups as Array<Any>)

        if (bindingResult.hasErrors()) {
            throw BindException(bindingResult)
        }
    }
}