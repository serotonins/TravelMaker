package com.gumibom.travelmaker.ui.main.myrecord.createRecord

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.data.dto.request.RecordRequestDTO
import com.gumibom.travelmaker.databinding.FragmentMakeMyRecordPictureBinding
import com.gumibom.travelmaker.databinding.FragmentMakeMyRecordVideoBinding
import com.gumibom.travelmaker.model.pamphlet.Emoji
import com.gumibom.travelmaker.ui.common.DialogLoading
import com.gumibom.travelmaker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Float.min

private const val TAG = "MakeMyRecordVideoFragme_싸피"
@AndroidEntryPoint
class MakeMyRecordVideoFragment : Fragment() {

    private var _binding: FragmentMakeMyRecordVideoBinding? = null
    private val binding get() = _binding!!

    private val makeMyRecordViewModel : MakeMyRecordViewModel by viewModels()
    private lateinit var adapter : MyRecordEmojiAdapter
    lateinit var getResult: ActivityResultLauncher<Intent>

    private var pamphletId : Long = 0
    private var thumbnailImagePath = ""
    private var filePath = ""
    private lateinit var activity: MainActivity

    private lateinit var dialogLoading: DialogLoading

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pamphletId = arguments?.getLong("pamphletId") ?: 0

        // intent 결과를 받음
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.data?.let { uri ->
                    val thumbnailBitmap = getVideoThumbnail(uri)

                    setThumbnailImage(thumbnailBitmap)
                    filePath = getVideoPathUri(uri)
                }
            }
        }

    }

    private fun setThumbnailImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            binding.ivPictureAdd.visibility = View.GONE

            Glide.with(this)
                .asBitmap()
                .load(bitmap)
                .into(binding.btnPictureAdd)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakeMyRecordVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogLoading = DialogLoading(requireContext())

        setEmojiAdapter()
        openGallery()
        sendVideoToServer()
        observeLiveData()
    }

    private fun loadingVideo() {
        dialogLoading.show()
    }

    private fun dismissDialog() {
        dialogLoading.dismiss()
    }

    private fun observeLiveData() {
        makeMyRecordViewModel.isSuccessMessage.observe(viewLifecycleOwner) { messaage ->
            dismissDialog()

            Toast.makeText(requireContext(), messaage, Toast.LENGTH_SHORT).show()
            activity.backToNavigation()
        }
    }

    /**
     * 썸네일 이미지와 비디오를 서버에 전송
     */
    private fun sendVideoToServer() {
        binding.btnMakeMyRecordVideo.setOnClickListener {
            val text = binding.etMakeMyRecordVideoText.text.toString()

            if (filePath.isNotEmpty() && text.isNotEmpty() && makeMyRecordViewModel.emojiText.isNotEmpty()) {
                makeMyRecord()
                loadingVideo()
            }
        }

    }

    /**
     * 서버에 필요한 request를 만드는 함수
     */
    private fun makeMyRecord() {
        val recordRequestDTO = RecordRequestDTO(
            pamphletId,
            binding.etMakeMyRecordVideoText.text.toString(),
            binding.etMakeMyRecordVideoText.text.toString(),
            makeMyRecordViewModel.emojiText
        )

        makeMyRecordViewModel.makeImageRecord(thumbnailImagePath, filePath, recordRequestDTO)


    }


    private fun openGallery() {
        binding.btnPictureAdd.setOnClickListener {
            if (!permissionGallery()) {
                val videoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                getResult.launch(videoIntent)
            }

        }

        binding.ivPictureAdd.setOnClickListener {
            if (!permissionGallery()) {
                val videoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                getResult.launch(videoIntent)
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

    /**
     * 파일 경로를 찾는 함수
     */
    private fun getVideoPathUri(uri: Uri) : String{
        val buildName = Build.MANUFACTURER

        // 샤오미 폰은 바로 경로 반환 가능
        if (buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        var cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)

        if (cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }

    /**
     * 영상의 1초 시간을 bitmap으로 반환하는 함수
     */
    private fun getVideoThumbnail(uri : Uri) : Bitmap? {
        val thumbnailTime = 1
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(requireContext(), uri)
            // 마이크로 시간으로 계산해서 10^6을 곱해줘야됨.
            val bitmap = retriever.getFrameAtTime((thumbnailTime * 1000000).toLong(), MediaMetadataRetriever.OPTION_CLOSEST)

            // bitmap을 절대 경로 파일에 저장
            val timestamp = System.currentTimeMillis()  // 중복을 피하기 위해 현재 시간을 넣어줌
            val thumbnailFileName = "thumbnail_$timestamp.jpg"

            val thumbnailFile = File(requireContext().cacheDir, thumbnailFileName)
            val fos = FileOutputStream(thumbnailFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            thumbnailImagePath = thumbnailFile.absolutePath
            Log.d(TAG, "getVideoThumbnail: $thumbnailImagePath")
            return bitmap
        } catch (e : IllegalArgumentException){
            e.printStackTrace()
            Log.d(TAG, "IllegalArgumentException: ${e.message}")
        } catch (e : RuntimeException){
            e.printStackTrace()
            Log.d(TAG, "RuntimeException: ${e.message}")
        } catch (e : IOException) {
            Log.d(TAG, "IOException: ${e.message}")
        } finally {
            try {
                retriever.release()
            } catch (e : RuntimeException){
                e.printStackTrace()
                Log.d(TAG, "RuntimeException2: ${e.message}")
            }
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}