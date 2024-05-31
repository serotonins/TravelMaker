package com.gumibom.travelmaker.ui.signup.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentSignupProfileBinding
import com.gumibom.travelmaker.model.RequestUserData
import com.gumibom.travelmaker.ui.dialog.ClickEventDialog
import com.gumibom.travelmaker.ui.signup.SignupActivity
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

private const val TAG = "SignupProfileFragment_싸피"
@AndroidEntryPoint
class SignupProfileFragment : Fragment() {
    private val imagePickCode = 1000
    private val cameraRequestCode = 1002
    private var _binding : FragmentSignupProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var signupActivity: SignupActivity
    private val signupViewModel: SignupViewModel by activityViewModels()

    private lateinit var getImageResult : ActivityResultLauncher<Intent>
    private lateinit var getCameraResult : ActivityResultLauncher<Intent>

    private var filePath = ""
    private var cameraPath = ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach:11 ")
        //Activity 연결
        signupActivity = context as SignupActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // intent 결과를 받음
        getImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.data?.let { uri ->
                    filePath = getFilePathUri(uri)
                    signupViewModel.profileImage = filePath

                    Glide.with(this)
                        .load(uri)
                        .transform(CenterCrop()) // Apply center crop to maintain aspect ratio
                        .transform(CenterCrop()) // Apply center crop to maintain aspect ratio
                        .into(binding.ivProfile)
                }
            }
        }

        getCameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val bitmap = data?.extras?.get("data") as? Bitmap

                bitmap?.let { bitmap ->
                    cameraPath = getAbsolute(bitmap)
                    signupViewModel.profileImage = cameraPath

                    Glide.with(this)
                        .load(bitmap)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                        .transform(CenterCrop()) // Apply center crop to maintain aspect ratio
                        .into(binding.ivProfile)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupProfileBinding.inflate(inflater, container, false)
        return binding.root
        //inflater.inflate(R.layout.fragment_profile_signup, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setInit()
        selectPicture()
        selectCategory()
        observeViewModel()
        backAndNextNaviBtn()
        // 회원가입 완료 버튼을 누를 때 sharedPreference에 email이 null이 아니면 저장
    }

    private fun setInit() {
        binding.tvProfileNickname.text = signupViewModel.nickname
    }

    private fun backAndNextNaviBtn(){
        binding.tvSignupProfileNext.setOnClickListener {
            if (signupViewModel.profileImage.isNotEmpty() && signupViewModel.categoryList.isNotEmpty()) {
                val requestUserData = RequestUserData(
                    signupViewModel.loginId,
                    signupViewModel.password,
                    signupViewModel.nickname,
                    signupViewModel.email,
                    signupViewModel.selectTown,
                    signupViewModel.selectGender,
                    signupViewModel.selectBirthDate,
                    signupViewModel.phoneNumber,
                    signupViewModel.selectNation,
                    signupViewModel.categoryList,
                )
                signupViewModel.signup(requestUserData)
            }
        }

        binding.tvSignupProfilePrevious.setOnClickListener {
            signupActivity.navigateToPreviousFragment()
        }


    }
    private fun observeViewModel() {
        signupViewModel.isSignup.observe(viewLifecycleOwner){
            if (it.isSuccess){//회원가입이 성공했다면? 화면전환
                signupActivity.navigateToNextFragment()
            }else{
                Toast.makeText(activity, "회원가입을 실패했습니다. ", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun selectPicture(){
        //+버튼 클릭 시
        binding.ivProfileAdd.setOnClickListener {
            //사진 엘범이 열려야 됨,
            val pictureDialog = ClickEventDialog(signupActivity)
            pictureDialog.setTitle("사진 선택 유형을 선택해주세요.")
            pictureDialog.setContent("")
            val pictureDialogItems = arrayOf( "엘범에서 선택","카메라 촬영","사진 삭제")
            pictureDialog.setItems(pictureDialogItems) { _, which ->
                when (which) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePhotoFromCamera()
                    2 -> deletePhotoFromProfile()
                }
            }
            //권한 체크

            pictureDialog.show()
        }
    }


    private fun deletePhotoFromProfile() {
        binding.ivProfile.setBackgroundResource(R.drawable.ic_empty_profile_circle)
        binding.ivProfile.setImageResource(R.drawable.ic_empty_profile_circle)
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        getImageResult.launch(galleryIntent)
    }
    private fun takePhotoFromCamera() {
        //권한을 설정하기
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        getCameraResult.launch(cameraIntent)
    }
    private fun getAbsolute(bitmap: Bitmap) : String {
        // bitmap을 절대 경로 파일에 저장
        val timestamp = System.currentTimeMillis()  // 중복을 피하기 위해 현재 시간을 넣어줌
        val thumbnailFileName = "thumbnail_$timestamp.jpg"

        val thumbnailFile = File(requireContext().cacheDir, thumbnailFileName) /**.절대 로하면
         절대 값 주소나오니까 그걸 저장해뒀다가 유저데이터에 담아서 올리기 .
         */
        val fos = FileOutputStream(thumbnailFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()

        return thumbnailFile.absolutePath
    }

    //빼올 떄?

    private fun getFilePathUri(uri: Uri) : String{ //URI를 String으로
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

    /**
     * 카테고리를 설정하는 함수
     */
    private fun selectCategory() {

        val chipMap = mapOf<String, String>("맛집" to "taste", "힐링" to "healing", "문화" to "culture", "활동" to "active",
            "사진" to "picture", "자연" to "nature", "쇼핑" to "shopping", "휴식" to "rest")

        val chipGroup1 = binding.chipGroup1
        val chipGroup2 = binding.chipGroup2

        for (index in 0 until chipGroup1.childCount) {
            val chip = chipGroup1.getChildAt(index) as? Chip
            chip?.setOnClickListener {
                val chipText = chipMap.getValue(chip.text.toString())
                // Chip 클릭 시 실행할 코드
                if (chip.isChecked) {
                    signupViewModel.categoryList.add(chipText)
                    Log.d(TAG, "selectCategory: ${signupViewModel.categoryList}")
                } else {
                    signupViewModel.categoryList.remove(chipText)
                }
            }
        }

        for (index in 0 until chipGroup2.childCount) {
            val chip = chipGroup2.getChildAt(index) as? Chip
            chip?.setOnClickListener {
                val chipText = chipMap.getValue(chip.text.toString())
                // Chip 클릭 시 실행할 코드
                if (chip.isChecked) {
                    signupViewModel.categoryList.add(chipText)
                } else {
                    signupViewModel.categoryList.remove(chipText)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}