package com.gumibom.travelmaker.ui.main.findmate.meeting_post

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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.constant.NOT_ENOUGH_INPUT
import com.gumibom.travelmaker.databinding.FragmentMeetingPostDateBinding
import com.gumibom.travelmaker.databinding.FragmentMeetingPostPictureTitleBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MeetingPostPictureFragment_싸피"
@AndroidEntryPoint
class MeetingPostPictureFragment : Fragment() {

    private var _binding: FragmentMeetingPostPictureTitleBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MeetingPostActivity
    private val meetingPostViewModel : MeetingPostViewModel by activityViewModels()

    lateinit var getResult: ActivityResultLauncher<Intent>
    private var filePath = ""
    private var isNextPage = false

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MeetingPostActivity
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
                meetingPostViewModel.addImageUrl(filePath)
            }
        }
        // OnBackPressedCallback 인스턴스 생성 및 추가
        callback = object : OnBackPressedCallback(true) { // true는 콜백을 활성화 상태로 만듭니다.
            override fun handleOnBackPressed() {
                activity.navigateToPreviousFragment()
            }
        }
        // OnBackPressedDispatcher에 콜백 추가
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetingPostPictureTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveNextFragment()
        movePreviousFragment()

        openGallery()
        setAdapter()

        // TODO 데이터 viewModel에 담기
    }

    private fun movePreviousFragment() {
        binding.btnMeetingPostPrevious.setOnClickListener {
            activity.navigateToPreviousFragment()
        }
    }

    /*
     * 이미지 리싸이클러뷰 초기화 하는 함수
     */
    private fun setAdapter() {
        val adapter = MeetingPostAdapter()
        binding.rvMeetingPostPicture.adapter = adapter

        meetingPostViewModel.urlLiveData.observe(viewLifecycleOwner) { list ->
            isNextPage = true
            // 사진은 최대 3장만 올릴 수 있음
            if (list.size >= 3) {
                binding.layoutPictureAdd.visibility = View.GONE
            }
            adapter.submitList(list.toMutableList())
        }
    }

    /*
     * 다음 버튼 클릭 시 다음 화면 페이지 전환
     */
    private fun moveNextFragment() {
        binding.btnMeetingPostNext.setOnClickListener {

            val title = binding.etMeetingPostPictureTitle.text.toString()
            val content = binding.etMeetingPostPictureContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty() && isNextPage) {
                meetingPostViewModel.title = title
                meetingPostViewModel.content = content
                activity.navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), NOT_ENOUGH_INPUT, Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*
     * 버튼 클릭 시 갤러리를 여는 함수
     */
    private fun openGallery() {
        Log.d(TAG, "openGallery: 불렀니?")
        binding.btnPictureAdd.setOnClickListener {
            if (!permissionGallery()) {
                val intent  = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getResult.launch(intent)
            }
        }

        binding.ivPictureAdd.setOnClickListener {
            if (!permissionGallery()) {
                val intent  = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getResult.launch(intent)
            }
        }
    }


    /**
     * 파일 경로를 찾는 함수
     */
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
     * 안드로이드 SDK 33 이상일 떄와 이하일 때를 구분하여 권한 체크
     */
    private fun permissionGallery() : Boolean{
        // 13 이상일 때
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
        // 13 이하일 때
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }
}