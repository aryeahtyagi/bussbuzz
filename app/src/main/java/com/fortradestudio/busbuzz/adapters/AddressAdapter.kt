package com.fortradestudio.busbuzz.adapters

import android.content.Context
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fortradestudio.busbuzz.R
import com.fortradestudio.busbuzz.room.Locations
import com.fortradestudio.busbuzz.room.LocationsRepo
import java.util.zip.Inflater

class AddressAdapter(
    var list: List<Locations>,
    var context: Context,
    var repo: LocationsRepo,
    val view: View
) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemView = view.findViewById<ConstraintLayout>(R.id.itemView) as ConstraintLayout
        val address = view.findViewById<TextView>(R.id.address)
        val deleteBtn = view.findViewById<AppCompatButton>(R.id.deleteAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.saved_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.address.text = list[position].address

        with(holder) {
            itemView.setOnClickListener {

                if (holder.deleteBtn.visibility == View.GONE) {
                    // navigate
                    val bundle = Bundle().apply {
                        putString(
                            "location",
                            "${list[position].latitude}/${list[position].longitude}"
                        )
                    }
                    Navigation.findNavController(view)
                        .navigate(R.id.action_homeFragment_to_reminderFragment, bundle)
                } else {
                    holder.deleteBtn.visibility = View.GONE
                }
            }
            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    holder.deleteBtn.visibility = View.GONE
                }
            }
            itemView.setOnLongClickListener {
                holder.deleteBtn.visibility = View.VISIBLE
                true
            }
            holder.deleteBtn.setOnClickListener {
                repo.deleteFromDb(list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}