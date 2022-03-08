package ru.kamanin.nstu.graduate.thesis.feature.sign.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.kamanin.nstu.graduate.thesis.component.core.coroutines.flow.*
import ru.kamanin.nstu.graduate.thesis.component.core.error.ErrorConverter
import ru.kamanin.nstu.graduate.thesis.component.core.error.ErrorState
import ru.kamanin.nstu.graduate.thesis.component.core.mvvm.lifecycle.EventDispatcher
import ru.kamanin.nstu.graduate.thesis.component.core.validation.ValidationResult
import ru.kamanin.nstu.graduate.thesis.feature.sign.domain.validation.EmailValidator
import ru.kamanin.nstu.graduate.thesis.shared.account.domain.usecase.ChangePasswordUseCase
import ru.kamanin.nstu.graduate.thesis.shared.session.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
	private val changePasswordUseCase: ChangePasswordUseCase,
	private val errorConverter: ErrorConverter
) : ViewModel() {

	private companion object {

		const val EMPTY_TEXT = ""
	}

	private val _state = MutableStateFlow(SignUpState())
	val state: StateFlow<SignUpState> get() = _state.asStateFlow()

	private val _errorEvent = MutableLiveState<ErrorState>()
	val errorEvent: LiveState<ErrorState> get() = _errorEvent.asLiveState()

	private val emailValidator = EmailValidator()

	val email = MutableStateFlow(EMPTY_TEXT)
	val password = MutableStateFlow(EMPTY_TEXT)
	val repeatPassword = MutableStateFlow(EMPTY_TEXT)
	val verificationCode = MutableStateFlow(EMPTY_TEXT)

	val eventDispatcher = EventDispatcher<EventListener>()

	interface EventListener {

		fun navigateBack()
		fun navigateToExamsList()
	}

	init {
		initRegistrationAvailableObserver()
	}

	private fun initRegistrationAvailableObserver() {
		combine(email, password, repeatPassword, verificationCode) { emailQuery, passwordQuery, repeatPasswordQuery, verificationCodeQuery ->
			emailQuery.isNotEmpty()
				&& passwordQuery.isNotEmpty()
				&& repeatPasswordQuery.isNotEmpty()
				&& verificationCodeQuery.isNotEmpty()
		}
			.onEach { available -> _state.value = _state.value.copy(registrationAvailable = available) }
			.launchIn(viewModelScope)
	}

	fun signUp() {
		verifyAllField(onSuccess = ::handleValidationSuccess, onError = ::handleValidationError)
	}

	private fun verifyAllField(
		onSuccess: () -> Unit,
		onError: (emailValidationResult: ValidationResult, passwordMatchResult: Boolean) -> Unit
	) {
		val emailValidationResult = emailValidator.validate(email.value)
		val passwordMatchResult = password.value == repeatPassword.value

		if (emailValidationResult.isValid() && passwordMatchResult) {
			onSuccess()
		} else {
			onError(emailValidationResult, passwordMatchResult)
		}
	}

	private fun handleValidationSuccess() {
		_state.value = _state.value.copy(registrationAvailable = false, registrationProcessAvailable = true)

		viewModelScope.launch {
			runCatching {
				changePasswordUseCase(
					username = email.value,
					password = password.value,
					verificationCode = verificationCode.value
				)
				loginUseCase(username = email.value, password = password.value)
				eventDispatcher.dispatchEvent { navigateToExamsList() }
			}.onFailure(::handleRegistrationError)
		}
	}

	private fun handleRegistrationError(throwable: Throwable) {
		_state.value = _state.value.copy(registrationAvailable = true, registrationProcessAvailable = false)

		val error = errorConverter.convert(throwable)
		_errorEvent(error)
	}

	private fun handleValidationError(emailValidationResult: ValidationResult, passwordMatchResult: Boolean) {
		_state.value = _state.value.copy(
			emailValidationResult = emailValidationResult,
			passwordMatch = passwordMatchResult
		)
		email.observe(::updateEmail, viewModelScope)
		combine(password, repeatPassword, ::updatePassword).launchIn(viewModelScope)
	}

	private fun updateEmail(emailQuery: String) {
		_state.value = _state.value.copy(emailValidationResult = emailValidator.validate(emailQuery))
	}

	private fun updatePassword(passwordQuery: String, repeatPasswordQuery: String) {
		_state.value = _state.value.copy(passwordMatch = passwordQuery == repeatPasswordQuery)
	}

	fun goBack() {
		eventDispatcher.dispatchEvent { navigateBack() }
	}
}