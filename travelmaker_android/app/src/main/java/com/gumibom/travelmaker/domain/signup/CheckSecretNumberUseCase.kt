package com.gumibom.travelmaker.domain.signup

import com.gumibom.travelmaker.data.repository.signup.SignupRepository
import com.gumibom.travelmaker.data.repository.signup.SignupRepositoryImpl
import javax.inject.Inject

class CheckSecretNumberUseCase @Inject constructor(
    private val signupRepositoryImpl: SignupRepository
) {

}