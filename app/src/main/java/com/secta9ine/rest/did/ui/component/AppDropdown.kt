package com.secta9ine.rest.did.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AppDropdown(
    modifier: Modifier = Modifier,
    selectedItem: String = "",
    itemList: List<String> = emptyList(),
    onSelectItem: (index: Int) -> Unit = {}
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = modifier.padding(horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = true }
                    .background(Color(0xFFE1E1E1))
                    .border(1.dp, Color(0xFFA1A1A1))
                    .padding(start = 6.dp)
            ) {
                Text(
//                modifier = Modifier.width(contentWidth),
                    text = selectedItem,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (expanded) 180f else 360f),
                    tint = Color(0xFFA1A1A1),
                )
            }
            DropdownMenu(
                modifier = Modifier.wrapContentSize(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                itemList.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            onSelectItem(index)
                            expanded = false
                        }
                    ) {
                        Text(text = item)
                    }
                }
            }
        }
    }