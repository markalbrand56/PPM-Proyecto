package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.uvg.todoba.R
import com.uvg.todoba.data.model.Event
import com.uvg.todoba.data.model.TestDatabase
import com.uvg.todoba.data.util.adapters.EventAdapter
import com.uvg.todoba.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home), EventAdapter.EventListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var eventList: MutableList<Event>
    private var isAllAddVisible : Boolean? = null

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
        isAllAddVisible = false
        binding.addCategoryAction.visibility = View.GONE
        binding.addEventAction.visibility = View.GONE
        binding.addeventActionText.visibility = View.GONE
        binding.addCategoryActionText.visibility = View.GONE
        setListeners()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        eventList = TestDatabase.getEvents()
        binding.recyclerViewHomeFragment.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHomeFragment.setHasFixedSize(true)
        binding.recyclerViewHomeFragment.adapter = EventAdapter(eventList, this)
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
}