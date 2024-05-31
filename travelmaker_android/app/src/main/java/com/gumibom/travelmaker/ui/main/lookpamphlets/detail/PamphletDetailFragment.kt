package com.gumibom.travelmaker.ui.main.lookpamphlets.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.gumibom.travelmaker.constant.NO_RECORD
import com.gumibom.travelmaker.databinding.FragmentPamphletBinding
import com.gumibom.travelmaker.databinding.FragmentPamphletDetailBinding
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.myrecord.detail.MyRecordDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PamphletDetailFragment_싸피"
@AndroidEntryPoint
class PamphletDetailFragment : Fragment() {

    private var _binding: FragmentPamphletDetailBinding? = null
    private val binding get() = _binding!!
    private var pamphletId : Long = 0
    private lateinit var activity: MainActivity
    private lateinit var callback: OnBackPressedCallback

    private lateinit var adapter : PamphletDetailAdapter
    private lateinit var recyclerView: RecyclerView

    private val myRecordDetailViewModel : MyRecordDetailViewModel by viewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pamphletId = arguments?.getLong("pamphletId") ?: 0

        // OnBackPressedCallback 인스턴스 생성 및 추가
        callback = object : OnBackPressedCallback(true) { // true는 콜백을 활성화 상태로 만듭니다.
            override fun handleOnBackPressed() {
                activity.navigationPop()
                activity.setOriginToolbar()
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
        _binding = FragmentPamphletDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeLiveData()
        recyclerViewEvent()
    }

    private fun setAdapter() {
        adapter = PamphletDetailAdapter(requireContext(), myRecordDetailViewModel)
        recyclerView = binding.rvPamphletDetail
        recyclerView.adapter = adapter

        /**
         * 리싸이클러뷰 슬라이딩을 페이지 고정처럼 설정
         */
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        myRecordDetailViewModel.getMyAllRecord(pamphletId)
    }

    private fun observeLiveData() {
        myRecordDetailViewModel.myAllRecord.observe(viewLifecycleOwner) { recordList ->
            adapter.submitList(recordList.toMutableList())
        }
    }

    /**
     * 리싸이클러뷰 아이템이 나타나고 사라짐을 감지하는 이벤트
     */
    private fun recyclerViewEvent() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val item = adapter.getItemAtPosition(visiblePosition)


                    if (visiblePosition != RecyclerView.NO_POSITION) {
                        val visibleHolder = recyclerView.findViewHolderForAdapterPosition(visiblePosition) as? PamphletDetailAdapter.PamphletDetailViewHolder

                        visibleHolder?.initializePlayer(item!!.videoUrl)

                        // 다른 아이템의 ExoPlayer를 정지합니다.
                        for (i in 0 until recyclerView.childCount) {
                            val child = recyclerView.getChildAt(i)
                            val holder = recyclerView.getChildViewHolder(child)
                            if (holder is PamphletDetailAdapter.PamphletDetailViewHolder && holder != visibleHolder) {
                                holder.releasePlayer()
                            }
                        }

                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}