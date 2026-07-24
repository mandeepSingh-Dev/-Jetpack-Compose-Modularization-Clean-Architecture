package com.uae.feature_home.ui.screens

import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.feature_home.remote.model.response.FaqsListResponse
import com.uae.feature_home.ui.viewmodel.FaqsViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.zodiaq.ui.theme.Shape_15


@Composable
fun FaqScreen(faqsViewModel: FaqsViewModel = hiltViewModel(),
              backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {

    val uiState by faqsViewModel.uiState.collectAsStateWithLifecycle()

    BoxCommon(uiState?.isLoading ?: false) {

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderView(title = "Faqs"){
                backStack.removeLastOrNull()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                .weight(1f)) {
                Image(painter = painterResource(R.drawable.faq_2), contentDescription = null, modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(top = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    itemsIndexed(uiState?.faqsList ?: emptyList()){index, item ->
                        FaqCard(item, modifier = Modifier.padding(horizontal = 20.dp) ){
                            faqsViewModel.updateState { state ->
                                val list = state?.faqsList?.toMutableList()
                                list?.set(index,item?.copy(isVisible = !item.isVisible))
                                state?.copy(faqsList = list)
                            }
                        }
                    }
                }

            }
        }
    }

}


@Composable
fun FaqCard(data: FaqsListResponse.FaqsData?, modifier: Modifier = Modifier, onClick : () -> Unit) {


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
            .dropShadow(shadow = androidx.compose.ui.graphics.shadow.Shadow(color = Color.Black.copy(0.1f), radius = 10.dp), shape = Shape_15)
            .clip(Shape_15)
            .clickable{
                onClick()
            }
            .background(color = Color.White, shape = Shape_15)
            .padding(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth())  {
            Text(data?.title ?: "", fontFamily = POPPINS_MEDIUM,
                modifier = Modifier.weight(1f), fontSize = 13.sp)
            Text(if(data?.isVisible ?: false) {"➖"} else {"➕"})
        }

        AnimatedVisibility(data?.isVisible ?: false) {
            Text(data?.description ?: "", fontSize = 13.sp)
        }
    }
}