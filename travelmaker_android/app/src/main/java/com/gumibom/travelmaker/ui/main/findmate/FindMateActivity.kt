package com.gumibom.travelmaker.ui.main.findmate

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.DENIED_LOCATION_PERMISSION
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.MarkerCategoryPositionRequestDTO
import com.gumibom.travelmaker.data.dto.request.MarkerPositionRequestDTO
import com.gumibom.travelmaker.databinding.ActivityMapBinding
import com.gumibom.travelmaker.model.MarkerPosition
import com.gumibom.travelmaker.model.PostDetail
import com.gumibom.travelmaker.ui.dialog.ClickEventDialog
import com.gumibom.travelmaker.ui.dialog.ClickEventProflleDialog
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.ui.main.findmate.bottomsheet.ImageAdapter
import com.gumibom.travelmaker.ui.main.findmate.bottomsheet.chipAdapter
import com.gumibom.travelmaker.ui.main.findmate.meeting_post.MeetingPostActivity
import com.gumibom.travelmaker.ui.main.findmate.search.FindMateSearchFragment
import com.gumibom.travelmaker.util.PermissionChecker
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notifyAll
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private const val TAG = "FindMateActivity_싸피"
@AndroidEntryPoint
class FindMateActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityMapBinding
    private lateinit var mMap : GoogleMap // 구글 맵
    private lateinit var fusedLocationClient: FusedLocationProviderClient // 효율적으로 위치정보를 제공
    private lateinit var locationCallback: LocationCallback
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private lateinit var findMateSearchFragment : FindMateSearchFragment
    private val mainViewModel : MainViewModel by viewModels()
    private var meetingId:Long =0;

    /**
     * 마커를 클릭했을 때 바텀 시트 다이얼로그 동작
     */

    private fun openMeetingDialog() {
        mMap.setOnMarkerClickListener { marker ->
            //바텀시트가 열리고 데이터를 받아서 띄운다.
            val markerPosition = marker.tag as MarkerPosition
            meetingId = markerPosition.id
            Log.d(TAG, "openMeetingDialog2: $meetingId")
            //아이디 넘겨서 데이터 받고
            mainViewModel.getPostDetail(meetingId)
            Log.d(TAG, "openMeetingDialog1: $meetingId")
            //뿌리기
            mainViewModel.postDTO.observe(this){
                var postDetail : PostDetail = it
                Log.d(TAG, "openMeetingDialog: ${postDetail.toString()}")
                settingBottomSheetUI(postDetail)
                setBottomSheet()
            }
            Log.d(TAG, "openMeetingDialog: $meetingId")
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesUtil = SharedPreferencesUtil(this@FindMateActivity)
        permissionChecker = PermissionChecker(this) // 퍼미션 체커 객체 생성
        findMateSearchFragment = FindMateSearchFragment(mainViewModel)
        googleMapInit()
        getLatLng()
        requestLocationUpdates()
        observeLivaData()
        selectPlace()
        selectCategory()
        moveMeetingPost()
        mainViewModel.getAllUser()

//        mainViewModel.
        /**
         *
         * */
    }
    private fun settingBottomSheetUI( postDetail : PostDetail){
        //이미지 클릭시 다이얼로그 띄운다.

        binding.bts.ivHeadProfile.setOnClickListener {
            val alertDialog = ClickEventProflleDialog(this@FindMateActivity)
            alertDialog.setTitle(postDetail.headNickname)
            alertDialog.setMessage("")
            alertDialog.clickDialogShow()
        }
1
        //리사이클러 뷰 이미지와 카테고리 이미지를 어뎁터에 올리고 띄운다.
        binding.bts.apply {
            tvRecruitTitle.text = postDetail.postTitle
            tvTravelPersonCnt.text = postDetail.numOfTraveler.toString()
            tvLocalPersonCnt.text = postDetail.numOfNative.toString()
            tvMaxPerson.text = (postDetail.numOfTraveler+postDetail.numOfNative).toString()
            btnRecruitState.text = "D"+postDetail.dday.toString()
            val startDateStr = postDetail.startDate.toString()
            val formattedDate = formatStartDateWithRegex(startDateStr)
            tvTripDay.text = formattedDate
            tvDetailPostContent.text = postDetail.postContent.toString()
            tvDetailPlaceTitle.text = postDetail.position.town+postDetail.position.name
            //Glide.with(this@FindMateActivity).load(postDetail.profileImgUrl).into(ivHeadProfile)
            Glide.with(this@FindMateActivity)
                .load(postDetail.profileImgUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .transform(CenterCrop())
                .into(ivHeadProfile)
        }

        val userId:Int =mainViewModel.user.value!!.userId.toInt();
        val postHeadId:Int =mainViewModel.postDTO.value!!.headId;
        if (userId==postHeadId){
            binding.bts.btnApplyGroup.text = "나의 모임입니다."
            binding.bts.btnApplyGroup.isEnabled = false;
        }else{
            binding.bts.btnApplyGroup.text = "모임 신청하기"
            binding.bts.btnApplyGroup.isEnabled = true
        }
        binding.bts.btnApplyGroup.setOnClickListener {
            //firebase 연동하기
            //다이얼로그 창 띄워서 한 번 더 확인하기.
            val alertDialog = ClickEventDialog(this@FindMateActivity)
            alertDialog.setTitle("잠시만요! \n 약속은 신뢰입니다!")
            alertDialog.setMessage("")
            alertDialog.setContent("모임 취소시 나의 신뢰도가 내려갈 수 있어요!")
            alertDialog.setPositiveBtnTitle("신청")
            alertDialog.setPositiveButtonListener {
                mainViewModel.requestGroup(FcmRequestGroupDTO(mainViewModel.user.value!!.userId,meetingId))
                Log.d(TAG, "setApplyGroup: ${mainViewModel.user.value!!.userId}")
                alertDialog.clickDialogDissMiss()
            }
            alertDialog.setNegativeBtnTitle("취소")
            alertDialog.setNegativeButtonListener {
                alertDialog.clickDialogDissMiss()
            }
            alertDialog.clickDialogShow()
            //그룹 id랑 현재 로그인 한 유저 id 전송하기

        }
        // chipAdapter 설정
        val chipAdapter = chipAdapter(postDetail.categories) // postDetailDTO에서 칩 리스트를 가져옵니다.
        binding.bts.rcChipList.apply {
            adapter = chipAdapter
            layoutManager = LinearLayoutManager(this@FindMateActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        //image 3개를 리스트로 담는다.
        val imageUrls = listOf(postDetail.mainImgUrl, postDetail.subImgUrl, postDetail.thirdImgUrl)
        var imageRealUrls : MutableList<String> = mutableListOf();
        for (i in 0 .. 2){
            if (imageUrls.get(i) != "" || !imageUrls.get(i).isNullOrEmpty()){
                imageRealUrls.add(imageUrls[i]);
            }
        }
        val imageAdapter = ImageAdapter(imageRealUrls) // postDetailDTO에서 이미지 리스트를 가져옵니다.
        binding.bts.rcDetailPlaceImage.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(this@FindMateActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setBottomSheet(){
        Log.d(TAG, "setBottomSheet: HIHIHISETBOTTOMSHEET!!")
        var isFirstClick:Boolean = true; 
        val standardBottomSheet = binding.bts.bottomSheetLayout
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        standardBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //if (!isUserDraggingBottomSheet) return // 사용자가 드래그하고 있지 않으면 리턴
                bottomSheet.postDelayed({
                    isFirstClick = false;
                    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                    val halfHeight = screenHeight / 2
                    val currentTop = screenHeight - bottomSheet.top
                    val bottomToHalfSize = halfHeight + 0 / 2
                    val halfToTop = (halfHeight + screenHeight) / 2
                    when (currentTop) {
                        in 0 until bottomToHalfSize -> standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        in bottomToHalfSize until halfToTop -> standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                        in halfToTop until screenHeight -> standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                },500)
                }
//             }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                isUserDraggingBottomSheet = newState == BottomSheetBehavior.STATE_DRAGGING || newState == BottomSheetBehavior.STATE_SETTLING
                Log.d(TAG, "onStateChanged: ${newState}")
            }
        })
    }

    /**
     * 모임 생성 화면으로 넘어가기
     */
    private fun moveMeetingPost() {
        binding.fabMapMeetingAdd.setOnClickListener {
            val intent = Intent(this, MeetingPostActivity::class.java)
            startActivity(intent)
        }
    }

    // 구글 맵 초기화
    private fun googleMapInit() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // fusedLocation 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val uiSettings = mMap.uiSettings
        // 나의 현재 위치를 파란점으로 표시
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // 권한이 없으면 설정창으로 이동한다.
            permissionChecker.moveToSettings()
        }
        // 줌 컨드롤러
        uiSettings.isZoomControlsEnabled = true

        // 줌 컨트롤러 위치 변경
        googleMap.setPadding(0, 360, 0, 250)

        openMeetingDialog()

    }

    /**
     * 내 현재 위치를 마커 없이 파란점만 표시하는 기능
     */
    fun setMyLocation(location : LatLng) {
        val zoomLevel = 15.0f // 줌 레벨을 조정하세요. 값이 클수록 더 가까워집니다.

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }

    /**
     * Google Map에 마커를 추가하고 해당 위치로 카메라 전환
     */
    private fun setMarker(location : LatLng, title : String) : Marker {
        val zoomLevel = 15.0f // 줌 레벨을 조정하세요. 값이 클수록 더 가까워집니다.

        val marker = mMap.addMarker(MarkerOptions().position(location).title(title))!!
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))

        return marker
    }

    /**
     * 현재 위치의 위도 경도를 받아오는 함수
     */
    private fun getLatLng() {
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        // 위치 권한이 있다면
        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){

            createLocationCallback()
            Log.d(TAG, "getLatLng: 위치권한이 있나?")
        }
        // 위치 권한이 없다면
        else {
            // 권한이 없으면 설정창으로 이동한다.
            permissionChecker.moveToSettings()
        }
    }

    /**
     * 위치 정보 콜백 함수를 만드는 함수
     */
    private fun createLocationCallback() {
        Log.d(TAG, "createLocationCallback: 콜백이 왜 안되지?")
        //          00:00:00
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let{
                    Log.d(TAG, "onResult: $it")
                    updateLocation(it.latitude, it.longitude)
                }
            }
        }
    }

    fun formatStartDateWithRegex(input: String): String {
        val regex = """(\d{4})-(\d{2})-(\d{2})T(\d{2}:\d{2})""".toRegex()

        val matchResult = regex.find(input) ?: throw IllegalArgumentException("Invalid input format")
        val (year, month, day, time) = matchResult.destructured

        val shortYear = year.takeLast(2)
        val monthInt = month.toInt()
        val dayInt = day.toInt()

        val timeValueList:List<String> = time.split(":")
        Log.d(TAG, "${timeValueList[0]}+ ${timeValueList[1]}")
        val timeInt=timeValueList[0].toInt()

        val suffix = when (dayInt) {
            1, 21, 31 -> "st"
            2, 22 -> "nd"
            3, 23 -> "rd"
            else -> "th"
        }

        // Format and return the final string
        return " ${shortYear}년 ${monthInt}월 ${dayInt}일 ${timeInt}시"
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = TimeUnit.MINUTES.toMillis(1) // 30분마다
        locationRequest.fastestInterval = TimeUnit.MINUTES.toMillis(1)
        locationRequest.smallestDisplacement = 100f // 100m마다
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            return
        } else {
            // 권한이 없으면 설정창으로 이동한다.
            permissionChecker.moveToSettings()
        }
    }

    private fun updateLocation(latitude : Double, longitude : Double) {
        mainViewModel.currentLatitude = latitude
        mainViewModel.currentLongitude = longitude

        mainViewModel.initLatitude = latitude
        mainViewModel.initLongitude = longitude

        val markerPositionRequestDTO = MarkerPositionRequestDTO(
            latitude, longitude, 3.0
        )
        Log.d(TAG, "currentLatitude: ${mainViewModel.currentLatitude}")
        Log.d(TAG, "currentLongitude: ${mainViewModel.currentLongitude}")
        Log.d(TAG, "initLatitude: ${mainViewModel.initLatitude}")
        Log.d(TAG, "initLongitude: ${mainViewModel.initLongitude}")
        mainViewModel.getMarkers(markerPositionRequestDTO)
//        setMyLocation(location)
    }

    private fun observeLivaData() {
        mainViewModel.isRequestSuccess.observe(this){
            if (it.isSuccess){
                //요청 메세지를 성공적으로 보냈는가?
                Log.d(TAG, "observeLiveData: ${it.isSuccess.toString()}")
                Log.d(TAG, "observeLiveData: ${it.message}")
            }else{
                Log.d(TAG, "observeLiveData: ${it.message}")
            }
        }
        // 현재 위치의 변화가 있으면 마커 리스트를 받아서 마커 표시
        mainViewModel.markerList.observe(this) { markerPosition ->
            Log.d(TAG, "observeLivaData: $markerPosition")
            // 내 위치 근방에 모임이 없다면
            if (markerPosition.isEmpty()) {
                val latitude = mainViewModel.currentLatitude
                val longitude = mainViewModel.currentLongitude

                val location = LatLng(latitude, longitude)
                setMyLocation(location)
            }  // 있다면
            else {
                for (marker in markerPosition) {

                    val location = LatLng(marker.latitude, marker.longitude)
                    Log.d(TAG, "observeLivaData: $location")
                    val googleMarker = setMarker(location, "title")
                    setMyLocation(LatLng(mainViewModel.currentLatitude, mainViewModel.currentLongitude))
                    googleMarker.tag = marker
                }
            }
        }
        mainViewModel.selectAddress.observe(this) { address ->
            // TODO 여기서 새롭게 받은 address로 서버한테 넘겨서 위치 재갱신 하기
            Log.d(TAG, "selectAddress: $address")
            val location = LatLng(address.latitude, address.longitude)

            val markerPositionRequestDTO = MarkerPositionRequestDTO(
                address.latitude, address.longitude, 3.0
            )
            mainViewModel.getMarkers(markerPositionRequestDTO)
            mainViewModel.currentLatitude = address.latitude
            mainViewModel.currentLongitude = address.longitude
            setMyLocation(location)
            binding.btnFindMatePlace.text = address.title
        }
    }
    /**
     * 위치를 선택하여 검색할 수 있는 바텀 시트 다이얼로그 show
     */
    private fun selectPlace() {
        binding.btnFindMatePlace.setOnClickListener {
            findMateSearchFragment.show(supportFragmentManager, "")
        }
    }


    /**
     * chip을 선택하고 필터링 버튼을 눌렀을 때 필터된 결과만 서버에서 가져옴
     */
    private fun selectCategory() {
        val taste = binding.chipMapTaste
        val healing = binding.chipMapHealing
        val culture = binding.chipMapCulture
        val active = binding.chipMapActive
        val picture = binding.chipMapPicture
        val nature = binding.chipMapNature
        val shopping = binding.chipShopping
        val rest = binding.chipRest

        val categoryList : List<Chip> = listOf(taste, healing, culture, active, picture, nature, shopping, rest)

        val chipMap = mapOf<String, String>("맛집" to "taste", "힐링" to "healing", "문화" to "culture", "활동" to "active",
            "사진" to "picture", "자연" to "nature", "쇼핑" to "shopping", "휴식" to "rest")

        binding.ivMapFiltering.setOnClickListener {
            val filterCategories = mutableListOf<String>()

            for (category in categoryList) {
                // chip이 선택되어 있으면
                if (category.isChecked) {
                    Log.d(TAG, "selectCategory: $filterCategories")
                    filterCategories.add(chipMap.getValue(category.text.toString()))
                }
            }
            Log.d(TAG, "selectCategory: $filterCategories")

            if (filterCategories.isEmpty()) {
                val markerPositionRequestDTO = MarkerPositionRequestDTO(
                    mainViewModel.currentLatitude, mainViewModel.currentLongitude, 3.0
                )
                clearMarkers()
                mainViewModel.getMarkers(markerPositionRequestDTO)
            } else {
                // TODO 여기서 서버 통신으로 필터링
                val markerCategoryPositionDTO = MarkerCategoryPositionRequestDTO(
                    mainViewModel.currentLatitude, mainViewModel.currentLongitude, 3.0, filterCategories
                )
                clearMarkers()
                mainViewModel.getCategoryMarkers(markerCategoryPositionDTO)
            }

        }
    }

    /**
     * 마커를 전부 제거하는 함수
     */
    private fun clearMarkers() {
        mMap.clear() // 현재 그려진 모든 마커를 제거합니다.
    }
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 100
    }
}