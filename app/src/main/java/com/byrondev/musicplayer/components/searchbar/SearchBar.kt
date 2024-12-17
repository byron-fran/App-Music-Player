package com.byrondev.musicplayer.components.searchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.byrondev.musicplayer.ui.theme.Gray20
import com.byrondev.musicplayer.ui.theme.Gray30


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavController) {
    val searchTerm = remember { mutableStateOf<String>("") }
    GridItemSpan(currentLineSpan = 1)
    Row(modifier = Modifier.fillMaxWidth().padding(end = 15.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            tint = Gray30,
            modifier = Modifier.size(30.dp).clickable {
                navController.navigate("HomeScreen")
            },

            )
        OutlinedTextField(
            value =searchTerm.value,
            onValueChange = {searchTerm.value = it },
            singleLine = true,
            modifier = Modifier.background(color= Color.Transparent).fillMaxWidth(),

            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor =  Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = Gray20,
                unfocusedBorderColor = Gray20,
                unfocusedPlaceholderColor = Gray30,
                focusedTextColor =Color.White,
                unfocusedTextColor = Color.White,

            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(

            ),
            placeholder = {
            Row (modifier = Modifier, horizontalArrangement = Arrangement.Center ){
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search music")
                Text("Search By Track, Artist, Album", fontSize = 17.sp)
            }

        })
    }
}
