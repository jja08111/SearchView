package io.github.jja08111.searchview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.jja08111.searchview.databinding.FragmentQueryBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class QueryFragment(
    private val onItemClick: (String) -> Unit
) : Fragment() {

    private var _binding: FragmentQueryBinding? = null
    private val binding: FragmentQueryBinding get() = requireNotNull(_binding)

    private val queryStream: MutableSharedFlow<List<String>> = MutableSharedFlow(replay = 1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QueryAdapter(onItemClick = onItemClick)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                queryStream.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    fun trySubmitQueries(queries: List<String>) {
        queryStream.tryEmit(queries)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}