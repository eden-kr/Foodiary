package com.example.foodiary.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.Adapter.FoodDataAdapter
import com.example.foodiary.Adapter.FoodDataHolder
import com.example.foodiary.R
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.etc.RecyclerDecoration

class FoodDataFragment : Fragment() {
    lateinit var adapter: RecyclerView.Adapter<FoodDataHolder>
    lateinit var recyclerView: RecyclerView

    companion object {
        const val KEY = "list"
        @JvmStatic
        fun newInstance(list: ArrayList<FoodDataPOJO>) = FoodDataFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY, list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
    }

    private val foodList by lazy { requireArguments().getSerializable(KEY) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("myTag", "$foodList")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.memo_fragment, container, false)
        recyclerView = view.findViewById(R.id.memo_frag_recycle)
        adapter = activity?.let { FoodDataAdapter(it, foodList as ArrayList<FoodDataPOJO>) }!!
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(RecyclerDecoration(50))  //간격 조절
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager(requireContext()).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        return view
    }


}
