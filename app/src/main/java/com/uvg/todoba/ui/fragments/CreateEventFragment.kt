package com.uvg.todoba.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseCategories
import com.uvg.todoba.data.local.entity.Category
import com.uvg.todoba.data.remote.firestore.FirestoreCategoryApiImpl
import com.uvg.todoba.data.repository.category.CategoryRepository
import com.uvg.todoba.data.repository.category.CategoryRepositoryImpl
import com.uvg.todoba.databinding.FragmentCreateEventBinding

import com.uvg.todoba.data.local.entity.TestDatabase


class CreateEventFragment : Fragment(R.layout.fragment_create_event) {
    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var categoryList: MutableList<Category>
    private lateinit var repositoryCategoryRepository: CategoryRepository
    private lateinit var databaseCategories: DatabaseCategories

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

        repositoryCategoryRepository = CategoryRepositoryImpl(
            FirestoreCategoryApiImpl(Firebase.firestore),
            databaseCategories.categoryDao()
        )
        categoryList = TestDatabase.getCategories()  // TODO: Cambiar por la lista de categorias de la base de datos
        println()
    }

}