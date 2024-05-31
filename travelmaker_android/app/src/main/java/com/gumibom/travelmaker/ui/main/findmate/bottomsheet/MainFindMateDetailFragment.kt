package com.gumibom.travelmaker.ui.main.findmate.bottomsheet
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.databinding.FragmentMainFindMateDetailBinding
import com.gumibom.travelmaker.model.PostDetail
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
private const val TAG = "MainFindMateDetailFragm"
@AndroidEntryPoint
class MainFindMateDetailFragment(private val postDetailDTO: PostDetail) : BottomSheetDialogFragment() {
    private var _binding : FragmentMainFindMateDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity : MainActivity
    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferencesUtil

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
        sharedPreferences = SharedPreferencesUtil(activity)
        Log.d(TAG, "onAttach: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterView()
        setApplyGroup()
        observeLiveData()
//        setBottomSheet()
//        setBottomSheetUI()
    }
//    private fun setBottomSheetUI(){
//
//        //리사이클러 뷰 이미지와 카테고리 이미지를 어뎁터에 올리고 띄운다.
//        // chipAdapter 설정
//        val chipAdapter = chipAdapter(postDetailDTO.categories) // postDetailDTO에서 칩 리스트를 가져옵니다.
//        binding.rcChipList.apply {
//            adapter = chipAdapter
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
//        //image 3개를 리스트로 담는다.
//        val imageUrls = listOf(postDetailDTO.mainImgUrl, postDetailDTO.subImgUrl, postDetailDTO.thirdImgUrl)
//        var imageRealUrls : MutableList<String> = mutableListOf();
//        for (i in 0 .. 3){
//            if (imageUrls.get(i) != "" || imageUrls.get(i).isNullOrEmpty()){
//                imageRealUrls.add(imageUrls[i]);
//            }
//            Log.d(TAG, "setBottomSheetUI: ${imageUrls.get(i)}")
//            Log.d(TAG, "setBottomSheetUI: ${imageRealUrls.get(i)}")
//        }
//
//        val imageAdapter = ImageAdapter(imageRealUrls) // postDetailDTO에서 이미지 리스트를 가져옵니다.
//        binding.rcDetailPlaceImage.apply {
//            adapter = imageAdapter
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        }
//    }
//    private fun setBottomSheet(){
//        val standardBottomSheet = binding.bottomSheetLayout
//        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
//        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN;
//        standardBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
//                val halfHeight = screenHeight / 2
//                val currentTop = screenHeight - bottomSheet.top
//                val bottomToHalfSize = halfHeight+0/2;
//                val halfToTop = (halfHeight+screenHeight)/2;
//                when (currentTop) {
//                    in 0 until bottomToHalfSize -> standardBottomSheetBehavior.state =
//                        BottomSheetBehavior.STATE_COLLAPSED
//                    in bottomToHalfSize until halfToTop -> standardBottomSheetBehavior.state =
//                        BottomSheetBehavior.STATE_HALF_EXPANDED
//                    in halfToTop  until  screenHeight -> standardBottomSheetBehavior.state =
//                        BottomSheetBehavior.STATE_EXPANDED
//                }
//                Log.d(TAG, "onSlide: _ ${screenHeight}-${halfHeight}:${currentTop}-${bottomToHalfSize}-${halfToTop} : PKEEKEKEKEK")
//            }
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                    }
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                    }
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
//                    }
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                    }
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//                    }
//                    BottomSheetBehavior.STATE_SETTLING -> {
//                    }
//                }
//            }
//        })
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainFindMateDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun observeLiveData(){
        viewModel.isRequestSuccess.observe(viewLifecycleOwner){
            if (it.isSuccess){
                //요청 메세지를 성공적으로 보냈는가?
                Log.d(TAG, "observeLiveData: ${it.isSuccess.toString()}")
                Log.d(TAG, "observeLiveData: ${it.message}")
            }else{
                Log.d(TAG, "observeLiveData: ${it.message}")
            }
        }
    }

    private fun setAdapterView(){

    }
    private fun setApplyGroup(){
        binding.btnApplyGroup.setOnClickListener {
            //firebase 연동하기
            //그룹 id랑 현재 로그인 한 유저 id 전송하기
            Log.d(TAG, "setApplyGroup: ")
//          val userid = sharedPreferences.getUser().userId.toString()
            viewModel.requestGroup(FcmRequestGroupDTO(1,5))
            Log.d(TAG, "setApplyGroup: 2")

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
