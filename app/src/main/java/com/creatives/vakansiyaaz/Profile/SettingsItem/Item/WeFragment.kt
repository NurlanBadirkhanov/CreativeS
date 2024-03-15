package com.creatives.vakansiyaaz.Profile.SettingsItem.Item

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentWeBinding
import com.creatives.vakansiyaaz.home.popBack

class WeFragment : Fragment() {

    private lateinit var binding: FragmentWeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bExit.setOnClickListener {
                popBack()
            }
            bMyWhatsaap.setOnClickListener {
                openLinkToBrowser("https://www.whatsapp.com/channel/0029VaHGclk9MF90XA4YF30n")
            }

            bInstaVakansiya.setOnClickListener {
                openLinkToBrowser("https://www.instagram.com/vakanciya.az/?hl=ru")
            }
            bInstaKiraye.setOnClickListener {
                openLinkToBrowser("https://www.instagram.com/kiraya.az/?hl=ru")
            }
            bMyTelegDirect.setOnClickListener {
                openLinkToBrowser("https://t.me/CreativeSAdmin")
            }
            bMyTeleg.setOnClickListener {
                openLinkToBrowser("https://t.me/CreativeSStudioS")
            }
            bPlayStoreKiraye.setOnClickListener {
                openLinkToBrowser("https://play.google.com/store/search?q=Kiraye.az&c=apps&hl=az")

            }


        }
    }

    private fun openLinkToBrowser(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }


}