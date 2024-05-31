package com.gumibom.travelmaker.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.gumibom.travelmaker.R

private const val TAG = "ClickEventProflleDialog"
class ClickEventProflleDialog(context: Context):AlertDialog.Builder(context){
    private val dialog:Dialog = Dialog(context)
    override fun setItems(
        items: Array<out CharSequence>?,
        listener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        return super.setItems(items, listener)
    }

    init { //첫 시작 세팅
        val x = 1000.toInt()
        val y = 1500.toInt()
        val view = LayoutInflater.from(context).inflate(R.layout.click_event_profile_dialog,null)
        dialog.setContentView(view)
        Log.d(TAG, "settingBottomSheetUI: gdgd123")
        dialog.window?.setLayout(x,y);
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_click_event_backgound)

    }
    fun setTitle(title: String) {
        val titleView: TextView = dialog.findViewById(R.id.dialog_title_profile)
        titleView.text = title
    }

    fun setContent(content: String) {
        val contentView: TextView = dialog.findViewById(R.id.dialog_content)
        contentView.text = content
    }

    fun setIgnoreTitleContent() {
        val titleView: TextView = dialog.findViewById(R.id.dialog_title)
        val contentView: TextView = dialog.findViewById(R.id.dialog_content)

        titleView.visibility = View.GONE
        contentView.visibility = View.GONE
    }

    fun setPositiveBtnTitle(title :String){
        val btnView : Button = dialog.findViewById(R.id.dialog_content_yes)
        btnView.text= title
    }
    fun setPositiveButtonListener(listener: () -> Unit) {
        val yesButton: Button = dialog.findViewById(R.id.dialog_content_yes)
        yesButton.setOnClickListener {
            listener()
            dialog.dismiss()
        }
    }
    fun setNegativeBtnTitle(title :String){
        val btnView : Button = dialog.findViewById(R.id.dialog_content_no)
        btnView.text= title
    }
    fun setNegativeButtonListener(listener: () -> Unit) {
        val noButton: Button = dialog.findViewById(R.id.dialog_content_no)
        noButton.setOnClickListener {
            listener()
            dialog.dismiss()
        }
    }

    fun clickDialogShow(){
        dialog.show()
    }
    fun clickDialogDissMiss(){
        dialog.dismiss()
    }



}
