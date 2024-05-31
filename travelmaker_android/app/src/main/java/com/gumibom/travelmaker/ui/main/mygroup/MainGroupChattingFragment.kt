package com.gumibom.travelmaker.ui.main.mygroup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.FragmentMainGroupChattingBinding
import com.gumibom.travelmaker.model.chat.ChatData
import com.gumibom.travelmaker.ui.main.MainActivity
import com.gumibom.travelmaker.ui.main.MainViewModel
import com.gumibom.travelmaker.util.SharedPreferencesUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainGroupChattingFragment(): Fragment() {

    var id  = ""
    private var _binding : FragmentMainGroupChattingBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity :MainActivity
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllUser()
        viewModel.user.observe(viewLifecycleOwner){
            id = viewModel.user.value!!.nickname
            initView()
        }



    }
    fun getCurrentDateTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentTimeMillis
        }

        // Define the desired format
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }
    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.editText.clearFocus() // editText의 Focus를 잃게 한다.
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    private fun initView() {
        val listview: ListView = binding.list
        val userName = R.layout.group_chat_list_item
        val adapter: MyAdapter =
            MyAdapter(activity, userName)
        listview.adapter = adapter
        val editText: EditText = binding.editText
        val button: Button = binding.button
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

//      val myRef: DatabaseReference = database.getReference("message").child(hry)


        val groupId = arguments?.getLong("groupId",1)

        //여기서 bundle의 그룹아이디 값을 빼와서 그 쪽으로 넘어간다.
        val myRef: DatabaseReference = database.getReference("message").child(groupId.toString()+"No_Group")
        button.setOnClickListener { view: View? ->
            //시간으로 변환하는 로직
            val currentTime = getCurrentDateTime()
            val data = ChatData(id,viewModel.user.value!!.nickname,editText.text.toString(),currentTime )
            myRef.push().setValue(data)
            binding.editText.text.clear()
            hideKeyboard()

        }
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                adapter.add(snapshot.getValue(ChatData::class.java))
                listview.smoothScrollToPosition(adapter.getCount())
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
        sharedPreferencesUtil = SharedPreferencesUtil(activity)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainGroupChattingBinding.inflate(inflater,container,false)
        return binding.root
    }

    inner class MyAdapter(context: Context, var resource: Int) :
        ArrayAdapter<ChatData?>(context, resource) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View? = null
            view = if (convertView == null) {
                //최초호출. View 생성.
                val inflater = LayoutInflater.from(context)
                inflater.inflate(resource, null)
            } else {
                // 재사용한다.
                convertView
            }
            val data: ChatData? = getItem(position)
            if (data!!.id ==id) { //내가 쓴 글
                view!!.findViewById<View>(R.id.left_layer).visibility = View.GONE
                view.findViewById<View>(R.id.right_layer).visibility = View.VISIBLE
                val name = view.findViewById<TextView>(R.id.name)
                val message = view.findViewById<TextView>(R.id.message)
                val time = view.findViewById<TextView>(R.id.time)
                name.text = data.name
                message.text = data.message
                time.text = data.time.toString()

            } else { //남이 쓴글.
                view!!.findViewById<View>(R.id.left_layer).visibility = View.VISIBLE
                view.findViewById<View>(R.id.right_layer).visibility = View.GONE
                val name = view.findViewById<TextView>(R.id.left_name)
                val message = view.findViewById<TextView>(R.id.left_message)
                val time = view.findViewById<TextView>(R.id.left_time)
                name.text = data.name
                message.text = data.message
                time.text = data.time.toString()
            }
            return view
        }
    }


}