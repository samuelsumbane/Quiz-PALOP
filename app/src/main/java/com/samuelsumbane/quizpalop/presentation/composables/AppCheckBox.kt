package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor

@Composable
fun AppCheckBox(
    checked: Boolean,
    onCheck: (Boolean) -> Unit
) {
    Checkbox(
        checked = checked,
        onCheckedChange = { value -> onCheck(value) },
        colors = CheckboxDefaults.colors(checkedColor = HomeOptionColor)
    )
}