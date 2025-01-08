package com.kindustry.market.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.kindustry.market.ui.theme.MarketTheme

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MarketTheme {
//                    LoginPage(
//                        onClickBack = {
//                            findNavController().popBackStack()
//                        },
//                        onLoginSuccess = {
//                            MainActivity.startActivity(requireContext())
//                        }
//                    )
                }
            }
        }
    }
}