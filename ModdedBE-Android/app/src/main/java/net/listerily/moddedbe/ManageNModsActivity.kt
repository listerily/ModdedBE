package net.listerily.moddedbe

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.endercore.android.exception.nmod.NMod

class ManageNModsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_nmods)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //findViewById<RecyclerView>(R.id.manageNModsRecycler).adapter = NModsAdapter(null)
    }

    private class NModViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

    }

    private class NModsAdapter(list: ArrayList<NMod>) : RecyclerView.Adapter<NModViewHolder>() {

        init {

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NModViewHolder {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: NModViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}