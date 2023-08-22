package io.github.jja08111.searchview.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.jja08111.searchview.databinding.FragmentQueryBinding
import io.github.jja08111.searchview.model.users

class UserFragment(private val query: String) : Fragment() {

    private var _binding: FragmentQueryBinding? = null
    private val binding: FragmentQueryBinding get() = requireNotNull(_binding)

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

        binding.recyclerView.adapter = UserAdapter().also { adapter ->
            adapter.submitList(users.filter { user -> user.name.contains(query) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}