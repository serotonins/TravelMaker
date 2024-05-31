package com.gumibom.travelmaker.ui.main.lookpamphlets

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentPamphletBinding
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.ui.main.myrecord.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainLookPamphletsFragme_싸피"
@AndroidEntryPoint
class MainLookPamphletsFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentPamphletBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel : MainViewModel by activityViewModels()
    private val lookPamphletViewModel : LookPamphletViewModel by viewModels()
    private var userId : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPamphletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInit()
        setAdapter()
    }

    private fun setInit() {
        userId = mainViewModel.user.value!!.userId
        lookPamphletViewModel.getOtherPamphletList(userId)
    }

    /**
     * 리싸이클러뷰 세팅
     */
    private fun setAdapter() {
        val adapter = LookPamphletAdapter(requireContext()).apply{
            itemClickListener = this@MainLookPamphletsFragment
        }
        binding.rvPamphlet.adapter = adapter

        lookPamphletViewModel.otherPamphlet.observe(viewLifecycleOwner) { pamphletList ->
            adapter.submitList(pamphletList)
        }
    }

    override fun moveRecordDetail(pamphletId: Long, view: View) {
        Log.d(TAG, "moveRecordDetail: 요기는??")
        val bundle = bundleOf("pamphletId" to pamphletId)
        Navigation.findNavController(view).navigate(R.id.action_mainLookPamphletsFragment_to_pamphletDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}