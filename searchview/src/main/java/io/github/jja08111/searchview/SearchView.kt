package io.github.jja08111.searchview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.jja08111.searchview.databinding.SearchViewLayoutBinding

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding: SearchViewLayoutBinding = SearchViewLayoutBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, false)
    )

    init {
        addView(binding.root)
    }

    // TODO: query 프래그먼트는 사용자 프래그먼트 위에 보이도록하기!
    fun showQueries(queries: List<String>, onItemClick: (String) -> Unit) {
        val fragment = QueryFragment(
            initialQueries = queries,
            onItemClick = { index ->
                val query = queries[index]
                onItemClick(query)
                updateSearchText(query)
            }
        )
        val fragmentManager = requireFragmentManager(context)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun hideQueries(fragment: Fragment) {
        val fragmentManager = requireFragmentManager(context)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun updateSearchText(text: String) {
        // TODO: 커서 위치 맨 끝으로 이동하기
        binding.editText.setText(text)
    }

    private fun requireFragmentManager(context: Context?): FragmentManager {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> requireFragmentManager(context.baseContext)
            else -> error("Cannot get the FragmentManager")
        }
    }
}