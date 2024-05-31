package com.gumibom.travelmaker.ui.signup.location

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.KOREAN_PATTERN
import com.gumibom.travelmaker.databinding.ItemLocationListBinding
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.KakaoAddress
import com.gumibom.travelmaker.model.NaverAddress
import com.gumibom.travelmaker.ui.common.CommonViewModel
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.ui.main.findmate.meeting_post.MeetingPostViewModel
import com.gumibom.travelmaker.ui.signup.SignupViewModel
import java.util.regex.Pattern


private const val TAG = "SignupLocationAdapter_싸피"
class SignupLocationAdapter(private val context : Context,
                            private val viewModel : CommonViewModel)
    : ListAdapter<Address, SignupLocationAdapter.SignupLocationViewHolder>(SignupLocationDiffUtil()) {

    // 클릭한 아이템의 position을 갱신하기 위한 변수
    private var selectItemPosition = -1
    private var previousItem = -1

    private var onItemClickListener: ((Address) -> Unit)? = null

    inner class SignupLocationViewHolder(private val binding : ItemLocationListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Address, position : Int) {
            binding.tvItemLocationTitle.text = item.title!!
            binding.tvItemLocationAddress.text = item.address

            // 아이템을 선택했을 때 color 변경을 위한 변수
            val notSelectColor = ContextCompat.getColor(context, R.color.white)
            val selectColor = ContextCompat.getColor(context, R.color.gray_a25)

            binding.itemLocationLayout.setOnClickListener {

                previousItem = selectItemPosition

                // 만약 넘겨받은 viewModel이 MainViewModel이면
                if (viewModel is MainViewModel || viewModel is MeetingPostViewModel) {
                    onItemClickListener?.invoke(item)
                }
                // 클릭한 아이템이 이전에 선택한 아이템과 다르면 선택된 아이템으로 설정하고 배경색 변경
                if (position != selectItemPosition) {
                    selectItemPosition = position

                    // 변경된 아이템을 업데이트합니다.
                    notifyItemChanged(previousItem)
                    notifyItemChanged(position)


                    // 만약 넘겨받은 viewModel이 SignupViewModel이면
                    if (viewModel is SignupViewModel) {
                        // 선택한 아이템의 주소를 ViewModel에 저장
                        viewModel.selectTown = item.address

                        // 주소가 한국이면
                        if (isMatchingPattern(item.address, KOREAN_PATTERN)) {
                            viewModel.setAddress(item.address)
                            viewModel.selectNation = "Korea"
                        } else {
                            viewModel.selectNation = item.title
                        }
                    }

                } else {
                    // 이미 선택된 아이템을 다시 클릭하면 이전에 선택한 아이템과 현재 선택한 아이템을 초기화하고 배경색 변경
                    selectItemPosition = -1
                    notifyItemChanged(previousItem)
                    notifyItemChanged(position)

                    if (viewModel is SignupViewModel) {
                        // 선택한 아이템의 주소를 ViewModel에서 초기화
                        viewModel.setAddress("")
                        viewModel.selectTown = ""
                        viewModel.selectNation = ""
                    }
                }
            }

            // 배경색을 설정합니다. 선택된 아이템이면 다른 색으로 설정합니다.
            if (position == selectItemPosition) {
                binding.itemLocationLayout.setBackgroundColor(selectColor)
            } else {
                binding.itemLocationLayout.setBackgroundColor(notSelectColor)
            }
        }

        private fun isMatchingPattern(input: String, pattern: String): Boolean {
            val matcher = Pattern.compile(pattern).matcher(input)
            return matcher.matches()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignupLocationAdapter.SignupLocationViewHolder {
        val binding = ItemLocationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignupLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignupLocationViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    // 아이템 클릭 이벤트를 처리하는 함수를 설정하는 메서드
    fun setOnItemClickListener(listener: (Address) -> Unit) {
        onItemClickListener = listener
    }

}