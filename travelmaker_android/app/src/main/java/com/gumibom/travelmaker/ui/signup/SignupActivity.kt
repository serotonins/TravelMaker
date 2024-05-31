package com.gumibom.travelmaker.ui.signup

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gumibom.travelmaker.BuildConfig
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ActivityMainBinding
import com.gumibom.travelmaker.databinding.ActivitySignupBinding
import com.gumibom.travelmaker.model.GoogleUser
import com.gumibom.travelmaker.util.PermissionChecker
import com.gumibom.travelmaker.util.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext


private const val TAG = "SignupActivity_싸피"


@AndroidEntryPoint
class SignupActivity : AppCompatActivity(){
    private lateinit var binding : ActivitySignupBinding
    private lateinit var navController : NavController

    private var mAuth : FirebaseAuth? = null
    var mGoogleSignInClient : GoogleSignInClient? = null

    private val signupViewModel : SignupViewModel by viewModels()

    private val permissionCheck = PermissionChecker(this)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 회원가입 네비게이션을 위한 navController 지정
        binding = ActivitySignupBinding.inflate(layoutInflater).apply {
            navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_signup)
            as NavHostFragment).navController
        }
        setContentView(binding.root)
        permissionCheck.permitted = object : PermissionListener {
                override fun onGranted() {
                    //퍼미션 획득 성공일때
//                    startScan()
                    Log.d(TAG, "onGranted: onGranted")
                }
        }
        googleSignup()
        // 프로그레스바 진행률 설정
        setProgressBar(0)
    }
    /// TODO: Back Stack 달기

    // 구글 로그인을 클릭했는데 처음 로그인한 사용자일 시
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun googleSignup() {

        val googleUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 레벨 33 이상에서는 타입 파라미터를 명시적으로 제공
            intent.getParcelableExtra("googleUser", GoogleUser::class.java)
        } else {
            // API 레벨 33 미만에서는 이전 방식의 getParcelable 사용
            intent.getParcelableExtra("googleUser") as? GoogleUser
        }

        // 구글 이메일을 인텐트로 성공적으로 받았다면
        if (googleUser != null) {
            val bundle = bundleOf("googleUser" to googleUser)

            // R.id.specific_fragment는 백 스택을 제거할 대상 프래그먼트의 ID입니다. 'true'는 이 프래그먼트를 포함하여 제거한다는 의미입니다.
            // 아이디 패스워드 프래그먼트는 구글 로그인 시 백스택에서 제거한다.
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.signupIdPwFramgnet, true)
                .build()
            navController.navigate(R.id.action_signupIdPwFramgnet_to_signupNicknameFragment, bundle, navOptions)
        }
    }



    fun navigateToNextFragment() {
        Log.d(TAG, "navigateToNextFragment: gdgd")
        when(navController.currentDestination?.id){
            R.id.signupIdPwFramgnet-> navController.navigate(R.id.action_signupIdPwFramgnet_to_signupNicknameFragment)
            R.id.signupNicknameFragment->navController.navigate(R.id.action_signupNicknameFragment_to_signupLocationFragment)
            R.id.signupLocationFragment->navController.navigate(R.id.action_signupLocationFragment_to_signupGenderBirthdayFragment)
            R.id.signupGenderBirthdayFragment->navController.navigate(R.id.action_signupGenderBirthdayFragment_to_signupPhoneFragment)
            R.id.signupPhoneFragment->navController.navigate(R.id.action_signupPhoneFragment_to_signupProfileFragment)
            R.id.signupProfileFragment->navController.navigate(R.id.action_signupProfileFragment_to_signupSuccessFragment) //로그인으로 이동
        }
        updateProgressBar(true)
    }

    fun navigateToPreviousFragment() {
        // 구글 로그인이 아니면 뒤로가기 정상 동작
        if (signupViewModel.bundle == null) {
            navController.navigateUp()
            updateProgressBar(false)
        } else {
            // 구글 로그인인데 회원가입 첫 화면인 닉네임 화면이면 activity 종료
            if (navController.currentDestination?.id == R.id.signupNicknameFragment) {
                finish()
            } else { // 닉네임 화면이 아니면 뒤로가기 정상 동작
                navController.navigateUp()
                updateProgressBar(false)
            }
        }
    }

    // 회원가입 화면 프로그레스바 진행률

    private fun updateProgressBar(isGo:Boolean) {
        var value = binding.pbSignup.progress
        if (isGo){//앞으로 가는 값
            value += 20 // 프로그레스바를 20% 증가
        }else{//뒤로 가는 값
            value -= 20 //20% 감소
        }
        setProgressBar(value)//담고 setProgressBar에 넘기고 넘긴 값을 에니메이팅 처리한다.

    }

    private fun setProgressBar(progress: Int) {
        // ObjectAnimator를 사용하여 부드러운 진행률 증가 애니메이션을 적용할 수 있음 ㅋㅋㅋ
        ObjectAnimator.ofArgb(binding.pbSignup, "progress", binding.pbSignup.progress, progress).apply {
            duration = 1000 //이동 시간을 ms 단위로 측정할 수 있는 로직
        }.start()
    }


}