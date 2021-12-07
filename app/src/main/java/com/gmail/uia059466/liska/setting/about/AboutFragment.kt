package com.gmail.uia059466.liska.setting.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.AboutFragmentBinding
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.BrowserUtils

class AboutFragment : Fragment() {

    private var _binding: AboutFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AboutFragmentBinding.inflate(inflater, container, false)

        binding.websiteRv.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/")
        }

        binding.termsOfUseRv.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/term_of_use/")
        }

        binding.privacyRv.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/privacypolicy/")
        }

        setupAppBar()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun launchBrowserOrShowError(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e:Exception){
            BrowserUtils.showDialogErrorBrowser(requireContext())
        }
    }

    private fun setupAppBar() {
        val title = getString(R.string.setting_app_bar_about)
        (activity as MainActivityImpl).renderAppbar(AppBarUiState.ArrayWithTitle(title))
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}