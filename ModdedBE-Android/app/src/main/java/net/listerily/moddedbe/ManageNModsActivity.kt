package net.listerily.moddedbe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.endercore.android.EnderCore
import org.endercore.android.nmod.NMod
import org.endercore.android.operator.NModManager
import java.io.FileNotFoundException

class ManageNModsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_nmods)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val recyclerView = findViewById<RecyclerView>(R.id.manageNModsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NModsAdapter(EnderCore.instance.nModManager, this)
    }

    private class NModViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rootView = itemView
    }

    private class NModsAdapter(mgr: NModManager, ctx: Context) : RecyclerView.Adapter<NModViewHolder>() {
        private val allNMods: ArrayList<NMod> = mgr.allNMods
        private val context = ctx
        private val manager = mgr
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NModViewHolder {
            val viewItem = LayoutInflater.from(context).inflate(R.layout.recycler_nmod_item, parent, false)
            return NModViewHolder(viewItem)
        }

        override fun getItemCount(): Int {
            return allNMods.size
        }

        override fun onBindViewHolder(holder: NModViewHolder, position: Int) {
            val view = holder.rootView
            val nmod = allNMods[position]

            val textViewName = view.findViewById<TextView>(R.id.textViewNModName)
            textViewName.text = nmod.name

            val imageViewIcon = view.findViewById<ImageView>(R.id.imageViewNModIcon)
            var icon: Drawable? = null
            if (nmod.packageManifest.icon != null)
                try {
                    icon = Drawable.createFromStream(nmod.openInFiles(nmod.packageManifest.icon), nmod.packageManifest.icon)
                } catch (ignored: FileNotFoundException) {
                }
            if (icon == null)
                imageViewIcon.visibility = View.INVISIBLE
            else {
                imageViewIcon.visibility = View.VISIBLE
                imageViewIcon.setImageDrawable(icon)
            }

            val buttonRemove = view.findViewById<Button>(R.id.buttonRemoveNMod)
            buttonRemove.setOnClickListener {
                run {
                    manager.uninstallNMod(nmod.uuid)
                    notifyItemRemoved(position)
                }
            }

            val switchEnabled = view.findViewById<SwitchCompat>(R.id.switchNModEnabled)
            switchEnabled.isChecked = manager.isNModEnabled(nmod)
            switchEnabled.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
                run {
                    manager.setNModEnabled(nmod, checked)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}