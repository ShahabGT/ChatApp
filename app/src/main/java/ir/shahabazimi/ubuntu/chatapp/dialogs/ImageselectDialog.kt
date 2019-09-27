package ir.shahabazimi.ubuntu.chatapp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.models.ImageSelect

class ImageselectDialog(context:Context,var myInterface: (ImageSelect)->Unit) :Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_imageselect)

        findViewById<LinearLayout>(R.id.imageselect_camera).setOnClickListener {
            myInterface.invoke(ImageSelect.Camera)
            dismiss()
        }
        findViewById<LinearLayout>(R.id.imageselect_gallery).setOnClickListener {
            myInterface.invoke(ImageSelect.Gallery)
            dismiss()
        }

    }




}