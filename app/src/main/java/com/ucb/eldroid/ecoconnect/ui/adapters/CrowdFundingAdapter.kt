package com.ucb.eldroid.ecoconnect.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.Donation

class CrowdfundingAdapter(
    private val context: Context,
    private val projects: List<CrowdfundingProject>
) : BaseAdapter() {

    override fun getCount(): Int = projects.size

    override fun getItem(position: Int): Any = projects[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.crowd_fund_list, parent, false) // Inflate the new CardView layout

        val project = getItem(position) as CrowdfundingProject

        val projectTitle = view.findViewById<TextView>(R.id.projectTitle)
        val createdByTextView = view.findViewById<TextView>(R.id.createdByTextView)
        val projectCreatedBy = view.findViewById<TextView>(R.id.projectCreatedBy)
        val donateBtn = view.findViewById<Button>(R.id.donateBtn)

        // Set data for the project
        projectTitle.text = project.title
        createdByTextView.text = "Created by: "
        projectCreatedBy.text = project.createdBy

        // Donate button click action
        donateBtn.setOnClickListener {
            Toast.makeText(context, "Thank you for donating to ${project.title}!", Toast.LENGTH_SHORT).show()
        }
        donateBtn.setOnClickListener {
            val intent = Intent(context, Donation::class.java)
            context.startActivity(intent)
        }


        return view
    }
}