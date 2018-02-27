package com.kanchi.periyava.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kanchi.periyava.MyApplication
import com.kanchi.periyava.R
import com.kanchi.periyava.model.Feature
import com.kanchi.periyava.model.Lineage
import com.kanchi.periyava.model.OnItemClickListener
import com.kanchi.periyava.version.dashboard.activity.DashBoardActivity
import com.kanchi.periyava.version.dashboard.model.GenericAdapter
import kotlinx.android.synthetic.main.dashboard_main.*
import kotlinx.android.synthetic.main.layout_lineage.*
import kotlinx.android.synthetic.main.layout_webcontent.*
import kotlin.reflect.KClass

/**
 * Created by m84098 on 2/13/18.
 */

class LineageActivity : AppCompatActivity(),OnItemClickListener {
    override fun onItemClick(itemView: View, position: Int, data: Any) {

    }

    var TAG: String = LineageActivity::class.java.simpleName

    lateinit var mapping: HashMap<KClass<*>, ArrayList<Int>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_lineage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapping = HashMap<KClass<*>, ArrayList<Int>>()
        mapping.put(Feature::class, arrayListOf<Int>(R.layout.list_item_lineage, BR.lineage))

        MyApplication.refLineage!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var featureListAcharayas: ArrayList<Any> = ArrayList<Any>()
                var iCount: Int = 1
                for (singleSnapshot in dataSnapshot.children) {
                    var lineage = Lineage( if(iCount < 10)  "0"+iCount else ""+iCount,
                    singleSnapshot.child("birth_place").getValue().toString(),
                    singleSnapshot.child("duration").getValue().toString(),
                    singleSnapshot.child("name").getValue().toString(),
                    singleSnapshot.child("samadhi_place").getValue().toString(),
                    singleSnapshot.child("year").getValue().toString())
                    featureListAcharayas.add(lineage)
                    iCount++
                }
                list_lineage.layoutManager = LinearLayoutManager(this@LineageActivity, LinearLayoutManager.VERTICAL, false)
                list_lineage.setItemAnimator(DefaultItemAnimator())
                list_lineage.adapter = GenericAdapter(this@LineageActivity, mapping, featureListAcharayas, this@LineageActivity)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.getItemId()
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
