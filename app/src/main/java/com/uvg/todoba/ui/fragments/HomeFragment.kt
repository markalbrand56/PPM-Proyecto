package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseEvents
import com.uvg.todoba.data.local.entity.Event
import com.uvg.todoba.data.remote.firebase.FirebaseAuthApiImpl
import com.uvg.todoba.data.remote.firestore.FirestoreEventApiImpl
import com.uvg.todoba.data.repository.auth.AuthRepositoryImpl
import com.uvg.todoba.data.repository.event.EventRepository
import com.uvg.todoba.data.repository.event.EventRepositoryImpl
import com.uvg.todoba.data.util.adapters.EventAdapter
import com.uvg.todoba.databinding.FragmentHomeBinding
import com.uvg.todoba.ui.viewmodels.EventViewModel
import com.uvg.todoba.ui.viewmodels.SessionViewModel
import com.uvg.todoba.ui.viewmodels.states.EventState
import com.uvg.todoba.util.dataStore
import com.uvg.todoba.util.getPreference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home), EventAdapter.EventListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var eventList: MutableList<Event>
    private var isAllAddVisible : Boolean? = null
    private lateinit var repositoryEvent: EventRepository
    private lateinit var databaseEvents: DatabaseEvents
    private lateinit var eventViewModel: EventViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var mainToolbar: MaterialToolbar
    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.progressCircular
        isAllAddVisible = false
        binding.addCategoryAction.visibility = View.GONE
        binding.addEventAction.visibility = View.GONE
        binding.addeventActionText.visibility = View.GONE
        binding.addCategoryActionText.visibility = View.GONE

        databaseEvents = Room.databaseBuilder(
            requireContext(),
            DatabaseEvents::class.java,
            "eventsDB"
        ).build()

        repositoryEvent = EventRepositoryImpl(
            FirestoreEventApiImpl(Firebase.firestore),
            databaseEvents.eventDao()
        )

        eventViewModel = EventViewModel(repositoryEvent)

        val authRepository = AuthRepositoryImpl(
            FirebaseAuthApiImpl()
        )
        sessionViewModel = SessionViewModel(authRepository, requireContext())

        mainToolbar = requireActivity().findViewById(R.id.main_toolbar)

        setObservables()
        lifecycleScope.launch {
            eventViewModel.getEvents(requireContext().dataStore.getPreference("user", ""))
        }
        setListeners()
    }

    private fun setObservables(){
        lifecycleScope.launchWhenStarted {
            eventViewModel.eventState.collectLatest { state ->
                handleState(state)
            }
        }
    }
    private fun handleState(state: EventState) {
        when(state){
            is EventState.Updated -> {
                progressBar.visibility = View.GONE
                eventList = state.events.toMutableList()
                if (eventList.isEmpty()){
                    binding.EmptyTextView.visibility = View.VISIBLE
                }else{
                    binding.EmptyTextView.visibility = View.GONE
                }
                binding.recyclerViewHomeFragment.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewHomeFragment.setHasFixedSize(true)
                binding.recyclerViewHomeFragment.adapter = EventAdapter(eventList, this)
                binding.recyclerViewHomeFragment.adapter?.notifyDataSetChanged()
            }
            is EventState.Error -> {
                progressBar.visibility = View.GONE
                binding.EmptyTextView.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_SHORT).show()
            }
            is EventState.Loading -> {
                progressBar.visibility = View.GONE
                binding.EmptyTextView.visibility = View.GONE
            }
            is EventState.Empty -> {
                progressBar.visibility = View.GONE
                binding.EmptyTextView.visibility = View.GONE
            }
            else -> {
                progressBar.visibility = View.GONE
                binding.EmptyTextView.visibility = View.GONE
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
        mainToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_item_logout -> {
                    sessionViewModel.logout()
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
                    true
                }
                R.id.menu_item_deleteAll -> {
                    lifecycleScope.launch {
                        eventViewModel.deleteAllEvents(requireContext().dataStore.getPreference("user", ""))

                    }
                    true
                }
                else -> false
            }
        }

    }

    override fun onPlaceClicked(event: Event, position: Int) {
        lifecycleScope.launch {
            requireView().findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsEventFragment(event))

        }
    }
}