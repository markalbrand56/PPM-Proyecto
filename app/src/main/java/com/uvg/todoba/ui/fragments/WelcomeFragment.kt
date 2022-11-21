package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uvg.todoba.R
import com.uvg.todoba.data.remote.firebase.FirebaseAuthApiImpl
import com.uvg.todoba.data.repository.auth.AuthRepositoryImpl
import com.uvg.todoba.databinding.FragmentWelcomeBinding
import com.uvg.todoba.ui.viewmodels.SessionViewModel
import com.uvg.todoba.ui.viewmodels.states.SessionState
import kotlinx.coroutines.flow.collectLatest


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authRepository = AuthRepositoryImpl(FirebaseAuthApiImpl())
        sessionViewModel = SessionViewModel(authRepository, requireContext())
        setObservables()
        setListeners()
        sessionViewModel.verifySession()
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            sessionViewModel.sessionState.collectLatest { state ->
                when (state) {
                    is SessionState.LoggedIn -> {
                        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
                    }
                    else->{}
                }

            }
        }
    }

    private fun setListeners() {
        binding.buttonLogin.setOnClickListener{
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }
        binding.buttonNewAccount.setOnClickListener{
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToCreateAccountFragment())
        }
    }
}