package com.gumibom.travelmaker.ui.main.notification

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO
import com.gumibom.travelmaker.databinding.FragmentMainNotificationBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

private const val TAG = "MainNotificationFragmen"
@AndroidEntryPoint
class MainNotificationFragment : Fragment() {
    private var _binding :FragmentMainNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity : MainActivity
    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferencesUtil
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
        sharedPreferences = SharedPreferencesUtil(activity)
    }
    private lateinit var mainFcmNotifyAdapter: MainFcmNotifyAdapter
    private lateinit var mainFcmSentNotifyAdapter: MainFcmNotifySentAdapter
    private lateinit var getNotifyList:FcmGetNotifyListDTO

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        mainFcmNotifyAdapter = MainFcmNotifyAdapter(activity, viewModel)
        mainFcmSentNotifyAdapter = MainFcmNotifySentAdapter(activity,viewModel)

        var isSent : Boolean = true;
        binding.btnMyResponseList.setOnClickListener {
            isSent= false;
            it.isSelected = true
            binding.btnMyRequireList.isSelected = false
            loadRecyclerView(isSent)
        }
        binding.btnMyRequireList.setOnClickListener {
            it.isSelected = !it.isSelected
            isSent = true
            it.isSelected = true
            binding.btnMyResponseList.isSelected = false
            loadRecyclerView(isSent)
        }

//      setupTabLayout()

    }
    private fun loadRecyclerView(isSent:Boolean){
        if (isSent){//알림을 요청 받은 리스트를 띄운다
            binding.rcNotifyList.adapter = mainFcmSentNotifyAdapter
            binding.rcNotifyList.layoutManager = LinearLayoutManager(context)
            if (getNotifyList.sentRequests != null){
                mainFcmSentNotifyAdapter.submitList(getNotifyList.sentRequests)
            }
        }else{//알림을 요청보낸 리스트를 띄운다.
            binding.rcNotifyList.adapter = mainFcmNotifyAdapter
//            binding.rcNotifyList.layoutManager = LinearLayoutManager(context).apply { reverseLayout = true }
            binding.rcNotifyList.layoutManager = LinearLayoutManager(context)
            if (getNotifyList.receivedRequests != null){

                mainFcmNotifyAdapter.submitList(getNotifyList.receivedRequests)
            }
        }
    }
    private fun observeLiveData(){
        viewModel.user.observe(viewLifecycleOwner){
            viewModel.getNotifyList(it.userId)
        }
        viewModel.isRequestAcceptCrew.observe(viewLifecycleOwner){ event ->
            val isSuccess = event.getContentIfNotHandled()

            if (isSuccess != null && isSuccess){
                Toast.makeText(activity, "참여요청을 수락했습니다.", Toast.LENGTH_SHORT).show()
            }else if (isSuccess != null && !isSuccess){
                Toast.makeText(activity, "서버와 통신을 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isRequestRefuseCrew.observe(viewLifecycleOwner){ event ->
            val isSuccess = event.getContentIfNotHandled()

            Log.d(TAG, "observeLiveData: $isSuccess")
            if (isSuccess != null && isSuccess){
                Toast.makeText(activity, "참여요청을 거절했습니다.", Toast.LENGTH_SHORT).show()
            }else if (isSuccess != null && !isSuccess){
                Toast.makeText(activity, "서버와 통신을 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isGetNotifyList.observe(viewLifecycleOwner){
            if (it!=null){
                Log.d(TAG, "observeLiveData: GDGD")
                getNotifyList = it
                Log.d(TAG, "observeLiveData: ${getNotifyList.toString()}")
                binding.rcNotifyList.adapter = mainFcmNotifyAdapter
                binding.rcNotifyList.layoutManager = LinearLayoutManager(context)
                mainFcmNotifyAdapter.submitList(getNotifyList.receivedRequests)
                binding.btnMyResponseList.isSelected = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}