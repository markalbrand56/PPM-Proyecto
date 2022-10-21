package com.uvg.todoba.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.uvg.todoba.R
import com.uvg.todoba.databinding.FragmentDetailsEventBinding


class DetailsEventFragment : Fragment(R.layout.fragment_details_event) {
    private lateinit var binding: FragmentDetailsEventBinding
    private val args: DetailsEventFragmentArgs by navArgs()

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
        setupDetails()
    }

    private fun setupDetails() {
        binding.textViewNombreEvento.text = args.nombre
        binding.textViewTituloUbicacion.text = args.ubicacion
        binding.textViewHora.text = args.hora
        binding.textViewFechaLimite.text = args.fecha
        binding.textViewFechaCreacion.text = "21/10/2022"
        binding.textViewCategoria.text = args.categoria
        binding.textViewComentarios.text = args.comentarios
        binding.textViewTiempoHorario.text = "1 hora"
    }


}