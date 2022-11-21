package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseEvents
import com.uvg.todoba.data.remote.firestore.FirestoreEventApiImpl
import com.uvg.todoba.data.repository.event.EventRepositoryImpl
import com.uvg.todoba.databinding.FragmentDetailsEventBinding
import com.uvg.todoba.ui.viewmodels.EventViewModel


class DetailsEventFragment : Fragment(R.layout.fragment_details_event) {
    private lateinit var binding: FragmentDetailsEventBinding
    private val args: DetailsEventFragmentArgs by navArgs()
    private lateinit var eventViewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventDB = Room.databaseBuilder(
            requireContext(),
            DatabaseEvents::class.java,
            "eventsDB"
        ).build()
        val eventRepository = EventRepositoryImpl(
            FirestoreEventApiImpl(Firebase.firestore),
            eventDB.eventDao()
        )

        eventViewModel = EventViewModel(eventRepository)


        setupDetails()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonFragmentDetailsEdit.setOnClickListener{
            requireView().findNavController().navigate(DetailsEventFragmentDirections.actionDetailsEventFragmentToCreateEventFragment(
                args.event
            ))
        }
        binding.buttonFragmentDetailsMarcarCompletado.setOnClickListener{

        }
    }

    private fun setupDetails() {
        binding.textViewNombreEvento.text = args.event.title
        binding.textViewUbicacion.text = args.event.location
        binding.textViewHora.text = args.event.time
        binding.textViewFechaLimite.text = args.event.date
        binding.textViewFechaCreacion.text = "21/10/2022"
        binding.textViewCategoria.text = args.event.category
        binding.textViewComentarios.text = args.event.description
        binding.textViewTiempoHorario.text = "1 hora"
    }


}