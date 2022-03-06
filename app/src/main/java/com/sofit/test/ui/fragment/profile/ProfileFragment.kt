package com.sofit.test.ui.fragment.profile

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.FragmentProfileBinding
import com.sofit.test.ui.base.BaseFragment
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup2.*
import kotlinx.android.synthetic.main.activity_signup2.edtUsername
import kotlinx.android.synthetic.main.activity_signup2.spnCountry
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(),ProfileNavigator, AdapterView.OnItemSelectedListener {
    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var sharedPreference: AppPreferences
    lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun getViewModel(): ProfileViewModel {return profileViewModel}

    override fun getLifeCycleOwner(): LifecycleOwner = this

    private lateinit var auth: FirebaseAuth
    private var mUri: Uri? = null
    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    var data: ArrayList<String>? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentProfileBinding = getViewDataBinding()
        profileViewModel.setNavigator(this)
        val name = sharedPreference.getString(AppConstant.PREF_NAME)
        val country = sharedPreference.getString(AppConstant.PREF_COUNTRY)
        edtUsername.setText(name)
        setUp()
    }

    private fun setUp() {
        // call api for data
        profileViewModel.viewModelScope.launch(Dispatchers.Main) {
            data?.add(0, "Select a Country");
            data = profileViewModel.fetchDataFromDatabase()
            initSpinner();
        }
    }

    fun initSpinner() {
        val aa = data?.let {
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                it.toArray()
            )

        }
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(spnCountry)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@ProfileFragment
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


    override fun updateUser(name: String) {

    }

    private fun showToast(context: Context = activity as Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}