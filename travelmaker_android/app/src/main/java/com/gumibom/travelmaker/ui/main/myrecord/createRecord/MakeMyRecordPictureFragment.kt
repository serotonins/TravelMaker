package com.gumibom.travelmaker.ui.main.myrecord.createRecord

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
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.NOT_ENOUGH_INPUT
import com.gumibom.travelmaker.data.dto.request.RecordRequestDTO
import com.gumibom.travelmaker.databinding.FragmentMakeMyRecordPictureBinding
import com.gumibom.travelmaker.databinding.FragmentMyRecordDetailBinding
import com.gumibom.travelmaker.model.pamphlet.Emoji
import com.gumibom.travelmaker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MakeMyRecordPictureFrag_싸피"
@AndroidEntryPoint
class MakeMyRecordPictureFragment : Fragment() {

    private var _binding: FragmentMakeMyRecordPictureBinding? = null
    private val binding get() = _binding!!
    private val makeMyRecordViewModel : MakeMyRecordViewModel by viewModels()
    private lateinit var adapter : MyRecordEmojiAdapter
    private lateinit var activity: MainActivity

    lateinit var getResult: ActivityResultLauncher<Intent>
    private var pamphletId : Long = 0
    private var filePath = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pamphletId = arguments?.getLong("pamphletId") ?: 0
        Log.d(TAG, "makePictureRecord: $pamphletId")
        // intent 결과를 받음
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // contentResolver로 filePath를 받는다.
                filePath = getFilePathUri(it.data?.data!!)

                // ViewModel에 추가
                makeMyRecordViewModel.setMyRecordImage(filePath)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakeMyRecordPictureBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEmojiAdapter()
        openGallery()
        saveMyRecord()
        observeLiveData()
    }

    private fun observeLiveData() {
        makeMyRecordViewModel.myRecordImage.observe(viewLifecycleOwner) { imageUrl ->
            binding.ivPictureAdd.visibility = View.GONE

            Glide.with(this)
                .load(imageUrl)
                .into(binding.btnPictureAdd)
        }

        makeMyRecordViewModel.isSuccessMessage.observe(viewLifecycleOwner) { messaage ->
            Toast.makeText(requireContext(), messaage, Toast.LENGTH_SHORT).show()
            activity.backToNavigation()
        }
    }

    /**
     * 내 기록 저장
     */
    private fun saveMyRecord() {
        binding.btnMakeMyRecordPicture.setOnClickListener {
            val text = binding.etMakeMyRecordPictureText.text.toString()
            if (filePath.isNotEmpty() && text.isNotEmpty() && makeMyRecordViewModel.emojiText.isNotEmpty()) {
                makeMyRecord()

            } else {
                Toast.makeText(requireContext(), NOT_ENOUGH_INPUT, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeMyRecord() {
        val recordRequestDTO = RecordRequestDTO(
            pamphletId,
            binding.etMakeMyRecordPictureText.text.toString(),
            binding.etMakeMyRecordPictureText.text.toString(),
            makeMyRecordViewModel.emojiText
        )

        makeMyRecordViewModel.makeImageRecord(filePath, "", recordRequestDTO)
    }

    private fun openGallery() {
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

    private fun setEmojiAdapter() {
        val happyEmoji = Emoji(
            R.drawable.happy,
            R.drawable.ic_emoji_check
        )
        val smileEmoji = Emoji(
            R.drawable.smile,
            R.drawable.ic_emoji_check
        )
        val sosoEmoji = Emoji(
            R.drawable.soso,
            R.drawable.ic_emoji_check
        )
        val sadEmoji = Emoji(
            R.drawable.sad,
            R.drawable.ic_emoji_check
        )
        val angryEmoji = Emoji(
            R.drawable.angry,
            R.drawable.ic_emoji_check
        )

        val emojiList = listOf(happyEmoji, smileEmoji, sosoEmoji, sadEmoji, angryEmoji)

        adapter = MyRecordEmojiAdapter(requireContext(), makeMyRecordViewModel)
        binding.rvMyRecordDetailPictureEmoji.adapter = adapter

        adapter.submitList(emojiList)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}