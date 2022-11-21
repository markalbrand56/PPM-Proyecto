package com.uvg.todoba.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseCategories
import com.uvg.todoba.data.local.database.DatabaseEvents
import com.uvg.todoba.data.local.entity.Category
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.remote.firestore.FirestoreCategoryApiImpl
import com.uvg.todoba.data.remote.firestore.FirestoreEventApiImpl
import com.uvg.todoba.data.repository.category.CategoryRepository
import com.uvg.todoba.data.repository.category.CategoryRepositoryImpl
import com.uvg.todoba.data.repository.event.EventRepository
import com.uvg.todoba.data.repository.event.EventRepositoryImpl
import com.uvg.todoba.databinding.FragmentCreateEventBinding

import com.uvg.todoba.ui.viewmodels.CategoryViewModel
import com.uvg.todoba.ui.viewmodels.EventViewModel
import com.uvg.todoba.ui.viewmodels.states.CategoryState
import com.uvg.todoba.ui.viewmodels.states.EventState
import com.uvg.todoba.util.dataStore
import com.uvg.todoba.util.getPreference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID


class CreateEventFragment : Fragment(R.layout.fragment_create_event) {
    private val args: CreateEventFragmentArgs by navArgs()
    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var categoryList: MutableList<Category>

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var databaseCategories: DatabaseCategories
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var eventRepository : EventRepository
    private lateinit var databaseEvents: DatabaseEvents
    private lateinit var eventViewModel: EventViewModel

    private lateinit var sp1 : 

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseCategories = Room.databaseBuilder(
            requireContext(),
            DatabaseCategories::class.java,
            "categoriesDB"
        ).build()
        categoryRepository = CategoryRepositoryImpl(
            FirestoreCategoryApiImpl(Firebase.firestore),
            databaseCategories.categoryDao()
        )
        categoryViewModel = CategoryViewModel(categoryRepository)

        databaseEvents = Room.databaseBuilder(
            requireContext(),
            DatabaseEvents::class.java,
            "eventsDB"
        ).build()
        eventRepository = EventRepositoryImpl(
            FirestoreEventApiImpl(Firebase.firestore),
            databaseEvents.eventDao()
        )
        eventViewModel = EventViewModel(eventRepository)

        lifecycleScope.launch {
            categoryViewModel.getCategories(requireContext().dataStore.getPreference("user", ""))
        }

        setObservables()
        setOnClickListeners()
        if(args.event != null){
            // LLENAR LA DATA
            // set title on textinputedittext
            binding.textInputTitle.setText(args.event!!.title)
            binding.textInputLugar.setText(args.event!!.location)
            binding.textInputComentario.setText(args.event!!.description)
        }
    }

    private fun setOnClickListeners() {
        binding.buttonCrearEvento.setOnClickListener {
            if(args.event != null){
                // UPDATE
                lifecycleScope.launch {
                    eventViewModel.updateEvent(
                        requireContext().dataStore.getPreference("user", ""),
                        Event(
                            id = args.event!!.id,
                            firestoreId = args.event!!.firestoreId,
                            title = binding.textInputTitle.text.toString(),
                            category = binding.spinnerCreateCategoryFragment.selectedItem.toString(),
                            date = binding.calendarCreateCategoryFragment.date.toString(),
                            time = "",
                            location = binding.textInputLugar.text.toString(),
                            description = binding.textInputComentario.text.toString(),
                        )
                    )
                }
            } else {
                // CREATE
                lifecycleScope.launch {
                    eventViewModel.addEvent(
                        requireContext().dataStore.getPreference("user", ""),
                        Event(
                            firestoreId = UUID.randomUUID().toString(),
                            title = binding.textInputTitle.text.toString(),
                            category = binding.spinnerCreateCategoryFragment.selectedItem.toString(),
                            date = binding.calendarCreateCategoryFragment.date.toString(),
                            time = "",
                            location = binding.textInputLugar.text.toString(),
                            description = binding.textInputComentario.text.toString(),
                        )
                    )
                }
            }
        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.categoryState.collectLatest { state ->
                handleCategoryState(state)
            }
        }
        lifecycleScope.launchWhenStarted {
            eventViewModel.eventState.collectLatest { state ->
                handleEventState(state)
            }
        }
    }

    private fun handleEventState(state: EventState) {
        when (state) {
            is EventState.Loading -> {
                Toast.makeText(
                    requireContext(),
                    "Loading",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is EventState.Updated -> {
                Toast.makeText(requireContext(), "Evento creado", Toast.LENGTH_SHORT).show()
                findNavController().navigate(CreateCategoryFragmentDirections.actionCreateCategoryFragmentToHomeFragment())
            }
            is EventState.Error -> {
                Toast.makeText(requireContext(), "Error al crear evento", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun handleCategoryState(state: CategoryState) {
        when (state) {
            is CategoryState.Updated -> {
                categoryList = state.categories.toMutableList()
                // ACA AÑADIR LA LISTA A EL SPINNER
            }
            is CategoryState.Error -> {
                Toast.makeText(
                    requireContext(),
                    "Error: ${state.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is CategoryState.Loading -> {
                Toast.makeText(
                    requireContext(),
                    "Loading...",
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