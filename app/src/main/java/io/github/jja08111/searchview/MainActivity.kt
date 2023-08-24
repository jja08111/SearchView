package io.github.jja08111.searchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.jja08111.searchview.databinding.ActivityMainBinding
import io.github.jja08111.searchview.model.users
import io.github.jja08111.searchview.user.UserAdapter

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView()
        initRecyclerView()
    }

    private fun initSearchView() {
        binding.searchView.setQueries(listOf("a", "b"))
        binding.searchView.setOnItemClickListener { query ->
            val adapter = binding.recyclerView.adapter as? UserAdapter
            adapter?.submitList(users.filter { it.name.contains(query) })
        }
        binding.searchView.setEditTextHint(hint = getString(R.string.search_view_edit_text_hint))
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = UserAdapter().also { it.submitList(users) }
        binding.recyclerView.itemAnimator = null
    }
}
