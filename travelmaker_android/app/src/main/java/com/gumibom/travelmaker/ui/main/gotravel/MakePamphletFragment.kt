package com.gumibom.travelmaker.ui.main.gotravel

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.NOT_ALLOW_TITLE
import com.gumibom.travelmaker.databinding.FragmentMakePamphletBinding
import com.gumibom.travelmaker.databinding.FragmentPamphletWelcomeBinding
import com.gumibom.travelmaker.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MakePamphletFragment_싸피"
@AndroidEntryPoint
class MakePamphletFragment : Fragment() {

    private var _binding: FragmentMakePamphletBinding? = null
    private val binding get() = _binding!!
    lateinit var getResult: ActivityResultLauncher<Intent>
    private var filePath = ""
    private val mainViewModel : MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // contentResolver로 filePath를 받는다.
                filePath = getFilePathUri(it.data?.data!!)
                mainViewModel.setPamphletThumbnail(filePath)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakePamphletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPamphletTitle()
        moveStartPamphlet()
        uploadPamphletThumbnail()
        observeLiveData()
    }

    /**
     * Glide로 앱에 사진 업로드
     */
    private fun observeLiveData() {
        mainViewModel.pamphletThumbnail.observe(viewLifecycleOwner) { imageUrl ->
            binding.ivPictureAdd.visibility = View.GONE
            Glide.with(this)
                .load(imageUrl)
                .into(binding.btnPictureAdd)
        }
    }

    private fun moveStartPamphlet() {
        binding.btnMakePamphletNext.setOnClickListener {
            if (binding.tiePamphletTitle.error == null && binding.tiePamphletTitle.text!!.isNotEmpty() && filePath.isNotEmpty()) {
                // 제목을 viewModel에 저장
                mainViewModel.pamphletTitle = binding.tiePamphletTitle.text.toString()

                Navigation.findNavController(it).navigate(R.id.action_makePamphletFragment_to_startPamphletFragment)
            } else {
                Toast.makeText(requireContext(), NOT_ALLOW_TITLE, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 팜플렛 제목의 유효성 검사
     */
    private fun checkPamphletTitle() {
        binding.tiePamphletTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val title = binding.tiePamphletTitle.text.toString()

                if (title.length > 10) {
                    binding.tiePamphletTitle.error = "최대 10글자만 입력해주세요."
                } else {
                    binding.tiePamphletTitle.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    /**
     * 팜플렛 썸네일을 등록하는 함수
     */
    private fun uploadPamphletThumbnail() {
        binding.ivPictureAdd.setOnClickListener {
            if (!permissionGallery()) {
                val intent  = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getResult.launch(intent)
            }
        }

        binding.btnPictureAdd.setOnClickListener {
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
                    requireActivity(), arrayOf(
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
                    requireActivity(), arrayOf(
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
}