package com.uvg.todoba.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
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
    private var options : ArrayList<String> = arrayListOf()

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var databaseCategories: DatabaseCategories
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var eventRepository : EventRepository
    private lateinit var databaseEvents: DatabaseEvents
    private lateinit var eventViewModel: EventViewModel

    private lateinit var timePicker: TimePicker

    private lateinit var sp1 : Spinner

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
        sp1 = view.findViewById(R.id.spinner_createCategoryFragment)
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

    @RequiresApi(Build.VERSION_CODES.M)
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
                            time = "${binding.editTextTimeCreateEventFragmentHoraEvento.hour} : ${binding.editTextTimeCreateEventFragmentHoraEvento.minute}",
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
                            time = "${binding.editTextTimeCreateEventFragmentHoraEvento.hour} : ${binding.editTextTimeCreateEventFragmentHoraEvento.minute}",
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
                findNavController().navigate(CreateEventFragmentDirections.actionCreateEventFragmentToHomeFragment())
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
                options = arrayListOf()
                for (item in categoryList) {
                    if (item.name.isNotEmpty()) {
                        options.add(item.name)
                    }
                }
                val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
                sp1.adapter = arrayAdapter
                sp1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        Toast.makeText(requireContext(), "option selected", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
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