package com.uvg.todoba.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.uvg.todoba.R
import com.uvg.todoba.data.model.Event
import com.uvg.todoba.data.model.TestDatabase
import com.uvg.todoba.data.util.adapters.EventAdapter
import com.uvg.todoba.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var eventList: MutableList<Event>

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
        TODO("Not yet implemented")
    }
}