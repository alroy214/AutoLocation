package com.alroy214.autolocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alroy214.autolocation.MainActivity.Companion.sneakCurrentLocation
import com.alroy214.autolocation.databinding.FragmentLocationBinding

/**
 * A location [Fragment] subclass as the default destination in the navigation.
 */
class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLocationOn.setOnClickListener {
            sneakLocationShell(view, true)
        }
        binding.buttonLocationOff.setOnClickListener {
            sneakLocationShell(view, false)
        }
        binding.buttonLocationToggle.setOnClickListener {
            if(MainActivity.isLocationEnabled(context)) {
                sneakLocationShell(view, false)
            } else {
                sneakLocationShell(view, true)
            }
        }
    }

    private fun sneakLocationShell(view: View, boolean: Boolean) {
        locationEnable(boolean)
        sneakCurrentLocation(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private fun executeShellCommand(command: String) {
            val process: Process = Runtime.getRuntime()
                .exec(arrayOf("su", "-c", command, "exit"))
            process.waitFor()
        }

        fun locationEnable(boolean: Boolean) {
            if(boolean) {
                executeShellCommand("settings put secure location_mode 3")
            } else {
                executeShellCommand("settings put secure location_mode 0")
            }
        }
    }
}