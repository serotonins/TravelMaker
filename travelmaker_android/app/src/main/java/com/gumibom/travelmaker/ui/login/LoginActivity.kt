package com.gumibom.travelmaker.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gumibom.travelmaker.BuildConfig

import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.FAIL_GOOGLE_LOGIN
import com.gumibom.travelmaker.databinding.ActivityLoginBinding
import com.gumibom.travelmaker.model.GoogleUser
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.util.ApplicationClass
import com.gumibom.travelmaker.util.PermissionChecker
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity_싸피"
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var navController : NavController
    private lateinit var permissionChecker: PermissionChecker

    private var mAuth : FirebaseAuth? = null
    var mGoogleSignInClient : GoogleSignInClient? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_login)
                    as NavHostFragment).navController
        }
        permissionChecker = PermissionChecker(this) // 퍼미션 체커 객체 생성

        // 파이어베이스 권한 체크
        setFirebasePermission()

        setContentView(binding.root)
        initAuth()
    }

    // 파이어베이스의 notification 권한을 체크하는 함수
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setFirebasePermission() {
        val jwtToken = ApplicationClass.sharedPreferencesUtil.getToken()
        Log.d(TAG, "setFirebasePermission: $jwtToken")
        if (jwtToken.isEmpty()) {
            permissionChecker.checkPermission()
        }
    }



    private fun initAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()  // 인증 방식은 email
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        Log.d(TAG, "initAuth: 처음부터 일로 오는거야? ")
    }


    fun googleLogin() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        requestActivity.launch(signInIntent)
    }

    private val requestActivity : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken)
                Log.d(TAG, "처음부터 일로 오는거야? ")
            } catch (e: ApiException) {
                Log.d(TAG, "구글 로그인 실패: $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken : String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val socialGoogleUser = mAuth!!.currentUser

                Log.d(TAG, "firebaseAuthWithGoogle: ${socialGoogleUser?.displayName}")
                Log.d(TAG, "firebaseAuthWithGoogle: ${socialGoogleUser?.email}")

                val email = socialGoogleUser?.email ?: ""
                val uId = socialGoogleUser?.uid ?: ""

                val googleUser = GoogleUser(email, uId)

                val isEmail = ApplicationClass.sharedPreferencesUtil.getGoogleEmail()
                // 이메일이 저장되어 있지 않으면 회원가입 2번 페이지로 이동
                if (!isEmail) {
                    moveSignupActivity(googleUser)
                }
                // 이메일이 저장되어 있으면 메인 화면으로 이동
                else {
                    moveMainActivity()
                }
            } else {
                Log.d(TAG, "실패: $task")
            }
        }
    }



    fun navigateToNextFragment(bundle : Bundle) {
        when(navController.currentDestination?.id){
            R.id.loginFragment2-> navController.navigate(R.id.action_loginFragment2_to_findIdPwFragment, bundle)
        }
    }

    fun backToLoginFragment() {
        navController.navigateUp()
    }


    // 메인 화면으로 넘어가는 함수
    fun moveMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    // 회원가입 화면으로 넘어가는 함수
    fun moveSignupActivity(googleUser : GoogleUser?) {
        Log.d(TAG, "moveSignup: 처음부터 일로 오는거야? ")
        val intent = Intent(this, SignupActivity::class.java)

        intent.putExtra("googleUser", googleUser)
        startActivity(intent)
    }
}