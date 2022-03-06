package com.sofit.test.ui.activity.signu2

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivitySignup2Binding
import com.sofit.test.ui.activity.main.MainActivity
import com.sofit.test.ui.activity.signup.Signup2Navigator
import com.sofit.test.ui.activity.signup.Signup2ViewModel
import com.sofit.test.ui.base.BaseActivity
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class Signup2Activity : BaseActivity<ActivitySignup2Binding, Signup2ViewModel>(), Signup2Navigator,
    AdapterView.OnItemSelectedListener{

    @Inject
    lateinit var mSignup2ViewModel: Signup2ViewModel
    @Inject
    lateinit var sharedPreferences: AppPreferences
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_signup2

    override fun getViewModel(): Signup2ViewModel = mSignup2ViewModel

    private lateinit var auth: FirebaseAuth
    private var mUri: Uri? = null
    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    var data: ArrayList<String>? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    lateinit var imageUrlUpload: String

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        mSignup2ViewModel.setNavigator(this)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val storage = Firebase.storage

        // [START upload_create_reference]
        // Create a storage reference from our app
        storageRef = storage.reference
//        checkPermissionForImage()
        lblProfile.setOnClickListener {
            checkPermissionForImage()
        }

        // call api for data
        mSignup2ViewModel.viewModelScope.launch(Dispatchers.Main) {
            data = mSignup2ViewModel.fetchDataFromDatabase()
            initSpinner();
        }

    }

    fun initSpinner() {
        val aa = data?.let {
            ArrayAdapter(this, android.R.layout.simple_spinner_item,
                it.toArray()
            )

        }
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(spnCountry)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@Signup2Activity
            prompt = "Select your Country"
            gravity = Gravity.CENTER
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${data?.get(position)}")
            else -> {
                showToast(message = "Spinner 1 Position:${position} and language: ${data?.get(position)}")
            }
        }
    }

    private fun capturePhoto(){
        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
        if(capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "com.sofit.test.fileprovider",
                capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            lblProfile?.setImageBitmap(bitmap)
        }
        else {
            Toast.makeText(baseContext,"ImagePath is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = uri?.let { contentResolver.query(it, null, selection, null, null ) }
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        mUri?.let { contentResolver.openInputStream(it) })
                    lblProfile!!.setImageBitmap(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                        mUri = data?.data

                    }
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>
                                            , grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when(requestCode){
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else {
                    Toast.makeText(baseContext,"Unfortunately You are Denied Permission to Perform this Operataion.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if (uri != null) {
                if ("com.android.providers.media.documents" == uri.authority){
                    val id = docId.split(":")[1]
                    val selsetion = MediaStore.Images.Media._ID + "=" + id
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selsetion)
                } else if ("com.android.providers.downloads.documents" == uri.authority){
                    val contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                    imagePath = getImagePath(contentUri, null)
                }
            }
        }
        else if (uri != null) {
            if ("content".equals(uri.scheme, ignoreCase = true)){
                imagePath = getImagePath(uri, null)
            }
            else if ("file".equals(uri.scheme, ignoreCase = true)){
                imagePath = uri.path
            }
        }
        renderImage(imagePath)
    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    private val PERMISSION_CODE_READ = 123
    private val PERMISSION_CODE_WRITE = 1234

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                openGallery()
            }
        }
    }

    companion object {
        const val TAG = "Signup2Activity"
    }

    override fun createUser(name: String) {
        val uri = mUri
        val fileName = UUID.randomUUID().toString() +".jpg"
        val uploadTask =
            storageRef.child("images/$fileName")
        if (uri != null) {
            uploadTask.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrlUpload = it.toString()
                        Log.d(TAG, "onActivityResult: $imageUrlUpload")
                        createFirebaseUser(name)
                    }
                }

                .addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }


    }

    private fun createFirebaseUser(name: String) {
        val email=intent.getStringExtra("email")
        val password=intent.getStringExtra("password")
        val profilePic = imageUrlUpload
        val country:String = spnCountry.selectedItem.toString()
        auth.createUserWithEmailAndPassword(email!!,password!!)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap["userId"] = userId
                    hashMap["userName"] = name
                    hashMap["country"] = country
                    hashMap["profileImage"] = profilePic

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            sharedPreferences.setString(AppConstant.PREF_NAME, name)
                            sharedPreferences.setString(AppConstant.PREF_EMAIL, email)
                            sharedPreferences.setString(AppConstant.PREF_COUNTRY, country)
                            sharedPreferences.setString(AppConstant.PREF_PIC_URI, profilePic)
                            startActivity(Intent(this@Signup2Activity, MainActivity::class.java))
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            finish()
                        }
                    }
                }
            }
    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(
                        ins,
                        destinationFilename
                    )
                }
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

}