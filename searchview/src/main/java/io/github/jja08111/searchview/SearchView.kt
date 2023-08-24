package io.github.jja08111.searchview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
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

    private val _showQuery = AtomicBoolean(false)
    private var queries: List<String> = emptyList()
    private var onQuery: (String) -> Unit = {}
    private var onQueryListVisibleChange: (visible: Boolean) -> Unit = {}

    val showQuery: Boolean get() = _showQuery.get()
    val query: String get() = binding.editText.text?.toString() ?: ""

    init {
        addView(binding.root)
        initRecyclerView()
        initEditText()
        showQueries()
    }

    private fun initRecyclerView() {
        val adapter = QueryAdapter(
            onItemClick = { query ->
                onQuery(query)
                updateSearchText(query)
                hideQueries()
            }
        )
        binding.queryRecyclerView.adapter = adapter
        binding.queryRecyclerView.itemAnimator = null
    }

    private fun initEditText() {
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showQueries()
            }
        }
        binding.editText.addTextChangedListener {
            submitFilteredQueries(queries)
        }
        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onQuery(binding.editText.text.toString())
                hideQueries()
                true
            } else {
                false
            }
        }
    }

    fun setOnQueryListVisibleChangeListener(listener: (visible: Boolean) -> Unit) {
        onQueryListVisibleChange = listener
    }

    fun setQueries(queries: List<String>) {
        this.queries = queries
        submitFilteredQueries(queries)
    }

    private fun submitFilteredQueries(queries: List<String>) {
        val text = binding.editText.text?.toString() ?: ""
        val filteredQueries = if (text.isEmpty()) {
            queries
        } else {
            queries.filter { query -> query.contains(text, ignoreCase = true) }
        }
        val adapter = binding.queryRecyclerView.adapter as? QueryAdapter
        adapter?.submitList(filteredQueries)
    }

    fun setOnQueryListener(onQuery: (String) -> Unit) {
        this.onQuery = onQuery
    }

    fun showQueries() {
        if (_showQuery.getAndSet(true)) {
            return
        }
        onQueryListVisibleChange(true)
        binding.queryRecyclerView.visibility = VISIBLE
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun hideQueries() {
        if (!_showQuery.getAndSet(false)) {
            return
        }
        onQueryListVisibleChange(false)
        clearFocusFromEditText()
        binding.queryRecyclerView.visibility = GONE
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setEditTextHint(hint: String) {
        binding.editText.hint = hint
    }

    private fun updateSearchText(text: String) {
        // TODO: 커서 위치 맨 끝으로 이동하기
        binding.editText.setText(text)
    }

    private fun clearFocusFromEditText() {
        binding.editText.clearFocus()
        val inputMethodManager = context.getSystemService(InputMethodManager::class.java)
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }
}