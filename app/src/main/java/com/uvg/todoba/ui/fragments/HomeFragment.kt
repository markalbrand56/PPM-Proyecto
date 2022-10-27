package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
    private lateinit var Add: FloatingActionButton
    private lateinit var AddEvent : FloatingActionButton
    private lateinit var AddCategory : FloatingActionButton
    private lateinit var AddEventText : TextView
    private lateinit var AddCategoryText : TextView
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
        Add = view.findViewById(R.id.floatingActionButton_HomeFragment_addEvent)
        AddEvent = view.findViewById(R.id.addevent_action)
        AddCategory = view.findViewById(R.id.addCategory_action)
        AddEventText = view.findViewById(R.id.addevent_action_text)
        AddCategoryText = view.findViewById(R.id.addCategory_action_text)
        isAllAddVisible = false
        AddCategory.visibility = View.GONE
        AddEvent.visibility = View.GONE
        AddCategoryText.visibility = View.GONE
        AddEventText.visibility = View.GONE
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
    //    binding.imageButtonHomeFragmentAddEvent.setOnClickListener{
      //      it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateEventFragment())
        //}
        Add.setOnClickListener(View.OnClickListener{
            (if (!isAllAddVisible!!){
                Add.animate().rotation(90F).setDuration(300)
                AddEvent.show()
                AddCategory.show()
                AddEventText.visibility = View.VISIBLE
                AddCategoryText.visibility = View.VISIBLE
                true
            }else{
                Add.animate().rotation(-90F).setDuration(300)
                AddEvent.hide()
                AddCategory.hide()
                AddEventText.visibility = View.GONE
                AddCategoryText.visibility = View.GONE
                false
            }).also { isAllAddVisible = it }
        })
        binding.addCategoryAction.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateCategoryFragment())

        }

        binding.addeventAction.setOnClickListener{
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