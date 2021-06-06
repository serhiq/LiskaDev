package com.gmail.uia059466.liska.setting.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.BrowserUtils


class AboutFragment : Fragment() {
    private lateinit var rvWebsite: RelativeLayout
    private lateinit var rvTermsOfUse: RelativeLayout
    private lateinit var rvPrivacy: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.setting_about_content,
            container,
            false
        )

        rvWebsite = view.findViewById(R.id.website_rv)
        rvTermsOfUse = view.findViewById(R.id.terms_of_use_rv)
        rvPrivacy = view.findViewById(R.id.privacy_rv)

        rvWebsite.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/")
        }

        rvTermsOfUse.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/term_of_use/")
        }

        rvPrivacy.setOnClickListener {
            launchBrowserOrShowError("https://serhiq.github.io/liska-site/privacypolicy/")
        }

        setupAppBar()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return view
    }

    private fun launchBrowserOrShowError(url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }catch (e:Exception){
            BrowserUtils.showDialogErrorBrowser(requireContext())
        }
    }

    private fun setupAppBar() {
        val mainActivity = activity as MainActivityImpl
        val title = getString(R.string.setting_app_bar_about)
        mainActivity.renderAppbar(AppBarUiState.ArrayWithTitle(title))
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goBack()
                return true
            }
        }
        return true
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}