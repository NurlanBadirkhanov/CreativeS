package com.creatives.vakansiyaaz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.creatives.vakansiyaaz.ADD.AddCategoryFragment
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.Login.LoginActivity
import com.creatives.vakansiyaaz.R.id
import com.creatives.vakansiyaaz.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: SpinnerViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SpinnerViewModel::class.java]
        navController = findNavController(id.nav_host_fragment_activity_main)
        buttons()
        menuItem()
    }

    private fun buttons() {
        binding.floatingActionButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null) {
                Toast.makeText(this, "Вы не зарегистрированы", Toast.LENGTH_SHORT).show()
                showDialogIfNotRegistered(this)
            } else {
                val bottomSheetFragment = AddCategoryFragment()
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }

        }

    }

    private fun showDialogIfNotRegistered(context: Context) {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Вы не зарегистрированы")
                .setMessage("Хотите создать аккаунт?")
                .setPositiveButton("Да") { dialog, which ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Нет") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun menuItem() {
        binding.bottomAppBar.selectedItemId = id.action_home
        binding.bottomAppBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                id.action_home -> {
                    navController.navigate(id.homeFragment)
                    true
                }
                id.action_profile -> {
                    val auth = FirebaseAuth.getInstance()
                    if (auth.currentUser == null) {
                        Toast.makeText(this, "Вы не зарегистрированы", Toast.LENGTH_SHORT).show()
                        showDialogIfNotRegistered(this)
                        false
                    } else {
                        navController.navigate(id.profileFragment)
                        true
                    }

                }
                id.action_search -> {
                    navController.navigate(id.searchFragment)
                    true
                }
                id.action_favorite -> {
                    navController.navigate(id.favoriteFragment)
                    true
                }
                else -> {
                    navController.navigate(id.action_home)
                    true
                }
            }
        }
    }


}