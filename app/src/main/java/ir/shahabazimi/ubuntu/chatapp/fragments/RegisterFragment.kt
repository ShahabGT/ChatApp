package ir.shahabazimi.ubuntu.chatapp.fragments


import MySharedPreference
import MyUtils
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.button.MaterialButton
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.dialogs.ImageselectDialog
import ir.shahabazimi.ubuntu.chatapp.enqueue
import ir.shahabazimi.ubuntu.chatapp.models.ImageSelect
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : Fragment() {
    private lateinit var v: View
    private lateinit var registerBtn: MaterialButton
    private lateinit var image: SimpleDraweeView
    private lateinit var path: Uri
    private var bitmap: Bitmap?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        init()

        return v
    }


    private fun init() {
        registerBtn = v.findViewById(R.id.register_register)
        registerBtn.setOnClickListener {
            val n = v.findViewById<EditText>(R.id.register_name).text.toString()
            val u = v.findViewById<EditText>(R.id.register_username).text.toString()
            val p = v.findViewById<EditText>(R.id.register_password).text.toString()
            val e = v.findViewById<EditText>(R.id.register_email).text.toString()

            if (n.isBlank() || u.isBlank() || p.isBlank() || e.isBlank()) {
                Toast.makeText(context, "fill the form", Toast.LENGTH_LONG).show()
            } else {
                if (!MyUtils.isEmailValid(e)) {
                    Toast.makeText(context, "invalid email", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                } else if (e.length < 5) {
                    Toast.makeText(context, "short password", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                } else if (u.length < 5) {
                    Toast.makeText(
                        context,
                        "username must be more than 5 characters",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                } else {

                    if (!MyUtils.isInternetAvailable(context!!))
                        Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show()
                    else {
                        var token: String = MySharedPreference.getInstance(context!!).getFBToken()!!
                        if (token.isBlank())
                            token = FirebaseInstanceId.getInstance().token!!
                        registerBtn.isEnabled = false
                        register(n, u, e, p, token);


                    }


                }


            }


        }
        image = v.findViewById(R.id.register_logo);

        v.findViewById<ImageView>(R.id.register_logo_cam).setOnClickListener {

            ImageselectDialog(context!!){option->
                if(option== ImageSelect.Camera) {
                    if(checkPermission()!= PackageManager.PERMISSION_GRANTED){
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                          Toast.makeText(context,"to select from camera we need this permission",Toast.LENGTH_LONG).show()
                            requestPermission()
                        }else if(!MySharedPreference.getInstance(context!!).getCameraPermission()){
                            requestPermission()
                            MySharedPreference.getInstance(context!!).setCameraPermission()
                        }else{
                            val intent = Intent()
                            intent.action= Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri =Uri.fromParts("package",activity?.packageName,null);
                            intent.data=uri
                            startActivity(intent);
                        }

                    }else{
                        selectFromCamera()
                    }
                }else
                    selectFromGallery()
            }.show()
        }


    }

    private fun checkPermission()= checkSelfPermission(context!!, Manifest.permission.CAMERA)

    private fun requestPermission()= requestPermissions(Array(1){Manifest.permission.CAMERA},123)


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==123){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectFromCamera()
            }
        }
    }

    private fun toBase64(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)

    }

    private fun selectFromCamera() {



        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        try {
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                    context!!,
                    "ir.shahabazimi.ubuntu.chatapp.fileprovider",
                    createFile()
                )
            )
            startActivityForResult(intent, 123)
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    private fun selectFromGallery() {
        val intent = Intent()

        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "select an image"), 596);
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        val date = SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val a = File.createTempFile(
            MySharedPreference.getInstance(activity!!).getUser() + date, ".jpeg",
            activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        path = Uri.fromFile(a)
        return a

    }


    private fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        token: String
    ) {
        var image=""
        if(bitmap!=null)
            image = toBase64(bitmap!!)



        MyUtils.hideKeyboard(activity!!)
        RetrofitClient.getInstance().getApi()
            .doRegister(name, username, email, password, token, image)
            .enqueue {
                onResponse = {
                    registerBtn.isEnabled = true

                    if (it.isSuccessful) {

                        MySharedPreference.getInstance(context!!).setUser(username)
                        MySharedPreference.getInstance(context!!)
                            .setAccessToken(it.body()?.accessToken!!)
                        MySharedPreference.getInstance(context!!).setIsLogin()
                        FirebaseMessaging.getInstance().subscribeToTopic("chatapp_1")
                        Navigation.findNavController(v)
                            .navigate(R.id.action_registerFragment_to_mainActivity)
                        activity?.finish()


                    } else {

                        Toast.makeText(
                            context, when (it.code()) {
                                409 -> "username/email exists"
                                else -> "error! try again"
                            }, Toast.LENGTH_LONG
                        ).show()

                    }
                }

                onFailure = {
                    registerBtn.isEnabled = true
                    Toast.makeText(context, "faild! try again", Toast.LENGTH_LONG).show()
                }


            }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {

            if (data != null && requestCode == 596) {
                try {
                    CropImage.activity(data.data)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setAutoZoomEnabled(true)
                        .setAllowRotation(true)
                        .setAllowFlipping(true)
                        .setActivityTitle("Crop Image")
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .setMaxCropResultSize(1000, 1000)
                        .setMinCropResultSize(100, 100)
                        .start(context!!, this);
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            } else if (requestCode == 123) {
                try {
                    CropImage.activity(path)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setAutoZoomEnabled(true)
                        .setAllowRotation(true)
                        .setAllowFlipping(true)
                        .setActivityTitle("Crop Image")
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .setMaxCropResultSize(1000, 1000)
                        .setMinCropResultSize(100, 100)
                        .start(context!!, this);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                try {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(activity?.contentResolver, resultUri);
                        image.setImageURI(resultUri)


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }
    }


}
