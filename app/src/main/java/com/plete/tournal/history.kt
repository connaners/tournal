package com.plete.tournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*

class history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupListOfDataIntoRecyclerView()
    }

    private fun getItemList(): ArrayList<dbModel>{
        val dBHandler: dbHandler = dbHandler(this)
        val DBList: ArrayList<dbModel> = dBHandler.viewDB()
        return DBList
    }

    private fun setupListOfDataIntoRecyclerView(){
        if (getItemList().size > 0){
            rvData.visibility = View.VISIBLE

            rvData.layoutManager = LinearLayoutManager(this)
            val itemAdapter = itemAdapter(this, getItemList())
            rvData.adapter = itemAdapter
        } else {
            rvData.visibility = View.GONE
        }
    }
}