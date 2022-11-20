package com.uvg.todoba.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.uvg.todoba.R
import com.uvg.todoba.data.local.database.DatabaseEvents
import com.uvg.todoba.data.remote.firestore.FirestoreEventApiImpl
import com.uvg.todoba.data.repository.event.EventRepository
import com.uvg.todoba.data.repository.event.EventRepositoryImpl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainToolbar: MaterialToolbar
    private lateinit var navController: NavController
    private lateinit var eventRepository: EventRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.FragmentContainer_MainActivity
        ) as NavHostFragment

        navController = navHostFragment.navController

        // Para que no se muestre el botón de regresar, tanto Login como Characters deben ser "top-level destinations"
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.welcomeFragment
            )
        )
        mainToolbar = findViewById(R.id.main_toolbar)
        mainToolbar.setupWithNavController(navController, appBarConfiguration)

        val db = Room.databaseBuilder(
            applicationContext,
            DatabaseEvents::class.java
            , "eventsDB"
        ).build()

        eventRepository = EventRepositoryImpl(
            FirestoreEventApiImpl(Firebase.firestore),
            db.eventDao()
        )


        listenToNavGraphChanges()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_logout -> {
                    println("Logout")
                    true
                }
                R.id.menu_item_deleteAll -> {
                    lifecycleScope.launch {
                        println("Delete all")
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun listenToNavGraphChanges() {
        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.welcomeFragment -> {
                    mainToolbar.isVisible = false
                }
                R.id.loginFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = false
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = false
                }
                R.id.createAccountFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = false
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = false
                }
                R.id.homeFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = true
                }
                R.id.detailsEventFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = false
                }
                R.id.createCategoryFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = false
                }
                R.id.createEventFragment -> {
                    mainToolbar.isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_logout).isVisible = true
                    mainToolbar.menu.findItem(R.id.menu_item_deleteAll).isVisible = false
                }

            }
        }
    }
}