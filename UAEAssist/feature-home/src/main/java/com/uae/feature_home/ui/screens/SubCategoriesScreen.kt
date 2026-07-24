package com.uae.feature_home.ui.screens

import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.clickableDeb
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.toComposeColor
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.utils.AsyncImageWithLoader
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import com.uae.feature_home.ui.viewmodel.SubCategoriesViewModel
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zodiaq.ui.theme.Shape_15


@Composable
fun SubCategoriesScreen(
    categoryId: String,
    categoryName: String,
    subCategoriesList : List<SubCategoriesListResponse.SubCategoryData>?,
    subCategoriesViewModel: SubCategoriesViewModel = hiltViewModel(),
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {
    val uiState by subCategoriesViewModel.uiState.collectAsStateWithLifecycle()
    val subCategoriesListPaging =
        subCategoriesViewModel.subCategoriesPagingData.collectAsLazyPagingItems()


    LaunchedEffect(categoryId) {
        subCategoriesViewModel.getSubCategoriesList(categoryId = categoryId, subCategoriesList)
    }

    LaunchedEffect(subCategoriesListPaging.loadState.refresh) {
        if (subCategoriesListPaging.loadState.refresh is LoadState.NotLoading) {
            subCategoriesViewModel.updateState { it?.copy(isRefreshing = false) }
        }
        if (subCategoriesListPaging.loadState.refresh is LoadState.Error) {
            (subCategoriesListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                subCategoriesViewModel.updateState { it?.copy(isRefreshing = false) }
            }
        }
        if (subCategoriesListPaging.loadState.hasError) {
            (subCategoriesListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }


    BoxCommon(
        isLoading = subCategoriesListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false,
        modifier = Modifier.navigationBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {

            HeaderView(title = categoryName, onBack = {
                backStack.removeLastOrNull()
            })

            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    subCategoriesViewModel.updateState {
                        it?.copy(isRefreshing = true)
                    }
                    subCategoriesListPaging.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .animateContentSize(),
                        contentPadding = PaddingValues(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    )
                    {

                        if (subCategoriesListPaging.itemCount > 0) {
                            items(subCategoriesListPaging.itemCount) { index ->
                                val subCategoryData = subCategoriesListPaging.get(index)

                                SubCategoryCard(
                                    modifier = Modifier.aspectRatio(1f)
                                        .clickableDeb{
                                            subCategoriesViewModel.performCategoryAction(
                                                subCategoryData = subCategoryData,
                                                categoryId = categoryId,
                                                type = subCategoryData?.actionItem?.type
                                            )
                                        },
                                    subCategoryData
                                )
                            }
                            item{
                                bottomPagingLoader(
                                    isVisible = subCategoriesListPaging.loadState.append is LoadState.Loading || subCategoriesListPaging.loadState.append is LoadState.Error,
                                    isRetry = subCategoriesListPaging.loadState.append is LoadState.Error,
                                    onRetry = {
                                        subCategoriesListPaging.retry()
                                    })
                            }


                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }


                }
                if (subCategoriesListPaging.itemCount == 0) {
                    if (subCategoriesListPaging.loadState.refresh is LoadState.NotLoading) {
                        emptyView(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun SubCategoryCard(
    modifier: Modifier, subCategoryData: SubCategoriesListResponse.SubCategoryData?
) {

    Box(modifier = modifier) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = subCategoryData?.bgColor?.toComposeColor() ?: theme_color_1,
                    shape = Shape_15
                )
        ) {
            Image(painter = painterResource(R.drawable.dots), contentDescription = null, modifier = Modifier.matchParentSize())

        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImageWithLoader(image = subCategoryData?.imgSrc, modifier = Modifier.size(40.dp))
//            androidx.compose.foundation.Image(
//                painter = ,
//                contentDescription = null,
//                modifier = Modifier.size(35.dp)
//            )
            Text(
                subCategoryData?.name ?: "",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}