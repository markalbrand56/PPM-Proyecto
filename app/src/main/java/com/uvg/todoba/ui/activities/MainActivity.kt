package com.uvg.todoba.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.uvg.todoba.R

class MainActivity : AppCompatActivity() {
    private lateinit var mainToolbar: MaterialToolbar
    private lateinit var navController: NavController


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

        listenToNavGraphChanges()
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