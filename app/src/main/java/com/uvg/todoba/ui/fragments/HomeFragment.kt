package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseEvents
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.remote.firestore.FirestoreEventApiImpl
import com.uvg.todoba.data.repository.event.EventRepository
import com.uvg.todoba.data.repository.event.EventRepositoryImpl
import com.uvg.todoba.data.util.adapters.EventAdapter
import com.uvg.todoba.databinding.FragmentHomeBinding
import com.uvg.todoba.ui.viewmodels.EventViewModel
import com.uvg.todoba.ui.viewmodels.states.EventState
import com.uvg.todoba.util.dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home), EventAdapter.EventListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var eventList: MutableList<Event>
    private var isAllAddVisible : Boolean? = null
    private lateinit var repositoryEvent: EventRepository
    private lateinit var databaseEvents: DatabaseEvents
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseEvents = Room.databaseBuilder(
            requireContext(),
            DatabaseEvents::class.java,
            "eventsDB"
        ).build()

        repositoryEvent = EventRepositoryImpl(
            FirestoreEventApiImpl(Firebase.firestore),
            databaseEvents.eventDao()
        )
        isAllAddVisible = false
        binding.addCategoryAction.visibility = View.GONE
        binding.addEventAction.visibility = View.GONE
        binding.addeventActionText.visibility = View.GONE
        binding.addCategoryActionText.visibility = View.GONE

        viewModel = EventViewModel(repositoryEvent)

        setObservables()
        lifecycleScope.launch {
            viewModel.getEvents(getValueFromKey("user")!!)
        }
        setListeners()
    }

    private fun setObservables(){
        lifecycleScope.launchWhenStarted {
            viewModel.eventState.collectLatest { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: EventState) {
        when(state){
            is EventState.Updated -> {
                eventList = state.events.toMutableList()
                binding.recyclerViewHomeFragment.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewHomeFragment.setHasFixedSize(true)
                binding.recyclerViewHomeFragment.adapter = EventAdapter(eventList, this)
            }
            is EventState.Error -> {
                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_SHORT).show()
            }
            is EventState.Loading -> {
                Toast.makeText(
                    requireContext(),
                    "Loading...",
                    Toast.LENGTH_SHORT).show()
            }
            is EventState.Empty -> {
                Toast.makeText(
                    requireContext(),
                    "Empty",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setListeners() {
        binding.floatingActionButtonHomeFragmentAddEvent.setOnClickListener{
            (if (!isAllAddVisible!!){
                binding.floatingActionButtonHomeFragmentAddEvent.animate().rotation(90F).setDuration(300)
                binding.addEventAction.show()
                binding.addCategoryAction.show()
                binding.addeventActionText.visibility = View.VISIBLE
                binding.addCategoryActionText.visibility = View.VISIBLE
                binding.transparentLayout.visibility = View.VISIBLE
                true
            }else{
                binding.floatingActionButtonHomeFragmentAddEvent.animate().rotation(-90F).setDuration(300)
                binding.addEventAction.hide()
                binding.addCategoryAction.hide()
                binding.addeventActionText.visibility = View.GONE
                binding.addCategoryActionText.visibility = View.GONE
                binding.transparentLayout.visibility = View.GONE
                false
            }).also { isAllAddVisible = it }
        }
        binding.addCategoryAction.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateCategoryFragment())

        }

        binding.addEventAction.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateEventFragment())
        }

    }

    override fun onPlaceClicked(event: Event, position: Int) {
        requireView().findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsEventFragment(
                nombre = event.title,
                fecha = event.date,
                hora = event.time,
                categoria = event.category,
                ubicacion = event.location,
                comentarios = event.description,
            )
        )
    }

    private suspend fun getValueFromKey(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = requireContext().dataStore.data.first()
        return preferences[dataStoreKey]
    }
}