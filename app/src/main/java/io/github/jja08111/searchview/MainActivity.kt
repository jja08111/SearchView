package io.github.jja08111.searchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.jja08111.searchview.databinding.ActivityMainBinding
import io.github.jja08111.searchview.model.users
import io.github.jja08111.searchview.user.UserAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    private var _viewModel: MainViewModel? = null
    private val viewModel: MainViewModel get() = requireNotNull(_viewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initSearchView()
        initRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentQueriesState.collectLatest { queries ->
                    binding.searchView.setQueries(queries)
                }
            }
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryListener { query ->
            viewModel.updateQuery(query)
            val adapter = binding.recyclerView.adapter as? UserAdapter
            adapter?.submitList(users.filter { it.name.contains(query, ignoreCase = true) })
        }
        binding.searchView.setEditTextHint(hint = getString(R.string.search_view_edit_text_hint))
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = UserAdapter().also { it.submitList(users) }
        binding.recyclerView.itemAnimator = null
    }
}
