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
    fun showQueries(queries: List<String>) {
        val fragment = QueryFragment(queries)
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

    private fun requireFragmentManager(context: Context?): FragmentManager {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> requireFragmentManager(context.baseContext)
            else -> error("Cannot get the FragmentManager")
        }
    }
}