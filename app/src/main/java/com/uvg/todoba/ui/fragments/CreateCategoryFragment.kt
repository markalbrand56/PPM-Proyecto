package com.uvg.todoba.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseCategories
import com.uvg.todoba.data.remote.firestore.FirestoreCategoryApiImpl
import com.uvg.todoba.data.repository.category.CategoryRepository
import com.uvg.todoba.data.repository.category.CategoryRepositoryImpl
import com.uvg.todoba.databinding.FragmentCreateCategoryBinding
import com.uvg.todoba.ui.viewmodels.CategoryViewModel
import com.uvg.todoba.ui.viewmodels.states.CategoryState
import com.uvg.todoba.util.dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class CreateCategoryFragment : Fragment(R.layout.fragment_create_category) {
    private lateinit var binding: FragmentCreateCategoryBinding
    private lateinit var repository: CategoryRepository
    private lateinit var database: DatabaseCategories
    private lateinit var viewModel: CategoryViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.button_createCategoryFragment_crearCategoria)
        progressBar = view.findViewById(R.id.progress_circular)
        database = Room.databaseBuilder(
            requireContext(),
            DatabaseCategories::class.java,
            "categoriesDB"
        ).build()

        repository = CategoryRepositoryImpl(
            FirestoreCategoryApiImpl(Firebase.firestore),
            database.categoryDao(),

        )
        viewModel = CategoryViewModel(repository)

        setObservables()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        binding.buttonCreateCategoryFragmentCrearCategoria.setOnClickListener {
            val categoryName = binding.inputLayoutCreateCategoryFragmentNombreEvento.editText?.text.toString()
            if (categoryName.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.addCategory(getValueFromKey("user")!!, categoryName)
                }
            } else {
                Toast.makeText(requireContext(), "El nombre de la categoría no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.categoryState.collectLatest {state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: CategoryState) {
        when(state) {
            is CategoryState.Loading -> {
                button.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            is CategoryState.Updated -> {
                button.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                findNavController().navigate(CreateCategoryFragmentDirections.actionCreateCategoryFragmentToHomeFragment())
            }
            is CategoryState.Error -> {
                Toast.makeText(
                    requireContext(),
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }
    private suspend fun getValueFromKey(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = requireContext().dataStore.data.first()
        return preferences[dataStoreKey]
    }

}