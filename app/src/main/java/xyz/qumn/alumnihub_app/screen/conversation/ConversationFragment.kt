/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.qumn.alumnihub_app.screen.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme

//class ConversationFragment : Fragment() {
//
//    private val activityViewModel: MainViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = ComposeView(inflater.context).apply {
//        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
//
//        setContent {
//            Alumnihub_appTheme {
//                ConversationContent(
//                    uiState = exampleUiState,
//                    navigateToProfile = { user ->
//                        // Click callback
//                        val bundle = bundleOf("userId" to user)
//                        findNavController().navigate(
//                            R.id.nav_profile,
//                            bundle
//                        )
//                    },
//                    onNavIconPressed = {
//                        activityViewModel.openDrawer()
//                    }
//                )
//            }
//        }
//    }
//}
