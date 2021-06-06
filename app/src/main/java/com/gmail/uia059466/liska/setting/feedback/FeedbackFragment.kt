package com.gmail.uia059466.liska.setting.feedback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.gmail.uia059466.liska.utils.showKeyboard
import java.util.*

const val emailId="rovc71217@protonmail.com"

class FeedbackFragment:Fragment()  {

  private lateinit var feedbackEditText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
                           ): View? {
    
    
    val view = inflater.inflate(
            R.layout.feedback_fragment,
            container,
            false)

    feedbackEditText=view.findViewById(R.id.feedback_et)

    setupAppBar()

    setupOnBackPressed()
    setHasOptionsMenu(true)
    return view
  }
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    openKeyboard()
  }

  private fun openKeyboard() {
    feedbackEditText.requestFocus()
    showKeyboard()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menu.clear()
    inflater.inflate(R.menu.feedback, menu)
  }


  private fun setupAppBar() {
    val mainActivity = activity as MainActivityImpl
    val title=getString(R.string.feedback_app_bar_label)
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
        hideKeyboard()
        goBack()
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
    val feedback= feedbackEditText.text.toString()
    if (feedback.isNotBlank()){
      sendEmail(feedback)
    }else{
      showDialogEmptyFeedback()
    }
  }

  private fun sendEmail(body: String?) {
    val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    emailIntent.type = "text/plain"
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(emailId))
    emailIntent.putExtra(
      Intent.EXTRA_SUBJECT,
      getString(R.string.feedback_mail_subject)
    )
    emailIntent.putExtra(Intent.EXTRA_TEXT, body)
    startActivity(
      createEmailOnlyChooserIntent(
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
    requireActivity().supportFragmentManager.let {
      dialog.show(
        it, EmptyFeedbackDialog.TAG
      )
    }
  }

  private fun goBack() {
    findNavController().navigateUp()
  }
}