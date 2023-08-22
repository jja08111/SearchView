package io.github.jja08111.searchview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.jja08111.searchview.databinding.ActivityMainBinding
import io.github.jja08111.searchview.user.UserFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setQueries(listOf("a", "b"))
        binding.searchView.setOnItemClickListener { query ->
            // TODO: Fragment를 새로 생성하지 않는 방향으로 수정하기
            binding.searchView.hideQueries(UserFragment(query = query))
        }
        binding.searchView.showQueries()
    }
}
