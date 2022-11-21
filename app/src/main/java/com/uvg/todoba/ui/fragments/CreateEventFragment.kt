package com.uvg.todoba.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
    private lateinit var titulo : TextInputEditText
    private lateinit var ubicacion : TextInputEditText
    private lateinit var comentario : TextInputEditText
    private lateinit var calendarView: CalendarView
    private lateinit var dateCalendar : String
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBar1: ProgressBar
    private lateinit var layout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp1 = view.findViewById(R.id.spinner_createCategoryFragment)
        titulo = view.findViewById(R.id.textInputTitle)
        ubicacion = view.findViewById(R.id.textInputLugar)
        comentario = view.findViewById(R.id.textInputComentario)
        calendarView = view.findViewById(R.id.calendar_createCategoryFragment)
        dateCalendar = covertDate(calendarView.date)
        button = view.findViewById(R.id.button_crear_evento)
        progressBar = view.findViewById(R.id.progress_circular)
        progressBar1 = view.findViewById(R.id.progress_circular2)
        layout = view.findViewById(R.id.transparent_Layout)
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
            if(comentario.text.isNullOrBlank()||titulo.text.isNullOrBlank()||ubicacion.text.isNullOrBlank()){
                Toast.makeText(requireContext(), "Ingrese todos los campos", Toast.LENGTH_LONG).show()
            }else {
                if (args.event != null) {
                    button.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        eventViewModel.updateEvent(
                            requireContext().dataStore.getPreference("user", ""),
                            Event(
                                id = args.event!!.id,
                                firestoreId = args.event!!.firestoreId,
                                title = binding.textInputTitle.text.toString(),
                                category = binding.spinnerCreateCategoryFragment.selectedItem.toString(),
                                date = dateCalendar,
                                time = "${binding.editTextTimeCreateEventFragmentHoraEvento.hour} : ${binding.editTextTimeCreateEventFragmentHoraEvento.minute}",
                                location = binding.textInputLugar.text.toString(),
                                description = binding.textInputComentario.text.toString(),
                            )
                        )
                    }
                } else {
                    button.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        eventViewModel.addEvent(
                            requireContext().dataStore.getPreference("user", ""),
                            Event(
                                firestoreId = UUID.randomUUID().toString(),
                                title = binding.textInputTitle.text.toString(),
                                category = binding.spinnerCreateCategoryFragment.selectedItem.toString(),
                                date = dateCalendar,
                                time = "${binding.editTextTimeCreateEventFragmentHoraEvento.hour} : ${binding.editTextTimeCreateEventFragmentHoraEvento.minute} ",
                                location = binding.textInputLugar.text.toString(),
                                description = binding.textInputComentario.text.toString(),
                            )
                        )
                    }
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
                layout.visibility = View.VISIBLE
                progressBar1.visibility = View.VISIBLE
            }
            is EventState.Updated -> {
                layout.visibility = View.VISIBLE
                progressBar1.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Evento creado", Toast.LENGTH_SHORT).show()
                findNavController().navigate(CreateEventFragmentDirections.actionCreateEventFragmentToHomeFragment())
            }
            is EventState.Error -> {
                layout.visibility = View.GONE
                progressBar1.visibility = View.GONE
                Toast.makeText(requireContext(), "Error al crear evento", Toast.LENGTH_SHORT).show()
            }
            else -> {layout.visibility = View.GONE
                progressBar1.visibility = View.GONE}
        }
    }

    private fun handleCategoryState(state: CategoryState) {
        when (state) {
            is CategoryState.Updated -> {
                layout.visibility = View.VISIBLE
                progressBar1.visibility = View.VISIBLE
                categoryList = state.categories.toMutableList()
                options = arrayListOf()
                for (item in categoryList) {
                    if (item.name.isNotEmpty()) {
                        options.add(item.name)
                    }
                }
                val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
                sp1.adapter = arrayAdapter

            }
            is CategoryState.Error -> {
                layout.visibility = View.GONE
                progressBar1.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Error: ${state.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is CategoryState.Loading -> {
                layout.visibility = View.VISIBLE
                progressBar1.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Loading...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {layout.visibility = View.GONE
                progressBar1.visibility = View.GONE}
        }
    }

    private suspend fun getValueFromKey(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = requireContext().dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private fun covertDate(time : Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        format.timeZone = TimeZone.getTimeZone("GTM")

        return format.format(date)
    }
}