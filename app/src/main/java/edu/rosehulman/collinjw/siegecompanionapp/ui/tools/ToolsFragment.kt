package edu.rosehulman.collinjw.siegecompanionapp.ui.tools

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.collinjw.siegecompanionapp.Constants
import edu.rosehulman.collinjw.siegecompanionapp.MainActivity
import edu.rosehulman.collinjw.siegecompanionapp.R
import edu.rosehulman.collinjw.siegecompanionapp.UserDataObject
import kotlinx.android.synthetic.main.fragment_tools.view.*

class ToolsFragment : Fragment() {

    lateinit var root: View
    private var listener: OnToolsListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_tools, container, false)
        root.logout_button.setOnClickListener {
            listener?.onSettingsSelected("Logout")
        }
        root.change_username_button.setOnClickListener {
            listener?.showChangeUsernameDialog()
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnToolsListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnToolsListener {
        // TODO: Update argument type and name
        fun onSettingsSelected(s: String)
        fun showChangeUsernameDialog()
    }
}