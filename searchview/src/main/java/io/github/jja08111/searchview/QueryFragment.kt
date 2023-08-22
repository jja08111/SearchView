package io.github.jja08111.searchview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.jja08111.searchview.databinding.QueryLayoutBinding

class QueryFragment(
    private val initialQueries: List<String>,
    private val onItemClick: (Int) -> Unit
) : Fragment() {

    private var _binding: QueryLayoutBinding? = null
    private val binding: QueryLayoutBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QueryLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QueryAdapter(onItemClick = onItemClick)
        binding.recyclerView.adapter = adapter
        adapter.submitList(initialQueries)
    }

    fun updateQueries(queries: List<String>) {
        if (_binding == null) {
            return
        }
        val adapter = (binding.recyclerView.adapter as? QueryAdapter)
        adapter?.submitList(queries)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}