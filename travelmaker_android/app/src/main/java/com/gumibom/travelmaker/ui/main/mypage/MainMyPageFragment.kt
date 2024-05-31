package com.gumibom.travelmaker.ui.main.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentMainMypageBinding
import com.gumibom.travelmaker.ui.dialog.ClickEventDialog
import com.gumibom.travelmaker.ui.login.LoginActivity
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.util.ApplicationClass
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainMyPageFragment_싸피"

@AndroidEntryPoint
class MainMyPageFragment:Fragment() {

    private val trustPoint = 800
    private var _binding:FragmentMainMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var activity : MainActivity

    lateinit var getResult: ActivityResultLauncher<Intent>

    private var filePath = ""

    private val myPageViewModel : MainViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        activity = context as  MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // intent 결과를 받음
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // contentResolver로 filePath를 받는다.
                filePath = getFilePathUri(it.data?.data!!)
                Log.d(TAG, "onCreate: $filePath")
                // ViewModel LiveData에 추가
                myPageViewModel.updateProfilePicture(filePath)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        _binding = FragmentMainMypageBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        goToEditMyInfo()
        observePicture() // 1. observePicture(): ok
        selectMypagePicture() // 2. 프로필사진 변경 로직: ok
        // 4. 회원정보 수정 버튼 클릭 -> 회원정보 수정할 다이얼로그 뜸
        logoutMyAccount() // 5. 로그아웃 버튼 클릭 -> 로그인 상태에서 로그아웃 되는 로직: ok
        showMyTrustLevel() // 7. 신뢰도 수준 보여주는 함수 <- 신뢰도 점수 구간에 맞게 drawable에서 img_trust_n 사진을 찾아서 그려줌: ok
        myPageViewModel.getAllUser()
        myPageViewModel.user.observe(viewLifecycleOwner){
            binding.tvMypageNickname.text = myPageViewModel.user.value!!.nickname
            Glide.with(activity)
                .load(myPageViewModel.user.value!!.profileImgURL)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .transform(CenterCrop()) // Apply center crop to maintain aspect ratio
                .into(binding.ivMypageProfileImg)




        }
    }


    private fun goToEditMyInfo(){
        val btnGoToEditMyInfo = binding.btnMyinfoEdit
        btnGoToEditMyInfo.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_mainMyPageFragment_to_dialogMainMypageEditMyinfo)
        }
    }
    /*
    신뢰도 확인 버튼을 누르고 자신의 신뢰도를 그림으로 확인하는 로직
     */
    private fun showMyTrustLevel(){
        val btnShowMeTrustLevel = binding.btnMypageCheckMyTrustlevel
        btnShowMeTrustLevel.setOnClickListener{
            // 버튼이 눌린 순간, lottie 이미지가 사라짐
            binding.lottieMyTrustlevel.visibility = View.GONE
            // ViewModel에서 신뢰도 점수에 따른 이미지 리소스 ID를 가져옴
            val trustLevelImageId = myPageViewModel.updateAndGetTrustLevelImageId(trustPoint)
            // 그 이미지 리소스 ID에 해당하는 이미지를 화면의 특정한 곳에 뿌려줌
            binding.ivEmptyTrustLevelImg.setImageResource(trustLevelImageId)
            binding.ivEmptyTrustLevelImg.visibility = View.VISIBLE
            binding.btnMypageCheckMyTrustlevel.visibility = View.INVISIBLE
        }

    }

    /*
    회원 프로필 사진 변경 로직
    플러스 버튼 누르면 ->
        2-1. 권한체크
        2-2. intent로 actionPick 생성
        2-3. intent 타입을 image로 지정
        2-4. 콜백함수 실행 -> 성공이면? 파일형태를 URL로 변경
    */


    private fun selectMypagePicture() {
        binding.ivMypageProfileEdit.setOnClickListener {
            if (!permissionGallery()) {
                val intent  = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getResult.launch(intent)
            }
        }
    }

    /*
     * 안드로이드 SDK 33 이상일 떄와 미만일 때를 구분하여 권한 체크
     */
    private fun permissionGallery() : Boolean{
        // 33 이상일 때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val readImagePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES)
            val readVideoPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_VIDEO)

            return if (readImagePermission == PackageManager.PERMISSION_DENIED || readVideoPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(  // activity, permission 배열, requestCode
                    activity, arrayOf(
                        android.Manifest.permission.READ_MEDIA_IMAGES,
                        android.Manifest.permission.READ_MEDIA_VIDEO),
                    1
                )
                Log.d(TAG, "permissionGallery: 여기니?")
                true
            } else {
                false
            }
            // 33 미만일 때
        } else {
            val writePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)

            return if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(  // activity, permission 배열, requestCode
                    activity, arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                Log.d(TAG, "permissionGallery: 여기니?")
                true
            } else {
                false
            }
        }
    }

    /*
    파일 경로를 찾는 함수
    */
    private fun getFilePathUri(uri: Uri) : String{
        val buildName = Build.MANUFACTURER

        // 샤오미 폰은 바로 경로 반환 가능
        if (buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)

        if (cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    /*
    observe로 관찰하다가, 새로운 사진이 들어올때 동그랗게 크롭해서 원래 frame에 넣어주기
     */
    private fun observePicture(){
        myPageViewModel.urlLiveData.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(filePath)
                .circleCrop()
                .into(binding.ivMypageProfileImg)
        }
    }

    /*
    이메일 수정 로직
    이메일et를 새로 치고, 이메일 수정 버튼을 누름
    */


    // 이메일이 비어있는지 확인하는 로직
    private fun validateEmail(email:String): Boolean{
        if (email.isBlank()){
            Toast.makeText(context,"이메일을 작성해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*
    로그아웃 로직
    */

    // 해야할 일: 첫화면으로 돌아가기는 하는데, 로그인은 유지되어서.. 다시 마이페이지에 들어갈수 있음.
    private fun logoutMyAccount(){
        val btnLogout = binding.btnMypageLogout
        btnLogout.setOnClickListener{
            ApplicationClass.sharedPreferencesUtil.deleteToken()
            var intent = Intent(requireContext(),LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
