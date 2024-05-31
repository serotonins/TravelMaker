package com.gumibom.travelmaker.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.databinding.ActivityMainBinding
import com.gumibom.travelmaker.model.User
import com.gumibom.travelmaker.ui.main.findmate.FindMateActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import com.gumibom.travelmaker.util.ApplicationClass
import com.gumibom.travelmaker.util.PermissionChecker
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

private const val TAG = "MainActivity_싸피"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private val binding get() = _binding!!
    private lateinit var navController : NavController
    private val viewModel : MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ${ApplicationClass.sharedPreferencesUtil.getToken()}")

        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            navController = (supportFragmentManager.findFragmentById(R.id.fragment_container_main)
                    as NavHostFragment).navController
        }
        setContentView(binding.root)
        sharedPreferencesUtil = SharedPreferencesUtil(this)

        //알림으로 온 매인엑티비티라면?? -> 알림리스트로 보내야 된다.
        // Intent에서 추가 정보 확인하기
        if (intent.getStringExtra("openNotifyFragment") == "notificationFragment") {
            // NotificationFragment로 이동하는 로직 구현
            // 네비게이션으로 Fragment 이동
            navController.navigate(R.id.action_mainFragment_to_mainNotificationFragment)
        }//노티피케이션으로 프래그먼트를 이동시킨다.


        Log.d(TAG, "onCreate: ${sharedPreferencesUtil.getToken()}")

        setFirebase()
        observeViewModel()
        setNavigationMenuToolbar()
        initToolbar()
        getLoginUserInfo()
        observeLiveData()
        backToNavigation()
    }

    /**
     * 네비 게이션 뒤로 가는 함수
     */
    fun backToNavigation() {
        navController.navigateUp()
    }

    /**
     * 메인 화면으로 넘어오면 로그인한 회원의 정보를 전부 받아오는 함수
     */
    private fun getLoginUserInfo() {
        viewModel.getAllUser()
    }

    /**
     *  앱 상단 프로필 사진 업로드
     */
    private fun observeLiveData() {
        val profileImage = findViewById<ImageView>(R.id.my_custom_icon)
        viewModel.user.observe(this) { user ->
            Log.d(TAG, "user: $user")
            if (user.profileImgURL.isNotEmpty()) {
                Glide.with(this)
                    .load(user.profileImgURL)
                    .circleCrop()
                    .into(profileImage)
            }
        }
    }

    fun setNavigationMenuToolbar(){
        //프래그먼트가 ~~ 일 땐 ~~로
        //프래그먼트가 ㅌㅌ 일 땐 ㅌㅌ 로



        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> {
                    Log.d(TAG, "setNavigationMenuToolbar: 1")
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.inflateMenu(R.menu.menu_main) // 새 메뉴 설정
                    binding.toolbar.navigationIcon = null
                    binding.toolbar.title =""
                    binding.linearAppIcon.visibility = View.VISIBLE
                    initToolbar()
                    observeLiveData()
                    Log.d(TAG, "setNavigationMenuToolbar: 2")
                }
                R.id.mainGoTravelFragment->{
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = "내 여행 시작"
                    binding.toolbar.setNavigationOnClickListener {
                        Log.d(TAG, "setNavigationMenuToolbar: ")
                        navController.navigateUp()
                    }
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.inflateMenu(R.menu.detail_menu_main) // 새 메뉴 설정
                }
                R.id.mainFindMateDetailFragment->{
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = "여행 메이트 찾기"
                    binding.toolbar.setNavigationOnClickListener {
                        Log.d(TAG, "setNavigationMenuToolbar: ")
                        navController.navigateUp()
                    }
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.inflateMenu(R.menu.detail_menu_main) // 새 메뉴 설정
                }
                R.id.mainNotificationFragment-> {
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = "알림 페이지"
                    binding.toolbar.setNavigationOnClickListener {
                        Log.d(TAG, "setNavigationMenuToolbar: ")
                        navController.navigateUp()
                    }
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.inflateMenu(R.menu.detail_menu_main) // 새 메뉴 설정
                }
                R.id.mainMyGroupFragment -> {
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = getString(R.string.my_group)
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.setNavigationOnClickListener {
                        Log.d(TAG, "setNavigationMenuToolbar: ")
                        navController.navigateUp()
                    }
                    binding.toolbar.inflateMenu(R.menu.detail_menu_main) // 새 메뉴 설정
                }
                R.id.mainMyPageFragment -> {
                    binding.toolbar.menu.clear() // 기존 메뉴 제거
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = getString(R.string.mypage_title)
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.setNavigationOnClickListener {
                        Log.d(TAG, "setNavigationMenuToolbar: ")
                        navController.navigateUp()
                    }
                    binding.toolbar.inflateMenu(R.menu.detail_menu_main) // 새 메뉴 설정
                }
                R.id.mainMyRecordFragment -> {
                    binding.toolbar.menu.clear()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = getString(R.string.myRecord_title)
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }
                R.id.mainGoTravelFragment, R.id.makePamphletFragment, R.id.startPamphletFragment -> {
                    binding.toolbar.menu.clear()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = getString(R.string.makePamphlet_title)
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }
                R.id.myRecordDetail, R.id.pamphletDetailFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.linearAppIcon.visibility = View.GONE
                }

                R.id.mainLookPamphletsFragment -> {
                    binding.toolbar.menu.clear()
                    binding.linearAppIcon.visibility = View.GONE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_24)
                    binding.toolbar.title = getString(R.string.lookPamphlet_title)
                    binding.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }
            }
        }
    }

    /**
     * View.GONE 되었던 toolbar를 원래대로 되돌려 놓는 함수
     */
    fun setOriginToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun setFirebase(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                Log.d(TAG, "setFirebase: ${task.result}")//viewModel.user.value!!.username
                viewModel.uploadToken(FcmTokenRequestDTO(sharedPreferencesUtil.getLoginId(),task.result!!) )
                Log.d(TAG, "setFirebase: end")
            }
        })
        createNotificationChannel(CHANNEL_ID, "travelmaker")
    }
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
    fun navigationToGotoTravel() {
//        navController.navigate(R.id.action_mainFindMateFragment_to_mainFindMateDetailFragment)
        navController.navigate(R.id.action_mainFragment_to_mainGoTravelFragment)
    }
    fun navigationToGroupMSG(){
        navController.navigate(R.id.action_mainFragment_to_mainMyGroupFragment)
    }
    fun navigationToReadMyRecord(){
        navController.navigate(R.id.action_mainFragment_to_mainMyRecordFragment)
    }
    fun navigationToLookAroundPam(){
        navController.navigate(R.id.action_mainFragment_to_mainLookPamphletsFragment)
    }

    fun navigationToGroupChattingRoom(groupId: Long){
        val bundle = Bundle().apply {
            putLong("groupId", groupId)
        }
        navController.navigate(R.id.action_mainMyGroupFragment_to_mainGroupChattingFragment,bundle)
    }

    fun navigationPop() {
        navController.navigateUp()
    }

    fun observeViewModel(){
        viewModel.isUploadToken.observe(this){
            if (it.isSuccess){
                Log.d(TAG, "서버통신 성공 : ${it.isSuccess}")
                Log.d(TAG, "observeViewModel: ${it.message}")
            }else{
                Log.d(TAG, "실패 데스 : ${it.isSuccess}")
                Log.d(TAG, "observeViewModel: ${it.message}")
            }
        }
    }
    fun moveGoogleMap() {
        val intent = Intent(this, FindMateActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }
    private fun initToolbar(){
        val imageView = findViewById<ImageView>(R.id.my_custom_icon)
        imageView.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_mainMyPageFragment)
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_notify -> {
                    navController.navigate(R.id.action_mainFragment_to_mainNotificationFragment)
                    true
                }
                else -> false
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null //메모리 누수 방지
    }

    companion object {
        // Notification Channel ID
        const val CHANNEL_ID = "travelmaker_channel" //mainActivity의 채널

    }

}

