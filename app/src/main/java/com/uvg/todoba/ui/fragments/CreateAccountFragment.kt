package com.uvg.todoba.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uvg.todoba.R
import com.uvg.todoba.data.remote.firebase.FirebaseAuthApiImpl
import com.uvg.todoba.data.repository.auth.AuthRepository
import com.uvg.todoba.data.repository.auth.AuthRepositoryImpl
import com.uvg.todoba.databinding.FragmentCreateAccountBinding
import com.uvg.todoba.ui.viewmodels.SessionViewModel
import com.uvg.todoba.ui.viewmodels.states.SessionState
import com.uvg.todoba.util.dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    private lateinit var binding: FragmentCreateAccountBinding
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button


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
        button = view.findViewById(R.id.button_login)
        progressBar = view.findViewById(R.id.progress_circular)
        val authRepository = AuthRepositoryImpl(
            authAPI = FirebaseAuthApiImpl()
        )
        sessionViewModel = SessionViewModel(authRepository, requireContext())
        setListeners()
        setObservables()
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            sessionViewModel.sessionState.collectLatest { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: SessionState) {
        when(state){
            is SessionState.LoggedIn ->{
                findNavController().navigate(CreateAccountFragmentDirections.actionCreateAccountFragmentToHomeFragment())
            }
            is SessionState.Loading -> {
                button.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            else -> {
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

            }
        }
    }

    private fun setListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.inputLayoutCreateAccountFragmentEmail.editText!!.text.toString()
            val password = binding.inputLayoutCreateAccountFragmentPassword.editText!!.text.toString()

            sessionViewModel.signUp(email, password)
        }
    }

}