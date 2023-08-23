package io.github.jja08111.searchview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import io.github.jja08111.searchview.databinding.SearchViewLayoutBinding
import java.util.concurrent.atomic.AtomicBoolean

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding: SearchViewLayoutBinding = SearchViewLayoutBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, false)
    )

    private val showQuery = AtomicBoolean(false)
    private var queries: List<String> = emptyList()
    private var onItemClick: (String) -> Unit = {}

    init {
        addView(binding.root)
        initQueryFragment()
        initEditText()
        // TODO: Add back button listener for showing queries
    }

    private fun initQueryFragment() {
        val fragment = QueryFragment(
            onItemClick = { query ->
                onItemClick(query)
                updateSearchText(query)
                hideQueries()
            }
        )
        val fragmentManager = requireFragmentManager(context)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        showQueries()
    }

    private fun initEditText() {
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showQueries()
            }
        }
        binding.editText.addTextChangedListener { editable ->
            val text = editable?.toString() ?: ""
            val queries = if (text.isEmpty()) {
                this.queries
            } else {
                this.queries.filter { query -> query.contains(text) }
            }
            submitQueriesToFragment(queries)
        }
    }

    fun setQueries(queries: List<String>) {
        this.queries = queries
        submitQueriesToFragment(queries)
    }

    private fun submitQueriesToFragment(queries: List<String>) {
        val fragment = findQueryFragment()
        fragment?.submitQueries(queries)
    }

    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }

    fun showQueries() {
        if (showQuery.getAndSet(true)) {
            return
        }
        val queryFragment = findQueryFragment()
        queryFragment?.view?.visibility = VISIBLE
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun hideQueries() {
        if (!showQuery.getAndSet(false)) {
            return
        }
        clearFocusFromEditText()
        val queryFragment = findQueryFragment()
        queryFragment?.view?.visibility = GONE
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun updateSearchText(text: String) {
        // TODO: 커서 위치 맨 끝으로 이동하기
        binding.editText.setText(text)
    }

    private fun findQueryFragment(): QueryFragment? {
        val fragmentManager = requireFragmentManager(context)
        return (fragmentManager.findFragmentById(R.id.fragment_container) as? QueryFragment)
    }

    private fun requireFragmentManager(context: Context?): FragmentManager {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> requireFragmentManager(context.baseContext)
            else -> error("Cannot get the FragmentManager")
        }
    }

    private fun clearFocusFromEditText() {
        binding.editText.clearFocus()
        val inputMethodManager = context.getSystemService(InputMethodManager::class.java)
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }
}