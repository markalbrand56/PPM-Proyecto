package com.uvg.todoba.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uvg.todoba.R
import com.uvg.todoba.data.remote.firebase.FirebaseAuthApiImpl
import com.uvg.todoba.data.repository.auth.AuthRepository
import com.uvg.todoba.data.repository.auth.AuthRepositoryImpl
import com.uvg.todoba.databinding.FragmentCreateAccountBinding
import kotlinx.coroutines.launch


class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    private lateinit var binding: FragmentCreateAccountBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authRepository = AuthRepositoryImpl(
            authAPI = FirebaseAuthApiImpl()
        )
        setListeners()
    }

    private fun setListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.inputLayoutCreateAccountFragmentEmail.editText!!.text.toString()
            val password = binding.inputLayoutCreateAccountFragmentPassword.editText!!.text.toString()

            lifecycleScope.launch{
                val response = authRepository.createAccountWithEmailAndPassword(email, password)
                if (response != null) {
                    findNavController().navigate(R.id.action_createAccountFragment_to_homeFragment)
                }else{
                    Toast.makeText(requireContext(), "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}