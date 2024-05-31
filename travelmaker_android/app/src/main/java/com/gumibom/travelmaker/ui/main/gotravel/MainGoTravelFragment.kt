package com.gumibom.travelmaker.ui.main.gotravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentMainFindMateBinding
import com.gumibom.travelmaker.databinding.FragmentPamphletWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainGoTravelFragment : Fragment() {

    private var _binding: FragmentPamphletWelcomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPamphletWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveMakePamphletFragment()
    }

    private fun moveMakePamphletFragment() {
        binding.btnPersonalPamphlet.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainGoTravelFragment_to_makePamphletFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}