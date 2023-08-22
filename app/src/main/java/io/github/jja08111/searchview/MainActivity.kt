package io.github.jja08111.searchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.jja08111.searchview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.showQueries(listOf("hi", "wow"), onItemClick = { println(it) })
    }
}
