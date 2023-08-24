package io.github.jja08111.searchview

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.searchView.showQuery) {
                binding.searchView.hideQueries()
                submitFilteredListToAdapter(binding.searchView.query)
            }
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryListener { query ->
            viewModel.updateQuery(query)
            submitFilteredListToAdapter(query)
        }
        binding.searchView.setOnQueryListVisibleChangeListener { visible ->
            onBackPressedCallback.isEnabled = visible
        }
        binding.searchView.setEditTextHint(hint = getString(R.string.search_view_edit_text_hint))
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = UserAdapter().also { it.submitList(users) }
        binding.recyclerView.itemAnimator = null
    }

    private fun submitFilteredListToAdapter(queryToFilter: String) {
        val adapter = binding.recyclerView.adapter as? UserAdapter
        adapter?.submitList(users.filter { it.name.contains(queryToFilter, ignoreCase = true) })
    }
}
