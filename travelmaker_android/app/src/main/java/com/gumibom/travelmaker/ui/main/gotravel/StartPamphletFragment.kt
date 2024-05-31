package com.gumibom.travelmaker.ui.main.gotravel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentMakePamphletBinding
import com.gumibom.travelmaker.databinding.FragmentStartPamphletBinding
import com.gumibom.travelmaker.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "StartPamphletFragment_싸피"
@AndroidEntryPoint
class StartPamphletFragment : Fragment() {

    private var _binding: FragmentStartPamphletBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartPamphletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectCategory()
        makePamphlet()
        observeMessage()
    }

    private fun observeMessage() {
        mainViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 팜플렛 카테고리를 선택하는 함수
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
                    mainViewModel.pamphletCategory.add(chipText)

                } else {
                    mainViewModel.pamphletCategory.remove(chipText)
                }
            }
        }

        for (index in 0 until chipGroup2.childCount) {
            val chip = chipGroup2.getChildAt(index) as? Chip
            chip?.setOnClickListener {
                val chipText = chipMap.getValue(chip.text.toString())
                // Chip 클릭 시 실행할 코드
                if (chip.isChecked) {
                    mainViewModel.pamphletCategory.add(chipText)
                } else {
                    mainViewModel.pamphletCategory.remove(chipText)
                }
            }
        }
    }

    /**
     * 팜플렛을 만드는 함수
     */
    private fun makePamphlet() {
        binding.btnPersonalPamphlet.setOnClickListener {
            lifecycleScope.launch {
                val toastMessage = mainViewModel.makePamphlet()
                Log.d(TAG, "makePamphlet: ${mainViewModel.pamphletCategory}")
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                Navigation.findNavController(it).navigate(R.id.action_startPamphletFragment_to_mainMyRecordFragment)
            }
        }
    }
}