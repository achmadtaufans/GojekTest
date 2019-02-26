/**
 * @copyright ©2019 Onoff Insurance All rights reserved. Trade Secret, Confidential and Proprietary.
 *            Any dissemination outside of Onoff Insurance is strictly prohibited.
 */
package insure.onoff.views.fragments.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import insure.onoff.R
import insure.onoff.core.events.ShowToolbarEvent
import insure.onoff.core.events.UpdateToolbarNameEvent
import insure.onoff.databinding.FragmentRegisterOptionsBinding
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import insure.onoff.common.validation.FormFieldEmptyValidator
import insure.onoff.utilities.InjectorUtils
import insure.onoff.viewmodels.AuthViewModel
import org.greenrobot.eventbus.EventBus


/**
 * RegisterOptionsFragment
 *
 * This class function is responsible to display register manners views
 *
 * @author    Andika Kurniawan  <andikakurniawan@onoff.insure>
 */
class RegisterOptionsFragment : Fragment() {
    private lateinit var binding: FragmentRegisterOptionsBinding;
    private lateinit var viewModel: AuthViewModel;

    /*
     * To display fragment and configurate needed variables
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterOptionsBinding.inflate(inflater, container, false)

        EventBus.getDefault().post(UpdateToolbarNameEvent("Register"))
        EventBus.getDefault().post(ShowToolbarEvent(true))

        val bindingBtnRegisterEmail = binding.btnRegisterEmail

        //To send field and button to validating in FormFieldEmptyValidator class
        val editTextList = arrayOf<EditText>(binding.etMobileNumber)
        val textWatcher = FormFieldEmptyValidator(editTextList, binding.btnRegisterPhone, "PhoneNumberValidation")

        for (editText in editTextList) {
            editText.addTextChangedListener(textWatcher)
        }

        binding.btnRegisterEmail.setOnClickListener { view ->
            view.findNavController().navigate(R.id.actionRegisterEmailFirstFragment)
        }

        val factory = InjectorUtils.provideAuthViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java);

        binding.btnRegisterPhone.setOnClickListener { view ->
            val phoneNumber : String = "+62" + binding.etMobileNumber.text.toString();
            viewModel.checkExistedPhoneNumber(phoneNumber, "customer").observe(viewLifecycleOwner,
                Observer {
                    responseData ->
                        if (responseData != null) {
//                            if(responseData.status == true) {
//                                val direction = RegisterOptionsFragmentDirections.actionRegisterVerificationCode(phoneNumber)
//                                view.findNavController().navigate(direction);
//                            } else {
//                                binding.tvErrorMessage.visibility = View.VISIBLE;
//                            }
                            val direction = RegisterOptionsFragmentDirections.actionRegisterVerificationCode(phoneNumber)
                            view.findNavController().navigate(direction);
                        }
                }
            )
        }

        return binding.root
    }
}



