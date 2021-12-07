package com.gmail.uia059466.liska.setting.feedback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.FeedbackFragmentBinding
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.gmail.uia059466.liska.utils.showKeyboard
import java.util.*

class FeedbackFragment:Fragment()  {

  private var _binding: FeedbackFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
                           ): View {

    _binding = FeedbackFragmentBinding.inflate(inflater, container, false)

    setupAppBar()
    setupOnBackPressed()
    setHasOptionsMenu(true)
    return binding.root
  }

  private fun setupAppBar() {
    val title=getString(R.string.feedback_app_bar_label)
    (activity as MainActivityImpl).renderAppbar(AppBarUiState.ArrayWithTitle(title))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    openKeyboard()
  }

  private fun openKeyboard() {
    binding.feedbackEt.requestFocus()
    showKeyboard()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menu.clear()
    inflater.inflate(R.menu.feedback, menu)
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
        hideKeyboard()
        findNavController().navigateUp()
        return true
      }
      R.id.menu_send_feedback -> {
        checkAndSendFeedBack()
        return true
      }
    }
    return true
  }

  private fun checkAndSendFeedBack() {
    val feedback = binding.feedbackEt.text.toString()
    if (feedback.isNotBlank()) sendEmail(feedback) else showDialogEmptyFeedback()
  }

  private fun sendEmail(body: String?) {
    val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(emailId))
      putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_mail_subject))
      putExtra(Intent.EXTRA_TEXT, body)
    }

    startActivity(createEmailOnlyChooserIntent(
      requireContext(),
      emailIntent,
      getString(R.string.send_feedback_two)
    )
    )
  }
  private fun createEmailOnlyChooserIntent(
    context: Context, source: Intent?,
    chooserTitle: CharSequence?
  ): Intent? {
    val intents = Stack<Intent>()
    val i = Intent(
      Intent.ACTION_SENDTO, Uri.fromParts(
        "mailto",
        "info@domain.com", null
      )
    )
    val activities = context.packageManager
      .queryIntentActivities(i, 0)
    for (ri in activities) {
      val target = Intent(source)
      target.setPackage(ri.activityInfo.packageName)
      intents.add(target)
    }
    return if (!intents.isEmpty()) {
      val chooserIntent = Intent.createChooser(
        intents.removeAt(0),
        chooserTitle
      )
      chooserIntent.putExtra(
        Intent.EXTRA_INITIAL_INTENTS,
        intents.toTypedArray()
      )
      chooserIntent
    } else {
      Intent.createChooser(source, chooserTitle)
    }
  }

  private fun showDialogEmptyFeedback() {
    val dialog = EmptyFeedbackDialog()
    dialog.show(requireActivity().supportFragmentManager, null)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val emailId="rovc71217@protonmail.com"
  }
}