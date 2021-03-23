package com.fortradestudio.busbuzz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fortradestudio.busbuzz.adapters.AddressAdapter
import com.fortradestudio.busbuzz.location.LocationRepository
import com.fortradestudio.busbuzz.room.LocationDatabase
import com.fortradestudio.busbuzz.room.Locations
import com.fortradestudio.busbuzz.room.LocationsRepo
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<FloatingActionButton>(R.id.reminderBtn)

        val dao = LocationDatabase.getLocationDatabase(requireContext()).locationDao()

        val repo = LocationsRepo(dao)

        button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = AddressAdapter(listOf(Locations(
            address = "No Saved Locations Found !",
            latitude = 0.0,
            longitude = 0.0)),requireContext(),repo,requireView())

        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapter

        repo.getLocations().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                adapter.list = it
                adapter.notifyDataSetChanged()
            }else{
                adapter.list = arrayListOf(Locations(
                    address = "No Saved Locations Found !",
                    latitude = 0.0,
                    longitude = 0.0))
                adapter.notifyDataSetChanged()
            }
        })

    }

}