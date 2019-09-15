package com.henry.springvalid.controller

import com.henry.springvalid.dto.SignUpRequest
import com.henry.springvalid.dto.SignUpStep1
import com.henry.springvalid.dto.SignUpStep2
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BeanPropertyBindingResult
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
import javax.validation.Valid

@RestController
@RequestMapping("/signup")
class SignupController1(
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
        @Valid @RequestBody step1: SignUpStep1,
        bindingResult: BindingResult
    ): ResponseEntity<Unit> {
        if (bindingResult.hasErrors()) {
            throw BindException(bindingResult)
        }

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/step2")
    fun validatorSignUpStep2(
        @RequestBody step2: SignUpStep2,
        bindingResult: BindingResult
    ): ResponseEntity<Unit> {
        val groups = mutableListOf<Class<*>?>()

        step2.man?.let {
            if (it) {
                groups.add(SignUpStep2.Man::class.java)
            }
        }

        validator.validate(step2, bindingResult, *groups.toTypedArray())

        if (bindingResult.hasErrors()) {
            throw BindException(bindingResult)
        }

        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun validatorSignUp(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<Unit> {
        val step1Result = BeanPropertyBindingResult(signUpRequest.step1, "step1")
        validator.validate(signUpRequest.step1, step1Result)

        if (step1Result.hasErrors()) {
            throw BindException(step1Result)
        }

        val step2Result = BeanPropertyBindingResult(signUpRequest.step2, "step2")

        val groups = mutableListOf<Class<*>?>()

        signUpRequest.step2.man?.let {
            if (it) {
                groups.add(SignUpStep2.Man::class.java)
            }
        }

        validator.validate(signUpRequest.step2, step2Result, *groups.toTypedArray())

        if (step2Result.hasErrors()) {
            throw BindException(step2Result)
        }

        return ResponseEntity.ok().build()
    }
}