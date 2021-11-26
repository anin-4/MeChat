package com.example.chatapp.ui.addGroupDialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog
import com.example.chatapp.R

class AddGroupDialog(context: Context, private var addGroupButtonListener: AddGroupButtonListener):AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_group_dialog)
        val editText = findViewById<EditText>(R.id.groupNameEditTextDialog)
        val button = findViewById<Button>(R.id.groupNameAddButtonDialog)

        button?.setOnClickListener {
            val name = editText?.text.toString()
            addGroupButtonListener.onAddButtonListener(name)
            dismiss()
        }
    }
}