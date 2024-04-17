package de.syntax.androidabschluss.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.databinding.FragmentSignupBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel


class SignupFragment : Fragment() {
    private val viewModel: FirebaseViewModel by activityViewModels()

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupSignupButton.setOnClickListener {
            signup()
        }

        //navigate to Login
        binding.signupCancelButton.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        //observe current user to navigate
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(de.syntax.androidabschluss.R.id.homeFragment)
            }
        }

    }

    private fun signup() {
        val email = binding.signupMail.text.toString()
        val password = binding.signupPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.signup(email, password)
        }
    }
}