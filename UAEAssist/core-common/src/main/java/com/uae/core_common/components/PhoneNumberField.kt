package com.uae.core_common.components

import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.grey_1
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.joelkanyi.jcomposecountrycodepicker.annotation.RestrictedApi
import com.joelkanyi.jcomposecountrycodepicker.component.CountrySelectionDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.joelkanyi.jcomposecountrycodepicker.component.CountryCodePicker
import com.zodiaq.ui.theme.Shape_10


@OptIn(RestrictedApi::class)
@Composable
fun PhoneNumberField(
    modifier: Modifier,
    onPhone: (String?) -> Unit,
    state: CountryCodePicker,
    phone: String?,
) {

    var showCountryPicker by rememberSaveable { mutableStateOf(false) }


    if (showCountryPicker) {
        CountrySelectionDialog(
            countryList = state.countryList,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onDismissRequest = { showCountryPicker = false },
            onSelect = { country ->
                state.setCode(country.code)
                showCountryPicker = false
            },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .dropShadow(
                shape = Shape_10,
                shadow = Shadow(radius = 10.dp, color = Color.Black.copy(alpha = 0.1f))
            )
            .background(color = Color.White, shape = Shape_10)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {

        Image(
            painter = painterResource(com.uae.core_common.R.drawable.phone),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .clickable { showCountryPicker = true }
                .padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = state.selectedCountry.code.uppercase(),
                fontFamily = POPPINS_MEDIUM,
                color = Color.Black)
            Text(text = state.selectedCountry.phoneNoCode,
                fontFamily = POPPINS_MEDIUM,
                color = Color.Black)
            Icon(
                Icons.Default.KeyboardArrowDown, contentDescription = null,
                modifier = Modifier.size(13.dp)
            )
        }
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 45.dp)
                .align(Alignment.CenterVertically),
            value = phone ?: "",
            textStyle = TextStyle(color = Color.Black),
            onValueChange = onPhone,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Phone),
            keyboardActions = KeyboardActions(onDone = {

            }),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (phone?.isNullOrEmpty() ?: false || phone == null) {
                        Text(
                            text = "Enter Mobile Number",
                            color = grey_1
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}